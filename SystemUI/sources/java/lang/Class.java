package java.lang;

import android.net.wifi.WifiEnterpriseConfig;
import com.android.launcher3.icons.cache.BaseIconCache;
import dalvik.system.ClassExt;
import java.lang.annotation.Annotation;
import java.lang.annotation.Inherited;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.p026io.InputStream;
import java.p026io.Serializable;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import kotlin.text.Typography;
import libcore.reflect.GenericSignatureParser;
import libcore.reflect.Types;
import libcore.util.BasicLruCache;
import libcore.util.CollectionUtils;
import libcore.util.EmptyArray;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

public final class Class<T> implements Serializable, GenericDeclaration, Type, AnnotatedElement {
    private static final int ANNOTATION = 8192;
    private static final int ENUM = 16384;
    private static final int FINALIZABLE = Integer.MIN_VALUE;
    private static final int SYNTHETIC = 4096;
    private static final long serialVersionUID = 3206093459760846163L;
    private transient int accessFlags;
    private transient int classFlags;
    private transient ClassLoader classLoader;
    private transient int classSize;
    private transient int clinitThreadId;
    private transient Class<?> componentType;
    private transient short copiedMethodsOffset;
    private transient Object dexCache;
    private transient int dexClassDefIndex;
    private volatile transient int dexTypeIndex;
    private transient ClassExt extData;
    private transient long iFields;
    private transient Object[] ifTable;
    private transient long methods;
    private transient String name;
    private transient int numReferenceInstanceFields;
    private transient int numReferenceStaticFields;
    private transient int objectSize;
    private transient int objectSizeAllocFastPath;
    private transient int primitiveType;
    private transient int referenceInstanceOffsets;
    private transient long sFields;
    private transient int status;
    private transient Class<? super T> superClass;
    private transient short virtualMethodsOffset;
    private transient Object vtable;

    static native Class<?> classForName(String str, boolean z, ClassLoader classLoader2) throws ClassNotFoundException;

    private native Constructor<T> getDeclaredConstructorInternal(Class<?>[] clsArr);

    private native Constructor<?>[] getDeclaredConstructorsInternal(boolean z);

    private native Method getDeclaredMethodInternal(String str, Class<?>[] clsArr);

    private native Constructor<?> getEnclosingConstructorNative();

    private native Method getEnclosingMethodNative();

    private native int getInnerClassFlags(int i);

    private native String getInnerClassName();

    private native Class<?>[] getInterfacesInternal();

    private native String getNameNative();

    static native Class<?> getPrimitiveClass(String str);

    private native Field[] getPublicDeclaredFields();

    private native Field getPublicFieldRecursive(String str);

    private native String[] getSignatureAnnotation();

    private native boolean isDeclaredAnnotationPresent(Class<? extends Annotation> cls);

    public boolean desiredAssertionStatus() {
        return false;
    }

    public native <A extends Annotation> A getDeclaredAnnotation(Class<A> cls);

    public native Annotation[] getDeclaredAnnotations();

    public native Class<?>[] getDeclaredClasses();

    public native Field getDeclaredField(String str) throws NoSuchFieldException;

    public native Field[] getDeclaredFields();

    public native Field[] getDeclaredFieldsUnchecked(boolean z);

    public native Method[] getDeclaredMethodsUnchecked(boolean z);

    public native Class<?> getDeclaringClass();

    public native Class<?> getEnclosingClass();

    public ProtectionDomain getProtectionDomain() {
        return null;
    }

    public Object[] getSigners() {
        return null;
    }

    public native boolean isAnonymousClass();

    public native T newInstance() throws InstantiationException, IllegalAccessException;

    private Class() {
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(isInterface() ? "interface " : isPrimitive() ? "" : "class ");
        sb.append(getName());
        return sb.toString();
    }

    public String toGenericString() {
        if (isPrimitive()) {
            return toString();
        }
        StringBuilder sb = new StringBuilder();
        int modifiers = getModifiers() & Modifier.classModifiers();
        if (modifiers != 0) {
            sb.append(Modifier.toString(modifiers));
            sb.append(' ');
        }
        if (isAnnotation()) {
            sb.append('@');
        }
        if (isInterface()) {
            sb.append("interface");
        } else if (isEnum()) {
            sb.append("enum");
        } else {
            sb.append("class");
        }
        sb.append(' ');
        sb.append(getName());
        TypeVariable[] typeParameters = getTypeParameters();
        if (typeParameters.length > 0) {
            sb.append((char) Typography.less);
            int length = typeParameters.length;
            boolean z = true;
            int i = 0;
            while (i < length) {
                TypeVariable typeVariable = typeParameters[i];
                if (!z) {
                    sb.append(',');
                }
                sb.append(typeVariable.getTypeName());
                i++;
                z = false;
            }
            sb.append((char) Typography.greater);
        }
        return sb.toString();
    }

    @CallerSensitive
    public static Class<?> forName(String str) throws ClassNotFoundException {
        return forName(str, true, ClassLoader.getClassLoader(Reflection.getCallerClass()));
    }

    @CallerSensitive
    public static Class<?> forName(String str, boolean z, ClassLoader classLoader2) throws ClassNotFoundException {
        if (classLoader2 == null) {
            classLoader2 = BootClassLoader.getInstance();
        }
        try {
            return classForName(str, z, classLoader2);
        } catch (ClassNotFoundException e) {
            Throwable cause = e.getCause();
            if (cause instanceof LinkageError) {
                throw ((LinkageError) cause);
            }
            throw e;
        }
    }

    public boolean isInstance(Object obj) {
        if (obj == null) {
            return false;
        }
        return isAssignableFrom(obj.getClass());
    }

    public boolean isAssignableFrom(Class<?> cls) {
        if (this == cls) {
            return true;
        }
        if (this == Object.class) {
            return !cls.isPrimitive();
        }
        if (isArray()) {
            if (!cls.isArray() || !this.componentType.isAssignableFrom(cls.componentType)) {
                return false;
            }
            return true;
        } else if (isInterface()) {
            Object[] objArr = cls.ifTable;
            if (objArr != null) {
                for (int i = 0; i < objArr.length; i += 2) {
                    if (objArr[i] == this) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            boolean isInterface = cls.isInterface();
            Class cls2 = cls;
            if (!isInterface) {
                do {
                    cls2 = cls2.superClass;
                    if (cls2 != null) {
                    }
                } while (cls2 != this);
                return true;
            }
            return false;
        }
    }

    public boolean isInterface() {
        return (this.accessFlags & 512) != 0;
    }

    public boolean isArray() {
        return getComponentType() != null;
    }

    public boolean isPrimitive() {
        return (this.primitiveType & 65535) != 0;
    }

    public boolean isFinalizable() {
        return (getModifiers() & Integer.MIN_VALUE) != 0;
    }

    public boolean isAnnotation() {
        return (getModifiers() & 8192) != 0;
    }

    public boolean isSynthetic() {
        return (getModifiers() & 4096) != 0;
    }

    public String getName() {
        String str = this.name;
        if (str != null) {
            return str;
        }
        String nameNative = getNameNative();
        this.name = nameNative;
        return nameNative;
    }

    public ClassLoader getClassLoader() {
        if (isPrimitive()) {
            return null;
        }
        ClassLoader classLoader2 = this.classLoader;
        return classLoader2 == null ? BootClassLoader.getInstance() : classLoader2;
    }

    public synchronized TypeVariable<Class<T>>[] getTypeParameters() {
        String signatureAttribute = getSignatureAttribute();
        if (signatureAttribute == null) {
            return EmptyArray.TYPE_VARIABLE;
        }
        GenericSignatureParser genericSignatureParser = new GenericSignatureParser(getClassLoader());
        genericSignatureParser.parseForClass(this, signatureAttribute);
        return genericSignatureParser.formalTypeParameters;
    }

    public Class<? super T> getSuperclass() {
        if (isInterface()) {
            return null;
        }
        return this.superClass;
    }

    public Type getGenericSuperclass() {
        Type superclass = getSuperclass();
        if (superclass == null) {
            return null;
        }
        String signatureAttribute = getSignatureAttribute();
        if (signatureAttribute != null) {
            GenericSignatureParser genericSignatureParser = new GenericSignatureParser(getClassLoader());
            genericSignatureParser.parseForClass(this, signatureAttribute);
            superclass = genericSignatureParser.superclassType;
        }
        return Types.getType(superclass);
    }

    public Package getPackage() {
        String packageName;
        ClassLoader classLoader2 = getClassLoader();
        if (classLoader2 == null || (packageName = getPackageName()) == null) {
            return null;
        }
        return classLoader2.getPackage(packageName);
    }

    public String getPackageName() {
        while (this.isArray()) {
            this = this.getComponentType();
        }
        if (this.isPrimitive()) {
            return "java.lang";
        }
        String name2 = this.getName();
        int lastIndexOf = name2.lastIndexOf(46);
        return lastIndexOf != -1 ? name2.substring(0, lastIndexOf).intern() : "";
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r2v4, resolved type: java.lang.Class<?>[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.Class<?>[] getInterfaces() {
        /*
            r2 = this;
            boolean r0 = r2.isArray()
            if (r0 == 0) goto L_0x0014
            r2 = 2
            java.lang.Class[] r2 = new java.lang.Class[r2]
            r0 = 0
            java.lang.Class<java.lang.Cloneable> r1 = java.lang.Cloneable.class
            r2[r0] = r1
            r0 = 1
            java.lang.Class<java.io.Serializable> r1 = java.p026io.Serializable.class
            r2[r0] = r1
            return r2
        L_0x0014:
            java.lang.Class[] r2 = r2.getInterfacesInternal()
            if (r2 != 0) goto L_0x001c
            java.lang.Class<?>[] r2 = libcore.util.EmptyArray.CLASS
        L_0x001c:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: java.lang.Class.getInterfaces():java.lang.Class[]");
    }

    public Type[] getGenericInterfaces() {
        Type[] typeArr;
        synchronized (Caches.genericInterfaces) {
            typeArr = (Type[]) Caches.genericInterfaces.get(this);
            if (typeArr == null) {
                String signatureAttribute = getSignatureAttribute();
                if (signatureAttribute == null) {
                    typeArr = getInterfaces();
                } else {
                    GenericSignatureParser genericSignatureParser = new GenericSignatureParser(getClassLoader());
                    genericSignatureParser.parseForClass(this, signatureAttribute);
                    typeArr = Types.getTypeArray(genericSignatureParser.interfaceTypes, false);
                }
                Caches.genericInterfaces.put(this, typeArr);
            }
        }
        return typeArr.length == 0 ? typeArr : (Type[]) typeArr.clone();
    }

    public Class<?> getComponentType() {
        return this.componentType;
    }

    public int getModifiers() {
        if (!isArray()) {
            return getInnerClassFlags(this.accessFlags & 65535) & 65535;
        }
        int modifiers = getComponentType().getModifiers();
        if ((modifiers & 512) != 0) {
            modifiers &= -521;
        }
        return modifiers | 1040;
    }

    public Method getEnclosingMethod() {
        if (classNameImpliesTopLevel()) {
            return null;
        }
        return getEnclosingMethodNative();
    }

    public Constructor<?> getEnclosingConstructor() {
        if (classNameImpliesTopLevel()) {
            return null;
        }
        return getEnclosingConstructorNative();
    }

    private boolean classNameImpliesTopLevel() {
        return !getName().contains("$");
    }

    public String getSimpleName() {
        if (isArray()) {
            return getComponentType().getSimpleName() + "[]";
        } else if (isAnonymousClass()) {
            return "";
        } else {
            if (isMemberClass() || isLocalClass()) {
                return getInnerClassName();
            }
            String name2 = getName();
            return name2.lastIndexOf(BaseIconCache.EMPTY_CLASS_NAME) > 0 ? name2.substring(name2.lastIndexOf(BaseIconCache.EMPTY_CLASS_NAME) + 1) : name2;
        }
    }

    public String getTypeName() {
        if (isArray()) {
            Class cls = this;
            int i = 0;
            while (cls.isArray()) {
                try {
                    i++;
                    cls = cls.getComponentType();
                } catch (Throwable unused) {
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append(cls.getName());
            for (int i2 = 0; i2 < i; i2++) {
                sb.append("[]");
            }
            return sb.toString();
        }
        return getName();
    }

    public String getCanonicalName() {
        if (isArray()) {
            String canonicalName = getComponentType().getCanonicalName();
            if (canonicalName == null) {
                return null;
            }
            return canonicalName + "[]";
        } else if (isLocalOrAnonymousClass()) {
            return null;
        } else {
            Class<?> enclosingClass = getEnclosingClass();
            if (enclosingClass == null) {
                return getName();
            }
            String canonicalName2 = enclosingClass.getCanonicalName();
            if (canonicalName2 == null) {
                return null;
            }
            return canonicalName2 + BaseIconCache.EMPTY_CLASS_NAME + getSimpleName();
        }
    }

    public boolean isLocalClass() {
        return !(getEnclosingMethod() == null && getEnclosingConstructor() == null) && !isAnonymousClass();
    }

    public boolean isMemberClass() {
        return getDeclaringClass() != null;
    }

    private boolean isLocalOrAnonymousClass() {
        return isLocalClass() || isAnonymousClass();
    }

    @CallerSensitive
    public Class<?>[] getClasses() {
        ArrayList arrayList = new ArrayList();
        while (this != null) {
            for (Class cls : this.getDeclaredClasses()) {
                if (Modifier.isPublic(cls.getModifiers())) {
                    arrayList.add(cls);
                }
            }
            this = this.superClass;
        }
        return (Class[]) arrayList.toArray(new Class[arrayList.size()]);
    }

    @CallerSensitive
    public Field[] getFields() throws SecurityException {
        ArrayList arrayList = new ArrayList();
        getPublicFieldsRecursive(arrayList);
        return (Field[]) arrayList.toArray(new Field[arrayList.size()]);
    }

    private void getPublicFieldsRecursive(List<Field> list) {
        for (Class cls = this; cls != null; cls = cls.superClass) {
            Collections.addAll(list, cls.getPublicDeclaredFields());
        }
        Object[] objArr = this.ifTable;
        if (objArr != null) {
            for (int i = 0; i < objArr.length; i += 2) {
                Collections.addAll(list, ((Class) objArr[i]).getPublicDeclaredFields());
            }
        }
    }

    @CallerSensitive
    public Method[] getMethods() throws SecurityException {
        ArrayList arrayList = new ArrayList();
        getPublicMethodsInternal(arrayList);
        CollectionUtils.removeDuplicates(arrayList, Method.ORDER_BY_SIGNATURE);
        return (Method[]) arrayList.toArray(new Method[arrayList.size()]);
    }

    private void getPublicMethodsInternal(List<Method> list) {
        Collections.addAll(list, getDeclaredMethodsUnchecked(true));
        if (!isInterface()) {
            for (Class<? super T> cls = this.superClass; cls != null; cls = cls.superClass) {
                Collections.addAll(list, cls.getDeclaredMethodsUnchecked(true));
            }
        }
        Object[] objArr = this.ifTable;
        if (objArr != null) {
            for (int i = 0; i < objArr.length; i += 2) {
                Collections.addAll(list, ((Class) objArr[i]).getDeclaredMethodsUnchecked(true));
            }
        }
    }

    @CallerSensitive
    public Constructor<?>[] getConstructors() throws SecurityException {
        return getDeclaredConstructorsInternal(true);
    }

    public Field getField(String str) throws NoSuchFieldException {
        if (str != null) {
            Field publicFieldRecursive = getPublicFieldRecursive(str);
            if (publicFieldRecursive != null) {
                return publicFieldRecursive;
            }
            throw new NoSuchFieldException(str);
        }
        throw new NullPointerException("name == null");
    }

    @CallerSensitive
    public Method getMethod(String str, Class<?>... clsArr) throws NoSuchMethodException, SecurityException {
        return getMethod(str, clsArr, true);
    }

    public Constructor<T> getConstructor(Class<?>... clsArr) throws NoSuchMethodException, SecurityException {
        return getConstructor0(clsArr, 0);
    }

    public Method[] getDeclaredMethods() throws SecurityException {
        Method[] declaredMethodsUnchecked = getDeclaredMethodsUnchecked(false);
        for (Method method : declaredMethodsUnchecked) {
            method.getReturnType();
            method.getParameterTypes();
        }
        return declaredMethodsUnchecked;
    }

    public Constructor<?>[] getDeclaredConstructors() throws SecurityException {
        return getDeclaredConstructorsInternal(false);
    }

    @CallerSensitive
    public Method getDeclaredMethod(String str, Class<?>... clsArr) throws NoSuchMethodException, SecurityException {
        return getMethod(str, clsArr, false);
    }

    private Method getMethod(String str, Class<?>[] clsArr, boolean z) throws NoSuchMethodException {
        Method method;
        if (str != null) {
            if (clsArr == null) {
                clsArr = EmptyArray.CLASS;
            }
            int length = clsArr.length;
            int i = 0;
            while (i < length) {
                if (clsArr[i] != null) {
                    i++;
                } else {
                    throw new NoSuchMethodException("parameter type is null");
                }
            }
            if (z) {
                method = getPublicMethodRecursive(str, clsArr);
            } else {
                method = getDeclaredMethodInternal(str, clsArr);
            }
            if (method != null && (!z || Modifier.isPublic(method.getAccessFlags()))) {
                return method;
            }
            throw new NoSuchMethodException(getName() + BaseIconCache.EMPTY_CLASS_NAME + str + WifiEnterpriseConfig.CA_CERT_ALIAS_DELIMITER + Arrays.toString((Object[]) clsArr));
        }
        throw new NullPointerException("name == null");
    }

    private Method getPublicMethodRecursive(String str, Class<?>[] clsArr) {
        for (Class cls = this; cls != null; cls = cls.getSuperclass()) {
            Method declaredMethodInternal = cls.getDeclaredMethodInternal(str, clsArr);
            if (declaredMethodInternal != null && Modifier.isPublic(declaredMethodInternal.getAccessFlags())) {
                return declaredMethodInternal;
            }
        }
        return findInterfaceMethod(str, clsArr);
    }

    public Method getInstanceMethod(String str, Class<?>[] clsArr) throws NoSuchMethodException, IllegalAccessException {
        for (Class cls = this; cls != null; cls = cls.getSuperclass()) {
            Method declaredMethodInternal = cls.getDeclaredMethodInternal(str, clsArr);
            if (declaredMethodInternal != null && !Modifier.isStatic(declaredMethodInternal.getModifiers())) {
                return declaredMethodInternal;
            }
        }
        return findInterfaceMethod(str, clsArr);
    }

    private Method findInterfaceMethod(String str, Class<?>[] clsArr) {
        Object[] objArr = this.ifTable;
        if (objArr == null) {
            return null;
        }
        for (int length = objArr.length - 2; length >= 0; length -= 2) {
            Method publicMethodRecursive = ((Class) objArr[length]).getPublicMethodRecursive(str, clsArr);
            if (publicMethodRecursive != null && Modifier.isPublic(publicMethodRecursive.getAccessFlags())) {
                return publicMethodRecursive;
            }
        }
        return null;
    }

    @CallerSensitive
    public Constructor<T> getDeclaredConstructor(Class<?>... clsArr) throws NoSuchMethodException, SecurityException {
        return getConstructor0(clsArr, 1);
    }

    public InputStream getResourceAsStream(String str) {
        String resolveName = resolveName(str);
        ClassLoader classLoader2 = getClassLoader();
        if (classLoader2 == null) {
            return ClassLoader.getSystemResourceAsStream(resolveName);
        }
        return classLoader2.getResourceAsStream(resolveName);
    }

    public URL getResource(String str) {
        String resolveName = resolveName(str);
        ClassLoader classLoader2 = getClassLoader();
        if (classLoader2 == null) {
            return ClassLoader.getSystemResource(resolveName);
        }
        return classLoader2.getResource(resolveName);
    }

    private String resolveName(String str) {
        if (str == null) {
            return str;
        }
        if (str.startsWith("/")) {
            return str.substring(1);
        }
        while (this.isArray()) {
            this = this.getComponentType();
        }
        String name2 = this.getName();
        int lastIndexOf = name2.lastIndexOf(46);
        if (lastIndexOf == -1) {
            return str;
        }
        return name2.substring(0, lastIndexOf).replace('.', '/') + "/" + str;
    }

    private Constructor<T> getConstructor0(Class<?>[] clsArr, int i) throws NoSuchMethodException {
        if (clsArr == null) {
            clsArr = EmptyArray.CLASS;
        }
        int length = clsArr.length;
        int i2 = 0;
        while (i2 < length) {
            if (clsArr[i2] != null) {
                i2++;
            } else {
                throw new NoSuchMethodException("parameter type is null");
            }
        }
        Constructor<T> declaredConstructorInternal = getDeclaredConstructorInternal(clsArr);
        if (declaredConstructorInternal != null && (i != 0 || Modifier.isPublic(declaredConstructorInternal.getAccessFlags()))) {
            return declaredConstructorInternal;
        }
        throw new NoSuchMethodException(getName() + ".<init> " + Arrays.toString((Object[]) clsArr));
    }

    public boolean isEnum() {
        return (getModifiers() & 16384) != 0 && getSuperclass() == Enum.class;
    }

    public T[] getEnumConstants() {
        Object[] enumConstantsShared = getEnumConstantsShared();
        if (enumConstantsShared != null) {
            return (Object[]) enumConstantsShared.clone();
        }
        return null;
    }

    public T[] getEnumConstantsShared() {
        if (!isEnum()) {
            return null;
        }
        return (Object[]) Enum.getSharedConstants(this);
    }

    public T cast(Object obj) {
        if (obj == null || isInstance(obj)) {
            return obj;
        }
        throw new ClassCastException(cannotCastMsg(obj));
    }

    private String cannotCastMsg(Object obj) {
        return "Cannot cast " + obj.getClass().getName() + " to " + getName();
    }

    public <U> Class<? extends U> asSubclass(Class<U> cls) {
        if (cls.isAssignableFrom(this)) {
            return this;
        }
        throw new ClassCastException(toString() + " cannot be cast to " + cls.getName());
    }

    public <A extends Annotation> A getAnnotation(Class<A> cls) {
        A declaredAnnotation;
        Objects.requireNonNull(cls);
        A declaredAnnotation2 = getDeclaredAnnotation(cls);
        if (declaredAnnotation2 != null) {
            return declaredAnnotation2;
        }
        if (!cls.isDeclaredAnnotationPresent(Inherited.class)) {
            return null;
        }
        do {
            this = this.getSuperclass();
            if (this == null) {
                return null;
            }
            declaredAnnotation = this.getDeclaredAnnotation(cls);
        } while (declaredAnnotation == null);
        return declaredAnnotation;
    }

    public boolean isAnnotationPresent(Class<? extends Annotation> cls) {
        if (cls == null) {
            throw new NullPointerException("annotationClass == null");
        } else if (isDeclaredAnnotationPresent(cls)) {
            return true;
        } else {
            if (!cls.isDeclaredAnnotationPresent(Inherited.class)) {
                return false;
            }
            do {
                this = this.getSuperclass();
                if (this == null) {
                    return false;
                }
            } while (!this.isDeclaredAnnotationPresent(cls));
            return true;
        }
    }

    public <A extends Annotation> A[] getAnnotationsByType(Class<A> cls) {
        Class superclass;
        A[] annotationsByType = super.getAnnotationsByType(cls);
        if (annotationsByType.length != 0) {
            return annotationsByType;
        }
        if (!cls.isDeclaredAnnotationPresent(Inherited.class) || (superclass = getSuperclass()) == null) {
            return (Annotation[]) Array.newInstance((Class<?>) cls, 0);
        }
        return superclass.getAnnotationsByType(cls);
    }

    public Annotation[] getAnnotations() {
        HashMap hashMap = new HashMap();
        for (Annotation annotation : getDeclaredAnnotations()) {
            hashMap.put(annotation.annotationType(), annotation);
        }
        while (true) {
            this = this.getSuperclass();
            if (this != null) {
                for (Annotation annotation2 : this.getDeclaredAnnotations()) {
                    Class<? extends Annotation> annotationType = annotation2.annotationType();
                    if (!hashMap.containsKey(annotationType) && annotationType.isDeclaredAnnotationPresent(Inherited.class)) {
                        hashMap.put(annotationType, annotation2);
                    }
                }
            } else {
                Collection values = hashMap.values();
                return (Annotation[]) values.toArray((T[]) new Annotation[values.size()]);
            }
        }
    }

    private String getSignatureAttribute() {
        String[] signatureAnnotation = getSignatureAnnotation();
        if (signatureAnnotation == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String append : signatureAnnotation) {
            sb.append(append);
        }
        return sb.toString();
    }

    public boolean isProxy() {
        return (this.accessFlags & 262144) != 0;
    }

    public int getAccessFlags() {
        return this.accessFlags;
    }

    private static class Caches {
        /* access modifiers changed from: private */
        public static final BasicLruCache<Class, Type[]> genericInterfaces = new BasicLruCache<>(8);

        private Caches() {
        }
    }
}
