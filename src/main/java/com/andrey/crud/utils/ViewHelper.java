package com.andrey.crud.utils;

import com.andrey.crud.controllers.AccountController;
import com.andrey.crud.controllers.DeveloperController;
import com.andrey.crud.controllers.SkillController;
import com.andrey.crud.model.Account;
import com.andrey.crud.model.AccountStatus;
import com.andrey.crud.model.Developer;
import com.andrey.crud.model.Skill;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.w3c.dom.ls.LSOutput;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ViewHelper {
    private static DeveloperController developerController = new DeveloperController();
    private static AccountController accountController = new AccountController();
    private static SkillController skillController = new SkillController();

    public static void showMenu() {
        System.out.println("===============================");
        System.out.println("Введите номер операции, которую хотите совершить");
        System.out.println("1. Показать подробные данные всех аккаунтов");
        System.out.println("2. Показать подробные данные одного аккаунта по id");
        System.out.println("3. Добавить пользователя");
        System.out.println("4. Удалить пользователя");
        System.out.println();
        System.out.println("5. Удалить аккаунт");
        System.out.println("6. Заблокировать аккаунт");
        System.out.println("7. Восстановить аккаунт");
        System.out.println("8. Список активных аккаунтов");
        System.out.println("9. Список удалённых аккаунтов");
        System.out.println("10. Список заблокированных аккаунтов");
        System.out.println();
        System.out.println("11. Список существующих навыков");
        System.out.println("12. Удалить заданный навык у всех");
        System.out.println("13. Список пользователей с ключевым словом навыка");
        System.out.println("0. Выход");
        System.out.println("===============================");
    }

    public static void showSecondaryMenu() {
        System.out.println("===============================");
        System.out.println("Введите номер операции, которую хотите совершить");
        System.out.println("20. Изменить имя");
        System.out.println("21. Изменить фамилию");
        System.out.println("22. Добавить навык");
        System.out.println("23. Удалить навык");
        System.out.println("24. Удалить все навыки");
        System.out.println("25. Показать данные другого аккаунта");
        System.out.println();
        System.out.println("30. Полный список меню");
        System.out.println("0. Выход");
        System.out.println("===============================");
    }

    public static void showAllInShort() {
        System.out.println(developerController.showAllInShortForm());
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


//1.
    public static void showAllData() {
        System.out.println(developerController.showAllDevelopers());
    }
//2.
    public static void showDeveloper(int id) {
        Developer developer = developerController.showOneDeveloper(id);
        if (developer != null) {
            System.out.println("Данные пользователя:\n");
            System.out.println(developer.toString());
            return;
        }
        System.out.println("Нет данных");
    }
//3.
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
        Account account = accountController.createAccount(data[0]);
        if (account == null) {
            System.out.println("Не удалось создать и сохранить аккаунт");
            return;
        }
        Developer developer = developerController.createDeveloper(data[1],data[2],data[3]);
        if (developer != null) {
            developer.setAccount(account);
            System.out.println("Пользователь добавлен.");
            System.out.println("Данные нового пользователя:");
            System.out.println(developer.toString());
            return;
        }
        System.out.println("Пользователь не создан.");
    }
//4.
    public static void deleteDeveloper(int id) {
        Developer developer = developerController.deleteDeveloper(id);
        if (developer != null) {
            System.out.println("Пользователь " +
                                developer.getLastName() + " " +
                                developer.getLastName() + " " +
                                "удалён");
            return;
        }
        System.out.println("Операция не выполнена");
    }
//5.
    public static void deleteAccount(int id) {
        Account account = accountController.changeAccountStatus(id, AccountStatus.DELETED);
        if (account != null) {
            System.out.println("Аккаунт успешно удалён");
            System.out.println(account.toString());
            return;
        }
        System.out.println("Не удалось удалить.");
    }
//6.
    public static void blockAccount(int id) {
        Account account = accountController.changeAccountStatus(id, AccountStatus.BANNED);
        if (account != null) {
            System.out.println("Аккаунт успешно заблокирован");
            System.out.println(account.toString());
            return;
        }
        System.out.println("Не удалось заблокировать.");
    }
//7.
    public static void recoverAccount(int id) {
        Account account = accountController.changeAccountStatus(id, AccountStatus.ACTIVE);
        if (account != null) {
            System.out.println("Аккаунт успешно восстановлен");
            System.out.println("Данные аккаунта:\n");
            Developer developer = developerController.showOneDeveloper(account.getId());
            if (developer != null) {
                System.out.println(developer.toString());
            } else {
                System.out.println(account.toString());
            }
            return;
        }
        System.out.println("Операция не выполнена.");
    }
//8.
    public static void allActive() {
        System.out.println(accountController.showAccountWithRequiredStatus(AccountStatus.ACTIVE));
    }
//9.
    public static void allDeleted() {
    System.out.println(accountController.showAccountWithRequiredStatus(AccountStatus.DELETED));
    }
//10.
    public static void  allBlocked() {
        System.out.println(accountController.showAccountWithRequiredStatus(AccountStatus.BANNED));
    }
//11.
    public static void showAllExistingSkills() {
        System.out.println(skillController.showAllSkills());
    }
//12.
    public static void removeSkillFromAll(int id) {
        int index = skillController.removeSkillFromAll(id);
        if (index == 0) {
            System.out.println("Навык не был найден ни у одного пользователя.");
        } else {
            System.out.println("Навык удалён у " + index + " пользователя(ей)");
        }
    }
//13.
    public static void showWithSimilarSkill(Scanner scanner) {
        System.out.println("Введите ключевое слово для поиска пользователей с похожим навыком");
        String key;
        key = scanner.nextLine();
        List<Developer> result = developerController.showDevelopersWithKeySkillWord(key);
        if (result.size() == 0) {
            System.out.println("Не найдено ни одного пользователя с похожим навыком.");
            return;
        }
        System.out.println("Список пользователей с похожим навыком:");
        System.out.println(result.stream().map(Developer::toString).collect(Collectors.joining()));
    }
//20.
    public static void changeName(Scanner scanner) {
        System.out.println("Введите новое имя пользователя:");
        scanner.nextLine();
        String name = scanner.nextLine();
        Developer developer = developerController.changeName(name);
        if (developer != null) {
            System.out.println("Данные изменены");
            System.out.println(developer.toString());
            return;
        }
        System.out.println("Не удалось изменить имя.");
    }
//21.
    public static void changeLastName(Scanner scanner) {
        System.out.println("Введите новую фамилию пользователя:");
        scanner.nextLine();
        String lastName = scanner.nextLine();
        Developer developer = developerController.changeLastName(lastName);
        if (developer != null) {
            System.out.println("Данные изменены");
            System.out.println(developer.toString());
            return;
        }
        System.out.println("Не удалось изменить фамилию.");
    }
//22.
    public static void addSkillToDeveloper(Scanner scanner) {
        String name;
        System.out.println("Введите имя навыка");
        scanner.nextLine();
        name = scanner.nextLine();
        Skill newSkill = skillController.saveSkill(name);
        if (newSkill != null) {
            Developer developer = developerController.getCurrentDeveloper();
            if (developer != null) {
                developer = developerController.addSkillToDeveloper(newSkill);
                System.out.println("Навык добавлен");
                System.out.println(developer.toString());
                return;
            }
        }
        System.out.println("Операция не выполнена");
    }
//23.
    public static void removeSkillFromDeveloper(Scanner scanner) {
        System.out.println("Введите id навыка, который нужно удалить");
        int skillId = readOption(scanner);
        Developer developer = developerController.removeSkillFromDeveloper(skillId);
        if (developer != null) {
            System.out.println("Навык успешно удалён");
            System.out.println(developer.toString());
            return;
        }
        System.out.println("Операция не выполнена");
    }
//24.
    public static void removeSkillsForOneDeveloper() {
        Developer developer = developerController.removeAllSkillsFromDeveloper();
        if (developer != null) {
            System.out.println("Навыки удалены");
            System.out.println(developer.toString());
            return;
        }
        System.out.println("Операция не выполнена.");
    }





}
