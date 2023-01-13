package java.p026io;

import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import dalvik.system.VMRuntime;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.misc.Unsafe;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ReflectUtil;

/* renamed from: java.io.ObjectStreamClass */
public class ObjectStreamClass implements Serializable {
    static final int MAX_SDK_TARGET_FOR_CLINIT_UIDGEN_WORKAROUND = 23;
    public static final ObjectStreamField[] NO_FIELDS;
    private static final ObjectStreamField[] serialPersistentFields;
    private static final long serialVersionUID = -6120832682080437368L;
    public static DefaultSUIDCompatibilityListener suidCompatibilityListener = new ObjectStreamClass$$ExternalSyntheticLambda0();
    /* access modifiers changed from: private */

    /* renamed from: cl */
    public Class<?> f524cl;
    /* access modifiers changed from: private */
    public Constructor<?> cons;
    private volatile ClassDataSlot[] dataLayout;
    private ExceptionInfo defaultSerializeEx;
    /* access modifiers changed from: private */
    public ExceptionInfo deserializeEx;
    /* access modifiers changed from: private */
    public boolean externalizable;
    private FieldReflector fieldRefl;
    /* access modifiers changed from: private */
    public ObjectStreamField[] fields;
    private boolean hasBlockExternalData = true;
    /* access modifiers changed from: private */
    public boolean hasWriteObjectData;
    private boolean initialized;
    /* access modifiers changed from: private */
    public boolean isEnum;
    private boolean isProxy;
    private ObjectStreamClass localDesc;
    private String name;
    private int numObjFields;
    private int primDataSize;
    /* access modifiers changed from: private */
    public Method readObjectMethod;
    /* access modifiers changed from: private */
    public Method readObjectNoDataMethod;
    /* access modifiers changed from: private */
    public Method readResolveMethod;
    private ClassNotFoundException resolveEx;
    private boolean serializable;
    /* access modifiers changed from: private */
    public ExceptionInfo serializeEx;
    /* access modifiers changed from: private */
    public volatile Long suid;
    private ObjectStreamClass superDesc;
    /* access modifiers changed from: private */
    public Method writeObjectMethod;
    /* access modifiers changed from: private */
    public Method writeReplaceMethod;

    /* renamed from: java.io.ObjectStreamClass$DefaultSUIDCompatibilityListener */
    public interface DefaultSUIDCompatibilityListener {
        void warnDefaultSUIDTargetVersionDependent(Class<?> cls, long j);
    }

    private static native boolean hasStaticInitializer(Class<?> cls, boolean z);

    static {
        ObjectStreamField[] objectStreamFieldArr = new ObjectStreamField[0];
        NO_FIELDS = objectStreamFieldArr;
        serialPersistentFields = objectStreamFieldArr;
    }

    /* renamed from: java.io.ObjectStreamClass$Caches */
    private static class Caches {
        static final ConcurrentMap<WeakClassKey, Reference<?>> localDescs = new ConcurrentHashMap();
        /* access modifiers changed from: private */
        public static final ReferenceQueue<Class<?>> localDescsQueue = new ReferenceQueue<>();
        static final ConcurrentMap<FieldReflectorKey, Reference<?>> reflectors = new ConcurrentHashMap();
        /* access modifiers changed from: private */
        public static final ReferenceQueue<Class<?>> reflectorsQueue = new ReferenceQueue<>();

        private Caches() {
        }
    }

    /* renamed from: java.io.ObjectStreamClass$ExceptionInfo */
    private static class ExceptionInfo {
        private final String className;
        private final String message;

        ExceptionInfo(String str, String str2) {
            this.className = str;
            this.message = str2;
        }

        /* access modifiers changed from: package-private */
        public InvalidClassException newInvalidClassException() {
            return new InvalidClassException(this.className, this.message);
        }
    }

    public static ObjectStreamClass lookup(Class<?> cls) {
        return lookup(cls, false);
    }

    public static ObjectStreamClass lookupAny(Class<?> cls) {
        return lookup(cls, true);
    }

    public String getName() {
        return this.name;
    }

    public long getSerialVersionUID() {
        if (this.suid == null) {
            this.suid = (Long) AccessController.doPrivileged(new PrivilegedAction<Long>() {
                public Long run() {
                    return Long.valueOf(ObjectStreamClass.computeDefaultSUID(ObjectStreamClass.this.f524cl));
                }
            });
        }
        return this.suid.longValue();
    }

    @CallerSensitive
    public Class<?> forClass() {
        if (this.f524cl == null) {
            return null;
        }
        requireInitialized();
        if (System.getSecurityManager() != null && ReflectUtil.needsPackageAccessCheck(Reflection.getCallerClass().getClassLoader(), this.f524cl.getClassLoader())) {
            ReflectUtil.checkPackageAccess(this.f524cl);
        }
        return this.f524cl;
    }

    public ObjectStreamField[] getFields() {
        return getFields(true);
    }

    public ObjectStreamField getField(String str) {
        return getField(str, (Class<?>) null);
    }

    public String toString() {
        return this.name + ": static final long serialVersionUID = " + getSerialVersionUID() + "L;";
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0053, code lost:
        if (r2 == null) goto L_0x0057;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    static java.p026io.ObjectStreamClass lookup(java.lang.Class<?> r6, boolean r7) {
        /*
            r0 = 0
            if (r7 != 0) goto L_0x000c
            java.lang.Class<java.io.Serializable> r7 = java.p026io.Serializable.class
            boolean r7 = r7.isAssignableFrom(r6)
            if (r7 != 0) goto L_0x000c
            return r0
        L_0x000c:
            java.lang.ref.ReferenceQueue r7 = java.p026io.ObjectStreamClass.Caches.localDescsQueue
            java.util.concurrent.ConcurrentMap<java.io.ObjectStreamClass$WeakClassKey, java.lang.ref.Reference<?>> r1 = java.p026io.ObjectStreamClass.Caches.localDescs
            processQueue(r7, r1)
            java.io.ObjectStreamClass$WeakClassKey r7 = new java.io.ObjectStreamClass$WeakClassKey
            java.lang.ref.ReferenceQueue r1 = java.p026io.ObjectStreamClass.Caches.localDescsQueue
            r7.<init>(r6, r1)
            java.util.concurrent.ConcurrentMap<java.io.ObjectStreamClass$WeakClassKey, java.lang.ref.Reference<?>> r1 = java.p026io.ObjectStreamClass.Caches.localDescs
            java.lang.Object r1 = r1.get(r7)
            java.lang.ref.Reference r1 = (java.lang.ref.Reference) r1
            if (r1 == 0) goto L_0x002d
            java.lang.Object r2 = r1.get()
            goto L_0x002e
        L_0x002d:
            r2 = r0
        L_0x002e:
            if (r2 != 0) goto L_0x0056
            java.io.ObjectStreamClass$EntryFuture r3 = new java.io.ObjectStreamClass$EntryFuture
            r3.<init>()
            java.lang.ref.SoftReference r4 = new java.lang.ref.SoftReference
            r4.<init>(r3)
        L_0x003a:
            if (r1 == 0) goto L_0x0041
            java.util.concurrent.ConcurrentMap<java.io.ObjectStreamClass$WeakClassKey, java.lang.ref.Reference<?>> r5 = java.p026io.ObjectStreamClass.Caches.localDescs
            r5.remove(r7, r1)
        L_0x0041:
            java.util.concurrent.ConcurrentMap<java.io.ObjectStreamClass$WeakClassKey, java.lang.ref.Reference<?>> r1 = java.p026io.ObjectStreamClass.Caches.localDescs
            java.lang.Object r1 = r1.putIfAbsent(r7, r4)
            java.lang.ref.Reference r1 = (java.lang.ref.Reference) r1
            if (r1 == 0) goto L_0x004f
            java.lang.Object r2 = r1.get()
        L_0x004f:
            if (r1 == 0) goto L_0x0053
            if (r2 == 0) goto L_0x003a
        L_0x0053:
            if (r2 != 0) goto L_0x0056
            goto L_0x0057
        L_0x0056:
            r3 = r0
        L_0x0057:
            boolean r1 = r2 instanceof java.p026io.ObjectStreamClass
            if (r1 == 0) goto L_0x005e
            java.io.ObjectStreamClass r2 = (java.p026io.ObjectStreamClass) r2
            return r2
        L_0x005e:
            boolean r1 = r2 instanceof java.p026io.ObjectStreamClass.EntryFuture
            if (r1 == 0) goto L_0x0075
            r3 = r2
            java.io.ObjectStreamClass$EntryFuture r3 = (java.p026io.ObjectStreamClass.EntryFuture) r3
            java.lang.Thread r1 = r3.getOwner()
            java.lang.Thread r2 = java.lang.Thread.currentThread()
            if (r1 != r2) goto L_0x0070
            goto L_0x0076
        L_0x0070:
            java.lang.Object r0 = r3.get()
            goto L_0x0076
        L_0x0075:
            r0 = r2
        L_0x0076:
            if (r0 != 0) goto L_0x0095
            java.io.ObjectStreamClass r0 = new java.io.ObjectStreamClass     // Catch:{ all -> 0x007e }
            r0.<init>(r6)     // Catch:{ all -> 0x007e }
            goto L_0x0080
        L_0x007e:
            r6 = move-exception
            r0 = r6
        L_0x0080:
            boolean r6 = r3.set(r0)
            if (r6 == 0) goto L_0x0091
            java.util.concurrent.ConcurrentMap<java.io.ObjectStreamClass$WeakClassKey, java.lang.ref.Reference<?>> r6 = java.p026io.ObjectStreamClass.Caches.localDescs
            java.lang.ref.SoftReference r1 = new java.lang.ref.SoftReference
            r1.<init>(r0)
            r6.put(r7, r1)
            goto L_0x0095
        L_0x0091:
            java.lang.Object r0 = r3.get()
        L_0x0095:
            boolean r6 = r0 instanceof java.p026io.ObjectStreamClass
            if (r6 == 0) goto L_0x009c
            java.io.ObjectStreamClass r0 = (java.p026io.ObjectStreamClass) r0
            return r0
        L_0x009c:
            boolean r6 = r0 instanceof java.lang.RuntimeException
            if (r6 != 0) goto L_0x00bc
            boolean r6 = r0 instanceof java.lang.Error
            if (r6 == 0) goto L_0x00a7
            java.lang.Error r0 = (java.lang.Error) r0
            throw r0
        L_0x00a7:
            java.lang.InternalError r6 = new java.lang.InternalError
            java.lang.StringBuilder r7 = new java.lang.StringBuilder
            java.lang.String r1 = "unexpected entry: "
            r7.<init>((java.lang.String) r1)
            r7.append((java.lang.Object) r0)
            java.lang.String r7 = r7.toString()
            r6.<init>((java.lang.String) r7)
            throw r6
        L_0x00bc:
            java.lang.RuntimeException r0 = (java.lang.RuntimeException) r0
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.ObjectStreamClass.lookup(java.lang.Class, boolean):java.io.ObjectStreamClass");
    }

    /* renamed from: java.io.ObjectStreamClass$EntryFuture */
    private static class EntryFuture {
        private static final Object unset = new Object();
        private Object entry;
        private final Thread owner;

        private EntryFuture() {
            this.owner = Thread.currentThread();
            this.entry = unset;
        }

        /* access modifiers changed from: package-private */
        public synchronized boolean set(Object obj) {
            if (this.entry != unset) {
                return false;
            }
            this.entry = obj;
            notifyAll();
            return true;
        }

        /* access modifiers changed from: package-private */
        public synchronized Object get() {
            boolean z = false;
            while (this.entry == unset) {
                try {
                    wait();
                } catch (InterruptedException unused) {
                    z = true;
                }
            }
            if (z) {
                AccessController.doPrivileged(new PrivilegedAction<Void>() {
                    public Void run() {
                        Thread.currentThread().interrupt();
                        return null;
                    }
                });
            }
            return this.entry;
        }

        /* access modifiers changed from: package-private */
        public Thread getOwner() {
            return this.owner;
        }
    }

    private ObjectStreamClass(final Class<?> cls) {
        this.f524cl = cls;
        this.name = cls.getName();
        this.isProxy = Proxy.isProxyClass(cls);
        this.isEnum = Enum.class.isAssignableFrom(cls);
        this.serializable = Serializable.class.isAssignableFrom(cls);
        this.externalizable = Externalizable.class.isAssignableFrom(cls);
        Class<? super Object> superclass = cls.getSuperclass();
        int i = 0;
        this.superDesc = superclass != null ? lookup(superclass, false) : null;
        this.localDesc = this;
        if (this.serializable) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                public Void run() {
                    if (ObjectStreamClass.this.isEnum) {
                        ObjectStreamClass.this.suid = 0L;
                        ObjectStreamClass.this.fields = ObjectStreamClass.NO_FIELDS;
                        return null;
                    } else if (cls.isArray()) {
                        ObjectStreamClass.this.fields = ObjectStreamClass.NO_FIELDS;
                        return null;
                    } else {
                        ObjectStreamClass.this.suid = ObjectStreamClass.getDeclaredSUID(cls);
                        try {
                            ObjectStreamClass.this.fields = ObjectStreamClass.getSerialFields(cls);
                            ObjectStreamClass.this.computeFieldOffsets();
                        } catch (InvalidClassException e) {
                            ObjectStreamClass objectStreamClass = ObjectStreamClass.this;
                            ExceptionInfo exceptionInfo = new ExceptionInfo(e.classname, e.getMessage());
                            objectStreamClass.deserializeEx = exceptionInfo;
                            objectStreamClass.serializeEx = exceptionInfo;
                            ObjectStreamClass.this.fields = ObjectStreamClass.NO_FIELDS;
                        }
                        if (ObjectStreamClass.this.externalizable) {
                            ObjectStreamClass.this.cons = ObjectStreamClass.getExternalizableConstructor(cls);
                        } else {
                            ObjectStreamClass.this.cons = ObjectStreamClass.getSerializableConstructor(cls);
                            boolean z = true;
                            ObjectStreamClass.this.writeObjectMethod = ObjectStreamClass.getPrivateMethod(cls, "writeObject", new Class[]{ObjectOutputStream.class}, Void.TYPE);
                            ObjectStreamClass.this.readObjectMethod = ObjectStreamClass.getPrivateMethod(cls, "readObject", new Class[]{ObjectInputStream.class}, Void.TYPE);
                            ObjectStreamClass.this.readObjectNoDataMethod = ObjectStreamClass.getPrivateMethod(cls, "readObjectNoData", (Class<?>[]) null, Void.TYPE);
                            ObjectStreamClass objectStreamClass2 = ObjectStreamClass.this;
                            if (objectStreamClass2.writeObjectMethod == null) {
                                z = false;
                            }
                            objectStreamClass2.hasWriteObjectData = z;
                        }
                        ObjectStreamClass.this.writeReplaceMethod = ObjectStreamClass.getInheritableMethod(cls, "writeReplace", (Class<?>[]) null, Object.class);
                        ObjectStreamClass.this.readResolveMethod = ObjectStreamClass.getInheritableMethod(cls, "readResolve", (Class<?>[]) null, Object.class);
                        return null;
                    }
                }
            });
        } else {
            this.suid = 0L;
            this.fields = NO_FIELDS;
        }
        try {
            this.fieldRefl = getReflector(this.fields, this);
            if (this.deserializeEx == null) {
                if (this.isEnum) {
                    this.deserializeEx = new ExceptionInfo(this.name, "enum type");
                } else if (this.cons == null) {
                    this.deserializeEx = new ExceptionInfo(this.name, "no valid constructor");
                }
            }
            while (true) {
                ObjectStreamField[] objectStreamFieldArr = this.fields;
                if (i < objectStreamFieldArr.length) {
                    if (objectStreamFieldArr[i].getField() == null) {
                        this.defaultSerializeEx = new ExceptionInfo(this.name, "unmatched serializable field(s) declared");
                    }
                    i++;
                } else {
                    this.initialized = true;
                    return;
                }
            }
        } catch (InvalidClassException e) {
            throw new InternalError((Throwable) e);
        }
    }

    ObjectStreamClass() {
    }

    /* access modifiers changed from: package-private */
    public void initProxy(Class<?> cls, ClassNotFoundException classNotFoundException, ObjectStreamClass objectStreamClass) throws InvalidClassException {
        ObjectStreamClass objectStreamClass2;
        if (cls != null) {
            objectStreamClass2 = lookup(cls, true);
            if (!objectStreamClass2.isProxy) {
                throw new InvalidClassException("cannot bind proxy descriptor to a non-proxy class");
            }
        } else {
            objectStreamClass2 = null;
        }
        this.f524cl = cls;
        this.resolveEx = classNotFoundException;
        this.superDesc = objectStreamClass;
        this.isProxy = true;
        this.serializable = true;
        this.suid = 0L;
        ObjectStreamField[] objectStreamFieldArr = NO_FIELDS;
        this.fields = objectStreamFieldArr;
        if (objectStreamClass2 != null) {
            this.localDesc = objectStreamClass2;
            this.name = objectStreamClass2.name;
            this.externalizable = objectStreamClass2.externalizable;
            this.writeReplaceMethod = objectStreamClass2.writeReplaceMethod;
            this.readResolveMethod = objectStreamClass2.readResolveMethod;
            this.deserializeEx = objectStreamClass2.deserializeEx;
            this.cons = objectStreamClass2.cons;
        }
        this.fieldRefl = getReflector(objectStreamFieldArr, this.localDesc);
        this.initialized = true;
    }

    /* access modifiers changed from: package-private */
    public void initNonProxy(ObjectStreamClass objectStreamClass, Class<?> cls, ClassNotFoundException classNotFoundException, ObjectStreamClass objectStreamClass2) throws InvalidClassException {
        ObjectStreamClass objectStreamClass3;
        boolean z;
        long longValue = Long.valueOf(objectStreamClass.getSerialVersionUID()).longValue();
        if (cls != null) {
            objectStreamClass3 = lookup(cls, true);
            if (!objectStreamClass3.isProxy) {
                boolean z2 = objectStreamClass.isEnum;
                if (z2 != objectStreamClass3.isEnum) {
                    throw new InvalidClassException(z2 ? "cannot bind enum descriptor to a non-enum class" : "cannot bind non-enum descriptor to an enum class");
                } else if (objectStreamClass.serializable == objectStreamClass3.serializable && !cls.isArray() && longValue != objectStreamClass3.getSerialVersionUID()) {
                    String str = objectStreamClass3.name;
                    throw new InvalidClassException(str, "local class incompatible: stream classdesc serialVersionUID = " + longValue + ", local class serialVersionUID = " + objectStreamClass3.getSerialVersionUID());
                } else if (!classNamesEqual(objectStreamClass.name, objectStreamClass3.name)) {
                    String str2 = objectStreamClass3.name;
                    throw new InvalidClassException(str2, "local class name incompatible with stream class name \"" + objectStreamClass.name + "\"");
                } else if (!objectStreamClass.isEnum) {
                    boolean z3 = objectStreamClass.serializable;
                    boolean z4 = objectStreamClass3.serializable;
                    if (z3 == z4 && objectStreamClass.externalizable != objectStreamClass3.externalizable) {
                        throw new InvalidClassException(objectStreamClass3.name, "Serializable incompatible with Externalizable");
                    } else if (!(z3 == z4 && (z = objectStreamClass.externalizable) == objectStreamClass3.externalizable && (z3 || z))) {
                        this.deserializeEx = new ExceptionInfo(objectStreamClass3.name, "class invalid for deserialization");
                    }
                }
            } else {
                throw new InvalidClassException("cannot bind non-proxy descriptor to a proxy class");
            }
        } else {
            objectStreamClass3 = null;
        }
        this.f524cl = cls;
        this.resolveEx = classNotFoundException;
        this.superDesc = objectStreamClass2;
        this.name = objectStreamClass.name;
        this.suid = Long.valueOf(longValue);
        this.isProxy = false;
        this.isEnum = objectStreamClass.isEnum;
        this.serializable = objectStreamClass.serializable;
        this.externalizable = objectStreamClass.externalizable;
        this.hasBlockExternalData = objectStreamClass.hasBlockExternalData;
        this.hasWriteObjectData = objectStreamClass.hasWriteObjectData;
        ObjectStreamField[] objectStreamFieldArr = objectStreamClass.fields;
        this.fields = objectStreamFieldArr;
        this.primDataSize = objectStreamClass.primDataSize;
        this.numObjFields = objectStreamClass.numObjFields;
        if (objectStreamClass3 != null) {
            this.localDesc = objectStreamClass3;
            this.writeObjectMethod = objectStreamClass3.writeObjectMethod;
            this.readObjectMethod = objectStreamClass3.readObjectMethod;
            this.readObjectNoDataMethod = objectStreamClass3.readObjectNoDataMethod;
            this.writeReplaceMethod = objectStreamClass3.writeReplaceMethod;
            this.readResolveMethod = objectStreamClass3.readResolveMethod;
            if (this.deserializeEx == null) {
                this.deserializeEx = objectStreamClass3.deserializeEx;
            }
            this.cons = objectStreamClass3.cons;
        }
        FieldReflector reflector = getReflector(objectStreamFieldArr, this.localDesc);
        this.fieldRefl = reflector;
        this.fields = reflector.getFields();
        this.initialized = true;
    }

    /* access modifiers changed from: package-private */
    public void readNonProxy(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        this.name = objectInputStream.readUTF();
        this.suid = Long.valueOf(objectInputStream.readLong());
        this.isProxy = false;
        byte readByte = objectInputStream.readByte();
        this.hasWriteObjectData = (readByte & 1) != 0;
        this.hasBlockExternalData = (readByte & 8) != 0;
        boolean z = (readByte & 4) != 0;
        this.externalizable = z;
        boolean z2 = (readByte & 2) != 0;
        if (!z || !z2) {
            this.serializable = z || z2;
            boolean z3 = (readByte & 16) != 0;
            this.isEnum = z3;
            if (!z3 || this.suid.longValue() == 0) {
                int readShort = objectInputStream.readShort();
                if (!this.isEnum || readShort == 0) {
                    this.fields = readShort > 0 ? new ObjectStreamField[readShort] : NO_FIELDS;
                    int i = 0;
                    while (i < readShort) {
                        char readByte2 = (char) objectInputStream.readByte();
                        String readUTF = objectInputStream.readUTF();
                        try {
                            this.fields[i] = new ObjectStreamField(readUTF, (readByte2 == 'L' || readByte2 == '[') ? objectInputStream.readTypeString() : new String(new char[]{readByte2}), false);
                            i++;
                        } catch (RuntimeException e) {
                            throw ((IOException) new InvalidClassException(this.name, "invalid descriptor for field " + readUTF).initCause(e));
                        }
                    }
                    computeFieldOffsets();
                    return;
                }
                throw new InvalidClassException(this.name, "enum descriptor has non-zero field count: " + readShort);
            }
            throw new InvalidClassException(this.name, "enum descriptor has non-zero serialVersionUID: " + this.suid);
        }
        throw new InvalidClassException(this.name, "serializable and externalizable flags conflict");
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x0029  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0030  */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x0041  */
    /* JADX WARNING: Removed duplicated region for block: B:24:0x0061 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void writeNonProxy(java.p026io.ObjectOutputStream r5) throws java.p026io.IOException {
        /*
            r4 = this;
            java.lang.String r0 = r4.name
            r5.writeUTF(r0)
            long r0 = r4.getSerialVersionUID()
            r5.writeLong(r0)
            boolean r0 = r4.externalizable
            r1 = 0
            if (r0 == 0) goto L_0x001d
            r0 = 4
            byte r0 = (byte) r0
            int r2 = r5.getProtocolVersion()
            r3 = 1
            if (r2 == r3) goto L_0x0025
            r0 = r0 | 8
            goto L_0x0022
        L_0x001d:
            boolean r0 = r4.serializable
            if (r0 == 0) goto L_0x0024
            r0 = 2
        L_0x0022:
            byte r0 = (byte) r0
            goto L_0x0025
        L_0x0024:
            r0 = r1
        L_0x0025:
            boolean r2 = r4.hasWriteObjectData
            if (r2 == 0) goto L_0x002c
            r0 = r0 | 1
            byte r0 = (byte) r0
        L_0x002c:
            boolean r2 = r4.isEnum
            if (r2 == 0) goto L_0x0033
            r0 = r0 | 16
            byte r0 = (byte) r0
        L_0x0033:
            r5.writeByte(r0)
            java.io.ObjectStreamField[] r0 = r4.fields
            int r0 = r0.length
            r5.writeShort(r0)
        L_0x003c:
            java.io.ObjectStreamField[] r0 = r4.fields
            int r2 = r0.length
            if (r1 >= r2) goto L_0x0061
            r0 = r0[r1]
            char r2 = r0.getTypeCode()
            r5.writeByte(r2)
            java.lang.String r2 = r0.getName()
            r5.writeUTF(r2)
            boolean r2 = r0.isPrimitive()
            if (r2 != 0) goto L_0x005e
            java.lang.String r0 = r0.getTypeString()
            r5.writeTypeString(r0)
        L_0x005e:
            int r1 = r1 + 1
            goto L_0x003c
        L_0x0061:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.ObjectStreamClass.writeNonProxy(java.io.ObjectOutputStream):void");
    }

    /* access modifiers changed from: package-private */
    public ClassNotFoundException getResolveException() {
        return this.resolveEx;
    }

    private final void requireInitialized() {
        if (!this.initialized) {
            throw new InternalError("Unexpected call when not initialized");
        }
    }

    /* access modifiers changed from: package-private */
    public void checkDeserialize() throws InvalidClassException {
        requireInitialized();
        ExceptionInfo exceptionInfo = this.deserializeEx;
        if (exceptionInfo != null) {
            throw exceptionInfo.newInvalidClassException();
        }
    }

    /* access modifiers changed from: package-private */
    public void checkSerialize() throws InvalidClassException {
        requireInitialized();
        ExceptionInfo exceptionInfo = this.serializeEx;
        if (exceptionInfo != null) {
            throw exceptionInfo.newInvalidClassException();
        }
    }

    /* access modifiers changed from: package-private */
    public void checkDefaultSerialize() throws InvalidClassException {
        requireInitialized();
        ExceptionInfo exceptionInfo = this.defaultSerializeEx;
        if (exceptionInfo != null) {
            throw exceptionInfo.newInvalidClassException();
        }
    }

    /* access modifiers changed from: package-private */
    public ObjectStreamClass getSuperDesc() {
        requireInitialized();
        return this.superDesc;
    }

    /* access modifiers changed from: package-private */
    public ObjectStreamClass getLocalDesc() {
        requireInitialized();
        return this.localDesc;
    }

    /* access modifiers changed from: package-private */
    public ObjectStreamField[] getFields(boolean z) {
        ObjectStreamField[] objectStreamFieldArr = this.fields;
        return z ? (ObjectStreamField[]) objectStreamFieldArr.clone() : objectStreamFieldArr;
    }

    /* access modifiers changed from: package-private */
    public ObjectStreamField getField(String str, Class<?> cls) {
        ObjectStreamField objectStreamField;
        Class<?> type;
        int i = 0;
        while (true) {
            ObjectStreamField[] objectStreamFieldArr = this.fields;
            if (i >= objectStreamFieldArr.length) {
                return null;
            }
            objectStreamField = objectStreamFieldArr[i];
            if (!objectStreamField.getName().equals(str) || (cls != null && ((cls != Object.class || objectStreamField.isPrimitive()) && ((type = objectStreamField.getType()) == null || !cls.isAssignableFrom(type))))) {
                i++;
            }
        }
        return objectStreamField;
    }

    /* access modifiers changed from: package-private */
    public boolean isProxy() {
        requireInitialized();
        return this.isProxy;
    }

    /* access modifiers changed from: package-private */
    public boolean isEnum() {
        requireInitialized();
        return this.isEnum;
    }

    /* access modifiers changed from: package-private */
    public boolean isExternalizable() {
        requireInitialized();
        return this.externalizable;
    }

    /* access modifiers changed from: package-private */
    public boolean isSerializable() {
        requireInitialized();
        return this.serializable;
    }

    /* access modifiers changed from: package-private */
    public boolean hasBlockExternalData() {
        requireInitialized();
        return this.hasBlockExternalData;
    }

    /* access modifiers changed from: package-private */
    public boolean hasWriteObjectData() {
        requireInitialized();
        return this.hasWriteObjectData;
    }

    /* access modifiers changed from: package-private */
    public boolean isInstantiable() {
        requireInitialized();
        return this.cons != null;
    }

    /* access modifiers changed from: package-private */
    public boolean hasWriteObjectMethod() {
        requireInitialized();
        return this.writeObjectMethod != null;
    }

    /* access modifiers changed from: package-private */
    public boolean hasReadObjectMethod() {
        requireInitialized();
        return this.readObjectMethod != null;
    }

    /* access modifiers changed from: package-private */
    public boolean hasReadObjectNoDataMethod() {
        requireInitialized();
        return this.readObjectNoDataMethod != null;
    }

    /* access modifiers changed from: package-private */
    public boolean hasWriteReplaceMethod() {
        requireInitialized();
        return this.writeReplaceMethod != null;
    }

    /* access modifiers changed from: package-private */
    public boolean hasReadResolveMethod() {
        requireInitialized();
        return this.readResolveMethod != null;
    }

    /* access modifiers changed from: package-private */
    public Object newInstance() throws InstantiationException, InvocationTargetException, UnsupportedOperationException {
        requireInitialized();
        Constructor<?> constructor = this.cons;
        if (constructor != null) {
            try {
                return constructor.newInstance(new Object[0]);
            } catch (IllegalAccessException e) {
                throw new InternalError((Throwable) e);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /* access modifiers changed from: package-private */
    public void invokeWriteObject(Object obj, ObjectOutputStream objectOutputStream) throws IOException, UnsupportedOperationException {
        requireInitialized();
        Method method = this.writeObjectMethod;
        if (method != null) {
            try {
                method.invoke(obj, objectOutputStream);
            } catch (InvocationTargetException e) {
                Throwable targetException = e.getTargetException();
                if (!(targetException instanceof IOException)) {
                    throwMiscException(targetException);
                    return;
                }
                throw ((IOException) targetException);
            } catch (IllegalAccessException e2) {
                throw new InternalError((Throwable) e2);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /* access modifiers changed from: package-private */
    public void invokeReadObject(Object obj, ObjectInputStream objectInputStream) throws ClassNotFoundException, IOException, UnsupportedOperationException {
        requireInitialized();
        Method method = this.readObjectMethod;
        if (method != null) {
            try {
                method.invoke(obj, objectInputStream);
            } catch (InvocationTargetException e) {
                Throwable targetException = e.getTargetException();
                if (targetException instanceof ClassNotFoundException) {
                    throw ((ClassNotFoundException) targetException);
                } else if (!(targetException instanceof IOException)) {
                    throwMiscException(targetException);
                } else {
                    throw ((IOException) targetException);
                }
            } catch (IllegalAccessException e2) {
                throw new InternalError((Throwable) e2);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /* access modifiers changed from: package-private */
    public void invokeReadObjectNoData(Object obj) throws IOException, UnsupportedOperationException {
        requireInitialized();
        Method method = this.readObjectNoDataMethod;
        if (method != null) {
            try {
                Object[] objArr = null;
                method.invoke(obj, (Object[]) null);
            } catch (InvocationTargetException e) {
                Throwable targetException = e.getTargetException();
                if (!(targetException instanceof ObjectStreamException)) {
                    throwMiscException(targetException);
                    return;
                }
                throw ((ObjectStreamException) targetException);
            } catch (IllegalAccessException e2) {
                throw new InternalError((Throwable) e2);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /* access modifiers changed from: package-private */
    public Object invokeWriteReplace(Object obj) throws IOException, UnsupportedOperationException {
        requireInitialized();
        Method method = this.writeReplaceMethod;
        if (method != null) {
            try {
                Object[] objArr = null;
                return method.invoke(obj, (Object[]) null);
            } catch (InvocationTargetException e) {
                Throwable targetException = e.getTargetException();
                if (targetException instanceof ObjectStreamException) {
                    throw ((ObjectStreamException) targetException);
                }
                throwMiscException(targetException);
                throw new InternalError(targetException);
            } catch (IllegalAccessException e2) {
                throw new InternalError((Throwable) e2);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /* access modifiers changed from: package-private */
    public Object invokeReadResolve(Object obj) throws IOException, UnsupportedOperationException {
        requireInitialized();
        Method method = this.readResolveMethod;
        if (method != null) {
            try {
                Object[] objArr = null;
                return method.invoke(obj, (Object[]) null);
            } catch (InvocationTargetException e) {
                Throwable targetException = e.getTargetException();
                if (targetException instanceof ObjectStreamException) {
                    throw ((ObjectStreamException) targetException);
                }
                throwMiscException(targetException);
                throw new InternalError(targetException);
            } catch (IllegalAccessException e2) {
                throw new InternalError((Throwable) e2);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    /* renamed from: java.io.ObjectStreamClass$ClassDataSlot */
    static class ClassDataSlot {
        final ObjectStreamClass desc;
        final boolean hasData;

        ClassDataSlot(ObjectStreamClass objectStreamClass, boolean z) {
            this.desc = objectStreamClass;
            this.hasData = z;
        }
    }

    /* access modifiers changed from: package-private */
    public ClassDataSlot[] getClassDataLayout() throws InvalidClassException {
        if (this.dataLayout == null) {
            this.dataLayout = getClassDataLayout0();
        }
        return this.dataLayout;
    }

    private ClassDataSlot[] getClassDataLayout0() throws InvalidClassException {
        ArrayList arrayList = new ArrayList();
        Class cls = this.f524cl;
        Class cls2 = cls;
        while (cls2 != null && Serializable.class.isAssignableFrom(cls2)) {
            cls2 = cls2.getSuperclass();
        }
        HashSet hashSet = new HashSet(3);
        while (this != null) {
            if (!hashSet.contains(this.name)) {
                hashSet.add(this.name);
                Class<?> cls3 = this.f524cl;
                String name2 = cls3 != null ? cls3.getName() : this.name;
                Class cls4 = cls;
                while (true) {
                    if (cls4 == cls2) {
                        cls4 = null;
                        break;
                    } else if (name2.equals(cls4.getName())) {
                        break;
                    } else {
                        cls4 = cls4.getSuperclass();
                    }
                }
                if (cls4 != null) {
                    while (cls != cls4) {
                        arrayList.add(new ClassDataSlot(lookup(cls, true), false));
                        cls = cls.getSuperclass();
                    }
                    cls = cls4.getSuperclass();
                }
                arrayList.add(new ClassDataSlot(this.getVariantFor(cls4), true));
                this = this.superDesc;
            } else {
                throw new InvalidClassException("Circular reference.");
            }
        }
        while (cls != cls2) {
            arrayList.add(new ClassDataSlot(lookup(cls, true), false));
            cls = cls.getSuperclass();
        }
        Collections.reverse(arrayList);
        return (ClassDataSlot[]) arrayList.toArray(new ClassDataSlot[arrayList.size()]);
    }

    /* access modifiers changed from: package-private */
    public int getPrimDataSize() {
        return this.primDataSize;
    }

    /* access modifiers changed from: package-private */
    public int getNumObjFields() {
        return this.numObjFields;
    }

    /* access modifiers changed from: package-private */
    public void getPrimFieldValues(Object obj, byte[] bArr) {
        this.fieldRefl.getPrimFieldValues(obj, bArr);
    }

    /* access modifiers changed from: package-private */
    public void setPrimFieldValues(Object obj, byte[] bArr) {
        this.fieldRefl.setPrimFieldValues(obj, bArr);
    }

    /* access modifiers changed from: package-private */
    public void getObjFieldValues(Object obj, Object[] objArr) {
        this.fieldRefl.getObjFieldValues(obj, objArr);
    }

    /* access modifiers changed from: package-private */
    public void setObjFieldValues(Object obj, Object[] objArr) {
        this.fieldRefl.setObjFieldValues(obj, objArr);
    }

    /* access modifiers changed from: private */
    public void computeFieldOffsets() throws InvalidClassException {
        int i = 0;
        this.primDataSize = 0;
        this.numObjFields = 0;
        int i2 = -1;
        while (true) {
            ObjectStreamField[] objectStreamFieldArr = this.fields;
            if (i < objectStreamFieldArr.length) {
                ObjectStreamField objectStreamField = objectStreamFieldArr[i];
                char typeCode = objectStreamField.getTypeCode();
                if (typeCode != 'F') {
                    if (typeCode != 'L') {
                        if (typeCode != 'S') {
                            if (typeCode != 'I') {
                                if (typeCode != 'J') {
                                    if (typeCode != 'Z') {
                                        if (typeCode != '[') {
                                            switch (typeCode) {
                                                case 'B':
                                                    break;
                                                case 'C':
                                                    break;
                                                case 'D':
                                                    break;
                                                default:
                                                    throw new InternalError();
                                            }
                                        }
                                    }
                                    int i3 = this.primDataSize;
                                    this.primDataSize = i3 + 1;
                                    objectStreamField.setOffset(i3);
                                    i++;
                                }
                                objectStreamField.setOffset(this.primDataSize);
                                this.primDataSize += 8;
                                i++;
                            }
                        }
                        objectStreamField.setOffset(this.primDataSize);
                        this.primDataSize += 2;
                        i++;
                    }
                    int i4 = this.numObjFields;
                    this.numObjFields = i4 + 1;
                    objectStreamField.setOffset(i4);
                    if (i2 == -1) {
                        i2 = i;
                    }
                    i++;
                }
                objectStreamField.setOffset(this.primDataSize);
                this.primDataSize += 4;
                i++;
            } else if (i2 != -1 && i2 + this.numObjFields != objectStreamFieldArr.length) {
                throw new InvalidClassException(this.name, "illegal field order");
            } else {
                return;
            }
        }
    }

    private ObjectStreamClass getVariantFor(Class<?> cls) throws InvalidClassException {
        if (this.f524cl == cls) {
            return this;
        }
        ObjectStreamClass objectStreamClass = new ObjectStreamClass();
        if (this.isProxy) {
            objectStreamClass.initProxy(cls, (ClassNotFoundException) null, this.superDesc);
        } else {
            objectStreamClass.initNonProxy(this, cls, (ClassNotFoundException) null, this.superDesc);
        }
        return objectStreamClass;
    }

    /* access modifiers changed from: private */
    public static Constructor<?> getExternalizableConstructor(Class<?> cls) {
        try {
            Class[] clsArr = null;
            Constructor<?> declaredConstructor = cls.getDeclaredConstructor((Class<?>[]) null);
            declaredConstructor.setAccessible(true);
            if ((1 & declaredConstructor.getModifiers()) != 0) {
                return declaredConstructor;
            }
            return null;
        } catch (NoSuchMethodException unused) {
            return null;
        }
    }

    /* access modifiers changed from: private */
    public static Constructor<?> getSerializableConstructor(Class<?> cls) {
        Class<?> cls2 = cls;
        while (Serializable.class.isAssignableFrom(cls2)) {
            cls2 = cls2.getSuperclass();
            if (cls2 == null) {
                return null;
            }
        }
        try {
            Class[] clsArr = null;
            Constructor<?> declaredConstructor = cls2.getDeclaredConstructor((Class<?>[]) null);
            int modifiers = declaredConstructor.getModifiers();
            if ((modifiers & 2) == 0) {
                if ((modifiers & 5) != 0 || packageEquals(cls, cls2)) {
                    if (declaredConstructor.getDeclaringClass() != cls) {
                        declaredConstructor = declaredConstructor.serializationCopy(declaredConstructor.getDeclaringClass(), cls);
                    }
                    declaredConstructor.setAccessible(true);
                    return declaredConstructor;
                }
            }
        } catch (NoSuchMethodException unused) {
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static Method getInheritableMethod(Class<?> cls, String str, Class<?>[] clsArr, Class<?> cls2) {
        Method method;
        Class<? super Object> cls3 = cls;
        while (true) {
            if (cls3 == null) {
                method = null;
                break;
            }
            try {
                method = cls3.getDeclaredMethod(str, clsArr);
                break;
            } catch (NoSuchMethodException unused) {
                cls3 = cls3.getSuperclass();
            }
        }
        if (method == null || method.getReturnType() != cls2) {
            return null;
        }
        method.setAccessible(true);
        int modifiers = method.getModifiers();
        if ((modifiers & 1032) != 0) {
            return null;
        }
        if ((modifiers & 5) != 0) {
            return method;
        }
        if ((modifiers & 2) != 0) {
            if (cls == cls3) {
                return method;
            }
            return null;
        } else if (packageEquals(cls, cls3)) {
            return method;
        } else {
            return null;
        }
    }

    /* access modifiers changed from: private */
    public static Method getPrivateMethod(Class<?> cls, String str, Class<?>[] clsArr, Class<?> cls2) {
        try {
            Method declaredMethod = cls.getDeclaredMethod(str, clsArr);
            declaredMethod.setAccessible(true);
            int modifiers = declaredMethod.getModifiers();
            if (declaredMethod.getReturnType() == cls2 && (modifiers & 8) == 0 && (modifiers & 2) != 0) {
                return declaredMethod;
            }
            return null;
        } catch (NoSuchMethodException unused) {
            return null;
        }
    }

    private static boolean packageEquals(Class<?> cls, Class<?> cls2) {
        return cls.getClassLoader() == cls2.getClassLoader() && getPackageName(cls).equals(getPackageName(cls2));
    }

    private static String getPackageName(Class<?> cls) {
        String name2 = cls.getName();
        int lastIndexOf = name2.lastIndexOf(91);
        if (lastIndexOf >= 0) {
            name2 = name2.substring(lastIndexOf + 2);
        }
        int lastIndexOf2 = name2.lastIndexOf(46);
        return lastIndexOf2 >= 0 ? name2.substring(0, lastIndexOf2) : "";
    }

    private static boolean classNamesEqual(String str, String str2) {
        return str.substring(str.lastIndexOf(46) + 1).equals(str2.substring(str2.lastIndexOf(46) + 1));
    }

    /* access modifiers changed from: private */
    public static String getClassSignature(Class<?> cls) {
        StringBuilder sb = new StringBuilder();
        while (cls.isArray()) {
            sb.append('[');
            cls = cls.getComponentType();
        }
        if (!cls.isPrimitive()) {
            sb.append("L" + cls.getName().replace('.', '/') + ';');
        } else if (cls == Integer.TYPE) {
            sb.append('I');
        } else if (cls == Byte.TYPE) {
            sb.append('B');
        } else if (cls == Long.TYPE) {
            sb.append('J');
        } else if (cls == Float.TYPE) {
            sb.append('F');
        } else if (cls == Double.TYPE) {
            sb.append('D');
        } else if (cls == Short.TYPE) {
            sb.append('S');
        } else if (cls == Character.TYPE) {
            sb.append('C');
        } else if (cls == Boolean.TYPE) {
            sb.append('Z');
        } else if (cls == Void.TYPE) {
            sb.append('V');
        } else {
            throw new InternalError();
        }
        return sb.toString();
    }

    /* access modifiers changed from: private */
    public static String getMethodSignature(Class<?>[] clsArr, Class<?> cls) {
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.KEY_CODE_START);
        for (Class<?> classSignature : clsArr) {
            sb.append(getClassSignature(classSignature));
        }
        sb.append(')');
        sb.append(getClassSignature(cls));
        return sb.toString();
    }

    private static void throwMiscException(Throwable th) throws IOException {
        if (th instanceof RuntimeException) {
            throw ((RuntimeException) th);
        } else if (th instanceof Error) {
            throw ((Error) th);
        } else {
            IOException iOException = new IOException("unexpected exception type");
            iOException.initCause(th);
            throw iOException;
        }
    }

    /* access modifiers changed from: private */
    public static ObjectStreamField[] getSerialFields(Class<?> cls) throws InvalidClassException {
        if (!Serializable.class.isAssignableFrom(cls) || Externalizable.class.isAssignableFrom(cls) || Proxy.isProxyClass(cls) || cls.isInterface()) {
            return NO_FIELDS;
        }
        ObjectStreamField[] declaredSerialFields = getDeclaredSerialFields(cls);
        if (declaredSerialFields == null) {
            declaredSerialFields = getDefaultSerialFields(cls);
        }
        Arrays.sort((Object[]) declaredSerialFields);
        return declaredSerialFields;
    }

    /* JADX WARNING: Removed duplicated region for block: B:8:0x001e A[RETURN] */
    /* JADX WARNING: Removed duplicated region for block: B:9:0x001f  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static java.p026io.ObjectStreamField[] getDeclaredSerialFields(java.lang.Class<?> r10) throws java.p026io.InvalidClassException {
        /*
            r0 = 1
            r1 = 0
            java.lang.String r2 = "serialPersistentFields"
            java.lang.reflect.Field r2 = r10.getDeclaredField(r2)     // Catch:{ Exception -> 0x001b }
            int r3 = r2.getModifiers()     // Catch:{ Exception -> 0x001b }
            r4 = 26
            r3 = r3 & r4
            if (r3 != r4) goto L_0x001b
            r2.setAccessible(r0)     // Catch:{ Exception -> 0x001b }
            java.lang.Object r2 = r2.get(r1)     // Catch:{ Exception -> 0x001b }
            java.io.ObjectStreamField[] r2 = (java.p026io.ObjectStreamField[]) r2     // Catch:{ Exception -> 0x001b }
            goto L_0x001c
        L_0x001b:
            r2 = r1
        L_0x001c:
            if (r2 != 0) goto L_0x001f
            return r1
        L_0x001f:
            int r1 = r2.length
            if (r1 != 0) goto L_0x0025
            java.io.ObjectStreamField[] r10 = NO_FIELDS
            return r10
        L_0x0025:
            int r1 = r2.length
            java.io.ObjectStreamField[] r1 = new java.p026io.ObjectStreamField[r1]
            java.util.HashSet r3 = new java.util.HashSet
            int r4 = r2.length
            r3.<init>((int) r4)
            r4 = 0
        L_0x002f:
            int r5 = r2.length
            if (r4 >= r5) goto L_0x008c
            r5 = r2[r4]
            java.lang.String r6 = r5.getName()
            boolean r7 = r3.contains(r6)
            if (r7 != 0) goto L_0x0078
            r3.add(r6)
            java.lang.reflect.Field r7 = r10.getDeclaredField(r6)     // Catch:{ NoSuchFieldException -> 0x0062 }
            java.lang.Class r8 = r7.getType()     // Catch:{ NoSuchFieldException -> 0x0062 }
            java.lang.Class r9 = r5.getType()     // Catch:{ NoSuchFieldException -> 0x0062 }
            if (r8 != r9) goto L_0x0062
            int r8 = r7.getModifiers()     // Catch:{ NoSuchFieldException -> 0x0062 }
            r8 = r8 & 8
            if (r8 != 0) goto L_0x0062
            java.io.ObjectStreamField r8 = new java.io.ObjectStreamField     // Catch:{ NoSuchFieldException -> 0x0062 }
            boolean r9 = r5.isUnshared()     // Catch:{ NoSuchFieldException -> 0x0062 }
            r8.<init>((java.lang.reflect.Field) r7, (boolean) r9, (boolean) r0)     // Catch:{ NoSuchFieldException -> 0x0062 }
            r1[r4] = r8     // Catch:{ NoSuchFieldException -> 0x0062 }
        L_0x0062:
            r7 = r1[r4]
            if (r7 != 0) goto L_0x0075
            java.io.ObjectStreamField r7 = new java.io.ObjectStreamField
            java.lang.Class r8 = r5.getType()
            boolean r5 = r5.isUnshared()
            r7.<init>((java.lang.String) r6, (java.lang.Class<?>) r8, (boolean) r5)
            r1[r4] = r7
        L_0x0075:
            int r4 = r4 + 1
            goto L_0x002f
        L_0x0078:
            java.io.InvalidClassException r10 = new java.io.InvalidClassException
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            java.lang.String r1 = "multiple serializable fields named "
            r0.<init>((java.lang.String) r1)
            r0.append((java.lang.String) r6)
            java.lang.String r0 = r0.toString()
            r10.<init>(r0)
            throw r10
        L_0x008c:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.ObjectStreamClass.getDeclaredSerialFields(java.lang.Class):java.io.ObjectStreamField[]");
    }

    private static ObjectStreamField[] getDefaultSerialFields(Class<?> cls) {
        Field[] declaredFields = cls.getDeclaredFields();
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < declaredFields.length; i++) {
            if ((declaredFields[i].getModifiers() & 136) == 0) {
                arrayList.add(new ObjectStreamField(declaredFields[i], false, true));
            }
        }
        int size = arrayList.size();
        if (size == 0) {
            return NO_FIELDS;
        }
        return (ObjectStreamField[]) arrayList.toArray(new ObjectStreamField[size]);
    }

    /* access modifiers changed from: private */
    public static Long getDeclaredSUID(Class<?> cls) {
        try {
            Field declaredField = cls.getDeclaredField("serialVersionUID");
            if ((declaredField.getModifiers() & 24) == 24) {
                declaredField.setAccessible(true);
                return Long.valueOf(declaredField.getLong((Object) null));
            }
        } catch (Exception unused) {
        }
        return null;
    }

    /* access modifiers changed from: private */
    public static long computeDefaultSUID(Class<?> cls) {
        boolean z;
        Class<?> cls2 = cls;
        if (!Serializable.class.isAssignableFrom(cls2) || Proxy.isProxyClass(cls)) {
            return 0;
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
            dataOutputStream.writeUTF(cls.getName());
            int modifiers = cls.getModifiers() & 1553;
            Method[] declaredMethods = cls.getDeclaredMethods();
            if ((modifiers & 512) != 0) {
                modifiers = declaredMethods.length > 0 ? modifiers | 1024 : modifiers & -1025;
            }
            dataOutputStream.writeInt(modifiers);
            if (!cls.isArray()) {
                Class[] interfaces = cls.getInterfaces();
                int length = interfaces.length;
                String[] strArr = new String[length];
                for (int i = 0; i < interfaces.length; i++) {
                    strArr[i] = interfaces[i].getName();
                }
                Arrays.sort((Object[]) strArr);
                for (int i2 = 0; i2 < length; i2++) {
                    dataOutputStream.writeUTF(strArr[i2]);
                }
            }
            Field[] declaredFields = cls.getDeclaredFields();
            int length2 = declaredFields.length;
            MemberSignature[] memberSignatureArr = new MemberSignature[length2];
            for (int i3 = 0; i3 < declaredFields.length; i3++) {
                memberSignatureArr[i3] = new MemberSignature(declaredFields[i3]);
            }
            Arrays.sort(memberSignatureArr, new Comparator<MemberSignature>() {
                public int compare(MemberSignature memberSignature, MemberSignature memberSignature2) {
                    return memberSignature.name.compareTo(memberSignature2.name);
                }
            });
            for (int i4 = 0; i4 < length2; i4++) {
                MemberSignature memberSignature = memberSignatureArr[i4];
                int modifiers2 = memberSignature.member.getModifiers() & 223;
                if ((modifiers2 & 2) == 0 || (modifiers2 & 136) == 0) {
                    dataOutputStream.writeUTF(memberSignature.name);
                    dataOutputStream.writeInt(modifiers2);
                    dataOutputStream.writeUTF(memberSignature.signature);
                }
            }
            boolean z2 = VMRuntime.getRuntime().getTargetSdkVersion() <= 23;
            if (hasStaticInitializer(cls2, z2)) {
                z = z2 && !hasStaticInitializer(cls2, false);
                dataOutputStream.writeUTF("<clinit>");
                dataOutputStream.writeInt(8);
                dataOutputStream.writeUTF("()V");
            } else {
                z = false;
            }
            Constructor[] declaredConstructors = cls.getDeclaredConstructors();
            int length3 = declaredConstructors.length;
            MemberSignature[] memberSignatureArr2 = new MemberSignature[length3];
            for (int i5 = 0; i5 < declaredConstructors.length; i5++) {
                memberSignatureArr2[i5] = new MemberSignature((Constructor<?>) declaredConstructors[i5]);
            }
            Arrays.sort(memberSignatureArr2, new Comparator<MemberSignature>() {
                public int compare(MemberSignature memberSignature, MemberSignature memberSignature2) {
                    return memberSignature.signature.compareTo(memberSignature2.signature);
                }
            });
            for (int i6 = 0; i6 < length3; i6++) {
                MemberSignature memberSignature2 = memberSignatureArr2[i6];
                int modifiers3 = memberSignature2.member.getModifiers() & 3391;
                if ((modifiers3 & 2) == 0) {
                    dataOutputStream.writeUTF("<init>");
                    dataOutputStream.writeInt(modifiers3);
                    dataOutputStream.writeUTF(memberSignature2.signature.replace('/', '.'));
                }
            }
            int length4 = declaredMethods.length;
            MemberSignature[] memberSignatureArr3 = new MemberSignature[length4];
            for (int i7 = 0; i7 < declaredMethods.length; i7++) {
                memberSignatureArr3[i7] = new MemberSignature(declaredMethods[i7]);
            }
            Arrays.sort(memberSignatureArr3, new Comparator<MemberSignature>() {
                public int compare(MemberSignature memberSignature, MemberSignature memberSignature2) {
                    int compareTo = memberSignature.name.compareTo(memberSignature2.name);
                    return compareTo == 0 ? memberSignature.signature.compareTo(memberSignature2.signature) : compareTo;
                }
            });
            for (int i8 = 0; i8 < length4; i8++) {
                MemberSignature memberSignature3 = memberSignatureArr3[i8];
                int modifiers4 = memberSignature3.member.getModifiers() & 3391;
                if ((modifiers4 & 2) == 0) {
                    dataOutputStream.writeUTF(memberSignature3.name);
                    dataOutputStream.writeInt(modifiers4);
                    dataOutputStream.writeUTF(memberSignature3.signature.replace('/', '.'));
                }
            }
            dataOutputStream.flush();
            byte[] digest = MessageDigest.getInstance("SHA").digest(byteArrayOutputStream.toByteArray());
            long j = 0;
            for (int min = Math.min(digest.length, 8) - 1; min >= 0; min--) {
                j = (j << 8) | ((long) (digest[min] & 255));
            }
            if (z) {
                suidCompatibilityListener.warnDefaultSUIDTargetVersionDependent(cls2, j);
            }
            return j;
        } catch (IOException e) {
            throw new InternalError((Throwable) e);
        } catch (NoSuchAlgorithmException e2) {
            throw new SecurityException(e2.getMessage());
        }
    }

    /* renamed from: java.io.ObjectStreamClass$MemberSignature */
    private static class MemberSignature {
        public final Member member;
        public final String name;
        public final String signature;

        public MemberSignature(Field field) {
            this.member = field;
            this.name = field.getName();
            this.signature = ObjectStreamClass.getClassSignature(field.getType());
        }

        public MemberSignature(Constructor<?> constructor) {
            this.member = constructor;
            this.name = constructor.getName();
            this.signature = ObjectStreamClass.getMethodSignature(constructor.getParameterTypes(), Void.TYPE);
        }

        public MemberSignature(Method method) {
            this.member = method;
            this.name = method.getName();
            this.signature = ObjectStreamClass.getMethodSignature(method.getParameterTypes(), method.getReturnType());
        }
    }

    /* renamed from: java.io.ObjectStreamClass$FieldReflector */
    private static class FieldReflector {
        private static final Unsafe unsafe = Unsafe.getUnsafe();
        private final ObjectStreamField[] fields;
        private final int numPrimFields;
        private final int[] offsets;
        private final long[] readKeys;
        private final char[] typeCodes;
        private final Class<?>[] types;
        private final long[] writeKeys;

        FieldReflector(ObjectStreamField[] objectStreamFieldArr) {
            this.fields = objectStreamFieldArr;
            int length = objectStreamFieldArr.length;
            this.readKeys = new long[length];
            this.writeKeys = new long[length];
            this.offsets = new int[length];
            this.typeCodes = new char[length];
            ArrayList arrayList = new ArrayList();
            HashSet hashSet = new HashSet();
            for (int i = 0; i < length; i++) {
                ObjectStreamField objectStreamField = objectStreamFieldArr[i];
                Field field = objectStreamField.getField();
                long j = -1;
                long objectFieldOffset = field != null ? unsafe.objectFieldOffset(field) : -1;
                this.readKeys[i] = objectFieldOffset;
                this.writeKeys[i] = hashSet.add(Long.valueOf(objectFieldOffset)) ? objectFieldOffset : j;
                this.offsets[i] = objectStreamField.getOffset();
                this.typeCodes[i] = objectStreamField.getTypeCode();
                if (!objectStreamField.isPrimitive()) {
                    arrayList.add(field != null ? field.getType() : null);
                }
            }
            Class<?>[] clsArr = (Class[]) arrayList.toArray(new Class[arrayList.size()]);
            this.types = clsArr;
            this.numPrimFields = length - clsArr.length;
        }

        /* access modifiers changed from: package-private */
        public ObjectStreamField[] getFields() {
            return this.fields;
        }

        /* access modifiers changed from: package-private */
        public void getPrimFieldValues(Object obj, byte[] bArr) {
            obj.getClass();
            for (int i = 0; i < this.numPrimFields; i++) {
                long j = this.readKeys[i];
                int i2 = this.offsets[i];
                char c = this.typeCodes[i];
                if (c == 'F') {
                    Bits.putFloat(bArr, i2, unsafe.getFloat(obj, j));
                } else if (c == 'S') {
                    Bits.putShort(bArr, i2, unsafe.getShort(obj, j));
                } else if (c == 'Z') {
                    Bits.putBoolean(bArr, i2, unsafe.getBoolean(obj, j));
                } else if (c == 'I') {
                    Bits.putInt(bArr, i2, unsafe.getInt(obj, j));
                } else if (c != 'J') {
                    switch (c) {
                        case 'B':
                            bArr[i2] = unsafe.getByte(obj, j);
                            break;
                        case 'C':
                            Bits.putChar(bArr, i2, unsafe.getChar(obj, j));
                            break;
                        case 'D':
                            Bits.putDouble(bArr, i2, unsafe.getDouble(obj, j));
                            break;
                        default:
                            throw new InternalError();
                    }
                } else {
                    Bits.putLong(bArr, i2, unsafe.getLong(obj, j));
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void setPrimFieldValues(Object obj, byte[] bArr) {
            obj.getClass();
            for (int i = 0; i < this.numPrimFields; i++) {
                long j = this.writeKeys[i];
                if (j != -1) {
                    int i2 = this.offsets[i];
                    char c = this.typeCodes[i];
                    if (c == 'F') {
                        unsafe.putFloat(obj, j, Bits.getFloat(bArr, i2));
                    } else if (c == 'S') {
                        unsafe.putShort(obj, j, Bits.getShort(bArr, i2));
                    } else if (c == 'Z') {
                        unsafe.putBoolean(obj, j, Bits.getBoolean(bArr, i2));
                    } else if (c == 'I') {
                        unsafe.putInt(obj, j, Bits.getInt(bArr, i2));
                    } else if (c != 'J') {
                        switch (c) {
                            case 'B':
                                unsafe.putByte(obj, j, bArr[i2]);
                                break;
                            case 'C':
                                unsafe.putChar(obj, j, Bits.getChar(bArr, i2));
                                break;
                            case 'D':
                                unsafe.putDouble(obj, j, Bits.getDouble(bArr, i2));
                                break;
                            default:
                                throw new InternalError();
                        }
                    } else {
                        unsafe.putLong(obj, j, Bits.getLong(bArr, i2));
                    }
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void getObjFieldValues(Object obj, Object[] objArr) {
            obj.getClass();
            int i = this.numPrimFields;
            while (i < this.fields.length) {
                char c = this.typeCodes[i];
                if (c == 'L' || c == '[') {
                    objArr[this.offsets[i]] = unsafe.getObject(obj, this.readKeys[i]);
                    i++;
                } else {
                    throw new InternalError();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void setObjFieldValues(Object obj, Object[] objArr) {
            obj.getClass();
            for (int i = this.numPrimFields; i < this.fields.length; i++) {
                long j = this.writeKeys[i];
                if (j != -1) {
                    char c = this.typeCodes[i];
                    if (c == 'L' || c == '[') {
                        Object obj2 = objArr[this.offsets[i]];
                        if (obj2 == null || this.types[i - this.numPrimFields].isInstance(obj2)) {
                            unsafe.putObject(obj, j, obj2);
                        } else {
                            Field field = this.fields[i].getField();
                            throw new ClassCastException("cannot assign instance of " + obj2.getClass().getName() + " to field " + field.getDeclaringClass().getName() + BaseIconCache.EMPTY_CLASS_NAME + field.getName() + " of type " + field.getType().getName() + " in instance of " + obj.getClass().getName());
                        }
                    } else {
                        throw new InternalError();
                    }
                }
            }
        }
    }

    private static FieldReflector getReflector(ObjectStreamField[] objectStreamFieldArr, ObjectStreamClass objectStreamClass) throws InvalidClassException {
        Object obj;
        EntryFuture entryFuture = null;
        Class<?> cls = (objectStreamClass == null || objectStreamFieldArr.length <= 0) ? null : objectStreamClass.f524cl;
        processQueue(Caches.reflectorsQueue, Caches.reflectors);
        FieldReflectorKey fieldReflectorKey = new FieldReflectorKey(cls, objectStreamFieldArr, Caches.reflectorsQueue);
        Reference reference = Caches.reflectors.get(fieldReflectorKey);
        Object obj2 = reference != null ? reference.get() : null;
        if (obj2 == null) {
            EntryFuture entryFuture2 = new EntryFuture();
            SoftReference softReference = new SoftReference(entryFuture2);
            do {
                if (reference != null) {
                    Caches.reflectors.remove(fieldReflectorKey, reference);
                }
                reference = Caches.reflectors.putIfAbsent(fieldReflectorKey, softReference);
                if (reference != null) {
                    obj2 = reference.get();
                }
                if (reference == null) {
                    break;
                }
            } while (obj2 == null);
            if (obj2 == null) {
                entryFuture = entryFuture2;
            }
        }
        if (obj2 instanceof FieldReflector) {
            return (FieldReflector) obj2;
        }
        if (obj2 instanceof EntryFuture) {
            obj2 = ((EntryFuture) obj2).get();
        } else if (obj2 == null) {
            try {
                obj = new FieldReflector(matchFields(objectStreamFieldArr, objectStreamClass));
            } catch (Throwable th) {
                obj = th;
            }
            entryFuture.set(obj2);
            Caches.reflectors.put(fieldReflectorKey, new SoftReference(obj2));
        }
        if (obj2 instanceof FieldReflector) {
            return (FieldReflector) obj2;
        }
        if (obj2 instanceof InvalidClassException) {
            throw ((InvalidClassException) obj2);
        } else if (obj2 instanceof RuntimeException) {
            throw ((RuntimeException) obj2);
        } else if (obj2 instanceof Error) {
            throw ((Error) obj2);
        } else {
            throw new InternalError("unexpected entry: " + obj2);
        }
    }

    /* renamed from: java.io.ObjectStreamClass$FieldReflectorKey */
    private static class FieldReflectorKey extends WeakReference<Class<?>> {
        private final int hash;
        private final boolean nullClass;
        private final String sigs;

        FieldReflectorKey(Class<?> cls, ObjectStreamField[] objectStreamFieldArr, ReferenceQueue<Class<?>> referenceQueue) {
            super(cls, referenceQueue);
            this.nullClass = cls == null;
            StringBuilder sb = new StringBuilder();
            for (ObjectStreamField objectStreamField : objectStreamFieldArr) {
                sb.append(objectStreamField.getName());
                sb.append(objectStreamField.getSignature());
            }
            String sb2 = sb.toString();
            this.sigs = sb2;
            this.hash = System.identityHashCode(cls) + sb2.hashCode();
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            Class cls;
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof FieldReflectorKey)) {
                return false;
            }
            FieldReflectorKey fieldReflectorKey = (FieldReflectorKey) obj;
            if (!this.nullClass ? !((cls = (Class) get()) == null || cls != fieldReflectorKey.get()) : fieldReflectorKey.nullClass) {
                if (this.sigs.equals(fieldReflectorKey.sigs)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static ObjectStreamField[] matchFields(ObjectStreamField[] objectStreamFieldArr, ObjectStreamClass objectStreamClass) throws InvalidClassException {
        ObjectStreamField[] objectStreamFieldArr2;
        if (objectStreamClass != null) {
            objectStreamFieldArr2 = objectStreamClass.fields;
        } else {
            objectStreamFieldArr2 = NO_FIELDS;
        }
        ObjectStreamField[] objectStreamFieldArr3 = new ObjectStreamField[objectStreamFieldArr.length];
        for (int i = 0; i < objectStreamFieldArr.length; i++) {
            ObjectStreamField objectStreamField = objectStreamFieldArr[i];
            ObjectStreamField objectStreamField2 = null;
            for (ObjectStreamField objectStreamField3 : objectStreamFieldArr2) {
                if (objectStreamField.getName().equals(objectStreamField3.getName()) && objectStreamField.getSignature().equals(objectStreamField3.getSignature())) {
                    if (objectStreamField3.getField() != null) {
                        objectStreamField2 = new ObjectStreamField(objectStreamField3.getField(), objectStreamField3.isUnshared(), false);
                    } else {
                        objectStreamField2 = new ObjectStreamField(objectStreamField3.getName(), objectStreamField3.getSignature(), objectStreamField3.isUnshared());
                    }
                }
            }
            if (objectStreamField2 == null) {
                objectStreamField2 = new ObjectStreamField(objectStreamField.getName(), objectStreamField.getSignature(), false);
            }
            objectStreamField2.setOffset(objectStreamField.getOffset());
            objectStreamFieldArr3[i] = objectStreamField2;
        }
        return objectStreamFieldArr3;
    }

    private static long getConstructorId(Class<?> cls) {
        int targetSdkVersion = VMRuntime.getRuntime().getTargetSdkVersion();
        if (targetSdkVersion <= 0 || targetSdkVersion > 24) {
            throw new UnsupportedOperationException("ObjectStreamClass.getConstructorId(Class<?>) is not supported on SDK " + targetSdkVersion);
        }
        System.logE("WARNING: ObjectStreamClass.getConstructorId(Class<?>) is private API andwill be removed in a future Android release.");
        return 1189998819991197253L;
    }

    private static Object newInstance(Class<?> cls, long j) {
        int targetSdkVersion = VMRuntime.getRuntime().getTargetSdkVersion();
        if (targetSdkVersion <= 0 || targetSdkVersion > 24) {
            throw new UnsupportedOperationException("ObjectStreamClass.newInstance(Class<?>, long) is not supported on SDK " + targetSdkVersion);
        }
        System.logE("WARNING: ObjectStreamClass.newInstance(Class<?>, long) is private API andwill be removed in a future Android release.");
        return Unsafe.getUnsafe().allocateInstance(cls);
    }

    static void processQueue(ReferenceQueue<Class<?>> referenceQueue, ConcurrentMap<? extends WeakReference<Class<?>>, ?> concurrentMap) {
        while (true) {
            Reference<? extends Class<?>> poll = referenceQueue.poll();
            if (poll != null) {
                concurrentMap.remove(poll);
            } else {
                return;
            }
        }
    }

    /* renamed from: java.io.ObjectStreamClass$WeakClassKey */
    static class WeakClassKey extends WeakReference<Class<?>> {
        private final int hash;

        WeakClassKey(Class<?> cls, ReferenceQueue<Class<?>> referenceQueue) {
            super(cls, referenceQueue);
            this.hash = System.identityHashCode(cls);
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof WeakClassKey)) {
                return false;
            }
            Object obj2 = get();
            if (obj2 == null || obj2 != ((WeakClassKey) obj).get()) {
                return false;
            }
            return true;
        }
    }
}
