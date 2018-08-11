package ee.ttu.algoritmid.dijkstra;

import ee.ttu.algoritmid.bfs.Direction;

public class Edge {
    public final Vertex vertex;
    public final int distance;
    public final Direction.Type direction;

    public Edge(Vertex vertex, int distance, Direction.Type direction) {
        this.vertex = vertex;
        this.distance = distance;
        this.direction = direction;
    }
}
