package com.andrey.crud.app;

import static com.andrey.crud.utils.ViewHelper.*;

import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int option = -1;

        System.out.println("Добро пожаловать в приложение!");
        System.out.println("*********************************\n");
        System.out.println("Список существующих разработчиков:");
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
                case 5:
                    System.out.println("Введите id пользователя");
                    option = readOption(scanner);
                    removeSkillsForOneDeveloper(option);
                    showSecondaryMenu();
                    break;
                case 6:
                    System.out.println("Введите id навыка(данный навык будет удалён у всех)");
                    option = readOption(scanner);
                    removeSkillFromAll(option);
                    showMenu();
                    break;
                case 7:
                    System.out.println("Введите id аккаунта,который необходимо заблокировать");
                    option = readOption(scanner);
                    blockAccount(option);
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
                    showWithSimilarSkill(scanner);
                    showMenu();
                    break;
                case 12:
                    System.out.println("Введите id аккаунта, который нужно восстановить");
                    option = readOption(scanner);
                    recoverAccount();
                    showSecondaryMenu();
                    break;
                case 13:
                    showAllData();
                    showMenu();
                    break;
                case 14:

                    break;
                case 20:
                    showMenu();
                    break;
            }

        }while(!(option == 0));
    }
}