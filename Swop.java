import java.io.*;
import java.util.*;

public class Swop {
    // return true if swap successful
    // return false if swap is pending

    private static ArrayList<SwopGroup> courseSwoplist;
    private static HashMap<String, ArrayList<SwopGroup>> groups;

    @SuppressWarnings("unchecked")
    public static void loadSwopList() {
        try {
            FileInputStream fis = new FileInputStream("SwopList.ser");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object temp = ois.readObject();
            if (temp instanceof HashMap<?, ?>) {
                groups = (HashMap<String, ArrayList<SwopGroup>>) temp;
            }
            ois.close();
            fis.close();
        } catch (IOException ioe) {
            // ioe.printStackTrace();
            groups = new HashMap<String, ArrayList<SwopGroup>>();
        } catch (ClassNotFoundException c) {
            // System.out.println("Class not found");
            // c.printStackTrace();
            groups = new HashMap<String, ArrayList<SwopGroup>>();
        }
    }

    public static void saveSwopList() {
        try {
            FileOutputStream fos = new FileOutputStream("SwopList.ser");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(groups);
            oos.close();
            fos.close();
        }

        catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static boolean swopStudent(String course, int index1, int index2, String student1, String student2)
            throws UserNotFound, UserAlreadyExists, CourseDontExist {
        String k;
        if (student1.compareTo(student2) > 0) {
            k = student1 + ":" + student2;
        } else {
            k = student2 + ":" + student1;
        }

        if (groups.containsKey(k)) { // Student has pending swap with 2nd student
            // While else loop

            boolean nobreak = true;
            courseSwoplist = groups.get(k);
            ListIterator<SwopGroup> i = courseSwoplist.listIterator();
            while (i.hasNext()) {
                SwopGroup temp = i.next();
                if (temp.getIndex1() == index1 & temp.getIndex2() == index2) {
                    System.out.printf("Pending!\nWaiting for %s to swop", student2);
                    return false;
                } else if (temp.getIndex1() == index2 & temp.getIndex2() == index1) {
                    // getIndex1 == index of student that initiated
                    // index1 = current student's index
                    CourseList.SwopCourse(student1, student2, index1, index2, course);
                    // drop swopgroup class from array
                    courseSwoplist.remove(temp);
                    // Call student by username and set swop status
                    Student st = (Student) PersonList.getByUsername(student2);
                    st.setSwopstatus(index2, index1, course);
                    // replace courseswaplist
                    if (courseSwoplist.isEmpty()) {
                        groups.remove(k);
                        saveSwopList();
                    } else {
                        groups.replace(k, courseSwoplist);
                        saveSwopList();
                    }
                    temp = null; // remove object from memory
                    nobreak = false;
                    System.out.println("Swop Successful!");
                    return true;
                }
            }

            if (nobreak) {
                // Student has pending swap this is a different swap;
                SwopGroup newswop1 = new SwopGroup(student1, student2, index1, index2);
                courseSwoplist.add(newswop1);
                groups.put(k, courseSwoplist);
                System.out.printf("Pending!\n You will be notified when swop is successful!", student2);
                return false;

            }

        } else { // if student does not have existing swaps with student2
            SwopGroup newswop = new SwopGroup(student1, student2, index1, index2);
            ArrayList<SwopGroup> temparray = new ArrayList<SwopGroup>();
            temparray.add(newswop);
            groups.put(k, temparray);
            System.out.printf("Pending!\n You will be notified when swop is successful!", student2);
            return false;
        }
        return false;
    }

    public static boolean dropSwop(String student1, int index1) {

        for (Map.Entry<String, ArrayList<SwopGroup>> entry : groups.entrySet()) {
            if (entry.getKey().contains(student1)) {
                courseSwoplist = entry.getValue();
                ListIterator<SwopGroup> i = courseSwoplist.listIterator();
                while (i.hasNext()) {
                    // Only student that initiated the swop can drop the swop
                    SwopGroup temp = i.next();
                    if (temp.getIndex1() == index1) {
                        courseSwoplist.remove(temp);
                        groups.replace(entry.getKey(), courseSwoplist);
                        saveSwopList();
                        return true;
                    }
                }
            }
        }
        return false;
    }

}

class SwopGroup {
    private String student1;
    private String student2;
    private int index1;
    private int index2;

    SwopGroup(String user1, String user2, int ind1, int ind2) {
        // Student1 is the first person that initiated the swop
        student1 = user1;
        student2 = user2;
        index1 = ind1;
        index2 = ind2;
    }

    public int getIndex1() {
        return index1;
    }

    public int getIndex2() {
        return index2;
    }

    public String getStudent1() {
        return student1;
    }

    public String getStudent2() {
        return student2;
    }

}e