/**
 * A class containing the error message exception for when the access period of the user is not found
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class AccessPeriodNotFound extends Exception {
    /**
     * Serialization of the object AccessPeriodNotFound
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * Displaying a string message exception for when the access period is not found
     * @param message This is the error message when access period is not found
     */
    public AccessPeriodNotFound(String message){
        super(message);
    }
}
