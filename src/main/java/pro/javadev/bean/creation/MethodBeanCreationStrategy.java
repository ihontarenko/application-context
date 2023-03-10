package pro.javadev.bean.creation;

import pro.javadev.ReflectionUtils;
import pro.javadev.bean.BeanDependency;
import pro.javadev.bean.BeanFactory;
import pro.javadev.bean.definition.BeanDefinition;
import pro.javadev.bean.definition.ConstructorBeanDefinition;
import pro.javadev.bean.definition.DuplicateBeanDefinitionException;
import pro.javadev.bean.definition.MethodBeanDefinition;

import java.lang.reflect.Method;
import java.util.List;

public class MethodBeanCreationStrategy extends AbstractBeanCreationStrategy {

    @Override
    public Object createBean(BeanDefinition definition, BeanFactory factory) {
        MethodBeanDefinition      beanDefinition    = (MethodBeanDefinition) definition;
        Method                    factoryMethod     = beanDefinition.getBeanFactoryMethod();
        Class<?>                  factoryClass      = factoryMethod.getDeclaringClass();
        ConstructorBeanDefinition factoryDefinition = (ConstructorBeanDefinition) factory.createBeanDefinition(factoryClass);

        try {
            factory.registerBeanDefinition(factoryDefinition);
        } catch (DuplicateBeanDefinitionException e) {
            // no-ops
        }

        List<BeanDependency> dependencies = beanDefinition.getBeanDependencies();
        Object               object       = factory.getBean(factoryDefinition.getBeanName());
        Object[]             arguments    = new Object[0];

        beanDefinition.setBeanFactoryObject(object);

        if (!dependencies.isEmpty()) {
            arguments = getArguments(dependencies, factory);
        }

        return ReflectionUtils.invokeMethod(object, factoryMethod, arguments);
    }

    @Override
    public boolean canCreate(BeanDefinition definition) {
        return MethodBeanDefinition.class.isAssignableFrom(definition.getClass());
    }
}
