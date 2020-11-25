import java.io.*;
import java.util.*;

/**
 * Represnts the list of venues within the school
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class VenueList {

    /**
	 * Creation of a hashstring hash map for venue
	 */
	private static HashMap<String, Timetable> venueMap;

    /**
     * A Venue Constructor
     */
	public VenueList() {
		venueMap = new HashMap<String, Timetable>();
		saveVenueMap();
	}

    /**
     * Creation of a new venue
     * @param newVenue This is the name of the new venue
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     */
    public static void newVenue(String newVenue, Timetable t) throws VenueAlreadyExists, TimetableClash,
			CloneNotSupportedException {
		if (venueMap == null) {
			loadVenueList();
		}
		if (checkVenue(newVenue)) {
			throw new VenueAlreadyExists("Venue Already Exists!");
		}
		venueMap.put(newVenue.toUpperCase(), t);
		saveVenueMap();
		return;

	}

    /**
	 * Check if the user's input venue exist in the venueMap hash map
	 * @param venue This is the venue
	 * @return This is a true/false on whether the user's input venue exist in the venueMap hash map
	 * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
	 * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
	 * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
	 */
    public static boolean checkVenue(String venue)
			throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		if (venueMap == null) {
			loadVenueList();
		}
		if (venueMap.containsKey(venue.toUpperCase())) {
			return true;
		} else {
			return false;
		}

	}

    /**
     * Get the venue for the timetable
     * @param venue This is the venue
     * @return This is the venue for a timetable
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    public static Timetable getVenueTimetable(String venue)
			throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		if (venueMap == null) {
			loadVenueList();
		}
		return venueMap.get(venue.toUpperCase());
	}

    /**
     * Print all the venues in the database
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
	public static void printAllVenues() throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		if (venueMap == null) {
			loadVenueList();
		}
		if (venueMap.isEmpty()) {
			System.out.println("\tThere are no venues stored in our database!");
			return;
		}
		Set<String> s = venueMap.keySet();
		Iterator<String> i = s.iterator();
		while (i.hasNext()) {
			System.out.println("\t" + i.next());
		}
		return;

	}

	/**
     * Removes the venue's timetable 
     * @param t This is the venue's timetable
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    public static void removetimetable(Timetable t)
			throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		if (venueMap == null) {
			loadVenueList();
		}
		TreeMap<Integer, ArrayList<String>> map = t.getTimeSlotInformation();

		for (Map.Entry<Integer, ArrayList<String>> entry : map.entrySet()) {
			ArrayList<String> a = entry.getValue();
			String venue = a.get(4);
			//get venue timetable
			Timetable temp = getVenueTimetable(venue);
			//remove class from venue timetable
			temp.removeClass(entry.getKey(), Integer.parseInt(a.get(0)));

		}
		saveVenueMap();
	}

	/**
     * Saves the list of Venue into the VenueMap.ser file 
     */
	public static void saveVenueMap() {
		try {
			FileOutputStream fos = new FileOutputStream("VenueMap.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(venueMap);
			oos.close();
			fos.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}
	}

	/**
     * Loads the list of venues from the VenueMap.ser file
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    @SuppressWarnings("unchecked")
	public static void loadVenueList() throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		try {
			FileInputStream fis = new FileInputStream("VenueMap.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object temp = ois.readObject();
			if (temp instanceof HashMap<?, ?>) {
				venueMap = (HashMap<String, Timetable>) temp;
			}
			ois.close();
			fis.close();
		} catch (IOException ioe) {
			// ioe.printStackTrace();
			new VenueList();
		} catch (ClassNotFoundException c) {
			System.out.println("Class not found");
			c.printStackTrace();
		}
	}

	/**
     * Student is able to book the slot in the venue's timetable
     * @param startSerial This is the venue's start serial number
     * @param endSerial This is the venue's end serial number
     * @param courseCode This is the course code
     * @param index This is the index number
     * @param courseType This is the course type
     * @param venue This is the venue
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     */
    public static void bookSlot(int startSerial, int endSerial, String courseCode, int index, String courseType, String venue)
            throws VenueAlreadyExists, TimetableClash, CloneNotSupportedException {
        if (!venueMap.containsKey(venue.toUpperCase())){
            Timetable t = new Timetable();
            t.addClass(startSerial, endSerial, courseCode, index, courseType, venue.toUpperCase());
            newVenue(venue.toUpperCase(), t);
        }
        else{
            venueMap.get(venue).addClass(startSerial, endSerial, courseCode, index, courseType, venue);
        }
        saveVenueMap();
    }
        

}
