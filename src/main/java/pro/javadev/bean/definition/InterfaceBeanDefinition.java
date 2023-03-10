package pro.javadev.bean.definition;

import pro.javadev.bean.BeanCreationType;

import java.util.List;
import java.util.function.Supplier;

public class InterfaceBeanDefinition extends AbstractBeanDefinition {

    public InterfaceBeanDefinition(String name, Class<?> interfaceType) {
        super(name, interfaceType);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("INTERFACE_BEAN_DEFINITION: ");

        builder.append(super.toString());

        return builder.toString();
    }

    @Override
    public BeanCreationType getBeanCreationType() {
        return BeanCreationType.SUB_CLASSES;
    }
}
