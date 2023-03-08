package pro.javadev.bean.scanner.filter;

import java.lang.annotation.Annotation;

public class ClassAnnotatedClassFilter extends AbstractClassFilter {

    private final Class<? extends Annotation> annotation;

    public ClassAnnotatedClassFilter(Class<? extends Annotation> annotation) {
        this(annotation, false);
    }

    public ClassAnnotatedClassFilter(Class<? extends Annotation> annotation, boolean invert) {
        super(invert);
        this.annotation = annotation;
    }

    @Override
    public boolean accept(Class<?> clazz) {
        return invert() != clazz.isAnnotationPresent(annotation);
    }

}