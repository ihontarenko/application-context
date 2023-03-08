package pro.javadev.bean.definition;

import pro.javadev.bean.BeanCreationType;

import java.lang.reflect.Constructor;

public class ConstructorBeanDefinition extends AbstractBeanDefinition {

    private Constructor<?> constructor;

    public ConstructorBeanDefinition(String name, Class<?> type) {
        super(name, type);

        this.strategy = null;
    }

    @Override
    public BeanCreationType getBeanCreationType() {
        return BeanCreationType.CONSTRUCTOR;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public void setConstructor(Constructor<?> constructor) {
        this.constructor = constructor;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("CONSTRUCTOR_BEAN_DEFINITION: ");

        builder.append(super.toString());
        builder.append("CONSTRUCTOR: ").append(constructor).append("; ");

        return builder.toString();
    }
}
