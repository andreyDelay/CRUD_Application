package app;

import controllers.SkillController;
import exeptions.AddSkillException;

public class Application {

    public static void main(String[] args) throws AddSkillException {
        SkillController controller = new SkillController();
        System.out.println(controller.addSkill("SKILL"));
//        controller.deleteSkill(1);
//        controller.deleteSkill("SKILL");
    }
}