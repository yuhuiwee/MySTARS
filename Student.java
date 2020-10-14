import java.util.*;

public class Student extends Person {
    private HashMap <String, Integer> registered_courses;
    private HashMap <String, Integer> waitlist_courses;

    public Student(String user, String name, String matric, char gender, String nationality){
        this.username = user;
        this.studentname = name;
        this.matricnum = matric;
        this.gender = gender;
        this.nationality = nationality;
        registered_courses = new HashMap<String, Integer>();
        waitlist_courses = new HashMap<String, Integer>();
    }

    public void add_course(String course, int index){
        boolean courseadd = CourseList.addCourse(course, index, username);  //CourseList.add returns boolean
        if (courseadd){
            System.out.println("Course added successfully!");
            registered_courses.put(course, index);
        }
        else{
            System.out.println("Course index is full! You will be put on waitlist!");
            waitlist_courses.put(course, index);

        }
    };
    public void drop_course(String course, double index){
		boolean coursedrop = CourseList.dropCourse(course, index, username); //CourseList.drop returns boolean
        
        if(coursedrop){
            waitlist_courses.remove(course, index);
            registered_courses.remove(course, index);
            System.out.println("Course removed successfully!");
        }
        else {
            System.out.println("Error! You are not registered for this course.");
        }
    };
    
    public void check_course(){

        System.out.println("Registered Courses");
        for (Map.Entry<String, Integer> entry: registered_courses.entrySet()){
            System.out.println(entry.getKey()+","+ entry.getValue());
        }

        System.out.println("Courses on Waitlist");
        for (Map.Entry<String, Integer> entry: waitlist_courses.entrySet()){
            System.out.println(entry.getKey() + "," + entry.getValue());
        }
    }
    public void check_vacancies(){
        System.out.println("The vacancies for each index are as follows:");
        //print vacancies of each index
    };
    public void swop_index(String cor, int newindex, String newstudentusername){
        int selfindex = registered_courses.get(cor);
        boolean swaped = Swop.swopStudent(cor, selfindex, newindex, username, newstudentusername);

    };

}
