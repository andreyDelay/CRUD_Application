package utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

public class DeveloperUtil {

    public static <T,V,K> Collector<T,?, Optional<Map<K,V>>> devCollector(
                                                            Predicate<T> checkString,
                                                            Function<? super T,? extends K> keyMapper,
                                                            Function<? super List<T>,? extends V> valueMapper,
                                                            Predicate<T> predictBorder)
    {
        class TempDeveloper {
            boolean startNewObj;
            boolean id;

            K key;

            final Map<K,V> storage = new ConcurrentHashMap<>();
            final List<T> fields = new ArrayList<>();

            void add(T t) {
                if (predictBorder.test(t) && !startNewObj) {
                    startNewObj = id = true;
                } else if (!predictBorder.test(t) && id && startNewObj) {
                    key = keyMapper.apply(t);
                    id = false;
                } else if (!predictBorder.test(t) && startNewObj) {
                    fields.add(t);
                } else if (predictBorder.test(t) && startNewObj) {
                    storage.put(key,valueMapper.apply(fields));
                    fields.clear();
                    startNewObj = false;
                }
            }

            TempDeveloper combine(TempDeveloper other) {
                if (other.storage.size() == 0) return this;
                if (this.storage.size() == 0) return other;
                this.storage.putAll(other.storage);
                return this;
            }

            Optional<Map<K,V>> result() {
                if (this.storage.size() != 0)
                    return Optional.of(this.storage);
                return Optional.empty();
            }
        }
        return Collector.of(
                TempDeveloper::new,
                TempDeveloper::add,
                TempDeveloper::combine,
                TempDeveloper::result
        );
    }
}
