package app;

import controllers.SkillController;
import exeptions.SkillException;

public class Application {
        private static SkillController controller;
    public static void main(String[] args) throws SkillException {
        controller = new SkillController();
        System.out.println(controller.deleteSkill(5L));
        System.out.println(controller.showAllSkills());
        System.out.println(controller.save("drink alcohol"));
        System.out.println(controller.showAllSkills());


    }
}