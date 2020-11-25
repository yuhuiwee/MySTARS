/**
 * A class containing the error message exception for when the user entered a user that already exist in MySTARS
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class UserAlreadyExists extends Exception{

    /**
     * Serialization of the object UserAlreadyExists
     */
    private static final long serialVersionUID = 1L;

    /**
     * Displaying a string message exception for when the user entered a user that already exist in MySTARS
     * @param message This is the error exception message
     */
    public UserAlreadyExists(String message) {
        super(message);
    }
    
}
