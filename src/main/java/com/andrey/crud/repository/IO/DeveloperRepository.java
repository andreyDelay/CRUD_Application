package com.andrey.crud.repository.IO;

import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;
import com.andrey.crud.model.Developer;
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

    private final String separator = ",";

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
        Map<Long, Developer> developers = fromRepository.stream()
                                                        .filter(row -> row.length() > 5)
                                                        .map(row -> row.split(","))
                                                        .map(array -> new Developer(
                                                                Long.parseLong(array[0]),
                                                                array[1],
                                                                array[2],
                                                                Integer.parseInt(array[3])))
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
        developers.values().stream()
                                .filter(value -> value.getId().equals(id))
                                .peek(value -> value = obj);
        //TODO проверить меняется ли объект в мапе после обработки в стриме
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
        StringBuilder dataToWrite = new StringBuilder();
                    dataToWrite.append("{")
                    .append("\n")
                    .append("\t")
                    .append(object.getId()).append(",")
                    .append(object.getFirstName()).append(",")
                    .append(object.getLastName()).append(",")
                    .append(object.getAge())
                    .append("\n").append("}\n");
        return dataToWrite.toString();
    }
}
