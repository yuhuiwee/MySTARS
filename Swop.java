import java.util.*;

public class Swop {
    //return true if swap successful
    //return false if swap is pending
    String k;
    ArrayList<Swopgroup> courseswaplist;
    private HashMap <String, ArrayList<Swopgroup>> groups;

    public Swop(){
        // TODO import from .ser file. If !exist, create new Hashmap & export
        // To call .ser file at the start of the application!
        groups = new HashMap <String, ArrayList<Swopgroup>>();
    }
    public boolean swopStudent(String cor, int index1, int index2, String student1, String student2){
        if (student1.compareTo(student2)>0){
            k = student1 + ":" + student2;            
        }
        else{
            k = student2 + ":" + student1;
        }
        
        if (groups.containsKey(k)){ // Student has pending swap with 2nd student
            //While else loop

            boolean nobreak = true;
            courseswaplist = groups.get(k);
            ListIterator<Swopgroup> i = courseswaplist.listIterator();
            while (i.hasNext()){
                Swopgroup temp = i.next();
                if (temp.getIndex1() == index1 & temp.getIndex2() == index2){
                    System.out.printf("Pending!\nWaiting for %s to swop", student2);
                    return false;
                    break;
                    
                }
                else if (temp.getIndex1() == index2 & temp.getIndex2() == index1){
                    //TODO change to CourseList.swop()
                    CourseList.swop(cor, index1, student1);
                    CourseList.swop(cor, index2, student2);
                    CourseList.swop(cor, index1, student2);
                    CourseList.swop(cor, index2, student1);
                    
                    //FIXME: Call student2 and set swop status
                    // drop swopgroup class from array
                    courseswaplist.remove(temp);
                    temp = null; //remove object from memory
                    nobreak = false;
                    return true;
                    break;
                }
            }

            if (nobreak){
                //Student has pending swap this is a different swap;
                Swopgroup newswop1 = new Swopgroup(student1, student2, index1, index2);
                courseswaplist.add(newswop1);
                groups.put(k, courseswaplist);
            }
            return true;
            
        }
        else{ //if student does not have existing swaps with student2
            Swopgroup newswop = new Swopgroup(student1, student2, index1, index2);
            ArrayList<Swopgroup> temparray = new ArrayList<Swopgroup>();
            temparray.add(newswop);
            groups.put(k, temparray);
            return false;
        }
    }
    public boolean dropswop(String student1, String student2, int index1, int index2){
        if (student1.compareTo(student2)>0){
            k = student1 + ":" + student2;            
        }
        else{
            k = student2 + ":" + student1;
        }

        if (groups.containsKey(k)){
            boolean nobreak = true;
            courseswaplist = groups.get(k);
            ListIterator<Swopgroup> i = courseswaplist.listIterator();
            while (i.hasNext()){
                //Only student that initiated the swop can drop the swop
                Swopgroup temp = i.next();
                if (temp.getIndex1() == index1 & temp.getIndex2() == index2){
                    courseswaplist.remove(temp);
                    temp = null; //remove object from memory
                    nobreak = false;
                    return true;
                }
            }

            if (nobreak){
                System.out.println("Swop not found!");
                return false;
            }
        }
        
        else{
            System.out.println("Swop not found!");
            return false;
        }
        return true;
    }

}
class Swopgroup{
    private String student1;
    private String student2;
    private int index1;
    private int index2;

    Swopgroup(String user1, String user2, int ind1, int ind2){
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


}