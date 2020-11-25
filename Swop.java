import java.io.*;
import java.util.*;

/**
 * Represents when a student is trying to swap with another student
 * @author Kurai Sora
 * @version 1.0
 * @since 25 November 2020
 */
public class Swop {
	// return true if swap successful
	// return false if swap is pending

	/**
	 * An array list of the different course swap
	 */
	private static ArrayList<SwopGroup> courseSwoplist;

	/**
	 * A string to array list hash map of swap groups
	 */
	private static HashMap<String, ArrayList<SwopGroup>> groups;

	/**
	 * Loads the swap list from the SwopList.ser file
	 */
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

	/**
	 * Saves the swop list in the SwopList.ser file
	 */
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

	/**
	 * A Swop constructor
	 * Student is able to swap an index with another student's index
	 * @param course This is the course
	 * @param index1 This is the index student1 have
	 * @param index2 This is the index student2 have
	 * @param student1 This is the username of student1
	 * @param student2 This is the username of student2
	 * @return This is a true/false on whether the student is successful in swapping their indexes
	 * @throws UserNotFound This exception is thrown when the username entered is not found
	 * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database / hash map
	 * @throws CourseDontExist This exception is thrown when the course entered is not found
	 * @throws CloneNotSupportedException This exception is thrown when an object cannot be clone
	 * @throws TimetableClash This exception is thrown when the lesson time entered is the same as another lesson time of the same index
	 * @throws VenueAlreadyExists This exception is thrown when the venue entered is already in the database / hash map
	 */
	public static boolean swopStudent(String course, int index1, int index2, String student1, String student2)
			throws UserNotFound, UserAlreadyExists, CourseDontExist, CloneNotSupportedException, TimetableClash,
			VenueAlreadyExists {
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

	/**
	 * Student is able to retract their request in swapping index with another student's index
	 * @param student1 This is the student's username
	 * @param index1 This is the student's initial index number
	 * @return This is a true/false on whether the student is successful in dropping the swap
	 */
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

/**
 * Represents a group of students wanting to swap with each other
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
class SwopGroup {

	/**
	 * This is student1's username
	 */
	private String student1;

	/**
	 * This is student1's username
	 */
	private String student2;

	/**
     * This is student1's index
     */
	private int index1;

	/**
     * This is student2's index
     */
	private int index2;

	/**
     * A swop group constructor
     * @param user1 This is student1 who initiated the swap
     * @param user2 This is student2 who student1 wants to swap with
     * @param ind1 This is student1's index
     * @param ind2 This is student2's index
     */
	SwopGroup(String user1, String user2, int ind1, int ind2) {
		// Student1 is the first person that initiated the swop
		student1 = user1;
		student2 = user2;
		index1 = ind1;
		index2 = ind2;
	}

	/**
     * Gets student1's index
     * @return This is student1's index
     */
	public int getIndex1() {
		return index1;
	}

	/**
     * Gets student2's index
     * @return This is student2's index
     */
	public int getIndex2() {
		return index2;
	}

	/**
     * Gets the username of student1
     * @return username of student1
     */
	public String getStudent1() {
		return student1;
	}

	/**
     * Gets the username of student2
     * @return username of student2
     */
    public String getStudent2() {
        return student2;
    }

}