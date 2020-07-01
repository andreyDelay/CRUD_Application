package com.andrey.crud.controllers;

import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.model.Developer;
import com.andrey.crud.repository.IO.DeveloperRepository;

import java.util.Map;
import java.util.Optional;

public class DeveloperController {

    private DeveloperRepository repository = new DeveloperRepository();

    private Developer current;
    //TODO определиться что делать с исключением
    public Developer find(String name) throws ReadFileException {
        Map<Long,Developer> developers = findAll();
        Optional<Developer> required = developers.values().stream()
                                    .filter(object -> object.getFirstName().equalsIgnoreCase(name))
                                    .findFirst();
        return required.orElse(null);
    }

    public Developer find(Long id) throws ReadFileException {
        Optional<Developer> result = repository.find(id);
        return result.orElse(null);
    }

    public Map<Long,Developer> findAll() throws ReadFileException {
        return repository.findAll();
    }

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

    private void fillWithSkills(Developer developer) {
        SkillController skillController = new SkillController();
        //TODO
    }

    private void fillWithSkills(Map<Long,Developer> developers) {
        SkillController skillController = new SkillController();
        //TODO
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
