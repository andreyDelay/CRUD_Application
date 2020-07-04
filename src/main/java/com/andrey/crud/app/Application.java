package com.andrey.crud.app;

import static com.andrey.crud.utils.ViewHelper.*;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option;

        System.out.println("Добро пожаловать в приложение!");
        System.out.println("*********************************\n");
        System.out.println("Список существующих разработчиков:");
        showAllData();
        showMenu();

        do {
            option = scanner.nextInt();

            switch (option) {
                case 1 :
                    System.out.println("Введите id пользователя");
                    option = readOption(scanner);
                    showDeveloper(option);
                    showSecondaryMenu();
                    break;
                case 2:
                    System.out.println("Введите id аккаунта, который хотите удалить");
                    option = readOption(scanner);
                    removeAccount(option);
                    showMenu();
                    break;
                case 3:
                    addEntity(scanner);
                    showMenu();
                    break;
                case 4:
                    System.out.println("Введите id пользователя, у которого хотите удалить навык");
                    option = readOption(scanner);
                    removeSkillFromDeveloper(option,scanner);
                    showSecondaryMenu();
                    break;
                case 13:
                    System.out.println(111);
                    recoverAccount();
                    showSecondaryMenu();
                    break;
                case 14:
                    showAllData();
                    break;
                case 20:
                    showMenu();
                    option = readOption(scanner);
                    break;
            }

        }while(!(option == 0));
    }
}