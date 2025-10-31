package com.smartcity.graph.topo;

import com.smartcity.common.Graph;
import org.junit.Test;
import static org.junit.Assert.*;

public class TopologicalSortTest {
    
    // Test simple DAG topological sort
    @Test
    public void testSimpleDAG() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);
        
        KahnTopologicalSort kahn = new KahnTopologicalSort(graph);
        KahnTopologicalSort.TopoResult result = kahn.topologicalSort();
        
        assertTrue(result.isDAG());
        assertEquals(4, result.getOrder().size());
    }
    
    // Test cyclic graph detection
    @Test
    public void testCyclicGraph() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 0, 1);
        
        KahnTopologicalSort kahn = new KahnTopologicalSort(graph);
        KahnTopologicalSort.TopoResult result = kahn.topologicalSort();
        
        assertFalse(result.isDAG());
    }
    
    // Test DFS topological sort
    @Test
    public void testDFSTopologicalSort() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 1);
        graph.addEdge(0, 2, 1);
        graph.addEdge(1, 3, 1);
        graph.addEdge(2, 3, 1);
        
        DFSTopologicalSort dfs = new DFSTopologicalSort(graph);
        KahnTopologicalSort.TopoResult result = dfs.topologicalSort();
        
        assertTrue(result.isDAG());
        assertEquals(4, result.getOrder().size());
    }
    
    // Test empty graph
    @Test
    public void testEmptyGraph() {
        Graph graph = new Graph(1, true);
        
        KahnTopologicalSort kahn = new KahnTopologicalSort(graph);
        KahnTopologicalSort.TopoResult result = kahn.topologicalSort();
        
        assertTrue(result.isDAG());
        assertEquals(1, result.getOrder().size());
    }
}
