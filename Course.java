import java.util.*;

public class Course 
{

    private String courseCode; 
    private String school;
    private String courseType; // lect/ lect + tuts / lect + tuts + lab
    private String userName;
    private HashMap<Integer, IndexNum> mapIndex; //The Integer in this case is the name of the IndexNum. So for
                                                // example if i want to find for index 1024, i can simply use
                                                // mapIndex.get(1024) to obtain the object indexNum.

    public Course(String courseCode, String school, String courseType) {
        this.courseCode = courseCode; 
        this.school = school;
        this.courseType = courseType;
        mapIndex = new HashMap<Integer, IndexNum>();
		//IndexNum I1 = new IndexNum("classSchedule", "venue", 30);
    }

    /* ----------- Get Methods -----------*/

    public String getCourseCode() {
        return courseCode;
    }
	public String getSchool() {
		return school;
    }
	public String getCourseType() {
		return courseType;
    }

	/* ----------- Set Methods -----------*/

    public void setcourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
	public void setSchool(String school) {
        this.school = school;
    }

    public void updateCourseCode(String currentCourseCode, String newCourseCode)
    {   
        //FIXME: Is the handling of updating enough in the course list.
        //TODO: How to find the course that they want to update






    }
    // Since Admin can add/update course, we have to include all the mutator methods of course. But since
    // they can only change these 2 variables, we don't include the rest of the variables.

    /* ----------- Normal Methods -----------*/

    public boolean addCourse(int index, String userName) //for students to add course
    {
        mapIndex.get(index).addStudent(userName); //mapIndex.get(course) = object
        return true;
    }

    public boolean dropCourse(int index, String userName, Boolean swopFlag) 
    {
		mapIndex.get(index).removeStudent(userName, swopFlag);
        return true;
    }
    public void printVacancy(int indexNum)
	{
        mapIndex.get(indexNum).getVacancy();
    }
    
    public boolean checkIndex(int index){
        if (mapIndex.containsKey(index)){
            return true;
        }
        else{
            return false;
        }
    }
}