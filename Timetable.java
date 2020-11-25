import java.io.Serializable;
import java.time.DayOfWeek;
import java.util.*;
import java.util.Map.Entry;

public class Timetable implements Serializable, Cloneable{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ArrayList<Integer> timeSlotSerialNumber;
    private TreeMap<Integer, ArrayList<String>> timeSlotInformation;// startSerial : <endSerial, coursecode, indexnum,
                                                                    // classtype, venue>

    // Constructors
    public Timetable(ArrayList<Integer> timeSlotSerialNumber, TreeMap<Integer, ArrayList<String>> timeSlotInfo) {
        this.timeSlotSerialNumber = new ArrayList<Integer>(timeSlotSerialNumber);// arraylist of all timeslots
        this.timeSlotInformation = new TreeMap<Integer, ArrayList<String>>(timeSlotInfo);
    }

    public Timetable() { // for creating empty timetable
        this.timeSlotSerialNumber = new ArrayList<Integer>();
        this.timeSlotInformation = new TreeMap<Integer, ArrayList<String>>();
    }

    // Normal Methods
    public void printSchedule(int startSerial) { // print only one timing
        ArrayList<String> a = timeSlotInformation.get(startSerial);
        int day = startSerial / 100000;
        int type = startSerial % 10;

        String weekType;
        if (type == 2) {
            weekType = "Both";
        } else if (type == 1) {
            weekType = "Odd";
        } else {
            weekType = "Even";
        }

        System.out.println("\tClass Type: " + a.get(3));
        System.out.println("\tWeek Type: " + weekType);
        System.out.println("\tDay of the Week: " + DayOfWeek.of(day));
        System.out.println("\tTimeslot: " + getTimefromSerial(startSerial) + " - " + getTimefromSerial(Integer.parseInt(a.get(0))));
        System.out.println("\tVenue: " + a.get(4) + "\n");
    }

    public void printSchedule() {// printall
        for (Integer k : timeSlotInformation.keySet()) {
            printSchedule(k);
        }
    }

    public void printWeeklySchedule() {
        TreeMap<Integer, ArrayList<String>> oddWeek = new TreeMap<Integer, ArrayList<String>>();
        TreeMap<Integer, ArrayList<String>> evenWeek = new TreeMap<Integer, ArrayList<String>>();

        for (Map.Entry<Integer, ArrayList<String>> entry : timeSlotInformation.entrySet()) {
            if (entry.getKey()%10 ==2){
                oddWeek.put(entry.getKey(), entry.getValue());
                evenWeek.put(entry.getKey(), entry.getValue());
            }
            else if (entry.getKey() % 10 == 1) {
                oddWeek.put(entry.getKey(), entry.getValue());
            } else {
                evenWeek.put(entry.getKey(), entry.getValue());
            }
        }

        System.out.println("\n\n");
        System.out.println("Odd Week: ");
        printWeek(oddWeek);
        System.out.println("\n\n");
        System.out.println("Even Week");
        printWeek(evenWeek);
        return;
    }

    private void printWeek(TreeMap<Integer, ArrayList<String>> map) {
        for (int i = 1; i <= 5; i++) {
            SortedMap<Integer, ArrayList<String>> temptree = new TreeMap<Integer, ArrayList<String>>();
            temptree = map.subMap(i*100000,(i + 1) * 100000);// get all courses for that day
            if (!temptree.isEmpty()) { // if temp tree == null, means no lessons for that day
                System.out.println(DayOfWeek.of(i) + ": ");
                for (Entry<Integer, ArrayList<String>> entry : temptree.entrySet()) {
                    // endSerial[0], coursecode[1], indexnum[2], classtype[3],venue[4]
                    ArrayList<String> a = entry.getValue();

                    if (a.get(3).equals("LECTURE") | a.get(3).equals("LEC")){
                        System.out.println("\tTime: " + getTimefromSerial(entry.getKey()) + " - "
                            + getTimefromSerial(Integer.parseInt(a.get(0))));
                        System.out.println("\tCourse: " + a.get(1)); //dont print index when its lecture classtype
                        System.out.println("\tClass Type: "+a.get(3));
                        System.out.println("\tVenue: " + a.get(4));
                        System.out.println();
                    }
                    else{//if not lecture type
                        System.out.println("Time: " + getTimefromSerial(entry.getKey()) + " - "
                                + getTimefromSerial(Integer.parseInt(a.get(0))));
                        System.out.println("Course: " + a.get(1) + ", Index: " + a.get(2));
                        System.out.println("\tClass Type: "+a.get(3));
                        System.out.println("Venue: " + a.get(4));
                    }
                }
            }
        }
    }

    // Getters and Setters
    public ArrayList<Integer> getTimeSlotSerialNumber() {
        return timeSlotSerialNumber;
    }

    public TreeMap<Integer, ArrayList<String>> getTimeSlotInformation() {
        return timeSlotInformation;
    }

    public void setTimeSlotSerialNumber(ArrayList<Integer> t) {
        this.timeSlotSerialNumber = t;
    }

    @SuppressWarnings("unchecked")
    public boolean checkTimeslot(Timetable t) {
        ArrayList<Integer> time = (ArrayList<Integer>) t.getTimeSlotSerialNumber().clone();
        ArrayList<Integer> temp = (ArrayList<Integer>) timeSlotSerialNumber.clone();

        temp.retainAll(time); // check for intersection
        if (temp.isEmpty()) {
            return true;// no time clash
        } else {
            return false;
        }
    }

    public void mergeTimetable(Timetable t) throws TimetableClash {
        if (!checkTimeslot(t)) {// should not invole this! check before calling this method!
            throw new TimetableClash("Timetable Clash!");
        }
        if (this.timeSlotInformation==null | this.timeSlotSerialNumber==null){
            this.timeSlotInformation = t.getTimeSlotInformation();
            this.timeSlotSerialNumber= t.getTimeSlotSerialNumber();
        }
        else{
            this.timeSlotInformation.putAll(t.getTimeSlotInformation());
            this.timeSlotSerialNumber.addAll(t.getTimeSlotSerialNumber());
        }
    }

    public void removeTimetable(Timetable t) {
        timeSlotSerialNumber.removeAll(t.getTimeSlotSerialNumber());
        timeSlotInformation.entrySet().removeAll(t.getTimeSlotInformation().entrySet());
    }

    public void removeClass(int startSerial, int endSerial){
        timeSlotInformation.remove(startSerial);

        while (startSerial!=endSerial){
            if (startSerial%10 == 2){
                if (timeSlotSerialNumber.contains(startSerial - 1)){
                    timeSlotSerialNumber.remove(startSerial - 1);
                    timeSlotSerialNumber.remove(startSerial - 2);
                }
            }
            else{
                if(timeSlotSerialNumber.contains(startSerial)){
                    timeSlotSerialNumber.remove(startSerial);
                }
            }

            startSerial = startSerial + 300;
            if (startSerial%1000 >= 600){
                startSerial = startSerial + 400;
            }

        }
    }



    public void addClass(int startSerial, int endSerial, String courseCode, int index, String courseType, String venue)
            throws TimetableClash {
        // endSerial[0], coursecode[1], indexnum[2], classtype[3],venue[4]
        if (!checkStartEnd(startSerial, endSerial)) {
            throw new TimetableClash("Timetable Clash!");
        }

        ArrayList<String> temp = new ArrayList<String>();
        temp.add(String.valueOf(endSerial));
        temp.add(courseCode.toUpperCase());
        temp.add(String.valueOf(index));
        temp.add(courseType.toUpperCase());
        temp.add(venue.toUpperCase());

        timeSlotInformation.put(startSerial, temp); // Start time serial will be the key, so it will be arranged in
                                                    // order

        while (startSerial != endSerial) {
            if (startSerial % 10 == 2) { // For classes with normal week type, we will add both odd and even week into
                                         // the timetable of the class
                timeSlotSerialNumber.add(startSerial - 1);
                timeSlotSerialNumber.add(startSerial - 2);
            } else {
                timeSlotSerialNumber.add(startSerial);
            }
            startSerial = startSerial + 300; // Account for every half an hour interval (0800 - 0900 = Serial Code:
                                             // 108000 & 108300)
            if (startSerial % 1000 >= 600) { // If it reaches 0860, we make it 0900.
                startSerial = startSerial + 400;
            }
        }
        

        return;
    }
    public boolean checkStartEnd(int startSerial, int endSerial) {
        // return true if no clash
        // return false if clash
        if (timeSlotSerialNumber.isEmpty()) {
            return true;
        }
        ListIterator<Integer> i = timeSlotSerialNumber.listIterator();
        while (i.hasNext()) {
            int s = i.next();
            if (s >= startSerial & s <= endSerial) { 
                /* Eg. s = 108300 (Monday, 8AM, Odd week) startSerial = 108002 endSerial = 110002
                so as long as there is a clash between the intervals, the timing
                will not be accepted
                */
                if (startSerial % 10 == 2) { // For normal weeks
                    return false;
                }
                else if (s % 10 == startSerial % 10) { // For odd/even week
                    return false;
                }
            }

        }
        return true;
    }

    public String getTimefromSerial(int serialtime) {
        String time = "";

        serialtime = serialtime / 10;
        serialtime = serialtime % 10000;

        if (serialtime < 1000) {
            time = "0";
        }

        time = time + String.valueOf(serialtime);
        return time;
    }

    @Override
    public Timetable clone() throws CloneNotSupportedException {

        return (Timetable) super.clone();
    }

    public void editIndex(int index) {// to be used only in course creation in application!
        for (ArrayList<String> entry : timeSlotInformation.values()) {
            entry.set(2, String.valueOf(index)); // 2 represents the timeSlotInformation Tree map KEY value for IndexNum
        } // So here, we are adding the index value to this tree map
        return;

    }

    public Timetable getLectureTimings() throws NumberFormatException, TimetableClash { // for adding new indexes
        Timetable t = new Timetable();
        for (Map.Entry<Integer, ArrayList<String>> entry : timeSlotInformation.entrySet()) {
            ArrayList<String> a = entry.getValue();
            if (a.get(3) == "LECTURE" | a.get(3) == "LEC") {
                // endSerial[0], coursecode[1], indexnum[2], classtype[3],venue[4]
                t.addClass(entry.getKey(), Integer.parseInt(a.get(0)), a.get(1), Integer.parseInt(a.get(2)), a.get(3),
                        a.get(4));

            }
        }
        return t;
    }

    public boolean isEmpty(){
        if (this.timeSlotSerialNumber.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }

}
