Problem URL : https://www.codechef.com/problems/KGP16J

Please try solving this problem yourself before jumping into the solution given.

Hint: Consider nodes with positive outdegree(indegree - outdegree >0) as connected to source and nodes with negative outdegree as connected to destination. Then use Bellman-ford algorithm to find shortest path from source to all the edges and then take shortest path to destination and reduce flow to the sorce. 

For further clerification follow this editorial :https://discuss.codechef.com/t/kgp16j-editorial/18259

Exmple accepted solution path : DataStructureSolutions/algorithms/graphTheory/mincutmaxflow/problems/KGP16J/MinCutMaxFlowCodechefKGP16JProblem.java
 
