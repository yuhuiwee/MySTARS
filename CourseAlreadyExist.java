public class CourseAlreadyExist extends Exception {
    private static final long serialVersionUID = 1L;

    public CourseAlreadyExist(String message) {
        super(message);
    }
}