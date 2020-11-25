/**
 * A class containing the error message exception for when the user entered a course code that is not in the database / hash map
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class CourseCodeDontExist extends Exception {
    /**
     * Serialization of the object CourseCodeDontExist
     */
	private static final long serialVersionUID = 1L;

	/**
	 * Displaying a string message exception for when the user entered a course code that is not in the database / hash map
	 * @param message This is the error exception message
	 */
    public CourseCodeDontExist(String message) {
        super(message);
    }
}