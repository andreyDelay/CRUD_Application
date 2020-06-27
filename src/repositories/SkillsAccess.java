package repositories;

import exeptions.SkillException;
import model.Skill;
import repositories.interfaces.SkillInterface;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class SkillsAccess implements SkillInterface {

    /**
     * Поле для хранения пути к заданному репозиторию
     */
    private final Path repoPath = Paths.get("src\\resources\\skills.txt");
    /**
     * Поле для хранения объектов типа Skill в структуре данных типа ArrayList()
     */
    private List<Skill> skills = new ArrayList<>();
    /**
     * поле, хранящее заданный разделитель ID и Name при представлении объектов
     * в виде строк, непосредственно перед записью в репозиторий repoPath
     */
    private final String separator = "---";

    /**
     * Конструктор призван для инициализации списка всех объектов их файла repoPath в private List<Skill>
     * @throws IOException - пробрасывает исключение из метода readFile() если не удалось прочитать данные
     */
    public SkillsAccess() throws SkillException {
        readFile();
    }

    /**
     * Метод добавляет в ремозиторий новый объект типа Skill
     * @param obj - переданный от пользователя новый объект Skill
     * @return возвращает true если удалось записать в файл коллекцию объектов с вновь добавленным obj
     * @throws IOException пробрасывает исключение из метода writeFile если не удалось сохранить данные в файле
     * по пути repoPath
     */
    @Override
    public boolean save(Skill obj) throws SkillException {
        Long incrementedID = generateID();
        skills.add(new Skill(obj.getSkillName(), incrementedID));

        return writeFile(skills);
    }

    /**
     * Метод ищет заданный объект в списке объектов типа List<Skill>
     * по входящему идентефикатору
     * @param id - уникальный идентификатор объекта для поиска Объекта в списке
     * @return - Optional<Skill>
     */
    @Override
    public Optional<Skill> find(Long id) {
        return
                skills.stream()
                .filter(skill -> skill.getID().equals(id))
                .findFirst();
    }

    /**
     * Метод осуществляет поиск объекта типа Skill в списке List<Skill> и удаляет его
     * после чего данные записываются непосредственно в файл repoPath
     * @param id - идентификатор для поиска объекта
     * @return - true если удалось найти и удалить объект
     * @throws IOException - если не удалось записать новые данные в файл repoPath
     */
    @Override
    public boolean delete(Long id) throws SkillException {
        Optional<Skill> foundSkill = skills.stream()
                                            .filter(skill -> skill.getID().equals(id))
                                            .findFirst();
        if (!foundSkill.isPresent()) return false;
        skills = skills.stream()
                .filter(skill -> !skill.getID().equals(id))
                .collect(Collectors.toList());

        return writeFile(skills);
    }


    @Override
    public boolean saveAll(List<Skill> list) throws SkillException {
        for (Skill skill : list)
            save(skill);

        return writeFile(skills);
    }

    @Override
    public boolean update(Long id, Skill newObj) throws SkillException {
      skills = skills.stream()
                .filter(skill -> skill.getID().equals(id))
                .map(oldSkill -> new Skill(newObj.getSkillName(), id))
                .collect(Collectors.toList());

        return writeFile(skills);
    }

    /**
     * Метод возвращает отображение, где ключами является уникальный идентефикатор объекта Skill - Long
     * а значением являются объекты типа Skill
     * @return HashNap<ID, Skill>
     */
    @Override
    public Map<Long, Skill> findAll() {
        Map<Long, Skill> allSkills = skills.stream()
                                        .collect(Collectors.toMap(
                                        Skill::getID,
                                        Function.identity(),
                                        (name1, name2) -> name2,
                                        HashMap::new));
        return allSkills;
    }

    /**
     * Метод перебирает список строковых
     * затем обработкой стрима получаем максимальный ID
     * @return максимальный ID+1 на данный момент
     */
    private Long generateID() {
        if (skills.size() != 0) {
            Optional<Skill> maxID = skills.stream()
                                    .max(Comparator.comparing(Skill::getID));

            return (maxID.get().getID() + 1L);
        }
        return 1L;
    }

    /**
     * Метод вызывается при инициализации данного объекта
     * считывает файл по пути @repoPath преобразует считанные строки в List<Skill>
     * заполняет приватный список List<Skill>
     */
    private void readFile() throws SkillException{
        try(Stream<String> stream = Files.lines(repoPath))
        {
            skills = stream.map(this::skillMapper)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
        } catch (IOException e) {
            throw new SkillException(e.getMessage());
        }
    }

    /**
     * Метод записывает перечень строк в файл, при удачной записи возвращает значение true
     * при ином случае генерирует исключение типа IOException
     * @param data List<String> содержащий строковые представления объектов типа Skill
     */
    private boolean writeFile(List<Skill> data) throws SkillException {
            byte [] bytes =
                            data.stream()
                            .map(this::skillToFileFormat)
                            .collect(Collectors.joining("\n"))
                            .getBytes();
            try {
                Files.write(repoPath, bytes,
                        StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING);
            } catch (IOException e) {
                throw new SkillException(e.getMessage());
            }

        return true;
    }

    /**
     * Метод получает на вход строку из репозитория и превращает её в объект типа Skill
     * если входящая строка не соответствует задуманному формату - возвращает null
     * @param line - строка из репозитория
     * @return - объект типа Skill
     */
    private Skill skillMapper(String line) {
        String [] tmp = line.split(separator);
        if (tmp.length != 2) return null;

        long currentID = Long.parseLong(tmp[0]);

        return new Skill(tmp[1], currentID);
    }

    /**
     * Метод получает на вход объект типа Skill и представляет его в формате, который задуман для
     * хранения в репозитории
     * @param skill - объект, который необходимо представить в формате хранения в репозитории
     * @return возвращает строковое представление объекта
     */
    private String skillToFileFormat(Skill skill) {
        StringBuilder stringSkillRepresentation = new StringBuilder();

        stringSkillRepresentation
                                .append(skill.getID())
                                .append(separator)
                                .append(skill.getSkillName());

        return stringSkillRepresentation.toString();
    }

}
