package pro.javadev.bean.creation;

import pro.javadev.bean.BeanFactory;
import pro.javadev.bean.definition.BeanDefinition;

public interface BeanCreationStrategy {

    Object createBean(BeanDefinition definition, BeanFactory factory);

    boolean canCreate(BeanDefinition definition);

}
