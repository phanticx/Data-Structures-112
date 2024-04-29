package spiderman;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
 * ColliderOutputFile name is passed in through the command line as args[2]
 * Output to ColliderOutputFile with the format:
 * 1. e lines, each with a different dimension number, then listing
 *       all of the dimension numbers connected to that dimension (space separated)
 * 
 * @author Seth Kelley
 */

public class Collider {

    public static void main(String[] args) {

        if ( args.length < 3 ) {
            StdOut.println(
                "Execute: java -cp bin spiderman.Collider <dimension INput file> <spiderverse INput file> <collider OUTput file>");
                return;
        }


        String dimensionInputFile = args[0];
        String spiderverseInputFile = args[1];
        String colliderOutputFile = args[2];

        // Read from the DimensionsInputFile
        StdIn.setFile(dimensionInputFile);
        int a = StdIn.readInt();
        int b = StdIn.readInt();
        double c = StdIn.readDouble();

        ArrayList<DimensionNode> adjacencyList = new ArrayList<>(a);

        for (int i = 0; i < a; i++) {
            adjacencyList.add(null);
        }

        ArrayList<DimensionNode> clusterTable = spiderman.Clusters.getCluster(dimensionInputFile);

        createList(clusterTable, adjacencyList);
        ArrayList<DimensionNode> populatedAdjacencyList = populatePeople(adjacencyList, spiderverseInputFile);
        writeToFile(colliderOutputFile, populatedAdjacencyList);
    }

    public static ArrayList<DimensionNode> getAdjacencyList(String dimensionInputFile, String spiderverseInputFile) {
        StdIn.setFile(dimensionInputFile);
        int a = StdIn.readInt();
        int b = StdIn.readInt();
        double c = StdIn.readDouble();

        ArrayList<DimensionNode> adjacencyList = new ArrayList<>(a);

        for (int i = 0; i < a; i++) {
            adjacencyList.add(null);
        }

        ArrayList<DimensionNode> clusterTable = spiderman.Clusters.getCluster(dimensionInputFile);

        ArrayList<DimensionNode> populatedAdjacencyList = populatePeople(createList(clusterTable, adjacencyList), spiderverseInputFile);

        return populatedAdjacencyList;
    }

    private static ArrayList<DimensionNode> createList(ArrayList<DimensionNode> clusterTable, ArrayList<DimensionNode> adjacencyList) {
        //populate adjacencyList with dimensions
        for (int i = 0; i < clusterTable.size(); i++) {
            DimensionNode cluster = clusterTable.get(i);
            while (cluster != null) {
                int connectedDimension = cluster.getDimension();
                //System.out.println("Dimension: " + connectedDimension);

                if (!checkIfInList(adjacencyList, connectedDimension)) {
                    adjacencyList.add(new DimensionNode(connectedDimension, null, null, cluster.getWeight(), cluster.getCanonEvents()));
                }
                cluster = cluster.getNext();
            }

        }

        //populate adjacencyList with connections
        for (int i = 0; i < clusterTable.size(); i++) {
            DimensionNode cluster = clusterTable.get(i);
            cluster = cluster.getNext();
            while (cluster != null) {
                int connectedDimension = cluster.getDimension();
                int dimension = clusterTable.get(i).getDimension();
                int dimension2 = connectedDimension;

                addConnection(adjacencyList, dimension, dimension2);
                cluster = cluster.getNext();
            }
        }

        return adjacencyList;
    }

    private static ArrayList<DimensionNode> populatePeople(ArrayList<DimensionNode> adjacencyList, String spiderverseInputFile) {
        StdIn.setFile(spiderverseInputFile);
        int d = StdIn.readInt();

        for (int i = 0; i < d; i++) {
            int dimension = StdIn.readInt();
            StdIn.readChar();
            String name = StdIn.readString();
            int signature = StdIn.readInt();
            StdIn.readLine();

            DimensionNode current = adjacencyList.get(getIndexOfDimension(adjacencyList, dimension));
            if (current.getPeople() == null) {
                current.setPeople(new ArrayList<People>());
            }
            current.addPeople(new People(name, signature, dimension));
        }

        return adjacencyList;
    }


    private static void writeToFile(String colliderOutputFile, ArrayList<DimensionNode> adjacencyList) {
        StdOut.setFile(colliderOutputFile);
        for (int i = 0; i < adjacencyList.size(); i++) {
            if (adjacencyList.get(i) == null) {
                continue;
            }
            DimensionNode counter = adjacencyList.get(i);

            while (counter != null) {
                StdOut.print(counter.getDimension() + " ");
                counter = counter.getNext();
            }

            StdOut.println();
        }
    }


    private static boolean checkIfInList(ArrayList<DimensionNode> list, int dimension) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null) {
                continue;
            }
            if (list.get(i).getDimension() == dimension) {
                return true;
            }
        }
        return false;
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

    private static int getWeightOfDimension(ArrayList<DimensionNode> list, int dimension) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null) {
                continue;
            }
            if (list.get(i).getDimension() == dimension) {
                return list.get(i).getWeight();
            }
        }
        return -1;
    }

    private static int getCanonEventsOfDimension(ArrayList<DimensionNode> list, int dimension) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) == null) {
                continue;
            }
            if (list.get(i).getDimension() == dimension) {
                return list.get(i).getCanonEvents();
            }
        }
        return -1;
    }

    private static void addConnection(ArrayList<DimensionNode> list, int dimension1, int dimension2) {
        int index1 = getIndexOfDimension(list, dimension1);
        int index2 = getIndexOfDimension(list, dimension2);

        list.get(index1).addLast(new DimensionNode(dimension2, null, null, getWeightOfDimension(list, dimension2), getCanonEventsOfDimension(list, dimension2)));
        list.get(index2).addLast(new DimensionNode(dimension1, null, null, getWeightOfDimension(list, dimension1), getCanonEventsOfDimension(list, dimension1)));
    }
}