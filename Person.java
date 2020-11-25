import java.io.Serializable;

/**
 * Represents the users created and their details in MySTARS
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class Person implements Serializable {
    
    /**
	 * Serialization of the object Person
	 */
	private static final long serialVersionUID = 1L;

    /**
      * This is the user's username
      */
	protected String username;

    /**
      * This is the user's name
      */
	protected String name;

    /**
      * This is the user's matriculation number
      */
	protected String matricnum;

    /**
      * This is the user's gender
      */
	protected char gender;

    /**
      * This is the user's nationality
      */
	protected String nationality;

    /**
      * This is the user's school
      */
	protected String school;

    /**
      * This is the user's email
      */
	protected String email;

    /**
     * A Person constructor
     * @param username This is the user's username
     * @param name This is the user's name
     * @param matric This is the user's matriculation number
     * @param gender This is the user's gender
     * @param nationality This is the user's nationality
     * @param school This is the user's school
     * @param email This is the user's email
     */
	public Person(String username, String name, String matric, char gender, String nationality, String school,
			String email) {
		this.username = username;
		this.name = name;
		this.matricnum = matric;
		this.gender = gender;
		this.nationality = nationality;
		this.school = school;
		this.email = email;
	}

    /**
	  * Setting of the user's username
	  * The string is also set to lower case
	  * @param username This is the user's username
	  */
	public void setUsername(String username) {
		this.username = username.toLowerCase();
	}

    /**
      * Setting of the user's name
      * The string is also set to lower case
      * @param name This is the user's name
      */
	public void setName(String name) {
		this.name = name.toLowerCase();
	}

    /**
      * Setting of the user's matriculation number
      * The string is also set to lower case
      * @param matric This is the user's matriculation number
      */
	public void setMatric(String matric) {
		this.matricnum = matric.toLowerCase();
	}

    /**
      * Setting of the user's gender
      * The string is also set to upper case
      * @param gender This is the user's gender
      */
	public void setGender(char gender) {
		this.gender = Character.toUpperCase(gender);
	}

     /**
      * Setting of the user's nationality
      * The string is also set to lower case
      * @param nationality This is the user's nationality
      */
	public void setNationality(String nationality) {
		this.nationality = nationality.toLowerCase();
	}

    /**
      * Setting of the user's school
      * The string is also set to lower case
      * @param school
      */
	public void setSchool(String school) {
		this.school = school.toLowerCase();
	}

    /**
      * Setting of the user's email
      * The string is also set to lower case
      * @param email This is the user's email
      */
	public void setEmail(String email) {
		this.email = email.toLowerCase();
	}

    /**
      * Get the user's username
      * @return This is the user's username
      */
	public String getUsername() {
		return username;
	}

    /**
     * Get the user's matriculation number
     * @return This is the user's matriculation number
     */
	public String getMatric() {
		return matricnum;
	}

    /**
      * Get the user's name
      * @return This is the user's name
      */
	public String getName() {
		return name;
	}

    /**
     * Get the user's gender
     * @return This is the user's gender
     */
	public char getGender() {
		return gender;
	}

    /**
      * Get the user's nationality
      * @return This is the user's nationality
      */
	public String getNationality() {
		return nationality;
	}

    /**
     * Get the user's school
     * @return This is the user's school
     */
	public String getSchool() {
	    return school;
	}

    /**
     * Get the user's email
     * @return This is the user's email
     */
	public String getEmail() {
		return email;
	}
}
