package solver;

import java.util.*;

/**
 * This class contains a universal algorithm to find a path from a starting
 * configuration to a solution, if one exists
 *
 * @author Luke Chelius
 */
public class Solver<C extends Configuration> {

    /**
     * The queue of nodes saying which will be next to check.
     */
    private LinkedList<C> queue;

    /**
     * HashMap of the previously visited nodes to prevent duplicates.
     */
    private HashMap<C, C> predecessorMap;

    /**
     * The starting configuration.
     */
    private C start;

    /**
     * The total number of configurations found, including duplicates.
     */
    private int totalConfigs;

    /**
     * Creates a new Solver object, initializes the queue and predecessorMap, and sets the
     * total configurations to 0.
     */
    public Solver(C start) {
        this.queue = new LinkedList<>();
        this.predecessorMap = new HashMap<>();
        this.start = start;
        this.totalConfigs = 0;
    }

    /**
     * Performs BFS on the configuration given using the configuration to pull in data for the puzzle
     * being solved so that this method can be used for any puzzle.
     * @param print a boolean, true to print the total and unique configs, false not to
     * @return a LinkedList containing the configurations in order to get from the start
     * to the end node in the least number of steps
     */
    public LinkedList<C> solve(boolean print) {
        // Adds the start configuration to the queue and predecessorMap
        queue.add(start);
        predecessorMap.put(start, null);
        totalConfigs++;  // Adds 1 to the configuration count

        // Runs until the queue is empty
        while (!queue.isEmpty()) {
            C config = queue.remove(0);  // Removes the first element from queue

            // If this node is the end node it prints the configurations and returns the list
            // of configurations in order of which to visit for the shortest path
            if (config.isSolution()) {
                printConfigs(print);

                LinkedList<C> solution = new LinkedList<>();  // Empty LinkedList for the solution configs

                // Runs until it gets back to the start configuration
                while (!config.equals(start)) {
                    solution.add(0, config);  // Adds the configuration to the front of the LinkedList
                    config = predecessorMap.get(config);  // Gets the previous config to the current one
                }
                solution.add(0, config);  // Adds the start config to the LinkedList
                return solution;
            }

            LinkedList<C> neighbors = config.getNeighbors();  // Gets the neighbors of the current config

            // Runs for each neighbor of the current config
            for (C neighbor : neighbors) {
                totalConfigs++;  // Adds 1 to the total config count

                // If the neighbor has not been previously visited its queued and added to the predecessorMap
                if (!predecessorMap.containsKey(neighbor)) {
                    queue.add(neighbor);
                    predecessorMap.put(neighbor, config);
                }
            }
        }

        // Runs if no solution was found
        printConfigs(print);
        return null;
    }

    public void printConfigs(boolean print) {
        if (print) {
            System.out.println("Total configs: " + totalConfigs);  // Prints the total config count
            System.out.println("Unique configs: " + predecessorMap.size());  // Prints the unique config count
        }
    }
}
