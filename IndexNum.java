import java.io.Serializable;
import java.util.*;

public class IndexNum implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int vacancy;
    private int indexNumber;
    private Timetable classSchedule;
    private ArrayList<String> listOfRegisteredStudents;
    private ArrayList<String> waitingList;

    public IndexNum(int indexNumber, int vacancy) {
        this.indexNumber = indexNumber;
        this.vacancy = vacancy;
        this.classSchedule = new Timetable();
        listOfRegisteredStudents = new ArrayList<String>(); // So when index 1024 created, it also creates
        waitingList = new ArrayList<String>(); // these 2 lists.
    }

    /* ----------- Get Methods ----------- */
    public Timetable getClassSchedule() {
        return classSchedule;
    }

    public int getIndexNumber() {
        return indexNumber;
    }

    public int getVacancy() {
        return vacancy;
    }

    public ArrayList<String> getRegisteredStudentList() {
        return listOfRegisteredStudents;
    }

    /* ----------- Set Methods ----------- */

    public void setVacancy(int vacancy) {
        this.vacancy = vacancy;
        CourseList.saveCourseMap();
    }

    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
        CourseList.saveCourseMap();
    }

    /* ----------- Normal Methods ----------- */

    public boolean addStudent(String username) {
        // So there will be list of students available in application
        // But this will be initiated by the student class
        int temp = this.vacancy;
        if (temp > 0) {
            listOfRegisteredStudents.add(username);
            this.vacancy=temp-1;
            System.out.println("You have been added to the index." + this.indexNumber + ".");
            CourseList.saveCourseMap();
            return true;
        } else {
            waitingList.add(username);
            System.out.println("There are no vacancy left in this index. You have been placed in the waitlist.");
            return false;
        }
    }

    public void removeStudent(String coursecode, String username, boolean swopFlag)
            throws UserNotFound, UserAlreadyExists, CloneNotSupportedException, TimetableClash, VenueAlreadyExists {
        // flag is for the swop to happen without adding in the students in the waiting
        // list

        if (listOfRegisteredStudents.contains(username)) {
            listOfRegisteredStudents.remove(username);
        } else {
            waitingList.remove(username);
        }

        System.out.println("You have been removed from the index.");
        // Swop flag = True -> Just Increase vacancy without adding Students from
        // waitlist
        if (swopFlag || waitingList.isEmpty()) {
            setVacancy(vacancy+1);
            return;

        } else {// swopflag is false
            int i = 0;
            String user = waitingList.get(i);
            Student st = (Student) PersonList.getByUsername(user);
            while (!classSchedule.checkTimeslot(st.getTimetable())) {
                // iterate through waitlist to find 1st student that does not have timetable
                // clash
                i++;
                user = waitingList.get(i);
                st = (Student) PersonList.getByUsername(user);
            }
            listOfRegisteredStudents.add(user);
            st.WaitingToRegistered(coursecode, indexNumber); // inform student of swop
            waitingList.remove(0);
            return;

        }
    }

    public void addClassSchedule(Timetable t) throws TimetableClash {
        classSchedule.mergeTimetable(t);
    }

    public void printIndex() {
        System.out.println("Index Number Info: " + indexNumber);
        System.out.println("Vacancy: " + this.vacancy);
    }

    public void printWeeklySchedule() {
        classSchedule.printWeeklySchedule();
    }

    public void printCourseSchedule() {
        classSchedule.printSchedule();
    }

    // ****** Method to print student by index ******
    public void printStudentList() throws UserNotFound, UserAlreadyExists {
        System.out.println("\nStudent list of Course index number " + String.valueOf(indexNumber) + ":\n");
        ListIterator<String> i = listOfRegisteredStudents.listIterator();
        int n = 1;
        while (i.hasNext()) {
            String name = i.next();
            Student s = (Student) PersonList.getByUsername(name);
            System.out.println(
                    "\t" + String.valueOf(n++) + ") " + name + String.valueOf(s.getGender()) + s.getNationality());
        }
    }

    public void deleteIndexNum(String courseCode) throws UserNotFound, UserAlreadyExists, CourseDontExist,
            CloneNotSupportedException, TimetableClash, VenueAlreadyExists {
        if (!listOfRegisteredStudents.isEmpty()){
            ListIterator<String> i = listOfRegisteredStudents.listIterator();

            //remove registeration for all students
            while (i.hasNext()){ 
                String name = i.next();
                Student s = (Student) PersonList.getByUsername(name);
                s.dropCourse(courseCode, indexNumber);
                String text = "Dear "+name+"\nThis email is to inform you that Course: " + courseCode +", Index"+indexNumber+ " has been deleted by admin. You have been de-registered from this course.";
                s.sendEmail("Course has been removed", text);
            }
            
        }

        //remove waiting list
        if (!waitingList.isEmpty()){
            ListIterator<String> i = waitingList.listIterator();

            //remove registeration for all students
            while (i.hasNext()){ 
                String name = i.next();
                Student s = (Student) PersonList.getByUsername(name);
                s.dropCourse(courseCode, indexNumber);
                String text = "Dear "+name+"\nThis email is to inform you that Course: " + courseCode +", Index"+indexNumber+ " has been deleted by admin. You have been removed from the waiting list of this course.";
                s.sendEmail("Course has been removed", text);
            }
        }

        VenueList.removetimetable(classSchedule);
        classSchedule = null;
        listOfRegisteredStudents = null;
        waitingList = null;
        return;
    }

    public ArrayList<String> getWaitList(){
        return waitingList;
    }
}
