package pro.javadev.bean.creation;

import pro.javadev.bean.BeanDependency;
import pro.javadev.bean.BeanFactory;
import pro.javadev.bean.ObjectCreationException;
import pro.javadev.bean.definition.BeanDefinition;
import pro.javadev.bean.definition.ConstructorBeanDefinition;

import java.util.List;

import static pro.javadev.ReflectionUtils.instantiate;

public class ConstructorBeanCreationStrategy extends AbstractBeanCreationStrategy {

    @Override
    public Object createBean(BeanDefinition definition, BeanFactory factory) {
        ConstructorBeanDefinition beanDefinition = (ConstructorBeanDefinition) definition;
        List<BeanDependency>      dependencies   = beanDefinition.getBeanDependencies();
        Object[]                  arguments      = new Object[0];

        if (!dependencies.isEmpty()) {
            try {
                arguments = getArguments(dependencies, factory);
            } catch (RuntimeException exception) {
                throw new ObjectCreationException(
                        "UNABLE TO CREATE BEAN VIA CONSTRUCTOR STRATEGY FOR BEAN TYPE: " + definition.getBeanClass(), exception);
            }
        }

        return instantiate(beanDefinition.getConstructor(), arguments);
    }

    @Override
    public boolean canCreate(BeanDefinition definition) {
        return ConstructorBeanDefinition.class.isAssignableFrom(definition.getClass());
    }
}
