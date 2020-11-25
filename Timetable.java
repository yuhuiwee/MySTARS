import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.*;
import java.util.Map.Entry;

/**
 * Represents the timetable of each student
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class Timetable implements Serializable, Cloneable {
    
    /**
     * Serialization of the object Timetable
     */
	private static final long serialVersionUID = 1L;

    /**
     * An array list of the time slot serial number
     */
	private ArrayList<Integer> timeSlotSerialNumber;

    /**
     * A integer to hash map of the time slot information
     */
    private TreeMap<Integer, ArrayList<String>> timeSlotInformation;// startSerial : <endSerial, coursecode, indexnum,
																	// classtype, venue>

    /**
     * A timetable constructor
     * Creates a new array list of all time slots
     * @param timeSlotSerialNumber This is the time slot serial number
     * @param timeSlotInfo This is the time slot information
     */
    // Constructors
	public Timetable(ArrayList<Integer> timeSlotSerialNumber, TreeMap<Integer, ArrayList<String>> timeSlotInfo) {
		this.timeSlotSerialNumber = new ArrayList<Integer>(timeSlotSerialNumber);// arraylist of all timeslots
		this.timeSlotInformation = new TreeMap<Integer, ArrayList<String>>(timeSlotInfo);
	}

    /**
     * A empty timetable constructor
     * Creation of an empty timetable
     */
	public Timetable() { // for creating empty timetable
		this.timeSlotSerialNumber = new ArrayList<Integer>();
		this.timeSlotInformation = new TreeMap<Integer, ArrayList<String>>();
	}

    /**
     * Printing of one time slot information
     * @param startSerial This is the start of the serial number
     */
    // Normal Methods
	public void printSchedule(int startSerial) { // print only one timing
		ArrayList<String> a = timeSlotInformation.get(startSerial);
		int day = startSerial / 100000;
		int type = startSerial % 10;

		String weekType;
		if (type == 2) {
			weekType = "Both";
		} else if (type == 1) {
			weekType = "Odd";
		} else {
			weekType = "Even";
		}

		System.out.println("\tClass Type: " + a.get(3));
		System.out.println("\tWeek Type: " + weekType);
		System.out.println("\tDay of the Week: " + DayOfWeek.of(day));
		System.out.println("\tTimeslot: " + getTimefromSerial(startSerial) + " - "
				+ getTimefromSerial(Integer.parseInt(a.get(0))));
		System.out.println("\tVenue: " + a.get(4) + "\n");
	}

    /**
     * Print all the time slot information / schedule
     */
	public void printSchedule() {// printall
		for (Integer k : timeSlotInformation.keySet()) {
			printSchedule(k);
		}
	}

    /**
     * Print the weekly schedule of the time slot
     */
	public void printWeeklySchedule() {
		TreeMap<Integer, ArrayList<String>> oddWeek = new TreeMap<Integer, ArrayList<String>>();
		TreeMap<Integer, ArrayList<String>> evenWeek = new TreeMap<Integer, ArrayList<String>>();

		for (Map.Entry<Integer, ArrayList<String>> entry : timeSlotInformation.entrySet()) {
			if (entry.getKey() % 10 == 2) {
				oddWeek.put(entry.getKey(), entry.getValue());
				evenWeek.put(entry.getKey(), entry.getValue());
			} else if (entry.getKey() % 10 == 1) {
				oddWeek.put(entry.getKey(), entry.getValue());
			} else {
				evenWeek.put(entry.getKey(), entry.getValue());
			}
		}

		System.out.println("\n\n");
		System.out.println("Odd Week: ");
		printWeek(oddWeek);
		System.out.println("\n\n");
		System.out.println("Even Week");
		printWeek(evenWeek);
		return;
	}

    /**
     * Print the time slot information of a week
     * @param map this is the Treemap the courses
     */
    private void printWeek(TreeMap<Integer, ArrayList<String>> map) {
        for (int i = 1; i <= 5; i++) {
            SortedMap<Integer, ArrayList<String>> temptree = new TreeMap<Integer, ArrayList<String>>();
            temptree = map.subMap(i*100000,(i + 1) * 100000);// get all courses for that day
            if (!temptree.isEmpty()) { // if temp tree == null, means no lessons for that day
                System.out.println(DayOfWeek.of(i) + ": ");
                for (Entry<Integer, ArrayList<String>> entry : temptree.entrySet()) {
                    // endSerial[0], coursecode[1], indexnum[2], classtype[3],venue[4]
                    ArrayList<String> a = entry.getValue();

                    if (a.get(3).equals("LECTURE") | a.get(3).equals("LEC")){
                        System.out.println("\tTime: " + getTimefromSerial(entry.getKey()) + " - "
                            + getTimefromSerial(Integer.parseInt(a.get(0))));
                        System.out.println("\tCourse: " + a.get(1)); //dont print index when its lecture classtype
                        System.out.println("\tClass Type: "+a.get(3));
                        System.out.println("\tVenue: " + a.get(4));
                        System.out.println();
                    }
                    else{//if not lecture type
                        System.out.println("Time: " + getTimefromSerial(entry.getKey()) + " - "
                                + getTimefromSerial(Integer.parseInt(a.get(0))));
                        System.out.println("Course: " + a.get(1) + ", Index: " + a.get(2));
                        System.out.println("\tClass Type: "+a.get(3));
                        System.out.println("Venue: " + a.get(4));
                    }
                }
            }
        }
    }


	// Getters and Setters
    /**
     * Get the array list of time slot serial number
     * @return This is the list of time slot serial number
     */
	public ArrayList<Integer> getTimeSlotSerialNumber() {
		return timeSlotSerialNumber;
	}

    /**
     * Get the array list of time slot information
     * @return This is the list of time slot information
     */
	public TreeMap<Integer, ArrayList<String>> getTimeSlotInformation() {
		return timeSlotInformation;
	}

    /**
     * Set the new time slot serial number
     * @param t This is the new time slot serial number
     */
	public void setTimeSlotSerialNumber(ArrayList<Integer> t) {
		this.timeSlotSerialNumber = t;
	}

    /**
     * Check if there is a clash in the time table
     * @param t This is the timetable
     * @return This is a true/false on whether there is a clash in the timetable
     */
    @SuppressWarnings("unchecked")
	public boolean checkTimeslot(Timetable t) {
		ArrayList<Integer> time = (ArrayList<Integer>) t.getTimeSlotSerialNumber().clone();
		ArrayList<Integer> temp = (ArrayList<Integer>) timeSlotSerialNumber.clone();

		temp.retainAll(time); // check for intersection
		if (temp.isEmpty()) {
			return true;// no time clash
		} else {
			return false;
		}
	}

    /**
     * Merging of the time table
     * @param t This is the timetable
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     */
	public void mergeTimetable(Timetable t) throws TimetableClash {
		if (!checkTimeslot(t)) {// should not invole this! check before calling this method!
			throw new TimetableClash("Timetable Clash!");
		}
		if (this.timeSlotInformation == null | this.timeSlotSerialNumber == null) {
			this.timeSlotInformation = t.getTimeSlotInformation();
			this.timeSlotSerialNumber = t.getTimeSlotSerialNumber();
		} else {
			this.timeSlotInformation.putAll(t.getTimeSlotInformation());
			this.timeSlotSerialNumber.addAll(t.getTimeSlotSerialNumber());
		}
	}

    /**
     * Removing of the timetable
     * @param t This is the timetable
     */
	public void removeTimetable(Timetable t) {
		timeSlotSerialNumber.removeAll(t.getTimeSlotSerialNumber());
		timeSlotInformation.entrySet().removeAll(t.getTimeSlotInformation().entrySet());
	}

    /**
     * Removing of a class in the time slot serial number
     * @param startSerial This is the lesson start serial
     * @param endSerial This is the lesson end serial
     */
	public void removeClass(int startSerial, int endSerial) {
		timeSlotInformation.remove(startSerial);

		while (startSerial != endSerial) {
			if (startSerial % 10 == 2) {
				if (timeSlotSerialNumber.contains(startSerial - 1)) {
					timeSlotSerialNumber.remove(startSerial - 1);
					timeSlotSerialNumber.remove(startSerial - 2);
				}
			} else {
				if (timeSlotSerialNumber.contains(startSerial)) {
					timeSlotSerialNumber.remove(startSerial);
				}
			}

			startSerial = startSerial + 300;
			if (startSerial % 1000 >= 600) {
				startSerial = startSerial + 400;
			}

		}
	}

    /**
     * Adding of the class to the timetable
     * @param startSerial This is the class start serial number
     * @param endSerial This is the class end serial number
     * @param courseCode This is the course code
     * @param index This is the course's index number
     * @param courseType This is the course's type
     * @param venue This is the class's venue
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     */
    public void addClass(int startSerial, int endSerial, String courseCode, int index, String courseType, String venue)
			throws TimetableClash {
		// endSerial[0], coursecode[1], indexnum[2], classtype[3],venue[4]
		if (!checkStartEnd(startSerial, endSerial)) {
			throw new TimetableClash("Timetable Clash!");
		}

		ArrayList<String> temp = new ArrayList<String>();
		temp.add(String.valueOf(endSerial));
		temp.add(courseCode.toUpperCase());
		temp.add(String.valueOf(index));
		temp.add(courseType.toUpperCase());
		temp.add(venue.toUpperCase());

		timeSlotInformation.put(startSerial, temp); // Start time serial will be the key, so it will be arranged in
													// order

		while (startSerial != endSerial) {
			if (startSerial % 10 == 2) { // For classes with normal week type, we will add both odd and even week into
											// the timetable of the class
				timeSlotSerialNumber.add(startSerial - 1);
				timeSlotSerialNumber.add(startSerial - 2);
			} else {
				timeSlotSerialNumber.add(startSerial);
			}
			startSerial = startSerial + 300; // Account for every half an hour interval (0800 - 0900 = Serial Code:
												// 108000 & 108300)
			if (startSerial % 1000 >= 600) { // If it reaches 0860, we make it 0900.
				startSerial = startSerial + 400;
			}
		}

		return;
	}

    /**
     * Checking if there is a clash between the class interval
     * @param startSerial This is the class start serial number
     * @param endSerial This is the class end serial number
     * @return This is a true/false on whether there is a clash between the class interval
     */
	public boolean checkStartEnd(int startSerial, int endSerial) {
		// return true if no clash
		// return false if clash
		if (timeSlotSerialNumber.isEmpty()) {
			return true;
		}
		ListIterator<Integer> i = timeSlotSerialNumber.listIterator();
		while (i.hasNext()) {
			int s = i.next();
			if (s >= startSerial & s <= endSerial) {
				/* Eg. s = 108300 (Monday, 8AM, Odd week) startSerial = 108002 endSerial = 110002
				so as long as there is a clash between the intervals, the timing
				will not be accepted
				*/
				if (startSerial % 10 == 2) { // For normal weeks
					return false;
				} else if (s % 10 == startSerial % 10) { // For odd/even week
					return false;
				}
			}

		}
		return true;
	}

	/**
     * Get the time from the serial number
     * @param serialtime This is the timing
     * @return This is the time
     */
	public String getTimefromSerial(int serialtime) {
		String time = "";

		serialtime = serialtime / 10;
		serialtime = serialtime % 10000;

		if (serialtime < 1000) {
			time = "0";
		}

		time = time + String.valueOf(serialtime);
		return time;
	}

	/**
     * This is a clone of the timetable
     */
    @Override
	public Timetable clone() throws CloneNotSupportedException {

		return (Timetable) super.clone();
	}

	/**
     * Edit the index in the time slot information
     * @param index This is the index to be changed
     */
	public void editIndex(int index) {// to be used only in course creation in application!
		for (ArrayList<String> entry : timeSlotInformation.values()) {
			entry.set(2, String.valueOf(index)); // 2 represents the timeSlotInformation Tree map KEY value for IndexNum
		} // So here, we are adding the index value to this tree map
		return;

	}

	/**
     * Get the lecture timings from the time slot information array list
     * @return This is the timetable
     * @throws NumberFormatException This exception is thrown when there is a number format error
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     */
	public Timetable getLectureTimings() throws NumberFormatException, TimetableClash { // for adding new indexes
		Timetable t = new Timetable();
		for (Map.Entry<Integer, ArrayList<String>> entry : timeSlotInformation.entrySet()) {
			ArrayList<String> a = entry.getValue();
			if (a.get(3) == "LECTURE" | a.get(3) == "LEC") {
				// endSerial[0], coursecode[1], indexnum[2], classtype[3],venue[4]
				t.addClass(entry.getKey(), Integer.parseInt(a.get(0)), a.get(1), Integer.parseInt(a.get(2)), a.get(3),
						a.get(4));

			}
		}
		return t;
	}

	/**
     * Check whether the time slot serial number is empty
     * @return This is a true/false value on whether the time slot serial number is empty
     */
    public boolean isEmpty(){
        if (this.timeSlotSerialNumber.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

}
