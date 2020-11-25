import java.io.Serializable;
import java.util.*;

/**
 * Represents the Courses in MySTARS
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class Course implements Serializable {

	// private static ArrayList<String> schoolType;
	/**
	 * Serialization of the object Course
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * A string containing the Course Code
	 */
	private String courseCode;

	/**
	 * A string containing the School of each Course Code
	 */
	private String school;

	/**
	 * The AU of each Course
	 */
	private int au;
	// private String userName;
	/**
	 * A hash map to obtain the object indexNum
	 */
	private HashMap<Integer, IndexNum> mapIndex; // The Integer in this case is the name of the IndexNum. So for
													// example if i want to find for index 1024, i can simply use
													// mapIndex.get(1024) to obtain the object indexNum.

	/**
	 * Setting the current Course with the users' input
	 * @param courseCode This is the course code of a course
	 * @param school This is the school the course belongs to
	 */
	public Course(String courseCode, String school) {
		this.courseCode = courseCode.toUpperCase();
		this.school = school.toUpperCase();
		mapIndex = new HashMap<Integer, IndexNum>();
		au = 3;
	}

	
	/** 
	 * @return String
	 */
	/*
	 * public static void createSchoolType() { schoolType = new ArrayList<String>();
	 * schoolType.add("Computer Science"); schoolType.add("Engineering");
	 * schoolType.add("Philodophy"); }
	 * 
	 * public static void printSchoolType() { for (String i : schoolType) {
	 * System.out.println(i); } }
	 * 
	 * public static boolean checkSchoolExistence(String school) { if
	 * (schoolType.contains(school)) return true; return false; }
	 */
	/* ----------- Get Methods ----------- */

	/**
	 * Get the Course Code
	 * @return This is the course code
	 */
	public String getCourseCode() {
		return courseCode;
	}

	/**
	 * Get the School
	 * @return This is the school
	 */
	public String getSchool() {
		return school;
	}

	/**
	 * Get the Index number from an array list
	 * @return This is the list of index
	 */
	public ArrayList<Integer> getIndexNumber() {
		ArrayList<Integer> listOfIndex = new ArrayList<Integer>(mapIndex.keySet());
		return listOfIndex;
	}

	/**
	 * Get the number of AU
	 * @return This is the AU of a course
	 */
	public int getAU() {
		return au;
	}

	/* ----------- Set Methods ----------- */
	/**
	 * Set the AU
	 * @param newAU This is the new AU
	 */
	public void setAU(int newAU) {
		this.au = newAU;
	}

	/**
	 * Set the Course Code
	 * @param courseCode This is the new course code
	 */
	public void setcourseCode(String courseCode) {
		this.courseCode = courseCode;
	}

	/**
	 * Set the School
	 * @param school This is the new School 
	 */
	public void setSchool(String school) {
		this.school = school;
	}

	/**
	 * Set the Index number into the hash map
	 * @param indexMap This is the new Index number 
	 */
	public void setIndexNumber(HashMap<Integer, IndexNum> indexMap) {
		for (Map.Entry<Integer, IndexNum> entry : indexMap.entrySet()) {
			mapIndex.put(entry.getKey(), entry.getValue());
		}
	}

	/* ----------- Normal Methods ----------- */

	/**
	 * For Students to add course / Register courses
	 * @param index This is the course's index number
	 * @param userName This is the student's username
	 * @return This is the return value (true / false) on whether the student is successful in registering to a course
	 */
	public boolean addCourse(int index, String userName) // for students to add course
	{
		IndexNum ind = getIndexNum(index);
		boolean bool = ind.addStudent(userName); // mapIndex.get(course) = object
		return bool;
	}

	/**
	 * For Student to drop the course they had previously registered
	 * @param index This is the course index number
	 * @param username This is the student's username
	 * @param swopFlag This is whether the student is swopping with another student (true / false)
	 * @throws UserNotFound This exception is thrown when the username entered is not found
	 * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
	 * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
	 * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
	 * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
	 */
	public void dropCourse(int index, String username, Boolean swopFlag)
			throws UserNotFound, UserAlreadyExists, CloneNotSupportedException, TimetableClash, VenueAlreadyExists {
		IndexNum ind = getIndexNum(index);
		ind.removeStudent(courseCode, username, swopFlag);
		return;
	}

	/**
	 * Get the vacancy of the given index
	 * @param indexNum This is the index's number
	 * @return This is the index's vacancy
	 */
	public int getIndexVacancy(int indexNum) {
		return mapIndex.get(indexNum).getVacancy();
	}

	/**
	 * Print the vacancy of the given index
	 * @param indexNum This is the index's number
	 */
	public void printVacancy(int indexNum) {
		mapIndex.get(indexNum).getVacancy();
	}

	/**
	 * Check if the given index exist
	 * @param index This is the index's number
	 * @return This is true/false on whether the given index is in the hash map
	 */
	public boolean checkIndex(int index) {
		if (mapIndex.containsKey(index)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Print the current Course code and Course school
	 */
	public void rse() {
		System.out.println("Course Info: " + courseCode);
		System.out.println("School: " + school);
	}

	/**
	 * Prints the student list by index
	 * @throws UserNotFound This exception is thrown when the username entered is not found
	 * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
	 */
	public void printStudentList() throws UserNotFound, UserAlreadyExists {
		System.out.println("Course: " + courseCode);
		for (Map.Entry<Integer, IndexNum> entryOfIndexNum : mapIndex.entrySet()) {
			entryOfIndexNum.getValue().printStudentList();
		}
	}

	// public ArrayList<String> getListOfStudents(Integer index){
	//     ArrayList<String> studentsListInIndex;

	//     for (Map.Entry<Integer, IndexNum> entryOfIndexNum : mapIndex.entrySet()) {
	//         studentsListInIndex = entryOfIndexNum.getValue().getRegisteredStudentList();
	//     }

	//     return studentsListInIndex;

	// }

	/**
	 * Prints the vacancy of all indexes
	 */
	public void printAllIndexVacancies() {
		for (Map.Entry<Integer, IndexNum> entry : mapIndex.entrySet()) {
			entry.getValue().printIndex();
		}
	}

	/**
     * Remove a course and its included index
     * @throws UserNotFound This exception is thrown when the username entered is not found
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     * @throws CourseDontExist This exception is thrown when the course entered is not found
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
	public void removeCourse() throws UserNotFound, UserAlreadyExists, CourseDontExist, CloneNotSupportedException,
			TimetableClash, VenueAlreadyExists {
		for (Map.Entry<Integer, IndexNum> entry : mapIndex.entrySet()) {
			IndexNum ind = entry.getValue();
			ind.deleteIndexNum(courseCode);
		}
		mapIndex = null;
		return;
	}

	/**
     * Print all index in a course
     */
	public void printAllIndex() {
		System.out.println("Course: " + courseCode);
		ArrayList<Integer> a = getIndexNumber();
		ListIterator<Integer> i = a.listIterator();
		while (i.hasNext()) {
			System.out.println("\t" + String.valueOf(i.next()));
		}
	}

	/**
     * Get the index number from the class IndexNum
     * @param index This is the user's input index's number
     * @return This is the index's number from the hash map
     */
	public IndexNum getIndexNum(int index) {
		return mapIndex.get(index);
	}

	/**
     * Change the index's number
     * @param oldindex This is the old index number
     * @param newindex This is the old index number
     * @throws CourseDontExist This exception is thrown when the course entered is not found
     */
	public void changeIndexNum(int oldindex, int newindex) throws CourseDontExist {
		if (!mapIndex.containsKey(oldindex)) {
			throw new CourseDontExist("Index number does not exist!");
		}

		IndexNum i = mapIndex.remove(oldindex);
		i.setIndexNumber(newindex);
		mapIndex.put(newindex, i);
	}

}