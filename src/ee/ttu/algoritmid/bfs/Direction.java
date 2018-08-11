package ee.ttu.algoritmid.bfs;

import java.util.HashMap;
import java.util.LinkedList;

public class Direction {
    public enum Type {
        N, S, W, E
    }

    private Node[] nodes;
    private int[] distances;
    private HashMap<Type, LinkedList<Type>> routes;

    public Direction() {
        this.nodes = new Node[Type.values().length];
        this.distances = new int[Type.values().length];
        this.routes = new HashMap<>();
    }

    public void setRoute(Type dir, LinkedList<Type> route, int distance) {
        this.routes.put(dir, cloneRoute(route));
        setDistance(dir, distance);
    }

    public void setReversedRoute(Type dir, LinkedList<Type> route, int distance, int parentVal, int currentVal) {
        LinkedList<Type> clonedRoute = cloneRoute(route);
        LinkedList<Type> reversedRoute = new LinkedList<>();
        while (!clonedRoute.isEmpty()) {
            reversedRoute.addLast(getReversedDirection(clonedRoute.pollLast()));
        }

        this.routes.put(dir, reversedRoute);
        distance += (parentVal != 0) ? (parentVal - currentVal) : 0;
        setDistance(dir, distance);
    }

    public LinkedList<Type> getClonedRoute(Type dir) {
        return cloneRoute(this.routes.get(dir));
    }

    @SuppressWarnings("unchecked")
    private LinkedList<Type> cloneRoute(LinkedList<Type> route) {
        return (LinkedList<Type>) route.clone();
    }

    public void removeRoute(Type dir) {
        this.routes.remove(dir);
    }

    public HashMap<Type, LinkedList<Type>> getPassedRoutes() {
        return this.routes;
    }

    public void setNode(Type dir, Node node) {
        this.nodes[dir.ordinal()] = node;
    }

    public Node getNode(Type dir) {
        return this.nodes[dir.ordinal()];
    }

    public int getNodeIndex(Node node) {
        for (int i = 0; i < nodes.length; ++i) {
            if (nodes[i] != null && nodes[i].equals(node))
                return i;
        }
        return -1;
    }

    public Node[] getNodes() {
        return this.nodes;
    }

    public int[] getDistances() {
        return distances;
    }

    public void setDistance(Type dir, int distance) {
        this.distances[dir.ordinal()] = distance;
    }

    public static Type getReversedDirection(Type dir) {
        Type reversedDirection = null;

        switch (dir) {
            case N:
                reversedDirection = Type.S;
                break;
            case S:
                reversedDirection = Type.N;
                break;
            case W:
                reversedDirection = Type.E;
                break;
            case E:
                reversedDirection = Type.W;
                break;
        }
        return reversedDirection;
    }
}