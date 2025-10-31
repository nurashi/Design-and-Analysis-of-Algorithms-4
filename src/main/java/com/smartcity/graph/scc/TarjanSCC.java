package com.smartcity.graph.scc;

import com.smartcity.common.Graph;
import com.smartcity.common.Metrics;
import com.smartcity.common.MetricsImpl;

import java.util.*;

// Tarjan's algorithm for finding strongly connected components in O(V+E) time
public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics;
    
    private int time;
    private int[] disc;
    private int[] low;
    private boolean[] onStack;
    private Stack<Integer> stack;
    private List<List<Integer>> sccs;
    
    public TarjanSCC(Graph graph) {
        this.graph = graph;
        this.metrics = new MetricsImpl();
    }
    
    // Find all strongly connected components
    public SCCResult findSCCs() {
        int n = graph.getVertices();
        disc = new int[n];
        low = new int[n];
        onStack = new boolean[n];
        stack = new Stack<>();
        sccs = new ArrayList<>();
        
        Arrays.fill(disc, -1);
        Arrays.fill(low, -1);
        
        time = 0;
        
        metrics.reset();
        metrics.startTiming();
        
        for (int v = 0; v < n; v++) {
            if (disc[v] == -1) {
                tarjanDFS(v);
            }
        }
        
        metrics.stopTiming();
        
        return new SCCResult(sccs, metrics);
    }
    
    // DFS traversal for Tarjan's algorithm
    private void tarjanDFS(int u) {
        disc[u] = low[u] = time++;
        stack.push(u);
        onStack[u] = true;
        metrics.incrementOperations();
        
        for (Graph.Edge edge : graph.getAdjacentEdges(u)) {
            int v = edge.to;
            metrics.incrementOperations();
            
            if (disc[v] == -1) {
                tarjanDFS(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (onStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
        }
        
        if (low[u] == disc[u]) {
            List<Integer> scc = new ArrayList<>();
            int v;
            do {
                v = stack.pop();
                onStack[v] = false;
                scc.add(v);
                metrics.incrementOperations();
            } while (v != u);
            
            Collections.sort(scc);
            sccs.add(scc);
        }
    }
    
    // Build condensation graph (DAG of SCCs)
    public Graph buildCondensationGraph(SCCResult sccResult) {
        List<List<Integer>> sccs = sccResult.getComponents();
        int numSCCs = sccs.size();
        
        int[] vertexToSCC = new int[graph.getVertices()];
        for (int i = 0; i < numSCCs; i++) {
            for (int v : sccs.get(i)) {
                vertexToSCC[v] = i;
            }
        }
        
        Graph condensation = new Graph(numSCCs, true);
        Set<String> addedEdges = new HashSet<>();
        
        for (int u = 0; u < graph.getVertices(); u++) {
            int sccU = vertexToSCC[u];
            for (Graph.Edge edge : graph.getAdjacentEdges(u)) {
                int v = edge.to;
                int sccV = vertexToSCC[v];
                
                if (sccU != sccV) {
                    String edgeKey = sccU + "->" + sccV;
                    if (!addedEdges.contains(edgeKey)) {
                        condensation.addEdge(sccU, sccV, edge.weight);
                        addedEdges.add(edgeKey);
                    }
                }
            }
        }
        
        return condensation;
    }
    
    public static class SCCResult {
        private final List<List<Integer>> components;
        private final Metrics metrics;
        
        public SCCResult(List<List<Integer>> components, Metrics metrics) {
            this.components = components;
            this.metrics = metrics;
        }
        
        public List<List<Integer>> getComponents() {
            return components;
        }
        
        public int[] getSizes() {
            int[] sizes = new int[components.size()];
            for (int i = 0; i < components.size(); i++) {
                sizes[i] = components.get(i).size();
            }
            return sizes;
        }
        
        public Metrics getMetrics() {
            return metrics;
        }
    }
}
