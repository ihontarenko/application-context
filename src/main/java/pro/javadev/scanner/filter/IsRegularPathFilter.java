package pro.javadev.scanner.filter;

import java.nio.file.Files;
import java.nio.file.Path;

public class IsRegularPathFilter extends AbstractPathFilter {

    public IsRegularPathFilter() {
        super(false);
    }

    public IsRegularPathFilter(boolean invert) {
        super(invert);
    }

    @Override
    public boolean accept(Path object) {
        return Files.isReadable(object) != invert;
    }

}
