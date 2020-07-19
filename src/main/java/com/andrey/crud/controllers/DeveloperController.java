package com.andrey.crud.controllers;

import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;
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

    /**
     * method creates a new object of Developer type
     * @param name - name of user
     * @param lastName - last name of user
     * @param age - age of user
     * @return - a new created Developer if all parameters have successfully passed verification
     */
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

    /**
     * delete developer by id if Developer with this id was found
     * @param developerId
     * @param <ID>
     */
    public<ID extends Number> void deleteDeveloper(ID developerId) {
        try {
            repository.delete(longParser(developerId));
        } catch (WriteFileException e) {
            System.out.println("Ошибка записи изменений в базу данных.");
        } catch (ReadFileException e) {
            System.out.println("Ошибка чтения базы данных.");
        }
    }

    /**
     * show all existing developers in repository
     * @return - string representation of all developers
     */
    public String showAllDevelopers() {
        try {
            List<Developer> developers = repository.findAll();
            return developers.stream()
                    .map(Developer::toString)
                    .collect(Collectors.joining());
        } catch (ReadFileException e) {
            return e.getMessage();
        }
    }

    public String showAllInShortForm() {
        try {
            List<Developer> developers = repository.findAll();
            return developers.stream()
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
                return current;
            }
        } catch (ReadFileException e) {
            System.out.println("Ошибка чтения базы данных.");
        }
        return null;
    }

    /**
     * method add new Skill to Developer
     * @param newSkill - Skill that must be added
     * @return - Developer with new data
     */
    public Developer addSkillToDeveloper(Skill newSkill) {
        Set<Skill> skills = current.getSkills();
        if (skills.size() != 0) {
            for (Skill s: skills)
                if (s.getID().equals(newSkill.getID())) return null;
        }

        current.addSkill(newSkill);
        return current;
    }

    /**
     * removes Skill from Developer
     * @param skillId
     * @param <ID>
     * @return
     */
    public <ID extends Number>  Developer removeSkillFromDeveloper(ID skillId) {
        try {
            Skill removed = null;
            Set<Skill> skills = current.getSkills();
            for (Skill s: skills) {
                if (s.getID().equals(longParser(skillId))) {
                    removed = s;
                }
            }

            if (removed == null) {
                return null;
            }
            skills.remove(removed);
            current.setSkills(skills);
            current = repository.update(current.getId(), current);
            return current;
        } catch (ReadFileException e) {
            System.out.println("Ошибка чтения базы данных." + e.getMessage());
        } catch (WriteFileException e) {
            System.out.println("Ошибка записи в базу данных." + e.getMessage());
        }
        return null;
    }

    /**
     * removes all Skills from Developer
     * @return
     */
    public Developer removeAllSkillsFromDeveloper() {
        if (current.getSkills().size() == 0)
            return null;

        current.setSkills(new HashSet<>());
        return current;
    }

    /**
     * removes one skill from all developers
     * @param skill
     * @return
     */
    public int removeSkillFromAll(Skill skill) {
        int counter = 0;
        try {
            List<Developer> developers = repository.findAll();
            for (Developer dev: developers) {
                Set<Skill> skills = dev.getSkills();
                    if (skills.remove(skill)) {
                        dev.setSkills(skills);
                        counter++;
                    }
                }
            repository.saveAll(developers);
        } catch (ReadFileException | WriteFileException e) {
            System.out.println("Ошибка при работе с базой данных в методе removeSkillFromAll()");
        }
        return counter;
    }

    /**
     * accept key word for searching similar word in skill names
     * if this ward was found - Developer object will be added to List
     * @param keyWord - key word
     * @return - collection of Developer
     */
    public List<Developer> showDevelopersWithKeySkillWord(String keyWord) {
        try {
            List<Developer> developers = repository.findAll();

            if (developers.size() == 0)
                return null;
            List<Developer> result = new ArrayList<>();
            for (Developer developer: developers) {
                if (developer != null) {
                    Set<Skill> skills = developer.getSkills();

                    for (Skill s : skills) {
                        if (s.getSkillName().toLowerCase().contains(keyWord.toLowerCase())) {
                            result.add(developer);
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

//    private Developer developerBuilder(Developer developer) {
//
//        if (developer.getNumbersOfSkills().contains(",")) {
//            SkillController skillController = new SkillController();
//            String[] skillNumbers = developer.getNumbersOfSkills().split(",");
//            try {
//                Map<Long, Skill> allSkills = skillController.allSkills();
//                for (int i = 0; i < skillNumbers.length; i++) {
//                    Long skillId = Long.parseLong(skillNumbers[i]);
//                    Skill skill = allSkills.get(skillId);
//                    developer.addSkill(skill);
//                }
//            } catch (ReadFileException e) {
//                System.out.println("Не удалось прочитать навыки из базы " + e.getMessage());
//            }
//        }
//
//        AccountController accountController = new AccountController();
//        Account account = accountController.getAccount(developer.getId());
//        if (account == null) {
//            return null;
//        }
//        developer.setAccount(account);
//        return developer;
//    }

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
