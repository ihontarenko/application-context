package pro.javadev.scanner;

import pro.javadev.filter.Filter;
import pro.javadev.filter.FilterAware;
import pro.javadev.filter.FilteringMode;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class AbstractScanner<T> implements Scanner<T>, CompositeScanner<T>, FilterAware<Filter<T>> {

    private final   List<Predicate<T>> filters  = new ArrayList<>();
    private         FilteringMode      mode     = FilteringMode.AND;
    protected final List<Scanner<T>>   scanners = new ArrayList<>();

    @Override
    public void addScanner(Scanner<T> scanner) {
        scanners.add(scanner);
    }

    @Override
    public FilteringMode getFilteringMode() {
        return mode;
    }

    @Override
    public void setFilteringMode(FilteringMode mode) {
        this.mode = mode;
    }

    @Override
    public void addFilter(Filter<T> filter) {
        filters.add(filter);
    }

    @Override
    public void removeFilter(Class<? extends Filter<T>> filterClass) {
        filters.removeIf(predicate -> predicate.getClass().isAssignableFrom(filterClass));
    }

    @Override
    public void clearFilters() {
        filters.clear();
    }

    protected boolean doFilter(T target) {
        Stream<Predicate<T>> stream = filters.stream();
        return ((mode == FilteringMode.AND)
                ? stream.reduce(Predicate::and)
                : stream.reduce(Predicate::or)).orElse(object -> false).test(target);
    }

}
