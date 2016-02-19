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
    public void sendingArmy ( Node node ) {
    	   
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