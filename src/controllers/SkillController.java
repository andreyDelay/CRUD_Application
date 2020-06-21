package controllers;


import model.Skill;
import repositories.skill.SkillsAccess;

import java.util.Arrays;

public class SkillController {
    private Skill currentSkill;

    public void addSkill(String str) {
        String s [] = str.split("\\|");
        System.out.println(Arrays.toString(s));
        if (s.length > 1) {
            currentSkill = new Skill(s[0], s[1]);
        } else if (s.length == 1)
            currentSkill = new Skill(s[0]);

        SkillsAccess access = new SkillsAccess();
        access.addEntity(currentSkill);
    }

    public void deleteSkill(String skillName) {

    }

    public void deleteSkill(Long skillID) {

    }

    public void updateSkill(String skillName) {

    }

    public void updateSkill(Long ID) {

    }

}
