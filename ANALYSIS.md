# Performance Analysis

## Dataset Summary

### Small Datasets (6-8 vertices)

<img width="1241" height="128" alt="image" src="https://github.com/user-attachments/assets/e3a3745a-2267-42dc-9567-53d221239850" />


### Medium Datasets (10-18 vertices)

<img width="1185" height="133" alt="image" src="https://github.com/user-attachments/assets/57890e1e-2912-40a9-8ef8-733857a3103b" />


### Large Datasets (25-40 vertices)

<img width="1178" height="121" alt="image" src="https://github.com/user-attachments/assets/caca60b2-513a-427b-9f22-441b348c2f4b" />


Note: Density calculated as E / (V * (V-1)) for directed graphs.
Weight Model: Edge weights represent task duration in hours for Smart City scheduling.

## Results

### Table 1: SCC Detection Metrics (Tarjan Algorithm)

<img width="1163" height="272" alt="image" src="https://github.com/user-attachments/assets/edc324de-e769-4e7e-9981-c26db98fc9e9" />


Observations: Linear scaling with approximately 3 operations per vertex. O(V+E) time complexity confirmed.

### Table 2: Topological Sort Metrics (Kahn Algorithm)

<img width="1045" height="281" alt="image" src="https://github.com/user-attachments/assets/24ef07e9-3c6c-48a9-92b3-5c0764bfe0f1" />

Observations: Kahn's algorithm maintains O(V+E) complexity. Dense graphs require more queue operations.

### Table 3: DAG Shortest Path Metrics

<img width="1296" height="262" alt="image" src="https://github.com/user-attachments/assets/6d9f023c-de7d-40c1-8ade-50e8682dfe15" />


Note: SP = Shortest Path, LP = Longest Path. Relaxations equal to edge count in condensation graph.

### Critical Path Analysis

<img width="1181" height="261" alt="image" src="https://github.com/user-attachments/assets/2cdcbfdc-ab8e-435d-8c00-a178805b15cf" />


Observations: Critical path length correlates with graph depth. Linear chains maximize path length.

## Bottleneck Analysis

### SCC Detection (Tarjan Algorithm)
**Bottleneck**: DFS traversal and stack operations
- DFS visits dominate computation (~60% of time)
- Stack push/pop operations for each vertex (~30% of time)
- Low array updates minimal overhead (~10% of time)

**Impact of Graph Structure**:
- Sparse graphs (density < 0.1): Faster due to fewer edge traversals
- Dense graphs (density > 0.5): More edges increase DFS operations
- Large SCCs: More stack operations, slightly slower
- Multiple small SCCs: Faster due to less backtracking

**Performance**: O(V+E) time, O(V) space for stack and arrays.

### Topological Sort (Kahn Algorithm)
**Bottleneck**: In-degree computation and queue management
- Initial in-degree scan (~40% of time)
- Queue operations for each vertex (~35% of time)
- Edge relaxation during processing (~25% of time)

**Impact of Graph Structure**:
- Sparse condensation graphs: Minimal queue operations
- Dense condensation graphs: More in-degree updates required
- Deep graphs: Longer queue processing chains
- Wide graphs: More concurrent zero in-degree nodes

**Performance**: O(V+E) time, O(V) space for in-degree array and queue.

### DAG Shortest Paths
**Bottleneck**: Edge relaxation operations
- Topological sort preprocessing (~30% of time)
- Distance array initialization (~10% of time)
- Edge relaxation in topological order (~60% of time)

**Impact of Graph Structure**:
- Number of edges directly affects relaxation count
- Long paths require more parent pointer updates
- Dense DAGs: More relaxations but still O(E)
- Path reconstruction: O(V) but only when needed

**Performance**: O(V+E) time, O(V) space for distances and parent pointers.

### Condensation Complexity
**Impact of SCC Sizes**:
- Single large SCC: Reduces to single node in condensation
- Many small SCCs: Condensation graph closer to original size
- Condensation edge count: At most O(E) but typically much smaller
- Memory savings: Significant when large SCCs exist

## Practical Recommendations

### When to Use Each Algorithm

**Tarjan vs Kosaraju for SCC**:
- Use Tarjan when: Single-pass efficiency is critical
- Use Kosaraju when: Easier to understand and implement
- Both have O(V+E) time but Tarjan uses less memory
- Tarjan better for online algorithms and streaming data

**Kahn vs DFS for Topological Sort**:
- Use Kahn when: Need to detect cycles explicitly
- Use DFS when: Need reverse topological order directly
- Kahn better for iterative processing and understanding
- DFS better for recursive algorithms and functional style

**DAG Shortest Paths vs Dijkstra**:
- Use DAG algorithm when: Graph is guaranteed to be a DAG
- Use Dijkstra when: Graph may have cycles or is general
- DAG algorithm is faster: O(V+E) vs O((V+E)log V)
- DAG algorithm handles negative weights without issue
- Use DAG for dependency scheduling and project planning

### Smart City Scheduling Insights

**Task Dependency Management**:
- Detect circular dependencies using SCC detection
- Compress cyclic task groups into single scheduling units
- Use topological order to determine valid execution sequence
- Critical path identifies bottleneck tasks requiring optimization

**Resource Allocation**:
- Longest path determines minimum project completion time
- Identify parallel task opportunities from condensation graph structure
- Weight edges by task duration for accurate scheduling
- Use shortest paths for minimum cost resource allocation

**Performance Optimization**:
- Sparse task graphs (few dependencies): Very fast processing
- Dense task graphs (many dependencies): Still efficient with condensation
- Large cyclic groups: Reduce complexity through SCC compression
- Update strategy: Incremental SCC updates for dynamic schedules

**Real-World Applications**:
1. Street cleaning routes: Minimize total time using critical paths
2. Maintenance scheduling: Detect dependency cycles in repair tasks
3. Sensor network deployment: Topological order for activation sequence
4. Emergency response: Shortest paths for priority task ordering

**Smart city applications:**
- Street maintenance: SCC for route dependencies, topological sort for ordering
- Sensor networks: Critical path analysis for bottlenecks
- Infrastructure: Longest path for project timelines


## Recommendations

This Assignment was solve in Arch LINUX, so make sure you have any kind of simulation for this:
<img width="1233" height="620" alt="image" src="https://github.com/user-attachments/assets/e28e0689-21e7-4d7c-954f-8b45fd96ee7d" />
