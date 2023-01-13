package java.lang.reflect;

import java.lang.ref.WeakReference;
import java.p026io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import libcore.util.EmptyArray;
import sun.reflect.CallerSensitive;

public class Proxy implements Serializable {
    /* access modifiers changed from: private */
    public static final Comparator<Method> ORDER_BY_SIGNATURE_AND_SUBTYPE = new Comparator<Method>() {
        public int compare(Method method, Method method2) {
            int compare = Method.ORDER_BY_SIGNATURE.compare(method, method2);
            if (compare != 0) {
                return compare;
            }
            Class<?> declaringClass = method.getDeclaringClass();
            Class<?> declaringClass2 = method2.getDeclaringClass();
            if (declaringClass == declaringClass2) {
                return 0;
            }
            if (declaringClass.isAssignableFrom(declaringClass2)) {
                return 1;
            }
            if (declaringClass2.isAssignableFrom(declaringClass)) {
                return -1;
            }
            return 0;
        }
    };
    private static final Class<?>[] constructorParams = {InvocationHandler.class};
    /* access modifiers changed from: private */
    public static final Object key0 = new Object();
    private static final WeakCache<ClassLoader, Class<?>[], Class<?>> proxyClassCache = new WeakCache<>(new KeyFactory(), new ProxyClassFactory());
    private static final long serialVersionUID = -2222568056686623797L;

    /* renamed from: h */
    protected InvocationHandler f550h;

    /* access modifiers changed from: private */
    public static native Class<?> generateProxy(String str, Class<?>[] clsArr, ClassLoader classLoader, Method[] methodArr, Class<?>[][] clsArr2);

    private Proxy() {
    }

    protected Proxy(InvocationHandler invocationHandler) {
        Objects.requireNonNull(invocationHandler);
        this.f550h = invocationHandler;
    }

    @CallerSensitive
    public static Class<?> getProxyClass(ClassLoader classLoader, Class<?>... clsArr) throws IllegalArgumentException {
        return getProxyClass0(classLoader, clsArr);
    }

    private static Class<?> getProxyClass0(ClassLoader classLoader, Class<?>... clsArr) {
        if (clsArr.length <= 65535) {
            return proxyClassCache.get(classLoader, clsArr);
        }
        throw new IllegalArgumentException("interface limit exceeded");
    }

    private static final class Key1 extends WeakReference<Class<?>> {
        private final int hash;

        Key1(Class<?> cls) {
            super(cls);
            this.hash = cls.hashCode();
        }

        public int hashCode() {
            return this.hash;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:4:0x000c, code lost:
            r2 = (java.lang.Class) get();
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean equals(java.lang.Object r3) {
            /*
                r2 = this;
                if (r2 == r3) goto L_0x001f
                if (r3 == 0) goto L_0x001d
                java.lang.Class r0 = r3.getClass()
                java.lang.Class<java.lang.reflect.Proxy$Key1> r1 = java.lang.reflect.Proxy.Key1.class
                if (r0 != r1) goto L_0x001d
                java.lang.Object r2 = r2.get()
                java.lang.Class r2 = (java.lang.Class) r2
                if (r2 == 0) goto L_0x001d
                java.lang.reflect.Proxy$Key1 r3 = (java.lang.reflect.Proxy.Key1) r3
                java.lang.Object r3 = r3.get()
                if (r2 != r3) goto L_0x001d
                goto L_0x001f
            L_0x001d:
                r2 = 0
                goto L_0x0020
            L_0x001f:
                r2 = 1
            L_0x0020:
                return r2
            */
            throw new UnsupportedOperationException("Method not decompiled: java.lang.reflect.Proxy.Key1.equals(java.lang.Object):boolean");
        }
    }

    private static final class Key2 extends WeakReference<Class<?>> {
        private final int hash;
        private final WeakReference<Class<?>> ref2;

        Key2(Class<?> cls, Class<?> cls2) {
            super(cls);
            this.hash = (cls.hashCode() * 31) + cls2.hashCode();
            this.ref2 = new WeakReference<>(cls2);
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            Class cls;
            Class<?> cls2;
            if (this != obj) {
                if (!(obj == null || obj.getClass() != Key2.class || (cls = (Class) get()) == null)) {
                    Key2 key2 = (Key2) obj;
                    if (!(cls == key2.get() && (cls2 = this.ref2.get()) != null && cls2 == key2.ref2.get())) {
                        return false;
                    }
                }
                return false;
            }
            return true;
        }
    }

    private static final class KeyX {
        private final int hash;
        private final WeakReference<Class<?>>[] refs;

        KeyX(Class<?>[] clsArr) {
            this.hash = Arrays.hashCode((Object[]) clsArr);
            this.refs = new WeakReference[clsArr.length];
            for (int i = 0; i < clsArr.length; i++) {
                this.refs[i] = new WeakReference<>(clsArr[i]);
            }
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            return this == obj || (obj != null && obj.getClass() == KeyX.class && equals(this.refs, ((KeyX) obj).refs));
        }

        private static boolean equals(WeakReference<Class<?>>[] weakReferenceArr, WeakReference<Class<?>>[] weakReferenceArr2) {
            if (weakReferenceArr.length != weakReferenceArr2.length) {
                return false;
            }
            for (int i = 0; i < weakReferenceArr.length; i++) {
                Class<?> cls = weakReferenceArr[i].get();
                if (cls == null || cls != weakReferenceArr2[i].get()) {
                    return false;
                }
            }
            return true;
        }
    }

    private static final class KeyFactory implements BiFunction<ClassLoader, Class<?>[], Object> {
        private KeyFactory() {
        }

        public Object apply(ClassLoader classLoader, Class<?>[] clsArr) {
            int length = clsArr.length;
            if (length == 0) {
                return Proxy.key0;
            }
            if (length == 1) {
                return new Key1(clsArr[0]);
            }
            if (length != 2) {
                return new KeyX(clsArr);
            }
            return new Key2(clsArr[0], clsArr[1]);
        }
    }

    private static final class ProxyClassFactory implements BiFunction<ClassLoader, Class<?>[], Class<?>> {
        private static final AtomicLong nextUniqueNumber = new AtomicLong();
        private static final String proxyClassNamePrefix = "$Proxy";

        private ProxyClassFactory() {
        }

        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v0, resolved type: java.lang.Class<?>} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: java.lang.Class<?>} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v2, resolved type: java.lang.String} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.lang.String} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: java.lang.String} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v5, resolved type: java.lang.Class<?>} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v3, resolved type: java.lang.String} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v6, resolved type: java.lang.Class<?>} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v5, resolved type: java.lang.String} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v7, resolved type: java.lang.Class<?>} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v9, resolved type: java.lang.String} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v10, resolved type: java.lang.Class<?>} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v11, resolved type: java.lang.Class<?>} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v12, resolved type: java.lang.Class<?>} */
        /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v13, resolved type: java.lang.Class<?>} */
        /* JADX WARNING: Multi-variable type inference failed */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.lang.Class<?> apply(java.lang.ClassLoader r8, java.lang.Class<?>[] r9) {
            /*
                r7 = this;
                java.util.IdentityHashMap r7 = new java.util.IdentityHashMap
                int r0 = r9.length
                r7.<init>((int) r0)
                int r0 = r9.length
                r1 = 0
                r2 = r1
            L_0x0009:
                r3 = 0
                if (r2 >= r0) goto L_0x0073
                r4 = r9[r2]
                java.lang.String r5 = r4.getName()     // Catch:{ ClassNotFoundException -> 0x0016 }
                java.lang.Class r3 = java.lang.Class.forName(r5, r1, r8)     // Catch:{ ClassNotFoundException -> 0x0016 }
            L_0x0016:
                if (r3 != r4) goto L_0x005c
                boolean r4 = r3.isInterface()
                if (r4 == 0) goto L_0x0041
                java.lang.Boolean r4 = java.lang.Boolean.TRUE
                java.lang.Object r4 = r7.put(r3, r4)
                if (r4 != 0) goto L_0x0029
                int r2 = r2 + 1
                goto L_0x0009
            L_0x0029:
                java.lang.IllegalArgumentException r7 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r8 = new java.lang.StringBuilder
                java.lang.String r9 = "repeated interface: "
                r8.<init>((java.lang.String) r9)
                java.lang.String r9 = r3.getName()
                r8.append((java.lang.String) r9)
                java.lang.String r8 = r8.toString()
                r7.<init>((java.lang.String) r8)
                throw r7
            L_0x0041:
                java.lang.IllegalArgumentException r7 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r8 = new java.lang.StringBuilder
                r8.<init>()
                java.lang.String r9 = r3.getName()
                r8.append((java.lang.String) r9)
                java.lang.String r9 = " is not an interface"
                r8.append((java.lang.String) r9)
                java.lang.String r8 = r8.toString()
                r7.<init>((java.lang.String) r8)
                throw r7
            L_0x005c:
                java.lang.IllegalArgumentException r7 = new java.lang.IllegalArgumentException
                java.lang.StringBuilder r8 = new java.lang.StringBuilder
                r8.<init>()
                r8.append((java.lang.Object) r4)
                java.lang.String r9 = " is not visible from class loader"
                r8.append((java.lang.String) r9)
                java.lang.String r8 = r8.toString()
                r7.<init>((java.lang.String) r8)
                throw r7
            L_0x0073:
                int r7 = r9.length
                r0 = r1
            L_0x0075:
                java.lang.String r2 = ""
                if (r0 >= r7) goto L_0x00af
                r4 = r9[r0]
                int r5 = r4.getModifiers()
                boolean r5 = java.lang.reflect.Modifier.isPublic(r5)
                if (r5 != 0) goto L_0x00ac
                java.lang.String r4 = r4.getName()
                r5 = 46
                int r5 = r4.lastIndexOf((int) r5)
                r6 = -1
                if (r5 != r6) goto L_0x0093
                goto L_0x0099
            L_0x0093:
                int r5 = r5 + 1
                java.lang.String r2 = r4.substring(r1, r5)
            L_0x0099:
                if (r3 != 0) goto L_0x009d
                r3 = r2
                goto L_0x00ac
            L_0x009d:
                boolean r2 = r2.equals(r3)
                if (r2 == 0) goto L_0x00a4
                goto L_0x00ac
            L_0x00a4:
                java.lang.IllegalArgumentException r7 = new java.lang.IllegalArgumentException
                java.lang.String r8 = "non-public interfaces from different packages"
                r7.<init>((java.lang.String) r8)
                throw r7
            L_0x00ac:
                int r0 = r0 + 1
                goto L_0x0075
            L_0x00af:
                if (r3 != 0) goto L_0x00b2
                r3 = r2
            L_0x00b2:
                java.util.List r7 = java.lang.reflect.Proxy.getMethods(r9)
                java.util.Comparator r0 = java.lang.reflect.Proxy.ORDER_BY_SIGNATURE_AND_SUBTYPE
                java.util.Collections.sort(r7, r0)
                java.lang.reflect.Proxy.validateReturnTypes(r7)
                java.util.List r0 = java.lang.reflect.Proxy.deduplicateAndGetExceptions(r7)
                int r1 = r7.size()
                java.lang.reflect.Method[] r1 = new java.lang.reflect.Method[r1]
                java.lang.Object[] r7 = r7.toArray(r1)
                java.lang.reflect.Method[] r7 = (java.lang.reflect.Method[]) r7
                int r1 = r0.size()
                java.lang.Class[][] r1 = new java.lang.Class[r1][]
                java.lang.Object[] r0 = r0.toArray(r1)
                java.lang.Class[][] r0 = (java.lang.Class[][]) r0
                java.util.concurrent.atomic.AtomicLong r1 = nextUniqueNumber
                long r1 = r1.getAndIncrement()
                java.lang.StringBuilder r4 = new java.lang.StringBuilder
                r4.<init>()
                r4.append((java.lang.String) r3)
                java.lang.String r3 = "$Proxy"
                r4.append((java.lang.String) r3)
                r4.append((long) r1)
                java.lang.String r1 = r4.toString()
                java.lang.Class r7 = java.lang.reflect.Proxy.generateProxy(r1, r9, r8, r7, r0)
                return r7
            */
            throw new UnsupportedOperationException("Method not decompiled: java.lang.reflect.Proxy.ProxyClassFactory.apply(java.lang.ClassLoader, java.lang.Class[]):java.lang.Class");
        }
    }

    /* access modifiers changed from: private */
    public static List<Class<?>[]> deduplicateAndGetExceptions(List<Method> list) {
        ArrayList arrayList = new ArrayList(list.size());
        int i = 0;
        while (i < list.size()) {
            Method method = list.get(i);
            Class[] exceptionTypes = method.getExceptionTypes();
            if (i > 0) {
                int i2 = i - 1;
                if (Method.ORDER_BY_SIGNATURE.compare(method, list.get(i2)) == 0) {
                    arrayList.set(i2, intersectExceptions((Class[]) arrayList.get(i2), exceptionTypes));
                    list.remove(i);
                }
            }
            arrayList.add(exceptionTypes);
            i++;
        }
        return arrayList;
    }

    private static Class<?>[] intersectExceptions(Class<?>[] clsArr, Class<?>[] clsArr2) {
        if (clsArr.length == 0 || clsArr2.length == 0) {
            return EmptyArray.CLASS;
        }
        if (Arrays.equals((Object[]) clsArr, (Object[]) clsArr2)) {
            return clsArr;
        }
        HashSet hashSet = new HashSet();
        for (Class<?> cls : clsArr) {
            for (Class<?> cls2 : clsArr2) {
                if (cls.isAssignableFrom(cls2)) {
                    hashSet.add(cls2);
                } else if (cls2.isAssignableFrom(cls)) {
                    hashSet.add(cls);
                }
            }
        }
        return (Class[]) hashSet.toArray(new Class[hashSet.size()]);
    }

    /* access modifiers changed from: private */
    public static void validateReturnTypes(List<Method> list) {
        Method method = null;
        for (Method next : list) {
            if (method != null && method.equalNameAndParameters(next)) {
                Class<?> returnType = next.getReturnType();
                Class<?> returnType2 = method.getReturnType();
                if (!returnType.isInterface() || !returnType2.isInterface()) {
                    if (!returnType2.isAssignableFrom(returnType)) {
                        if (!returnType.isAssignableFrom(returnType2)) {
                            throw new IllegalArgumentException("proxied interface methods have incompatible return types:\n  " + method + "\n  " + next);
                        }
                    }
                }
            }
            method = next;
        }
    }

    /* access modifiers changed from: private */
    public static List<Method> getMethods(Class<?>[] clsArr) {
        ArrayList arrayList = new ArrayList();
        try {
            arrayList.add(Object.class.getMethod("equals", Object.class));
            arrayList.add(Object.class.getMethod("hashCode", EmptyArray.CLASS));
            arrayList.add(Object.class.getMethod("toString", EmptyArray.CLASS));
            getMethodsRecursive(clsArr, arrayList);
            return arrayList;
        } catch (NoSuchMethodException unused) {
            throw new AssertionError();
        }
    }

    private static void getMethodsRecursive(Class<?>[] clsArr, List<Method> list) {
        for (Class<?> cls : clsArr) {
            getMethodsRecursive(cls.getInterfaces(), list);
            Collections.addAll(list, cls.getDeclaredMethods());
        }
    }

    @CallerSensitive
    public static Object newProxyInstance(ClassLoader classLoader, Class<?>[] clsArr, InvocationHandler invocationHandler) throws IllegalArgumentException {
        Objects.requireNonNull(invocationHandler);
        Class<?> proxyClass0 = getProxyClass0(classLoader, (Class[]) clsArr.clone());
        try {
            Constructor<?> constructor = proxyClass0.getConstructor(constructorParams);
            if (!Modifier.isPublic(proxyClass0.getModifiers())) {
                constructor.setAccessible(true);
            }
            return constructor.newInstance(invocationHandler);
        } catch (IllegalAccessException | InstantiationException e) {
            throw new InternalError(e.toString(), e);
        } catch (InvocationTargetException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            throw new InternalError(cause.toString(), cause);
        } catch (NoSuchMethodException e3) {
            throw new InternalError(e3.toString(), e3);
        }
    }

    public static boolean isProxyClass(Class<?> cls) {
        return Proxy.class.isAssignableFrom(cls) && proxyClassCache.containsValue(cls);
    }

    @CallerSensitive
    public static InvocationHandler getInvocationHandler(Object obj) throws IllegalArgumentException {
        if (isProxyClass(obj.getClass())) {
            return ((Proxy) obj).f550h;
        }
        throw new IllegalArgumentException("not a proxy instance");
    }

    private static Object invoke(Proxy proxy, Method method, Object[] objArr) throws Throwable {
        return proxy.f550h.invoke(proxy, method, objArr);
    }
}
