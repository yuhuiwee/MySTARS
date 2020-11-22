import java.util.*;

import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class Application {

    public static void main(String[] args) throws UserNotFound, UserAlreadyExists, WrongPassword, CourseDontExist,
            AccessPeriodNotFound, CourseAlreadyExist, TimetableClash, CloneNotSupportedException {

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
            System.out.println("Welcome to MySTARS!");
            System.out.println(user.getName().toUpperCase()); // print user's name

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

    private static void StudentApp(Scanner sc, Student st, String username, Console con) throws UserNotFound,
            UserAlreadyExists, WrongPassword, CourseDontExist, TimetableClash, CloneNotSupportedException {
        String coursecode;
        int indexnum;
        int index2;
        int elec;

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
                    System.out.println("Enter elective type:\n1. CORE\n2. Major PE\n3.GER-PE\n4. GER-UE\n5. UE");
                    elec = sc.nextInt();
                    Course c = CourseList.getCourse(coursecode);
                    if (!st.checkAU(c.getAU())) { // check if au exceeded
                        System.out.println("Maxinum AUs exceeded!");
                        break;
                    }
                    if (CourseList.checkCourseExistence(coursecode) & CourseList.checkIndex(coursecode, indexnum)) {
                        st.addCourse(coursecode, indexnum, elec);
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
                    System.out.println("Total AU registered: " + String.valueOf(st.getAU()));
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
                    int index = st.verifyCourse(coursecode);
                    if (index == -1) {
                        throw new CourseDontExist("You are not Registered for this course!");
                    }
                    System.out.print("Enter new Index Number: ");
                    index2 = sc.nextInt();
                    if (!st.swapIndexCheck(index, index2)) {
                        System.out.println("Error! There is a time clash! You are not able to swop to this index!");
                        break;
                    }
                    if (CourseList.checkVacancies(index) > 0) {

                        elec = st.getCourseElectiveIndex(coursecode);
                        st.dropCourse(coursecode, index);
                        st.addCourse(coursecode, index2, elec);
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
                    if (!st.swapIndexCheck(indexnum, index2)) {
                        System.out.println(
                                "Error! There is a time clash with the new index! Please try again with another person/index");
                        break;
                    }

                    Swop.swopStudent(coursecode, indexnum, index2, username, newuser);
                    break;
                case 7:// change password
                    char[] orgpwd = con.readPassword("Old Password: ");
                    char[] newpwd = con.readPassword("New Password: ");
                    char[] reppwd = con.readPassword("Repeat Password: ");
                    if (String.valueOf(newpwd).equals(String.valueOf(reppwd))) {
                        PasswordHash.changePwd(String.valueOf(orgpwd), String.valueOf(newpwd), username);
                        break;
                    } else {
                        throw new WrongPassword("Password do not match!");
                    }
                case 8:// change elective type for course
                    System.out.println("Enter the course code to change: ");
                    coursecode = sc.nextLine();
                    System.out.println(
                            "Enter elective type to change to:\n1. CORE\n2. Major PE\n3.GER-PE\n4. GER-UE\n5. UE");
                    elec = sc.nextInt();
                    st.swapElectiveType(coursecode, elec);
                case 9: // Quit
                    System.out.println("Thank you for using MySTARS!\nQuitting...");
                    break;
                default:
                    System.out.println("Quitting...");
                    break;
            }
        }
    }

    private static void AdminApp(Scanner sc, Admin ad, String username, Console con)
            throws UserAlreadyExists, AccessPeriodNotFound, UserNotFound, CourseDontExist, CourseAlreadyExist,
            TimetableClash, CloneNotSupportedException {
        String s_name;
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
                    + "(6) Print student list by index number\n" + "(7) Print registered student list by course\n"
                    + "(8) Remove Student\n(9)Edit Student Details\n(10)Edit Profile Details\n(11)Quit\n");
            sel = sc.nextInt();
            switch (sel) {
                case 1:// edit student access period
                    int acc = 0;
                    do {
                        System.out.println("(1) Print all Access Period\n(2) Edit default Access Period\n"
                                + "(3)Add new Access Period by Major/Year\n" + "(4)Remove Access Peiod for Major/Year\n"
                                + "(5)Remove all Access Peiod by Major/Year\n" + "(6)Quit");
                        acc = sc.nextInt();
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
                                    major = sc.nextLine();
                                    System.out.println("Enter year: \nFrom");
                                    f = sc.nextInt();
                                    System.out.println("to ");
                                    t = sc.nextInt();

                                    // error prompts
                                    if (t < f) {
                                        System.out.println("Please enter in ascending order!");
                                    } else if (t > 6 | f > 6) {
                                        System.out.println("Please enter a valid year! (max year = 6");
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
                case 2: // add a student
                    check = true;
                    do {
                        System.out.print("Enter student's username: ");
                        username = sc.nextLine();
                        check = PersonList.checkusername(username);
                        if (check) {
                            System.out.println("This username is already taken!");
                        }
                    } while (check);
                    System.out.print("Enter student's name: ");
                    s_name = sc.nextLine();
                    check = true;
                    do {
                        System.out.print("Enter student's matric number: ");
                        matric = sc.nextLine();
                        check = PersonList.checkMatricNum(matric);
                        if (check) {
                            System.out.println("This matric number is already taken!");
                        }
                    } while (check);
                    System.out.print("\nEnter student's gender(M/F): ");
                    gender = sc.next().charAt(0);
                    while (Character.toLowerCase(gender) != 'f' | Character.toLowerCase(gender) != 'm') {
                        System.out.println("\nError! Please re-enter gender: ");
                        gender = sc.next().charAt(0);
                    }
                    System.out.print("\nEnter student's nationality: ");
                    nationality = sc.next();
                    System.out.print("\nEnter student's major: ");
                    major = sc.nextLine();
                    System.out.print("\nEnter student's year: ");
                    year = sc.nextInt();
                    System.out.print("\nEnter student's email: ");
                    email = sc.nextLine();
                    while (!email.contains("@")) {
                        System.out.println("Invalid email address!");
                        System.out.print("\nPlease re-enter email: ");
                        email = sc.nextLine();
                    }
                    PersonList.newStudent(username.toLowerCase(), s_name, matric, Character.toUpperCase(gender),
                            nationality, major, year, email);
                    break;

                case 3:
                    newCourse(sc, ad);
                    break;

                case 4:
                    updateCourse(sc, ad); // TODO: Create the logic for UpdateCourse info
                    break;
                case 5:// check vacancies
                    check = false;
                    do {
                        System.out.print("Enter index number: ");
                        index = sc.nextInt();
                        check = CourseList.checkIndexExistence(index);
                        if (!check) {
                            System.out.println("This course does not exist!");
                        }
                    } while (!check);
                    int vacancy = CourseList.getIndexNum(index).getVacancy();
                    System.out.println("\nThe vacancy for index " + index + " is: " + vacancy);
                    break;
                case 6:
                    int option;
                    do {
                        System.out.println("(1) Print Student List by Course: ");
                        System.out.println("(2) Print Student List by Index Number: ");
                        option = sc.nextInt();

                        switch (option) {
                            case 1:
                                check = false;
                                do {
                                    System.out.print("Enter Course code: ");
                                    courseCode = sc.nextLine();
                                    check = CourseList.checkCourseExistence(courseCode);
                                    if (!check) {
                                        System.out.println("This course does not exist!");
                                    }
                                } while (!check);
                                CourseList.getCourse(courseCode).printStudentList();
                                break;
                            case 2:
                                check = false;
                                do {
                                    System.out.print("Enter index number: ");
                                    index = sc.nextInt();
                                    check = CourseList.checkIndexExistence(index);
                                    if (!check) {
                                        System.out.println("This course does not exist!");
                                    }
                                } while (!check);
                                CourseList.getIndexNum(index).printStudentList();
                                break;
                            default:
                                System.out.println("Please choose a valid option!");
                        }

                    } while (option > 0 & option <= 2);

                case 7:// remove student
                    check = false;
                    do {
                        System.out.println("Username of Student to be removed: ");
                        username = sc.nextLine();
                        check = PersonList.checkusername(username);
                        if (!check) {
                            System.out.println("Error! This Student does not exist!");
                        } else { // check if username entered is a student's username
                            if (PersonList.getByUsername(username) instanceof Student) {
                                check = true;
                            } else {
                                check = false;
                            }
                        }
                    } while (!check);
                    PersonList.removeStudent(username);
                    break;

                case 8: // edit student details
                    check = false;
                    do {
                        System.out.println("Username of Student to edit: ");
                        username = sc.nextLine();
                        check = PersonList.checkusername(username);
                        if (!check) {
                            System.out.println("Error! This Student does not exist!");
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
                                "(1) Name\n(2) Matric\n(3) Gender\n(4) Nationality\n(5) Major\n(6) Year\n(7) Email (8) Submit");

                        editchoice = sc.nextInt();

                        switch (editchoice) {
                            case 1: // name
                                System.out.println("Current name: " + s_name);
                                System.out.print("\nNew Name: ");
                                s_name = sc.nextLine();
                                s.setName(s_name);
                                break;
                            case 2:// matric
                                check = true;
                                do {
                                    System.out.println("Current matric: " + matric);
                                    System.out.print("\nNew Matrix: ");
                                    matric = sc.nextLine();
                                    check = PersonList.checkMatricNum(matric);
                                    if (check) {
                                        System.out.println("this matric number is already taken!");
                                    }
                                } while (check);
                                s.setMatric(matric);
                                break;
                            case 3: // gender
                                System.out.println("Current gender: " + Character.toString(gender));
                                System.out.print("\nNew Gender: ");
                                gender = sc.next().charAt(0);
                                while (Character.toLowerCase(gender) != 'f' | Character.toLowerCase(gender) != 'm') {
                                    System.out.println("Error! Please re-enter gender: ");
                                    gender = sc.next().charAt(0);
                                }
                                s.setGender(gender);
                                break;
                            case 4: // nationality
                                System.out.println("Current nationality: " + nationality);
                                System.out.print("\nNew Nationality: ");
                                nationality = sc.nextLine();
                                s.setNationality(nationality);
                                break;
                            case 5: // major
                                System.out.println("Current major: " + major);
                                System.out.print("\nNew major: ");
                                major = sc.nextLine();
                                s.setSchool(major);
                                break;
                            case 6: // year
                                System.out.println("Current year: " + String.valueOf(year));
                                System.out.print("\nUpdated Year: ");
                                year = sc.nextInt();
                                s.setYear(year);
                                break;
                            case 7: // email
                                System.out.println("Current email: " + email);
                                System.out.print("\nNew email: ");
                                email = sc.nextLine();
                                while (!email.contains("@")) {
                                    System.out.println("Error! Please enter a valid email: ");
                                    email = sc.nextLine();
                                }
                                s.setEmail(email);
                                break;
                            case 9:// maxau
                                System.out.println("Current max AU: " + String.valueOf(au));
                                System.out.print("\nNew max AU: ");
                                au = sc.nextInt();
                                s.setMaxAU(au);
                                break;
                        }
                    } while (editchoice > 0 & editchoice < 8);

                    break;

                case 9:// edit own details
                    break;
                case 10:
                    System.out.println("Thank you for using MYSTARS");

            }
        }

        while (sel != 10);
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

    // ****** CREATE NEW COURSE ******
    private static void newCourse(Scanner sc, Admin ad)
            throws CourseAlreadyExist, TimetableClash, CloneNotSupportedException {
        String venue, courseCode, school;
        int index, vacancy, classNum, startTime, endTime, totalIndex, m;
        int classType = -1, weekType = -1, dayOfTheWeek = -1;
        int totalHalfInterval, temp1, temp2, temp3, temp4, startSerial, endSerial;
        String[] indexDetails;
        HashMap<Integer, IndexNum> indexMap = new HashMap<Integer, IndexNum>();
        ArrayList<Integer> classArrayTypes = new ArrayList<Integer>();
        ArrayList<Timetable> lectures = new ArrayList<Timetable>();
        boolean check;
        IndexNum tempIndex;
        Timetable time = new Timetable();
        String courseType;
        Timetable t;
        int au;

        sc.nextLine();

        // ENTER COURSE CODE AND SCHOOL
        do {
            CourseList.printAllCourse(null); // So if value null, all courses will be printed out
            System.out.print("\nEnter the new Course Code: ");
            courseCode = sc.nextLine();
            school = ad.getSchool();
            check = CourseList.checkCourseExistence(courseCode);
            if (check) { // true == coursecode exists
                System.out.println("This course alreay exists!");
            }
        } while (check);

        Course c = CourseList.newCourse(courseCode, school);

        System.out.print("Enter the number of credits (AU) Course " + courseCode);
        au = sc.nextInt();
        c.setAU(au);

        // ENTER TOTAL NUMBER OF INDEX IN THIS NEW COURSE
        System.out.println("Indexes that are in use: ");
        CourseList.printAllIndex();
        System.out.print("Enter the total number of indexes to add to the course: ");
        totalIndex = sc.nextInt();

        System.out.printf("Enter %d new index numbers to add to the course: ", totalIndex);
        for (int i = 0; i < totalIndex; i++) {
            do {
                index = sc.nextInt();
                if (CourseList.checkIndexExistence(index)) {
                    System.out.println("This index is already in use!");
                    System.out.println("Please re-enter!");
                }
            } while (CourseList.checkIndexExistence(index));
            System.out.print("Enter the number of vacancies in index " + String.valueOf(index));
            vacancy = sc.nextInt();
            tempIndex = new IndexNum(index, vacancy);
            indexMap.put(index, tempIndex);
        }
        CourseList.newIndexNumbers(indexMap); // add to indexlist in courselist
        c.setIndexNumber(indexMap); // add index to Course

        System.out.println("\n=====================================================================================");
        System.out.println("Classes can only conduct from Monday to Friday, between 0800 to 1900 hrs."
                + "\nAdjust your number of classes and duration accordingly.");
        System.out.println("\n=====================================================================================\n");

        System.out.print("Enter the number of lectures: ");
        classNum = sc.nextInt();
        for (int i = 0; i < classNum; i++) {
            t = getCourseDetails(sc, "Lecture", courseCode, 0); // use 0 as a temporary index
            time.mergeTimetable(t);
        }
        System.out.print("Enter the total number of tutorials/seminars/labs per indexnum: ");
        // Num of classes per index number
        classNum = sc.nextInt();

        Timetable temp = new Timetable();

        for (Map.Entry<Integer, IndexNum> entry : indexMap.entrySet()) { // For each index created in this course
            temp = time.clone();
            index = entry.getKey();
            System.out.println("For Index: " + String.valueOf(index));
            temp.editIndex(index);
            for (int i = 0; i < classNum; i++) { // Classes in this index
                System.out.println("Enter Course Type: \n\tEg. Lecture, Seminar, Lab etc.");
                courseType = sc.nextLine(); // will not be checking coursetype
                t = getCourseDetails(sc, courseType, courseCode, index);// edit venue timetable // But there is no
                                                                        // checking whether these classes internally
                                                                        // clash?
                temp.mergeTimetable(t);
            }
            entry.getValue().addClassSchedule(temp);// add to indexnum timetable
        }

        System.out.println("You have successfully added the courses");

        // NOTE: For debugging purposes! Delete after debugging
        for (Map.Entry<Integer, IndexNum> entry : indexMap.entrySet()) {
            entry.getValue().printCourseSchedule();
        }

    }

    // ****** Method to update a course ******
    private static void updateCourse(Scanner sc, Admin ad)
            throws NumberFormatException, TimetableClash, CloneNotSupportedException {
        String courseCode, newCourseCode;
        int choice, index, numClass, newIndex;
        boolean check;
        Course c;
        CourseList.printAllCourse(ad.getSchool()); // So for depending on which school the admin works for, it will only
                                                   // print courses registered
                                                   // under that school. So this prevents admin from school A from
                                                   // modifying courses of school B
        Timetable t = new Timetable();
        char ch;
        IndexNum ind;

        check = false;
        do {
            System.out.print("Enter Course Code that you would like to update: ");
            courseCode = sc.nextLine();
            check = CourseList.checkCourseExistence(courseCode);
            if (!check) {
                System.out.println("Error! Course Code not found!");
            }
        } while (!check);

        c = CourseList.getCourse(courseCode);

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
                // COURSE CODE NAME
                check = true;
                do {
                    System.out.print("Enter new Course Code: ");
                    newCourseCode = sc.nextLine();
                    check = CourseList.checkCourseExistence(newCourseCode);
                    if (check) {
                        System.out.println("This Course code is already in use! Please choose another Course code!");
                    }
                } while (check);

                CourseList.updateCourseCode(newCourseCode, courseCode);
                break;
            case 2:
                // SCHOOL
                System.out.print("Enter the new school: ");
                String school = sc.nextLine();
                c.setSchool(school);
                break;
            case 3:
                // remove course
                CourseList.dropCourseByAdmin(courseCode);
                break;
            case 4:
                check = true;
                do {
                    System.out.println("Enter a new index number to add");
                    index = sc.nextInt();
                    check = CourseList.checkIndexExistence(index);
                    if (check) {
                        System.out.println("This index number is already taken!");
                    }
                } while (check);
                System.out.print("Enter the number of vacancies for this index: ");
                int vacancy = sc.nextInt();
                ind = new IndexNum(index, vacancy);

                do {
                    System.out.println(
                            "Does this index have the same lecture time slots as the rest of the indices? [y/n]");
                    ch = sc.nextLine().charAt(0);

                    switch (Character.toLowerCase(ch)) {
                        case 'y':
                            int[] indexes = c.getIndexNumber();
                            t = CourseList.getIndexNum(indexes[0]).getClassSchedule().getLectureTimings();
                            break;
                        case 'n':
                            t = new Timetable();
                            break;
                        default:
                            System.out.println("Please enter a valid option!");
                            break;
                    }
                } while (Character.toLowerCase(ch) != 'y' & Character.toLowerCase(ch) != 'n');

                t.editIndex(index);
                System.out.print("Enter the number of classes to add: ");
                numClass = sc.nextInt();
                for (int i = 0; i < numClass; i++) {
                    check = false;
                    do {
                        System.out.print("Enter the lesson type (eg. tutorial/seminar/lab): ");
                        String courseType = sc.nextLine();
                        Timetable time = getCourseDetails(sc, courseType, courseCode, index);
                        check = t.checkTimeslot(time);
                        if (!check) {
                            System.out.println("Error! Lessons cannot clash with any other lessons in this index num!");
                        }
                    } while (!check);
                }
                ind.addClassSchedule(t);

                System.out.println("Successfully added new index num!");
                // NOTE: For debugging purposes!
                ind.printCourseSchedule();
                break;
            case 5:
                check = false;
                do {
                    System.out.print("Enter index number to change");
                    index = sc.nextInt();
                    check = c.checkIndex(index);
                    if (!check) {
                        System.out.println("Error! This course does not contain this index number!");
                    }
                } while (!check);

                ind = CourseList.getIndexNum(index);

                check = true;
                do {
                    System.out.print("Enter index number to change to: ");
                    newIndex = sc.nextInt();
                    check = CourseList.checkIndexExistence(newIndex);
                    if (check) {
                        System.out.println("Error! This index is already being used!");
                    }
                } while (check);

                ind.setIndexNumber(newIndex); // update self
                HashMap<Integer, IndexNum> map = new HashMap<Integer, IndexNum>();
                map.put(newIndex, ind);
                CourseList.newIndexNumbers(map); // update courselist
                c.setIndexNumber(map); // update course
                break;
            case 6:
                check = false;
                do {
                    System.out.print("Enter index number: ");
                    index = sc.nextInt();
                    check = c.checkIndex(index);

                    if (!check) {
                        System.out.println("Error! This course does not contain this index!");
                    }
                } while (!check);
                ind = CourseList.getIndexNum(index);
                System.out.println("Change availble vacancies: ");
                System.out.println("Current vacancies: " + String.valueOf(ind.getVacancy()));
                System.out.print("New vacancies: ");
                vacancy = sc.nextInt();

                ind.setVacancy(vacancy);
                break;
            case 7:
                // update index number schedule
                // Might not need this
                System.out.println("Enter Index Number: ");
                int indexNum2 = sc.nextInt();
                System.out.println("Enter new Index Number's Schedule: ");
                int updateSchedule = sc.nextInt();
                // CourseList.updateIndexNumSchedule(courseCode, indexNum2, updateSchedule);
                // haven't create yet
                break;
            case 8:
                System.out.println("Goodbye!");
                System.exit(0);
            default:
                System.out.println("\nInvalid input entered! Please enter only from 1-8");
        }

    }

    private static int getNumTimeslots(int start, int end) {
        int num = 0;
        while (start != end) {
            start = start + 30;
            num = 1;
            if (start % 100 == 60) {
                start += 40;
            }
        }
        return num;
    }

    private static int getTimeInput(Scanner sc, int minTime) {
        int HH = -1;
        int MM = -1;

        do {
            System.out.print("Enter hour (24-hour format): ");
            HH = sc.nextInt();
            if (HH < minTime / 100) { // Hour lesser than 8
                System.out.println("Error! Please enter a valid hour!");
            } // Should we check If user enter value > 23? And since this method is reused for
              // end time, should we check that the time cannot be 20 as 1900 should be the
              // last timing?
        } while (HH < minTime / 100);

        do {
            System.out.print("Enter minutes (00 / 30): ");
            MM = sc.nextInt();
            if (MM != 0 | MM != 15) {// only check this if this is used for lesson time input
                System.out.println("Error! You can only have lessons at 1/2 hour intervals!\nPlease try again!");
            }
        } while (MM != 0 | MM != 15); // Should this be 30 instead?

        int time = HH * 100 + MM;

        String minTimeStr = "";
        if (minTime < 1000) { // If start time is 0900, we add 0 as the first string to represent 0900
                              // properly, instead of 900.
            minTimeStr = "0";
        }
        minTimeStr = minTimeStr + String.valueOf(time);
        if (minTime > time) { // Why do we have to recheck if hour in time is lesser than 800 again?
            System.out.print("You cannot enter a timing before " + minTimeStr);
            System.out.println("Please try again!");
            time = getTimeInput(sc, minTime);
        }

        return time;
    }

    private static Timetable getCourseDetails(Scanner sc, String courseType, String courseCode, int index)
            throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
        int serialNum, startSerial, endSerial;
        int classFreq;
        int day, time;
        String venue;
        Timetable t;
        boolean check;

        do {
            System.out.println("Class Frequency: \n\t(1) Every Even Week\n\t(2) Every Odd Week\n\t(3) Every Week");
            System.out.print("Enter class frequency");
            classFreq = sc.nextInt();

            if (classFreq <= 0 | classFreq > 3) {
                System.out.println("Please enter a valid option!");
            }
        } while (classFreq <= 0 | classFreq > 3);

        serialNum = classFreq - 1;

        do {
            System.out.println("Day of the Week:");
            System.out.println("\t(1) Monday\n\t(2) Tuesday\n\t.\n\t.\n\t(5) Friday");
            System.out.print("Enter day of week: ");

            day = sc.nextInt();

            if (day <= 0 | day > 7) {
                System.out.println("Error! Please enter a valid option!");
            } else if (day > 5) {
                System.out.println("Classes can only be held between Monday - Friday!");
            }

        } while (day <= 0 | day > 5);

        serialNum = serialNum + day * 100000;

        System.out.println("Venue: ");
        System.out.println("Enter the location that this lesson will be held");
        System.out.println("List of venues currently in our database: ");
        VenueList.printAllVenues();

        System.out.println("Enter the venue: ");
        venue = sc.nextLine();

        if (VenueList.checkVenue(venue)) {
            t = VenueList.getTimetable(venue);
            System.out.println("Bookings for this venue: ");
            t.printWeeklySchedule();

        } else {
            VenueList.newVenue(venue);
            t = VenueList.getTimetable(venue);
        }

        do {
            System.out.println("Enter Start Time: ");
            time = getTimeInput(sc, 800); // 800 represents 8AM, so basically it is used to check if starttime is before
                                          // 8
            startSerial = serialNum + time * 10;
            System.out.println("Enter End Time: ");
            time = getTimeInput(sc, time); // Have to pass in starttime as an argument as we are reusing this method for
                                           // both start and endtime
            endSerial = serialNum + time * 10;
            check = t.checkStartEnd(startSerial, endSerial);
            if (!check) {
                System.out.println("Sorry the venue is booked for this timing already!");
            }
        } while (!check);

        t.addClass(startSerial, endSerial, courseCode, index, courseType, venue);
        // NOTE: Venue should technically be updated already...
        // if it isnt, un-comment bottomline
        // VenueList.updateTimetable(venue, t.clone());
        return t.clone(); // Return the clone of the timetable object
    }

}
