/**
 * A class containing the error message exception for when the user entered a course does not exist in the database / hash map
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class CourseDontExist extends Exception {
	/**
	 * Serialization of the object CourseDontExist
	 */
    private static final long serialVersionUID = 1L;

    /**
     * Displaying a string message exception for when the user entered a course that does not exist in the database / hash map
     * @param message This is the error exception message
     */
    public CourseDontExist(String message) {
        super(message);
    }
}