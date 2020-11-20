import java.util.*;
import java.io.*;

public class CourseList {

	private static HashMap<String, Course> mapCourse = null;
	private static TreeMap<Integer, IndexNum> indexList = null; // changed this to tree map to allow printing in order
	private static HashMap<String, ArrayList<Integer>> venueLoc;
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

		Course c1 = new Course("CZ2001", "Computer Science");
		Course c2 = new Course("CZ2002", "Engineering");
		Course c3 = new Course("PH2018", "Philosophy");

		mapCourse.put("CZ2001", c1);
		mapCourse.put("CZ2002", c2);
		mapCourse.put("PH2018", c3);

		indexList = new TreeMap<Integer, IndexNum>();

		IndexNum i1 = new IndexNum(1024, 10);
		IndexNum i2 = new IndexNum(1025, 2);
		IndexNum i3 = new IndexNum(1026, 10);
		IndexNum i4 = new IndexNum(1027, 10);
		IndexNum i5 = new IndexNum(1028, 10);
		IndexNum i6 = new IndexNum(1029, 10);

		venueLoc = new HashMap<String, ArrayList<Integer>>();
		venueLoc.put("LT1A", new ArrayList<Integer>());
		venueLoc.put("LT2A", new ArrayList<Integer>());
		venueLoc.put("TR1", new ArrayList<Integer>());
		venueLoc.put("SWLAB", new ArrayList<Integer>());

		Timetable sc1 = new Timetable("Lecture", "Normal", "Monday", 1000, 1200, "LT2A");
		Timetable sc2 = new Timetable("Tutorial", "Normal", "Tuesday", 1000, 1200, "TR1");
		Timetable sc3 = new Timetable("Tutorial", "Normal", "Monday", 1300, 1400, "TR1");
		Timetable sc4 = new Timetable("Lecture", "Normal", "Wednesday", 1100, 1300, "LT2A");
		Timetable sc5 = new Timetable("Lecture", "Normal", "Wednesday", 1100, 1300, "LT1A");
		Timetable sc6 = new Timetable("Laboratory Session", "Odd", "Tuesday", 1000, 1200, "SWLAB");
		Timetable sc7 = new Timetable("Laboratory Session", "Even", "Tuesday", 1000, 1200, "SWLAB");

		venueLoc.get("LT2A").add(110001);
		venueLoc.get("LT2A").add(110301);
		venueLoc.get("LT2A").add(111001);
		venueLoc.get("LT2A").add(111301); // The serial number added in half an hour interval (1000 - 1200) for LT2A
											// Note that serial number 112001 is not added here as that would mean the
											// class ends at 1230
		venueLoc.get("TR1").add(210001);
		venueLoc.get("TR1").add(210301);
		venueLoc.get("TR1").add(211001);
		venueLoc.get("TR1").add(211301); // sc2

		venueLoc.get("TR1").add(113001);
		venueLoc.get("TR1").add(113301); // sc3

		venueLoc.get("LT2A").add(311001);
		venueLoc.get("LT2A").add(311301);
		venueLoc.get("LT2A").add(312001);
		venueLoc.get("LT2A").add(312301); // sc4

		venueLoc.get("LT1A").add(311001);
		venueLoc.get("LT1A").add(311301);
		venueLoc.get("LT1A").add(312001);
		venueLoc.get("LT1A").add(312301); // sc5

		venueLoc.get("SWLAB").add(210002);
		venueLoc.get("SWLAB").add(210302);
		venueLoc.get("SWLAB").add(211002);
		venueLoc.get("SWLAB").add(211302); // sc6

		venueLoc.get("SWLAB").add(210003);
		venueLoc.get("SWLAB").add(210303);
		venueLoc.get("SWLAB").add(211003);
		venueLoc.get("SWLAB").add(211303); // sc7

		indexList.put(1024, i1);
		indexList.put(1025, i2);
		indexList.put(1026, i3);
		indexList.put(1027, i4);
		indexList.put(1028, i5);
		indexList.put(1029, i6);

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
	// multiple methods to
	// change different elements of the course. Case 2 and below

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