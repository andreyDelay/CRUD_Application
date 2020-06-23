package controllers;

import model.Skill;
import repositories.skill.SkillsAccess;

public class SkillController {
    private SkillsAccess access = new SkillsAccess();

    public String addSkill(String skillName) {
        String tmp = skillName.trim().replaceAll("[^a-zA-Zа-яА-Я]","");
        if (!(tmp.length() <= 2)) {
            access = new SkillsAccess();
            access.addEntity(new Skill(skillName));
            return "Навык успешно добавлен";
        }
        return "Некорректное имя навыка";
    }

    public void deleteSkill(String skillName) {
        access.deleteByName(skillName);
    }

    public void deleteAll() {
        access.deleteAll();
    }

    public <T extends Number> void deleteSkill(T id) {
        access.deleteByID(Long.parseLong(String.valueOf(id)));
    }

    public void updateSkill(String skillName) {

    }

    public void updateSkill(Long ID) {

    }

}
