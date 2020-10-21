import java.io.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AccessPeriod{
    
    private static HashMap<String, ArrayList<ZonedDateTime>> majoracc = null; 
    private ZonedDateTime start;
    private ZonedDateTime end;
    public static void CreateDefaultAccessPeriod(){
        //Default access period: 1st Nov 2020  to 30 Nov 2020 for all students
        ZonedDateTime start = ZonedDateTime.parse("2020-01-11T00:00:00+08:00[Asia/Singapore]");
        ZonedDateTime end = ZonedDateTime.parse("2020-30-11T23:59:59+08:00[Asia/Singapore]");
        majoracc = new HashMap<String, ArrayList<ZonedDateTime>>();
        ArrayList<ZonedDateTime> temp = new ArrayList<ZonedDateTime>(2);
        temp.add(start);
        temp.add(end);

        majoracc.put("Default", temp);

        saveAccessPeriod(); //Save to file
    }

    public static void saveAccessPeriod(){
        try{
            FileOutputStream fos =
                new FileOutputStream("AccessPeriod.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(majoracc);
            oos.close();
            fos.close();
        }
        
        catch(IOException ioe){
            ioe.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static void loadAccessPeriod(){
        try
        {
         FileInputStream fis = new FileInputStream("AccessPeriod.ser");
         ObjectInputStream ois = new ObjectInputStream(fis);
         Object temp = ois.readObject();
         if(temp instanceof HashMap<?,?>){
             majoracc = (HashMap<String, ArrayList<ZonedDateTime>>) temp;
        }
         ois.close();
         fis.close();
        }
        catch(IOException ioe){
            ioe.printStackTrace();
            CreateDefaultAccessPeriod(); //If file not found, create default values
        }
        catch(ClassNotFoundException c){
            System.out.println("Class not found");
            c.printStackTrace();
            CreateDefaultAccessPeriod();
      }
    }

    public static boolean checkAccessPeriod(AccessPeriod st){
        ZonedDateTime current = ZonedDateTime.now();

        if(current.getZone().toString()!= "Asia/Singapore"){
            current = current.withZoneSameInstant(ZoneId.of("Asia/Singapore"));
        }

        if (current.isAfter(st.getEndDateTime()) & current.isBefore(st.getEndDateTime())){
            return true;
        }
        else{
            System.out.println("Access Denied! Your Personalised access period is: ");
            System.out.println("Start Date: "+DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm Z").format(st.getStartDateTime()));
            System.out.println("Start Date: "+DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm Z").format(st.getEndDateTime()));
            return false;
        }

    }


    //Constructor
    public AccessPeriod(String major){
        //Iterate through hashmap to get major. If not in hashmap, set start and end date as default
        if(majoracc == null){
            loadAccessPeriod();
        }

        boolean notdefault = false;
        for (Map.Entry<String, ArrayList<ZonedDateTime>> e: majoracc.entrySet()){
            if (e.getKey().toLowerCase()==major.toLowerCase()){
                this.start = e.getValue().get(0);
                this.end = e.getValue().get(1);
                notdefault = true;
                return;
            }
        }

        if (!notdefault){
            this.start = majoracc.get("Default").get(0);
            this.end = majoracc.get("Default").get(1);
            return;
        }
    }

    public ZonedDateTime getStartDateTime(){
        return this.start;
    }

    public ZonedDateTime getEndDateTime(){
        return this.end;
    }


    //For Admin
    /*
    PrintAll
    Change AccessPeriod by major
    Remove AccessPeriod for major
    Add AccessPeriod for major
    Change Default Access Period
    Remove All but Default
    */

    public static void PrintALLaccess(){
        for (Map.Entry<String, ArrayList<ZonedDateTime>> entry : majoracc.entrySet()){
            if (entry.getKey()!="Default"){ //Print all but default
                System.out.println(entry.getKey()+" major: ");
                System.out.println("Start Date: "+DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm Z").format(entry.getValue().get(0)));
                System.out.println("End Date: " + DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm Z").format(entry.getValue().get(1)));
            }
        }
        System.out.println("Default Access Period: ");
        System.out.println("Start Date: "+DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm Z").format(majoracc.get("Default").get(0)));
        System.out.println("Start Date: "+DateTimeFormatter.ofPattern("dd/MM/yyyy - hh:mm Z").format(majoracc.get("Default").get(0)));

    }

    public static void ChangeMajorAccess(String major){
        if (majoracc.containsKey(major)){

        }
        else{
            
        }
    }
}