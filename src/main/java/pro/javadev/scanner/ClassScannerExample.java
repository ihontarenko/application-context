package pro.javadev.scanner;

import pro.javadev.filter.FilteringMode;
import pro.javadev.scanner.filter.AccessModifierClassFilter;
import pro.javadev.scanner.filter.IsRegularPathFilter;

import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;

public class ClassScannerExample {

    public static void main(String... arguments) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
//        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        ClassScanner        scanner          = new ClassScanner();
        JrtClassScanner       jrtClassScanner     = new JrtClassScanner("java.base");
        ResourcesClassScanner defaultClassScanner = new ResourcesClassScanner();

        defaultClassScanner.addScanner(new LocalClassScanner());
        defaultClassScanner.addScanner(new JarClassScanner());

        scanner.addScanner(jrtClassScanner);
        scanner.addScanner(defaultClassScanner);

//        scanner.addIncludeFilter(new RegexClassFilter(".*\\$.*", false));
//        scanner.addIncludeFilter(new RegexClassFilter(".*Zip.*", true));
//        scanner.addIncludeFilter(new RegexClassFilter(".*package-info.*", true));
//        scanner.addIncludeFilter(Class::isEnum);
        scanner.addFilter(Class::isInterface);
//        scanner.addFilter(Class::isAnnotation);
//        scanner.addIncludeFilter(new AnnotationClassFilter(FunctionalInterface.class));
//        scanner.addIncludeFilter(new SubclassClassFilter(Serializable.class));
//        scanner.addFilter(new SubclassClassFilter(BeanDefinition.class));
        scanner.addFilter(new AccessModifierClassFilter(Modifier.ABSTRACT, true));

        scanner.setFilteringMode(FilteringMode.OR);

//        Set<Class<?>> classes = scanner.scan("pro.javadev", loader);
        Set<Class<?>> classes = scanner.scan("javax.net", loader);

        for (Class<?> type : classes) {
            System.out.println(type);
        }

        System.out.println("total classes found: " + classes.size());

        FileScanner filesScanner = new FileScanner();

        filesScanner.addFilter(new IsRegularPathFilter());

        System.out.println(
                ClassLoader.getSystemClassLoader().getResource("pro/javadev")
        );

        Set<Path> paths = filesScanner.scan("pro/javadev", ClassLoader.getSystemClassLoader());

        for (Path path : paths) {
            System.out.println(path);
            System.out.println(Files.isRegularFile(path));
        }
    }

}
