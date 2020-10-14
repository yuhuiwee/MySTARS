import java.util.*;

public class CourseList {
	private String courseCode;
	private String school;
	private String courseType; // lect/ lect + tuts / lect + tuts + lab
	private String userName;
    private IndexNum indexNum; /*Actually now to think of it, since the values of each indexNum is unique
								 and the values are fixed, should we use the one YiHui talked abt, the
								 hashmap thing like dictionary in java??
								 something like that
								 so we do that ah?
								 according to YH? ah we do that instead of creating alot of array
								 so when we store all our data, it is store as a Key/Value
								 Okay, guess have a lot to read up
								 hahaha same here
								 This value realtes to the IndexNum class which has separate attributes
								 like class-schedule, venue, vacancy and indexnum etc. 
								 */

	public CourseList(String courseCode, String school, String courseType, IndexNum indexNum) {
		this.courseCode = courseCode; 
        this.school = school;
        this.courseType = courseType;
		this.indexNum = indexNum;
    }

    /* ----------- Get Methods -----------*/

	public String getCourseCode() {
		return courseCode;
	}
    public String getSchool(){ return school; }
    public String getCourseType(){ return courseCode; }
    

	public boolean addCourse( String course, int index, String userName){
        
        return true;    // Actually hor, should we include userName of student in the CourseList class?
                        // because if u see Chapt 2 slide 68, they assign the student to the course
                        // using a different application class
                        // For this method i guess we have to fetch the vacancy from IndexNum class
                        // to return true or false depending if its full or not
                        // yall want 
    }

	public boolean dropCourse(String course, int index, String userName) {
		// i blur already sia
		return true;
	}
}