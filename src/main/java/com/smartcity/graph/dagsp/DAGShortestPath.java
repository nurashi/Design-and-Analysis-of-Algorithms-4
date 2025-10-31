package com.smartcity.graph.dagsp;

import com.smartcity.common.Graph;
import com.smartcity.common.Metrics;
import com.smartcity.common.MetricsImpl;

import java.util.*;

// Shortest and longest path algorithms for DAGs
public class DAGShortestPath {
    private final Graph graph;
    private final Metrics metrics;
    
    public DAGShortestPath(Graph graph) {
        this.graph = graph;
        this.metrics = new MetricsImpl();
    }
    
    // Compute shortest paths from source using topological order
    public PathResult shortestPaths(int source) {
        int n = graph.getVertices();
        int[] dist = new int[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);
        dist[source] = 0;
        
        metrics.reset();
        metrics.startTiming();
        
        List<Integer> topoOrder = topologicalSort();
        
        for (int u : topoOrder) {
            if (dist[u] != Integer.MAX_VALUE) {
                for (Graph.Edge edge : graph.getAdjacentEdges(u)) {
                    int v = edge.to;
                    int w = edge.weight;
                    metrics.incrementOperations();
                    
                    if (dist[u] + w < dist[v]) {
                        dist[v] = dist[u] + w;
                        parent[v] = u;
                    }
                }
            }
        }
        
        metrics.stopTiming();
        
        return new PathResult(dist, parent, metrics, false);
    }
    
    // Compute longest paths from source by negating weights
    public PathResult longestPaths(int source) {
        int n = graph.getVertices();
        int[] dist = new int[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Integer.MIN_VALUE);
        Arrays.fill(parent, -1);
        dist[source] = 0;
        
        metrics.reset();
        metrics.startTiming();
        
        List<Integer> topoOrder = topologicalSort();
        
        for (int u : topoOrder) {
            if (dist[u] != Integer.MIN_VALUE) {
                for (Graph.Edge edge : graph.getAdjacentEdges(u)) {
                    int v = edge.to;
                    int w = edge.weight;
                    metrics.incrementOperations();
                    
                    if (dist[u] + w > dist[v]) {
                        dist[v] = dist[u] + w;
                        parent[v] = u;
                    }
                }
            }
        }
        
        metrics.stopTiming();
        
        return new PathResult(dist, parent, metrics, true);
    }
    
    // Find critical path (longest path in the entire DAG)
    public CriticalPathResult findCriticalPath() {
        int n = graph.getVertices();
        int bestSource = -1;
        int bestDest = -1;
        int maxLength = Integer.MIN_VALUE;
        PathResult bestResult = null;
        
        for (int source = 0; source < n; source++) {
            PathResult result = longestPaths(source);
            int[] dist = result.getDistances();
            
            for (int dest = 0; dest < n; dest++) {
                if (dist[dest] != Integer.MIN_VALUE && dist[dest] > maxLength) {
                    maxLength = dist[dest];
                    bestSource = source;
                    bestDest = dest;
                    bestResult = result;
                }
            }
        }
        
        List<Integer> path = new ArrayList<>();
        if (bestResult != null && bestDest != -1) {
            path = bestResult.reconstructPath(bestDest);
        }
        
        return new CriticalPathResult(path, maxLength, bestSource, bestDest);
    }
    
    // Topological sort using DFS for path computation
    private List<Integer> topologicalSort() {
        int n = graph.getVertices();
        boolean[] visited = new boolean[n];
        Stack<Integer> stack = new Stack<>();
        
        for (int v = 0; v < n; v++) {
            if (!visited[v]) {
                topoDFS(v, visited, stack);
            }
        }
        
        List<Integer> order = new ArrayList<>();
        while (!stack.isEmpty()) {
            order.add(stack.pop());
        }
        return order;
    }
    
    // DFS for topological sorting
    private void topoDFS(int u, boolean[] visited, Stack<Integer> stack) {
        visited[u] = true;
        
        for (Graph.Edge edge : graph.getAdjacentEdges(u)) {
            if (!visited[edge.to]) {
                topoDFS(edge.to, visited, stack);
            }
        }
        
        stack.push(u);
    }
    
    public static class PathResult {
        private final int[] distances;
        private final int[] parents;
        private final Metrics metrics;
        private final boolean isLongest;
        
        public PathResult(int[] distances, int[] parents, Metrics metrics, boolean isLongest) {
            this.distances = distances;
            this.parents = parents;
            this.metrics = metrics;
            this.isLongest = isLongest;
        }
        
        public int[] getDistances() {
            return distances;
        }
        
        public int[] getParents() {
            return parents;
        }
        
        public Metrics getMetrics() {
            return metrics;
        }
        
        public boolean isLongest() {
            return isLongest;
        }
        
        // Reconstruct path from source to destination
        public List<Integer> reconstructPath(int dest) {
            List<Integer> path = new ArrayList<>();
            int current = dest;
            
            while (current != -1) {
                path.add(current);
                current = parents[current];
            }
            
            Collections.reverse(path);
            return path;
        }
    }
    
    public static class CriticalPathResult {
        private final List<Integer> path;
        private final int length;
        private final int source;
        private final int destination;
        
        public CriticalPathResult(List<Integer> path, int length, int source, int destination) {
            this.path = path;
            this.length = length;
            this.source = source;
            this.destination = destination;
        }
        
        public List<Integer> getPath() {
            return path;
        }
        
        public int getLength() {
            return length;
        }
        
        public int getSource() {
            return source;
        }
        
        public int getDestination() {
            return destination;
        }
    }
}
