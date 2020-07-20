package com.andrey.crud.model;
import java.util.Objects;

public class Skill {

    private String skillName;
    private Long id;

    public Skill(Long ID, String skillName)
    {
        this.skillName = skillName;
        this.id = ID;
    }

    public Skill(String skillName) {
        this.skillName = skillName;
    }
    public String getSkillName() {
        return skillName;
    }
    public void setID(Long ID) {
        this.id = ID;
    }
    public Long getID() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Skill skill = (Skill) o;
        return skillName.equals(skill.skillName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skillName);
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id: ").append(id);
        builder.append(", ");
        builder.append("name: ").append(skillName);
        builder.append(";");
        return builder.toString();
    }
}
