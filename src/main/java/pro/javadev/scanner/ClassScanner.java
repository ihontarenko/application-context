package pro.javadev.scanner;

import pro.javadev.filter.FilterAware;
import pro.javadev.scanner.filter.ClassFilter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ClassScanner extends Scanner.DefaultClassScanner implements FilterAware<ClassFilter> {

    private static final ClassScanner CLASS_SCANNER;

    static {
        CLASS_SCANNER = new ClassScanner();
        CLASS_SCANNER.addScanner(new FileSystemClassScanner() {{
            addScanner(new JarClassScanner());
            addScanner(new LocalClassScanner());
        }});
    }

    private final List<ClassFilter> filters = new ArrayList<>();

    @Override
    public Set<Class<?>> scan(String name, ClassLoader loader) {
        Set<Class<?>> classes = new HashSet<>();

        for (Scanner scanner : getScanners()) {
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

    private boolean applyFilter(Class<?> clazz) {
        boolean result = true;

        for (ClassFilter filter : filters) {
            result = (result && filter.accept(clazz));
        }

        return result;
    }

    public static ClassScanner getDefaultScanner() {
        return CLASS_SCANNER;
    }

}
