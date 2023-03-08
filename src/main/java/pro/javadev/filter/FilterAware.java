package pro.javadev.filter;

public interface FilterAware<F extends Filter<?>> {

    void addFilter(F filter);

    void removeFilter(Class<? extends F> filterClass);

    void clearFilters();

}
