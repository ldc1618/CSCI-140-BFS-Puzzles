package puzzles.tipover;

import puzzles.tipover.model.TipOverConfig;
import solver.Solver;

import java.io.*;
import java.util.LinkedList;
import java.util.*;

/**
 * Main class for the Tip Over puzzle.
 *
 * @author Luke Chelius
 * November 2021
 */
public class TipOver {

    /**
     * The number of rows in the game board.
     */
    private int numRows;

    /**
     * The number of columns in the game board.
     */
    private int numCols;

    /**
     * The starting location of the tipper.
     */
    private int[] startCoord;

    /**
     * The ending location, goal, of the tipper.
     */
    private int[] endCoord;

    /**
     * The starting setup of the game board.
     */
    private String[][] startBoard;

    /**
     * Creates a new TipOver object.
     * @param numRows the number of rows in the board
     * @param numCols the number of columns in the board
     * @param startCoord the starting location of the tipper
     * @param endCoord the goal, ending location of the tipper
     * @param startBoard the initial setup of the board
     */
    public TipOver(int numRows, int numCols, int[] startCoord, int[] endCoord, String[][] startBoard) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.startCoord = startCoord;
        this.endCoord = endCoord;
        this.startBoard = startBoard;
    }

    /**
     * Returns the number of rows in the board.
     * @return the number of rows in the board
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Returns the number of columns in the board.
     * @return the number of columns in the board
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Returns the starting location of the tipper.
     * @return the tipper's starting spot
     */
    public int[] getStartCoord() {
        return startCoord;
    }

    /**
     * Returns the goal, which is the ending location of the tipper.
     * @return the tippers ending spot, the goal of the puzzle
     */
    public int[] getEndCoord() {
        return endCoord;
    }

    /**
     * Returns the starting setup of the board, where all towers and crates are initially placed.
     * @return the starting setup of the board
     */
    public String[][] getStartBoard() {
        return startBoard;
    }

    /*
     * code to read the file name from the command line and
     * run the solver on the puzzle
     */
    public static void main(String[] args) throws FileNotFoundException {
        // Displays error message if command line args are incorrect
        if ( args.length != 1 ) {
            System.out.println("Usage: java TipOver file");
        }
        else {
            // Creates a scanner object to read the input from the file
            try (Scanner in = new Scanner(new File(args[0]))) {
                String next = in.nextLine();  // Reads the next line
                String[] line = next.split(" ");  // Splits the next line
                int rows = Integer.parseInt(line[0]);  // Gets the number of rows
                int cols = Integer.parseInt(line[1]);  // Gets the number of columns
                int[] start = {Integer.parseInt(line[2]), Integer.parseInt(line[3])};  // Gets the starting location
                int[] end = {Integer.parseInt(line[4]), Integer.parseInt(line[5])};  // Gets the ending location

                String[][] grid = new String[rows][cols];  // Empty 2D array to store the game board initial setup

                // Loops through the file for each row of the board needed
                for (int i = 0; i < rows; i++) {
                    next = in.nextLine();  // Gets the next line from the file
                    line = next.split(" ");  // Splits the next line

                    // Copies the array from the file to the game board at the correct spot
                    System.arraycopy(line, 0, grid[i], 0, grid[i].length);
                }

                TipOver tipOver = new TipOver(rows, cols, start, end, grid);  // Creates new TipOver object

                // Creates a new solver with a TipOverConfig of the puzzle, staring board, and tipper starting spot
                Solver<TipOverConfig> solution = new Solver<>(new TipOverConfig(tipOver, grid, start));
                LinkedList<TipOverConfig> path = solution.solve(true);  // Stores the solution path in a LinkedList

                // If the path is null no solution was found
                if (path == null) {
                    System.out.println("No Solution");
                }
                // Otherwise, print the steps in order
                else {
                    int step = 0;  // Counter for what step its on
                    for (TipOverConfig config : path) {
                        System.out.println("Step " + step + ": \n" + config);  // Prints the step
                        step++;  // Increments to next step
                    }
                }
            }
            catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }
        }
    }
}
