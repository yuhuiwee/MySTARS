import java.util.*;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Student extends Person{
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    enum electiveType {
        CORE, MPE, GERPE, GERUE, UE,
    }

    private HashMap<String, electiveType> electivemap;
    private HashMap<String, Integer> registeredCourses;
    private HashMap<String, Integer> waitlistCourses;
    private boolean changeStatus = false;
    private HashMap<Integer, Integer> pendingSwap;
    private HashMap<String, ArrayList<Integer>> successChange;
    private AccessPeriod accessTime;
    private int year;
    private Timetable timetable;
    private int totalau;
    private int maxau;

    public Student(String user, String name, String matric, char gender, String nationality, String major, int year,
            String email) throws UserAlreadyExists {
        //public Person(String username, String name, String matric, char gender, String nationality, String school, String email)
        super(user.toUpperCase(), name.toUpperCase(), matric.toUpperCase(), Character.toUpperCase(gender), nationality.toUpperCase(), major.toUpperCase(), email.toUpperCase());
        this.totalau = 0;
        this.year = year;
        registeredCourses = new HashMap<String, Integer>();
        waitlistCourses = new HashMap<String, Integer>();
        this.accessTime = new AccessPeriod(major, year);
        this.timetable = new Timetable();
        electivemap = new HashMap<String, electiveType>();
        maxau = 21; // default
        pendingSwap = new HashMap<Integer, Integer>();
        successChange = new HashMap<String, ArrayList<Integer>>();
        

        PasswordHash.addUserPwd(user.toUpperCase(), user.toUpperCase());
    }

    private electiveType intToEnum(int i) {
        switch (i) {
            case 1:
                return electiveType.CORE;
            case 2:
                return electiveType.MPE;
            case 3:
                return electiveType.GERPE;
            case 4:
                return electiveType.GERUE;
            case 5:
                return electiveType.UE;
            default:
                return electiveType.UE;
        }
    }

    public int getCourseElectiveIndex(String course) {
        electiveType e = electivemap.get(course.toUpperCase());

        switch (e) {
            case CORE:
                return 1;
            case MPE:
                return 2;
            case GERPE:
                return 3;
            case GERUE:
                return 4;
            case UE:
                return 5;
            default:
                return 0;
        }
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void addCourse(String course, int index, int elec) throws CourseDontExist, TimetableClash,
            CloneNotSupportedException, VenueAlreadyExists {
        if (!CourseList.checkIndex(course, index)) {
            throw new CourseDontExist("Error! Course/Index does not match!");
        }
        if (!timetable.checkTimeslot(getIndexTimetable(index))) {
            throw new TimetableClash("Error! You are not allowed to register for courses with timetable clash!");
        }
        electivemap.put(course.toUpperCase(), intToEnum(elec));
        boolean courseadd = CourseList.addCourseByStudent(course.toUpperCase(), index, username); // CourseList.add
                                                                                                  // returns boolean
        if (courseadd) {
            System.out.println("Course added successfully!");
            totalau = totalau + CourseList.getCourse(course.toUpperCase()).getAU();// add au
            registeredCourses.put(course.toUpperCase(), index);
            timetable.mergeTimetable(getIndexTimetable(index));
        } else {
            System.out.println("Course index is full! You will be put on waitlist!");
            waitlistCourses.put(course, index);

        }

        PersonList.savePersonMap();
        CourseList.saveCourseMap();
    };

    public void dropCourse(String course, int index) throws CourseDontExist, UserNotFound, UserAlreadyExists,
            CloneNotSupportedException, TimetableClash, VenueAlreadyExists {
        course = course.toUpperCase();
        int verify = verifyCourse(course);
        if (verify == -1) {
            throw new CourseDontExist("You are not registered for this course!");
        } else if (verify != index) {
            throw new CourseDontExist("You are not registered for this course!");
        }

        electivemap.remove(course);
        CourseList.dropCourseByStudent(course, index, username);
        if (waitlistCourses.containsKey(course)) {
            waitlistCourses.remove(course);

        } else if (registeredCourses.containsKey(course)) {
            registeredCourses.remove(course);
            totalau = totalau - CourseList.getCourse(course).getAU(); // minus au
            if (pendingSwap.containsKey(index)) {
                dropSwap(index);
            }
            timetable.removeTimetable(getIndexTimetable(index));
        }
    };

    public void checkCourse() {

        System.out.println("Registered Courses");
        for (Map.Entry<String, Integer> entry : registeredCourses.entrySet()) {
            System.out.println("Course: " + entry.getKey() + ", Index: " + entry.getValue());
            System.out.println("\tElective type: " + electivemap.get(entry.getKey()).name());
        }

        System.out.println("Courses on Waitlist");
        for (Map.Entry<String, Integer> entry : waitlistCourses.entrySet()) {
            System.out.println("Course: " + entry.getKey() + ", Index: " + entry.getValue());
            System.out.println("\tElective type: " + electivemap.get(entry.getKey()).name());
        }
    }

    public void swopIndex(String course, int selfIndex, int newIndex, String newStudentUsername) throws UserNotFound,
            UserAlreadyExists, CourseDontExist, CourseAlreadyExist, TimetableClash, CloneNotSupportedException,
            VenueAlreadyExists {
        course = course.toUpperCase();
        int verify = verifyCourse(course);
        if (verify == -1) {
            throw new CourseDontExist("You are not registered for this course!");
        } else if (verify != selfIndex) {
            throw new CourseDontExist("You are not registered for this course!");
        }

        if (!swapIndexCheck(selfIndex, newIndex)) {
            throw new TimetableClash("Error! The new course clashes with your Timetable!");
        }

        if (pendingSwap.containsKey(selfIndex)) {
            throw new CourseAlreadyExist("You already have a pending swap with this course index!");
        }

        boolean swaped = Swop.swopStudent(course, selfIndex, newIndex, username, newStudentUsername.toUpperCase());
        if (swaped) {// if true, swap successful
            registeredCourses.replace(course, selfIndex, newIndex);
            System.out.println("Swap successful!");
            timetable.removeTimetable(getIndexTimetable(selfIndex));
            timetable.mergeTimetable(getIndexTimetable(newIndex));

        } else {// pending swap
            pendingSwap.put(selfIndex, newIndex);
        }

    }

    public boolean getSwopStatus() {
        // If true call printswoppedlist()
        return changeStatus;
    }

    public void setSwopstatus(int index1, int index2, String course) throws TimetableClash, CloneNotSupportedException,
            VenueAlreadyExists {
        course = course.toUpperCase();
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
        timetable.removeTimetable(getIndexTimetable(index1));
        timetable.mergeTimetable(getIndexTimetable(index2));

    }

    public void printChanges() {
        // Print list of courses successfully swopped
        if (successChange != null) {
            System.out.println("\n\n");
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
        System.out.printf("Name: %s, Gender: %s, Nationality: %s, Year: %d", name.toUpperCase(), gender,
                nationality.toUpperCase(), year);
    }

    public AccessPeriod getAccessPeriod() {
        return this.accessTime;
    }

    public void dropAllCourse() throws CourseDontExist, UserNotFound, UserAlreadyExists, CloneNotSupportedException,
            TimetableClash, VenueAlreadyExists {
        for (Map.Entry<String, Integer> entry : registeredCourses.entrySet()) {
            dropCourse(entry.getKey(), entry.getValue()); // also remove in pending swop
        }

        for (Map.Entry<String, Integer> entry : waitlistCourses.entrySet()) {
            dropCourse(entry.getKey(), entry.getValue());
        }
        registeredCourses = null;
        waitlistCourses = null;
        timetable = null;
        totalau = 0;
        changeStatus = false;
        successChange = null;
        pendingSwap = null;
    }

    public void WaitingToRegistered(String course, int indexnum) throws TimetableClash, CloneNotSupportedException,
            VenueAlreadyExists {
        String text = "You have been registered for course " + course.toUpperCase() + " with index "
                + String.valueOf(indexnum);
        sendEmail("Wait No More!", text);
        changeStatus = true;

        ArrayList<Integer> s = new ArrayList<Integer>();
        s.add(indexnum);
        successChange.put(course, s);

        waitlistCourses.remove(course);
        registeredCourses.put(course, indexnum);
        totalau = totalau + CourseList.getCourse(course).getAU();
        timetable.mergeTimetable(getIndexTimetable(indexnum));
        PersonList.savePersonMap();
    }

    public int verifyCourse(String course) {
        // check if student is registered for course
        course = course.toUpperCase();
        if (registeredCourses.containsKey(course)) {
            return registeredCourses.get(course);
        } else if (waitlistCourses.containsKey(course)) {
            return waitlistCourses.get(course);
        } else {
            return -1;
        }
    }

    public void dropSwap(int selfindex) throws CourseDontExist {
        if (!pendingSwap.containsKey(selfindex)) {// sshould not be invoked if verified beforehand
            throw new CourseDontExist("You dont have any pending swaps with this index!");
        } else {
            boolean b = Swop.dropSwop(username, selfindex);
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
            message.setText("Dear " + this.name.toUpperCase() + ": \n\n" + text);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void printTimeTableByWeek() {
        timetable.printWeeklySchedule();
    }

    public void printTimetableByCourse() {
        timetable.printSchedule();
    }

    public boolean swapIndexCheck(int oldIndex, int newIndex) throws CloneNotSupportedException, TimetableClash,
            VenueAlreadyExists {
        // to check is timetable clash before swap!
        Timetable temp = (Timetable) timetable.clone();
        temp.removeTimetable(getIndexTimetable(oldIndex));
        if (!temp.checkTimeslot(getIndexTimetable(newIndex))) {
            return false;
        }
        return true;

    }

    public int getAU() {
        return this.totalau;
    }

    public int getMaxAU() {
        return this.maxau;
    }

    public void setMaxAU(int au) {
        this.maxau = au;
    }

    public boolean checkAU(int au) {
        if (maxau == totalau) {
            return false;
        } else if (maxau < totalau + au) {
            return false;
        } else {
            return true;
        }
    }

    public void swapElectiveType(String course, int elec) {
        if (verifyCourse(course.toUpperCase()) == -1) {
            return;// should not be invoked as check is doen in application class
        }
        this.electivemap.replace(course.toUpperCase(), intToEnum(elec));
        return;

    }

    public electiveType getElectiveTypeCourse(String course) {
        return this.electivemap.get(course.toUpperCase());
    }

    private Timetable getIndexTimetable(int index)
            throws TimetableClash, CloneNotSupportedException, VenueAlreadyExists {
        return CourseList.getIndexNum(index).getClassSchedule();
    }

    public Timetable getTimetable() throws CloneNotSupportedException {
        // return clone of timetable
        // Outside methods should not be able to edit student timetable
        Timetable t;
        try {
            t = (Timetable) timetable.clone();
        } catch (CloneNotSupportedException c) {
            c.printStackTrace();
            t = getTimetable();
        }
        return t;
    }

    public boolean checkRegistered(String course){ //only return true if registered
        if (verifyCourse(course)==-1){
            return false;
        }
        if (registeredCourses.containsKey(course)){
            return true;
        }
        else if (waitlistCourses.containsKey(course)){
            return false;
        }else{
            return false;
        }
    }

    public boolean checkWaitList(String course){
        if (verifyCourse(course)==-1){
            return false;
        }
        if (registeredCourses.containsKey(course)){
            return false;
        }
        if (waitlistCourses.containsKey(course)){
            return true;
        }
        return false;
    }


}
