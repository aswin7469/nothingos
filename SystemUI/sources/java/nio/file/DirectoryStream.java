package java.nio.file;

import java.p026io.Closeable;
import java.p026io.IOException;
import java.util.Iterator;

public interface DirectoryStream<T> extends Closeable, Iterable<T> {

    @FunctionalInterface
    public interface Filter<T> {
        boolean accept(T t) throws IOException;
    }

    Iterator<T> iterator();
}
