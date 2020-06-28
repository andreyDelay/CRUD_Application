package controllers;

import exeptions.AddDeveloperException;
import model.Developer;
import model.Skill;
import repositories.DevelopersAccess;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


public class DeveloperController {
    /**
     * Поле для хранения класса контроллер
     */
    private DevelopersAccess access;
    /**
     * Поле для хранения класса SkillController
     * для работы с объектами данных типа Skill
     */
    private SkillController controller;
    /**
     * Текущий Developer для которого был выполнен вход в систему
     */
    private Developer currentDeveloper;

    public DeveloperController(String accountName) {
        try {
            access = new DevelopersAccess(accountName);
        } catch (AddDeveloperException e) {
            System.out.println("Не удалось войти в систему");
        }
    }

    /**
     * Метод отображает данные о текущем пользователе
     * @return - строковое представление пользователя
     */
    public String showCurrentDeveloper() {
        if (controller == null)
            controller = new SkillController();

        Set<Skill> allSkills =controller.skills()
                                        .entrySet()
                                        .stream()
                                        .map(Map.Entry::getValue)
                                        .collect(Collectors.toSet());

        Map<Long, Developer> devS = access.findAll();
        Optional<Developer> result = devS.entrySet().stream()
                                    .map(Map.Entry::getValue)
                                    .findFirst();
        if (result.isPresent()) {
            currentDeveloper = result.get();
            currentDeveloper.setSkills(allSkills);
            return currentDeveloper.toString();
        }

        return "Не удалось найти данные для этого аккаунта";
    }

    /**
     * Метод изменяет имя пользователя и сохраняет данные
     * в репозитории
     * @param newName - новое имя пользователя
     * @return - строковый результат работы метода
     */
    public String updateName(String newName) {
        if (!checkDevName(newName))
            return "Некорректное имя";

        currentDeveloper.setFirstName(newName);
        try {
            access.save(currentDeveloper);
        } catch (Exception e) {
            System.out.println("Не удалось созранить данные в базе.");
        }
        return "Имя изменено";
    }

    /**
     * Метод изменяет фамилию пользователя и сохраняет данные
     * @param newLastName - новая фамилия
     * @return - строковый результат работы метода
     */
    public String updateLastName(String newLastName) {
        if (!checkDevName(newLastName))
            return "Некорректная фамилия";

        currentDeveloper.setLastName(newLastName);
        try {
            access.save(currentDeveloper);
        } catch (Exception e) {
            System.out.println("Не удалось созранить данные в базе.");
        }
        return "Фамилия изменена";
    }

    /**
     * Метод удаляет навык из списка навыков класса Developer
     * @param id - идентификатор навыка, который необходимо удалить
     * @return - строковый результат работы метода
     */
    public String deleteSkill(Long id) {
        return controller.deleteSkill(id);
    }

    /**
     * Метод удаляет навык из списка навыков класса Developer
     * @param - имя навыка, который необходимо удалить
     * @return - строковый результат работы метода
     */
    public String deleteSkill(String skillName) {
        return controller.deleteSkill(skillName);
    }

    /**
     * Метод добавляет навык в список навыков текущего класса Developer
     * @param skillName - имя новго навыка, который будет добавлен
     * @return - строковый результат работы метода
     */
    public String addSkill(String skillName) {
        return controller.save(skillName);
    }

    /**
     * Метод изменяет название навыка текущего класса Developer
     * @param names - имя тарого и нового навыка
     * @return - строковый результат работы метода
     */
    public String updateSkill(String names) {
        String [] checkNames = names.split(",");
        if (checkNames.length != 2) return "Некорректные данные";
        return controller.updateSkill(checkNames[0], checkNames[1]);
    }

    /**
     * Метод проверяет корректность переданного имени
     * @param name - имя для дальнейшей обработки
     * @return - true если имя прошло проверку
     */
    private boolean checkDevName(String name) {
        String tmp = name.trim();
        if (tmp.length() == 0) return false;
        return tmp.replaceAll("[^a-zA-Zа-яА-Я]", "").length() != 0;
    }

}
