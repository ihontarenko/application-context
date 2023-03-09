package pro.javadev.bean.creation;

import pro.javadev.bean.BeanDependency;
import pro.javadev.bean.BeanFactory;
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
            arguments = getArguments(dependencies, factory);
        }

        return instantiate(beanDefinition.getConstructor(), arguments);
    }

    @Override
    public boolean canCreate(BeanDefinition definition) {
        return ConstructorBeanDefinition.class.isAssignableFrom(definition.getClass());
    }
}
