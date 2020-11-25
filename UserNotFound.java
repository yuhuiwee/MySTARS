/**
 * A class containing the error message exception for when the user entered is not found within the Person hash map
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class UserNotFound extends Exception {
    /**
     * Serialization of the object UserNotFound
     */
    private static final long serialVersionUID = 1L;

    /**
     * Displaying a string message exception for when the user entered is not found within the Person hash map
     * @param message This is the error exception message
     */
    public UserNotFound(String message) {
        super(message);
    }
    
}
