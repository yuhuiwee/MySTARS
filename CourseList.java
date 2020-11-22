import java.util.*;
import java.io.*;

public class CourseList {

	private static HashMap<String, Course> mapCourse = null;
	private static TreeMap<Integer, IndexNum> indexList = null; // changed this to tree map to allow printing in order
	private static HashMap<String, ArrayList<String>> schoolMapCourse;
	// venueLoc hashmap is used to keep track of the vacancy of each venue. So
	// each venue has its own timetable. The timeslot will be represented as serial
	// number of integer type as
	// [DAYOFWEEK|TIMEPERIOD|WEEKTYPE]. So for example if LT2A saves a serial number
	// of 110001,
	// what this means is that LT2A will be occupied on a Monday (Monday=1 .. Friday
	// = 5), from 1000 to 1030
	// (time represented in 24 hour format) on every week(1=normal, 2 = Odd, 3 =
	// Even week)
	// So if during a course creation and the class venue selected clashes with the
	// values in venueLoc,
	// they will mention that that timeslot is occupied.

	// ****** JUST AN INITIALIZATION WHEN PROGRAM STARTS ******
	public CourseList() {
		mapCourse = new HashMap<String, Course>();

		schoolMapCourse = new HashMap<String, ArrayList<String>>();
		schoolMapCourse.put("Computer Science", new ArrayList<String>(Arrays.asList("CZ2001")));
		schoolMapCourse.put("Engineering", new ArrayList<String>(Arrays.asList("CZ2002")));
		schoolMapCourse.put("Philosophy", new ArrayList<String>(Arrays.asList("PH2018")));

		// Create new course
		Course c1 = new Course("CZ2001", "Computer Science");
		Course c2 = new Course("CZ2002", "Engineering");
		Course c3 = new Course("PH2018", "Philosophy");

		// Saving course in HashMap
		mapCourse.put("CZ2001", c1);
		mapCourse.put("CZ2002", c2);
		mapCourse.put("PH2018", c3);

		// Instantiate new indexlist
		indexList = new TreeMap<Integer, IndexNum>();

		// Create IndexNum Objects
		IndexNum i1 = new IndexNum(1024, 10);
		IndexNum i2 = new IndexNum(1025, 2);
		IndexNum i3 = new IndexNum(1026, 10);
		IndexNum i4 = new IndexNum(1027, 10);
		IndexNum i5 = new IndexNum(1028, 10);
		IndexNum i6 = new IndexNum(1029, 10);

		// Create HashMap to put index for each course
		HashMap<Integer, IndexNum> cz2001 = new HashMap<Integer, IndexNum>();
		cz2001.put(1024, i1);
		cz2001.put(1025, i2);

		HashMap<Integer, IndexNum> cz2002 = new HashMap<Integer, IndexNum>();
		cz2002.put(1026, i3);
		cz2002.put(1027, i4);

		HashMap<Integer, IndexNum> ph2018 = new HashMap<Integer, IndexNum>();
		ph2018.put(1028, i5);
		ph2018.put(1029, i6);

		// Save index number map to indexlist
		newIndexNumbers(cz2001);
		newIndexNumbers(cz2002);
		newIndexNumbers(ph2018);
		// Save HashMap in Course Object
		c1.setIndexNumber(cz2001);
		c2.setIndexNumber(cz2002);
		c2.setIndexNumber(ph2018);

		// Creating and instantiating new venues
		VenueList.newVenue("LT1A");
		VenueList.newVenue("LT2A");
		VenueList.newVenue("TR1");
		VenueList.newVenue("SWLAB");

		// Intiantiate timetable
		Timetable sc1 = new Timetable();
		Timetable sc2 = new Timetable();
		Timetable sc3 = new Timetable();
		Timetable sc4 = new Timetable();
		Timetable sc5 = new Timetable();
		Timetable sc6 = new Timetable();
		Timetable sc7 = new Timetable();

		// Add Schedule to timetable with placeholder index
		sc1.addClass(110002, 112002, "CZ2001", -1, "Lecture", "LT2A");
		sc2.addClass(210002, 212002, "CZ2001", -1, "Tutorial", "TR1");
		sc3.addClass(213002, 214002, "CZ2001", -1, "Tutorial", "TR1");
		sc4.addClass(311002, 313002, "CZ2002", -1, "Lecture", "LT2A");
		sc5.addClass(311002, 313002, "PH2018", -1, "Lecture", "LT1A");
		sc6.addClass(210001, 212001, "CZ2002", -1, "Lab", "SWLAB");
		sc7.addClass(210002, 212002, "CZ2002", -1, "Lab", "SWLAB");

		// Add timetable clone to index
		Timetable temp = new Timetable();
		temp = (Timetable) sc1.clone();
		temp.editIndex(1024);
		i1.addClassSchedule(temp);
		VenueList.updateTimetable("LT2A", temp);

		temp = (Timetable) sc2.clone();
		temp.editIndex(1024);
		i1.addClassSchedule(sc2.clone());
		VenueList.updateTimetable("TR1", temp);

		temp = (Timetable) sc1.clone();
		temp.editIndex(1025);
		i2.addClassSchedule(sc1.clone());
		VenueList.updateTimetable("LT2A", temp);

		temp = (Timetable) sc3.clone();
		temp.editIndex(1025);
		i2.addClassSchedule(sc3.clone());
		VenueList.updateTimetable("TR1", temp);

		temp = (Timetable) sc4.clone();
		temp.editIndex(1026);
		i3.addClassSchedule(sc4.clone());

		temp = (Timetable) sc6.clone();
		temp.editIndex(1026);
		i3.addClassSchedule(sc6.clone());

		temp = (Timetable) sc4.clone();
		temp.editIndex(1027);
		i4.addClassSchedule(sc4.clone());

		temp = (Timetable) sc7.clone();
		temp.editIndex(1027);
		i4.addClassSchedule(sc7.clone());

		temp = (Timetable) sc5.clone();
		temp.editIndex(1028);
		i5.addClassSchedule(sc5.clone());

		temp = (Timetable) sc5.clone();
		temp.editIndex(1028);
		i6.addClassSchedule(sc5.clone());

		indexList.get(1024).addSchedule(sc1);
		indexList.get(1024).addSchedule(sc2);
		indexList.get(1025).addSchedule(sc3);
		indexList.get(1026).addSchedule(sc4);
		indexList.get(1027).addSchedule(sc4);
		indexList.get(1028).addSchedule(sc5);
		indexList.get(1028).addSchedule(sc6);
		indexList.get(1029).addSchedule(sc5);
		indexList.get(1029).addSchedule(sc7);

		mapCourse.get("CZ2001").setIndexNumber(1024, indexList.get(1024));
		mapCourse.get("CZ2001").setIndexNumber(1025, indexList.get(1025));
		mapCourse.get("CZ2002").setIndexNumber(1026, indexList.get(1026));
		mapCourse.get("CZ2002").setIndexNumber(1027, indexList.get(1027));
		mapCourse.get("PH2018").setIndexNumber(1028, indexList.get(1028));
		mapCourse.get("PH2018").setIndexNumber(1029, indexList.get(1029));

		// write to ser file

		saveCourseMap();
	}

	@SuppressWarnings("unchecked")
	public static void loadCourseList() {
		try {
			FileInputStream fis = new FileInputStream("CourseList.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object temp = ois.readObject();
			if (temp instanceof HashMap<?, ?>) {
				mapCourse = (HashMap<String, Course>) temp;
			}
			ois.close();
			fis.close();
		} catch (IOException ioe) {
			// ioe.printStackTrace();
			new CourseList(); // If file not found, create default values
		} catch (ClassNotFoundException c) {
			// System.out.println("Class not found");
			// c.printStackTrace();
			new CourseList();
		}

		try {
			FileInputStream fis = new FileInputStream("IndexList.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object temp = ois.readObject();
			if (temp instanceof TreeMap<?, ?>) {
				indexList = (TreeMap<Integer, IndexNum>) temp;
			}
			ois.close();
			fis.close();
		} catch (IOException ioe) {
			// ioe.printStackTrace();
			new CourseList(); // If file not found, create default values
		} catch (ClassNotFoundException c) {
			// System.out.println("Class not found");
			// c.printStackTrace();
			new CourseList();
		}
	}

	public static void saveCourseMap() {
		try {
			FileOutputStream fos = new FileOutputStream("CourseList.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(mapCourse);
			oos.close();
			fos.close();
		}

		catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}

		try {
			FileOutputStream fos = new FileOutputStream("IndexList.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(indexList);
			oos.close();
			fos.close();
			return;
		}

		catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}
	}

	// ****** PRINT ALL COURSE/BASED ON SCHOOL IN EXISTENCE ******
	public static void printAllCourse(String school) // School entry only used for admin updating course. Other purpose
														// will have
	{ // school parameter default value of null

		int k = 0;
		System.out.println("\nCurrent Courses available:");
		if (!(school.equals("null"))) {
			ArrayList<String> temp = schoolMapCourse.get(school);
			for (int i = 0; i < temp.size(); i++) {
				System.out.print(temp.get(i) + "\t");
				k++;
				if (k % 4 == 0) {
					System.out.print("\n");
				}
			}
		}
		for (int j = 0; j < schoolMapCourse.size(); j++) {
			for (int l = 0; l < schoolMapCourse.get(j).size(); l++) {
				ArrayList<String> temp = schoolMapCourse.get(l);
				for (int i = 0; i < temp.size(); i++) {
					System.out.print(temp.get(i) + "\t");
					k++;
					if (k % 4 == 0) {
						System.out.print("\n");
					}
				}
			}
			System.out.print(temp.get(j) + "\t");
			k++;
			if (k % 4 == 0) {
				System.out.print("\n");
			}
		}
		System.out.print("\n");
	}

	// ****** PRINT ALL COURSE IN EXISTENCE ******
	/*
	 * public static void printAllCourse() { if (indexList == null | mapCourse ==
	 * null) { loadCourseList(); } int k = 0; ArrayList<String> temp = new
	 * ArrayList<String>(); for (String i : mapCourse.keySet()) { temp.add(i); }
	 * Collections.sort(temp); System.out.println("\nCurrent Courses available:");
	 * for (int j = 0; j < temp.size(); j++) { System.out.print(temp.get(j) + "\t");
	 * k++; if (k % 4 == 0) { System.out.print("\n"); } } System.out.print("\n"); }
	 */

	// ****** Method to print all Indexes in existence ******
	public static void printAllIndex() {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}

		// iterate through and get sorted index num keys
		for (Map.Entry<Integer, IndexNum> entry : indexList.entrySet()) {
			System.out.println(entry.getKey());
		}
	}

	// ****** Method to check course existence ******
	public static boolean checkCourseExistence(String courseCode) { // Should we add school as an argument? So if admin
																	// from school A tries
		if (indexList == null | mapCourse == null) { // to edit courses from school B, it will produce an error message
			loadCourseList();
		}
		if (mapCourse.containsKey(courseCode))
			return true;
		else {
			return false;
		}
	}

	// ****** Method to check index existence ******
	public static boolean checkIndexExistence(int indexNum) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		if (indexList.containsKey(indexNum))
			return true;
		else {
			return false; // if does not contain
		}
	}

	// ****** Method to create new course ******
	public static Course newCourse(String courseCode, String school) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		if (!(schoolType.contains(school))) {
			schoolType.add(school);
		}
		Course c = new Course(courseCode, school);
		mapCourse.put(courseCode, c);
		CourseList.saveCourseMap(); // Save map immediately after creating new Course
		return c;
	}

	// ****** Method to Create a new Index Number for a Course ******
	public static void newIndexNumbers(HashMap<Integer, IndexNum> indexmap) {
		indexmap.putAll(indexmap);
	}

	// IRS: Since the requirement didn't include remove course, should we not
	// include it?

	// ****** CHANGE COURSE CODE NAME (Admin) ******
	public static void updateCourseCode(String newCourseCode, String currentCourseCode) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		Course c = getCourse(currentCourseCode);
		c.setcourseCode(newCourseCode);
		mapCourse.put(newCourseCode, c);
		mapCourse.remove(currentCourseCode);
	}

	// TODO: Refer to updateCourse Method in application class. We will have
	// multiple methods to change different elements of the course. Case 2 and below

	public static boolean addCourseByStudent(String coursecode, int index, String username) throws CourseDontExist {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		coursecode = coursecode.toUpperCase();
		if (mapCourse.containsKey(coursecode)) {
			boolean bool = getCourse(coursecode).addCourse(index, username);
			return bool;
		}

		else {
			throw new CourseDontExist("The course does not exist!\n");

		}
	}
	// --for student to add new courses
	// --Use String coursecode to get Course object
	// --Call method addcourse from Course object
	// --Course.addcourse shold return a true/false value
	// --true = successfully added
	// --false = no more vacancies. Put on waitlist

	public static void dropCourseByStudent(String coursecode, int index, String username)
			throws CourseDontExist, UserNotFound, UserAlreadyExists {

		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}

		coursecode = coursecode.toUpperCase();

		if (mapCourse.containsKey(coursecode)) {
			mapCourse.get(coursecode).dropCourse(index, username, false);// swopflag=false
			return;
		}

		else {
			throw new CourseDontExist("The course does not exist!\n");
		}

	}

	// Assume no student is registered under the course
	public static void dropCourseByAdmin(String coursecode) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		coursecode = coursecode.toUpperCase();
		if (!(mapCourse.containsKey(coursecode))) {
			System.out.println("The course does not exist!\n");
		}
		mapCourse.remove(coursecode);
	}

	// Dropcourse(String coursecode, int index, String username)
	// --for student to drop courses
	// --Similar to CourseList.addcourse
	// --Course.dropcourse should return true/false
	// --true = successfully dropped course
	// --false = student not registered for that course index the first place.. so
	// unable to drop

	public static void SwopCourse(String student1, String student2, int index1, int index2, String coursecode)
			throws UserNotFound, UserAlreadyExists, CourseDontExist {

		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}

		coursecode = coursecode.toUpperCase();

		if (mapCourse.containsKey(coursecode)) {
			Course c = mapCourse.get(coursecode);
			c.dropCourse(index1, student1, true);
			c.dropCourse(index2, student2, true);
			c.addCourse(index1, student2);
			c.addCourse(index2, student1);
			return;

		} else {
			throw new CourseDontExist("The course does not exist!\n");
		}
	}

	// ****** Method to check course vacancy ******
	public static void checkCourseVacancies(String coursecode) { // For students to view the vacancy of all the index in
																	// course
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}

		coursecode = coursecode.toUpperCase();

		int[] temp = mapCourse.get(coursecode).getIndexNumber();
		System.out.println("Vacancy for course " + coursecode);
		System.out.println("Index/Vacancy ");
		for (int i = 0; i < temp.length; i++) {
			System.out.println((i + 1) + ") " + temp[i] + " / " + checkVacancies(temp[i]));
		}
	}

	public static boolean checkIndex(String coursecode, int index) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}

		coursecode = coursecode.toUpperCase();
		if (mapCourse.containsKey(coursecode)) {
			Course c = mapCourse.get(coursecode);
			return c.checkIndex(index); // true if index matches false otherwise
		}

		else {
			return false;
		}
	}

	public static IndexNum getIndexNum(int index) {
		return indexList.get(index);
	}

	public static Course getCourse(String course) {
		course = course.toUpperCase();
		return mapCourse.get(course);
	}

	// Swopcourse(String student1, String student2, int index1, int index 2, String
	// coursecode)
	// --to swop index with students

}