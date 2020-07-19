package com.andrey.crud.controllers;


import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;
import com.andrey.crud.model.Skill;
import com.andrey.crud.repository.IO.SkillsRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class SkillController {

    private SkillsRepository repository = new SkillsRepository();
    private Skill current;

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

    public List<Skill> allSkills() throws ReadFileException {
        return repository.findAll();
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
