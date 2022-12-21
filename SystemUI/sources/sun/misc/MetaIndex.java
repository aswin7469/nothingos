package sun.misc;

import java.p026io.BufferedReader;
import java.p026io.File;
import java.p026io.FileReader;
import java.p026io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MetaIndex {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static volatile Map<File, MetaIndex> jarMap;
    private String[] contents;
    private boolean isClassOnlyJar;

    public static MetaIndex forJar(File file) {
        return getJarMap().get(file);
    }

    public static synchronized void registerDirectory(File file) {
        boolean z;
        synchronized (MetaIndex.class) {
            File file2 = new File(file, "meta-index");
            if (file2.exists()) {
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file2));
                    ArrayList arrayList = new ArrayList();
                    Map<File, MetaIndex> jarMap2 = getJarMap();
                    File canonicalFile = file.getCanonicalFile();
                    String readLine = bufferedReader.readLine();
                    if (readLine != null) {
                        if (readLine.equals("% VERSION 2")) {
                            String str = null;
                            loop0:
                            while (true) {
                                z = false;
                                while (true) {
                                    String readLine2 = bufferedReader.readLine();
                                    if (readLine2 == null) {
                                        break loop0;
                                    }
                                    char charAt = readLine2.charAt(0);
                                    if (!(charAt == '!' || charAt == '#')) {
                                        if (charAt == '%') {
                                            continue;
                                        } else if (charAt != '@') {
                                            arrayList.add(readLine2);
                                        }
                                    }
                                    if (str != null && arrayList.size() > 0) {
                                        jarMap2.put(new File(canonicalFile, str), new MetaIndex(arrayList, z));
                                        arrayList.clear();
                                    }
                                    str = readLine2.substring(2);
                                    if (readLine2.charAt(0) == '!') {
                                        z = true;
                                    } else if (z) {
                                        break;
                                    }
                                }
                            }
                            if (str != null && arrayList.size() > 0) {
                                jarMap2.put(new File(canonicalFile, str), new MetaIndex(arrayList, z));
                            }
                            bufferedReader.close();
                        }
                    }
                    bufferedReader.close();
                } catch (IOException unused) {
                }
            }
        }
    }

    public boolean mayContain(String str) {
        if (this.isClassOnlyJar && !str.endsWith(".class")) {
            return false;
        }
        String[] strArr = this.contents;
        for (String startsWith : strArr) {
            if (str.startsWith(startsWith)) {
                return true;
            }
        }
        return false;
    }

    private MetaIndex(List<String> list, boolean z) throws IllegalArgumentException {
        if (list != null) {
            this.contents = (String[]) list.toArray(new String[0]);
            this.isClassOnlyJar = z;
            return;
        }
        throw new IllegalArgumentException();
    }

    private static Map<File, MetaIndex> getJarMap() {
        if (jarMap == null) {
            synchronized (MetaIndex.class) {
                if (jarMap == null) {
                    jarMap = new HashMap();
                }
            }
        }
        return jarMap;
    }
}
