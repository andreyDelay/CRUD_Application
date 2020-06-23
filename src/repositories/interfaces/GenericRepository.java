package repositories.interfaces;


import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GenericRepository<T, ID> {

    boolean save(T obj) throws IOException;

    Optional<T> find(ID id);

    List<T> findAll() throws IOException;

    boolean saveAll(List<T> list) throws IOException;

    boolean update(ID id) throws IOException;

    boolean delete(ID id) throws IOException;

    Map<ID,T> mapOfAll();

}
