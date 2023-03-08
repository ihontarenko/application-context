package pro.javadev.bean.scanner.filter;

abstract public class AbstractClassFilter implements ClassFilter {

    private final boolean invert;

    public AbstractClassFilter(boolean invert) {
        this.invert = invert;
    }

    public boolean invert() {
        return invert;
    }

}
