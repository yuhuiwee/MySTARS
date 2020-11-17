import java.util.*;
import java.io.*;

public class CourseList {

	private static HashMap<String, Course> mapCourse = null;
	private static HashMap<Integer, IndexNum> indexList = null;
	// venueTimetable hashmap is used to keep track of the vacancy of each venue. So
	// each venue has its own
	// timetable. Eg. LT2A has a hashmap containing dayOfTheWeek from 1-10 which
	// represents (1= Odd Week Monday,
	// 10 = Even week Friday). Then in each dayOfTheWeek, we have an arraylist to
	// store the timeslots every
	// time the venue is booked for a class. So for example a lecture is held at
	// LT2A on every monday from
	// 1000 - 1200. So in the hashmap for LT2A key, we have both 1 and 6
	// dayOfTheWeek key containing
	// 10,11,12 in its respective arraylist. So if another mod tries to book at this
	// timeslot on those
	// dayOfTheWeek mentioned, we will not allow it.

	public CourseList() {
		mapCourse = new HashMap<String, Course>();

		Course c1 = new Course("CZ2001", "Comp Sci");
		Course c2 = new Course("CZ2002", "Comp Sci");
		Course c3 = new Course("PH2018", "Philosophy");

		mapCourse.put("CZ2001", c1);
		mapCourse.put("CZ2002", c2);
		mapCourse.put("PH2018", c3);

		indexList = new HashMap<Integer, IndexNum>();

		IndexNum i1 = new IndexNum(1024, 10);
		IndexNum i2 = new IndexNum(1025, 2);
		IndexNum i3 = new IndexNum(1026, 10);
		IndexNum i4 = new IndexNum(1027, 10);
		IndexNum i5 = new IndexNum(1028, 10);
		IndexNum i6 = new IndexNum(1029, 10);

		ClassSchedule sc1 = new ClassSchedule("Lecture", "Normal", "Monday", 1000, 1200, "LT2A");
		ClassSchedule sc2 = new ClassSchedule("Tutorial", "Normal", "Tuesday", 1000, 1200, "TR1");
		ClassSchedule sc3 = new ClassSchedule("Tutorial", "Normal", "Monday", 1300, 1400, "TR1");
		ClassSchedule sc4 = new ClassSchedule("Lecture", "Normal", "Wednesday", 1100, 1300, "LT2A");
		ClassSchedule sc5 = new ClassSchedule("Lecture", "Normal", "Wednesday", 1100, 1300, "LT1A");
		ClassSchedule sc6 = new ClassSchedule("Laboratory Session", "Odd", "Tuesday", 1000, 1200, "SWLAB");
		ClassSchedule sc7 = new ClassSchedule("Laboratory Session", "Even", "Tuesday", 1000, 1200, "SWLAB");

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
			if (temp instanceof HashMap<?, ?>) {
				indexList = (HashMap<Integer, IndexNum>) temp;
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

	// ****** Method to check venue vacancy during course creation ******
	public static boolean checkVenueVacancy(int weekType, int dayOfTheWeek, int startTime, int endTime, String venue) {
		int day1, day2;

		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}

		if (weekType == 1) // Normal week type (Incld: odd and week)
		{
			day1 = dayOfTheWeek;
			day2 = dayOfTheWeek + 5;
			if ((venueTimeTable.get(Venue.valueOf(venue)).get(day2).contains(startTime))
					|| (venueTimeTable.get(Venue.valueOf(venue)).get(day2).contains(endTime)))
				return false; // Clash for the even week
		} else if (weekType == 2)
			day1 = dayOfTheWeek; // Odd week mapping
		else
			day1 = dayOfTheWeek + 5; // Even week mapping
		venueTimeTable.get(Venue.LT2A).put(3, new ArrayList<Integer>(Arrays.asList(11, 12, 13)));
		if ((venueTimeTable.get(Venue.valueOf(venue)).get(day1).contains(startTime))
				|| (venueTimeTable.get(Venue.valueOf(venue)).get(day1).contains(endTime)))
			return false; // This means that there is a clash, no vacancy
		return true;
	}

	// ****** Method to print student by index ******
	public static void printStudentByIndex(int indexNum) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		System.out.println("\nStudent list of Course index number " + indexNum + ":\n");
		ArrayList<String> students = indexList.get(indexNum).getRegisteredStudentList();
		for (int i = 0; i < students.size(); i++) {
			System.out.println((i + 1) + ") " + students.get(i));
		}
		if (students.size() == 0) {
			System.out.println("<Empty>\n");
		}
	}

	// ****** Method to print student by course ******
	public static void printStudentByCourse(String courseCode) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		System.out.println("\nStudent list of Course " + courseCode + ":\n");
		int[] temp = mapCourse.get(courseCode).getIndexNumber();
		ArrayList<String> temp2 = new ArrayList<>();
		ArrayList<String> students = new ArrayList<>();
		for (int i = 0; i < temp.length; i++) {
			temp2 = indexList.get(temp[i]).getRegisteredStudentList();
			for (int j = 0; j < temp2.size(); j++) {
				students.add(temp2.get(j));
			}
			temp2.clear();
		}
		for (int k = 0; k < students.size(); k++) {
			System.out.println((k + 1) + ") " + students.get(k));
		}
		if (students.size() == 0) {
			System.out.println("<Empty>\n");
		}
	}

	// ****** Method to print all courses in existence ******
	public static void printAllCourse() {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		int k = 0;
		ArrayList<String> temp = new ArrayList<String>();
		for (String i : mapCourse.keySet()) {
			temp.add(i);
		}
		Collections.sort(temp);
		System.out.println("\nCurrent Courses available:");
		for (int j = 0; j < temp.size(); j++) {
			System.out.print(temp.get(j) + "\t");
			k++;
			if (k % 4 == 0) {
				System.out.print("\n");
			}
		}
		System.out.print("\n");
	}

	// ****** Method to print all Indexes in existence ******
	public static void printAllIndex() {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		int k = 0;
		ArrayList<Integer> temp = new ArrayList<Integer>();
		for (Integer i : indexList.keySet()) {
			temp.add(i);
		}
		Collections.sort(temp);
		System.out.println("\nCurrent Indexes available:");
		for (int j = 0; j < temp.size(); j++) {
			System.out.print(temp.get(j) + "\t");
			k++;
			if (k % 4 == 0) {
				System.out.print("\n");
			}
		}
		System.out.print("\n");
	}

	// ****** Method to print course info ******
	public static void printCourseInfo(String courseCode) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		mapCourse.get(courseCode).printCourse();
	}

	// ****** Method to print all indexes in a course ******
	public static void printIndexOfCourse(String courseCode) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		int[] temp = mapCourse.get(courseCode).getIndexNumber();
		System.out.println("Total Index Numbers of " + courseCode + " : " + temp.length);
		for (int i = 0; i < temp.length; i++) {
			System.out.println("========================");
			indexList.get(temp[i]).printIndex();
		}
		System.out.println("========================\n");
	}

	// ****** Method to check course existence ******
	public static boolean checkCourseExistence(String courseCode) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		if (mapCourse.containsKey(courseCode))
			return true;
		else {
			System.out.println("The Course code does not exist!\nPlease enter the correct Course code!");
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
			System.out.println("The Index Number does not exist!\nPlease enter the correct Index Number!");
			return false;
		}
	}

	// ****** Method to create new course ******
	public static boolean newCourse(String courseCode, String school) throws CourseAlreadyExist {
		// Since we are simply adding the new course to the Courselist hashmap, we must
		// make sure the hashmap
		// exist in the first place. In case we cannot load the serealized file.
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}

		// Check if courseCode number already exist! If yes, different number must be
		// entered
		if (mapCourse.containsKey(courseCode)) {
			System.out.println("\nThe Course code already exist! Please enter a new Course code!");
			return false;
		}
		if (!(Course.checkSchoolExistence(school))) {
			System.out.println("\nThe school entered does not exist! Please enter a valid school!");
			return false;
		}
		Course c = new Course(courseCode, school);
		mapCourse.put(courseCode, c);
		CourseList.saveCourseMap(); // Save map immediately after creating new Course
		return true;
	}

	// ****** Method to Create a new Index Number for a Course ******
	public static boolean newIndexNumber(String courseCode, int indexNumber, int vacancy) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		if (indexList.containsKey(indexNumber)) {
			System.out.println("\nThis index number already exist!\nPlease enter a different index number!");
			return false;
		}
		// TODO: Check whether the day, timeSlot and venue clashes with other
		// indexNumber by a timetable class
		// DONE: This is done separately where the application class calls a method from
		// Courselist
		// to validate user input and return the necessary feedback
		IndexNum i = new IndexNum(indexNumber, vacancy);
		indexList.put(indexNumber, i);
		mapCourse.get(courseCode).setIndexNumber(indexNumber, indexList.get(indexNumber));
		CourseList.saveCourseMap();
		return true;
	}

	// ****** Method to Create a new schedule for an indexnumber ******
	public static void newSchedule(int indexNumber, String classType, String weekType, String dayOfTheWeek,
			int startTime, int endTime, String venue) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}

		ClassSchedule sc = new ClassSchedule(classType, weekType, dayOfTheWeek, startTime, endTime, venue);
		indexList.get(indexNumber).addSchedule(sc);
		// CourseList.saveCourseMap();
	}

	// ****** Similar to above except create similar lecture schedules for each
	// index number in the same course ******
	public static void newSchedule(int indexNumber, ClassSchedule lecture) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}

		indexList.get(indexNumber).addSchedule(lecture);
		// CourseList.saveCourseMap();
	}

	// IRS: Since the requirement didn't include remove course, should we not
	// include it?

	public static void updateCourseCode(Scanner sc, String currentCourseCode) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}

		String newCourseCode;
		while (true) {
			System.out.println("Enter new course code name: ");
			newCourseCode = sc.nextLine();
			if (checkCourseExistence(newCourseCode))
				System.out.println("This course code already exist! Please enter a different course code!\n");
			else
				break;
		}
		Course c = mapCourse.remove(currentCourseCode);
		c.setcourseCode(newCourseCode);
		mapCourse.put(newCourseCode, c);
	}

	public static void updateCourseCodeSchool(Scanner sc, String courseCode) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		System.out.println("Enter new School for Course code (" + courseCode + "): ");
		String newCouseCodeSchool = sc.next();
		String currentSchType = mapCourse.get(courseCode).getSchool();
		mapCourse.get(courseCode).setSchool(newCourseCodeSchool);
		mapCourse.remove(currentSchType);
		// nid compare whether curr n new is samw
		// nid compare whether input valid or not
	}

	// ****** Method to Change the Index Number digits of a Course ******
	public static boolean updateIndexNumber(String courseCode, int indexNumber, int newindexNumber) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		if (!(indexList.containsKey(indexNumber))) {
			System.out.println("\nThis index number does not exist!\nPlease enter a different index number!");
			return false;
		}
		int vacancy = mapCourse.get(courseCode).getIndexVacancy(indexNumber);
		IndexNum i = new IndexNum(newindexNumber, vacancy);
		indexList.put(newindexNumber, i);
		mapCourse.get(courseCode).setIndexNumber(newindexNumber, indexList.get(newindexNumber));
		mapCourse.remove(indexNumber, indexList.get(indexNumber));
		CourseList.saveCourseMap();
		return true;
	}

	public static void updateIndexNumVacancy(String courseCode, int indexNumber, int newVacancy) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		while (true) {
			if (!(indexList.containsKey(indexNumber))) {
				System.out.println("\nThis index number does not exist!\nPlease enter a different index number!");

			} else
				break;
		}
		indexList.get(indexNumber).setVacancy(newVacancy);

	}

	// TODO: Refer to updateCourse Method in application class. We will have
	// multiple methods to
	// change different elements of the course. Case 2 and below

	public static boolean addCourseByStudent(String coursecode, int index, String username) throws CourseDontExist {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}

		if (mapCourse.containsKey(coursecode)) {
			boolean bool = mapCourse.get(coursecode).addCourse(index, username);
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
		while (true) {
			if (!(mapCourse.containsKey(coursecode))) {
				System.out.println("The course does not exist!\n");
			} else {
				break;
			}
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

		int[] temp = mapCourse.get(coursecode).getIndexNumber();
		System.out.println("Vacancy for course " + coursecode);
		System.out.println("Index/Vacancy ");
		for (int i = 0; i < temp.length; i++) {
			System.out.println((i + 1) + ") " + temp[i] + " / " + checkVacancies(temp[i]));
		}
	}

	// ****** Method to check index vacancy ******
	public static int checkVacancies(Integer indexNum) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		return indexList.get(indexNum).getVacancy();
	}

	public static boolean checkIndex(String coursecode, int index) {
		if (indexList == null | mapCourse == null) {
			loadCourseList();
		}
		if (mapCourse.containsKey(coursecode)) {
			Course c = mapCourse.get(coursecode);
			return c.checkIndex(index); // true if index matches false otherwise
		}

		else {
			return false;
		}
	}

	// Swopcourse(String student1, String student2, int index1, int index 2, String
	// coursecode)
	// --to swop index with students

}

// ****** Method to GET index object from hashmap ******
// public static IndexNum getIndexObject(Integer indexNum) {
// return indexList.get(indexNum);
// }

// ****** Method to GET all indexes in that course ******
// public static int[] getCourseIndex(String courseCode) {
// return mapCourse.get(courseCode).getIndexNumber();
// }

// ****** Method to get course object from hashmap ******
// public static Course getCourseObject(String courseCode) {
// return mapCourse.get(courseCode);
// }

/*
 * public static void readCourseList() { String filePath = "test.txt";
 * //HashMap<String, String> map = new HashMap<String, String>();
 * 
 * String line; BufferedReader reader = new BufferedReader(new
 * FileReader(filePath)); while ((line = reader.readLine()) != null) { String[]
 * parts = line.split(":", 2); if (parts.length >= 2) { String key = parts[0];
 * String objectSia = parts[1]; String[] partSia = objectSia.split("|", 3); if
 * (partSia.length >= 3) { String courseCode = partSia[0]; String school =
 * partSia[1]; String courseType = partSia[2];
 * 
 * Course value = new Course(courseCode, school, courseType); mapCourse.put(key,
 * value); } //String value = parts[1]; } else {
 * System.out.println("ignoring line: " + line); } } }
 */
// Attributes needed: (Yes, literally only one attribute needed)
// Hashmap <String, Course>
// --maps "CE2002" to Object CE2002
// TODO: Add method to load and save hashmap.
// OR:: Load and save hashmap with every method
// OR:: Have a check at the start of the function. If hashmap is empty, load the
// hashmap
// --If file isnt found, create new hashmap
// --else, load the file and assign to hashmap.
// ----save before exiting the method if the hashmap is edited.
// ----also update static hashmap attribut in this class
// ----personally recommend this method as its safer :)

// PrintAll()
// -- prints all courses in a list
// --If yall very free, can also filter by school etc... but this isnt required
/*
 * public void PrintAll() { for (Map.Entry<String, Course> entry :
 * mapCourse.entrySet()) { System.out.println(entry.getKey()); } }
 */

// Print course(String course, int index)
// --for student to print the courses they have registered for

// --- I think this one should be placed in the Student Class since there is a
// hashmap which stores the
// registered courses of each student.---
// YuHui: This hashmap only stores the course name and index. Need this method
// to call for course description details :)
// YuHui: But if we arent planning on displaying this info, then its ok.. Its
// just more convenient to add more deets in the future if we really need to.

// 2 methods, print student list by index number
// print student list by course

// Newcourse(String coursecode, Hashmap <int, int> indexNumMappedtoVacancies,
// ...)
// --for admin to add new courses
// --Create new Course object & set attributes
// --adds newly created Course object to hashmap with String coursecode as key
// For method NewCourse, it is for the admin to add a totally new course into
// the list