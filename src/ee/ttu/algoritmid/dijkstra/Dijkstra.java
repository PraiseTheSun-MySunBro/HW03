package ee.ttu.algoritmid.dijkstra;

import ee.ttu.algoritmid.bfs.Node;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;

public class Dijkstra {
    public Graph graph;

    public Dijkstra(Node root) {
        graph = new Graph(root);
    }

    public void calculateShortestRoutes(Vertex root) {
        root.minDistance = 0;

        PriorityQueue<Vertex> queue = new PriorityQueue<>();
        queue.add(root);

        while (!queue.isEmpty()) {
            Vertex vertex = queue.poll();

            System.out.println("queue = " + vertex.name + " " + vertex.minDistance);
            for (Edge neighbour : vertex.neighbours){
                int newDistance = vertex.minDistance + neighbour.distance;

                if (newDistance < neighbour.vertex.minDistance){
                    queue.remove(neighbour.vertex);
                    neighbour.vertex.minDistance = newDistance;

                    neighbour.vertex.route = new LinkedList<>(vertex.route);
                    neighbour.vertex.route.add(vertex);

                    queue.add(neighbour.vertex);
                }
            }
        }

    }
}
