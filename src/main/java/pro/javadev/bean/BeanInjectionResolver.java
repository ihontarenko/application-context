package pro.javadev.bean;

public interface BeanInjectionResolver {

    BeanInjectionResolver NULL_RESOLVER = new NullResolver();

    default boolean enable() {
        return true;
    }

    default String name() {
        return null;
    }

    class NullResolver implements BeanInjectionResolver {}

}
