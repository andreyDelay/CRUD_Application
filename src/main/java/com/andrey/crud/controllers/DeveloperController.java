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

    /**
     * method accepts three three string that represent parameters required to create
     * a new object of Developer type. These parameter firstly checked by method checkDevName()
     * and if this method returns true - object of type Developer will be created.
     * @param name - name new of user.
     * @param lastName - last name new of user.
     * @param age - age of new user.
     * @return - a new object of Developer type.
     */
    public Developer createDeveloper(String name, String lastName, String age, Account account) {
        if (!(checkDevName(name) || checkDevName(lastName) || numberChecker(age)))
            return null;

        try {
            current = new Developer(name,lastName,Integer.parseInt(age));
            current.setAccount(account);
            return repository.save(current);
        } catch (ReadFileException e) {
            System.out.println("Ошибка при чтении базы данных." + e.getMessage());
        } catch (WriteFileException e) {
            System.out.println("Ошибка при записи в базу данных." + e.getMessage());
        }
        return null;
    }

    /**
     * Accepts int value of Developer object that must be deleted from the repository,
     * call method delete() of DeveloperRepository , and if the id is present in repo file
     * all data for required developer will be removed from the repo.
     * @param developerId - id of required object.
     * @param <ID> -  generic type that allow to pass to the method only extends Number's type.
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
     * Method shows all existing developers from repository file.
     * Call method findAll() of DeveloperRepository and get a collection of Developers
     * @return - string representation of all developers from collection,
     * if collection is empty returns string result "Нет данных."
     */
    public String showAllDevelopers() {
        try {
            List<Developer> developers = repository.findAll();
            if (developers.size() == 0) return "Нет данных.";
            return developers.stream()
                    .map(Developer::toString)
                    .collect(Collectors.joining());
        } catch (ReadFileException e) {
            return e.getMessage();
        }
    }

    /**
     * When application is started this method read repository file and get a collection with
     * all existing developers, then represent it as string in short form to show to user start data.
     * @return - string representation of all developers from collection in short form,
     * if collection is empty returns string result "Нет данных.".
     */
    public String showAllInShortForm() {
        try {
            List<Developer> developers = repository.findAll();
            if (developers.size() == 0) return "Нет данных.";
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

    /**
     * Accepts id value of required object and verify this id by method numberChecker()
     * if the method returns true method find() of DeveloperRepository will be called.
     * If this id is present in the repository file returned value will be assigned to private filed
     * private Developer current and will be returned.
     * @param developerId - id of required object.
     * @param <ID> - generic type that allow to pass to the method only extends Number's type.
     * @return - object of Developer type, if object with required id is not present in the.
     * repository file returns null.
     */
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
     * Accepts object of Skill type, gets Set<Skill> from current Developer, if Set is null
     * creates new HashSet() and add the incoming skill object to this HashSet, if Set is not null
     * method checks whether the object present into private
     * field Set<Skill> skills of current Developer or not. If skill is already present into Set returns null, if not
     * the skill will be added to Set and new data will bew written into repository file.
     * @param newSkill - Skill that must be added.
     * @return - Developer with new data, if skill wasn't added returns null.
     */
    public Developer addSkillToDeveloper(Skill newSkill) {
        Set<Skill> skills = current.getSkills();
        if (skills == null) {
            skills = new HashSet<>();
            skills.add(newSkill);
        } else {
            for (Skill s: skills)
                if (s.getID().equals(newSkill.getID())) return null;
        }

        skills.add(newSkill);
        current.setSkills(skills);
        try {
            repository.update(current.getId(), current);
        } catch (ReadFileException | WriteFileException e) {
            System.out.println("Добавленные данные не были сохранены." + e);
        }
        return current;
    }

    /**
     * Accepts id value of object that have to be extends Number type. Then get Set<Skill> from current
     * Developer and check if Set contains the object of Skill type with this id. If object is present
     * in the Set it will be removed and Developer with changes will be written to repository file.
     * If object with the id is not present returns null.
     * @param skillId - id of Skill that must be removed from Developer.
     * @param <ID> - generic type that allow to pass to the method only extends Number's type.
     * @return - Developer with changed data, if data wasn't changed returns null.
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
            repository.update(current.getId(), current);
            return current;
        } catch (ReadFileException e) {
            System.out.println("Ошибка чтения базы данных." + e.getMessage());
        } catch (WriteFileException e) {
            System.out.println("Ошибка записи в базу данных." + e.getMessage());
        }
        return null;
    }

    /** Method removes all skills from current developer and write changed data to repository file
     * @return - Developer with changes.
     */
    public Developer removeAllSkillsFromDeveloper() {
        if (current.getSkills().size() == 0)
            return null;

        current.setSkills(new HashSet<>());
        return current;
    }

    /**
     * Accepts object of Skill type that must be removed from all existing developers.
     * Firstly call method findAll() of DeveloperRepository class and get Collection with all existing developers.
     * Then with loop compare whether this object is present for current developer or not, if it is present
     * temp int counter increments by 1 and the found objects removes from current developer. In the end new data will
     * be written to repository file.
     * @param skill - object of Skill type that must be removed from all developers.
     * @return - quantity of developers from whose skill was removed.
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
     * Accepts key word for searching similar word in skill names for all existing developers.
     * Firstly gets collection of Developers by findAll() method. Then in loop gets Set<Skill> for each developer
     * and check each skill name whether name contains incoming key string or not, if contains adds this Developer to List<Developer>
     * @param keyWord - key word for searching
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
                    if (skills != null)
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
