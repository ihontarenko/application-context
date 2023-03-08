package pro.javadev.bean.scanner.filter;

public class LambdaClassFilter extends ClassAnnotatedClassFilter {

    public LambdaClassFilter(boolean invert) {
        super(FunctionalInterface.class, invert);
    }

    public LambdaClassFilter() {
        this(false);
    }

}
