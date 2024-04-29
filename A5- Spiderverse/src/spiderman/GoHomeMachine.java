package spiderman;

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
 * Read from the SpotInputFile with the format:
 * One integer
 *      i.    The dimensional number of the starting hub (int)
 * 
 * Step 4:
 * AnomaliesInputFile name is passed through the command line as args[3]
 * Read from the AnomaliesInputFile with the format:
 * 1. e (int): number of anomalies in the file
 * 2. e lines, each with:
 *      i.   The Name of the anomaly which will go from the hub dimension to their home dimension (String)
 *      ii.  The time allotted to return the anomaly home before a canon event is missed (int)
 * 
 * Step 5:
 * ReportOutputFile name is passed in through the command line as args[4]
 * Output to ReportOutputFile with the format:
 * 1. e Lines (one for each anomaly), listing on the same line:
 *      i.   The number of canon events at that anomalies home dimensionafter being returned
 *      ii.  Name of the anomaly being sent home
 *      iii. SUCCESS or FAILED in relation to whether that anomaly made it back in time
 *      iv.  The route the anomaly took to get home
 * 
 * @author Seth Kelley
 */

public class GoHomeMachine {

    // javac -d bin src/spiderman/*.java
    // java -cp bin spiderman.GoHomeMachine dimension.in spiderverse.in hub.in anomalies.in report.out

    public static void main(String[] args) {

        if (args.length < 5) {
            StdOut.println(
                    "Execute: java -cp bin spiderman.GoHomeMachine <dimension INput file> <spiderverse INput file> <hub INput file> <anomalies INput file> <report OUTput file>");
            return;
        }

        // WRITE YOUR CODE HERE
        String dimensionInputFile = args[0];
        String spiderverseInputFile = args[1];
        String hubInputFile = args[2];
        String anomaliesInputFile = args[3];
        String reportOutputFile = args[4];

        ArrayList<DimensionNode> adjacencyList = spiderman.Collider.getAdjacencyList(dimensionInputFile, spiderverseInputFile);
        ArrayList<People> anomalies = spiderman.CollectAnomalies.collectAnomalies(adjacencyList, hubInputFile);

        //System.out.println(anomalies);

        StdIn.setFile(anomaliesInputFile);
        int e = StdIn.readInt();
        StdIn.readLine();

        class Anomaly {
            String name;
            int time;
        }

        ArrayList<Anomaly> anomalyList = new ArrayList<>();
        for (int i = 0; i < e; i++) {
            Anomaly anomaly = new Anomaly();
            anomaly.name = StdIn.readString();
            anomaly.time = StdIn.readInt();
            StdIn.readLine();
            anomalyList.add(anomaly);
        }

        //update anomaly list
        for (int i = anomalies.size() - 1; i > -1; i--) {
            People person = anomalies.get(i);
            boolean found = false;
            for (int j = 0; j < anomalyList.size(); j++) {
                Anomaly anomaly = anomalyList.get(j);

                if (person.getName().equals(anomaly.name)) {
                    person.setTime(anomaly.time);
                    anomalyList.remove(j);
                    person.setDimension(928);
                    //System.out.println(person);
                    found = true;
                    break;
                }
            }
            if (!found) {
                anomalies.remove(i);
            }
        }


        StdOut.setFile(reportOutputFile);
        //System.out.println("fefnoqwfjiqwjofqwpjfqpo");


        for (People anomaly : anomalies) {
            //System.out.println(anomaly);
            ArrayList<Integer> path = shortestPath(adjacencyList, anomaly.getDimension(), anomaly.getSignature());
            if (anomaly.getTime() >= findPathDistance(path, adjacencyList)) {
                String result = "SUCCESS";
                DimensionNode signature = adjacencyList.get(getIndexOfDimension(adjacencyList, anomaly.getSignature()));
                int canonEvents = signature.getCanonEvents();
                StdOut.print(canonEvents + " " + anomaly.getName() + " " + result + " ");
                for (int i = 0; i < path.size(); i++) {
                    StdOut.print(path.get(i) + " ");
                }
                StdOut.println();
            } else {
                String result = "FAILED";
                DimensionNode signature = adjacencyList.get(getIndexOfDimension(adjacencyList, anomaly.getSignature()));
                int canonEvents = signature.getCanonEvents();
                signature.setCanonEvents(canonEvents - 1);
                canonEvents -= 1;
                StdOut.print(canonEvents + " " + anomaly.getName() + " " + result + " ");
                for (int i = 0; i < path.size(); i++) {
                    StdOut.print(path.get(i) + " ");
                }
                StdOut.println();
            }
        }

    }

    public static int findPathDistance(ArrayList<Integer> path, ArrayList<DimensionNode> adjacencyList) {
        int distance = 0;
        for (int i = 0; i < path.size(); i++) {
            DimensionNode current = adjacencyList.get(getIndexOfDimension(adjacencyList, path.get(i)));
            if (i != 0 && i != path.size()) {
                distance += current.getWeight();
                distance += current.getWeight();
            } else {
                distance += current.getWeight();
            }
        }
        return distance;
    }


    public static ArrayList<Integer> shortestPath(ArrayList<DimensionNode> adjacencyList, int start, int end) {
        Map<Integer, Integer> distances = new HashMap<>();
        Map<Integer, Integer> previous = new HashMap<>();
        PriorityQueue<Integer> queue = new PriorityQueue<>(distances.values());

        for (DimensionNode node : adjacencyList) {
            if (node == null) continue;
            distances.put(node.getDimension(), 123456789);
        }

        distances.put(start, 0);
        queue.add(start);


        //System.out.println(queue.peek());
        while (!queue.isEmpty()) {
            //System.out.println("currentNode: " + queue.peek());
            int currentNode = queue.poll();


            DimensionNode currentDimensionNode = adjacencyList.get(getIndexOfDimension(adjacencyList, currentNode));


            while (currentDimensionNode != null) {
                //System.out.println("currentDimensionNode: " + currentDimensionNode.getDimension());
                int neighborNode = currentDimensionNode.getDimension();
                int newDistance = distances.get(currentNode) + currentDimensionNode.getWeight();

                if (newDistance < distances.get(neighborNode)) {
                    //System.out.println("newDistance: " + newDistance);
                    distances.put(neighborNode, newDistance);
                    previous.put(neighborNode, currentNode);
                    queue.add(neighborNode);
                }
                currentDimensionNode = currentDimensionNode.getNext();
            }
        }

        //System.out.println(previous);
        //System.out.println(queue);

        ArrayList<Integer> path = new ArrayList<>();
        Integer currentNode = end;
        while (currentNode != null) {
            path.add(currentNode);
            currentNode = previous.get(currentNode);
        }
        Collections.reverse(path);

        return path;
    }

    private static int getIndexOfDimension(ArrayList<DimensionNode> list, int dimension) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null) {
                continue;
            }
            if (list.get(i).getDimension() == dimension) {
                return i;
            }
        }
        return -1;
    }

}

