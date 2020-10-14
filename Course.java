import java.util.*;

public class Course 
{

    private String courseCode;
    private String school;
    private String courseType; // lect/ lect + tuts / lect + tuts + lab
    private String userName;
    private HashMap<String, IndexNum> mapIndex; //The String in this case is the name of the IndexNum. So for
                                                // example if i want to find for index 1024, i can simply use
                                                // mapIndex.get(1024) to obtain the object indexNum.

    public Course(String courseCode, String school, String courseType, IndexNum indexNum) {
        this.courseCode = courseCode; 
        this.school = school;
        this.courseType = courseType;
        //this.indexNum = indexNum;
        mapIndex = new HashMap<String, IndexNum>();
		IndexNum I1 = new IndexNum("classSchedule", "venue", 30);
        
    }

    /* ----------- Get Methods -----------*/

    public String getCourseCode() {
        return courseCode;
    }
	public String getSchool() {
		return school;
    }
	public String getCourseType() {
		return courseCode;
    }

	/* ----------- Set Methods -----------*/

    public void setcourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
	public void setSchool(String school) {
        this.school = school;
    }
    // Since Admin can add/update course, we have to include all the mutator methods of course. But since
    // they can only change these 2 variables, we don't include the rest of the variables.

    /* ----------- Normal Methods -----------*/

    public boolean addCourse(int index, String userName)
    {
        mapIndex.get(index).addStudent(userName); //mapIndex.get(course) = object
        return true;
    }

    public boolean dropCourse(int index, String userName, Boolean swopFlag) 
    {
		mapIndex.get(index).removeStudent(userName, swopFlag);
        return true;
    }
    public void printVacancy(String indexNum)
	{
        mapIndex.get(indexNum).getVacancy();
	}
}