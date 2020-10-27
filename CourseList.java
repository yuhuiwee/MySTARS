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

		Course c1 = new Course("CZ2001", "Comp Sci", "Lect-Tuts-Lab");
		Course c2 = new Course("CZ2002", "Comp Sci", "Lect-Tuts-Lab");
		Course c3 = new Course("PH2018", "Philosophy", "Lect");

		mapCourse.put("CZ2001", c1);
		mapCourse.put("CZ2002", c2);
		mapCourse.put("PH2018", c3);

		indexList = new HashMap<Integer, IndexNum>();

		IndexNum i1 = new IndexNum(1024, "Monday", "0900-1000", "SoftwareLab", 2);
		IndexNum i2 = new IndexNum(1025, "Monday", "1000-1100", "SoftwareLab", 2);

		indexList.put(1024, i1);
		indexList.put(1025, i2);

		mapCourse.get("CZ2001").setIndexNumber(1024, indexList.get(1024));
		mapCourse.get("CZ2001").setIndexNumber(1025, indexList.get(1025));

		// IRS: Should IndexNum be created in courselist constructor or should it be in
		// its own method?
		// For now i put it inside another method so once the CourseList object is
		// initialized,
		// we have to call the object method to assign the indexNum hasmap to its
		// attributes.
		// I have tested this in a separate program.

		// write to ser file

		try {
			FileOutputStream fos = new FileOutputStream("CourseList.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(mapCourse);
			oos.close();
			fos.close();
		}

		catch (IOException ioe) {
			ioe.printStackTrace();
		}
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
	}

	// Default Index Creation
	public static void IndexCreate() {
		indexList = new HashMap<Integer, IndexNum>();

		IndexNum i1 = new IndexNum(1024, "Monday", "0900-1000", "SoftwareLab", 2);
		IndexNum i2 = new IndexNum(1025, "Monday", "1000-1100", "SoftwareLab", 2);

		indexList.put(1024, i1);
		indexList.put(1025, i2);
	}

	// Also part of the above
	public static void setIndexToCourse() {
		mapCourse.get("CZ2001").setIndexNumber(1024, indexList.get(1024));
		mapCourse.get("CZ2001").setIndexNumber(1025, indexList.get(1025));
	}

	// Part of the method to print registered student by index (Admin Function)
	public static void printStudentByIndex(int index) {
		indexList.get(index).printStudentListOfIndex();
	}

	// Part of the method to print registered student by course (Admin Function)
	public static void printStudentByCourse(String courseCode) {
		mapCourse.get(courseCode).printStudentListOfCourse();
	}

	// PrintAll()
	// -- prints all courses in a list
	// --If yall very free, can also filter by school etc... but this isnt required
	public void PrintAll() {
		for (Map.Entry<String, Course> entry : mapCourse.entrySet()) {
			System.out.println(entry.getKey());
		}
	}

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

	public static void saveCourseMap() {
		try {
			FileOutputStream fos = new FileOutputStream("CourseList.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(mapCourse);
			oos.close();
			fos.close();
			return;
		}

		catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}
	}
	// Newcourse(String coursecode, Hashmap <int, int> indexNumMappedtoVacancies,
	// ...)
	// --for admin to add new courses
	// --Create new Course object & set attributes
	// --adds newly created Course object to hashmap with String coursecode as key
	// For method NewCourse, it is for the admin to add a totally new course into
	// the list

	public static void NewCourse(String courseCode, String school, String courseType) throws CourseAlreadyExist {
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
		Course c = new Course(courseCode, school, courseType);
		mapCourse.put(courseCode, c);
		CourseList.saveCourseMap(); // Save map immediately after creating new Course
		return;
	}

	public static void NewIndexNumber(String courseCode, int indexNumber, String day, String timeSlot, String venue,
			int vacancy) throws IndexAlreadyExist {
		if (indexList == null) {
			CourseList.loadCourseList();
		}
		if (indexList.containsKey(indexNumber)) {
			throw new CourseAlreadyExist("This Course Code number already exist!");
		}
		// TODO: Check whether the day, timeSlot and venue clashes with other
		// indexNumber by a timetable class
		IndexNum i = new IndexNum(indexNumber, day, timeSlot, venue, vacancy);
		indexList.put(indexNumber, i);
		mapCourse.get(courseCode).setIndexNumber(indexNumber, indexList.get(indexNumber));
		CourseList.saveCourseMap();
	}

	// IRS: Since the requirement didn't include remove course, should we not
	// include it?

	public void updateCourse(String currentCourseCode, String newCourseCode, String school,
			HashMap<Integer, indexNum> smthg)
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

	public static boolean AddCourse(String coursecode, int index, String username) {
		if (mapCourse.containsKey(coursecode)) {
			mapCourse.get(coursecode).addCourse(index, username);
			return true;
		}

		else {
			System.out.println("The course does not exist!\n");
			return false;
		}
	}
	// --for student to add new courses
	// --Use String coursecode to get Course object
	// --Call method addcourse from Course object
	// --Course.addcourse shold return a true/false value
	// --true = successfully added
	// --false = no more vacancies. Put on waitlist

	public static boolean DropCourse(String coursecode, int index, String username) {
		if (mapCourse.containsKey(coursecode)) {
			mapCourse.get(coursecode).dropCourse(index, username, false);
			return true;
		}

		else {
			return false;
		}

	}

	// Dropcourse(String coursecode, int index, String username)
	// --for student to drop courses
	// --Similar to CourseList.addcourse
	// --Course.dropcourse should return true/false
	// --true = successfully dropped course
	// --false = student not registered for that course index the first place.. so
	// unable to drop

	public static void SwopCourse(String student1, String student2, int index1, int index2, String coursecode) {

	}

	public static boolean checkCode(String coursecode) {
		if (mapCourse.containsKey(coursecode)) {
			return true;
		}

		else {
			return false;
		}

	}

	public static boolean checkIndex(String coursecode, int index) {
		if (mapCourse.containsKey(coursecode)) {
			Course c = mapCourse.get(coursecode);
			return c.checkIndex(index);
		}

		else {
			return false;
		}
	}

	// Swopcourse(String student1, String student2, int index1, int index 2, String
	// coursecode)
	// --to swop index with students

	public static void checkCourseVacancies(String coursecode) // For students to view the vacancy of all
	{ // the index in the course
		int[] temp = mapCourse.get(coursecode).getIndexNumber();
		System.out.println("Vacancy for course " + coursecode);
		System.out.println("Course/Vacancy ");
		for (int i = 0; i < temp.length; i++) {
			System.out.println((i + 1) + ") " + temp[i] + " / " + checkVacancies(temp[i]));
		}
	}

	public static int checkVacancies(int indexnum) { // For admin to print out the van
		return indexList.get(indexnum).getVacancy();
	}
	// TODO: printStudentList(String coursecode, int index)

	public static String getCoursecode(int index) {
		// FIXME: return the coursecode by index
		return mapCourse.get(index).getCourseCode();

	}

}
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