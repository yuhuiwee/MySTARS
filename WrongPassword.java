/**
 * A class containing the error message exception for when the user enters the wrong password
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class WrongPassword extends Exception {

    /**
     * Serialization of the object WrongPassword
     */
    private static final long serialVersionUID = 1L;

    /**
     * Displaying a string message exception for when the user enters the wrong password
     * @param message This is the error exception message
     */
    public WrongPassword(String message) {
        super(message);
    }

}
