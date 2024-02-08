package conwaygame;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import java.io.File;
import java.io.FileNotFoundException;
/**
 * Conway's Game of Life Class holds various methods that will
 * progress the state of the game's board through it's many iterations/generations.
 *
 * Rules 
 * Alive cells with 0-1 neighbors die of loneliness.
 * Alive cells with >=4 neighbors die of overpopulation.
 * Alive cells with 2-3 neighbors survive.
 * Dead cells with exactly 3 neighbors become alive by reproduction.

 * @author Seth Kelley 
 * @author Maxwell Goldberg
 */
public class GameOfLife {

    // Instance variables
    private static final boolean ALIVE = true;
    private static final boolean  DEAD = false;

    private boolean[][] grid;    // The board has the current generation of cells
    private int totalAliveCells; // Total number of alive cells in the grid (board)

    /**
    * Default Constructor which creates a small 5x5 grid with five alive cells.
    * This variation does not exceed bounds and dies off after four iterations.
    */
    public GameOfLife() {
        grid = new boolean[5][5];
        totalAliveCells = 5;
        grid[1][1] = ALIVE;
        grid[1][3] = ALIVE;
        grid[2][2] = ALIVE;
        grid[3][2] = ALIVE;
        grid[3][3] = ALIVE;
    }

    /**
    * Constructor used that will take in values to create a grid with a given number
    * of alive cells
    * @param file is the input file with the initial game pattern formatted as follows:
    * An integer representing the number of grid rows, say r
    * An integer representing the number of grid columns, say c
    * Number of r lines, each containing c true or false values (true denotes an ALIVE cell)
    */
    public GameOfLife (String file) {
        try { 
            Scanner scan = new Scanner(new File(file));
            int r = scan.nextInt();
            int c = scan.nextInt();
            grid = new boolean[r][c];
            scan.nextLine();
            for (int j = 0; j < r; j++) {
                String a = scan.nextLine().replaceAll("\\s","");
                String[] row = a.split("e");
                System.out.println(a);
                for (int i = 0; i < row.length; i++) {
                    if (row[i].equalsIgnoreCase("tru")) {
                        grid[j][i] = ALIVE;
                    } else {
                        grid[j][i] = DEAD;
                    }
                }
                System.out.println(j);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    /**
     * Returns grid
     * @return boolean[][] for current grid
     */
    public boolean[][] getGrid () {
        return grid;
    }
    
    /**
     * Returns totalAliveCells
     * @return int for total number of alive cells in grid
     */
    public int getTotalAliveCells () {
        return totalAliveCells;
    }

    /**
     * Returns the status of the cell at (row,col): ALIVE or DEAD
     * @param row row position of the cell
     * @param col column position of the cell
     * @return true or false value "ALIVE" or "DEAD" (state of the cell)
     */
    public boolean getCellState (int row, int col) {
        return grid[row][col]; // update this line, provided so that code compiles
    }

    /**
     * Returns true if there are any alive cells in the grid
     * @return true if there is at least one cell alive, otherwise returns false
     */
    public boolean isAlive () {
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == ALIVE)
                    return true;
            }
        }
        return false; // update this line, provided so that code compiles
    }

    /**
     * Determines the number of alive cells around a given cell.
     * Each cell has 8 neighbor cells which are the cells that are 
     * horizontally, vertically, or diagonally adjacent.
     * 
     * @param col column position of the cell
     * @param row row position of the cell
     * @return neighboringCells, the number of alive cells (at most 8).
     */
    public int numOfAliveNeighbors (int row, int col) {
        System.out.println(row +" "+ col);
        int count = 0;
        int[] adjustedRows = new int[3];
        int[] adjustedCols = new int[3];
        if (row == 0) {
            adjustedRows[0] = row;
            adjustedRows[1] = row + 1;
            adjustedRows[2] = grid.length-1;
        } else if (row == grid.length-1) {
            adjustedRows[0] = 0;
            adjustedRows[1] = row - 1;
            adjustedRows[2] = row;
        } else {
            adjustedRows[0] = row - 1;
            adjustedRows[1] = row;
            adjustedRows[2] = row + 1;
        }

        if (col == 0) {
            adjustedCols[0] = col;
            adjustedCols[1] = col + 1;
            adjustedCols[2] = grid[0].length-1;
        } else if (col == grid[0].length-1) {
            adjustedCols[0] = 0;
            adjustedCols[1] = col - 1;
            adjustedCols[2] = col;
        } else {
            adjustedCols[0] = col - 1;
            adjustedCols[1] = col;
            adjustedCols[2] = col + 1;
        }

        for (int r : adjustedRows) {
            for (int c : adjustedCols) {
                if (r == row && c == col) {
                    continue;
                }
                if (grid[r][c] == ALIVE) {
                    count++;
                }
            }
        }

        // WRITE YOUR CODE HERE
        return count; // update this line, provided so that code compiles
    }

    /**
     * Creates a new grid with the next generation of the current grid using 
     * the rules for Conway's Game of Life.
     * 
     * @return boolean[][] of new grid (this is a new 2D array)
     */
    public boolean[][] computeNewGrid () {
        boolean[][] newGrid = new boolean[grid.length][grid[0].length];
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                newGrid[r][c] = DEAD;
                if (grid[r][c] == ALIVE) {
                    if (numOfAliveNeighbors(r, c) == 2 || numOfAliveNeighbors(r, c) == 3) {
                        newGrid[r][c] = ALIVE;
                    }
                } else
                if (grid[r][c] == DEAD) {
                    if (numOfAliveNeighbors(r, c) == 3) {
                        newGrid[r][c] = ALIVE;
                    }
                }
            }
        }

        // WRITE YOUR CODE HERE
        return newGrid;// update this line, provided so that code compiles
    }

    /**
     * Updates the current grid (the grid instance variable) with the grid denoting
     * the next generation of cells computed by computeNewGrid().
     * 
     * Updates totalAliveCells instance variable
     */
    public void nextGeneration () {
        this.grid = computeNewGrid();
        int count = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == ALIVE)
                    count++;
            }

        }
        this.totalAliveCells = count;
    }

    /**
     * Updates the current grid with the grid computed after multiple (n) generations. 
     * @param n number of iterations that the grid will go through to compute a new grid
     */
    public void nextGeneration (int n) {
        for (int i = 0; i < n; i++) {
            nextGeneration();
        }
        // WRITE YOUR CODE HERE
    }

    /**
     * Determines the number of separate cell communities in the grid
     * @return the number of communities in the grid, communities can be formed from edges
     */
    public int numOfCommunities() {
        WeightedQuickUnionUF wf = new WeightedQuickUnionUF(grid.length, grid[0].length);
        ArrayList<Integer> communityRoots = new ArrayList<>();
        for (int row = 0; row < grid.length; row++) {
            for (int col = 0; col < grid[0].length; col++) {
               if (grid[row][col] == ALIVE){
                   int[] adjustedRows = new int[3];
                   int[] adjustedCols = new int[3];
                   if (row == 0) {
                       adjustedRows[0] = row;
                       adjustedRows[1] = row + 1;
                       adjustedRows[2] = grid.length-1;
                   } else if (row == grid.length-1) {
                       adjustedRows[0] = 0;
                       adjustedRows[1] = row - 1;
                       adjustedRows[2] = row;
                   } else {
                       adjustedRows[0] = row - 1;
                       adjustedRows[1] = row;
                       adjustedRows[2] = row + 1;
                   }

                   if (col == 0) {
                       adjustedCols[0] = col;
                       adjustedCols[1] = col + 1;
                       adjustedCols[2] = grid[0].length-1;
                   } else if (col == grid[0].length-1) {
                       adjustedCols[0] = 0;
                       adjustedCols[1] = col - 1;
                       adjustedCols[2] = col;
                   } else {
                       adjustedCols[0] = col - 1;
                       adjustedCols[1] = col;
                       adjustedCols[2] = col + 1;
                   }

                   for (int row1 : adjustedRows) {
                       for (int col1 : adjustedCols) {
                           if (row1 == row && col1 == col) {
                               continue;
                           }
                           if (grid[row1][col1] == ALIVE) {
                               wf.union(row, col, row1, col1);
                           }
                       }
                   }
               }

                /*if (grid[r][c] == ALIVE) {
                    if (r < grid.length - 1 && grid[r][c] == grid[r + 1][c])
                        wf.union(r, c, r + 1, c);
                    if (c < grid[0].length - 1 && grid[r][c] == grid[r][c + 1])
                        wf.union(r, c, r, c + 1);
                    if (c > 0 && grid[r][c] == grid[r][c - 1])
                        wf.union(r, c, r, c - 1);
                    if (r > 0 && grid[r][c] == grid[r-1][c])
                        wf.union(r, c, r - 1, c );
                    if (r < grid.length - 1 && c < grid[0].length - 1 && grid[r][c] == grid[r + 1][c + 1])
                        wf.union(r, c, r + 1, c + 1);
                    if (r > 0 && c < grid[0].length - 1 && grid[r][c] == grid[r - 1][c + 1])
                        wf.union(r, c, r - 1, c + 1);
                    if (r > 0 && c > 0 && grid[r][c] == grid[r - 1][c - 1])
                        wf.union(r, c, r - 1, c - 1);
                    if (r < grid.length - 1 && c > 0 && grid[r][c] == grid[r + 1][c - 1])
                        wf.union(r, c, r + 1, c - 1);
                }*/
            }
        }

        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] == ALIVE && !communityRoots.contains(wf.find(r, c))) {
                    System.out.println("find: " + r + " " + c + ", resultant root: " + wf.find(r, c));
                    communityRoots.add(wf.find(r, c));
                }

            }
        }
        // WRITE YOUR CODE HERE
        return communityRoots.size(); // update this line, provided so that code compiles
    }

}
