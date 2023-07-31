import java.time.LocalDate;

public class User {
    String username;
    String password;
    String firstName;
    String lastName;
    String address;
    String occupation;
    String ssn;
    String creditCard;
    LocalDate birthDate;


    public User () {
    }

    public User(String username, String password, String firstName, String lastName, String address, String occupation,
                String ssn, String creditCard, String birthDate) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.occupation = occupation;
        this.ssn = ssn;
        this.creditCard = creditCard;
        this.birthDate = LocalDate.parse(birthDate);
    }

    public String sqlInsertString(){
        return "('" + username + "', '" + password + "', '" + firstName + "', '" + lastName + "', '" + address + "', '"
                + occupation + "', '" + ssn + "', '" + creditCard + "', ?);";
    }

    public String validateData(){
        // If any of the fields are empty, return false
        if (username.equals("") || password.equals("") || firstName.equals("") || lastName.equals("") || address.equals("")
                || occupation.equals("") || ssn.equals("")){
            return "All fields must be filled out, do not leave any fields blank (except credit card).";
        }
        // If any of the fields are too long, return false
        if (username.length() > 16 || password.length() > 16 || firstName.length() > 15 || lastName.length() > 15 || address.length() > 25
                || occupation.length() > 20 || ssn.length() > 9 || creditCard.length() > 16){
            return "One or more fields are too long.";
        }

        return "pass";
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }


}
