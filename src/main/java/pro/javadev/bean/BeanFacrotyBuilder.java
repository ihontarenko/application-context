package pro.javadev.bean;

import pro.javadev.Builder;
import pro.javadev.scanner.filter.type.SubclassClassFilter;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static pro.javadev.ReflectionUtils.findAllAnnotatedMethods;
import static pro.javadev.bean.BeanClassScanner.*;
import static pro.javadev.bean.BeanClassScanner.METHOD_BEAN_ANNOTATED_CLASS_FILTER;

public class BeanFacrotyBuilder implements Builder<BeanFactory> {

    private final Class<?>[] classes;

    public BeanFacrotyBuilder(Class<?>[] classes) {
        this.classes = classes;
    }

    @Override
    public BeanFactory build() {
        BeanFactory factory = new AnnotationBeanFactory();

        // scanning classes @Bean annotated
        Set<Class<?>> candidates = scanBeanClasses(classes);
        for (Class<?> candidate : candidates) {
            // is bean interface founded
            if (candidate.isInterface()) {
                // find all classes implemented current interface
                List<Class<?>> subClasses = new ArrayList<>();
                for (Class<?> subClass : scanPackagesWithFilter(new SubclassClassFilter(candidate), classes)) {
                    if (!candidates.contains(subClass)) {
                        subClasses.add(subClass);
                    }
                }
                // no registration for this definition
                factory.createBeanDefinition(candidate, subClasses);
            } else {
                factory.registerBeanDefinition(
                        factory.createBeanDefinition(candidate)
                );
            }
        }

        // scanning bean factory methods
        for (Class<?> candidate : scanBeanConfigurationClasses(classes)) {
            for (Method method : findAllAnnotatedMethods(candidate, Bean.class)) {
                factory.registerBeanDefinition(
                        factory.createBeanDefinition(method.getReturnType(), method.getDeclaringClass(), method)
                );
            }
        }

        return factory;
    }



    private static Set<Class<?>> scanBeanClasses(Class<?>... classes) {
        return scanPackagesWithFilter(CLASS_BEAN_ANNOTATED_CLASS_FILTER, classes);
    }

    private static Set<Class<?>> scanBeanConfigurationClasses(Class<?>... classes) {
        Set<Class<?>> candidates = new HashSet<>();

        candidates.addAll(scanPackagesWithFilter(CLASS_CONFIGURATION_ANNOTATED_CLASS_FILTER, classes));
        candidates.addAll(scanPackagesWithFilter(METHOD_BEAN_ANNOTATED_CLASS_FILTER, classes));

        return candidates;
    }

}
