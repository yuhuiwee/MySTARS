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

    private static HashMap<String, Person> plist = null;

    public PersonList() throws UserAlreadyExists {

        // Student(username, name, matric, gender, nationality)

        plist = new HashMap<String, Person>();
        newStudent("TestStudent1", "Test Student 1", "U1234567890", 'M', "Singaporean", "Comp Sci", 1);
        newStudent("TestStudent2", "Test Student 2", "U2345678901", 'F', "Singaporean", "Comp Sci", 2);
        newStudent("TestStudent3", "Test Student 3", "U3456789012", 'M', "Singaporean", "Comp Sci", 3);
        newStudent("TestStudent4", "Test Student 4", "U4567890123", 'F', "Singaporean", "Comp Sci", 4);
        newStudent("TestStudent5", "Test Student 5", "U5678901234", 'M', "Singaporean", "Comp Sci", 1);
        newStudent("TestStudent6", "Test Student 6", "U6789012345", 'F', "Singaporean", "Mathematics", 2);
        newStudent("TestStudent7", "Test Student 7", "U7890123456", 'M', "Singaporean", "Mathematics", 3);
        newStudent("TestStudent8", "Test Student 8", "U8901234567", 'F', "Singaporean", "Mathematics", 4);
        newStudent("TestStudent9", "Test Student 9", "U9012345678", 'M', "Singaporean", "Mathematics", 1);
        newStudent("TestStudent10", "Test Student 10", "U0123456789", 'F', "Malaysian", "Mathematics", 2);
        newStudent("TestStudent11", "Test Student 11", "U0987654321", 'M', "Indian", "Philosophy", 3);
        newStudent("TestStudent12", "Test Student 12", "U9876543210", 'F', "Chinese", "Philosophy", 4);
        newStudent("TestStudent13", "Test Student 13", "U8765432109", 'M', "Thai", "NIE", 1);
        newStudent("TestStudent14", "Test Student 14", "U7654321098", 'F', "Filipino", "NIE", 2);
        newStudent("TestStudent15", "Test Student 15", "U6543210987", 'M', "Indonesian", "Business", 3);

        newAdmin("Admin1", "Admin 1", "G1234567890", 'M', "Singaporean", "Professor");
        newAdmin("Admin2", "Admin 2", "G2345678901", 'F', "Singaporean", "Associate Professor");

        // write to ser file
        savePersonMap();
    }

    @SuppressWarnings("unchecked")
    public static void loadPersonList() throws UserAlreadyExists {
        try {
            FileInputStream fis = new FileInputStream("PersonList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object temp = ois.readObject();
            if (temp instanceof HashMap<?, ?>) {
                plist = (HashMap<String, Person>) temp;
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

        if (plist.containsKey(username)) {
            return plist.get(username);
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
            String major, int year) throws UserAlreadyExists {
        // Check is username and matric number is already taken!
        if (plist.containsKey(username) & plist != null) {
            throw new UserAlreadyExists("This Username is taken!");
        } else {
            for (Person p : plist.values()) {
                if (p.getMatric() == matric) {
                    throw new UserAlreadyExists("Matric number is taken!");
                }
            }
        }

        Student s = new Student(username.toLowerCase(), name, matric, Character.toUpperCase(gender), nationality, major,
                year);
        plist.put(username.toLowerCase(), s);
        PersonList.savePersonMap(); // Save map immediately after creating new student
        return;

    }

    public static void removeStudent(String username) throws UserNotFound, UserAlreadyExists {
        if (plist == null) {
            loadPersonList();
        }

        Student s = getStudentByUsername(username);
        s.dropAllCourse();
        PasswordHash.removeUser(username);
        plist.remove(username, s);
    }

    public static void newAdmin(String username, String name, String id, char gender, String nationality,
            String position) throws UserAlreadyExists {
        if (plist == null) {
            PersonList.loadPersonList();
        }

        // Check is username and matric number is already taken!
        if (plist.containsKey(username)) {
            throw new UserAlreadyExists("This Username is taken!");
        } else {
            for (Person p : plist.values()) {
                if (p.getMatric() == id) {
                    throw new UserAlreadyExists("Matric number is taken!");
                }
            }
        }

        Admin ad = new Admin(username.toLowerCase(), name, id, Character.toUpperCase(gender), nationality, position);
        plist.put(username.toLowerCase(), ad);
        PersonList.savePersonMap();
    }

    public static void removeAdmin(String username) throws UserNotFound, UserAlreadyExists {
        if (plist == null) {
            loadPersonList();
        }

        if (plist.containsKey(username)) {
            if (plist.get(username) instanceof Admin) {
                plist.remove(username);
            } else {
                throw new UserNotFound("Admin not found!");
            }
        } else {
            throw new UserNotFound("Admin not found");
        }
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

    public static void editAdmin(String username, String name, String id, char gender, String nationality,
            String position) throws UserNotFound, UserAlreadyExists {
        if (plist == null) {
            PersonList.loadPersonList();
        }
        PersonList.removeAdmin(username);
        PersonList.newAdmin(username, name, id, gender, nationality, position);
    }

    public static boolean checkusername(String username) throws UserAlreadyExists {
        if (plist == null) {
            loadPersonList();
        }

        if (plist.containsKey(username)) {
            return true;
        } else {
            return false;
        }
    }
}