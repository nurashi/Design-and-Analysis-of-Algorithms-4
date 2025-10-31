package com.smartcity.graph.topo;

import com.smartcity.common.Graph;
import com.smartcity.common.Metrics;
import com.smartcity.common.MetricsImpl;

import java.util.*;

// Kahn's algorithm for topological sorting using BFS and in-degree
public class KahnTopologicalSort {
    private final Graph graph;
    private final Metrics metrics;
    
    public KahnTopologicalSort(Graph graph) {
        this.graph = graph;
        this.metrics = new MetricsImpl();
    }
    
    // Compute topological order using Kahn's algorithm
    public TopoResult topologicalSort() {
        int n = graph.getVertices();
        int[] inDegree = new int[n];
        
        metrics.reset();
        metrics.startTiming();
        
        for (int u = 0; u < n; u++) {
            for (Graph.Edge edge : graph.getAdjacentEdges(u)) {
                inDegree[edge.to]++;
                metrics.incrementOperations();
            }
        }
        
        Queue<Integer> queue = new LinkedList<>();
        for (int v = 0; v < n; v++) {
            if (inDegree[v] == 0) {
                queue.offer(v);
                metrics.incrementOperations();
            }
        }
        
        List<Integer> order = new ArrayList<>();
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);
            metrics.incrementOperations();
            
            for (Graph.Edge edge : graph.getAdjacentEdges(u)) {
                int v = edge.to;
                inDegree[v]--;
                metrics.incrementOperations();
                
                if (inDegree[v] == 0) {
                    queue.offer(v);
                }
            }
        }
        
        metrics.stopTiming();
        
        boolean isDAG = (order.size() == n);
        
        return new TopoResult(order, isDAG, metrics);
    }
    
    public static class TopoResult {
        private final List<Integer> order;
        private final boolean isDAG;
        private final Metrics metrics;
        
        public TopoResult(List<Integer> order, boolean isDAG, Metrics metrics) {
            this.order = order;
            this.isDAG = isDAG;
            this.metrics = metrics;
        }
        
        public List<Integer> getOrder() {
            return order;
        }
        
        public boolean isDAG() {
            return isDAG;
        }
        
        public Metrics getMetrics() {
            return metrics;
        }
    }
}
