
import java.util.*;
import java.io.*;

public class CourseList {

	private static HashMap<String, Course> mapCourse;
	private static HashMap<String, ArrayList<String>> schoolMapCourse;
	private static HashMap<Integer, String> indexMap;
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
	public CourseList() throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		mapCourse = new HashMap<String, Course>();
		indexMap = new HashMap<Integer, String>();

		schoolMapCourse = new HashMap<String, ArrayList<String>>();
		schoolMapCourse.put("COMPUTER SCIENCE", new ArrayList<String>());
		schoolMapCourse.put("COMPUTER SCIENCE", new ArrayList<String>());
		schoolMapCourse.put("PHILOSOPHY", new ArrayList<String>());
		schoolMapCourse.put("ARTS", new ArrayList<String>());
		schoolMapCourse.put("BUSINESS", new ArrayList<String>());
		schoolMapCourse.get("COMPUTER SCIENCE").add("CZ2001");
		schoolMapCourse.get("COMPUTER SCIENCE").add("CZ2002");
		schoolMapCourse.get("PHILOSOPHY").add("PH2018");
		

		// Create new course
		Course c1 = new Course("CZ2001", "COMPUTER SCIENCE");
		Course c2 = new Course("CZ2002", "ENGINEERING");
		Course c3 = new Course("PH2018", "PHILOSOPHY");

		// Create IndexNum Objects (Index Number, Vacancy)
		IndexNum i1 = new IndexNum(1024, 10);
		IndexNum i2 = new IndexNum(1025, 2);
		IndexNum i3 = new IndexNum(1026, 10);
		IndexNum i4 = new IndexNum(1027, 10);
		IndexNum i5 = new IndexNum(1028, 10);
		IndexNum i6 = new IndexNum(1029, 10);
		
		new VenueList();
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
		sc2.addClass(210002, 212002, "CZ2001", 1024, "Tutorial", "TR1");
		sc3.addClass(213002, 214002, "CZ2001", 1025, "Tutorial", "TR1");
		sc4.addClass(311002, 313002, "CZ2002", -1, "Lecture", "LT2A");
		sc5.addClass(311002, 313002, "PH2018", -1, "Lecture", "LT1A");
		sc6.addClass(210001, 212001, "CZ2002", 1026, "Lab", "SWLAB");
		sc7.addClass(210002, 212002, "CZ2002", 1027, "Lab", "SWLAB");

		VenueList.updateTimetable("LT2A", sc1.clone());
		VenueList.updateTimetable("TR1", sc2.clone());
		VenueList.updateTimetable("TR1", sc3.clone());
		VenueList.updateTimetable("LT2A", sc4.clone());
		VenueList.updateTimetable("LT1A", sc5.clone());
		VenueList.updateTimetable("SWLAB", sc6.clone());
		VenueList.updateTimetable("SWLAB", sc7.clone());
		
		

		// Add timetable clone to index and to venue

		//Add lecture for i1 and i2
		i1.addClassSchedule(sc1.clone());
		i2.addClassSchedule(sc1.clone());
		VenueList.updateTimetable("LT2A", sc1.clone());

		//Add tutorial for i1 and i2
		i1.addClassSchedule(sc2.clone());
		VenueList.updateTimetable("TR1", sc2.clone());
		i2.addClassSchedule(sc3.clone());
		VenueList.updateTimetable("TR1", sc3.clone());


		//Add lecture for i3 and i4
		i3.addClassSchedule(sc4.clone());
		i4.addClassSchedule(sc4.clone());
		VenueList.updateTimetable("LT2A", sc4.clone());

		//Add lab session for i3 and i4
		i3.addClassSchedule(sc6.clone());
		VenueList.updateTimetable("SWLAB", sc6.clone());
		i4.addClassSchedule(sc7.clone());
		VenueList.updateTimetable("SWLAB", sc7.clone());

		//Add lecture for i5 and i6
		i5.addClassSchedule(sc5.clone());
		i6.addClassSchedule(sc5.clone());
		VenueList.updateTimetable("LT1A", sc5.clone());

		// Create HashMap to put index for each course (Index Number, index Number object)
		HashMap<Integer, IndexNum> cz2001 = new HashMap<Integer, IndexNum>();
		cz2001.put(1024, i1);
		cz2001.put(1025, i2);

		HashMap<Integer, IndexNum> cz2002 = new HashMap<Integer, IndexNum>();
		cz2002.put(1026, i3);
		cz2002.put(1027, i4);

		HashMap<Integer, IndexNum> ph2018 = new HashMap<Integer, IndexNum>();
		ph2018.put(1028, i5);
		ph2018.put(1029, i6);
		// Save index number map of each course to indexlist

		mapCourse.put("CZ2001", c1);
		mapCourse.put("CZ2002", c2);
		mapCourse.put("PH2018", c3);

		newIndexNumbers(cz2001, "CZ2001");
		newIndexNumbers(cz2002, "CZ2002");
		newIndexNumbers(ph2018, "PH2018");

		
		VenueList.saveVenueMap();
		saveCourseMap();
	}

	@SuppressWarnings("unchecked")
	public static void loadCourseList() throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
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
				indexMap = (HashMap<Integer, String>) temp;
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
			FileInputStream fis = new FileInputStream("SchoolMap.ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			Object temp = ois.readObject();
			if (temp instanceof HashMap<?, ?>) {
				schoolMapCourse = (HashMap<String, ArrayList<String>>) temp;
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
			oos.writeObject(indexMap);
			oos.close();
			fos.close();
		}

		catch (IOException ioe) {
			ioe.printStackTrace();
			return;
		}

		try {
			FileOutputStream fos = new FileOutputStream("SchoolMap.ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(schoolMapCourse);
			oos.close();
			fos.close();
			return;
		}

		catch (IOException ioe) {
			//ioe.printStackTrace();
			return;
		}
	}

	// ****** PRINT ALL COURSE/BASED ON SCHOOL IN EXISTENCE ******
	public static void printAllCourse(String school)
			throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists // School entry only used for admin
																					// updating course. Other purpose
														// will have
	{ // school parameter default value of null
		if (mapCourse == null | indexMap==null | schoolMapCourse==null){
			loadCourseList();
		}
		
		int k = 0;
		System.out.println("\n\nCurrent Courses available:");
		if (school.equals("All"))	//Print all course in existence
		{
			for(String j: mapCourse.keySet())
			{
				checkCourseVacancies(j);
			}
		}
		else //Print course by school
		{
			ArrayList<String> temp = schoolMapCourse.get(school);
			if (temp!=null){
				for (int i = 0; i < temp.size(); i++) {
					System.out.print(temp.get(i) + "\t");
					k++;
					if (k % 4 == 0) {
						System.out.print("\n");
					}
				}
			}
			else{
				System.out.println("There are no courses registered under " + school);
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
	public static void printAllIndex() throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		if (mapCourse == null | indexMap==null | schoolMapCourse==null){
			loadCourseList();
		}
		// iterate through and get sorted index num keys
		for (Map.Entry<Integer,String> entry : indexMap.entrySet()) {
			System.out.println(entry.getKey());
		}
	}

	// ****** Method to check course existence ******
	public static boolean checkCourseExistence(String courseCode, String school)
			throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		if (mapCourse == null | indexMap==null | schoolMapCourse==null){
			loadCourseList();
		}
		courseCode = courseCode.toUpperCase();
		if (school.equals("All"))
		{
			if (mapCourse.containsKey(courseCode))
				return true;
			else {
				return false;
			}
		}
		else {
			if(schoolMapCourse.containsKey(courseCode))		// Makes sure admin from school A can only
				return true;									// update course from school A
			else
				return false;
		}
		
	}

	// ****** Method to check index existence ******

	//TODO: Edited method.. check
	public static boolean checkIndexExistence(int index)
			throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		if (mapCourse == null | indexMap==null | schoolMapCourse==null){
			loadCourseList();
		}
		if (indexMap.containsKey(index)){
			return true;//return true if index exists
		}
		else{
			return false;
		}
		
	}

	// ****** Method to create new course ******
	public static Course newCourse(String courseCode, String school)
			throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		if (mapCourse == null | indexMap==null | schoolMapCourse==null){
			loadCourseList();
		}
		schoolMapCourse.get(school.toUpperCase()).add(courseCode.toUpperCase());		//Set course to school grouping
		Course c = new Course(courseCode.toUpperCase(), school.toUpperCase());
		mapCourse.put(courseCode.toUpperCase(), c);
		CourseList.saveCourseMap(); // Save map immediately after creating new Course
		return c;
	}

	// ****** Method to Create a new Index Number for a Course ******
	public static void newIndexNumbers(HashMap<Integer, IndexNum> map, String course)
			throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		//indexmap.putAll(indexmap);
		//NOTE: Cannot use putall method cus it puts a COPY inside the hashmap.

		getCourse(course.toUpperCase()).setIndexNumber(map);

		for (Map.Entry<Integer, IndexNum> entry : map.entrySet())
		{
			indexMap.put(entry.getKey(), course.toUpperCase());
		}

	}

	// ****** CHANGE COURSE CODE NAME (Admin) ******
	public static void updateCourseCode(String newCourseCode, String currentCourseCode)
			throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		String school;
		if (mapCourse == null | indexMap==null | schoolMapCourse==null){
			loadCourseList();
		}
		Course c = getCourse(currentCourseCode);
		c.setcourseCode(newCourseCode);
		school = c.getSchool();
		schoolMapCourse.get(school).add(newCourseCode);
		schoolMapCourse.get(school).remove(currentCourseCode);	//Remove the currentCourseCode string in the school group
		mapCourse.put(newCourseCode, c);
		mapCourse.remove(currentCourseCode);
		CourseList.saveCourseMap();
	}

	public static boolean addCourseByStudent(String coursecode, int index, String username) throws CourseDontExist,
			TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		if (mapCourse == null | indexMap==null | schoolMapCourse==null){
			loadCourseList();
		}
		coursecode = coursecode.toUpperCase();
		if (mapCourse.containsKey(coursecode)) {
			Course c = getCourse(coursecode);
			boolean bool = c.addCourse(index, username);
			saveCourseMap();
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
			throws CourseDontExist, UserNotFound, UserAlreadyExists, CloneNotSupportedException, TimetableClash,
			VenueAlreadyExists {

		if (mapCourse == null | indexMap==null | schoolMapCourse==null){
			loadCourseList();
		}

		coursecode = coursecode.toUpperCase();

		if (mapCourse.containsKey(coursecode)) {
			mapCourse.get(coursecode).dropCourse(index, username, false);// swopflag=false
			saveCourseMap();
			return;
		}

		else {
			throw new CourseDontExist("The course does not exist!\n");
		}

	}
	// ********** BASICALLY REMOVE COURSE **********
	public static void dropCourseByAdmin(String coursecode) throws UserNotFound, UserAlreadyExists, CourseDontExist,
			TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		if (mapCourse == null | indexMap==null | schoolMapCourse==null){
			loadCourseList();
		}
		coursecode = coursecode.toUpperCase();
		if (!(mapCourse.containsKey(coursecode))) {
			System.out.println("The course does not exist!\n");
		}
		Course c = getCourse(coursecode);
		
		//Remove from school map
		String school = c.getSchool();
		ArrayList<String> a = schoolMapCourse.get(school);
		a.remove(coursecode);
		schoolMapCourse.replace(school, a);

		//Remove from indexmap
		ArrayList<Integer> indexes = c.getIndexNumber();
		ListIterator<Integer> i = indexes.listIterator();
		while (i.hasNext()){
			indexMap.remove(i.next());
		}

		//remove all indexes
		c.removeCourse();

		//remove from coursemap
		mapCourse.remove(coursecode);
		saveCourseMap();


	}

	// Dropcourse(String coursecode, int index, String username)
	// --for student to drop courses
	// --Similar to CourseList.addcourse
	// --Course.dropcourse should return true/false
	// --true = successfully dropped course
	// --false = student not registered for that course index the first place.. so
	// unable to drop

	public static void SwopCourse(String student1, String student2, int index1, int index2, String coursecode)
			throws UserNotFound, UserAlreadyExists, CourseDontExist, CloneNotSupportedException, TimetableClash,
			VenueAlreadyExists {

		if (mapCourse == null | indexMap==null | schoolMapCourse==null){
			loadCourseList();
		}

		coursecode = coursecode.toUpperCase();

		if (mapCourse.containsKey(coursecode)) {
			Course c = mapCourse.get(coursecode);
			c.dropCourse(index1, student1, true);
			c.dropCourse(index2, student2, true);
			c.addCourse(index1, student2);
			c.addCourse(index2, student1);
			saveCourseMap();
			return;

		} else {
			throw new CourseDontExist("The course does not exist!\n");
		}
	}

	public static boolean checkSchoolExistence(String school)
	{
		return schoolMapCourse.containsKey(school);
	}

	public static void updateSchool(Course c, String courseCode, String newSchool)
	{
		String currentSchool;
		currentSchool = c.getSchool();
		schoolMapCourse.get(currentSchool).remove(courseCode); 
		schoolMapCourse.get(newSchool).add(courseCode);
		c.setSchool(newSchool);
		saveCourseMap();
	}

	// ****** Method to check course vacancy ******
	public static void checkCourseVacancies(String coursecode) throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists { // For students to view the vacancy of all the index in
																	// course
		if (mapCourse == null | indexMap==null | schoolMapCourse==null){
			loadCourseList();
		}

		coursecode = coursecode.toUpperCase();

		//int[] temp = mapCourse.get(coursecode).getIndexNumber();
		ArrayList<Integer> listOfIndex = getCourse(coursecode).getIndexNumber();
		System.out.println("Vacancy for course " + coursecode);
		System.out.println("Index/Vacancy ");
		ListIterator<Integer> i = listOfIndex.listIterator();
		while(i.hasNext()){
			int index = i.next();
			System.out.println(String.valueOf(index)+ " / "+getIndexNum(index).getVacancy());
		}
	}

	// *********** CHECK INDEX EXISTENCE IN COURSE ***************
	public static boolean checkIndex(String coursecode, int index) throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists{
		if (mapCourse == null | indexMap==null | schoolMapCourse==null){
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

	public static IndexNum getIndexNum(int index)
			throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		if (mapCourse == null | indexMap==null | schoolMapCourse==null){
			loadCourseList();
		}
		String coursecode = indexMap.get(index);
		Course c = getCourse(coursecode);
		return c.getIndexNum(index);
	}

	public static Course getCourse(String course)
			throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		if (mapCourse == null | indexMap==null | schoolMapCourse==null){
			loadCourseList();
		}
		course = course.toUpperCase();
		return mapCourse.get(course);
	}

	// Swopcourse(String student1, String student2, int index1, int index 2, String
	// coursecode)
	// --to swop index with students

}