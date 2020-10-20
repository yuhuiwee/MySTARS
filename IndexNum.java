import java.util.*;

public class IndexNum{

    private String classSchedule;
    private String venue;
    private int vacancy;
    //private int indexNum;
    private ArrayList<String> listOfRegisteredStudents;
    private ArrayList<String> waitingList;

    public IndexNum(String classSchedule, String venue, int vacancy){
        this.classSchedule  = classSchedule;
        this.venue = venue;
        this.vacancy = vacancy;
        listOfRegisteredStudents = new ArrayList<String>();     //So when index 1024 created, it also creates
        waitingList = new ArrayList<String>();                  //these 2 lists.
        //this.indexNum = indexNum;
    }

    /* ----------- Get Methods -----------*/

    public String getClassSchedule(){ return classSchedule; }
    public String getVenue(){ return venue; }
    public int getVacancy(){ return vacancy; }
    //public int getIndexNumber(){ return indexNum; }

    /* ----------- Set Methods -----------*/
    
    public void setVacancy(int vacancy){ this.vacancy = vacancy; }
    //public void setIndexNumber(int indexNum){ this.indexNum = indexNum; }

    /* ----------- Normal Methods -----------*/

	public void addStudent(String username){
        // So there will be list of students available in application
        // But this will be initiated by the student class
        if(vacancy > 0)
        {
            listOfRegisteredStudents.add(username);
            vacancy--;
            System.out.println("You have been added to the index.");
            
        }
        else
        {
			waitingList.add(username);
			System.out.println("There are no vacancy left in this index. You have been placed in the waitlist.");
        }
    }
    public void removeStudent(String username, boolean swopFlag) //flag is for the swop to happen without adding in the students in the waiting list
	{
        listOfRegisteredStudents.remove(username);
		if (swopFlag) 
        {
            if (waitingList.isEmpty())
            {
                vacancy++;
                System.out.println("You have been removed from the index.");
            }
            else
            {
				listOfRegisteredStudents.add(waitingList.get(0));
                waitingList.remove(0);
                System.out.println("You have been removed from the index.");
            }
		} 
        else 
        {
            listOfRegisteredStudents.remove(username); //Might be a duplicate since we alr have it up on top
			vacancy++;
		}
    }   
    public void printStudentList()
    {
		System.out.println(listOfRegisteredStudents); 
    }
}
