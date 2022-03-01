package puzzles.tipover.model;

import puzzles.tipover.TipOver;
import solver.*;
import java.util.LinkedList;

/**
 * Configuration for the TipOver puzzle for the solver algorithm to use.
 *
 * @author Luke Chelius
 * November 2021
 */
public class TipOverConfig implements Configuration{

    /**
     * the TipOver puzzle the configuration is for.
     */
    private TipOver tipOver;

    /**
     * The current layout of the board, showing which spaces are occupied by what height towers/crates.
     */
    private String[][] board;

    /**
     * The current position of the tipper given in [row, col] format.
     */
    private int[] currentPos;

    /**
     * Creates a new tip over configuration for a tip over puzzle with a particular game board and location of the
     * tipper on the game board.
     * @param tipOver the TipOver puzzle the configuration is for
     * @param board the location of the different towers/crates for this particular config
     * @param currentPos the coordinates of the tipper on the board
     */
    public TipOverConfig(TipOver tipOver, String[][] board, int[] currentPos) {
        this.tipOver = tipOver;
        this.board = board;
        this.currentPos = currentPos;
    }

    /**
     * Creates a new tip over configuration for a tip over puzzle with a particular game board and location of the
     * tipper on the game board. This is a copy constructor, takes another configuration and makes a deep copy of it.
     * @param other the TipOverConfig to make a copy of
     */
    public TipOverConfig(TipOverConfig other) {
        this.tipOver = other.tipOver;
        this.board = new String[other.board.length][other.board[0].length];  // Initializes board to right size

        // Copies each row in the old board to the new board
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(other.board[i], 0, this.board[i], 0, this.board[i].length);
        }

        this.currentPos = new int[2];  // Initializes currentPos to the right size
        // Copies the old currentPos to the new one
        System.arraycopy(other.currentPos, 0, this.currentPos, 0, this.currentPos.length);
    }

    /**
     * Returns the current position of the tipper.
     * @return the tipper's current position
     */
    public int[] getCurrentPos() {
        return currentPos;
    }

    /**
     * Returns the board of the configuration, a 2D array of strings.
     * @return the config's board
     */
    public String[][] getBoard() {
        return board;
    }

    /**
     * Returns the start configuration for this puzzle, how the board looked at the beginning
     * @return the starting configuration of the board
     */
    public Object getStart() {
        return tipOver.getStartBoard();
    }

    /**
     * Returns the ending position for the puzzle.
     * @return the ending position
     */
    public int[] getEndPos() {
        return tipOver.getEndCoord();
    }

    /**
     * Turns the configuration into a unique int that cna be hashed.
     * @return a unique int that represents the configuration
     */
    @Override
    public int hashCode() {
        return currentPos[0] * tipOver.getNumRows() + currentPos[1] * tipOver.getNumCols();
    }

    /**
     * Compares two TipOverConfig for equality, they're equal if the tippers are in the same current positions
     * and the boards are the same.
     * @param o the other possible TipOver Config to check for equality with
     * @return true if the two TipOverConfigs are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof TipOverConfig other) {
            // They are not equal if the tipper is in different spots
            if (currentPos[0] != other.currentPos[0] || currentPos[1] != other.currentPos[1]) {
                return false;
            }

            // They are not equal if the boards are not the same
            for (int i = 0; i < board.length; i++) {
                for (int j = 0; j < board[i].length; j++) {
                    if (!board[i][j].equals(other.board[i][j])) {
                        return false;
                    }
                }
            }

            return true;  // If the tippers are in the same spot and the boards are the same the configs are equal
        }

        return false;
    }

    /**
     * Checks if the tipper is at the same location as the ending crate.
     * @return true if the tipper is at the end location, false otherwise
     */
    @Override
    public boolean isSolution() {
        return currentPos[0] == tipOver.getEndCoord()[0] && currentPos[1] == tipOver.getEndCoord()[1];
    }

    /**
     * Checks for a possible neighbor above the current space, whether it tips a tower and moves up, moves
     * to another tower above it, or has no neighbor to the north.
     * @return the northern neighbor, or null if it doesn't exist
     */
    public TipOverConfig getNorth() {
        // Check space above the current position
        if (currentPos[0] > 0 && !board[currentPos[0] - 1][currentPos[1]].equals("0")) {
            TipOverConfig newConfig = new TipOverConfig(this);  // Creates a copy of the config
            newConfig.currentPos[0] -= 1;  // Changes the config's tipper position
            return newConfig;
        }
        // If the tipper is on a tower it can be tipped, so check if the tower can actually tip as well
        // as get to another valid space once tipped
        if (!board[currentPos[0]][currentPos[1]].equals("1")) {
            // Variable stores the height of the tower the tipper is on
            int height = Integer.parseInt(board[currentPos[0]][currentPos[1]]);

            // Finds upper neighbor of a tower
            if (currentPos[0] - height >= 0) {
                TipOverConfig newConfig = new TipOverConfig(this);  // Creates a copy of the config
                // Sets the original tower location to 0 because it is tipped over now
                newConfig.board[currentPos[0]][currentPos[1]] = "0";
                boolean canTip = true;  // Boolean to check if there is space to tip the tower
                // Checks the spaces the tower tips into to see if they are empty
                for (int i = 1; i <= height; i++) {
                    // If the space is not empty the tower cannot be tipped
                    if (!(board[currentPos[0] - i][currentPos[1]]).equals("0")) {
                        canTip = false;
                        break;
                    }

                    newConfig.board[currentPos[0] - i][currentPos[1]] = "1";  // Sets the spot to 1 for tipped tower
                }

                // If the tower can be tipped, it adjusts the tipper position to the first spot of the tipped tower
                // nex to where it originally stood and adds it to the neighbors LinkedList
                if (canTip) {
                    newConfig.currentPos[0] -= 1;
                    return newConfig;
                }
            }
        }
        return null;
    }

    /**
     * Checks for a possible neighbor below the current space, whether it tips a tower and moves down, moves
     * to another tower below it, or has no neighbor to the south.
     * @return the southern neighbor, or null if it doesn't exist
     */
    public TipOverConfig getSouth() {
        // Check space below the current position
        if (currentPos[0] < board.length - 1 && !board[currentPos[0] + 1][currentPos[1]].equals("0")) {
            TipOverConfig newConfig = new TipOverConfig(this);  // Creates a copy of the config
            newConfig.currentPos[0] += 1;  // Changes the config's tipper position
            return newConfig;
        }
        // If the tipper is on a tower it can be tipped, so check which ways the tower can actually tip as well
        // as get to another valid space once tipped
        if (!board[currentPos[0]][currentPos[1]].equals("1")) {
            // Variable stores the height of the tower the tipper is on
            int height = Integer.parseInt(board[currentPos[0]][currentPos[1]]);

            // Finds lower neighbor of a tower
            if (currentPos[0] + height <= board.length - 1) {
                TipOverConfig newConfig = new TipOverConfig(this);  // Creates a copy of the config
                // Sets the original tower location to 0 because it is tipped over now
                newConfig.board[currentPos[0]][currentPos[1]] = "0";
                boolean canTip = true;  // Boolean to check if there is space to tip the tower
                // Checks the spaces the tower tips into to see if they are empty
                for (int i = 1; i <= height; i++) {
                    // If the space is not empty the tower cannot be tipped
                    if (!(board[currentPos[0] + i][currentPos[1]]).equals("0")) {
                        canTip = false;
                        break;
                    }

                    newConfig.board[currentPos[0] + i][currentPos[1]] = "1";  // Sets the spot to 1 for tipped tower
                }

                // If the tower can be tipped, it adjusts the tipper position to the first spot of the tipped tower
                // nex to where it originally stood and adds it to the neighbors LinkedList
                if (canTip) {
                    newConfig.currentPos[0] += 1;
                    return newConfig;
                }
            }
        }
        return null;
    }

    /**
     * Checks for a possible neighbor right of the current space, whether it tips a tower and moves right, moves
     * to another tower right of it, or has no neighbor to the east.
     * @return the eastern neighbor, or null if it doesn't exist
     */
    public TipOverConfig getEast() {
        // Check space to the right of the current position
        if (currentPos[1] < tipOver.getNumCols() - 1 && !board[currentPos[0]][currentPos[1] + 1].equals("0")) {
            TipOverConfig newConfig = new TipOverConfig(this);  // Creates a copy of the config
            newConfig.currentPos[1] += 1;  // Changes the config's tipper position
            return newConfig;
        }
        // If the tipper is on a tower it can be tipped, so check which ways the tower can actually tip as well
        // as get to another valid space once tipped
        if (!board[currentPos[0]][currentPos[1]].equals("1")) {
            // Variable stores the height of the tower the tipper is on
            int height = Integer.parseInt(board[currentPos[0]][currentPos[1]]);

            // Finds right neighbor of a tower
            if (currentPos[1] + height <= board[0].length - 1) {
                TipOverConfig newConfig = new TipOverConfig(this);  // Creates a copy of the config
                // Sets the original tower location to 0 because it is tipped over now
                newConfig.board[currentPos[0]][currentPos[1]] = "0";
                boolean canTip = true;  // Boolean to check if there is space to tip the tower
                // Checks the spaces the tower tips into to see if they are empty
                for (int i = 1; i <= height; i++) {
                    // If the space is not empty the tower cannot be tipped
                    if (!(board[currentPos[0]][currentPos[1] + i]).equals("0")) {
                        canTip = false;
                        break;
                    }

                    newConfig.board[currentPos[0]][currentPos[1] + i] = "1";  // Sets the spot to 1 for tipped tower
                }

                // If the tower can be tipped, it adjusts the tipper position to the first spot of the tipped tower
                // nex to where it originally stood and adds it to the neighbors LinkedList
                if (canTip) {
                    newConfig.currentPos[1] += 1;
                    return newConfig;
                }
            }
        }
        return null;
    }

    /**
     * Checks for a possible neighbor left of the current space, whether it tips a tower and moves left, moves
     * to another tower left of it, or has no neighbor to the west.
     * @return the western neighbor, or null if it doesn't exist
     */
    public TipOverConfig getWest() {
        // Check space to the left of the current position
        if (currentPos[1] > 0 && !board[currentPos[0]][currentPos[1] - 1].equals("0")) {
            TipOverConfig newConfig = new TipOverConfig(this);  // Creates a copy of the config
            newConfig.currentPos[1] -= 1;  // Changes the config's tipper position
            return newConfig;
        }
        // If the tipper is on a tower it can be tipped, so check which ways the tower can actually tip as well
        // as get to another valid space once tipped
        if (!board[currentPos[0]][currentPos[1]].equals("1")) {
            // Variable stores the height of the tower the tipper is on
            int height = Integer.parseInt(board[currentPos[0]][currentPos[1]]);

            // Finds left neighbor of a tower
            if (currentPos[1] - height >= 0) {
                TipOverConfig newConfig = new TipOverConfig(this);  // Creates a copy of the config
                // Sets the original tower location to 0 because it is tipped over now
                newConfig.board[currentPos[0]][currentPos[1]] = "0";
                boolean canTip = true;  // Boolean to check if there is space to tip the tower
                // Checks the spaces the tower tips into to see if they are empty
                for (int i = 1; i <= height; i++) {
                    // If the space is not empty the tower cannot be tipped
                    if (!(board[currentPos[0]][currentPos[1] - i]).equals("0")) {
                        canTip = false;
                        break;
                    }

                    newConfig.board[currentPos[0]][currentPos[1] - i] = "1";  // Sets the spot to 1 for tipped tower
                }

                // If the tower can be tipped, it adjusts the tipper position to the first spot of the tipped tower
                // nex to where it originally stood and adds it to the neighbors LinkedList
                if (canTip) {
                    newConfig.currentPos[1] -= 1;
                    return newConfig;
                }
            }
        }
        return null;
    }

    /**
     * Returns a LinkedList of the TipOverConfig's neighbors, which can be found by moving the tipper to a space
     * that is next to it horizontally or vertically if there is a crate/tower there, or by tipping if the tipper
     * is on a tower and there is room to tip the tower over.
     * @return a LinkedList of the configuration's neighbors
     */
    @Override
    public LinkedList<TipOverConfig> getNeighbors() {
        LinkedList<TipOverConfig> neighbors = new LinkedList<>();  // LinkedList to store neighbors

        TipOverConfig north = getNorth();
        TipOverConfig south = getSouth();
        TipOverConfig east = getEast();
        TipOverConfig west = getWest();

        if (north != null) {
            neighbors.add(north);
        }
        if (south != null) {
            neighbors.add(south);
        }
        if (east != null) {
            neighbors.add(east);
        }
        if (west != null) {
            neighbors.add(west);
        }

        return neighbors;
    }

    /**
     * Creates a string form of the configuration that displays the board with an '*' representing the tipper location,
     * an '!' representing the ending location, numbers 1-9 representing the towers and crates, and '_' representing
     * the empty spaces. It will also have numbers showing the rows and columns on the outside of the board separated
     * from the rest of the board by lines.
     * @return a String representation of the configuration
     */
    @Override
    public String toString() {
        String gameBoard = "    ";  // String to hold the representation of the config

        // Adds numbers to show the numbered columns to the string
        for (int i = 0; i < tipOver.getNumCols(); i++) {
            gameBoard += "  " + i;
        }

        // Adds '_'s to separate the column numbers from the board
        gameBoard += "\n    ";
        for (int j = 0; j < tipOver.getNumCols(); j++) {
            gameBoard += "___";
        }

        // Adds numbers to represent the row of the board, and the actual corresponding row from the
        // board to the string
        for (int k = 0; k < board.length; k++) {
            gameBoard += "\n " + k + " |";  // Adds the row number

            // Adds the row of the board
            for (int l = 0; l < board[k].length; l++) {
                // If the tipper is at this spot add a '*' in front to show that
                if (currentPos[0] == k && currentPos[1] == l) {
                    gameBoard += " *";
                }
                // Otherwise, if this is the ending location add a '!' here to show that
                else if (tipOver.getEndCoord()[0] == k && tipOver.getEndCoord()[1] == l) {
                    gameBoard += " !";
                }
                else {
                    gameBoard += "  ";
                }

                // If the board is empty add a '_' to show that
                if (board[k][l].equals("0")) {
                    gameBoard += "_";
                }
                // Otherwise, add the String from the board to show the height of the tower/crate there
                else {
                    gameBoard += board[k][l];
                }
            }
        }

        gameBoard += "\n";
        return gameBoard;
    }
}
