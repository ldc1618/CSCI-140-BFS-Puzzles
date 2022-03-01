package puzzles.clock;

import solver.*;
import java.util.*;

/**
 * Main class for the "clock" puzzle.
 *
 * @author Luke Chelius
 */
public class Clock {

    /**
     * The number of hours on the clock.
     */
    private int hours;

    /**
     * The start hour for the puzzle.
     */
    private int start;

    /**
     * The end hour for the puzzle.
     */
    private int end;

    /**
     * Creates a new clock object.
     * @param hours the number of hours on the clock
     * @param start the start hour of the puzzle
     * @param end the end hour of the puzzle
     */
    public Clock(int hours, int start, int end) {
        this.hours = hours;
        this.start = start;
        this.end = end;
    }

    /**
     * Returns the number of hours on the clock.
     * @return the number of hours on the clock
     */
    public int getHours() { return hours; }

    /**
     * Returns the start hour on the clock for the puzzle.
     * @return the start hour of the puzzle
     */
    public int getStart() { return start; }

    /**
     * Returns the end hour on the clock for the puzzle.
     * @return the end hour of the puzzle
     */
    public int getEnd() { return end; }

    /**
     * Run an instance of the clock puzzle.
     * @param args [0]: number of hours on the clock;
     *             [1]: starting time on the clock;
     *             [2]: goal time to which the clock should be set.
     */
    public static void main( String[] args ) {
        if ( args.length != 3 ) {
            System.out.println("Usage: java Clock hours start end");
        }
        else {
            // Try catch block to catch if command line args aren't ints
            try {
                // Creates new Clock with input from command line
                Clock clock = new Clock(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                        Integer.parseInt(args[2]));

                // Prints out info on the puzzle
                System.out.println("Hours: " + clock.getHours() + ", Start: " + clock.getStart() + ", End: " +
                        clock.getEnd());

                // Creates a new Solver with the initial configuration to find a solution
                Solver<ClockConfiguration> solution = new Solver<>(new ClockConfiguration(clock, clock.getStart()));
                LinkedList<ClockConfiguration> path = solution.solve(true);  // LinkedList to store solution

                // If path is null no solution was found
                if (path == null) {
                    System.out.println("No Solution");
                }
                // Otherwise, print the steps in order
                else {
                    int step = 0;  // Counter for what step its on
                    for (ClockConfiguration config : path) {
                        System.out.println("Step " + step + ": " + config.getHour());  // Prints the step
                        step++;  // Increments to next step
                    }
                }
            } catch (NumberFormatException nfe) {
                System.out.println("NumberFormatException: " + nfe.getMessage());
            }
        }
    }
}
