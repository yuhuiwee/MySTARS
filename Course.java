import java.io.Serializable;
import java.util.*;

public class Course implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String courseCode;
    private String school;
    // private String userName;
    private HashMap<Integer, IndexNum> mapIndex; // The Integer in this case is the name of the IndexNum. So for
                                                 // example if i want to find for index 1024, i can simply use
                                                 // mapIndex.get(1024) to obtain the object indexNum.

    public Course(String courseCode, String school) {
        this.courseCode = courseCode;
        this.school = school;
        mapIndex = new HashMap<Integer, IndexNum>();
        listOfRegisteredStudents = new ArrayList<String>();
    }

    /* ----------- Get Methods ----------- */

    public String getCourseCode() {
        return courseCode;
    }

    public String getSchool() {
        return school;
    }

    public int[] getIndexNumber() {
        int j = 0;
        int[] k = new int[mapIndex.size()];
        for (Integer i : mapIndex.keySet()) {
            k[j] = i;
            j++;
        }
        return k;
    }

    /* ----------- Set Methods ----------- */

    public void setcourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setIndexNumber(int key, IndexNum value) {
        mapIndex.put(key, value);
    }

    /* ----------- Normal Methods ----------- */

    public boolean addCourse(int index, String userName) // for students to add course
    {
        boolean bool = mapIndex.get(index).addStudent(userName); // mapIndex.get(course) = object
        return bool;
    }

    public void dropCourse(int index, String username, Boolean swopFlag) throws UserNotFound, UserAlreadyExists {
        mapIndex.get(index).removeStudent(courseCode, username, swopFlag);
        // return;
        // listOfRegisteredStudents.remove(username); // then how to account for people
        // added from waitlist?
    }

    public void updateCourseCode(String currentCourseCode, String newCourseCode) {
        // FIXME: Is the handling of updating enough in the course list.
        // TODO: How to find the course that they want to update
    }
    // Since Admin can add/update course, we have to include all the mutator methods
    // of course. But since
    // they can only change these 2 variables, we don't include the rest of the
    // variables.

    /*
     * public int[] getCourseIndexVacancy() { for (Integer i : mapIndex.keySet())
     * return new int[] { i, mapIndex.get(i).getVacancy() }; }
     */

    public int getIndexVacancy(int indexNum) {
        return mapIndex.get(indexNum).getVacancy();
    }

    public void printVacancy(int indexNum) {
        mapIndex.get(indexNum).getVacancy();
    }

    public boolean checkIndex(int index) {
        if (mapIndex.containsKey(index)) {
            return true;
        } else {
            return false;
        }
    }

    public void printCourse() {
        System.out.println("Course Info: " + courseCode);
        System.out.println("School: " + school);
    }

    // public void printStudentListOfCourse() {
    // System.out.println("\nStudent list of Course " + courseCode + ":\n");
    // for (int i = 0; i < mapIndex.size(); i++) {
    // // TO DO: LINK TO STUDENT CLASS TO OBTAIN NAME, GENDER AND NATIONALITY
    // mapIndex.get(i).printStudentListOfIndex();
    // //System.out.println((i + 1) + ") " + listOfRegisteredStudents.get(i));
    // }
    // }

}