package pro.javadev.filter;

import java.util.function.Predicate;

public interface Filter<T> extends Predicate<T> {

    boolean accept(T object);

    default boolean test(T object) {
        return accept(object);
    }

}
