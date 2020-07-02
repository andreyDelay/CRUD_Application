package com.andrey.crud.app;


import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.model.Developer;
import com.andrey.crud.model.Skill;
import com.andrey.crud.repository.IO.AccountRepository;
import com.andrey.crud.repository.IO.DeveloperRepository;
import com.andrey.crud.repository.IO.SkillsRepository;

import java.util.Map;

public class Application {

    public static void main(String[] args) {
        AccountRepository accountRepository = new AccountRepository();
        SkillsRepository skillsRepository = new SkillsRepository();
        DeveloperRepository repository = new DeveloperRepository();
        try {
            Map<Long, Developer> developers = repository.findAll();
            for (Map.Entry<Long,Developer> entry: developers.entrySet()) {
                Developer developer = entry.getValue();
                Map<Long,Skill> skills = skillsRepository.findAll();
                String skillNumbers = developer.getNumbersOfSkills().replaceAll("[^0-9,]","");
                if (skillNumbers.length() > 1) {
                    for (String number : skillNumbers.split(",")) {
                        developer.addSkill(skills.get(Long.parseLong(number)));
                    }
                }
                accountRepository.find(developer.getId()).ifPresent(developer::setAccount);
                System.out.println(developer);
            }

        } catch (ReadFileException e) {

        }



//        AccountController accountController = new AccountController();
//        Scanner scanner = new Scanner(System.in);
//        String accountName="";
//        String logIn ="";
//
//        String option;
//        System.out.println("Добро пожаловать в приложение!");
//        System.out.println();
//        System.out.println();
//
//        do {
//            System.out.println("===============================");
//            System.out.println("Введите номер пункта");
//            System.out.println("1. Войти в систему");
//            System.out.println("0. Выход");
//            System.out.println("===============================");
//            option = scanner.nextLine();
//
//            switch (option) {
//                case "1":
//                    System.out.println("Введите имя аккаунта");
//                    accountName = scanner.nextLine();
//                    logIn = accountController.checkAccountInRepo(accountName);
//                    if (!logIn.equals("Вход выполнен")) {
//                        System.out.println(logIn);
//                    }
//                    break;
//
//                default:
//                    System.out.println("Такого пункта не существует.");
//                    break;
//            }
//        }while(!option.equals("0") && !logIn.equals("Вход выполнен"));
//
//        if (!option.equals("0")) {
//            System.out.println();
//            System.out.println(logIn + "\n");
//
//            DeveloperController controller = new DeveloperController(accountName);
//
//            System.out.println("Ваши данные:");
//            System.out.println("--------------------");
//
//            System.out.println(controller.showCurrentDeveloper());
//            //********************************************************
//            System.out.println("Выберите дальнейшие действия");
//            System.out.println("---------------------------");
//
//            do {
//                System.out.println("Введите интересующий Вас пункт");
//                System.out.println("1. Удалить навык по имени");
//                System.out.println("2. Удалить навык по id");
//                System.out.println("3. Добавить навык");
//                System.out.println("4. Изменить навык");
//                System.out.println("5. Изменить имя");
//                System.out.println("6. Изменить фамилию");
//                System.out.println("7. Показать мои данные");
//                System.out.println("0. Выход");
//                System.out.println();
//                option = scanner.nextLine();
//
//                switch (option) {
//                    case "1":
//                        System.out.println("Введите имя навыка");
//                        System.out.println(
//                        controller.deleteSkill(scanner.nextLine()));
//                        break;
//                    case "2":
//                        System.out.println("Введите номер навыка");
//                        String tmp = scanner.nextLine().replaceAll("[^0-9]","");
//                        if (!(tmp.length() == 0)) {
//                            System.out.println(
//                                    controller.deleteSkill(Long.parseLong(tmp)));
//                        }else {
//                            System.out.println("Некорректное id");
//                        }
//                        break;
//                    case "3":
//                        System.out.println("Введите имя навыка, который хотите добавить");
//                        System.out.println(
//                                controller.addSkill(scanner.nextLine()));
//                        break;
//                    case "4":
//                        System.out.println("Введите старое имя навыка и новое через запятую");
//                        System.out.println(
//                                controller.updateSkill(scanner.nextLine()));
//                        break;
//                    case "5":
//                        System.out.println("Введите новое имя");
//                        System.out.println(controller.updateName(scanner.nextLine()));
//                        break;
//                    case "6":
//                        System.out.println("Введите новую фамилию");
//                        System.out.println(controller.updateLastName(scanner.nextLine()));
//                        break;
//                    case "7":
//                        System.out.println(controller.showCurrentDeveloper());
//                        break;
//                    default:
//                        System.out.println("Ошибка. Выбранный пункт меню не найден");
//                        System.out.println("==============================");
//                        break;
//                }
//
//            }while (!option.equals("0"));
//        }
    }
}