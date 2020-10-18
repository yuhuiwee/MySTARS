import java.util.*;
//FIXME: Make methods static.. Hash map should also be static--> same for all students at all times.
//If a method is static, there is no need to call new CourseList and hence, no need to pass CourseList object into methods
//If all methods are static, then there is no need to call constructer

public class CourseList {
	//All these attributes are not necessary! They should be under Course.java!
	//CourseList shoulld really be just a List of all the courses.
	//Kind of like the content page that direct you to the correct course
	//Start coding from IndexNum, then Course, then finally CourseList.. It will be easier
	//While its more logical to code top down, u have to keep visualising the codes downstream
	//Ciding from bottom up means u can see ur downstream codes and make edits easier

	private HashMap<String, Course> mapCourse = new HashMap<String, Course>();
	//Attributes needed: (Yes, literally only one attribute needed)
	//Hashmap <String, Course>
	//  --maps "CE2002" to Object CE2002
	//TODO: Add method to load and save hashmap.
	//OR:: Load and save hashmap with every method
	//OR:: Have a check at the start of the function. If hashmap is empty, load the hashmap
	//--If file isnt found, create new hashmap
	//--else, load the file and assign to hashmap. 
	//----save before exiting the method if the hashmap is edited.
	//----also update static hashmap attribut in this class
	//----personally recommend this method as its safer :)

	//Methods needed:
	public void PrintAll() {
		for (Map.Entry<String, Course> entry : mapCourse.entrySet()) {
			System.out.println(entry.getKey());
		}
	}
	//PrintAll()
	//  -- prints all courses in a list
	//  --If yall very free, can also filter by school etc... but this isnt required

	public void PrintStudentCourse(String course, int index, String username) {
	}
	//Print course(String course, int index)
	//  --for student to print the courses they have registered for
	
	//--- I think this one should be placed in the Student Class since there is a hashmap which stores the 
	//registered courses of each student.---
	//YuHui: This hashmap only stores the course name and index. Need this method to call for course description details :)
	//YuHui: But if we arent planning on displaying this info, then its ok.. Its just more convenient to add more deets in the future if we really need to.

	//For method NewCourse, 
	public void NewCourse(String courseCode, String newCourseCode, Boolean update) 
	{

		if (mapCourse.containsKey(courseCode))
		{
			return;
		}

		else
		{
			if (update)
			{
				mapCourse.get(courseCode).updateCourseCode(courseCode);
			}
			else
			{
				
			}
		
		}

	}

	public void updateCourseCode(String newCourseCode) // TODO: insert the current course code as well
	{
		if (mapCourse.containsKey(newCourseCode))
		{
			System.out.println("There is already an existing course code");
			return;
		}

		else 
		{
			
		}
	}

	//Newcourse(String coursecode, Hashmap <int, int> indexNumMappedtoVacancies, ...)
	//  --for admin to add new courses
	//  --Create new Course object & set attributes
	//  --adds newly created Course object to hashmap with String coursecode as key

	public void AddCourse(String coursecode, int index, String username) {
		mapCourse.get(coursecode).addCourse(index,username);
	}
	//Addcourse(String coursecode, int index, String username)
	//  --for student to add new courses
	//  --Use String coursecode to get Course object
	//  --Call method addcourse from Course object
	//  --Course.addcourse shold return a true/false value
	//  --true = successfully added
	//  --false = no more vacancies. Put on waitlist

	public void DropCourse(String coursecode, int index, String username) {
	}
	//Dropcourse(String coursecode, int index, String username)
	//  --for student to drop courses
	//  --Similar to CourseList.addcourse
	//  --Course.dropcourse should return true/false
	//  --true = successfully dropped course
	//  --false = student not registered for that course index the first place.. so unable to drop

	public void SwopCourse(String student1, String student2, int index1, int index 2, String coursecode){
		
	}
	//Swopcourse(String student1, String student2, int index1, int index 2, String coursecode)
	//  --to swop index with students

}