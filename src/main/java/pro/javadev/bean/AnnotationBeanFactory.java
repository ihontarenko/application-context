package pro.javadev.bean;

import pro.javadev.ClassUtils;
import pro.javadev.ReflectionUtils;
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

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
        this.resolver = new BeanCreationStrategyResolver(new ConstructorBeanCreationStrategy(), new MethodBeanCreationStrategy(), new SupplierBeanCreationStrategy());
    }

    @Override
    public void scan(Class<?>... classes) {
        for (Class<?> candidate : scanBeanClasses(classes)) {
            registerBeanDefinition(createBeanDefinition(candidate));
        }

        for (Class<?> candidate : scanBeanConfigurationClasses(classes)) {
            for (Method method : ReflectionUtils.findAllAnnotatedMethods(candidate, Bean.class)) {
                registerBeanDefinition(createBeanDefinition(method.getReturnType(), method.getDeclaringClass(), method));
            }
        }
    }

    @Override
    public <T> T getBean(Class<T> type) {
        String       name  = createBeanName(type);
        List<String> names = getBeanNames(type);

        if (names.size() > 1) {
            throw new AmbiguousBeanNameException(names, type);
        } else if (names.size() == 1) {
            name = names.get(0);
        }

        return getBean(name);
    }

    @Override
    public <T> T getBean(String name) {
        T bean = (T) beans.get(name);

        if (bean == null) {
            BeanDefinition definition = getBeanDefinition(name);
            bean = createBean(definition);
            beans.put(definition.getBeanName(), bean);
        }

        return bean;
    }

    @Override
    public List<String> getBeanNames(Class<?> type) {
        return names.get(type);
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
    public BeanDefinition createBeanDefinition(Class<?> clazz) {
        String                    beanName    = createBeanName(clazz);
        ConstructorBeanDefinition definition  = new ConstructorBeanDefinition(beanName, clazz);
        Constructor<?>            constructor = ReflectionUtils.findFirstConstructor(clazz);

        try {
            constructor = ReflectionUtils.findFirstAnnotatedConstructor(clazz, Inject.class);
        } catch (ObjectCreationException e) {
            // no-ops
        }

        definition.setConstructor(constructor);

        if (constructor.getParameterCount() != 0) {
            for (Parameter parameter : constructor.getParameters()) {
                processDependencies(definition.getBeanDependencies(), parameter);
            }
        }

        return definition;
    }

    @Override
    public BeanDefinition createBeanDefinition(Class<?> type, Class<?> factoryClass, Method factoryMethod) {
        String               beanName   = createBeanName(factoryMethod);
        MethodBeanDefinition definition = new MethodBeanDefinition(beanName, factoryMethod.getReturnType());

        definition.setBeanFactoryMethod(factoryMethod);

        if (factoryMethod.getParameterCount() != 0) {
            for (Parameter parameter : factoryMethod.getParameters()) {
                processDependencies(definition.getBeanDependencies(), parameter);
            }
        }

        return definition;
    }

    @Override
    public void registerBeanDefinition(BeanDefinition definition) {
        if (!definitions.containsKey(definition.getBeanName())) {
            definitions.put(definition.getBeanName(), definition);
            names.computeIfAbsent(definition.getBeanClass(), names -> new ArrayList<>()).add(definition.getBeanName());
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