package com.smartcity.graph.scc;

import com.smartcity.common.Graph;
import org.junit.Test;
import static org.junit.Assert.*;

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
}
