
package DataStructureSolutions.algorithms.graphTheory.minCut.problems.hackerEarth.baahubali-vs-bhallaladeva;

import java.io.*;
import java.util.*;
/**
 *
 * @author Priyank Patel
 */
public class MinCutHackerEarthBahuballiProblem {
 
    public static void main(String args[]) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        String cityCapitalDetails[] = bufferedReader.readLine().split(" ");
        int numberOfCity = Integer.parseInt(cityCapitalDetails[0]);
        int bahuballiCapital = cityCapitalDetails[1].charAt(0) -'A';
        int bhalladevaCapital = cityCapitalDetails[2].charAt(0) -'A';
        
        Map<Integer,Node> graph = new HashMap<Integer,Node>();
        
        for(int index=0; index<numberOfCity; index++){
            String cityConnectivityDetails[] = bufferedReader.readLine().split(" ");
            
            int source = cityConnectivityDetails[0].charAt(0) - 'A';
            int destination = cityConnectivityDetails[1].charAt(0) - 'A';
            int cost = Integer.parseInt(cityConnectivityDetails[2]);
            getNode(graph,source).addEdge(new Edge(source, destination, cost));
            getNode(graph,destination).addEdge(new Edge(destination, source, cost));
        }
     System.out.println(String.valueOf(minCut(graph,bahuballiCapital,bhalladevaCapital)));
       
    }
    
    public static int minCut(Map<Integer,Node> graph, int sourceVertex, int destinationVertex){
       int finalMaxLoss = 0;
       Queue<Node> queue = new LinkedList<Node>();
       int parent[] = new int[26];
       boolean visitedNodeFlag[] = new boolean[26];
       
      while(true){
       queue.add(graph.get(sourceVertex));
       
       for(int index= 0 ; index<= 'Z'-'A' ;index++){
           visitedNodeFlag[index]=false;
       }
       
       visitedNodeFlag[sourceVertex] = true;
        
       while(!queue.isEmpty()){
           Node queueHead = queue.poll();
           for(Edge edge : queueHead.getCorrespondingEdges()){
               if(!visitedNodeFlag[edge.getDestination()] && edge.getCost() > 0){
                   visitedNodeFlag[edge.getDestination()]= true;
                   parent[edge.getDestination()] = edge.getSource();
                   queue.add(graph.get(edge.getDestination()));
               }
           }
       }
       
       if(visitedNodeFlag[destinationVertex]){
            
           int currentMinimumCutValue = Integer.MAX_VALUE;
            for(int index = destinationVertex; index != sourceVertex; index = parent[index]){
                Edge requiredEdge = null;
                for(Edge edge: graph.get(index).getCorrespondingEdges()){
                    if(edge.getDestination()==parent[index]){
                        requiredEdge = edge;
                        break;
                    }
                }
               currentMinimumCutValue = Math.min(currentMinimumCutValue, requiredEdge.getCost());
            }
            
             for(int index = destinationVertex; index != sourceVertex; index = parent[index]){
               
                for(Edge edge: graph.get(index).getCorrespondingEdges()){
                    if(edge.getDestination()==parent[index]){
                        edge.reduceCost(currentMinimumCutValue);
                        break;
                    }
                }
                
                for(Edge edge: graph.get(parent[index]).getCorrespondingEdges()){
                    if(edge.getDestination()==index){
                        edge.reduceCost(currentMinimumCutValue);
                        break;
                    }
                }
            }
             finalMaxLoss += currentMinimumCutValue;
       } else{
           break;
       }
    }
       return finalMaxLoss;
    }
    
    public static Node getNode(Map<Integer,Node> graph, int nodeId){
        if(graph.get(nodeId) == null){
            graph.put(nodeId, new Node(nodeId));
        }
        return graph.get(nodeId);
    }
    
    public static class Node{
        private int nodeId;
        private Set<Edge> correspondingEdges;
        
        public Node(int nodeId){
            this.nodeId = nodeId;
            correspondingEdges = new HashSet<Edge>();
        }
        
        public int getNodeId(){
            return nodeId;
        }
        
        public void addEdge(Edge edge){
            this.correspondingEdges.add(edge);
        }
                
        public Set<Edge> getCorrespondingEdges(){
            return correspondingEdges;
        }
        
        @Override
        public boolean equals(Object objectToCompare){
            if(objectToCompare instanceof Node){
                return this.getNodeId() == ((Node)objectToCompare).getNodeId();
            }
            
            return false;
        }
    }
    
    public static class Edge{
        private int source;
        private int destination;
        private int cost;
        
        public Edge(int source, int destination, int cost){
            this.source = source;
            this.destination = destination;
            this.cost = cost;
        }
        
        public int getSource(){
            return source;
        }
        
        public int getDestination(){
            return destination;
        }
        
        public int getCost(){
            return cost;
        }
        
        public void reduceCost(int usedFlowValue){
            cost-=usedFlowValue;
        }
        
        @Override
        public boolean equals(Object objectToCompare){
            if(objectToCompare instanceof Edge){
                return this.getSource() == ((Edge)objectToCompare).getSource() && this.getDestination() == ((Edge)objectToCompare).getDestination();
            }
            return false;
        }
    }
    
}

