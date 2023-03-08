package pro.javadev.filter;

@FunctionalInterface
public interface Filter<T> {
    boolean accept(T object);
}
