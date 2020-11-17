import java.io.Serializable;
import java.util.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Student extends Person implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private HashMap<String, Integer> registeredCourses;
    private HashMap<String, Integer> waitlistCourses;
    private boolean changeStatus = false;
    private HashMap<Integer, Integer> pendingSwap;
    private HashMap<String, ArrayList<Integer>> successChange;
    private String major;
    private AccessPeriod accessTime;
    private int year;
    private String email;
    private HashMap<Integer, String> timetable;

    public Student(String user, String name, String matric, char gender, String nationality, String major, int year,
            String email) {
        this.username = user;
        this.name = name;
        this.matricnum = matric;
        this.gender = gender;
        this.nationality = nationality;
        this.major = major;
        this.year = year;
        this.email = email;
        registeredCourses = new HashMap<String, Integer>();
        waitlistCourses = new HashMap<String, Integer>();
        this.accessTime = new AccessPeriod(major, year);
        timetable = new HashMap<Integer, String>;
    }

    public void addCourse(String course, int index) throws CourseDontExist {
        boolean courseadd = CourseList.addCourseByStudent(course, index, username); // CourseList.add returns boolean
        if (courseadd) {
            System.out.println("Course added successfully!");
            registeredCourses.put(course, index);
        } else {
            System.out.println("Course index is full! You will be put on waitlist!");
            waitlistCourses.put(course, index);

        }
    };

    public void dropCourse(String course, int index) throws CourseDontExist, UserNotFound, UserAlreadyExists {
        int verify = verifyCourse(course);
        if (verify == -1) {
            throw new CourseDontExist("You are not registered for this course!");
        } else if (verify != index) {
            throw new CourseDontExist("You are not registered for this course!");
        }
        CourseList.dropCourseByStudent(course, index, username);
        if (waitlistCourses.containsKey(course)) {
            waitlistCourses.remove(course);

        } else if (registeredCourses.containsKey(course)) {
            registeredCourses.remove(course);
        }
    };

    public void checkCourse() {

        System.out.println("Registered Courses");
        for (Map.Entry<String, Integer> entry : registeredCourses.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }

        System.out.println("Courses on Waitlist");
        for (Map.Entry<String, Integer> entry : waitlistCourses.entrySet()) {
            System.out.println(entry.getKey() + "," + entry.getValue());
        }
    }

    public void checkVacancies() {
        System.out.println("The vacancies for each index are as follows:");
        // TODO: print vacancies of each index
        return;
    }

    public void swopIndex(String course, int selfIndex, int newIndex, String newStudentUsername)
            throws UserNotFound, UserAlreadyExists, CourseDontExist, CourseAlreadyExist {
        int verify = verifyCourse(course);
        if (verify == -1) {
            throw new CourseDontExist("You are not registered for this course!");
        } else if (verify != selfIndex) {
            throw new CourseDontExist("You are not registered for this course!");
        }

        if (pendingSwap.containsKey(selfIndex)) {
            throw new CourseAlreadyExist("You already have a pending swap with this course index!");
        }

        boolean swaped = Swop.swopStudent(course, selfIndex, newIndex, username, newStudentUsername);
        if (swaped) {// if true, swap successful
            registeredCourses.replace(course, selfIndex, newIndex);
            System.out.println("Swap successful!");

        } else {// pending swap
            pendingSwap.put(selfIndex, newIndex);
        }

    }

    public boolean getSwopStatus() {
        // If true call printswoppedlist()
        return changeStatus;
    }

    public void setSwopstatus(int index1, int index2, String course) {
        String text = "Successfully swopped course " + course + " from index " + String.valueOf(index1) + " to index "
                + String.valueOf(index2);
        sendEmail("NTU STARS swop", text);
        this.changeStatus = true; // set true when swap with student2 successful
        pendingSwap.remove(index1); // remove from pending
        ArrayList<Integer> s = new ArrayList<Integer>();
        s.add(index1);
        s.add(index2);
        successChange.put(course, s);
        registeredCourses.replace(course, index1, index2); // replace with swopped index

    }

    public void printChanges() {
        // Print list of courses successfully swopped
        if (successChange != null) {
            for (Map.Entry<String, ArrayList<Integer>> entry : successChange.entrySet()) {
                ArrayList<Integer> s = entry.getValue();
                if (s.size() == 2) {
                    System.out.println("Successfullly swapped " + entry.getKey() + " from index " + s.get(0)
                            + " to index " + s.get(1));
                } else if (s.size() == 1) {
                    System.out.println(
                            "You have been registered for " + entry.getKey() + " with course index " + s.get(0));
                }

            }
            successChange = null; // remove swop from hashmap
        }

        this.changeStatus = false; // reset swop status

    }

    public void printStudentDetails() {
        System.out.printf("Name: %s, Gender: %s, Nationality: %s, Year: %d", name, gender, nationality, year);
    }

    public String getMajor() {
        return this.major;
    }

    public AccessPeriod getAccessPeriod() {
        return this.accessTime;
    }

    public void dropAllCourse() throws CourseDontExist, UserNotFound, UserAlreadyExists {
        for (Map.Entry<String, Integer> entry : registeredCourses.entrySet()) {
            dropCourse(entry.getKey(), entry.getValue());
        }

        for (Map.Entry<String, Integer> entry : waitlistCourses.entrySet()) {
            dropCourse(entry.getKey(), entry.getValue());
        }
        registeredCourses = null;
        waitlistCourses = null;
    }

    public void WaitingToRegistered(String course, int indexnum) {
        String text = "You have been registered for course " + course + " with index " + String.valueOf(indexnum);
        sendEmail("Wait No More!", text);
        changeStatus = true;

        ArrayList<Integer> s = new ArrayList<Integer>();
        s.add(indexnum);
        successChange.put(course, s);

        waitlistCourses.remove(course);
        registeredCourses.put(course, indexnum);
    }

    private int verifyCourse(String course) {
        // check if student is registered for course
        if (registeredCourses.containsKey(course)) {
            return registeredCourses.get(course);
        } else if (waitlistCourses.containsKey(course)) {
            return waitlistCourses.get(course);
        } else {
            return -1;
        }
    }

    public void dropSwap(String course, int selfindex, String student2) throws CourseDontExist {
        if (!pendingSwap.containsKey(selfindex)) {// check if index is registered
            throw new CourseDontExist("You dont have any pending swaps with this index!");
        } else if (!CourseList.checkIndex(course, selfindex)) {
            throw new CourseDontExist("This index does not exist for this course!");
        } else {
            boolean b = Swop.dropSwop(username, student2, selfindex);
            if (b) {
                pendingSwap.remove(selfindex);
                System.out.println("Successfully dropped the Course Swop");
                return;
            }
        }
        return;
    }

    public void sendEmail(String subject, String text) {

        final String username = "cz2002ss1@gmail.com"; // to be added
        final String password = "CZ2002ss1!"; // to be added

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(this.email)); // to be
                                                                                                // added an
                                                                                                // email addr
            message.setSubject(subject);
            message.setText("Dear" + this.name + "\n\n" + text);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyTimeslot(ArrayList<Integer> timeslots, String course) { // TODO:

    }
}
