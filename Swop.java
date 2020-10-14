import java.util.*;

public class Swop {
    //return true if swap successful
    //return false if swap is pending
    String k;
    ArrayList<Swopgroup> courseswaplist;
    private HashMap <String, ArrayList<Swopgroup>> groups;
    public Swop(){
        groups = new HashMap <String, ArrayList<Swopgroup>>();
    }
    public boolean swopStudent(String cor, int index1, int index2, String student1, String student2){
        if (student1.compareTo(student2)>0){
            k = student1 + ":" + student2;            
        }
        else{
            k = student2 + ":" + student1;
        }
        
        if (groups.containsKey(k)){
            courseswaplist = groups.get(k);

            ListIterator<Swopgroup> i = courseswaplist.listIterator();
            while (i.hasNext()){
                Swopgroup temp = i.next();
                if (temp.getIndex1() == index1 & temp.getIndex2() == index2){
                    CourseList.dropCourse(cor, index1, student1);
                    CourseList.dropCourse(cor, index2, student2);
                    CourseList.addCourse(cor, index1, student2);
                    CourseList.addCourse(cor, index2, student1);
                    dropswop(k, temp);
                }
                else if (temp.getIndex1() == index2 & temp.getIndex2() == index1){

                }
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
    private boolean dropswop(){
        return true;
    }

    private boolean swapping(){

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