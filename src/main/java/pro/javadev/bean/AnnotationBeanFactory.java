package pro.javadev.bean;

import pro.javadev.ClassUtils;
import pro.javadev.StringUtils;
import pro.javadev.bean.context.ApplicationContext;
import pro.javadev.bean.creation.BeanCreationStrategy;
import pro.javadev.bean.creation.ConstructorBeanCreationStrategy;
import pro.javadev.bean.creation.MethodBeanCreationStrategy;
import pro.javadev.bean.creation.SupplierBeanCreationStrategy;
import pro.javadev.bean.definition.BeanDefinition;
import pro.javadev.bean.definition.ConstructorBeanDefinition;
import pro.javadev.bean.definition.DuplicateBeanDefinitionException;
import pro.javadev.bean.definition.MethodBeanDefinition;
import pro.javadev.bean.processor.BeanProcessor;
import pro.javadev.bean.scanner.filter.SubclassClassFilter;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.EMPTY_LIST;
import static pro.javadev.ReflectionUtils.*;
import static pro.javadev.bean.BeanClassScanner.*;

public class AnnotationBeanFactory implements BeanFactory {

    private final Map<String, BeanDefinition>  definitions;
    private final Map<String, Object>          beans;
    private final Map<Class<?>, List<String>>  names;
    private final BeanCreationStrategyResolver resolver;
    private final List<BeanProcessor>          processors = new ArrayList<>();
    private       ApplicationContext           context;

    public AnnotationBeanFactory() {
        this.beans = new ConcurrentHashMap<>();
        this.definitions = new ConcurrentHashMap<>();
        this.names = new ConcurrentHashMap<>();
        this.resolver = new BeanCreationStrategyResolver(
                new ConstructorBeanCreationStrategy(), new MethodBeanCreationStrategy(), new SupplierBeanCreationStrategy());
    }

    @Override
    public void scan(Class<?>... classes) {
        // scanning classes @Bean annotated
        Set<Class<?>> candidates = scanBeanClasses(classes);
        for (Class<?> candidate : candidates) {
            // is bean interface founded
            if (candidate.isInterface()) {
                // find all sub-classes
                for (Class<?> subClass : scanPackagesWithFilter(new SubclassClassFilter(candidate), classes)) {
                    if (!candidates.contains(subClass)) {
                        System.out.println(subClass);
                    }
                }
            } else {
                registerBeanDefinition(createBeanDefinition(candidate));
            }
        }

        // scanning bean factory methods
        for (Class<?> candidate : scanBeanConfigurationClasses(classes)) {
            for (Method method : findAllAnnotatedMethods(candidate, Bean.class)) {
                registerBeanDefinition(createBeanDefinition(method.getReturnType(), method.getDeclaringClass(), method));
            }
        }
    }

    @Override
    public <T> T getBean(Class<T> type) {
        String likelyName = createBeanName(type);
        String beanName   = resolveBeanName(type, likelyName);

        return getBean(beanName);
    }

    @SuppressWarnings({"all"})
    @Override
    public <T> T getBean(String name) {
        T bean = (T) beans.get(name);

        if (bean == null) {
            BeanDefinition definition = getBeanDefinition(name);
            bean = createBean(definition);

            if (definition.isSingleton()) {
                beans.put(definition.getBeanName(), bean);
            }
        }

        return bean;
    }

    @Override
    public String resolveBeanName(Class<?> type, String likelyName) {
        String       resolvedName = likelyName;
        List<String> names        = getBeanNames(type);

        if (names.size() == 1) {
            resolvedName = names.get(0);
        } else if (names.size() > 1 && !names.contains(resolvedName)) {
            throw new ObjectCreationException("SPECIFY THE ACTUAL BEAN NAME OF ONE OF "+ names +" NAMES FOR TYPE: " + type);
        } else if (names.isEmpty()) {
            throw new ObjectCreationException("NO BEAN NAMES FOR TYPE: (" + type + ") AT ALL");
        }

        return resolvedName;
    }

    @Override
    public List<String> getBeanNames(Class<?> type) {
        return names.containsKey(type) ? names.get(type) : (List<String>)EMPTY_LIST;
    }

    @Override
    public <T> T createBean(BeanDefinition definition) {
        BeanCreationStrategy strategy = resolver.resolve(definition);
        T                    instance = (T) strategy.createBean(definition, this);

        processors.forEach(processor -> processor.process(instance, getApplicationContext()));

        definition.setBeanInstance(instance);

        return instance;
    }

    @Override
    public BeanDefinition getBeanDefinition(Class<?> type) {
        return getBeanDefinition(createBeanName(type));
    }

    @Override
    public BeanDefinition getBeanDefinition(String name) {
        BeanDefinition definition = definitions.get(name);

        if (definition == null) {
            throw new ObjectCreationException("NO BEAN DEFINITION FOUND FOR NAME: " + name);
        }

        return definition;
    }

    @Override
    public BeanDefinition createBeanDefinition(Class<?> klass) {
        String                    beanName    = createBeanName(klass);
        ConstructorBeanDefinition definition  = new ConstructorBeanDefinition(beanName, klass);

        Constructor<?> constructor;

        try {
            constructor = findFirstAnnotatedConstructor(klass, BeanContructor.class);
        } catch (Exception annotatedConstructorException) {
            try {
                constructor = findFirstConstructor(klass);
            } catch (Exception defaultConstructorException) {
                RuntimeException exception = new ObjectCreationException(
                        "NO CONSTRUCTOR WAS FOUND. PLEASE CREATE A DEFAULT CONSTRUCTOR FOR (" + klass + ") AT LEAST");
                defaultConstructorException.addSuppressed(annotatedConstructorException);
                exception.addSuppressed(defaultConstructorException);
                throw exception;
            }
        }

        definition.setConstructor(constructor);

        if (constructor.getParameterCount() != 0) {
            for (Parameter parameter : constructor.getParameters()) {
                processDependencies(definition.getBeanDependencies(), parameter);
            }
        }

        if (klass.isAnnotationPresent(Bean.class)) {
            definition.setBeanScope(klass.getAnnotation(Bean.class).scope());
        } else {
            definition.setBeanScope(Scope.NON_BEAN);
        }

        return definition;
    }

    @Override
    public BeanDefinition createBeanDefinition(Class<?> type, Class<?> klass, Method method) {
        String               beanName   = createBeanName(method);
        MethodBeanDefinition definition = new MethodBeanDefinition(beanName, method.getReturnType());

        definition.setBeanFactoryMethod(method);

        if (method.getParameterCount() != 0) {
            for (Parameter parameter : method.getParameters()) {
                processDependencies(definition.getBeanDependencies(), parameter);
            }
        }

        definition.setBeanScope(method.getAnnotation(Bean.class).scope());

        return definition;
    }

    @Override
    public void registerBeanDefinition(BeanDefinition definition) {
        String   beanName = definition.getBeanName();
        Class<?> beanType = definition.getBeanClass();

        if (!definitions.containsKey(beanName)) {
            definitions.put(beanName, definition);
            names.computeIfAbsent(beanType, names -> new ArrayList<>()).add(beanName);
        } else {
            throw new DuplicateBeanDefinitionException(definition);
        }
    }

    @Override
    public String createBeanName(Class<?> type) {
        String beanName = ClassUtils.getShortName(type);

        if (type.isAnnotationPresent(Bean.class)) {
            Bean annotation = type.getAnnotation(Bean.class);
            if (!annotation.value().isBlank() || !annotation.name().isBlank()) {
                beanName = !annotation.value().isBlank() ? annotation.value() : annotation.name();
            }
        }

        beanName = StringUtils.underscored(beanName, true);

        return beanName;
    }

    @Override
    public String createBeanName(Method method) {
        String beanName = method.getName();

        if (method.isAnnotationPresent(Bean.class)) {
            Bean annotation = method.getAnnotation(Bean.class);
            if (!annotation.value().isBlank() || !annotation.name().isBlank()) {
                beanName = !annotation.value().isBlank() ? annotation.value() : annotation.name();
            }
        }

        beanName = StringUtils.underscored(beanName, true);

        return beanName;
    }

    @Override
    public void addBeanProcessor(BeanProcessor processor) {
        this.processors.add(processor);
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return context;
    }

    private void processDependencies(List<BeanDependency> dependencies, Parameter parameter) {
        String name = null;

        if (parameter.isAnnotationPresent(Name.class)) {
            name = parameter.getAnnotation(Name.class).value();
        }

        dependencies.add(new NamedDependency(parameter.getType(), name));
    }

    private Set<Class<?>> scanBeanClasses(Class<?>... classes) {
        return scanPackagesWithFilter(CLASS_BEAN_ANNOTATED_CLASS_FILTER, classes);
    }

    private Set<Class<?>> scanBeanConfigurationClasses(Class<?>... classes) {
        Set<Class<?>> candidates = new HashSet<>();

        candidates.addAll(scanPackagesWithFilter(CLASS_CONFIGURATION_ANNOTATED_CLASS_FILTER, classes));
        candidates.addAll(scanPackagesWithFilter(METHOD_BEAN_ANNOTATED_CLASS_FILTER, classes));

        return candidates;
    }

}