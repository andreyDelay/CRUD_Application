package repositories;

import exeptions.AddDeveloperException;
import model.Account;
import model.Developer;
import repositories.interfaces.DeveloperInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DevelopersAccess implements DeveloperInterface {

    /**
     * Поле хранит путь к файлу
     */
    private final Path repoPath = Paths.get("src\\resources\\developers.txt");
    /**
     * Список разработчиков
     */
    private List<Developer> developers = new ArrayList<>();
    /**
     * Имя аккаунта, для которого необходимо найти данные в базе
     */
    private String accountName;

    public DevelopersAccess(String accountName) throws AddDeveloperException {
        this.accountName = accountName;
        readFile();
    }

    /**
     * Метод сохраняет изменения у пользователя в репозитории
     * @param obj - пользователь, которого необходимо сохранить
     * @return - true если сохранение прошло успешно
     * @throws AddDeveloperException - если не удалось сохранить
     */
    @Override
    public boolean save(Developer obj) throws AddDeveloperException {
        StringBuilder data = new StringBuilder();
        data.append("{")
            .append("\n")
            .append("\t")
            .append(obj.getId()).append(",")
            .append(obj.getAccount().getAccountName()).append(",")
            .append(obj.getFirstName())
            .append(obj.getLastName())
            .append(obj.getAge());

        return writeFile(data.toString());
    }

    /**
     * Метод не используется
     * @param id
     * @return
     */
    @Override
    public Optional<Developer> find(Long id) {
        if (developers.size() != 0) {
            return Optional.of(developers.get(0));
        }
        return Optional.empty();
    }

    /**
     * Возвращает отображение пользователей
     * @return
     */
    @Override
    public Map<Long, Developer> findAll() {
        Map<Long,Developer> devOps= developers.stream()
                                        .collect(
                                        Collectors.toMap(
                                        Developer::getId,
                                        Function.identity(),
                                        (dev1,dev2) -> dev1,
                                        HashMap::new));
        return devOps;
    }

    /**
     * Не используется
     * @param list
     * @return
     * @throws Exception
     */
    @Override
    public boolean saveAll(List<Developer> list) {
        return false;
    }

    /**
     * Не используется
     * @param aLong
     * @param obj
     * @return
     * @throws Exception
     */
    @Override
    public boolean update(Long aLong, Developer obj) {
        return false;
    }

    /**
     * Не используется
     * @param aLong
     * @return
     * @throws Exception
     */
    @Override
    public boolean delete(Long aLong) {
        return false;
    }

    /**
     * Метод получает данные из репозитория
     * @throws AddDeveloperException - если не удалось прочитать файл
     */
    private void readFile() throws AddDeveloperException {
        try (Stream<String> stream = Files.lines(repoPath)){

            Iterator<String> iterator = stream.iterator();
            while (iterator.hasNext()) {
                String currentLine = iterator.next();
                if (currentLine.equals("{")) {
                    currentLine = iterator.next();
                    if (currentLine.contains(accountName)) {
                        String[] arr = currentLine.split(",");
                        developers.add(new Developer(
                                Long.parseLong(arr[0].trim()),
                                arr[2],
                                arr[3],
                                Integer.parseInt(arr[4].trim()),
                                new Account(accountName)));
                    }
                }
            }
//            allLines.stream().collect(DeveloperUtil.devCollector(
//                                                border -> (border.equals("{") || border.equals("}")),
//                                                Long::parseLong,
//                                                Account::new,
//                                                data ->  data.size() == 4,
//                                                (list, account) -> new Developer(
//                                                                                list.get(0),
//                                                                                list.get(1),
//                                                                                Integer.parseInt(list.get(1)),
//                                                                                account),
//                                                Developer::new));
//            allLines.stream().collect(DeveloperUtil.skillsCollector(
//                                    str -> str.contains("skills:"),
//                                    str -> {
//                                        String [] tmpA = str.replaceFirst("skills: ","").split(",");
//                                        Set<Skill> tmpS = new HashSet<>();
//                                        for (int i = 0; i < tmpA.length; i++) {
//                                            tmpS.add(skills.get(Long.parseLong(tmpA[i])));
//                                        }
//                                        return tmpS;
//                                    }));
        } catch (IOException e) {
            throw new AddDeveloperException(e.getMessage());
        }
    }

    /**
     * Метод записывает строковое представление данных о текущем пользователе
     * @param data - даные о текущем пользователе
     * @return - true если запись данных в файл прошла успешно
     * @throws AddDeveloperException - если не удалось записать данные в файл
     */
    private boolean writeFile(String data) throws AddDeveloperException {
        byte [] bytes = data.getBytes();
        try {
            Files.write(repoPath, bytes,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new AddDeveloperException(e.getMessage());
        }
        return true;
    }
}
