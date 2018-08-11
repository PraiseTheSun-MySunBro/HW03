package ee.ttu.algoritmid.labyrinth;

import java.util.List;
import java.util.ArrayList;
import java.util.AbstractMap.SimpleEntry;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.net.URISyntaxException;

public class MazeRunner {
    /**
     * The labyrinth being explored.
     * List of rows of list of columns in that row
     * e.g., map.get(1).get(3) gives the 3rd cell value of the first row
     * NB! You are not allowed to use any "tricks" to read the map directly.
     * You must explore the map using move() and scan() and create your
     * own map.
     */
    private List<List<Integer>> map;
    /**
     * The labyrinth being explored as raw values.
     * NB! You are not allowed to use any "tricks" to read the map values directly.
     * You must explore the map using move() and scan() and create your
     * own map.
     */
    private List<List<String>> rawMap;
    /**
     * The current x coordinate.
     */
    private Integer x;
    /**
     * The current y coordinate.
     */
    private Integer y;

    /**
     * Construct the map and set start coordinates from a file.
     *
     * @param fileName  file with labyrinth map
     */
    public MazeRunner(String fileName) throws IOException, URISyntaxException {
        File file = new File(this.getClass().getResource(fileName).toURI());
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String fileLine;
        map = new ArrayList<List<Integer>>();
        rawMap = new ArrayList<List<String>>();
        Integer lineNumber = 0;
        while ((fileLine = reader.readLine()) != null) {
            List<Integer> line = new ArrayList<Integer>();
            List<String> rawLine = new ArrayList<String>();
            for (int i = 0; i < fileLine.length(); i++) {
                rawLine.add(Character.toString(fileLine.charAt(i)));
                if (fileLine.charAt(i) == 'X') {
                    line.add(-1);
                } else if (fileLine.charAt(i) == 'B') {
                    line.add(0);
                    x = i;
                    y = lineNumber;
                } else if (fileLine.charAt(i) == 'T') {
                    line.add(-2);
                } else {
                    line.add(Character.getNumericValue(fileLine.charAt(i)));
                }
            }
            lineNumber++;
            map.add(line);
            rawMap.add(rawLine);
        }
    }

    /**
     * Moves one cell in the specified heading.
     *
     * @param   heading the heading (one of "N", "E", "S" or "W")
     * @return          true if move was successful, false if not.
     */
    public boolean move(String heading) {
        if (heading.equals("N") && (y > 0) &&
                ((map.get(y - 1).get(x) >= 0) || (map.get(y - 1).get(x) == -2))) {
            y -= 1;
            return true;
        } else if (heading.equals("E") && (x < map.get(y).size() - 1) &&
                ((map.get(y).get(x + 1) >= 0) || (map.get(y).get(x + 1) == -2))) {
            x += 1;
            return true;
        } else if (heading.equals("S") && (y < map.size() - 1) &&
                ((map.get(y + 1).get(x) >= 0) || (map.get(y + 1).get(x) == -2))) {
            y += 1;
            return true;
        } else if (heading.equals("W") && (x > 0) &&
                ((map.get(y).get(x - 1) >= 0) || (map.get(y).get(x - 1) == -2))) {
            x -= 1;
            return true;
        }
        return false;
    }

    /**
     * Scan the surroundings.
     *
     * @return  the values of surrounding cells of the current location
     */
    public List<List<Integer>> scan() {
        List<List<Integer>> result = new ArrayList<List<Integer>>();
        for (int i = y - 1; i < y + 2; i++) {
            List<Integer> line = new ArrayList<Integer>();
            for (int j = x - 1; j < x + 2; j++) {
                if (i >= 0 && map.size() > i && j >= 0 && map.get(i).size() > j) {
                    line.add(map.get(i).get(j));
                } else {
                    line.add(-1);
                }
            }
            result.add(line);
        }
        return result;
    }

    /**
     * Scan the surroundings, return result as string values.
     *
     * @return  the values of surrounding cells of the current location
     */
    public List<List<String>> scanAsString() {
        List<List<String>> result = new ArrayList<List<String>>();
        for (int i = y - 1; i < y + 2; i++) {
            List<String> line = new ArrayList<String>();
            for (int j = x - 1; j < x + 2; j++) {
                if (i >= 0 && map.size() > i && j >= 0 && map.get(i).size() > j) {
                    line.add(rawMap.get(i).get(j));
                } else {
                    line.add("#");
                }
            }
            result.add(line);
        }
        return result;
    }

    /**
     * Returns the current position coordinates.
     * @return  the current position coordinates
     */
    public SimpleEntry<Integer, Integer> getPosition() {
        return new SimpleEntry<Integer, Integer>(x, y);
    }

    /**
     * Returns the size of the labyrinth (height width).
     * @return  the size of the labyrinth (height, width)
     */
    public SimpleEntry<Integer, Integer> getSize() {
        if (map != null && map.get(0) != null) {
            return new SimpleEntry<Integer, Integer>(map.size(), map.get(0).size());
        }
        return null;
    }

}