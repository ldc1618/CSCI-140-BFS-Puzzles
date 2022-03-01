package puzzles.tipover.ptui;


import puzzles.tipover.model.TipOverModel;
import util.Observer;
import util.ptui.ConsoleApplication;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;


/**
 * A Plain Text User Interface for the Tip Over puzzle that allows the user to load a file containing a tip over
 * puzzle, display the puzzle board, move the tipper, get hints to solve the puzzle, reload the current file,
 * load a new file, get all the valid commands for the PTUI, and quit the program.
 *
 * @author Luke Chelius
 * November 2021
 */
public class TipOverPTUI
        extends ConsoleApplication
        implements Observer<TipOverModel, Object> {

    /**
     * String for the load command.
     */
    private final String LOAD = "load";

    /**
     * String for the reload command.
     */
    private final String RELOAD = "reload";

    /**
     * String for the move command.
     */
    private final String MOVE = "move";

    /**
     * String for the hint command.
     */
    private final String HINT = "hint";

    /**
     * String for the show command.
     */
    private final String SHOW = "show";

    /**
     * The model for the PTUI, how it gets the information to display.
     */
    private TipOverModel model;

    /**
     * A PrintWriter for the PTUI.
     */
    private PrintWriter out;

    /**
     * The puzzle file currently being used in the PTUI.
     */
    private String currentFile;

    /**
     * Initializes the model and adds it to the observers.
     * @throws FileNotFoundException thrown if the puzzle file is invalid
     */
    @Override
    public void init() throws FileNotFoundException {
        List<String> args = getArguments();  // Gets the arguments that contain the puzzle file
        this.currentFile = args.get(0);  // Gets the file from the arguments
        this.model = new TipOverModel(this.currentFile);  // Creates the model from the puzzle file
        this.model.addObserver(this);  // Adds the model to the observers
    }

    /**
     * Creates the PTUI elements by defining the usable commands, finds the console to print to, and
     * displays the puzzle.
     * @param console Where the UI should print output. It is recommended to save
     *                this object in a field in the subclass.
     */
    @Override
    public void start(PrintWriter console) {
        this.out = console;  // Sets the console to print to
        this.out.println("New file loaded.");  // Prints the file that is loading

        // If the configuration is the solution say so
        if (model.getCurrentConfig().isSolution()) {
            this.out.println("YOU WON!");
        }
        this.out.println(model.getCurrentConfig());  // Prints the configuration

        // Sets the load command that loads a new specified file
        super.setOnCommand(
                LOAD, 1, "{board-file-name}: Load a new game board file.",
                event -> {
                    try {
                        this.out.println("New file loaded.");  // Prints the file being loaded
                        this.model = model.load(event[0]);  // Sets the model to the new puzzle file model
                        this.model.addObserver(this);  // Adds the model to the observers
                        // If the model is the solution say so
                        if (model.getCurrentConfig().isSolution()) {
                            this.out.println("YOU WON!");
                        }
                        this.currentFile = event[0];  // Sets the current file to the new file
                        this.out.println(model.getCurrentConfig());  // Prints the config
                    }
                    catch (IllegalArgumentException | ArrayIndexOutOfBoundsException | FileNotFoundException e) {
                        this.out.println("Invalid file.");
                    }
                }
        );
        // Sets the reload command that reloads the current file
        super.setOnCommand(
                RELOAD, 0, ": Load the most recent file again.",
                event -> {
                    this.out.println("New file loaded.");  // Prints that the file is being reloaded
                    this.model = model.load(currentFile);  // Sets the model to the same file's initial state
                    this.model.addObserver(this);  // Adds the model to the observers
                    // If the model is the solution say so
                    if (model.getCurrentConfig().isSolution()) {
                        this.out.println("YOU WON!");
                    }
                    this.out.println(model.getCurrentConfig());  // Prints the config
                }
        );
        // Sets the move command that allows the user to move the tipper in a direction
        super.setOnCommand(
                MOVE, 1, "{north|south|east|west}: Go in given direction, possibly tipping a tower.",
                event -> {
                    // If the model is the solution say so and don't allow the user to make a move
                    if (model.getCurrentConfig().isSolution()) {
                        this.out.println("You won! No more moves are needed.");
                    }
                    else {
                        boolean[] bools = model.move(event[0]);
                        if (bools[1]) {
                            this.out.println("A tower has been tipped over.");
                        }
                        if (bools[0]) {
                            this.out.println(model.getCurrentConfig());  // Print the move
                        }
                        else {
                            this.out.println("Legal directions are\n[NORTH, EAST, SOUTH, WEST]");  // Print the move
                        }
                    }
                }
        );
        // Sets the hint command that shows the next step in the shortest path to the solution
        super.setOnCommand(
                HINT, 0, ": Make the next move for me",
                event -> {
                    // If the model is the solution no hint can be given
                    if (model.getCurrentConfig().isSolution()) {
                        this.out.println("You won! No more moves are needed.");
                    }
                    else {
                        boolean[] bools = model.hint();

                        if (bools[1]) {
                            this.out.println("A tower has been tipped over.");
                        }
                        if (bools[0]) {
                            this.out.println(model.getCurrentConfig());
                        }
                        else {
                            this.out.println("Unsolvable board");
                        }
                    }
                }
        );
        // Sets the show command that prints out the model's config
        super.setOnCommand(
                SHOW, 0, ": Display the board.",
                event -> this.out.println(model.getCurrentConfig()) // Prints the model's config
        );
    }

    /**
     * Called if the model was changed to update the PTUI.
     * @param model the model that informs that something happened
     * @param o optional additional data that the model can send to the observer
     */
    @Override
    public void update(TipOverModel model, Object o) {
        // If the model is the solution say so
        if (model.getCurrentConfig().isSolution()) {
            this.out.println("YOU WON!");
        }
    }

    /**
     * Launches the PTUI with the specified file, or gets one from the user if needed.
     * @param args an optional puzzle file to read
     */
    public static void main(String[] args) {
        // If there is a String in args pass it as the file
        if (args.length > 0) {
            ConsoleApplication.launch(TipOverPTUI.class, args);  // Launch the PTUI with the puzzle file
        }
        // Otherwise, read a file name as input from the user
        else {
            System.out.println("Enter a file name for a tip over puzzle: ");  // Prompt user for the file name
            Scanner in = new Scanner(System.in);  // Create a Scanner object to get the file name as input
            String[] file = {in.nextLine()};  // Get the name of the puzzle file
            ConsoleApplication.launch(TipOverPTUI.class, file);  // Launch the PTUI with the puzzle file
        }
    }
}
