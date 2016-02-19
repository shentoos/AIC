package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import client.model.Node;

/**
 * AI class.
 * You should fill body of the method {@link #doTurn}.
 * Do not change name or modifiers of the methods or fields
 * and do not add constructor for this class.
 * You can add as many methods or fields as you want!
 * Use world parameter to access and modify game's
 * world!
 * See World interface for more details.
 */
public class AI {
	ArrayList<NodeInfo> nodesInfo = new ArrayList<>();
	private double DISFACTOR = 2;
	private double RFACTORtoAmount = 0.1;
	private double RFACTORtoChoose = 0.1;
	private double INF = 1000 * 1000 * 1000.0;
	private int EPS = 5;
	private double EPSPROB = 0.5;
	
    public void doTurn(World world) {
	  init(world);    	   	
	  Node[] myNodes = world.getMyNodes();
	  for (Node source : myNodes) {
	      // get neighbours
		  sendingArmy(world, source);
	  }
	  for (Node source : myNodes) {
	      // get neighbours
		  finalScoring(source);
	  }
    }
    public void CompScores(World world){
    	Node[] totNodes = getNodes(world);
		for(int i=0; i<totNodes.length; i++){
    		calcScore(totNodes[i], world);
    	}
	}
    public static <T> T[] concat(T[] first, T[] second) {
    	  T[] result = Arrays.copyOf(first, first.length + second.length);
    	  System.arraycopy(second, 0, result, first.length, second.length);
    	  return result;
    }
    public Node[] getNodes(World world){
    	Node worldOccupiedNodes[] = concat(world.getMyNodes(),world.getOpponentNodes());
    	Node worldNodes[] = concat(worldOccupiedNodes, world.getFreeNodes());
    	return worldNodes;
    }
    public void init(World world) {
		Node worldNodes[] = getNodes(world);
		for(int i=0; i<worldNodes.length; i++){
			NodeInfo nInfo = new NodeInfo();
			Node neis[] = worldNodes[i].getNeighbours();
			for(int j=0; i<neis.length; j++){
				nInfo.adjScores.put(neis[j], 0.0);
				nInfo.neededArmy.put(neis[j], 0);
			}
			nInfo.finalScore = 0.0;
			nInfo.ownScore = 0.0;
			nodesInfo.add(nInfo);
		}
	}

	public void  finalScoring ( Node node ){
    	nodesInfo.get(node.getIndex()).finalScore = new Double(nodesInfo.get(node.getIndex()).ownScore);
    	for ( Node neg : node.getNeighbours() )
    		if ( nodesInfo.get(node.getIndex()).adjScores.get(neg) > 0 )
    			nodesInfo.get(node.getIndex()).finalScore += nodesInfo.get(node.getIndex()).adjScores.get(neg) / DISFACTOR;
    }
    
    public Node getBestChoiceNeighbour( Node node ) {
    	Node bestNeg = node;
    	Double price = new Double(-INF);
    	for ( Node neg : node.getNeighbours() )
    		if ( price < nodesInfo.get(neg.getIndex()).finalScore ) {
    			bestNeg = neg;
    			price = nodesInfo.get(neg.getIndex()).finalScore;
    		}
    	return bestNeg;
    }
    public int getMoveAmount ( Node v, Node u ) {
    	int send = 0, amountNow = v.getArmyCount();
    	if ( amountNow == 0 )
    		return 0;
    	if ( Math.random() < RFACTORtoAmount ){
    		send = Math.max((int) ( amountNow * Math.random()) , 1); 
    	} else {
    		send = Math.min( nodesInfo.get(v.getIndex()).neededArmy.get(u), Math.max(0, amountNow-2) );
    		amountNow -= send;
    		if ( amountNow - 10 >= EPS && Math.random() <= EPSPROB ){
    			send += amountNow - 10;
    			amountNow = 10;
    		}
    		if ( amountNow - 30 >= EPS && Math.random() <= EPSPROB ){
    			send += amountNow - 30;
    			amountNow = 30;
    		}
    	}
    	
    	return send;
    	
    }
    public void sendingArmy ( World world, Node node ) {
    	Node bestNeg = getBestChoiceNeighbour( node );
    	if ( Math.random() < RFACTORtoChoose && node.getNeighbours().length > 0 )
    		bestNeg = node.getNeighbours()[(int) ( Math.random() * node.getNeighbours().length )] ;
    	
    	if ( nodesInfo.get(node.getIndex()).finalScore > nodesInfo.get(bestNeg.getIndex()).finalScore )
    		return;
    	if ( bestNeg == node )
    		return;
    	
    	world.moveArmy(node, bestNeg, getMoveAmount(node, bestNeg));
    }
    
    public void calcScore(Node node, World world){
    	int owner = node.getOwner();
    	int scr = 0;
		Node neis[] = node.getNeighbours();
		int deg = neis.length;
		int opptotNeis = 0;
		int oppWeakNeis = 0;
		int oppMediumNeis = 0;
		int oppStrongNeis = 0;
		int oppArmies = 0;
		int ourNeis = 0;
		int freeNeis = 0;
		int neededArmy = 0;
		for(int i=0; i<neis.length; i++){
			if(neis[i].getOwner()==world.getMyID()){
				ourNeis++;
			}
			else if(neis[i].getOwner()==(1-world.getMyID())){
				opptotNeis++;
				if(neis[i].getArmyCount()<10)
					oppWeakNeis++;
				else if(neis[i].getArmyCount()<30)
					oppMediumNeis++;
				else
					oppStrongNeis++;
				oppArmies += neis[i].getArmyCount();
			}
			else
				freeNeis++;
		}
    	if(owner == -1){
    		scr = deg*10-oppWeakNeis-2*oppMediumNeis-3*oppStrongNeis+ourNeis;
    		neededArmy = (deg+2)/(ourNeis);
    	}
    	else if(owner == world.getMyID()){
    		scr = deg*10+2*oppWeakNeis+4*oppMediumNeis+6*oppStrongNeis-ourNeis;
    		neededArmy = (int)(1.5*(oppArmies - node.getArmyCount())/ourNeis);
    	}
    	else{
    		scr = deg*5+oppWeakNeis*2-oppMediumNeis-2*oppStrongNeis+5*ourNeis;;
    		neededArmy = neededArmy = (int)(3*(oppArmies - node.getArmyCount())/ourNeis);
    	}
    	nodesInfo.get(node.getIndex()).ownNeededArmy = neededArmy;
    	nodesInfo.get(node.getIndex()).ownScore = (double) scr;
    }
}

class NodeInfo{
	HashMap<Node, Double> adjScores = new HashMap<>();
	HashMap<Node, Integer> neededArmy = new HashMap<>();
	Integer ownNeededArmy;
	Double ownScore;
	Double finalScore;
}

class Info{
	int type;
	int score;
	Node node;
}