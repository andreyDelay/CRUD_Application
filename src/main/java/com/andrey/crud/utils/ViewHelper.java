package com.andrey.crud.utils;

import com.andrey.crud.controllers.AccountController;
import com.andrey.crud.controllers.DeveloperController;
import com.andrey.crud.controllers.SkillController;

import java.util.Scanner;

public class ViewHelper {
    private static DeveloperController developerController = new DeveloperController();
    private static AccountController accountController = new AccountController();
    private static SkillController skillController = new SkillController();

    public static void showMenu() {
        System.out.println("===============================");
        System.out.println("Введите номер операции, которую хотите совершить");
        System.out.println("1. Показать данные одного аккаунта по id");
        System.out.println("2. Удалить аккаунт");
        System.out.println("3. Добавить пользователя");
        System.out.println("4. Удалить навык у пользователя");
        System.out.println("5. Удалить все навыки у пользователя");
        System.out.println("6. Удалить заданный навык у всех");
        System.out.println("7. Добавить заданный навык всем");
        System.out.println();
        System.out.println("8. Заблокировать аккаунт");
        System.out.println("9. Список активных аккаунтов");
        System.out.println("10. Список удалённых аккаунтов");
        System.out.println("11. Список заблокированных аккаунтов");
        System.out.println();
        System.out.println("12. Список пользователей с похожим навыком");
        System.out.println("13. Восстановить аккаунт");
        System.out.println("14. Все данные");
        System.out.println();
        System.out.println("0. Выход");
        System.out.println("===============================");
    }

    public static void showSecondaryMenu() {
        System.out.println("===============================");
        System.out.println("Введите номер операции, которую хотите совершить");
        System.out.println("2. Удалить аккаунт");
        System.out.println("4. Удалить навык у пользователя");
        System.out.println("5. Удалить все навыки у пользователя");
        System.out.println("8. Заблокировать аккаунт");
        System.out.println("13. Восстановить аккаунт");
        System.out.println("14. Все данные");
        System.out.println("20. Полный список меню");
        System.out.println("0. Выход");
        System.out.println("===============================");
    }

    public static int readOption(Scanner scanner) {
        try {
            int option = scanner.nextInt();
            return option;
        } catch (Exception e) {
            System.out.println("Введите номер так, как он указан в списке меню.");
        }
        return -1;
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
}
