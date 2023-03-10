package pro.javadev.bean;

import pro.javadev.bean.context.ApplicationContext;
import pro.javadev.bean.creation.BeanCreationStrategy;
import pro.javadev.bean.creation.ConstructorBeanCreationStrategy;
import pro.javadev.bean.creation.MethodBeanCreationStrategy;
import pro.javadev.bean.creation.SupplierBeanCreationStrategy;
import pro.javadev.bean.definition.*;
import pro.javadev.bean.processor.BeanProcessor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static pro.javadev.ReflectionUtils.findFirstAnnotatedConstructor;
import static pro.javadev.ReflectionUtils.findFirstConstructor;

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
    public <T> T getBean(Class<T> beanType, String beanName) {
        String finalName = beanName;

        if (finalName == null) {
            finalName = getBeanName(beanType);
            finalName = resolveBeanName(beanType, finalName);
        }

        return getBean(finalName);
    }

    @Override
    public <T> T getBean(Class<T> beanType) {
        return getBean(beanType, null);
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
            throw new ObjectCreationException("SPECIFY THE ACTUAL BEAN NAME OF ONE OF " + names + " NAMES FOR TYPE: " + type);
        } else if (names.isEmpty()) {
            throw new ObjectCreationException("NO BEAN NAMES FOR TYPE: (" + type + ") AT ALL");
        }

        return resolvedName;
    }

    @Override
    public List<String> getBeanNames(Class<?> type) {
        return names.computeIfAbsent(type, names -> new ArrayList<>());
    }

    @Override
    public <T> T createBean(BeanDefinition definition) {
        BeanCreationStrategy strategy = resolver.resolve(definition);
        T                    instance = (T) strategy.createBean(definition, this);

        if (instance == null) {
            throw new ObjectCreationException(
                    "UNFORTUNATELY, THE STRATEGY FAILED TO CREATE THE BEAN OF TYPE: " + definition.getBeanClass());
        }

        processors.forEach(processor -> processor.process(instance, getApplicationContext()));

        definition.setBeanInstance(instance);

        return instance;
    }

    @Override
    public BeanDefinition getBeanDefinition(Class<?> type) {
        return getBeanDefinition(getBeanName(type));
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
    public BeanDefinition createBeanDefinition(Class<?> interfaceType, List<Class<?>> subClasses) {
        String                  beanName   = getBeanName(interfaceType);
        InterfaceBeanDefinition definition = new InterfaceBeanDefinition(beanName, interfaceType);

        for (Class<?> subClass : subClasses) {
            BeanDefinition subClassDefinition = createBeanDefinition(subClass);
            subClassDefinition.setParentDefinition(definition);
            definition.addChildDefinition(subClassDefinition);
            registerBeanDefinition(subClassDefinition);
        }

        return definition;
    }

    @Override
    public BeanDefinition createBeanDefinition(Class<?> klass) {
        String                    beanName   = getBeanName(klass);
        ConstructorBeanDefinition definition = new ConstructorBeanDefinition(beanName, klass);

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
        String               beanName   = getBeanName(method);
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
        String beanName = definition.getBeanName();
        if (!definitions.containsKey(beanName)) {
            definitions.put(beanName, definition);
            updateBeanNames(definition);
        } else {
            throw new DuplicateBeanDefinitionException(definition);
        }
    }

    @Override
    public void addBeanProcessor(BeanProcessor processor) {
        this.processors.add(processor);
    }

    @Override
    public ApplicationContext getApplicationContext() {
        return context;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) {
        this.context = context;
    }

    private void updateBeanNames(BeanDefinition definition) {
        List<String>   beanNames        = getBeanNames(definition.getBeanClass());
        String         beanName         = definition.getBeanName();
        BeanDefinition parentDefinition = definition.getParentDefinition();

        beanNames.add(beanName);

        if (parentDefinition != null) {
            beanNames.add(parentDefinition.getBeanName());
            getBeanNames(parentDefinition.getBeanClass()).add(beanName);
        }
    }

    private void processDependencies(List<BeanDependency> dependencies, Parameter parameter) {
        String name = null;

        if (parameter.isAnnotationPresent(Name.class)) {
            name = parameter.getAnnotation(Name.class).value();
        }

        dependencies.add(new NamedDependency(parameter.getType(), name));
    }

}