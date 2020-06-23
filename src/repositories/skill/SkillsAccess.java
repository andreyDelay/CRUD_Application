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
        readLines();
        skills.add(currentID + "-" + obj.getSkillName());
        writeLines(skills);
    }


    @Override
    public Skill getByID(Long id) {
        readLines();
        Optional<String[]> rid = skills.stream()
                .map((a) -> a.split("-"))
                .filter((a) -> a[1].equals(""+id))
                .findFirst();
        if (rid.isPresent()) {
            String [] tmp = rid.get();
            return new Skill(tmp[1], Long.parseLong(tmp[0]));
        }
        return null;
    }



    @Override
    public Skill getByName(String name) {
        readLines();
            Optional<String[]> rn = skills.stream()
                    .map((a) -> a.split("-"))
                    .filter((arr) -> arr[1].equalsIgnoreCase(name))
                    .findFirst();
            if (rn.isPresent()) {
                String[] tmp = rn.get();
                return new Skill(tmp[1], Long.parseLong(tmp[0]));
            }
        return null;
    }

    @Override
    public void deleteByID(Long id) {
        readLines();
        List<String> newData = skills.stream()
                .map((a) -> a.split("-"))
                .filter((a) -> (a != null && a.length ==2))
                .filter((arr) -> Long.parseLong(arr[0]) != id)
                .map((arr) -> arr[0] + "-" + arr[1])
                .collect(Collectors.toList());
        writeLines(newData);
    }

    @Override
    public void deleteByName(String name) {
        readLines();
        List<String> newData = skills.stream()
                .map((a) -> a.split("-"))
                .filter((a) -> (a != null && a.length ==2))
                .filter((arr) -> (!arr[1].equalsIgnoreCase(name)))
                .map((arr) -> arr[0] + "-" + arr[1])
                .collect(Collectors.toList());
        writeLines(newData);
    }

    @Override
    public void updateEntity(String name) {

    }

    @Override
    public void deleteAll() {
        writeLines(Arrays.asList());
    }

    /**
     * Этот метод вызывет метод readLines() для обновления коллекции из файла
     * затем обработкой стрима получаем максимальный ID
     * @return максимальный ID+1 на данный момент
     */
    private Long generateID() {
            readLines();
        if (skills.size() != 0) {
            Optional<Skill> maxID =
                    skills.stream()
                            .map((a) -> (a.length() > 2) ? a.split("-") : new String[]{})
                            .map((arr) -> arr.length == 2 ?
                                                new Skill(
                                                arr[1], //skill_name
                                                Long.parseLong(arr[0])) //skill_ID
                                                : null)
                            .filter((a) -> a != null)
                            .max(Comparator.comparing(Skill::getID));
            if (maxID.isPresent())
                return (maxID.get().getID() + 1L);
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

    private void writeLines(List<String> data) {
        byte [] bytes = data.stream().collect(Collectors.joining("\n")).getBytes();
        try {
            Files.write(repoPath, bytes,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            System.out.println("Не удалось записать список навыков в файл: " + e);
        }
    }

}
