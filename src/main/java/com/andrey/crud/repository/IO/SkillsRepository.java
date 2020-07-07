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
import java.util.function.Function;
import java.util.stream.Collectors;

public class SkillsRepository implements SkillIORepository {

    private final Path filepath = Paths.get("src\\resources\\skills.txt");

    private final String separator = "=";

    @Override
    public Skill save(Skill skill) throws WriteFileException, ReadFileException {
        skill.setID(IOUtils.generateID(filepath,separator));
        String dataToWrite = objectToRepositoryFormat(skill);

        IOUtils.writeFile(dataToWrite,filepath, StandardOpenOption.APPEND);
        return skill;
    }

    @Override
    public Optional<Skill> find(Long id) throws ReadFileException {
        Map<Long,Skill> skills = findAll();
        return skills.values().stream()
                .filter(value -> value.getID().equals(id))
                .findFirst();
    }

    @Override
    public Skill delete(Long id) throws ReadFileException, WriteFileException {
        Map<Long,Skill> skills = findAll();
        Skill removed = skills.remove(id);
        if (removed != null) {
            saveAll(new ArrayList<>(skills.values()));
            return removed;
        }
        return null;
    }

    @Override
    public boolean saveAll(List<Skill> list) throws WriteFileException {
        String dataToWrite =
                list.stream()
                .map(this::objectToRepositoryFormat)
                .collect(Collectors.joining("\n"));

        return IOUtils.writeFile(dataToWrite,filepath,StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public Skill update(Long id, Skill newValue) throws ReadFileException, WriteFileException {
        Map<Long, Skill> skills = findAll();
        for(Map.Entry<Long, Skill> entry: skills.entrySet()) {
            if (entry.getKey().equals(id)) {
                entry.setValue(newValue);
                Skill updated = entry.getValue();
                saveAll(new ArrayList<>(skills.values()));
                return updated;
            }
        }
            return null;
    }

    @Override
    public Map<Long,Skill> findAll() throws ReadFileException {
        List<String> rows = IOUtils.readFile(filepath);
        Iterator<String> iterator = rows.iterator();
        List<Skill> list = new ArrayList<>();
        while (iterator.hasNext()) {
            String current = iterator.next();
            if (current.equals("{")) {
                String [][] tmp = new String[2][2];
                int cnt = 0;
                current = iterator.next().trim();
                while (!current.equals("}")) {
                    tmp[cnt++] = current.split(separator);
                    current = iterator.next().trim();
                }
                list.add(new Skill(
                            Long.parseLong(tmp[0][1]),
                            tmp[1][1])
                        );
            }
        }
        Map<Long,Skill> skills = list.stream()
                                    .collect(Collectors.toMap(
                                            Skill::getID,
                                            Function.identity(),
                                            (s1,s2) -> s1,
                                            HashMap::new));
        return skills;
    }


    private String objectToRepositoryFormat(Skill skill) {
        StringBuilder result = new StringBuilder();
            result
                    .append("{\n")
                    .append("\t")
                    .append("id:=").append(skill.getID()).append("\n")
                    .append("\t")
                    .append("name:=").append(skill.getSkillName()).append("\n")
                    .append("}\n");

        return result.toString();
    }

}
