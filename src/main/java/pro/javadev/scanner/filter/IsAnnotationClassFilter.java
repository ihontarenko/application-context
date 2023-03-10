package pro.javadev.scanner.filter;

public class IsAnnotationClassFilter extends AbstractClassFilter {

    public IsAnnotationClassFilter() {
        this(false);
    }

    public IsAnnotationClassFilter(boolean invert) {
        super(invert);
    }

    @Override
    public boolean accept(Class<?> clazz) {
        return clazz.isAnnotation() != invert();
    }

}