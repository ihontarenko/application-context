package pro.javadev.bean.scanner.filter;

public class AccessModifierClassFilter implements ClassFilter {

    private final int modifiers;
    private final boolean invert;

    public AccessModifierClassFilter(int modifiers, boolean invert) {
        this.modifiers = modifiers;
        this.invert = invert;
    }

    public AccessModifierClassFilter(int modifiers) {
        this(modifiers, false);
    }

    @Override
    public boolean accept(Class<?> object) {
        return invert == ((object.getModifiers() & modifiers) == 0);
    }

}
