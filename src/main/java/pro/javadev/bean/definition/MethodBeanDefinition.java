package pro.javadev.bean.definition;

import pro.javadev.bean.BeanCreationType;

import java.lang.reflect.Method;

public class MethodBeanDefinition extends AbstractBeanDefinition {

    private Method method;
    private Object object;

    public MethodBeanDefinition(String name, Class<?> type) {
        super(name, type);
    }

    @Override
    public BeanCreationType getBeanCreationType() {
        return BeanCreationType.METHOD;
    }

    public Method getBeanFactoryMethod() {
        return method;
    }

    public void setBeanFactoryMethod(Method method) {
        this.method = method;
    }

    public Object getBeanFactoryObject() {
        return object;
    }

    public void setBeanFactoryObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("METHOD_BEAN_DEFINITION: ");

        builder.append(super.toString());
        builder.append("FACTORY_METHOD: ").append(method).append("; ");
        builder.append("FACTORY_OBJECT: ").append(object).append("; ");

        return builder.toString();
    }

}
