package com.andrey.crud.view;

import com.andrey.crud.controllers.AccountController;
import com.andrey.crud.controllers.DeveloperController;
import com.andrey.crud.controllers.SkillController;

import java.util.Scanner;

public class MainMenu {
    private DeveloperController developerController = new DeveloperController();
    private AccountController accountController = new AccountController();
    private SkillController skillController = new SkillController();

    public void mainMenu() {
        Scanner scanner = new Scanner(System.in);
        String option;

        System.out.println("Добро пожаловать в приложение!");
        System.out.println("*********************************");
        System.out.println("Список существующих разработчиков:\n");

        System.out.println(developerController.showAllDevelopers());
        System.out.print("имя аккаунта: ");


    }
}
