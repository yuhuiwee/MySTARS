import java.util.*;
import java.io.*;
import java.time.format.DateTimeFormatter;

public class Application {

    public static void main(String[] args) throws UserNotFound, UserAlreadyExists, WrongPassword {

        /*
         * Just for testing purposes, the actual one is reading data from a file and
         * insert those values into the respective objects
         */

        // IndexNum 1024 = new IndexNum("1000-1200", "LT21", 8);
        // IndexNum 1025 = new IndexNum("1300-1400", "LT02", 10);

        // Course Course1 = new Course("Cz2002", "SCSE", "LectTutsLab");

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
            System.out.print("(1) Edit/Check Student Access Period\n" + "(2) Add Student\n" + "(3) Add/Update course\n"
                    + "(4) Check Available slot for an index number\n" + "(5) Print student list by index number\n"
                    + "(6) Print registered student list by course\n" + "(7) Quit\n");
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
                    // ad.addNewCourse();
                    break;

                case 4:
                    // ad.checkVacancies();
                    break;
                case 5: {
                    int index = sc.nextInt();
                    System.out.print("Enter index number of course: \n");
                    ad.printByIndex(index);
                    break;
                }
                case 6: {
                    String coursecode = sc.nextLine();
                    System.out.print("Enter course name: \n");
                    ad.printByCourse(coursecode);
                    break;
                }

            }
        }

        while (sel != 7);
    }
}
