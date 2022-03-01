package solver;

import java.util.*;

/**
 * Configuration abstraction for the solver algorithm
 *
 * @author Luke Chelius
 */
public interface Configuration<E> {

    /*
     * List here the methods that the configurations of all the
     * puzzles must implement.
     * The project writeup explains that there are other acceptable designs,
     * so use of this interface is not required. However, for full design
     * credit, use of a shared solver that requires the implementation of
     * a certain abstraction from all puzzles is required.
     */

    /**
     * Returns true if the configuration is equal to another possible configuration.
     * @param other object to check equality with
     * @return true if the configuration is equal to other, false otherwise
     */
    public boolean equals(Object other);

    /**
     * Converts the configuration into a unique hashcode so that it can be hashed.
     *
     * Works a whole lot better when you don't spell it 'hashcode'.
     *
     * @return a unique int that represents the configuration
     */
    public int hashCode();

    /**
     * Returns true if the configuration is the solution.
     * @return true if the configuration equals the end point, false otherwise
     */
    public boolean isSolution();

    /**
     * Returns a LinkedList of all the neighbors of the configuration.
     * @return a LinkedList of all neighbors
     */
    public LinkedList<E> getNeighbors();


}
