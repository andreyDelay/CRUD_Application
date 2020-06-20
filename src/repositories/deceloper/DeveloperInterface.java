package repositories.deceloper;

import model.Developer;
import repositories.GenericRepository;
import java.util.Set;


public interface DeveloperInterface extends GenericRepository<Developer, Long> {
    @Override
    default void writeData(Set<Developer> obj) {

    }

    @Override
    default Set<Developer> readData() {
        return null;
    }
}

