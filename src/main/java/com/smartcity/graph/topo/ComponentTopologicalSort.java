package com.smartcity.graph.topo;

import com.smartcity.common.Graph;
import com.smartcity.common.Metrics;
import com.smartcity.common.MetricsImpl;
import com.smartcity.graph.scc.CondensationGraph;

import java.util.*;

/**
 * Topological sorting for condensation graphs.
 * Provides both component ordering and derived task ordering.
 */
public class ComponentTopologicalSort {
    private final CondensationGraph condensationGraph;
    private final Metrics metrics;
    
    /**
     * Create topological sorter for condensation graph.
     * @param condensationGraph The condensation graph to sort
     */
    public ComponentTopologicalSort(CondensationGraph condensationGraph) {
        this.condensationGraph = condensationGraph;
        this.metrics = new MetricsImpl();
    }
    
    /**
     * Compute topological order using Kahn's algorithm.
     * @return Result with component order and task order
     */
    public ComponentTopoResult topologicalSort() {
        Graph graph = condensationGraph.getCondensationGraph();
        int n = graph.getVertices();
        int[] inDegree = new int[n];
        
        metrics.reset();
        metrics.startTiming();
        
        // Calculate in-degrees
        for (int u = 0; u < n; u++) {
            for (Graph.Edge edge : graph.getAdjacentEdges(u)) {
                inDegree[edge.to]++;
                metrics.incrementOperations();
            }
        }
        
        // Find nodes with no incoming edges
        Queue<Integer> queue = new LinkedList<>();
        for (int v = 0; v < n; v++) {
            if (inDegree[v] == 0) {
                queue.offer(v);
                metrics.incrementOperations();
            }
        }
        
        List<Integer> componentOrder = new ArrayList<>();
        
        // Process nodes in topological order
        while (!queue.isEmpty()) {
            int u = queue.poll();
            componentOrder.add(u);
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
        
        // Derive task ordering from component order
        List<Integer> taskOrder = new ArrayList<>();
        for (int componentId : componentOrder) {
            List<Integer> vertices = condensationGraph.getVerticesInComponent(componentId);
            taskOrder.addAll(vertices);
        }
        
        metrics.stopTiming();
        
        boolean isDAG = (componentOrder.size() == n);
        
        return new ComponentTopoResult(componentOrder, taskOrder, isDAG, metrics);
    }
    
    /**
     * Result containing both component and task orderings.
     */
    public static class ComponentTopoResult {
        private final List<Integer> componentOrder;
        private final List<Integer> taskOrder;
        private final boolean isDAG;
        private final Metrics metrics;
        
        /**
         * Create result.
         * @param componentOrder Topological order of components
         * @param taskOrder Derived order of original tasks
         * @param isDAG Whether graph is a valid DAG
         * @param metrics Performance metrics
         */
        public ComponentTopoResult(List<Integer> componentOrder, List<Integer> taskOrder, 
                                   boolean isDAG, Metrics metrics) {
            this.componentOrder = componentOrder;
            this.taskOrder = taskOrder;
            this.isDAG = isDAG;
            this.metrics = metrics;
        }
        
        /**
         * Get component ordering.
         * @return List of component IDs in topological order
         */
        public List<Integer> getComponentOrder() {
            return componentOrder;
        }
        
        /**
         * Get task ordering.
         * @return List of original task IDs in valid order
         */
        public List<Integer> getTaskOrder() {
            return taskOrder;
        }
        
        /**
         * Check if graph is a DAG.
         * @return true if valid DAG
         */
        public boolean isDAG() {
            return isDAG;
        }
        
        /**
         * Get performance metrics.
         * @return Metrics object
         */
        public Metrics getMetrics() {
            return metrics;
        }
    }
}
