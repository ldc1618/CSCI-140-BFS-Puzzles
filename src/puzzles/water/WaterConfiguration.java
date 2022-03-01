package puzzles.water;

import solver.*;
import java.util.*;

/**
 * Configuration for the Water puzzle for the solver algorithm to use.
 *
 * @author Luke Chelius
 */
public class WaterConfiguration implements Configuration {

    /**
     * The water puzzle the configuration is for.
     */
    private Water water;

    /**
     * The amount of water in the buckets for this configuration.
     */
    private ArrayList<Integer> buckets;

    /**
     * Creates a new water configuration for a water puzzle with a certain amount of water in its buckets.
     * @param water the water puzzle the configuration is for
     * @param buckets the amount of water in the buckets it represents
     */
    public WaterConfiguration(Water water, ArrayList<Integer> buckets) {
        this.water = water;
        this.buckets = buckets;
    }

    /**
     * Creates a new water configuration for a water puzzle with a certain amount of water in its buckets.
     * This is a copy constructor, takes another configuration and makes a deep copy of it.
     * @param other the configuration to deep copy
     */
    public WaterConfiguration(WaterConfiguration other) {
        this.water = other.getWater();
        this.buckets = new ArrayList<>(other.getBuckets());
    }

    /**
     * Returns the water puzzle the configuration is a part of.
     * @return the water puzzle
     */
    public Water getWater() {
        return water;
    }

    /**
     * Returns an ArrayList containing the water in each bucket.
     * @return an ArrayList with the amount of water in each bucket
     */
    public ArrayList<Integer> getBuckets() {
        return buckets;
    }

    /**
     * Returns the max amount of water allowed in each bucket.
     * @return an ArrayList with the max amount of water allowed in the buckets
     */
    public Object getStart() {
        return water.getBuckets();
    }

    /**
     * Turns the configuration into a unique int that cna be hashed.
     * @return a unique int that represents the configuration
     */
    @Override
    public int hashCode() {
        int hash = 0;
        // Sums all the buckets' water amounts
        for (Integer bucket : buckets) {
            hash += bucket;
        }
        return hash;
    }

    /**
     * Compares two water configurations for equality, they are equal if the amount of water in each of their buckets
     * is the same.
     * @param o the other possible water configuration to check for equality with
     * @return true if the two water configs are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof WaterConfiguration other) {
            return buckets.equals(other.getBuckets());  // Equal if buckets are the same
        }
        return false;
    }

    /**
     * Checks if any of the configuration's buckets have the goal amount in it.
     * @return true if a bucket has the goal amount, false otherwise
     */
    @Override
    public boolean isSolution() {
        return buckets.contains(water.getAmount());
    }

    /**
     * Returns a LinkedList of the water configuration's neighbors, which can be gotten by pouring one of the buckets
     * out completely, filling a bucket completely, or pouring one bucket into another until its full or the bucket
     * is empty.
     * @return a LinkedList of the configuration's neighbors
     */
    @Override
    public LinkedList<WaterConfiguration> getNeighbors() {
        LinkedList<WaterConfiguration> neighbors = new LinkedList<>();  // LinkedList to store the neighbors

        // Loops through all the configurations neighbors
        for (int i = 0; i < buckets.size(); i++) {

            // Copy constructor to create a new config with a copy of the buckets' amounts
            WaterConfiguration newConfig = new WaterConfiguration(this);
            newConfig.getBuckets().set(i, 0);  // Completely empties the current bucket

            // Adds it to the neighbors if it isn't already in the list of neighbors and is not equal to the current
            // configuration
            if (!this.equals(newConfig) && !neighbors.contains(newConfig)) {
                neighbors.add(newConfig);
            }

            newConfig = new WaterConfiguration(this);  // Resets the new config to the buckets' amounts again
            newConfig.getBuckets().set(i, water.getBuckets().get(i));  // Completely fills the bucket

            // Adds it to the neighbors if it isn't already in the list of neighbors and is not equal to the current
            // configuration
            if (!this.equals(newConfig) && !neighbors.contains(newConfig)) {
                neighbors.add(newConfig);
            }

            // Nested loop to pour bucket into another one
            for (int j = 0; j < buckets.size(); j++) {

                // Can't pour a bucket into itself
                if (i != j) {
                    newConfig = new WaterConfiguration(this);  // Resets new config to the buckets' amounts again

                    // Completely fills another bucket and removes that water from the other if full enough
                    if (buckets.get(j) + buckets.get(i) >= water.getBuckets().get(j)) {
                        int difference = water.getBuckets().get(j) - buckets.get(j);  // Amount needed to fill bucket j
                        newConfig.getBuckets().set(j, water.getBuckets().get(j));  // Bucket j to max
                        newConfig.getBuckets().set(i, buckets.get(i) - difference);  // Removes water from bucket i
                    }
                    // Adds all the bucket's water to the other otherwise and sets it to 0
                    else {
                        newConfig.getBuckets().set(j, buckets.get(j) + buckets.get(i));  // Adds i's water to j
                        newConfig.getBuckets().set(i, 0);  // Bucket i to 0
                    }

                    // Adds it to the neighbors if it isn't already in the list of neighbors and is not equal to the current
                    // configuration
                    if (!this.equals(newConfig) && !neighbors.contains(newConfig)) {
                        neighbors.add(newConfig);
                    }
                }
            }
        }

        return neighbors;
    }
}
