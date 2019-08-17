
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataStructureSolutions.algorithms.graphtheory.mincutmaxflow.problems.KGP16J;

import java.io.*;
import java.util.*;

/**
 * solution to problem : https://www.codechef.com/problems/KGP16J/
 * @author Priyank Patel
 */
public class MinCutMaxFlowCodechefKGP16JProblem {
    
    public static void main(String args[]) throws IOException{
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        int numberOfTestCases = Integer.parseInt(bufferedReader.readLine());
        
        for(int index=0; index < numberOfTestCases; index++){
            String[] cityRouteCount= bufferedReader.readLine().split(" ");
            int numberOfCity = Integer.parseInt(cityRouteCount[0]);
            int numberOfVitalNodes = Integer.parseInt(cityRouteCount[1]);
            int numberOfNonVitalNodes = Integer.parseInt(cityRouteCount[2]);
            
            // store total degree of node at given point
            int degreeOfNodes[] = new int[numberOfCity];
            
            //Graph information, that store node and edges information.. that is city and route info.
            // here we are taking index 0 as source and index numberOfCity+1 as destination;
            Map<Integer, Node> graph = new HashMap<Integer, Node>();
            for(int cityIndex=0; cityIndex <= numberOfCity+1; cityIndex++){
                graph.put(cityIndex, new Node(cityIndex));
            }
                    
            for(int vitalNodeIndex=0; vitalNodeIndex < numberOfVitalNodes; vitalNodeIndex++){
                 //reading source destination information for each vital route of kingdom
                String sourceDestinationInfo[] = bufferedReader.readLine().split(" ");
                int source = Integer.parseInt(sourceDestinationInfo[0]);
                int destination = Integer.parseInt(sourceDestinationInfo[1]);
                
                // increase degree by one for out degree
                degreeOfNodes[source-1] += 1;
                // decrease degree by one for in degree
                degreeOfNodes[destination-1] -= 1;
            }
            
            for(int nonVitalNodeIndex=0; nonVitalNodeIndex < numberOfNonVitalNodes; nonVitalNodeIndex++){
                //reading source destination information for each non-vital route of kingdom
                 //we need to store info of these edges
             
                String sourceDestinationInfo[] = bufferedReader.readLine().split(" ");
                int source = Integer.parseInt(sourceDestinationInfo[0]);
                int destination = Integer.parseInt(sourceDestinationInfo[1]);
                
                // increase degree by one for out degree
                degreeOfNodes[source-1] += 1;
                // decrease degree by one for in degree
                degreeOfNodes[destination-1] -= 1;
                
                // store edge information
                //edge has cost 1, which indicate if we remove this edge then we increase our answer by one.
                //that is.. one edge is cut from graph.
                Edge edge = new Edge(source, destination,1, 1);
                Edge reverseEdge = new Edge(destination, source,0, -1);
                
                //get the number of edges at source and destination node before adding any edge to graph.
                // that will help us to add reverseEdge index.                
                edge.setReverseEdgeIndex(graph.get(destination).getEdgesFromNode().size());
                reverseEdge.setReverseEdgeIndex(graph.get(source).getEdgesFromNode().size());
                
                graph.get(source).addEdge(edge);
                graph.get(destination).addEdge(reverseEdge);
                
            }
            
            //for each positive degree add node from source
            // sameway for each negative degree add node from destination
            int totalOutDegree = 0;
            for(int cityIndex=0; cityIndex < numberOfCity; cityIndex++){
                if(degreeOfNodes[cityIndex] > 0){
                    
                //remember if solution is possible then the min cut value will be equal to totalOutDegree value
                // else solution is not possible
                 totalOutDegree+=degreeOfNodes[cityIndex];
                    
                // for each postive degree add edge from source, which is 0
                // cityIndex are 0 based index so actual value will be cityIndex+1
                Edge edge = new Edge(0, cityIndex+1 , degreeOfNodes[cityIndex], 0);
                Edge reverseEdge = new Edge(cityIndex+1, 0, 0, 0);
                
                edge.setReverseEdgeIndex(graph.get(cityIndex+1).getEdgesFromNode().size());
                reverseEdge.setReverseEdgeIndex(graph.get(0).getEdgesFromNode().size());
                
                graph.get(0).addEdge(edge);
                graph.get(cityIndex+1).addEdge(reverseEdge);
                    
                }else if(degreeOfNodes[cityIndex] < 0){
                    
                //for each negative edgree add edge to destination , which is numberOfCity + 1

                Edge edge = new Edge(cityIndex+1, numberOfCity+1 , -degreeOfNodes[cityIndex], 0);
                Edge reverseEdge = new Edge(numberOfCity+1, cityIndex+1, 0, 0);
                
                edge.setReverseEdgeIndex(graph.get(numberOfCity+1).getEdgesFromNode().size());
                reverseEdge.setReverseEdgeIndex(graph.get(cityIndex+1).getEdgesFromNode().size());
                
                graph.get(cityIndex+1).addEdge(edge);
                graph.get(numberOfCity+1).addEdge(reverseEdge);                    
                }
            }
             System.out.println(calculateMinCutMaxFlowValue(graph,numberOfCity,totalOutDegree));
        }
    }

    private static int calculateMinCutMaxFlowValue(Map<Integer, Node> graph,int numberOfCity, int totalOutDegree) {
        int totalNodesOfGraph = numberOfCity + 2;
        int parentInfo[] = new int[totalNodesOfGraph];
        int reversedgeIndexInfo[] = new int[totalNodesOfGraph];
        int distanceOfNodeFromSource[] = new int[totalNodesOfGraph];
        int minimuCutValue=0;
        Queue<Node> queue = new LinkedList<Node>();
        // instead of below flags we can use queue.contains(object); method as well
        boolean[] nodePresentInQueueFlag = new boolean[totalNodesOfGraph];
        
       while(true){
        
        distanceOfNodeFromSource[0] = 0;
        queue.add(graph.get(0));
        nodePresentInQueueFlag[0] = true;
        for(int index=1; index < totalNodesOfGraph; index++){
            //marking each node as not reachable initially
            distanceOfNodeFromSource[index] = Integer.MAX_VALUE;
            nodePresentInQueueFlag[index] = false;
        }
        
        while(!queue.isEmpty()){
            
            Node headOfQueue = queue.poll();
            nodePresentInQueueFlag[headOfQueue.getNodeId()] = false;
            
            for(Edge edge : headOfQueue.getEdgesFromNode()){
                
                //if flow possible then only process the edge
                // if flow and capcity are same means pipe is at maximum capacity and no more flow can flow through it
                if(edge.getCapacity() > edge.getFlow()
                        && distanceOfNodeFromSource[edge.getDestination()] > (distanceOfNodeFromSource[edge.getSource()] + edge.getCost())){
                 distanceOfNodeFromSource[edge.getDestination()] = distanceOfNodeFromSource[edge.getSource()] + edge.getCost();
                 
                 parentInfo[edge.getDestination()] = edge.getSource();
                 reversedgeIndexInfo[edge.getDestination()] = edge.getReverseEdgeIndex();
                 
                 //if destination node is not present in queue then add it
                 if(!nodePresentInQueueFlag[edge.getDestination()]){
                     queue.add(graph.get(edge.getDestination()));
                     nodePresentInQueueFlag[edge.getDestination()] = true;
                 }
                    
                }                
            }
        }
        
        //  check if destination is reachable
        if(distanceOfNodeFromSource[totalNodesOfGraph-1] != Integer.MAX_VALUE) {
            int minimumFlow = Integer.MAX_VALUE;
            for(int nodeIndex= totalNodesOfGraph-1; nodeIndex!=0; nodeIndex= parentInfo[nodeIndex]){
               //we can change below logic :)
               // no need of reversedgeIndexInfo; we can store currentEdgeinfo in the list
               Edge reverseEdge =  graph.get(nodeIndex).getEdgesFromNode().get(reversedgeIndexInfo[nodeIndex]);
               Edge edge = graph.get(parentInfo[nodeIndex]).getEdgesFromNode().get(reverseEdge.getReverseEdgeIndex());
                minimumFlow = Math.min(minimumFlow, edge.getCapacity());
            }
            //update the flow for the given residual path
            for(int nodeIndex= totalNodesOfGraph-1; nodeIndex!=0; nodeIndex= parentInfo[nodeIndex]){
               
               Edge reverseEdge =  graph.get(nodeIndex).getEdgesFromNode().get(reversedgeIndexInfo[nodeIndex]);
               Edge edge = graph.get(parentInfo[nodeIndex]).getEdgesFromNode().get(reverseEdge.getReverseEdgeIndex());
               edge.updateFlow(minimumFlow);
               // add same value neative flow for reverse edge
               reverseEdge.updateFlow(-minimumFlow);
               
               //updating minimum cut value
               // cost value is 0 for edges connecting source and destination
               // cost value is 1 for actual edges of graph and -1 for reverse edges
               minimuCutValue += minimumFlow * edge.getCost();
            }
        }
        else{
            if(minimuCutValue!=totalOutDegree){
                return -1;
            }
            break;
        }
    }
        
        return minimuCutValue; 
    }
    
    public static class Node{
        private int nodeId;
        private List<Edge> edgesFromNode;
        
        public Node(int nodeId){
            this.nodeId = nodeId;
            edgesFromNode = new LinkedList<Edge>();
        }
        
        public int getNodeId(){
            return this.nodeId;
        }
        
        public List<Edge> getEdgesFromNode(){
            return this.edgesFromNode;
        }
        
        public void addEdge(Edge edge){
            this.edgesFromNode.add(edge);
        }
        
        @Override
        public boolean equals(Object objectToCompare){
            if(objectToCompare instanceof Node){
                return this.getNodeId()== ((Node)objectToCompare).getNodeId();
            }
            return false;
        }
        
    }
    
    public static class Edge {
        private int source;
        private int destination;
        private int cost;
        private int capacity;
        private int flow;
        // it is not necessary to store reverse Edge index, we can traverse whole list at particular node to find reverseEdge.
        // However, having this while generating graph will speed up ssearching reverseEdge from Node.
        private int reverseEdgeIndex;
        
        public Edge(int source,int destination,int capacity, int cost){
            this.source = source;
            this.destination = destination;
            this.cost = cost;
            this.capacity = capacity;
            this.flow = 0;
        }
        
        public int getSource(){
            return this.source;
        }
        
        public int getDestination(){
            return this.destination;
        }
                
        public int getCost(){
            return this.cost;
        }
        
        public int getFlow(){
            return this.flow;
        }
        
        public int getReverseEdgeIndex(){
            return this.reverseEdgeIndex;
        }
        
        public int getCapacity(){
           return this.capacity; 
        }
        
        public void updateFlow(int updatedFlowValue){
            this.flow += updatedFlowValue;
        }
        
        public void setReverseEdgeIndex(int reverseEdgeIndex){
            this.reverseEdgeIndex = reverseEdgeIndex;
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

