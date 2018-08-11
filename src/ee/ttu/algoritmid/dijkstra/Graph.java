package ee.ttu.algoritmid.dijkstra;

import ee.ttu.algoritmid.bfs.Direction;
import ee.ttu.algoritmid.bfs.Node;

import java.util.*;

public class Graph {
    private HashMap<Integer, Vertex> vertices;
    public int endHash = -1;

    public Graph(Node root) {
        vertices = new HashMap<>();
        vertices.put(root.hashCode(), new Vertex(root.getPosition().toString(), root.hashCode()));

        for (Node currentNode : Node.getNodes().values()) {
            if (currentNode.equals(root)) continue;

            int nHashCode = currentNode.hashCode();
            vertices.putIfAbsent(nHashCode, new Vertex(currentNode.getPosition().toString(), nHashCode));
            //System.out.println("Current node value = " + currentNode.getValue());
            if (currentNode.getValue() == -2) {
                endHash = nHashCode;
            }

            Direction direction = currentNode.getDirections();
            int[] distances = direction.getDistances();
            for (int i = 0; i < distances.length; ++i) {
                if (distances[i] > 0) {
                    Node child = currentNode.getDirections().getNodes()[i];
                    if (child != null) {
                        int childHashCode = child.hashCode();
                        vertices.putIfAbsent(childHashCode, new Vertex(child.getPosition().toString(), childHashCode));
                        addEdge(nHashCode, childHashCode, distances[i], Direction.Type.values()[i]);
                    }
                }
            }

        }
    }

    public void addEdge(int src, int dest, int distance, Direction.Type direction) {
        Vertex vertex = vertices.get(src);
        Edge edge = new Edge(vertices.get(dest), distance, direction);
        vertex.neighbours.add(edge);
    }

    public Vertex getTreasureVertex() {
        return vertices.get(endHash);
    }

    public LinkedList<Vertex> getReversedRoute(int hashCode) {
        Vertex vertex = vertices.get(hashCode);
        LinkedList<Vertex> route = vertex.route;
        route.add(vertex);
        Collections.reverse(route);
        return route;
    }

    public Collection<Vertex> getVertices() {
        return vertices.values();
    }
}
