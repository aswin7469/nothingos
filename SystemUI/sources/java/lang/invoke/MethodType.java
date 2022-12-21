package java.lang.invoke;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.ObjectOutputStream;
import java.p026io.ObjectStreamField;
import java.p026io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.invoke.util.BytecodeDescriptor;
import sun.invoke.util.Wrapper;

public final class MethodType implements Serializable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    static final int MAX_JVM_ARITY = 255;
    static final int MAX_MH_ARITY = 254;
    static final int MAX_MH_INVOKER_ARITY = 253;
    static final Class<?>[] NO_PTYPES = new Class[0];
    static final ConcurrentWeakInternSet<MethodType> internTable = new ConcurrentWeakInternSet<>();
    private static final MethodType[] objectOnlyTypes = new MethodType[20];
    private static final long ptypesOffset;
    private static final long rtypeOffset;
    private static final ObjectStreamField[] serialPersistentFields = new ObjectStreamField[0];
    private static final long serialVersionUID = 292;
    @Stable
    private MethodTypeForm form;
    @Stable
    private String methodDescriptor;
    private final Class<?>[] ptypes;
    private final Class<?> rtype;
    @Stable
    private MethodType wrapAlt;

    static {
        Class<MethodType> cls = MethodType.class;
        try {
            rtypeOffset = MethodHandleStatics.UNSAFE.objectFieldOffset(cls.getDeclaredField("rtype"));
            ptypesOffset = MethodHandleStatics.UNSAFE.objectFieldOffset(cls.getDeclaredField("ptypes"));
        } catch (Exception e) {
            throw new Error((Throwable) e);
        }
    }

    /* JADX WARNING: type inference failed for: r1v2, types: [java.lang.Object[]] */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private MethodType(java.lang.Class<?> r1, java.lang.Class<?>[] r2, boolean r3) {
        /*
            r0 = this;
            r0.<init>()
            checkRtype(r1)
            checkPtypes(r2)
            r0.rtype = r1
            if (r3 == 0) goto L_0x000e
            goto L_0x0016
        L_0x000e:
            int r1 = r2.length
            java.lang.Object[] r1 = java.util.Arrays.copyOf((T[]) r2, (int) r1)
            r2 = r1
            java.lang.Class[] r2 = (java.lang.Class[]) r2
        L_0x0016:
            r0.ptypes = r2
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.invoke.MethodType.<init>(java.lang.Class, java.lang.Class[], boolean):void");
    }

    private MethodType(Class<?>[] clsArr, Class<?> cls) {
        this.rtype = cls;
        this.ptypes = clsArr;
    }

    /* access modifiers changed from: package-private */
    public MethodTypeForm form() {
        return this.form;
    }

    public Class<?> rtype() {
        return this.rtype;
    }

    public Class<?>[] ptypes() {
        return this.ptypes;
    }

    private static void checkRtype(Class<?> cls) {
        Objects.requireNonNull(cls);
    }

    private static void checkPtype(Class<?> cls) {
        Objects.requireNonNull(cls);
        if (cls == Void.TYPE) {
            throw MethodHandleStatics.newIllegalArgumentException("parameter type cannot be void");
        }
    }

    private static int checkPtypes(Class<?>[] clsArr) {
        int i = 0;
        for (Class<Long> cls : clsArr) {
            checkPtype(cls);
            if (cls == Double.TYPE || cls == Long.TYPE) {
                i++;
            }
        }
        checkSlotCount(clsArr.length + i);
        return i;
    }

    static void checkSlotCount(int i) {
        if ((i & 255) != i) {
            throw MethodHandleStatics.newIllegalArgumentException("bad parameter count " + i);
        }
    }

    private static IndexOutOfBoundsException newIndexOutOfBoundsException(Object obj) {
        if (obj instanceof Integer) {
            obj = "bad index: " + obj;
        }
        return new IndexOutOfBoundsException(obj.toString());
    }

    public static MethodType methodType(Class<?> cls, Class<?>[] clsArr) {
        return makeImpl(cls, clsArr, false);
    }

    public static MethodType methodType(Class<?> cls, List<Class<?>> list) {
        return makeImpl(cls, listToArray(list), false);
    }

    private static Class<?>[] listToArray(List<Class<?>> list) {
        checkSlotCount(list.size());
        return (Class[]) list.toArray(NO_PTYPES);
    }

    public static MethodType methodType(Class<?> cls, Class<?> cls2, Class<?>... clsArr) {
        Class[] clsArr2 = new Class[(clsArr.length + 1)];
        clsArr2[0] = cls2;
        System.arraycopy((Object) clsArr, 0, (Object) clsArr2, 1, clsArr.length);
        return makeImpl(cls, clsArr2, true);
    }

    public static MethodType methodType(Class<?> cls) {
        return makeImpl(cls, NO_PTYPES, true);
    }

    public static MethodType methodType(Class<?> cls, Class<?> cls2) {
        return makeImpl(cls, new Class[]{cls2}, true);
    }

    public static MethodType methodType(Class<?> cls, MethodType methodType) {
        return makeImpl(cls, methodType.ptypes, true);
    }

    static MethodType makeImpl(Class<?> cls, Class<?>[] clsArr, boolean z) {
        ConcurrentWeakInternSet<MethodType> concurrentWeakInternSet = internTable;
        MethodType methodType = concurrentWeakInternSet.get(new MethodType(clsArr, cls));
        if (methodType != null) {
            return methodType;
        }
        if (clsArr.length == 0) {
            clsArr = NO_PTYPES;
            z = true;
        }
        MethodType methodType2 = new MethodType(cls, clsArr, z);
        methodType2.form = MethodTypeForm.findForm(methodType2);
        return concurrentWeakInternSet.add(methodType2);
    }

    public static MethodType genericMethodType(int i, boolean z) {
        MethodType methodType;
        checkSlotCount(i);
        int i2 = (i * 2) + (z ? 1 : 0);
        MethodType[] methodTypeArr = objectOnlyTypes;
        if (i2 < methodTypeArr.length && (methodType = methodTypeArr[i2]) != null) {
            return methodType;
        }
        Class[] clsArr = new Class[(i + z)];
        Arrays.fill((Object[]) clsArr, (Object) Object.class);
        if (z) {
            clsArr[i] = Object[].class;
        }
        MethodType makeImpl = makeImpl(Object.class, clsArr, true);
        if (i2 < methodTypeArr.length) {
            methodTypeArr[i2] = makeImpl;
        }
        return makeImpl;
    }

    public static MethodType genericMethodType(int i) {
        return genericMethodType(i, false);
    }

    public MethodType changeParameterType(int i, Class<?> cls) {
        if (parameterType(i) == cls) {
            return this;
        }
        checkPtype(cls);
        Class[] clsArr = (Class[]) this.ptypes.clone();
        clsArr[i] = cls;
        return makeImpl(this.rtype, clsArr, true);
    }

    public MethodType insertParameterTypes(int i, Class<?>... clsArr) {
        int length = this.ptypes.length;
        if (i < 0 || i > length) {
            throw newIndexOutOfBoundsException(Integer.valueOf(i));
        }
        checkSlotCount(parameterSlotCount() + clsArr.length + checkPtypes(clsArr));
        int length2 = clsArr.length;
        if (length2 == 0) {
            return this;
        }
        Class[] clsArr2 = (Class[]) Arrays.copyOfRange((T[]) this.ptypes, 0, length + length2);
        System.arraycopy((Object) clsArr2, i, (Object) clsArr2, i + length2, length - i);
        System.arraycopy((Object) clsArr, 0, (Object) clsArr2, i, length2);
        return makeImpl(this.rtype, clsArr2, true);
    }

    public MethodType appendParameterTypes(Class<?>... clsArr) {
        return insertParameterTypes(parameterCount(), clsArr);
    }

    public MethodType insertParameterTypes(int i, List<Class<?>> list) {
        return insertParameterTypes(i, (Class<?>[]) listToArray(list));
    }

    public MethodType appendParameterTypes(List<Class<?>> list) {
        return insertParameterTypes(parameterCount(), list);
    }

    /* access modifiers changed from: package-private */
    public MethodType replaceParameterTypes(int i, int i2, Class<?>... clsArr) {
        if (i == i2) {
            return insertParameterTypes(i, clsArr);
        }
        int length = this.ptypes.length;
        if (i < 0 || i > i2 || i2 > length) {
            throw newIndexOutOfBoundsException("start=" + i + " end=" + i2);
        } else if (clsArr.length == 0) {
            return dropParameterTypes(i, i2);
        } else {
            return dropParameterTypes(i, i2).insertParameterTypes(i, clsArr);
        }
    }

    /* access modifiers changed from: package-private */
    public MethodType asSpreaderType(Class<?> cls, int i, int i2) {
        if (i2 == 0) {
            return this;
        }
        if (cls == Object[].class) {
            if (isGeneric()) {
                return this;
            }
            if (i == 0) {
                MethodType genericMethodType = genericMethodType(i2);
                Class<?> cls2 = this.rtype;
                return cls2 != Object.class ? genericMethodType.changeReturnType(cls2) : genericMethodType;
            }
        }
        Class<?> componentType = cls.getComponentType();
        int i3 = i;
        while (true) {
            int i4 = i + i2;
            if (i3 >= i4) {
                return this;
            }
            Class<?>[] clsArr = this.ptypes;
            if (clsArr[i3] != componentType) {
                Class[] clsArr2 = (Class[]) clsArr.clone();
                Arrays.fill((Object[]) clsArr2, i3, i4, (Object) componentType);
                return methodType(this.rtype, (Class<?>[]) clsArr2);
            }
            i3++;
        }
    }

    /* access modifiers changed from: package-private */
    public Class<?> leadingReferenceParameter() {
        Class<?>[] clsArr = this.ptypes;
        if (clsArr.length != 0) {
            Class<?> cls = clsArr[0];
            if (!cls.isPrimitive()) {
                return cls;
            }
        }
        throw MethodHandleStatics.newIllegalArgumentException("no leading reference parameter");
    }

    /* access modifiers changed from: package-private */
    public MethodType asCollectorType(Class<?> cls, int i, int i2) {
        MethodType methodType;
        if (cls == Object[].class) {
            methodType = genericMethodType(i2);
            Class<?> cls2 = this.rtype;
            if (cls2 != Object.class) {
                methodType = methodType.changeReturnType(cls2);
            }
        } else {
            methodType = methodType(this.rtype, (List<Class<?>>) Collections.nCopies(i2, cls.getComponentType()));
        }
        Class<?>[] clsArr = this.ptypes;
        if (clsArr.length == 1) {
            return methodType;
        }
        if (i < clsArr.length - 1) {
            methodType = methodType.insertParameterTypes(i2, (Class<?>[]) (Class[]) Arrays.copyOfRange((T[]) clsArr, i + 1, clsArr.length));
        }
        return methodType.insertParameterTypes(0, (Class<?>[]) (Class[]) Arrays.copyOf((T[]) this.ptypes, i));
    }

    public MethodType dropParameterTypes(int i, int i2) {
        Class<?>[] clsArr;
        Class<?>[] clsArr2 = this.ptypes;
        int length = clsArr2.length;
        if (i < 0 || i > i2 || i2 > length) {
            throw newIndexOutOfBoundsException("start=" + i + " end=" + i2);
        } else if (i == i2) {
            return this;
        } else {
            if (i == 0) {
                if (i2 == length) {
                    clsArr = NO_PTYPES;
                } else {
                    clsArr = (Class[]) Arrays.copyOfRange((T[]) clsArr2, i2, length);
                }
            } else if (i2 == length) {
                clsArr = (Class[]) Arrays.copyOfRange((T[]) clsArr2, 0, i);
            } else {
                int i3 = length - i2;
                Class<?>[] clsArr3 = (Class[]) Arrays.copyOfRange((T[]) clsArr2, 0, i + i3);
                System.arraycopy((Object) this.ptypes, i2, (Object) clsArr3, i, i3);
                clsArr = clsArr3;
            }
            return makeImpl(this.rtype, clsArr, true);
        }
    }

    public MethodType changeReturnType(Class<?> cls) {
        if (returnType() == cls) {
            return this;
        }
        return makeImpl(cls, this.ptypes, true);
    }

    public boolean hasPrimitives() {
        return this.form.hasPrimitives();
    }

    public boolean hasWrappers() {
        return unwrap() != this;
    }

    public MethodType erase() {
        return this.form.erasedType();
    }

    public MethodType generic() {
        return genericMethodType(parameterCount());
    }

    /* access modifiers changed from: package-private */
    public boolean isGeneric() {
        return this == erase() && !hasPrimitives();
    }

    public MethodType wrap() {
        return hasPrimitives() ? wrapWithPrims(this) : this;
    }

    public MethodType unwrap() {
        if (hasPrimitives()) {
            this = wrapWithPrims(this);
        }
        return unwrapWithNoPrims(this);
    }

    private static MethodType wrapWithPrims(MethodType methodType) {
        MethodType methodType2 = methodType.wrapAlt;
        if (methodType2 != null) {
            return methodType2;
        }
        MethodType canonicalize = MethodTypeForm.canonicalize(methodType, 2, 2);
        methodType.wrapAlt = canonicalize;
        return canonicalize;
    }

    private static MethodType unwrapWithNoPrims(MethodType methodType) {
        MethodType methodType2 = methodType.wrapAlt;
        if (methodType2 == null) {
            methodType2 = MethodTypeForm.canonicalize(methodType, 3, 3);
            if (methodType2 == null) {
                methodType2 = methodType;
            }
            methodType.wrapAlt = methodType2;
        }
        return methodType2;
    }

    public Class<?> parameterType(int i) {
        return this.ptypes[i];
    }

    public int parameterCount() {
        return this.ptypes.length;
    }

    public Class<?> returnType() {
        return this.rtype;
    }

    public List<Class<?>> parameterList() {
        return Collections.unmodifiableList(Arrays.asList((Class[]) this.ptypes.clone()));
    }

    public Class<?> lastParameterType() {
        Class<?>[] clsArr = this.ptypes;
        int length = clsArr.length;
        return length == 0 ? Void.TYPE : clsArr[length - 1];
    }

    public Class<?>[] parameterArray() {
        return (Class[]) this.ptypes.clone();
    }

    public boolean equals(Object obj) {
        return this == obj || ((obj instanceof MethodType) && equals((MethodType) obj));
    }

    private boolean equals(MethodType methodType) {
        return this.rtype == methodType.rtype && Arrays.equals((Object[]) this.ptypes, (Object[]) methodType.ptypes);
    }

    public int hashCode() {
        int hashCode = this.rtype.hashCode() + 31;
        for (Class<?> hashCode2 : this.ptypes) {
            hashCode = (hashCode * 31) + hashCode2.hashCode();
        }
        return hashCode;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.KEY_CODE_START);
        for (int i = 0; i < this.ptypes.length; i++) {
            if (i > 0) {
                sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR);
            }
            sb.append(this.ptypes[i].getSimpleName());
        }
        sb.append(NavigationBarInflaterView.KEY_CODE_END);
        sb.append(this.rtype.getSimpleName());
        return sb.toString();
    }

    /* access modifiers changed from: package-private */
    public boolean effectivelyIdenticalParameters(int i, List<Class<?>> list) {
        int i2;
        int length = this.ptypes.length;
        int size = list.size();
        if (i > length || (i2 = length - i) > size) {
            return false;
        }
        List asList = Arrays.asList(this.ptypes);
        if (i != 0) {
            asList = asList.subList(i, length);
            length = i2;
        }
        if (size == length) {
            return asList.equals(list);
        }
        return asList.equals(list.subList(0, length));
    }

    /* access modifiers changed from: package-private */
    public boolean isConvertibleTo(MethodType methodType) {
        if (!canConvert(returnType(), methodType.returnType())) {
            return false;
        }
        Class<?>[] clsArr = methodType.ptypes;
        Class<?>[] clsArr2 = this.ptypes;
        if (clsArr == clsArr2) {
            return true;
        }
        int length = clsArr.length;
        if (length != clsArr2.length) {
            return false;
        }
        if (length > 1) {
            return canConvertParameters(clsArr, clsArr2);
        }
        if (length != 1 || canConvert(clsArr[0], clsArr2[0])) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: package-private */
    public boolean explicitCastEquivalentToAsType(MethodType methodType) {
        if (this == methodType) {
            return true;
        }
        if (!explicitCastEquivalentToAsType(this.rtype, methodType.rtype)) {
            return false;
        }
        Class<?>[] clsArr = methodType.ptypes;
        Class<?>[] clsArr2 = this.ptypes;
        if (clsArr2 == clsArr) {
            return true;
        }
        for (int i = 0; i < clsArr2.length; i++) {
            if (!explicitCastEquivalentToAsType(clsArr[i], clsArr2[i])) {
                return false;
            }
        }
        return true;
    }

    private static boolean explicitCastEquivalentToAsType(Class<?> cls, Class<?> cls2) {
        if (cls == cls2 || cls2 == Object.class || cls2 == Void.TYPE) {
            return true;
        }
        if (cls.isPrimitive()) {
            return canConvert(cls, cls2);
        }
        if (cls2.isPrimitive()) {
            return false;
        }
        if (!cls2.isInterface() || cls2.isAssignableFrom(cls)) {
            return true;
        }
        return false;
    }

    private boolean canConvertParameters(Class<?>[] clsArr, Class<?>[] clsArr2) {
        for (int i = 0; i < clsArr.length; i++) {
            if (!canConvert(clsArr[i], clsArr2[i])) {
                return false;
            }
        }
        return true;
    }

    static boolean canConvert(Class<?> cls, Class<?> cls2) {
        if (!(cls == cls2 || cls == Object.class || cls2 == Object.class)) {
            if (cls.isPrimitive()) {
                if (cls == Void.TYPE) {
                    return true;
                }
                Wrapper forPrimitiveType = Wrapper.forPrimitiveType(cls);
                if (cls2.isPrimitive()) {
                    return Wrapper.forPrimitiveType(cls2).isConvertibleFrom(forPrimitiveType);
                }
                return cls2.isAssignableFrom(forPrimitiveType.wrapperType());
            } else if (!cls2.isPrimitive() || cls2 == Void.TYPE) {
                return true;
            } else {
                Wrapper forPrimitiveType2 = Wrapper.forPrimitiveType(cls2);
                if (cls.isAssignableFrom(forPrimitiveType2.wrapperType())) {
                    return true;
                }
                if (!Wrapper.isWrapperType(cls) || !forPrimitiveType2.isConvertibleFrom(Wrapper.forWrapperType(cls))) {
                    return false;
                }
                return true;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public int parameterSlotCount() {
        return this.form.parameterSlotCount();
    }

    public static MethodType fromMethodDescriptorString(String str, ClassLoader classLoader) throws IllegalArgumentException, TypeNotPresentException {
        if (!str.startsWith(NavigationBarInflaterView.KEY_CODE_START) || str.indexOf(41) < 0 || str.indexOf(46) >= 0) {
            throw MethodHandleStatics.newIllegalArgumentException("not a method descriptor: " + str);
        }
        List<Class<?>> parseMethod = BytecodeDescriptor.parseMethod(str, classLoader);
        checkSlotCount(parseMethod.size());
        return makeImpl(parseMethod.remove(parseMethod.size() - 1), listToArray(parseMethod), true);
    }

    public String toMethodDescriptorString() {
        String str = this.methodDescriptor;
        if (str != null) {
            return str;
        }
        String unparse = BytecodeDescriptor.unparse(this);
        this.methodDescriptor = unparse;
        return unparse;
    }

    static String toFieldDescriptorString(Class<?> cls) {
        return BytecodeDescriptor.unparse(cls);
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        objectOutputStream.defaultWriteObject();
        objectOutputStream.writeObject(returnType());
        objectOutputStream.writeObject(parameterArray());
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        Class cls = (Class) objectInputStream.readObject();
        Class[] clsArr = (Class[]) objectInputStream.readObject();
        checkRtype(cls);
        checkPtypes(clsArr);
        MethodType_init(cls, (Class[]) clsArr.clone());
    }

    private MethodType() {
        this.rtype = null;
        this.ptypes = null;
    }

    private void MethodType_init(Class<?> cls, Class<?>[] clsArr) {
        checkRtype(cls);
        checkPtypes(clsArr);
        MethodHandleStatics.UNSAFE.putObject(this, rtypeOffset, cls);
        MethodHandleStatics.UNSAFE.putObject(this, ptypesOffset, clsArr);
    }

    private Object readResolve() {
        return methodType(this.rtype, this.ptypes);
    }

    private static class ConcurrentWeakInternSet<T> {
        private final ConcurrentMap<WeakEntry<T>, WeakEntry<T>> map = new ConcurrentHashMap();
        private final ReferenceQueue<T> stale = new ReferenceQueue<>();

        public T get(T t) {
            T t2;
            t.getClass();
            expungeStaleElements();
            WeakEntry weakEntry = this.map.get(new WeakEntry(t));
            if (weakEntry == null || (t2 = weakEntry.get()) == null) {
                return null;
            }
            return t2;
        }

        public T add(T t) {
            T t2;
            t.getClass();
            WeakEntry weakEntry = new WeakEntry(t, this.stale);
            do {
                expungeStaleElements();
                WeakEntry putIfAbsent = this.map.putIfAbsent(weakEntry, weakEntry);
                if (putIfAbsent == null) {
                    t2 = t;
                    continue;
                } else {
                    t2 = putIfAbsent.get();
                    continue;
                }
            } while (t2 == null);
            return t2;
        }

        private void expungeStaleElements() {
            while (true) {
                Reference<? extends T> poll = this.stale.poll();
                if (poll != null) {
                    this.map.remove(poll);
                } else {
                    return;
                }
            }
        }

        private static class WeakEntry<T> extends WeakReference<T> {
            public final int hashcode;

            public WeakEntry(T t, ReferenceQueue<T> referenceQueue) {
                super(t, referenceQueue);
                this.hashcode = t.hashCode();
            }

            public WeakEntry(T t) {
                super(t);
                this.hashcode = t.hashCode();
            }

            public boolean equals(Object obj) {
                if (!(obj instanceof WeakEntry)) {
                    return false;
                }
                Object obj2 = ((WeakEntry) obj).get();
                Object obj3 = get();
                if (obj2 != null && obj3 != null) {
                    return obj3.equals(obj2);
                }
                if (this == obj) {
                    return true;
                }
                return false;
            }

            public int hashCode() {
                return this.hashcode;
            }
        }
    }
}
