package repositories;

import model.Developer;
import repositories.interfaces.DeveloperInterface;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DevelopersAccess implements DeveloperInterface {

    private final Path repoPath = Paths.get("src\\resources\\developers.txt");

    private List<Developer> developers = new ArrayList<>();

    public DevelopersAccess() {
        readFile();
    }
    @Override
    public boolean save(Developer obj) throws Exception {
        return false;
    }

    @Override
    public Optional<Developer> find(Long aLong) {
        return Optional.empty();
    }

    @Override
    public Map<Long, Developer> findAll() {
        return null;
    }

    @Override
    public boolean saveAll(List<Developer> list) throws Exception {
        return false;
    }

    @Override
    public boolean update(Long aLong, Developer obj) throws Exception {
        return false;
    }

    @Override
    public boolean delete(Long aLong) throws Exception {
        return false;
    }

    private void readFile() {

    }
}
