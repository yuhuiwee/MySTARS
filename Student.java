import java.io.Serializable;
import java.util.*;

public class Student extends Person implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private HashMap<String, Integer> registeredCourses;
    private HashMap<String, Integer> waitlistCourses;
    private boolean changeStatus = false;
    private HashMap<Integer, Integer> pendingSwap;
    private HashMap<String, ArrayList<Integer>> successChange;
    private String major;
    private AccessPeriod accessTime;
    private int year;

    public Student(String user, String name, String matric, char gender, String nationality, String major, int year) {
        this.username = user;
        this.name = name;
        this.matricnum = matric;
        this.gender = gender;
        this.nationality = nationality;
        this.major = major;
        this.year = year;
        registeredCourses = new HashMap<String, Integer>();
        waitlistCourses = new HashMap<String, Integer>();
        this.accessTime = new AccessPeriod(major, year);
    }

    public void addCourse(String course, int index) throws CourseDontExist {
        boolean courseadd = CourseList.AddCourse(course, index, username); // CourseList.add returns boolean
        if (courseadd) {
            System.out.println("Course added successfully!");
            registeredCourses.put(course, index);
        } else {
            System.out.println("Course index is full! You will be put on waitlist!");
            waitlistCourses.put(course, index);

        }
    };

    public void dropCourse(String course, int index) throws CourseDontExist, UserNotFound, UserAlreadyExists {
        int verify = verifyCourse(course);
        if (verify == -1) {
            throw new CourseDontExist("You are not registered for this course!");
        } else if (verify != index) {
            throw new CourseDontExist("You are not registered for this course!");
        }
        CourseList.DropCourse(course, index, username);
        if (waitlistCourses.containsKey(course)) {
            waitlistCourses.remove(course);

        } else if (registeredCourses.containsKey(course)) {
            registeredCourses.remove(course);
        }
    };

    public void checkCourse() {

        System.out.println("Registered Courses");
        for (Map.Entry<String, Integer> entry : registeredCourses.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }

        System.out.println("Courses on Waitlist");
        for (Map.Entry<String, Integer> entry : waitlistCourses.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }
    }

    public void checkVacancies() {
        System.out.println("The vacancies for each index are as follows:");
        // TODO: print vacancies of each index
        return;
    }

    public void swopIndex(String course, int selfIndex, int newIndex, String newStudentUsername)
            throws UserNotFound, UserAlreadyExists, CourseDontExist, CourseAlreadyExist {
        int verify = verifyCourse(course);
        if (verify == -1) {
            throw new CourseDontExist("You are not registered for this course!");
        } else if (verify != selfIndex) {
            throw new CourseDontExist("You are not registered for this course!");
        }

        if (pendingSwap.containsKey(selfIndex)) {
            throw new CourseAlreadyExist("You already have a pending swap with this course index!");
        }

        boolean swaped = Swop.swopStudent(course, selfIndex, newIndex, username, newStudentUsername);
        if (swaped) {// if true, swap successful
            registeredCourses.replace(course, selfIndex, newIndex);
            System.out.println("Swap successful!");

        } else {// pending swap
            pendingSwap.put(selfIndex, newIndex);
        }

    }

    public boolean getSwopStatus() {
        // If true call printswoppedlist()
        return changeStatus;
    }

    public void setSwopstatus(int index1, int index2, String course) {
        this.changeStatus = true; // set true when swap with student2 successful
        pendingSwap.remove(index1); // remove from pending
        ArrayList<Integer> s = new ArrayList<Integer>();
        s.add(index1);
        s.add(index2);
        successChange.put(course, s);
        registeredCourses.replace(course, index1, index2); // replace with swopped index

    }

    public void printChanges() {
        // Print list of courses successfully swopped
        if (successChange != null) {
            for (Map.Entry<String, ArrayList<Integer>> entry : successChange.entrySet()) {
                ArrayList<Integer> s = entry.getValue();
                if (s.size() == 2) {
                    System.out.println("Successfullly swapped " + entry.getKey() + " from index " + s.get(0)
                            + " to index " + s.get(1));
                } else if (s.size() == 1) {
                    System.out.println(
                            "You have been registered for " + entry.getKey() + " with course index " + s.get(0));
                }

            }
            successChange = null; // remove swop from hashmap
        }

        this.changeStatus = false; // reset swop status

    }

    public void printStudentDetails() {
        System.out.printf("Name: %s, Gender: %s, Nationality: %s, Year: %d", name, gender, nationality, year);
    }

    public String getMajor() {
        return this.major;
    }

    public AccessPeriod getAccessPeriod() {
        return this.accessTime;
    }

    public void dropAllCourse() throws CourseDontExist, UserNotFound, UserAlreadyExists {
        for (Map.Entry<String, Integer> entry : registeredCourses.entrySet()) {
            dropCourse(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, Integer> entry : waitlistCourses.entrySet()) {
            dropCourse(entry.getKey(), entry.getValue());
        }
        registeredCourses = null;
        waitlistCourses = null;
    }

    public void WaitingToRegistered(String course, int indexnum) {
        // TODO: Add email API HERE!!
        changeStatus = true;

        ArrayList<Integer> s = new ArrayList<Integer>();
        s.add(indexnum);
        successChange.put(course, s);

        waitlistCourses.remove(course);
        registeredCourses.put(course, indexnum);
    }

    private int verifyCourse(String course) {
        if (registeredCourses.containsKey(course)) {
            return registeredCourses.get(course);
        } else if (waitlistCourses.containsKey(course)) {
            return waitlistCourses.get(course);
        } else {
            return -1;
        }
    }

    public void dropSwap(String course, int selfindex, String student2) throws CourseDontExist {
        if (!pendingSwap.containsKey(selfindex)) {// check if index is registered
            throw new CourseDontExist("You dont have any pending swaps with this index!");
        } else if (!CourseList.checkIndex(course, selfindex)) {
            throw new CourseDontExist("This index does not exist for this course!");
        } else {
            boolean b = Swop.dropSwop(username, student2, selfindex);
            if (b) {
                pendingSwap.remove(selfindex);
                System.out.println("Successfully dropped the Course Swop");
                return;
            }
        }
        return;
    }
}
