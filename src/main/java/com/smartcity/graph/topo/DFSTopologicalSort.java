package com.smartcity.graph.topo;

import com.smartcity.common.Graph;
import com.smartcity.common.Metrics;
import com.smartcity.common.MetricsImpl;

import java.util.*;

// DFS-based topological sorting algorithm
public class DFSTopologicalSort {
    private final Graph graph;
    private final Metrics metrics;
    
    public DFSTopologicalSort(Graph graph) {
        this.graph = graph;
        this.metrics = new MetricsImpl();
    }
    
    // Compute topological order using DFS
    public KahnTopologicalSort.TopoResult topologicalSort() {
        int n = graph.getVertices();
        boolean[] visited = new boolean[n];
        Stack<Integer> stack = new Stack<>();
        
        metrics.reset();
        metrics.startTiming();
        
        for (int v = 0; v < n; v++) {
            if (!visited[v]) {
                dfs(v, visited, stack);
            }
        }
        
        metrics.stopTiming();
        
        List<Integer> order = new ArrayList<>();
        while (!stack.isEmpty()) {
            order.add(stack.pop());
        }
        
        boolean isDAG = (order.size() == n);
        
        return new KahnTopologicalSort.TopoResult(order, isDAG, metrics);
    }
    
    // DFS traversal
    private void dfs(int u, boolean[] visited, Stack<Integer> stack) {
        visited[u] = true;
        metrics.incrementOperations();
        
        for (Graph.Edge edge : graph.getAdjacentEdges(u)) {
            int v = edge.to;
            metrics.incrementOperations();
            if (!visited[v]) {
                dfs(v, visited, stack);
            }
        }
        
        stack.push(u);
    }
}
