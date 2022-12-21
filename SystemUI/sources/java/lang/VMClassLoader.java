package java.lang;

import java.net.URL;
import java.p026io.File;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.List;
import libcore.p030io.ClassPathURLStreamHandler;

class VMClassLoader {
    private static final ClassPathURLStreamHandler[] bootClassPathUrlHandlers = createBootClassPathUrlHandlers();

    static native Class findLoadedClass(ClassLoader classLoader, String str);

    private static native String[] getBootClassPathEntries();

    VMClassLoader() {
    }

    private static ClassPathURLStreamHandler[] createBootClassPathUrlHandlers() {
        String[] bootClassPathEntries = getBootClassPathEntries();
        ArrayList arrayList = new ArrayList(bootClassPathEntries.length);
        for (String str : bootClassPathEntries) {
            try {
                new File(str).toURI().toString();
                arrayList.add(new ClassPathURLStreamHandler(str));
            } catch (IOException e) {
                System.logE("Unable to open boot classpath entry: " + str, e);
            }
        }
        return (ClassPathURLStreamHandler[]) arrayList.toArray(new ClassPathURLStreamHandler[arrayList.size()]);
    }

    static URL getResource(String str) {
        for (ClassPathURLStreamHandler entryUrlOrNull : bootClassPathUrlHandlers) {
            URL entryUrlOrNull2 = entryUrlOrNull.getEntryUrlOrNull(str);
            if (entryUrlOrNull2 != null) {
                return entryUrlOrNull2;
            }
        }
        return null;
    }

    static List<URL> getResources(String str) {
        ArrayList arrayList = new ArrayList();
        for (ClassPathURLStreamHandler entryUrlOrNull : bootClassPathUrlHandlers) {
            URL entryUrlOrNull2 = entryUrlOrNull.getEntryUrlOrNull(str);
            if (entryUrlOrNull2 != null) {
                arrayList.add(entryUrlOrNull2);
            }
        }
        return arrayList;
    }
}
