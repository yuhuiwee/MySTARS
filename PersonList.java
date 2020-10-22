import java.io.*;
import java.util.*;

public class PersonList {
    /*
    Methods:
    Constructor: PersonList: Create default PersonList hashmap
    loadPersonList(): void
    getStudentMap(): Hashmap<String,Student>
    getStudentByUsername(String username): Student
    savePersonMap(): void
    newStudent(String username, String name, String matric, char gender, String nationality): void
    removeStudent(String username): void
    editStudentDetails(String username, String name, String matric, char gender, String nationality): void
    newAdmin(String username, String name, String matric, char gender, String nationality, String position): void
    removaAdmin(String username): void
    getAdminMap(): Hashmap<String, Admin>
    getAdminByUsername(String username): Admin
    editAdmin(String username, String name, String id, char gender, String nationality, String position): void
    */

    private static HashMap <String, Person> plist = null;

    public PersonList(){
        plist = new HashMap<String, Person>();
        //Student(username, name, matric, gender, nationality)
        Student s1 = new Student("TestStudent1", "Test Student 1", "U1234567890", 'M', "Singaporean", "Comp Sci", 1);
        Student s2 = new Student("TestStudent2", "Test Student 2", "U2345678901", 'F', "Singaporean", "Comp Sci", 2);
        Student s3 = new Student("TestStudent3", "Test Student 3", "U3456789012", 'M', "Singaporean", "Comp Sci", 3);
        Student s4 = new Student("TestStudent4", "Test Student 4", "U4567890123", 'F', "Singaporean", "Comp Sci", 4);
        Student s5 = new Student("TestStudent5", "Test Student 5", "U5678901234", 'M', "Singaporean", "Comp Sci", 1);
        Student s6 = new Student("TestStudent6", "Test Student 6", "U6789012345", 'F', "Singaporean", "Mathematics", 2);
        Student s7 = new Student("TestStudent7", "Test Student 7", "U7890123456", 'M', "Singaporean", "Mathematics", 3);
        Student s8 = new Student("TestStudent8", "Test Student 8", "U8901234567", 'F', "Singaporean", "Mathematics", 4);
        Student s9 = new Student("TestStudent9", "Test Student 9", "U9012345678", 'M', "Singaporean", "Mathematics", 1);
        Student s10 = new Student("TestStudent10", "Test Student 10", "U0123456789", 'F', "Malaysian", "Mathematics", 2);
        Student s11 = new Student("TestStudent11", "Test Student 11", "U0987654321", 'M', "Indian", "Philosophy", 3);
        Student s12 = new Student("TestStudent12", "Test Student 12", "U9876543210", 'F', "Chinese", "Philosophy", 4);
        Student s13 = new Student("TestStudent13", "Test Student 13", "U8765432109", 'M', "Thai", "NIE", 1);
        Student s14 = new Student("TestStudent14", "Test Student 14", "U7654321098", 'F', "Filipino", "NIE", 2);
        Student s15 = new Student("TestStudent15", "Test Student 15", "U6543210987", 'M', "Indonesian", "Business", 3);

        Admin a1 = new Admin("Admin1", "Admin 1", "G1234567890", 'M', "Singaporean", "Professor");
        Admin a2 = new Admin("Admin2", "Admin 2", "G2345678901", 'F', "Singaporean", "Associate Professor");

        plist.put("TestStudent1", s1);
        plist.put("TestStudent2", s2);
        plist.put("TestStudent3", s3);
        plist.put("TestStudent4", s4);
        plist.put("TestStudent5", s5);
        plist.put("TestStudent6", s6);
        plist.put("TestStudent7", s7);
        plist.put("TestStudent8", s8);
        plist.put("TestStudent9", s9);
        plist.put("TestStudent10", s10);
        plist.put("TestStudent11", s11);
        plist.put("TestStudent12", s12);
        plist.put("TestStudent13", s13);
        plist.put("TestStudent14", s14);
        plist.put("TestStudent15", s15);

        plist.put("Admin1", a1);
        plist.put("Admin2", a2);

        //write to ser file

        try{
            FileOutputStream fos =
                new FileOutputStream("PersonList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(plist);
            oos.close();
            fos.close();
        }
        
        catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadPersonList(){
        try
        {
         FileInputStream fis = new FileInputStream("PersonList.ser");
         ObjectInputStream ois = new ObjectInputStream(fis);
         Object temp = ois.readObject();
         if(temp instanceof HashMap<?,?>){
             plist = (HashMap<String, Person>) temp;
        }
         ois.close();
         fis.close();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            new PersonList(); //If file not found, create default values
        }
        catch(ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
            new PersonList();
      }
    }

    public static Person getByUsername(String username) throws UserNotFound{
        if (plist == null){
            loadPersonList();
        }

        if (plist.containsKey(username)){
            return plist.get(username);
        }
        else{
            throw new UserNotFound("User not found!");
        }
    }

    public static HashMap<String, Student> getStudentMap() {
        //load hashmap if plist is null
        if (plist==null){
            PersonList.loadPersonList();
        }

        HashMap <String, Student> temp = new HashMap<String, Student>();
        for (Map.Entry<String, Person> entry : plist.entrySet()){
            if (entry.getValue() instanceof Student){
                temp.put(entry.getKey(), (Student) entry.getValue());
            }
        }
        return temp;
    }

    public static Student getStudentByUsername(String username) throws UserNotFound{
        if (plist == null){
            PersonList.loadPersonList();
        }
        
        Person p = getByUsername(username);
        if (p instanceof Student){
            return (Student) p;
        }
        else{
            throw new UserNotFound("Student not found");
        }
    }

    public static void savePersonMap(){
        try{
            FileOutputStream fos =
                new FileOutputStream("StudentList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(plist);
            oos.close();
            fos.close();
            return;
        }
        
        catch(IOException ioe){
            ioe.printStackTrace();
            return;
        }
    }

    public static void newStudent(String username, String name, String matric, char gender, String nationality, String major, int year) throws UserAlreadyExists{
        if (plist == null){
            PersonList.loadPersonList();
        }

        //Check is username and matric number is already taken!
        if (plist.containsKey(username)){
            throw new UserAlreadyExists ("This Username is taken!");
        }
        else{
            for (Person p : plist.values() ){
                if(p.getMatric()==matric){
                    throw new UserAlreadyExists("Matric number is taken!");
                }
            }
        }

        Student s = new Student(username, name, matric, gender, nationality, major, year);
        plist.put(username, s);
        PersonList.savePersonMap(); //Save map immediately after creating new student
        return;

    }

    public static void removeStudent(String username) throws UserNotFound{
        if (plist == null){
            PersonList.loadPersonList();
        }

        Student s = getStudentByUsername(username);
        plist.remove(username, s); 

        //TODO: Remove student from Course and index
    }

    public static void editStudentDetails(String username, String name, String matric, char gender, String nationality, String major, int year) throws UserNotFound, UserAlreadyExists{
        if (plist == null){
            PersonList.loadPersonList();
        }
        
        PersonList.removeStudent(username); //IRS: If this is removed, will his/her registeredCourseList hashmap be removed as well? 
        PersonList.newStudent(username, name, matric, gender, nationality, major, year);
    }

    public static void newAdmin(String username, String name, String id, char gender, String nationality, String position) throws UserAlreadyExists{
        if (plist == null){
            PersonList.loadPersonList();
        }

        //Check is username and matric number is already taken!
        if (plist.containsKey(username)){
            throw new UserAlreadyExists ("This Username is taken!");
        }
        else{
            for (Person p : plist.values() ){
                if(p.getMatric()==id){
                    throw new UserAlreadyExists("Matric number is taken!");
                }
            }
        }

        Admin ad = new Admin(username, name, id, gender, nationality, position);
        plist.put(username, ad);
        PersonList.savePersonMap();
    }

    public static void removeAdmin(String username) throws UserNotFound{
        if (plist == null){
            PersonList.loadPersonList();
        }

        if (plist.containsKey(username)){
            if (plist.get(username) instanceof Admin){
                plist.remove(username);
            }
            else{
                throw new UserNotFound("Admin not found!");
            }
        }
        else{
            throw new UserNotFound("Admin not found");
        }
    }

    public static HashMap<String, Admin> getAdminMap(){
        if (plist == null){
            PersonList.loadPersonList();
        }

        HashMap<String, Admin> temp = new HashMap<String, Admin>();
        for (Map.Entry<String, Person> entry : plist.entrySet()){
            if(entry.getValue() instanceof Admin){
                temp.put(entry.getKey(), (Admin) entry.getValue());
            }
        }
        return temp;
    }

    public static Admin getAdminByUsername(String username) throws UserNotFound{
        if (plist == null){
            PersonList.loadPersonList();
        }

        if (plist.containsKey(username)){
            if(plist.get(username) instanceof Admin){
                return (Admin) plist.get(username);
            }
            else{
                throw new UserNotFound("Admin not found!");
            }
        }
        else{
            throw new UserNotFound("Admin not found!");
        }
    }

    public static void editAdmin(String username, String name, String id, char gender, String nationality, String position)
            throws UserNotFound, UserAlreadyExists {
        if (plist == null){
            PersonList.loadPersonList();
        }
        PersonList.removeAdmin(username);
        PersonList.newAdmin(username, name, id, gender, nationality, position);
    }

    public static boolean checkusername(String username){
        if (plist == null){
            PersonList.loadPersonList();
        }

        if (plist.containsKey(username)){
            return true;
        }
        else{
            return false;
        }
    }
}