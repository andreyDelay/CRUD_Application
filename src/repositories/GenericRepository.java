package repositories;



public interface GenericRepository<T, ID> {

    void addEntity(T obj);

    T getByID(ID id);

    T getByName(String name);

    void deleteByID(ID id);

    void deleteByName(String name);

    void updateEntity(String name);

}
