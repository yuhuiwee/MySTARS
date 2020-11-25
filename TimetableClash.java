/**
 * A class containing the error message exception for when there is a clash within the timetable
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class TimetableClash extends Exception {

    /**
     * Serialization of the object TimetableClash
     */
    private static final long serialVersionUID = 1L;

    /**
     * Displaying a string message exception for when there is a clash within the timetable
     * @param message This is the error exception message
     */
    public TimetableClash(String message) {
        super(message);
    }

}
