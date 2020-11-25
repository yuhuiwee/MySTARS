import java.util.*;

/**
 * Represents the list of venues in MySTARS
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class Venue {

	/**
	 * An array list of the time slot 
	*/
	private ArrayList<Integer> timeSlot = new ArrayList<Integer>();

	/**
	 * An empty Venue constructor
	*/
	public Venue() {
	}

	/**
	 * Check if the venue's serial code is in the array list
	 * @param serialCode This is the serial code of the venue
     * @return This is a true/false on whether the serial code is found within the time slot list
	*/
    public boolean checkVenueTimeSlot(int serialCode) {
        if (timeSlot.contains(serialCode))
            return true;
        return false;
    }
}