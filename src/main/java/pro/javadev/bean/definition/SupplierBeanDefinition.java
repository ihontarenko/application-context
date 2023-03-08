package pro.javadev.bean.definition;

import pro.javadev.bean.BeanCreationType;

import java.lang.reflect.Method;
import java.util.function.Supplier;

public class SupplierBeanDefinition extends AbstractBeanDefinition {

    private Supplier<Object> supplier;

    public SupplierBeanDefinition(String name, Class<?> type, Supplier<Object> supplier) {
        super(name, type);
        setSupplier(supplier);
    }

    @Override
    public BeanCreationType getBeanCreationType() {
        return BeanCreationType.SUPPLIER;
    }

    public Supplier<Object> getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier<Object> supplier) {
        this.supplier = supplier;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder("SUPPLIER_BEAN_DEFINITION: ");

        builder.append(super.toString());

        return builder.toString();
    }

}
