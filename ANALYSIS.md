# Analysis Report - Smart City Scheduling

## Executive Summary

This report presents a comprehensive analysis of graph algorithms for smart city task scheduling, covering Strongly Connected Components detection, Topological Sorting, and DAG shortest/longest path algorithms. The analysis is based on 9 datasets of varying sizes and structures.

## Dataset Analysis

### Small Datasets (6-8 vertices)

| Dataset | Vertices | Edges | SCCs | Structure |
|---------|----------|-------|------|-----------|
| small_cyclic_1 | 6 | 5 | 4 | Mixed: 1 cycle, 3 isolated |
| small_dag_1 | 7 | 6 | 7 | Pure DAG: linear chain |
| small_multi_scc | 8 | 7 | 4 | Multiple SCCs: 2 cycles |

### Medium Datasets (10-18 vertices)

| Dataset | Vertices | Edges | SCCs | Structure |
|---------|----------|-------|------|-----------|
| medium_cyclic_1 | 15 | 16 | 10 | Mixed: 2 large cycles |
| medium_dag_1 | 12 | 14 | 12 | Pure DAG: dense |
| medium_dense_scc | 18 | 20 | 12 | Dense: 3 moderate cycles |

### Large Datasets (25-40 vertices)

| Dataset | Vertices | Edges | SCCs | Structure |
|---------|----------|-------|------|-----------|
| large_cyclic_sparse | 30 | 31 | 21 | Sparse: 2 large SCCs |
| large_dag_dense | 25 | 37 | 25 | Dense DAG: many paths |
| large_multi_scc | 40 | 43 | 29 | Sparse: 4 SCCs |

## Performance Results

### Tarjan's SCC Algorithm

| Dataset | Vertices | Operations | Time (ms) | Ops/V |
|---------|----------|-----------|-----------|-------|
| small_cyclic_1 | 6 | 17 | 0.038 | 2.83 |
| small_dag_1 | 7 | 20 | 0.012 | 2.86 |
| small_multi_scc | 8 | 23 | 0.015 | 2.88 |
| medium_cyclic_1 | 15 | 46 | 0.038 | 3.07 |
| medium_dag_1 | 12 | 38 | 0.110 | 3.17 |
| medium_dense_scc | 18 | 56 | 0.053 | 3.11 |
| large_cyclic_sparse | 30 | 91 | 0.075 | 3.03 |
| large_dag_dense | 25 | 87 | 0.064 | 3.48 |
| large_multi_scc | 40 | 123 | 0.042 | 3.08 |

**Key Observations**:
1. Operations scale linearly with vertices: ~3 operations per vertex
2. Time complexity remains O(V+E) as expected
3. Dense graphs show slightly more operations per vertex
4. Consistent performance across all dataset sizes

### Topological Sort (Kahn's Algorithm)

| Dataset | Condensed Vertices | Operations | Time (ms) | Ops/V |
|---------|-------------------|-----------|-----------|-------|
| small_cyclic_1 | 4 | 10 | 0.032 | 2.50 |
| small_dag_1 | 7 | 20 | 0.011 | 2.86 |
| small_multi_scc | 4 | 9 | 0.006 | 2.25 |
| medium_cyclic_1 | 10 | 29 | 0.009 | 2.90 |
| medium_dag_1 | 12 | 41 | 0.030 | 3.42 |
| medium_dense_scc | 12 | 37 | 0.029 | 3.08 |
| large_cyclic_sparse | 21 | 62 | 0.042 | 2.95 |
| large_dag_dense | 25 | 100 | 0.037 | 4.00 |
| large_multi_scc | 29 | 89 | 0.021 | 3.07 |

**Key Observations**:
1. Operations scale with condensed graph size
2. Dense graphs require more queue operations
3. BFS approach maintains O(V+E) complexity
4. Consistent performance across structures

### Critical Path Analysis

| Dataset | Critical Path Length | Path Vertices | Max Weight |
|---------|---------------------|---------------|------------|
| small_cyclic_1 | 5 | 2 | 5 |
| small_dag_1 | 17 | 7 | 17 |
| small_multi_scc | 5 | 2 | 5 |
| medium_cyclic_1 | 30 | 10 | 30 |
| medium_dag_1 | 30 | 8 | 30 |
| medium_dense_scc | 17 | 6 | 17 |
| large_cyclic_sparse | 79 | 21 | 79 |
| large_dag_dense | 55 | 13 | 55 |
| large_multi_scc | 69 | 18 | 69 |

**Key Observations**:
1. Critical path length increases with graph depth
2. Linear chains produce longest critical paths
3. Dense graphs offer more alternative paths
4. Critical path identifies scheduling bottlenecks

## Algorithmic Analysis

### SCC Detection Bottlenecks

1. **DFS Stack Operations**: Most expensive operation
   - Average: 3 operations per vertex
   - Increases with cycle complexity
   
2. **Low-Link Updates**: O(E) edge traversals
   - Dense graphs: more updates
   - Sparse graphs: fewer comparisons

3. **Stack Management**: Push/pop for SCC extraction
   - Linear with vertex count
   - Minimal overhead

### Topological Sort Efficiency

1. **In-Degree Computation**: O(E) preprocessing
   - Single pass over all edges
   - Necessary for Kahn's algorithm

2. **Queue Operations**: O(V) vertex processing
   - Each vertex enqueued/dequeued once
   - Constant time per operation

3. **Edge Relaxation**: O(E) total relaxations
   - Each edge processed once
   - Optimal for DAGs

### DAG Shortest Path Performance

1. **Topological Order Dependency**: Must compute first
   - One-time O(V+E) cost
   - Amortized over all path queries

2. **Edge Relaxation**: Single pass sufficient
   - O(E) operations total
   - No priority queue needed

3. **Path Reconstruction**: O(path length)
   - Backtracking through parent pointers
   - Minimal memory overhead

## Effect of Graph Structure

### Density Impact

**Sparse Graphs (E ≈ V)**:
- Fewer operations per vertex
- Faster SCC detection
- Simpler condensation
- Linear critical paths

**Dense Graphs (E ≈ V²)**:
- More edge traversals
- Complex SCC structures
- Multiple alternative paths
- Shorter critical paths

### SCC Size Impact

**Large SCCs**:
- More stack operations
- Fewer condensed vertices
- Simpler topological order
- Compressed dependencies

**Small SCCs**:
- More condensed vertices
- Complex topological order
- Detailed dependency tracking
- Granular scheduling control

### Depth vs Width

**Deep Graphs**:
- Long critical paths
- Sequential dependencies
- Limited parallelization
- Higher completion time

**Wide Graphs**:
- Multiple parallel paths
- Concurrent task execution
- Better resource utilization
- Lower critical path length

## Practical Recommendations

### Algorithm Selection

1. **For Cycle Detection**:
   - Use Tarjan's SCC: O(V+E) single pass
   - More efficient than DFS-based cycle detection
   - Provides complete component information

2. **For Task Ordering**:
   - Kahn's algorithm: explicit cycle detection
   - DFS topological sort: memory efficient
   - Choose based on implementation constraints

3. **For Scheduling Optimization**:
   - DAG shortest path: minimize resource usage
   - DAG longest path: critical path analysis
   - Both essential for project management

### Smart City Applications

1. **Street Maintenance Scheduling**:
   - SCC: identify circular route dependencies
   - Topological sort: determine maintenance order
   - Critical path: estimate completion time

2. **Sensor Network Management**:
   - SCC: detect communication loops
   - DAG paths: optimize data flow
   - Critical path: identify bottlenecks

3. **Infrastructure Projects**:
   - SCC: compress interdependent tasks
   - Topological order: construction sequence
   - Longest path: project timeline

### Performance Optimization

1. **For Large Graphs**:
   - Consider iterative DFS implementations
   - Use adjacency lists for sparse graphs
   - Cache condensation results

2. **For Real-Time Systems**:
   - Precompute SCCs for static dependencies
   - Maintain topological order incrementally
   - Use batch updates for efficiency

3. **For Memory Constraints**:
   - Stream processing for very large graphs
   - External memory algorithms for massive datasets
   - Compressed graph representations

## Complexity Verification

All algorithms achieve theoretical optimal complexity:

- **Tarjan SCC**: O(V+E) verified by linear operation scaling
- **Kahn Topological Sort**: O(V+E) confirmed by edge counts
- **DAG Shortest Path**: O(V+E) demonstrated in results
- **DAG Longest Path**: O(V+E) same as shortest path

The empirical results confirm linear scaling with graph size, validating the implementation correctness.

## Conclusion

The implemented algorithms provide efficient solutions for smart city task scheduling:

1. **Tarjan's SCC** effectively identifies and compresses circular dependencies
2. **Topological sorting** provides valid execution orders for acyclic task graphs
3. **DAG path algorithms** optimize scheduling and identify critical bottlenecks

The analysis demonstrates that algorithm choice should be based on:
- Graph structure (cyclic vs acyclic)
- Performance requirements (real-time vs batch)
- Output needs (ordering vs optimization)

For smart city applications, the combination of all three algorithms provides comprehensive scheduling solutions with optimal time complexity.

## Future Work

1. Parallel algorithms for multi-core systems
2. Incremental updates for dynamic graphs
3. Approximation algorithms for NP-hard extensions
4. Integration with constraint satisfaction solvers
5. Real-time monitoring and adaptive scheduling
