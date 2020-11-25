import java.io.*;
import java.util.*;

public class PersonList {
    /*
     * Methods: Constructor: PersonList: Create default PersonList hashmap
     * loadPersonList(): void getStudentMap(): Hashmap<String,Student>
     * getStudentByUsername(String username): Student savePersonMap(): void
     * newStudent(String username, String name, String matric, char gender, String
     * nationality): void removeStudent(String username): void
     * editStudentDetails(String username, String name, String matric, char gender,
     * String nationality): void newAdmin(String username, String name, String
     * matric, char gender, String nationality, String position): void
     * removaAdmin(String username): void getAdminMap(): Hashmap<String, Admin>
     * getAdminByUsername(String username): Admin editAdmin(String username, String
     * name, String id, char gender, String nationality, String position): void
     */

    private static TreeMap<String, Person> plist = null;

    public PersonList() throws UserAlreadyExists {

        // Student(username, name, matric, gender, nationality)

        plist = new TreeMap<String, Person>();
        new PasswordHash();
        newStudent("TestStudent1", "Test Student 1", "U1234567890", 'M', "Singaporean", "Comp Sci", 1,
                "studentcz2002ss1@gmail.com");
        newStudent("TestStudent2", "Test Student 2", "U2345678901", 'F', "Singaporean", "Comp Sci", 2,
                "studentcz2002ss1@gmail.com");
        newStudent("TestStudent3", "Test Student 3", "U3456789012", 'M', "Singaporean", "Comp Sci", 3,
                "studentcz2002ss1@gmail.com");
        newStudent("TestStudent4", "Test Student 4", "U4567890123", 'F', "Singaporean", "Comp Sci", 4,
                "studentcz2002ss1@gmail.com");
        newStudent("TestStudent5", "Test Student 5", "U5678901234", 'M', "Singaporean", "Comp Sci", 1,
                "studentcz2002ss1@gmail.com");
        newStudent("TestStudent6", "Test Student 6", "U6789012345", 'F', "Singaporean", "Mathematics", 2,
                "studentcz2002ss1@gmail.com");
        newStudent("TestStudent7", "Test Student 7", "U7890123456", 'M', "Singaporean", "Mathematics", 3,
                "studentcz2002ss1@gmail.com");
        newStudent("TestStudent8", "Test Student 8", "U8901234567", 'F', "Singaporean", "Mathematics", 4,
                "studentcz2002ss1@gmail.com");
        newStudent("TestStudent9", "Test Student 9", "U9012345678", 'M', "Singaporean", "Mathematics", 1,
                "studentcz2002ss1@gmail.com");
        newStudent("TestStudent10", "Test Student 10", "U0123456789", 'F', "Malaysian", "Mathematics", 2,
                "studentcz2002ss1@gmail.com");
        newStudent("TestStudent11", "Test Student 11", "U0987654321", 'M', "Indian", "Philosophy", 3,
                "studentcz2002ss1@gmail.com");
        newStudent("TestStudent12", "Test Student 12", "U9876543210", 'F', "Chinese", "Philosophy", 4,
                "studentcz2002ss1@gmail.com");
        newStudent("TestStudent13", "Test Student 13", "U8765432109", 'M', "Thai", "NIE", 1,
                "studentcz2002ss1@gmail.com");
        newStudent("TestStudent14", "Test Student 14", "U7654321098", 'F', "Filipino", "NIE", 2,
                "studentcz2002ss1@gmail.com");
        newStudent("TestStudent15", "Test Student 15", "U6543210987", 'M', "Indonesian", "Business", 3,
                "studentcz2002ss1@gmail.com");

        newAdmin("Admin1", "Admin 1", "G1234567890", 'M', "Singaporean", "Computer Science",
                "Professor", "cz2002ss1@gmail.com");
        newAdmin("Admin2", "Admin 2", "G2345678901", 'F', "Singaporean", "Philosophy",
                "Associate Professor", "cz2002ss1@gmail.com");

        // write to ser file
        savePersonMap();
    }

    @SuppressWarnings("unchecked")
    public static void loadPersonList() throws UserAlreadyExists {
        try {
            FileInputStream fis = new FileInputStream("PersonList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object temp = ois.readObject();
            if (temp instanceof TreeMap<?, ?>) {
                plist = (TreeMap<String, Person>) temp;
            }
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            // ioe.printStackTrace();
            new PersonList(); // If file not found, create default values
        } catch (ClassNotFoundException c) {
            // System.out.println("Class not found");
            // c.printStackTrace();
            new PersonList();
        }
    }

    public static Person getByUsername(String username) throws UserNotFound, UserAlreadyExists {
        if (plist == null) {
            loadPersonList();
        }

        if (plist.containsKey(username.toUpperCase())) {
            return plist.get(username.toUpperCase());
        } else {
            throw new UserNotFound("User not found!");
        }
    }

    public static HashMap<String, Student> getStudentMap() throws UserAlreadyExists {
        // load hashmap if plist is null
        if (plist == null) {
            PersonList.loadPersonList();
        }

        HashMap<String, Student> temp = new HashMap<String, Student>();
        for (Map.Entry<String, Person> entry : plist.entrySet()) {
            if (entry.getValue() instanceof Student) {
                temp.put(entry.getKey(), (Student) entry.getValue());
            }
        }
        return temp;
    }

    public static void savePersonMap() {
        try {
            FileOutputStream fos = new FileOutputStream("PersonList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(plist);
            oos.close();
            fos.close();
            return;
        }

        catch (IOException ioe) {
            ioe.printStackTrace();
            return;
        }
    }

    public static void newStudent(String username, String name, String matric, char gender, String nationality,
            String major, int year, String email) throws UserAlreadyExists {
        // Check is username and matric number is already taken!
        if (plist.containsKey(username) & plist != null) {
            throw new UserAlreadyExists("This Username is taken!");
        }
        if (checkMatricNum(matric)) {
            throw new UserAlreadyExists("This matric number is already taken!");
        }
        Student s = new Student(username.toUpperCase(), name.toUpperCase(), matric.toUpperCase(),
                Character.toUpperCase(gender), nationality.toUpperCase(), major.toUpperCase(), year,
                email.toUpperCase());
        plist.put(username.toUpperCase(), s);
        savePersonMap(); // Save map immediately after creating new student
        return;

    }

    public static void removeStudent(String username) throws UserNotFound, UserAlreadyExists, CourseDontExist,
            CloneNotSupportedException, TimetableClash, VenueAlreadyExists {
        if (plist == null) {
            loadPersonList();
        }

        Student s = (Student) getByUsername(username.toUpperCase());
        s.dropAllCourse();
        PasswordHash.removeUser(username.toUpperCase());
        plist.remove(username.toUpperCase(), s);
        s = null;
        savePersonMap();
    }

    public static void newAdmin(String username, String name, String id, char gender, String nationality, String school,
            String position, String email) throws UserAlreadyExists {
        if (plist == null) {
            PersonList.loadPersonList();
        }

        // Check is username and matric number is already taken!
        if (plist.containsKey(username.toUpperCase())) {
            throw new UserAlreadyExists("This Username is taken!");
        }
        if (checkMatricNum(id.toUpperCase())) {
            throw new UserAlreadyExists("This matric number is taken!");
        }

        Admin ad = new Admin(username.toUpperCase(), name.toUpperCase(), id.toUpperCase(),
                Character.toUpperCase(gender), nationality.toUpperCase(), school.toUpperCase(), position.toUpperCase(),
                email.toUpperCase());
        plist.put(username.toUpperCase(), ad);
        PersonList.savePersonMap();
    }

    public static void removeAdmin(String username) throws UserNotFound, UserAlreadyExists {
        if (plist == null) {
            loadPersonList();
        }

        if (plist.containsKey(username.toUpperCase())) {
            if (plist.get(username.toUpperCase()) instanceof Admin) {
                plist.remove(username.toUpperCase());
            } else {
                throw new UserNotFound("Admin not found!");
            }
        } else {
            throw new UserNotFound("Admin not found");
        }
        savePersonMap();
    }

    public static HashMap<String, Admin> getAdminMap() throws UserAlreadyExists {
        if (plist == null) {
            loadPersonList();
        }

        HashMap<String, Admin> temp = new HashMap<String, Admin>();
        for (Map.Entry<String, Person> entry : plist.entrySet()) {
            if (entry.getValue() instanceof Admin) {
                temp.put(entry.getKey(), (Admin) entry.getValue());
            }
        }
        return temp;
    }

    public static boolean checkusername(String username) throws UserAlreadyExists {
        if (plist == null) {
            loadPersonList();
        }

        if (plist.containsKey(username.toUpperCase())) {
            return true;
        } else {
            return false; //false if  not used
        }
    }

    public static boolean checkMatricNum(String matric) throws UserAlreadyExists {
        if (plist == null) {
            loadPersonList();
        }
        for (Person p : plist.values()) {
            if (p.getMatric() .equals( matric.toUpperCase())) {
                return true;
            }
        }
        return false; // return false id matric isnt taken
    }

    public static void printStudentList() throws UserAlreadyExists {
        if (plist == null) {
            loadPersonList();
        }
        Student s;
        for (Map.Entry<String, Person> entry: plist.entrySet()){
            if (entry.getValue() instanceof Student){
                s = (Student) entry.getValue();
                s.printStudentDetails();
                System.out.println();
            }
        }
    }
}