/**
 * A class containing the error message exception for when the user enter a venue that already exist in the hash map
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class VenueAlreadyExists extends Exception {

    /**
     * Serialization of the object VenueAlreadyExists
     */
    private static final long serialVersionUID = 1L;

    /**
     * Displaying a string message exception for when the user enter a venue that already exist in the hash map
     * @param message This is the error exception message
     */
    public VenueAlreadyExists(String message) {
        super(message);
    }

}
