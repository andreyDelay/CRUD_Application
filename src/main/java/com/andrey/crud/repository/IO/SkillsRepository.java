package com.andrey.crud.repository.IO;

import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;
import com.andrey.crud.model.Skill;
import com.andrey.crud.repository.SkillIORepository;
import com.andrey.crud.utils.IOUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class SkillsRepository implements SkillIORepository {

    private final Path filepath = Paths.get("src\\resources\\skills.txt");

    private final String separator = "/";

    @Override
    public boolean save(Skill obj) throws WriteFileException, ReadFileException {
        obj.setID(IOUtils.generateID(filepath,separator));
        String dataToWrite = objectToRepositoryFormat(obj);

        return IOUtils.writeFile(dataToWrite,filepath, StandardOpenOption.APPEND);
    }


    @Override
    public Optional<Skill> find(Long id) throws ReadFileException {
        List<Skill> skills = findAll();
        return skills.stream()
                .filter(value -> value.getID().equals(id))
                .findFirst();
    }


    @Override
    public boolean delete(Long id) throws ReadFileException, WriteFileException {
        List<Skill> skills = findAll();
        Optional<Skill> required = skills.stream().filter(obj -> obj.getID().equals(id)).findFirst();
        if (!required.isPresent()) return false;

        skills.remove(required.get());
        return saveAll(skills);
    }


    @Override
    public boolean saveAll(List<Skill> list) throws WriteFileException {
        Map<Long,Set<Skill>> groupedSkills = list.stream()
                                            .collect(Collectors.groupingBy(Skill::getID,Collectors.toSet()));
        String dataToWrite =
                groupedSkills.values().stream()
                .map(this::objectToRepositoryFormat)
                .collect(Collectors.joining("\n"));

        return IOUtils.writeFile(dataToWrite,filepath,StandardOpenOption.TRUNCATE_EXISTING);
    }


    @Override
    public boolean update(Long id, Skill oldValue, Skill newValue) throws ReadFileException, WriteFileException {
        List<Skill> skills = findAll();
        int index = skills.indexOf(oldValue);
        if (index < 0) return false;
        //TODO пользуюсь ли по итогу параметром id???
        skills.set(index, newValue);
            return saveAll(skills);
        }


    @Override
    public List<Skill> findAll() throws ReadFileException {
        List<String> rows = IOUtils.readFile(filepath);
        List<Skill> skills = rows.stream()
                                        .map(row -> row.split(separator))
                                        .map(array -> new Skill(
                                                Long.parseLong(array[0]),
                                                array[1]))
                                        .collect(Collectors.toList());
        return skills;
    }


    private String objectToRepositoryFormat(Set<Skill> skills) {
        StringBuilder result = new StringBuilder();
        for(Skill elem: skills) {
            result.append(elem.getID())
                    .append(separator)
                    .append(elem.getSkillName());
        }
        return result.toString();
    }

}
