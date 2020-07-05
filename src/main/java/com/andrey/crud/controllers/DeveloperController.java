package com.andrey.crud.controllers;

import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;
import com.andrey.crud.model.Account;
import com.andrey.crud.model.Developer;
import com.andrey.crud.model.Skill;
import com.andrey.crud.repository.IO.DeveloperRepository;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class DeveloperController {

    private DeveloperRepository repository = new DeveloperRepository();
    private Developer current;

    public String showAllDevelopers() {
        try {
            Map<Long,Developer> developers = repository.findAll();
            return developers.values().stream()
                    .map(this::developerBuilder)
                    .map(Developer::toString)
                    .collect(Collectors.joining());
        } catch (ReadFileException e) {
            return e.getMessage();
        }
    }

    public String showAllInShortForm() {
        try {
            Map<Long,Developer> developers = repository.findAll();
            return developers.values().stream()
                    .map(developer -> {
                        StringBuilder list = new StringBuilder();
                        list.append("id:=").append(developer.getId()).append(", ")
                                .append("имя:=").append(developer.getFirstName()).append(", ")
                                .append("фамилия:=").append(developer.getLastName());
                        return list.toString();})
                    .collect(Collectors.joining("\n"));

        } catch (ReadFileException e) {
            return e.getMessage();
        }
    }

    public<ID extends Number> String showOneDeveloper(ID developerId) {
        if (!numberChecker(developerId))
            return "Неверный id пользователя";
        try {
            Optional<Developer> result = repository.find(longParser(developerId));
            if (result.isPresent()) {
                current = result.get();
                return "Данные пользователя:\n" + developerBuilder(current);
            }
        } catch (ReadFileException e) {
            return "Не удалось получить данные пользователя";
        }
        return "Пользователь не найден";
    }

    public String createDeveloper(String name, String lastName,String age) {
        if (!(checkDevName(name) || checkDevName(lastName) || numberChecker(age)))
            return "Некорректные данные";

        try {
            current = new Developer(name,lastName,Integer.parseInt(age));
            repository.save(current);
        } catch (WriteFileException | ReadFileException e) {
            return "Ошибка при работе с базой данных." + e.getMessage();
        }
        return "Пользователь добавлен.";
    }

    public<ID extends Number> String deleteDeveloper(ID developerId) throws ReadFileException {
        return "удалить пользователя по id";
    }

    public Developer getCurrentDeveloper() {
        return current;
    }
//????????????????
    public<ID extends Number> String showAllSkills(ID developerId) {
        try {
            Optional<Developer> result = repository.find(longParser(developerId));
            if (!result.isPresent())
                return "Пользователь с таким id не найден";

            current = result.get();
            Set<Skill> skills = current.getSkills();
            if (skills == null || skills.size() == 0)
                return "Список навыков пуст";

            StringBuilder allSkills = new StringBuilder();
            allSkills.append("Список навыков:\n");
            for (Skill s: skills)
                allSkills.append("id:=")
                        .append(s.getID())
                        .append(", имя:=")
                        .append(s.getSkillName())
                        .append(";");

            return allSkills.toString();
        } catch (ReadFileException e) {
            return "Ошибка при работе с базой данных." + e.getMessage();
        }
    }

    public<ID extends Number,S> String addSkillToDeveloper(ID developerId, String skillName) {
        return "навык добавлен";
    }

    public<ID extends Number,ID2 extends Number,S> String updateDeveloperSkill(ID developerID,ID2 skillId,S newSkillName) {
        return "навык у данного разработчика обновлён";
    }

    public <ID1 extends Number,ID2 extends Number>  String removeSkillFromDeveloper(ID1 developerID, ID2 skillId) {
        try {
            Optional<Developer> result = repository.find(longParser(developerID));
            if (!result.isPresent()) {
                return "Пользователь с таким id не найден";
            }
            current = developerBuilder(result.get());
            Set<Skill> oldValue = current.getSkills();
            Set<Skill> newValue = oldValue.stream()
                                        .filter(skill -> !(skill.getID().equals(longParser(skillId))))
                                        .collect(Collectors.toSet());
            if (oldValue.size() == newValue.size())
                return "Данный навык отсутствует у заданного пользователя.";

            current.setSkills(newValue);
            repository.update(current.getId(),current);
        } catch (ReadFileException e) {
            return "не удалось прочитать базу данных." + e.getMessage();
        } catch (WriteFileException e) {
            return "не удалось сохранить изменения.";
        }

        return "навык удалён";
    }

    public<ID extends Number> String removeAllSkillsFromDeveloper(ID developerId) {
        try {
            Optional<Developer> result = repository.find(longParser(developerId));
            if (!result.isPresent())
                return "пользователь с таким id не найден";

                current = result.get();
                current.setNumbersOfSkills("");
                repository.update(current.getId(), current);
        } catch (ReadFileException | WriteFileException e) {
            return "Ошибка при работе с базой данных в методе removeAllSkillsFromDeveloper()";
        }
        return "все навыки удалены";
    }

    public boolean removeSkillFromAll(Skill skill) {
        try {
            Map<Long,Developer> developers = repository.findAll();

                                    developers.values().stream()
                                    .peek(this::developerBuilder)
                                    .map(Developer::getSkills)
                                    .peek(set -> set.remove(skill))
                                    .collect(Collectors.toList());

            return repository.saveAll(new ArrayList<>(developers.values()));
        } catch (ReadFileException | WriteFileException e) {
            System.out.println("Ошибка при работе с базой данных в методе removeSkillFromAll()");
        }
        return false;
    }

    public String showDevelopersWithKeySkillWord(String keyWord) {
        return "все с ключевым словом в навыках";
    }

    public String changeName(String newName) {
        return "Имя изменено";
    }

    public String changeLastName(String newLastName) {
        return "Фамилия изменена";
    }

    private Developer developerBuilder(Developer developer) {

        if (developer.getNumbersOfSkills().contains(",")) {
            SkillController skillController = new SkillController();
            String[] skillNumbers = developer.getNumbersOfSkills().split(",");
            try {
                Map<Long, Skill> allSkills = skillController.allSkills();
                for (int i = 0; i < skillNumbers.length; i++) {
                    Long skillId = Long.parseLong(skillNumbers[i]);
                    Skill skill = allSkills.get(skillId);
                    developer.addSkill(skill);
                }
            } catch (ReadFileException e) {
                System.out.println("Не удалось добавить навыки для пользователя " + developer.getFirstName());
            }
        }

        AccountController accountController = new AccountController();
        try {

            Account account = accountController.accounts().get(developer.getId());
            developer.setAccount(account);
        } catch (ReadFileException e) {
            System.out.println("Не удалось найти аккаунт для пользователя " + developer.getFirstName());
        }
        return developer;
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

    private<L extends Number> Long longParser(L number) {
            return number.longValue();
    }

    private<L extends Number> boolean numberChecker(L number) {
        String tmp = String.valueOf(number).replaceAll("[^0-9]","");
        if (tmp.length() == 0) return false;
        if (tmp.length() != String.valueOf(number).length()) return false;
        return true;
    }

    private<L extends Number> boolean numberChecker(String number) {
        String tmp = String.valueOf(number).replaceAll("[^0-9]","");
        if (tmp.length() == 0) return false;
        if (tmp.length() != String.valueOf(number).length()) return false;
        return true;
    }

}
