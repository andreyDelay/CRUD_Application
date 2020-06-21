package model;
import java.io.Serializable;
import java.util.Objects;

public class Skill implements Serializable {

    String skillName;
    String skillDescription;
    private Long ID;

    public Skill(String skillName, String skillDescription, Long ID) {
        this.skillName = skillName;
        this.skillDescription = skillDescription;
        this.ID = ID;
    }

    public Skill(String skillName, String skillDescription) {
        this.skillName = skillName;
        this.skillDescription = skillDescription;
        this.ID = ID;
    }

    public Skill(String skillName, Long ID) {
        this.skillName = skillName;
    }

    public Skill(String skillName) {
        this.skillName = skillName;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setID(Long ID) {
        this.ID = ID;
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
        builder.append("===\n");
        builder.append("ID: ").append(ID).append("\n");
        builder.append("skill name: ").append(skillName).append("\n");
        builder.append("description: ").append(skillDescription).append("\n");
        return builder.toString();
    }
}
