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

    /**
     * accepts an object and save it into repository file with method writeFile from IOUtils class
     * above all incrementing Long id value with method generateID from IOUtils class
     * @param skill - an object that must be written to the target file
     * @return - a saved object with incremented id
     * @throws WriteFileException
     * @throws ReadFileException
     */
    @Override
    public Skill save(Skill skill) throws WriteFileException, ReadFileException {
        skill.setID(IOUtils.generateID(filepath,separator));
        String dataToWrite = objectToRepositoryFormat(skill);

        IOUtils.writeFile(dataToWrite,filepath, StandardOpenOption.APPEND);
        return skill;
    }

    /**
     * call findAll() method from this class and get Map where keys are Long values with id of
     * Skill objects
     * then it search required id in the Map and if the key is found gets and returns the value
     * @param id - Long value as id of required object
     * @return
     * @throws ReadFileException
     */
    @Override
    public Optional<Skill> find(Long id) throws ReadFileException {
        Iterator<String> iterator = IOUtils.readFile(filepath).iterator();
        String currentLine;
        while (iterator.hasNext()) {
            currentLine = iterator.next().trim();
            if (currentLine.contains("id:") && checkId(currentLine, String.valueOf(id))) {
                return Optional.of(builder(iterator, currentLine));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Skill> findAll() throws ReadFileException {
        Iterator<String> iterator = IOUtils.readFile(filepath).iterator();
        List<Skill> skills = new ArrayList<>();
        String currentLine;
        while (iterator.hasNext()) {
            currentLine = iterator.next();
            if (currentLine.equals("{")) {
                currentLine = iterator.next();
                skills.add(builder(iterator, currentLine));
            }
        }
        return skills;
    }

    /**
     * Update the old object representation with new value
     * @param id - id of the old object
     * @param newValue - new value that must be written instead old value
     * @return - object as old replaced value
     * @throws ReadFileException
     * @throws WriteFileException
     */
    @Override
    public Skill update(Long id, Skill newValue) throws ReadFileException, WriteFileException {
        List<String> rowsFromRepo = IOUtils.readFile(filepath);
        Iterator<String> iterator = rowsFromRepo.iterator();
        Skill oldValue = null;
        int index = 0;
        String currentLine;
        while (iterator.hasNext()) {
            index++;
            currentLine = iterator.next().trim();
            if (currentLine.contains("id:") && checkId(currentLine, String.valueOf(id))) {
                oldValue = builder(iterator, currentLine);
                updater(rowsFromRepo, newValue, index);
                String dataToWrite = String.join("",rowsFromRepo);
                IOUtils.writeFile(dataToWrite,filepath,StandardOpenOption.TRUNCATE_EXISTING);
            }
        }
        return oldValue;
    }

    /**
     * delete object by id if the id value is present
     * @param id - Long value as id of required object
     * @return - deleted object
     * @throws ReadFileException
     * @throws WriteFileException
     */
    @Override
    public void delete(Long id) throws ReadFileException, WriteFileException {
        List<String> rowsFromRepo = IOUtils.readFile(filepath);
        Iterator<String> iterator = rowsFromRepo.iterator();
        int index = 0;
        String currentLine;
        while (iterator.hasNext()) {
            index++;
            currentLine = iterator.next().trim();
            if (currentLine.contains("id:") && checkId(currentLine, String.valueOf(id))) {
                rowsFromRepo.remove(index - 1);
                rowsFromRepo.remove(index);
                rowsFromRepo.remove(index + 1);
                rowsFromRepo.remove(index + 2);

                String dataToWrite = String.join("\n",rowsFromRepo);
                IOUtils.writeFile(dataToWrite,filepath,StandardOpenOption.TRUNCATE_EXISTING);
            }
        }
    }

    /**
     * write data from collection of Skills objects into target file
     * @param list - Collection with objects that must be saved
     * @return - true if the data successfully written in the target file
     * @throws WriteFileException
     */
    @Override
    public boolean saveAll(List<Skill> list) throws WriteFileException {
        String dataToWrite =
                list.stream()
                .map(this::objectToRepositoryFormat)
                .collect(Collectors.joining("\n"));

        return IOUtils.writeFile(dataToWrite,filepath,StandardOpenOption.TRUNCATE_EXISTING);
    }

    /**
     *
     * @param allData
     * @param newSkill
     * @param index
     */
    private void updater(List<String> allData,Skill newSkill, int index) {
        StringBuilder newData = new StringBuilder();
        newData
                .append("\t")
                .append("name:=").append(newSkill.getSkillName()).append("\n")
                .append("}")
                .append("\n");

        allData.remove(index + 1);
        allData.set(index + 2, newData.toString());
    }

    /**
     * //TODO сделать описание
     * @param iterator
     * @param currentLine
     * @return
     */
    private Skill builder(Iterator<String> iterator, String currentLine) {
        String [][] dataForSkillObject = new String[2][2];
        int counter = 0;

        while (!currentLine.equals("}")) {
            dataForSkillObject[counter++] = currentLine.split(separator);
            currentLine = iterator.next().trim();
        }

        return new Skill(Long.parseLong(dataForSkillObject[0][1]), dataForSkillObject[1][1]);
    }

    /**
     *
     * @param currentLine
     * @param requiredId
     * @return
     */
    private boolean checkId(String currentLine, String requiredId) {
        String [] splitter = currentLine.split("=");
        return  requiredId.equals(splitter[1].trim());
    }

    /**
     * accept Skill object and build a String representation of this object
     * @param skill - Skill object for building
     * @return - string representation of the object
     */
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
