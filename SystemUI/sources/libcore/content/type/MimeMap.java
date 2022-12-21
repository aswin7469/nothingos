package libcore.content.type;

import android.annotation.SystemApi;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class MimeMap {
    private static volatile MemoizingSupplier<MimeMap> instanceSupplier = new MemoizingSupplier<>(new MimeMap$$ExternalSyntheticLambda0());
    private final Map<String, String> extToMime;
    private volatile int hashCode;
    private final Map<String, String> mimeToExt;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static Builder builder() {
        return new Builder();
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public Builder buildUpon() {
        return new Builder(this.mimeToExt, this.extToMime);
    }

    private MimeMap(Map<String, String> map, Map<String, String> map2) {
        this.hashCode = 0;
        Map<String, String> map3 = (Map) Objects.requireNonNull(map);
        this.mimeToExt = map3;
        this.extToMime = (Map) Objects.requireNonNull(map2);
        for (Map.Entry next : map3.entrySet()) {
            checkValidMimeType((String) next.getKey());
            checkValidExtension((String) next.getValue());
        }
        for (Map.Entry next2 : this.extToMime.entrySet()) {
            checkValidExtension((String) next2.getKey());
            checkValidMimeType((String) next2.getValue());
        }
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static MimeMap getDefault() {
        return (MimeMap) Objects.requireNonNull(instanceSupplier.get());
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [java.util.function.Supplier<libcore.content.type.MimeMap>, java.lang.Object] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @android.annotation.SystemApi(client = android.annotation.SystemApi.Client.MODULE_LIBRARIES)
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void setDefaultSupplier(java.util.function.Supplier<libcore.content.type.MimeMap> r1) {
        /*
            libcore.content.type.MimeMap$MemoizingSupplier r0 = new libcore.content.type.MimeMap$MemoizingSupplier
            java.lang.Object r1 = java.util.Objects.requireNonNull(r1)
            java.util.function.Supplier r1 = (java.util.function.Supplier) r1
            r0.<init>(r1)
            instanceSupplier = r0
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: libcore.content.type.MimeMap.setDefaultSupplier(java.util.function.Supplier):void");
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public final boolean hasExtension(String str) {
        return guessMimeTypeFromExtension(str) != null;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public final String guessMimeTypeFromExtension(String str) {
        if (str == null) {
            return null;
        }
        return this.extToMime.get(toLowerCase(str));
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public final boolean hasMimeType(String str) {
        return guessExtensionFromMimeType(str) != null;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public final String guessExtensionFromMimeType(String str) {
        if (str == null) {
            return null;
        }
        return this.mimeToExt.get(toLowerCase(str));
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public Set<String> mimeTypes() {
        return Collections.unmodifiableSet(this.mimeToExt.keySet());
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public Set<String> extensions() {
        return Collections.unmodifiableSet(this.extToMime.keySet());
    }

    /* access modifiers changed from: private */
    public static String toLowerCase(String str) {
        return str.toLowerCase(Locale.ROOT);
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            this.hashCode = this.mimeToExt.hashCode() + (this.extToMime.hashCode() * 31);
        }
        return this.hashCode;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof MimeMap)) {
            return false;
        }
        MimeMap mimeMap = (MimeMap) obj;
        if (hashCode() == mimeMap.hashCode() && this.mimeToExt.equals(mimeMap.mimeToExt) && this.extToMime.equals(mimeMap.extToMime)) {
            return true;
        }
        return false;
    }

    public String toString() {
        return "MimeMap[" + this.mimeToExt + ", " + this.extToMime + NavigationBarInflaterView.SIZE_MOD_END;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public static final class Builder {
        private final Map<String, String> extToMime;
        private final Map<String, String> mimeToExt;

        Builder() {
            this.mimeToExt = new HashMap();
            this.extToMime = new HashMap();
        }

        Builder(Map<String, String> map, Map<String, String> map2) {
            this.mimeToExt = new HashMap(map);
            this.extToMime = new HashMap(map2);
        }

        static class Element {
            final boolean keepExisting;
            final String mimeOrExt;

            private Element(String str, boolean z) {
                if (str.startsWith("?")) {
                    this.keepExisting = true;
                    this.mimeOrExt = MimeMap.toLowerCase(str.substring(1));
                } else {
                    this.keepExisting = false;
                    this.mimeOrExt = MimeMap.toLowerCase(str);
                }
                if (z) {
                    MimeMap.checkValidMimeType(this.mimeOrExt);
                } else {
                    MimeMap.checkValidExtension(this.mimeOrExt);
                }
            }

            public static Element ofMimeSpec(String str) {
                return new Element(str, true);
            }

            public static Element ofExtensionSpec(String str) {
                return new Element(str, false);
            }
        }

        private static String maybePut(Map<String, String> map, Element element, String str) {
            if (element.keepExisting) {
                return map.putIfAbsent(element.mimeOrExt, str);
            }
            return map.put(element.mimeOrExt, str);
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public Builder addMimeMapping(String str, List<String> list) {
            Element ofMimeSpec = Element.ofMimeSpec(str);
            if (list.isEmpty()) {
                return this;
            }
            Element ofExtensionSpec = Element.ofExtensionSpec(list.get(0));
            maybePut(this.mimeToExt, ofMimeSpec, ofExtensionSpec.mimeOrExt);
            maybePut(this.extToMime, ofExtensionSpec, ofMimeSpec.mimeOrExt);
            for (String ofExtensionSpec2 : list.subList(1, list.size())) {
                maybePut(this.extToMime, Element.ofExtensionSpec(ofExtensionSpec2), ofMimeSpec.mimeOrExt);
            }
            return this;
        }

        public Builder addMimeMapping(String str, String str2) {
            return addMimeMapping(str, (List<String>) Collections.singletonList(str2));
        }

        @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
        public MimeMap build() {
            return new MimeMap(this.mimeToExt, this.extToMime);
        }

        public String toString() {
            return "MimeMap.Builder[" + this.mimeToExt + ", " + this.extToMime + NavigationBarInflaterView.SIZE_MOD_END;
        }
    }

    private static boolean isValidMimeTypeOrExtension(String str) {
        return str != null && !str.isEmpty() && str.indexOf(63) < 0 && str.indexOf(32) < 0 && str.indexOf(9) < 0 && str.equals(toLowerCase(str));
    }

    static void checkValidMimeType(String str) {
        if (!isValidMimeTypeOrExtension(str) || str.indexOf(47) < 0) {
            throw new IllegalArgumentException("Invalid MIME type: " + str);
        }
    }

    static void checkValidExtension(String str) {
        if (!isValidMimeTypeOrExtension(str) || str.indexOf(47) >= 0) {
            throw new IllegalArgumentException("Invalid extension: " + str);
        }
    }

    private static final class MemoizingSupplier<T> implements Supplier<T> {
        private volatile Supplier<T> mDelegate;
        private volatile boolean mInitialized = false;
        private volatile T mInstance;

        public MemoizingSupplier(Supplier<T> supplier) {
            this.mDelegate = supplier;
        }

        public T get() {
            if (!this.mInitialized) {
                synchronized (this) {
                    if (!this.mInitialized) {
                        this.mInstance = this.mDelegate.get();
                        this.mDelegate = null;
                        this.mInitialized = true;
                    }
                }
            }
            return this.mInstance;
        }
    }
}
