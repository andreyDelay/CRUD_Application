package com.andrey.crud.controllers;


import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;
import com.andrey.crud.model.Skill;
import com.andrey.crud.repository.IO.SkillsRepository;

import java.util.List;
import java.util.Optional;

public class SkillController {

    private SkillsRepository repository = new SkillsRepository();
    private Skill current;

    /**
     * Accepts id of Number type and tries to get object by this id from repository file
     * and if the object is present into repo file calls method removeSkillFromAll() of DeveloperController class,
     * then writes changed data to repository file.
     * @param skillId - id of Skill that must be removed from all existing developers and from repository file.
     * @param <ID> - generic type that allow to pass to the method only extends Number's type.
     * @return - quantity of developers from whose skill was removed.
     */
    public<ID extends Number> int removeSkillFromAll(ID skillId) {
        int index = 0;
        try {
            Optional<Skill> result = repository.find(longParser(skillId));
            if (!result.isPresent()) {
                System.out.println("Такого навыка не существует");
            }
            DeveloperController developerController = new DeveloperController();
            index = developerController.removeSkillFromAll(result.get());

            repository.delete(longParser(skillId));
        } catch (ReadFileException | WriteFileException e) {
            System.out.println("ошибка при удалении навыка " + e.getMessage());
        }
        return index;
    }

    /**
     * Accepts a string name for creating a new Skill object. If method checkSkillName() returns true
     * all skills' names will be compared with incoming value for matching. If skill with same name was found
     * this value will be return and new object won't be created, otherwise a new Skill object will be created
     * and returned.
     * @param name - name for new Skill object
     * @return - old value if some skill already has such name, otherwise a new Skill object
     */
    public Skill saveSkill(String name) {
        try {
            if (!checkSkillName(name))
                return null;

            List<Skill> skills = repository.findAll();
            for (Skill s: skills) {
                if (s.getSkillName().equalsIgnoreCase(name)) {
                    current = s;
                    return current;
                }
            }

            current = repository.save(new Skill(name));
            return current;
        } catch (WriteFileException e) {
            System.out.println("Ошибка записи в базу данных." + e.getMessage());
        } catch (ReadFileException e) {
            System.out.println("Ошибка чтения базы данных." + e.getMessage());
        }
        return null;
    }

    /**
     * Method calls findAll() of SkillsRepository class,  to get a collection with all existing skills
     * and represent this collection as String with all the data. If collection if empty it returns null
     * and print out "В базе нет навыков".
     * @return - String representation of all existing skills.
     */
    public String showAllSkills() {
        try {
            List<Skill> skills = repository.findAll();
            if (skills.size() == 0) {
                System.out.println("В базе нет навыков");
                return null;
            }
            StringBuilder result = new StringBuilder("Список всех навыков:\n");
            for (Skill s: skills) {
                result.append("id:=").append(s.getID());
                result.append(", name:=").append(s.getSkillName()).append("\n");
            }
            return result.toString();
        } catch (ReadFileException e) {
            System.out.println("Ошибка чтения базы данных." + e.getMessage());
        }
       return null;
    }

    private boolean checkSkillName(String name) {
        String tmp = name.trim();
        if (tmp.length() == 0) return false;
        return tmp.replaceAll("[^a-zA-Zа-яА-Я]", "").length() != 0;
    }

    private<L extends Number> Long longParser(L number) {
        return number.longValue();
    }

}
