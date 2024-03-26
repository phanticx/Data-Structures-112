package forensic;

import com.sun.source.tree.Tree;

/**
 * This class represents a forensic analysis system that manages DNA data using
 * BSTs.
 * Contains methods to create, read, update, delete, and flag profiles.
 * 
 * @author Kal Pandit
 */
public class ForensicAnalysis {

    private TreeNode treeRoot;            // BST's root
    private String firstUnknownSequence;
    private String secondUnknownSequence;

    public ForensicAnalysis () {
        treeRoot = null;
        firstUnknownSequence = null;
        secondUnknownSequence = null;
    }

    /**
     * Builds a simplified forensic analysis database as a BST and populates unknown sequences.
     * The input file is formatted as follows:
     * 1. one line containing the number of people in the database, say p
     * 2. one line containing first unknown sequence
     * 3. one line containing second unknown sequence
     * 2. for each person (p), this method:
     * - reads the person's name
     * - calls buildSingleProfile to return a single profile.
     * - calls insertPerson on the profile built to insert into BST.
     *      Use the BST insertion algorithm from class to insert.
     * 
     * DO NOT EDIT this method, IMPLEMENT buildSingleProfile and insertPerson.
     * 
     * @param filename the name of the file to read from
     */
    public void buildTree(String filename) {
        // DO NOT EDIT THIS CODE
        StdIn.setFile(filename); // DO NOT remove this line

        // Reads unknown sequences
        String sequence1 = StdIn.readLine();
        firstUnknownSequence = sequence1;
        String sequence2 = StdIn.readLine();
        secondUnknownSequence = sequence2;
        
        int numberOfPeople = Integer.parseInt(StdIn.readLine()); 

        for (int i = 0; i < numberOfPeople; i++) {
            // Reads name, count of STRs
            String fname = StdIn.readString();
            String lname = StdIn.readString();
            String fullName = lname + ", " + fname;
            // Calls buildSingleProfile to create
            Profile profileToAdd = createSingleProfile();
            // Calls insertPerson on that profile: inserts a key-value pair (name, profile)
            insertPerson(fullName, profileToAdd);
        }
    }

    /** 
     * Reads ONE profile from input file and returns a new Profile.
     * Do not add a StdIn.setFile statement, that is done for you in buildTree.
    */
    public Profile createSingleProfile() {

        // WRITE YOUR CODE HERE
        int length = StdIn.readInt();
        STR[] strs = new STR[length];

        for (int i = 0; i < length; i++) {
            String str = StdIn.readString();
            int count = StdIn.readInt();
            strs[i] = new STR(str, count);
        }

        Profile profile = new Profile(strs);
        return profile; // update this line
    }

    /**
     * Inserts a node with a new (key, value) pair into
     * the binary search tree rooted at treeRoot.
     * 
     * Names are the keys, Profiles are the values.
     * USE the compareTo method on keys.
     * 
     * @param newProfile the profile to be inserted
     */
    public void insertPerson(String name, Profile newProfile) {

        // WRITE YOUR CODE HERE
        TreeNode treeNode = new TreeNode(name, newProfile, null, null);
        if (treeRoot != null) { ins(treeRoot, name, newProfile); }
        else { treeRoot = treeNode; }

    }

    private static TreeNode ins(TreeNode root, String name, Profile newProfile) {
        if (root == null) {
            return new TreeNode(name, newProfile, null, null);
        } else if (name.compareTo(root.getName()) < 0) {
            root.setLeft(ins(root.getLeft(), name, newProfile));
        } else if (name.compareTo(root.getName()) > 0) {
            root.setRight(ins(root.getRight(), name, newProfile));
        }
        return root;
    }

    /**
     * Finds the number of profiles in the BST whose interest status matches
     * isOfInterest.
     *
     * @param isOfInterest the search mode: whether we are searching for unmarked or
     *                     marked profiles. true if yes, false otherwise
     * @return the number of profiles according to the search mode marked
     */
    public int getMatchingProfileCount(boolean isOfInterest) {

        // WRITE YOUR CODE HERE
        int matches = countMatches(treeRoot, isOfInterest);
        return matches; // update this line
    }

    private int countMatches(TreeNode node, boolean isOfInterest) {
        if (node == null) return 0;
        int count = 0;
        if (isOfInterest) {
            if (node.getProfile().getMarkedStatus()) {count++;}
        } else {
            if (!node.getProfile().getMarkedStatus()) {count++;}
        }
        count += countMatches(node.getLeft(), isOfInterest);
        count += countMatches(node.getRight(), isOfInterest);
        return count;
    }

    /**
     * Helper method that counts the # of STR occurrences in a sequence.
     * Provided method - DO NOT UPDATE.
     * 
     * @param sequence the sequence to search
     * @param STR      the STR to count occurrences of
     * @return the number of times STR appears in sequence
     */
    private int numberOfOccurrences(String sequence, String STR) {
        
        // DO NOT EDIT THIS CODE
        
        int repeats = 0;
        // STRs can't be greater than a sequence
        if (STR.length() > sequence.length())
            return 0;
        
            // indexOf returns the first index of STR in sequence, -1 if not found
        int lastOccurrence = sequence.indexOf(STR);
        
        while (lastOccurrence != -1) {
            repeats++;
            // Move start index beyond the last found occurrence
            lastOccurrence = sequence.indexOf(STR, lastOccurrence + STR.length());
        }
        return repeats;
    }

    /**
     * Traverses the BST at treeRoot to mark profiles if:
     * - For each STR in profile STRs: at least half of STR occurrences match (round
     * UP)
     * - If occurrences THROUGHOUT DNA (first + second sequence combined) matches
     * occurrences, add a match
     */
    public void flagProfilesOfInterest() {
        // WRITE YOUR CODE HERE
        flag(treeRoot);
    }

    private void flag(TreeNode node) {
        if (node == null) { return; }
        flag(node.getLeft());
        flag(node.getRight());
        Profile profile = node.getProfile();
        STR[] strs = profile.getStrs();
        int count = 0;
        for (STR str : strs) {
            int first = numberOfOccurrences(firstUnknownSequence, str.getStrString());
            int second = numberOfOccurrences(secondUnknownSequence, str.getStrString());
            if (str.getOccurrences() == (first + second)) { count++; }
        }

        int half = strs.length; // round up shi
        if (half % 2 == 0) half = half / 2;
        else half = half / 2 + 1;

        if (count >= half) { profile.setInterestStatus(true); }
    }

    /**
     * Uses a level-order traversal to populate an array of unmarked Strings representing unmarked people's names.
     * - USE the getMatchingProfileCount method to get the resulting array length.
     * - USE the provided Queue class to investigate a node and enqueue its
     * neighbors.
     * 
     * @return the array of unmarked people
     */
    public String[] getUnmarkedPeople() {

        // WRITE YOUR CODE HERE

        String[] unmarked = queueMarked(treeRoot, new String[getMatchingProfileCount(false)]);
        return unmarked; // update this line
    }

    private String[] queueMarked(TreeNode node, String[] unmarked) {
        Queue<TreeNode> queue = new Queue<TreeNode>();
        queue.enqueue(node);
        while (!queue.isEmpty()) {
            TreeNode tempNode = queue.dequeue();
            if (!tempNode.getProfile().getMarkedStatus()) { unmarked[nextOpenPos(unmarked)] = tempNode.getName(); }
            if (tempNode.getLeft() != null) { queue.enqueue(tempNode.getLeft()); }
            if (tempNode.getRight() != null) { queue.enqueue(tempNode.getRight()); }
        }
        return unmarked;
    }

    private int nextOpenPos(String[] unmarked) {
        for (int i = 0; i < unmarked.length; i++) {
            if (unmarked[i] == null) return i;
        }
        return 0;
    }

    /**
     * Removes a SINGLE node from the BST rooted at treeRoot, given a full name (Last, First)
     * This is similar to the BST delete we have seen in class.
     * 
     * If a profile containing fullName doesn't exist, do nothing.
     * You may assume that all names are distinct.
     * 
     * @param fullName the full name of the person to delete
     */
    public void removePerson(String fullName) {
        // WRITE YOUR CODE HERE
        if (treeRoot.getName().equals(fullName) && treeRoot.getRight() == null && treeRoot.getLeft() == null) {
            treeRoot = null; // only one root in tree and we tryna remove
            return;
        }
        deleteNode(treeRoot, fullName);
    }

    private TreeNode deleteNode(TreeNode checkNode, String fullName) {
        if (checkNode == null) return null;
        if (checkNode == treeRoot && checkNode.getName().equals(fullName)) {
            if (checkNode.getLeft() == null && checkNode.getRight() != null) {
                TreeNode temp = checkNode.getRight();
                checkNode.setRight(temp.getRight());
                checkNode.setName(temp.getName());
                checkNode.setProfile(temp.getProfile());
                return temp;
            } else if (checkNode.getRight() == null && checkNode.getLeft() != null) {
                TreeNode temp = checkNode.getLeft();
                checkNode.setLeft(temp.getLeft());
                checkNode.setName(temp.getName());
                checkNode.setProfile(temp.getProfile());
                return temp;
            }
        }
        if (fullName.compareTo(checkNode.getName()) < 0) {
            //System.out.println("left");
            checkNode.setLeft(deleteNode(checkNode.getLeft(), fullName));
        } else if (fullName.compareTo(checkNode.getName()) > 0) {
            //System.out.println("right");
            checkNode.setRight(deleteNode(checkNode.getRight(), fullName));
        } else {
            //System.out.println("found");
            if (checkNode.getLeft() == null) { return checkNode.getRight(); }
            else if (checkNode.getRight() == null) { return checkNode.getLeft(); }
            TreeNode temp = successor(checkNode);
            //System.out.println("deleted: " + checkNode.getName() + ", successor: " + temp.getName());
            checkNode.setName(temp.getName());
            checkNode.setProfile(temp.getProfile());
            checkNode.setRight(deleteNode(checkNode.getRight(), temp.getName()));
        }
        return checkNode;
    }

    private TreeNode successor(TreeNode node) {
        TreeNode current = node;
        TreeNode prev = null;
        if (current.getRight() != null) {
            //System.out.println("going right");
            prev = current;
            current = node.getRight(); }
        while (current.getLeft() != null) {
            //System.out.println("going left");
            prev = current;
            current = current.getLeft();
        }

        if (prev != node)
            prev.setLeft(current.getRight());
        else
            prev.setRight(current.getRight());

        //System.out.println("successor is " + current.getName() + ", prev is " + prev.getName());
        return current;
    }

    /**
     * Clean up the tree by using previously written methods to remove unmarked
     * profiles.
     * Requires the use of getUnmarkedPeople and removePerson.
     */
    public void cleanupTree() {
        // WRITE YOUR CODE HERE
        String[] unmarked = getUnmarkedPeople();
        for (String person : unmarked) {
            removePerson(person);
        }
    }

    /**
     * Gets the root of the binary search tree.
     *
     * @return The root of the binary search tree.
     */
    public TreeNode getTreeRoot() {
        return treeRoot;
    }

    /**
     * Sets the root of the binary search tree.
     *
     * @param newRoot The new root of the binary search tree.
     */
    public void setTreeRoot(TreeNode newRoot) {
        treeRoot = newRoot;
    }

    /**
     * Gets the first unknown sequence.
     * 
     * @return the first unknown sequence.
     */
    public String getFirstUnknownSequence() {
        return firstUnknownSequence;
    }

    /**
     * Sets the first unknown sequence.
     * 
     * @param newFirst the value to set.
     */
    public void setFirstUnknownSequence(String newFirst) {
        firstUnknownSequence = newFirst;
    }

    /**
     * Gets the second unknown sequence.
     * 
     * @return the second unknown sequence.
     */
    public String getSecondUnknownSequence() {
        return secondUnknownSequence;
    }

    /**
     * Sets the second unknown sequence.
     * 
     * @param newSecond the value to set.
     */
    public void setSecondUnknownSequence(String newSecond) {
        secondUnknownSequence = newSecond;
    }

}
