import java.util.*;

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

	public void updateCourseCode(String newCourseCode)
	{
		if (mapCourse.containsKey(newCourseCode))
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

	public void Swopcourse(String student1, String student2, int index1, int index 2, String coursecode){
		
	}
	//Swopcourse(String student1, String student2, int index1, int index 2, String coursecode)
	//  --to swop index with students

}