package dalvik.system;

import java.lang.invoke.MethodType;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import sun.invoke.util.Wrapper;

public class EmulatedStackFrame {
    /* access modifiers changed from: private */
    public final Object[] references;
    /* access modifiers changed from: private */
    public final byte[] stackFrame;
    /* access modifiers changed from: private */
    public final MethodType type;

    private EmulatedStackFrame(MethodType methodType, Object[] objArr, byte[] bArr) {
        this.type = methodType;
        this.references = objArr;
        this.stackFrame = bArr;
    }

    public final MethodType getMethodType() {
        return this.type;
    }

    public static final class Range {
        private static Range EMPTY_RANGE = new Range(0, 0, 0, 0);
        public final int numBytes;
        public final int numReferences;
        public final int referencesStart;
        public final int stackFrameStart;

        private Range(int i, int i2, int i3, int i4) {
            this.referencesStart = i;
            this.numReferences = i2;
            this.stackFrameStart = i3;
            this.numBytes = i4;
        }

        public static Range all(MethodType methodType) {
            return m1693of(methodType, 0, methodType.parameterCount());
        }

        /* renamed from: of */
        public static Range m1693of(MethodType methodType, int i, int i2) {
            if (i >= i2) {
                return EMPTY_RANGE;
            }
            Class[] ptypes = methodType.ptypes();
            int i3 = 0;
            int i4 = 0;
            int i5 = 0;
            for (int i6 = 0; i6 < i; i6++) {
                Class cls = ptypes[i6];
                if (!cls.isPrimitive()) {
                    i5++;
                } else {
                    i4 += EmulatedStackFrame.getSize(cls);
                }
            }
            int i7 = 0;
            while (i < i2) {
                Class cls2 = ptypes[i];
                if (!cls2.isPrimitive()) {
                    i3++;
                } else {
                    i7 += EmulatedStackFrame.getSize(cls2);
                }
                i++;
            }
            return new Range(i5, i3, i4, i7);
        }

        public static Range from(MethodType methodType, int i) {
            return m1693of(methodType, i, methodType.parameterCount());
        }
    }

    public static EmulatedStackFrame create(MethodType methodType) {
        int i = 0;
        int i2 = 0;
        for (Class cls : methodType.ptypes()) {
            if (!cls.isPrimitive()) {
                i2++;
            } else {
                i += getSize(cls);
            }
        }
        Class<?> rtype = methodType.rtype();
        if (!rtype.isPrimitive()) {
            i2++;
        } else {
            i += getSize(rtype);
        }
        return new EmulatedStackFrame(methodType, new Object[i2], new byte[i]);
    }

    /* access modifiers changed from: package-private */
    public int getReferenceIndex(int i) {
        Class[] ptypes = this.type.ptypes();
        int i2 = 0;
        for (int i3 = 0; i3 < i; i3++) {
            if (!ptypes[i3].isPrimitive()) {
                i2++;
            }
        }
        return i2;
    }

    public void setReference(int i, Object obj) {
        Class[] ptypes = this.type.ptypes();
        if (i < 0 || i >= ptypes.length) {
            throw new IllegalArgumentException("Invalid index: " + i);
        } else if (obj == null || ptypes[i].isInstance(obj)) {
            this.references[getReferenceIndex(i)] = obj;
        } else {
            throw new IllegalStateException("reference is not of type: " + this.type.ptypes()[i]);
        }
    }

    public <T> T getReference(int i, Class<T> cls) {
        if (cls == this.type.ptypes()[i]) {
            return this.references[getReferenceIndex(i)];
        }
        throw new IllegalArgumentException("Argument: " + i + " is of type " + this.type.ptypes()[i] + " expected " + cls + "");
    }

    public void copyRangeTo(EmulatedStackFrame emulatedStackFrame, Range range, int i, int i2) {
        if (range.numReferences > 0) {
            System.arraycopy((Object) this.references, range.referencesStart, (Object) emulatedStackFrame.references, i, range.numReferences);
        }
        if (range.numBytes > 0) {
            System.arraycopy((Object) this.stackFrame, range.stackFrameStart, (Object) emulatedStackFrame.stackFrame, i2, range.numBytes);
        }
    }

    public void copyReturnValueTo(EmulatedStackFrame emulatedStackFrame) {
        Class<?> returnType = this.type.returnType();
        if (!returnType.isPrimitive()) {
            Object[] objArr = emulatedStackFrame.references;
            Object[] objArr2 = this.references;
            objArr[objArr.length - 1] = objArr2[objArr2.length - 1];
        } else if (!is64BitPrimitive(returnType)) {
            byte[] bArr = this.stackFrame;
            byte[] bArr2 = emulatedStackFrame.stackFrame;
            System.arraycopy((Object) bArr, bArr.length - 4, (Object) bArr2, bArr2.length - 4, 4);
        } else {
            byte[] bArr3 = this.stackFrame;
            byte[] bArr4 = emulatedStackFrame.stackFrame;
            System.arraycopy((Object) bArr3, bArr3.length - 8, (Object) bArr4, bArr4.length - 8, 8);
        }
    }

    public void setReturnValueTo(Object obj) {
        Class<?> returnType = this.type.returnType();
        if (returnType.isPrimitive()) {
            throw new IllegalStateException("return type is not a reference type: " + returnType);
        } else if (obj == null || returnType.isInstance(obj)) {
            Object[] objArr = this.references;
            objArr[objArr.length - 1] = obj;
        } else {
            throw new IllegalArgumentException("reference is not of type " + returnType);
        }
    }

    private static boolean is64BitPrimitive(Class<?> cls) {
        return cls == Double.TYPE || cls == Long.TYPE;
    }

    public static int getSize(Class<?> cls) {
        if (cls.isPrimitive()) {
            return is64BitPrimitive(cls) ? 8 : 4;
        }
        throw new IllegalArgumentException("type.isPrimitive() == false: " + cls);
    }

    public static class StackFrameAccessor {
        private static final int RETURN_VALUE_IDX = -2;
        protected int argumentIdx = 0;
        protected EmulatedStackFrame frame;
        protected ByteBuffer frameBuf = null;
        private int numArgs = 0;
        protected int referencesOffset = 0;

        protected StackFrameAccessor() {
        }

        public StackFrameAccessor attach(EmulatedStackFrame emulatedStackFrame) {
            return attach(emulatedStackFrame, 0, 0, 0);
        }

        public StackFrameAccessor attach(EmulatedStackFrame emulatedStackFrame, int i, int i2, int i3) {
            if (this.frame != emulatedStackFrame) {
                this.frame = emulatedStackFrame;
                this.frameBuf = ByteBuffer.wrap(emulatedStackFrame.stackFrame).order(ByteOrder.LITTLE_ENDIAN);
                this.numArgs = this.frame.type.ptypes().length;
            }
            this.frameBuf.position(i3);
            this.referencesOffset = i2;
            this.argumentIdx = i;
            return this;
        }

        private Class<?> getCurrentArgumentType() {
            int i = this.argumentIdx;
            if (i < this.numArgs && i != -1) {
                return i == -2 ? this.frame.type.rtype() : this.frame.type.ptypes()[this.argumentIdx];
            }
            throw new IllegalArgumentException("Invalid argument index: " + this.argumentIdx);
        }

        private static void checkAssignable(Class<?> cls, Class<?> cls2) {
            if (!cls.isAssignableFrom(cls2)) {
                throw new IllegalArgumentException("Incorrect type: " + cls2 + ", expected: " + cls);
            }
        }

        /* access modifiers changed from: protected */
        public void checkWriteType(Class<?> cls) {
            checkAssignable(getCurrentArgumentType(), cls);
        }

        /* access modifiers changed from: protected */
        public void checkReadType(Class<?> cls) {
            checkAssignable(cls, getCurrentArgumentType());
        }

        public void makeReturnValueAccessor() {
            Class<?> rtype = this.frame.type.rtype();
            this.argumentIdx = -2;
            if (rtype.isPrimitive()) {
                ByteBuffer byteBuffer = this.frameBuf;
                byteBuffer.position(byteBuffer.capacity() - EmulatedStackFrame.getSize(rtype));
                return;
            }
            this.referencesOffset = this.frame.references.length - 1;
        }

        public static void copyNext(StackFrameReader stackFrameReader, StackFrameWriter stackFrameWriter, Class<?> cls) {
            char basicTypeChar = Wrapper.basicTypeChar(cls);
            if (basicTypeChar == 'F') {
                stackFrameWriter.putNextFloat(stackFrameReader.nextFloat());
            } else if (basicTypeChar == 'L') {
                stackFrameWriter.putNextReference(stackFrameReader.nextReference(cls), cls);
            } else if (basicTypeChar == 'S') {
                stackFrameWriter.putNextShort(stackFrameReader.nextShort());
            } else if (basicTypeChar == 'Z') {
                stackFrameWriter.putNextBoolean(stackFrameReader.nextBoolean());
            } else if (basicTypeChar == 'I') {
                stackFrameWriter.putNextInt(stackFrameReader.nextInt());
            } else if (basicTypeChar != 'J') {
                switch (basicTypeChar) {
                    case 'B':
                        stackFrameWriter.putNextByte(stackFrameReader.nextByte());
                        return;
                    case 'C':
                        stackFrameWriter.putNextChar(stackFrameReader.nextChar());
                        return;
                    case 'D':
                        stackFrameWriter.putNextDouble(stackFrameReader.nextDouble());
                        return;
                    default:
                        return;
                }
            } else {
                stackFrameWriter.putNextLong(stackFrameReader.nextLong());
            }
        }
    }

    public static class StackFrameWriter extends StackFrameAccessor {
        public void putNextByte(byte b) {
            checkWriteType(Byte.TYPE);
            this.argumentIdx++;
            this.frameBuf.putInt(b);
        }

        public void putNextInt(int i) {
            checkWriteType(Integer.TYPE);
            this.argumentIdx++;
            this.frameBuf.putInt(i);
        }

        public void putNextLong(long j) {
            checkWriteType(Long.TYPE);
            this.argumentIdx++;
            this.frameBuf.putLong(j);
        }

        public void putNextChar(char c) {
            checkWriteType(Character.TYPE);
            this.argumentIdx++;
            this.frameBuf.putInt(c);
        }

        public void putNextBoolean(boolean z) {
            checkWriteType(Boolean.TYPE);
            this.argumentIdx++;
            this.frameBuf.putInt(z ? 1 : 0);
        }

        public void putNextShort(short s) {
            checkWriteType(Short.TYPE);
            this.argumentIdx++;
            this.frameBuf.putInt(s);
        }

        public void putNextFloat(float f) {
            checkWriteType(Float.TYPE);
            this.argumentIdx++;
            this.frameBuf.putFloat(f);
        }

        public void putNextDouble(double d) {
            checkWriteType(Double.TYPE);
            this.argumentIdx++;
            this.frameBuf.putDouble(d);
        }

        public void putNextReference(Object obj, Class<?> cls) {
            checkWriteType(cls);
            this.argumentIdx++;
            Object[] r4 = this.frame.references;
            int i = this.referencesOffset;
            this.referencesOffset = i + 1;
            r4[i] = obj;
        }
    }

    public static class StackFrameReader extends StackFrameAccessor {
        public byte nextByte() {
            checkReadType(Byte.TYPE);
            this.argumentIdx++;
            return (byte) this.frameBuf.getInt();
        }

        public int nextInt() {
            checkReadType(Integer.TYPE);
            this.argumentIdx++;
            return this.frameBuf.getInt();
        }

        public long nextLong() {
            checkReadType(Long.TYPE);
            this.argumentIdx++;
            return this.frameBuf.getLong();
        }

        public char nextChar() {
            checkReadType(Character.TYPE);
            this.argumentIdx++;
            return (char) this.frameBuf.getInt();
        }

        public boolean nextBoolean() {
            checkReadType(Boolean.TYPE);
            this.argumentIdx++;
            if (this.frameBuf.getInt() != 0) {
                return true;
            }
            return false;
        }

        public short nextShort() {
            checkReadType(Short.TYPE);
            this.argumentIdx++;
            return (short) this.frameBuf.getInt();
        }

        public float nextFloat() {
            checkReadType(Float.TYPE);
            this.argumentIdx++;
            return this.frameBuf.getFloat();
        }

        public double nextDouble() {
            checkReadType(Double.TYPE);
            this.argumentIdx++;
            return this.frameBuf.getDouble();
        }

        public <T> T nextReference(Class<T> cls) {
            checkReadType(cls);
            this.argumentIdx++;
            T[] r3 = this.frame.references;
            int i = this.referencesOffset;
            this.referencesOffset = i + 1;
            return r3[i];
        }
    }

    public static class RandomOrderStackFrameReader extends StackFrameReader {
        int[] frameOffsets;
        int[] referencesOffsets;

        private void buildTables(MethodType methodType) {
            Class[] parameterArray = methodType.parameterArray();
            this.frameOffsets = new int[parameterArray.length];
            this.referencesOffsets = new int[parameterArray.length];
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < parameterArray.length; i3++) {
                this.frameOffsets[i3] = i;
                this.referencesOffsets[i3] = i2;
                Class cls = parameterArray[i3];
                if (cls.isPrimitive()) {
                    i += EmulatedStackFrame.getSize(cls);
                } else {
                    i2++;
                }
            }
        }

        public StackFrameAccessor attach(EmulatedStackFrame emulatedStackFrame, int i, int i2, int i3) {
            super.attach(emulatedStackFrame, i, i2, i3);
            buildTables(emulatedStackFrame.getMethodType());
            return this;
        }

        public RandomOrderStackFrameReader moveTo(int i) {
            this.referencesOffset = this.referencesOffsets[i];
            this.frameBuf.position(this.frameOffsets[i]);
            this.argumentIdx = i;
            return this;
        }
    }
}
