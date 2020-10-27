public class CourseDontExist extends Exception {
    private static final long serialVersionUID = 1L;

    public CourseDontExist(String message) {
        super(message);
    }
}