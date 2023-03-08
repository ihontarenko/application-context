package pro.javadev.bean.creation;

import pro.javadev.bean.AbstractBeanCreationStrategy;
import pro.javadev.bean.BeanFactory;
import pro.javadev.bean.definition.BeanDefinition;
import pro.javadev.bean.definition.MethodBeanDefinition;

public class MethodBeanCreationStrategy extends AbstractBeanCreationStrategy {

    @Override
    public Object createBean(BeanDefinition definition, BeanFactory factory) {
        MethodBeanDefinition beanDefinition = (MethodBeanDefinition) definition;


        return null;
    }

    @Override
    public boolean canCreate(BeanDefinition definition) {
        return MethodBeanDefinition.class.isAssignableFrom(definition.getClass());
    }
}
