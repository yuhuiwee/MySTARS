public abstract class Person {
     protected String username;
     protected String name;
     protected String matricnum;
     protected char gender;
     protected String nationality;
     protected String school;
     protected String email;

     public void setUsername(String username) {
          this.username = username.toLowerCase();
     }

     public void setName(String name) {
          this.name = name.toLowerCase();
     }

     public void setMatric(String matric) {
          this.matricnum = matric.toLowerCase();
     }

     public void setGender(char gender) {
          this.gender = Character.toUpperCase(gender);
     }

     public void setNationality(String nationality) {
          this.nationality = nationality.toLowerCase();
     }

     public void setSchool(String school) {
          this.school = school.toLowerCase();
     }

     public void setEmail(String email) {
          this.email = email.toLowerCase();
     }

     public String getUsername() {
          return username;
     }

     public String getMatric() {
          return matricnum;
     }

     public String getName() {
          return name;
     }

     public char getGender() {
          return gender;
     }

     public String getNationality() {
          return nationality;
     }

     public String getSchool() {
          return school;
     }

     public String getEmail() {
          return email;
     }
}
