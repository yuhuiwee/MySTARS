/**
 * A class containing the error message exception for when the user entered a course that already exist in the database
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class CourseAlreadyExist extends Exception {
	/**
	 * Serialization of the object CourseAlreadyExist
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Displaying a string message exception for when the user entered a course that already exist
     * @param message This is the error message of when user entered a course that already exist
     */
    public CourseAlreadyExist(String message) {
        super(message);
    }
}