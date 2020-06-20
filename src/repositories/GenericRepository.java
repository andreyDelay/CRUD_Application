package repositories;

import java.util.Set;

public interface GenericRepository<T, ID> {

    void writeData(Set<T> obj);

    Set<T> readData();


//    Set<T> initObjectsCollection();
//
//    void writeObjectCollection(Set<T> objectsCollection);

}
