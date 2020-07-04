package com.andrey.crud.controllers;


import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.model.Skill;
import com.andrey.crud.repository.IO.SkillsRepository;

import java.util.Map;

public class SkillController {

    private SkillsRepository repository = new SkillsRepository();
    private Skill current;

    public<ID extends Number> String removeSkillFromAll(ID skillId) {
        return "удалить навык с таким ID у всех";
    }

    public String showAllExistingSkills() {
        return "Все навыки в общем всего";
    }

    public String showSkillWithKeyWord(String keyWord) {
        return "все навыки, в которых содержится слово";
    }

    public<ID extends Number,S> String updateSkill(ID skillId, String newName) {
        return "Навык обновлён";
    }

    public String saveSkill() {
        return "добавить навык";
    }

    public Map<Long,Skill> allSkills() throws ReadFileException {
       return repository.findAll();
    }

    private boolean checkSkillName(String name) {
        String tmp = name.trim();
        if (tmp.length() == 0) return false;
        return tmp.replaceAll("[^a-zA-Zа-яА-Я]", "").length() != 0;
    }

    /**
     * Метод для проверки id существующего объекта типа Skill по ID
     * @param id - идентификатор объекта
     * @return - boolean результат проверки, содержится ли объект Skill в списке
     */
    private boolean ifContainsID(Long id) {
        return true;
    }

    /**
     * Метод для проверки id существующего объекта типа Skill по name
     * @param name - имя объекта
     * @return - boolean результат проверки, содержится ли объект Skill в списке
     */
    private boolean ifContainsValue(String name) {
        return true;

    }

    /**
     * Метод для получения id существующего объекта типа Skill
     * @param oldName - имя объекта
     * @return - Long идентификатор заданного объекта
     */
    private Long findID(String oldName) {
        return 1L;
    }

}
