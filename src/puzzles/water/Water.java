package puzzles.water;

import solver.*;
import java.util.*;

/**
 * Main class for the water buckets puzzle.
 *
 * @author Luke Chelius
 */

public class Water {

    /**
     * The ending amount that is trying to be reached.
     */
    private int amount;

    /**
     * The max amounts each bucket can hold.
     */
    private ArrayList<Integer> buckets;

    /**
     * Creates a new Water object.
     * @param amount the goal amount to get
     * @param buckets the max amounts each bucket can hold
     */
    public Water(int amount, ArrayList<Integer> buckets) {
        this.amount = amount;
        this.buckets = buckets;
    }

    /**
     * Returns the goal amount of water in a bucket.
     * @return the goal amount
     */
    public int getAmount() { return amount; }

    /**
     * Returns the max amount of water each bucket can hold in an ArrayList where each index is a bucket.
     * @return an ArrayList of the max volume of each bucket
     */
    public ArrayList<Integer> getBuckets() { return buckets; }

    /**
     * Run an instance of the water buckets puzzle.
     * @param args [0]: desired amount of water to be collected;
     *             [1..N]: the capacities of the N available buckets.
     */
    public static void main( String[] args ) {
        if ( args.length < 2 ) {
            System.out.println(
                    ( "Usage: java Water amount bucket1 bucket2 ..." )
            );
        }
        else {
            // Try catch block for if command line args aren't ints
            try {
                int amount = Integer.parseInt(args[0]);  // Gets the goal amount from input
                ArrayList<Integer> buckets = new ArrayList<>();  // Empty ArrayList to store max bucket volumes
                ArrayList<Integer> emptyBuckets = new ArrayList<>();  // ArrayList to fill with 0s for each bucket

                // Adds the rest of the command line args as bucket values
                for (int i = 1; i < args.length; i++) {
                    buckets.add(Integer.parseInt(args[i]));  // Adds as a max bucket volume
                    emptyBuckets.add(0);  // Adds a 0 as an empty bucket to pass to the configuration
                }

                Water water = new Water(amount, buckets);  // Creates new water object

                System.out.println("Amount: " + amount + ", Buckets: " + buckets);  // Prints water puzzle info

                // Creates new solver with a water configuration of the puzzle and an ArrayList of empty buckets
                Solver<WaterConfiguration> solution = new Solver<>(new WaterConfiguration(water, emptyBuckets));
                LinkedList<WaterConfiguration> path = solution.solve(true);  // Stores the solution path in a LinkedList

                // If the path is null no solution was found
                if (path == null) {
                    System.out.println("No Solution");
                }
                // Otherwise, print the steps in order
                else {
                    int step = 0;  // Counter for what step its on
                    for (WaterConfiguration config : path) {
                        System.out.println("Step " + step + ": " + config.getBuckets());  // Prints the step
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
