import java.util.*;
import java.io.*;
//FIXME: Make methods static.. Hash map should also be static--> same for all students at all times.
//If a method is static, there is no need to call new CourseList and hence, no need to pass CourseList object into methods
//If all methods are static, then there is no need to call constructer

public class CourseList {
	// CourseList shoulld really be just a List of all the courses.
	// Kind of like the content page that direct you to the correct course
	// Start coding from IndexNum, then Course, then finally CourseList.. It will be
	// easier
	// While its more logical to code top down, u have to keep visualising the codes
	// downstream
	// Ciding from bottom up means u can see ur downstream codes and make edits
	// easier

	private static HashMap<String, Course> mapCourse = null;
	private static HashMap<Integer, IndexNum> indexList = null;

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
		ClassSchedule sc2 = new ClassSchedule("Tutorial", "Normal", "Tuesday", 1000, 1200, "TR18");
		ClassSchedule sc3 = new ClassSchedule("Tutorial", "Normal", "Monday", 1300, 1400, "TR17");
		ClassSchedule sc4 = new ClassSchedule("Lecture", "Normal", "Wednesday", 1100, 1300, "LT2A");
		ClassSchedule sc5 = new ClassSchedule("Lecture", "Normal", "Wednesday", 1100, 1300, "LT1A");
		ClassSchedule sc6 = new ClassSchedule("Laboratory Session", "Odd", "Tuesday", 1000, 1200, "LabR2");
		ClassSchedule sc7 = new ClassSchedule("Laboratory Session", "Even", "Tuesday", 1000, 1200, "LabR2");

		indexList.put(1024, i1);
		indexList.put(1025, i2);
		indexList.put(1026, i3);
		indexList.put(1027, i4);
		indexList.put(1028, i5);
		indexList.put(1029, i6);

		indexList.get(1024).addSchedule(sc1);
		indexList.get(1024).addSchedule(sc2);
		indexList.get(1025).addSchedule(sc1);
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

	// ****** Method to print student by index ******
	public static void printStudentByIndex(int indexNum) {
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
		for (String i : mapCourse.keySet()) {
			System.out.println(i);
		}
	}

	// ****** Method to print all Indexes in existence ******
	public static void printAllIndex() {
		Integer temp[] = new Integer[indexList.size()];
		for (Integer i : indexList.keySet()) {

			System.out.println(i);
		}
	}

	// ****** Method to print course info ******
	public static void printCourseInfo(String courseCode) {
		mapCourse.get(courseCode).printCourse();
	}

	// ****** Method to print all indexes in a course ******
	public static void printIndexOfCourse(String courseCode) {
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
		if (mapCourse.containsKey(courseCode))
			return true;
		else {
			System.out.println("The Course code does not exist!\nPlease enter the correct Course code!");
			return false;
		}
	}

	// ****** Method to check index existence ******
	public static boolean checkIndexExistence(int indexNum) {
		if (indexList.containsKey(indexNum))
			return true;
		else {
			System.out.println("The Index Number does not exist!\nPlease enter the correct Index Number!");
			return false;
		}
	}

	public static void NewCourse(String courseCode, String school) throws CourseAlreadyExist {
		// Since we are simply adding the new course to the Courselist hashmap, we must
		// make sure the hashmap
		// exist in the first place. In case we cannot load the serealized file.
		if (mapCourse == null) {
			CourseList.loadCourseList();
		}

		// Check if courseCode number already exist! If yes, different number must be
		// entered
		if (mapCourse.containsKey(courseCode)) {
			throw new CourseAlreadyExist("This Course Code number already exist!");
		}
		Course c = new Course(courseCode, school);
		mapCourse.put(courseCode, c);
		CourseList.saveCourseMap(); // Save map immediately after creating new Course
		return;
	} // Not sure to put boolean or not

	public static void NewIndexNumber(String courseCode, int indexNumber, String day, String timeSlot, String venue,
			int vacancy) throws IndexAlreadyExist {
		if (indexList == null) {
			CourseList.loadCourseList();
		}
		if (indexList.containsKey(indexNumber)) {
			throw new CourseAlreadyExist("T\nThis index number already exist!\nPlease enter a different index number!");
		}
		// TODO: Check whether the day, timeSlot and venue clashes with other
		// indexNumber by a timetable class
		IndexNum i = new IndexNum(indexNumber, vacancy);
		indexList.put(indexNumber, i);
		mapCourse.get(courseCode).setIndexNumber(indexNumber, indexList.get(indexNumber));
		CourseList.saveCourseMap();
	}

	// ****** Method to Create a new schedule for an indexnumber ******
	public static void newSchedule(int indexNumber, String classType, String weekType, String dayOfTheWeek,
			int startTime, int endTime, String venue) {
		// TODO: Check whether the day, timeSlot and venue clashes with other
		// indexNumber by a timetable class
		ClassSchedule sc = new ClassSchedule(classType, weekType, dayOfTheWeek, startTime, endTime, venue);
		indexList.get(indexNumber).addSchedule(sc);
		// CourseList.saveCourseMap();
	}

	// ****** Similar to above except create similar lecture schedules for each
	// index number in the same course ******
	public static void newSchedule(int indexNumber, ClassSchedule lecture) {
		indexList.get(indexNumber).addSchedule(lecture);
		// CourseList.saveCourseMap();
	}

	// IRS: Since the requirement didn't include remove course, should we not
	// include it?

	public void updateCourse(String currentCourseCode, String newCourseCode, String school,
			HashMap<Integer, IndexNum> smthg)
	// TODO: insert the current course code as well
	{
		if (mapCourse == null) {
			CourseList.loadCourseList();
		}

		if (mapCourse.containsKey(currentCourseCode)) {
			Course tempCourse = mapCourse.get(currentCourseCode);
			mapCourse.remove(currentCourseCode);
			mapCourse.put(newCourseCode, tempCourse);
			CourseList.saveCourseMap(); // Is this saving state? HUEHUE
		}

		else {
			throw new CourseDontExist("This Course Code number does NOT exist!"); // Have to create new exception
		}

	}

	public static boolean AddCourse(String coursecode, int index, String username) throws CourseDontExist {
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

	public static void DropCourse(String coursecode, int index, String username)
			throws CourseDontExist, UserNotFound, UserAlreadyExists {
		if (mapCourse.containsKey(coursecode)) {
			mapCourse.get(coursecode).dropCourse(index, username, false);// swopflag=false
			return;
		}

		else {
			throw new CourseDontExist("The course does not exist!\n");
		}

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

		int[] temp = mapCourse.get(courseCode).getIndexNumber();
		System.out.println("Vacancy for course " + coursecode);
		System.out.println("Index/Vacancy ");
		for (int i = 0; i < temp.length; i++) {
			System.out.println((i + 1) + ") " + temp[i] + " / " + checkVacancies(temp[i]));
		}
	}

	// ****** Method to check index vacancy ******
	public static int checkVacancies(Integer indexNum) {
		return indexList.get(indexNum).getVacancy();
	}

	public static boolean checkIndex(String coursecode, int index) {
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