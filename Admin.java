import java.io.Serializable;

public class Admin extends Person implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String position;

    public Admin(String username, String name, String id, char gender, String nationality, String position) {
        this.username = username;
        this.name = name;
        this.matricnum = id;
        this.gender = gender;
        this.nationality = nationality;
        this.position = position;
    }

    public void addStudent(String username, String name, String matric, char gender, String nationality, String major,
            int year) throws UserAlreadyExists {
        PersonList.newStudent(username, name, matric, gender, nationality, major, year);
        return;
    }

    public void removeStudent(String username) throws UserNotFound, UserAlreadyExists {
        PersonList.removeStudent(username);
    }

    // When creating a new course, the admin will have to then create a new
    // indexNumber as well as exisiting
    // indexNumber objects cannot be mapped to the new course or it will result in a
    // clash in timing
    public void addNewCourse(String courseCode, String school, String courseType) {
        CourseList.NewCourse(courseCode, school, courseType);
    }

    // A course might have multiple index number
    // In the Admin UI, probably when admin Creates new Course, admin is given a
    // choice to create k amount
    // of index Numbers. So this method will iterate based on the k value itself
    public void addNewIndexNum(String courseCode, int indexNumber, String day, String timeSlot, String venue,
            int vacancy) {
        CourseList.NewIndexNumber(courseCode, indexNumber, day, timeSlot, venue, vacancy);
    }

    public void updateCourse(String courseCode, String school, int indexNumber, int vacancy) {
        // TO DO
    }

    public void checkVacancies(int index) {
        CourseList.checkVacancies(index);
    }

    public void printByIndex(int index) {
        CourseList.printStudentByIndex(index);
    }

    public void printByCourse(String coursecode) {
        CourseList.printStudentByCourse(coursecode);
    }

    public String getMatric() {
        return matricnum;
    }
}
