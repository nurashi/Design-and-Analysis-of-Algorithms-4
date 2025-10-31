package com.smartcity.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.FileReader;
import java.io.IOException;

// Utility class for loading graphs from JSON files
public class GraphLoader {
    
    // Load graph from JSON file
    public static GraphData loadFromFile(String filename) throws IOException {
        Gson gson = new Gson();
        JsonObject json = gson.fromJson(new FileReader(filename), JsonObject.class);
        
        boolean directed = json.get("directed").getAsBoolean();
        int n = json.get("n").getAsInt();
        Graph graph = new Graph(n, directed);
        
        String weightModel = json.has("weight_model") ? 
            json.get("weight_model").getAsString() : "edge";
        graph.setWeightModel(weightModel);
        
        JsonArray edges = json.getAsJsonArray("edges");
        for (JsonElement element : edges) {
            JsonObject edge = element.getAsJsonObject();
            int u = edge.get("u").getAsInt();
            int v = edge.get("v").getAsInt();
            int w = edge.get("w").getAsInt();
            graph.addEdge(u, v, w);
        }
        
        int source = json.has("source") ? json.get("source").getAsInt() : 0;
        
        return new GraphData(graph, source, weightModel);
    }
    
    public static class GraphData {
        public final Graph graph;
        public final int source;
        public final String weightModel;
        
        public GraphData(Graph graph, int source, String weightModel) {
            this.graph = graph;
            this.source = source;
            this.weightModel = weightModel;
        }
    }
}
