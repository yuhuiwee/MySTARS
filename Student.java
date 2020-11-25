import java.util.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Representation of all the students in MySTARS
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class Student extends Person {

    /**
     * Serialization of the object Student
     */
	private static final long serialVersionUID = 1L;

	/**
	 * Representation of all the elective type in the school
	 * @author Group 02
	 * @version 1.0
	 * @since 25 November 2020
	 */
	enum electiveType {
		CORE, MPE, GERPE, GERUE, UE,
	}

    /**
     * A String to electiveType hash map for the elective
     */
	private HashMap<String, electiveType> electivemap;

    /**
     * A String to integer hash map for the list of students registered courses
     */
	private HashMap<String, Integer> registeredCourses;

    /**
     * A String to integer hash map for the list of students under the wait list for courses
     */
	private HashMap<String, Integer> waitlistCourses;

    /**
     * A boolean on whether the student is planning to swap with another student
     */
	private boolean changeStatus = false;

    /**
     * A integer to integer hash map for the list of students waiting to swop courses / index
     */
	private HashMap<Integer, Integer> pendingSwap;

    /**
     * A String to array list hash map of the list of successful changes of swapping
     */
	private HashMap<String, ArrayList<Integer>> successChange;

    /**
     * The access time of the access period
     */
	private AccessPeriod accessTime;

    /**
     * The year of the student
     */
	private int year;

    /**
     * The timetable of the student
     */
	private Timetable timetable;

    /**
     * The total AUs of a student is registered to
     */
	private int totalau;

    /**
     * The maximum AUs a student can take
     */
	private int maxau;

    /**
     * A Student Constructor
     * @param user This the student's username
     * @param name This the student's name
     * @param matric This the student's matriculation number
     * @param gender This the student's gender
     * @param nationality This the student's nationality
     * @param major This the student's major
     * @param year This the student's year
     * @param email This the student's email
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     */
    public Student(String user, String name, String matric, char gender, String nationality, String major, int year,
			String email) throws UserAlreadyExists {
		//public Person(String username, String name, String matric, char gender, String nationality, String school, String email)
		super(user.toUpperCase(), name.toUpperCase(), matric.toUpperCase(), Character.toUpperCase(gender),
				nationality.toUpperCase(), major.toUpperCase(), email.toUpperCase());
		this.totalau = 0;
		this.year = year;
		registeredCourses = new HashMap<String, Integer>();
		waitlistCourses = new HashMap<String, Integer>();
		this.accessTime = new AccessPeriod(major, year);
		this.timetable = new Timetable();
		electivemap = new HashMap<String, electiveType>();
		maxau = 21; // default
		pendingSwap = new HashMap<Integer, Integer>();
		successChange = new HashMap<String, ArrayList<Integer>>();

		PasswordHash.addUserPwd(user.toUpperCase(), user.toUpperCase());
	}

    /**
     * The Type of elective available
     * @param i This is the users' choice in choosing which elective type
     * @return This is the chosen elective
     */
	private electiveType intToEnum(int i) {
		switch (i) {
			case 1:
				return electiveType.CORE;
			case 2:
				return electiveType.MPE;
			case 3:
				return electiveType.GERPE;
			case 4:
				return electiveType.GERUE;
			case 5:
				return electiveType.UE;
			default:
				return electiveType.UE;
		}
	}

    /**
     * Gets the course elective's index
     * @param course This is the course
     * @return This is the elective type
     */
	public int getCourseElectiveIndex(String course) {
		electiveType e = electivemap.get(course.toUpperCase());

		switch (e) {
			case CORE:
				return 1;
			case MPE:
				return 2;
			case GERPE:
				return 3;
			case GERUE:
				return 4;
			case UE:
				return 5;
			default:
				return 0;
		}
	}

    /**
     * Get the student's year
     * @return
     */
	public int getYear() {
		return year;
	}

    /**
     * Set the student's year
     * @param year
     */
	public void setYear(int year) {
		this.year = year;
	}

    /**
     * Student registering to a course
     * The student is able to see if there are any timetable clashes while registering
     * @param course This is the course the student wants to register
     * @param index This is the index number the student wants to enter
     * @param elec This is the elective type of the course
     * @throws CourseDontExist This exception is thrown when the course entered is not found
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    public void addCourse(String course, int index, int elec) throws CourseDontExist, TimetableClash,
            CloneNotSupportedException, VenueAlreadyExists {
        if (!CourseList.checkIndex(course, index)) {
            throw new CourseDontExist("Error! Course/Index does not match!");
        }
        if (!timetable.checkTimeslot(getIndexTimetable(index))) {
            throw new TimetableClash("Error! You are not allowed to register for courses with timetable clash!");
        }
        electivemap.put(course.toUpperCase(), intToEnum(elec));
        boolean courseadd = CourseList.addCourseByStudent(course.toUpperCase(), index, username); // CourseList.add
                                                                                                  // returns boolean
        if (courseadd) {
            System.out.println("Course added successfully!");
            totalau = totalau + CourseList.getCourse(course.toUpperCase()).getAU();// add au
            registeredCourses.put(course.toUpperCase(), index);
            timetable.mergeTimetable(getIndexTimetable(index));
        } else {
            System.out.println("Course index is full! You will be put on waitlist!");
            waitlistCourses.put(course, index);

        }

        PersonList.savePersonMap();
        CourseList.saveCourseMap();
	};

    /**
     * Student wants to drop / unregister from a course
     * @param course This is the course the student wants to drop
     * @param index This is the index number the student wants to get out of
     * @throws CourseDontExist This exception is thrown when the course entered is not found
     * @throws UserNotFound This exception is thrown when the username entered is not found
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    public void dropCourse(String course, int index) throws CourseDontExist, UserNotFound, UserAlreadyExists,
            CloneNotSupportedException, TimetableClash, VenueAlreadyExists {
        course = course.toUpperCase();
        int verify = verifyCourse(course);
        if (verify == -1) {
            throw new CourseDontExist("You are not registered for this course!");
        } else if (verify != index) {
            throw new CourseDontExist("You are not registered for this course!");
        }

        electivemap.remove(course);
        CourseList.dropCourseByStudent(course, index, username);
        if (waitlistCourses.containsKey(course)) {
            waitlistCourses.remove(course);

        } else if (registeredCourses.containsKey(course)) {
            registeredCourses.remove(course);
            totalau = totalau - CourseList.getCourse(course).getAU(); // minus au
            if (pendingSwap.containsKey(index)) {
                dropSwap(index);
            }
            timetable.removeTimetable(getIndexTimetable(index));
        }
	};

    /**
     * Check what courses the student is registered to and are on waiting list
     */
	public void checkCourse() {

		System.out.println("Registered Courses");
		for (Map.Entry<String, Integer> entry : registeredCourses.entrySet()) {
			System.out.println("Course: " + entry.getKey() + ", Index: " + entry.getValue());
			System.out.println("\tElective type: " + electivemap.get(entry.getKey()).name());
		}

		System.out.println("Courses on Waitlist");
		for (Map.Entry<String, Integer> entry : waitlistCourses.entrySet()) {
			System.out.println("Course: " + entry.getKey() + ", Index: " + entry.getValue());
			System.out.println("\tElective type: " + electivemap.get(entry.getKey()).name());
		}
	}

    /**
     * When two students wants to swap course index
     * @param course This is the course the two student wants to swap in
     * @param selfIndex This is the index student1 want to swap with student2
     * @param newIndex This is the new index student1 want to get from student2
     * @param newStudentUsername This is the username of the student whom the user(student) wants to swop with
     * @throws UserNotFound This exception is thrown when the username entered is not found
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     * @throws CourseDontExist This exception is thrown when the course entered is not found
     * @throws CourseAlreadyExist This exception is thrown when the course entered is already in the database / hash map
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    public void swopIndex(String course, int selfIndex, int newIndex, String newStudentUsername) throws UserNotFound,
            UserAlreadyExists, CourseDontExist, CourseAlreadyExist, TimetableClash, CloneNotSupportedException,
			VenueAlreadyExists {
		course = course.toUpperCase();
		int verify = verifyCourse(course);
		if (verify == -1) {
			throw new CourseDontExist("You are not registered for this course!");
		} else if (verify != selfIndex) {
			throw new CourseDontExist("You are not registered for this course!");
		}

		if (!swapIndexCheck(selfIndex, newIndex)) {
			throw new TimetableClash("Error! The new course clashes with your Timetable!");
		}

		if (pendingSwap.containsKey(selfIndex)) {
			throw new CourseAlreadyExist("You already have a pending swap with this course index!");
		}

		boolean swaped = Swop.swopStudent(course, selfIndex, newIndex, username, newStudentUsername.toUpperCase());
		if (swaped) {// if true, swap successful
			registeredCourses.replace(course, selfIndex, newIndex);
			System.out.println("Swap successful!");
			timetable.removeTimetable(getIndexTimetable(selfIndex));
			timetable.mergeTimetable(getIndexTimetable(newIndex));

		} else {// pending swap
			pendingSwap.put(selfIndex, newIndex);
		}

	}

    /**
     * Get the swop status of the students swopping their indexes
     * @return
     */
	public boolean getSwopStatus() {
		// If true call printswoppedlist()
		return changeStatus;
	}

    /**
     * Set the swop status of the students swopping their indexes
     * @param index1 This is the index of the course student1 wants to change
     * @param index2 This is the index of the course student2 wants to change
     * @param course This is the Index of the course both student wants to swap
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    public void setSwopstatus(int index1, int index2, String course) throws TimetableClash, CloneNotSupportedException,
			VenueAlreadyExists {
		course = course.toUpperCase();
		String text = "Successfully swopped course " + course + " from index " + String.valueOf(index1) + " to index "
				+ String.valueOf(index2);
		sendEmail("NTU STARS swop", text);
		this.changeStatus = true; // set true when swap with student2 successful
		pendingSwap.remove(index1); // remove from pending
		ArrayList<Integer> s = new ArrayList<Integer>();
		s.add(index1);
		s.add(index2);
		successChange.put(course, s);
		registeredCourses.replace(course, index1, index2); // replace with swopped index
		timetable.removeTimetable(getIndexTimetable(index1));
		timetable.mergeTimetable(getIndexTimetable(index2));

	}

    /**
     * Print list of courses successfully swopped
     */
	public void printChanges() {
		// Print list of courses successfully swopped
		if (successChange != null) {
			System.out.println("\n\n");
			for (Map.Entry<String, ArrayList<Integer>> entry : successChange.entrySet()) {
				ArrayList<Integer> s = entry.getValue();
				if (s.size() == 2) {
					System.out.println("Successfullly swapped " + entry.getKey() + " from index " + s.get(0)
							+ " to index " + s.get(1));
				} else if (s.size() == 1) {
					System.out.println(
							"You have been registered for " + entry.getKey() + " with course index " + s.get(0));
				}

			}
			successChange = null; // remove swop from hashmap
		}

		this.changeStatus = false; // reset swop status

	}

    /**
     * Prints the student's details
     */
	public void printStudentDetails() {
		System.out.printf("Name: %s, Gender: %s, Nationality: %s, Year: %d", name.toUpperCase(), gender,
				nationality.toUpperCase(), year);
	}

    /**
     * Get the access period's time
     * @return
     */
	public AccessPeriod getAccessPeriod() {
		return this.accessTime;
	}

    /**
     * Drop all the course in a school
     * @throws CourseDontExist This exception is thrown when the course entered is not found
     * @throws UserNotFound This exception is thrown when the username entered is not found
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    public void dropAllCourse() throws CourseDontExist, UserNotFound, UserAlreadyExists, CloneNotSupportedException,
			TimetableClash, VenueAlreadyExists {
		for (Map.Entry<String, Integer> entry : registeredCourses.entrySet()) {
			dropCourse(entry.getKey(), entry.getValue()); // also remove in pending swop
		}

		for (Map.Entry<String, Integer> entry : waitlistCourses.entrySet()) {
			dropCourse(entry.getKey(), entry.getValue());
		}
		registeredCourses = null;
		waitlistCourses = null;
		timetable = null;
		totalau = 0;
		changeStatus = false;
		successChange = null;
		pendingSwap = null;
	}

    /**
     * Student is successful in registering into a course from wait list
     * @param course This is the course the student was under the wait list
     * @param indexnum This is the index number the student was under the wait list
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    public void WaitingToRegistered(String course, int indexnum) throws TimetableClash, CloneNotSupportedException,
			VenueAlreadyExists {
		String text = "You have been registered for course " + course.toUpperCase() + " with index "
				+ String.valueOf(indexnum);
		sendEmail("Wait No More!", text);
		changeStatus = true;

		ArrayList<Integer> s = new ArrayList<Integer>();
		s.add(indexnum);
		successChange.put(course, s);

		waitlistCourses.remove(course);
		registeredCourses.put(course, indexnum);
		totalau = totalau + CourseList.getCourse(course).getAU();
		timetable.mergeTimetable(getIndexTimetable(indexnum));
		PersonList.savePersonMap();
	}

    /**
     * Check if student is registered for course
     * @param course This the course name
     * @return This is the value on whether the student is under registered, wait list or not registered of a course
     */
	public int verifyCourse(String course) {
		// check if student is registered for course
		course = course.toUpperCase();
		if (registeredCourses.containsKey(course)) {
			return registeredCourses.get(course);
		} else if (waitlistCourses.containsKey(course)) {
			return waitlistCourses.get(course);
		} else {
			return -1;
		}
	}

    /**
     * Students wants to retract the swapping of indexes with another student
     * @param selfindex This is the index the student initially wants to swap out
     * @throws CourseDontExist This exception is thrown when the course entered is not found
     */
	public void dropSwap(int selfindex) throws CourseDontExist {
		if (!pendingSwap.containsKey(selfindex)) {// sshould not be invoked if verified beforehand
			throw new CourseDontExist("You dont have any pending swaps with this index!");
		} else {
			boolean b = Swop.dropSwop(username, selfindex);
			if (b) {
				pendingSwap.remove(selfindex);
				System.out.println("Successfully dropped the Course Swop");
				return;
			}
		}
		return;
	}

    /**
     * Send an email to user for any changes / updates
     * @param subject This is the purpose of the email
     * @param text This is the content / body of the email
     */
	public void sendEmail(String subject, String text) {

		final String username = "cz2002ss1@gmail.com"; // to be added
		final String password = "CZ2002ss1!"; // to be added

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");

		Session session = Session.getInstance(props, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		});

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.email)); // to be
																								// added an
																								// email addr
			message.setSubject(subject);
			message.setText("Dear " + this.name.toUpperCase() + ": \n\n" + text);

			Transport.send(message);

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}

    /**
     * Print the timetable by weekly schedule
     */
	public void printTimeTableByWeek() {
		timetable.printWeeklySchedule();
	}

    /**
     * Print the timetable by course schedule
     */
	public void printTimetableByCourse() {
		timetable.printSchedule();
	}

    /**
     * Check if there is a timetable clash before swap
     * @param oldIndex This is the old course index which the student is registered to
     * @param newIndex This is the new course index which the student wants registered to
     * @return This is a true/false on whether there is a timetable clash
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    public boolean swapIndexCheck(int oldIndex, int newIndex) throws CloneNotSupportedException, TimetableClash,
			VenueAlreadyExists {
		// to check is timetable clash before swap!
		Timetable temp = (Timetable) timetable.clone();
		temp.removeTimetable(getIndexTimetable(oldIndex));
		if (!temp.checkTimeslot(getIndexTimetable(newIndex))) {
			return false;
		}
		return true;

	}

    /**
     * Get the total AUs registered
     * @return This is the total AUs registered
     */
	public int getAU() {
		return this.totalau;
	}

    /**
     * Get the Maximum AUs that a student can register
     * @return This is the Maximum AUs that a student can register
     */
	public int getMaxAU() {
		return this.maxau;
	}

    /**
     * Set the Maximum AUs that a student can register
     * @param au This is the new AU
     */
	public void setMaxAU(int au) {
		this.maxau = au;
	}

    /**
     * Check if the student is able to register more courses based on the number of AUs registered against the Maximum AUs
     * @param au This is the number of AUs the student wants to add
     * @return This is a true/false on whether the student's AUs is less / more than the maximum AUs
     */
	public boolean checkAU(int au) {
		if (maxau == totalau) {
			return false;
		} else if (maxau < totalau + au) {
			return false;
		} else {
			return true;
		}
	}

    /**
	 * Swapping of the student's elective courses
	 * @param course This is the course
	 * @param elec This is the elective
	 */
	public void swapElectiveType(String course, int elec) {
		if (verifyCourse(course.toUpperCase()) == -1) {
			return;// should not be invoked as check is doen in application class
		}
		this.electivemap.replace(course.toUpperCase(), intToEnum(elec));
		return;

	}

    /**
     * Get the elective type from the hash map
     * @param course this is the course where the elective is from
     * @return This is the course's elective type
     */
	public electiveType getElectiveTypeCourse(String course) {
		return this.electivemap.get(course.toUpperCase());
	}

	/**
     * Get the index class schedule
     * @param index This is the index number
     * @return This is the class schedule of an index number
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    private Timetable getIndexTimetable(int index)
			throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		return CourseList.getIndexNum(index).getClassSchedule();
	}

    /**
     * Gets the student's timetable
     * @return This is the timetable
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     */
	public Timetable getTimetable() throws CloneNotSupportedException {
		// return clone of timetable
		// Outside methods should not be able to edit student timetable
		Timetable t;
		try {
			t = (Timetable) timetable.clone();
		} catch (CloneNotSupportedException c) {
			c.printStackTrace();
			t = getTimetable();
		}
		return t;
	}

	/**
     * Checks if the student is registered to a course
     * @param course This is the course
     * @return This is a true/false value on whether the student is registered to a course
     */
	public boolean checkRegistered(String course) { //only return true if registered
		if (verifyCourse(course) == -1) {
			return false;
		}
		if (registeredCourses.containsKey(course)) {
			return true;
		} else if (waitlistCourses.containsKey(course)) {
			return false;
		} else {
			return false;
		}
	}

	 /**
     * Checks if the student is under wait list to a course
     * @param course This is the course
     * @return This is a true/false value on whether the student is under the wait list of a course
     */
    public boolean checkWaitList(String course){
        if (verifyCourse(course)==-1){
            return false;
        }
        if (registeredCourses.containsKey(course)){
            return false;
        }
        if (waitlistCourses.containsKey(course)){
            return true;
        }
        return false;
    }


}
