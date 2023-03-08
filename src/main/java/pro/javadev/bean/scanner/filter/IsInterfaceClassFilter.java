package pro.javadev.bean.scanner.filter;

public class IsInterfaceClassFilter extends AbstractClassFilter {

    public IsInterfaceClassFilter() {
        this(false);
    }

    public IsInterfaceClassFilter(boolean invert) {
        super(invert);
    }

    @Override
    public boolean accept(Class<?> clazz) {
        return clazz.isInterface() != invert();
    }

}