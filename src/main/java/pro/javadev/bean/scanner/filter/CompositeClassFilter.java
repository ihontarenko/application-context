package pro.javadev.bean.scanner.filter;

import pro.javadev.filter.FilterAware;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class CompositeClassFilter implements ClassFilter, FilterAware<ClassFilter> {

    private final boolean           anyMatch;
    private final List<ClassFilter> filters = new ArrayList<>();

    public CompositeClassFilter(ClassFilter... filters) {
        this(true, filters);
    }

    public CompositeClassFilter(boolean anyMatch, ClassFilter... filters) {
        this.filters.addAll(Arrays.asList(filters));
        this.anyMatch = anyMatch;
    }

    @Override
    public void addFilter(ClassFilter filter) {
        filters.add(filter);
    }

    @Override
    public void removeFilter(Class<? extends ClassFilter> filterClass) {
        filters.removeIf(filter -> filter.getClass().isAssignableFrom(filterClass));
    }

    @Override
    public void clearFilters() {
        filters.clear();
    }

    @Override
    public boolean accept(Class<?> object) {
        Stream<ClassFilter>    stream    = filters.stream();
        Predicate<ClassFilter> predicate = filter -> filter.accept(object);

        return anyMatch ? stream.anyMatch(predicate) : stream.allMatch(predicate);
    }

}
