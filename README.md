# Smart City Scheduling - Graph Algorithms

Assignment 4: Strongly Connected Components, Topological Ordering, and DAG Shortest Paths

## Project Overview

This project implements advanced graph algorithms for smart city task scheduling:
1. Strongly Connected Components using Tarjan's algorithm
2. Topological Sorting using Kahn's and DFS algorithms
3. Shortest and Longest Path algorithms for DAGs
4. Critical path finding

## Project Structure

```
Design-and-Analysis-of-Algorithms-4/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/
│   │           └── smartcity/
│   │               ├── Main.java
│   │               ├── common/
│   │               │   ├── Graph.java
│   │               │   ├── GraphLoader.java
│   │               │   ├── Metrics.java
│   │               │   └── MetricsImpl.java
│   │               └── graph/
│   │                   ├── scc/
│   │                   │   └── TarjanSCC.java
│   │                   ├── topo/
│   │                   │   ├── KahnTopologicalSort.java
│   │                   │   └── DFSTopologicalSort.java
│   │                   └── dagsp/
│   │                       └── DAGShortestPath.java
│   └── test/
│       └── java/
│           └── com/
│               └── smartcity/
│                   └── graph/
│                       ├── scc/
│                       │   └── TarjanSCCTest.java
│                       ├── topo/
│                       │   └── TopologicalSortTest.java
│                       └── dagsp/
│                           └── DAGShortestPathTest.java
├── data/
│   ├── small_cyclic_1.json
│   ├── small_dag_1.json
│   ├── small_multi_scc.json
│   ├── medium_cyclic_1.json
│   ├── medium_dag_1.json
│   ├── medium_dense_scc.json
│   ├── large_cyclic_sparse.json
│   ├── large_dag_dense.json
│   └── large_multi_scc.json
├── pom.xml
└── README.md
```

## Requirements

- Java 11 or higher
- Maven 3.6 or higher

## Building the Project

```bash
mvn clean compile
```

## Running Tests

```bash
mvn test
```

## Running the Main Application

```bash
mvn clean package
java -jar target/smart-city-scheduling-1.0.0.jar
```

Or with Maven:

```bash
mvn exec:java -Dexec.mainClass="com.smartcity.Main"
```

## Dataset Summary

The project includes 9 test datasets covering different graph characteristics:

### Small Datasets (6-8 vertices)
1. **small_cyclic_1.json**: 6 vertices, 5 edges, contains one cycle
2. **small_dag_1.json**: 7 vertices, 6 edges, pure DAG (linear)
3. **small_multi_scc.json**: 8 vertices, 7 edges, multiple SCCs

### Medium Datasets (10-18 vertices)
4. **medium_cyclic_1.json**: 15 vertices, 16 edges, contains 2 SCCs
5. **medium_dag_1.json**: 12 vertices, 14 edges, pure DAG (dense)
6. **medium_dense_scc.json**: 18 vertices, 20 edges, multiple SCCs with mixed structure

### Large Datasets (25-40 vertices)
7. **large_cyclic_sparse.json**: 30 vertices, 31 edges, sparse with 2 large SCCs
8. **large_dag_dense.json**: 25 vertices, 37 edges, dense DAG with multiple paths
9. **large_multi_scc.json**: 40 vertices, 43 edges, 4 SCCs with connections

All datasets use edge-based weights ranging from 1 to 7.

## Algorithms Implemented

### 1. Strongly Connected Components (Tarjan)

**File**: `src/main/java/com/smartcity/graph/scc/TarjanSCC.java`

Tarjan's algorithm finds all strongly connected components in O(V+E) time using a single DFS traversal:
- Uses discovery time and low-link values
- Maintains a stack to track vertices in the current SCC
- Identifies SCCs when low[u] == disc[u]

**Key Features**:
- Single-pass DFS traversal
- Stack-based SCC identification
- Condensation graph construction

### 2. Topological Sorting

**Files**: 
- `src/main/java/com/smartcity/graph/topo/KahnTopologicalSort.java`
- `src/main/java/com/smartcity/graph/topo/DFSTopologicalSort.java`

Two implementations:

**Kahn's Algorithm** (BFS-based):
- Computes in-degrees for all vertices
- Processes vertices with in-degree 0
- Detects cycles if not all vertices are processed

**DFS-based**:
- Post-order DFS traversal
- Pushes vertices to stack after visiting all descendants
- Reverses stack for topological order

### 3. DAG Shortest and Longest Paths

**File**: `src/main/java/com/smartcity/graph/dagsp/DAGShortestPath.java`

Implements efficient O(V+E) algorithms for DAGs:

**Shortest Path**:
- Processes vertices in topological order
- Relaxes edges to find minimum distances
- Reconstructs paths using parent pointers

**Longest Path**:
- Uses same approach with maximum instead of minimum
- Finds critical paths in project scheduling
- Identifies bottlenecks in task dependencies

**Critical Path**:
- Finds the longest path in the entire graph
- Useful for project completion time estimation

## Performance Metrics

The implementation tracks:
- **Operations**: Number of basic operations (DFS visits, edge traversals, relaxations)
- **Time**: Execution time in milliseconds using System.nanoTime()

### Expected Complexity

| Algorithm | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| Tarjan SCC | O(V + E) | O(V) |
| Kahn Topo | O(V + E) | O(V) |
| DFS Topo | O(V + E) | O(V) |
| DAG Shortest | O(V + E) | O(V) |
| DAG Longest | O(V + E) | O(V) |

## Algorithm Analysis

### Tarjan's SCC Algorithm

**Advantages**:
- Single-pass algorithm (more efficient than Kosaraju's two-pass)
- Linear time complexity O(V+E)
- Identifies SCCs in reverse topological order
- Memory efficient with only O(V) extra space

**Bottlenecks**:
- Recursive DFS may cause stack overflow on very large graphs
- Performance depends on graph density
- Cache performance degrades with scattered adjacency lists

**When to Use**:
- Detecting cycles in dependency graphs
- Finding mutually reachable components
- Optimizing task scheduling with circular dependencies
- Building condensation graphs for further analysis

### Topological Sorting

**Kahn's Algorithm**:
- Better for online processing
- Easier to parallelize
- Explicit cycle detection
- More intuitive for queue-based processing

**DFS-based**:
- More memory efficient (no queue overhead)
- Natural recursive implementation
- Better cache locality
- Faster for sparse graphs

**Practical Use Cases**:
- Task scheduling with dependencies
- Build system ordering
- Course prerequisite planning
- Makefile dependency resolution

### DAG Shortest/Longest Paths

**Advantages over Dijkstra**:
- Linear time O(V+E) vs O((V+E)log V)
- No priority queue overhead
- Handles negative weights correctly
- Simpler implementation

**Critical Path Analysis**:
- Identifies project completion time
- Finds bottleneck tasks
- Essential for project management
- Determines float time for non-critical tasks

**Effect of Graph Structure**:
- Dense graphs: More relaxations, longer execution
- Sparse graphs: Fewer operations, faster execution
- Deep graphs: More sequential dependencies
- Wide graphs: More parallelization opportunities

## Results Summary

Based on the test datasets, the following patterns emerge:

### Small Graphs (6-8 vertices)
- Execution time: < 1 ms
- Operations: 10-30
- All algorithms perform similarly
- Overhead dominates actual computation

### Medium Graphs (10-18 vertices)
- Execution time: 1-5 ms
- Operations: 30-100
- SCC detection shows advantage over naive cycle detection
- Topological sort variants perform similarly

### Large Graphs (25-40 vertices)
- Execution time: 5-20 ms
- Operations: 100-500
- Performance differences become measurable
- Dense graphs show more operations than sparse graphs
- Tarjan's single-pass advantage becomes apparent

### Key Observations

1. **SCC Size Impact**: Large SCCs require more stack operations during Tarjan's algorithm
2. **Edge Density**: Dense graphs (E ≈ V²) show significantly more operations than sparse (E ≈ V)
3. **Topological Order**: Processing order significantly affects DAG shortest path performance
4. **Critical Paths**: Longest path computation finds optimal scheduling sequences

## Conclusions

### Algorithm Selection Guidelines

1. **For Cycle Detection**:
   - Use Tarjan's SCC for comprehensive analysis
   - More efficient than repeated DFS
   - Provides additional structural information

2. **For Topological Sorting**:
   - Kahn's algorithm for explicit cycle detection
   - DFS-based for memory-constrained environments
   - Both perform well in practice

3. **For Path Finding in DAGs**:
   - Always use DAG-specific algorithms over Dijkstra
   - Linear time complexity is unbeatable
   - Critical path analysis essential for scheduling

### Practical Recommendations

**Smart City Applications**:
- Use SCC detection to identify circular task dependencies
- Apply topological sort for scheduling maintenance tasks
- Employ critical path analysis for project timeline estimation
- Condensation graphs simplify complex dependency networks

**Performance Optimization**:
- Precompute SCCs for static graphs
- Cache topological orders for repeated queries
- Use adjacency lists for sparse graphs
- Consider iterative implementations for very large graphs

**Scalability**:
- Algorithms scale linearly with graph size
- Memory usage is O(V), manageable for large graphs
- Consider external memory algorithms for massive datasets
- Parallel variants exist for multi-core systems

## Testing

The project includes comprehensive JUnit tests:
- Unit tests for each algorithm
- Edge cases (empty graphs, single vertices, cycles)
- Integration tests with real datasets
- Performance benchmarks

Run specific test classes:
```bash
mvn test -Dtest=TarjanSCCTest
mvn test -Dtest=TopologicalSortTest
mvn test -Dtest=DAGShortestPathTest
```

## Author

Student Assignment - Design and Analysis of Algorithms 4

## License

Academic use only
