package java.nio.charset;

import com.android.icu.charset.CharsetFactory;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.spi.CharsetProvider;
import java.p026io.UnsupportedEncodingException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import sun.misc.ASCIICaseInsensitiveComparator;
import sun.misc.C4752VM;
import sun.nio.p034cs.ThreadLocalCoders;
import sun.security.action.GetPropertyAction;

public abstract class Charset implements Comparable<Charset> {
    private static volatile String bugLevel;
    private static volatile Map.Entry<String, Charset> cache1;
    private static final HashMap<String, Charset> cache2 = new HashMap<>();
    private static Charset defaultCharset;
    private static ThreadLocal<ThreadLocal<?>> gate = new ThreadLocal<>();
    private Set<String> aliasSet = null;
    private final String[] aliases;
    private final String name;

    public boolean canEncode() {
        return true;
    }

    public abstract boolean contains(Charset charset);

    public abstract CharsetDecoder newDecoder();

    public abstract CharsetEncoder newEncoder();

    static boolean atBugLevel(String str) {
        String str2 = bugLevel;
        if (str2 == null) {
            if (!C4752VM.isBooted()) {
                return false;
            }
            str2 = (String) AccessController.doPrivileged(new GetPropertyAction("sun.nio.cs.bugLevel", ""));
            bugLevel = str2;
        }
        return str2.equals(str);
    }

    private static void checkName(String str) {
        int length = str.length();
        if (atBugLevel("1.4") || length != 0) {
            for (int i = 0; i < length; i++) {
                char charAt = str.charAt(i);
                if ((charAt < 'A' || charAt > 'Z') && ((charAt < 'a' || charAt > 'z') && ((charAt < '0' || charAt > '9') && ((charAt != '-' || i == 0) && ((charAt != '+' || i == 0) && ((charAt != ':' || i == 0) && ((charAt != '_' || i == 0) && (charAt != '.' || i == 0)))))))) {
                    throw new IllegalCharsetNameException(str);
                }
            }
            return;
        }
        throw new IllegalCharsetNameException(str);
    }

    private static void cache(String str, Charset charset) {
        HashMap<String, Charset> hashMap = cache2;
        synchronized (hashMap) {
            String name2 = charset.name();
            Charset charset2 = hashMap.get(name2);
            if (charset2 != null) {
                charset = charset2;
            } else {
                hashMap.put(name2, charset);
                for (String put : charset.aliases()) {
                    cache2.put(put, charset);
                }
            }
            cache2.put(str, charset);
        }
        cache1 = new AbstractMap.SimpleImmutableEntry(str, charset);
    }

    /* access modifiers changed from: private */
    public static Iterator<CharsetProvider> providers() {
        return new Iterator<CharsetProvider>() {

            /* renamed from: i */
            Iterator<CharsetProvider> f582i;
            CharsetProvider next = null;

            /* renamed from: sl */
            ServiceLoader<CharsetProvider> f583sl;

            {
                ServiceLoader<CharsetProvider> load = ServiceLoader.load(CharsetProvider.class);
                this.f583sl = load;
                this.f582i = load.iterator();
            }

            private boolean getNext() {
                while (this.next == null) {
                    try {
                        if (!this.f582i.hasNext()) {
                            return false;
                        }
                        this.next = this.f582i.next();
                    } catch (ServiceConfigurationError e) {
                        if (!(e.getCause() instanceof SecurityException)) {
                            throw e;
                        }
                    }
                }
                return true;
            }

            public boolean hasNext() {
                return getNext();
            }

            public CharsetProvider next() {
                if (getNext()) {
                    CharsetProvider charsetProvider = this.next;
                    this.next = null;
                    return charsetProvider;
                }
                throw new NoSuchElementException();
            }

            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    /* JADX INFO: finally extract failed */
    private static Charset lookupViaProviders(final String str) {
        if (!C4752VM.isBooted() || gate.get() != null) {
            return null;
        }
        try {
            ThreadLocal<ThreadLocal<?>> threadLocal = gate;
            threadLocal.set(threadLocal);
            Charset charset = (Charset) AccessController.doPrivileged(new PrivilegedAction<Charset>() {
                public Charset run() {
                    Iterator r0 = Charset.providers();
                    while (r0.hasNext()) {
                        Charset charsetForName = ((CharsetProvider) r0.next()).charsetForName(String.this);
                        if (charsetForName != null) {
                            return charsetForName;
                        }
                    }
                    return null;
                }
            });
            gate.set(null);
            return charset;
        } catch (Throwable th) {
            gate.set(null);
            throw th;
        }
    }

    private static Charset lookup(String str) {
        if (str != null) {
            Map.Entry<String, Charset> entry = cache1;
            if (entry == null || !str.equals(entry.getKey())) {
                return lookup2(str);
            }
            return entry.getValue();
        }
        throw new IllegalArgumentException("Null charset name");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0019, code lost:
        if (r0 != null) goto L_0x0027;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x001b, code lost:
        r0 = lookupViaProviders(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001f, code lost:
        if (r0 == null) goto L_0x0022;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0022, code lost:
        checkName(r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0026, code lost:
        return null;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0027, code lost:
        cache(r3, r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x002a, code lost:
        return r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0015, code lost:
        r0 = com.android.icu.charset.CharsetFactory.create(r3);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.nio.charset.Charset lookup2(java.lang.String r3) {
        /*
            java.util.HashMap<java.lang.String, java.nio.charset.Charset> r0 = cache2
            monitor-enter(r0)
            java.lang.Object r1 = r0.get(r3)     // Catch:{ all -> 0x002b }
            java.nio.charset.Charset r1 = (java.nio.charset.Charset) r1     // Catch:{ all -> 0x002b }
            if (r1 == 0) goto L_0x0014
            java.util.AbstractMap$SimpleImmutableEntry r2 = new java.util.AbstractMap$SimpleImmutableEntry     // Catch:{ all -> 0x002b }
            r2.<init>(r3, r1)     // Catch:{ all -> 0x002b }
            cache1 = r2     // Catch:{ all -> 0x002b }
            monitor-exit(r0)     // Catch:{ all -> 0x002b }
            return r1
        L_0x0014:
            monitor-exit(r0)     // Catch:{ all -> 0x002b }
            java.nio.charset.Charset r0 = com.android.icu.charset.CharsetFactory.create(r3)
            if (r0 != 0) goto L_0x0027
            java.nio.charset.Charset r0 = lookupViaProviders(r3)
            if (r0 == 0) goto L_0x0022
            goto L_0x0027
        L_0x0022:
            checkName(r3)
            r3 = 0
            return r3
        L_0x0027:
            cache(r3, r0)
            return r0
        L_0x002b:
            r3 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x002b }
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: java.nio.charset.Charset.lookup2(java.lang.String):java.nio.charset.Charset");
    }

    public static boolean isSupported(String str) {
        return lookup(str) != null;
    }

    public static Charset forName(String str) {
        Charset lookup = lookup(str);
        if (lookup != null) {
            return lookup;
        }
        throw new UnsupportedCharsetException(str);
    }

    public static Charset forNameUEE(String str) throws UnsupportedEncodingException {
        try {
            return forName(str);
        } catch (Exception e) {
            UnsupportedEncodingException unsupportedEncodingException = new UnsupportedEncodingException(str);
            unsupportedEncodingException.initCause(e);
            throw unsupportedEncodingException;
        }
    }

    /* access modifiers changed from: private */
    public static void put(Iterator<Charset> it, Map<String, Charset> map) {
        while (it.hasNext()) {
            Charset next = it.next();
            if (!map.containsKey(next.name())) {
                map.put(next.name(), next);
            }
        }
    }

    public static SortedMap<String, Charset> availableCharsets() {
        return (SortedMap) AccessController.doPrivileged(new PrivilegedAction<SortedMap<String, Charset>>() {
            public SortedMap<String, Charset> run() {
                TreeMap treeMap = new TreeMap(ASCIICaseInsensitiveComparator.CASE_INSENSITIVE_ORDER);
                for (String create : CharsetFactory.getAvailableCharsetNames()) {
                    Charset create2 = CharsetFactory.create(create);
                    treeMap.put(create2.name(), create2);
                }
                Iterator r0 = Charset.providers();
                while (r0.hasNext()) {
                    Charset.put(((CharsetProvider) r0.next()).charsets(), treeMap);
                }
                return Collections.unmodifiableSortedMap(treeMap);
            }
        });
    }

    public static Charset defaultCharset() {
        Charset charset;
        synchronized (Charset.class) {
            if (defaultCharset == null) {
                defaultCharset = StandardCharsets.UTF_8;
            }
            charset = defaultCharset;
        }
        return charset;
    }

    protected Charset(String str, String[] strArr) {
        checkName(str);
        strArr = strArr == null ? new String[0] : strArr;
        for (String checkName : strArr) {
            checkName(checkName);
        }
        this.name = str;
        this.aliases = strArr;
    }

    public final String name() {
        return this.name;
    }

    public final Set<String> aliases() {
        Set<String> set = this.aliasSet;
        if (set != null) {
            return set;
        }
        HashSet hashSet = new HashSet(r0);
        for (String add : this.aliases) {
            hashSet.add(add);
        }
        Set<String> unmodifiableSet = Collections.unmodifiableSet(hashSet);
        this.aliasSet = unmodifiableSet;
        return unmodifiableSet;
    }

    public String displayName() {
        return this.name;
    }

    public final boolean isRegistered() {
        return !this.name.startsWith("X-") && !this.name.startsWith("x-");
    }

    public String displayName(Locale locale) {
        return this.name;
    }

    public final CharBuffer decode(ByteBuffer byteBuffer) {
        try {
            return ThreadLocalCoders.decoderFor(this).onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).decode(byteBuffer);
        } catch (CharacterCodingException e) {
            throw new Error((Throwable) e);
        }
    }

    public final ByteBuffer encode(CharBuffer charBuffer) {
        try {
            return ThreadLocalCoders.encoderFor(this).onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE).encode(charBuffer);
        } catch (CharacterCodingException e) {
            throw new Error((Throwable) e);
        }
    }

    public final ByteBuffer encode(String str) {
        return encode(CharBuffer.wrap((CharSequence) str));
    }

    public final int compareTo(Charset charset) {
        return name().compareToIgnoreCase(charset.name());
    }

    public final int hashCode() {
        return name().hashCode();
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof Charset)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        return this.name.equals(((Charset) obj).name());
    }

    public final String toString() {
        return name();
    }
}
