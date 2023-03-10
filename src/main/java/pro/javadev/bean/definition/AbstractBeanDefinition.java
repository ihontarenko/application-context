package pro.javadev.bean.definition;

import pro.javadev.bean.BeanDependency;
import pro.javadev.bean.Scope;
import pro.javadev.bean.creation.BeanCreationStrategy;

import java.util.ArrayList;
import java.util.List;

abstract public class AbstractBeanDefinition implements BeanDefinition {

    protected final List<BeanDependency> dependencies;
    protected       String               name;
    protected       Class<?>             type;
    protected       Scope                scope;
    protected       Object               instance = null;
    protected       BeanCreationStrategy strategy = null;

    public AbstractBeanDefinition(String name, Class<?> type) {
        this.name = name;
        this.type = type;
        this.dependencies = new ArrayList<>();
    }

    @Override
    public String getBeanName() {
        return name;
    }

    @Override
    public void setBeanName(String name) {
        this.name = name;
    }

    @Override
    public Class<?> getBeanClass() {
        return type;
    }

    @Override
    public void setBeanClass(Class<?> type) {
        this.type = type;
    }

    @Override
    public Scope getBeanScope() {
        return scope;
    }

    @Override
    public void setBeanScope(Scope scope) {
        this.scope = scope;
    }

    @Override
    public List<BeanDependency> getBeanDependencies() {
        return dependencies;
    }

    @Override
    public <T> T getBeanInstance() {
        return (T) instance;
    }

    @Override
    public void setBeanInstance(Object instance) {
        this.instance = instance;
    }

    @Override
    public BeanCreationStrategy getBeanCreationStrategy() {
        return strategy;
    }

    @Override
    public void setBeanCreationStrategy(BeanCreationStrategy strategy) {
        this.strategy = strategy;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("[").append(name).append(':').append(scope).append("]: ");
        builder.append("CLASS: ").append(type).append("; ");
        builder.append("INSTANCE: ").append(instance).append("; ");
        builder.append("DEPENDENCIES: ").append(dependencies).append("; ");
        builder.append("CREATION_TYPE: ").append(getBeanCreationType()).append("; ");
        builder.append("STRATEGY: ").append(strategy).append("; ");

        return builder.toString();
    }
}
