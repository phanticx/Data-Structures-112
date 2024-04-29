package spiderman;

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
         *
         * Step 2:
         * ClusterOutputFile name is passed in through the command line as args[1]
         * Output to ClusterOutputFile with the format:
         * 1. n lines, listing all of the dimension numbers connected to
         *    that dimension in order (space separated)
         *    n is the size of the cluster table.
         *
         * @author Seth Kelley
         */
        
        
import java.util.ArrayList;


public class Clusters {
    public static void main(String[] args) {
		
        if (args.length < 2) {
            System.out.println("Usage: java Clusters <input file> <output file>");
            return;
        }

        ArrayList<DimensionNode> clusterTable = getClusterTable(args[0]);

        writeTable(clusterTable, args[1]);

    }

    private static void writeTable(ArrayList<DimensionNode> clusterTable, String outputFile) {

        StdOut.setFile(outputFile);

        //write to  output file
        for (int i = 0; i < clusterTable.size(); i++) {

            DimensionNode cluster = clusterTable.get(i);

            if (cluster != null) {
                StdOut.print(cluster.getDimension());
                while (cluster.getNext() != null) {
                    cluster = cluster.getNext();
                    StdOut.print(" " + cluster.getDimension());
                }
//                for (int j = 0; j < cluster.size(); j++) {
//                    StdOut.print(" " + cluster.get(j));
//                }

                StdOut.println();
            }

        }
    }


    private static ArrayList<DimensionNode> getClusterTable(String inputFile) {

        StdIn.setFile(inputFile);

        int a = StdIn.readInt();
        int b = StdIn.readInt();
        double c = StdIn.readDouble();

        ArrayList<DimensionNode> clusterTable = new ArrayList<DimensionNode>(b);

        for (int i = 0; i < b; i++) { // add a linked list for each index in the cluster table
            clusterTable.add(null);
        }

        //System.out.println(b);
        //System.out.println(clusterTable.size());


        int dimensionsAdded = 0;
        for (int i = 0; i < a; i++) {
            int dimension = StdIn.readInt();
            int canonEvents = StdIn.readInt();
            int weight = StdIn.readInt();

            DimensionNode currentCluster = clusterTable.get(dimension % clusterTable.size());

            if (currentCluster == null) {
                clusterTable.set(dimension % clusterTable.size(), new DimensionNode(dimension, null, null, weight, canonEvents));
            } else {
                clusterTable.set(dimension % clusterTable.size(), new DimensionNode(dimension, currentCluster, null, weight, canonEvents));
            }

            dimensionsAdded++;

            if (dimensionsAdded / (double) clusterTable.size() >= c) {

                clusterTable = rehash(clusterTable);
            }
        }

        connect(clusterTable);
        return clusterTable;
    }

    public static ArrayList<DimensionNode> getCluster(String inputFile) {
        return getClusterTable(inputFile);
    }

    private static ArrayList<DimensionNode> rehash(ArrayList<DimensionNode> prev) {

        ArrayList<DimensionNode> newClusterTable = new ArrayList<DimensionNode>(prev.size() * 2);

        for (int i = 0; i < prev.size() * 2; i++) { //populate table
            newClusterTable.add(null);
        }

        for (int i = 0; i < prev.size(); i++) {
            DimensionNode cluster = prev.get(i);
		
            while (cluster != null) {
                int dimension = cluster.getDimension();
                DimensionNode currentCluster = newClusterTable.get(dimension % (prev.size() * 2));

                if (currentCluster == null) {
                    newClusterTable.set(dimension % (prev.size() * 2), new DimensionNode(dimension, null, null, cluster.getWeight(), cluster.getCanonEvents()));
                } else {
                    newClusterTable.set(dimension % (prev.size() * 2), new DimensionNode(dimension, currentCluster, null, cluster.getWeight(), cluster.getCanonEvents()));
                }

//                newClusterTable.get(dimension % (prev.size() * 2)).setDimension(dimension);
                cluster = cluster.getNext();
            }

//            for (int j = 0; j < cluster.size(); j++) {
//                newClusterTable.get(cluster.get(j) % (prev.size() * 2))
//                        .setDimension(cluster.get(j));
//
//            }
        }
		
        return newClusterTable;
    }
		

                
    private static void connect(ArrayList<DimensionNode> clusterTable) {
        ArrayList<Integer> firstDimensions = new ArrayList<>();
        for (int i = 0; i < clusterTable.size(); i++) {
            DimensionNode cluster = clusterTable.get(i);

            if (cluster != null) {
                firstDimensions.add(cluster.getDimension());
            }
//            if (!cluster.isEmpty()) {
//                firstDimensions.add(cluster.getFirst());
//            }
		
        }
		
        for (int i = 0; i < clusterTable.size(); i++) {
            DimensionNode currentCluster = clusterTable.get(i);

            int size = clusterTable.size() + i;

            int index = (size - 1) % clusterTable.size();
            int index1 = (size - 2) % clusterTable.size();

            if (firstDimensions.size() > index) {
                if (clusterTable.get(index) != null) {
                    DimensionNode temp = currentCluster;
                    while (temp.getNext() != null) {
                        temp = temp.getNext();
                    }
                    temp.setNext(new DimensionNode(firstDimensions.get(index), null, null, findNode(clusterTable, firstDimensions.get(index)).getWeight(), findNode(clusterTable, firstDimensions.get(index)).getCanonEvents()));
                }
            }

            if (firstDimensions.size() > index) {
                if (clusterTable.get(index1) != null) {
                    if (index != index1) {
                        DimensionNode temp = currentCluster;
                        while (temp.getNext() != null) {
                            temp = temp.getNext();
                        }
                        temp.setNext(new DimensionNode(firstDimensions.get(index1), null, null, findNode(clusterTable, firstDimensions.get(index1)).getWeight(), findNode(clusterTable, firstDimensions.get(index1)).getCanonEvents()));
                    }
                }
            }

        }
		
    }

    private static DimensionNode findNode(ArrayList<DimensionNode> clusterTable, int dimension) {
        for (int i = 0; i < clusterTable.size(); i++) {
            if (clusterTable.get(i) == null) {
                continue;
            }

            if (clusterTable.get(i).getDimension() == dimension) {
                return clusterTable.get(i);
            }
        }
        return null;
    }
		
}
