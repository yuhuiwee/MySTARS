import java.io.Serializable;
import java.time.DayOfWeek;
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
    private String email;
    private TreeMap<Integer, String> timetable;
    private int totalau;

    public Student(String user, String name, String matric, char gender, String nationality, String major, int year,
            String email) {
        this.username = user;
        this.name = name;
        this.matricnum = matric;
        this.gender = gender;
        this.nationality = nationality;
        this.school = major;
        this.year = year;
        this.email = email;
        this.totalau = 0;
        registeredCourses = new HashMap<String, Integer>();
        waitlistCourses = new HashMap<String, Integer>();
        this.accessTime = new AccessPeriod(major, year);
        timetable = new TreeMap<Integer, String>();
        electivemap = null;
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
        electiveType e = electivemap.get(course);

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

    public void addTimeSlot(ArrayList<Integer> timeslots, String course) {
        timeslots.forEach((lambda) -> {
            timetable.put(lambda, course);
        });
    }

    private void dropTimeSlot(ArrayList<Integer> timeslots, String course) {
        if (timetable == null | timetable.isEmpty()) {
            return;
        }
        ListIterator<Integer> i = timeslots.listIterator();
        while (i.hasNext()) {
            timetable.remove(i.next(), course);
        }
    }

    public void addCourse(String course, int index, int elec) throws CourseDontExist, TimetableClash {
        ArrayList<Integer> timeslots = CourseList.getIndexNum(index).getTimeSlots();
        if (!verifyTimeslot(timeslots)) { // safety net. Should not be invoked if checks are done properly
            throw new TimetableClash(
                    "You are not allowed to have classes with time clash. Please try again with another Course/Index!");
        }
        electivemap.put(course, intToEnum(elec));
        boolean courseadd = CourseList.addCourseByStudent(course, index, username); // CourseList.add returns boolean
        if (courseadd) {
            System.out.println("Course added successfully!");
            totalau = totalau + CourseList.getCourse(course).getAU();// add au
            registeredCourses.put(course, index);
            addTimeSlot(timeslots, course);
        } else {
            System.out.println("Course index is full! You will be put on waitlist!");
            waitlistCourses.put(course, index);

        }
    };

    public int getYear() {
        return year;
    }

    public void dropCourse(String course, int index) throws CourseDontExist, UserNotFound, UserAlreadyExists {
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

            ArrayList<Integer> timeslots = CourseList.getIndexNum(index).getTimeSlots();
            dropTimeSlot(timeslots, course);
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

    public AccessPeriod getAccessPeriod() {
        return this.accessTime;
    }

    public void dropAllCourse() throws CourseDontExist, UserNotFound, UserAlreadyExists {
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

    public void WaitingToRegistered(String course, int indexnum) {
        String text = "You have been registered for course " + course + " with index " + String.valueOf(indexnum);
        sendEmail("Wait No More!", text);
        changeStatus = true;

        ArrayList<Integer> s = new ArrayList<Integer>();
        s.add(indexnum);
        successChange.put(course, s);

        waitlistCourses.remove(course);
        registeredCourses.put(course, indexnum);
        totalau = totalau + CourseList.getCourse(course).getAU();
        addTimeSlot(CourseList.getIndexNum(indexnum).getTimeSlots(), course);// Add to timeslot
    }

    public int verifyCourse(String course) {
        // check if student is registered for course
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
            message.setText("Dear" + this.name + "\n\n" + text);

            Transport.send(message);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean verifyTimeslot(ArrayList<Integer> timeslots) {
        ListIterator<Integer> i = timeslots.listIterator();
        while (i.hasNext()) {
            if (timetable.containsKey(i.next())) {
                return false;// return false if there is a time clash
            }
        }
        return true;
    }

    public void editStudentDetails(String user, String name, String matric, char gender, String nationality,
            String major, int year, String email) {
        this.username = user;
        this.name = name;
        this.matricnum = matric;
        this.gender = gender;
        this.nationality = nationality;
        this.school = major;
        this.year = year;
        this.email = email;

    }

    public void printTimeTableByWeek() {
        TreeMap<Integer, String> oddweek = new TreeMap<Integer, String>();
        TreeMap<Integer, String> evenweek = new TreeMap<Integer, String>();

        for (Map.Entry<Integer, String> entry : timetable.entrySet()) {
            if (entry.getKey() % 2 == 1) {
                oddweek.put(entry.getKey(), entry.getValue());
            } else {
                evenweek.put(entry.getKey(), entry.getValue());
            }
        }

        System.out.println("\n\nTimetable");
        System.out.println("\nOdd Week Timetable");
        printWeeklyTimetable(oddweek);
        System.out.println("\n\nEven Week Timetable");
        printWeeklyTimetable(evenweek);
    }

    private void printWeeklyTimetable(TreeMap<Integer, String> map) {
        for (int i = 1; i <= 5; i++) {
            NavigableMap<Integer, String> temptree = new TreeMap<Integer, String>();
            temptree = map.headMap((i + 1) * 100000, false);// get all courses for that day
            if (temptree != null) {
                System.out.println(DayOfWeek.of(i) + ": ");
                for (Map.Entry<Integer, String> entry : temptree.entrySet()) {

                    ClassSchedule sc = CourseList.getIndexNum(registeredCourses.get(entry.getValue()))
                            .getClassSchedule(entry.getKey());

                    System.out.println("\t" + String.valueOf(sc.getStartTime()) + " - "
                            + String.valueOf(sc.getEndTime() + ": " + entry.getValue()));
                    System.out.println("\t\tType: " + sc.getClassType() + ", Venue: " + sc.getVenue());
                }
            }
        }

    }

    public void printTimetableByCourse() {
        for (Map.Entry<String, Integer> entry : registeredCourses.entrySet()) {
            System.out.println("Course: " + entry.getKey());
            System.out.print("\tElective type: " + electivemap.get(entry.getKey()).name() + "\n");
            CourseList.getIndexNum(entry.getValue()).printSchedule();
        }
    }

    @SuppressWarnings("unchecked")
    public boolean swapIndexCheck(int oldindex, int newindex) { // to check is timetable clash before swap!
        TreeMap<Integer, String> temp = (TreeMap<Integer, String>) timetable.clone();
        ArrayList<Integer> oldtimeslots = CourseList.getIndexNum(oldindex).getTimeSlots();
        ArrayList<Integer> newtimeslots = CourseList.getIndexNum(newindex).getTimeSlots();
        ListIterator<Integer> i = oldtimeslots.listIterator();
        while (i.hasNext()) {
            temp.remove(i.next());
        }

        ListIterator<Integer> h = newtimeslots.listIterator();
        while (h.hasNext()) {
            if (timetable.containsKey(h.next())) {
                temp = null;
                return false;// return false if there is a time clash
            }
        }
        temp = null;
        return true;

    }

    public int getAU() {
        return this.totalau;
    }

    public void swapElectiveType(String course, int elec) {
        if (verifyCourse(course) == -1) {
            return;// should not be invoked as check is doen in application class
        }
        electivemap.replace(course, intToEnum(elec));
        return;

    }

    public electiveType getElectiveTypeCourse(String course) {
        return electivemap.get(course.toLowerCase());
    }

}
