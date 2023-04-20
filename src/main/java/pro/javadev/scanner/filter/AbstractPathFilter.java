package pro.javadev.scanner.filter;

abstract public class AbstractPathFilter implements PathFilter {

    protected final boolean invert;

    public AbstractPathFilter(boolean invert) {
        this.invert = invert;
    }

    public boolean invert() {
        return invert;
    }

}
