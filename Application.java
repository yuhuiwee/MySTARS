import java.util.*;
import java.util.zip.CheckedInputStream;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Application {

    public static void main(String[] args) throws UserNotFound, UserAlreadyExists, WrongPassword, CourseDontExist {

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

    private static void StudentApp(Scanner sc, Student st, String username, Console con)
            throws UserNotFound, UserAlreadyExists, WrongPassword, CourseDontExist {
        String coursecode;
        int indexnum;
        int index2;

        System.out.println("Welcome to MySTARS!");
        if (st.getSwopStatus()) { // Notify student if swop is successful
            st.printChanges();
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
                    if (CourseList.checkCourseExistence(coursecode) & CourseList.checkIndex(coursecode, indexnum)) {
                        st.addCourse(coursecode, indexnum);
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
                    st.dropCourse(coursecode, indexnum);
                    break;
                case 3: // Print registered courses
                    st.checkCourse();
                    break;
                case 4: // Check vacancies
                    System.out.print("Enter course code: ");
                    coursecode = sc.next();
                    if (CourseList.checkCourseExistence(coursecode)) {
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
                        st.dropCourse(coursecode, indexnum);
                        st.addCourse(coursecode, index2);
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

    private static void AdminApp(Scanner sc, Admin ad, String username, Console con) throws UserAlreadyExists {
        String s_name;
        String matric;
        char gender;
        String nationality;
        String major;
        Integer year;
        int sel = 0;
        ZonedDateTime start;
        ZonedDateTime end;
        int f;
        int t;

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
                            AccessPeriod.printAllAccess();
                            break;
                        case 2:
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
                            major = sc.nextLine();
                            System.out.println("Enter year: \nFrom");
                            f = sc.nextInt();
                            System.out.println("to ");
                            t = sc.nextInt();

                            while (t < f) {
                                System.out.println("Please enter in ascending order");
                                System.out.println("Enter year: \nFrom");
                                f = sc.nextInt();
                                System.out.println("to ");
                                t = sc.nextInt();
                            }

                            while (t > 6 | f > 6) {
                                System.out.println("Please enter a valid year (<6)");
                                System.out.println("Enter year: \nFrom");
                                f = sc.nextInt();
                                System.out.println("to ");
                                t = sc.nextInt();
                            }

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

                            for (int i = f; i <= t; i++) {
                                AccessPeriod.changeMajorAccess(major, i, start, end);
                            }

                            break;
                        case 4:
                            // Remove access period by major/year

                            System.out.print("Enter major: ");
                            major = sc.nextLine();
                            System.out.println("Enter year: \nFrom");
                            f = sc.nextInt();
                            System.out.println("to ");
                            t = sc.nextInt();

                            while (t < f) {
                                System.out.println("Please enter in ascending order");
                                System.out.println("Enter year: \nFrom");
                                f = sc.nextInt();
                                System.out.println("to ");
                                t = sc.nextInt();
                            }

                            while (t > 6 | f > 6) {
                                System.out.println("Please enter a valid year (<6)");
                                System.out.println("Enter year: \nFrom");
                                f = sc.nextInt();
                                System.out.println("to ");
                                t = sc.nextInt();
                            }

                            for (int i = f; i <= t; i++) {
                                AccessPeriod.removeMajorAccess(major, i);
                            }
                            break;
                        case 5:
                            // remove all but default
                            AccessPeriod.removeAllbutDefault();

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
                    newCourse(sc);
                    break;

                case 4:
                    UpdateCourse(sc); // TODO: Create the logic for UpdateCourse info
                    break;
                case 5:
                    CourseList.printAllIndex();
                    int indexNum = checkIndexNumExistence(sc);
                    int vacancy = CourseList.checkVacancies(indexNum);
                    System.out.println("\nThe vacancy for index " + indexNum + " is: " + vacancy);
                    break;
                case 6: {
                    CourseList.printAllIndex();
                    int indexNum2 = checkIndexNumExistence(sc);
                    CourseList.printStudentByIndex(indexNum2);
                    break;
                    /*
                     * int index = sc.nextInt();
                     * System.out.print("Enter index number of course: \n"); ad.printByIndex(index);
                     * break;
                     */
                }
                case 7: {
                    CourseList.printAllCourse();
                    String courseCode = checkCourseExistence(sc);
                    CourseList.printStudentByCourse(courseCode);
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

    private static ZonedDateTime inputDateTime(Scanner sc, int minyear, int minmonth, int minday, int minhour,
            int minmin) {
        boolean mindate;
        String datestring = "";
        // Get year
        System.out.println("Enter Date: ");
        System.out.print("\nYear (yyyy): ");
        int yyyy = sc.nextInt();
        while (yyyy < minyear) { // if admin enters year < current year
            System.out.println("Error! Please re-enter!\nYear (yyyy): ");
            yyyy = sc.nextInt();
        }
        datestring = datestring + String.valueOf(yyyy);
        // Get Month
        System.out.print("\nMonth(mm): ");
        int mm = sc.nextInt();
        while (mm > 12 && (mm < minmonth & minyear == yyyy)) {
            // check if month is > 12
            // if current year entered, check if month is < current month
            System.out.println("Error! Please re-enter!");
            System.out.print("\nMonth (mm): ");
            mm = sc.nextInt();
        }
        datestring = datestring + "-" + String.format("%02d", mm);

        // Get Day
        System.out.print("Day (dd): ");
        int dd = sc.nextInt();

        // Get max number of days in that month
        YearMonth yearMonthObject = YearMonth.of(yyyy, mm);
        int daysInMonth = yearMonthObject.lengthOfMonth();

        while (dd > daysInMonth && (dd < minday & mm == minmonth & minyear == yyyy)) {
            // check if month > max days in month entered
            // check if day entered is before the current date
            System.out.println("Error! Please re-enter!");
            System.out.print("\nDay (dd): ");
            dd = sc.nextInt();
        }

        datestring = datestring + "-" + String.format("%02d", dd);

        if (dd == minday & mm == minmonth & yyyy == minyear) {
            mindate = true;
        } else {
            mindate = false;
        }

        // Get Time
        System.out.println("Enter Time: ");
        // Get hour
        System.out.print("\nHour (24 hour format) (HH): ");
        int HH = sc.nextInt();
        while (HH > 24 && (mindate & HH < minhour)) {
            System.out.println("Error! Please re-enter!");
            System.out.print("\nHour in 24 hour format (HH): ");
            HH = sc.nextInt();

        }
        datestring = datestring + "T" + String.format("%02d", HH);

        // Get min
        System.out.print("\nMin (mm)");
        int MM = sc.nextInt();
        while (MM >= 60 && (mindate & HH == minhour & MM < minmin)) {
            System.out.println("Error! Please re-enter!");
            System.out.print("\nMin (mm)");
            MM = sc.nextInt();
        }

        datestring = datestring + ":" + String.format("%02d", MM) + ":00";

        // Get TimeZone
        System.out.println("Enter TimeZone (eg. 00:00 for GMT and -08:00 for GMT-08:00)");
        System.out.println("Press Enter for Singapore Time Zone (Default)");
        String zone = sc.nextLine();

        boolean check = false;
        String z;
        while (!check) {
            if (zone.isEmpty()) {
                check = true;
                z = "+08:00[Asia/Singapore]";
            } else if (zone == "00:00") {
                check = true;
                z = "+00:00";
            } else if (zone.contains(":") & (zone.startsWith("+") | zone.startsWith("-"))) {
                check = true;
                z = zone;
            } else {
                System.out.println("Error! Please re-enter!");
                check = false;
                zone = sc.nextLine();
            }
        }

        datestring = datestring + zone;
        ZonedDateTime dt;
        try {
            dt = ZonedDateTime.parse(datestring);
        } catch (DateTimeException e) {
            System.out.println("Error!");
            dt = inputDateTime(sc, minyear, minmonth, minday, minhour, minmin);
        }

        return dt;

    }

    // ****** Method to add new course ******
    private static void newCourse(Scanner sc) {
        String venue, courseCode, school, startStr, endStr;
        int indexNum, vacancy, classNum, startTime, endTime, totalIndex, m;
        int classType = -1, weekType = -1, dayOfTheWeek = -1;
        String[] indexDetails;
        ArrayList<Integer> classArrayTypes = new ArrayList<Integer>();
        ArrayList<ClassSchedule> lectures = new ArrayList<ClassSchedule>();

        sc.nextLine();

        // ENTER COURSE CODE AND SCHOOL
        do {
            CourseList.printAllCourse();
            System.out.print("\nEnter the new Course Code: ");
            courseCode = sc.nextLine();
            System.out.println("\nHere are the schools available:");
            Course.printSchoolType();
            System.out.print("Enter the school: ");
            school = sc.nextLine();
        } while (!(CourseList.newCourse(courseCode, school)));

        // ENTER TOTAL NUMBER OF INDEX IN THIS NEW COURSE
        System.out.print("Enter the total number index to add to course: ");
        totalIndex = sc.nextInt();

        // Assuming all indexes of the same course have similar # of classes and types,
        // and lecture location/timing/venue is the same for
        // all indexes. This first index will be the "Model Index" where the rest of the
        // indexes
        // will follow its framework.

        CourseList.printAllIndex();
        do {
            System.out.print("\nEnter the new indexNumber for your first index : ");
            indexNum = sc.nextInt();
            System.out.print("Enter the vacancy: ");
            vacancy = sc.nextInt();
        } while (!(CourseList.newIndexNumber(courseCode, indexNum, vacancy)));

        System.out.println(
                "\n==========================================================================================");
        System.out
                .print("Take note that the Total duration of all classes in each Index number must not exceed 6 hours."
                        + "\nClasses can only conduct from Monday to Friday, between 0800 to 1900 hrs."
                        + "\nAdjust your number of classes and duration accordingly.");
        System.out.println(
                "\n==========================================================================================\n");
        System.out.print("Enter the number of classes: "); // Num of classes per index number
        classNum = sc.nextInt();

        // TEST
        ArrayList<Integer> test = new ArrayList<Integer>();
        int testInput;

        for (int k = 0; k < classNum; k++) { // Will iterate thru all the classes defined
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
                testInput = weekType;
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
                testInput = testInput + dayOfTheWeek * 100000; // Added as the last digit
                break;
            }

            System.out.println("Note that classes can only have a duration made up of full"
                    + " hour or half and hour.\nEg. Class duration of 1h 30 min = valid"
                    + "\nEg. Class duration of 1h 45 min = invalid" + "\nUsing 24 Hour Format. Eg. 10AM = 1000");
            // **** STARTTIME SELECTION ****
            while (true) {
                try {
                    sc.nextLine();
                    System.out.print("Set the start time: ");
                    startTime = sc.nextInt();
                    // startTime = Integer.parseInt(startStr);
                    testInput = testInput + (startTime * 10);
                    if ((startTime < 800) || (startTime > 1900))
                        throw new Exception("Error! Start time cannot be earlier than 8AM or later than 7PM!\n"
                                + "Please enter a valid time!");
                    if (test.contains(testInput))
                        throw new Exception("Error! Start time cannot clash with other classes in this index!\n"
                                + "Please enter a valid time!");
                    if ((startTime % 100 != 30) && (startTime % 100 != 0))
                        throw new Exception("Error! Start time can only start at each hour or half and hour\n"
                                + "Please enter a valid time!");
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                test.add(testInput); // Add the final serial code into test arraylist
                testInput -= (startTime * 10); // Reset testInput back to not having startTime value
                break;
            }
            // **** ENDTIME SELECTION ****
            int totalHalfInterval, temp1, temp2;
            while (true) {
                try {
                    sc.nextLine();
                    System.out.print("Set the end time: ");
                    endTime = sc.nextInt();
                    // endTime = Integer.parseInt(endStr);
                    if ((endTime < 800) || (endTime > 1900))
                        throw new Exception(
                                "Error! End time cannot be the same as start time, earlier than 8AM or later than 7PM!\n"
                                        + "Please enter a valid time!");
                    if ((endTime % 100 != 30) && (endTime % 100 != 0))
                        throw new Exception("Error! Start time can only start at each hour or half and hour\n"
                                + "Please enter a valid time!");
                    temp1 = endTime - startTime;

                    if (temp1 % 100 == 30) {
                        totalHalfInterval = ((temp1 - 30) / 100) * 2 + 1; // So for 2 hours or temp1 = 200, total # of
                                                                          // half hour interval = 4
                    } else {
                        totalHalfInterval = (temp1 / 100) * 2;
                    }

                    temp1 = testInput; // We reuse temp1 here and testInput which now do not contain any timeslot code
                                       // Eg. Only the weektype and dayOfTheWeek is in testInput(100001)
                    temp2 = startTime;

                    for (int j = 0; j < totalHalfInterval; j++) {
                        temp1 = temp1 + (temp2 * 10);
                        if (test.contains(temp1))
                            throw new Exception("Error! Start time cannot clash with other classes in this index!\n"
                                    + "Please enter a valid time!");
                        temp1 -= (temp2 * 10);
                        temp2 += 30;
                        if (temp2 % 60 == 0) {
                            temp2 += 40;
                        }
                    }

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                for (int j = 0; j < totalHalfInterval; j++) {
                    temp1 = temp1 + (temp2 * 10);
                    test.add(testInput);
                    temp1 -= (temp2 * 10);
                    temp2 += 30;
                    if (temp2 % 60 == 0) {
                        temp2 += 40;
                    }
                }
                break;
            }

            System.out.print("Set the venue: "); // Maybe have a separate hashmap which stores all the location in NTU
            venue = sc.nextLine();
            indexDetails = indexDetailsConverter(classType, weekType, dayOfTheWeek);

            CourseList.newSchedule(indexNum, indexDetails[0], indexDetails[1], indexDetails[2], startTime, endTime,
                    venue);
            if (classType == 1) {
                ClassSchedule lecture = new ClassSchedule("Lecture", indexDetails[1], indexDetails[2], startTime,
                        endTime, venue);
                lectures.add(lecture);
            }
        }
        for (int j = 0; j < (totalIndex - 1); j++) {
            m = 0;
            CourseList.printAllIndex();
            do {
                System.out.print("\nEnter the new indexNumber " + (j + 2) + " : ");
                indexNum = sc.nextInt();
                System.out.print("Enter the vacancy: ");
                vacancy = sc.nextInt();
                sc.nextLine();
            } while (!(CourseList.newIndexNumber(courseCode, indexNum, vacancy)));
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
                    CourseList.newSchedule(indexNum, lectures.get(m));
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
                    CourseList.newSchedule(indexNum, indexDetails[0], indexDetails[1], indexDetails[2], startTime,
                            endTime, venue);
                }
            }

        }
        System.out.println("\n==================================================================================\n");
        System.out.println("Course " + courseCode + " have been added successfully!");
        System.out.println("\n==================================================================================\n");
        CourseList.printCourseInfo(courseCode);
        CourseList.printIndexOfCourse(courseCode);
    }

    // ****** Method to update a course ******
    private static void UpdateCourse(Scanner sc) {
        String courseCode, newCourseCode;
        int choice;
        CourseList.printAllCourse();

        courseCode = checkCourseExistence(sc);
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
                // change course code name
                CourseList.updateCourseCode(sc, courseCode);
                break;
            case 2:
                // change course code school
                CourseList.updateCourseCodeSchool(sc, courseCode);
                break;
            case 3:
                // remove course
                CourseList.dropCourseByAdmin(courseCode);
                break;
            case 4:
                // add new index number
                System.out.println("Enter new Index Number: ");
                int newindexNumber = sc.nextInt();
                System.out.println("Enter new Index Number's Vacancy: ");
                int newVacancy = sc.nextInt();
                CourseList.newIndexNumber(courseCode, newindexNumber, newVacancy);
                break;
            case 5:
                // change index number digits
                System.out.println("Enter Index Number to change: ");
                int currentIndexNumber = sc.nextInt();
                System.out.println("Enter updated Index Number: ");
                int updatedIndexNumber = sc.nextInt();
                CourseList.updateIndexNumber(courseCode, currentIndexNumber, updatedIndexNumber);
                break;
            case 6:
                // update index number vacancy
                System.out.println("Enter Index Number: ");
                int indexNum = sc.nextInt();
                System.out.println("Enter new Vacancy: ");
                int updateVacancy = sc.nextInt();
                CourseList.updateIndexNumVacancy(courseCode, indexNum, updateVacancy);
                break;
            case 7:
                // update index number schedule
                // Might not need this
                System.out.println("Enter Index Number: ");
                int indexNum2 = sc.nextInt();
                System.out.println("Enter new Index Number's Schedule: ");
                int updateSchedule = sc.nextInt();
                // CourseList.updateIndexNumSchedule(courseCode, indexNum2, updateSchedule); //
                // haven't create yet
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
    private static String[] indexDetailsConverter(int classType, int weekType, int dayOfTheWeek) {
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

    // ****** Method to make sure the indexnum entered exist in database. ******
    private static int checkIndexNumExistence(Scanner sc) {
        int indexNum;
        do {
            System.out.print("Please enter the index number you want to query: ");
            indexNum = sc.nextInt();
        } while (!(CourseList.checkIndexExistence(indexNum)));
        return indexNum;
    }

    // ****** Method to make sure the coursecode entered exist in database. ******
    private static String checkCourseExistence(Scanner sc) {
        String courseCode;
        do {
            System.out.print("Please enter the Course code you want to query: ");
            courseCode = sc.nextLine();
        } while (!(CourseList.checkCourseExistence(courseCode)));
        return courseCode;
    }

    private static int getNumTimeslots(int start, int end) {
        int num = 0;
        while (start != end) {
            start += 30;
            num += 1;
            if (start % 100 == 60) {
                start += 40;
            }
        }
        return num;
    }
}
