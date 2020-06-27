package model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class Developer implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private int age;

    private Set<Skill> skills;
    private Account account;
    private String accountName;

    public Developer(
            Long id, String firstName, String lastName, String dateOfBirth, int age, String accountName)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.age = age;
        this.accountName = accountName;
//        account = new Account(accountName);
    }

    public Developer(Long id, String firstName, int age, String accountName)
    {
        this.id = id;
        this.firstName = firstName;
        this.age = age;
        this.accountName = accountName;
//        account = new Account(accountName);
    }

    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public int getAge() {
        return age;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public Account getAccount() {
        return account;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Developer developer = (Developer) o;
        return age == developer.age &&
                id.equals(developer.id) &&
                firstName.equals(developer.firstName) &&
                accountName.equals(developer.accountName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, age, accountName);
    }

    @Override
    public String toString() {
        return "Developer:\n" +
                "id: " + id + "\n" +
                "firstName: " + firstName + "\n" +
                "age: " + age + "\n" +
                "accountName: " + accountName + "\n" +
                "skills: \n" + skills;
    }
}
