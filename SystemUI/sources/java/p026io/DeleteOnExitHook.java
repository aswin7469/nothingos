package java.p026io;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;

/* renamed from: java.io.DeleteOnExitHook */
class DeleteOnExitHook {
    private static LinkedHashSet<String> files = new LinkedHashSet<>();

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                DeleteOnExitHook.runHooks();
            }
        });
    }

    private DeleteOnExitHook() {
    }

    static synchronized void add(String str) {
        synchronized (DeleteOnExitHook.class) {
            LinkedHashSet<String> linkedHashSet = files;
            if (linkedHashSet != null) {
                linkedHashSet.add(str);
            } else {
                throw new IllegalStateException("Shutdown in progress");
            }
        }
    }

    static void runHooks() {
        LinkedHashSet<String> linkedHashSet;
        synchronized (DeleteOnExitHook.class) {
            linkedHashSet = files;
            files = null;
        }
        ArrayList arrayList = new ArrayList(linkedHashSet);
        Collections.reverse(arrayList);
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            new File((String) it.next()).delete();
        }
    }
}
