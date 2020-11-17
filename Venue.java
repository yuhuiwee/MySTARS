import java.util.*;

public class Venue {
    private ArrayList<Integer> timeSlot = new ArrayList<Integer>();

    public Venue() {
    }

    public boolean checkVenueTimeSlot(int serialCode) {
        if (timeSlot.contains(serialCode))
            return true;
        return false;
    }
}