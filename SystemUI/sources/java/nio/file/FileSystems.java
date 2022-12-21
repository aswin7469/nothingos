package java.nio.file;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.net.URI;
import java.nio.file.spi.FileSystemProvider;
import java.p026io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;
import sun.nio.p035fs.DefaultFileSystemProvider;

public final class FileSystems {
    private FileSystems() {
    }

    private static class DefaultFileSystemHolder {
        static final FileSystem defaultFileSystem = defaultFileSystem();

        private DefaultFileSystemHolder() {
        }

        private static FileSystem defaultFileSystem() {
            return ((FileSystemProvider) AccessController.doPrivileged(new PrivilegedAction<FileSystemProvider>() {
                public FileSystemProvider run() {
                    return DefaultFileSystemHolder.getDefaultProvider();
                }
            })).getFileSystem(URI.create("file:///"));
        }

        /* access modifiers changed from: private */
        public static FileSystemProvider getDefaultProvider() {
            FileSystemProvider create = DefaultFileSystemProvider.create();
            String property = System.getProperty("java.nio.file.spi.DefaultFileSystemProvider");
            if (property != null) {
                String[] split = property.split(NavigationBarInflaterView.BUTTON_SEPARATOR);
                int length = split.length;
                int i = 0;
                while (i < length) {
                    try {
                        create = (FileSystemProvider) Class.forName(split[i], true, ClassLoader.getSystemClassLoader()).getDeclaredConstructor(FileSystemProvider.class).newInstance(create);
                        if (create.getScheme().equals("file")) {
                            i++;
                        } else {
                            throw new Error("Default provider must use scheme 'file'");
                        }
                    } catch (Exception e) {
                        throw new Error((Throwable) e);
                    }
                }
            }
            return create;
        }
    }

    public static FileSystem getDefault() {
        return DefaultFileSystemHolder.defaultFileSystem;
    }

    public static FileSystem getFileSystem(URI uri) {
        String scheme = uri.getScheme();
        for (FileSystemProvider next : FileSystemProvider.installedProviders()) {
            if (scheme.equalsIgnoreCase(next.getScheme())) {
                return next.getFileSystem(uri);
            }
        }
        throw new ProviderNotFoundException("Provider \"" + scheme + "\" not found");
    }

    public static FileSystem newFileSystem(URI uri, Map<String, ?> map) throws IOException {
        return newFileSystem(uri, map, (ClassLoader) null);
    }

    public static FileSystem newFileSystem(URI uri, Map<String, ?> map, ClassLoader classLoader) throws IOException {
        String scheme = uri.getScheme();
        for (FileSystemProvider next : FileSystemProvider.installedProviders()) {
            if (scheme.equalsIgnoreCase(next.getScheme())) {
                return next.newFileSystem(uri, map);
            }
        }
        if (classLoader != null) {
            Iterator<S> it = ServiceLoader.load(FileSystemProvider.class, classLoader).iterator();
            while (it.hasNext()) {
                FileSystemProvider fileSystemProvider = (FileSystemProvider) it.next();
                if (scheme.equalsIgnoreCase(fileSystemProvider.getScheme())) {
                    return fileSystemProvider.newFileSystem(uri, map);
                }
            }
        }
        throw new ProviderNotFoundException("Provider \"" + scheme + "\" not found");
    }

    public static FileSystem newFileSystem(Path path, ClassLoader classLoader) throws IOException {
        path.getClass();
        Map emptyMap = Collections.emptyMap();
        for (FileSystemProvider newFileSystem : FileSystemProvider.installedProviders()) {
            try {
                return newFileSystem.newFileSystem(path, (Map<String, ?>) emptyMap);
            } catch (UnsupportedOperationException unused) {
            }
        }
        if (classLoader != null) {
            Iterator<S> it = ServiceLoader.load(FileSystemProvider.class, classLoader).iterator();
            while (it.hasNext()) {
                try {
                    return ((FileSystemProvider) it.next()).newFileSystem(path, (Map<String, ?>) emptyMap);
                } catch (UnsupportedOperationException unused2) {
                }
            }
        }
        throw new ProviderNotFoundException("Provider not found");
    }
}
