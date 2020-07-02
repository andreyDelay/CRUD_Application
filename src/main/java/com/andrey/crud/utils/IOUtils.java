package com.andrey.crud.utils;

import com.andrey.crud.exeptions.ReadFileException;
import com.andrey.crud.exeptions.WriteFileException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IOUtils {

    public static List<String> readFile(Path filepath) throws ReadFileException {
        try (Stream<String> stream = Files.lines(filepath)){
            List<String> rows = stream.filter(row -> row.length() != 0)
                                        .collect(Collectors.toList());
            return rows;
        } catch (IOException e) {
            throw new ReadFileException("Не удалось прочитать файл в методе readFile()." + e.getMessage());
        }
    }

    public static boolean writeFile(String dataToWrite, Path filepath, StandardOpenOption option) throws WriteFileException {
        byte [] bytes = dataToWrite.getBytes();
        try {
            Files.write(filepath, bytes,
                    StandardOpenOption.CREATE,
                    option);
            return true;
        } catch (IOException e) {
            throw new WriteFileException("Не удалось записать данные в файл в методе writeFile()." + e.getMessage());
        }
    }

    public static Long generateID(Path sourceFile, String separator) throws ReadFileException {
        try {
            List<String> rows = readFile(sourceFile);
            Optional<Long> result = rows.stream()
                    .filter(row -> row.length() > 5)
                    .filter(row -> row.contains("id"))
                    .map(row -> row.split(separator))
                    .map(array -> Long.parseLong(array[1]))
                    .max(Long::compareTo);
            return result.map(id -> id + 1L).orElse(1L);
        }  catch (ReadFileException e) {
            throw new ReadFileException("Ошибка при обращении к readFile() из метода generateID()." + e.getMessage());
        }

    }
//******************************************************* Не используется!
    public static <T,V> Collector<T,?, Optional<V>> developerMapper(
                                                    Predicate<T> startFinder,
                                                    Function<? super T,? extends V> objectMapper,
                                                    Predicate<V> valueFilter)
    {

        class Dev {
            boolean start;
            V object;

            void add(T t) {
                if (startFinder.test(t) && !start) {
                    start = true;
                } else if (!startFinder.test(t) && start) {
                    V tmp = objectMapper.apply(t);
                    if (valueFilter.test(tmp))
                            object = tmp;
                    start = false;
                }
            }

            Dev combine(Dev other) {
                if (Objects.isNull(this.object)) return other;
                if (Objects.isNull(other.object)) return this;
                throw new IllegalStateException("Конфликт объектов при чтении файла");
            }

            Optional<V> result() {
                if (Objects.nonNull(this.object))
                    return Optional.of(this.object);

                return Optional.empty();
            }
        }
        return Collector.of(
                Dev::new,
                Dev::add,
                Dev::combine,
                Dev::result);
    }

}
