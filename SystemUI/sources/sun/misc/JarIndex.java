package sun.misc;

import java.p026io.BufferedReader;
import java.p026io.BufferedWriter;
import java.p026io.File;
import java.p026io.IOException;
import java.p026io.InputStream;
import java.p026io.InputStreamReader;
import java.p026io.OutputStream;
import java.p026io.OutputStreamWriter;
import java.security.AccessController;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import sun.security.action.GetPropertyAction;

public class JarIndex {
    public static final String INDEX_NAME = "META-INF/INDEX.LIST";
    private static final boolean metaInfFilenames = "true".equals(AccessController.doPrivileged(new GetPropertyAction("sun.misc.JarIndex.metaInfFilenames")));
    private HashMap<String, LinkedList<String>> indexMap;
    private String[] jarFiles;
    private HashMap<String, LinkedList<String>> jarMap;

    public JarIndex() {
        this.indexMap = new HashMap<>();
        this.jarMap = new HashMap<>();
    }

    public JarIndex(InputStream inputStream) throws IOException {
        this();
        read(inputStream);
    }

    public JarIndex(String[] strArr) throws IOException {
        this();
        this.jarFiles = strArr;
        parseJars(strArr);
    }

    public static JarIndex getJarIndex(JarFile jarFile) throws IOException {
        return getJarIndex(jarFile, (MetaIndex) null);
    }

    public static JarIndex getJarIndex(JarFile jarFile, MetaIndex metaIndex) throws IOException {
        JarEntry jarEntry;
        if ((metaIndex == null || metaIndex.mayContain(INDEX_NAME)) && (jarEntry = jarFile.getJarEntry(INDEX_NAME)) != null) {
            return new JarIndex(jarFile.getInputStream(jarEntry));
        }
        return null;
    }

    public String[] getJarFiles() {
        return this.jarFiles;
    }

    private void addToList(String str, String str2, HashMap<String, LinkedList<String>> hashMap) {
        LinkedList linkedList = hashMap.get(str);
        if (linkedList == null) {
            LinkedList linkedList2 = new LinkedList();
            linkedList2.add(str2);
            hashMap.put(str, linkedList2);
        } else if (!linkedList.contains(str2)) {
            linkedList.add(str2);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x000a, code lost:
        r1 = r4.lastIndexOf("/");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.util.LinkedList<java.lang.String> get(java.lang.String r4) {
        /*
            r3 = this;
            java.util.HashMap<java.lang.String, java.util.LinkedList<java.lang.String>> r0 = r3.indexMap
            java.lang.Object r0 = r0.get(r4)
            java.util.LinkedList r0 = (java.util.LinkedList) r0
            if (r0 != 0) goto L_0x0021
            java.lang.String r1 = "/"
            int r1 = r4.lastIndexOf((java.lang.String) r1)
            r2 = -1
            if (r1 == r2) goto L_0x0021
            java.util.HashMap<java.lang.String, java.util.LinkedList<java.lang.String>> r3 = r3.indexMap
            r0 = 0
            java.lang.String r4 = r4.substring(r0, r1)
            java.lang.Object r3 = r3.get(r4)
            r0 = r3
            java.util.LinkedList r0 = (java.util.LinkedList) r0
        L_0x0021:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: sun.misc.JarIndex.get(java.lang.String):java.util.LinkedList");
    }

    public void add(String str, String str2) {
        int lastIndexOf = str.lastIndexOf("/");
        if (lastIndexOf != -1) {
            str = str.substring(0, lastIndexOf);
        }
        addMapping(str, str2);
    }

    private void addMapping(String str, String str2) {
        addToList(str, str2, this.indexMap);
        addToList(str2, str, this.jarMap);
    }

    private void parseJars(String[] strArr) throws IOException {
        if (strArr != null) {
            for (String str : strArr) {
                ZipFile zipFile = new ZipFile(str.replace('/', File.separatorChar));
                Enumeration<? extends ZipEntry> entries = zipFile.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry zipEntry = (ZipEntry) entries.nextElement();
                    String name = zipEntry.getName();
                    if (!name.equals("META-INF/") && !name.equals(INDEX_NAME) && !name.equals(JarFile.MANIFEST_NAME)) {
                        if (!metaInfFilenames || !name.startsWith("META-INF/")) {
                            add(name, str);
                        } else if (!zipEntry.isDirectory()) {
                            addMapping(name, str);
                        }
                    }
                }
                zipFile.close();
            }
        }
    }

    public void write(OutputStream outputStream) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF8"));
        bufferedWriter.write("JarIndex-Version: 1.0\n\n");
        if (this.jarFiles != null) {
            int i = 0;
            while (true) {
                String[] strArr = this.jarFiles;
                if (i < strArr.length) {
                    String str = strArr[i];
                    bufferedWriter.write(str + "\n");
                    LinkedList linkedList = this.jarMap.get(str);
                    if (linkedList != null) {
                        Iterator it = linkedList.iterator();
                        while (it.hasNext()) {
                            bufferedWriter.write(((String) it.next()) + "\n");
                        }
                    }
                    bufferedWriter.write("\n");
                    i++;
                } else {
                    bufferedWriter.flush();
                    return;
                }
            }
        }
    }

    public void read(InputStream inputStream) throws IOException {
        String readLine;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF8"));
        Vector vector = new Vector();
        do {
            readLine = bufferedReader.readLine();
            if (readLine == null || readLine.endsWith(".jar")) {
                String str = null;
            }
            readLine = bufferedReader.readLine();
            break;
        } while (readLine.endsWith(".jar"));
        String str2 = null;
        while (readLine != null) {
            if (readLine.length() != 0) {
                if (readLine.endsWith(".jar")) {
                    vector.add(readLine);
                    str2 = readLine;
                } else {
                    addMapping(readLine, str2);
                }
            }
            readLine = bufferedReader.readLine();
        }
        this.jarFiles = (String[]) vector.toArray(new String[vector.size()]);
    }

    public void merge(JarIndex jarIndex, String str) {
        for (Map.Entry next : this.indexMap.entrySet()) {
            String str2 = (String) next.getKey();
            Iterator it = ((LinkedList) next.getValue()).iterator();
            while (it.hasNext()) {
                String str3 = (String) it.next();
                if (str != null) {
                    str3 = str.concat(str3);
                }
                jarIndex.addMapping(str2, str3);
            }
        }
    }
}
