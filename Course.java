import java.io.Serializable;
import java.util.*;

public class Course implements Serializable {

    /**
     *
     */

    // private static ArrayList<String> schoolType;
    private static final long serialVersionUID = 1L;
    private String courseCode;
    private String school;
    private int au;
    // private String userName;
    private HashMap<Integer, IndexNum> mapIndex; // The Integer in this case is the name of the IndexNum. So for
                                                 // example if i want to find for index 1024, i can simply use
                                                 // mapIndex.get(1024) to obtain the object indexNum.

    public Course(String courseCode, String school) {
        this.courseCode = courseCode.toUpperCase();
        this.school = school.toUpperCase();
        mapIndex = new HashMap<Integer, IndexNum>();
        au = 3;
    }

    /*
     * public static void createSchoolType() { schoolType = new ArrayList<String>();
     * schoolType.add("Computer Science"); schoolType.add("Engineering");
     * schoolType.add("Philodophy"); }
     * 
     * public static void printSchoolType() { for (String i : schoolType) {
     * System.out.println(i); } }
     * 
     * public static boolean checkSchoolExistence(String school) { if
     * (schoolType.contains(school)) return true; return false; }
     */
    /* ----------- Get Methods ----------- */

    public String getCourseCode() {
        return courseCode;
    }

    public String getSchool() {
        return school;
    }

    public ArrayList<Integer> getIndexNumber() {
        ArrayList<Integer> listOfIndex = new ArrayList<Integer>(mapIndex.keySet());
        return listOfIndex;
    }

    public int getAU() {
        return au;
    }

    /* ----------- Set Methods ----------- */
    public void setAU(int newau) {
        this.au = newau;
    }

    public void setcourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setIndexNumber(HashMap<Integer, IndexNum> indexMap) {
        mapIndex.putAll(indexMap);
    }

    /* ----------- Normal Methods ----------- */

    public boolean addCourse(IndexNum ind, String userName) // for students to add course
    {
        boolean bool = ind.addStudent(userName); // mapIndex.get(course) = object
        return bool;
    }

    public void dropCourse(int index, String username, Boolean swopFlag)
            throws UserNotFound, UserAlreadyExists, CloneNotSupportedException, TimetableClash, VenueAlreadyExists {
        mapIndex.get(index).removeStudent(courseCode, username, swopFlag);
        return;
    }
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

    public void rse() {
        System.out.println("Course Info: " + courseCode);
        System.out.println("School: " + school);
    }

    public void printStudentList() throws UserNotFound, UserAlreadyExists {
        System.out.println("Course: " + courseCode);
        for (Map.Entry<Integer, IndexNum> entry : mapIndex.entrySet()) {
            entry.getValue().printStudentList();
        }
    }

    public void printAllIndexVacancies() {
        for (Map.Entry<Integer, IndexNum> entry : mapIndex.entrySet()) {
            entry.getValue().printIndex();
        }
    }

    public void removeCourse() throws UserNotFound, UserAlreadyExists, CourseDontExist, CloneNotSupportedException,
            TimetableClash, VenueAlreadyExists {
        for (Map.Entry<Integer, IndexNum> entry: mapIndex.entrySet()){
            IndexNum ind = entry.getValue();
            ind.deleteIndexNum(courseCode);
        }
        mapIndex = null;
        return;
    }
    public void printAllIndex(){
        System.out.println("Course: "+courseCode);
        ArrayList<Integer> a = getIndexNumber();
        ListIterator<Integer> i = a.listIterator();
        while (i.hasNext()){
            System.out.println("\t"+String.valueOf(i.next()));
        }
    }

}