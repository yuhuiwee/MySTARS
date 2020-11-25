/**
 * Represents the Admin feature / function of MySTARS
 * The admin is able to add another admin
 * @author Group 02
 * @version 1.0
 * @since 25 November 2020
 */
public class Admin extends Person{
    /**
     * Serialization of the object Admin
     */
	private static final long serialVersionUID = 1L;

    /**
     * Position of the Admin within his registered school
     */
	private String position;

    /**
     * Admin's Constructor
     * @param username This is the Admin's username
     * @param name This is the name of the Admin
     * @param id This is the Admin's ID number
     * @param gender This is the Admin's gender
     * @param nationality This is Admin's nationality
     * @param school This the school the Admin is registered with
     * @param position This is the Admin's position in the school
     * @param email This is the Admin's email
     * @throws UserAlreadyExists This exception is thrown when Admin is trying to add a user whom is already in the database
     */
    public Admin(String username, String name, String id, char gender, String nationality, String school,
            String position, String email) throws UserAlreadyExists {
        super(username.toUpperCase(), name.toUpperCase(), id.toUpperCase(), Character.toUpperCase(gender), nationality.toUpperCase(), school.toUpperCase(), email.toUpperCase());
        this.position = position;
        PasswordHash.addUserPwd(username, username.toUpperCase());
    }

    /**
     * Setting the Admin's position
     * @param position This Admin's position
     */
	public void setPosition(String position) {
		this.position = position;
	}

    /**
     * Retrieve the Admin's position
     * @return This Admin's position
     */
	public String getPosition() {
		return position;
	}

    /**
     * Print out the Admin's details / information
     */
    public void printAdminDetails(){
        System.out.println("\tName: "+this.name);
        System.out.println("\tID: "+this.matricnum);
        System.out.println("\tGender: "+String.valueOf(gender));
        System.out.println("\tNationality: "+this.nationality);
        System.out.println("\tSchool/Department: "+this.school);
        System.out.println("\tPosition: "+this.position);
        System.out.println("\tEmail: " + this.email);

    }
}
