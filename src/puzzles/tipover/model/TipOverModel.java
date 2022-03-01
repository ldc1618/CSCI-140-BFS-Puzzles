package puzzles.tipover.model;

import puzzles.tipover.TipOver;

import solver.Solver;
import util.Observer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * DESCRIPTION
 * @author Luke Chelius
 * November 2021
 */
public class TipOverModel {

    /**
     * The configuration of the puzzle.
     */
    private TipOverConfig currentConfig;

    /**
     * List of objects observing every done to this object.
     */
    private final List<Observer<TipOverModel, Object>> observers = new LinkedList<>();

    /*
     * Code here includes...
     * Additional data variables for anything needed beyond what is in
     *   the config object to describe the current state of the puzzle
     * Methods to support the controller part of the GUI, e.g., load, move
     * Methods and data to support the "subject" side of the Observer pattern
     *
     * WARNING: To support the hint command, you will likely have to do
     *   a cast of Config to TipOverConfig somewhere, since the solve
     *   method works with, and returns, objects of type Configuration.
     */

    /**
     * Creates a TipOverModel object that passes information about a TipOverConfig to the UI's.
     * @param file the file to read the config from
     * @throws FileNotFoundException thrown if an invalid file is read
     */
    public TipOverModel(String file) throws FileNotFoundException {
        try (Scanner in = new Scanner(new File(file))) {
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
            this.currentConfig = new TipOverConfig(tipOver, grid, start);
        }
        this.notifyObservers();  // Notifies the observers of the creation of a new model
    }

    /**
     * Creates a new TipOverModel with a new puzzle file.
     * @param file the file to read the new config from
     * @return the new config
     * @throws FileNotFoundException thrown if invalid file is read
     */
    public TipOverModel load(String file) throws FileNotFoundException {
        TipOverModel temp = new TipOverModel(file);
        this.notifyObservers();
        return temp;
    }

    /**
     * Adds an observer to the list of observers.
     * @param observer the observer to add
     */
    public void addObserver(Observer<TipOverModel, Object> observer) {
        observers.add(observer);
    }

    /**
     * Notifies the observers of any changes made to the model and updates them.
     */
    private void notifyObservers() {
        for (Observer<TipOverModel, Object> observer : observers) {
            observer.update(this, null);
        }
    }

    /**
     * Returns the configuration of the model.
     * @return the model's config
     */
    public TipOverConfig getCurrentConfig() {
        return currentConfig;
    }

    /**
     * Moves the tipper in a specified direction if it is able to move in that direction.
     * @param direction the direction to move ('north', 'south', 'east', or 'west')
     * @return a boolean, true if it is a valid move, false otherwise
     */
    public boolean[] move(String direction) {
        boolean validMove = false;  // Boolean initially set to false
        boolean tipped = false;  // Boolean for if a tower was tipped
        int[] oldPos = currentConfig.getCurrentPos();
        switch (direction) {
            case "north" -> {
                TipOverConfig north = currentConfig.getNorth();  // Gets the move north
                // If it isn't null then it's a valid move and sets the config to it
                if (north != null) {
                    validMove = true;
                    if (north.getBoard()[oldPos[0]][oldPos[1]].equals("0")) {
                        tipped = true;
                    }
                    currentConfig = north;
                }
            }
            case "south" -> {
                TipOverConfig south = currentConfig.getSouth();  // Gets the move south
                // If it isn't null then it's a valid move and sets the config to it
                if (south != null) {
                    validMove = true;
                    if (south.getBoard()[oldPos[0]][oldPos[1]].equals("0")) {
                        tipped = true;
                    }
                    currentConfig = south;
                }
            }
            case "east" -> {
                TipOverConfig east = currentConfig.getEast();  // Gets the move east
                // If it isn't null then it's a valid move and sets the config to it
                if (east != null) {
                    validMove = true;
                    if (east.getBoard()[oldPos[0]][oldPos[1]].equals("0")) {
                        tipped = true;
                    }
                    currentConfig = east;
                }
            }
            case "west" -> {
                TipOverConfig west = currentConfig.getWest();  // Gets the move west
                // If it isn't null then it's a valid move and sets the config to it
                if (west != null) {
                    validMove = true;
                    if (west.getBoard()[oldPos[0]][oldPos[1]].equals("0")) {
                        tipped = true;
                    }
                    currentConfig = west;
                }
            }
            default -> {
                this.notifyObservers();  // Notifies the observers
                return new boolean[]{false, false};
            }
        }
        this.notifyObservers();  // Notifies the observers
        return new boolean[]{validMove, tipped};
    }

    /**
     * Gets the next move in the shortest path to the solution if it exists.
     * @return The next config in the solution as a string, or a string saying it is unsolvable
     */
    public boolean[] hint() {
        Solver<TipOverConfig> hint = new Solver<>(currentConfig);  // Creates a solver
        LinkedList<TipOverConfig> path = hint.solve(false);  // Gets the solution path from the solver
        // If the path is null it is unsolvable, so return that
        if (path == null) {
            return new boolean[]{false, false};
        }
        // Otherwise, return the next config in the path as a string
        boolean tipped = false;
        int[] oldPos = currentConfig.getCurrentPos();
        TipOverConfig newConfig = path.get(1);
        if (newConfig.getBoard()[oldPos[0]][oldPos[1]].equals("0")) {
            tipped = true;
        }
        currentConfig = newConfig;
        this.notifyObservers();  // Notifies the observers
        return new boolean[]{true, tipped};
    }
}
