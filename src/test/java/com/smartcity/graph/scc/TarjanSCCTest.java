package com.smartcity.graph.scc;

import com.smartcity.common.Graph;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class TarjanSCCTest {
    
    // Test simple cycle detection
    @Test
    public void testSimpleCycle() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        assertEquals(1, result.getComponents().size());
        assertEquals(3, result.getComponents().get(0).size());
    }
    
    // Test multiple SCCs
    @Test
    public void testMultipleSCCs() {
        Graph graph = new Graph(6, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 5, 1);
        graph.addEdge(5, 3, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        assertEquals(2, result.getComponents().size());
    }
    
    // Test DAG (no cycles)
    @Test
    public void testDAG() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        assertEquals(4, result.getComponents().size());
    }
    
    // Test condensation graph
    @Test
    public void testCondensationGraph() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        Graph condensation = tarjan.buildCondensationGraph(result);
        
        assertTrue(condensation.getVertices() <= 3);
    }
    
    // Test single vertex graph
    @Test
    public void testSingleVertex() {
        Graph graph = new Graph(1, true);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        assertEquals(1, result.getComponents().size());
        assertEquals(1, result.getComponents().get(0).size());
    }
    
    // Test graph with self-loops
    @Test
    public void testSelfLoops() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 0, 1);
        graph.addEdge(1, 1, 1);
        graph.addEdge(2, 2, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        assertEquals(3, result.getComponents().size());
    }
    
    // Test disconnected components
    @Test
    public void testDisconnectedGraph() {
        Graph graph = new Graph(6, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 0, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 3, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        assertTrue(result.getComponents().size() >= 2);
    }
    
    // Test graph where all vertices form one SCC
    @Test
    public void testAllOneSCC() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 0, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        assertEquals(1, result.getComponents().size());
        assertEquals(4, result.getComponents().get(0).size());
    }
    
    // Test graph with no edges
    @Test
    public void testNoEdges() {
        Graph graph = new Graph(5, true);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        assertEquals(5, result.getComponents().size());
        for (List<Integer> component : result.getComponents()) {
            assertEquals(1, component.size());
        }
    }
}
