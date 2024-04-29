package spiderman;
public class People {
    private String name;
    private int signature;
    private int dimension;
    private String type;
    private int time;

    public People(String name, int signature, int dimension) {
        this.name = name;
        this.signature = signature;
        this.dimension = dimension;
    }

    public String getName() {
        return name;
    }

    public int getSignature() {
        return signature;
    }

    public int getDimension() {
        return dimension;
    }

    public void setType() {
        if (signature == dimension) { this.type = "Spider"; }
        else { this.type =  "Anomaly"; }
    }

    public String getType() {
        return type;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    @Override
    public String toString() {
        return "People{" +
                "name='" + name + '\'' +
                ", signature=" + signature +
                ", dimension=" + dimension +
                ", type='" + type + '\'' +
                ", time=" + time +
                '}';
    }
}
