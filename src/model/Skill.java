package model;
import java.io.Serializable;
import java.util.Objects;

public class Skill implements Serializable {
    String skillName;
    String skillDescription;
    String privacyStrength;

    public Skill(String skillName, String skillDescription, SkillStrength ps) {
        this.skillName = skillName;
        this.skillDescription = skillDescription;
        this.privacyStrength = ps.getStatus();
    }

    public Skill(String skillName, SkillStrength ps) {
        this.skillName = skillName;
        this.privacyStrength = ps.getStatus();
    }

    public String getSkillName() {
        return skillName;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public String privacyStrength() {
        return privacyStrength;
    }

    public void setPrivacyStrength(SkillStrength ps) {
        this.privacyStrength = ps.getStatus();
    }

    enum SkillStrength {
            LEARNING("in learning process of the skill"),
            BEGINNER("beginning level qualification"),
            MIDDLE("middle experience in skill"),
            GURU("knows everything about the skill");

            private String status;
        SkillStrength(String status) {
                this.status = status;
            }

            public String getStatus() {
                return status;
            }
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
}
