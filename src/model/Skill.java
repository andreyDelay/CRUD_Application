package model;
import java.io.Serializable;
import java.util.Objects;

public class Skill implements Serializable {

    private String skillName;
    private Long ID;

    public Skill(String skillName, Long ID)
    {
        this.skillName = skillName;
        this.ID = ID;
    }

    public Skill(String skillName) {
        this.skillName = skillName;
    }
    public String getSkillName() {
        return skillName;
    }
    public Long getID() {
        return ID;
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
        builder.append("id: ").append(ID);
        builder.append(", ");
        builder.append("name: ").append(skillName);
        builder.append(";");
        return builder.toString();
    }
}
