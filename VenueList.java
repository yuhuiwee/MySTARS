import java.util.*;

public class VenueList {

    private static HashMap<String, Timetable> venueMap;

    public VenueList() {
        venueMap = new HashMap<String, Timetable>();
    }

    public static void newVenue(String newVenue) throws VenueAlreadyExists {
        if (!checkVenue(newVenue)) { // if false
            throw new VenueAlreadyExists("Venue already exists!");
        }
        Timetable t = new Timetable();
        venueMap.put(newVenue, t);
    }

    public static Timetable getTimetable(String venue) {
        return venueMap.get(venue);
    }

    public static boolean checkVenue(String venue) {
        if (venueMap.containsKey(venue)) {
            return true;
        } else {
            return false;
        }

    }

    public static Timetable getVenueTimetable(String venue) {
        return venueMap.get(venue);
    }

    public static void printAllVenues() {
        if (venueMap.isEmpty()) {
            System.out.println("\tSorry, there are no venues stored in our database!");
            return;
        }
        Set<String> s = venueMap.keySet();
        Iterator<String> i = s.iterator();
        while (i.hasNext()) {
            System.out.println("\t" + i.next());
        }
        return;

    }

    public static void updateTimetable(String venue, Timetable t) {
        venueMap.replace(venue, t);
        return;
    }

}
