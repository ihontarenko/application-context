package pro.javadev.scanner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FilesScanner extends AbstractScanner<Path> {

    @Override
    public Set<Path> scan(String name, ClassLoader loader) {
        URL url = loader.getResource(name);

        assert url != null;

        return scan(url, name, loader);
    }

    @Override
    public Set<Path> scan(URL resource, String name, ClassLoader loader) {
        try (Stream<Path> stream = Files.walk(Paths.get(resource.toURI()))) {
            return stream.filter(this::doFilter).collect(Collectors.toSet());
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean supports(Object object) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_CLASS_MESSAGE + this.getClass().getName());
    }

    @Override
    public void addScanner(Scanner<Path> scanner) {
        throw new UnsupportedOperationException(UNSUPPORTED_FOR_CLASS_MESSAGE + this.getClass().getName());
    }

}
