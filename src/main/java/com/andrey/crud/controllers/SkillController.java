package com.andrey.crud.controllers;



import com.andrey.crud.repository.IO.SkillsRepository;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class SkillController {

    private SkillsRepository repository = new SkillsRepository();

//    public Map<Long, Skill> skills() {
//        return access.findAll();
//    }
//
//    /**
//     * Метод преобразует строку в новый объект типа Skill
//     * если не получилось добавить новый объект в ремозиторий, то метод возвращает строку
//     * сообщающую о неудачной попытке сохранения
//     * @param skillName - имя нового навыка
//     * @return - строка с итогом выполнения добавления/сохранения объекта типа Skill
//     */
//    public String save(String skillName) {
//        if (!checkSkillName(skillName))
//            return "Некорректное название навыка.";
//        try {
//            access.save(new Skill(skillName));
//        } catch (SkillException e) {
//            System.out.println("Ошибка сохранения навыка.");
//        }
//        return "Навык добавлен";
//    }
//
//    /**
//     * Метод герерирует строку с перставление всех навыков для текущего пользователя
//     * если список пуст - сообщает об этом
//     * @return - строка со списком всех навыков
//     */
//    public String showAllSkills() {
//            String result = access.findAll()
//                                    .entrySet()
//                                    .stream()
//                                    .map(entry -> "id: " + entry.getKey() + ", name: " + entry.getValue().getSkillName())
//                                    .collect(Collectors.joining(";\n"));
//
//            if (result.substring(0,result.length()-1).length() == 0)
//                return "Список навыков пуст";
//            return result;
//    }
//
//    /**
//     * Удаляет объект типа Skill из репозитория по заданному ID
//     * если такого ID нет - сообщает об этом
//     * при неудачной попытке сохранения изменений - информирует пользователя
//     * @param skillID - ID объекта типа Skill
//     * @return - строковый результат работы метода
//     */
//    public String deleteSkill(Long skillID) {
//        if (!ifContainsID(skillID))
//            return "Навыка с таким id нет в списке";
//
//        try {
//            access.delete(skillID);
//        } catch (SkillException e) {
//            System.out.println("Ошибка удаления навыка." + e.getMessage());
//        }
//        return "Навык удалён";
//    }
//
//    /**
//     * Метод удаляет объект типа Skill из репозитория по имени
//     * если такого имени не нашлось в списке - сообщает об этом
//     * при неудачной попытке сохранения изменений - информирует пользователя
//     * @param name - имя объекта типа Skill
//     * @return - строковый результат работы метода
//     */
//    public String deleteSkill(String name) {
//        if (!ifContainsValue(name))
//            return "Навыка с таким именем нет в списке";
//
//        try {
//            access.delete(findID(name));
//        } catch (SkillException e) {
//            System.out.println("Ошибка удаления навыка." + e.getMessage());
//        }
//        return "Навык удалён";
//    }
//
//    /**
//     * Метод вносит изменения в существующий объект типа Skill
//     * если такого объекта нет в списке - сообщает об этом
//     * при неудачной попытке сохранения изменений - информирует пользователя
//     * @param oldName - старое название навыка
//     * @param newName - новое название навыка
//     * @return - строковый результат работы метода
//     */
//    public String updateSkill(String oldName, String newName) {
//        if (!(checkSkillName(oldName) || checkSkillName(newName)))
//            return "Некорректное имя навыка.";
//
//        if (!skillsList.containsValue(new Skill(oldName)))
//            return "Навыка с таким именем нет в списке.";
//
//        try {
//            access.update(findID(oldName), new Skill(newName));
//        } catch (SkillException e) {
//            System.out.println("Не удалось обновить навык." + e.getMessage());
//        }
//        return "Навык обновлён";
//    }
//
//    /**
//     * Метод создаёт и сохраняет несколько навыков в репозитории
//     * если формат строки некорректный - сообщает об этом
//     * @param skillNames - имена навыков, которые необходимо добавить в репозиторий
//     * @return - строковый результат работы метода
//     */
//    public String saveSeveralSkills(String skillNames) {
//        if (skillNames.replaceAll("[^\\,]","").length() == 0)
//            return "Напишите навыки через запятую";
//        String [] names = skillNames.split(",");
//        List<Skill> newSkills = new ArrayList<>();
//        for (int i = 0; i < names.length; i++) {
//            if (!checkSkillName(names[i])) {
//                return "Некорректное имя навыка №" + i;
//            }
//
//            String tmpName = names[i].replaceAll("[^0-9a-zA-Zа-яА-Я]","");
//            newSkills.add(new Skill(tmpName));
//        }
//
//        try {
//            access.saveAll(newSkills);
//        } catch (SkillException e) {
//            System.out.println("Не удалось добавить навыки." + e.getMessage());
//        }
//        return "Навыки добавлены";
//    }
//
//    /**
//     * Вспомогательный метод для проверки корректности имени будущего
//     * объекта типа Skill непосредственно перед созданием/удаление/изменением
//     * @param name - имя объекта типа Skill
//     * @return - boolean результат проверки name
//     */
//
//    private boolean checkSkillName(String name) {
//        String tmp = name.trim();
//        if (tmp.length() == 0) return false;
//        return tmp.replaceAll("[^a-zA-Zа-яА-Я]", "").length() != 0;
//    }
//
//    /**
//     * Метод для проверки id существующего объекта типа Skill по ID
//     * @param id - идентификатор объекта
//     * @return - boolean результат проверки, содержится ли объект Skill в списке
//     */
//    private boolean ifContainsID(Long id) {
//        return skillsList.containsKey(id);
//    }
//
//    /**
//     * Метод для проверки id существующего объекта типа Skill по name
//     * @param name - имя объекта
//     * @return - boolean результат проверки, содержится ли объект Skill в списке
//     */
//    private boolean ifContainsValue(String name) {
//        return skillsList.containsValue(new Skill(name));
//
//    }
//
//    /**
//     * Метод для получения id существующего объекта типа Skill
//     * @param oldName - имя объекта
//     * @return - Long идентификатор заданного объекта
//     */
//    private Long findID(String oldName) {
//        Long id = skillsList.entrySet().stream()
//                .filter(a -> a.getValue().getSkillName().equals(oldName))
//                .map(Map.Entry::getKey).findFirst()
//                .get();
//        return id;
//    }

}
