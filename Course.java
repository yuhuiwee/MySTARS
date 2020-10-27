import java.io.Serializable;
import java.util.*;

public class Course implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String courseCode;
    private String school;
    private String courseType; // lect/ lect + tuts / lect + tuts + lab
    // private String userName;
    private HashMap<Integer, IndexNum> mapIndex; // The Integer in this case is the name of the IndexNum. So for
                                                 // example if i want to find for index 1024, i can simply use
                                                 // mapIndex.get(1024) to obtain the object indexNum.
    private ArrayList<String> listOfRegisteredStudents;

    public Course(String courseCode, String school, String courseType) {
        this.courseCode = courseCode;
        this.school = school;
        this.courseType = courseType;
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

    public String getCourseType() {
        return courseType;
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

    /*
     * public Integer[] getListOfIndexKey(){ return mapIndex.keySet().toArray(); }
     * 
     * public HashMap<Integer, IndexNum> getIndexMap(int key) { // returning the
     * list of index object return mapIndex; }
     */

    /* ----------- Set Methods ----------- */

    public void setcourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setIndexNumber(Integer key, IndexNum value) {
        mapIndex.put(key, value);
    }

    public void updateCourseCode(String currentCourseCode, String newCourseCode) {
        // FIXME: Is the handling of updating enough in the course list.
        // TODO: How to find the course that they want to update
    }
    // Since Admin can add/update course, we have to include all the mutator methods
    // of course. But since
    // they can only change these 2 variables, we don't include the rest of the
    // variables.

    /* ----------- Normal Methods ----------- */

    public boolean addCourse(int index, String userName) // for students to add course
    {
        mapIndex.get(index).addStudent(userName); // mapIndex.get(course) = object
        listOfRegisteredStudents.add(userName);
        return true;
    }

    public boolean dropCourse(int index, String userName, Boolean swopFlag) {
        mapIndex.get(index).removeStudent(userName, swopFlag);
        return true;
    }

    /*
     * public int[] getCourseIndexVacancy() { for (Integer i : mapIndex.keySet())
     * return new int[] { i, mapIndex.get(i).getVacancy() }; }
     */

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

    public void printStudentListOfCourse() {
        System.out.println("\nStudent list of Course " + courseCode + ":\n");
        for (int i = 0; i < listOfRegisteredStudents.size(); i++) {
            // TO DO: LINK TO STUDENT CLASS TO OBTAIN NAME, GENDER AND NATIONALITY
            System.out.println((i + 1) + ") " + listOfRegisteredStudents.get(i));
        }
    }
}