package kotlinx.coroutines.internal;

import java.p026io.BufferedReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;

@Metadata(mo65042d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\bÀ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J!\u0010\u0005\u001a\u0004\u0018\u00010\u00062\f\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\u00060\b2\u0006\u0010\t\u001a\u00020\u0004H\bJ1\u0010\n\u001a\u0002H\u000b\"\u0004\b\u0000\u0010\u000b2\u0006\u0010\f\u001a\u00020\u00042\u0006\u0010\r\u001a\u00020\u000e2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u000b0\bH\u0002¢\u0006\u0002\u0010\u0010J*\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0012\"\u0004\b\u0000\u0010\u000b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u000b0\b2\u0006\u0010\r\u001a\u00020\u000eH\u0002J\u0013\u0010\u0013\u001a\b\u0012\u0004\u0012\u00020\u00060\u0012H\u0000¢\u0006\u0002\b\u0014J/\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u000b0\u0012\"\u0004\b\u0000\u0010\u000b2\f\u0010\u000f\u001a\b\u0012\u0004\u0012\u0002H\u000b0\b2\u0006\u0010\r\u001a\u00020\u000eH\u0000¢\u0006\u0002\b\u0016J\u0016\u0010\u0017\u001a\b\u0012\u0004\u0012\u00020\u00040\u00122\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u0016\u0010\u001a\u001a\b\u0012\u0004\u0012\u00020\u00040\u00122\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J,\u0010\u001d\u001a\u0002H\u001e\"\u0004\b\u0000\u0010\u001e*\u00020\u001f2\u0012\u0010 \u001a\u000e\u0012\u0004\u0012\u00020\u001f\u0012\u0004\u0012\u0002H\u001e0!H\b¢\u0006\u0002\u0010\"R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006#"}, mo65043d2 = {"Lkotlinx/coroutines/internal/FastServiceLoader;", "", "()V", "PREFIX", "", "createInstanceOf", "Lkotlinx/coroutines/internal/MainDispatcherFactory;", "baseClass", "Ljava/lang/Class;", "serviceClass", "getProviderInstance", "S", "name", "loader", "Ljava/lang/ClassLoader;", "service", "(Ljava/lang/String;Ljava/lang/ClassLoader;Ljava/lang/Class;)Ljava/lang/Object;", "load", "", "loadMainDispatcherFactory", "loadMainDispatcherFactory$kotlinx_coroutines_core", "loadProviders", "loadProviders$kotlinx_coroutines_core", "parse", "url", "Ljava/net/URL;", "parseFile", "r", "Ljava/io/BufferedReader;", "use", "R", "Ljava/util/jar/JarFile;", "block", "Lkotlin/Function1;", "(Ljava/util/jar/JarFile;Lkotlin/jvm/functions/Function1;)Ljava/lang/Object;", "kotlinx-coroutines-core"}, mo65044k = 1, mo65045mv = {1, 5, 1}, mo65047xi = 48)
/* compiled from: FastServiceLoader.kt */
public final class FastServiceLoader {
    public static final FastServiceLoader INSTANCE = new FastServiceLoader();
    private static final String PREFIX = "META-INF/services/";

    private FastServiceLoader() {
    }

    public final List<MainDispatcherFactory> loadMainDispatcherFactory$kotlinx_coroutines_core() {
        ArrayList arrayList;
        MainDispatcherFactory mainDispatcherFactory;
        MainDispatcherFactory mainDispatcherFactory2;
        Class<MainDispatcherFactory> cls = MainDispatcherFactory.class;
        if (!FastServiceLoaderKt.getANDROID_DETECTED()) {
            return load(cls, cls.getClassLoader());
        }
        try {
            arrayList = new ArrayList(2);
            mainDispatcherFactory = null;
            mainDispatcherFactory2 = cls.cast(Class.forName("kotlinx.coroutines.android.AndroidDispatcherFactory", true, cls.getClassLoader()).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
        } catch (ClassNotFoundException unused) {
            MainDispatcherFactory mainDispatcherFactory3 = null;
            mainDispatcherFactory2 = null;
        } catch (Throwable unused2) {
            return load(cls, cls.getClassLoader());
        }
        if (mainDispatcherFactory2 != null) {
            arrayList.add(mainDispatcherFactory2);
        }
        try {
            mainDispatcherFactory = cls.cast(Class.forName("kotlinx.coroutines.test.internal.TestMainDispatcherFactory", true, cls.getClassLoader()).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
        } catch (ClassNotFoundException unused3) {
            MainDispatcherFactory mainDispatcherFactory4 = null;
        }
        if (mainDispatcherFactory != null) {
            arrayList.add(mainDispatcherFactory);
        }
        return arrayList;
    }

    private final MainDispatcherFactory createInstanceOf(Class<MainDispatcherFactory> cls, String str) {
        try {
            return cls.cast(Class.forName(str, true, cls.getClassLoader()).getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
        } catch (ClassNotFoundException unused) {
            MainDispatcherFactory mainDispatcherFactory = null;
            return null;
        }
    }

    private final <S> List<S> load(Class<S> cls, ClassLoader classLoader) {
        try {
            return loadProviders$kotlinx_coroutines_core(cls, classLoader);
        } catch (Throwable unused) {
            return CollectionsKt.toList(ServiceLoader.load(cls, classLoader));
        }
    }

    public final <S> List<S> loadProviders$kotlinx_coroutines_core(Class<S> cls, ClassLoader classLoader) {
        ArrayList<T> list = Collections.list(classLoader.getResources(Intrinsics.stringPlus(PREFIX, cls.getName())));
        Intrinsics.checkNotNullExpressionValue(list, "java.util.Collections.list(this)");
        Collection arrayList = new ArrayList();
        for (T parse : list) {
            CollectionsKt.addAll(arrayList, INSTANCE.parse(parse));
        }
        Set set = CollectionsKt.toSet((List) arrayList);
        if (!set.isEmpty()) {
            Iterable<String> iterable = set;
            Collection arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(iterable, 10));
            for (String providerInstance : iterable) {
                arrayList2.add(INSTANCE.getProviderInstance(providerInstance, classLoader, cls));
            }
            return (List) arrayList2;
        }
        throw new IllegalArgumentException("No providers were loaded with FastServiceLoader".toString());
    }

    private final <S> S getProviderInstance(String str, ClassLoader classLoader, Class<S> cls) {
        Class<?> cls2 = Class.forName(str, false, classLoader);
        if (cls.isAssignableFrom(cls2)) {
            return cls.cast(cls2.getDeclaredConstructor(new Class[0]).newInstance(new Object[0]));
        }
        throw new IllegalArgumentException(("Expected service of class " + cls + ", but found " + cls2).toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0056, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        kotlin.p028io.CloseableKt.closeFinally(r5, r4);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x005a, code lost:
        throw r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x005d, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:27:?, code lost:
        r0.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0061, code lost:
        throw r5;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0062, code lost:
        r5 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:30:0x0063, code lost:
        kotlin.ExceptionsKt.addSuppressed(r4, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0066, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x008b, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:41:0x008c, code lost:
        kotlin.p028io.CloseableKt.closeFinally(r4, r5);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:42:0x008f, code lost:
        throw r0;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final java.util.List<java.lang.String> parse(java.net.URL r5) {
        /*
            r4 = this;
            java.lang.String r4 = r5.toString()
            java.lang.String r0 = "jar"
            r1 = 0
            r2 = 2
            r3 = 0
            boolean r0 = kotlin.text.StringsKt.startsWith$default(r4, r0, r1, r2, r3)
            if (r0 == 0) goto L_0x0067
            java.lang.String r5 = "jar:file:"
            java.lang.String r5 = kotlin.text.StringsKt.substringAfter$default((java.lang.String) r4, (java.lang.String) r5, (java.lang.String) r3, (int) r2, (java.lang.Object) r3)
            r0 = 33
            java.lang.String r5 = kotlin.text.StringsKt.substringBefore$default((java.lang.String) r5, (char) r0, (java.lang.String) r3, (int) r2, (java.lang.Object) r3)
            java.lang.String r0 = "!/"
            java.lang.String r4 = kotlin.text.StringsKt.substringAfter$default((java.lang.String) r4, (java.lang.String) r0, (java.lang.String) r3, (int) r2, (java.lang.Object) r3)
            java.util.jar.JarFile r0 = new java.util.jar.JarFile
            r0.<init>((java.lang.String) r5, (boolean) r1)
            java.io.BufferedReader r5 = new java.io.BufferedReader     // Catch:{ all -> 0x005b }
            java.io.InputStreamReader r1 = new java.io.InputStreamReader     // Catch:{ all -> 0x005b }
            java.util.zip.ZipEntry r2 = new java.util.zip.ZipEntry     // Catch:{ all -> 0x005b }
            r2.<init>((java.lang.String) r4)     // Catch:{ all -> 0x005b }
            java.io.InputStream r4 = r0.getInputStream(r2)     // Catch:{ all -> 0x005b }
            java.lang.String r2 = "UTF-8"
            r1.<init>((java.p026io.InputStream) r4, (java.lang.String) r2)     // Catch:{ all -> 0x005b }
            java.io.Reader r1 = (java.p026io.Reader) r1     // Catch:{ all -> 0x005b }
            r5.<init>(r1)     // Catch:{ all -> 0x005b }
            java.io.Closeable r5 = (java.p026io.Closeable) r5     // Catch:{ all -> 0x005b }
            r4 = r3
            java.lang.Throwable r4 = (java.lang.Throwable) r4     // Catch:{ all -> 0x005b }
            r4 = r5
            java.io.BufferedReader r4 = (java.p026io.BufferedReader) r4     // Catch:{ all -> 0x0054 }
            kotlinx.coroutines.internal.FastServiceLoader r1 = INSTANCE     // Catch:{ all -> 0x0054 }
            java.util.List r4 = r1.parseFile(r4)     // Catch:{ all -> 0x0054 }
            kotlin.p028io.CloseableKt.closeFinally(r5, r3)     // Catch:{ all -> 0x005b }
            r0.close()     // Catch:{ all -> 0x0052 }
            return r4
        L_0x0052:
            r4 = move-exception
            throw r4
        L_0x0054:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x0056 }
        L_0x0056:
            r1 = move-exception
            kotlin.p028io.CloseableKt.closeFinally(r5, r4)     // Catch:{ all -> 0x005b }
            throw r1     // Catch:{ all -> 0x005b }
        L_0x005b:
            r4 = move-exception
            throw r4     // Catch:{ all -> 0x005d }
        L_0x005d:
            r5 = move-exception
            r0.close()     // Catch:{ all -> 0x0062 }
            throw r5
        L_0x0062:
            r5 = move-exception
            kotlin.ExceptionsKt.addSuppressed(r4, r5)
            throw r4
        L_0x0067:
            java.io.BufferedReader r4 = new java.io.BufferedReader
            java.io.InputStreamReader r0 = new java.io.InputStreamReader
            java.io.InputStream r5 = r5.openStream()
            r0.<init>(r5)
            java.io.Reader r0 = (java.p026io.Reader) r0
            r4.<init>(r0)
            java.io.Closeable r4 = (java.p026io.Closeable) r4
            r5 = r3
            java.lang.Throwable r5 = (java.lang.Throwable) r5
            r5 = r4
            java.io.BufferedReader r5 = (java.p026io.BufferedReader) r5     // Catch:{ all -> 0x0089 }
            kotlinx.coroutines.internal.FastServiceLoader r0 = INSTANCE     // Catch:{ all -> 0x0089 }
            java.util.List r5 = r0.parseFile(r5)     // Catch:{ all -> 0x0089 }
            kotlin.p028io.CloseableKt.closeFinally(r4, r3)
            return r5
        L_0x0089:
            r5 = move-exception
            throw r5     // Catch:{ all -> 0x008b }
        L_0x008b:
            r0 = move-exception
            kotlin.p028io.CloseableKt.closeFinally(r4, r5)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.FastServiceLoader.parse(java.net.URL):java.util.List");
    }

    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0013, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0014, code lost:
        kotlin.jvm.internal.InlineMarker.finallyStart(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:?, code lost:
        r2.close();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x001a, code lost:
        kotlin.jvm.internal.InlineMarker.finallyEnd(1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x001d, code lost:
        throw r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x001e, code lost:
        r1 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x001f, code lost:
        kotlin.ExceptionsKt.addSuppressed(r3, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0022, code lost:
        throw r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final <R> R use(java.util.jar.JarFile r2, kotlin.jvm.functions.Function1<? super java.util.jar.JarFile, ? extends R> r3) {
        /*
            r1 = this;
            r1 = 1
            java.lang.Object r3 = r3.invoke(r2)     // Catch:{ all -> 0x0011 }
            kotlin.jvm.internal.InlineMarker.finallyStart(r1)
            r2.close()     // Catch:{ all -> 0x000f }
            kotlin.jvm.internal.InlineMarker.finallyEnd(r1)
            return r3
        L_0x000f:
            r1 = move-exception
            throw r1
        L_0x0011:
            r3 = move-exception
            throw r3     // Catch:{ all -> 0x0013 }
        L_0x0013:
            r0 = move-exception
            kotlin.jvm.internal.InlineMarker.finallyStart(r1)
            r2.close()     // Catch:{ all -> 0x001e }
            kotlin.jvm.internal.InlineMarker.finallyEnd(r1)
            throw r0
        L_0x001e:
            r1 = move-exception
            kotlin.ExceptionsKt.addSuppressed(r3, r1)
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.internal.FastServiceLoader.use(java.util.jar.JarFile, kotlin.jvm.functions.Function1):java.lang.Object");
    }

    private final List<String> parseFile(BufferedReader bufferedReader) {
        boolean z;
        Set linkedHashSet = new LinkedHashSet();
        while (true) {
            String readLine = bufferedReader.readLine();
            if (readLine == null) {
                return CollectionsKt.toList(linkedHashSet);
            }
            String substringBefore$default = StringsKt.substringBefore$default(readLine, "#", (String) null, 2, (Object) null);
            if (substringBefore$default != null) {
                String obj = StringsKt.trim((CharSequence) substringBefore$default).toString();
                CharSequence charSequence = obj;
                boolean z2 = false;
                int i = 0;
                while (true) {
                    if (i >= charSequence.length()) {
                        z = true;
                        break;
                    }
                    char charAt = charSequence.charAt(i);
                    if (!(charAt == '.' || Character.isJavaIdentifierPart(charAt))) {
                        z = false;
                        break;
                    }
                    i++;
                }
                if (z) {
                    if (charSequence.length() > 0) {
                        z2 = true;
                    }
                    if (z2) {
                        linkedHashSet.add(obj);
                    }
                } else {
                    throw new IllegalArgumentException(Intrinsics.stringPlus("Illegal service provider class name: ", obj).toString());
                }
            } else {
                throw new NullPointerException("null cannot be cast to non-null type kotlin.CharSequence");
            }
        }
    }
}
