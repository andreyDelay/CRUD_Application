package repositories.skill;

import model.Skill;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class SkillsAccess implements SkillInterface {

    private Path repoPath = Paths.get("src\\resources\\skills.txt");
    private Long currentID;
    private List<String> skills = new ArrayList<>();


    @Override
    public void addEntity(Skill obj) {
        currentID = generateID();
        obj.setID(currentID);
        byte [] bytes = obj.toString().getBytes();
        try {
            Files.write(repoPath, bytes,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Не удалось записать навык в файл: " + e);
        }
    }


    @Override
    public Skill getByID(Long id) {
        readLines();
        Iterator<String> iter = skills.stream().iterator();
        String currentLine;
        while (iter.hasNext()) {
            currentLine = iter.next();
            if (currentLine.equals("ID: " + id)) {
                return generateSkill(iter, currentLine);
            }
        }
        return null;
    }



    @Override
    public Skill getByName(String name) {
        readLines();
        Iterator<String> iter = skills.stream().iterator();
        String currentLine;
        String previousLine = iter.next();
        while (iter.hasNext()) {
            currentLine = iter.next();
            if (currentLine.equals("skill name: " + name)) {
                return generateSkill(iter, previousLine);
            }
            previousLine = currentLine;
        }
        return null;
    }

    @Override
    public void deleteByID(Long id) {

    }

    @Override
    public void deleteByName(String name) {

    }

    @Override
    public void updateEntity(String name) {

    }

    /**
     * Этот метод считывает стрим строк файла,
     * затем обработкой стрима получаем максимальный ID
     * @return максимальный ID+1 на данный момент
     */
    private Long generateID() {
            readLines();
        List<String> temp =
                skills.stream()
                        .filter((a) -> a.contains("ID: "))
                        .collect(Collectors.toList());

        if (temp.size() != 0) {
            Optional<Long> id =
                    temp.stream()
                            .map((a) -> a.replaceAll("ID: ", ""))
                            .map(Long::parseLong)
                            .max(Long::compareTo);

            if (id.isPresent())
                return (id.get() + 1L);
        }
        return 1L;
    }

    private void readLines() {
        try (Stream<String> lines = Files.lines(repoPath)) {
            skills = lines.collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("Не удалось считать skills.txt: " + e);
        }
    }

    private void writeLines(ArrayList<String> data) {
        byte [] bytes = data.stream().collect(Collectors.joining("\n")).getBytes();
        try {
            Files.write(repoPath, bytes,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE,
                    StandardOpenOption.APPEND);
        } catch (IOException e) {
            System.out.println("Не удалось записать список навыков в файл: " + e);
        }
    }

    private Skill generateSkill(Iterator<String> iter, String id) {
        Long tmpID = Long.parseLong(id.replaceAll("ID: ", ""));
        String tmpName = iter.next().replaceAll("skill name: ","");
        String tmpDescription = iter.next().replaceAll("description: ", "");
        Skill skill = new Skill(tmpName, tmpDescription, tmpID);
        return skill;
    }

}
