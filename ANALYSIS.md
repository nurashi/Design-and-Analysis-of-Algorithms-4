# Performance Analysis

## Dataset Summary

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

## Results

### SCC Detection (Tarjan)

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

Observations: Linear scaling with ~3 ops/vertex. O(V+E) confirmed.

### Topological Sort

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

Observations: Kahn's algorithm maintains O(V+E). Dense graphs need more operations.

### Critical Paths

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

Observations: Longer paths in deep graphs. Linear chains maximize critical path length.

## Algorithm Complexity

All algorithms achieve O(V+E) time complexity:
- Tarjan SCC: Single DFS pass
- Topological Sort: One pass over vertices and edges
- DAG Paths: Topological order + edge relaxation

## Performance Analysis

**Bottlenecks:**
- SCC: DFS stack operations dominate (~3 ops per vertex)
- Topological Sort: In-degree computation requires full edge scan
- DAG Paths: Must compute topological order first

**Graph Structure Impact:**
- Sparse graphs (E ≈ V): Faster, fewer operations
- Dense graphs (E ≈ V²): More edge traversals
- Deep graphs: Longer critical paths
- Wide graphs: Better parallelization potential

## Recommendations

**When to use:**
- Tarjan SCC: Cycle detection and dependency compression
- Kahn's algorithm: When explicit cycle detection needed
- DFS topological: When memory is limited
- DAG paths: Project scheduling and optimization

**Smart city applications:**
- Street maintenance: SCC for route dependencies, topological sort for ordering
- Sensor networks: Critical path analysis for bottlenecks
- Infrastructure: Longest path for project timelines
