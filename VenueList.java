import java.io.*;
import java.util.*;

public class VenueList {

    private static HashMap<String, Timetable> venueMap;

    public VenueList() {
        venueMap = new HashMap<String, Timetable>();
        saveVenueMap();
    }

    public static void newVenue(String newVenue) throws VenueAlreadyExists, TimetableClash,
            CloneNotSupportedException {
        if (venueMap==null){
            loadVenueList();
        }
        if (!checkVenue(newVenue)) { // if false
            return;
        }
        Timetable t = new Timetable();
        venueMap.put(newVenue.toUpperCase(), t);
        saveVenueMap();
        return;

    }

    public static boolean checkVenue(String venue)
            throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
        if (venueMap==null){
            loadVenueList();
        }
        if (venueMap.containsKey(venue.toUpperCase())) {
            return true;
        } else {
            return false;
        }

    }

    public static Timetable getVenueTimetable(String venue)
            throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
        if (venueMap==null){
            loadVenueList();
        }
        return venueMap.get(venue.toUpperCase());
    }

    public static void printAllVenues() throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
        if (venueMap==null){
            loadVenueList();
        }
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

    public static void updateTimetable(String venue, Timetable t)
            throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
        if (venueMap==null){
            loadVenueList();
        }
        if (!venueMap.containsKey(venue)){
            newVenue(venue);
        }
        venueMap.replace(venue.toUpperCase(), t);
        saveVenueMap();
        return;
    }

    public static void removetimetable(Timetable t)
            throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
        if (venueMap==null){
            loadVenueList();
        }
        TreeMap<Integer, ArrayList<String>> map = t.getTimeSlotInformation();

        for (Map.Entry<Integer, ArrayList<String>> entry : map.entrySet()){
            ArrayList<String> a = entry.getValue();
            String venue = a.get(4);
            //get venue timetable
            Timetable temp = getVenueTimetable(venue);
            //remove class from venue timetable
            temp.removeClass(entry.getKey(), Integer.parseInt(a.get(0)));

        }
        saveVenueMap();
    }

    public static void saveVenueMap() {
		try {
			FileOutputStream fos = new FileOutputStream("VenueMap.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(venueMap);
			oos.close();
			fos.close();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}
	}
    @SuppressWarnings("unchecked")
	public static void loadVenueList() throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		try {
			FileInputStream fis = new FileInputStream("VenueMap.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object temp = ois.readObject();
			if (temp instanceof HashMap<?, ?>) {
				venueMap = (HashMap<String,Timetable>) temp;
			}
			ois.close();
			fis.close();
		} catch (IOException ioe) {
            // ioe.printStackTrace();
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
            c.printStackTrace();
        }
    }

}
