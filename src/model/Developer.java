package model;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

public class Developer implements Serializable {

    private Long id;
    private String firstName;
    private String lastName;
    private int age;

    private Set<Skill> skills;
    private Account account;

    public Developer(Long id,String firstName,String lastName, int age, Account account)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.account = account;
    }

    public Developer(String firstName,String lastName, int age, Account account)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.account = account;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getAge() {
        return age;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
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
                firstName.equals(developer.firstName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, age);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for(Skill s: skills) {
            builder.append("id: ")
                    .append(s.getID())
                    .append(", name: ")
                    .append(s.getSkillName())
                    .append("\n");
        }
        return
                "first name: " + firstName + "\n" +
                "last name: " + lastName + "\n" +
                "age: " + age + "\n" +
                "skills: " + "\n" + builder.toString();
    }
}
