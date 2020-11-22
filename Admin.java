public class Admin extends Person{
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String position;

    public Admin(String username, String name, String id, char gender, String nationality, String school,
            String position, String email) throws UserAlreadyExists {
        super(username.toUpperCase(), name.toUpperCase(), id.toUpperCase(), Character.toUpperCase(gender), nationality.toUpperCase(), school.toUpperCase(), email.toUpperCase());
        this.position = position;
        PasswordHash.addUserPwd(username, username.toUpperCase());
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getPosition() {
        return position;
    }

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
