package java.nio.file;

import java.nio.file.WatchEvent;
import java.p026io.IOException;

public interface Watchable {
    WatchKey register(WatchService watchService, WatchEvent.Kind<?>... kindArr) throws IOException;

    WatchKey register(WatchService watchService, WatchEvent.Kind<?>[] kindArr, WatchEvent.Modifier... modifierArr) throws IOException;
}
