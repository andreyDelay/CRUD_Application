package utils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collector;

public interface DeveloperUtil {

     static <T,V,K,A> Collector<T,?, Optional<Map<K,V>>> devCollector(
                                                            Predicate<T> predictBorder,
                                                            Function<? super T,? extends K> keyMapper,
                                                            Function<? super T,? extends A> accountMapper,
                                                            Predicate<? super List<T>> checkData,
                                                            BiFunction<? super List<T>,? super A,? extends V> valueMapper,
                                                            BiFunction<? extends V,? super K,? extends V> setID)
    {
        class TempDeveloper {
            boolean startNewObj;
            boolean id;
            boolean acc;

            K key;
            A account;

            final Map<K,V> storage = new ConcurrentHashMap<>();
            final List<T> fields = new ArrayList<>();

            void add(T t) {

                if (predictBorder.test(t) && !startNewObj) {
                    startNewObj = id = true;
                } else if (!predictBorder.test(t) && id && startNewObj) {
                    key = keyMapper.apply(t);
                    id = false;
                    acc = true;
                } else if (!predictBorder.test(t) && acc) {
                    account = accountMapper.apply(t);
                    acc = false;
                } else if (!predictBorder.test(t) && startNewObj) {
                    fields.add(t);
                } else if (predictBorder.test(t) && startNewObj) {
                    checkData.test(fields);
                    storage.put(key,valueMapper.apply(fields, account));
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

    static <T,V> Collector<T,?,Optional<Set<V>>> skillsCollector(
                                                Predicate<T> skillPredict,
                                                Function<? super T,? extends Set<V>> skillMapper)
    {
            class Skills {
                Set<V> skills = new HashSet<>();

                void add(T t) {
                    if (skillPredict.test(t)) {
                        skills.addAll(skillMapper.apply(t));
                    }
                }

                Skills combine(Skills other) {
                    if (other.skills.size() == 0) return this;
                    if (this.skills.size() == 0) return other;
                    this.skills.addAll(other.skills);
                    return this;
                }

                Optional<Set<V>> result() {
                    if (this.skills.size() != 0)
                        return Optional.of(this.skills);
                    return Optional.empty();
                }
            }

            return Collector.of(
                    Skills::new,
                    Skills::add,
                    Skills::combine,
                    Skills::result
            );
    }
}
