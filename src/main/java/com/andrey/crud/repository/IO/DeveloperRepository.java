package com.andrey.crud.repository.IO;

import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;
import com.andrey.crud.model.Account;
import com.andrey.crud.model.Developer;
import com.andrey.crud.model.Skill;
import com.andrey.crud.repository.DeveloperIORepository;
import com.andrey.crud.utils.IOUtils;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Collectors;

public class DeveloperRepository implements DeveloperIORepository {

    private final Path filepath = Paths.get("src\\resources\\developers.txt");

    private final String separator = "=";

    @Override
    public Developer save(Developer developer) throws WriteFileException, ReadFileException {
        developer.setId(IOUtils.generateID(filepath,separator));
        String dataToWrite = objectToRepositoryFormat(developer);

        IOUtils.writeFile(dataToWrite, filepath, StandardOpenOption.APPEND);
        return developer;
    }

    @Override
    public Optional<Developer> find(Long id) throws ReadFileException {
        Iterator<String> iterator = IOUtils.readFile(filepath).iterator();
        String current;
        while (iterator.hasNext()) {
            current = iterator.next().trim();
            if (current.contains("id:") && checkId(current, String.valueOf(id))) {
                return Optional.of(buildDeveloper(iterator, current));
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Developer> findAll() throws ReadFileException {
        Iterator<String> iterator = IOUtils.readFile(filepath).iterator();
        List<Developer> developers = new ArrayList<>();
        String current;
            while (iterator.hasNext()) {
                current = iterator.next();
                if (current.equals("{")) {
                    current = iterator.next().trim();

                    developers.add(buildDeveloper(iterator, current));
                }
            }

        return developers;
    }

    @Override
    public boolean saveAll(List<Developer> list) throws WriteFileException {
        String dataToWrite = list.stream()
                                .map(this::objectToRepositoryFormat)
                                .collect(Collectors.joining());

        return IOUtils.writeFile(dataToWrite, filepath,StandardOpenOption.TRUNCATE_EXISTING);
    }

    @Override
    public Developer update(Long id, Developer developer) throws ReadFileException, WriteFileException {
        List<String> rowsFromRepo = IOUtils.readFile(filepath);
        Iterator<String> iterator = rowsFromRepo.iterator();
        Developer oldValue = null;
        int index = 0;
        String currentLine;
        while (iterator.hasNext()) {
            index ++;
            currentLine = iterator.next().trim();
            if (currentLine.contains("id:") && checkId(currentLine, String.valueOf(id))) {
                oldValue = buildDeveloper(iterator, currentLine);
                updater(rowsFromRepo, developer, index);
                String dataToWrite = String.join("\n", rowsFromRepo);
                IOUtils.writeFile(dataToWrite, filepath,StandardOpenOption.TRUNCATE_EXISTING);
                break;
            }
        }
        return oldValue;
    }

    /**
     * Just delete all elements by index from collection List<String> that represents all rows from repository file,
     * after incoming id is matched with current in method
     * checkId() of this class,
     * @param id - id of object that must be deleted from repository
     * @throws ReadFileException - if repository file is not exists or not available for reading
     * @throws WriteFileException - if repository file is not available for writing
     */
    @Override
    public void delete(Long id) throws ReadFileException, WriteFileException {
        List<String> rowsFromRepo = IOUtils.readFile(filepath);
        Iterator<String> iterator = rowsFromRepo.iterator();
        int index = 0;
        String currentLine;
        while (iterator.hasNext()) {
            index ++;
            currentLine = iterator.next().trim();
            if (currentLine.contains("id:") && checkId(currentLine, String.valueOf(id))) {
                for (int i = 0; i < 7; i++)
                    rowsFromRepo.remove(index -1);

                String dataToWrite = String.join("", rowsFromRepo);
                IOUtils.writeFile(dataToWrite, filepath,StandardOpenOption.TRUNCATE_EXISTING);
            }
        }
    }

    /**+
     * Method updates string data with new value, it accepts an index of row where object's id that must be updated
     * was found, then all the rows anywhere until "}" will be replaced by index in incoming collection
     * @param allData - collection with all data from repository
     * @param newDeveloper - object that must be written instead old value
     * pay attention that it is doesn't matter whether newDeveloper object contains different data with repository data
     * or not, the data will be written anyway
     * @param index - current index
     */
    private void updater(List<String> allData, Developer newDeveloper, int index) {
        StringBuilder newDataForOneLine = new StringBuilder();
        newDataForOneLine
                        .append("\t").append("firstName:=").append(newDeveloper.getFirstName()).append("\n")
                        .append("\t").append("lastName:=").append(newDeveloper.getLastName()).append("\n")
                        .append("\t").append("age:=").append(newDeveloper.getAge()).append("\n")
                        .append("\t").append(numbersOfSkills(newDeveloper)).append("\n")
                        .append("}").append("\n");
        for (int i = 0; i < 4; i++)
            allData.remove(index);

        allData.set(index, newDataForOneLine.toString());
    }

    /**
     * Method checks required and current id as string and return a result as boolean
     * @param currentLine - current row that definitely contains string "id:" thereby
     * may be splitted and checked whether contains required value or not
     * @param requiredId - id that we try to find in repository file
     * @return - true if id from current row matches with required id, otherwise return false
     */
    private boolean checkId(String currentLine, String requiredId) {
        String [] splitter = currentLine.split("=");
        return  requiredId.equals(splitter[1].trim());
    }

    /**
     * Restore string data from repository into Developer object
     * @param iterator - Iterator of all rows from repository file
     * @param currentLine - row that the iterator points to right now
     * @return - Developer with all the data getting from repository
     * @throws ReadFileException - if method buildSetOfSkills() throws this exception
     * while reading repository file in SkillRepository class
     */
    private Developer buildDeveloper(Iterator<String> iterator, String currentLine) throws ReadFileException {
        String [][] arrayForBuildDeveloper = new String[5][2];
        int cnt = 0;

        while (!currentLine.equals("}")){
            arrayForBuildDeveloper[cnt++] = currentLine.split("=");
            currentLine = iterator.next().trim();
        }
        Long id = Long.parseLong(arrayForBuildDeveloper[0][1]);
        String firstName = arrayForBuildDeveloper[1][1];
        String lastName = arrayForBuildDeveloper[2][1];
        int age = Integer.parseInt(arrayForBuildDeveloper[3][1]);

        Set<Skill> skills = buildSetOfSkills(arrayForBuildDeveloper);
        Account account = buildAccount(id);

        return new Developer(id, firstName, lastName, age, skills, account);
    }

    /**
     * Method for represent Set<Skill> for each Developer as String with all id numbers
     * for example 1,2,3
     * @param developer - current Object of Developer type
     * @return - String numbers of all skills for current Developer
     */
    private String numbersOfSkills(Developer developer) {
        if (developer.getSkills() == null || developer.getSkills().size() == 0)
            return "";

            String numbers =  developer.getSkills().stream()
                            .map(Skill::getID)
                            .map(Object::toString)
                            .collect(Collectors.joining(","));
            return "skills:=" + numbers;
    }

    /**
     * Method restores Set<Skill> from string with id numbers
     * split incoming data by "," and go with simple loop through the array
     * and find object of Skill type with SkillsRepository method - find()
     * @param data - string with id numbers like "1,2,3"
     * @return - Set<Skill> - collection with skills that were successfully found
     * @throws ReadFileException - if skillsRepository cannot be read
     */
    private Set<Skill> buildSetOfSkills(String [][] data) throws ReadFileException {
        if (data[4].length != 2) return null;
        String [] numbers = data[4][1].split(",");
        if (numbers.length == 0) return null;

        SkillsRepository skillsRepository = new SkillsRepository();
        Set<Skill> skills = new HashSet<>();
        for (int i = 0; i < numbers.length; i++) {
            Optional<Skill> skill = skillsRepository.find(Long.parseLong(numbers[i]));
            skill.ifPresent(skills::add);
        }
        return skills;
    }

    private Account buildAccount(Long id) throws ReadFileException {
        AccountRepository accountRepository = new AccountRepository();
        Optional<Account> account = accountRepository.find(id);
        return account.orElse(null);
    }

    /**
     * Method accepts and represents object of Developer type in String format
     * to write these data to the repository file as text
     * @param object - current Developer object
     * @return - String with all required data from incoming object
     */
    private String objectToRepositoryFormat(Developer object) {
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
                    .append(numbersOfSkills(object)).append("\n")
                    .append("}\n");

        return dataToWrite.toString();
    }
}
