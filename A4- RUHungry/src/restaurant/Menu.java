package restaurant;
/**
 * Use this class to test your Menu method. 
 * This class takes in two arguments:
 * - args[0] is the menu input file
 * - args[1] is the output file
 * 
 * This class:
 * - Reads the input and output file names from args
 * - Instantiates a new RUHungry object
 * - Calls the menu() method 
 * - Sets standard output to the output and prints the restaurant
 *   to that file
 * 
 * To run: java -cp bin restaurant.Menu menu.in menu.out
 * 
 */
public class Menu {
    public static void main(String[] args) {

	// 1. Read input files
	// Option to hardcode these values if you don't want to use the command line arguments
	   
        //String inputFile = args[0];
        //String outputFile = args[1];
	
        // 2. Instantiate an RUHungry object
        RUHungry rh = new RUHungry();

        rh.menu("/Users/ethan/Downloads/2024SP/Data Structures 112/A4- RUHungry/menu.in");

        rh.createStockHashTable("/Users/ethan/Downloads/2024SP/Data Structures 112/A4- RUHungry/stock.in");
        rh.updatePriceAndProfit();
        //order(rh, "/Users/ethan/Downloads/2024SP/Data Structures 112/A4- RUHungry/order3.in");
        //donate(rh, "/Users/ethan/Downloads/2024SP/Data Structures 112/A4- RUHungry/donate1.in");
        //restock(rh, "/Users/ethan/Downloads/2024SP/Data Structures 112/A4- RUHungry/restock1.in");

        transactions(rh, "/Users/ethan/Downloads/2024SP/Data Structures 112/A4- RUHungry/transaction3.in");

        //StdOut.setFile(outputFile);

	// 5. Print restaurant
        rh.printRestaurant();
    }

    private static void order(RUHungry rh, String file) {
        StdIn.setFile(file); // opens the inputFile to be read

        class Order {
            String dishName;
            int quantity;
        }

        // WRITE YOUR CODE HERE
        int count = StdIn.readInt();
        StdIn.readLine();
        Order[] orders = new Order[count];
        for (int i = 0; i < count; i++) {
            orders[i] = new Order();
            orders[i].quantity = StdIn.readInt();
            StdIn.readChar();
            orders[i].dishName = StdIn.readLine();
        }

        for (Order o : orders) {
            System.out.println("ordered " + o.quantity + " " + o.dishName);
            rh.order(o.dishName, o.quantity);
        }

    }

    private static void donate(RUHungry rh, String file) {
        StdIn.setFile(file); // opens the inputFile to be read

        class Donation {
            String ingredientName;
            int quantity;
        }

        // WRITE YOUR CODE HERE
        int count = StdIn.readInt();
        StdIn.readLine();
        Donation[] donos = new Donation[count];
        for (int i = 0; i < count; i++) {
            donos[i] = new Donation();
            donos[i].quantity = StdIn.readInt();
            StdIn.readChar();
            donos[i].ingredientName = StdIn.readLine();
        }

        for (Donation o : donos) {
            System.out.println("donated " + o.quantity + " " + o.ingredientName);
            rh.donation(o.ingredientName, o.quantity);
        }

    }

    private static void restock(RUHungry rh, String file) {
        StdIn.setFile(file); // opens the inputFile to be read

        class Restock {
            String ingredientName;
            int quantity;
        }

        // WRITE YOUR CODE HERE
        int count = StdIn.readInt();
        StdIn.readLine();
        Restock[] stock = new Restock[count];
        for (int i = 0; i < count; i++) {
            stock[i] = new Restock();
            stock[i].quantity = StdIn.readInt();
            StdIn.readChar();
            stock[i].ingredientName = StdIn.readLine();
        }

        for (Restock o : stock) {
            System.out.println("restocked " + o.quantity + " " + o.ingredientName);
            rh.restock(o.ingredientName, o.quantity);
        }
    }

    private static void transactions(restaurant.RUHungry rh, String file) {
        StdIn.setFile(file); // opens the inputFile to be read

        class Transaction {
            String type;
            String name;
            int quantity;
        }

        // WRITE YOUR CODE HERE
        int count = StdIn.readInt();
        StdIn.readLine();
        Transaction[] transactions = new Transaction[count];
        for (int i = 0; i < count; i++) {
            transactions[i] = new Transaction();
            transactions[i].type = StdIn.readString();
            StdIn.readChar();
            transactions[i].quantity = StdIn.readInt();
            StdIn.readChar();
            transactions[i].name = StdIn.readLine();

        }

        for (Transaction t : transactions) {
            if (t.type.equals("order")) {
                rh.order(t.name, t.quantity);
            } else if (t.type.equals("donation")) {
                rh.donation(t.name, t.quantity);
            } else if (t.type.equals("restock")) {
                rh.restock(t.name, t.quantity);
            }
        }
    }
}
