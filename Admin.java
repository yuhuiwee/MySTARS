public class Admin extends Person{
    private String position;
    public Admin(String username, String name, String id, char gender, String nationality, String position){
        this.username = username;
        this.name = name;
        this.matricnum = id;
        this.gender = gender;
        this.nationality = nationality;
        this.position = position;
    }

    public void editAccessPeriod(){

    }

    public void addStudent(String username, String name, String matric, char gender, String nationality, String major, int year)
            throws UserAlreadyExists {
        PersonList.newStudent(username, name, matric, gender, nationality, major, year);
        return;
    }
    public void removeStudent(String username) throws UserNotFound{
        PersonList.removeStudent(username);
    }

    public void editStudent(String username, String name, String matric, char gender, String nationality, String major, int year) throws UserNotFound,
            UserAlreadyExists {
        PersonList.editStudentDetails(username, name, matric, gender, nationality, major, year);
    }

    public void addCourse(){
        //TODO: Add arguments
    }

    public void checkVacancies(int index){
        CourseList.checkVacancies(index);
    }

    public void printByIndex(int index){
        CourseList.printStudent(index);
    }
    public void printByCourse(String coursecode){
        CourseList.printStudent(coursecode);
    }

    public String getMatric(){
        return matricnum;
    }
}

