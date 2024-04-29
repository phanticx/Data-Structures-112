package spiderman;

import java.lang.reflect.Array;
import java.util.*;

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
 * HubInputFile name is passed through the command line as args[2]
 * Read from the HubInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * CollectedOutputFile name is passed in through the command line as args[3]
 * Output to CollectedOutputFile with the format:
 * 1. e Lines, listing the Name of the anomaly collected with the Spider who
 *    is at the same Dimension (if one exists, space separated) followed by 
 *    the Dimension number for each Dimension in the route (space separated)
 * 
 * @author Seth Kelley
 */

public class CollectAnomalies {

    // javac -d bin src/spiderman/*.java
    // java -cp bin spiderman.CollectAnomalies dimension.in spiderverse.in hub.in collected.out

    public static void main(String[] args) {

        if ( args.length < 4 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.CollectAnomalies <dimension INput file> <spiderverse INput file> <hub INput file> <collected OUTput file>");
                return;
        }

        // WRITE YOUR CODE HERE
        String dimensionInputFile = args[0];
        String spiderverseInputFile = args[1];
        String hubInputFile = args[2];
        String collectedOutputFile = args[3];

        StdIn.setFile(hubInputFile);
        int hub = StdIn.readInt();

        ArrayList<DimensionNode> adjacencyList = spiderman.Collider.getAdjacencyList(dimensionInputFile, spiderverseInputFile);
        ArrayList<People> people = collectPeople(adjacencyList, hub);

        writeCollectedAnomaliesToFile(people, adjacencyList, collectedOutputFile, hub);

    }

    private static ArrayList<People> collectPeople(ArrayList<DimensionNode> adjacencyList, int hub) {
        // WRITE YOUR CODE HERE
        ArrayList<People> people = new ArrayList<>();

        for (int i = 0; i < adjacencyList.size(); i++) {
            DimensionNode dimensionNode = adjacencyList.get(i);

            if (dimensionNode != null) {
                //System.out.println(dimensionNode.getDimension());

                ArrayList<People> dimensionPeople = dimensionNode.getPeople();

                if (dimensionPeople != null) {
                    for (int j = 0; j < dimensionPeople.size(); j++) {
                        People person = dimensionPeople.get(j);
                        if (person != null) {
                            if (person.getDimension() == hub) {
                                continue;
                            } else if (person.getDimension() == person.getSignature()) {
                                person.setType();
                                people.add(person);
                            } else if (person.getDimension() != person.getSignature()) {
                                person.setType();
                                people.add(person);
                            }
                        }
                    }
                }
            }
        }

        return people;

    }

    public static ArrayList<People> collectAnomalies(ArrayList<DimensionNode> adjacencyList, String hubInputFile) {
        ArrayList<People> anomalies = new ArrayList<>();
        StdIn.setFile(hubInputFile);
        int hub = StdIn.readInt();

        ArrayList<People> people = new ArrayList<>();

        for (int i = 0; i < adjacencyList.size(); i++) {
            DimensionNode dimensionNode = adjacencyList.get(i);

            if (dimensionNode != null) {

                ArrayList<People> dimensionPeople = dimensionNode.getPeople();

                if (dimensionPeople != null) {
                    for (int j = 0; j < dimensionPeople.size(); j++) {
                        People person = dimensionPeople.get(j);
                        if (person != null) {
                            if (person.getDimension() == person.getSignature()) {
                                continue;
                            } else if (person.getDimension() != person.getSignature()) {
                                person.setType();
                                people.add(person);
                            }
                        }
                    }
                }
            }
        }


        for (int i = 0; i < people.size(); i++) {
            People person = people.get(i);
            if (person != null) {
                if (person.getType().equalsIgnoreCase("ANOMALY")) {
                    anomalies.add(person);
                    //System.out.println(person.getName() + " is an anomaly!");
                }
            }
        }

        return anomalies;
    }

    private static void writeCollectedAnomaliesToFile(ArrayList<People> people, ArrayList<DimensionNode> adjacencyList, String collectedOutputFile, int hub) {
        class Dimension {
            int dimension;
            ArrayList<People> spiders;
            ArrayList<People> anomalies;
            ArrayList<Integer> path;
        }

        ArrayList<Dimension> dimensions = new ArrayList<>();

        for (int i = 0; i < people.size(); i++) {
            People person = people.get(i);
            if (person != null) {
                if (person.getType().equalsIgnoreCase("ANOMALY")) {
                    int dimension = person.getDimension();

                    boolean dimensionExists = false;
                    for (int j = 0; j < dimensions.size(); j++) {
                        Dimension d = dimensions.get(j);
                        if (d.dimension == dimension) {
                            d.anomalies.add(person);
                            dimensionExists = true;
                        }
                    }

                    if (!dimensionExists) {
                        Dimension d = new Dimension();
                        d.dimension = dimension;
                        d.anomalies = new ArrayList<>();
                        d.anomalies.add(person);
                        d.spiders = new ArrayList<People>();
                        dimensions.add(d);
                    }

                }
            }
        }

        for (int i = 0; i < people.size(); i++) {
            People person = people.get(i);
            if (person != null) {
                if (person.getType().equalsIgnoreCase("SPIDER")) {
                    int dimension = person.getDimension();

                    for (int j = 0; j < dimensions.size(); j++) {
                        Dimension d = dimensions.get(j);
                        if (d.dimension == dimension) {
                            d.spiders.add(person);
                        }
                    }

                }
            }
        }


        StdOut.setFile(collectedOutputFile);

        for (int i = 0; i < dimensions.size(); i++) {
            ArrayList<Integer> resultPath = new ArrayList<>();


            if (!dimensions.get(i).spiders.isEmpty()) {
                int start = dimensions.get(i).spiders.get(0).getDimension();
                int end = hub;
                resultPath = breadthFirstSearch(adjacencyList, end, start);
                if (resultPath != null) Collections.reverse(resultPath);

                StdOut.print(dimensions.get(i).anomalies.get(0).getName() + " " + dimensions.get(i).spiders.get(0).getName() + " ");
                for (int j = 0; j < resultPath.size(); j++) {
                    StdOut.print(resultPath.get(j) + " ");
                }
                StdOut.println();

                System.out.println("done!");

            } else {
                int start = hub;
                int end = dimensions.get(i).anomalies.get(0).getDimension();
                resultPath = breadthFirstSearch(adjacencyList, start, end);



                StdOut.print(dimensions.get(i).anomalies.get(0).getName() + " ");
                for (int j = 0; j < resultPath.size(); j++) {
                    StdOut.print(resultPath.get(j) + " ");
                }
                resultPath.remove(resultPath.size() - 1);
                Collections.reverse(resultPath);
                for (int j = 0; j < resultPath.size(); j++) {
                    StdOut.print(resultPath.get(j) + " ");
                }

                StdOut.println();

                //System.out.println("done!");
            }


        }


    }

    private static ArrayList<Integer> breadthFirstSearch(ArrayList<DimensionNode> adjacencyList, int start, int end) {
        Queue<Integer> queue = new LinkedList<>();
        Set<Integer> visited = new HashSet<>();
        Map<Integer, Integer> path = new HashMap<>();

        queue.add(start);
        visited.add(start);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            if (current == end) {
                ArrayList<Integer> resultPath = new ArrayList<>();
                while (current != start) {
                    resultPath.add(current);
                    current = path.get(current);
                }
                resultPath.add(start);
                Collections.reverse(resultPath);
                return resultPath;
            }

            DimensionNode node = findIndexOfNode(adjacencyList, current);
            //System.out.println("Node: " + node.getDimension());
            while (node != null) {
                int next = node.getDimension();
                if (!isVisited(visited, next)) {
                    queue.add(next);
                    //System.out.println("Added: " + next);
                    path.put(next, current);
                    //System.out.println("put: " + next + ", " + current);
                }
                markAsVisited(visited, node.getDimension());
                node = node.getNext();
            }

            //System.out.println("Map: " + path);
        }

        return null;
    }

    private static ArrayList<DimensionNode> neighborNodes (ArrayList<DimensionNode> adjacencyList, DimensionNode node) {
        ArrayList<DimensionNode> neighbors = new ArrayList<>();
        DimensionNode current = findIndexOfNode(adjacencyList, node.getDimension());

        while (current != null) {
            neighbors.add(current);
            current = current.getNext();
        }

        return neighbors;
    }


    private static DimensionNode findIndexOfNode(ArrayList<DimensionNode> adjacencyList, int dimension) {
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

    private static boolean isVisited(Set<Integer> visited, int dimension) {
        return visited.contains(dimension);
    }

    private static void markAsVisited(Set<Integer> visited, int dimension) {
        visited.add(dimension);
    }
}
