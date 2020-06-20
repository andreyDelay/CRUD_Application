package controllers;
import exeptions.AddAccountException;
import exeptions.AddSkillException;

import java.util.*;
public interface GenericController<T, E extends Exception> {

    T getEntity(Long id) throws Exception; //метод для получения текушей сушности

    boolean addNewEntity(T entity) throws Exception; //метод для добавления новой сущности

    void updateEntity(T entity); //обновление данных

    T deleteEntity(T entity); //удаление сущности

    Set<T> startApplication();

    void endApplication(Set<T> data); //метод для завершения приложения и автозаписи в репозиторий



}
