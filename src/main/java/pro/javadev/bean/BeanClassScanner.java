package pro.javadev.bean;

import pro.javadev.scanner.ClassScanner;
import pro.javadev.scanner.filter.type.ClassAnnotatedClassFilter;
import pro.javadev.scanner.filter.type.TypeFilter;
import pro.javadev.scanner.filter.type.MethodAnnotatedClassFilter;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class BeanClassScanner extends ClassScanner {

    public static final  TypeFilter   METHOD_BEAN_ANNOTATED_CLASS_FILTER         = new MethodAnnotatedClassFilter(Bean.class, Modifier.PUBLIC);
    public static final  TypeFilter   CLASS_BEAN_ANNOTATED_CLASS_FILTER          = new ClassAnnotatedClassFilter(Bean.class);
    public static final  TypeFilter   CLASS_CONFIGURATION_ANNOTATED_CLASS_FILTER = new ClassAnnotatedClassFilter(BeanConfiguration.class);
    private static final ClassScanner CLASS_SCANNER                              = ClassScanner.getDefaultScanner();

    public static Set<Class<?>> scanPackagesWithFilter(TypeFilter filter, Class<?>... baseClasses) {
        Set<Class<?>> classes = new HashSet<>();
        ClassLoader   loader  = Thread.currentThread().getContextClassLoader();

        CLASS_SCANNER.clearFilters();
        CLASS_SCANNER.addFilter(filter);

        for (Class<?> baseClass : baseClasses) {
            classes.addAll(CLASS_SCANNER.scan(baseClass.getPackageName(), loader));
        }

        return classes;
    }

}
