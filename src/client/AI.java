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
	int N=1000;
	ArrayList<NodeInfo> nodesInfo = new ArrayList<>();
	private void MyMain(Node nd, World world){
		Node[] neighbours = nd.getNeighbours();
		ArrayList<Node> good = new ArrayList<>();
		int not_my_nodes = 0;
        for(int i=0; i<neighbours.length; i++){
        	if(neighbours[i].getOwner()!=world.getMyID()){
        		not_my_nodes++;
        		good.add(neighbours[i]);
        	}
        	
        }
        for(int j=0; j<Math.min(nd.getArmyCount()/2,good.size()); j++){
        	world.moveArmy(nd, good.get(j), Math.max(2,nd.getArmyCount()/good.size()));
        }
	}
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

}

class NodeInfo{
	HashMap<Node, Integer> score = new HashMap<>();
}

class Info{
	int type;
	int score;
	Node node;
}