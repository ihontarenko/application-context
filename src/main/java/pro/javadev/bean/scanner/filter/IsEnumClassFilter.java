package pro.javadev.bean.scanner.filter;

public class IsEnumClassFilter extends AbstractClassFilter {

    public IsEnumClassFilter() {
        this(false);
    }

    public IsEnumClassFilter(boolean invert) {
        super(invert);
    }

    @Override
    public boolean accept(Class<?> clazz) {
        return clazz.isEnum() != invert();
    }

}