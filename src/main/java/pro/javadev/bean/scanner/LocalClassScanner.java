package pro.javadev.bean.scanner;

import java.io.File;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import static java.util.Objects.requireNonNull;

public class LocalClassScanner extends Scanner.DefaultClassScanner {

    @Override
    public Set<Class<?>> scan(String name, ClassLoader loader) {
        return null;
    }

    @Override
    public Set<Class<?>> scan(URL resource, String name, ClassLoader loader) {
        try {
            return findClasses(new File(resource.getFile()), name, loader);
        } catch (Exception e) {
            throw new ClassScannerException(e);
        }
    }

    @Override
    public boolean supports(Object object) {
        return object.equals("file");
    }

    private Set<Class<?>> findClasses(File directory, String name, ClassLoader loader) {
        Set<Class<?>> classes = new HashSet<>();

        if (directory.exists()) {
            for (File file : requireNonNull(directory.listFiles())) {
                if (file.isDirectory()) {
                    classes.addAll(findClasses(file, name + "." + file.getName(), loader));
                } else if (file.getName().endsWith(CLASS_FILE_SUFFIX)) {
                    try {
                        classes.add(loader.loadClass(getClassName(name, file)));
                    } catch (Throwable ignore) {}
                }
            }
        }

        return classes;
    }

    private String getClassName(String name, File file) {
        return name + '.' + file.getName().substring(0, file.getName().length() - CLASS_FILE_SUFFIX.length());
    }


}
