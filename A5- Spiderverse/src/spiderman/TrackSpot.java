package spiderman;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Steps to implement this class main method:
 * 
 * Step 1:
 * DimensionInputFile name is passed through the command line as args[0]
 * Read from the DimensionsInputFile with the format:
 * 1. The first line with three numbers:
 *      i.    a (int): number of dimensions in the graph
 *      ii.   b (int): the initial size of the cluster table prior to rehashing
 *      iii.  c (double): the capacity(threshold) used to rehash the cluster table 
 * 2. a lines, each with:
 *      i.    The dimension number (int)
 *      ii.   The number of canon events for the dimension (int)
 *      iii.  The dimension weight (int)
 * 
 * Step 2:
 * SpiderverseInputFile name is passed through the command line as args[1]
 * Read from the SpiderverseInputFile with the format:
 * 1. d (int): number of people in the file
 * 2. d lines, each with:
 *      i.    The dimension they are currently at (int)
 *      ii.   The name of the person (String)
 *      iii.  The dimensional signature of the person (int)
 * 
 * Step 3:
 * SpotInputFile name is passed through the command line as args[2]
 * Read from the SpotInputFile with the format:
 * Two integers (line seperated)
 *      i.    Line one: The starting dimension of Spot (int)
 *      ii.   Line two: The dimension Spot wants to go to (int)
 * 
 * Step 4:
 * TrackSpotOutputFile name is passed in through the command line as args[3]
 * Output to TrackSpotOutputFile with the format:
 * 1. One line, listing the dimenstional number of each dimension Spot has visited (space separated)
 * 
 * @author Seth Kelley
 */

public class TrackSpot {


    //java -cp bin spiderman.TrackSpot dimension.in spiderverse.in spot.in trackspot.out
    
    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.TrackSpot <dimension INput file> <spiderverse INput file> <spot INput file> <trackspot OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE

        String dimensionInputFile = args[0];
        String spiderverseInputFile = args[1];
        String spotInputFile = args[2];
        String trackSpotOutputFile = args[3];


        ArrayList<DimensionNode> adjacencyList = spiderman.Collider.getAdjacencyList(dimensionInputFile, spiderverseInputFile);

        StdIn.setFile(spotInputFile);
        int start = StdIn.readInt();
        int end = StdIn.readInt();

        //System.out.println(start);
        //System.out.println(end);

        ArrayList<Integer> visited = new ArrayList<>();
        ArrayList<Integer> path = new ArrayList<>();

        ArrayList<Integer> searchPath = depthFirstSearch(adjacencyList, start, end, visited, path);
        writeToFile(searchPath, trackSpotOutputFile);
    }

    private static void writeToFile(ArrayList<Integer> searchPath, String trackSpotOutputFile) {
        StdOut.setFile(trackSpotOutputFile);

        for (int i = 0; i < searchPath.size(); i++) {
            StdOut.print(searchPath.get(i) + " ");
        }
    }

    private static DimensionNode findNode(ArrayList<DimensionNode> adjacencyList, int dimension) {
        for (int i = 0; i < adjacencyList.size(); i++) {
            if (adjacencyList.get(i) == null) {
                continue;
            }

            if (adjacencyList.get(i).getDimension() == dimension) {
                return adjacencyList.get(i);
            }
        }
        return null;
    }

    public static boolean isVisited(ArrayList<Integer> visited, int dimension) {
        for (int i = 0; i < visited.size(); i++) {
            if (visited.get(i) == dimension) {
                return true;
            }
        }
        return false;
    }

    public static ArrayList<Integer> depthFirstSearch(ArrayList<DimensionNode> adjacencyList, int start, int end, ArrayList<Integer> visited, ArrayList<Integer> path) {
        visited.add(start);
        path.add(start);
        if (start == end) {
            return path;
        }

        DimensionNode node = findNode(adjacencyList, start);
        while (node != null) {
            int next = node.getDimension();
            if (!isVisited(visited, next)) {
                // System.out.println("Recursing to depth first search with " + next + " and " + end);
                ArrayList<Integer> result = depthFirstSearch(adjacencyList, next, end, visited, path);
                if (result != null) {
                    return result;
                }
            }
            node = node.getNext();
        }

        return null;
    }
}
