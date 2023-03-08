package pro.javadev.bean.scanner.filter;

public class SubclassClassFilter extends AbstractClassFilter {

    private final Class<?> superClass;

    public SubclassClassFilter(Class<?> superClass, boolean invert) {
        super(invert);
        this.superClass = superClass;
    }

    public SubclassClassFilter(Class<?> superClass) {
        this(superClass, false);
    }

    @Override
    public boolean accept(Class<?> klass) {
        return (superClass.isAssignableFrom(klass) && superClass != klass) != invert();
    }

}
