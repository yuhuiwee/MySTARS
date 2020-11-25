import java.util.*;
import java.io.*;
import java.time.*;

/**
 * Represents the main part of MySTARS
 * User is able to login and log out of the site
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class Application {

    /**
	 * The log in and log out phase of the user into our program
	 * Checks if the password entered is correct
	 * Check if the user is a Student or Admin
	 * @param args 
	 * @throws UserNotFound This exception is thrown when the username entered is not found
	 * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
	 * @throws WrongPassword This exception is thrown when wrong password is entered by the user
	 * @throws CourseDontExist This exception is thrown when the course entered is not found
	 * @throws AccessPeriodNotFound This exception is thrown when the access period entered is not found
	 * @throws CourseAlreadyExist This exception is thrown when the course entered is already in the database / hash map
	 * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
	 * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
	 * @throws NumberFormatException This exception is thrown when there is a number format error
	 * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
	 */
    public static void main(String[] args) throws UserNotFound, UserAlreadyExists, WrongPassword, CourseDontExist,
            AccessPeriodNotFound, CourseAlreadyExist, TimetableClash, CloneNotSupportedException, NumberFormatException,
			VenueAlreadyExists {

		Scanner sc = new Scanner(System.in);
		Console con = System.console();
		boolean check;
		String username;
		do {
			System.out.println("Welcome to MySTARS\nPlease login to continue");
			System.out.print("\nUsername: ");
			username = sc.next();
			System.out.print("\nPassword: ");
			char[] password = con.readPassword();

			if (!PersonList.checkusername(username)) {
				System.out.println("\n\tError! Username not found!\n\n");
				check = false;
			} else if (!PasswordHash.checkPwd(username, String.valueOf(password))) {
				check = false;
				System.out.println("\n\tError! Wrong password!\n\n");
			} else {
				check = true;
			}
		} while (!check);
		Person user = PersonList.getByUsername(username.toLowerCase());

		if (user instanceof Student) {
			Student st = (Student) user;
			if (AccessPeriod.checkAccessPeriod((Student) user)) {
				System.out.println("*** Welcome to MySTARS! ****");
				System.out.println(st.getName().toUpperCase()); // print user's name
				StudentApp(sc, st, username.toLowerCase(), con);

			} else {
				System.out.println("\n\tSorry, you are not allowed to access MySTARS\n\n");
				System.exit(0);
			}
		} else if (user instanceof Admin) {
			System.out.println("Welcome to MySTARS!");
			AdminApp(sc, (Admin) user, username.toLowerCase(), con);
		}
		sc.close();
	}

    /**
     * Represents the Student interface
     * Allows the student to Add Course, Drop Course, Print Courses Registered, Check Vacancies of an index number, Change Course Index number, Swop Index number with another student, Change Password, Edit Elective type, Print All courses and Print Timetable
     * @param sc This is a scanner to read the user's input value
     * @param st This is a student object
     * @param username This student's username
     * @param con This is the console to read the user's password
     * @throws UserNotFound This exception is thrown when the username entered is not found
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     * @throws WrongPassword WrongPassword This exception is thrown when wrong password is entered by the user
     * @throws CourseDontExist This exception is thrown when the course entered is not found
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    private static void StudentApp(Scanner sc, Student st, String username, Console con) throws UserNotFound,
            UserAlreadyExists, WrongPassword, CourseDontExist, TimetableClash, CloneNotSupportedException,
			VenueAlreadyExists {
		String coursecode, newuser;
		int indexnum;
		int index2;
		int elec, option;
		boolean check;
		Student s2;

		if (st.getSwopStatus()) { // Notify student if swop is successful
			st.printChanges();
		}

		int choice = 0;
		do {
			System.out.println("\n\n\nEnter one of the following options to continue");

			System.out.print(
					"1. Add Course\n2. Drop Course\n3. Print Courses Registered\n4. Check Vacancies\n5. Change Course Index number\n6. Swop Index number\n7. Change Password\n8. Edit Elective type\n9. Print All courses\n10. Print Timetable\n11.Quit\n");

			choice = getIntInput(sc);

			stapp: switch (choice) {
				case 1: // Add course
					do {
						CourseList.printAllCourse("All");
						do {
							System.out.print("Enter Course code (Enter Q to quit): ");
							coursecode = sc.next();
							if (coursecode.equals("q") | coursecode.equals("Q")) {
								System.out.println("\n\tQuitting...");
								break stapp;
							}
							if (!CourseList.checkCourseExistence(coursecode.toUpperCase(), "All")) {
								System.out.println("\n\tCourse does not exist!");
								check = false;
							} else if (st.checkRegistered(coursecode.toUpperCase())) {
								System.out.println(
										"\n\tError! You are already registered for this course! Please drop the course before trying again!");
								check = false;
							} else if (st.checkWaitList(coursecode.toUpperCase())) {
								check = false;
								System.out.println(
										"\n\tError! You are already put on waitlist for this course! Please drop the course before trying again!");
							} else {
								check = true;
							}
						} while (!check);
						check = false;
						System.out.print("Enter Index number: ");
						indexnum = getIntInput(sc);
						if (!CourseList.checkIndex(coursecode, indexnum)) {
							check = false;
							System.out.println("\n\tError! This index number does not exist for this course!");
						} else if (!CourseList.getIndexNum(indexnum).getClassSchedule()
								.checkTimeslot(st.getTimetable())) {
							check = false;
							System.out.println("\n\tError! You cannot register for courses with timetable clash!");
						} else {
							check = true;
						}
					} while (!check);
					do {
						System.out.println("Enter elective type:\n1. CORE\n2. Major PE\n3. GER-PE\n4. GER-UE\n5. UE");
						elec = getIntInput(sc);
						if (elec > 5 | elec < 1) {
							System.out.println("\n\tPlease enter a valid option!");
						}
					} while (elec > 5 | elec < 1);
					Course c = CourseList.getCourse(coursecode);
					if (!st.checkAU(c.getAU())) { // check if au exceeded
						System.out.println("\n\tMaxinum AUs exceeded!");
						break;
					}
					st.addCourse(coursecode, indexnum, elec);
					// CourseList.saveCourseMap();
					// CourseList.loadCourseList();
					break;

				case 2: // Drop course
					do {
						st.checkCourse();
						System.out.print("Enter Course code (Enter Q to quit): ");
						coursecode = sc.next();
						indexnum = st.verifyCourse(coursecode);
						if (coursecode.equals("Q") | coursecode.equals("q")) {
							System.out.println("\n\tQuitting...");
							break stapp;
						}
						if (indexnum == -1) {
							System.out.println("\n\tError! You are not registered for this course!");
						}

					} while (indexnum == -1);
					st.dropCourse(coursecode, indexnum);
					CourseList.saveCourseMap();
					CourseList.loadCourseList();
					break;
				case 3: // Print registered courses
					System.out.println("Total AU registered: " + String.valueOf(st.getAU()));
					st.checkCourse();
					break;
				case 4: // Check vacancies
					System.out.print("Enter course code (Enter Q to quit): ");
					coursecode = sc.next();
					if (coursecode.equals("Q") | coursecode.equals("q")) {
						System.out.println("\n\tQuitting...");
						break stapp;
					}
					if (CourseList.checkCourseExistence(coursecode, "All")) {
						CourseList.checkCourseVacancies(coursecode);
					} else {
						System.out.println("\n\tError! Please enter a valid course code!");
					}
					break;
				case 5: // Change course index number
					System.out.print("Enter course code (Enter Q to quit): ");
					coursecode = sc.next();
					if (coursecode.equals("Q") | coursecode.equals("q")) {
						System.out.println("\n\tQuitting...");
						break stapp;
					}
					int index = st.verifyCourse(coursecode);
					if (index == -1) {
						//this is should have been checked above!
						throw new CourseDontExist("You are not Registered for this course!"); //should not be thrown
					}
					System.out.print("Enter new Index Number: ");
					index2 = getIntInput(sc);
					if (!st.swapIndexCheck(index, index2)) {
						System.out
								.println("\n\tError! There is a time clash! You are not able to swop to this index!\n");
					}
					if (CourseList.getIndexNum(index2).getVacancy() > 0) {
						elec = st.getCourseElectiveIndex(coursecode);
						st.dropCourse(coursecode, index);
						st.addCourse(coursecode, index2, elec);
					} else {
						System.out.println(
								"\n\tCourse index full!\n Please choose another index of swop with another student\n");
					}
					break;
				case 6: // Swop course with another student
					do {
						System.out.print("Enter course code (Enter Q to quit): ");
						coursecode = sc.next();
						index = st.verifyCourse(coursecode);
						if (coursecode.equals("Q") | coursecode.equals("q")) {
							System.out.println("\n\tQuitting...");
							break stapp;
						}
						if (index == -1) {
							System.out.println("\n\tError! you are not registered for this course!");
						} else {
							if (!st.checkRegistered(coursecode)) {
								System.out.println("\n\tYou are currenly in waiting list for the course!");
								System.out.println("\tYou cannot swap courses on waiting list!");
								index = -1;
							}
						}
					} while (index == -1);

					check = false;
					do {
						System.out
								.println("Enter the username of the student you want to swop with (Enter Q to quit): ");
						newuser = sc.next();
						if (newuser.equals("Q") | newuser.equals("q")) {
							System.out.println("\n\tQuitting...");
							break stapp;
						}
						if (!PersonList.checkusername(newuser)) {
							check = false;
							System.out.println("\n\tError! User not found");
						} else {
							if (PersonList.getByUsername(newuser) instanceof Admin) {
								check = false;
								System.out.println("\n\tError! This is not a valid student username!");
							} else {
								check = true;
							}
						}
					} while (!check);

					s2 = (Student) PersonList.getByUsername(newuser);
					do {
						System.out.println("Enter the New Index Number that you want to swop to: ");
						index2 = getIntInput(sc);
						check = false;
						if (!CourseList.checkIndex(coursecode, index2)) {
							check = false;
							System.out.println("\n\tSorry this index does not exist for this course!");
						} else {
							int verify = s2.verifyCourse(coursecode);
							if (verify == -1) {
								check = false;
								System.out.println("\n\tStudent is not registered for this course!");
							} else if (verify != index2) {
								check = false;
								System.out.println("\n\tError! Student is not registered for this index!");
							} else {
								check = true;
							}
						}

					} while (!check);
					if (!st.swapIndexCheck(index, index2)) {
						System.out.println(
								"\n\tError! There is a time clash with the new index! Please try again with another person/index");
						break;
					}

					Swop.swopStudent(coursecode, index, index2, username, newuser);
					break;
				case 7:// change password
					check = false;
					do {
						System.out.println("\n\nUpdate Password");
						char[] orgpwd = con.readPassword("\tOld Password: ");
						char[] newpwd = con.readPassword("\tNew Password: ");
						char[] reppwd = con.readPassword("\tRepeat Password: ");
						if (String.valueOf(newpwd).equals(String.valueOf(reppwd))) {
							if (String.valueOf(orgpwd).equals(String.valueOf(newpwd))) {
								check = true;
							} else {
								check = false;
								System.out.println(
										"\n\tPlease enter a new password that is different from the current password!");
							}
						} else {
							check = false;
							System.out.println("\n\tPasswords dont match!");
						}
						if (check) {
							PasswordHash.changePwd(String.valueOf(orgpwd), String.valueOf(newpwd), username);
						}

					} while (!check);
				case 8:// change elective type for course
					System.out.print("Enter the course code to change (Enter Q to quit): ");
					coursecode = sc.next();
					if (coursecode.equals("Q") | coursecode.equals("q")) {
						System.out.println("\n\tQuitting...");
						break stapp;
					}
					System.out.println(
							"Enter elective type to change to:\n1. CORE\n2. Major PE\n3. GER-PE\n4. GER-UE\n5. UE");
					do {
						elec = getIntInput(sc);
						if (elec < 1 | elec > 5) {
							System.out.println("\n\tError! Please re-enter: ");
						}
					} while (elec < 1 | elec > 5);
					st.swapElectiveType(coursecode, elec);
					break;
				case 9:
					CourseList.printAllCourse("All");
					break;
				case 10:
					System.out.println("\n1. Print Timetable by week\n2. Print Timetable by course");
					option = getIntInput(sc);
					switch (option) {
						case 1:
							st.printTimeTableByWeek();
							break;
						case 2:
							st.printTimetableByCourse();
							;
							break;
					}
					break;
				case 11: // Quit
					System.out.println("\n\tThank you for using MySTARS!\nQuitting...");
					CourseList.saveCourseMap();
					PersonList.savePersonMap();
					VenueList.saveVenueMap();
					PasswordHash.saveHashMap();
					AccessPeriod.saveAccessPeriod();
					break;
				default:
					System.out.println("\n\tPlease enter a valid option!");
					break;
			}
		} while (choice < 11 & choice > 0);
	}

    /**
     * Represents the Admin's interface
     * Allow the Admin to edit/check student Access Period, add new student / course, update an existing course, check index number vacancies, print student list, remove student, edit student details and add new Admin
     * @param sc This is a scanner to read the user's input value
     * @param ad This is a Admin object
     * @param username This is the Admin's username
     * @param con This is the console to read the Admin's password
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     * @throws AccessPeriodNotFound This exception is thrown when the access period entered is not found
     * @throws UserNotFound This exception is thrown when the username entered is not found
     * @throws CourseDontExist This exception is thrown when the course entered is not found
     * @throws CourseAlreadyExist This exception is thrown when the course entered is already in the database / hash map
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws NumberFormatException This exception is thrown when there is a number format error
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    private static void AdminApp(Scanner sc, Admin ad, String username, Console con)
            throws UserAlreadyExists, AccessPeriodNotFound, UserNotFound, CourseDontExist, CourseAlreadyExist,
            TimetableClash, CloneNotSupportedException, NumberFormatException, VenueAlreadyExists {
        String s_name, newUser;
        String matric;
        char gender;
        String nationality;
        String major;
        Integer year;
        String email;
        int sel = 0;
        ZonedDateTime start;
        ZonedDateTime end;
        int f;
        int t;
        boolean check;
        int index;
        String courseCode;
        int au;

        
        do {
            System.out.print("(1) Edit/Check Student Access Period\n" + "(2) Add Student\n" + "(3) Add new course\n"
                    + "(4) Update an existing course\n" + "(5) Check index number vacancies\n"
                    + "(6) Print student list\n"
                    + "(7) Remove Student\n(8) Edit Student Details\n(9) Add new Admin\n(10) Quit\n");
            sel = getIntInput(sc);
            adapp:
            switch (sel) {
                case 1:// edit student access period
                    int acc = 0;
                    do {
                        System.out.println("(1) Print all Access Period\n(2) Edit default Access Period\n"
                                + "(3) Add new Access Period by Major/Year\n" + "(4) Remove Access Peiod for Major/Year\n"
                                + "(5) Remove all Access Peiod by Major/Year\n" + "(6) Quit");
                        acc = getIntInput(sc);
                        switch (acc) {
                            case 1:// print all access period
                                AccessPeriod.printAllAccess();
                                break;
                            case 2:// edit default access period
                                System.out.println("Start:");
                                start = inputDateTime(sc, Calendar.getInstance().get(Calendar.YEAR),
                                        Calendar.getInstance().get(Calendar.MONTH) + 1,
                                        Calendar.getInstance().get(Calendar.DATE),
                                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                                        Calendar.getInstance().get(Calendar.MINUTE));
                                // end date must be after start date
                                System.out.println("End: ");
                                end = inputDateTime(sc, start.getYear(), start.getMonthValue(), start.getDayOfMonth(),
                                        start.getHour(), start.getMinute());

                                AccessPeriod.editDefaultAccess(start, end);
                                break;
                            case 3:
                                // Add new access period by major
                                System.out.print("Enter major: ");
                                major = sc.next();
                                System.out.println("Enter year: \nFrom");
                                f = getIntInput(sc);
                                System.out.println("to ");
                                t = getIntInput(sc);

                                while (t < f) {
                                    System.out.println("\n\tPlease enter in ascending order");
                                    System.out.println("\nEnter year: \nFrom");
                                    f = getIntInput(sc);
                                    System.out.println("to ");
                                    t = getIntInput(sc);
                                }

                                while (t > 6 | f > 6) {
                                    System.out.println("\n\tPlease enter a valid year (<6)");
                                    System.out.println("\nEnter year: \nFrom");
                                    f = getIntInput(sc);
                                    System.out.println("to ");
                                    t = getIntInput(sc);
                                }

                                System.out.println("Start:");
                                // start date must be after today
                                start = inputDateTime(sc, Calendar.getInstance().get(Calendar.YEAR),
                                        Calendar.getInstance().get(Calendar.MONTH) + 1,
                                        Calendar.getInstance().get(Calendar.DATE),
                                        Calendar.getInstance().get(Calendar.HOUR_OF_DAY),
                                        Calendar.getInstance().get(Calendar.MINUTE));
                                // end date must be after start date
                                System.out.println("End: ");
                                end = inputDateTime(sc, start.getYear(), start.getMonthValue(), start.getDayOfMonth(),
                                        start.getHour(), start.getMinute());

                                for (int i = f; i <= t; i++) {
                                    AccessPeriod.changeMajorAccess(major, i, start, end);
                                }

                                break;
                            case 4:
                                // Remove access period by major/year

                                do {
                                    System.out.print("Enter major: ");
                                    major = sc.next();
                                    System.out.println("Enter year: \nFrom");
                                    f = getIntInput(sc);
                                    System.out.println("to ");
                                    t = getIntInput(sc);

                                    // error prompts
                                    if (t < f) {
                                        System.out.println("\n\tPlease enter in ascending order!");
                                    } else if (t > 6 | f > 6) {
                                        System.out.println("\n\tPlease enter a valid year! (max year = 6");
                                    }
                                } while (t < f | t > 6 | t > 6);

                                for (int i = f; i <= t; i++) {
                                    // method will print error message if access period not found
                                    // will not throw error message--> to allow user to continue in application
                                    AccessPeriod.removeMajorAccess(major, i);
                                }
                                break;
                            case 5:
                                // remove all but default
                                AccessPeriod.removeAllbutDefault();

                        }
                    } while (acc < 6);
                    break;
                case 2: // add a student
                    check = true;
                    do {
                        System.out.println("\n\nCurrent List of Students: ");
                        PersonList.printStudentList();
                        System.out.println("\n\n");
                        System.out.print("Enter student's username (Enter Q to quit): ");
                        newUser = sc.next();
                        if (newUser.equals("Q") | newUser.equals("q")){
                            System.out.println("\n\tQuitting...");
                            break adapp;
                        }
                        
                        check = PersonList.checkusername(newUser);
                        if (check) {
                            System.out.println("\n\tThis username is already taken!\n\n");
                        }
                    } while (check);
                    System.out.print("\nEnter student's name: ");
                    sc.nextLine();
                    s_name = sc.nextLine();
                    do {
                        System.out.print("\nEnter student's matric number: ");
						matric = sc.next();
    
                        check = PersonList.checkMatricNum(matric);
                        if (check) {
                            System.out.println("\n\tThis matric number is already taken!\n");
                        }
                    } while (check);

                    do{
                        System.out.print("\nEnter student's gender(M/F): ");
                        gender = sc.next().charAt(0);
                        gender = Character.toLowerCase(gender); 
                        if (gender!='m' & gender !='f'){
                        System.out.println("\n\tError! Please re-enter gender: ");
                        }
                    }while (gender!= 'f' & gender!= 'm');
                    System.out.print("\nEnter student's nationality: ");
                    nationality = sc.next();
                    System.out.print("\nEnter student's major: ");
                    sc.nextLine();
                    major = sc.nextLine();
                    do{
                        System.out.print("\nEnter student's year: ");
                        year = getIntInput(sc);
                        if (year>6){
                            System.out.println("\n\tPlease enter a value between 1 and 6!\n");
                        }
                        else if (year <1){
                            System.out.println("\n\tPlease enter a valid year!\n");
                        }
                    }while(year>6 | year < 1);
                    do{
                        System.out.print("\nEnter student's email: ");
                        email = sc.next();
                        if(!email.contains("@")){
                            System.out.println("\n\tInvalid email address!\n");
                            System.out.print("\tPlease re-enter email: ");
                            email = sc.next();
                        }
                    }while (!email.contains("@"));
                    PersonList.newStudent(newUser.toUpperCase(), s_name, matric, Character.toUpperCase(gender),
                            nationality, major, year, email);
                    System.out.println("\n\n");
                    PersonList.printStudentList();
                    System.out.println("\n\n");
                    break;

                case 3: //add new course
                    newCourse(sc, ad);
                    break;

                case 4: //update existing course
                    updateCourse(sc, ad);
                    break;
                case 5:// check vacancies
                    check = false;
                    do {
                        System.out.print("Enter index number: ");
                        index = getIntInput(sc);
                        check = CourseList.checkIndexExistence(index);
                        if (!check) {
                            System.out.println("\n\tThis course does not exist!");
                        }
                    } while (!check);
                    int vacancy = CourseList.getIndexNum(index).getVacancy();
                    System.out.println("The vacancy for index " + index + " is: " + vacancy + "\n\n");
                    break;
                case 6: //print student list
                    int option;
                    do {
                        System.out.println("(1) Print Student List by Course: ");
                        System.out.println("(2) Print Student List by Index Number: ");
                        System.out.println("(3) Go back to main menu ");
                        System.out.print("Pick your choice: ");
                        option = getIntInput(sc);

                        switch (option) {
                            case 1:
                                check = false;
                                do {
                                    System.out.print("Enter Course code: ");
                                    courseCode = sc.next();
                                    courseCode = courseCode.toUpperCase();
                                    check = CourseList.checkCourseExistence(courseCode, ad.getSchool());    //Admin school A update course from school A
                                    if (!check) {
                                        System.out.println("This course does not exist! You can only access"
                                        + " courses under " + ad.getSchool() + " school that you are registered to.");
                                    }
                                } while (!check);
                                CourseList.printStudentListByCourse(courseCode);
                                break;
                            case 2:
                                check = false;
                                do {
                                    System.out.print("Enter index number: ");
                                    index = getIntInput(sc);
                                    check = CourseList.checkIndexExistence(index);  //No constraints here tho compared to above
                                    if (!check) {
                                        System.out.println("\n\tThis index does not exist!");
                                    }
                                } while (!check);
                                CourseList.getIndexNum(index).printStudentList();                            
                                break;
                            case 3:
                                System.out.println("\n\tReturning to main menu..");
                                break;
                            default:
                                System.out.println("\n\tPlease choose a valid option!");
                        }

                    } while (option > 0 & option <= 2);
                    break;

                case 7:// remove student
                    check = false;
                    do {
                        System.out.println("Username of Student to be removed (Enter Q to quit): ");
                        username = sc.next();
                        if (username.equals("Q") | username.equals("q")){
                            System.out.println("\n\tQuitting...");
                            break adapp;
                        }
                        check = PersonList.checkusername(username);
                        if (!check) {
                            System.out.println("\n\tError! This Student does not exist!");
                        } else { // check if username entered is a student's username
                            if (PersonList.getByUsername(username) instanceof Student) {
                                check = true;
                            } else {
                                check = false;
                            }
                        }
                    } while (!check);
                    PersonList.removeStudent(username);
                    System.out.println("Current list of students: ");
                    PersonList.printStudentList();
                    System.out.println("\n\n");
                    break;

                case 8: // edit student details
                    check = false;
                    do {
                        System.out.println("Username of Student to edit (Enter Q to quit): ");
                        username = sc.next();
                        if (username.equals("Q") | username.equals("q")){
                            System.out.println("\n\tQuitting...");
                            break adapp;
                        }
                        check = PersonList.checkusername(username);
                        if (!check) {
                            System.out.println("\n\tError! This Student does not exist!");
                        } else { // check if username entered is a student's username
                            if (PersonList.getByUsername(username) instanceof Student) {
                                check = true;
                            } else {
                                check = false;
                            }
                        }
                    } while (!check);
                    Student s = (Student) PersonList.getByUsername(username);
                    s_name = s.getName();
                    matric = s.getMatric();
                    gender = s.getGender();
                    nationality = s.getNationality();
                    major = s.getSchool();
                    year = s.getYear();
                    email = s.getEmail();
                    au = s.getAU();
                    int editchoice;
                    do {
                        System.out.println("Choose which details to edit. Enter 8 to submit!");
                        System.out.println(
                                "(1) Name\n(2) Matric\n(3) Gender\n(4) Nationality\n(5) Major\n(6) Year\n(7) Email\n(8) Maximum AU\n(9) Done");

                        editchoice = getIntInput(sc);

                        switch (editchoice) {
                            case 1: // name
                                System.out.println("Current name: " + s_name);
                                System.out.print("\nNew Name: ");
                                s_name = sc.next();
                                s.setName(s_name);
                                break;
                            case 2:// matric
                                check = true;
                                do {
                                    System.out.println("Current matric: " + matric);
                                    System.out.print("\nNew Matrix: ");
                                    matric = sc.next();
                                    check = PersonList.checkMatricNum(matric);
                                    if (check) {
                                        System.out.println("\n\tError! This matric number is already taken!");
                                    }
                                } while (check);
                                s.setMatric(matric);
                                break;
                            case 3: // gender
                                System.out.println("Current gender: " + Character.toString(gender));
                                System.out.print("\nNew Gender (M/F): ");
                                gender = sc.next().charAt(0);
                                while (Character.toLowerCase(gender) != 'f' | Character.toLowerCase(gender) != 'm') {
                                    System.out.println("\n\tError! Please re-enter gender: ");
                                    gender = sc.next().charAt(0);
                                }
                                s.setGender(gender);
                                break;
                            case 4: // nationality
                                System.out.println("Current nationality: " + nationality);
                                System.out.print("\nNew Nationality: ");
                                nationality = sc.next();
                                s.setNationality(nationality);
                                break;
                            case 5: // major
                                System.out.println("Current major: " + major);
                                System.out.print("\nNew major: ");
                                major = sc.next();
                                s.setSchool(major);
                                break;
                            case 6: // year
                                System.out.println("Current year: " + String.valueOf(year));
                                System.out.print("\nUpdated Year: ");
                                do{
                                    year = getIntInput(sc);
                                    if (year <1 | year > 6){
                                        System.out.println("\n\tError! Please enter a valid yeat (1-6): ");
                                    }
                                }while (year <1 | year > 6);
                                
                                s.setYear(year);
                                break;
                            case 7: // email
                                System.out.println("Current email: " + email);
                                System.out.print("\nNew email: ");
                                email = sc.next();
                                while (!email.contains("@")) {
                                    System.out.println("\n\tError! Please enter a valid email: ");
                                    email = sc.next();
                                }
                                s.setEmail(email);
                                break;
                            case 9:// maxau
                                System.out.println("Current max AU: " + String.valueOf(au));
                                System.out.print("\nNew max AU: ");
                                au = getIntInput(sc);
                                s.setMaxAU(au);
                                break;
                            case 10:
                                System.out.println(username);
                                s.printStudentDetails();
                        }
                    } while (editchoice > 0 & editchoice < 9);
                    break;

                case 9:// add new admin
                    check = true;
                    do{
                        System.out.print("Enter new admin username (Enter Q to quit): ");
                        newUser = sc.next();
                        if (newUser.equals("Q") | newUser.equals("q")){
                            System.out.println("\n\tQuitting...");
                            break adapp;
                        }
                        check = PersonList.checkusername(newUser);
                        if(check){//if true --> username exists
                            System.out.println("\n\tUsername alreay exist! Please try again!\n");
                        }
                    }while (check);

                    System.out.print("Enter name: ");
                    s_name = sc.next();

                    check = true;
                    do{
                        System.out.print("Enter ID: ");
                        matric = sc.next();

                        check = PersonList.checkMatricNum(matric);
                        if (check){
                            System.out.println("\n\tID already taken! Please try again!\n");
                        }
                    }while(check);

                    do{
                        System.out.print("\nEnter gender(M/F): ");
                        gender = sc.next().charAt(0);
                        gender = Character.toLowerCase(gender);
                        if (gender!='m' | gender !='f'){
                        System.out.println("\n\tError! Please re-enter gender: ");
                        }
                    }while (gender!= 'f' | gender!= 'm');

                    System.out.print("\nEnter nationality: ");
                    nationality = sc.next();
                    System.out.print("\nEnter School (Eg. Computer Science): ");
                    major = sc.next();

                    System.out.print("Enter position: ");
                    String position = sc.next();

                    do{
                        System.out.print("\nEnter student's email: ");
                        email = sc.next();
                        if(!email.contains("@")){
                            System.out.println("\n\tInvalid email address!");
                            System.out.print("\nPlease re-enter email: ");
                            email = sc.next();
                        }
                    }while (!email.contains("@"));
                    
                    PersonList.newAdmin(newUser, s_name, matric, gender, nationality, major, position, email);
                    System.out.println("Successfully created a new admin: ");
                    System.out.println("Default login password will be the Admin's username (uppercase)\n");
                    Admin newadmin = (Admin) PersonList.getByUsername(newUser);
                    newadmin.printAdminDetails();
                    break;
                case 10:
                    System.out.println("\n\tThank you for using MYSTARS");
                    //save before exiting
                    CourseList.saveCourseMap();
                    PersonList.savePersonMap();
                    VenueList.saveVenueMap();
                    PasswordHash.saveHashMap();
                    AccessPeriod.saveAccessPeriod();
                    System.exit(0);

            }
        }

        while (sel != 10);
    }

	// ========= Normal Methods =========

    /**
     * (Admin) Input the Date and Time 
     * @param sc Scanner for user's input
     * @param minYear This is the year in yyyy format
     * @param minMonth This is the month in mm format
     * @param minDay This is the day in dd format
     * @param minhour This is the hour in 24-hour format
     * @param minmin This is the minute in 24-hour format
     * @return The date time in 24-hour format
     */
    private static ZonedDateTime inputDateTime(Scanner sc, int minYear, int minMonth, int minDay, int minhour,
			int minmin) {
		boolean mindate;
		String datestring = "";
		// Get year
		System.out.println("Enter Date: ");
		System.out.print("\nYear (yyyy): ");
		int yyyy = getIntInput(sc);
		while (yyyy < minYear) { // if admin enters year < current year
			System.out.println("\n\tError! Please re-enter!\n\nYear (yyyy): ");
			yyyy = getIntInput(sc);
		}
		datestring = datestring + String.valueOf(yyyy);
		// Get Month
		System.out.print("\nMonth(mm): ");
		int mm = getIntInput(sc);
		while (mm > 12 && (mm < minMonth & minYear == yyyy)) {
			// check if month is > 12
			// if current year entered, check if month is < current month
			System.out.println("\n\tError! Please re-enter!\n");
			System.out.print("\nMonth (mm): ");
			mm = getIntInput(sc);
		}
		datestring = datestring + "-" + String.format("%02d", mm);

		// Get Day
		System.out.print("Day (dd): ");
		int dd = getIntInput(sc);

		// Get max number of days in that month
		YearMonth yearMonthObject = YearMonth.of(yyyy, mm);
		int daysInMonth = yearMonthObject.lengthOfMonth(); //max number of days in a month

		while (dd > daysInMonth && (dd < minDay & mm == minMonth & minYear == yyyy)) {
			// check if month > max days in month entered
			// check if day entered is before the current date
			System.out.println("\n\tError! Please re-enter!\n");
			System.out.print("\nDay (dd): ");
			dd = getIntInput(sc);
		}

		datestring = datestring + "-" + String.format("%02d", dd);

		if (dd == minDay & mm == minMonth & yyyy == minYear) {
			mindate = true;
		} else {
			mindate = false;
		}

		// Get Time
		System.out.println("Enter Time: ");
		// Get hour
		System.out.print("\nHour (24 hour format) (HH): ");
		int HH = getIntInput(sc);
		while (HH > 24 && (mindate & HH < minhour)) {
			System.out.println("\n\tError! Please re-enter!\n");
			System.out.print("\nHour in 24 hour format (HH): ");
			HH = getIntInput(sc);

		}
		datestring = datestring + "T" + String.format("%02d", HH);

		// Get min
		System.out.print("\nMin (mm)");
		int MM = getIntInput(sc);
		while (MM >= 60 && (mindate & HH == minhour & MM < minmin)) {
			System.out.println("\n\tError! Please re-enter!\n");
			System.out.print("\nMin (mm)");
			MM = getIntInput(sc);
		}

		datestring = datestring + ":" + String.format("%02d", MM) + ":00";

		// Get TimeZone

		boolean check = false;
		String z = "";
		while (!check) {
			System.out.println("\nEnter TimeZone (eg. 00:00 for GMT and +08:00 for GMT +08:00)");
			System.out.println("Press Enter for Singapore Time Zone (Default)");
			String zone = sc.next();
			if (zone.isEmpty()) {
				check = true;
				z = "+08:00";
			} else if (zone.equals("00:00")) {
				check = true;
				z = "+00:00";
			} else if (zone.contains(":") & (zone.startsWith("+") | zone.startsWith("-"))) {
				String[] dt = zone.split(":");//split by ":"
				try {
					int h = Integer.parseInt(dt[0].substring(1));//remove "-"/"+"
					if (h < 0 | h > 12) {//check that gmt is between 0 - 12
						check = false;
					} else {
						check = true;
						z = zone;
					}
				} catch (NumberFormatException e) {
					check = false;
				}
			} else {
				System.out.println("\n\tError! Please re-enter in the correct format!\n");
				check = false;
			}
		}

		datestring = datestring + z;
		ZonedDateTime dt;
		try {
			dt = ZonedDateTime.parse(datestring);
		} catch (DateTimeException e) {
			System.out.println("\n\tError!\n");
			dt = inputDateTime(sc, minYear, minMonth, minDay, minhour, minmin); //retry if error
		}

		return dt;

	}

    /**
     * This method is for the Admin's use only
     * Creation of a new course with the variables course code, number of AUs, total number of indexes, the vacancy of each indexes and the class schedule
     * @param sc This is a scanner to read the user's input value
     * @param ad This is a Admin object
     * @throws CourseAlreadyExist This exception is thrown when the course entered is already in the database / hash map
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    // ****** CREATE NEW COURSE ******
    private static void newCourse(Scanner sc, Admin ad)
			throws CourseAlreadyExist, TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		String courseCode, school;
		int index, vacancy, classNum, totalIndex;
		HashMap<Integer, IndexNum> indexMap = new HashMap<Integer, IndexNum>();
		boolean check;
		IndexNum tempIndex;
		Timetable time = new Timetable();
		String courseType;
		int au;

		sc.nextLine();

		// ENTER COURSE CODE AND SCHOOL
		do {
			CourseList.printAllCourse("All"); // So if value "All", all courses will be printed out
			System.out.print("\nEnter the new Course Code (Enter Q to quit): ");
			courseCode = sc.next();
			if (courseCode.equals("Q") | courseCode.equals("q")) {
				System.out.println("\n\tQuitting...");
				return;
			}
			school = ad.getSchool();
			check = CourseList.checkCourseExistence(courseCode, "All");
			if (check) { // true == coursecode exists
				System.out.println("\n\tThis course alreay exists!\n");
			}
		} while (check);

		Course c = CourseList.newCourse(courseCode, school);

		System.out.print("\nEnter the number of credits (AU) Course " + courseCode + ": ");
		au = getIntInput(sc);
		c.setAU(au);

		// ENTER TOTAL NUMBER OF INDEX IN THIS NEW COURSE
		System.out.print("\nEnter the total number of indexes to add to the course: ");
		totalIndex = getIntInput(sc);

		//Create indexNum
		boolean indexExistence;
		ArrayList<Integer> tempintindex = new ArrayList<Integer>();
		System.out.println("\nIndexes that are in use: ");
		CourseList.printAllIndex();
		System.out.printf("\nEnter %d new index numbers to add to the course: ", totalIndex);

		for (int i = 0; i < totalIndex; i++) {
			do {
				System.out.print("\nIndex " + String.valueOf(i + 1) + ": ");
				index = getIntInput(sc);
				indexExistence = CourseList.checkIndexExistence(index);
				if (indexExistence) {
					System.out.println("\n\tThis index is already in use!");
					System.out.println("\tPlease re-enter!\n");
				}
				if (tempintindex != null && tempintindex.contains(index)) {
					indexExistence = true;
					System.out.println("\n\tError! You cannot enter the same index!");
				}
			} while (indexExistence);
			System.out.print("\nEnter the number of vacancies in index " + String.valueOf(index) + ": ");
			do {
				vacancy = getIntInput(sc);
				if (vacancy < 1) {
					System.out.println("\n\tError! Please enter a valid input!");
				}
			} while (vacancy < 1);
			tempIndex = new IndexNum(index, vacancy);
			tempintindex.add(index);
			indexMap.put(index, tempIndex);

		}

		System.out.println("\n=====================================================================================");
		System.out.println("Classes can only conduct from Monday to Friday, between 0800 to 1900 hrs."
				+ "\nAdjust your number of classes and duration accordingly.");
		System.out.println("\n=====================================================================================\n");

		System.out.print("\nEnter the number of lectures: ");
		classNum = getIntInput(sc);
		for (int i = 0; i < classNum; i++) {
			time = getCourseDetails(sc, "Lecture", courseCode, -1, time); //Missing the last timetable argument
		}
		System.out.print("\nEnter the total number of tutorials/seminars/labs per indexnum: ");
		// Num of classes per index number
		classNum = getIntInput(sc);

		Timetable temp = new Timetable();

		for (Map.Entry<Integer, IndexNum> entry : indexMap.entrySet()) { // For each index created in this course
			temp = time.clone(); // so as not to edit lecture timeslots
			index = entry.getKey();
			System.out.println("\nFor Index: " + String.valueOf(index));
			for (int i = 0; i < classNum; i++) { // Classes in this index
				System.out.println("Enter Course Type: \n\tEg. Tutorial,Seminar, Lab etc.");
				courseType = sc.next(); // will not be checking coursetype
				temp = getCourseDetails(sc, courseType, courseCode, index, temp);// Timetable clashes are handled inside this method
			}
			temp.removeTimetable(time);
			entry.getValue().addClassSchedule(temp);// add to indexnum timetable
		}

		CourseList.newIndexNumbers(indexMap, courseCode);// add to indexlist in courselist

		System.out.println("\nYou have successfully added the course");

		CourseList.printAllCourse("All"); // Requirement in the test case to list all the courses after the addition.

		// NOTE: For debugging purposes! Delete after debugging
		for (Map.Entry<Integer, IndexNum> entry : indexMap.entrySet()) {
			entry.getValue().printCourseSchedule();
		}

	}

    /**
     * This method is for the Admin's use only
     * Changing / Updating of a Course
     * Admin is able to change Course code name / school, remove a Course, add / edit / delete index numbers, update index number's vacancy and update index number's schedule
     * @param sc This is a scanner to read the user's input value
     * @param ad This is a Admin object
     * @throws NumberFormatException This exception is when the user enter a value other than a number
     * @throws TimetableClash This exception is when there is a timetable clash
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws UserNotFound This exception is thrown when the username entered is not found
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
     * @throws CourseDontExist This exception is thrown when the course entered is not found
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    // ****** Method to update a course ******
    private static void updateCourse(Scanner sc, Admin ad)
            throws NumberFormatException, TimetableClash, CloneNotSupportedException, UserNotFound, UserAlreadyExists,
			CourseDontExist, VenueAlreadyExists {
		String courseCode, newCourseCode;
		int choice, index, numClass, newIndex, option, vacancy;
		char yn;
		boolean check;
		Course c;
		IndexNum ind;
		Timetable t = new Timetable();
		Timetable time;
		char ch;
		String school;
		CourseList.printAllCourse(ad.getSchool()); // So for depending on which school the admin works for, it will only
													// print courses registered
													// under that school. So this prevents admin from school A from
													// modifying courses of school B

		check = false;
		do {
			System.out.print("\nEnter Course Code that you would like to update (Enter Q to quit): ");
			courseCode = sc.next();
			if (courseCode.equals("Q") | courseCode.equals("q")) {
				System.out.println("Quitting...");
				return;
			}
			courseCode = courseCode.toUpperCase();
			check = CourseList.checkCourseExistence(courseCode, ad.getSchool());
			if (!check) {
				System.out.println("\n\tError! Course Code not found!\n");
			}
		} while (!check);

		c = CourseList.getCourse(courseCode);

		System.out.println("\nWhat would you like to do with " + courseCode + " :");
		upapp: do {
			System.out.println("\n\n1) Change Course code name" + "\n" + "2) Change Course Code School" + "\n"
					+ "3) Remove Course" + "\n" + "4) Add/Delete index numbers" + "\n" + "5) Change index number digits"
					+ "\n" + "6) Update index number's vacancy" + "\n" + "7) Return to Main menu" + "\n");

			System.out.print("Please enter your choice: ");
			choice = getIntInput(sc);
			switch (choice) {
				case 1:
					// COURSE CODE NAME
					check = true;
					do {
						CourseList.printAllCourse("All");
						System.out.println("\nNote: Do not use any coursecode listed above as your new coursecode.\n");
						System.out.print("Enter new Course Code (Enter Q to quit): ");
						newCourseCode = sc.next();
						if (newCourseCode.equals("Q") | newCourseCode.equals("q")) {
							System.out.println("Quitting...");
							break upapp;
						}
						newCourseCode = newCourseCode.toUpperCase();
						check = CourseList.checkCourseExistence(newCourseCode, "All");
						if (check) {
							System.out.println(
									"\n\tThis Course code is already in use! Please choose another Course code!\n");
						}
					} while (check);

					CourseList.updateCourseCode(newCourseCode, courseCode);
					break;
				case 2:// UPDATE SCHOOL
					do {
						System.out.print("\nEnter the new school (Enter Q to quit): ");
						school = sc.next();
						if (school.equals("Q") | school.equals("q")) {
							System.out.println("Quitting...");
							break upapp;
						}
						school = school.toUpperCase();
						check = CourseList.checkSchoolExistence(school);
						if (check) {
							System.out.println(
									"\n\tThis Course code is already in use! Please choose another Course code!\n");
						}
					} while (check);
					CourseList.updateSchool(c, courseCode, school);
					break;
				case 3: // REMOVE COURSE
					ArrayList<Integer> a = c.getIndexNumber();
					ListIterator<Integer> i = a.listIterator();
					check = false;
					while (i.hasNext() & !check) {
						index = i.next();
						if (!CourseList.getIndexNum(index).getRegisteredStudentList().isEmpty()) {
							System.out.println("\n\tThis course has students registered");
							System.out.println("\tWould you still like to continue to remove this course? [Y/N]");
							yn = sc.next().charAt(0);

							do {
								switch (Character.toLowerCase(yn)) {
									case 'y':
										check = true;
										CourseList.dropCourseByAdmin(courseCode);
										break;
									case 'n':
										check = true;
										System.out.println("\n\tReturning to main menu\n");
										choice = 7;
										break;
									default:
										check = false;
										System.out.println("\n\tPlease enter a valid option!\n");
										break;
								}
							} while (!check);
						} else {
							CourseList.dropCourseByAdmin(courseCode); // If coursecode indexes are empty
						}
					}
					break;
				case 4: //ADD/DELETE INDEX NUMBER
					System.out.println(
							"\n\t1)Add a new index number\n\t2)Remove an existing index number\n\t3)Return to main menu");
					option = getIntInput(sc);

					switch (option) {
						case 1: //ADD NEW INDEX NUMBER TO COURSE
							System.out.println("\n\nIndexes currently in use: ");
							CourseList.printAllIndex();
							System.out.println("\n\n");
							do {
								System.out.println("\nEnter a new index number to add: ");
								index = getIntInput(sc);
								check = CourseList.checkIndexExistence(index);
								if (check) {
									System.out.println("\n\tThis index number is already taken!\n");
								}
							} while (check);
							System.out.print("\nEnter the number of vacancies for this index: ");
							vacancy = getIntInput(sc);
							ind = new IndexNum(index, vacancy);

							if (!c.getIndexNumber().isEmpty()) { // check if there are any existing index numbers
								ArrayList<Integer> indexes = c.getIndexNumber();
								if (!c.getIndexNum(indexes.get(0)).getClassSchedule().getLectureTimings().isEmpty()) {
									do {
										System.out.println(
												"\nDoes this index have the same lecture time slots as the rest of the indices? [y/n]");
										ch = sc.next().charAt(0);

										switch (Character.toLowerCase(ch)) {
											case 'y':
												t = CourseList.getIndexNum(indexes.get(0)).getClassSchedule()
														.getLectureTimings();
												break;
											case 'n':
												t = new Timetable();
												break;
											default:
												System.out.println("\n\tPlease enter a valid option!\n");
												break;
										}
									} while (Character.toLowerCase(ch) != 'y' & Character.toLowerCase(ch) != 'n');
								} else {
									t = new Timetable();
								}
							} else {
								t = new Timetable();
							}

							time = t.clone(); //clone so that original timetable is not edites
							System.out.print("\nEnter the number of classes to add: ");
							numClass = getIntInput(sc);
							for (int j = 0; j < numClass; j++) {
								System.out.print("\nEnter the lesson type (eg. tutorial/seminar/lab): ");
								String courseType = sc.next();
								//check venue timetable + check existing timetable
								time = getCourseDetails(sc, courseType, courseCode, index, time);
							}
							ind.addClassSchedule(time);
							HashMap<Integer, IndexNum> temp = new HashMap<Integer, IndexNum>();
							temp.put(index, ind);
							CourseList.newIndexNumbers(temp, courseCode);
							System.out.println("\n\tSuccessfully added new index num!\n");
							// NOTE: For debugging purposes!
							ind.printCourseSchedule();
							break;
						case 2: //REMOVE INDEXNUMBER FROM COURSE
							do {
								c.printAllIndex(); //Print Indexnums under the course
								System.out.print("\n\nEnter index number to delete: ");
								index = getIntInput(sc);

								if (!CourseList.checkIndex(courseCode, index)) {
									check = false;
									System.out.println("\n\tThis index number does not exist for this course!\n");
								} else {
									check = true;
									if (!CourseList.getIndexNum(index).getRegisteredStudentList().isEmpty()) { // If not empty  
										System.out.println("\n\tThis index has students registered to it.");
										System.out.println(
												"\tWould you still like to continue to remove this indexnumber? [Y/N]");
										yn = sc.next().charAt(0);

										do {
											switch (Character.toLowerCase(yn)) {
												case 'y':
													check = true;
													CourseList.getIndexNum(index).deleteIndexNum(courseCode);
													break;
												case 'n':
													check = true;
													System.out.println("\n\tReturning to main menu\n");
													break;
												default:
													check = false;
													System.out.println("\n\tPlease enter a valid option!\n");
													break;
											}
										} while (!check);
									}
								}
							} while (!check);
							break;
						case 3:
							System.out.println("\n\tReturning to main menu\n");
							choice = 7;
							break;
						default:
							System.out.println("\n\tPlease enter a valide option!\n");
					}
					while (option != 1 & option != 2 & option != 3)
						;
					break;

				case 5: //CHANGE INDEXNUMBER DIGIT
					check = false;
					do {
						System.out.print("\nEnter index number to change: ");
						index = getIntInput(sc);
						check = c.checkIndex(index);
						if (!check) {
							System.out.println("\n\tError! This course does not contain this index number!\n");
						}
					} while (!check);

					check = true;
					CourseList.printAllIndex();
					do {
						System.out.print("\nEnter index number to change to: ");
						newIndex = getIntInput(sc);
						check = CourseList.checkIndexExistence(newIndex);
						if (check) {
							System.out.println("\n\tError! This index is already being used!\n");
						}
					} while (check);

					CourseList.changeIndex(index, newIndex, courseCode);
					break;
				case 6: //UPDATE INDEXNUMBER VACANCY
					check = false;
					System.out.println("\nIndices for Course: " + courseCode.toUpperCase());
					c.printAllIndex();
					do {
						System.out.print("Enter index number: ");
						index = getIntInput(sc);
						check = c.checkIndex(index);

						if (!check) {
							System.out.println("\n\tError! This course does not contain this index!\n");
						}
					} while (!check);
					ind = CourseList.getIndexNum(index);
					System.out.println("\nChange availble vacancies: \n");
					System.out.println("Current vacancies: " + String.valueOf(ind.getVacancy()));
					System.out.print("New vacancies: ");
					do {
						vacancy = getIntInput(sc);
						if (vacancy < 1) {
							System.out.println("\n\tError! Please enter a valid number!");
						}
					} while (vacancy < 1);

					ind.setVacancy(vacancy);
					break;
				case 7:
					System.out.println("\n\tReturning to main menu\n");
					break;
				default:
					System.out.println("\n\tInvalid input entered! Please enter only from 1-8\n");
			}
		} while (choice > 0 & choice < 7);

	}

    /**
     * This method is for the Admin's use only
     * Setting of class's schedule
     * Admin are only able to set class at 30 minute interval starting from XX00 hrs or XX30 hrs
     * @param sc This is a scanner to read the user's input value
     * @param minTime This is the start time of the lesson
     * @return Returns the timing of the lesson
     */
	private static int getTimeInput(Scanner sc, int minTime) {
		int HH = -1;
		int MM = -1;

		do {
			System.out.print("\nEnter hour (24-hour format): ");
			HH = getIntInput(sc);
			if (HH < minTime / 100) { // Hour lesser than 8
				System.out.println("\n\tError! Please enter a valid hour!");
				System.out.printf("\tYou only enter a hour on or after %04d\n", minTime);
			} // Should we check If user enter value > 23? And since this method is reused for
				// end time, should we check that the time cannot be 20 as 1900 should be the
				// last timing?
			if (HH > 24) {
				System.out.println("\n\tPlease enter a valid hour!");
			}
		} while (HH < minTime / 100 | HH > 24);

		do {
			System.out.print("\nEnter minutes (00 / 30): ");
			MM = getIntInput(sc);
			if (MM != 0 & MM != 30) {// only check this if this is used for lesson time input
				System.out.println("\n\tError! You can only have lessons at 1/2 hour intervals!\nPlease try again!\n");
			}
		} while (MM != 0 & MM != 30); // Should this be 30 instead?

		int time = HH * 100 + MM;

		String minTimeStr = "";
		if (minTime < 1000) { // If start time is 0900, we add 0 as the first string to represent 0900
								// properly, instead of 900.
			minTimeStr = "0";
		}
		minTimeStr = minTimeStr + String.valueOf(time);
		if (minTime > time) { // Why do we have to recheck if hour in time is lesser than 800 again?
			System.out.print("\n\tYou cannot enter a timing before " + minTimeStr);
			System.out.println("\tPlease try again!\n");
			time = getTimeInput(sc, minTime);
		}

		return time;
	}

	/**
     * Getting the Course details from users' input details
     * Users are able to get the course details for different Courses / Indexes / Weeks
     * Users are able set the lesson at a specific timing and check whether there is a clash with another lesson of the same index
     * @param sc This is a scanner to read the user's input value
     * @param courseType This is the inputed course type
     * @param courseCode This is the inputed course code
     * @param index This is the inputed index
     * @param tb This is the timetable to set
     * @return Return the clone of the timetable object
     * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
     * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
     * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
     */
    private static Timetable getCourseDetails(Scanner sc, String courseType, String courseCode, int index, Timetable tb)
			throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
		int serialNum, startSerial, endSerial;
		int classFreq;
		int day, startTime, endTime;
		String venue;
		Timetable t;
		boolean check;
		do {
			do {
				System.out.println("Class Frequency: \n\t(1) Every Even Week\n\t(2) Every Odd Week\n\t(3) Every Week");
				System.out.print("\nEnter class frequency: ");
				classFreq = getIntInput(sc);

				if (classFreq <= 0 | classFreq > 3) {
					System.out.println("\n\tPlease enter a valid option!\n");
				}
			} while (classFreq <= 0 | classFreq > 3);

			serialNum = classFreq - 1;

			do {
				System.out.println("\n\nDay of the Week:");
				System.out.println("\t(1) Monday\n\t(2) Tuesday\n\t.\n\t.\n\t(5) Friday");
				System.out.print("Enter day of week: ");

				day = getIntInput(sc);

				if (day <= 0 | day > 7) {
					System.out.println("\n\tError! Please enter a valid option!\n");
				} else if (day > 5) {
					System.out.println("\n\tClasses can only be held between Monday - Friday!\n");
				}

			} while (day <= 0 | day > 5);

			serialNum = serialNum + day * 100000;

			System.out.println("\n\nVenue: ");
			System.out.println("Enter the location that this lesson will be held");
			System.out.println("\nList of venues currently in our database: ");
			VenueList.printAllVenues();

			System.out.println("\n\nEnter the venue: ");
			venue = sc.next();

			if (VenueList.checkVenue(venue)) {
				t = VenueList.getVenueTimetable(venue);
				System.out.println("\n\nBookings for this venue: ");
				t.printWeeklySchedule();

			} else {
				VenueList.newVenue(venue, new Timetable());
				t = VenueList.getVenueTimetable(venue);
			}

			do {
				System.out.println("Enter Start Time: ");
				startTime = getTimeInput(sc, 800); //ensure start time is after 0759 (earliest is 8am)
				startSerial = serialNum + startTime * 10;
				System.out.println("Enter End Time: ");
				endTime = getTimeInput(sc, startTime); // Have to pass in starttime as an argument as we are reusing this method for
				// both start and endtime
				endSerial = serialNum + endTime * 10;
				check = t.checkStartEnd(startSerial, endSerial);
				if (!check) {
					System.out.println("\n\tSorry the venue is booked for this timing already!\n");
				}
			} while (!check);

			if (!tb.checkStartEnd(startSerial, endSerial)) {
				System.out.println("\n\tSorry this timing clashes with another lesson in the SAME index number\n");
			}
		} while (!tb.checkStartEnd(startSerial, endSerial));
		tb.addClass(startSerial, endSerial, courseCode, index, courseType, venue);
		t.addClass(startSerial, endSerial, courseCode, index, courseType, venue);
		// NOTE: Venue should technically be updated already...
		// if it isnt, un-comment bottom code
		// VenueList.bookSlot(startSerial, endSerial, courseCode, index, venue)
		return tb; // Return the clone of the timetable object
	}

	/**
     * Gets the user's input value
     * @param sc This is a scanner to read the user's input value
     * @return This is the users input value
     */
    private static int getIntInput(Scanner sc){
        boolean success = false;
        int i = 0;
        while (!success){
            try{
                i = sc.nextInt();
                success = true;
                break;
            }catch(InputMismatchException e){
                System.out.println("\n\tPlease enter a valid input!!\n");
                sc.next();
            }
        }
        return i;
    }

}
