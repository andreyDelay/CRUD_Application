package app;

import model.Skill;
import repositories.skill.SkillsAccess;
import view.SkillV;

import java.util.Scanner;


public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        SkillV viewer = new SkillV();
        viewer.startSkillViewer(scanner);
        SkillsAccess access = new SkillsAccess();
        Skill s = access.getByID(3L);
        System.out.println(s.toString());

    }
}