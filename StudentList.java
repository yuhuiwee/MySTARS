import java.io.*;
import java.util.*;

public class StudentList {

    private static HashMap <String, Person> stulist;

    public StudentList(){
        stulist = new HashMap<String, Person>();
        Student s1 = new Student("TestStudent1", "Test Student 1", "U123456", 'M', "Singaporean");
        Student s2 = new Student("TestStudent2", "Test Student 2", "U234567", 'F', "Singaporean");
        Student s3 = new Student("TestStudent3", "Test Student 3", "U345678", 'M', "Singaporean");
        Student s4 = new Student("TestStudent4", "Test Student 4", "U456789", 'F', "Singaporean");
        //TODO: to add admin here in same list

        stulist.put("TestStudent1", s1);
        stulist.put("TestStudent2", s2);
        stulist.put("TestStudent3", s3);
        stulist.put("TestStudent4", s4);

        try{
            FileOutputStream fos =
                new FileOutputStream("StudentList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(stulist);
            oos.close();
            fos.close();
        }
        
        catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    public static void loadStudentList(){
        HashMap<String, Person> stulist = null;
        try
        {
         FileInputStream fis = new FileInputStream("StudentList.ser");
         ObjectInputStream ois = new ObjectInputStream(fis);
         stulist = (HashMap) ois.readObject(); //Ignore warning here. There is no fix for this as we are typestting object to hashmap
         ois.close();
         fis.close();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            new StudentList(); //If file not found, create default values
        }
        catch(ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
            new StudentList();
      }
    }

    public static HashMap<String, Student> getStudentmap() {
        return stulist;
    }

    public static Student getStudentbyusername(String username){
        return stulist.get(username);
    }

    public static void saveStudentmap(){
        try{
            FileOutputStream fos =
                new FileOutputStream("StudentList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(stulist);
            oos.close();
            fos.close();
            return;
        }
        
        catch(IOException ioe){
            ioe.printStackTrace();
            return;
        }
    }

    public static void newStudent(String username, String name, String matric, char gender, String nationality){
        Student s = new Student(username, name, matric, gender, nationality);
        stulist.put(username, s);
        return;

    }

    public static void removeStudent(String username){
        if (stulist.containsKey(username)){
            stulist.remove(username);
            return;
        }
        else{
            System.out.println("Student not found");
            return;
        }
    }

    public static void editStudentDetails(String username, String name, String matric, char gender, String nationality){
        if (stulist.containsKey(username)){
            stulist.remove(username);
            Student edits = new Student(username, name, matric, gender, nationality);
            stulist.put(username, edits);
            return;
        }
        else{
            System.out.printf("Student with username: %s not found", username);
            return;
        }
    }
}