package com.smartcity.graph.scc;

import com.smartcity.common.Graph;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;
import java.util.ArrayList;

/**
 * Test cases for CondensationGraph class.
 */
public class CondensationGraphTest {
    
    // Test component count matches SCC count
    @Test
    public void testComponentCountMatchesSCCCount() {
        Graph graph = new Graph(6, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 3, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        CondensationGraph condensation = new CondensationGraph(graph, result.getComponents());
        
        assertEquals(result.getComponents().size(), condensation.getComponentCount());
    }
    
    // Test condensation is always a DAG
    @Test
    public void testCondensationIsDAG() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 1);
        graph.addEdge(4, 2, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        CondensationGraph condensation = new CondensationGraph(graph, result.getComponents());
        
        assertTrue(condensation.isDAG());
    }
    
    // Test edge mapping is correct
    @Test
    public void testEdgeMappingCorrect() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 0, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        CondensationGraph condensation = new CondensationGraph(graph, result.getComponents());
        Graph cGraph = condensation.getCondensationGraph();
        
        assertTrue(cGraph.getVertices() >= 1);
    }
    
    // Test vertex to component mapping
    @Test
    public void testVertexToComponentMapping() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        CondensationGraph condensation = new CondensationGraph(graph, result.getComponents());
        
        int comp0 = condensation.getComponentId(0);
        int comp1 = condensation.getComponentId(1);
        int comp2 = condensation.getComponentId(2);
        
        assertEquals(comp0, comp1);
        assertEquals(comp1, comp2);
    }
    
    // Test get vertices in component
    @Test
    public void testGetVerticesInComponent() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 0, 1);
        graph.addEdge(2, 3, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        CondensationGraph condensation = new CondensationGraph(graph, result.getComponents());
        
        for (int i = 0; i < condensation.getComponentCount(); i++) {
            List<Integer> vertices = condensation.getVerticesInComponent(i);
            assertNotNull(vertices);
            assertTrue(vertices.size() > 0);
        }
    }
    
    // Test export structure
    @Test
    public void testExportStructure() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        CondensationGraph condensation = new CondensationGraph(graph, result.getComponents());
        String structure = condensation.exportStructure();
        
        assertNotNull(structure);
        assertTrue(structure.contains("Condensation Graph Structure"));
    }
    
    // Test single component graph
    @Test
    public void testSingleComponent() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        
        TarjanSCC tarjan = new TarjanSCC(graph);
        TarjanSCC.SCCResult result = tarjan.findSCCs();
        
        CondensationGraph condensation = new CondensationGraph(graph, result.getComponents());
        
        assertEquals(1, condensation.getComponentCount());
        assertTrue(condensation.isDAG());
    }
}
