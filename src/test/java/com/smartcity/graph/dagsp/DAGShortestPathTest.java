package com.smartcity.graph.dagsp;

import com.smartcity.common.Graph;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class DAGShortestPathTest {
    
    // Test shortest path in simple DAG
    @Test
    public void testShortestPath() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 3, 2);
        graph.addEdge(2, 3, 7);
        
        DAGShortestPath dagsp = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagsp.shortestPaths(0);
        
        assertEquals(0, result.getDistances()[0]);
        assertEquals(5, result.getDistances()[1]);
        assertEquals(3, result.getDistances()[2]);
        assertEquals(7, result.getDistances()[3]);
    }
    
    // Test longest path in DAG
    @Test
    public void testLongestPath() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(0, 2, 3);
        graph.addEdge(1, 3, 2);
        graph.addEdge(2, 3, 7);
        
        DAGShortestPath dagsp = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagsp.longestPaths(0);
        
        assertEquals(10, result.getDistances()[3]);
    }
    
    // Test path reconstruction
    @Test
    public void testPathReconstruction() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 2);
        
        DAGShortestPath dagsp = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagsp.shortestPaths(0);
        
        List<Integer> path = result.reconstructPath(3);
        assertEquals(4, path.size());
        assertEquals(Integer.valueOf(0), path.get(0));
        assertEquals(Integer.valueOf(3), path.get(3));
    }
    
    // Test critical path
    @Test
    public void testCriticalPath() {
        Graph graph = new Graph(5, true);
        graph.addEdge(0, 1, 3);
        graph.addEdge(0, 2, 2);
        graph.addEdge(1, 3, 4);
        graph.addEdge(2, 3, 1);
        graph.addEdge(3, 4, 2);
        
        DAGShortestPath dagsp = new DAGShortestPath(graph);
        DAGShortestPath.CriticalPathResult result = dagsp.findCriticalPath();
        
        assertTrue(result.getLength() > 0);
        assertFalse(result.getPath().isEmpty());
    }
    
    // Test graph with negative weights
    @Test
    public void testNegativeWeights() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, -2);
        graph.addEdge(0, 2, 4);
        graph.addEdge(1, 2, 3);
        graph.addEdge(2, 3, 1);
        
        DAGShortestPath dagsp = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagsp.shortestPaths(0);
        
        assertEquals(0, result.getDistances()[0]);
        assertEquals(-2, result.getDistances()[1]);
        assertEquals(1, result.getDistances()[2]);
        assertEquals(2, result.getDistances()[3]);
    }
    
    // Test source vertex with no outgoing edges
    @Test
    public void testSourceNoOutgoing() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 3);
        
        DAGShortestPath dagsp = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagsp.shortestPaths(2);
        
        assertEquals(0, result.getDistances()[2]);
        assertEquals(Integer.MAX_VALUE, result.getDistances()[0]);
        assertEquals(Integer.MAX_VALUE, result.getDistances()[1]);
    }
    
    // Test unreachable vertices
    @Test
    public void testUnreachableVertices() {
        Graph graph = new Graph(4, true);
        graph.addEdge(0, 1, 2);
        graph.addEdge(2, 3, 3);
        
        DAGShortestPath dagsp = new DAGShortestPath(graph);
        DAGShortestPath.PathResult result = dagsp.shortestPaths(0);
        
        assertEquals(0, result.getDistances()[0]);
        assertEquals(2, result.getDistances()[1]);
        assertEquals(Integer.MAX_VALUE, result.getDistances()[2]);
        assertEquals(Integer.MAX_VALUE, result.getDistances()[3]);
    }
    
    // Test longest path equals shortest path
    @Test
    public void testSameWeights() {
        Graph graph = new Graph(3, true);
        graph.addEdge(0, 1, 5);
        graph.addEdge(1, 2, 5);
        
        DAGShortestPath dagsp = new DAGShortestPath(graph);
        DAGShortestPath.PathResult shortest = dagsp.shortestPaths(0);
        DAGShortestPath.PathResult longest = dagsp.longestPaths(0);
        
        assertEquals(shortest.getDistances()[2], longest.getDistances()[2]);
    }
}
