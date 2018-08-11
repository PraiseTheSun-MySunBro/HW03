package ee.ttu.algoritmid.labyrinth;

import ee.ttu.algoritmid.bfs.DFS;
import ee.ttu.algoritmid.bfs.Node;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.PriorityQueue;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        long startTime = System.currentTimeMillis();
        HW03 impl = new HW03("/ee/ttu/algoritmid/publicSet/nm100b.maze");
        //impl.getMazeRunner().scan();
//        List<String> solution = impl.solve();
        long endTime = System.currentTimeMillis();
        //System.out.println(solution);
        System.out.println("Time = " + Long.toString(endTime - startTime) + " ms");
    }
}