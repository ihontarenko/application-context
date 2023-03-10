package pro.javadev.bean.definition;

import pro.javadev.bean.BeanDependency;
import pro.javadev.bean.Scope;
import pro.javadev.bean.creation.BeanCreationStrategy;
import pro.javadev.bean.BeanCreationType;

import java.util.List;

public interface BeanDefinition {

    String getBeanName();

    void setBeanName(String name);

    Class<?> getBeanClass();

    void setBeanClass(Class<?> type);

    void setBeanScope(Scope scope);

    default boolean isSingleton() {
        return getBeanScope() == Scope.SINGLETON || getBeanScope() == Scope.NON_BEAN;
    }

    default boolean isPrototype() {
        return !isSingleton();
    }

    Scope getBeanScope();

    List<BeanDependency> getBeanDependencies();

    BeanCreationType getBeanCreationType();

    <T> T getBeanInstance();

    void setBeanInstance(Object instance);

    BeanCreationStrategy getBeanCreationStrategy();

    void setBeanCreationStrategy(BeanCreationStrategy strategy);

}
