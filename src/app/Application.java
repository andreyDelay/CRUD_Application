package app;

import controllers.SkillController;

public class Application {

    public static void main(String[] args) {
        SkillController controller = new SkillController();
        System.out.println(controller.addSkill("Another one"));
//        controller.deleteSkill(3);
    }
}