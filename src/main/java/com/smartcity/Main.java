package com.smartcity;

import com.smartcity.common.Graph;
import com.smartcity.common.GraphLoader;
import com.smartcity.graph.scc.TarjanSCC;
import com.smartcity.graph.scc.CondensationGraph;
import com.smartcity.graph.topo.KahnTopologicalSort;
import com.smartcity.graph.topo.ComponentTopologicalSort;
import com.smartcity.graph.dagsp.DAGShortestPath;

import java.io.IOException;
import java.util.List;

/**
 * Main class for Smart City scheduling algorithms.
 * Runs SCC detection, topological sorting, and DAG shortest paths.
 */
public class Main {
    
    public static void main(String[] args) {
        System.out.println("Smart City Scheduling - Graph Algorithms");
        System.out.println("==========================================\n");
        
        String[] datasets = {
            "data/small_cyclic_1.json",
            "data/small_dag_1.json",
            "data/small_multi_scc.json",
            "data/medium_cyclic_1.json",
            "data/medium_dag_1.json",
            "data/medium_dense_scc.json",
            "data/large_cyclic_sparse.json",
            "data/large_dag_dense.json",
            "data/large_multi_scc.json"
        };
        
        for (String dataset : datasets) {
            try {
                processDataset(dataset);
            } catch (IOException e) {
                System.err.println("Error processing " + dataset + ": " + e.getMessage());
            }
        }
        
        System.out.println("All datasets processed successfully");
    }
    
    // Process a single dataset and output results
    private static void processDataset(String filename) throws IOException {
        System.out.println("Processing: " + filename);
        
        GraphLoader.GraphData data = GraphLoader.loadFromFile(filename);
        Graph graph = data.graph;
        int source = data.source;
        
        System.out.println("Graph Statistics:");
        System.out.println("  Vertices: " + graph.getVertices());
        System.out.println("  Directed: " + graph.isDirected());
        System.out.println("  Source: " + source);
        System.out.println("  Weight Model: " + data.weightModel + " (edge weights represent task duration in hours)");
        
        System.out.println("\n--- Strongly Connected Components (Tarjan) ---");
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult sccResult = tarjan.findSCCs();
        
        System.out.println("Number of SCCs: " + sccResult.getComponents().size());
        System.out.println("Components:");
        for (int i = 0; i < sccResult.getComponents().size(); i++) {
            List<Integer> component = sccResult.getComponents().get(i);
            System.out.println("  Component " + i + ": " + component + " (size: " + component.size() + ")");
        }
        System.out.println("Metrics:");
        System.out.println("  DFS Visits: " + sccResult.getMetrics().getOperations());
        System.out.println("  Time: " + String.format("%.3f", sccResult.getMetrics().getElapsedMillis()) + " ms");
        
        System.out.println("\nCondensation Graph");
        CondensationGraph condensationGraph = new CondensationGraph(graph, sccResult.getComponents());
        Graph condensation = condensationGraph.getCondensationGraph();
        System.out.println("Vertices (Components): " + condensation.getVertices());
        System.out.println("Structure:");
        for (int i = 0; i < condensation.getVertices(); i++) {
            List<Graph.Edge> edges = condensation.getAdjacentEdges(i);
            if (!edges.isEmpty()) {
                List<Integer> targets = new java.util.ArrayList<>();
                for (Graph.Edge e : edges) {
                    targets.add(e.to);
                }
                System.out.println("  SCC" + i + " -> " + targets);
            }
        }
        System.out.println("Is DAG: " + condensationGraph.isDAG());
        
        System.out.println("\nTopological Sort (Component Level)");
        ComponentTopologicalSort componentTopo = new ComponentTopologicalSort(condensationGraph);
        ComponentTopologicalSort.ComponentTopoResult componentTopoResult = componentTopo.topologicalSort();
        
        System.out.println("Is DAG: " + componentTopoResult.isDAG());
        System.out.println("Component Order: " + componentTopoResult.getComponentOrder());
        System.out.println("Task Order (derived): " + componentTopoResult.getTaskOrder());
        System.out.println("Metrics:");
        System.out.println("  Operations (pushes/pops): " + componentTopoResult.getMetrics().getOperations());
        System.out.println("  Time: " + String.format("%.3f", componentTopoResult.getMetrics().getElapsedMillis()) + " ms");
        
        if (componentTopoResult.isDAG()) {
            System.out.println("\nDAG Shortest Paths");
            DAGShortestPath dagsp = new DAGShortestPath(condensation);
            
            int condensedSource = 0;
            DAGShortestPath.PathResult shortestResult = dagsp.shortestPaths(condensedSource);
            
            System.out.println("Shortest distances from component " + condensedSource + ":");
            System.out.println(String.format("  %-12s %-12s %-20s", "Destination", "Distance", "Path"));
            System.out.println("  " + "-".repeat(50));
            int[] distances = shortestResult.getDistances();
            for (int i = 0; i < distances.length; i++) {
                if (distances[i] != Integer.MAX_VALUE) {
                    List<Integer> path = shortestResult.reconstructPath(i);
                    System.out.println(String.format("  %-12d %-12d %-20s", i, distances[i], path));
                }
            }
            System.out.println("Metrics:");
            System.out.println("  Relaxations: " + shortestResult.getMetrics().getOperations());
            System.out.println("  Time: " + String.format("%.3f", shortestResult.getMetrics().getElapsedMillis()) + " ms");
            
            System.out.println("\nDAG Longest Paths");
            DAGShortestPath.PathResult longestResult = dagsp.longestPaths(condensedSource);
            
            System.out.println("Longest distances from component " + condensedSource + ":");
            int[] longDistances = longestResult.getDistances();
            for (int i = 0; i < longDistances.length; i++) {
                if (longDistances[i] != Integer.MIN_VALUE) {
                    List<Integer> path = longestResult.reconstructPath(i);
                    System.out.println("  To component " + i + ": " + longDistances[i] + " | Path: " + path);
                }
            }
            System.out.println("Metrics:");
            System.out.println("  Relaxations: " + longestResult.getMetrics().getOperations());
            System.out.println("  Time: " + String.format("%.3f", longestResult.getMetrics().getElapsedMillis()) + " ms");
            
            System.out.println("\n--- Critical Path ---");
            DAGShortestPath.CriticalPathResult criticalPath = dagsp.findCriticalPath();
            
            System.out.println("Critical Path: " + criticalPath.getPath() + " with length " + criticalPath.getLength());
            System.out.println("From component " + criticalPath.getSource() + " to component " + criticalPath.getDestination());
        }
    }
}
