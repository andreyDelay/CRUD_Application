package repositories.interfaces;


import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface GenericRepository<T, ID> {

    boolean save(T obj) throws Exception;

    Optional<T> find(ID id);

    Map<ID,T> findAll();

    boolean saveAll(List<T> list) throws Exception;

    boolean update(ID id, T obj) throws Exception;

    boolean delete(ID id) throws Exception;

}
