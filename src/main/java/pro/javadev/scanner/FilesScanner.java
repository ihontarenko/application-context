package pro.javadev.scanner;

import java.net.URL;
import java.util.Set;

public class FilesScanner extends Scanner.DefaultClassScanner {

    @Override
    public Set<Class<?>> scan(String name, ClassLoader loader) {
        return null;
    }

    @Override
    public Set<Class<?>> scan(URL resource, String name, ClassLoader loader) {
        return null;
    }

}
