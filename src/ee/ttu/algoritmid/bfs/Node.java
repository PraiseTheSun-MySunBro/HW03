package ee.ttu.algoritmid.bfs;

import java.util.*;

public class Node {
    private int value;
    private Node parent;
    private Direction directions;
    private Position position;
    private static HashMap<Integer, Node> nodes;

    static void init() {
        nodes = new HashMap<>();
    }

    public static Node getInstance(int value, AbstractMap.SimpleEntry<Integer, Integer> pos) {
        Position position = Position.createInstance(pos);
        int x = pos.getKey();
        int y = pos.getValue();
        int hash = computeHash(position);
        return nodes.computeIfAbsent(hash, k -> new Node(value, position));
    }

    /**
     * @deprecated using only for tests
     * @param x - x
     * @param y - y
     */
    @Deprecated
    public Node(int x, int y) {
        this.position = Position.createInstance(x, y);
    }

    private Node(int value, Position position) {
        this.parent = null;
        this.value = value;
        this.position = position;
        this.directions = new Direction();
    }

    public static int computeHash(Position position) {
        int temp = (position.getY() + (position.getX()+1)/2);
        return position.getX() + temp*temp;
    }

    @Override
    public int hashCode() {
        return computeHash(this.position);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Node other = (Node) obj;
        return this.position.equals(other.position);
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public Direction getDirections() {
        return directions;
    }

    public Position getPosition() {
        return position;
    }

    public Node getParent() {
        return parent;
    }

    public int getValue() {
        return value;
    }

    public static boolean isNodeExistInMap(Node node) {
        return node != null && nodes.containsKey(node.hashCode());
    }

    public static Node getNodeByPosition(AbstractMap.SimpleEntry<Integer, Integer> position) {
        return nodes.get(computeHash(Position.createInstance(position)));
    }

    public static HashMap<Integer, Node> getNodes() {
        return nodes;
    }
}
