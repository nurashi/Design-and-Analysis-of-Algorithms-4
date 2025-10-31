# Assignment 4: Smart City Scheduling

Consolidates Strongly Connected Components, Topological Ordering, and DAG Shortest Paths for task dependency management and scheduling optimization.

## Weight Model

**This implementation uses EDGE WEIGHTS for DAG shortest paths.**

Weights represent task duration in hours for Smart City scheduling scenarios. This allows the system to:
- Calculate minimum completion times for task sequences
- Identify critical paths that determine project duration
- Optimize resource allocation based on time constraints
- Handle both positive task durations and negative adjustments

## Quick Start

### Building the Project

```bash
mvn clean compile
```
<img width="1913" height="792" alt="image" src="https://github.com/user-attachments/assets/e792e3bb-2b82-434b-ad66-ffa11a0014f6" />

### Running Tests

```bash
mvn test
```
<img width="1918" height="1150" alt="image" src="https://github.com/user-attachments/assets/cbcbf9aa-ef4a-478c-8c15-ec6bbaee6507" />

### Running the Main Application

```bash
mvn clean package
mvn exec:java -Dexec.mainClass="com.smartcity.Main"
```
<img width="978" height="220" alt="image" src="https://github.com/user-attachments/assets/e384e4ff-f3cc-4cf6-84bc-2f4bf4dd3d6d" />

Info about Processing:
<img width="887" height="975" alt="image" src="https://github.com/user-attachments/assets/57d0ba1e-5893-4ed0-9d31-cbb78d65c6ff" />
<img width="908" height="1038" alt="image" src="https://github.com/user-attachments/assets/27666964-fdd9-488c-8e63-4bb69d69fc2b" />
<img width="860" height="965" alt="image" src="https://github.com/user-attachments/assets/74558dc5-89f8-41b8-9fc2-201852cfb365" />
<img width="843" height="1117" alt="image" src="https://github.com/user-attachments/assets/227feb1f-046d-4420-9126-bcc854eea33d" />

## Dataset Summary

This is a sample data, you may change whatever you want.

### Small Datasets (6-8 vertices)
1. **small_cyclic_1.json**: 6 vertices, 5 edges, contains one cycle
2. **small_dag_1.json**: 7 vertices, 6 edges, pure DAG (linear)
3. **small_multi_scc.json**: 8 vertices, 7 edges, multiple SCCs

### Medium Datasets (10-18 vertices)
4. **medium_cyclic_1.json**: 15 vertices, 16 edges, contains 2 SCCs
5. **medium_dag_1.json**: 12 vertices, 14 edges, pure DAG (dense)
6. **medium_dense_scc.json**: 18 vertices, 20 edges, multiple SCCs with mixed structure

### Large Datasets (25-40 vertices)
7. **large_cyclic_sparse.json**: 30 vertices, 31 edges, sparse with 2 large SCCs (density < 0.05)
8. **large_dag_dense.json**: 25 vertices, 37 edges, dense DAG with multiple paths (density > 0.06)
9. **large_multi_scc.json**: 40 vertices, 43 edges, 4 SCCs with connections

All datasets use edge-based weights ranging from 1 to 7 representing task durations.

## Algorithms Implemented

### 1. Strongly Connected Components (Tarjan)

**File**: `src/main/java/com/smartcity/graph/scc/TarjanSCC.java`

Tarjan's algorithm finds all strongly connected components in O(V+E) time using a single DFS traversal:
- Uses discovery time and low-link values to identify components
- Maintains a stack to track vertices in the current SCC
- Identifies SCCs when low[u] == disc[u]
- Constructs condensation graph by mapping inter-component edges

**Complexity**: O(V+E) time, O(V) space

### 2. Condensation Graph

**File**: `src/main/java/com/smartcity/graph/scc/CondensationGraph.java`

Builds a DAG from strongly connected components:
- Each node represents an SCC from the original graph
- Edges connect components based on original graph edges
- Provides mapping between original vertices and components
- Guarantees acyclic structure for topological sorting

**Key Methods**:
- `getCondensationGraph()`: Returns the DAG of components
- `getComponentId(vertex)`: Maps vertex to its component
- `getVerticesInComponent(id)`: Lists all vertices in a component
- `isDAG()`: Verifies condensation is acyclic

### 3. Topological Sorting

**Files**: 
- `src/main/java/com/smartcity/graph/topo/KahnTopologicalSort.java`
- `src/main/java/com/smartcity/graph/topo/DFSTopologicalSort.java`
- `src/main/java/com/smartcity/graph/topo/ComponentTopologicalSort.java`

**Kahn's Algorithm** (BFS-based):
- Computes in-degrees for all vertices
- Processes vertices with in-degree 0 iteratively
- Detects cycles if not all vertices are processed
- Complexity: O(V+E) time, O(V) space

**DFS-based**:
- Post-order DFS traversal
- Pushes vertices to stack after visiting all descendants
- Reverses stack for topological order
- Complexity: O(V+E) time, O(V) space

**Component Topological Sort**:
- Works on condensation graphs
- Produces both component ordering and derived task ordering
- Provides valid execution sequence for original tasks

### 4. DAG Shortest and Longest Paths

**File**: `src/main/java/com/smartcity/graph/dagsp/DAGShortestPath.java`

Implements efficient O(V+E) algorithms for DAGs:

**Shortest Path**:
- Processes vertices in topological order
- Relaxes edges to find minimum distances
- Reconstructs paths using parent pointers
- Handles negative weights correctly

**Longest Path**:
- Uses same approach with maximum instead of minimum
- Finds critical paths in project scheduling
- Identifies bottlenecks in task dependencies
- Determines minimum project completion time

**Critical Path**:
- Finds the longest path in the entire DAG
- Reports source, destination, path, and length
- Essential for project management and scheduling

### Finding Critical Path

```java
DAGShortestPath dagsp = new DAGShortestPath(condensationDAG);
CriticalPathResult critical = dagsp.findCriticalPath();

System.out.println("Critical path: " + critical.getPath());
System.out.println("Length: " + critical.getLength() + " hours");
```

## Dataset Generation

All datasets are stored in `data/` directory in JSON format. Each file contains:
- `vertices`: Number of vertices
- `directed`: Whether graph is directed (always true)
- `edges`: Array of {from, to, weight} objects
- `source`: Default source vertex for path algorithms
- `weightModel`: "edge" (weights on edges)

Example format:
```json
{
  "vertices": 6,
  "directed": true,
  "edges": [
    {"from": 0, "to": 1, "weight": 3},
    {"from": 1, "to": 2, "weight": 4}
  ],
  "source": 0,
  "weightModel": "edge"
}
```

## Testing

Comprehensive JUnit test suite covers:

**SCC Tests** (`TarjanSCCTest.java`):
- Simple cycles
- Multiple SCCs
- Pure DAGs
- Single vertex graphs
- Self-loops
- Disconnected components
- Graphs with no edges

**Condensation Tests** (`CondensationGraphTest.java`):
- Component count validation
- DAG property verification
- Edge mapping correctness
- Vertex-to-component mapping
- Structure export

**Topological Sort Tests** (`TopologicalSortTest.java`):
- Simple DAGs
- Cyclic graph detection
- Multiple valid orderings
- Linear chains
- Single vertex graphs

**DAG Path Tests** (`DAGShortestPathTest.java`):
- Shortest paths with positive weights
- Longest paths
- Path reconstruction
- Critical path finding
- Negative weights
- Unreachable vertices
- Isolated sources

Run all tests:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=TarjanSCCTest
```

## Performance Metrics

The implementation tracks:
- **Operations**: DFS visits, edge traversals, relaxations
- **Time**: Execution time in milliseconds
- **Space**: Memory usage for data structures

Metrics are collected via the `Metrics` interface and displayed in output.

## Expected Complexity

| Algorithm | Time Complexity | Space Complexity |
|-----------|----------------|------------------|
| Tarjan SCC | O(V + E) | O(V) |
| Condensation | O(V + E) | O(V) |
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
