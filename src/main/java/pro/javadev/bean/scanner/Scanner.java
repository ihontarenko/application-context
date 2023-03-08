package pro.javadev.bean.scanner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface Scanner {

    String CLASS_FILE_SUFFIX             = ".class";
    String UNSUPPORTED_FOR_CLASS_MESSAGE = "THIS METHOD IS UNSUPPORTED FOR CLASS: ";

    default Set<Class<?>> scan(String name, ClassLoader loader) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_CLASS_MESSAGE + this.getClass().getName());
    }

    default Set<Class<?>> scan(String name) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_CLASS_MESSAGE + this.getClass().getName());
    }

    default Set<Class<?>> scan(URL resource, String name, ClassLoader loader) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_CLASS_MESSAGE + this.getClass().getName());
    }

    boolean supports(Object object);

    void addScanner(Scanner scanner);

    Scanner getScanner(Class<Scanner> type);

    List<Scanner> getScanners();

    Scanner getParent();

    void setParent(Scanner scanner);

    abstract class DefaultClassScanner implements Scanner {

        private final List<Scanner> scanners       = new ArrayList<>();
        private Scanner parent;

        @Override
        public boolean supports(Object object) {
            return false;
        }

        @Override
        public void addScanner(Scanner scanner) {
            scanner.setParent(this);
            scanners.add(scanner);
        }

        @Override
        public Scanner getScanner(Class<Scanner> type) {
            return scanners.stream().filter(scanner -> scanner.getClass().isAssignableFrom(type)).findFirst().orElse(null);
        }

        @Override
        public List<Scanner> getScanners() {
            return scanners;
        }

        @Override
        public Scanner getParent() {
            return parent;
        }

        @Override
        public void setParent(Scanner scanner) {
            parent = scanner;
        }
    }

}
