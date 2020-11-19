import java.util.*;

public class ClassSchedule {
    private String classType;
    private String weekType;
    private String dayOfTheWeek;
    private int startTime;
    private int endTime;
    private String venue;
    private ArrayList<Integer> timeSlotSerialNumber;
    // private static HashMap<String, ArrayList<Integer>> classTiming;
    // Venue venue;

    public ClassSchedule(String classType, String weekType, String dayOfTheWeek, int startTime, int endTime,
            String venue, ArrayList<Integer> timeSlotSerialNumber) {
        this.classType = classType;
        this.weekType = weekType;
        this.dayOfTheWeek = dayOfTheWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
        this.timeSlotSerialNumber = new ArrayList<Integer>(timeSlotSerialNumber);
    }

    /*
     * public static void createVenueLoc() { venueLoc = new HashMap<String,
     * ArrayList<Integer>>(); venueLoc.put("LT1A", new ArrayList<Integer>());
     * venueLoc.put("LT2A", new ArrayList<Integer>()); venueLoc.put("TR1", new
     * ArrayList<Integer>()); venueLoc.put("SWLAB", new ArrayList<Integer>()); }
     */

    // Normal Methods
    public void printSchedule() {
        System.out.println("\n\tClass Type: " + classType);
        System.out.println("\tWeek Type: " + weekType);
        System.out.println("\tDay of the Week: " + dayOfTheWeek);
        System.out.println("\tTimeslot: " + startTime + " - " + endTime);
        System.out.println("\tVenue: " + venue + "\n");
    }

    // Getters and Setters
    public ArrayList<Integer> getTimeSlotSerialNumber() {
        return timeSlotSerialNumber;
    }

    public String getClassType() {
        return classType;
    }

    public void setClassType(String classType) {
        this.classType = classType;
    }

    public String getWeekType() {
        return weekType;
    }

    public void setWeekType(String weekType) {
        this.weekType = weekType;
    }

    public String getDayOfTheWeek() {
        return dayOfTheWeek;
    }

    public void setDayOfTheWeek(String dayOfTheWeek) {
        this.dayOfTheWeek = dayOfTheWeek;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
