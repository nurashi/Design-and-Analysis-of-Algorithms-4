package com.smartcity;

import com.smartcity.common.Graph;
import com.smartcity.common.GraphLoader;
import com.smartcity.graph.scc.TarjanSCC;
import com.smartcity.graph.topo.KahnTopologicalSort;
import com.smartcity.graph.dagsp.DAGShortestPath;

import java.io.IOException;
import java.util.List;

// Main class for running all graph algorithms and generating performance reports
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
        
        System.out.println("\n==========================================");
        System.out.println("All datasets processed successfully");
    }
    
    // Process a single dataset and output results
    private static void processDataset(String filename) throws IOException {
        System.out.println("\n==========================================");
        System.out.println("Processing: " + filename);
        System.out.println("==========================================");
        
        GraphLoader.GraphData data = GraphLoader.loadFromFile(filename);
        Graph graph = data.graph;
        int source = data.source;
        
        System.out.println("Graph Statistics:");
        System.out.println("  Vertices: " + graph.getVertices());
        System.out.println("  Directed: " + graph.isDirected());
        System.out.println("  Source: " + source);
        System.out.println("  Weight Model: " + data.weightModel);
        
        System.out.println("\n--- Strongly Connected Components (Tarjan) ---");
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult sccResult = tarjan.findSCCs();
        
        System.out.println("Number of SCCs: " + sccResult.getComponents().size());
        System.out.println("SCC Sizes: ");
        for (int i = 0; i < sccResult.getComponents().size(); i++) {
            List<Integer> component = sccResult.getComponents().get(i);
            System.out.println("  SCC " + i + ": " + component.size() + " vertices " + component);
        }
        System.out.println("Operations: " + sccResult.getMetrics().getOperations());
        System.out.println("Time: " + String.format("%.3f", sccResult.getMetrics().getElapsedMillis()) + " ms");
        
        Graph condensation = tarjan.buildCondensationGraph(sccResult);
        System.out.println("Condensation Graph: " + condensation.getVertices() + " vertices");
        
        System.out.println("\n--- Topological Sort (Kahn) ---");
        KahnTopologicalSort kahn = new KahnTopologicalSort(condensation);
        KahnTopologicalSort.TopoResult topoResult = kahn.topologicalSort();
        
        System.out.println("Is DAG: " + topoResult.isDAG());
        System.out.println("Topological Order: " + topoResult.getOrder());
        System.out.println("Operations: " + topoResult.getMetrics().getOperations());
        System.out.println("Time: " + String.format("%.3f", topoResult.getMetrics().getElapsedMillis()) + " ms");
        
        if (topoResult.isDAG()) {
            System.out.println("\n--- DAG Shortest Paths ---");
            DAGShortestPath dagsp = new DAGShortestPath(condensation);
            
            int condensedSource = 0;
            DAGShortestPath.PathResult shortestResult = dagsp.shortestPaths(condensedSource);
            
            System.out.println("Shortest distances from condensed node " + condensedSource + ":");
            int[] distances = shortestResult.getDistances();
            for (int i = 0; i < distances.length; i++) {
                if (distances[i] != Integer.MAX_VALUE) {
                    System.out.println("  To node " + i + ": " + distances[i]);
                    List<Integer> path = shortestResult.reconstructPath(i);
                    System.out.println("    Path: " + path);
                }
            }
            System.out.println("Operations: " + shortestResult.getMetrics().getOperations());
            System.out.println("Time: " + String.format("%.3f", shortestResult.getMetrics().getElapsedMillis()) + " ms");
            
            System.out.println("\n--- DAG Longest Paths ---");
            DAGShortestPath.PathResult longestResult = dagsp.longestPaths(condensedSource);
            
            System.out.println("Longest distances from condensed node " + condensedSource + ":");
            int[] longDistances = longestResult.getDistances();
            for (int i = 0; i < longDistances.length; i++) {
                if (longDistances[i] != Integer.MIN_VALUE) {
                    System.out.println("  To node " + i + ": " + longDistances[i]);
                }
            }
            System.out.println("Operations: " + longestResult.getMetrics().getOperations());
            System.out.println("Time: " + String.format("%.3f", longestResult.getMetrics().getElapsedMillis()) + " ms");
            
            System.out.println("\n--- Critical Path ---");
            DAGShortestPath.CriticalPathResult criticalPath = dagsp.findCriticalPath();
            
            System.out.println("Critical Path Length: " + criticalPath.getLength());
            System.out.println("Critical Path: " + criticalPath.getPath());
            System.out.println("From: " + criticalPath.getSource() + " To: " + criticalPath.getDestination());
        }
    }
}
