import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Defining the access period of the students of different major and year
 * A default access period was created for the programmer for debugging
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class AccessPeriod implements Serializable {

    /**
     * Serialization of the object AccessPeriod
     */
	private static final long serialVersionUID = 1L;

    /**
     * Creation of an array list hash map with null value
     */
	private static HashMap<String, ArrayList<ZonedDateTime>> majoracc = null;
    
    /**
     * A string containing the students major and year
     */
	private String majoryear;

    /**
     * Creation of a default access period
     */
	public static void CreateDefaultAccessPeriod() {
		// Default access period: 1st Nov 2020 to 30 Nov 2020 for all students
		ZonedDateTime start = ZonedDateTime.parse("2020-11-01T00:00:00+08:00[Asia/Singapore]");
		ZonedDateTime end = ZonedDateTime.parse("2020-11-30T23:59:59+08:00[Asia/Singapore]");
		majoracc = new HashMap<String, ArrayList<ZonedDateTime>>();
		ArrayList<ZonedDateTime> temp = new ArrayList<ZonedDateTime>(2);
		temp.add(start);
		temp.add(end);

		majoracc.put("Default", temp);

		saveAccessPeriod(); // Save to file
	}

    /**
     * Saving the users' access period
     */
	public static void saveAccessPeriod() {
		try {
			FileOutputStream fos = new FileOutputStream("AccessPeriod.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(majoracc);
			oos.close();
			fos.close();
		}

		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

    /**
     * Load the previously saved users' access periods
     */
    @SuppressWarnings("unchecked")
	public static void loadAccessPeriod() {
		try {
			FileInputStream fis = new FileInputStream("AccessPeriod.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object temp = ois.readObject();
			if (temp instanceof HashMap<?, ?>) {
				majoracc = (HashMap<String, ArrayList<ZonedDateTime>>) temp;
			}
			if (majoracc == null) {
				CreateDefaultAccessPeriod();
			}
			ois.close();
			fis.close();
		} catch (IOException ioe) {
			// ioe.printStackTrace();
			CreateDefaultAccessPeriod(); // If file not found, create default values
		} catch (ClassNotFoundException c) {
			// System.out.println("Class not found");
			// c.printStackTrace();
			CreateDefaultAccessPeriod();
		}
	}

    /**
     * Check if the student accessing the site before, after or during their access timing
     * @param stu This is the student's username
     * @return return a true / false value whether if the student is in the correct time period to access the site
     */
	public static boolean checkAccessPeriod(Student stu) {
		if (majoracc == null) {
			loadAccessPeriod();
		}
		AccessPeriod st = stu.getAccessPeriod();
		ZonedDateTime current = ZonedDateTime.now();

		if (current.getZone().toString() != "Asia/Singapore" | current.getZone().toString() != "+08:00") {
			current = current.withZoneSameInstant(ZoneId.of("Asia/Singapore"));
		}

		if (current.isAfter(st.getStartDateTime()) & current.isBefore(st.getEndDateTime())) {
			return true;
		} else {
			System.out.println("\tAccess Denied! Your Personalised access period is: ");
			System.out.println("\tStart Date: "
					+ DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss Z").format(st.getStartDateTime()));
			System.out.println("\tEnd Date: "
					+ DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm:ss Z").format(st.getEndDateTime()));
			return false;
		}

	}

    /**
     * A constructor for the students access period
     * @param major This is the student's major
     * @param year This is the student's year
     */
    // Constructor for student
	public AccessPeriod(String major, int year) {
		this.majoryear = String.join(",", major.toLowerCase(), String.valueOf(year));
	}

    /**
     * Get the default access period through the students' major, year
     * @return This is a default access period time
     */
	private ArrayList<ZonedDateTime> getAccessPeriod() {
		// Iterate through hashmap to get major, year. If not in hashmap, set start and
		// end date as default
		if (majoracc == null) {
			loadAccessPeriod();
		}
		for (Map.Entry<String, ArrayList<ZonedDateTime>> e : majoracc.entrySet()) {
			if (e.getKey() == majoryear) {
				return e.getValue();
			}
		}
		return majoracc.get("Default");
	}

    /**
     * Getting the access period start time in terms of the time zone of the user
     * @return This the start date and time of an access period
     */
	public ZonedDateTime getStartDateTime() {
		return this.getAccessPeriod().get(0);
	}

    /**
     * Getting the access period end time in terms of the time zone of the user
     * @return This the end date and time of an access period
     */
    public ZonedDateTime getEndDateTime() {
        return this.getAccessPeriod().get(1);
    }

    // For Admin
    /*
	 * PrintAll Change AccessPeriod by major Remove AccessPeriod for major Add
	 * AccessPeriod for major Change Default Access Period Remove All but Default
	 */

    /**
     * Print all access period define in the array list containing all the access periods of each major and year
     */
	public static void printAllAccess() {
		if (majoracc == null) {
			loadAccessPeriod();
		}
		for (Map.Entry<String, ArrayList<ZonedDateTime>> entry : majoracc.entrySet()) {
			if (entry.getKey() != "Default") { // Print all but default
				String majy = entry.getKey();
				int index = majy.lastIndexOf(",");
				String maj = majy.substring(0, index);
				String y = majy.substring(index + 1, majy.length());
				System.out.println(maj + " major, Year" + y + ": ");
				System.out.println("Start Date: "
						+ DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm Z").format(entry.getValue().get(0)));
				System.out.println("End Date: "
						+ DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm Z").format(entry.getValue().get(1)));
			}
		}
		System.out.println("Default Access Period: ");
		System.out.println("Start Date: "
				+ DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm Z").format(majoracc.get("Default").get(0)));
		System.out.println("End Date: "
				+ DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm Z").format(majoracc.get("Default").get(1)));
		return;

	}

	/**
     * Changing the access period for students of different major and year
     * @param major This is the major
     * @param year This is the year of the students
     * @param start This is the start date and time of the access period created previously
     * @param end This is the end date and time of the access period created previously
     */
	public static void changeMajorAccess(String major, int year, ZonedDateTime start, ZonedDateTime end) {
		// Use for both adding and editing major access
		String key = String.join(",", major.toUpperCase(), String.valueOf(year));
		ArrayList<ZonedDateTime> temp = new ArrayList<ZonedDateTime>(2);
		temp.add(start);
		temp.add(end);
		if (majoracc.containsKey(key)) {
			System.out.printf("Changing Access Period for %s, Year %d", major, year);
			majoracc.replace(key, temp);
			saveAccessPeriod();
			return;
		} else {
			System.out.printf("Adding new Access Period for %s, Year %d", major, year);
			majoracc.put(key, temp);
			saveAccessPeriod();
			return;
		}
	}

	/**
     * Removing the access period for students of different major and year
     * @param major This is the students' major
     * @param year This is the students' year
     */
	public static void removeMajorAccess(String major, int year) {
		String key = String.join(",", major.toLowerCase(), String.valueOf(year));
		if (majoracc.containsKey(key)) {
			System.out.printf("Removing Access Period for %s, Year %d", major, year);
			saveAccessPeriod();
			return;
		}

		else {
			System.out.println("Error! Access period for " + major + " Year: " + year + "not found!");
			return;
		}
	}

	/**
     * Changing of the default access period start and end time
     * @param start This is the default access period start date and time
     * @param end This is the default access period end date and time
     */
	public static void editDefaultAccess(ZonedDateTime start, ZonedDateTime end) {
		ArrayList<ZonedDateTime> temp = new ArrayList<ZonedDateTime>(2);
		temp.add(start);
		temp.add(end);

		System.out.println("Changing Default Access Period");
		majoracc.replace("Default", temp);
		saveAccessPeriod();
		return;
	}

	/**
     * Removing all the access period created except the default access period
     */
    public static void removeAllbutDefault() {
        ArrayList<ZonedDateTime> temp = new ArrayList<ZonedDateTime>(2);
        temp = majoracc.get("Default");
        majoracc.clear();
        majoracc.put("Default", temp);
        saveAccessPeriod();
        return;
    }
}