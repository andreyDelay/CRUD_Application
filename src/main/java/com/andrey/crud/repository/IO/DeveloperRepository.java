package com.andrey.crud.repository.IO;

import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;
import com.andrey.crud.model.Developer;
import com.andrey.crud.model.Skill;
import com.andrey.crud.repository.DeveloperIORepository;
import com.andrey.crud.utils.IOUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DeveloperRepository implements DeveloperIORepository {

    private final Path filepath = Paths.get("src\\resources\\developers.txt");

    private final String separator = "=";

    @Override
    public boolean save(Developer obj) throws WriteFileException, ReadFileException {
        obj.setId(IOUtils.generateID(filepath,separator));
        String dataToWrite = objectToRepositoryFormat(obj);

        return IOUtils.writeFile(dataToWrite, filepath, StandardOpenOption.APPEND);
    }

    @Override
    public Optional<Developer> find(Long id) throws ReadFileException {
        Map<Long,Developer> developers = findAll();
        Developer required = developers.get(id);
        if (Objects.nonNull(required)) {
            return Optional.of(required);
        }
        return Optional.empty();
    }

    @Override
    public Map<Long, Developer> findAll() throws ReadFileException {
        List<String> fromRepository = IOUtils.readFile(filepath);
        Iterator<String> rows = fromRepository.iterator();
        List<Developer> list = new ArrayList<>();
            while (rows.hasNext()) {
                String current = rows.next();
                if (current.equals("{")) {
                    String [][] tmp = new String[5][2];
                    int cnt = 0;
                    current = rows.next().trim();
                    while (!current.equals("}")){
                        tmp[cnt++] = current.split("=");
                        current = rows.next().trim();
                    }
                    list.add(new Developer(
                                            Long.parseLong(tmp[0][1]),
                                            tmp[1][1],
                                            tmp[2][1],
                                            Integer.parseInt(tmp[3][1]),
                                            tmp[4][1]));
                }
            }
        Map<Long,Developer> developers = list.stream()
                                            .collect(Collectors.toMap(
                                            Developer::getId,
                                            Function.identity(),
                                            (dev1,dev2) -> dev1,
                                            HashMap::new));
        return developers;
    }

    @Override
    public boolean saveAll(List<Developer> list) throws WriteFileException {
        String dataToWrite = list.stream()
                                .map(this::objectToRepositoryFormat)
                                .collect(Collectors.joining("\n"));

        return IOUtils.writeFile(dataToWrite, filepath,StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public boolean update(Long id, Developer obj) throws ReadFileException, WriteFileException {
        Map<Long, Developer> developers = findAll();
        for(Map.Entry<Long,Developer> entry: developers.entrySet()) {
            if (entry.getKey().equals(id))
                entry.setValue(obj);
        }
        String dataToWrite = developers.values().stream()
                                                .map(this::objectToRepositoryFormat)
                                                .collect(Collectors.joining("\n"));

        return IOUtils.writeFile(dataToWrite, filepath,StandardOpenOption.TRUNCATE_EXISTING);
    }


    @Override
    public boolean delete(Long id) throws ReadFileException, WriteFileException {
        Map<Long, Developer> developers = findAll();
        Developer removed = developers.get(id);
        if (removed != null) {
            developers.remove(id);
            return saveAll(new ArrayList<>(developers.values()));
        }

        return false;
    }

    private String objectToRepositoryFormat(Developer object) {

        StringBuilder sk = new StringBuilder();
        if (!object.getNumbersOfSkills().contains(",")) {
            sk.append("\"\"");
        } else {
            sk.append(object.getNumbersOfSkills());
        }

        StringBuilder dataToWrite = new StringBuilder();
                    dataToWrite.append("{")
                    .append("\n")
                    .append("\t")
                    .append("id:=").append(object.getId()).append("\n")
                    .append("\t")
                    .append("firstName:=").append(object.getFirstName()).append("\n")
                    .append("\t")
                    .append("lastName:=").append(object.getLastName()).append("\n")
                    .append("\t")
                    .append("age:=").append(object.getAge()).append("\n")
                    .append("\t")
                    .append("skills:=").append(sk.toString()).append("\n")
                    .append("}\n");
        return dataToWrite.toString();
    }
}
