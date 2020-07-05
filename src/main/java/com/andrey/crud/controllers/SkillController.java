package com.andrey.crud.controllers;


import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;
import com.andrey.crud.model.Skill;
import com.andrey.crud.repository.IO.SkillsRepository;

import java.util.Map;
import java.util.Optional;

public class SkillController {

    private SkillsRepository repository = new SkillsRepository();
    private Skill current;

    public<ID extends Number> String removeSkillFromAll(ID skillId) {
        try {
            Optional<Skill> result = repository.find(longParser(skillId));
            if (!result.isPresent())
                return "Такого навыка не существует";

            DeveloperController developerController = new DeveloperController();
            if (!developerController.removeSkillFromAll(result.get()))
                return "не удалось удалить навык";

            repository.delete(longParser(skillId));
        } catch (ReadFileException | WriteFileException e) {
            return "ошибка при удалении навыка " + e.getMessage();
        }

        return "навык удалён.";
    }


    public<ID extends Number,S> String updateSkill(ID skillId, String newName) {
        return "Навык обновлён";
    }

    public Skill saveSkill(String name) {

        return current;
    }

    public Map<Long,Skill> allSkills() throws ReadFileException {
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
