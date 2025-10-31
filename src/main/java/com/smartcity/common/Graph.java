package com.smartcity.common;

import java.util.*;

// Directed graph representation using adjacency lists
public class Graph {
    private final int vertices;
    private final List<List<Edge>> adjList;
    private final boolean directed;
    private String weightModel;

    public Graph(int vertices, boolean directed) {
        this.vertices = vertices;
        this.directed = directed;
        this.adjList = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adjList.add(new ArrayList<>());
        }
        this.weightModel = "edge";
    }

    public void addEdge(int u, int v, int weight) {
        adjList.get(u).add(new Edge(v, weight));
        if (!directed) {
            adjList.get(v).add(new Edge(u, weight));
        }
    }

    public int getVertices() {
        return vertices;
    }

    public List<Edge> getAdjacentEdges(int vertex) {
        return adjList.get(vertex);
    }

    public boolean isDirected() {
        return directed;
    }

    public String getWeightModel() {
        return weightModel;
    }

    public void setWeightModel(String weightModel) {
        this.weightModel = weightModel;
    }

    // Get reverse graph for Kosaraju's algorithm
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

    public static class Edge {
        public final int to;
        public final int weight;

        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
}
