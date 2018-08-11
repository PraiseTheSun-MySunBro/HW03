package ee.ttu.algoritmid.dijkstra;

import java.util.ArrayList;
import java.util.LinkedList;

public class Vertex implements Comparable<Vertex> {
    public ArrayList<Edge> neighbours;
    public LinkedList<Vertex> route;
    public int minDistance = Integer.MAX_VALUE;
    public String name;
    public int hashCode;

    @Override
    public int compareTo(Vertex o) {
        return Integer.compare(minDistance, o.minDistance);
    }

    public Vertex(String name, int hashCode) {
        this.name = name;
        this.neighbours = new ArrayList<>();
        this.route = new LinkedList<>();
        this.hashCode = hashCode;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
