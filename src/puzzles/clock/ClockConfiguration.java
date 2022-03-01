package puzzles.clock;

import solver.*;
import java.util.LinkedList;

/**
 * Configuration for the Clock puzzle for the solver algorithm to use.
 *
 * @author Luke Chelius
 */
public class ClockConfiguration implements Configuration{

    /**
     * The clock puzzle the configuration is for.
     */
    private Clock clock;

    /**
     * The hour of the configuration, where on the clock it is.
     */
    private int hour;

    /**
     * Creates a new clock configuration for a specific clock at a certain hour.
     * @param clock the clock puzzle the configuration is for
     * @param hour the hour the configuration is representing
     */
    public ClockConfiguration(Clock clock, int hour) {
        this.clock = clock;
        this.hour = hour;
    }

    /**
     * Returns the hour of the configuration, the point it represents.
     * @return the hour the configuration represents
     */
    public int getHour() {
        return hour;
    }

    /**
     * Returns the starting hour of the puzzle.
     * @return the starting hour
     */
    public Object getStart() {
        return clock.getStart();
    }

    /**
     * Turns the configuration into a number that can be used to hash the configuration (the hour).
     * @return the configuration's hashcode (its hour)
     */
    @Override
    public int hashCode() {
        return hour;
    }

    /**
     * Compares two clock configurations to see if they are equal to one another or not.
     * @param o another possible clock configuration
     * @return true if the two configurations are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof ClockConfiguration other) {
            return hour == other.getHour();
        }
        return false;
    }

    /**
     * Checks if the configuration's hour is the end hour for the puzzle.
     * @return true if the hour is the same as the end hour, false otherwise
     */
    @Override
    public boolean isSolution() {
        return hour == clock.getEnd();
    }

    /**
     * Returns a LinkedList of the configuration's neighbors, which are its hour + or - 1 and wraps back to 1 or
     * to the last hour on the clock.
     * @return a LinkedList of the configuration's neighbors
     */
    @Override
    public LinkedList<ClockConfiguration> getNeighbors() {
        LinkedList<ClockConfiguration> neighbors = new LinkedList<>();  // LinkedList to hold the neighbors
        int next = hour + 1;  // Find the upper neighbor
        if (next > clock.getHours()) { next = 1; }  // Sets next to 1 if it goes past the max hour on the clock
        int prev = hour - 1;  // Find the previous neighbor
        if (prev < 1) { prev = clock.getHours(); }  // Sets prev to the max hour on the clock if it gets < 1

        // Adds both neighbors to the LinkedList of neighbors
        neighbors.add(new ClockConfiguration(clock, prev));
        neighbors.add(new ClockConfiguration(clock, next));
        return neighbors;
    }
}
