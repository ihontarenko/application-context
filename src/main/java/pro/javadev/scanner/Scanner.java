package pro.javadev.scanner;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface Scanner<T> {

    String CLASS_FILE_SUFFIX             = ".class";
    String UNSUPPORTED_FOR_CLASS_MESSAGE = "THIS METHOD IS UNSUPPORTED FOR CLASS: ";

    default Set<T> scan(String name, ClassLoader loader) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_CLASS_MESSAGE + this.getClass().getName());
    }

    default Set<T> scan(URL resource, String name, ClassLoader loader) {
        Set<T> result = null;

        try {
            result = scan(Paths.get(resource.toURI()), name, loader);
        } catch (URISyntaxException ignore) { }

        return result;
    }

    default Set<T> scan(Path path, String name, ClassLoader loader) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_CLASS_MESSAGE + this.getClass().getName());
    }

    boolean supports(Object object);

    void addScanner(Scanner<T> scanner);

    abstract class DefaultClassScanner implements Scanner<Class<?>> {

        protected final List<Scanner<Class<?>>> scanners = new ArrayList<>();

        @Override
        public boolean supports(Object object) {
            return false;
        }

        @Override
        public void addScanner(Scanner<Class<?>> scanner) {
            scanners.add(scanner);
        }
    }

}
