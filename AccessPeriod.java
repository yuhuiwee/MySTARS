import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccessPeriod implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static HashMap<String, ArrayList<ZonedDateTime>> majoracc = null;
    private String majoryear;

    public static void CreateDefaultAccessPeriod() {
        // Default access period: 1st Nov 2020 to 30 Nov 2020 for all students
        ZonedDateTime start = ZonedDateTime.parse("2020-10-25T00:00:00+08:00[Asia/Singapore]");
        ZonedDateTime end = ZonedDateTime.parse("2020-11-30T23:59:59+08:00[Asia/Singapore]");
        majoracc = new HashMap<String, ArrayList<ZonedDateTime>>();
        ArrayList<ZonedDateTime> temp = new ArrayList<ZonedDateTime>(2);
        temp.add(start);
        temp.add(end);

        majoracc.put("Default", temp);

        saveAccessPeriod(); // Save to file
    }

    public static void saveAccessPeriod() {
        try {
            FileOutputStream fos = new FileOutputStream("AccessPeriod.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(majoracc);
            oos.close();
            fos.close();
        }

        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadAccessPeriod() {
        try {
            FileInputStream fis = new FileInputStream("AccessPeriod.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object temp = ois.readObject();
            if (temp instanceof HashMap<?, ?>) {
                majoracc = (HashMap<String, ArrayList<ZonedDateTime>>) temp;
            }
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            // ioe.printStackTrace();
            CreateDefaultAccessPeriod(); // If file not found, create default values
        } catch (ClassNotFoundException c) {
            // System.out.println("Class not found");
            // c.printStackTrace();
            CreateDefaultAccessPeriod();
        }
    }

    public static boolean checkAccessPeriod(Student stu) {
        AccessPeriod st = stu.getAccessPeriod();
        ZonedDateTime current = ZonedDateTime.now();

        if (current.getZone().toString() != "Asia/Singapore" | current.getZone().toString() != "+08:00") {
            current = current.withZoneSameInstant(ZoneId.of("Asia/Singapore"));
        }

        if (current.isAfter(st.getStartDateTime()) & current.isBefore(st.getEndDateTime())) {
            return true;
        } else {
            System.out.println("Access Denied! Your Personalised access period is: ");
            System.out.println(
                    "Start Date: " + DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm Z").format(st.getStartDateTime()));
            System.out.println(
                    "Start Date: " + DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm Z").format(st.getEndDateTime()));
            return false;
        }

    }

    // Constructor for student
    public AccessPeriod(String major, int year) {
        this.majoryear = String.join(",", major.toLowerCase(), String.valueOf(year));
    }

    private ArrayList<ZonedDateTime> getAccessPeriod() {
        // Iterate through hashmap to get major, year. If not in hashmap, set start and
        // end date as default
        if (majoracc == null) {
            loadAccessPeriod();
        }
        for (Map.Entry<String, ArrayList<ZonedDateTime>> e : majoracc.entrySet()) {
            if (e.getKey() == majoryear) {
                return e.getValue();
            }
        }
        return majoracc.get("Default");
    }

    public ZonedDateTime getStartDateTime() {
        return this.getAccessPeriod().get(0);
    }

    public ZonedDateTime getEndDateTime() {
        return this.getAccessPeriod().get(1);
    }

    // For Admin
    /*
     * PrintAll Change AccessPeriod by major Remove AccessPeriod for major Add
     * AccessPeriod for major Change Default Access Period Remove All but Default
     */

    public static void printAllAccess() {
        for (Map.Entry<String, ArrayList<ZonedDateTime>> entry : majoracc.entrySet()) {
            if (entry.getKey() != "Default") { // Print all but default
                String majy = entry.getKey();
                int index = majy.lastIndexOf(",");
                String maj = majy.substring(0, index);
                String y = majy.substring(index + 1, majy.length());
                System.out.println(maj + " major, Year" + y + ": ");
                System.out.println("Start Date: "
                        + DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm Z").format(entry.getValue().get(0)));
                System.out.println("End Date: "
                        + DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm Z").format(entry.getValue().get(1)));
            }
        }
        System.out.println("Default Access Period: ");
        System.out.println("Start Date: "
                + DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm Z").format(majoracc.get("Default").get(0)));
        System.out.println("Start Date: "
                + DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm Z").format(majoracc.get("Default").get(0)));
        return;

    }

    public static void changeMajorAccess(String major, int year, ZonedDateTime start, ZonedDateTime end) {
        // Use for both adding and editing major access
        String key = String.join(",", major.toLowerCase(), String.valueOf(year));
        ArrayList<ZonedDateTime> temp = new ArrayList<ZonedDateTime>(2);
        temp.add(start);
        temp.add(end);
        if (majoracc.containsKey(key)) {
            System.out.printf("Changing Access Period for %s, Year %d", major, year);
            majoracc.replace(key, temp);
            saveAccessPeriod();
            return;
        } else {
            System.out.printf("Adding new Access Period for %s, Year %d", major, year);
            majoracc.put(key, temp);
            saveAccessPeriod();
            return;
        }
    }

    public static void removeMajorAccess(String major, int year) throws AccessPeriodNotFound {
        String key = String.join(",", major.toLowerCase(), String.valueOf(year));
        if (majoracc.containsKey(key)) {
            System.out.printf("Removing Access Period for %s, Year %d", major, year);
            saveAccessPeriod();
            return;
        }

        else {
            throw new AccessPeriodNotFound("Input Error! Access Period not found!");
        }
    }

    public static void editDefaultAccess(ZonedDateTime start, ZonedDateTime end) {
        ArrayList<ZonedDateTime> temp = new ArrayList<ZonedDateTime>(2);
        temp.add(start);
        temp.add(end);

        System.out.println("Changing Default Access Period");
        majoracc.replace("Default", temp);
        saveAccessPeriod();
        return;
    }

    public static void removeAllbutDefault() {
        ArrayList<ZonedDateTime> temp = new ArrayList<ZonedDateTime>(2);
        temp = majoracc.get("Default");
        majoracc.clear();
        majoracc.put("Default", temp);
        saveAccessPeriod();
        return;
    }
}