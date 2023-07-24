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













}
