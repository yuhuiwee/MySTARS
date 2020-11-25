import java.io.Serializable;
import java.util.*;

/**
 * Represents the index numbers in MySTARS
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class IndexNum implements Serializable {

    /**
     * Serialization of the object IndexNum
     */
	private static final long serialVersionUID = 1L;

    /**
     * The vacancy of each index number
     */
	private int vacancy;

    /**
     * The index's number
     */
	private int indexNumber;

    /**
     * The timetable of each class schedule
     */
	private Timetable classSchedule;

    /**
     * The list of registered students
     */
	private ArrayList<String> listOfRegisteredStudents;

    /**
     * The list of students under the waiting list of an index
     */
	private ArrayList<String> waitingList;

	/**
     * A IndexNum constructor
     * Creates an index with its number and vacancy
     * Creates a empty list of registered students and waiting list
     * @param indexNumber This is the index's number
     * @param vacancy This is the number of index's vacancy
     */
	public IndexNum(int indexNumber, int vacancy) {
		this.indexNumber = indexNumber;
		this.vacancy = vacancy;
		this.classSchedule = new Timetable();
		listOfRegisteredStudents = new ArrayList<String>(); // So when index 1024 created, it also creates
		waitingList = new ArrayList<String>(); // these 2 lists.
	}

    /**
     * Get the class schedule
     * @return This is the class schedule
     */
    /* ----------- Get Methods ----------- */
	public Timetable getClassSchedule() {
		return classSchedule;
	}

    /**
     * Get the Index number
     * @return This is the Index's number
     */
	public int getIndexNumber() {
		return indexNumber;
	}

    /**
     * Get the Vacancy
     * @return This is the number of index's vacancy
     */
	public int getVacancy() {
		return vacancy;
	}

    /**
     * Get the list of registered students
     * @return This is a list of registered students
     */
    public ArrayList<String> getRegisteredStudentList() {
        return listOfRegisteredStudents;
    }

    /* ----------- Set Methods ----------- */
    /**
     * Set the vacancy of the index
     * @param vacancy This is the new number of index's vacancy
     */
	public void setVacancy(int vacancy) {
		this.vacancy = vacancy;
		CourseList.saveCourseMap();
	}

    /**
     * Set a new index number
     * @param indexNumber This is the new index's number
     */
    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
        CourseList.saveCourseMap();
    }

    /* ----------- Normal Methods ----------- */
    /**
     * Adding a new student to an index
     * @param username This is the student's username
     * @return This is a true/false on whether the student is successful in registering their chosen index
     */
	public boolean addStudent(String username) {
		// So there will be list of students available in application
		// But this will be initiated by the student class
		int temp = this.vacancy;
		if (temp > 0) {
			listOfRegisteredStudents.add(username);
			this.vacancy = temp - 1;
			System.out.println("You have been added to the index: " + this.indexNumber);
			CourseList.saveCourseMap();
			return true;
		} else {
			waitingList.add(username);
			return false;
		}
	}

    /**
     * Removing a student from an index
     * @param coursecode This is course code the student wants to unregister from
     * @param username This is the student's username
     * @param swopFlag This is a boolean operation in the case where the student wants to unregister due to swapping with another student
     * @throws UserNotFound This exception is thrown when the username entered is not found
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    public void removeStudent(String coursecode, String username, boolean swopFlag)
			throws UserNotFound, UserAlreadyExists, CloneNotSupportedException, TimetableClash, VenueAlreadyExists {
		// flag is for the swop to happen without adding in the students in the waiting
		// list
		int temp = this.vacancy;
		if (listOfRegisteredStudents.contains(username)) {
			listOfRegisteredStudents.remove(username);
		} else {
			waitingList.remove(username);
		}

		System.out.println("You have been removed from the index.");
		// Swop flag = True -> Just Increase vacancy without adding Students from
		// waitlist
		if (swopFlag || waitingList.isEmpty()) {
			this.vacancy = temp + 1;
			return;

		} else {// swopflag is false
			int i = 0;
			String user = waitingList.get(i);
			Student st = (Student) PersonList.getByUsername(user);
			while (!classSchedule.checkTimeslot(st.getTimetable())) {
				// iterate through waitlist to find 1st student that does not have timetable
				// clash
				i++;
				user = waitingList.get(i);
				st = (Student) PersonList.getByUsername(user);
			}
			listOfRegisteredStudents.add(user);
			st.WaitingToRegistered(coursecode, indexNumber); // inform student of swop
			waitingList.remove(i);
			CourseList.saveCourseMap();
			return;

		}
	}

    /**
     * Add the class schedule to the students' timetable
     * @param t This is a Timetable object
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     */
	public void addClassSchedule(Timetable t) throws TimetableClash {
		classSchedule.mergeTimetable(t);
	}

    /**
     * Print the index number and vacancy
     */
	public void printIndex() {
		System.out.println("Index Number Info: " + indexNumber);
		System.out.println("Vacancy: " + this.vacancy);
	}

    /**
     * Print the class weekly schedule
     */
	public void printWeeklySchedule() {
		classSchedule.printWeeklySchedule();
	}

    /**
     * Print the course class schedule
     */
	public void printCourseSchedule() {
		classSchedule.printSchedule();
	}

    /**
     * Prints the Student list of Course by index number
     * @throws UserNotFound This exception is thrown when the username entered is not found
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     */
    // ****** Method to print student by index ******
	public void printStudentList() throws UserNotFound, UserAlreadyExists {
		System.out.println("\nStudent list of Course index number " + String.valueOf(indexNumber) + ":\n");
		ListIterator<String> listOfRegisteredStudentsIterator = listOfRegisteredStudents.listIterator();
		int n = 1;
		while (listOfRegisteredStudentsIterator.hasNext()) {
			String name = listOfRegisteredStudentsIterator.next();
			Student s = (Student) PersonList.getByUsername(name);
			System.out.println("\t" + String.valueOf(n++) + ") Name: " + s.getName() + ", Gender: "
					+ String.valueOf(s.getGender()) + ", Nationality: " + s.getNationality());
		}
	}

    /**
     * Removing an index from the course and informing any registered students of the changes
     * @param courseCode This is the course code the index belongs to
     * @throws UserNotFound This exception is thrown when the username entered is not found
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     * @throws CourseDontExist This exception is thrown when the course entered is not found
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    public void deleteIndexNum(String courseCode) throws UserNotFound, UserAlreadyExists, CourseDontExist,
			CloneNotSupportedException, TimetableClash, VenueAlreadyExists {
		if (!listOfRegisteredStudents.isEmpty()) {
			ListIterator<String> i = listOfRegisteredStudents.listIterator();

			//remove registeration for all students
			while (i.hasNext()) {
				String name = i.next();
				Student s = (Student) PersonList.getByUsername(name);
				s.dropCourse(courseCode, indexNumber);
				String text = "\nThis email is to inform you that Course: " + courseCode + ", Index" + indexNumber
						+ " has been deleted by admin. You have been de-registered from this course.";
				s.sendEmail("Course has been removed", text);
			}

		}

		//remove waiting list
		if (!waitingList.isEmpty()) {
			ListIterator<String> i = waitingList.listIterator();

			//remove registeration for all students
			while (i.hasNext()) {
				String username = i.next();
				Student s = (Student) PersonList.getByUsername(username);
				s.dropCourse(courseCode, indexNumber);
				String text = "\nThis email is to inform you that Course: " + courseCode + ", Index" + indexNumber
						+ " has been deleted by admin. You have been removed from the waiting list of this course.";
				s.sendEmail("Course has been removed", text);
			}
		}

		VenueList.removetimetable(classSchedule);
		classSchedule = null;
		listOfRegisteredStudents = null;
		waitingList = null;
		return;
	}

	/**
     * Retrieving the list of students in the waiting list of an index
     * @return This is the list of students waiting to be accepted into a index
     */
    public ArrayList<String> getWaitList(){
        return waitingList;
    }
}
