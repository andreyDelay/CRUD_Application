package com.andrey.crud.app;

import static com.andrey.crud.utils.ViewHelper.*;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option = -1;

        System.out.println("Добро пожаловать в приложение!");
        System.out.println("*********************************\n");
        System.out.println("Короткий список данных:");
        showAllInShort();
        showMenu();

        do {
            try {
                option = scanner.nextInt();
            } catch (Exception e) {
                System.out.println("Введите номер так, как он указан в списке меню.");
                scanner.nextLine();
                option = scanner.nextInt();
            }

            switch (option) {
                case 1 :
                    System.out.println("Список существующих разработчиков:");
                    showAllData();
                    showMenu();
                    break;
                case 2:
                    System.out.println("Введите id пользователя");
                    option = readOption(scanner);
                    showDeveloper(option);
                    showSecondaryMenu();
                    break;
                case 3:
                    addEntity(scanner);
                    showMenu();
                    break;
                case 4:
                    System.out.println("Введите id пользователя, которого хотите удалить");
                    option = readOption(scanner);
                    deleteDeveloper(option);
                    showMenu();
                    break;
                case 5:
                    System.out.println("Введите id аккаунта, который хотите удалить");
                    option = readOption(scanner);
                    deleteAccount(option);
                    showMenu();
                    break;
                case 6:
                    System.out.println("Введите id аккаунта, который хотите заблокировать");
                    option = readOption(scanner);
                    blockAccount(option);
                    showMenu();
                    break;
                case 7:
                    System.out.println("Введите id аккаунта,который хотите восстановить");
                    option = readOption(scanner);
                    recoverAccount(option);
                    showSecondaryMenu();
                    break;
                case 8:
                    allActive();
                    showMenu();
                    break;
                case 9:
                    allDeleted();
                    showMenu();
                    break;
                case 10:
                    allBlocked();
                    showMenu();
                    break;
                case 11:
                    showAllExistingSkills();
                    showMenu();
                    break;
                case 12:
                    System.out.println("Введите id навыка,который хотите удалить");
                    option = readOption(scanner);
                    removeSkillFromAll(option);
                    showMenu();
                    break;
                case 13:
                    showWithSimilarSkill(scanner);
                    showMenu();
                    break;
                case 20:
                    changeName(scanner);
                    showSecondaryMenu();
                    break;
                case 21:
                    changeLastName(scanner);
                    showSecondaryMenu();
                    break;
                case 22:
                    addSkillToDeveloper(scanner);
                    showSecondaryMenu();
                    break;
                case 30:
                    showMenu();
                    break;
            }

        }while(!(option == 0));
    }
}