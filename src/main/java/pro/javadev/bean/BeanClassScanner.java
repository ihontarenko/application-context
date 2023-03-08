package pro.javadev.bean;

import pro.javadev.bean.scanner.ClassScanner;
import pro.javadev.bean.scanner.FileSystemClassScanner;
import pro.javadev.bean.scanner.JarClassScanner;
import pro.javadev.bean.scanner.LocalClassScanner;
import pro.javadev.bean.scanner.filter.ClassAnnotatedClassFilter;
import pro.javadev.bean.scanner.filter.ClassFilter;
import pro.javadev.bean.scanner.filter.MethodAnnotatedClassFilter;

import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

public class BeanClassScanner extends ClassScanner {

    public static final  ClassFilter  METHOD_BEAN_ANNOTATED_CLASS_FILTER         = new MethodAnnotatedClassFilter(Bean.class, Modifier.PUBLIC);
    public static final  ClassFilter  CLASS_BEAN_ANNOTATED_CLASS_FILTER          = new ClassAnnotatedClassFilter(Bean.class);
    public static final  ClassFilter  CLASS_CONFIGURATION_ANNOTATED_CLASS_FILTER = new ClassAnnotatedClassFilter(Configuration.class);
    private static final ClassScanner CLASS_SCANNER                              = new BeanClassScanner();

    public BeanClassScanner() {
        addScanner(new FileSystemClassScanner() {{
            addScanner(new JarClassScanner());
            addScanner(new LocalClassScanner());
        }});
    }

    public static Set<Class<?>> scanPackagesWithFilter(ClassFilter filter, Class<?>... baseClasses) {
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
