import java.io.Serializable;
import java.util.*;

public class IndexNum implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int vacancy;
    private int indexNumber;
    private ArrayList<ClassSchedule> classSchedule;
    private ArrayList<String> listOfRegisteredStudents;
    private ArrayList<String> waitingList;

    public IndexNum(int indexNumber, int vacancy) {
        this.indexNumber = indexNumber;
        this.vacancy = vacancy;
        classSchedule = new ArrayList<ClassSchedule>();
        listOfRegisteredStudents = new ArrayList<String>(); // So when index 1024 created, it also creates
        waitingList = new ArrayList<String>(); // these 2 lists.
    }

    /* ----------- Get Methods ----------- */

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
    }

    public void setIndexNumber(int indexNumber) {
        this.indexNumber = indexNumber;
    }

    /* ----------- Normal Methods ----------- */

    public boolean addStudent(String username) {
        // So there will be list of students available in application
        // But this will be initiated by the student class
        if (vacancy > 0) {
            listOfRegisteredStudents.add(username);
            vacancy--;
            System.out.println("You have been added to the index." + this.indexNumber + ".");
            return true;
        } else {
            waitingList.add(username);
            System.out.println("There are no vacancy left in this index. You have been placed in the waitlist.");
            return false;
        }
    }

    // TODO: Edited method input variables... please change in class diag
    public void removeStudent(String coursecode, String username, boolean swopFlag)
            throws UserNotFound, UserAlreadyExists {
        // flag is for the swop to happen without adding in the students in the waiting
        // list

        if (listOfRegisteredStudents.contains(username)) {
            listOfRegisteredStudents.remove(username);
        } else {
            waitingList.remove(username);
        }

        System.out.println("You have been removed from the index.");
        if (swopFlag || waitingList.isEmpty()) // Swop flag = True -> Just Increase vacancy without adding Students from
                                               // waitlist
        {
            vacancy++;
            return;

        } else {// swopflag is false
            listOfRegisteredStudents.add(waitingList.get(0));
            Student st = (Student) PersonList.getByUsername(waitingList.get(0));
            st.WaitingToRegistered(coursecode, indexNumber); // inform student of swop
            waitingList.remove(0);
            return;

        }
    }

    public void addSchedule(ClassSchedule sch) {
        classSchedule.add(sch); // Schedule object not created here as we might need a list which keeps records
                                // of all schedule
        // to prevent any clash in terms of timing or venue.
    }

    // public void printStudentListOfIndex() {
    // if (listOfRegisteredStudents.size() == 0) {
    // System.out.println("<Empty>\n");
    // }
    // for (int i = 0; i < listOfRegisteredStudents.size(); i++) {
    // System.out.println((i + 1) + ") " + listOfRegisteredStudents.get(i));
    // }
    // }

    public void printIndex() {
        System.out.println("Index Number Info: " + indexNumber);
        System.out.println("Vacancy: " + vacancy);
        for (int i = 0; i < classSchedule.size(); i++) {
            classSchedule.get(i).printSchedule();
        }
    }
}
