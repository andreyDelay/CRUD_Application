package com.andrey.crud.controllers;

import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;
import com.andrey.crud.model.Account;
import com.andrey.crud.model.Developer;
import com.andrey.crud.model.Skill;
import com.andrey.crud.repository.IO.DeveloperRepository;

import java.util.*;
import java.util.stream.Collectors;

public class DeveloperController {

    private DeveloperRepository repository = new DeveloperRepository();
    private Developer current;

    public Developer getCurrentDeveloper() {
        return current;
    }

    public Developer createDeveloper(String name, String lastName,String age) {
        if (!(checkDevName(name) || checkDevName(lastName) || numberChecker(age)))
            return null;

        try {
            current = new Developer(name,lastName,Integer.parseInt(age));
            return repository.save(current);
        } catch (ReadFileException e) {
            System.out.println("Ошибка при чтении базы данных." + e.getMessage());
        } catch (WriteFileException e) {
            System.out.println("Ошибка при записи в базу данных." + e.getMessage());
        }
        return null;
    }

    public<ID extends Number> Developer deleteDeveloper(ID developerId) {
        try {
            current = repository.delete(longParser(developerId));

            AccountController accountController = new AccountController();
            Account account = accountController.deleteAccount(current.getAccount());
            if (account == null) {
                System.out.println("Аккаунт данного пользователя не был удалён из БД");
            }

            if (current != null)
                return current;
        } catch (WriteFileException e) {
            System.out.println("Ошибка записи изменений в базу данных.");
        } catch (ReadFileException e) {
            System.out.println("Ошибка чтения базы данных.");
        }
        return null;
    }

    public String showAllDevelopers() {
        try {
            Map<Long,Developer> developers = repository.findAll();
            return developers.values().stream()
                    .map(this::developerBuilder)
                    .filter(Objects::nonNull)
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
                    .map(this::developerBuilder)
                    .filter(Objects::nonNull)
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

    public<ID extends Number> Developer showOneDeveloper(ID developerId) {
        if (!numberChecker(developerId)) {
            System.out.println("Неверный id пользователя");
            return null;
        }
        try {
            Optional<Developer> result = repository.find(longParser(developerId));
            if (result.isPresent()) {
                current = result.get();
                return developerBuilder(current);
            }
        } catch (ReadFileException e) {
            System.out.println("Ошибка чтения базы данных.");
        }
        return null;
    }

    public Developer addSkillToDeveloper(Skill newSkill) {
        Set<Skill> skills = current.getSkills();
        if (skills.size() != 0) {
            for (Skill s: skills)
                if (s.getID().equals(newSkill.getID())) return null;
        }
        StringBuilder skillNumbers = new StringBuilder(current.getNumbersOfSkills());
        skillNumbers.append(newSkill.getID()).append(",");
        current.setNumbersOfSkills(skillNumbers.toString());

        current.addSkill(newSkill);
        return current;
    }

    public <ID extends Number>  Developer removeSkillFromDeveloper(ID skillId) {
        try {
            StringBuilder skillNumbers = new StringBuilder();
            Skill removed = null;
            Set<Skill> skills = current.getSkills();
            for (Skill s: skills) {
                if (!s.getID().equals(longParser(skillId))) {
                    skillNumbers.append(s.getID()).append(",");
                } else {
                    removed = s;
                }
            }

            if (removed == null) {
                return null;
            }
            skills.remove(removed);
            current.setSkills(skills);
            current = repository.update(current.getId(),current);
            return current;
        } catch (ReadFileException e) {
            System.out.println("Ошибка чтения базы данных." + e.getMessage());
        } catch (WriteFileException e) {
            System.out.println("Ошибка записи в базу данных." + e.getMessage());
        }
        return null;
    }

    public Developer removeAllSkillsFromDeveloper() {
        if (current.getSkills().size() == 0)
            return null;

        current.setNumbersOfSkills("\"\"");
        current.setSkills(new HashSet<>());
        return current;
    }

    public int removeSkillFromAll(Skill skill) {
        int counter = 0;
        try {
            Map<Long,Developer> developers = repository.findAll();
            for (Map.Entry<Long,Developer> map: developers.entrySet()) {
                StringBuilder skillNumbers = new StringBuilder();
                Developer current = developerBuilder(map.getValue());
                Set<Skill> skills = current.getSkills();
                    for (Skill s : skills) {
                        if (!s.equals(skill)) {
                            skillNumbers.append(s.getID()).append(",");
                        } else {
                            skills.remove(s);
                            counter++;
                        }
                    }
                    current.setNumbersOfSkills(skillNumbers.toString());
                    map.setValue(current);

            }
            repository.saveAll(new ArrayList<>(developers.values()));
        } catch (ReadFileException | WriteFileException e) {
            System.out.println("Ошибка при работе с базой данных в методе removeSkillFromAll()");
        }
        return counter;
    }

    public List<Developer> showDevelopersWithKeySkillWord(String keyWord) {
        try {
            Map<Long, Developer> developers = repository.findAll();
            StringBuilder str = new StringBuilder();
            if (developers.size() == 0)
                return null;
            List<Developer> result = new ArrayList<>();
            for (Map.Entry<Long,Developer> entry: developers.entrySet()) {
                current = developerBuilder(entry.getValue());
                if (current != null) {
                    Set<Skill> skills = current.getSkills();

                    for (Skill s : skills) {
                        if (s.getSkillName().contains(keyWord)) {
                            result.add(entry.getValue());
                            break;
                        }
                    }

                }
            }
            return result;
        } catch (ReadFileException e) {
            System.out.println("Ошибка чтения базы данных." + e.getMessage());
        }
        return null;
    }

    public Developer changeName(String newName) {
        if (!checkDevName(newName))
            return null;

        current.setFirstName(newName);
        try {
            repository.update(current.getId(),current);
            return current;
        } catch (ReadFileException e) {
            System.out.println("Ошибка чтения базы данных." + e.getMessage());
        } catch (WriteFileException e) {
            System.out.println("Ошибка записи в базу данных." + e.getMessage());
        }
        return null;
    }

    public Developer changeLastName(String newLastName) {
        if (!checkDevName(newLastName))
            return null;
        current.setLastName(newLastName);
        try {
            repository.update(current.getId(),current);
            return current;
        } catch (ReadFileException e) {
            System.out.println("Ошибка чтения базы данных." + e.getMessage());
        } catch (WriteFileException e) {
            System.out.println("Ошибка записи в базу данных." + e.getMessage());
        }
        return null;
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
                System.out.println("Не удалось прочитать навыки из базы " + e.getMessage());
            }
        }

        AccountController accountController = new AccountController();
        Account account = accountController.getAccount(developer.getId());
        if (account == null) {
            return null;
        }
        developer.setAccount(account);
        return developer;
    }

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
        if (Integer.parseInt(tmp) < 14) return false;
        return true;
    }

}
