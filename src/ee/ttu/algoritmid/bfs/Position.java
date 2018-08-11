package ee.ttu.algoritmid.bfs;

import static java.util.AbstractMap.SimpleEntry;

public class Position {
    private final int x, y;

    private Position(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @deprecated using only for tests
     * @param x - x
     * @param y - y
     */
    @Deprecated
    public static Position createInstance(int x, int y) {
        return new Position(x, y);
    }

    public static Position createInstance(SimpleEntry<Integer, Integer> pos) {
        return new Position(pos.getKey(), pos.getValue());
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;

        Position other = (Position) obj;
        return this.x == other.x && this.y == other.y;
    }

    @Override
    public String toString() {
        return String.format("[%d;%d]", x, y);
    }
}
