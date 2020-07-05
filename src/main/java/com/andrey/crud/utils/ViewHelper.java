package com.andrey.crud.utils;

import com.andrey.crud.controllers.AccountController;
import com.andrey.crud.controllers.DeveloperController;
import com.andrey.crud.controllers.SkillController;
import com.andrey.crud.model.Account;

import java.util.Map;
import java.util.Scanner;

public class ViewHelper {
    private static DeveloperController developerController = new DeveloperController();
    private static AccountController accountController = new AccountController();
    private static SkillController skillController = new SkillController();

    public static void showMenu() {
        System.out.println("===============================");
        System.out.println("Введите номер операции, которую хотите совершить");
        System.out.println("1. Показать подробные данные одного пользователя по id");
        System.out.println("2. Удалить аккаунт");
        System.out.println("3. Добавить пользователя");
        System.out.println("4. Удалить навык у пользователя");
        System.out.println("5. Удалить все навыки у пользователя");
        System.out.println("6. Удалить заданный навык у всех");
        System.out.println();
        System.out.println("7. Заблокировать аккаунт");
        System.out.println("8. Список активных аккаунтов");
        System.out.println("9. Список удалённых аккаунтов");
        System.out.println("10. Список заблокированных аккаунтов");
        System.out.println();
        System.out.println("11. Список пользователей с похожим навыком");
        System.out.println("12. Восстановить аккаунт");
        System.out.println("13. Все данные");
        System.out.println("14. Добавить навык");
        System.out.println();
        System.out.println("0. Выход");
        System.out.println("===============================");
    }

    public static void showSecondaryMenu() {
        System.out.println("===============================");
        System.out.println("Введите номер операции, которую хотите совершить");
        System.out.println("1. Показать подробные данные");
        System.out.println("2. Удалить аккаунт");
        System.out.println("4. Удалить навык у пользователя");
        System.out.println("5. Удалить все навыки у пользователя");
        System.out.println("7. Заблокировать аккаунт");
        System.out.println("12. Восстановить аккаунт");
        System.out.println("13. Все данные");
        System.out.println("20. Полный список меню");
        System.out.println("0. Выход");
        System.out.println("===============================");
    }

    public static int readOption(Scanner scanner) {
        int option;
        try {
            option = scanner.nextInt();
            return option;
        } catch (Exception e) {
            System.out.println("Введите номер так, как он указан в списке меню.");
            scanner.nextLine();
            option = readOption(scanner);
            return option;
        }
    }

    public static void showAllInShort() {
        System.out.println(developerController.showAllInShortForm());
    }

    public static void showAllData() {
        System.out.println(developerController.showAllDevelopers());
    }

    public static void showDeveloper(int id) {
        System.out.println(developerController.showOneDeveloper(id));
    }

    public static void removeAccount(int id) {
        System.out.println(accountController.deleteAccount(id));
    }

    public static void recoverAccount() {
        Long id = developerController.getCurrentDeveloper().getId();
        System.out.println(accountController.recoverAccount(id));
    }

    public static void addEntity(Scanner scanner) {
        System.out.println("Введите данные нового пользователя в формате:");
        System.out.println("Имя аккаунта, имя, фамилия, возраст");
        
        String option;
        scanner.nextLine();
        option = scanner.nextLine();

        String [] data = option.split(",");
        for (int i = 0; i < data.length; i++) {
            if (data[i].length() == 0) {
                System.out.println("Некорректные данные");
                return;
            }
        }
        String result = accountController.createAccount(data[0]);
        if (!result.equals("Аккаунт сохранён.")) {
            System.out.println(result);
            return;
        }
        System.out.println(
        developerController.createDeveloper(data[1],data[2],data[3]));
        System.out.println(developerController.showOneDeveloper(
                            developerController.getCurrentDeveloper().getId()));
    }

    public static void removeSkillFromDeveloper(int id, Scanner scanner) {
        System.out.println("Введите номер навыка, который нужно удалить");
        int skillId = readOption(scanner);
        System.out.println(developerController.removeSkillFromDeveloper(id, skillId));
    }

    public static void removeSkillsForOneDeveloper(int id) {
        System.out.println(developerController.removeAllSkillsFromDeveloper(id));
    }

    public static void removeSkillFromAll(int id) {
        System.out.println(skillController.removeSkillFromAll(id));
    }

    public static void blockAccount(int id) {
        System.out.println(accountController.blockAccount(id));
    }

    public static void allActive() {
        System.out.println(accountController.showAllActive());
    }

    public static void  allDeleted() {
        System.out.println(accountController.showDeleted());
    }

    public static void  allBlocked() {
        System.out.println(accountController.showBlocked());
    }

    public static void showWithSimilarSkill(Scanner scanner) {
        System.out.println("Введите ключевое слово для поиска пользователей с похожим навыком");
        String key;
        key = scanner.nextLine();
    }

    public static void addSkillToDeveloper(Scanner scanner) {
        int id;
        String skill;
        System.out.println("Введите id аккаунта");
        id = readOption(scanner);
        System.out.println("Введите имя навыка");
        skill = scanner.nextLine();

    }
}
