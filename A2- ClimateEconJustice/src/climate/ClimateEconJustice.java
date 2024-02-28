package climate;

import java.util.ArrayList;

/**
 * This class contains methods which perform various operations on a layered 
 * linked list structure that contains USA communitie's Climate and Economic information.
 * 
 * @author Navya Sharma
 */

public class ClimateEconJustice {

    private StateNode firstState;
    
    /*
    * Constructor
    * 
    * **** DO NOT EDIT *****
    */
    public ClimateEconJustice() {
        firstState = null;
    }

    /*
    * Get method to retrieve instance variable firstState
    * 
    * @return firstState
    * 
    * **** DO NOT EDIT *****
    */ 
    public StateNode getFirstState () {
        // DO NOT EDIT THIS CODE
        return firstState;
    }

    /**
     * Creates 3-layered linked structure consisting of state, county, 
     * and community objects by reading in CSV file provided.
     * 
     * @param inputFile, the file read from the Driver to be used for
     * @return void
     * 
     * **** DO NOT EDIT *****
     */
    public void createLinkedStructure ( String inputFile ) {
        
        // DO NOT EDIT THIS CODE
        StdIn.setFile(inputFile);
        StdIn.readLine();
        
        // Reads the file one line at a time
        while ( StdIn.hasNextLine() ) {
            // Reads a single line from input file
            String line = StdIn.readLine();
            // IMPLEMENT these methods
            addToStateLevel(line);
            addToCountyLevel(line);
            addToCommunityLevel(line);
        }
    }

    /*
    * Adds a state to the first level of the linked structure.
    * Do nothing if the state is already present in the structure.
    * 
    * @param inputLine a line from the input file
    */
    public void addToStateLevel ( String inputLine ) {
        // WRITE YOUR CODE HERE

        String[] data = inputLine.split(",");
        StateNode ptr = firstState;

        if (ptr == null) {
            firstState = new StateNode(data[2], null, null);
            return;
        }

        while (ptr.next != null) {
            if (ptr.name.equals(data[2])) {
                return;
            }
            ptr = ptr.next;
        }

        if (ptr.name.equals(data[2])) {
            return;
        }

        ptr.next = new StateNode(data[2], null, null);
    }

    /*
    * Adds a county to a state's list of counties.
    * 
    * Access the state's list of counties' using the down pointer from the State class.
    * Do nothing if the county is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCountyLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String[] data = inputLine.split(",");
        StateNode ptr = firstState;

        if (ptr == null) { return; }
        if (ptr.down == null) {
            ptr.down = new CountyNode(data[1], null, null);
            return;
        }

        while (ptr.next != null) {
            ptr = ptr.next;
        }

        CountyNode ptr2 = ptr.down;

        if (ptr2 == null) {
            ptr.down = new CountyNode(data[1], null, null);
            return;
        }

        while (ptr2.next != null) {
            if (ptr2.name.equals(data[1])) {
                return;
            }
            ptr2 = ptr2.next;
        }

        if (ptr2.name.equals(data[1])) {
            return;
        }

        ptr2.next = new CountyNode(data[1], null, null);

    }

    /*
    * Adds a community to a county's list of communities.
    * 
    * Access the county through its state
    *      - search for the state first, 
    *      - then search for the county.
    * Use the state name and the county name from the inputLine to search.
    * 
    * Access the state's list of counties using the down pointer from the StateNode class.
    * Access the county's list of communities using the down pointer from the CountyNode class.
    * Do nothing if the community is already present in the structure.
    * 
    * @param inputFile a line from the input file
    */
    public void addToCommunityLevel ( String inputLine ) {

        // WRITE YOUR CODE HERE
        String[] data = inputLine.split(",");
        StateNode ptr = firstState;

        if (ptr == null) { return; }
        if (ptr.down == null) { return; }

        while (ptr.next != null) {
            ptr = ptr.next;
        }

        CountyNode ptr2 = ptr.down;

        while (ptr2.next != null) {
            ptr2 = ptr2.next;
        }

        CommunityNode ptr3 = ptr2.down;

        if (ptr3 == null) {
            ptr2.down = new CommunityNode(data[0], null, new Data(
                    Double.parseDouble(data[3]),
                    Double.parseDouble(data[4]),
                    Double.parseDouble(data[5]),
                    Double.parseDouble(data[8]),
                    Double.parseDouble(data[9]),
                    data[19],
                    Double.parseDouble(data[49]),
                    Double.parseDouble(data[37]),
                    Double.parseDouble(data[121])));
            return;
        }

        while (ptr3.next != null) {
            if (ptr3.name.equals(data[0])) {
                return;
            }
            ptr3 = ptr3.next;
        }

        if (ptr3.name.equals(data[0])) {
            return;
        }

        ptr3.next = new CommunityNode(data[0], null, new Data(
                Double.parseDouble(data[3]),
                Double.parseDouble(data[4]),
                Double.parseDouble(data[5]),
                Double.parseDouble(data[8]),
                Double.parseDouble(data[9]),
                data[19],
                Double.parseDouble(data[49]),
                Double.parseDouble(data[37]),
                Double.parseDouble(data[121])));

    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int disadvantagedCommunities ( double userPrcntage, String race ) {

        // WRITE YOUR CODE HERE
        StateNode ptr = firstState;
        int count = 0;
        double convertedPct = userPrcntage / 100.0;

        if (ptr != null) {

            while (ptr != null) {
                CountyNode ptr2 = ptr.down;

                if (ptr2 != null) {
                    while (ptr2 != null) {
                        CommunityNode ptr3 = ptr2.down;

                        if (ptr3 != null) {
                            while (ptr3 != null) {

                                switch(race) {
                                    case "African American":
                                        if (ptr3.info.getPrcntAfricanAmerican() >= convertedPct && ptr3.info.disadvantaged.equals("True")) {
                                            count++;
                                        }
                                        break;

                                    case "Native American":
                                        if (ptr3.info.getPrcntNative() >= convertedPct && ptr3.info.disadvantaged.equals("True")) {
                                            count++;
                                        }
                                        break;

                                    case "Asian American":
                                        if (ptr3.info.getPrcntAsian() >= convertedPct && ptr3.info.disadvantaged.equals("True")) {
                                            count++;
                                        }
                                        break;

                                    case "White American":
                                        if (ptr3.info.getPrcntWhite() >= convertedPct && ptr3.info.disadvantaged.equals("True")) {
                                            count++;
                                        }
                                        break;

                                    case "Hispanic American":
                                        if (ptr3.info.getPrcntHispanic() >= convertedPct && ptr3.info.disadvantaged.equals("True")) {
                                            count++;
                                        }
                                        break;

                                    default:
                                        break;
                                }


                                ptr3 = ptr3.next;
                            }
                        }
                        ptr2 = ptr2.next;
                    }
                }
                ptr = ptr.next;
            }
        }

        return count; // replace this line
    }

    /**
     * Given a certain percentage and racial group inputted by user, returns
     * the number of communities that have that said percentage or more of racial group  
     * and are identified as non disadvantaged
     * 
     * Percentages should be passed in as integers for this method.
     * 
     * @param userPrcntage the percentage which will be compared with the racial groups
     * @param race the race which will be returned
     * @return the amount of communities that contain the same or higher percentage of the given race
     */
    public int nonDisadvantagedCommunities ( double userPrcntage, String race ) {

        // WRITE YOUR CODE HERE
        StateNode ptr = firstState;
        int count = 0;
        double convertedPct = userPrcntage / 100.0;

        if (ptr != null) {

            while (ptr != null) {
                CountyNode ptr2 = ptr.down;

                if (ptr2 != null) {
                    while (ptr2 != null) {
                        CommunityNode ptr3 = ptr2.down;

                        if (ptr3 != null) {
                            while (ptr3 != null) {

                                switch(race) {
                                    case "African American":
                                        if (ptr3.info.getPrcntAfricanAmerican() >= convertedPct && ptr3.info.disadvantaged.equals("False")) {
                                            count++;
                                        }
                                        break;

                                    case "Native American":
                                        if (ptr3.info.getPrcntNative() >= convertedPct && ptr3.info.disadvantaged.equals("False")) {
                                            count++;
                                        }
                                        break;

                                    case "Asian American":
                                        if (ptr3.info.getPrcntAsian() >= convertedPct && ptr3.info.disadvantaged.equals("False")) {
                                            count++;
                                        }
                                        break;

                                    case "White American":
                                        if (ptr3.info.getPrcntWhite() >= convertedPct && ptr3.info.disadvantaged.equals("False")) {
                                            count++;
                                        }
                                        break;

                                    case "Hispanic American":
                                        if (ptr3.info.getPrcntHispanic() >= convertedPct && ptr3.info.disadvantaged.equals("False")) {
                                            count++;
                                        }
                                        break;

                                    default:
                                        break;
                                }


                                ptr3 = ptr3.next;
                            }
                        }
                        ptr2 = ptr2.next;
                    }
                }
                ptr = ptr.next;
            }
        }

        return count; // replace this line
    }
    
    /** 
     * Returns a list of states that have a PM (particulate matter) level
     * equal to or higher than value inputted by user.
     * 
     * @param PMlevel the level of particulate matter
     * @return the States which have or exceed that level
     */ 
    public ArrayList<StateNode> statesPMLevels ( double PMlevel ) {

        StateNode ptr = firstState;
        ArrayList<StateNode> states = new ArrayList<StateNode>();

        if (ptr != null) {

            while (ptr != null) {
                CountyNode ptr2 = ptr.down;

                if (ptr2 != null) {
                    while (ptr2 != null) {
                        CommunityNode ptr3 = ptr2.down;

                        if (ptr3 != null) {
                            while (ptr3 != null) {

                                if (ptr3.info.getPMlevel() >= PMlevel) {

                                    boolean found = false;
                                    for (StateNode state : states) {
                                        if (state.equals(ptr)) {
                                            found = true;
                                        }
                                    }
                                    if (!found) {states.add(ptr);}
                                }

                                ptr3 = ptr3.next;
                            }
                        }
                        ptr2 = ptr2.next;
                    }
                }
                ptr = ptr.next;
            }
        }

        return states; // replace this line
    }

    /**
     * Given a percentage inputted by user, returns the number of communities 
     * that have a chance equal to or higher than said percentage of
     * experiencing a flood in the next 30 years.
     * 
     * @param userPercntage the percentage of interest/comparison
     * @return the amount of communities at risk of flooding
     */
    public int chanceOfFlood ( double userPercntage ) {

        StateNode ptr = firstState;
        int count = 0;

        if (ptr != null) {

            while (ptr != null) {
                CountyNode ptr2 = ptr.down;

                if (ptr2 != null) {
                    while (ptr2 != null) {
                        CommunityNode ptr3 = ptr2.down;

                        if (ptr3 != null) {
                            while (ptr3 != null) {

                                if (ptr3.info.getChanceOfFlood() >= userPercntage) {
                                    count++;
                                }

                                ptr3 = ptr3.next;
                            }
                        }
                        ptr2 = ptr2.next;
                    }
                }
                ptr = ptr.next;
            }
        }

        return count; // replace this line
    }

    /** 
     * Given a state inputted by user, returns the communities with 
     * the 10 lowest incomes within said state.
     * 
     *  @param stateName the State to be analyzed
     *  @return the top 10 lowest income communities in the State, with no particular order
    */
    public ArrayList<CommunityNode> lowestIncomeCommunities ( String stateName ) {

        ArrayList<CommunityNode> communities = new ArrayList<CommunityNode>();
        StateNode ptr = firstState;

        while (ptr.next != null && !ptr.name.equals(stateName)) {
            ptr = ptr.next;
        }

        CountyNode ptr2 = ptr.down;

        if (ptr2 != null) {
            while (ptr2 != null) {
                CommunityNode ptr3 = ptr2.down;

                if (ptr3 != null) {
                    while (ptr3 != null) {

                        communities = updateList(communities, ptr3);
                        ptr3 = ptr3.next;
                    }
                }

                ptr2 = ptr2.next;
            }
        }


        return communities; // replace this line
    }

    private static ArrayList<CommunityNode> updateList(ArrayList<CommunityNode> list, CommunityNode node) {
        if (list.size() < 10) {
            list.add(node);
        } else {
            CommunityNode lowestPov = list.get(0);
            int lowestPovIndex = 0;
            for (int i = 0; i < list.size(); i++) { //sets lowestPov to lowest income community
                if (list.get(i).info.getPercentPovertyLine() < lowestPov.info.getPercentPovertyLine()) {
                    lowestPov = list.get(i);
                    lowestPovIndex = i;
                }
            }

            if (lowestPov.info.getPercentPovertyLine() < node.info.getPercentPovertyLine()) {
                list.set(lowestPovIndex, node);
            }
        }
        return list;
    }
}
    
