package com.smartcity.common;

import java.util.*;

/**
 * Directed graph representation using adjacency lists.
 * Supports weighted edges and operations for graph algorithms.
 * 
 * Complexity: O(V) space for adjacency lists, O(1) edge addition
 */
public class Graph {
    private final int vertices;
    private final List<List<Edge>> adjList;
    private final boolean directed;
    private String weightModel;

    /**
     * Create a new graph.
     * @param vertices Number of vertices in the graph
     * @param directed Whether the graph is directed
     */
    public Graph(int vertices, boolean directed) {
        this.vertices = vertices;
        this.directed = directed;
        this.adjList = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
        }
        this.weightModel = "edge";
    }

    /**
     * Add a weighted edge to the graph.
     * @param u Source vertex
     * @param v Destination vertex
     * @param weight Edge weight (task duration in hours)
     */
    public void addEdge(int u, int v, int weight) {
        adjList.get(u).add(new Edge(v, weight));
        if (!directed) {
            adjList.get(v).add(new Edge(u, weight));
        }
    }

    /**
     * Get number of vertices.
     * @return Vertex count
     */
    public int getVertices() {
        return vertices;
    }

    /**
     * Get all edges from a vertex.
     * @param vertex Source vertex
     * @return List of outgoing edges
     */
    public List<Edge> getAdjacentEdges(int vertex) {
        return adjList.get(vertex);
    }

    /**
     * Check if graph is directed.
     * @return true if directed
     */
    public boolean isDirected() {
        return directed;
    }

    /**
     * Get weight model.
     * @return Weight model string
     */
    public String getWeightModel() {
        return weightModel;
    }

    /**
     * Set weight model.
     * @param weightModel Weight model description
     */
    public void setWeightModel(String weightModel) {
        this.weightModel = weightModel;
    }

    /**
     * Get reverse graph for Kosaraju's algorithm.
     * @return Graph with all edges reversed
     * @complexity O(V+E) time and space
     */
    public Graph getReverse() {
        Graph reverse = new Graph(vertices, directed);
        reverse.setWeightModel(this.weightModel);
        for (int u = 0; u < vertices; u++) {
            for (Edge edge : adjList.get(u)) {
                reverse.addEdge(edge.to, u, edge.weight);
            }
        }
        return reverse;
    }

    /**
     * Weighted edge in the graph.
     */
    public static class Edge {
        public final int to;
        public final int weight;

        /**
         * Create an edge.
         * @param to Destination vertex
         * @param weight Edge weight
         */
        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
}
