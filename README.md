# Smart City Scheduling - Graph Algorithms

Assignment 4: Strongly Connected Components, Topological Ordering, and DAG Shortest Paths

## Building the Project

```bash
mvn clean compile
```
<img width="1913" height="792" alt="image" src="https://github.com/user-attachments/assets/e792e3bb-2b82-434b-ad66-ffa11a0014f6" />

## Running Tests

```bash
mvn test
```
<img width="1918" height="1150" alt="image" src="https://github.com/user-attachments/assets/cbcbf9aa-ef4a-478c-8c15-ec6bbaee6507" />

## Running the Main Application

```bash
mvn clean package
java -jar target/smart-city-scheduling-1.0.0.jar
```


## Dataset Summary
This is a sample data, you may change whthever what you want.
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
- 
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
<img width="1921" height="1095" alt="image" src="https://github.com/user-attachments/assets/3111893f-b45a-4553-bda6-d5bb267c2653" />
<img width="1901" height="1082" alt="image" src="https://github.com/user-attachments/assets/cdf5dd01-a1a2-4e68-a53c-e1835ddf8d2b" />
<img width="1912" height="1077" alt="image" src="https://github.com/user-attachments/assets/34f7117b-e196-4c14-ab2f-b79e22d0a097" />



## Author

Student Assignment - Design and Analysis of Algorithms 4 by Nurassyl Orazbek SE-2405 
