package java.lang.invoke;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda13;
import java.lang.invoke.Transformers;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import sun.invoke.util.VerifyAccess;
import sun.invoke.util.Wrapper;
import sun.reflect.Reflection;

public class MethodHandles {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    @Stable
    private static final MethodHandle[] HANDLES = new MethodHandle[12];
    private static final MethodHandle[] IDENTITY_MHS = new MethodHandle[10];
    static final int MH_Array_newInstance = 11;
    static final int MH_LIMIT = 12;
    static final int MH_arrayIdentity = 5;
    static final int MH_cast = 0;
    static final int MH_copyAsPrimitiveArray = 2;
    static final int MH_countedLoopPred = 6;
    static final int MH_countedLoopStep = 7;
    static final int MH_fillNewArray = 4;
    static final int MH_fillNewTypedArray = 3;
    static final int MH_initIterator = 8;
    static final int MH_iterateNext = 10;
    static final int MH_iteratePred = 9;
    static final int MH_selectAlternative = 1;
    private static final MethodHandle[] ZERO_MHS = new MethodHandle[10];

    public static boolean countedLoopPredicate(int i, int i2) {
        return i2 < i;
    }

    public static int countedLoopStep(int i, int i2) {
        return i2 + 1;
    }

    public static byte identity(byte b) {
        return b;
    }

    public static char identity(char c) {
        return c;
    }

    public static double identity(double d) {
        return d;
    }

    public static float identity(float f) {
        return f;
    }

    public static int identity(int i) {
        return i;
    }

    public static long identity(long j) {
        return j;
    }

    public static short identity(short s) {
        return s;
    }

    public static boolean identity(boolean z) {
        return z;
    }

    static /* synthetic */ boolean lambda$loopChecks1cd$14(Class cls, Class cls2) {
        return cls2 != cls;
    }

    private MethodHandles() {
    }

    public static Lookup lookup() {
        return new Lookup(Reflection.getCallerClass());
    }

    public static Lookup publicLookup() {
        return Lookup.PUBLIC_LOOKUP;
    }

    public static Lookup privateLookupIn(Class<?> cls, Lookup lookup) throws IllegalAccessException {
        if (cls.isPrimitive()) {
            throw new IllegalArgumentException(cls + " is a primitive class");
        } else if (!cls.isArray()) {
            return new Lookup(cls);
        } else {
            throw new IllegalArgumentException(cls + " is an array class");
        }
    }

    public static <T extends Member> T reflectAs(Class<T> cls, MethodHandle methodHandle) {
        return (Member) cls.cast(getMethodHandleImpl(methodHandle).getMemberInternal());
    }

    public static final class Lookup {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final int ALL_MODES = 15;
        static final Lookup IMPL_LOOKUP = new Lookup(Object.class, 15);
        public static final int PACKAGE = 8;
        public static final int PRIVATE = 2;
        public static final int PROTECTED = 4;
        public static final int PUBLIC = 1;
        static final Lookup PUBLIC_LOOKUP = new Lookup(Object.class, 1);
        private final int allowedModes;
        private final Class<?> lookupClass;

        private static int fixmods(int i) {
            int i2 = i & 7;
            if (i2 != 0) {
                return i2;
            }
            return 8;
        }

        static {
            Class<MethodHandles> cls = MethodHandles.class;
        }

        public Class<?> lookupClass() {
            return this.lookupClass;
        }

        public int lookupModes() {
            return this.allowedModes & 15;
        }

        Lookup(Class<?> cls) {
            this(cls, 15);
            checkUnprivilegedlookupClass(cls, 15);
        }

        private Lookup(Class<?> cls, int i) {
            this.lookupClass = cls;
            this.allowedModes = i;
        }

        /* renamed from: in */
        public Lookup mo59954in(Class<?> cls) {
            cls.getClass();
            Class<?> cls2 = this.lookupClass;
            if (cls == cls2) {
                return this;
            }
            int i = this.allowedModes & 11;
            if ((i & 8) != 0 && !VerifyAccess.isSamePackage(cls2, cls)) {
                i &= -11;
            }
            if ((i & 2) != 0 && !VerifyAccess.isSamePackageMember(this.lookupClass, cls)) {
                i &= -3;
            }
            if ((i & 1) != 0 && !VerifyAccess.isClassAccessible(cls, this.lookupClass, this.allowedModes)) {
                i = 0;
            }
            checkUnprivilegedlookupClass(cls, i);
            return new Lookup(cls, i);
        }

        private static void checkUnprivilegedlookupClass(Class<?> cls, int i) {
            String name = cls.getName();
            if (name.startsWith("java.lang.invoke.")) {
                throw MethodHandleStatics.newIllegalArgumentException("illegal lookupClass: " + cls);
            } else if (i != 15 || cls.getClassLoader() != Object.class.getClassLoader()) {
            } else {
                if ((name.startsWith("java.") && !name.startsWith("java.util.concurrent.") && !name.equals("java.lang.Thread")) || (name.startsWith("sun.") && !name.startsWith("sun.invoke.") && !name.equals("sun.reflect.ReflectionFactory"))) {
                    throw MethodHandleStatics.newIllegalArgumentException("illegal lookupClass: " + cls);
                }
            }
        }

        public String toString() {
            String name = this.lookupClass.getName();
            int i = this.allowedModes;
            if (i == 0) {
                return name + "/noaccess";
            } else if (i == 1) {
                return name + "/public";
            } else if (i == 9) {
                return name + "/package";
            } else if (i == 11) {
                return name + "/private";
            } else if (i == 15) {
                return name;
            } else {
                return name + "/" + Integer.toHexString(this.allowedModes);
            }
        }

        public MethodHandle findStatic(Class<?> cls, String str, MethodType methodType) throws NoSuchMethodException, IllegalAccessException {
            Method declaredMethod = cls.getDeclaredMethod(str, methodType.ptypes());
            int modifiers = declaredMethod.getModifiers();
            if (Modifier.isStatic(modifiers)) {
                checkReturnType(declaredMethod, methodType);
                checkAccess(cls, declaredMethod.getDeclaringClass(), modifiers, declaredMethod.getName());
                return createMethodHandle(declaredMethod, 3, methodType);
            }
            throw new IllegalAccessException("Method" + declaredMethod + " is not static");
        }

        private MethodHandle findVirtualForMH(String str, MethodType methodType) {
            if ("invoke".equals(str)) {
                return MethodHandles.invoker(methodType);
            }
            if ("invokeExact".equals(str)) {
                return MethodHandles.exactInvoker(methodType);
            }
            return null;
        }

        private MethodHandle findVirtualForVH(String str, MethodType methodType) {
            try {
                return MethodHandles.varHandleInvoker(VarHandle.AccessMode.valueFromMethodName(str), methodType);
            } catch (IllegalArgumentException unused) {
                return null;
            }
        }

        private static MethodHandle createMethodHandle(Method method, int i, MethodType methodType) {
            MethodHandleImpl methodHandleImpl = new MethodHandleImpl(method.getArtMethod(), i, methodType);
            return method.isVarArgs() ? new Transformers.VarargsCollector(methodHandleImpl) : methodHandleImpl;
        }

        public MethodHandle findVirtual(Class<?> cls, String str, MethodType methodType) throws NoSuchMethodException, IllegalAccessException {
            MethodHandle findVirtualForVH;
            if (cls == MethodHandle.class) {
                MethodHandle findVirtualForMH = findVirtualForMH(str, methodType);
                if (findVirtualForMH != null) {
                    return findVirtualForMH;
                }
            } else if (cls == VarHandle.class && (findVirtualForVH = findVirtualForVH(str, methodType)) != null) {
                return findVirtualForVH;
            }
            Method instanceMethod = cls.getInstanceMethod(str, methodType.ptypes());
            if (instanceMethod == null) {
                try {
                    Method declaredMethod = cls.getDeclaredMethod(str, methodType.ptypes());
                    if (Modifier.isStatic(declaredMethod.getModifiers())) {
                        throw new IllegalAccessException("Method" + declaredMethod + " is static");
                    }
                } catch (NoSuchMethodException unused) {
                }
                throw new NoSuchMethodException(str + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + Arrays.toString((Object[]) methodType.ptypes()));
            }
            checkReturnType(instanceMethod, methodType);
            checkAccess(cls, instanceMethod.getDeclaringClass(), instanceMethod.getModifiers(), instanceMethod.getName());
            return createMethodHandle(instanceMethod, 0, methodType.insertParameterTypes(0, (Class<?>[]) new Class[]{cls}));
        }

        public MethodHandle findConstructor(Class<?> cls, MethodType methodType) throws NoSuchMethodException, IllegalAccessException {
            if (!cls.isArray()) {
                Constructor<?> declaredConstructor = cls.getDeclaredConstructor(methodType.ptypes());
                if (declaredConstructor != null) {
                    checkAccess(cls, declaredConstructor.getDeclaringClass(), declaredConstructor.getModifiers(), declaredConstructor.getName());
                    return createMethodHandleForConstructor(declaredConstructor);
                }
                throw new NoSuchMethodException("No constructor for " + declaredConstructor.getDeclaringClass() + " matching " + methodType);
            }
            throw new NoSuchMethodException("no constructor for array class: " + cls.getName());
        }

        private MethodHandle createMethodHandleForConstructor(Constructor constructor) {
            MethodHandle methodHandle;
            Class<String> declaringClass = constructor.getDeclaringClass();
            MethodType methodType = MethodType.methodType((Class<?>) declaringClass, (Class<?>[]) constructor.getParameterTypes());
            if (declaringClass == String.class) {
                methodHandle = new MethodHandleImpl(constructor.getArtMethod(), 2, methodType);
            } else {
                methodHandle = new Transformers.Construct(new MethodHandleImpl(constructor.getArtMethod(), 2, initMethodType(methodType)), methodType);
            }
            return constructor.isVarArgs() ? new Transformers.VarargsCollector(methodHandle) : methodHandle;
        }

        private static MethodType initMethodType(MethodType methodType) {
            Class[] clsArr = new Class[(methodType.ptypes().length + 1)];
            clsArr[0] = methodType.rtype();
            System.arraycopy((Object) methodType.ptypes(), 0, (Object) clsArr, 1, methodType.ptypes().length);
            return MethodType.methodType((Class<?>) Void.TYPE, (Class<?>[]) clsArr);
        }

        public MethodHandle findSpecial(Class<?> cls, String str, MethodType methodType, Class<?> cls2) throws NoSuchMethodException, IllegalAccessException {
            if (cls2 == null) {
                throw new NullPointerException("specialCaller == null");
            } else if (methodType == null) {
                throw new NullPointerException("type == null");
            } else if (str == null) {
                throw new NullPointerException("name == null");
            } else if (cls != null) {
                checkSpecialCaller(cls2, cls);
                if (!str.startsWith("<")) {
                    Method declaredMethod = cls.getDeclaredMethod(str, methodType.ptypes());
                    checkReturnType(declaredMethod, methodType);
                    return findSpecial(declaredMethod, methodType, cls, cls2);
                }
                throw new NoSuchMethodException(str + " is not a valid method name.");
            } else {
                throw new NullPointerException("ref == null");
            }
        }

        private MethodHandle findSpecial(Method method, MethodType methodType, Class<?> cls, Class<?> cls2) throws IllegalAccessException {
            if (Modifier.isStatic(method.getModifiers())) {
                throw new IllegalAccessException("expected a non-static method:" + method);
            } else if (Modifier.isPrivate(method.getModifiers())) {
                if (cls == lookupClass()) {
                    return createMethodHandle(method, 2, methodType.insertParameterTypes(0, (Class<?>[]) new Class[]{cls}));
                }
                throw new IllegalAccessException("no private access for invokespecial : " + cls + ", from" + this);
            } else if (method.getDeclaringClass().isAssignableFrom(cls2)) {
                return createMethodHandle(method, 1, methodType.insertParameterTypes(0, (Class<?>[]) new Class[]{cls2}));
            } else {
                throw new IllegalAccessException(cls + "is not assignable from " + cls2);
            }
        }

        public MethodHandle findGetter(Class<?> cls, String str, Class<?> cls2) throws NoSuchFieldException, IllegalAccessException {
            return findAccessor(cls, str, cls2, 8);
        }

        private MethodHandle findAccessor(Class<?> cls, String str, Class<?> cls2, int i) throws NoSuchFieldException, IllegalAccessException {
            return findAccessor(findFieldOfType(cls, str, cls2), cls, cls2, i, true);
        }

        private MethodHandle findAccessor(Field field, Class<?> cls, Class<?> cls2, int i, boolean z) throws IllegalAccessException {
            MethodType methodType;
            Class<?> cls3 = cls;
            Class<?> cls4 = cls2;
            int i2 = i;
            boolean z2 = i2 == 9 || i2 == 11;
            commonFieldChecks(field, cls, cls2, i2 == 10 || i2 == 11, z);
            if (z) {
                int modifiers = field.getModifiers();
                if (z2 && Modifier.isFinal(modifiers)) {
                    StringBuilder sb = new StringBuilder("Field ");
                    Field field2 = field;
                    sb.append((Object) field);
                    sb.append(" is final");
                    throw new IllegalAccessException(sb.toString());
                }
            }
            Field field3 = field;
            switch (i2) {
                case 8:
                    methodType = MethodType.methodType(cls2, cls);
                    break;
                case 9:
                    methodType = MethodType.methodType(Void.TYPE, cls, cls4);
                    break;
                case 10:
                    methodType = MethodType.methodType(cls2);
                    break;
                case 11:
                    methodType = MethodType.methodType((Class<?>) Void.TYPE, cls2);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid kind " + i2);
            }
            return new MethodHandleImpl(field.getArtField(), i2, methodType);
        }

        public MethodHandle findSetter(Class<?> cls, String str, Class<?> cls2) throws NoSuchFieldException, IllegalAccessException {
            return findAccessor(cls, str, cls2, 9);
        }

        public VarHandle findVarHandle(Class<?> cls, String str, Class<?> cls2) throws NoSuchFieldException, IllegalAccessException {
            Field findFieldOfType = findFieldOfType(cls, str, cls2);
            commonFieldChecks(findFieldOfType, cls, cls2, false, true);
            return FieldVarHandle.create(findFieldOfType);
        }

        private Field findFieldOfType(Class<?> cls, String str, Class<?> cls2) throws NoSuchFieldException {
            Field field;
            Class<?> cls3 = cls;
            while (true) {
                if (cls3 == null) {
                    field = null;
                    break;
                }
                try {
                    field = cls3.getDeclaredField(str);
                    break;
                } catch (NoSuchFieldException unused) {
                    cls3 = cls3.getSuperclass();
                }
            }
            if (field == null) {
                field = cls.getDeclaredField(str);
            }
            if (field.getType() == cls2) {
                return field;
            }
            throw new NoSuchFieldException(str);
        }

        private void commonFieldChecks(Field field, Class<?> cls, Class<?> cls2, boolean z, boolean z2) throws IllegalAccessException {
            int modifiers = field.getModifiers();
            if (z2) {
                checkAccess(cls, field.getDeclaringClass(), modifiers, field.getName());
            }
            if (Modifier.isStatic(modifiers) != z) {
                StringBuilder sb = new StringBuilder("Field ");
                sb.append((Object) field);
                sb.append(" is ");
                sb.append(z ? "not " : "");
                sb.append("static");
                throw new IllegalAccessException(sb.toString());
            }
        }

        public MethodHandle findStaticGetter(Class<?> cls, String str, Class<?> cls2) throws NoSuchFieldException, IllegalAccessException {
            return findAccessor(cls, str, cls2, 10);
        }

        public MethodHandle findStaticSetter(Class<?> cls, String str, Class<?> cls2) throws NoSuchFieldException, IllegalAccessException {
            return findAccessor(cls, str, cls2, 11);
        }

        public VarHandle findStaticVarHandle(Class<?> cls, String str, Class<?> cls2) throws NoSuchFieldException, IllegalAccessException {
            Field findFieldOfType = findFieldOfType(cls, str, cls2);
            commonFieldChecks(findFieldOfType, cls, cls2, true, true);
            return StaticFieldVarHandle.create(findFieldOfType);
        }

        public MethodHandle bind(Object obj, String str, MethodType methodType) throws NoSuchMethodException, IllegalAccessException {
            MethodHandle findVirtual = findVirtual(obj.getClass(), str, methodType);
            MethodHandle bindTo = findVirtual.bindTo(obj);
            MethodType type = bindTo.type();
            return findVirtual.isVarargsCollector() ? bindTo.asVarargsCollector(type.parameterType(type.parameterCount() - 1)) : bindTo;
        }

        public MethodHandle unreflect(Method method) throws IllegalAccessException {
            if (method != null) {
                MethodType methodType = MethodType.methodType(method.getReturnType(), (Class<?>[]) method.getParameterTypes());
                if (!method.isAccessible()) {
                    checkAccess(method.getDeclaringClass(), method.getDeclaringClass(), method.getModifiers(), method.getName());
                }
                if (Modifier.isStatic(method.getModifiers())) {
                    return createMethodHandle(method, 3, methodType);
                }
                return createMethodHandle(method, 0, methodType.insertParameterTypes(0, (Class<?>[]) new Class[]{method.getDeclaringClass()}));
            }
            throw new NullPointerException("m == null");
        }

        public MethodHandle unreflectSpecial(Method method, Class<?> cls) throws IllegalAccessException {
            if (method == null) {
                throw new NullPointerException("m == null");
            } else if (cls != null) {
                if (!method.isAccessible()) {
                    checkSpecialCaller(cls, (Class<?>) null);
                }
                return findSpecial(method, MethodType.methodType(method.getReturnType(), (Class<?>[]) method.getParameterTypes()), method.getDeclaringClass(), cls);
            } else {
                throw new NullPointerException("specialCaller == null");
            }
        }

        public MethodHandle unreflectConstructor(Constructor<?> constructor) throws IllegalAccessException {
            if (constructor != null) {
                if (!constructor.isAccessible()) {
                    checkAccess(constructor.getDeclaringClass(), constructor.getDeclaringClass(), constructor.getModifiers(), constructor.getName());
                }
                return createMethodHandleForConstructor(constructor);
            }
            throw new NullPointerException("c == null");
        }

        public MethodHandle unreflectGetter(Field field) throws IllegalAccessException {
            return findAccessor(field, field.getDeclaringClass(), field.getType(), Modifier.isStatic(field.getModifiers()) ? 10 : 8, !field.isAccessible());
        }

        public MethodHandle unreflectSetter(Field field) throws IllegalAccessException {
            return findAccessor(field, field.getDeclaringClass(), field.getType(), Modifier.isStatic(field.getModifiers()) ? 11 : 9, !field.isAccessible());
        }

        public VarHandle unreflectVarHandle(Field field) throws IllegalAccessException {
            boolean isStatic = Modifier.isStatic(field.getModifiers());
            commonFieldChecks(field, field.getDeclaringClass(), field.getType(), isStatic, true);
            return isStatic ? StaticFieldVarHandle.create(field) : FieldVarHandle.create(field);
        }

        public MethodHandleInfo revealDirect(MethodHandle methodHandle) {
            MethodHandleInfo reveal = MethodHandles.getMethodHandleImpl(methodHandle).reveal();
            try {
                checkAccess(lookupClass(), reveal.getDeclaringClass(), reveal.getModifiers(), reveal.getName());
                return reveal;
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Unable to access memeber.", e);
            }
        }

        private boolean hasPrivateAccess() {
            return (this.allowedModes & 2) != 0;
        }

        /* access modifiers changed from: package-private */
        public void checkAccess(Class<?> cls, Class<?> cls2, int i, String str) throws IllegalAccessException {
            int i2 = this.allowedModes;
            if (Modifier.isProtected(i) && cls2 == Object.class && "clone".equals(str) && cls.isArray()) {
                i ^= 5;
            }
            if (Modifier.isProtected(i) && Modifier.isConstructor(i)) {
                i ^= 4;
            }
            if (!Modifier.isPublic(i) || !Modifier.isPublic(cls.getModifiers()) || i2 == 0) {
                int fixmods = fixmods(i);
                if ((fixmods & i2) != 0) {
                    if (VerifyAccess.isMemberAccessible(cls, cls2, i, lookupClass(), i2)) {
                        return;
                    }
                } else if (!((fixmods & 4) == 0 || (i2 & 8) == 0 || !VerifyAccess.isSamePackage(cls2, lookupClass()))) {
                    return;
                }
                throwMakeAccessException(accessFailedMessage(cls, cls2, i), this);
            }
        }

        /* access modifiers changed from: package-private */
        public String accessFailedMessage(Class<?> cls, Class<?> cls2, int i) {
            boolean z = true;
            boolean z2 = Modifier.isPublic(cls2.getModifiers()) && (cls2 == cls || Modifier.isPublic(cls.getModifiers()));
            if (!z2 && (this.allowedModes & 8) != 0) {
                if (!VerifyAccess.isClassAccessible(cls2, lookupClass(), 15) || (cls2 != cls && !VerifyAccess.isClassAccessible(cls, lookupClass(), 15))) {
                    z = false;
                }
                z2 = z;
            }
            if (!z2) {
                return "class is not public";
            }
            if (Modifier.isPublic(i)) {
                return "access to public member failed";
            }
            if (Modifier.isPrivate(i)) {
                return "member is private";
            }
            return Modifier.isProtected(i) ? "member is protected" : "member is private to package";
        }

        private void checkSpecialCaller(Class<?> cls, Class<?> cls2) throws IllegalAccessException {
            boolean z = cls2 != null && cls2.isInterface() && cls2.isAssignableFrom(cls);
            if (!hasPrivateAccess() || (cls != lookupClass() && !z)) {
                throw new IllegalAccessException("no private access for invokespecial : " + cls + ", from" + this);
            }
        }

        private void throwMakeAccessException(String str, Object obj) throws IllegalAccessException {
            String str2 = str + ": " + toString();
            if (obj != null) {
                str2 = str2 + ", from " + obj;
            }
            throw new IllegalAccessException(str2);
        }

        private void checkReturnType(Method method, MethodType methodType) throws NoSuchMethodException {
            if (method.getReturnType() != methodType.rtype()) {
                throw new NoSuchMethodException(method.getName() + methodType);
            }
        }
    }

    /* access modifiers changed from: private */
    public static MethodHandleImpl getMethodHandleImpl(MethodHandle methodHandle) {
        if (methodHandle instanceof Transformers.Construct) {
            methodHandle = ((Transformers.Construct) methodHandle).getConstructorHandle();
        }
        if (methodHandle instanceof Transformers.VarargsCollector) {
            methodHandle = methodHandle.asFixedArity();
        }
        if (methodHandle instanceof MethodHandleImpl) {
            return (MethodHandleImpl) methodHandle;
        }
        throw new IllegalArgumentException(methodHandle + " is not a direct handle");
    }

    public static MethodHandle arrayConstructor(Class<?> cls) throws IllegalArgumentException {
        if (cls.isArray()) {
            return new Transformers.ArrayConstructor(cls);
        }
        throw MethodHandleStatics.newIllegalArgumentException("not an array class: " + cls.getName());
    }

    public static MethodHandle arrayLength(Class<?> cls) throws IllegalArgumentException {
        if (cls.isArray()) {
            return new Transformers.ArrayLength(cls);
        }
        throw MethodHandleStatics.newIllegalArgumentException("not an array class: " + cls.getName());
    }

    private static void checkClassIsArray(Class<?> cls) {
        if (!cls.isArray()) {
            throw new IllegalArgumentException("Not an array type: " + cls);
        }
    }

    private static void checkTypeIsViewable(Class<?> cls) {
        if (cls != Short.TYPE && cls != Character.TYPE && cls != Integer.TYPE && cls != Long.TYPE && cls != Float.TYPE && cls != Double.TYPE) {
            throw new UnsupportedOperationException("Component type not supported: " + cls);
        }
    }

    public static MethodHandle arrayElementGetter(Class<?> cls) throws IllegalArgumentException {
        checkClassIsArray(cls);
        Class<?> componentType = cls.getComponentType();
        if (!componentType.isPrimitive()) {
            return new Transformers.ReferenceArrayElementGetter(cls);
        }
        try {
            return Lookup.PUBLIC_LOOKUP.findStatic(MethodHandles.class, "arrayElementGetter", MethodType.methodType(componentType, cls, Integer.TYPE));
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    public static byte arrayElementGetter(byte[] bArr, int i) {
        return bArr[i];
    }

    public static boolean arrayElementGetter(boolean[] zArr, int i) {
        return zArr[i];
    }

    public static char arrayElementGetter(char[] cArr, int i) {
        return cArr[i];
    }

    public static short arrayElementGetter(short[] sArr, int i) {
        return sArr[i];
    }

    public static int arrayElementGetter(int[] iArr, int i) {
        return iArr[i];
    }

    public static long arrayElementGetter(long[] jArr, int i) {
        return jArr[i];
    }

    public static float arrayElementGetter(float[] fArr, int i) {
        return fArr[i];
    }

    public static double arrayElementGetter(double[] dArr, int i) {
        return dArr[i];
    }

    public static MethodHandle arrayElementSetter(Class<?> cls) throws IllegalArgumentException {
        checkClassIsArray(cls);
        Class<?> componentType = cls.getComponentType();
        if (!componentType.isPrimitive()) {
            return new Transformers.ReferenceArrayElementSetter(cls);
        }
        try {
            return Lookup.PUBLIC_LOOKUP.findStatic(MethodHandles.class, "arrayElementSetter", MethodType.methodType(Void.TYPE, cls, Integer.TYPE, componentType));
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    public static void arrayElementSetter(byte[] bArr, int i, byte b) {
        bArr[i] = b;
    }

    public static void arrayElementSetter(boolean[] zArr, int i, boolean z) {
        zArr[i] = z;
    }

    public static void arrayElementSetter(char[] cArr, int i, char c) {
        cArr[i] = c;
    }

    public static void arrayElementSetter(short[] sArr, int i, short s) {
        sArr[i] = s;
    }

    public static void arrayElementSetter(int[] iArr, int i, int i2) {
        iArr[i] = i2;
    }

    public static void arrayElementSetter(long[] jArr, int i, long j) {
        jArr[i] = j;
    }

    public static void arrayElementSetter(float[] fArr, int i, float f) {
        fArr[i] = f;
    }

    public static void arrayElementSetter(double[] dArr, int i, double d) {
        dArr[i] = d;
    }

    public static VarHandle arrayElementVarHandle(Class<?> cls) throws IllegalArgumentException {
        checkClassIsArray(cls);
        return ArrayElementVarHandle.create(cls);
    }

    public static VarHandle byteArrayViewVarHandle(Class<?> cls, ByteOrder byteOrder) throws IllegalArgumentException {
        checkClassIsArray(cls);
        checkTypeIsViewable(cls.getComponentType());
        return ByteArrayViewVarHandle.create(cls, byteOrder);
    }

    public static VarHandle byteBufferViewVarHandle(Class<?> cls, ByteOrder byteOrder) throws IllegalArgumentException {
        checkClassIsArray(cls);
        checkTypeIsViewable(cls.getComponentType());
        return ByteBufferViewVarHandle.create(cls, byteOrder);
    }

    public static MethodHandle spreadInvoker(MethodType methodType, int i) {
        if (i < 0 || i > methodType.parameterCount()) {
            throw MethodHandleStatics.newIllegalArgumentException("bad argument count", Integer.valueOf(i));
        }
        return invoker(methodType).asSpreader(Object[].class, methodType.parameterCount() - i);
    }

    public static MethodHandle exactInvoker(MethodType methodType) {
        return new Transformers.Invoker(methodType, true);
    }

    public static MethodHandle invoker(MethodType methodType) {
        return new Transformers.Invoker(methodType, false);
    }

    private static MethodHandle methodHandleForVarHandleAccessor(VarHandle.AccessMode accessMode, MethodType methodType, boolean z) {
        try {
            Method declaredMethod = VarHandle.class.getDeclaredMethod(accessMode.methodName(), Object[].class);
            return new MethodHandleImpl(declaredMethod.getArtMethod(), z ? 7 : 6, methodType.insertParameterTypes(0, (Class<?>[]) new Class[]{VarHandle.class}));
        } catch (NoSuchMethodException e) {
            throw new InternalError("No method for AccessMode " + accessMode, e);
        }
    }

    public static MethodHandle varHandleExactInvoker(VarHandle.AccessMode accessMode, MethodType methodType) {
        return methodHandleForVarHandleAccessor(accessMode, methodType, true);
    }

    public static MethodHandle varHandleInvoker(VarHandle.AccessMode accessMode, MethodType methodType) {
        return methodHandleForVarHandleAccessor(accessMode, methodType, false);
    }

    public static MethodHandle explicitCastArguments(MethodHandle methodHandle, MethodType methodType) {
        explicitCastArgumentsChecks(methodHandle, methodType);
        MethodType type = methodHandle.type();
        if (type == methodType) {
            return methodHandle;
        }
        if (!type.explicitCastEquivalentToAsType(methodType)) {
            return new Transformers.ExplicitCastArguments(methodHandle, methodType);
        }
        if (Transformers.Transformer.class.isAssignableFrom(methodHandle.getClass())) {
            return new Transformers.ExplicitCastArguments(methodHandle.asFixedArity(), methodType);
        }
        return methodHandle.asFixedArity().asType(methodType);
    }

    private static void explicitCastArgumentsChecks(MethodHandle methodHandle, MethodType methodType) {
        if (methodHandle.type().parameterCount() != methodType.parameterCount()) {
            throw new WrongMethodTypeException("cannot explicitly cast " + methodHandle + " to " + methodType);
        }
    }

    public static MethodHandle permuteArguments(MethodHandle methodHandle, MethodType methodType, int... iArr) {
        int[] iArr2 = (int[]) iArr.clone();
        permuteArgumentChecks(iArr2, methodType, methodHandle.type());
        return new Transformers.PermuteArguments(methodType, methodHandle, iArr2);
    }

    private static boolean permuteArgumentChecks(int[] iArr, MethodType methodType, MethodType methodType2) {
        if (methodType.returnType() == methodType2.returnType()) {
            if (iArr.length == methodType2.parameterCount()) {
                int parameterCount = methodType.parameterCount();
                boolean z = false;
                int i = 0;
                while (true) {
                    if (i >= iArr.length) {
                        break;
                    }
                    int i2 = iArr[i];
                    if (i2 < 0 || i2 >= parameterCount) {
                        z = true;
                    } else if (methodType.parameterType(i2) == methodType2.parameterType(i)) {
                        i++;
                    } else {
                        throw MethodHandleStatics.newIllegalArgumentException("parameter types do not match after reorder", methodType2, methodType);
                    }
                }
                z = true;
                if (!z) {
                    return true;
                }
            }
            throw MethodHandleStatics.newIllegalArgumentException("bad reorder array: " + Arrays.toString(iArr));
        }
        throw MethodHandleStatics.newIllegalArgumentException("return types do not match", methodType2, methodType);
    }

    public static MethodHandle constant(Class<?> cls, Object obj) {
        if (cls.isPrimitive()) {
            if (cls != Void.TYPE) {
                Wrapper forPrimitiveType = Wrapper.forPrimitiveType(cls);
                Object convert = forPrimitiveType.convert(obj, cls);
                if (forPrimitiveType.zero().equals(convert)) {
                    return zero(forPrimitiveType, cls);
                }
                return insertArguments(identity(cls), 0, convert);
            }
            throw MethodHandleStatics.newIllegalArgumentException("void type");
        } else if (obj == null) {
            return zero(Wrapper.OBJECT, cls);
        } else {
            return identity(cls).bindTo(obj);
        }
    }

    public static MethodHandle identity(Class<?> cls) {
        Objects.requireNonNull(cls);
        Wrapper forPrimitiveType = cls.isPrimitive() ? Wrapper.forPrimitiveType(cls) : Wrapper.OBJECT;
        int ordinal = forPrimitiveType.ordinal();
        MethodHandle[] methodHandleArr = IDENTITY_MHS;
        MethodHandle methodHandle = methodHandleArr[ordinal];
        if (methodHandle == null) {
            methodHandle = setCachedMethodHandle(methodHandleArr, ordinal, makeIdentity(forPrimitiveType.primitiveType()));
        }
        if (methodHandle.type().returnType() == cls) {
            return methodHandle;
        }
        return makeIdentity(cls);
    }

    public static MethodHandle zero(Class<?> cls) {
        Objects.requireNonNull(cls);
        return zero(cls.isPrimitive() ? Wrapper.forPrimitiveType(cls) : Wrapper.OBJECT, cls);
    }

    private static MethodHandle identityOrVoid(Class<?> cls) {
        return cls == Void.TYPE ? zero(cls) : identity(cls);
    }

    public static MethodHandle empty(MethodType methodType) {
        Objects.requireNonNull(methodType);
        return dropArguments(zero(methodType.returnType()), 0, methodType.parameterList());
    }

    private static MethodHandle makeIdentity(Class<?> cls) {
        if (!cls.isPrimitive()) {
            return new Transformers.ReferenceIdentity(cls);
        }
        try {
            return Lookup.PUBLIC_LOOKUP.findStatic(MethodHandles.class, WifiEnterpriseConfig.IDENTITY_KEY, MethodType.methodType(cls, cls));
        } catch (IllegalAccessException | NoSuchMethodException e) {
            throw new AssertionError(e);
        }
    }

    private static MethodHandle zero(Wrapper wrapper, Class<?> cls) {
        int ordinal = wrapper.ordinal();
        MethodHandle[] methodHandleArr = ZERO_MHS;
        MethodHandle methodHandle = methodHandleArr[ordinal];
        if (methodHandle == null) {
            methodHandle = setCachedMethodHandle(methodHandleArr, ordinal, makeZero(wrapper.primitiveType()));
        }
        if (methodHandle.type().returnType() == cls) {
            return methodHandle;
        }
        return makeZero(cls);
    }

    private static MethodHandle makeZero(Class<?> cls) {
        return new Transformers.ZeroValue(cls);
    }

    private static synchronized MethodHandle setCachedMethodHandle(MethodHandle[] methodHandleArr, int i, MethodHandle methodHandle) {
        synchronized (MethodHandles.class) {
            MethodHandle methodHandle2 = methodHandleArr[i];
            if (methodHandle2 != null) {
                return methodHandle2;
            }
            methodHandleArr[i] = methodHandle;
            return methodHandle;
        }
    }

    public static MethodHandle insertArguments(MethodHandle methodHandle, int i, Object... objArr) {
        int length = objArr.length;
        Class[] insertArgumentsChecks = insertArgumentsChecks(methodHandle, length, i);
        if (length == 0) {
            return methodHandle;
        }
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i + i2;
            Class cls = insertArgumentsChecks[i3];
            if (!cls.isPrimitive()) {
                insertArgumentsChecks[i3].cast(objArr[i2]);
            } else {
                objArr[i2] = Wrapper.forPrimitiveType((Class<?>) cls).convert(objArr[i2], cls);
            }
        }
        return new Transformers.InsertArguments(methodHandle, i, objArr);
    }

    private static Class<?>[] insertArgumentsChecks(MethodHandle methodHandle, int i, int i2) throws RuntimeException {
        MethodType type = methodHandle.type();
        int parameterCount = type.parameterCount() - i;
        if (parameterCount < 0) {
            throw MethodHandleStatics.newIllegalArgumentException("too many values to insert");
        } else if (i2 >= 0 && i2 <= parameterCount) {
            return type.ptypes();
        } else {
            throw MethodHandleStatics.newIllegalArgumentException("no argument type to append");
        }
    }

    public static MethodHandle dropArguments(MethodHandle methodHandle, int i, List<Class<?>> list) {
        return dropArguments0(methodHandle, i, copyTypes(list.toArray()));
    }

    private static List<Class<?>> copyTypes(Object[] objArr) {
        return Arrays.asList((Class[]) Arrays.copyOf(objArr, objArr.length, Class[].class));
    }

    private static MethodHandle dropArguments0(MethodHandle methodHandle, int i, List<Class<?>> list) {
        MethodType type = methodHandle.type();
        int dropArgumentChecks = dropArgumentChecks(type, i, list);
        return dropArgumentChecks == 0 ? methodHandle : new Transformers.DropArguments(type.insertParameterTypes(i, list), methodHandle, i, dropArgumentChecks);
    }

    private static int dropArgumentChecks(MethodType methodType, int i, List<Class<?>> list) {
        int size = list.size();
        MethodType.checkSlotCount(size);
        int parameterCount = methodType.parameterCount();
        int i2 = parameterCount + size;
        if (i >= 0 && i <= parameterCount) {
            return size;
        }
        throw MethodHandleStatics.newIllegalArgumentException("no argument type to remove" + Arrays.asList(methodType, Integer.valueOf(i), list, Integer.valueOf(i2), Integer.valueOf(parameterCount)));
    }

    public static MethodHandle dropArguments(MethodHandle methodHandle, int i, Class<?>... clsArr) {
        return dropArguments0(methodHandle, i, copyTypes(clsArr));
    }

    private static MethodHandle dropArgumentsToMatch(MethodHandle methodHandle, int i, List<Class<?>> list, int i2, boolean z) {
        List<Class<?>> list2;
        List<Class<?>> copyTypes = copyTypes(list.toArray());
        List<Class<?>> parameterList = methodHandle.type().parameterList();
        int size = parameterList.size();
        if (i != 0) {
            if (i < 0 || i > size) {
                throw MethodHandleStatics.newIllegalArgumentException("illegal skip", Integer.valueOf(i), methodHandle);
            }
            parameterList = parameterList.subList(i, size);
            size -= i;
        }
        int size2 = copyTypes.size();
        if (i2 == 0) {
            list2 = copyTypes;
        } else if (i2 < 0 || i2 > size2) {
            throw MethodHandleStatics.newIllegalArgumentException("illegal pos", Integer.valueOf(i2), copyTypes);
        } else {
            list2 = copyTypes.subList(i2, size2);
            size2 -= i2;
        }
        if (size <= size2 && parameterList.equals(list2.subList(0, size))) {
            List<Class<?>> subList = list2.subList(size, size2);
            if (size2 - size > 0) {
                methodHandle = dropArguments0(methodHandle, size + i, subList);
            }
            return i2 > 0 ? dropArguments0(methodHandle, i, copyTypes.subList(0, i2)) : methodHandle;
        } else if (z) {
            return null;
        } else {
            throw MethodHandleStatics.newIllegalArgumentException("argument lists do not match", parameterList, copyTypes);
        }
    }

    public static MethodHandle dropArgumentsToMatch(MethodHandle methodHandle, int i, List<Class<?>> list, int i2) {
        Objects.requireNonNull(methodHandle);
        Objects.requireNonNull(list);
        return dropArgumentsToMatch(methodHandle, i, list, i2, false);
    }

    public static MethodHandle filterArguments(MethodHandle methodHandle, int i, MethodHandle... methodHandleArr) {
        filterArgumentsCheckArity(methodHandle, i, methodHandleArr);
        for (int i2 = 0; i2 < methodHandleArr.length; i2++) {
            filterArgumentChecks(methodHandle, i2 + i, methodHandleArr[i2]);
        }
        return new Transformers.FilterArguments(methodHandle, i, methodHandleArr);
    }

    static MethodHandle filterArgument(MethodHandle methodHandle, int i, MethodHandle methodHandle2) {
        filterArgumentChecks(methodHandle, i, methodHandle2);
        return new Transformers.FilterArguments(methodHandle, i, methodHandle2);
    }

    private static void filterArgumentsCheckArity(MethodHandle methodHandle, int i, MethodHandle[] methodHandleArr) {
        if (i + methodHandleArr.length > methodHandle.type().parameterCount()) {
            throw MethodHandleStatics.newIllegalArgumentException("too many filters");
        }
    }

    private static void filterArgumentChecks(MethodHandle methodHandle, int i, MethodHandle methodHandle2) throws RuntimeException {
        MethodType type = methodHandle.type();
        MethodType type2 = methodHandle2.type();
        if (type2.parameterCount() != 1 || type2.returnType() != type.parameterType(i)) {
            throw MethodHandleStatics.newIllegalArgumentException("target and filter types do not match", type, type2);
        }
    }

    public static MethodHandle collectArguments(MethodHandle methodHandle, int i, MethodHandle methodHandle2) {
        return new Transformers.CollectArguments(methodHandle, methodHandle2, i, collectArgumentsChecks(methodHandle, i, methodHandle2));
    }

    private static MethodType collectArgumentsChecks(MethodHandle methodHandle, int i, MethodHandle methodHandle2) throws RuntimeException {
        MethodType type = methodHandle.type();
        MethodType type2 = methodHandle2.type();
        Class<?> returnType = type2.returnType();
        List<Class<?>> parameterList = type2.parameterList();
        if (returnType == Void.TYPE) {
            return type.insertParameterTypes(i, parameterList);
        }
        if (returnType == type.parameterType(i)) {
            return type.dropParameterTypes(i, i + 1).insertParameterTypes(i, parameterList);
        }
        throw MethodHandleStatics.newIllegalArgumentException("target and filter types do not match", type, type2);
    }

    public static MethodHandle filterReturnValue(MethodHandle methodHandle, MethodHandle methodHandle2) {
        filterReturnValueChecks(methodHandle.type(), methodHandle2.type());
        return new Transformers.FilterReturnValue(methodHandle, methodHandle2);
    }

    private static void filterReturnValueChecks(MethodType methodType, MethodType methodType2) throws RuntimeException {
        Class<?> returnType = methodType.returnType();
        int parameterCount = methodType2.parameterCount();
        if (parameterCount == 0) {
            if (returnType == Void.TYPE) {
                return;
            }
        } else if (returnType == methodType2.parameterType(0) && parameterCount == 1) {
            return;
        }
        throw MethodHandleStatics.newIllegalArgumentException("target and filter types do not match", methodType, methodType2);
    }

    public static MethodHandle foldArguments(MethodHandle methodHandle, MethodHandle methodHandle2) {
        return foldArguments(methodHandle, 0, methodHandle2);
    }

    public static MethodHandle foldArguments(MethodHandle methodHandle, int i, MethodHandle methodHandle2) {
        foldArgumentChecks(i, methodHandle.type(), methodHandle2.type());
        return new Transformers.FoldArguments(methodHandle, i, methodHandle2);
    }

    private static Class<?> foldArgumentChecks(int i, MethodType methodType, MethodType methodType2) {
        int parameterCount = methodType2.parameterCount();
        Class<?> returnType = methodType2.returnType();
        boolean z = true;
        boolean z2 = false;
        int i2 = returnType == Void.TYPE ? 0 : 1;
        int i3 = i + i2;
        if (methodType.parameterCount() < i3 + parameterCount) {
            z = false;
        }
        if (z) {
            int i4 = 0;
            while (true) {
                if (i4 >= parameterCount) {
                    break;
                } else if (methodType2.parameterType(i4) != methodType.parameterType(i4 + i3)) {
                    z = false;
                    break;
                } else {
                    i4++;
                }
            }
        }
        if (!z || i2 == 0 || methodType2.returnType() == methodType.parameterType(i)) {
            z2 = z;
        }
        if (z2) {
            return returnType;
        }
        throw misMatchedTypes("target and combiner types", methodType, methodType2);
    }

    public static MethodHandle guardWithTest(MethodHandle methodHandle, MethodHandle methodHandle2, MethodHandle methodHandle3) {
        MethodType type = methodHandle.type();
        MethodType type2 = methodHandle2.type();
        MethodType type3 = methodHandle3.type();
        if (!type2.equals((Object) type3)) {
            throw misMatchedTypes("target and fallback types", type2, type3);
        } else if (type.returnType() == Boolean.TYPE) {
            List<Class<?>> parameterList = type2.parameterList();
            List<Class<?>> parameterList2 = type.parameterList();
            if (!parameterList.equals(parameterList2)) {
                int size = parameterList2.size();
                int size2 = parameterList.size();
                if (size >= size2 || !parameterList.subList(0, size).equals(parameterList2)) {
                    throw misMatchedTypes("target and test types", type2, type);
                }
                methodHandle = dropArguments(methodHandle, size, parameterList.subList(size, size2));
                methodHandle.type();
            }
            return new Transformers.GuardWithTest(methodHandle, methodHandle2, methodHandle3);
        } else {
            throw MethodHandleStatics.newIllegalArgumentException("guard type is not a predicate " + type);
        }
    }

    static <T> RuntimeException misMatchedTypes(String str, T t, T t2) {
        return MethodHandleStatics.newIllegalArgumentException(str + " must match: " + t + " != " + t2);
    }

    public static MethodHandle catchException(MethodHandle methodHandle, Class<? extends Throwable> cls, MethodHandle methodHandle2) {
        MethodType type = methodHandle.type();
        MethodType type2 = methodHandle2.type();
        if (!Throwable.class.isAssignableFrom(cls)) {
            throw new ClassCastException(cls.getName());
        } else if (type2.parameterCount() < 1 || !type2.parameterType(0).isAssignableFrom(cls)) {
            throw MethodHandleStatics.newIllegalArgumentException("handler does not accept exception type " + cls);
        } else if (type2.returnType() == type.returnType()) {
            MethodHandle dropArgumentsToMatch = dropArgumentsToMatch(methodHandle2, 1, type.parameterList(), 0, true);
            if (dropArgumentsToMatch != null) {
                return new Transformers.CatchException(methodHandle, dropArgumentsToMatch, cls);
            }
            throw misMatchedTypes("target and handler types", type, type2);
        } else {
            throw misMatchedTypes("target and handler return types", type, type2);
        }
    }

    public static MethodHandle throwException(Class<?> cls, Class<? extends Throwable> cls2) {
        if (Throwable.class.isAssignableFrom(cls2)) {
            return new Transformers.AlwaysThrow(cls, cls2);
        }
        throw new ClassCastException(cls2.getName());
    }

    public static MethodHandle loop(MethodHandle[]... methodHandleArr) {
        loopChecks0(methodHandleArr);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        ArrayList arrayList3 = new ArrayList();
        ArrayList arrayList4 = new ArrayList();
        Stream.m1787of((T[]) methodHandleArr).filter(new MethodHandles$$ExternalSyntheticLambda24()).forEach(new MethodHandles$$ExternalSyntheticLambda5(arrayList, arrayList2, arrayList3, arrayList4));
        int size = arrayList.size();
        ArrayList arrayList5 = new ArrayList();
        for (int i = 0; i < size; i++) {
            MethodHandle methodHandle = (MethodHandle) arrayList.get(i);
            MethodHandle methodHandle2 = (MethodHandle) arrayList2.get(i);
            if (methodHandle == null && methodHandle2 == null) {
                arrayList5.add(Void.TYPE);
            } else if (methodHandle == null || methodHandle2 == null) {
                arrayList5.add((methodHandle == null ? methodHandle2.type() : methodHandle.type()).returnType());
            } else {
                loopChecks1a(i, methodHandle, methodHandle2);
                arrayList5.add(methodHandle.type().returnType());
            }
        }
        List list = (List) arrayList5.stream().filter(new MethodHandles$$ExternalSyntheticLambda7()).collect(Collectors.toList());
        List<Class<?>> buildCommonSuffix = buildCommonSuffix(arrayList, arrayList2, arrayList3, arrayList4, list.size());
        loopChecks1b(arrayList, buildCommonSuffix);
        Class cls = (Class) arrayList4.stream().filter(new MethodHandles$$ExternalSyntheticLambda10()).map(new MethodHandles$$ExternalSyntheticLambda17()).map(new MethodHandles$$ExternalSyntheticLambda8()).findFirst().orElse(Void.TYPE);
        loopChecks1cd(arrayList3, arrayList4, cls);
        ArrayList arrayList6 = new ArrayList(list);
        arrayList6.addAll(buildCommonSuffix);
        loopChecks2(arrayList2, arrayList3, arrayList4, arrayList6);
        for (int i2 = 0; i2 < size; i2++) {
            Class cls2 = (Class) arrayList5.get(i2);
            if (arrayList.get(i2) == null) {
                arrayList.set(i2, empty(MethodType.methodType((Class<?>) cls2, buildCommonSuffix)));
            }
            if (arrayList2.get(i2) == null) {
                arrayList2.set(i2, dropArgumentsToMatch(identityOrVoid(cls2), 0, arrayList6, i2));
            }
            if (arrayList3.get(i2) == null) {
                arrayList3.set(i2, dropArguments0(constant(Boolean.TYPE, true), 0, arrayList6));
            }
            if (arrayList4.get(i2) == null) {
                arrayList4.set(i2, empty(MethodType.methodType((Class<?>) cls2, (List<Class<?>>) arrayList6)));
            }
        }
        return new Transformers.Loop(cls, buildCommonSuffix, (MethodHandle[]) fixArities(fillParameterTypes(arrayList, buildCommonSuffix)).toArray((IntFunction<T[]>) new MethodHandles$$ExternalSyntheticLambda1()), (MethodHandle[]) fixArities(fillParameterTypes(arrayList2, arrayList6)).toArray((IntFunction<T[]>) new MethodHandles$$ExternalSyntheticLambda2()), (MethodHandle[]) fixArities(fillParameterTypes(arrayList3, arrayList6)).toArray((IntFunction<T[]>) new MethodHandles$$ExternalSyntheticLambda3()), (MethodHandle[]) fixArities(fillParameterTypes(arrayList4, arrayList6)).toArray((IntFunction<T[]>) new MethodHandles$$ExternalSyntheticLambda4()));
    }

    static /* synthetic */ void lambda$loop$1(List list, List list2, List list3, List list4, MethodHandle[] methodHandleArr) {
        list.add(methodHandleArr[0]);
        MethodHandle methodHandle = null;
        list2.add(methodHandleArr.length <= 1 ? null : methodHandleArr[1]);
        list3.add(methodHandleArr.length <= 2 ? null : methodHandleArr[2]);
        if (methodHandleArr.length > 3) {
            methodHandle = methodHandleArr[3];
        }
        list4.add(methodHandle);
    }

    static /* synthetic */ boolean lambda$loop$2(Class cls) {
        return cls != Void.TYPE;
    }

    static /* synthetic */ MethodHandle[] lambda$loop$5(int i) {
        return new MethodHandle[i];
    }

    static /* synthetic */ MethodHandle[] lambda$loop$6(int i) {
        return new MethodHandle[i];
    }

    static /* synthetic */ MethodHandle[] lambda$loop$7(int i) {
        return new MethodHandle[i];
    }

    static /* synthetic */ MethodHandle[] lambda$loop$8(int i) {
        return new MethodHandle[i];
    }

    private static void loopChecks0(MethodHandle[][] methodHandleArr) {
        if (methodHandleArr == null || methodHandleArr.length == 0) {
            throw MethodHandleStatics.newIllegalArgumentException("null or no clauses passed");
        } else if (Stream.m1787of((T[]) methodHandleArr).anyMatch(new MethodHandles$$ExternalSyntheticLambda12())) {
            throw MethodHandleStatics.newIllegalArgumentException("null clauses are not allowed");
        } else if (Stream.m1787of((T[]) methodHandleArr).anyMatch(new MethodHandles$$ExternalSyntheticLambda13())) {
            throw MethodHandleStatics.newIllegalArgumentException("All loop clauses must be represented as MethodHandle arrays with at most 4 elements.");
        }
    }

    static /* synthetic */ boolean lambda$loopChecks0$9(MethodHandle[] methodHandleArr) {
        return methodHandleArr.length > 4;
    }

    private static void loopChecks1a(int i, MethodHandle methodHandle, MethodHandle methodHandle2) {
        if (methodHandle.type().returnType() != methodHandle2.type().returnType()) {
            throw misMatchedTypes("clause " + i + ": init and step return types", methodHandle.type().returnType(), methodHandle2.type().returnType());
        }
    }

    private static List<Class<?>> longestParameterList(Stream<MethodHandle> stream, int i) {
        List<Class<?>> of = List.m1728of();
        List list = (List) stream.filter(new MethodHandles$$ExternalSyntheticLambda10()).map(new MethodHandles$$ExternalSyntheticLambda17()).filter(new MethodHandles$$ExternalSyntheticLambda19(i)).map(new MethodHandles$$ExternalSyntheticLambda20()).reduce(new MethodHandles$$ExternalSyntheticLambda21()).orElse(of);
        return list.size() == 0 ? of : list.subList(i, list.size());
    }

    static /* synthetic */ boolean lambda$longestParameterList$10(int i, MethodType methodType) {
        return methodType.parameterCount() > i;
    }

    static /* synthetic */ List lambda$longestParameterList$11(List list, List list2) {
        return list.size() >= list2.size() ? list : list2;
    }

    private static List<Class<?>> longestParameterList(List<List<Class<?>>> list) {
        return list.stream().reduce(new MethodHandles$$ExternalSyntheticLambda23()).orElse(List.m1728of());
    }

    static /* synthetic */ List lambda$longestParameterList$12(List list, List list2) {
        return list.size() >= list2.size() ? list : list2;
    }

    private static List<Class<?>> buildCommonSuffix(List<MethodHandle> list, List<MethodHandle> list2, List<MethodHandle> list3, List<MethodHandle> list4, int i) {
        return longestParameterList(Arrays.asList(longestParameterList(Stream.m1787of((T[]) new List[]{list2, list3, list4}).flatMap(new WifiPickerTracker$$ExternalSyntheticLambda13()), i), longestParameterList(list.stream(), 0)));
    }

    private static void loopChecks1b(List<MethodHandle> list, List<Class<?>> list2) {
        if (list.stream().filter(new MethodHandles$$ExternalSyntheticLambda10()).map(new MethodHandles$$ExternalSyntheticLambda17()).anyMatch(new MethodHandles$$ExternalSyntheticLambda11(list2))) {
            throw MethodHandleStatics.newIllegalArgumentException("found non-effectively identical init parameter type lists: " + list + " (common suffix: " + list2 + NavigationBarInflaterView.KEY_CODE_END);
        }
    }

    static /* synthetic */ boolean lambda$loopChecks1b$13(List list, MethodType methodType) {
        return !methodType.effectivelyIdenticalParameters(0, list);
    }

    private static void loopChecks1cd(List<MethodHandle> list, List<MethodHandle> list2, Class<?> cls) {
        if (list2.stream().filter(new MethodHandles$$ExternalSyntheticLambda10()).map(new MethodHandles$$ExternalSyntheticLambda17()).map(new MethodHandles$$ExternalSyntheticLambda8()).anyMatch(new MethodHandles$$ExternalSyntheticLambda15(cls))) {
            throw MethodHandleStatics.newIllegalArgumentException("found non-identical finalizer return types: " + list2 + " (return type: " + cls + NavigationBarInflaterView.KEY_CODE_END);
        } else if (!list.stream().filter(new MethodHandles$$ExternalSyntheticLambda10()).findFirst().isPresent()) {
            throw MethodHandleStatics.newIllegalArgumentException("no predicate found", list);
        } else if (list.stream().filter(new MethodHandles$$ExternalSyntheticLambda10()).map(new MethodHandles$$ExternalSyntheticLambda17()).map(new MethodHandles$$ExternalSyntheticLambda8()).anyMatch(new MethodHandles$$ExternalSyntheticLambda16())) {
            throw MethodHandleStatics.newIllegalArgumentException("predicates must have boolean return type", list);
        }
    }

    static /* synthetic */ boolean lambda$loopChecks1cd$15(Class cls) {
        return cls != Boolean.TYPE;
    }

    private static void loopChecks2(List<MethodHandle> list, List<MethodHandle> list2, List<MethodHandle> list3, List<Class<?>> list4) {
        if (Stream.m1787of((T[]) new List[]{list, list2, list3}).flatMap(new WifiPickerTracker$$ExternalSyntheticLambda13()).filter(new MethodHandles$$ExternalSyntheticLambda10()).map(new MethodHandles$$ExternalSyntheticLambda17()).anyMatch(new MethodHandles$$ExternalSyntheticLambda18(list4))) {
            throw MethodHandleStatics.newIllegalArgumentException("found non-effectively identical parameter type lists:\nstep: " + list + "\npred: " + list2 + "\nfini: " + list3 + " (common parameter sequence: " + list4 + NavigationBarInflaterView.KEY_CODE_END);
        }
    }

    static /* synthetic */ boolean lambda$loopChecks2$16(List list, MethodType methodType) {
        return !methodType.effectivelyIdenticalParameters(0, list);
    }

    private static List<MethodHandle> fillParameterTypes(List<MethodHandle> list, List<Class<?>> list2) {
        return (List) list.stream().map(new MethodHandles$$ExternalSyntheticLambda22(list2)).collect(Collectors.toList());
    }

    static /* synthetic */ MethodHandle lambda$fillParameterTypes$17(List list, MethodHandle methodHandle) {
        int parameterCount = methodHandle.type().parameterCount();
        int size = list.size();
        return parameterCount < size ? dropArguments0(methodHandle, parameterCount, list.subList(parameterCount, size)) : methodHandle;
    }

    private static List<MethodHandle> fixArities(List<MethodHandle> list) {
        return (List) list.stream().map(new MethodHandles$$ExternalSyntheticLambda14()).collect(Collectors.toList());
    }

    public static MethodHandle whileLoop(MethodHandle methodHandle, MethodHandle methodHandle2, MethodHandle methodHandle3) {
        whileLoopChecks(methodHandle, methodHandle2, methodHandle3);
        return loop(new MethodHandle[]{null, null, methodHandle2, identityOrVoid(methodHandle3.type().returnType())}, new MethodHandle[]{methodHandle, methodHandle3});
    }

    public static MethodHandle doWhileLoop(MethodHandle methodHandle, MethodHandle methodHandle2, MethodHandle methodHandle3) {
        whileLoopChecks(methodHandle, methodHandle3, methodHandle2);
        return loop(new MethodHandle[]{methodHandle, methodHandle2, methodHandle3, identityOrVoid(methodHandle2.type().returnType())});
    }

    private static void whileLoopChecks(MethodHandle methodHandle, MethodHandle methodHandle2, MethodHandle methodHandle3) {
        List<Class<?>> list;
        Objects.requireNonNull(methodHandle2);
        Objects.requireNonNull(methodHandle3);
        MethodType type = methodHandle3.type();
        Class<?> returnType = type.returnType();
        List<Class<?>> parameterList = type.parameterList();
        if (returnType == Void.TYPE) {
            list = parameterList;
        } else if (parameterList.size() == 0 || parameterList.get(0) != returnType) {
            throw misMatchedTypes("body function", type, type.insertParameterTypes(0, (Class<?>[]) new Class[]{returnType}));
        } else {
            list = parameterList.subList(1, parameterList.size());
        }
        MethodType type2 = methodHandle2.type();
        if (type2.returnType() != Boolean.TYPE || !type2.effectivelyIdenticalParameters(0, parameterList)) {
            throw misMatchedTypes("loop predicate", type2, MethodType.methodType((Class<?>) Boolean.TYPE, parameterList));
        } else if (methodHandle != null) {
            MethodType type3 = methodHandle.type();
            if (type3.returnType() != returnType || !type3.effectivelyIdenticalParameters(0, list)) {
                throw misMatchedTypes("loop initializer", type3, MethodType.methodType(returnType, list));
            }
        }
    }

    public static MethodHandle countedLoop(MethodHandle methodHandle, MethodHandle methodHandle2, MethodHandle methodHandle3) {
        return countedLoop(empty(methodHandle.type()), methodHandle, methodHandle2, methodHandle3);
    }

    public static MethodHandle countedLoop(MethodHandle methodHandle, MethodHandle methodHandle2, MethodHandle methodHandle3, MethodHandle methodHandle4) {
        MethodHandle methodHandle5;
        countedLoopChecks(methodHandle, methodHandle2, methodHandle3, methodHandle4);
        Class<?> returnType = methodHandle.type().returnType();
        methodHandle2.type().returnType();
        Class<?> returnType2 = methodHandle4.type().returnType();
        MethodHandle constantHandle = getConstantHandle(7);
        MethodHandle constantHandle2 = getConstantHandle(6);
        if (returnType2 != Void.TYPE) {
            constantHandle = dropArguments(constantHandle, 1, (Class<?>[]) new Class[]{returnType2});
            constantHandle2 = dropArguments(constantHandle2, 1, (Class<?>[]) new Class[]{returnType2});
            methodHandle5 = dropArguments(identity(returnType2), 0, (Class<?>[]) new Class[]{returnType});
        } else {
            methodHandle5 = null;
        }
        return loop(new MethodHandle[]{methodHandle2, null, constantHandle2, methodHandle5}, new MethodHandle[]{methodHandle3, dropArguments(methodHandle4, 0, (Class<?>[]) new Class[]{returnType})}, new MethodHandle[]{methodHandle, constantHandle});
    }

    private static void countedLoopChecks(MethodHandle methodHandle, MethodHandle methodHandle2, MethodHandle methodHandle3, MethodHandle methodHandle4) {
        Objects.requireNonNull(methodHandle);
        Objects.requireNonNull(methodHandle2);
        Objects.requireNonNull(methodHandle4);
        Class<?> returnType = methodHandle.type().returnType();
        if (returnType != Integer.TYPE) {
            throw misMatchedTypes("start function", methodHandle.type(), methodHandle.type().changeReturnType(Integer.TYPE));
        } else if (methodHandle2.type().returnType() == returnType) {
            MethodType type = methodHandle4.type();
            Class<?> returnType2 = type.returnType();
            List<Class<?>> parameterList = type.parameterList();
            int i = returnType2 == Void.TYPE ? 0 : 1;
            if (i != 0 && (parameterList.size() == 0 || parameterList.get(0) != returnType2)) {
                throw misMatchedTypes("body function", type, type.insertParameterTypes(0, (Class<?>[]) new Class[]{returnType2}));
            } else if (parameterList.size() <= i || parameterList.get(i) != returnType) {
                throw misMatchedTypes("body function", type, type.insertParameterTypes(i, (Class<?>[]) new Class[]{returnType}));
            } else {
                int i2 = i + 1;
                List<Class<?>> subList = parameterList.subList(i2, parameterList.size());
                if (subList.isEmpty()) {
                    subList = methodHandle2.type().parameterList();
                    type.insertParameterTypes(i2, subList).parameterList();
                }
                MethodType methodType = MethodType.methodType(returnType, subList);
                if (!methodHandle.type().effectivelyIdenticalParameters(0, subList)) {
                    throw misMatchedTypes("start parameter types", methodHandle.type(), methodType);
                } else if (methodHandle2.type() != methodHandle.type() && !methodHandle2.type().effectivelyIdenticalParameters(0, subList)) {
                    throw misMatchedTypes("end parameter types", methodHandle2.type(), methodType);
                } else if (methodHandle3 != null) {
                    MethodType type2 = methodHandle3.type();
                    if (type2.returnType() != returnType2 || !type2.effectivelyIdenticalParameters(0, subList)) {
                        throw misMatchedTypes("loop initializer", type2, MethodType.methodType(returnType2, subList));
                    }
                }
            }
        } else {
            throw misMatchedTypes("end function", methodHandle2.type(), methodHandle2.type().changeReturnType(returnType));
        }
    }

    public static MethodHandle iteratedLoop(MethodHandle methodHandle, MethodHandle methodHandle2, MethodHandle methodHandle3) {
        MethodType methodType;
        MethodHandle methodHandle4;
        Class<?> iteratedLoopChecks = iteratedLoopChecks(methodHandle, methodHandle2, methodHandle3);
        Class<?> returnType = methodHandle3.type().returnType();
        MethodHandle constantHandle = getConstantHandle(9);
        MethodHandle constantHandle2 = getConstantHandle(10);
        if (methodHandle == null) {
            methodHandle = getConstantHandle(8);
            methodType = methodHandle.type().changeParameterType(0, iteratedLoopChecks);
        } else {
            methodType = methodHandle.type().changeReturnType(Iterator.class);
        }
        MethodType changeReturnType = constantHandle2.type().changeReturnType(methodHandle3.type().parameterType(returnType == Void.TYPE ? 0 : 1));
        try {
            MethodHandle asType = methodHandle.asType(methodType);
            MethodHandle asType2 = constantHandle2.asType(changeReturnType);
            if (returnType != Void.TYPE) {
                methodHandle4 = dropArguments(identity(returnType), 0, (Class<?>[]) new Class[]{Iterator.class});
                methodHandle3 = swapArguments(methodHandle3, 0, 1);
            } else {
                methodHandle4 = null;
            }
            return loop(new MethodHandle[]{asType, null, constantHandle, methodHandle4}, new MethodHandle[]{methodHandle2, filterArgument(methodHandle3, 0, asType2)});
        } catch (WrongMethodTypeException e) {
            throw new IllegalArgumentException((Throwable) e);
        }
    }

    private static Class<?> iteratedLoopChecks(MethodHandle methodHandle, MethodHandle methodHandle2, MethodHandle methodHandle3) {
        Class<Iterable> cls;
        Objects.requireNonNull(methodHandle3);
        MethodType type = methodHandle3.type();
        Class<?> returnType = type.returnType();
        List<Class<?>> parameterList = type.parameterList();
        int i = returnType == Void.TYPE ? 0 : 1;
        if (i != 0 && (parameterList.size() == 0 || parameterList.get(0) != returnType)) {
            throw misMatchedTypes("body function", type, type.insertParameterTypes(0, (Class<?>[]) new Class[]{returnType}));
        } else if (parameterList.size() > i) {
            List<Class<?>> subList = parameterList.subList(i + 1, parameterList.size());
            if (methodHandle != null) {
                if (subList.isEmpty()) {
                    subList = methodHandle.type().parameterList();
                }
                MethodType type2 = methodHandle.type();
                if (!Iterator.class.isAssignableFrom(type2.returnType())) {
                    throw MethodHandleStatics.newIllegalArgumentException("iteratedLoop first argument must have Iterator return type");
                } else if (type2.effectivelyIdenticalParameters(0, subList)) {
                    cls = null;
                } else {
                    throw misMatchedTypes("iterator parameters", type2, MethodType.methodType(type2.returnType(), subList));
                }
            } else if (subList.isEmpty()) {
                subList = Arrays.asList(Iterable.class);
                cls = Iterable.class;
            } else {
                cls = subList.get(0);
                if (!Iterable.class.isAssignableFrom(cls)) {
                    throw MethodHandleStatics.newIllegalArgumentException("inferred first loop argument must inherit from Iterable: " + cls);
                }
            }
            if (methodHandle2 != null) {
                MethodType type3 = methodHandle2.type();
                if (type3.returnType() != returnType || !type3.effectivelyIdenticalParameters(0, subList)) {
                    throw misMatchedTypes("loop initializer", type3, MethodType.methodType(returnType, subList));
                }
            }
            return cls;
        } else {
            throw misMatchedTypes("body function", type, type.insertParameterTypes(i, (Class<?>[]) new Class[]{Object.class}));
        }
    }

    static MethodHandle swapArguments(MethodHandle methodHandle, int i, int i2) {
        int parameterCount = methodHandle.type().parameterCount();
        int[] iArr = new int[parameterCount];
        for (int i3 = 0; i3 < parameterCount; i3++) {
            iArr[i3] = i3;
        }
        iArr[i] = i2;
        iArr[i2] = i;
        Class[] parameterArray = methodHandle.type().parameterArray();
        Class cls = parameterArray[i];
        parameterArray[i] = parameterArray[i2];
        parameterArray[i2] = cls;
        return permuteArguments(methodHandle, MethodType.methodType(methodHandle.type().returnType(), (Class<?>[]) parameterArray), iArr);
    }

    public static MethodHandle tryFinally(MethodHandle methodHandle, MethodHandle methodHandle2) {
        List<Class<?>> parameterList = methodHandle.type().parameterList();
        Class<?> returnType = methodHandle.type().returnType();
        tryFinallyChecks(methodHandle, methodHandle2);
        MethodHandle dropArgumentsToMatch = dropArgumentsToMatch(methodHandle2, returnType == Void.TYPE ? 1 : 2, parameterList, 0);
        return new Transformers.TryFinally(methodHandle.asFixedArity(), dropArgumentsToMatch.asType(dropArgumentsToMatch.type().changeParameterType(0, Throwable.class)).asFixedArity());
    }

    private static void tryFinallyChecks(MethodHandle methodHandle, MethodHandle methodHandle2) {
        Class<?> returnType = methodHandle.type().returnType();
        if (returnType == methodHandle2.type().returnType()) {
            MethodType type = methodHandle2.type();
            if (Throwable.class.isAssignableFrom(type.parameterType(0))) {
                int i = 1;
                if (returnType == Void.TYPE || type.parameterType(1) == returnType) {
                    if (returnType != Void.TYPE) {
                        i = 2;
                    }
                    if (!type.effectivelyIdenticalParameters(i, methodHandle.type().parameterList())) {
                        throw misMatchedTypes("cleanup parameters after (Throwable,result) and target parameter list prefix", methodHandle2.type(), methodHandle.type());
                    }
                    return;
                }
                throw misMatchedTypes("cleanup second argument and target return type", methodHandle2.type(), returnType);
            }
            throw misMatchedTypes("cleanup first argument and Throwable", methodHandle2.type(), Throwable.class);
        }
        throw misMatchedTypes("target and return types", methodHandle2.type().returnType(), returnType);
    }

    public static Iterator<?> initIterator(Iterable<?> iterable) {
        return iterable.iterator();
    }

    public static boolean iteratePredicate(Iterator<?> it) {
        return it.hasNext();
    }

    public static Object iterateNext(Iterator<?> it) {
        return it.next();
    }

    static MethodHandle getConstantHandle(int i) {
        MethodHandle methodHandle = HANDLES[i];
        if (methodHandle != null) {
            return methodHandle;
        }
        return setCachedHandle(i, makeConstantHandle(i));
    }

    private static synchronized MethodHandle setCachedHandle(int i, MethodHandle methodHandle) {
        synchronized (MethodHandles.class) {
            MethodHandle[] methodHandleArr = HANDLES;
            MethodHandle methodHandle2 = methodHandleArr[i];
            if (methodHandle2 != null) {
                return methodHandle2;
            }
            methodHandleArr[i] = methodHandle;
            return methodHandle;
        }
    }

    private static MethodHandle makeConstantHandle(int i) {
        try {
            Lookup lookup = Lookup.IMPL_LOOKUP;
            Class<MethodHandles> cls = MethodHandles.class;
            switch (i) {
                case 6:
                    return lookup.findStatic(cls, "countedLoopPredicate", MethodType.methodType(Boolean.TYPE, Integer.TYPE, Integer.TYPE));
                case 7:
                    return lookup.findStatic(cls, "countedLoopStep", MethodType.methodType(Integer.TYPE, Integer.TYPE, Integer.TYPE));
                case 8:
                    return lookup.findStatic(cls, "initIterator", MethodType.methodType((Class<?>) Iterator.class, (Class<?>) Iterable.class));
                case 9:
                    return lookup.findStatic(cls, "iteratePredicate", MethodType.methodType((Class<?>) Boolean.TYPE, (Class<?>) Iterator.class));
                case 10:
                    return lookup.findStatic(cls, "iterateNext", MethodType.methodType((Class<?>) Object.class, (Class<?>) Iterator.class));
                default:
                    throw MethodHandleStatics.newInternalError("Unknown function index: " + i);
            }
        } catch (ReflectiveOperationException e) {
            throw MethodHandleStatics.newInternalError((Throwable) e);
        }
        throw MethodHandleStatics.newInternalError((Throwable) e);
    }
}
