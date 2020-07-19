package com.andrey.crud.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Developer {

    private Long id;
    private String firstName;
    private String lastName;
    private int age;

    private Set<Skill> skills = new HashSet<>();
    private Account account;

    public Developer(Long id,String firstName,String lastName, int age, Set<Skill> skills, Account account)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.skills = skills;
        this.account = account;
    }

    public Developer(String firstName,String lastName, int age)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public void setAccount(Account account) {
        this.account = account;
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
    public Account getAccount() {
        return account;
    }

    public Set<Skill> getSkills() {
        return skills;
    }
    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public void addSkill(Skill skill) {
        skills.add(skill);
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
        StringBuilder developer = new StringBuilder();
        String topLine = "-----------------------\n";
                developer
                        .append(topLine)
                        .append("id пользователя: ").append(id).append("\n")
                        .append("имя: ").append(firstName).append("\n")
                        .append("фамилия: ").append(lastName).append("\n")
                        .append("возраст: ").append(age).append("\n")
                        .append("имя аккаунта: ").append(account.getAccountName())
                        .append(", статус - ").append(account.getStatus().toString()).append("\n");
                if (skills != null && skills.size() != 0) {
                    developer.append("Список навыков: \n");
                    for(Skill s: skills) {
                        developer.append("id:=")
                                .append(s.getID())
                                .append(", название:=")
                                .append(s.getSkillName())
                                .append(";\n");
                    }
                } else {
                    developer.append("Список навыков пуст.\n");
                }

        return developer.toString();
    }
}
