package pro.javadev.scanner;

import pro.javadev.filter.FilterAware;
import pro.javadev.filter.FilteringMode;
import pro.javadev.scanner.filter.ClassFilter;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toSet;

public class ClassScanner extends Scanner.DefaultClassScanner implements FilterAware<ClassFilter> {

    private static final ClassScanner CLASS_SCANNER;

    static {
        CLASS_SCANNER = new ClassScanner();
        CLASS_SCANNER.addScanner(new ResourcesClassScanner() {{
            addScanner(new JarClassScanner());
            addScanner(new LocalClassScanner());
        }});
    }

    private final List<Predicate<Class<?>>> filters = new ArrayList<>();
    private       FilteringMode             mode    = FilteringMode.AND;

    public static ClassScanner getDefaultScanner() {
        return CLASS_SCANNER;
    }

    @Override
    public Set<Class<?>> scan(String name, ClassLoader loader) {
        Set<Class<?>> classes = new HashSet<>();

        for (Scanner<Class<?>> scanner : scanners) {
            classes.addAll(scanner.scan(name, loader));
        }

        return classes.stream().filter(this::applyFilter).collect(toSet());
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
    public FilteringMode getFilteringMode() {
        return mode;
    }

    @Override
    public void setFilteringMode(FilteringMode mode) {
        this.mode = mode;
    }

    private boolean applyFilter(Class<?> clazz) {
        Stream<Predicate<Class<?>>>   stream = filters.stream();
        Optional<Predicate<Class<?>>> filter = mode == FilteringMode.AND
                ? stream.reduce(Predicate::and) : stream.reduce(Predicate::or);

        return filter.orElse(object -> false).test(clazz);
    }

}
