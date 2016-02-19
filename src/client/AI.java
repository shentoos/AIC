package client;

import java.util.ArrayList;
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
        // fill this method, we've presented a stupid AI for example!
    	
    	  Node[] myNodes = world.getMyNodes();
          for (Node source : myNodes) {
              // get neighbours
              Node[] neighbours = source.getNeighbours();
              if (neighbours.length > 0) {
                  // select a random neighbour
                  //Node destination = neighbours[(int) (neighbours.length * Math.random())];
                  //world.moveArmy(source, destination, source.getArmyCount()/2);
                  // move half of the node's army to the neighbor node
                  //MyMain(source,world);
                  
              }
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
    public int calcScore(Node node, World world){
    	int owner = node.getOwner();
    	if(owner == -1)
    		return caclFreeCellScore(node, world);
    	else if(owner == world.getMyID()){
    		return caclOurCellScore(node, world);
    	}
    	else{
    		return calcOppCellScore(node, world);
    	}
    }
	private int calcOppCellScore(Node node, World world) {
		// TODO Auto-generated method stub
		return 0;
	}
	private int caclOurCellScore(Node node, World world) {
		// TODO Auto-generated method stub
		return 0;
	}
	private int caclFreeCellScore(Node node, World world) {
		int deg = node.getNeighbours().length;
		
		return 0;
	}

}

class NodeInfo{
	HashMap<Node, Double> adjScores = new HashMap<>();
	Double ownScore;
	Double finalScore;
}

class Info{
	int type;
	int score;
	Node node;
}