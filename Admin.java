import java.io.Serializable;

public class Admin extends Person implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String position;

    public Admin(String username, String name, String id, char gender, String nationality, String school,
            String position, String email) {
        this.username = username;
        this.name = name;
        this.matricnum = id;
        this.gender = gender;
        this.nationality = nationality;
        this.school = school;
        this.position = position;
        this.email = email;
    }

    // When creating a new course, the admin will have to then create a new
    // indexNumber as well as exisiting
    // indexNumber objects cannot be mapped to the new course or it will result in a
    // clash in timing

    // A course might have multiple index number
    // In the Admin UI, probably when admin Creates new Course, admin is given a
    // choice to create k amount
    // of index Numbers. So this method will iterate based on the k value itself

    // TODO: The method above obtains the Student name, so we have to get details of
    // the student's gender and nationality by using
    // some Student method. Also perhaps we might have to store the student ID
    // instead in the Course class since ID is unique
    // but not student name.
}
