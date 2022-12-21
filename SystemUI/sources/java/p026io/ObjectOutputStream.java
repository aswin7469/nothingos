package java.p026io;

import java.lang.ref.ReferenceQueue;
import java.p026io.ObjectStreamClass;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import sun.reflect.misc.ReflectUtil;

/* renamed from: java.io.ObjectOutputStream */
public class ObjectOutputStream extends OutputStream implements ObjectOutput, ObjectStreamConstants {
    private static final boolean extendedDebugInfo = false;
    /* access modifiers changed from: private */
    public final BlockDataOutputStream bout;
    private SerialCallbackContext curContext;
    private PutFieldImpl curPut;
    private final DebugTraceInfoStack debugInfoStack;
    private int depth;
    private final boolean enableOverride;
    private boolean enableReplace;
    private final HandleTable handles;
    private byte[] primVals;
    private int protocol = 2;
    private final ReplaceTable subs;

    /* renamed from: java.io.ObjectOutputStream$PutField */
    public static abstract class PutField {
        public abstract void put(String str, byte b);

        public abstract void put(String str, char c);

        public abstract void put(String str, double d);

        public abstract void put(String str, float f);

        public abstract void put(String str, int i);

        public abstract void put(String str, long j);

        public abstract void put(String str, Object obj);

        public abstract void put(String str, short s);

        public abstract void put(String str, boolean z);

        @Deprecated
        public abstract void write(ObjectOutput objectOutput) throws IOException;
    }

    /* access modifiers changed from: private */
    public static native void doublesToBytes(double[] dArr, int i, byte[] bArr, int i2, int i3);

    /* access modifiers changed from: private */
    public static native void floatsToBytes(float[] fArr, int i, byte[] bArr, int i2, int i3);

    /* access modifiers changed from: protected */
    public void annotateClass(Class<?> cls) throws IOException {
    }

    /* access modifiers changed from: protected */
    public void annotateProxyClass(Class<?> cls) throws IOException {
    }

    /* access modifiers changed from: protected */
    public Object replaceObject(Object obj) throws IOException {
        return obj;
    }

    /* renamed from: java.io.ObjectOutputStream$Caches */
    private static class Caches {
        static final ConcurrentMap<ObjectStreamClass.WeakClassKey, Boolean> subclassAudits = new ConcurrentHashMap();
        static final ReferenceQueue<Class<?>> subclassAuditsQueue = new ReferenceQueue<>();

        private Caches() {
        }
    }

    public ObjectOutputStream(OutputStream outputStream) throws IOException {
        verifySubclass();
        BlockDataOutputStream blockDataOutputStream = new BlockDataOutputStream(outputStream);
        this.bout = blockDataOutputStream;
        this.handles = new HandleTable(10, 3.0f);
        this.subs = new ReplaceTable(10, 3.0f);
        this.enableOverride = false;
        writeStreamHeader();
        blockDataOutputStream.setBlockDataMode(true);
        this.debugInfoStack = null;
    }

    protected ObjectOutputStream() throws IOException, SecurityException {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SUBCLASS_IMPLEMENTATION_PERMISSION);
        }
        this.bout = null;
        this.handles = null;
        this.subs = null;
        this.enableOverride = true;
        this.debugInfoStack = null;
    }

    public void useProtocolVersion(int i) throws IOException {
        if (this.handles.size() != 0) {
            throw new IllegalStateException("stream non-empty");
        } else if (i == 1 || i == 2) {
            this.protocol = i;
        } else {
            throw new IllegalArgumentException("unknown version: " + i);
        }
    }

    public final void writeObject(Object obj) throws IOException {
        if (this.enableOverride) {
            writeObjectOverride(obj);
            return;
        }
        try {
            writeObject0(obj, false);
        } catch (IOException e) {
            if (this.depth == 0) {
                try {
                    writeFatalException(e);
                } catch (IOException unused) {
                }
            }
            throw e;
        }
    }

    /* access modifiers changed from: protected */
    public void writeObjectOverride(Object obj) throws IOException {
        if (!this.enableOverride) {
            throw new IOException();
        }
    }

    public void writeUnshared(Object obj) throws IOException {
        try {
            writeObject0(obj, true);
        } catch (IOException e) {
            if (this.depth == 0) {
                writeFatalException(e);
            }
            throw e;
        }
    }

    public void defaultWriteObject() throws IOException {
        SerialCallbackContext serialCallbackContext = this.curContext;
        if (serialCallbackContext != null) {
            Object obj = serialCallbackContext.getObj();
            ObjectStreamClass desc = serialCallbackContext.getDesc();
            this.bout.setBlockDataMode(false);
            defaultWriteFields(obj, desc);
            this.bout.setBlockDataMode(true);
            return;
        }
        throw new NotActiveException("not in call to writeObject");
    }

    public PutField putFields() throws IOException {
        if (this.curPut == null) {
            SerialCallbackContext serialCallbackContext = this.curContext;
            if (serialCallbackContext != null) {
                serialCallbackContext.getObj();
                this.curPut = new PutFieldImpl(serialCallbackContext.getDesc());
            } else {
                throw new NotActiveException("not in call to writeObject");
            }
        }
        return this.curPut;
    }

    public void writeFields() throws IOException {
        if (this.curPut != null) {
            this.bout.setBlockDataMode(false);
            this.curPut.writeFields();
            this.bout.setBlockDataMode(true);
            return;
        }
        throw new NotActiveException("no current PutField object");
    }

    public void reset() throws IOException {
        if (this.depth == 0) {
            this.bout.setBlockDataMode(false);
            this.bout.writeByte(121);
            clear();
            this.bout.setBlockDataMode(true);
            return;
        }
        throw new IOException("stream active");
    }

    /* access modifiers changed from: protected */
    public boolean enableReplaceObject(boolean z) throws SecurityException {
        SecurityManager securityManager;
        if (z == this.enableReplace) {
            return z;
        }
        if (z && (securityManager = System.getSecurityManager()) != null) {
            securityManager.checkPermission(SUBSTITUTION_PERMISSION);
        }
        this.enableReplace = z;
        return !z;
    }

    /* access modifiers changed from: protected */
    public void writeStreamHeader() throws IOException {
        this.bout.writeShort(-21267);
        this.bout.writeShort(5);
    }

    /* access modifiers changed from: protected */
    public void writeClassDescriptor(ObjectStreamClass objectStreamClass) throws IOException {
        objectStreamClass.writeNonProxy(this);
    }

    public void write(int i) throws IOException {
        this.bout.write(i);
    }

    public void write(byte[] bArr) throws IOException {
        this.bout.write(bArr, 0, bArr.length, false);
    }

    public void write(byte[] bArr, int i, int i2) throws IOException {
        bArr.getClass();
        int i3 = i + i2;
        if (i < 0 || i2 < 0 || i3 > bArr.length || i3 < 0) {
            throw new IndexOutOfBoundsException();
        }
        this.bout.write(bArr, i, i2, false);
    }

    public void flush() throws IOException {
        this.bout.flush();
    }

    /* access modifiers changed from: protected */
    public void drain() throws IOException {
        this.bout.drain();
    }

    public void close() throws IOException {
        flush();
        this.bout.close();
    }

    public void writeBoolean(boolean z) throws IOException {
        this.bout.writeBoolean(z);
    }

    public void writeByte(int i) throws IOException {
        this.bout.writeByte(i);
    }

    public void writeShort(int i) throws IOException {
        this.bout.writeShort(i);
    }

    public void writeChar(int i) throws IOException {
        this.bout.writeChar(i);
    }

    public void writeInt(int i) throws IOException {
        this.bout.writeInt(i);
    }

    public void writeLong(long j) throws IOException {
        this.bout.writeLong(j);
    }

    public void writeFloat(float f) throws IOException {
        this.bout.writeFloat(f);
    }

    public void writeDouble(double d) throws IOException {
        this.bout.writeDouble(d);
    }

    public void writeBytes(String str) throws IOException {
        this.bout.writeBytes(str);
    }

    public void writeChars(String str) throws IOException {
        this.bout.writeChars(str);
    }

    public void writeUTF(String str) throws IOException {
        this.bout.writeUTF(str);
    }

    /* access modifiers changed from: package-private */
    public int getProtocolVersion() {
        return this.protocol;
    }

    /* access modifiers changed from: package-private */
    public void writeTypeString(String str) throws IOException {
        if (str == null) {
            writeNull();
            return;
        }
        int lookup = this.handles.lookup(str);
        if (lookup != -1) {
            writeHandle(lookup);
        } else {
            writeString(str, false);
        }
    }

    private void verifySubclass() {
        SecurityManager securityManager;
        Class<?> cls = getClass();
        if (cls != ObjectOutputStream.class && (securityManager = System.getSecurityManager()) != null) {
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
                Class<? super ObjectOutputStream> cls = Class.this;
                while (cls != ObjectOutputStream.class) {
                    try {
                        cls.getDeclaredMethod("writeUnshared", Object.class);
                        return Boolean.FALSE;
                    } catch (NoSuchMethodException unused) {
                        try {
                            Class[] clsArr = null;
                            cls.getDeclaredMethod("putFields", (Class<?>[]) null);
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
        this.subs.clear();
        this.handles.clear();
    }

    /* access modifiers changed from: private */
    public void writeObject0(Object obj, boolean z) throws IOException {
        Object obj2;
        int lookup;
        Class<?> cls;
        boolean blockDataMode = this.bout.setBlockDataMode(false);
        this.depth++;
        try {
            Object lookup2 = this.subs.lookup(obj);
            if (lookup2 == null) {
                writeNull();
            } else {
                if (!z) {
                    int lookup3 = this.handles.lookup(lookup2);
                    if (lookup3 != -1) {
                        writeHandle(lookup3);
                    }
                }
                Class<?> cls2 = lookup2.getClass();
                ObjectStreamClass lookup4 = ObjectStreamClass.lookup(cls2, true);
                if (lookup4.hasWriteReplaceMethod()) {
                    obj2 = lookup4.invokeWriteReplace(lookup2);
                    if (!(obj2 == null || (cls = obj2.getClass()) == cls2)) {
                        lookup4 = ObjectStreamClass.lookup(cls, true);
                        cls2 = cls;
                    }
                } else {
                    obj2 = lookup2;
                }
                if (this.enableReplace) {
                    Object replaceObject = replaceObject(obj2);
                    if (!(replaceObject == obj2 || replaceObject == null)) {
                        cls2 = replaceObject.getClass();
                        lookup4 = ObjectStreamClass.lookup(cls2, true);
                    }
                    obj2 = replaceObject;
                }
                if (obj2 != lookup2) {
                    this.subs.assign(lookup2, obj2);
                    if (obj2 == null) {
                        writeNull();
                    } else if (!z && (lookup = this.handles.lookup(obj2)) != -1) {
                        writeHandle(lookup);
                    }
                }
                if (obj2 instanceof Class) {
                    writeClass((Class) obj2, z);
                } else if (obj2 instanceof ObjectStreamClass) {
                    writeClassDesc((ObjectStreamClass) obj2, z);
                } else if (obj2 instanceof String) {
                    writeString((String) obj2, z);
                } else if (cls2.isArray()) {
                    writeArray(obj2, lookup4, z);
                } else if (obj2 instanceof Enum) {
                    writeEnum((Enum) obj2, lookup4, z);
                } else if (obj2 instanceof Serializable) {
                    writeOrdinaryObject(obj2, lookup4, z);
                } else {
                    throw new NotSerializableException(cls2.getName());
                }
                this.depth--;
                this.bout.setBlockDataMode(blockDataMode);
            }
        } finally {
            this.depth--;
            this.bout.setBlockDataMode(blockDataMode);
        }
    }

    private void writeNull() throws IOException {
        this.bout.writeByte(112);
    }

    private void writeHandle(int i) throws IOException {
        this.bout.writeByte(113);
        this.bout.writeInt(i + ObjectStreamConstants.baseWireHandle);
    }

    private void writeClass(Class<?> cls, boolean z) throws IOException {
        this.bout.writeByte(118);
        writeClassDesc(ObjectStreamClass.lookup(cls, true), false);
        HandleTable handleTable = this.handles;
        if (z) {
            cls = null;
        }
        handleTable.assign(cls);
    }

    private void writeClassDesc(ObjectStreamClass objectStreamClass, boolean z) throws IOException {
        int lookup;
        if (objectStreamClass == null) {
            writeNull();
        } else if (!z && (lookup = this.handles.lookup(objectStreamClass)) != -1) {
            writeHandle(lookup);
        } else if (objectStreamClass.isProxy()) {
            writeProxyDesc(objectStreamClass, z);
        } else {
            writeNonProxyDesc(objectStreamClass, z);
        }
    }

    private boolean isCustomSubclass() {
        return getClass().getClassLoader() != ObjectOutputStream.class.getClassLoader();
    }

    private void writeProxyDesc(ObjectStreamClass objectStreamClass, boolean z) throws IOException {
        this.bout.writeByte(125);
        this.handles.assign(z ? null : objectStreamClass);
        Class<?> forClass = objectStreamClass.forClass();
        Class[] interfaces = forClass.getInterfaces();
        this.bout.writeInt(interfaces.length);
        for (Class name : interfaces) {
            this.bout.writeUTF(name.getName());
        }
        this.bout.setBlockDataMode(true);
        if (forClass != null && isCustomSubclass()) {
            ReflectUtil.checkPackageAccess(forClass);
        }
        annotateProxyClass(forClass);
        this.bout.setBlockDataMode(false);
        this.bout.writeByte(120);
        writeClassDesc(objectStreamClass.getSuperDesc(), false);
    }

    private void writeNonProxyDesc(ObjectStreamClass objectStreamClass, boolean z) throws IOException {
        this.bout.writeByte(114);
        this.handles.assign(z ? null : objectStreamClass);
        if (this.protocol == 1) {
            objectStreamClass.writeNonProxy(this);
        } else {
            writeClassDescriptor(objectStreamClass);
        }
        Class<?> forClass = objectStreamClass.forClass();
        this.bout.setBlockDataMode(true);
        if (forClass != null && isCustomSubclass()) {
            ReflectUtil.checkPackageAccess(forClass);
        }
        annotateClass(forClass);
        this.bout.setBlockDataMode(false);
        this.bout.writeByte(120);
        writeClassDesc(objectStreamClass.getSuperDesc(), false);
    }

    private void writeString(String str, boolean z) throws IOException {
        this.handles.assign(z ? null : str);
        long uTFLength = this.bout.getUTFLength(str);
        if (uTFLength <= 65535) {
            this.bout.writeByte(116);
            this.bout.writeUTF(str, uTFLength);
            return;
        }
        this.bout.writeByte(124);
        this.bout.writeLongUTF(str, uTFLength);
    }

    private void writeArray(Object obj, ObjectStreamClass objectStreamClass, boolean z) throws IOException {
        this.bout.writeByte(117);
        writeClassDesc(objectStreamClass, false);
        this.handles.assign(z ? null : obj);
        Class<?> componentType = objectStreamClass.forClass().getComponentType();
        if (!componentType.isPrimitive()) {
            this.bout.writeInt(r4);
            for (Object writeObject0 : (Object[]) obj) {
                writeObject0(writeObject0, false);
            }
        } else if (componentType == Integer.TYPE) {
            int[] iArr = (int[]) obj;
            this.bout.writeInt(iArr.length);
            this.bout.writeInts(iArr, 0, iArr.length);
        } else if (componentType == Byte.TYPE) {
            byte[] bArr = (byte[]) obj;
            this.bout.writeInt(bArr.length);
            this.bout.write(bArr, 0, bArr.length, true);
        } else if (componentType == Long.TYPE) {
            long[] jArr = (long[]) obj;
            this.bout.writeInt(jArr.length);
            this.bout.writeLongs(jArr, 0, jArr.length);
        } else if (componentType == Float.TYPE) {
            float[] fArr = (float[]) obj;
            this.bout.writeInt(fArr.length);
            this.bout.writeFloats(fArr, 0, fArr.length);
        } else if (componentType == Double.TYPE) {
            double[] dArr = (double[]) obj;
            this.bout.writeInt(dArr.length);
            this.bout.writeDoubles(dArr, 0, dArr.length);
        } else if (componentType == Short.TYPE) {
            short[] sArr = (short[]) obj;
            this.bout.writeInt(sArr.length);
            this.bout.writeShorts(sArr, 0, sArr.length);
        } else if (componentType == Character.TYPE) {
            char[] cArr = (char[]) obj;
            this.bout.writeInt(cArr.length);
            this.bout.writeChars(cArr, 0, cArr.length);
        } else if (componentType == Boolean.TYPE) {
            boolean[] zArr = (boolean[]) obj;
            this.bout.writeInt(zArr.length);
            this.bout.writeBooleans(zArr, 0, zArr.length);
        } else {
            throw new InternalError();
        }
    }

    private void writeEnum(Enum<?> enumR, ObjectStreamClass objectStreamClass, boolean z) throws IOException {
        this.bout.writeByte(126);
        ObjectStreamClass superDesc = objectStreamClass.getSuperDesc();
        if (superDesc.forClass() != Enum.class) {
            objectStreamClass = superDesc;
        }
        writeClassDesc(objectStreamClass, false);
        this.handles.assign(z ? null : enumR);
        writeString(enumR.name(), false);
    }

    private void writeOrdinaryObject(Object obj, ObjectStreamClass objectStreamClass, boolean z) throws IOException {
        objectStreamClass.checkSerialize();
        this.bout.writeByte(115);
        writeClassDesc(objectStreamClass, false);
        this.handles.assign(z ? null : obj);
        if (!objectStreamClass.isExternalizable() || objectStreamClass.isProxy()) {
            writeSerialData(obj, objectStreamClass);
        } else {
            writeExternalData((Externalizable) obj);
        }
    }

    /* JADX INFO: finally extract failed */
    private void writeExternalData(Externalizable externalizable) throws IOException {
        PutFieldImpl putFieldImpl = this.curPut;
        this.curPut = null;
        SerialCallbackContext serialCallbackContext = this.curContext;
        try {
            this.curContext = null;
            if (this.protocol == 1) {
                externalizable.writeExternal(this);
            } else {
                this.bout.setBlockDataMode(true);
                externalizable.writeExternal(this);
                this.bout.setBlockDataMode(false);
                this.bout.writeByte(120);
            }
            this.curContext = serialCallbackContext;
            this.curPut = putFieldImpl;
        } catch (Throwable th) {
            this.curContext = serialCallbackContext;
            throw th;
        }
    }

    /* JADX INFO: finally extract failed */
    private void writeSerialData(Object obj, ObjectStreamClass objectStreamClass) throws IOException {
        ObjectStreamClass.ClassDataSlot[] classDataLayout = objectStreamClass.getClassDataLayout();
        for (ObjectStreamClass.ClassDataSlot classDataSlot : classDataLayout) {
            ObjectStreamClass objectStreamClass2 = classDataSlot.desc;
            if (objectStreamClass2.hasWriteObjectMethod()) {
                PutFieldImpl putFieldImpl = this.curPut;
                this.curPut = null;
                SerialCallbackContext serialCallbackContext = this.curContext;
                try {
                    this.curContext = new SerialCallbackContext(obj, objectStreamClass2);
                    this.bout.setBlockDataMode(true);
                    objectStreamClass2.invokeWriteObject(obj, this);
                    this.bout.setBlockDataMode(false);
                    this.bout.writeByte(120);
                    this.curContext.setUsed();
                    this.curContext = serialCallbackContext;
                    this.curPut = putFieldImpl;
                } catch (Throwable th) {
                    this.curContext.setUsed();
                    this.curContext = serialCallbackContext;
                    throw th;
                }
            } else {
                defaultWriteFields(obj, objectStreamClass2);
            }
        }
    }

    private void defaultWriteFields(Object obj, ObjectStreamClass objectStreamClass) throws IOException {
        Class<?> forClass = objectStreamClass.forClass();
        if (forClass == null || obj == null || forClass.isInstance(obj)) {
            objectStreamClass.checkDefaultSerialize();
            int primDataSize = objectStreamClass.getPrimDataSize();
            byte[] bArr = this.primVals;
            if (bArr == null || bArr.length < primDataSize) {
                this.primVals = new byte[primDataSize];
            }
            objectStreamClass.getPrimFieldValues(obj, this.primVals);
            this.bout.write(this.primVals, 0, primDataSize, false);
            ObjectStreamField[] fields = objectStreamClass.getFields(false);
            int numObjFields = objectStreamClass.getNumObjFields();
            Object[] objArr = new Object[numObjFields];
            int length = fields.length - numObjFields;
            objectStreamClass.getObjFieldValues(obj, objArr);
            for (int i = 0; i < numObjFields; i++) {
                writeObject0(objArr[i], fields[length + i].isUnshared());
            }
            return;
        }
        throw new ClassCastException();
    }

    private void writeFatalException(IOException iOException) throws IOException {
        clear();
        boolean blockDataMode = this.bout.setBlockDataMode(false);
        try {
            this.bout.writeByte(123);
            writeObject0(iOException, false);
            clear();
        } finally {
            this.bout.setBlockDataMode(blockDataMode);
        }
    }

    /* renamed from: java.io.ObjectOutputStream$PutFieldImpl */
    private class PutFieldImpl extends PutField {
        private final ObjectStreamClass desc;
        private final Object[] objVals;
        private final byte[] primVals;

        PutFieldImpl(ObjectStreamClass objectStreamClass) {
            this.desc = objectStreamClass;
            this.primVals = new byte[objectStreamClass.getPrimDataSize()];
            this.objVals = new Object[objectStreamClass.getNumObjFields()];
        }

        public void put(String str, boolean z) {
            Bits.putBoolean(this.primVals, getFieldOffset(str, Boolean.TYPE), z);
        }

        public void put(String str, byte b) {
            this.primVals[getFieldOffset(str, Byte.TYPE)] = b;
        }

        public void put(String str, char c) {
            Bits.putChar(this.primVals, getFieldOffset(str, Character.TYPE), c);
        }

        public void put(String str, short s) {
            Bits.putShort(this.primVals, getFieldOffset(str, Short.TYPE), s);
        }

        public void put(String str, int i) {
            Bits.putInt(this.primVals, getFieldOffset(str, Integer.TYPE), i);
        }

        public void put(String str, float f) {
            Bits.putFloat(this.primVals, getFieldOffset(str, Float.TYPE), f);
        }

        public void put(String str, long j) {
            Bits.putLong(this.primVals, getFieldOffset(str, Long.TYPE), j);
        }

        public void put(String str, double d) {
            Bits.putDouble(this.primVals, getFieldOffset(str, Double.TYPE), d);
        }

        public void put(String str, Object obj) {
            this.objVals[getFieldOffset(str, Object.class)] = obj;
        }

        public void write(ObjectOutput objectOutput) throws IOException {
            if (ObjectOutputStream.this == objectOutput) {
                byte[] bArr = this.primVals;
                int i = 0;
                objectOutput.write(bArr, 0, bArr.length);
                ObjectStreamField[] fields = this.desc.getFields(false);
                int length = fields.length - this.objVals.length;
                while (i < this.objVals.length) {
                    if (!fields[length + i].isUnshared()) {
                        objectOutput.writeObject(this.objVals[i]);
                        i++;
                    } else {
                        throw new IOException("cannot write unshared object");
                    }
                }
                return;
            }
            throw new IllegalArgumentException("wrong stream");
        }

        /* access modifiers changed from: package-private */
        public void writeFields() throws IOException {
            BlockDataOutputStream r0 = ObjectOutputStream.this.bout;
            byte[] bArr = this.primVals;
            int i = 0;
            r0.write(bArr, 0, bArr.length, false);
            ObjectStreamField[] fields = this.desc.getFields(false);
            int length = fields.length - this.objVals.length;
            while (true) {
                Object[] objArr = this.objVals;
                if (i < objArr.length) {
                    ObjectOutputStream.this.writeObject0(objArr[i], fields[length + i].isUnshared());
                    i++;
                } else {
                    return;
                }
            }
        }

        private int getFieldOffset(String str, Class<?> cls) {
            ObjectStreamField field = this.desc.getField(str, cls);
            if (field != null) {
                return field.getOffset();
            }
            throw new IllegalArgumentException("no such field " + str + " with type " + cls);
        }
    }

    /* renamed from: java.io.ObjectOutputStream$BlockDataOutputStream */
    private static class BlockDataOutputStream extends OutputStream implements DataOutput {
        private static final int CHAR_BUF_SIZE = 256;
        private static final int MAX_BLOCK_SIZE = 1024;
        private static final int MAX_HEADER_SIZE = 5;
        private boolean blkmode = false;
        private final byte[] buf = new byte[1024];
        private final char[] cbuf = new char[256];
        private final DataOutputStream dout;
        private final byte[] hbuf = new byte[5];
        private final OutputStream out;
        private int pos = 0;
        private boolean warnOnceWhenWriting;

        BlockDataOutputStream(OutputStream outputStream) {
            this.out = outputStream;
            this.dout = new DataOutputStream(this);
        }

        /* access modifiers changed from: package-private */
        public boolean setBlockDataMode(boolean z) throws IOException {
            boolean z2 = this.blkmode;
            if (z2 == z) {
                return z2;
            }
            drain();
            this.blkmode = z;
            return !z;
        }

        /* access modifiers changed from: package-private */
        public boolean getBlockDataMode() {
            return this.blkmode;
        }

        private void warnIfClosed() {
            if (this.warnOnceWhenWriting) {
                System.logW("The app is relying on undefined behavior. Attempting to write to a closed ObjectOutputStream could produce corrupt output in a future release of Android.", new IOException("Stream Closed"));
                this.warnOnceWhenWriting = false;
            }
        }

        public void write(int i) throws IOException {
            if (this.pos >= 1024) {
                drain();
            }
            byte[] bArr = this.buf;
            int i2 = this.pos;
            this.pos = i2 + 1;
            bArr[i2] = (byte) i;
        }

        public void write(byte[] bArr) throws IOException {
            write(bArr, 0, bArr.length, false);
        }

        public void write(byte[] bArr, int i, int i2) throws IOException {
            write(bArr, i, i2, false);
        }

        public void flush() throws IOException {
            drain();
            this.out.flush();
        }

        public void close() throws IOException {
            flush();
            this.out.close();
            this.warnOnceWhenWriting = true;
        }

        /* access modifiers changed from: package-private */
        public void write(byte[] bArr, int i, int i2, boolean z) throws IOException {
            if (z || this.blkmode) {
                while (i2 > 0) {
                    if (this.pos >= 1024) {
                        drain();
                    }
                    if (i2 < 1024 || z || this.pos != 0) {
                        int min = Math.min(i2, 1024 - this.pos);
                        System.arraycopy((Object) bArr, i, (Object) this.buf, this.pos, min);
                        this.pos += min;
                        i += min;
                        i2 -= min;
                    } else {
                        writeBlockHeader(1024);
                        this.out.write(bArr, i, 1024);
                        i += 1024;
                        i2 -= 1024;
                    }
                }
                warnIfClosed();
                return;
            }
            drain();
            this.out.write(bArr, i, i2);
            warnIfClosed();
        }

        /* access modifiers changed from: package-private */
        public void drain() throws IOException {
            int i = this.pos;
            if (i != 0) {
                if (this.blkmode) {
                    writeBlockHeader(i);
                }
                this.out.write(this.buf, 0, this.pos);
                this.pos = 0;
                warnIfClosed();
            }
        }

        private void writeBlockHeader(int i) throws IOException {
            if (i <= 255) {
                byte[] bArr = this.hbuf;
                bArr[0] = ObjectStreamConstants.TC_BLOCKDATA;
                bArr[1] = (byte) i;
                this.out.write(bArr, 0, 2);
            } else {
                byte[] bArr2 = this.hbuf;
                bArr2[0] = ObjectStreamConstants.TC_BLOCKDATALONG;
                Bits.putInt(bArr2, 1, i);
                this.out.write(this.hbuf, 0, 5);
            }
            warnIfClosed();
        }

        public void writeBoolean(boolean z) throws IOException {
            if (this.pos >= 1024) {
                drain();
            }
            byte[] bArr = this.buf;
            int i = this.pos;
            this.pos = i + 1;
            Bits.putBoolean(bArr, i, z);
        }

        public void writeByte(int i) throws IOException {
            if (this.pos >= 1024) {
                drain();
            }
            byte[] bArr = this.buf;
            int i2 = this.pos;
            this.pos = i2 + 1;
            bArr[i2] = (byte) i;
        }

        public void writeChar(int i) throws IOException {
            int i2 = this.pos;
            if (i2 + 2 <= 1024) {
                Bits.putChar(this.buf, i2, (char) i);
                this.pos += 2;
                return;
            }
            this.dout.writeChar(i);
        }

        public void writeShort(int i) throws IOException {
            int i2 = this.pos;
            if (i2 + 2 <= 1024) {
                Bits.putShort(this.buf, i2, (short) i);
                this.pos += 2;
                return;
            }
            this.dout.writeShort(i);
        }

        public void writeInt(int i) throws IOException {
            int i2 = this.pos;
            if (i2 + 4 <= 1024) {
                Bits.putInt(this.buf, i2, i);
                this.pos += 4;
                return;
            }
            this.dout.writeInt(i);
        }

        public void writeFloat(float f) throws IOException {
            int i = this.pos;
            if (i + 4 <= 1024) {
                Bits.putFloat(this.buf, i, f);
                this.pos += 4;
                return;
            }
            this.dout.writeFloat(f);
        }

        public void writeLong(long j) throws IOException {
            int i = this.pos;
            if (i + 8 <= 1024) {
                Bits.putLong(this.buf, i, j);
                this.pos += 8;
                return;
            }
            this.dout.writeLong(j);
        }

        public void writeDouble(double d) throws IOException {
            int i = this.pos;
            if (i + 8 <= 1024) {
                Bits.putDouble(this.buf, i, d);
                this.pos += 8;
                return;
            }
            this.dout.writeDouble(d);
        }

        public void writeBytes(String str) throws IOException {
            int length = str.length();
            int i = 0;
            int i2 = 0;
            int i3 = 0;
            while (i < length) {
                if (i2 >= i3) {
                    i3 = Math.min(length - i, 256);
                    str.getChars(i, i + i3, this.cbuf, 0);
                    i2 = 0;
                }
                if (this.pos >= 1024) {
                    drain();
                }
                int min = Math.min(i3 - i2, 1024 - this.pos);
                int i4 = this.pos + min;
                while (true) {
                    int i5 = this.pos;
                    if (i5 >= i4) {
                        break;
                    }
                    byte[] bArr = this.buf;
                    this.pos = i5 + 1;
                    bArr[i5] = (byte) this.cbuf[i2];
                    i2++;
                }
                i += min;
            }
        }

        public void writeChars(String str) throws IOException {
            int length = str.length();
            int i = 0;
            while (i < length) {
                int min = Math.min(length - i, 256);
                int i2 = i + min;
                str.getChars(i, i2, this.cbuf, 0);
                writeChars(this.cbuf, 0, min);
                i = i2;
            }
        }

        public void writeUTF(String str) throws IOException {
            writeUTF(str, getUTFLength(str));
        }

        /* access modifiers changed from: package-private */
        public void writeBooleans(boolean[] zArr, int i, int i2) throws IOException {
            int i3 = i2 + i;
            while (i < i3) {
                if (this.pos >= 1024) {
                    drain();
                }
                int min = Math.min(i3, (1024 - this.pos) + i);
                while (i < min) {
                    byte[] bArr = this.buf;
                    int i4 = this.pos;
                    this.pos = i4 + 1;
                    Bits.putBoolean(bArr, i4, zArr[i]);
                    i++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void writeChars(char[] cArr, int i, int i2) throws IOException {
            int i3 = i2 + i;
            while (i < i3) {
                int i4 = this.pos;
                if (i4 <= 1022) {
                    int min = Math.min(i3, ((1024 - i4) >> 1) + i);
                    while (i < min) {
                        Bits.putChar(this.buf, this.pos, cArr[i]);
                        this.pos += 2;
                        i++;
                    }
                } else {
                    this.dout.writeChar(cArr[i]);
                    i++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void writeShorts(short[] sArr, int i, int i2) throws IOException {
            int i3 = i2 + i;
            while (i < i3) {
                int i4 = this.pos;
                if (i4 <= 1022) {
                    int min = Math.min(i3, ((1024 - i4) >> 1) + i);
                    while (i < min) {
                        Bits.putShort(this.buf, this.pos, sArr[i]);
                        this.pos += 2;
                        i++;
                    }
                } else {
                    this.dout.writeShort(sArr[i]);
                    i++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void writeInts(int[] iArr, int i, int i2) throws IOException {
            int i3 = i2 + i;
            while (i < i3) {
                int i4 = this.pos;
                if (i4 <= 1020) {
                    int min = Math.min(i3, ((1024 - i4) >> 2) + i);
                    while (i < min) {
                        Bits.putInt(this.buf, this.pos, iArr[i]);
                        this.pos += 4;
                        i++;
                    }
                } else {
                    this.dout.writeInt(iArr[i]);
                    i++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void writeFloats(float[] fArr, int i, int i2) throws IOException {
            int i3 = i2 + i;
            while (i < i3) {
                int i4 = this.pos;
                if (i4 <= 1020) {
                    int min = Math.min(i3 - i, (1024 - i4) >> 2);
                    ObjectOutputStream.floatsToBytes(fArr, i, this.buf, this.pos, min);
                    i += min;
                    this.pos += min << 2;
                } else {
                    this.dout.writeFloat(fArr[i]);
                    i++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void writeLongs(long[] jArr, int i, int i2) throws IOException {
            int i3 = i2 + i;
            while (i < i3) {
                int i4 = this.pos;
                if (i4 <= 1016) {
                    int min = Math.min(i3, ((1024 - i4) >> 3) + i);
                    while (i < min) {
                        Bits.putLong(this.buf, this.pos, jArr[i]);
                        this.pos += 8;
                        i++;
                    }
                } else {
                    this.dout.writeLong(jArr[i]);
                    i++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public void writeDoubles(double[] dArr, int i, int i2) throws IOException {
            int i3 = i2 + i;
            while (i < i3) {
                int i4 = this.pos;
                if (i4 <= 1016) {
                    int min = Math.min(i3 - i, (1024 - i4) >> 3);
                    ObjectOutputStream.doublesToBytes(dArr, i, this.buf, this.pos, min);
                    i += min;
                    this.pos += min << 3;
                } else {
                    this.dout.writeDouble(dArr[i]);
                    i++;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public long getUTFLength(String str) {
            int length = str.length();
            long j = 0;
            int i = 0;
            while (i < length) {
                int min = Math.min(length - i, 256);
                int i2 = i + min;
                str.getChars(i, i2, this.cbuf, 0);
                for (int i3 = 0; i3 < min; i3++) {
                    char c = this.cbuf[i3];
                    j += (c < 1 || c > 127) ? c > 2047 ? 3 : 2 : 1;
                }
                i = i2;
            }
            return j;
        }

        /* access modifiers changed from: package-private */
        public void writeUTF(String str, long j) throws IOException {
            if (j <= 65535) {
                writeShort((int) j);
                if (j == ((long) str.length())) {
                    writeBytes(str);
                } else {
                    writeUTFBody(str);
                }
            } else {
                throw new UTFDataFormatException();
            }
        }

        /* access modifiers changed from: package-private */
        public void writeLongUTF(String str) throws IOException {
            writeLongUTF(str, getUTFLength(str));
        }

        /* access modifiers changed from: package-private */
        public void writeLongUTF(String str, long j) throws IOException {
            writeLong(j);
            if (j == ((long) str.length())) {
                writeBytes(str);
            } else {
                writeUTFBody(str);
            }
        }

        private void writeUTFBody(String str) throws IOException {
            int length = str.length();
            int i = 0;
            while (i < length) {
                int min = Math.min(length - i, 256);
                int i2 = i + min;
                str.getChars(i, i2, this.cbuf, 0);
                for (int i3 = 0; i3 < min; i3++) {
                    char c = this.cbuf[i3];
                    int i4 = this.pos;
                    if (i4 <= 1021) {
                        if (c <= 127 && c != 0) {
                            byte[] bArr = this.buf;
                            this.pos = i4 + 1;
                            bArr[i4] = (byte) c;
                        } else if (c > 2047) {
                            byte[] bArr2 = this.buf;
                            bArr2[i4 + 2] = (byte) (((c >> 0) & 63) | 128);
                            bArr2[i4 + 1] = (byte) (((c >> 6) & 63) | 128);
                            bArr2[i4 + 0] = (byte) (((c >> 12) & 15) | 224);
                            this.pos = i4 + 3;
                        } else {
                            byte[] bArr3 = this.buf;
                            bArr3[i4 + 1] = (byte) (((c >> 0) & 63) | 128);
                            bArr3[i4 + 0] = (byte) (((c >> 6) & 31) | 192);
                            this.pos = i4 + 2;
                        }
                    } else if (c <= 127 && c != 0) {
                        write((int) c);
                    } else if (c > 2047) {
                        write(((c >> 12) & 15) | 224);
                        write(((c >> 6) & 63) | 128);
                        write(((c >> 0) & 63) | 128);
                    } else {
                        write(((c >> 6) & 31) | 192);
                        write(((c >> 0) & 63) | 128);
                    }
                }
                i = i2;
            }
        }
    }

    /* renamed from: java.io.ObjectOutputStream$HandleTable */
    private static class HandleTable {
        private final float loadFactor;
        private int[] next;
        private Object[] objs;
        private int size;
        private int[] spine;
        private int threshold;

        HandleTable(int i, float f) {
            this.loadFactor = f;
            this.spine = new int[i];
            this.next = new int[i];
            this.objs = new Object[i];
            this.threshold = (int) (((float) i) * f);
            clear();
        }

        /* access modifiers changed from: package-private */
        public int assign(Object obj) {
            if (this.size >= this.next.length) {
                growEntries();
            }
            if (this.size >= this.threshold) {
                growSpine();
            }
            insert(obj, this.size);
            int i = this.size;
            this.size = i + 1;
            return i;
        }

        /* access modifiers changed from: package-private */
        public int lookup(Object obj) {
            if (this.size == 0) {
                return -1;
            }
            int hash = hash(obj);
            int[] iArr = this.spine;
            int i = iArr[hash % iArr.length];
            while (i >= 0) {
                if (this.objs[i] == obj) {
                    return i;
                }
                i = this.next[i];
            }
            return -1;
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            Arrays.fill(this.spine, -1);
            Arrays.fill(this.objs, 0, this.size, (Object) null);
            this.size = 0;
        }

        /* access modifiers changed from: package-private */
        public int size() {
            return this.size;
        }

        private void insert(Object obj, int i) {
            int hash = hash(obj);
            int[] iArr = this.spine;
            int length = hash % iArr.length;
            this.objs[i] = obj;
            this.next[i] = iArr[length];
            iArr[length] = i;
        }

        private void growSpine() {
            int[] iArr = new int[((this.spine.length << 1) + 1)];
            this.spine = iArr;
            this.threshold = (int) (((float) iArr.length) * this.loadFactor);
            Arrays.fill(iArr, -1);
            for (int i = 0; i < this.size; i++) {
                insert(this.objs[i], i);
            }
        }

        private void growEntries() {
            int[] iArr = this.next;
            int length = (iArr.length << 1) + 1;
            int[] iArr2 = new int[length];
            System.arraycopy((Object) iArr, 0, (Object) iArr2, 0, this.size);
            this.next = iArr2;
            Object[] objArr = new Object[length];
            System.arraycopy((Object) this.objs, 0, (Object) objArr, 0, this.size);
            this.objs = objArr;
        }

        private int hash(Object obj) {
            return System.identityHashCode(obj) & Integer.MAX_VALUE;
        }
    }

    /* renamed from: java.io.ObjectOutputStream$ReplaceTable */
    private static class ReplaceTable {
        private final HandleTable htab;
        private Object[] reps;

        ReplaceTable(int i, float f) {
            this.htab = new HandleTable(i, f);
            this.reps = new Object[i];
        }

        /* access modifiers changed from: package-private */
        public void assign(Object obj, Object obj2) {
            int assign = this.htab.assign(obj);
            while (true) {
                Object[] objArr = this.reps;
                if (assign >= objArr.length) {
                    grow();
                } else {
                    objArr[assign] = obj2;
                    return;
                }
            }
        }

        /* access modifiers changed from: package-private */
        public Object lookup(Object obj) {
            int lookup = this.htab.lookup(obj);
            return lookup >= 0 ? this.reps[lookup] : obj;
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            Arrays.fill(this.reps, 0, this.htab.size(), (Object) null);
            this.htab.clear();
        }

        /* access modifiers changed from: package-private */
        public int size() {
            return this.htab.size();
        }

        private void grow() {
            Object[] objArr = this.reps;
            Object[] objArr2 = new Object[((objArr.length << 1) + 1)];
            System.arraycopy((Object) objArr, 0, (Object) objArr2, 0, objArr.length);
            this.reps = objArr2;
        }
    }

    /* renamed from: java.io.ObjectOutputStream$DebugTraceInfoStack */
    private static class DebugTraceInfoStack {
        private final List<String> stack = new ArrayList();

        DebugTraceInfoStack() {
        }

        /* access modifiers changed from: package-private */
        public void clear() {
            this.stack.clear();
        }

        /* access modifiers changed from: package-private */
        public void pop() {
            List<String> list = this.stack;
            list.remove(list.size() - 1);
        }

        /* access modifiers changed from: package-private */
        public void push(String str) {
            List<String> list = this.stack;
            list.add("\t- " + str);
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (!this.stack.isEmpty()) {
                int size = this.stack.size();
                while (size > 0) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(this.stack.get(size - 1));
                    sb2.append(size != 1 ? "\n" : "");
                    sb.append(sb2.toString());
                    size--;
                }
            }
            return sb.toString();
        }
    }
}
