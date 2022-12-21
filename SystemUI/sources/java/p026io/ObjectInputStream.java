package java.p026io;

import dalvik.system.VMStack;
import java.lang.ref.ReferenceQueue;
import java.lang.reflect.Array;
import java.lang.reflect.Proxy;
import java.p026io.ObjectStreamClass;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import jdk.internal.misc.SharedSecrets;
import kotlin.UShort;
import sun.reflect.misc.ReflectUtil;
import sun.security.util.DerValue;

/* renamed from: java.io.ObjectInputStream */
public class ObjectInputStream extends InputStream implements ObjectInput, ObjectStreamConstants {
    private static final int NULL_HANDLE = -1;
    private static final HashMap<String, Class<?>> primClasses;
    private static final Object unsharedMarker = new Object();
    /* access modifiers changed from: private */
    public final BlockDataInputStream bin;
    private boolean closed;
    private SerialCallbackContext curContext;
    /* access modifiers changed from: private */
    public boolean defaultDataEnd = false;
    private int depth;
    private final boolean enableOverride;
    private boolean enableResolve;
    /* access modifiers changed from: private */
    public final HandleTable handles;
    /* access modifiers changed from: private */
    public int passHandle = -1;
    private byte[] primVals;
    private final ValidationList vlist;

    /* renamed from: java.io.ObjectInputStream$GetField */
    public static abstract class GetField {
        public abstract boolean defaulted(String str) throws IOException;

        public abstract byte get(String str, byte b) throws IOException;

        public abstract char get(String str, char c) throws IOException;

        public abstract double get(String str, double d) throws IOException;

        public abstract float get(String str, float f) throws IOException;

        public abstract int get(String str, int i) throws IOException;

        public abstract long get(String str, long j) throws IOException;

        public abstract Object get(String str, Object obj) throws IOException;

        public abstract short get(String str, short s) throws IOException;

        public abstract boolean get(String str, boolean z) throws IOException;

        public abstract ObjectStreamClass getObjectStreamClass();
    }

    /* access modifiers changed from: private */
    public static native void bytesToDoubles(byte[] bArr, int i, double[] dArr, int i2, int i3);

    /* access modifiers changed from: private */
    public static native void bytesToFloats(byte[] bArr, int i, float[] fArr, int i2, int i3);

    /* access modifiers changed from: protected */
    public Object readObjectOverride() throws IOException, ClassNotFoundException {
        return null;
    }

    /* access modifiers changed from: protected */
    public Object resolveObject(Object obj) throws IOException {
        return obj;
    }

    static {
        HashMap<String, Class<?>> hashMap = new HashMap<>(8, 1.0f);
        primClasses = hashMap;
        hashMap.put("boolean", Boolean.TYPE);
        hashMap.put("byte", Byte.TYPE);
        hashMap.put("char", Character.TYPE);
        hashMap.put("short", Short.TYPE);
        hashMap.put("int", Integer.TYPE);
        hashMap.put("long", Long.TYPE);
        hashMap.put("float", Float.TYPE);
        hashMap.put("double", Double.TYPE);
        hashMap.put("void", Void.TYPE);
        SharedSecrets.setJavaObjectInputStreamAccess(new ObjectInputStream$$ExternalSyntheticLambda0());
    }

    /* renamed from: java.io.ObjectInputStream$Caches */
    private static class Caches {
        static final ConcurrentMap<ObjectStreamClass.WeakClassKey, Boolean> subclassAudits = new ConcurrentHashMap();
        static final ReferenceQueue<Class<?>> subclassAuditsQueue = new ReferenceQueue<>();

        private Caches() {
        }
    }

    public ObjectInputStream(InputStream inputStream) throws IOException {
        verifySubclass();
        BlockDataInputStream blockDataInputStream = new BlockDataInputStream(inputStream);
        this.bin = blockDataInputStream;
        this.handles = new HandleTable(10);
        this.vlist = new ValidationList();
        this.enableOverride = false;
        readStreamHeader();
        blockDataInputStream.setBlockDataMode(true);
    }

    protected ObjectInputStream() throws IOException, SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
        }
        this.bin = null;
        this.handles = null;
        this.vlist = null;
        this.enableOverride = true;
    }

    public final Object readObject() throws IOException, ClassNotFoundException {
        if (this.enableOverride) {
            return readObjectOverride();
        }
        int i = this.passHandle;
        try {
            Object readObject0 = readObject0(false);
            this.handles.markDependency(i, this.passHandle);
            ClassNotFoundException lookupException = this.handles.lookupException(this.passHandle);
            if (lookupException == null) {
                if (this.depth == 0) {
                    this.vlist.doCallbacks();
                }
                return readObject0;
            }
            throw lookupException;
        } finally {
            this.passHandle = i;
            if (this.closed && this.depth == 0) {
                clear();
            }
        }
    }

    public Object readUnshared() throws IOException, ClassNotFoundException {
        int i = this.passHandle;
        try {
            Object readObject0 = readObject0(true);
            this.handles.markDependency(i, this.passHandle);
            ClassNotFoundException lookupException = this.handles.lookupException(this.passHandle);
            if (lookupException == null) {
                if (this.depth == 0) {
                    this.vlist.doCallbacks();
                }
                return readObject0;
            }
            throw lookupException;
        } finally {
            this.passHandle = i;
            if (this.closed && this.depth == 0) {
                clear();
            }
        }
    }

    public void defaultReadObject() throws IOException, ClassNotFoundException {
        SerialCallbackContext serialCallbackContext = this.curContext;
        if (serialCallbackContext != null) {
            Object obj = serialCallbackContext.getObj();
            ObjectStreamClass desc = serialCallbackContext.getDesc();
            this.bin.setBlockDataMode(false);
            defaultReadFields(obj, desc);
            this.bin.setBlockDataMode(true);
            if (!desc.hasWriteObjectData()) {
                this.defaultDataEnd = true;
            }
            ClassNotFoundException lookupException = this.handles.lookupException(this.passHandle);
            if (lookupException != null) {
                throw lookupException;
            }
            return;
        }
        throw new NotActiveException("not in call to readObject");
    }

    public GetField readFields() throws IOException, ClassNotFoundException {
        SerialCallbackContext serialCallbackContext = this.curContext;
        if (serialCallbackContext != null) {
            serialCallbackContext.getObj();
            ObjectStreamClass desc = serialCallbackContext.getDesc();
            this.bin.setBlockDataMode(false);
            GetFieldImpl getFieldImpl = new GetFieldImpl(desc);
            getFieldImpl.readFields();
            this.bin.setBlockDataMode(true);
            if (!desc.hasWriteObjectData()) {
                this.defaultDataEnd = true;
            }
            return getFieldImpl;
        }
        throw new NotActiveException("not in call to readObject");
    }

    public void registerValidation(ObjectInputValidation objectInputValidation, int i) throws NotActiveException, InvalidObjectException {
        if (this.depth != 0) {
            this.vlist.register(objectInputValidation, i);
            return;
        }
        throw new NotActiveException("stream inactive");
    }

    /* access modifiers changed from: protected */
    public Class<?> resolveClass(ObjectStreamClass objectStreamClass) throws IOException, ClassNotFoundException {
        String name = objectStreamClass.getName();
        try {
            return Class.forName(name, false, latestUserDefinedLoader());
        } catch (ClassNotFoundException e) {
            Class<?> cls = primClasses.get(name);
            if (cls != null) {
                return cls;
            }
            throw e;
        }
    }

    /* access modifiers changed from: protected */
    public Class<?> resolveProxyClass(String[] strArr) throws IOException, ClassNotFoundException {
        ClassLoader latestUserDefinedLoader = latestUserDefinedLoader();
        Class[] clsArr = new Class[strArr.length];
        ClassLoader classLoader = null;
        boolean z = false;
        for (int i = 0; i < strArr.length; i++) {
            Class<?> cls = Class.forName(strArr[i], false, latestUserDefinedLoader);
            if ((cls.getModifiers() & 1) == 0) {
                if (!z) {
                    classLoader = cls.getClassLoader();
                    z = true;
                } else if (classLoader != cls.getClassLoader()) {
                    throw new IllegalAccessError("conflicting non-public interface class loaders");
                }
            }
            clsArr[i] = cls;
        }
        if (z) {
            latestUserDefinedLoader = classLoader;
        }
        try {
            return Proxy.getProxyClass(latestUserDefinedLoader, clsArr);
        } catch (IllegalArgumentException e) {
            throw new ClassNotFoundException((String) null, e);
        }
    }

    /* access modifiers changed from: protected */
    public boolean enableResolveObject(boolean z) throws SecurityException {
        SecurityManager securityManager;
        if (z == this.enableResolve) {
            return z;
        }
        if (z && (securityManager = System.getSecurityManager()) != null) {
            securityManager.checkPermission(SUBSTITUTION_PERMISSION);
        }
        this.enableResolve = z;
        return !z;
    }

    /* access modifiers changed from: protected */
    public void readStreamHeader() throws IOException, StreamCorruptedException {
        short readShort = this.bin.readShort();
        short readShort2 = this.bin.readShort();
        if (readShort != -21267 || readShort2 != 5) {
            throw new StreamCorruptedException(String.format("invalid stream header: %04X%04X", Short.valueOf(readShort), Short.valueOf(readShort2)));
        }
    }

    /* access modifiers changed from: protected */
    public ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
        ObjectStreamClass objectStreamClass = new ObjectStreamClass();
        objectStreamClass.readNonProxy(this);
        return objectStreamClass;
    }

    public int read() throws IOException {
        return this.bin.read();
    }

    public int read(byte[] bArr, int i, int i2) throws IOException {
        bArr.getClass();
        int i3 = i + i2;
        if (i >= 0 && i2 >= 0 && i3 <= bArr.length && i3 >= 0) {
            return this.bin.read(bArr, i, i2, false);
        }
        throw new IndexOutOfBoundsException();
    }

    public int available() throws IOException {
        return this.bin.available();
    }

    public void close() throws IOException {
        this.closed = true;
        if (this.depth == 0) {
            clear();
        }
        this.bin.close();
    }

    public boolean readBoolean() throws IOException {
        return this.bin.readBoolean();
    }

    public byte readByte() throws IOException {
        return this.bin.readByte();
    }

    public int readUnsignedByte() throws IOException {
        return this.bin.readUnsignedByte();
    }

    public char readChar() throws IOException {
        return this.bin.readChar();
    }

    public short readShort() throws IOException {
        return this.bin.readShort();
    }

    public int readUnsignedShort() throws IOException {
        return this.bin.readUnsignedShort();
    }

    public int readInt() throws IOException {
        return this.bin.readInt();
    }

    public long readLong() throws IOException {
        return this.bin.readLong();
    }

    public float readFloat() throws IOException {
        return this.bin.readFloat();
    }

    public double readDouble() throws IOException {
        return this.bin.readDouble();
    }

    public void readFully(byte[] bArr) throws IOException {
        this.bin.readFully(bArr, 0, bArr.length, false);
    }

    public void readFully(byte[] bArr, int i, int i2) throws IOException {
        int i3 = i + i2;
        if (i < 0 || i2 < 0 || i3 > bArr.length || i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        this.bin.readFully(bArr, i, i2, false);
    }

    public int skipBytes(int i) throws IOException {
        return this.bin.skipBytes(i);
    }

    @Deprecated
    public String readLine() throws IOException {
        return this.bin.readLine();
    }

    public String readUTF() throws IOException {
        return this.bin.readUTF();
    }

    /* access modifiers changed from: private */
    public void checkArray(Class<?> cls, int i) throws InvalidClassException {
        if (!cls.isArray()) {
            throw new IllegalArgumentException("not an array type");
        } else if (i < 0) {
            throw new NegativeArraySizeException();
        }
    }

    private void verifySubclass() {
        SecurityManager securityManager;
        Class<?> cls = getClass();
        if (cls != ObjectInputStream.class && (securityManager = System.getSecurityManager()) != null) {
            ObjectStreamClass.processQueue(Caches.subclassAuditsQueue, Caches.subclassAudits);
            ObjectStreamClass.WeakClassKey weakClassKey = new ObjectStreamClass.WeakClassKey(cls, Caches.subclassAuditsQueue);
            Boolean bool = Caches.subclassAudits.get(weakClassKey);
            if (bool == null) {
                bool = Boolean.valueOf(auditSubclass(cls));
                Caches.subclassAudits.putIfAbsent(weakClassKey, bool);
            }
            if (!bool.booleanValue()) {
                securityManager.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
            }
        }
    }

    private static boolean auditSubclass(final Class<?> cls) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            public Boolean run() {
                Class<? super ObjectInputStream> cls = Class.this;
                while (cls != ObjectInputStream.class) {
                    try {
                        Class[] clsArr = null;
                        cls.getDeclaredMethod("readUnshared", (Class<?>[]) null);
                        return Boolean.FALSE;
                    } catch (NoSuchMethodException unused) {
                        try {
                            Class[] clsArr2 = null;
                            cls.getDeclaredMethod("readFields", (Class<?>[]) null);
                            return Boolean.FALSE;
                        } catch (NoSuchMethodException unused2) {
                            cls = cls.getSuperclass();
                        }
                    }
                }
                return Boolean.TRUE;
            }
        })).booleanValue();
    }

    private void clear() {
        this.handles.clear();
        this.vlist.clear();
    }

    /* access modifiers changed from: private */
    public Object readObject0(boolean z) throws IOException {
        byte peekByte;
        Object readNull;
        boolean blockDataMode = this.bin.getBlockDataMode();
        if (blockDataMode) {
            int currentBlockRemaining = this.bin.currentBlockRemaining();
            if (currentBlockRemaining > 0) {
                throw new OptionalDataException(currentBlockRemaining);
            } else if (!this.defaultDataEnd) {
                this.bin.setBlockDataMode(false);
            } else {
                throw new OptionalDataException(true);
            }
        }
        while (true) {
            peekByte = this.bin.peekByte();
            if (peekByte != 121) {
                break;
            }
            this.bin.readByte();
            handleReset();
        }
        this.depth++;
        switch (peekByte) {
            case 112:
                readNull = readNull();
                break;
            case 113:
                readNull = readHandle(z);
                break;
            case 114:
            case 125:
                readNull = readClassDesc(z);
                break;
            case 115:
                readNull = checkResolve(readOrdinaryObject(z));
                break;
            case 116:
            case 124:
                readNull = checkResolve(readString(z));
                break;
            case 117:
                readNull = checkResolve(readArray(z));
                break;
            case 118:
                readNull = readClass(z);
                break;
            case 119:
            case 122:
                if (blockDataMode) {
                    this.bin.setBlockDataMode(true);
                    this.bin.peek();
                    throw new OptionalDataException(this.bin.currentBlockRemaining());
                }
                throw new StreamCorruptedException("unexpected block data");
            case 120:
                if (blockDataMode) {
                    throw new OptionalDataException(true);
                }
                throw new StreamCorruptedException("unexpected end of block data");
            case 123:
                throw new WriteAbortedException("writing aborted", readFatalException());
            case 126:
                readNull = checkResolve(readEnum(z));
                break;
            default:
                try {
                    throw new StreamCorruptedException(String.format("invalid type code: %02X", Byte.valueOf(peekByte)));
                } catch (Throwable th) {
                    this.depth--;
                    this.bin.setBlockDataMode(blockDataMode);
                    throw th;
                }
        }
        this.depth--;
        this.bin.setBlockDataMode(blockDataMode);
        return readNull;
    }

    private Object checkResolve(Object obj) throws IOException {
        if (!this.enableResolve || this.handles.lookupException(this.passHandle) != null) {
            return obj;
        }
        Object resolveObject = resolveObject(obj);
        if (resolveObject != obj) {
            this.handles.setObject(this.passHandle, resolveObject);
        }
        return resolveObject;
    }

    /* access modifiers changed from: package-private */
    public String readTypeString() throws IOException {
        int i = this.passHandle;
        try {
            byte peekByte = this.bin.peekByte();
            if (peekByte == 112) {
                String str = (String) readNull();
                this.passHandle = i;
                return str;
            } else if (peekByte != 113) {
                if (peekByte != 116) {
                    if (peekByte != 124) {
                        throw new StreamCorruptedException(String.format("invalid type code: %02X", Byte.valueOf(peekByte)));
                    }
                }
                return readString(false);
            } else {
                String str2 = (String) readHandle(false);
                this.passHandle = i;
                return str2;
            }
        } finally {
            this.passHandle = i;
        }
    }

    private Object readNull() throws IOException {
        if (this.bin.readByte() == 112) {
            this.passHandle = -1;
            return null;
        }
        throw new InternalError();
    }

    private Object readHandle(boolean z) throws IOException {
        if (this.bin.readByte() == 113) {
            int readInt = this.bin.readInt() - ObjectStreamConstants.baseWireHandle;
            this.passHandle = readInt;
            if (readInt < 0 || readInt >= this.handles.size()) {
                throw new StreamCorruptedException(String.format("invalid handle value: %08X", Integer.valueOf(this.passHandle + ObjectStreamConstants.baseWireHandle)));
            } else if (!z) {
                Object lookupObject = this.handles.lookupObject(this.passHandle);
                if (lookupObject != unsharedMarker) {
                    return lookupObject;
                }
                throw new InvalidObjectException("cannot read back reference to unshared object");
            } else {
                throw new InvalidObjectException("cannot read back reference as unshared");
            }
        } else {
            throw new InternalError();
        }
    }

    private Class<?> readClass(boolean z) throws IOException {
        if (this.bin.readByte() == 118) {
            ObjectStreamClass readClassDesc = readClassDesc(false);
            Class<?> forClass = readClassDesc.forClass();
            this.passHandle = this.handles.assign(z ? unsharedMarker : forClass);
            ClassNotFoundException resolveException = readClassDesc.getResolveException();
            if (resolveException != null) {
                this.handles.markException(this.passHandle, resolveException);
            }
            this.handles.finish(this.passHandle);
            return forClass;
        }
        throw new InternalError();
    }

    private ObjectStreamClass readClassDesc(boolean z) throws IOException {
        byte peekByte = this.bin.peekByte();
        if (peekByte == 125) {
            return readProxyDesc(z);
        }
        switch (peekByte) {
            case 112:
                return (ObjectStreamClass) readNull();
            case 113:
                return (ObjectStreamClass) readHandle(z);
            case 114:
                return readNonProxyDesc(z);
            default:
                throw new StreamCorruptedException(String.format("invalid type code: %02X", Byte.valueOf(peekByte)));
        }
    }

    private boolean isCustomSubclass() {
        return getClass().getClassLoader() != ObjectInputStream.class.getClassLoader();
    }

    private ObjectStreamClass readProxyDesc(boolean z) throws IOException {
        Class<?> cls;
        if (this.bin.readByte() == 125) {
            ObjectStreamClass objectStreamClass = new ObjectStreamClass();
            int assign = this.handles.assign(z ? unsharedMarker : objectStreamClass);
            this.passHandle = -1;
            int readInt = this.bin.readInt();
            String[] strArr = new String[readInt];
            for (int i = 0; i < readInt; i++) {
                strArr[i] = this.bin.readUTF();
            }
            this.bin.setBlockDataMode(true);
            ClassNotFoundException e = null;
            try {
                cls = resolveProxyClass(strArr);
                if (cls == null) {
                    try {
                        e = new ClassNotFoundException("null class");
                    } catch (ClassNotFoundException e2) {
                        e = e2;
                    }
                } else if (Proxy.isProxyClass(cls)) {
                    ReflectUtil.checkProxyPackageAccess(getClass().getClassLoader(), cls.getInterfaces());
                } else {
                    throw new InvalidClassException("Not a proxy");
                }
            } catch (ClassNotFoundException e3) {
                ClassNotFoundException classNotFoundException = e3;
                cls = null;
                e = classNotFoundException;
            }
            skipCustomData();
            objectStreamClass.initProxy(cls, e, readClassDesc(false));
            this.handles.finish(assign);
            this.passHandle = assign;
            return objectStreamClass;
        }
        throw new InternalError();
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v1, resolved type: java.lang.ClassNotFoundException} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v0, resolved type: java.lang.Class} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v2, resolved type: java.lang.ClassNotFoundException} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v3, resolved type: java.lang.ClassNotFoundException} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: java.lang.Class} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v4, resolved type: java.lang.ClassNotFoundException} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v5, resolved type: java.lang.ClassNotFoundException} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r3v6, resolved type: java.lang.ClassNotFoundException} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v3, resolved type: java.lang.Class} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v4, resolved type: java.lang.Class} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v5, resolved type: java.lang.Class} */
    /* JADX WARNING: type inference failed for: r4v2, types: [java.lang.Class] */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x003e, code lost:
        if (r2 == false) goto L_0x0047;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0040, code lost:
        sun.reflect.misc.ReflectUtil.checkPackageAccess((java.lang.Class<?>) r4);
        r4 = r4;
     */
    /* JADX WARNING: Multi-variable type inference failed */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private java.p026io.ObjectStreamClass readNonProxyDesc(boolean r6) throws java.p026io.IOException {
        /*
            r5 = this;
            java.io.ObjectInputStream$BlockDataInputStream r0 = r5.bin
            byte r0 = r0.readByte()
            r1 = 114(0x72, float:1.6E-43)
            if (r0 != r1) goto L_0x0069
            java.io.ObjectStreamClass r0 = new java.io.ObjectStreamClass
            r0.<init>()
            java.io.ObjectInputStream$HandleTable r1 = r5.handles
            if (r6 == 0) goto L_0x0016
            java.lang.Object r6 = unsharedMarker
            goto L_0x0017
        L_0x0016:
            r6 = r0
        L_0x0017:
            int r6 = r1.assign(r6)
            r1 = -1
            r5.passHandle = r1
            java.io.ObjectStreamClass r1 = r5.readClassDescriptor()     // Catch:{ ClassNotFoundException -> 0x005a }
            java.io.ObjectInputStream$BlockDataInputStream r2 = r5.bin
            r3 = 1
            r2.setBlockDataMode(r3)
            boolean r2 = r5.isCustomSubclass()
            r3 = 0
            java.lang.Class r4 = r5.resolveClass(r1)     // Catch:{ ClassNotFoundException -> 0x0044 }
            if (r4 != 0) goto L_0x003e
            java.lang.ClassNotFoundException r3 = new java.lang.ClassNotFoundException     // Catch:{ ClassNotFoundException -> 0x003b }
            java.lang.String r2 = "null class"
            r3.<init>(r2)     // Catch:{ ClassNotFoundException -> 0x003b }
            goto L_0x0047
        L_0x003b:
            r2 = move-exception
            r3 = r4
            goto L_0x0045
        L_0x003e:
            if (r2 == 0) goto L_0x0047
            sun.reflect.misc.ReflectUtil.checkPackageAccess((java.lang.Class<?>) r4)     // Catch:{ ClassNotFoundException -> 0x003b }
            goto L_0x0047
        L_0x0044:
            r2 = move-exception
        L_0x0045:
            r4 = r3
            r3 = r2
        L_0x0047:
            r5.skipCustomData()
            r2 = 0
            java.io.ObjectStreamClass r2 = r5.readClassDesc(r2)
            r0.initNonProxy(r1, r4, r3, r2)
            java.io.ObjectInputStream$HandleTable r1 = r5.handles
            r1.finish(r6)
            r5.passHandle = r6
            return r0
        L_0x005a:
            r5 = move-exception
            java.io.InvalidClassException r6 = new java.io.InvalidClassException
            java.lang.String r0 = "failed to read class descriptor"
            r6.<init>(r0)
            java.lang.Throwable r5 = r6.initCause(r5)
            java.io.IOException r5 = (java.p026io.IOException) r5
            throw r5
        L_0x0069:
            java.lang.InternalError r5 = new java.lang.InternalError
            r5.<init>()
            throw r5
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.ObjectInputStream.readNonProxyDesc(boolean):java.io.ObjectStreamClass");
    }

    private String readString(boolean z) throws IOException {
        String str;
        byte readByte = this.bin.readByte();
        if (readByte == 116) {
            str = this.bin.readUTF();
        } else if (readByte == 124) {
            str = this.bin.readLongUTF();
        } else {
            throw new StreamCorruptedException(String.format("invalid type code: %02X", Byte.valueOf(readByte)));
        }
        int assign = this.handles.assign(z ? unsharedMarker : str);
        this.passHandle = assign;
        this.handles.finish(assign);
        return str;
    }

    private Object readArray(boolean z) throws IOException {
        Object obj;
        Class<?> cls;
        if (this.bin.readByte() == 117) {
            ObjectStreamClass readClassDesc = readClassDesc(false);
            int readInt = this.bin.readInt();
            Class<?> forClass = readClassDesc.forClass();
            if (forClass != null) {
                cls = forClass.getComponentType();
                obj = Array.newInstance(cls, readInt);
            } else {
                cls = null;
                obj = null;
            }
            int assign = this.handles.assign(z ? unsharedMarker : obj);
            ClassNotFoundException resolveException = readClassDesc.getResolveException();
            if (resolveException != null) {
                this.handles.markException(assign, resolveException);
            }
            if (cls == null) {
                for (int i = 0; i < readInt; i++) {
                    readObject0(false);
                }
            } else if (!cls.isPrimitive()) {
                Object[] objArr = (Object[]) obj;
                for (int i2 = 0; i2 < readInt; i2++) {
                    objArr[i2] = readObject0(false);
                    this.handles.markDependency(assign, this.passHandle);
                }
            } else if (cls == Integer.TYPE) {
                this.bin.readInts((int[]) obj, 0, readInt);
            } else if (cls == Byte.TYPE) {
                this.bin.readFully((byte[]) obj, 0, readInt, true);
            } else if (cls == Long.TYPE) {
                this.bin.readLongs((long[]) obj, 0, readInt);
            } else if (cls == Float.TYPE) {
                this.bin.readFloats((float[]) obj, 0, readInt);
            } else if (cls == Double.TYPE) {
                this.bin.readDoubles((double[]) obj, 0, readInt);
            } else if (cls == Short.TYPE) {
                this.bin.readShorts((short[]) obj, 0, readInt);
            } else if (cls == Character.TYPE) {
                this.bin.readChars((char[]) obj, 0, readInt);
            } else if (cls == Boolean.TYPE) {
                this.bin.readBooleans((boolean[]) obj, 0, readInt);
            } else {
                throw new InternalError();
            }
            this.handles.finish(assign);
            this.passHandle = assign;
            return obj;
        }
        throw new InternalError();
    }

    private Enum<?> readEnum(boolean z) throws IOException {
        if (this.bin.readByte() == 126) {
            ObjectStreamClass readClassDesc = readClassDesc(false);
            if (readClassDesc.isEnum()) {
                Enum<?> enumR = null;
                int assign = this.handles.assign(z ? unsharedMarker : null);
                ClassNotFoundException resolveException = readClassDesc.getResolveException();
                if (resolveException != null) {
                    this.handles.markException(assign, resolveException);
                }
                String readString = readString(false);
                Class forClass = readClassDesc.forClass();
                if (forClass != null) {
                    try {
                        enumR = Enum.valueOf(forClass, readString);
                        if (!z) {
                            this.handles.setObject(assign, enumR);
                        }
                    } catch (IllegalArgumentException e) {
                        throw ((IOException) new InvalidObjectException("enum constant " + readString + " does not exist in " + forClass).initCause(e));
                    }
                }
                this.handles.finish(assign);
                this.passHandle = assign;
                return enumR;
            }
            throw new InvalidClassException("non-enum class: " + readClassDesc);
        }
        throw new InternalError();
    }

    private Object readOrdinaryObject(boolean z) throws IOException {
        if (this.bin.readByte() == 115) {
            ObjectStreamClass readClassDesc = readClassDesc(false);
            readClassDesc.checkDeserialize();
            Class<?> forClass = readClassDesc.forClass();
            if (forClass == String.class || forClass == Class.class || forClass == ObjectStreamClass.class) {
                throw new InvalidClassException("invalid class descriptor");
            }
            try {
                Object newInstance = readClassDesc.isInstantiable() ? readClassDesc.newInstance() : null;
                this.passHandle = this.handles.assign(z ? unsharedMarker : newInstance);
                ClassNotFoundException resolveException = readClassDesc.getResolveException();
                if (resolveException != null) {
                    this.handles.markException(this.passHandle, resolveException);
                }
                if (readClassDesc.isExternalizable()) {
                    readExternalData((Externalizable) newInstance, readClassDesc);
                } else {
                    readSerialData(newInstance, readClassDesc);
                }
                this.handles.finish(this.passHandle);
                if (newInstance == null || this.handles.lookupException(this.passHandle) != null || !readClassDesc.hasReadResolveMethod()) {
                    return newInstance;
                }
                Object invokeReadResolve = readClassDesc.invokeReadResolve(newInstance);
                if (z && invokeReadResolve.getClass().isArray()) {
                    invokeReadResolve = cloneArray(invokeReadResolve);
                }
                if (invokeReadResolve == newInstance) {
                    return newInstance;
                }
                this.handles.setObject(this.passHandle, invokeReadResolve);
                return invokeReadResolve;
            } catch (Exception e) {
                throw ((IOException) new InvalidClassException(readClassDesc.forClass().getName(), "unable to create instance").initCause(e));
            }
        } else {
            throw new InternalError();
        }
    }

    private void readExternalData(Externalizable externalizable, ObjectStreamClass objectStreamClass) throws IOException {
        boolean hasBlockExternalData;
        SerialCallbackContext serialCallbackContext = this.curContext;
        if (serialCallbackContext != null) {
            serialCallbackContext.check();
        }
        this.curContext = null;
        try {
            hasBlockExternalData = objectStreamClass.hasBlockExternalData();
            if (hasBlockExternalData) {
                this.bin.setBlockDataMode(true);
            }
            if (externalizable != null) {
                externalizable.readExternal(this);
            }
        } catch (ClassNotFoundException e) {
            this.handles.markException(this.passHandle, e);
        } catch (Throwable th) {
            if (serialCallbackContext != null) {
                serialCallbackContext.check();
            }
            this.curContext = serialCallbackContext;
            throw th;
        }
        if (hasBlockExternalData) {
            skipCustomData();
        }
        if (serialCallbackContext != null) {
            serialCallbackContext.check();
        }
        this.curContext = serialCallbackContext;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0042, code lost:
        if (r3 != null) goto L_0x0056;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0054, code lost:
        if (r3 == null) goto L_0x0059;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0056, code lost:
        r3.check();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0059, code lost:
        r7.curContext = r3;
        r7.defaultDataEnd = false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void readSerialData(java.lang.Object r8, java.p026io.ObjectStreamClass r9) throws java.p026io.IOException {
        /*
            r7 = this;
            java.io.ObjectStreamClass$ClassDataSlot[] r9 = r9.getClassDataLayout()
            r0 = 0
            r1 = r0
        L_0x0006:
            int r2 = r9.length
            if (r1 >= r2) goto L_0x009c
            r2 = r9[r1]
            java.io.ObjectStreamClass r2 = r2.desc
            r3 = r9[r1]
            boolean r3 = r3.hasData
            if (r3 == 0) goto L_0x0083
            if (r8 == 0) goto L_0x006f
            java.io.ObjectInputStream$HandleTable r3 = r7.handles
            int r4 = r7.passHandle
            java.lang.ClassNotFoundException r3 = r3.lookupException(r4)
            if (r3 == 0) goto L_0x0020
            goto L_0x006f
        L_0x0020:
            boolean r3 = r2.hasReadObjectMethod()
            if (r3 == 0) goto L_0x006b
            java.io.SerialCallbackContext r3 = r7.curContext
            if (r3 == 0) goto L_0x002d
            r3.check()
        L_0x002d:
            java.io.SerialCallbackContext r4 = new java.io.SerialCallbackContext     // Catch:{ ClassNotFoundException -> 0x0047 }
            r4.<init>(r8, r2)     // Catch:{ ClassNotFoundException -> 0x0047 }
            r7.curContext = r4     // Catch:{ ClassNotFoundException -> 0x0047 }
            java.io.ObjectInputStream$BlockDataInputStream r4 = r7.bin     // Catch:{ ClassNotFoundException -> 0x0047 }
            r5 = 1
            r4.setBlockDataMode(r5)     // Catch:{ ClassNotFoundException -> 0x0047 }
            r2.invokeReadObject(r8, r7)     // Catch:{ ClassNotFoundException -> 0x0047 }
            java.io.SerialCallbackContext r4 = r7.curContext
            r4.setUsed()
            if (r3 == 0) goto L_0x0059
            goto L_0x0056
        L_0x0045:
            r8 = move-exception
            goto L_0x005e
        L_0x0047:
            r4 = move-exception
            java.io.ObjectInputStream$HandleTable r5 = r7.handles     // Catch:{ all -> 0x0045 }
            int r6 = r7.passHandle     // Catch:{ all -> 0x0045 }
            r5.markException(r6, r4)     // Catch:{ all -> 0x0045 }
            java.io.SerialCallbackContext r4 = r7.curContext
            r4.setUsed()
            if (r3 == 0) goto L_0x0059
        L_0x0056:
            r3.check()
        L_0x0059:
            r7.curContext = r3
            r7.defaultDataEnd = r0
            goto L_0x0073
        L_0x005e:
            java.io.SerialCallbackContext r9 = r7.curContext
            r9.setUsed()
            if (r3 == 0) goto L_0x0068
            r3.check()
        L_0x0068:
            r7.curContext = r3
            throw r8
        L_0x006b:
            r7.defaultReadFields(r8, r2)
            goto L_0x0073
        L_0x006f:
            r3 = 0
            r7.defaultReadFields(r3, r2)
        L_0x0073:
            boolean r2 = r2.hasWriteObjectData()
            if (r2 == 0) goto L_0x007d
            r7.skipCustomData()
            goto L_0x0098
        L_0x007d:
            java.io.ObjectInputStream$BlockDataInputStream r2 = r7.bin
            r2.setBlockDataMode(r0)
            goto L_0x0098
        L_0x0083:
            if (r8 == 0) goto L_0x0098
            boolean r3 = r2.hasReadObjectNoDataMethod()
            if (r3 == 0) goto L_0x0098
            java.io.ObjectInputStream$HandleTable r3 = r7.handles
            int r4 = r7.passHandle
            java.lang.ClassNotFoundException r3 = r3.lookupException(r4)
            if (r3 != 0) goto L_0x0098
            r2.invokeReadObjectNoData(r8)
        L_0x0098:
            int r1 = r1 + 1
            goto L_0x0006
        L_0x009c:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.p026io.ObjectInputStream.readSerialData(java.lang.Object, java.io.ObjectStreamClass):void");
    }

    private void skipCustomData() throws IOException {
        int i = this.passHandle;
        while (true) {
            if (this.bin.getBlockDataMode()) {
                this.bin.skipBlockData();
                this.bin.setBlockDataMode(false);
            }
            byte peekByte = this.bin.peekByte();
            if (peekByte != 119) {
                if (peekByte == 120) {
                    this.bin.readByte();
                    this.passHandle = i;
                    return;
                } else if (peekByte != 122) {
                    readObject0(false);
                }
            }
            this.bin.setBlockDataMode(true);
        }
    }

    private void defaultReadFields(Object obj, ObjectStreamClass objectStreamClass) throws IOException {
        Class<?> forClass = objectStreamClass.forClass();
        if (forClass == null || obj == null || forClass.isInstance(obj)) {
            int primDataSize = objectStreamClass.getPrimDataSize();
            byte[] bArr = this.primVals;
            if (bArr == null || bArr.length < primDataSize) {
                this.primVals = new byte[primDataSize];
            }
            this.bin.readFully(this.primVals, 0, primDataSize, false);
            if (obj != null) {
                objectStreamClass.setPrimFieldValues(obj, this.primVals);
            }
            int i = this.passHandle;
            ObjectStreamField[] fields = objectStreamClass.getFields(false);
            int numObjFields = objectStreamClass.getNumObjFields();
            Object[] objArr = new Object[numObjFields];
            int length = fields.length - numObjFields;
            for (int i2 = 0; i2 < numObjFields; i2++) {
                ObjectStreamField objectStreamField = fields[length + i2];
                objArr[i2] = readObject0(objectStreamField.isUnshared());
                if (objectStreamField.getField() != null) {
                    this.handles.markDependency(i, this.passHandle);
                }
            }
            if (obj != null) {
                objectStreamClass.setObjFieldValues(obj, objArr);
            }
            this.passHandle = i;
            return;
        }
        throw new ClassCastException();
    }

    private IOException readFatalException() throws IOException {
        if (this.bin.readByte() == 123) {
            clear();
            IOException iOException = (IOException) readObject0(false);
            clear();
            return iOException;
        }
        throw new InternalError();
    }

    /* access modifiers changed from: private */
    public void handleReset() throws StreamCorruptedException {
        if (this.depth <= 0) {
            clear();
            return;
        }
        throw new StreamCorruptedException("unexpected reset; recursion depth: " + this.depth);
    }

    private static ClassLoader latestUserDefinedLoader() {
        return VMStack.getClosestUserClassLoader();
    }

    /* renamed from: java.io.ObjectInputStream$GetFieldImpl */
    private class GetFieldImpl extends GetField {
        private final ObjectStreamClass desc;
        private final int[] objHandles;
        private final Object[] objVals;
        private final byte[] primVals;

        GetFieldImpl(ObjectStreamClass objectStreamClass) {
            this.desc = objectStreamClass;
            this.primVals = new byte[objectStreamClass.getPrimDataSize()];
            Object[] objArr = new Object[objectStreamClass.getNumObjFields()];
            this.objVals = objArr;
            this.objHandles = new int[objArr.length];
        }

        public ObjectStreamClass getObjectStreamClass() {
            return this.desc;
        }

        public boolean defaulted(String str) throws IOException {
            return getFieldOffset(str, (Class<?>) null) < 0;
        }

        public boolean get(String str, boolean z) throws IOException {
            int fieldOffset = getFieldOffset(str, Boolean.TYPE);
            return fieldOffset >= 0 ? Bits.getBoolean(this.primVals, fieldOffset) : z;
        }

        public byte get(String str, byte b) throws IOException {
            int fieldOffset = getFieldOffset(str, Byte.TYPE);
            return fieldOffset >= 0 ? this.primVals[fieldOffset] : b;
        }

        public char get(String str, char c) throws IOException {
            int fieldOffset = getFieldOffset(str, Character.TYPE);
            return fieldOffset >= 0 ? Bits.getChar(this.primVals, fieldOffset) : c;
        }

        public short get(String str, short s) throws IOException {
            int fieldOffset = getFieldOffset(str, Short.TYPE);
            return fieldOffset >= 0 ? Bits.getShort(this.primVals, fieldOffset) : s;
        }

        public int get(String str, int i) throws IOException {
            int fieldOffset = getFieldOffset(str, Integer.TYPE);
            return fieldOffset >= 0 ? Bits.getInt(this.primVals, fieldOffset) : i;
        }

        public float get(String str, float f) throws IOException {
            int fieldOffset = getFieldOffset(str, Float.TYPE);
            return fieldOffset >= 0 ? Bits.getFloat(this.primVals, fieldOffset) : f;
        }

        public long get(String str, long j) throws IOException {
            int fieldOffset = getFieldOffset(str, Long.TYPE);
            return fieldOffset >= 0 ? Bits.getLong(this.primVals, fieldOffset) : j;
        }

        public double get(String str, double d) throws IOException {
            int fieldOffset = getFieldOffset(str, Double.TYPE);
            return fieldOffset >= 0 ? Bits.getDouble(this.primVals, fieldOffset) : d;
        }

        public Object get(String str, Object obj) throws IOException {
            int fieldOffset = getFieldOffset(str, Object.class);
            if (fieldOffset < 0) {
                return obj;
            }
            int i = this.objHandles[fieldOffset];
            ObjectInputStream.this.handles.markDependency(ObjectInputStream.this.passHandle, i);
            if (ObjectInputStream.this.handles.lookupException(i) == null) {
                return this.objVals[fieldOffset];
            }
            return null;
        }

        /* access modifiers changed from: package-private */
        public void readFields() throws IOException {
            BlockDataInputStream r0 = ObjectInputStream.this.bin;
            byte[] bArr = this.primVals;
            int i = 0;
            r0.readFully(bArr, 0, bArr.length, false);
            int r02 = ObjectInputStream.this.passHandle;
            ObjectStreamField[] fields = this.desc.getFields(false);
            int length = fields.length - this.objVals.length;
            while (true) {
                Object[] objArr = this.objVals;
                if (i < objArr.length) {
                    objArr[i] = ObjectInputStream.this.readObject0(fields[length + i].isUnshared());
                    this.objHandles[i] = ObjectInputStream.this.passHandle;
                    i++;
                } else {
                    ObjectInputStream.this.passHandle = r02;
                    return;
                }
            }
        }

        private int getFieldOffset(String str, Class<?> cls) {
            ObjectStreamField field = this.desc.getField(str, cls);
            if (field != null) {
                return field.getOffset();
            }
            if (this.desc.getLocalDesc().getField(str, cls) != null) {
                return -1;
            }
            throw new IllegalArgumentException("no such field " + str + " with type " + cls);
        }
    }

    /* renamed from: java.io.ObjectInputStream$ValidationList */
    private static class ValidationList {
        /* access modifiers changed from: private */
        public Callback list;

        /* renamed from: java.io.ObjectInputStream$ValidationList$Callback */
        private static class Callback {
            final AccessControlContext acc;
            Callback next;
            final ObjectInputValidation obj;
            final int priority;

            Callback(ObjectInputValidation objectInputValidation, int i, Callback callback, AccessControlContext accessControlContext) {
                this.obj = objectInputValidation;
                this.priority = i;
                this.next = callback;
                this.acc = accessControlContext;
            }
        }

        ValidationList() {
        }

        /* access modifiers changed from: package-private */
        public void register(ObjectInputValidation objectInputValidation, int i) throws InvalidObjectException {
            if (objectInputValidation != null) {
                Callback callback = this.list;
                Callback callback2 = null;
                while (callback != null && i < callback.priority) {
                    callback2 = callback;
                    callback = callback.next;
                }
                AccessControlContext context = AccessController.getContext();
                if (callback2 != null) {
                    callback2.next = new Callback(objectInputValidation, i, callback, context);
                } else {
                    this.list = new Callback(objectInputValidation, i, this.list, context);
                }
            } else {
                throw new InvalidObjectException("null callback");
            }
        }

        /* access modifiers changed from: package-private */
        public void doCallbacks() throws InvalidObjectException {
            while (this.list != null) {
                try {
                    AccessController.doPrivileged(new PrivilegedExceptionAction<Void>() {
                        public Void run() throws InvalidObjectException {
                            ValidationList.this.list.obj.validateObject();
                            return null;
                        }
                    }, this.list.acc);
                    this.list = this.list.next;
                } catch (PrivilegedActionException e) {
                    this.list = null;
                    throw ((InvalidObjectException) e.getException());
                }
            }
        }

        public void clear() {
            this.list = null;
        }
    }

    /* renamed from: java.io.ObjectInputStream$PeekInputStream */
    private static class PeekInputStream extends InputStream {

        /* renamed from: in */
        private final InputStream f525in;
        private int peekb = -1;
        private long totalBytesRead = 0;

        PeekInputStream(InputStream inputStream) {
            this.f525in = inputStream;
        }

        /* access modifiers changed from: package-private */
        public int peek() throws IOException {
            int i = this.peekb;
            if (i >= 0) {
                return i;
            }
            int read = this.f525in.read();
            this.peekb = read;
            this.totalBytesRead += read >= 0 ? 1 : 0;
            return read;
        }

        public int read() throws IOException {
            int i = this.peekb;
            if (i >= 0) {
                this.peekb = -1;
                return i;
            }
            int read = this.f525in.read();
            this.totalBytesRead += read >= 0 ? 1 : 0;
            return read;
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            if (i2 == 0) {
                return 0;
            }
            int i3 = this.peekb;
            long j = 0;
            if (i3 < 0) {
                int read = this.f525in.read(bArr, i, i2);
                long j2 = this.totalBytesRead;
                if (read >= 0) {
                    j = (long) read;
                }
                this.totalBytesRead = j2 + j;
                return read;
            }
            bArr[i] = (byte) i3;
            this.peekb = -1;
            int read2 = this.f525in.read(bArr, i + 1, i2 - 1);
            long j3 = this.totalBytesRead;
            if (read2 >= 0) {
                j = (long) read2;
            }
            this.totalBytesRead = j3 + j;
            if (read2 >= 0) {
                return 1 + read2;
            }
            return 1;
        }

        /* access modifiers changed from: package-private */
        public void readFully(byte[] bArr, int i, int i2) throws IOException {
            int i3 = 0;
            while (i3 < i2) {
                int read = read(bArr, i + i3, i2 - i3);
                if (read >= 0) {
                    i3 += read;
                } else {
                    throw new EOFException();
                }
            }
        }

        public long skip(long j) throws IOException {
            int i;
            if (j <= 0) {
                return 0;
            }
            if (this.peekb >= 0) {
                this.peekb = -1;
                j--;
                i = 1;
            } else {
                i = 0;
            }
            long skip = ((long) i) + this.f525in.skip(j);
            this.totalBytesRead += skip;
            return skip;
        }

        public int available() throws IOException {
            return this.f525in.available() + (this.peekb >= 0 ? 1 : 0);
        }

        public void close() throws IOException {
            this.f525in.close();
        }

        public long getBytesRead() {
            return this.totalBytesRead;
        }
    }

    /* renamed from: java.io.ObjectInputStream$BlockDataInputStream */
    private class BlockDataInputStream extends InputStream implements DataInput {
        private static final int CHAR_BUF_SIZE = 256;
        private static final int HEADER_BLOCKED = -2;
        private static final int MAX_BLOCK_SIZE = 1024;
        private static final int MAX_HEADER_SIZE = 5;
        private boolean blkmode = false;
        private final byte[] buf = new byte[1024];
        private final char[] cbuf = new char[256];
        private final DataInputStream din;
        private int end = -1;
        private final byte[] hbuf = new byte[5];

        /* renamed from: in */
        private final PeekInputStream f524in;
        private int pos = 0;
        private int unread = 0;

        BlockDataInputStream(InputStream inputStream) {
            this.f524in = new PeekInputStream(inputStream);
            this.din = new DataInputStream(this);
        }

        /* access modifiers changed from: package-private */
        public boolean setBlockDataMode(boolean z) throws IOException {
            boolean z2 = this.blkmode;
            if (z2 == z) {
                return z2;
            }
            if (z) {
                this.pos = 0;
                this.end = 0;
                this.unread = 0;
            } else if (this.pos < this.end) {
                throw new IllegalStateException("unread block data");
            }
            this.blkmode = z;
            return !z;
        }

        /* access modifiers changed from: package-private */
        public boolean getBlockDataMode() {
            return this.blkmode;
        }

        /* access modifiers changed from: package-private */
        public void skipBlockData() throws IOException {
            if (this.blkmode) {
                while (this.end >= 0) {
                    refill();
                }
                return;
            }
            throw new IllegalStateException("not in block data mode");
        }

        private int readBlockHeader(boolean z) throws IOException {
            int i;
            if (ObjectInputStream.this.defaultDataEnd) {
                return -1;
            }
            while (true) {
                if (z) {
                    i = Integer.MAX_VALUE;
                } else {
                    try {
                        i = this.f524in.available();
                    } catch (EOFException unused) {
                        throw new StreamCorruptedException("unexpected EOF while reading block data header");
                    }
                }
                if (i == 0) {
                    return -2;
                }
                int peek = this.f524in.peek();
                if (peek != 119) {
                    if (peek == 121) {
                        this.f524in.read();
                        ObjectInputStream.this.handleReset();
                    } else if (peek != 122) {
                        if (peek >= 0) {
                            if (peek < 112 || peek > 126) {
                                throw new StreamCorruptedException(String.format("invalid type code: %02X", Integer.valueOf(peek)));
                            }
                        }
                        return -1;
                    } else if (i < 5) {
                        return -2;
                    } else {
                        this.f524in.readFully(this.hbuf, 0, 5);
                        int i2 = Bits.getInt(this.hbuf, 1);
                        if (i2 >= 0) {
                            return i2;
                        }
                        throw new StreamCorruptedException("illegal block data header length: " + i2);
                    }
                } else if (i < 2) {
                    return -2;
                } else {
                    this.f524in.readFully(this.hbuf, 0, 2);
                    return this.hbuf[1] & 255;
                }
            }
        }

        private void refill() throws IOException {
            do {
                try {
                    this.pos = 0;
                    int i = this.unread;
                    if (i > 0) {
                        int read = this.f524in.read(this.buf, 0, Math.min(i, 1024));
                        if (read >= 0) {
                            this.end = read;
                            this.unread -= read;
                        } else {
                            throw new StreamCorruptedException("unexpected EOF in middle of data block");
                        }
                    } else {
                        int readBlockHeader = readBlockHeader(true);
                        if (readBlockHeader >= 0) {
                            this.end = 0;
                            this.unread = readBlockHeader;
                        } else {
                            this.end = -1;
                            this.unread = 0;
                        }
                    }
                } catch (IOException e) {
                    this.pos = 0;
                    this.end = -1;
                    this.unread = 0;
                    throw e;
                }
            } while (this.pos == this.end);
        }

        /* access modifiers changed from: package-private */
        public int currentBlockRemaining() {
            if (this.blkmode) {
                int i = this.end;
                if (i >= 0) {
                    return (i - this.pos) + this.unread;
                }
                return 0;
            }
            throw new IllegalStateException();
        }

        /* access modifiers changed from: package-private */
        public int peek() throws IOException {
            if (!this.blkmode) {
                return this.f524in.peek();
            }
            if (this.pos == this.end) {
                refill();
            }
            if (this.end >= 0) {
                return this.buf[this.pos] & 255;
            }
            return -1;
        }

        /* access modifiers changed from: package-private */
        public byte peekByte() throws IOException {
            int peek = peek();
            if (peek >= 0) {
                return (byte) peek;
            }
            throw new EOFException();
        }

        public int read() throws IOException {
            if (!this.blkmode) {
                return this.f524in.read();
            }
            if (this.pos == this.end) {
                refill();
            }
            if (this.end < 0) {
                return -1;
            }
            byte[] bArr = this.buf;
            int i = this.pos;
            this.pos = i + 1;
            return bArr[i] & 255;
        }

        public int read(byte[] bArr, int i, int i2) throws IOException {
            return read(bArr, i, i2, false);
        }

        public long skip(long j) throws IOException {
            long j2 = j;
            while (j2 > 0) {
                if (this.blkmode) {
                    if (this.pos == this.end) {
                        refill();
                    }
                    int i = this.end;
                    if (i < 0) {
                        break;
                    }
                    int min = (int) Math.min(j2, (long) (i - this.pos));
                    j2 -= (long) min;
                    this.pos += min;
                } else {
                    int read = this.f524in.read(this.buf, 0, (int) Math.min(j2, 1024));
                    if (read < 0) {
                        break;
                    }
                    j2 -= (long) read;
                }
            }
            return j - j2;
        }

        public int available() throws IOException {
            int readBlockHeader;
            if (!this.blkmode) {
                return this.f524in.available();
            }
            if (this.pos == this.end && this.unread == 0) {
                do {
                    readBlockHeader = readBlockHeader(false);
                } while (readBlockHeader == 0);
                if (readBlockHeader != -2) {
                    if (readBlockHeader != -1) {
                        this.pos = 0;
                        this.end = 0;
                        this.unread = readBlockHeader;
                    } else {
                        this.pos = 0;
                        this.end = -1;
                    }
                }
            }
            int min = this.unread > 0 ? Math.min(this.f524in.available(), this.unread) : 0;
            int i = this.end;
            if (i >= 0) {
                return (i - this.pos) + min;
            }
            return 0;
        }

        public void close() throws IOException {
            if (this.blkmode) {
                this.pos = 0;
                this.end = -1;
                this.unread = 0;
            }
            this.f524in.close();
        }

        /* access modifiers changed from: package-private */
        public int read(byte[] bArr, int i, int i2, boolean z) throws IOException {
            if (i2 == 0) {
                return 0;
            }
            if (this.blkmode) {
                if (this.pos == this.end) {
                    refill();
                }
                int i3 = this.end;
                if (i3 < 0) {
                    return -1;
                }
                int min = Math.min(i2, i3 - this.pos);
                System.arraycopy((Object) this.buf, this.pos, (Object) bArr, i, min);
                this.pos += min;
                return min;
            } else if (!z) {
                return this.f524in.read(bArr, i, i2);
            } else {
                int read = this.f524in.read(this.buf, 0, Math.min(i2, 1024));
                if (read > 0) {
                    System.arraycopy((Object) this.buf, 0, (Object) bArr, i, read);
                }
                return read;
            }
        }

        public void readFully(byte[] bArr) throws IOException {
            readFully(bArr, 0, bArr.length, false);
        }

        public void readFully(byte[] bArr, int i, int i2) throws IOException {
            readFully(bArr, i, i2, false);
        }

        public void readFully(byte[] bArr, int i, int i2, boolean z) throws IOException {
            while (i2 > 0) {
                int read = read(bArr, i, i2, z);
                if (read >= 0) {
                    i += read;
                    i2 -= read;
                } else {
                    throw new EOFException();
                }
            }
        }

        public int skipBytes(int i) throws IOException {
            return this.din.skipBytes(i);
        }

        public boolean readBoolean() throws IOException {
            int read = read();
            if (read >= 0) {
                return read != 0;
            }
            throw new EOFException();
        }

        public byte readByte() throws IOException {
            int read = read();
            if (read >= 0) {
                return (byte) read;
            }
            throw new EOFException();
        }

        public int readUnsignedByte() throws IOException {
            int read = read();
            if (read >= 0) {
                return read;
            }
            throw new EOFException();
        }

        public char readChar() throws IOException {
            if (!this.blkmode) {
                this.pos = 0;
                this.f524in.readFully(this.buf, 0, 2);
            } else if (this.end - this.pos < 2) {
                return this.din.readChar();
            }
            char c = Bits.getChar(this.buf, this.pos);
            this.pos += 2;
            return c;
        }

        public short readShort() throws IOException {
            if (!this.blkmode) {
                this.pos = 0;
                this.f524in.readFully(this.buf, 0, 2);
            } else if (this.end - this.pos < 2) {
                return this.din.readShort();
            }
            short s = Bits.getShort(this.buf, this.pos);
            this.pos += 2;
            return s;
        }

        public int readUnsignedShort() throws IOException {
            if (!this.blkmode) {
                this.pos = 0;
                this.f524in.readFully(this.buf, 0, 2);
            } else if (this.end - this.pos < 2) {
                return this.din.readUnsignedShort();
            }
            short s = Bits.getShort(this.buf, this.pos) & UShort.MAX_VALUE;
            this.pos += 2;
            return s;
        }

        public int readInt() throws IOException {
            if (!this.blkmode) {
                this.pos = 0;
                this.f524in.readFully(this.buf, 0, 4);
            } else if (this.end - this.pos < 4) {
                return this.din.readInt();
            }
            int i = Bits.getInt(this.buf, this.pos);
            this.pos += 4;
            return i;
        }

        public float readFloat() throws IOException {
            if (!this.blkmode) {
                this.pos = 0;
                this.f524in.readFully(this.buf, 0, 4);
            } else if (this.end - this.pos < 4) {
                return this.din.readFloat();
            }
            float f = Bits.getFloat(this.buf, this.pos);
            this.pos += 4;
            return f;
        }

        public long readLong() throws IOException {
            if (!this.blkmode) {
                this.pos = 0;
                this.f524in.readFully(this.buf, 0, 8);
            } else if (this.end - this.pos < 8) {
                return this.din.readLong();
            }
            long j = Bits.getLong(this.buf, this.pos);
            this.pos += 8;
            return j;
        }

        public double readDouble() throws IOException {
            if (!this.blkmode) {
                this.pos = 0;
                this.f524in.readFully(this.buf, 0, 8);
            } else if (this.end - this.pos < 8) {
                return this.din.readDouble();
            }
            double d = Bits.getDouble(this.buf, this.pos);
            this.pos += 8;
            return d;
        }

        public String readUTF() throws IOException {
            return readUTFBody((long) readUnsignedShort());
        }

        public String readLine() throws IOException {
            return this.din.readLine();
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:10:0x0038 A[LOOP:1: B:9:0x0036->B:10:0x0038, LOOP_END] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void readBooleans(boolean[] r6, int r7, int r8) throws java.p026io.IOException {
            /*
                r5 = this;
                int r8 = r8 + r7
            L_0x0001:
                if (r7 >= r8) goto L_0x004a
                boolean r0 = r5.blkmode
                if (r0 != 0) goto L_0x001b
                int r0 = r8 - r7
                r1 = 1024(0x400, float:1.435E-42)
                int r0 = java.lang.Math.min((int) r0, (int) r1)
                java.io.ObjectInputStream$PeekInputStream r1 = r5.f524in
                byte[] r2 = r5.buf
                r3 = 0
                r1.readFully(r2, r3, r0)
                int r0 = r0 + r7
                r5.pos = r3
                goto L_0x0036
            L_0x001b:
                int r0 = r5.end
                int r1 = r5.pos
                int r2 = r0 - r1
                r3 = 1
                if (r2 >= r3) goto L_0x0030
                int r0 = r7 + 1
                java.io.DataInputStream r1 = r5.din
                boolean r1 = r1.readBoolean()
                r6[r7] = r1
                r7 = r0
                goto L_0x0001
            L_0x0030:
                int r0 = r0 + r7
                int r0 = r0 - r1
                int r0 = java.lang.Math.min((int) r8, (int) r0)
            L_0x0036:
                if (r7 >= r0) goto L_0x0001
                int r1 = r7 + 1
                byte[] r2 = r5.buf
                int r3 = r5.pos
                int r4 = r3 + 1
                r5.pos = r4
                boolean r2 = java.p026io.Bits.getBoolean(r2, r3)
                r6[r7] = r2
                r7 = r1
                goto L_0x0036
            L_0x004a:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.p026io.ObjectInputStream.BlockDataInputStream.readBooleans(boolean[], int, int):void");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:10:0x003c A[LOOP:1: B:9:0x003a->B:10:0x003c, LOOP_END] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void readChars(char[] r7, int r8, int r9) throws java.p026io.IOException {
            /*
                r6 = this;
                int r9 = r9 + r8
            L_0x0001:
                if (r8 >= r9) goto L_0x004f
                boolean r0 = r6.blkmode
                r1 = 2
                if (r0 != 0) goto L_0x001e
                int r0 = r9 - r8
                r2 = 512(0x200, float:7.175E-43)
                int r0 = java.lang.Math.min((int) r0, (int) r2)
                java.io.ObjectInputStream$PeekInputStream r2 = r6.f524in
                byte[] r3 = r6.buf
                int r4 = r0 << 1
                r5 = 0
                r2.readFully(r3, r5, r4)
                int r0 = r0 + r8
                r6.pos = r5
                goto L_0x003a
            L_0x001e:
                int r0 = r6.end
                int r2 = r6.pos
                int r3 = r0 - r2
                if (r3 >= r1) goto L_0x0032
                int r0 = r8 + 1
                java.io.DataInputStream r1 = r6.din
                char r1 = r1.readChar()
                r7[r8] = r1
                r8 = r0
                goto L_0x0001
            L_0x0032:
                int r0 = r0 - r2
                int r0 = r0 >> 1
                int r0 = r0 + r8
                int r0 = java.lang.Math.min((int) r9, (int) r0)
            L_0x003a:
                if (r8 >= r0) goto L_0x0001
                int r2 = r8 + 1
                byte[] r3 = r6.buf
                int r4 = r6.pos
                char r3 = java.p026io.Bits.getChar(r3, r4)
                r7[r8] = r3
                int r8 = r6.pos
                int r8 = r8 + r1
                r6.pos = r8
                r8 = r2
                goto L_0x003a
            L_0x004f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.p026io.ObjectInputStream.BlockDataInputStream.readChars(char[], int, int):void");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:10:0x003c A[LOOP:1: B:9:0x003a->B:10:0x003c, LOOP_END] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void readShorts(short[] r7, int r8, int r9) throws java.p026io.IOException {
            /*
                r6 = this;
                int r9 = r9 + r8
            L_0x0001:
                if (r8 >= r9) goto L_0x004f
                boolean r0 = r6.blkmode
                r1 = 2
                if (r0 != 0) goto L_0x001e
                int r0 = r9 - r8
                r2 = 512(0x200, float:7.175E-43)
                int r0 = java.lang.Math.min((int) r0, (int) r2)
                java.io.ObjectInputStream$PeekInputStream r2 = r6.f524in
                byte[] r3 = r6.buf
                int r4 = r0 << 1
                r5 = 0
                r2.readFully(r3, r5, r4)
                int r0 = r0 + r8
                r6.pos = r5
                goto L_0x003a
            L_0x001e:
                int r0 = r6.end
                int r2 = r6.pos
                int r3 = r0 - r2
                if (r3 >= r1) goto L_0x0032
                int r0 = r8 + 1
                java.io.DataInputStream r1 = r6.din
                short r1 = r1.readShort()
                r7[r8] = r1
                r8 = r0
                goto L_0x0001
            L_0x0032:
                int r0 = r0 - r2
                int r0 = r0 >> 1
                int r0 = r0 + r8
                int r0 = java.lang.Math.min((int) r9, (int) r0)
            L_0x003a:
                if (r8 >= r0) goto L_0x0001
                int r2 = r8 + 1
                byte[] r3 = r6.buf
                int r4 = r6.pos
                short r3 = java.p026io.Bits.getShort(r3, r4)
                r7[r8] = r3
                int r8 = r6.pos
                int r8 = r8 + r1
                r6.pos = r8
                r8 = r2
                goto L_0x003a
            L_0x004f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.p026io.ObjectInputStream.BlockDataInputStream.readShorts(short[], int, int):void");
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:10:0x003c A[LOOP:1: B:9:0x003a->B:10:0x003c, LOOP_END] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void readInts(int[] r7, int r8, int r9) throws java.p026io.IOException {
            /*
                r6 = this;
                int r9 = r9 + r8
            L_0x0001:
                if (r8 >= r9) goto L_0x004f
                boolean r0 = r6.blkmode
                r1 = 4
                if (r0 != 0) goto L_0x001e
                int r0 = r9 - r8
                r2 = 256(0x100, float:3.59E-43)
                int r0 = java.lang.Math.min((int) r0, (int) r2)
                java.io.ObjectInputStream$PeekInputStream r2 = r6.f524in
                byte[] r3 = r6.buf
                int r4 = r0 << 2
                r5 = 0
                r2.readFully(r3, r5, r4)
                int r0 = r0 + r8
                r6.pos = r5
                goto L_0x003a
            L_0x001e:
                int r0 = r6.end
                int r2 = r6.pos
                int r3 = r0 - r2
                if (r3 >= r1) goto L_0x0032
                int r0 = r8 + 1
                java.io.DataInputStream r1 = r6.din
                int r1 = r1.readInt()
                r7[r8] = r1
                r8 = r0
                goto L_0x0001
            L_0x0032:
                int r0 = r0 - r2
                int r0 = r0 >> 2
                int r0 = r0 + r8
                int r0 = java.lang.Math.min((int) r9, (int) r0)
            L_0x003a:
                if (r8 >= r0) goto L_0x0001
                int r2 = r8 + 1
                byte[] r3 = r6.buf
                int r4 = r6.pos
                int r3 = java.p026io.Bits.getInt(r3, r4)
                r7[r8] = r3
                int r8 = r6.pos
                int r8 = r8 + r1
                r6.pos = r8
                r8 = r2
                goto L_0x003a
            L_0x004f:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.p026io.ObjectInputStream.BlockDataInputStream.readInts(int[], int, int):void");
        }

        /* access modifiers changed from: package-private */
        public void readFloats(float[] fArr, int i, int i2) throws IOException {
            int i3;
            int i4 = i2 + i;
            while (i < i4) {
                if (!this.blkmode) {
                    i3 = Math.min(i4 - i, 256);
                    this.f524in.readFully(this.buf, 0, i3 << 2);
                    this.pos = 0;
                } else {
                    int i5 = this.end;
                    int i6 = this.pos;
                    if (i5 - i6 < 4) {
                        fArr[i] = this.din.readFloat();
                        i++;
                    } else {
                        i3 = Math.min(i4 - i, (i5 - i6) >> 2);
                    }
                }
                ObjectInputStream.bytesToFloats(this.buf, this.pos, fArr, i, i3);
                i += i3;
                this.pos += i3 << 2;
            }
        }

        /* access modifiers changed from: package-private */
        /* JADX WARNING: Removed duplicated region for block: B:10:0x003d A[LOOP:1: B:9:0x003b->B:10:0x003d, LOOP_END] */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void readLongs(long[] r7, int r8, int r9) throws java.p026io.IOException {
            /*
                r6 = this;
                int r9 = r9 + r8
            L_0x0001:
                if (r8 >= r9) goto L_0x0050
                boolean r0 = r6.blkmode
                r1 = 8
                if (r0 != 0) goto L_0x001f
                int r0 = r9 - r8
                r2 = 128(0x80, float:1.794E-43)
                int r0 = java.lang.Math.min((int) r0, (int) r2)
                java.io.ObjectInputStream$PeekInputStream r2 = r6.f524in
                byte[] r3 = r6.buf
                int r4 = r0 << 3
                r5 = 0
                r2.readFully(r3, r5, r4)
                int r0 = r0 + r8
                r6.pos = r5
                goto L_0x003b
            L_0x001f:
                int r0 = r6.end
                int r2 = r6.pos
                int r3 = r0 - r2
                if (r3 >= r1) goto L_0x0033
                int r0 = r8 + 1
                java.io.DataInputStream r1 = r6.din
                long r1 = r1.readLong()
                r7[r8] = r1
                r8 = r0
                goto L_0x0001
            L_0x0033:
                int r0 = r0 - r2
                int r0 = r0 >> 3
                int r0 = r0 + r8
                int r0 = java.lang.Math.min((int) r9, (int) r0)
            L_0x003b:
                if (r8 >= r0) goto L_0x0001
                int r2 = r8 + 1
                byte[] r3 = r6.buf
                int r4 = r6.pos
                long r3 = java.p026io.Bits.getLong(r3, r4)
                r7[r8] = r3
                int r8 = r6.pos
                int r8 = r8 + r1
                r6.pos = r8
                r8 = r2
                goto L_0x003b
            L_0x0050:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: java.p026io.ObjectInputStream.BlockDataInputStream.readLongs(long[], int, int):void");
        }

        /* access modifiers changed from: package-private */
        public void readDoubles(double[] dArr, int i, int i2) throws IOException {
            int i3;
            int i4 = i2 + i;
            while (i < i4) {
                if (!this.blkmode) {
                    i3 = Math.min(i4 - i, 128);
                    this.f524in.readFully(this.buf, 0, i3 << 3);
                    this.pos = 0;
                } else {
                    int i5 = this.end;
                    int i6 = this.pos;
                    if (i5 - i6 < 8) {
                        dArr[i] = this.din.readDouble();
                        i++;
                    } else {
                        i3 = Math.min(i4 - i, (i5 - i6) >> 3);
                    }
                }
                ObjectInputStream.bytesToDoubles(this.buf, this.pos, dArr, i, i3);
                i += i3;
                this.pos += i3 << 3;
            }
        }

        /* access modifiers changed from: package-private */
        public String readLongUTF() throws IOException {
            return readUTFBody(readLong());
        }

        private String readUTFBody(long j) throws IOException {
            long j2;
            StringBuilder sb = new StringBuilder();
            if (!this.blkmode) {
                this.pos = 0;
                this.end = 0;
            }
            while (j > 0) {
                int i = this.end;
                int i2 = this.pos;
                int i3 = i - i2;
                if (i3 >= 3 || ((long) i3) == j) {
                    j2 = readUTFSpan(sb, j);
                } else if (this.blkmode) {
                    j2 = (long) readUTFChar(sb, j);
                } else {
                    if (i3 > 0) {
                        byte[] bArr = this.buf;
                        System.arraycopy((Object) bArr, i2, (Object) bArr, 0, i3);
                    }
                    this.pos = 0;
                    int min = (int) Math.min(1024, j);
                    this.end = min;
                    this.f524in.readFully(this.buf, i3, min - i3);
                }
                j -= j2;
            }
            return sb.toString();
        }

        private long readUTFSpan(StringBuilder sb, long j) throws IOException {
            int i = this.pos;
            int min = Math.min(this.end - i, 256);
            int i2 = this.pos + (j > ((long) min) ? min - 2 : (int) j);
            int i3 = 0;
            while (true) {
                try {
                    int i4 = this.pos;
                    if (i4 < i2) {
                        byte[] bArr = this.buf;
                        int i5 = i4 + 1;
                        this.pos = i5;
                        byte b = bArr[i4] & 255;
                        switch (b >> 4) {
                            case 0:
                            case 1:
                            case 2:
                            case 3:
                            case 4:
                            case 5:
                            case 6:
                            case 7:
                                this.cbuf[i3] = (char) b;
                                i3++;
                                break;
                            case 12:
                            case 13:
                                this.pos = i5 + 1;
                                byte b2 = bArr[i5];
                                if ((b2 & DerValue.TAG_PRIVATE) == 128) {
                                    this.cbuf[i3] = (char) (((b & 31) << 6) | ((b2 & 63) << 0));
                                    i3++;
                                    break;
                                } else {
                                    throw new UTFDataFormatException();
                                }
                            case 14:
                                byte b3 = bArr[i5 + 1];
                                byte b4 = bArr[i5 + 0];
                                this.pos = i5 + 2;
                                if ((b4 & DerValue.TAG_PRIVATE) != 128 || (b3 & DerValue.TAG_PRIVATE) != 128) {
                                    break;
                                } else {
                                    this.cbuf[i3] = (char) (((b & 15) << 12) | ((b4 & 63) << 6) | ((b3 & 63) << 0));
                                    i3++;
                                    break;
                                }
                            default:
                                throw new UTFDataFormatException();
                        }
                    } else if (((long) (i4 - i)) <= j) {
                        sb.append(this.cbuf, 0, i3);
                        return (long) (this.pos - i);
                    } else {
                        this.pos = i + ((int) j);
                        throw new UTFDataFormatException();
                    }
                } catch (ArrayIndexOutOfBoundsException unused) {
                    this.pos = i + ((int) j);
                    throw new UTFDataFormatException();
                } catch (Throwable th) {
                    if (((long) (this.pos - i)) > j) {
                        this.pos = i + ((int) j);
                        throw new UTFDataFormatException();
                    }
                    throw th;
                }
            }
            throw new UTFDataFormatException();
        }

        private int readUTFChar(StringBuilder sb, long j) throws IOException {
            byte readByte = readByte() & 255;
            switch (readByte >> 4) {
                case 0:
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                    sb.append((char) readByte);
                    return 1;
                case 12:
                case 13:
                    if (j >= 2) {
                        byte readByte2 = readByte();
                        if ((readByte2 & DerValue.TAG_PRIVATE) == 128) {
                            sb.append((char) (((readByte2 & 63) << 0) | ((readByte & 31) << 6)));
                            return 2;
                        }
                        throw new UTFDataFormatException();
                    }
                    throw new UTFDataFormatException();
                case 14:
                    if (j < 3) {
                        if (j == 2) {
                            readByte();
                        }
                        throw new UTFDataFormatException();
                    }
                    byte readByte3 = readByte();
                    byte readByte4 = readByte();
                    if ((readByte3 & DerValue.TAG_PRIVATE) == 128 && (readByte4 & DerValue.TAG_PRIVATE) == 128) {
                        int i = (readByte4 & 63) << 0;
                        sb.append((char) (i | ((readByte3 & 63) << 6) | ((readByte & 15) << 12)));
                        return 3;
                    }
                    throw new UTFDataFormatException();
                default:
                    throw new UTFDataFormatException();
            }
        }

        /* access modifiers changed from: package-private */
        public long getBytesRead() {
            return this.f524in.getBytesRead();
        }
    }

    /* renamed from: java.io.ObjectInputStream$HandleTable */
    private static class HandleTable {
        private static final byte STATUS_EXCEPTION = 3;
        private static final byte STATUS_OK = 1;
        private static final byte STATUS_UNKNOWN = 2;
        HandleList[] deps;
        Object[] entries;
        int lowDep = -1;
        int size = 0;
        byte[] status;

        HandleTable(int i) {
            this.status = new byte[i];
            this.entries = new Object[i];
            this.deps = new HandleList[i];
        }

        /* access modifiers changed from: package-private */
        public int assign(Object obj) {
            if (this.size >= this.entries.length) {
                grow();
            }
            byte[] bArr = this.status;
            int i = this.size;
            bArr[i] = 2;
            this.entries[i] = obj;
            this.size = i + 1;
            return i;
        }

        /* access modifiers changed from: package-private */
        public void markDependency(int i, int i2) {
            if (i != -1 && i2 != -1) {
                byte[] bArr = this.status;
                byte b = bArr[i];
                if (b == 2) {
                    byte b2 = bArr[i2];
                    if (b2 == 1) {
                        return;
                    }
                    if (b2 == 2) {
                        HandleList[] handleListArr = this.deps;
                        if (handleListArr[i2] == null) {
                            handleListArr[i2] = new HandleList();
                        }
                        this.deps[i2].add(i);
                        int i3 = this.lowDep;
                        if (i3 < 0 || i3 > i2) {
                            this.lowDep = i2;
                        }
                    } else if (b2 == 3) {
                        markException(i, (ClassNotFoundException) this.entries[i2]);
                    } else {
                        throw new InternalError();
                    }
                } else if (b != 3) {
                    throw new InternalError();
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void markException(int i, ClassNotFoundException classNotFoundException) {
            byte[] bArr = this.status;
            byte b = bArr[i];
            if (b == 2) {
                bArr[i] = 3;
                this.entries[i] = classNotFoundException;
                HandleList handleList = this.deps[i];
                if (handleList != null) {
                    int size2 = handleList.size();
                    for (int i2 = 0; i2 < size2; i2++) {
                        markException(handleList.get(i2), classNotFoundException);
                    }
                    this.deps[i] = null;
                }
            } else if (b != 3) {
                throw new InternalError();
            }
        }

        /* access modifiers changed from: package-private */
        public void finish(int i) {
            int i2;
            int i3 = this.lowDep;
            if (i3 < 0) {
                i2 = i + 1;
            } else if (i3 >= i) {
                i2 = this.size;
                this.lowDep = -1;
            } else {
                return;
            }
            while (i < i2) {
                byte[] bArr = this.status;
                byte b = bArr[i];
                if (b != 1) {
                    if (b == 2) {
                        bArr[i] = 1;
                        this.deps[i] = null;
                    } else if (b != 3) {
                        throw new InternalError();
                    }
                }
                i++;
            }
        }

        /* access modifiers changed from: package-private */
        public void setObject(int i, Object obj) {
            byte b = this.status[i];
            if (b == 1 || b == 2) {
                this.entries[i] = obj;
            } else if (b != 3) {
                throw new InternalError();
            }
        }

        /* access modifiers changed from: package-private */
        public Object lookupObject(int i) {
            if (i == -1 || this.status[i] == 3) {
                return null;
            }
            return this.entries[i];
        }

        /* access modifiers changed from: package-private */
        public ClassNotFoundException lookupException(int i) {
            if (i == -1 || this.status[i] != 3) {
                return null;
            }
            return (ClassNotFoundException) this.entries[i];
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            Arrays.fill(this.status, 0, this.size, (byte) 0);
            Arrays.fill(this.entries, 0, this.size, (Object) null);
            Arrays.fill((Object[]) this.deps, 0, this.size, (Object) null);
            this.lowDep = -1;
            this.size = 0;
        }

        /* access modifiers changed from: package-private */
        public int size() {
            return this.size;
        }

        private void grow() {
            int length = (this.entries.length << 1) + 1;
            byte[] bArr = new byte[length];
            Object[] objArr = new Object[length];
            HandleList[] handleListArr = new HandleList[length];
            System.arraycopy((Object) this.status, 0, (Object) bArr, 0, this.size);
            System.arraycopy((Object) this.entries, 0, (Object) objArr, 0, this.size);
            System.arraycopy((Object) this.deps, 0, (Object) handleListArr, 0, this.size);
            this.status = bArr;
            this.entries = objArr;
            this.deps = handleListArr;
        }

        /* renamed from: java.io.ObjectInputStream$HandleTable$HandleList */
        private static class HandleList {
            private int[] list = new int[4];
            private int size = 0;

            public void add(int i) {
                int i2 = this.size;
                int[] iArr = this.list;
                if (i2 >= iArr.length) {
                    int[] iArr2 = new int[(iArr.length << 1)];
                    System.arraycopy((Object) iArr, 0, (Object) iArr2, 0, iArr.length);
                    this.list = iArr2;
                }
                int[] iArr3 = this.list;
                int i3 = this.size;
                this.size = i3 + 1;
                iArr3[i3] = i;
            }

            public int get(int i) {
                if (i < this.size) {
                    return this.list[i];
                }
                throw new ArrayIndexOutOfBoundsException();
            }

            public int size() {
                return this.size;
            }
        }
    }

    private static Object cloneArray(Object obj) {
        if (obj instanceof Object[]) {
            return ((Object[]) obj).clone();
        }
        if (obj instanceof boolean[]) {
            return ((boolean[]) obj).clone();
        }
        if (obj instanceof byte[]) {
            return ((byte[]) obj).clone();
        }
        if (obj instanceof char[]) {
            return ((char[]) obj).clone();
        }
        if (obj instanceof double[]) {
            return ((double[]) obj).clone();
        }
        if (obj instanceof float[]) {
            return ((float[]) obj).clone();
        }
        if (obj instanceof int[]) {
            return ((int[]) obj).clone();
        }
        if (obj instanceof long[]) {
            return ((long[]) obj).clone();
        }
        if (obj instanceof short[]) {
            return ((short[]) obj).clone();
        }
        throw new AssertionError();
    }
}
