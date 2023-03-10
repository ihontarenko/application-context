package pro.javadev.bean.scanner;

import pro.javadev.bean.definition.BeanDefinition;
import pro.javadev.bean.scanner.filter.AccessModifierClassFilter;
import pro.javadev.bean.scanner.filter.SubclassClassFilter;

import java.lang.reflect.Modifier;
import java.util.Set;

public class ClassScannerExample {

    public static void main(String... arguments) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
//        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        ClassScanner        scanner          = new ClassScanner();
        JrtClassScanner        jrtClassScanner     = new JrtClassScanner("java.base");
        FileSystemClassScanner defaultClassScanner = new FileSystemClassScanner();

        defaultClassScanner.addScanner(new LocalClassScanner());
        defaultClassScanner.addScanner(new JarClassScanner());

        scanner.addScanner(jrtClassScanner);
        scanner.addScanner(defaultClassScanner);

//        scanner.addIncludeFilter(new RegexClassFilter(".*\\$.*", false));
//        scanner.addIncludeFilter(new RegexClassFilter(".*Zip.*", true));
//        scanner.addIncludeFilter(new RegexClassFilter(".*package-info.*", true));
//        scanner.addIncludeFilter(Class::isEnum);
//        scanner.addFilter(Class::isInterface);
        scanner.addFilter(Class::isAnnotation);
//        scanner.addIncludeFilter(new AnnotationClassFilter(FunctionalInterface.class));
//        scanner.addIncludeFilter(new SubclassClassFilter(Serializable.class));
//        scanner.addFilter(new SubclassClassFilter(BeanDefinition.class));
//        scanner.addFilter(new AccessModifierClassFilter(Modifier.ABSTRACT, true));

        Set<Class<?>> classes = scanner.scan("pro.javadev", loader);

        for (Class<?> type : classes) {
            System.out.println(type);
        }

        System.out.println("total classes found: " + classes.size());
    }

}
