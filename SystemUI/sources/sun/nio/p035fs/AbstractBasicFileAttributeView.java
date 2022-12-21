package sun.nio.p035fs;

import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.p026io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* renamed from: sun.nio.fs.AbstractBasicFileAttributeView */
abstract class AbstractBasicFileAttributeView implements BasicFileAttributeView, DynamicFileAttributeView {
    private static final String CREATION_TIME_NAME = "creationTime";
    private static final String FILE_KEY_NAME = "fileKey";
    private static final String IS_DIRECTORY_NAME = "isDirectory";
    private static final String IS_OTHER_NAME = "isOther";
    private static final String IS_REGULAR_FILE_NAME = "isRegularFile";
    private static final String IS_SYMBOLIC_LINK_NAME = "isSymbolicLink";
    private static final String LAST_ACCESS_TIME_NAME = "lastAccessTime";
    private static final String LAST_MODIFIED_TIME_NAME = "lastModifiedTime";
    private static final String SIZE_NAME = "size";
    static final Set<String> basicAttributeNames = Util.newSet("size", CREATION_TIME_NAME, LAST_ACCESS_TIME_NAME, LAST_MODIFIED_TIME_NAME, FILE_KEY_NAME, IS_DIRECTORY_NAME, IS_REGULAR_FILE_NAME, IS_SYMBOLIC_LINK_NAME, IS_OTHER_NAME);

    public String name() {
        return "basic";
    }

    protected AbstractBasicFileAttributeView() {
    }

    public void setAttribute(String str, Object obj) throws IOException {
        if (str.equals(LAST_MODIFIED_TIME_NAME)) {
            setTimes((FileTime) obj, (FileTime) null, (FileTime) null);
        } else if (str.equals(LAST_ACCESS_TIME_NAME)) {
            setTimes((FileTime) null, (FileTime) obj, (FileTime) null);
        } else if (str.equals(CREATION_TIME_NAME)) {
            setTimes((FileTime) null, (FileTime) null, (FileTime) obj);
        } else {
            throw new IllegalArgumentException("'" + name() + ":" + str + "' not recognized");
        }
    }

    /* renamed from: sun.nio.fs.AbstractBasicFileAttributeView$AttributesBuilder */
    static class AttributesBuilder {
        private boolean copyAll;
        private Map<String, Object> map = new HashMap();
        private Set<String> names = new HashSet();

        private AttributesBuilder(Set<String> set, String[] strArr) {
            for (String str : strArr) {
                if (str.equals("*")) {
                    this.copyAll = true;
                } else if (set.contains(str)) {
                    this.names.add(str);
                } else {
                    throw new IllegalArgumentException("'" + str + "' not recognized");
                }
            }
        }

        static AttributesBuilder create(Set<String> set, String[] strArr) {
            return new AttributesBuilder(set, strArr);
        }

        /* access modifiers changed from: package-private */
        public boolean match(String str) {
            return this.copyAll || this.names.contains(str);
        }

        /* access modifiers changed from: package-private */
        public void add(String str, Object obj) {
            this.map.put(str, obj);
        }

        /* access modifiers changed from: package-private */
        public Map<String, Object> unmodifiableMap() {
            return Collections.unmodifiableMap(this.map);
        }
    }

    /* access modifiers changed from: package-private */
    public final void addRequestedBasicAttributes(BasicFileAttributes basicFileAttributes, AttributesBuilder attributesBuilder) {
        if (attributesBuilder.match("size")) {
            attributesBuilder.add("size", Long.valueOf(basicFileAttributes.size()));
        }
        if (attributesBuilder.match(CREATION_TIME_NAME)) {
            attributesBuilder.add(CREATION_TIME_NAME, basicFileAttributes.creationTime());
        }
        if (attributesBuilder.match(LAST_ACCESS_TIME_NAME)) {
            attributesBuilder.add(LAST_ACCESS_TIME_NAME, basicFileAttributes.lastAccessTime());
        }
        if (attributesBuilder.match(LAST_MODIFIED_TIME_NAME)) {
            attributesBuilder.add(LAST_MODIFIED_TIME_NAME, basicFileAttributes.lastModifiedTime());
        }
        if (attributesBuilder.match(FILE_KEY_NAME)) {
            attributesBuilder.add(FILE_KEY_NAME, basicFileAttributes.fileKey());
        }
        if (attributesBuilder.match(IS_DIRECTORY_NAME)) {
            attributesBuilder.add(IS_DIRECTORY_NAME, Boolean.valueOf(basicFileAttributes.isDirectory()));
        }
        if (attributesBuilder.match(IS_REGULAR_FILE_NAME)) {
            attributesBuilder.add(IS_REGULAR_FILE_NAME, Boolean.valueOf(basicFileAttributes.isRegularFile()));
        }
        if (attributesBuilder.match(IS_SYMBOLIC_LINK_NAME)) {
            attributesBuilder.add(IS_SYMBOLIC_LINK_NAME, Boolean.valueOf(basicFileAttributes.isSymbolicLink()));
        }
        if (attributesBuilder.match(IS_OTHER_NAME)) {
            attributesBuilder.add(IS_OTHER_NAME, Boolean.valueOf(basicFileAttributes.isOther()));
        }
    }

    public Map<String, Object> readAttributes(String[] strArr) throws IOException {
        AttributesBuilder create = AttributesBuilder.create(basicAttributeNames, strArr);
        addRequestedBasicAttributes(readAttributes(), create);
        return create.unmodifiableMap();
    }
}
