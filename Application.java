import java.util.*;

public class Application{

	public static void main(String[] args) {

        /* Just for testing purposes, the actual one is reading data from a file and insert those values into
        the respective objects */

        IndexNum 1024 = new IndexNum("1000-1200", "LT21", 8);
        IndexNum 1025 = new IndexNum("1300-1400", "LT02", 10);

        Course Course1 = new Course("Cz2002", "SCSE", "LectTutsLab");


        Scanner sc = new Scanner(System.in);
        Console con = null;
        System.out.println("Welcome to MySTARS\nPlease login to continue");
        System.out.print("\nUsername: ");
        String username = sc.next();
        System.out.print("\nPassword: ");
        char[] password = Con.readpassword();

        //TODO: Check Password:

        Person user = PersonList.getByUsername(username);

        if( user instanceof Student){
            StudentApp(sc, user, username);
        }
        else if (user instanceof Admin){
            AdminApp(sc, user, username);
        }
    }

    public void StudentApp(Scanner sc, Student st, String username){
        System.out.println("Welcome to MySTARS!");
        System.out.println("Enter one of the following options to continue");

        System.out.print("1. Add Course\n2. Drop Course\n3. Print Courses Registered\n4. Check Vacancies\n5. Change Course Index number\n6. Swop Index number\n7. Logout");

        int choice = 0;
        while (choice <=7){
            choice = sc.nextInt();
            switch (choice){
                case 1:
                    System.out.print("Enter Course code: ");
                    String coursecode = sc.next();
                    System.out.print("Enter Index number: ");
                    int indexnum = sc.nextInt();
                    if (CourseList.checkCode(coursecode) & CourseList.checkIndex(coursecode, indexnum)){
                        st.add_course(coursecode, indexnum);
                        break;
                    }
                    else{
                        System.out.println("Error! Please enter a valid Course code / Index number");
                        break;
                    }
                case 2:
                    System.out.print("Enter Course code: ");
                    String course = sc.next();
                    System.out.print("Enter Index number: ");
                    int index = sc.nextInt();
                    st.drop_course(course, index);
                    break;
                case 3:
                    st.check_course();
                case 4:
                    

                    
            }
        }
    }

    public void AdminApp(Scanner sc, Admin ad, String username){}
}

