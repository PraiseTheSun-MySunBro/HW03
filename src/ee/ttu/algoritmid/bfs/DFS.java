package ee.ttu.algoritmid.bfs;

import ee.ttu.algoritmid.dijkstra.Dijkstra;
import ee.ttu.algoritmid.dijkstra.Vertex;
import ee.ttu.algoritmid.labyrinth.MazeRunner;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.AbstractMap.SimpleEntry;
import static ee.ttu.algoritmid.bfs.Direction.Type;

public class DFS {
    private static final Set<Integer> ILLEGAL_DIRECTIONS = new HashSet<>(Arrays.asList(-1, 0));
    private static final int STATE_TREASURE = -2;

    private final MazeRunner mazeRunner;
    private Node root;
    private Position treasurePosition;
    private SimpleEntry<Integer, Integer> size;


    public DFS(final MazeRunner mazeRunner) {
        this.mazeRunner = mazeRunner;
        this.size = mazeRunner.getSize();
        Node.init();
    }

    /* Treumax's algorithm. */
    public List<String> findBestPath() {
        if (size.getKey() == 0 || size.getValue() == 0) return null;

        Stack<Node> stack = new Stack<>();
        this.root = Node.getInstance(0, mazeRunner.getPosition());
        stack.push(this.root);

        while (!stack.isEmpty()) {
            Node currentNode = stack.pop();
            setTreasurePosition(currentNode, null);

            try {
                if (currentNode.getParent() != null)
                    moveToCurrentNode(currentNode);

                List<List<Integer>> scanMap = mazeRunner.scan();
                try {
                    List<SimpleEntry<Type, Integer>> possibleDirections = getPossibleDirections(currentNode,null, scanMap);

                    for (SimpleEntry<Type, Integer> entry : possibleDirections) {
                        LinkedList<Type> route = new LinkedList<>();
                        try {
                            Type moveDirection = entry.getKey();
                            int[] distance = new int[1];
                            distance[0] = entry.getValue();

                            move(route, moveDirection);
                            forwardTo(route, distance, moveDirection);

                            boolean visited = Node.isNodeExistInMap(Node.getNodeByPosition(mazeRunner.getPosition()));
                            int childVal = mazeRunner.scan().get(1).get(1);
                            Node child = Node.getInstance(childVal, mazeRunner.getPosition());
                            if (!visited) {
                                stack.add(child);
                                child.setParent(currentNode);
                            } else {
                                int idx = currentNode.getDirections().getNodeIndex(child);
                                if (idx != -1) {
                                    if (isCurrentNodeRouteShorter(currentNode, distance[0], idx))
                                        continue;
                                }
                            }

                            Type reversedDirection = Direction.getReversedDirection(route.peekLast());
                            currentNode.getDirections().setNode(moveDirection, child);
                            currentNode.getDirections().setRoute(moveDirection, route, distance[0]);
                            child.getDirections().setNode(reversedDirection, currentNode);
                            child.getDirections().setReversedRoute(
                                    reversedDirection, route, distance[0], currentNode.getValue(), child.getValue());
                        } catch (NoMoveException ex) {
                            // If no move then leave direction as null
                        }
                        finally {
                            if (!route.isEmpty())
                                turnBack(route);
                        }
                    }
                } catch (InvalidPossibleDirection ex) {
                    System.err.println("[getPossibleDirections]: Invalid result");
                }
            } catch (InvalidPosition ex) {
                ex.printStackTrace();
            }
        }
        return (treasurePosition != null) ? getShortestPath() : null;
    }

    private List<String> getShortestPath() {
        Dijkstra dijkstra = new Dijkstra(root);
        dijkstra.calculateShortestRoutes(dijkstra.graph.getTreasureVertex());

        LinkedList<Vertex> vertices = dijkstra.graph.getReversedRoute(root.hashCode());
        //System.out.println("Best path: " + vertices);

        int sum = 0;
        List<String> path = new ArrayList<>();
        Vertex[] route = vertices.toArray(new Vertex[vertices.size()]);
        Vertex src = route[0];

        for (int i = 1; i < route.length; ++i) {
            Vertex dest = route[i];
            Node parent = Node.getNodes().get(src.hashCode);
            Type dir = getDirectionBetweenNodes(Node.getNodes().get(src.hashCode), Node.getNodes().get(dest.hashCode));

            LinkedList<Type> routeList = parent.getDirections().getPassedRoutes().get(dir);
            sum += parent.getDirections().getDistances()[dir.ordinal()];

            path.addAll(routeList.stream()
                    .map(Type::name)
                    .collect(Collectors.toList()));

            src = route[i];
        }

        System.out.println("Sum = " + sum);
        return path;
    }

    private boolean isCurrentNodeRouteShorter(Node currentNode, int distance, int idx) {
        int knownDistance = currentNode.getDirections().getDistances()[idx];
        if (distance < knownDistance) {
            Type direction = Type.values()[idx];
            currentNode.getDirections().getPassedRoutes().remove(direction);
            currentNode.getDirections().getNodes()[idx] = null;
            currentNode.getDirections().getDistances()[idx] = 0;
            return false;
        }
        return true;
    }

    private void setTreasurePosition(Node currentNode, SimpleEntry<Integer, Integer> position) {
        if (currentNode != null && currentNode.getValue() == -2) {
            treasurePosition = currentNode.getPosition();
        } else if (position != null) {
            treasurePosition = Position.createInstance(position);
        }
    }

    private void moveToCurrentNode(Node destinationNode) throws InvalidPosition {
        Node node = Node.getNodeByPosition(mazeRunner.getPosition());
        if (node == null) {
            throw new InvalidPosition();
        }
        if (!destinationNode.equals(node)) {
            Set<Node> nodeSet = new HashSet<>(Arrays.asList(node.getDirections().getNodes()));

            if (!nodeSet.contains(destinationNode)) {
                Type dir = getDirectionBetweenNodes(node, node.getParent());
                moveTo(node.getDirections().getClonedRoute(dir));
                moveToCurrentNode(destinationNode);
            } else {
                Type dir = getDirectionBetweenNodes(node, destinationNode);
                moveTo(node.getDirections().getClonedRoute(dir));
            }
        }
    }

    private List<SimpleEntry<Type, Integer>> getPossibleDirections(Node currentNode, Type lastMove,
                    List<List<Integer>> scanMap) throws InvalidPossibleDirection {

        if (currentNode != null) {
            return getPossibleDirections(currentNode, scanMap);
        } else if (lastMove != null) {
            return getPossibleDirections(lastMove, scanMap);
        }
        throw new InvalidPossibleDirection();
    }

    private List<SimpleEntry<Type, Integer>> getPossibleDirections(Node currentNode, List<List<Integer>> scanMap) {
        List<SimpleEntry<Type, Integer>> possibleDirections = new ArrayList<>();
        Set<Type> passedRoutes = currentNode.getDirections().getPassedRoutes().keySet();

        int cost = scanMap.get(0).get(1);
        if (!ILLEGAL_DIRECTIONS.contains(cost) && !passedRoutes.contains(Type.N))
            possibleDirections.add(new SimpleEntry<>(Type.N, cost));
        cost = scanMap.get(1).get(0);
        if (!ILLEGAL_DIRECTIONS.contains(cost) && !passedRoutes.contains(Type.W))
            possibleDirections.add(new SimpleEntry<>(Type.W, cost));
        cost = scanMap.get(1).get(2);
        if (!ILLEGAL_DIRECTIONS.contains(cost) && !passedRoutes.contains(Type.E))
            possibleDirections.add(new SimpleEntry<>(Type.E, cost));
        cost = scanMap.get(2).get(1);
        if (!ILLEGAL_DIRECTIONS.contains(cost) && !passedRoutes.contains(Type.S))
            possibleDirections.add(new SimpleEntry<>(Type.S, cost));

        return possibleDirections;
    }

    private List<SimpleEntry<Type, Integer>> getPossibleDirections(Type lastMove, List<List<Integer>> scanMap) {
        List<SimpleEntry<Type, Integer>> possibleDirections = new ArrayList<>();

        int cost = scanMap.get(0).get(1);
        if (!ILLEGAL_DIRECTIONS.contains(cost) && lastMove != Type.S)
            possibleDirections.add(new SimpleEntry<>(Type.N, cost));
        cost = scanMap.get(1).get(0);
        if (!ILLEGAL_DIRECTIONS.contains(cost) && lastMove != Type.E)
            possibleDirections.add(new SimpleEntry<>(Type.W, cost));
        cost = scanMap.get(1).get(2);
        if (!ILLEGAL_DIRECTIONS.contains(cost) && lastMove != Type.W)
            possibleDirections.add(new SimpleEntry<>(Type.E, cost));
        cost = scanMap.get(2).get(1);
        if (!ILLEGAL_DIRECTIONS.contains(cost) && lastMove != Type.N)
            possibleDirections.add(new SimpleEntry<>(Type.S, cost));

        return possibleDirections;
    }

    /**
     * Get direction (Type instance) from parent node to current.
     * @param source source node.
     * @param destination destination node.
     * @return Type (direction) if exists otherwise null.
     */
    private Type getDirectionBetweenNodes(Node source, Node destination) {
        if (source != null && destination != null) {
            Node[] nodes = source.getDirections().getNodes();
            for (int i = 0; i < nodes.length; ++i) {
                if (nodes[i] != null && nodes[i].equals(destination)) {
                    return Type.values()[i];
                }
            }
        }
        return null;
    }

    private void moveTo(LinkedList<Type> route) {
        while (!route.isEmpty()) {
            mazeRunner.move(route.pollFirst().name());
        }
    }

    private void forwardTo(LinkedList<Type> route, int[] distance,
                            Type lastMove) throws NoMoveException, InvalidPossibleDirection {

        List<List<Integer>> scanMap = mazeRunner.scan();
        List<SimpleEntry<Type, Integer>> possibleDirections = getPossibleDirections(null, lastMove, scanMap);

        if (scanMap.get(1).get(1) == STATE_TREASURE) {
            System.out.println("I've found it!");
            setTreasurePosition(null, mazeRunner.getPosition());
            return;
        }

        if (possibleDirections.size() == 0) {
            throw new NoMoveException();
        }

        if (possibleDirections.size() == 1) {
            Type nextMove = possibleDirections.get(0).getKey();
            int dDistance = possibleDirections.get(0).getValue();

            distance[0] += (dDistance >= 0) ? dDistance : 0;
            move(route, nextMove);
            forwardTo(route, distance, nextMove);
        }

    }

    private void move(LinkedList<Type> route, Type moveDirection) {
        route.addLast(moveDirection);
        mazeRunner.move(route.peekLast().name());
    }

    private void turnBack(LinkedList<Type> route) {
        while (!route.isEmpty()) {
            mazeRunner.move(Direction.getReversedDirection(route.pollLast()).name());
        }
    }
}
