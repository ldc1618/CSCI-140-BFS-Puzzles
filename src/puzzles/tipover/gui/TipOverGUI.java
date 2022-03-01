package puzzles.tipover.gui;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import puzzles.tipover.model.TipOverModel;
import util.Observer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

/**
 * A Graphical User Interface for the Tip Over puzzle that allows the user to load a file containing a tip over
 * puzzle, display the puzzle board, move the tipper, get hints to solve the puzzle, reload the current file,
 * load a new file, and quit the program.
 *
 * @author Luke Chelius
 * November 2021
 */
public class TipOverGUI extends Application
        implements Observer<TipOverModel, Object> {

    /**
     * The model for the GUI, how it gets the information to display.
     */
    private TipOverModel model;

    /**
     * The puzzle file currently being used in the PTUI.
     */
    private String currentFile;

    /**
     * A label that displays info about the game, if towers were tipped, if the user won, etc.
     */
    private Label instructions = new Label();

    /**
     * A grid pane holding the board of towers.
     */
    private GridPane board;

    /**
     * Initializes the current file variable and model, adding it to the observers if a file is given to
     * initialize the model, otherwise it makes both null.
     * @throws FileNotFoundException thrown if an invalid file name is given
     */
    @Override
    public void init() throws FileNotFoundException {
        // get the command line args
        List<String> args = getParameters().getRaw();

        // If there is a command line arg, use it as the file name
        if (args.size() > 0) {
            this.currentFile = args.get(0);  // Set current file to the arg
            this.model = new TipOverModel(this.currentFile);  // Creates the model from the puzzle file
            this.model.addObserver(this);  // Adds the model to the observers
        }
        // Otherwise, set the model and file name to null
        else {
            this.currentFile = null;
            this.model = null;
        }
    }

    /**
     * Initializes the GUI elements, creates the stage that displays the game board, creates and gives
     * implementation to the move buttons and the option buttons (hint, load, reload), and displays
     * text at the top informing the user of what happened with the GUI (a file loaded, tower tipped,
     * they won, etc.).
     * @param stage the window that displays the tip over game for the user
     * @throws FileNotFoundException thrown if an invalid file name is given
     */
    @Override
    public void start(Stage stage) throws FileNotFoundException {
        stage.setTitle("Tip Over");  // Sets the window name

        // If the current file is null, need to get a file from the user to begin
        if (currentFile == null) {
            while (true) {
                FileChooser fileChooser = new FileChooser();  // Make a new file chooser
                fileChooser.setTitle("Open Tip Over File");  // Set the file chooser's title
                File selectedFile;  // Create a file variable to hold the file picked by the user
                // Allows the user to pick a text file
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Text Files", "*.txt"));
                selectedFile = fileChooser.showOpenDialog(stage);  // Holds the given file
                try {
                    this.currentFile = String.valueOf(selectedFile);  // Sets current file to the selected one
                    this.model = new TipOverModel(this.currentFile);  // Creates a model with the file
                    this.model.addObserver(this);  // Adds the model to the observers
                    break;
                }
                catch (FileNotFoundException | IllegalArgumentException | ArrayIndexOutOfBoundsException ignored) {}
            }
        }

        BorderPane mainPane = new BorderPane();  // The main border pane for the GUI

        instructions.setText(" New file loaded.");  // Sets text to tell user a file was loaded
        instructions.setAlignment(Pos.TOP_LEFT);  // Aligns the text in the top left
        mainPane.setTop(instructions);  // Sets the top of the main border pane to the text label

        // Creates a grid pane for the board to go in the center of the main border pane
        this.board = new GridPane();
        // Creates all the spaces on the board
        for (int i = 0; i < model.getCurrentConfig().getBoard().length; i++) {
            for (int j = 0; j < model.getCurrentConfig().getBoard()[i].length; j++) {
                Button temp = new Button();  // Makes the button
                this.board.add(temp, j, i);  // Adds the button to the grid pane
            }
        }
        this.board.setAlignment(Pos.CENTER);  // Places the grid pane in the center
        mainPane.setCenter(this.board);  // Sets the center of the border pane to the grid pane of buttons

        HBox buttons = new HBox(); // HBox to hold all the buttons
        buttons.setSpacing(10);

        BorderPane moves = new BorderPane();  // Border pane for movement buttons

        Button north = new Button("   ^   ");  // Button to move the tipper north
        north.setOnAction(event -> {
            // If the model is the solution say so and don't allow the user to make a move
            if (model.getCurrentConfig().isSolution()) {
                instructions.setText(" You won! No more moves are needed.");
            }
            else {
                boolean[] bools = model.move("north");  // Hold if the move is valid or not, and tipped a tower
                // If a tower was tipped display that
                if (bools[1]) {
                    instructions.setText(" A tower has been tipped over.");
                }
                // If the move was not valid say so
                if (!bools[0]) {
                    instructions.setText(" No crate or tower there.");
                }
            }
        });
        north.setAlignment(Pos.CENTER);  // Aligns the north button to the center

        Button south = new Button("   v   ");  // Button to move the tipper south
        south.setOnAction(event -> {
            // If the model is the solution say so and don't allow the user to make a move
            if (model.getCurrentConfig().isSolution()) {
                instructions.setText(" You won! No more moves are needed.");
            }
            else {
                boolean[] bools = model.move("south");  // Hold if the move is valid or not, and tipped a tower
                // If a tower was tipped display that
                if (bools[1]) {
                    instructions.setText(" A tower has been tipped over.");
                }
                // If the move was not valid say so
                if (!bools[0]) {
                    instructions.setText(" No crate or tower there.");
                }
            }
        });
        south.setAlignment(Pos.CENTER);  // Aligns the south button to the center

        Button east = new Button("   >   ");  // Button to move the tipper east
        east.setOnAction(event -> {
            // If the model is the solution say so and don't allow the user to make a move
            if (model.getCurrentConfig().isSolution()) {
                instructions.setText(" You won! No more moves are needed.");
            }
            else {
                boolean[] bools = model.move("east");  // Hold if the move is valid or not, and tipped a tower
                // If a tower was tipped display that
                if (bools[1]) {
                    instructions.setText(" A tower has been tipped over.");
                }
                // If the move was not valid say so
                if (!bools[0]) {
                    instructions.setText(" No crate or tower there.");
                }
            }
        });
        east.setAlignment(Pos.BOTTOM_RIGHT);  // Aligns the east button to the right

        Button west = new Button("   <   ");  // Button to move the tipper west
        west.setOnAction(event -> {
            // If the model is the solution say so and don't allow the user to make a move
            if (model.getCurrentConfig().isSolution()) {
                instructions.setText(" You won! No more moves are needed.");
            }
            else {
                boolean[] bools = model.move("west");  // Hold if the move is valid or not, and tipped a tower
                // If a tower was tipped display that
                if (bools[1]) {
                    instructions.setText(" A tower has been tipped over.");
                }
                // If the move was not valid say so
                if (!bools[0]) {
                    instructions.setText(" No crate or tower there.");
                }
            }
        });
        west.setAlignment(Pos.BOTTOM_LEFT);  // Aligns the west button to the left

        HBox top = new HBox();  // HBox for the top button
        top.getChildren().add(north);  // Adds the north button to the top
        HBox bottom = new HBox();  // HBox for the bottom button
        bottom.getChildren().add(south);  // Adds the south button to the bottom

        moves.setTop(top);  // Sets the top of the moves border pane to the north button
        top.setAlignment(Pos.CENTER);  // Aligns top to the center
        moves.setLeft(west);  // Sets the left of the moves border pane to the west button
        moves.setRight(east);  // Sets the right of the moves border pane to the east button
        moves.setBottom(bottom);  // Sets the center of the moves bored pane to the south button
        bottom.setAlignment(Pos.CENTER);  // Aligns bottom to the center

        buttons.getChildren().add(moves);  // Adds the moves buttons to the buttons HBox

        HBox options = new HBox();  // New HBox for the hint, load, and reload buttons
        Button hint = new Button("HINT");  // Hint button
        hint.setOnAction(event -> {
            // If the puzzle is the solution, can't give a hint
            if (model.getCurrentConfig().isSolution()) {
                instructions.setText(" You won! No more moves are needed.");
            }
            else {
                boolean[] bools = model.hint();  // Holds if the board is solvable and if a tower was tipped
                // If a tower was tipped display that
                if (bools[1]) {
                    instructions.setText(" A tower has been tipped over.");
                }
                // If the board is unsolvable say that
                if (!bools[0]) {
                    instructions.setText(" Unsolvable board");
                }
            }
        });

        Button load = new Button("LOAD");  // Load button - load a new file
        load.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();  // Create a new file chooser
            fileChooser.setTitle("Open Tip Over File");  // Set its title
            File selectedFile;  // Variable to hold selected file
            // User can select a text file
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Text Files", "*.txt"));
            selectedFile = fileChooser.showOpenDialog(stage);  // Holds the selected file
            try {
                stage.hide();
                this.model = this.model.load(String.valueOf(selectedFile));  // reassigns model to new model
                this.model.addObserver(this);  // Adds model to observers
                this.currentFile = String.valueOf(selectedFile);  // Sets current file to new selected file
                board.getChildren().clear();  // Clears the game board
                // Reassigns the buttons on the board
                for (int i = 0; i < model.getCurrentConfig().getBoard().length; i++) {
                    for (int j = 0; j < model.getCurrentConfig().getBoard()[i].length; j++) {
                        Button temp = new Button();  // Makes the button
                        this.board.add(temp, j, i);  // Adds the button to the grid pane
                    }
                }
                // Tell user if the start config is already the solution
                if (model.getCurrentConfig().isSolution()) {
                    instructions.setText(" YOU WON!");
                }
                else {
                    instructions.setText(" New file loaded.");  // Tells user a new file is being loaded
                }
                displayBoard();  // Places towers, tipper, and end point of the board
                stage.show();
            }
            catch (FileNotFoundException ignored) {}
            catch (IllegalArgumentException | ArrayIndexOutOfBoundsException nfe) {
                instructions.setText(" Invalid file selected.");
                stage.show();
            }
        });

        Button reload = new Button("RELOAD");  // Reload button - reloads same file
        reload.setOnAction(event -> {
            try {
                this.model = this.model.load(currentFile);  // Sets model to new model with file
                this.model.addObserver(this);  // Adds model to observers
            } catch (FileNotFoundException ignored) {}
            // Tell user if the start config is already the solution
            if (model.getCurrentConfig().isSolution()) {
                instructions.setText(" YOU WON!");
            }
            else {
                instructions.setText(" New file loaded.");  // Tells user a new file is being loaded
            }
            displayBoard();  // Places towers, tipper, and end point on the board
        });

        options.getChildren().addAll(hint, load, reload);  // Adds hint, load, and reload buttons to HBox
        buttons.getChildren().add(options);  // Adds the HBox of options to the buttons HBox
        options.setAlignment(Pos.CENTER_RIGHT);  // Aligns the HBox to the right center

        mainPane.setBottom(buttons);  // Sets the bottom of the main border pane to all the buttons

        displayBoard();  // Places the towers, tipper, and end point on the board

        Scene game = new Scene(mainPane);  // Creates scene with the main border pane
        stage.setScene(game);  // Sets the stage with the scene
        stage.show();  // Shows the stage
    }

    /**
     * Populates the board grid pane with the locations of the towers (represented by numbers), the tipper
     * (a red highlighted spot), and the end point (a green highlighted spot).
     */
    public void displayBoard() {
        ObservableList<Node> buttons = board.getChildren();  // Stores the Collection of buttons

        // Loops through the 2D array containing the board
        for (int i = 0 ; i < model.getCurrentConfig().getBoard().length; i++) {
            for (int j = 0; j < model.getCurrentConfig().getBoard()[i].length; j++) {
                // If the child in the border pane at that spot is a button then populate it with its height
                if (buttons.get(i * model.getCurrentConfig().getBoard()[i].length + j) instanceof Button) {
                    ((Button) buttons.get(i * model.getCurrentConfig().getBoard()[i].length + j))
                            .setText(model.getCurrentConfig().getBoard()[i][j]);

                    // If that spot is the tipper location highlight it in red
                    if (i == model.getCurrentConfig().getCurrentPos()[0] &&
                            j == model.getCurrentConfig().getCurrentPos()[1]) {
                        buttons.get(i * model.getCurrentConfig().getBoard()[i].length + j)
                                .setStyle("-fx-background-color: #ff0000");
                    }
                    // If it's the end goal highlight it in green
                    else if (i == model.getCurrentConfig().getEndPos()[0] &&
                            j == model.getCurrentConfig().getEndPos()[1]) {
                        buttons.get(i * model.getCurrentConfig().getBoard()[i].length + j)
                                .setStyle("-fx-background-color: #00aa00");
                    }
                    // Otherwise, highlight it white
                    else {
                        buttons.get(i * model.getCurrentConfig().getBoard()[i].length + j)
                                .setStyle("-fx-background-color: #ffffff");
                    }
                }
            }
        }
    }

    /**
     * Updates the board by calling the displayBoard method and checking if the user won the game.
     * @param tipOverModel the model that informs that something happened
     * @param o optional additional data that the model can send to the observer
     */
    @Override
    public void update(TipOverModel tipOverModel, Object o) {
        displayBoard();  // Updates the board

        // If the user won, display that
        if (model.getCurrentConfig().isSolution()) {
            instructions.setText(" YOU WON!");
        }
        else {
            instructions.setText("");
        }
    }

    /**
     * Launches the Tip Over GUI with the command line args that may or may not contain a String of a file
     * name to play.
     * @param args an optional puzzle file to read
     */
    public static void main(String[] args) {
        Application.launch(args);
    }
}
