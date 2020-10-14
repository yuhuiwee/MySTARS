import java.util.*;

public class IndexNum{

    private String classSchedule;  //Not sure abt this, should be int or string cause this one states from what time to what time
    private String venue;
    private int vacancy;
    private int indexNum;

    public IndexNum(String classSchedule, String venue, int vacancy){
        this.classSchedule  = classSchedule;
        this.venue = venue;
        this.vacancy = vacancy;
        //this.indexNum = indexNum;
    }

    /* ----------- Get Methods -----------*/

    public String getClassSchedule(){ return classSchedule; }
    public String getVenue(){ return venue; }
    public int getVacancy(){ return vacancy; }
    public int getIndexNumber(){ return indexNum; }

    /* ----------- Set Methods -----------*/

    public void setVacancy(int vacancy){ this.vacancy = vacancy; }
    public void setIndexNumber(int indexNum){ this.indexNum = indexNum; }
    

    //is there an extend to Courselist or ?????
    //Hmm, i guess not cause its not really a subclass but more of an object composition 
}//Yup now the issue resolved =D