import java.util.*;
import java.io.*;
import java.time.format.DateTimeFormatter;

public class Application {

    public static void main(String[] args) throws UserNotFound, UserAlreadyExists, WrongPassword {

        Scanner sc = new Scanner(System.in);
        Console con = System.console();
        System.out.println("Welcome to MySTARS\nPlease login to continue");
        System.out.print("\nUsername: ");
        String username = sc.next();
        System.out.print("\nPassword: ");
        char[] password = con.readPassword();

        if (!PasswordHash.checkPwd(username, String.valueOf(password))) {
            sc.close();
            throw new WrongPassword("Wrong Password entered!");
        }
        Person user = PersonList.getByUsername(username.toLowerCase());

        if (user instanceof Student) {
            if (AccessPeriod.checkAccessPeriod((Student) user)) {
                StudentApp(sc, (Student) user, username.toLowerCase(), con);
            } else {
                System.exit(0);
            }
        } else if (user instanceof Admin) {
            AdminApp(sc, (Admin) user, username.toLowerCase(), con);
        }
        sc.close();
    }

    public static void StudentApp(Scanner sc, Student st, String username, Console con)
            throws UserNotFound, UserAlreadyExists, WrongPassword {
        String coursecode;
        int indexnum;
        int index2;

        System.out.println("Welcome to MySTARS!");
        if (st.getSwopStatus()) { // Notify student if swop is successful
            st.printSwoppedlist();
        }

        int choice = 0;
        while (choice < 8) {
            System.out.println("Enter one of the following options to continue");

            System.out.print(
                    "1. Add Course\n2. Drop Course\n3. Print Courses Registered\n4. Check Vacancies\n5. Change Course Index number\n6. Swop Index number\n7. Change Password\n8. Logout\n");

            choice = sc.nextInt();
            switch (choice) {
                case 1: // Add course
                    System.out.print("Enter Course code: ");
                    coursecode = sc.next();
                    System.out.print("Enter Index number: ");
                    indexnum = sc.nextInt();
                    if (CourseList.checkCode(coursecode) & CourseList.checkIndex(coursecode, indexnum)) {
                        st.add_course(coursecode, indexnum);
                        break;
                    } else {
                        System.out.println("Error! Please enter a valid Course code / Index number");
                        break;
                    }
                case 2: // Drop course
                    System.out.print("Enter Course code: ");
                    coursecode = sc.next();
                    System.out.print("Enter Index number: ");
                    indexnum = sc.nextInt();
                    st.drop_course(coursecode, indexnum);
                    break;
                case 3: // Print registered courses
                    st.check_course();
                    break;
                case 4: // Check vacancies
                    System.out.print("Enter course code: ");
                    coursecode = sc.next();
                    if (CourseList.checkCode(coursecode)) {
                        CourseList.checkCourseVacancies(coursecode);
                        break;
                    } else {
                        System.out.println("Error! Please enter a valid course code!");
                        break;
                    }
                case 5: // Change course index number
                    System.out.print("Enter course code: ");
                    coursecode = sc.next();
                    System.out.print("Enter current Index Number: ");
                    indexnum = sc.nextInt();
                    System.out.print("Enter new Index Number: ");
                    index2 = sc.nextInt();
                    if (CourseList.checkVacancies(indexnum) > 0) {
                        st.drop_course(coursecode, indexnum);
                        st.add_course(coursecode, index2);
                        break;
                    } else {
                        System.out.println("Course index full! Please try again!");
                        break;
                    }
                case 6: // Swop course with another student
                    System.out.print("Enter course code: ");
                    coursecode = sc.next();
                    System.out.print("Enter current Index Number: ");
                    indexnum = sc.nextInt();

                    System.out.println("Enter the username of the student you want to swop with: ");
                    String newuser = sc.next();
                    if (!PersonList.checkusername(newuser)) {
                        throw new UserNotFound("User not found!");
                    }
                    System.out.println("Enter the Index Number that you want to swop to: ");
                    index2 = sc.nextInt();

                    Swop.swopStudent(coursecode, indexnum, index2, username, newuser);
                    break;
                case 7:
                    char[] orgpwd = con.readPassword("Old Password: ");
                    char[] newpwd = con.readPassword("New Password: ");
                    char[] reppwd = con.readPassword("Repeat Password: ");
                    if (String.valueOf(newpwd).equals(String.valueOf(reppwd))) {
                        PasswordHash.changePwd(String.valueOf(orgpwd), String.valueOf(newpwd), username);
                        break;
                    } else {
                        throw new WrongPassword("Password do not match!");
                    }

                case 8: // Quit
                    System.out.println("Thank you for using MySTARS!\nQuitting...");
                    break;
                default:
                    System.out.println("Quitting...");
                    break;
            }
        }
    }

    public static void AdminApp(Scanner sc, Admin ad, String username, Console con) throws UserAlreadyExists {
        String s_name;
        String matric;
        char gender;
        String nationality;
        String major;
        Integer year;
        int sel = 0;

        do {
            System.out.print("(1) Edit/Check Student Access Period\n" + "(2) Add Student\n" + "(3) Add new course\n"
                    + "(4) Update an existing course" + "\n" + "(5) Check Available slot for an index number\n"
                    + "(6) Print student list by index number\n" + "(7) Print registered student list by course\n"
                    + "(8) Quit\n");
            sel = sc.nextInt();
            switch (sel) {
                case 1:
                    System.out.println(
                            "(1) Check Access Period\n(2) Edit default Access Period\n(3)Add new Access Period by Major/Year\n(4)Remove Access Peiod for Major/Year\n(5)Remove all Access Peiod by Major/Year");
                    int acc = sc.nextInt();
                    switch (acc) {
                        case 1:
                            AccessPeriod.PrintALLaccess();
                            break;
                        case 2:
                            System.out.println("Enter Start date:\nYear (YYYY): ");
                            int yyyy = sc.nextInt();
                            break;

                        // AccessPeriod.editDefaultAccess(start, end);
                    }
                case 2: // add a student
                    System.out.print("Enter student's username: \n");
                    username = sc.next();
                    System.out.print("Enter student's name: \n");
                    s_name = sc.next();
                    System.out.print("Enter student's matric number: \n");
                    matric = sc.next();
                    System.out.print("Enter student's gender: \n");
                    gender = sc.next().charAt(0);
                    System.out.print("Enter student's nationality: \n");
                    nationality = sc.next();
                    System.out.print("Enter student's major: \n");
                    major = sc.next();
                    System.out.print("Enter student's year: \n");
                    year = sc.nextInt();
                    ad.addStudent(username.toLowerCase(), s_name, matric, Character.toUpperCase(gender), nationality,
                            major, year);
                    break;

                case 3:
                    NewCourse(ad);
                    break;

                case 4:
                    UpdateCourse(ad); // TODO: Create the logic for UpdateCourse info
                    break;
                case 5:
                    printIndexVacancy(ad);
                    break;
                case 6: {
                    printStudentListByIndex(ad);
                    break;
                    /*
                     * int index = sc.nextInt();
                     * System.out.print("Enter index number of course: \n"); ad.printByIndex(index);
                     * break;
                     */
                }
                case 7: {
                    printStudentListByCourse(ad);
                    break;
                    /*
                     * String coursecode = sc.nextLine(); System.out.print("Enter course name: \n");
                     * ad.printByCourse(coursecode); break;
                     */
                }

            }
        }

        while (sel != 8);
    }

    // =============================================== ALOT OF METHODS HERE YO
    // ===============================================

    // ========= Normal Methods =========

    // ****** Method to add new course ******
    public static void NewCourse(Admin Admin) {
        String venue, courseCode, school;
        int indexNum, vacancy, classNum, startTime, endTime, count, m;
        int classType = -1, weekType = -1, dayOfTheWeek = -1;
        String[] indexDetails;
        ArrayList<Integer> classArrayTypes = new ArrayList<Integer>();
        ArrayList<ClassSchedule> lectures = new ArrayList<ClassSchedule>();
        Scanner sc = new Scanner(System.in);
        printAllCourses();

        do {
            System.out.print("\nEnter the new Course Code: ");
            courseCode = sc.nextLine();
            System.out.print("Enter the school: ");
            school = sc.nextLine();
        } while (!(Admin.addNewCourse(courseCode, school)));

        System.out.print("Enter the total number index to add to course: ");
        count = sc.nextInt();

        // Assuming all indexes of the same course have similar # of classes and types,
        // and lecture location/timing/venue is the same for
        // all indexes. This will be the "Model Index" where the rest of the indexes
        // will follow its framework
        printAllIndexes();
        do {
            System.out.print("\nEnter the new indexNumber 1 : ");
            indexNum = sc.nextInt();
            System.out.print("Enter the vacancy: ");
            vacancy = sc.nextInt();
        } while (!(Admin.addNewIndexNum(courseCode, indexNum, vacancy)));

        System.out.println(
                "\n==========================================================================================");
        System.out
                .print("Take note that the Total duration of all classes in each Index number must not exceed 6 hours."
                        + "\nClasses can only conduct from Monday to Friday, between 0800 to 1900 hrs."
                        + "\nAdjust your number of classes and duration accordingly.");
        System.out.println(
                "\n==========================================================================================\n");
        System.out.print("Enter the number of classes: ");
        classNum = sc.nextInt();
        for (int k = 0; k < classNum; k++) {
            System.out.println("\n===== Enter class " + (k + 1) + " =====");
            System.out
                    .print("\nClass types available:\n" + "1)Lecture\n" + "2)Tutorial\n" + "3)Laboratory Session\n\n");
            while (true) {
                System.out.print("Enter class type: ");
                classType = sc.nextInt();
                if ((classType < 0) || (classType > 3)) {
                    System.out.println("Invalid Input, enter from 1 to 3!\n");
                    continue;
                }
                classArrayTypes.add(classType);
                break;
            }
            System.out.print("\nClass week type:\n" + "1)Normal\n" + "2)Odd\n" + "3)Even\n\n");
            while (true) {
                System.out.print("Enter Week type: ");
                weekType = sc.nextInt();
                if ((weekType < 0) || (weekType > 3)) {
                    System.out.println("Invalid Input, enter from 1 to 3!\n");
                    continue;
                }
                break;
            }
            System.out.println("Choose which day the class is held on.\n" + "Monday = 1\n" + "Tuesday = 2\n" + "...\n"
                    + "Friday = 5\n");
            while (true) {
                System.out.print("Enter the digit of the day of the week: ");
                dayOfTheWeek = sc.nextInt();
                if ((dayOfTheWeek < 0) || (dayOfTheWeek > 5)) {
                    System.out.println("Invalid Input, enter from 1 to 5!\n");
                    continue;
                }
                break;
            }
            System.out.println("Using 24 Hour Format. Eg. 10AM = 1000");
            System.out.print("Set the start time: ");
            startTime = sc.nextInt();
            System.out.print("Set the end time: ");
            endTime = sc.nextInt();
            System.out.print("Set the venue: "); // Maybe have a separate hashmap which stores all the location in NTU
            sc.nextLine();
            venue = sc.nextLine();
            indexDetails = indexDetailsConverter(classType, weekType, dayOfTheWeek);

            Admin.addNewSchedule(indexNum, indexDetails[0], indexDetails[1], indexDetails[2], startTime, endTime,
                    venue);
            if (classType == 1) {
                ClassSchedule lecture = new ClassSchedule("Lecture", indexDetails[1], indexDetails[2], startTime,
                        endTime, venue);
                lectures.add(lecture);
            }
        }
        for (int j = 0; j < (count - 1); j++) {
            m = 0;
            printAllIndexes();
            do {
                System.out.print("\nEnter the new indexNumber " + (j + 2) + " : ");
                indexNum = sc.nextInt();
                System.out.print("Enter the vacancy: ");
                vacancy = sc.nextInt();
                sc.nextLine();
            } while (!(Admin.addNewIndexNum(courseCode, indexNum, vacancy)));
            System.out.println(
                    "\n==========================================================================================");
            System.out.print(
                    "Take note that the Total duration of all classes in each Index number must not exceed 6 hours."
                            + "\nClasses can only conduct from Monday to Friday, between 0800 to 1900 hrs."
                            + "\nAdjust your number of classes and duration accordingly.");
            System.out.println(
                    "\n==========================================================================================\n");
            System.out.println("All lectures have been added automatically!");
            for (int l = 0; l < classArrayTypes.size(); l++) {
                if (classArrayTypes.get(l) == 1) {
                    Admin.addNewSchedule(indexNum, lectures.get(m));
                    m++;
                } else {
                    indexDetails = indexDetailsConverter(classArrayTypes.get(l), 1, 1); // 1 are just dummy units
                    System.out.println("\n===== Enter class " + (l + 1) + " =====");
                    System.out.println("\n===== Enter the details for " + indexDetails[0] + " class =====");
                    System.out.print("\nClass week type:\n" + "1)Normal\n" + "2)Odd\n" + "3)Even\n\n");
                    while (true) {
                        System.out.print("Enter Week type: ");
                        weekType = sc.nextInt();
                        if ((weekType < 0) || (weekType > 3)) {
                            System.out.println("Invalid Input, enter from 1 to 3!\n");
                            continue;
                        }
                        break;
                    }
                    System.out.println("Choose which day the class is held on.\n" + "Monday = 1\n" + "Tuesday = 2\n"
                            + "...\n" + "Friday = 5\n");
                    while (true) {
                        System.out.print("Enter the digit of the day of the week: ");
                        dayOfTheWeek = sc.nextInt();
                        if ((dayOfTheWeek < 0) || (dayOfTheWeek > 5)) {
                            System.out.println("Invalid Input, enter from 1 to 5!\n");
                            continue;
                        }
                        break;
                    }
                    System.out.println("\nUsing 24 Hour Format. Eg. 10AM = 1000");
                    System.out.print("Set the start time: ");
                    startTime = sc.nextInt();
                    System.out.print("Set the end time: ");
                    endTime = sc.nextInt();
                    System.out.print("Set the venue: "); // Maybe have a separate hashmap which stores all the location
                                                         // in NTU
                    sc.nextLine();
                    venue = sc.nextLine();
                    indexDetails = indexDetailsConverter(classArrayTypes.get(l), weekType, dayOfTheWeek);
                    Admin.addNewSchedule(indexNum, indexDetails[0], indexDetails[1], indexDetails[2], startTime,
                            endTime, venue);
                }
            }

        }
        printNewCourseMsg(courseCode);
    }

    // ****** Method to update a course ******
    public static void UpdateCourse(Admin admin) {
        String courseCode, newCourseCode;
        int choice;
        Scanner sc = new Scanner(System.in);
        printAllCourses();

        courseCode = checkCourseExistence();
        System.out.println("\nWhat would you like to do with " + courseCode + " :");
        System.out.println("1) Change Course code name" + "\n" + "2) Change Course Code School" + "\n"
                + "3) Remove Course" + "\n" + "4) Add a new index number" + "\n" + "5) Change index number digits"
                + "\n" + "6) Update index number's vacancy" + "\n" + "7) Update index number's schedule" + "\n"
                + "8) Exit the program" + "\n");

        System.out.print("Please enter your choice: ");
        choice = sc.nextInt();
        switch (choice) {
            // ALL OF THESE ARE IN PROGRESS
            case 1:
                System.out.println("Enter new course code name: ");
                newCourseCode = sc.nextLine();
                if (CourseList.checkCourseExistence(newCourseCode))
                    break;
            case 2:
                System.out.println("In progress");
                break;
            case 3:
                NewCourse(admin1);
                break;
            case 4:
                UpdateCourse(admin1);
                break;
            case 5:
                printIndexVacancy(admin1);
                break;
            case 6:
                printStudentListByIndex(admin1);
                break;
            case 7:
                printStudentListByCourse(admin1);
                break;
            case 8:
                System.out.println("Goodbye!");
                System.exit(0);
            default:
                System.out.println("\nInvalid input entered! Please enter only from 1-8");
        }

    }

    // ****** Method to convert integer type course details to its own meaning in
    // Sting type ******
    public static String[] indexDetailsConverter(int classType, int weekType, int dayOfTheWeek) {
        String[] convert = new String[3];
        switch (classType) {
            case 1:
                convert[0] = "Lecture";
                break;
            case 2:
                convert[0] = "Tutorial";
                break;
            case 3:
                convert[0] = "Laboratory Session";
                break;
        }
        switch (weekType) {
            case 1:
                convert[1] = "Normal";
                break;
            case 2:
                convert[1] = "Odd";
                break;
            case 3:
                convert[1] = "Even";
                break;
        }
        switch (dayOfTheWeek) {
            case 1:
                convert[2] = "Monday";
                break;
            case 2:
                convert[2] = "Tuesday";
                break;
            case 3:
                convert[2] = "Wednesday";
                break;
            case 4:
                convert[2] = "Thursday";
                break;
            case 5:
                convert[2] = "Friday";
                break;
        }
        return convert;
    }

    // ****** Method to print New course message ******
    public static void printNewCourseMsg(String courseCode) {
        System.out.println("\n==================================================================================\n");
        System.out.println("Course " + courseCode + " have been added successfully!");
        System.out.println("\n==================================================================================\n");
        printCourseInfo(courseCode);
    }

    // ****** Method to print Course Details including its index values and
    // schedules ******
    public static void printCourseInfo(String courseCode) {
        CourseList.printCourseInfo(courseCode);
        CourseList.printIndexOfCourse(courseCode);
    }

    // ****** Method to make sure the indexnum entered exist in database. ******
    public static int checkIndexNumExistence() {
        Scanner sc = new Scanner(System.in);
        int indexNum;
        do {
            System.out.print("Please enter the index number you want to query: ");
            indexNum = sc.nextInt();
        } while (!(CourseList.checkIndexExistence(indexNum)));
        return indexNum;
    }

    // ****** Method to make sure the coursecode entered exist in database. ******
    public static String checkCourseExistence() {
        Scanner sc = new Scanner(System.in);
        String courseCode;
        do {
            System.out.print("Please enter the Course code you want to query: ");
            courseCode = sc.nextLine();
        } while (!(CourseList.checkCourseExistence(courseCode)));
        return courseCode;
    }

    // ****** Method to print all indexes in existence ******
    public static void printAllIndexes() {
        System.out.println("\nCurrent Indexes available:");
        CourseList.PrintAllIndex();
    }

    // ****** Method to print index vacancy ******
    public static void printIndexVacancy(Admin admin) {
        int vacancy, indexNum;
        printAllIndexes();
        indexNum = checkIndexNumExistence();
        vacancy = admin.checkVacancies(indexNum);
        System.out.println("\nThe vacancy for index " + indexNum + " is: " + vacancy);
    }

    // ****** Method to print student list by index ******
    public static void printStudentListByIndex(Admin admin) {
        int indexNum;
        printAllIndexes();
        indexNum = checkIndexNumExistence();
        admin.printByIndex(indexNum);
    }

    // ****** Method to print out all courses in existence ******
    public static void printAllCourses() {
        System.out.println("\nCurrent courses available:");
        CourseList.PrintAllCourse();
    }

    // ****** Method to print student list by coursecode ******
    public static void printStudentListByCourse(Admin admin) {
        String courseCode;
        printAllCourses();
        courseCode = checkCourseExistence();
        admin.printByCourse(courseCode);
    }

}
