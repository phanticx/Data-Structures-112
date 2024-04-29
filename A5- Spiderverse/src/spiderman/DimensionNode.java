package spiderman;
import java.util.ArrayList;

public class DimensionNode {
    private int dimension;
    private DimensionNode next;
    private ArrayList<People> people;
    private int weight;
    private int canonEvents;

    public DimensionNode(int dimension) {
        this.dimension = dimension;
        this.next = null;
        this.people = new ArrayList<People>();
        this.weight = 0;
        this.canonEvents = 0;
    }

    public DimensionNode() {
        this.dimension = 0;
        this.next = null;
        this.people = new ArrayList<People>();
        this.weight = 0;
        this.canonEvents = 0;
    }

    public DimensionNode(int dimension, DimensionNode next, ArrayList<People> people, int weight, int canonEvents) {
        this.dimension = dimension;
        this.next = next;
        this.people = people;
        this.weight = weight;
        this.canonEvents = canonEvents;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCanonEvents() {
        return canonEvents;
    }

    public void setCanonEvents(int canonEvents) {
        this.canonEvents = canonEvents;
    }

    public int setDimension(int dimension) {
        return this.dimension = dimension;
    }

    public int getDimension() {
        return dimension;
    }

    public DimensionNode getNext() {
        return next;
    }

    public void setNext(DimensionNode next) {
        this.next = next;
    }

    public ArrayList<People> getPeople() {
        return people;
    }

    public void addPeople(People person) {
        people.add(person);
    }

    public void removePeople(People person) {
        people.remove(person);
    }

    public void removePeople(int index) {
        people.remove(index);
    }

    public int getPeopleSize() {
        return people.size();
    }

    public People getPeople(int index) {
        return people.get(index);
    }

    public void setPeople(ArrayList<People> people) {
        this.people = people;
    }
    public void setPeople(int index, People person) {
        people.set(index, person);
    }

    public void clearPeople() {
        people.clear();
    }


    public String toString() {
        return "Dimension: " + dimension + " Next: " + next + " People: " + people;
    }

    public boolean equals(Object obj) {
        if (obj instanceof DimensionNode) {
            DimensionNode other = (DimensionNode) obj;
            return dimension == other.dimension;
        }
        return false;
    }


    public void addLast(DimensionNode node) {
        if (next == null) {
            next = node;
        } else {
            next.addLast(node);
        }
    }

}
