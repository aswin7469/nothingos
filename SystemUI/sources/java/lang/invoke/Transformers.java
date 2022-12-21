package java.lang.invoke;

import dalvik.system.EmulatedStackFrame;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import sun.invoke.util.Wrapper;
import sun.misc.Unsafe;

public class Transformers {
    /* access modifiers changed from: private */
    public static final Method TRANSFORM_INTERNAL;

    private Transformers() {
    }

    static {
        try {
            TRANSFORM_INTERNAL = MethodHandle.class.getDeclaredMethod("transformInternal", EmulatedStackFrame.class);
        } catch (NoSuchMethodException unused) {
            throw new AssertionError();
        }
    }

    public static abstract class Transformer extends MethodHandle implements Cloneable {
        protected Transformer(MethodType methodType) {
            super(Transformers.TRANSFORM_INTERNAL.getArtMethod(), 5, methodType);
        }

        protected Transformer(MethodType methodType, int i) {
            super(Transformers.TRANSFORM_INTERNAL.getArtMethod(), i, methodType);
        }

        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        protected static void invokeFromTransform(MethodHandle methodHandle, EmulatedStackFrame emulatedStackFrame) throws Throwable {
            if (methodHandle instanceof Transformer) {
                ((Transformer) methodHandle).transform(emulatedStackFrame);
            } else {
                methodHandle.asType(emulatedStackFrame.getMethodType()).invokeExactWithFrame(emulatedStackFrame);
            }
        }

        /* access modifiers changed from: protected */
        public void invokeExactFromTransform(MethodHandle methodHandle, EmulatedStackFrame emulatedStackFrame) throws Throwable {
            if (methodHandle instanceof Transformer) {
                ((Transformer) methodHandle).transform(emulatedStackFrame);
            } else {
                methodHandle.invokeExactWithFrame(emulatedStackFrame);
            }
        }
    }

    static class AlwaysThrow extends Transformer {
        private final Class<? extends Throwable> exceptionType;

        AlwaysThrow(Class<?> cls, Class<? extends Throwable> cls2) {
            super(MethodType.methodType(cls, (Class<?>) cls2));
            this.exceptionType = cls2;
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            throw ((Throwable) emulatedStackFrame.getReference(0, this.exceptionType));
        }
    }

    static class DropArguments extends Transformer {
        private final MethodHandle delegate;
        private final EmulatedStackFrame.Range range1;
        private final EmulatedStackFrame.Range range2;

        DropArguments(MethodType methodType, MethodHandle methodHandle, int i, int i2) {
            super(methodType);
            this.delegate = methodHandle;
            this.range1 = EmulatedStackFrame.Range.m1687of(methodType, 0, i);
            this.range2 = EmulatedStackFrame.Range.from(methodType, i + i2);
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame create = EmulatedStackFrame.create(this.delegate.type());
            emulatedStackFrame.copyRangeTo(create, this.range1, 0, 0);
            emulatedStackFrame.copyRangeTo(create, this.range2, this.range1.numReferences, this.range1.numBytes);
            invokeFromTransform(this.delegate, create);
            create.copyReturnValueTo(emulatedStackFrame);
        }
    }

    static class CatchException extends Transformer {
        private final Class<?> exType;
        private final MethodHandle handler;
        private final EmulatedStackFrame.Range handlerArgsRange;
        private final MethodHandle target;

        CatchException(MethodHandle methodHandle, MethodHandle methodHandle2, Class<?> cls) {
            super(methodHandle.type());
            this.target = methodHandle;
            this.handler = methodHandle2;
            this.exType = cls;
            this.handlerArgsRange = EmulatedStackFrame.Range.m1687of(methodHandle.type(), 0, methodHandle2.type().parameterCount() - 1);
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            try {
                invokeFromTransform(this.target, emulatedStackFrame);
            } catch (Throwable th) {
                if (th.getClass() == this.exType) {
                    EmulatedStackFrame create = EmulatedStackFrame.create(this.handler.type());
                    create.setReference(0, th);
                    emulatedStackFrame.copyRangeTo(create, this.handlerArgsRange, 1, 0);
                    invokeFromTransform(this.handler, create);
                    create.copyReturnValueTo(emulatedStackFrame);
                    return;
                }
                throw th;
            }
        }
    }

    static class TryFinally extends Transformer {
        private final MethodHandle cleanup;
        private final MethodHandle target;

        TryFinally(MethodHandle methodHandle, MethodHandle methodHandle2) {
            super(methodHandle.type());
            this.target = methodHandle;
            this.cleanup = methodHandle2;
        }

        /* access modifiers changed from: protected */
        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0039, code lost:
            if (r3.cleanup.type().returnType() != java.lang.Void.TYPE) goto L_0x003b;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:11:0x003b, code lost:
            r0.copyReturnValueTo(r4);
         */
        /* JADX WARNING: Code restructure failed: missing block: B:12:0x003e, code lost:
            throw r1;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0023, code lost:
            r1 = move-exception;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x0024, code lost:
            r0 = prepareCleanupFrame(r4, r0);
            invokeExactFromTransform(r3.cleanup, r0);
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void transform(dalvik.system.EmulatedStackFrame r4) throws java.lang.Throwable {
            /*
                r3 = this;
                java.lang.invoke.MethodHandle r0 = r3.target     // Catch:{ all -> 0x0021 }
                r3.invokeExactFromTransform(r0, r4)     // Catch:{ all -> 0x0021 }
                r0 = 0
                dalvik.system.EmulatedStackFrame r0 = r3.prepareCleanupFrame(r4, r0)
                java.lang.invoke.MethodHandle r1 = r3.cleanup
                r3.invokeExactFromTransform(r1, r0)
                java.lang.invoke.MethodHandle r3 = r3.cleanup
                java.lang.invoke.MethodType r3 = r3.type()
                java.lang.Class r3 = r3.returnType()
                java.lang.Class<java.lang.Void> r1 = java.lang.Void.TYPE
                if (r3 == r1) goto L_0x0020
                r0.copyReturnValueTo(r4)
            L_0x0020:
                return
            L_0x0021:
                r0 = move-exception
                throw r0     // Catch:{ all -> 0x0023 }
            L_0x0023:
                r1 = move-exception
                dalvik.system.EmulatedStackFrame r0 = r3.prepareCleanupFrame(r4, r0)
                java.lang.invoke.MethodHandle r2 = r3.cleanup
                r3.invokeExactFromTransform(r2, r0)
                java.lang.invoke.MethodHandle r3 = r3.cleanup
                java.lang.invoke.MethodType r3 = r3.type()
                java.lang.Class r3 = r3.returnType()
                java.lang.Class<java.lang.Void> r2 = java.lang.Void.TYPE
                if (r3 == r2) goto L_0x003e
                r0.copyReturnValueTo(r4)
            L_0x003e:
                throw r1
            */
            throw new UnsupportedOperationException("Method not decompiled: java.lang.invoke.Transformers.TryFinally.transform(dalvik.system.EmulatedStackFrame):void");
        }

        private EmulatedStackFrame prepareCleanupFrame(EmulatedStackFrame emulatedStackFrame, Throwable th) {
            int i;
            EmulatedStackFrame create = EmulatedStackFrame.create(this.cleanup.type());
            EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
            stackFrameWriter.attach(create);
            stackFrameWriter.putNextReference(th, Throwable.class);
            Class<?> returnType = this.target.type().returnType();
            EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
            stackFrameReader.attach(emulatedStackFrame);
            if (returnType != Void.TYPE) {
                stackFrameReader.makeReturnValueAccessor();
                EmulatedStackFrame.StackFrameAccessor.copyNext(stackFrameReader, stackFrameWriter, returnType);
                stackFrameReader.attach(emulatedStackFrame);
                i = 2;
            } else {
                i = 1;
            }
            Class[] parameterArray = this.cleanup.type().parameterArray();
            while (i != parameterArray.length) {
                EmulatedStackFrame.StackFrameAccessor.copyNext(stackFrameReader, stackFrameWriter, parameterArray[i]);
                i++;
            }
            return create;
        }
    }

    static class GuardWithTest extends Transformer {
        private final MethodHandle fallback;
        private final MethodHandle target;
        private final MethodHandle test;
        private final EmulatedStackFrame.Range testArgsRange;

        GuardWithTest(MethodHandle methodHandle, MethodHandle methodHandle2, MethodHandle methodHandle3) {
            super(methodHandle2.type());
            this.test = methodHandle;
            this.target = methodHandle2;
            this.fallback = methodHandle3;
            this.testArgsRange = EmulatedStackFrame.Range.m1687of(methodHandle2.type(), 0, methodHandle.type().parameterCount());
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame create = EmulatedStackFrame.create(this.test.type());
            emulatedStackFrame.copyRangeTo(create, this.testArgsRange, 0, 0);
            EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
            stackFrameReader.attach(create);
            stackFrameReader.makeReturnValueAccessor();
            invokeFromTransform(this.test, create);
            if (stackFrameReader.nextBoolean()) {
                invokeFromTransform(this.target, emulatedStackFrame);
            } else {
                invokeFromTransform(this.fallback, emulatedStackFrame);
            }
        }
    }

    static class ReferenceArrayElementGetter extends Transformer {
        private final Class<?> arrayClass;

        ReferenceArrayElementGetter(Class<?> cls) {
            super(MethodType.methodType(cls.getComponentType(), (Class<?>[]) new Class[]{cls, Integer.TYPE}));
            this.arrayClass = cls;
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
            stackFrameReader.attach(emulatedStackFrame);
            int nextInt = stackFrameReader.nextInt();
            EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
            stackFrameWriter.attach(emulatedStackFrame);
            stackFrameWriter.makeReturnValueAccessor();
            stackFrameWriter.putNextReference(((Object[]) stackFrameReader.nextReference(this.arrayClass))[nextInt], this.arrayClass.getComponentType());
        }
    }

    static class ReferenceArrayElementSetter extends Transformer {
        private final Class<?> arrayClass;

        ReferenceArrayElementSetter(Class<?> cls) {
            super(MethodType.methodType((Class<?>) Void.TYPE, (Class<?>[]) new Class[]{cls, Integer.TYPE, cls.getComponentType()}));
            this.arrayClass = cls;
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
            stackFrameReader.attach(emulatedStackFrame);
            ((Object[]) stackFrameReader.nextReference(this.arrayClass))[stackFrameReader.nextInt()] = stackFrameReader.nextReference(this.arrayClass.getComponentType());
        }
    }

    static class ReferenceIdentity extends Transformer {
        private final Class<?> type;

        ReferenceIdentity(Class<?> cls) {
            super(MethodType.methodType(cls, cls));
            this.type = cls;
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
            stackFrameReader.attach(emulatedStackFrame);
            EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
            stackFrameWriter.attach(emulatedStackFrame);
            stackFrameWriter.makeReturnValueAccessor();
            stackFrameWriter.putNextReference(stackFrameReader.nextReference(this.type), this.type);
        }
    }

    static class ZeroValue extends Transformer {
        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
        }

        public ZeroValue(Class<?> cls) {
            super(MethodType.methodType(cls));
        }
    }

    static class ArrayConstructor extends Transformer {
        private final Class<?> componentType;

        ArrayConstructor(Class<?> cls) {
            super(MethodType.methodType(cls, (Class<?>) Integer.TYPE));
            this.componentType = cls.getComponentType();
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
            stackFrameReader.attach(emulatedStackFrame);
            emulatedStackFrame.setReturnValueTo(Array.newInstance(this.componentType, stackFrameReader.nextInt()));
        }
    }

    static class ArrayLength extends Transformer {
        private final Class<?> arrayType;

        ArrayLength(Class<?> cls) {
            super(MethodType.methodType((Class<?>) Integer.TYPE, cls));
            this.arrayType = cls;
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            int i;
            EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
            stackFrameReader.attach(emulatedStackFrame);
            Object nextReference = stackFrameReader.nextReference(this.arrayType);
            char basicTypeChar = Wrapper.basicTypeChar(this.arrayType.getComponentType());
            if (basicTypeChar == 'F') {
                i = ((float[]) nextReference).length;
            } else if (basicTypeChar == 'L') {
                i = ((Object[]) nextReference).length;
            } else if (basicTypeChar == 'S') {
                i = ((short[]) nextReference).length;
            } else if (basicTypeChar == 'Z') {
                i = ((boolean[]) nextReference).length;
            } else if (basicTypeChar == 'I') {
                i = ((int[]) nextReference).length;
            } else if (basicTypeChar != 'J') {
                switch (basicTypeChar) {
                    case 'B':
                        i = ((byte[]) nextReference).length;
                        break;
                    case 'C':
                        i = ((char[]) nextReference).length;
                        break;
                    case 'D':
                        i = ((double[]) nextReference).length;
                        break;
                    default:
                        throw new IllegalStateException("Unsupported type: " + this.arrayType);
                }
            } else {
                i = ((long[]) nextReference).length;
            }
            EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
            stackFrameWriter.attach(emulatedStackFrame).makeReturnValueAccessor();
            stackFrameWriter.putNextInt(i);
        }
    }

    static class Construct extends Transformer {
        private final EmulatedStackFrame.Range callerRange = EmulatedStackFrame.Range.all(type());
        private final MethodHandle constructorHandle;

        Construct(MethodHandle methodHandle, MethodType methodType) {
            super(methodType);
            this.constructorHandle = methodHandle;
        }

        /* access modifiers changed from: package-private */
        public MethodHandle getConstructorHandle() {
            return this.constructorHandle;
        }

        private static boolean isAbstract(Class<?> cls) {
            return (cls.getModifiers() & 1024) == 1024;
        }

        private static void checkInstantiable(Class<?> cls) throws InstantiationException {
            if (isAbstract(cls)) {
                String str = cls.isInterface() ? "interface " : "abstract class ";
                throw new InstantiationException("Can't instantiate " + str + cls);
            }
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            Class<?> parameterType = this.constructorHandle.type().parameterType(0);
            checkInstantiable(parameterType);
            Object allocateInstance = Unsafe.getUnsafe().allocateInstance(parameterType);
            EmulatedStackFrame create = EmulatedStackFrame.create(this.constructorHandle.type());
            create.setReference(0, allocateInstance);
            emulatedStackFrame.copyRangeTo(create, this.callerRange, 1, 0);
            invokeExactFromTransform(this.constructorHandle, create);
            emulatedStackFrame.setReturnValueTo(allocateInstance);
        }
    }

    static class BindTo extends Transformer {
        private final MethodHandle delegate;
        private final EmulatedStackFrame.Range range = EmulatedStackFrame.Range.all(type());
        private final Object receiver;

        BindTo(MethodHandle methodHandle, Object obj) {
            super(methodHandle.type().dropParameterTypes(0, 1));
            this.delegate = methodHandle;
            this.receiver = obj;
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame create = EmulatedStackFrame.create(this.delegate.type());
            create.setReference(0, this.receiver);
            emulatedStackFrame.copyRangeTo(create, this.range, 1, 0);
            invokeFromTransform(this.delegate, create);
            create.copyReturnValueTo(emulatedStackFrame);
        }
    }

    static class FilterReturnValue extends Transformer {
        private final EmulatedStackFrame.Range allArgs = EmulatedStackFrame.Range.all(type());
        private final MethodHandle filter;
        private final MethodHandle target;

        FilterReturnValue(MethodHandle methodHandle, MethodHandle methodHandle2) {
            super(MethodType.methodType(methodHandle2.type().rtype(), (Class<?>[]) methodHandle.type().ptypes()));
            this.target = methodHandle;
            this.filter = methodHandle2;
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame create = EmulatedStackFrame.create(this.target.type());
            emulatedStackFrame.copyRangeTo(create, this.allArgs, 0, 0);
            invokeFromTransform(this.target, create);
            EmulatedStackFrame create2 = EmulatedStackFrame.create(this.filter.type());
            Class<?> rtype = this.target.type().rtype();
            if (rtype != Void.TYPE) {
                EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
                stackFrameReader.attach(create).makeReturnValueAccessor();
                EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
                stackFrameWriter.attach(create2);
                EmulatedStackFrame.StackFrameAccessor.copyNext(stackFrameReader, stackFrameWriter, rtype);
            }
            invokeExactFromTransform(this.filter, create2);
            create2.copyReturnValueTo(emulatedStackFrame);
        }
    }

    static class PermuteArguments extends Transformer {
        private final int[] reorder;
        private final MethodHandle target;

        PermuteArguments(MethodType methodType, MethodHandle methodHandle, int[] iArr) {
            super(methodType);
            this.target = methodHandle;
            this.reorder = iArr;
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame.RandomOrderStackFrameReader randomOrderStackFrameReader = new EmulatedStackFrame.RandomOrderStackFrameReader();
            randomOrderStackFrameReader.attach(emulatedStackFrame);
            EmulatedStackFrame create = EmulatedStackFrame.create(this.target.type());
            EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
            stackFrameWriter.attach(create);
            Class[] parameterArray = emulatedStackFrame.getMethodType().parameterArray();
            int i = 0;
            while (true) {
                int[] iArr = this.reorder;
                if (i < iArr.length) {
                    int i2 = iArr[i];
                    randomOrderStackFrameReader.moveTo(i2);
                    EmulatedStackFrame.StackFrameAccessor.copyNext(randomOrderStackFrameReader, stackFrameWriter, parameterArray[i2]);
                    i++;
                } else {
                    invokeFromTransform(this.target, create);
                    create.copyReturnValueTo(emulatedStackFrame);
                    return;
                }
            }
        }
    }

    static class VarargsCollector extends Transformer {
        private final Class<?> arrayType;
        final MethodHandle target;

        public boolean isVarargsCollector() {
            return true;
        }

        VarargsCollector(MethodHandle methodHandle) {
            super(methodHandle.type());
            Class<?>[] ptypes = methodHandle.type().ptypes();
            if (lastParameterTypeIsAnArray(ptypes)) {
                this.target = methodHandle;
                this.arrayType = ptypes[ptypes.length - 1];
                return;
            }
            throw new IllegalArgumentException("target does not have array as last parameter");
        }

        private static boolean lastParameterTypeIsAnArray(Class<?>[] clsArr) {
            if (clsArr.length == 0) {
                return false;
            }
            return clsArr[clsArr.length - 1].isArray();
        }

        public MethodHandle asFixedArity() {
            return this.target;
        }

        /* access modifiers changed from: package-private */
        public MethodHandle asTypeUncached(MethodType methodType) {
            MethodHandle methodHandle;
            MethodType type = type();
            MethodHandle asFixedArity = asFixedArity();
            if (type.parameterCount() != methodType.parameterCount() || !type.lastParameterType().isAssignableFrom(methodType.lastParameterType())) {
                int parameterCount = (methodType.parameterCount() - type.parameterCount()) + 1;
                if (parameterCount < 0) {
                    throwWrongMethodTypeException(type, methodType);
                }
                try {
                    methodHandle = asFixedArity.asCollector(this.arrayType, parameterCount).asType(methodType);
                } catch (IllegalArgumentException unused) {
                    throwWrongMethodTypeException(type, methodType);
                    methodHandle = null;
                }
                this.asTypeCache = methodHandle;
                return methodHandle;
            }
            MethodHandle asType = asFixedArity.asType(methodType);
            this.asTypeCache = asType;
            return asType;
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            MethodType methodType = emulatedStackFrame.getMethodType();
            Class[] ptypes = methodType.ptypes();
            Class[] ptypes2 = type().ptypes();
            int length = ptypes2.length - 1;
            if (ptypes.length != ptypes2.length || !ptypes2[length].isAssignableFrom(ptypes[length])) {
                if (ptypes.length < ptypes2.length - 1) {
                    throwWrongMethodTypeException(methodType, type());
                }
                if (!MethodType.canConvert(type().rtype(), methodType.rtype())) {
                    throwWrongMethodTypeException(methodType, type());
                }
                if (!arityArgumentsConvertible(ptypes, length, ptypes2[length].getComponentType())) {
                    throwWrongMethodTypeException(methodType, type());
                }
                EmulatedStackFrame create = EmulatedStackFrame.create(makeTargetFrameType(methodType, type()));
                prepareFrame(emulatedStackFrame, create);
                invokeExactFromTransform(this.target, create);
                create.copyReturnValueTo(emulatedStackFrame);
                return;
            }
            invokeFromTransform(this.target, emulatedStackFrame);
        }

        public MethodHandle withVarargs(boolean z) {
            return z ? this : this.target;
        }

        private static void throwWrongMethodTypeException(MethodType methodType, MethodType methodType2) {
            throw new WrongMethodTypeException("Cannot convert " + methodType + " to " + methodType2);
        }

        private static boolean arityArgumentsConvertible(Class<?>[] clsArr, int i, Class<?> cls) {
            if (clsArr.length - 1 == i && clsArr[i].isArray() && clsArr[i].getComponentType() == cls) {
                return true;
            }
            while (i < clsArr.length) {
                if (!MethodType.canConvert(clsArr[i], cls)) {
                    return false;
                }
                i++;
            }
            return true;
        }

        private static Object referenceArray(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?>[] clsArr, Class<?> cls, int i, int i2) {
            Object obj;
            Object newInstance = Array.newInstance(cls, i2);
            for (int i3 = 0; i3 < i2; i3++) {
                Class<?> cls2 = clsArr[i3 + i];
                char basicTypeChar = Wrapper.basicTypeChar(cls2);
                if (basicTypeChar == 'F') {
                    obj = Float.valueOf(stackFrameReader.nextFloat());
                } else if (basicTypeChar == 'L') {
                    obj = stackFrameReader.nextReference(cls2);
                } else if (basicTypeChar == 'S') {
                    obj = Short.valueOf(stackFrameReader.nextShort());
                } else if (basicTypeChar == 'Z') {
                    obj = Boolean.valueOf(stackFrameReader.nextBoolean());
                } else if (basicTypeChar == 'I') {
                    obj = Integer.valueOf(stackFrameReader.nextInt());
                } else if (basicTypeChar != 'J') {
                    switch (basicTypeChar) {
                        case 'B':
                            obj = Byte.valueOf(stackFrameReader.nextByte());
                            break;
                        case 'C':
                            obj = Character.valueOf(stackFrameReader.nextChar());
                            break;
                        case 'D':
                            obj = Double.valueOf(stackFrameReader.nextDouble());
                            break;
                        default:
                            obj = null;
                            break;
                    }
                } else {
                    obj = Long.valueOf(stackFrameReader.nextLong());
                }
                Array.set(newInstance, i3, cls.cast(obj));
            }
            return newInstance;
        }

        private static Object intArray(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?>[] clsArr, int i, int i2) {
            int[] iArr = new int[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                Class<?> cls = clsArr[i3 + i];
                char basicTypeChar = Wrapper.basicTypeChar(cls);
                if (basicTypeChar == 'B') {
                    iArr[i3] = stackFrameReader.nextByte();
                } else if (basicTypeChar == 'I') {
                    iArr[i3] = stackFrameReader.nextInt();
                } else if (basicTypeChar != 'S') {
                    iArr[i3] = ((Integer) stackFrameReader.nextReference(cls)).intValue();
                } else {
                    iArr[i3] = stackFrameReader.nextShort();
                }
            }
            return iArr;
        }

        private static Object longArray(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?>[] clsArr, int i, int i2) {
            long[] jArr = new long[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                Class<?> cls = clsArr[i3 + i];
                char basicTypeChar = Wrapper.basicTypeChar(cls);
                if (basicTypeChar == 'B') {
                    jArr[i3] = (long) stackFrameReader.nextByte();
                } else if (basicTypeChar == 'S') {
                    jArr[i3] = (long) stackFrameReader.nextShort();
                } else if (basicTypeChar == 'I') {
                    jArr[i3] = (long) stackFrameReader.nextInt();
                } else if (basicTypeChar != 'J') {
                    jArr[i3] = ((Long) stackFrameReader.nextReference(cls)).longValue();
                } else {
                    jArr[i3] = stackFrameReader.nextLong();
                }
            }
            return jArr;
        }

        private static Object byteArray(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?>[] clsArr, int i, int i2) {
            byte[] bArr = new byte[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                Class<?> cls = clsArr[i3 + i];
                if (Wrapper.basicTypeChar(cls) != 'B') {
                    bArr[i3] = ((Byte) stackFrameReader.nextReference(cls)).byteValue();
                } else {
                    bArr[i3] = stackFrameReader.nextByte();
                }
            }
            return bArr;
        }

        private static Object shortArray(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?>[] clsArr, int i, int i2) {
            short[] sArr = new short[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                Class<?> cls = clsArr[i3 + i];
                char basicTypeChar = Wrapper.basicTypeChar(cls);
                if (basicTypeChar == 'B') {
                    sArr[i3] = (short) stackFrameReader.nextByte();
                } else if (basicTypeChar != 'S') {
                    sArr[i3] = ((Short) stackFrameReader.nextReference(cls)).shortValue();
                } else {
                    sArr[i3] = stackFrameReader.nextShort();
                }
            }
            return sArr;
        }

        private static Object charArray(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?>[] clsArr, int i, int i2) {
            char[] cArr = new char[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                Class<?> cls = clsArr[i3 + i];
                if (Wrapper.basicTypeChar(cls) != 'C') {
                    cArr[i3] = ((Character) stackFrameReader.nextReference(cls)).charValue();
                } else {
                    cArr[i3] = stackFrameReader.nextChar();
                }
            }
            return cArr;
        }

        private static Object booleanArray(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?>[] clsArr, int i, int i2) {
            boolean[] zArr = new boolean[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                Class<?> cls = clsArr[i3 + i];
                if (Wrapper.basicTypeChar(cls) != 'Z') {
                    zArr[i3] = ((Boolean) stackFrameReader.nextReference(cls)).booleanValue();
                } else {
                    zArr[i3] = stackFrameReader.nextBoolean();
                }
            }
            return zArr;
        }

        private static Object floatArray(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?>[] clsArr, int i, int i2) {
            float[] fArr = new float[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                Class<?> cls = clsArr[i3 + i];
                char basicTypeChar = Wrapper.basicTypeChar(cls);
                if (basicTypeChar == 'B') {
                    fArr[i3] = (float) stackFrameReader.nextByte();
                } else if (basicTypeChar == 'F') {
                    fArr[i3] = stackFrameReader.nextFloat();
                } else if (basicTypeChar == 'S') {
                    fArr[i3] = (float) stackFrameReader.nextShort();
                } else if (basicTypeChar == 'I') {
                    fArr[i3] = (float) stackFrameReader.nextInt();
                } else if (basicTypeChar != 'J') {
                    fArr[i3] = ((Float) stackFrameReader.nextReference(cls)).floatValue();
                } else {
                    fArr[i3] = (float) stackFrameReader.nextLong();
                }
            }
            return fArr;
        }

        private static Object doubleArray(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?>[] clsArr, int i, int i2) {
            double[] dArr = new double[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                Class<?> cls = clsArr[i3 + i];
                char basicTypeChar = Wrapper.basicTypeChar(cls);
                if (basicTypeChar == 'B') {
                    dArr[i3] = (double) stackFrameReader.nextByte();
                } else if (basicTypeChar == 'D') {
                    dArr[i3] = stackFrameReader.nextDouble();
                } else if (basicTypeChar == 'F') {
                    dArr[i3] = (double) stackFrameReader.nextFloat();
                } else if (basicTypeChar == 'S') {
                    dArr[i3] = (double) stackFrameReader.nextShort();
                } else if (basicTypeChar == 'I') {
                    dArr[i3] = (double) stackFrameReader.nextInt();
                } else if (basicTypeChar != 'J') {
                    dArr[i3] = ((Double) stackFrameReader.nextReference(cls)).doubleValue();
                } else {
                    dArr[i3] = (double) stackFrameReader.nextLong();
                }
            }
            return dArr;
        }

        private static Object makeArityArray(MethodType methodType, EmulatedStackFrame.StackFrameReader stackFrameReader, int i, Class<?> cls) {
            int length = methodType.ptypes().length - i;
            Class<?> componentType = cls.getComponentType();
            Class[] ptypes = methodType.ptypes();
            char basicTypeChar = Wrapper.basicTypeChar(componentType);
            if (basicTypeChar == 'F') {
                return floatArray(stackFrameReader, ptypes, i, length);
            }
            if (basicTypeChar == 'L') {
                return referenceArray(stackFrameReader, ptypes, componentType, i, length);
            }
            if (basicTypeChar == 'S') {
                return shortArray(stackFrameReader, ptypes, i, length);
            }
            if (basicTypeChar == 'Z') {
                return booleanArray(stackFrameReader, ptypes, i, length);
            }
            if (basicTypeChar == 'I') {
                return intArray(stackFrameReader, ptypes, i, length);
            }
            if (basicTypeChar == 'J') {
                return longArray(stackFrameReader, ptypes, i, length);
            }
            switch (basicTypeChar) {
                case 'B':
                    return byteArray(stackFrameReader, ptypes, i, length);
                case 'C':
                    return charArray(stackFrameReader, ptypes, i, length);
                case 'D':
                    return doubleArray(stackFrameReader, ptypes, i, length);
                default:
                    throw new InternalError("Unexpected type: " + componentType);
            }
        }

        public static Object collectArguments(char c, Class<?> cls, EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?>[] clsArr, int i, int i2) {
            if (c == 'F') {
                return floatArray(stackFrameReader, clsArr, i, i2);
            }
            if (c == 'L') {
                return referenceArray(stackFrameReader, clsArr, cls, i, i2);
            }
            if (c == 'S') {
                return shortArray(stackFrameReader, clsArr, i, i2);
            }
            if (c == 'Z') {
                return booleanArray(stackFrameReader, clsArr, i, i2);
            }
            if (c == 'I') {
                return intArray(stackFrameReader, clsArr, i, i2);
            }
            if (c == 'J') {
                return longArray(stackFrameReader, clsArr, i, i2);
            }
            switch (c) {
                case 'B':
                    return byteArray(stackFrameReader, clsArr, i, i2);
                case 'C':
                    return charArray(stackFrameReader, clsArr, i, i2);
                case 'D':
                    return doubleArray(stackFrameReader, clsArr, i, i2);
                default:
                    throw new InternalError("Unexpected type: " + c);
            }
        }

        private static void copyParameter(EmulatedStackFrame.StackFrameReader stackFrameReader, EmulatedStackFrame.StackFrameWriter stackFrameWriter, Class<?> cls) {
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
                        throw new InternalError("Unexpected type: " + cls);
                }
            } else {
                stackFrameWriter.putNextLong(stackFrameReader.nextLong());
            }
        }

        private static void prepareFrame(EmulatedStackFrame emulatedStackFrame, EmulatedStackFrame emulatedStackFrame2) {
            EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
            stackFrameWriter.attach(emulatedStackFrame2);
            EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
            stackFrameReader.attach(emulatedStackFrame);
            MethodType methodType = emulatedStackFrame2.getMethodType();
            int length = methodType.ptypes().length - 1;
            for (int i = 0; i < length; i++) {
                copyParameter(stackFrameReader, stackFrameWriter, methodType.ptypes()[i]);
            }
            Class cls = methodType.ptypes()[length];
            stackFrameWriter.putNextReference(makeArityArray(emulatedStackFrame.getMethodType(), stackFrameReader, length, cls), cls);
        }

        private static MethodType makeTargetFrameType(MethodType methodType, MethodType methodType2) {
            int length = methodType2.ptypes().length;
            Class[] clsArr = new Class[length];
            int i = length - 1;
            System.arraycopy((Object) methodType.ptypes(), 0, (Object) clsArr, 0, i);
            clsArr[i] = methodType2.ptypes()[i];
            return MethodType.methodType(methodType.rtype(), (Class<?>[]) clsArr);
        }
    }

    static class Invoker extends Transformer {
        private final EmulatedStackFrame.Range copyRange = EmulatedStackFrame.Range.from(type(), 1);
        private final boolean isExactInvoker;
        private final MethodType targetType;

        Invoker(MethodType methodType, boolean z) {
            super(methodType.insertParameterTypes(0, (Class<?>[]) new Class[]{MethodHandle.class}));
            this.targetType = methodType;
            this.isExactInvoker = z;
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            MethodHandle methodHandle = (MethodHandle) emulatedStackFrame.getReference(0, MethodHandle.class);
            EmulatedStackFrame create = EmulatedStackFrame.create(this.targetType);
            emulatedStackFrame.copyRangeTo(create, this.copyRange, 0, 0);
            if (this.isExactInvoker) {
                invokeExactFromTransform(methodHandle, create);
            } else {
                invokeFromTransform(methodHandle, create);
            }
            create.copyReturnValueTo(emulatedStackFrame);
        }

        private static boolean exactMatch(MethodType methodType, MethodType methodType2) {
            int parameterCount = methodType.parameterCount();
            if (methodType.parameterCount() != methodType2.parameterCount()) {
                return false;
            }
            for (int i = 0; i < parameterCount; i++) {
                if (!exactMatch((Class) methodType.parameterType(i), (Class) methodType2.parameterType(i))) {
                    return false;
                }
            }
            if (methodType.returnType() == Void.TYPE || exactMatch((Class) methodType.returnType(), (Class) methodType2.returnType())) {
                return true;
            }
            return false;
        }

        private static boolean exactMatch(Class cls, Class cls2) {
            return (!cls.isPrimitive() && !cls2.isPrimitive()) || cls == cls2;
        }
    }

    static class Spreader extends Transformer {
        private final int arrayOffset;
        private final Class<?> componentType;
        private final EmulatedStackFrame.Range leadingRange;
        private final int numArrayArgs;
        private final MethodHandle target;
        private final EmulatedStackFrame.Range trailingRange;

        Spreader(MethodHandle methodHandle, MethodType methodType, int i, int i2) {
            super(methodType);
            this.target = methodHandle;
            this.arrayOffset = i;
            Class<?> componentType2 = methodType.ptypes()[i].getComponentType();
            this.componentType = componentType2;
            if (componentType2 != null) {
                this.numArrayArgs = i2;
                this.leadingRange = EmulatedStackFrame.Range.m1687of(methodType, 0, i);
                this.trailingRange = EmulatedStackFrame.Range.from(methodType, i + 1);
                return;
            }
            throw new AssertionError((Object) "Argument " + i + " must be an array.");
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            Class<?> parameterType = type().parameterType(this.arrayOffset);
            Object reference = emulatedStackFrame.getReference(this.arrayOffset, parameterType);
            int length = (this.numArrayArgs == 0 && reference == null) ? 0 : Array.getLength(reference);
            if (length == this.numArrayArgs) {
                EmulatedStackFrame create = EmulatedStackFrame.create(this.target.type());
                emulatedStackFrame.copyRangeTo(create, this.leadingRange, 0, 0);
                if (this.componentType.isPrimitive()) {
                    emulatedStackFrame.copyRangeTo(create, this.trailingRange, this.leadingRange.numReferences, this.leadingRange.numBytes + (EmulatedStackFrame.getSize(this.componentType) * length));
                } else {
                    emulatedStackFrame.copyRangeTo(create, this.trailingRange, this.leadingRange.numReferences + this.numArrayArgs, this.leadingRange.numBytes);
                }
                if (length != 0) {
                    EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
                    stackFrameWriter.attach(create, this.arrayOffset, this.leadingRange.numReferences, this.leadingRange.numBytes);
                    spreadArray(parameterType, reference, stackFrameWriter);
                }
                invokeExactFromTransform(this.target, create);
                create.copyReturnValueTo(emulatedStackFrame);
                return;
            }
            throw new IllegalArgumentException("Invalid array length " + length + " expected " + this.numArrayArgs);
        }

        private void spreadArray(Class<?> cls, Object obj, EmulatedStackFrame.StackFrameWriter stackFrameWriter) {
            Class<?> componentType2 = cls.getComponentType();
            char basicTypeChar = Wrapper.basicTypeChar(componentType2);
            int i = 0;
            if (basicTypeChar == 'F') {
                float[] fArr = (float[]) obj;
                while (i < fArr.length) {
                    stackFrameWriter.putNextFloat(fArr[i]);
                    i++;
                }
            } else if (basicTypeChar == 'L') {
                Object[] objArr = (Object[]) obj;
                while (i < objArr.length) {
                    stackFrameWriter.putNextReference(objArr[i], componentType2);
                    i++;
                }
            } else if (basicTypeChar == 'S') {
                short[] sArr = (short[]) obj;
                while (i < sArr.length) {
                    stackFrameWriter.putNextShort(sArr[i]);
                    i++;
                }
            } else if (basicTypeChar == 'Z') {
                boolean[] zArr = (boolean[]) obj;
                while (i < zArr.length) {
                    stackFrameWriter.putNextBoolean(zArr[i]);
                    i++;
                }
            } else if (basicTypeChar == 'I') {
                int[] iArr = (int[]) obj;
                while (i < iArr.length) {
                    stackFrameWriter.putNextInt(iArr[i]);
                    i++;
                }
            } else if (basicTypeChar != 'J') {
                switch (basicTypeChar) {
                    case 'B':
                        byte[] bArr = (byte[]) obj;
                        while (i < bArr.length) {
                            stackFrameWriter.putNextByte(bArr[i]);
                            i++;
                        }
                        return;
                    case 'C':
                        char[] cArr = (char[]) obj;
                        while (i < cArr.length) {
                            stackFrameWriter.putNextChar(cArr[i]);
                            i++;
                        }
                        return;
                    case 'D':
                        double[] dArr = (double[]) obj;
                        while (i < dArr.length) {
                            stackFrameWriter.putNextDouble(dArr[i]);
                            i++;
                        }
                        return;
                    default:
                        return;
                }
            } else {
                long[] jArr = (long[]) obj;
                while (i < jArr.length) {
                    stackFrameWriter.putNextLong(jArr[i]);
                    i++;
                }
            }
        }
    }

    static class Collector extends Transformer {
        private final int arrayLength;
        private final int arrayOffset;
        private final Class arrayType;
        private final EmulatedStackFrame.Range leadingRange;
        private final MethodHandle target;
        private final EmulatedStackFrame.Range trailingRange;

        Collector(MethodHandle methodHandle, Class<?> cls, int i, int i2) {
            super(methodHandle.type().asCollectorType(cls, i, i2));
            this.target = methodHandle;
            this.arrayOffset = i;
            this.arrayLength = i2;
            this.arrayType = cls;
            this.leadingRange = EmulatedStackFrame.Range.m1687of(type(), 0, i);
            this.trailingRange = EmulatedStackFrame.Range.from(type(), i + i2);
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame create = EmulatedStackFrame.create(this.target.type());
            int i = 0;
            emulatedStackFrame.copyRangeTo(create, this.leadingRange, 0, 0);
            emulatedStackFrame.copyRangeTo(create, this.trailingRange, this.leadingRange.numReferences + 1, this.leadingRange.numBytes);
            EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
            stackFrameWriter.attach(create, this.arrayOffset, this.leadingRange.numReferences, this.leadingRange.numBytes);
            EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
            stackFrameReader.attach(emulatedStackFrame, this.arrayOffset, this.leadingRange.numReferences, this.leadingRange.numBytes);
            char basicTypeChar = Wrapper.basicTypeChar(this.arrayType.getComponentType());
            if (basicTypeChar == 'F') {
                float[] fArr = new float[this.arrayLength];
                while (i < this.arrayLength) {
                    fArr[i] = stackFrameReader.nextFloat();
                    i++;
                }
                stackFrameWriter.putNextReference(fArr, float[].class);
            } else if (basicTypeChar == 'L') {
                Class cls = this.target.type().ptypes()[this.arrayOffset];
                Class<?> componentType = this.arrayType.getComponentType();
                Object[] objArr = (Object[]) Array.newInstance(componentType, this.arrayLength);
                while (i < this.arrayLength) {
                    objArr[i] = stackFrameReader.nextReference(componentType);
                    i++;
                }
                stackFrameWriter.putNextReference(objArr, cls);
            } else if (basicTypeChar == 'S') {
                short[] sArr = new short[this.arrayLength];
                while (i < this.arrayLength) {
                    sArr[i] = stackFrameReader.nextShort();
                    i++;
                }
                stackFrameWriter.putNextReference(sArr, short[].class);
            } else if (basicTypeChar == 'Z') {
                boolean[] zArr = new boolean[this.arrayLength];
                while (i < this.arrayLength) {
                    zArr[i] = stackFrameReader.nextBoolean();
                    i++;
                }
                stackFrameWriter.putNextReference(zArr, boolean[].class);
            } else if (basicTypeChar == 'I') {
                int[] iArr = new int[this.arrayLength];
                while (i < this.arrayLength) {
                    iArr[i] = stackFrameReader.nextInt();
                    i++;
                }
                stackFrameWriter.putNextReference(iArr, int[].class);
            } else if (basicTypeChar != 'J') {
                switch (basicTypeChar) {
                    case 'B':
                        byte[] bArr = new byte[this.arrayLength];
                        while (i < this.arrayLength) {
                            bArr[i] = stackFrameReader.nextByte();
                            i++;
                        }
                        stackFrameWriter.putNextReference(bArr, byte[].class);
                        break;
                    case 'C':
                        char[] cArr = new char[this.arrayLength];
                        while (i < this.arrayLength) {
                            cArr[i] = stackFrameReader.nextChar();
                            i++;
                        }
                        stackFrameWriter.putNextReference(cArr, char[].class);
                        break;
                    case 'D':
                        double[] dArr = new double[this.arrayLength];
                        while (i < this.arrayLength) {
                            dArr[i] = stackFrameReader.nextDouble();
                            i++;
                        }
                        stackFrameWriter.putNextReference(dArr, double[].class);
                        break;
                }
            } else {
                long[] jArr = new long[this.arrayLength];
                while (i < this.arrayLength) {
                    jArr[i] = stackFrameReader.nextLong();
                    i++;
                }
                stackFrameWriter.putNextReference(jArr, long[].class);
            }
            invokeFromTransform(this.target, create);
            create.copyReturnValueTo(emulatedStackFrame);
        }
    }

    static class FilterArguments extends Transformer {
        private final MethodHandle[] filters;
        private final int pos;
        private final MethodHandle target;

        FilterArguments(MethodHandle methodHandle, int i, MethodHandle... methodHandleArr) {
            super(deriveType(methodHandle, i, methodHandleArr));
            this.target = methodHandle;
            this.pos = i;
            this.filters = methodHandleArr;
        }

        private static MethodType deriveType(MethodHandle methodHandle, int i, MethodHandle[] methodHandleArr) {
            Class[] clsArr = new Class[methodHandleArr.length];
            for (int i2 = 0; i2 < methodHandleArr.length; i2++) {
                clsArr[i2] = methodHandleArr[i2].type().parameterType(0);
            }
            return methodHandle.type().replaceParameterTypes(i, methodHandleArr.length + i, clsArr);
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
            stackFrameReader.attach(emulatedStackFrame);
            EmulatedStackFrame create = EmulatedStackFrame.create(this.target.type());
            EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
            stackFrameWriter.attach(create);
            Class[] ptypes = this.target.type().ptypes();
            for (int i = 0; i < ptypes.length; i++) {
                Class cls = ptypes[i];
                int i2 = this.pos;
                MethodHandle methodHandle = null;
                if (i >= i2) {
                    MethodHandle[] methodHandleArr = this.filters;
                    if (i < methodHandleArr.length + i2) {
                        methodHandle = methodHandleArr[i - i2];
                    }
                }
                if (methodHandle != null) {
                    EmulatedStackFrame create2 = EmulatedStackFrame.create(methodHandle.type());
                    EmulatedStackFrame.StackFrameWriter stackFrameWriter2 = new EmulatedStackFrame.StackFrameWriter();
                    stackFrameWriter2.attach(create2);
                    EmulatedStackFrame.StackFrameAccessor.copyNext(stackFrameReader, stackFrameWriter2, methodHandle.type().ptypes()[0]);
                    invokeFromTransform(methodHandle, create2);
                    EmulatedStackFrame.StackFrameReader stackFrameReader2 = new EmulatedStackFrame.StackFrameReader();
                    stackFrameReader2.attach(create2);
                    stackFrameReader2.makeReturnValueAccessor();
                    EmulatedStackFrame.StackFrameAccessor.copyNext(stackFrameReader2, stackFrameWriter, cls);
                } else {
                    EmulatedStackFrame.StackFrameAccessor.copyNext(stackFrameReader, stackFrameWriter, cls);
                }
            }
            invokeFromTransform(this.target, create);
            create.copyReturnValueTo(emulatedStackFrame);
        }
    }

    static class CollectArguments extends Transformer {
        private final MethodHandle collector;
        private final EmulatedStackFrame.Range collectorRange;
        private final int pos;
        private final EmulatedStackFrame.Range range1;
        private final EmulatedStackFrame.Range range2;
        private final int referencesOffset;
        private final int stackFrameOffset;
        private final MethodHandle target;

        CollectArguments(MethodHandle methodHandle, MethodHandle methodHandle2, int i, MethodType methodType) {
            super(methodType);
            this.target = methodHandle;
            this.collector = methodHandle2;
            this.pos = i;
            int parameterCount = methodHandle2.type().parameterCount() + i;
            this.collectorRange = EmulatedStackFrame.Range.m1687of(type(), i, parameterCount);
            this.range1 = EmulatedStackFrame.Range.m1687of(type(), 0, i);
            this.range2 = EmulatedStackFrame.Range.from(type(), parameterCount);
            Class<?> rtype = methodHandle2.type().rtype();
            if (rtype == Void.TYPE) {
                this.stackFrameOffset = 0;
                this.referencesOffset = 0;
            } else if (rtype.isPrimitive()) {
                this.stackFrameOffset = EmulatedStackFrame.getSize(rtype);
                this.referencesOffset = 0;
            } else {
                this.stackFrameOffset = 0;
                this.referencesOffset = 1;
            }
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame create = EmulatedStackFrame.create(this.collector.type());
            emulatedStackFrame.copyRangeTo(create, this.collectorRange, 0, 0);
            invokeFromTransform(this.collector, create);
            EmulatedStackFrame create2 = EmulatedStackFrame.create(this.target.type());
            emulatedStackFrame.copyRangeTo(create2, this.range1, 0, 0);
            if (!(this.referencesOffset == 0 && this.stackFrameOffset == 0)) {
                EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
                stackFrameReader.attach(create).makeReturnValueAccessor();
                EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
                stackFrameWriter.attach(create2, this.pos, this.range1.numReferences, this.range1.numBytes);
                EmulatedStackFrame.StackFrameAccessor.copyNext(stackFrameReader, stackFrameWriter, this.target.type().ptypes()[0]);
            }
            emulatedStackFrame.copyRangeTo(create2, this.range2, this.range1.numReferences + this.referencesOffset, this.range2.numBytes + this.stackFrameOffset);
            invokeFromTransform(this.target, create2);
            create2.copyReturnValueTo(emulatedStackFrame);
        }
    }

    static class FoldArguments extends Transformer {
        private final MethodHandle combiner;
        private final EmulatedStackFrame.Range combinerArgs;
        private final EmulatedStackFrame.Range leadingArgs;
        private final int position;
        private final int referencesOffset;
        private final int stackFrameOffset;
        private final MethodHandle target;
        private final EmulatedStackFrame.Range trailingArgs;

        FoldArguments(MethodHandle methodHandle, int i, MethodHandle methodHandle2) {
            super(deriveType(methodHandle, i, methodHandle2));
            this.target = methodHandle;
            this.combiner = methodHandle2;
            this.position = i;
            this.combinerArgs = EmulatedStackFrame.Range.m1687of(type(), i, methodHandle2.type().parameterCount() + i);
            this.leadingArgs = EmulatedStackFrame.Range.m1687of(type(), 0, i);
            this.trailingArgs = EmulatedStackFrame.Range.from(type(), i);
            Class<?> rtype = methodHandle2.type().rtype();
            if (rtype == Void.TYPE) {
                this.stackFrameOffset = 0;
                this.referencesOffset = 0;
            } else if (rtype.isPrimitive()) {
                this.stackFrameOffset = EmulatedStackFrame.getSize(rtype);
                this.referencesOffset = 0;
            } else {
                this.stackFrameOffset = 0;
                this.referencesOffset = 1;
            }
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame create = EmulatedStackFrame.create(this.combiner.type());
            emulatedStackFrame.copyRangeTo(create, this.combinerArgs, 0, 0);
            invokeExactFromTransform(this.combiner, create);
            EmulatedStackFrame create2 = EmulatedStackFrame.create(this.target.type());
            emulatedStackFrame.copyRangeTo(create2, this.leadingArgs, 0, 0);
            if (!(this.referencesOffset == 0 && this.stackFrameOffset == 0)) {
                EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
                stackFrameReader.attach(create).makeReturnValueAccessor();
                EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
                stackFrameWriter.attach(create2, this.position, this.leadingArgs.numReferences, this.leadingArgs.numBytes);
                EmulatedStackFrame.StackFrameAccessor.copyNext(stackFrameReader, stackFrameWriter, this.target.type().ptypes()[this.position]);
            }
            emulatedStackFrame.copyRangeTo(create2, this.trailingArgs, this.leadingArgs.numReferences + this.referencesOffset, this.leadingArgs.numBytes + this.stackFrameOffset);
            invokeExactFromTransform(this.target, create2);
            create2.copyReturnValueTo(emulatedStackFrame);
        }

        private static MethodType deriveType(MethodHandle methodHandle, int i, MethodHandle methodHandle2) {
            if (methodHandle2.type().rtype() == Void.TYPE) {
                return methodHandle.type();
            }
            return methodHandle.type().dropParameterTypes(i, i + 1);
        }
    }

    static class InsertArguments extends Transformer {
        private final int pos;
        private final EmulatedStackFrame.Range range1;
        private final EmulatedStackFrame.Range range2;
        private final MethodHandle target;
        private final Object[] values;

        InsertArguments(MethodHandle methodHandle, int i, Object[] objArr) {
            super(methodHandle.type().dropParameterTypes(i, objArr.length + i));
            this.target = methodHandle;
            this.pos = i;
            this.values = objArr;
            MethodType type = type();
            this.range1 = EmulatedStackFrame.Range.m1687of(type, 0, i);
            this.range2 = EmulatedStackFrame.Range.m1687of(type, i, type.parameterCount());
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame create = EmulatedStackFrame.create(this.target.type());
            emulatedStackFrame.copyRangeTo(create, this.range1, 0, 0);
            EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
            stackFrameWriter.attach(create, this.pos, this.range1.numReferences, this.range1.numBytes);
            Class[] ptypes = this.target.type().ptypes();
            int i = 0;
            int i2 = 0;
            for (int i3 = 0; i3 < this.values.length; i3++) {
                Class cls = ptypes[this.pos + i3];
                char basicTypeChar = Wrapper.basicTypeChar(cls);
                if (basicTypeChar == 'L') {
                    stackFrameWriter.putNextReference(this.values[i3], cls);
                    i++;
                } else {
                    if (basicTypeChar == 'F') {
                        stackFrameWriter.putNextFloat(((Float) this.values[i3]).floatValue());
                    } else if (basicTypeChar == 'S') {
                        stackFrameWriter.putNextShort(((Short) this.values[i3]).shortValue());
                    } else if (basicTypeChar == 'Z') {
                        stackFrameWriter.putNextBoolean(((Boolean) this.values[i3]).booleanValue());
                    } else if (basicTypeChar == 'I') {
                        stackFrameWriter.putNextInt(((Integer) this.values[i3]).intValue());
                    } else if (basicTypeChar != 'J') {
                        switch (basicTypeChar) {
                            case 'B':
                                stackFrameWriter.putNextByte(((Byte) this.values[i3]).byteValue());
                                break;
                            case 'C':
                                stackFrameWriter.putNextChar(((Character) this.values[i3]).charValue());
                                break;
                            case 'D':
                                stackFrameWriter.putNextDouble(((Double) this.values[i3]).doubleValue());
                                break;
                        }
                    } else {
                        stackFrameWriter.putNextLong(((Long) this.values[i3]).longValue());
                    }
                    i2 += EmulatedStackFrame.getSize(cls);
                }
            }
            EmulatedStackFrame.Range range = this.range2;
            if (range != null) {
                emulatedStackFrame.copyRangeTo(create, range, this.range1.numReferences + i, this.range1.numBytes + i2);
            }
            invokeFromTransform(this.target, create);
            create.copyReturnValueTo(emulatedStackFrame);
        }
    }

    static class AsTypeAdapter extends Transformer {
        private final MethodHandle target;

        AsTypeAdapter(MethodHandle methodHandle, MethodType methodType) {
            super(methodType);
            this.target = methodHandle;
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame create = EmulatedStackFrame.create(this.target.type());
            EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
            EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
            stackFrameReader.attach(emulatedStackFrame);
            stackFrameWriter.attach(create);
            adaptArguments(stackFrameReader, stackFrameWriter);
            invokeFromTransform(this.target, create);
            if (emulatedStackFrame.getMethodType().rtype() != Void.TYPE) {
                stackFrameReader.attach(create).makeReturnValueAccessor();
                stackFrameWriter.attach(emulatedStackFrame).makeReturnValueAccessor();
                adaptReturnValue(stackFrameReader, stackFrameWriter);
            }
        }

        private void adaptArguments(EmulatedStackFrame.StackFrameReader stackFrameReader, EmulatedStackFrame.StackFrameWriter stackFrameWriter) {
            Class[] ptypes = type().ptypes();
            Class[] ptypes2 = this.target.type().ptypes();
            for (int i = 0; i < ptypes.length; i++) {
                adaptArgument(stackFrameReader, ptypes[i], stackFrameWriter, ptypes2[i]);
            }
        }

        private void adaptReturnValue(EmulatedStackFrame.StackFrameReader stackFrameReader, EmulatedStackFrame.StackFrameWriter stackFrameWriter) {
            adaptArgument(stackFrameReader, this.target.type().rtype(), stackFrameWriter, type().rtype());
        }

        private void throwWrongMethodTypeException() throws WrongMethodTypeException {
            throw new WrongMethodTypeException("Cannot convert from " + type() + " to " + this.target.type());
        }

        private static void throwClassCastException(Class cls, Class cls2) throws ClassCastException {
            throw new ClassCastException("Cannot cast from " + cls + " to " + cls2);
        }

        private void writePrimitiveByteAs(EmulatedStackFrame.StackFrameWriter stackFrameWriter, char c, byte b) throws WrongMethodTypeException {
            if (c == 'B') {
                stackFrameWriter.putNextByte(b);
            } else if (c == 'D') {
                stackFrameWriter.putNextDouble((double) b);
            } else if (c == 'F') {
                stackFrameWriter.putNextFloat((float) b);
            } else if (c == 'S') {
                stackFrameWriter.putNextShort((short) b);
            } else if (c == 'I') {
                stackFrameWriter.putNextInt(b);
            } else if (c != 'J') {
                throwWrongMethodTypeException();
            } else {
                stackFrameWriter.putNextLong((long) b);
            }
        }

        private void writePrimitiveShortAs(EmulatedStackFrame.StackFrameWriter stackFrameWriter, char c, short s) throws WrongMethodTypeException {
            if (c == 'D') {
                stackFrameWriter.putNextDouble((double) s);
            } else if (c == 'F') {
                stackFrameWriter.putNextFloat((float) s);
            } else if (c == 'S') {
                stackFrameWriter.putNextShort(s);
            } else if (c == 'I') {
                stackFrameWriter.putNextInt(s);
            } else if (c != 'J') {
                throwWrongMethodTypeException();
            } else {
                stackFrameWriter.putNextLong((long) s);
            }
        }

        private void writePrimitiveCharAs(EmulatedStackFrame.StackFrameWriter stackFrameWriter, char c, char c2) throws WrongMethodTypeException {
            if (c == 'C') {
                stackFrameWriter.putNextChar(c2);
            } else if (c == 'D') {
                stackFrameWriter.putNextDouble((double) c2);
            } else if (c == 'F') {
                stackFrameWriter.putNextFloat((float) c2);
            } else if (c == 'I') {
                stackFrameWriter.putNextInt(c2);
            } else if (c != 'J') {
                throwWrongMethodTypeException();
            } else {
                stackFrameWriter.putNextLong((long) c2);
            }
        }

        private void writePrimitiveIntAs(EmulatedStackFrame.StackFrameWriter stackFrameWriter, char c, int i) throws WrongMethodTypeException {
            if (c == 'D') {
                stackFrameWriter.putNextDouble((double) i);
            } else if (c == 'F') {
                stackFrameWriter.putNextFloat((float) i);
            } else if (c == 'I') {
                stackFrameWriter.putNextInt(i);
            } else if (c != 'J') {
                throwWrongMethodTypeException();
                throwWrongMethodTypeException();
            } else {
                stackFrameWriter.putNextLong((long) i);
            }
        }

        private void writePrimitiveLongAs(EmulatedStackFrame.StackFrameWriter stackFrameWriter, char c, long j) throws WrongMethodTypeException {
            if (c == 'D') {
                stackFrameWriter.putNextDouble((double) j);
            } else if (c == 'F') {
                stackFrameWriter.putNextFloat((float) j);
            } else if (c != 'J') {
                throwWrongMethodTypeException();
            } else {
                stackFrameWriter.putNextLong(j);
            }
        }

        private void writePrimitiveFloatAs(EmulatedStackFrame.StackFrameWriter stackFrameWriter, char c, float f) throws WrongMethodTypeException {
            if (c == 'D') {
                stackFrameWriter.putNextDouble((double) f);
            } else if (c != 'F') {
                throwWrongMethodTypeException();
            } else {
                stackFrameWriter.putNextFloat(f);
            }
        }

        private void writePrimitiveDoubleAs(EmulatedStackFrame.StackFrameWriter stackFrameWriter, char c, double d) throws WrongMethodTypeException {
            if (c != 'D') {
                throwWrongMethodTypeException();
            } else {
                stackFrameWriter.putNextDouble(d);
            }
        }

        private void writePrimitiveVoidAs(EmulatedStackFrame.StackFrameWriter stackFrameWriter, char c) {
            if (c == 'F') {
                stackFrameWriter.putNextFloat(0.0f);
            } else if (c == 'S') {
                stackFrameWriter.putNextShort(0);
            } else if (c == 'Z') {
                stackFrameWriter.putNextBoolean(false);
            } else if (c == 'I') {
                stackFrameWriter.putNextInt(0);
            } else if (c != 'J') {
                switch (c) {
                    case 'B':
                        stackFrameWriter.putNextByte((byte) 0);
                        return;
                    case 'C':
                        stackFrameWriter.putNextChar(0);
                        return;
                    case 'D':
                        stackFrameWriter.putNextDouble(0.0d);
                        return;
                    default:
                        throwWrongMethodTypeException();
                        return;
                }
            } else {
                stackFrameWriter.putNextLong(0);
            }
        }

        private static Class getBoxedPrimitiveClass(char c) {
            if (c == 'F') {
                return Float.class;
            }
            if (c == 'S') {
                return Short.class;
            }
            if (c == 'Z') {
                return Boolean.class;
            }
            if (c == 'I') {
                return Integer.class;
            }
            if (c == 'J') {
                return Long.class;
            }
            switch (c) {
                case 'B':
                    return Byte.class;
                case 'C':
                    return Character.class;
                case 'D':
                    return Double.class;
                default:
                    return null;
            }
        }

        private void adaptArgument(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?> cls, EmulatedStackFrame.StackFrameWriter stackFrameWriter, Class<?> cls2) {
            Object obj;
            if (cls.equals(cls2)) {
                EmulatedStackFrame.StackFrameAccessor.copyNext(stackFrameReader, stackFrameWriter, cls);
            } else if (cls2.isPrimitive()) {
                if (cls.isPrimitive()) {
                    char basicTypeChar = Wrapper.basicTypeChar(cls);
                    char basicTypeChar2 = Wrapper.basicTypeChar(cls2);
                    if (basicTypeChar == 'B') {
                        writePrimitiveByteAs(stackFrameWriter, basicTypeChar2, stackFrameReader.nextByte());
                    } else if (basicTypeChar == 'C') {
                        writePrimitiveCharAs(stackFrameWriter, basicTypeChar2, stackFrameReader.nextChar());
                    } else if (basicTypeChar == 'F') {
                        writePrimitiveFloatAs(stackFrameWriter, basicTypeChar2, stackFrameReader.nextFloat());
                    } else if (basicTypeChar == 'S') {
                        writePrimitiveShortAs(stackFrameWriter, basicTypeChar2, stackFrameReader.nextShort());
                    } else if (basicTypeChar == 'V') {
                        writePrimitiveVoidAs(stackFrameWriter, basicTypeChar2);
                    } else if (basicTypeChar == 'I') {
                        writePrimitiveIntAs(stackFrameWriter, basicTypeChar2, stackFrameReader.nextInt());
                    } else if (basicTypeChar != 'J') {
                        throwWrongMethodTypeException();
                    } else {
                        writePrimitiveLongAs(stackFrameWriter, basicTypeChar2, stackFrameReader.nextLong());
                    }
                } else {
                    Object nextReference = stackFrameReader.nextReference(Object.class);
                    if (cls2 != Void.TYPE) {
                        nextReference.getClass();
                        if (!Wrapper.isWrapperType(nextReference.getClass())) {
                            throwClassCastException(nextReference.getClass(), cls2);
                        }
                        Wrapper forWrapperType = Wrapper.forWrapperType(nextReference.getClass());
                        Wrapper forPrimitiveType = Wrapper.forPrimitiveType(cls2);
                        if (!forPrimitiveType.isConvertibleFrom(forWrapperType)) {
                            throwClassCastException(cls, cls2);
                        }
                        char basicTypeChar3 = forPrimitiveType.basicTypeChar();
                        char basicTypeChar4 = forWrapperType.basicTypeChar();
                        if (basicTypeChar4 == 'F') {
                            writePrimitiveFloatAs(stackFrameWriter, basicTypeChar3, ((Float) nextReference).floatValue());
                        } else if (basicTypeChar4 == 'S') {
                            writePrimitiveShortAs(stackFrameWriter, basicTypeChar3, ((Short) nextReference).shortValue());
                        } else if (basicTypeChar4 == 'Z') {
                            stackFrameWriter.putNextBoolean(((Boolean) nextReference).booleanValue());
                        } else if (basicTypeChar4 == 'I') {
                            writePrimitiveIntAs(stackFrameWriter, basicTypeChar3, ((Integer) nextReference).intValue());
                        } else if (basicTypeChar4 != 'J') {
                            switch (basicTypeChar4) {
                                case 'B':
                                    writePrimitiveByteAs(stackFrameWriter, basicTypeChar3, ((Byte) nextReference).byteValue());
                                    return;
                                case 'C':
                                    writePrimitiveCharAs(stackFrameWriter, basicTypeChar3, ((Character) nextReference).charValue());
                                    return;
                                case 'D':
                                    writePrimitiveDoubleAs(stackFrameWriter, basicTypeChar3, ((Double) nextReference).doubleValue());
                                    return;
                                default:
                                    throw new IllegalStateException();
                            }
                        } else {
                            writePrimitiveLongAs(stackFrameWriter, basicTypeChar3, ((Long) nextReference).longValue());
                        }
                    }
                }
            } else if (cls.isPrimitive()) {
                char basicTypeChar5 = Wrapper.basicTypeChar(cls);
                Class boxedPrimitiveClass = getBoxedPrimitiveClass(basicTypeChar5);
                if (boxedPrimitiveClass != null && !cls2.isAssignableFrom(boxedPrimitiveClass)) {
                    throwWrongMethodTypeException();
                }
                if (basicTypeChar5 == 'F') {
                    obj = Float.valueOf(stackFrameReader.nextFloat());
                } else if (basicTypeChar5 == 'S') {
                    obj = Short.valueOf(stackFrameReader.nextShort());
                } else if (basicTypeChar5 == 'V') {
                    obj = null;
                } else if (basicTypeChar5 == 'Z') {
                    obj = Boolean.valueOf(stackFrameReader.nextBoolean());
                } else if (basicTypeChar5 == 'I') {
                    obj = Integer.valueOf(stackFrameReader.nextInt());
                } else if (basicTypeChar5 != 'J') {
                    switch (basicTypeChar5) {
                        case 'B':
                            obj = Byte.valueOf(stackFrameReader.nextByte());
                            break;
                        case 'C':
                            obj = Character.valueOf(stackFrameReader.nextChar());
                            break;
                        case 'D':
                            obj = Double.valueOf(stackFrameReader.nextDouble());
                            break;
                        default:
                            throw new IllegalStateException();
                    }
                } else {
                    obj = Long.valueOf(stackFrameReader.nextLong());
                }
                stackFrameWriter.putNextReference(obj, cls2);
            } else {
                Object nextReference2 = stackFrameReader.nextReference(Object.class);
                if (nextReference2 != null && !cls2.isAssignableFrom(nextReference2.getClass())) {
                    throwClassCastException(nextReference2.getClass(), cls2);
                }
                stackFrameWriter.putNextReference(nextReference2, cls2);
            }
        }
    }

    static class ExplicitCastArguments extends Transformer {
        private final MethodHandle target;

        private static boolean toBoolean(byte b) {
            return (b & 1) == 1;
        }

        ExplicitCastArguments(MethodHandle methodHandle, MethodType methodType) {
            super(methodType);
            this.target = methodHandle;
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame create = EmulatedStackFrame.create(this.target.type());
            explicitCastArguments(emulatedStackFrame, create);
            invokeFromTransform(this.target, create);
            explicitCastReturnValue(emulatedStackFrame, create);
        }

        private void explicitCastArguments(EmulatedStackFrame emulatedStackFrame, EmulatedStackFrame emulatedStackFrame2) {
            EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
            stackFrameReader.attach(emulatedStackFrame);
            EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
            stackFrameWriter.attach(emulatedStackFrame2);
            Class[] ptypes = type().ptypes();
            Class[] ptypes2 = this.target.type().ptypes();
            for (int i = 0; i < ptypes.length; i++) {
                explicitCast(stackFrameReader, ptypes[i], stackFrameWriter, ptypes2[i]);
            }
        }

        private void explicitCastReturnValue(EmulatedStackFrame emulatedStackFrame, EmulatedStackFrame emulatedStackFrame2) {
            Class<?> rtype = this.target.type().rtype();
            Class<?> rtype2 = type().rtype();
            if (rtype2 != Void.TYPE) {
                EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
                stackFrameWriter.attach(emulatedStackFrame);
                stackFrameWriter.makeReturnValueAccessor();
                if (rtype != Void.TYPE) {
                    EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
                    stackFrameReader.attach(emulatedStackFrame2);
                    stackFrameReader.makeReturnValueAccessor();
                    explicitCast(stackFrameReader, this.target.type().rtype(), stackFrameWriter, type().rtype());
                } else if (rtype2.isPrimitive()) {
                    unboxNull(stackFrameWriter, rtype2);
                } else {
                    stackFrameWriter.putNextReference((Object) null, rtype2);
                }
            }
        }

        private static void throwUnexpectedType(Class<?> cls) {
            throw new InternalError("Unexpected type: " + cls);
        }

        private static void badCast(Class<?> cls, Class<?> cls2) {
            throw new ClassCastException("Cannot cast " + cls.getName() + " to " + cls2.getName());
        }

        private static byte readPrimitiveAsByte(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?> cls) {
            char basicTypeChar = Wrapper.basicTypeChar(cls);
            if (basicTypeChar == 'F') {
                return (byte) ((int) stackFrameReader.nextFloat());
            }
            if (basicTypeChar == 'S') {
                return (byte) stackFrameReader.nextShort();
            }
            if (basicTypeChar == 'Z') {
                return stackFrameReader.nextBoolean() ? (byte) 1 : 0;
            }
            if (basicTypeChar == 'I') {
                return (byte) stackFrameReader.nextInt();
            }
            if (basicTypeChar == 'J') {
                return (byte) ((int) stackFrameReader.nextLong());
            }
            switch (basicTypeChar) {
                case 'B':
                    return stackFrameReader.nextByte();
                case 'C':
                    return (byte) stackFrameReader.nextChar();
                case 'D':
                    return (byte) ((int) stackFrameReader.nextDouble());
                default:
                    throwUnexpectedType(cls);
                    return 0;
            }
        }

        private static char readPrimitiveAsChar(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?> cls) {
            char basicTypeChar = Wrapper.basicTypeChar(cls);
            if (basicTypeChar == 'F') {
                return (char) ((int) stackFrameReader.nextFloat());
            }
            if (basicTypeChar == 'S') {
                return (char) stackFrameReader.nextShort();
            }
            if (basicTypeChar == 'Z') {
                return stackFrameReader.nextBoolean() ? (char) 1 : 0;
            }
            if (basicTypeChar == 'I') {
                return (char) stackFrameReader.nextInt();
            }
            if (basicTypeChar == 'J') {
                return (char) ((int) stackFrameReader.nextLong());
            }
            switch (basicTypeChar) {
                case 'B':
                    return (char) stackFrameReader.nextByte();
                case 'C':
                    return stackFrameReader.nextChar();
                case 'D':
                    return (char) ((int) stackFrameReader.nextDouble());
                default:
                    throwUnexpectedType(cls);
                    return 0;
            }
        }

        private static short readPrimitiveAsShort(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?> cls) {
            char basicTypeChar = Wrapper.basicTypeChar(cls);
            if (basicTypeChar == 'F') {
                return (short) ((int) stackFrameReader.nextFloat());
            }
            if (basicTypeChar == 'S') {
                return stackFrameReader.nextShort();
            }
            if (basicTypeChar == 'Z') {
                return stackFrameReader.nextBoolean() ? (short) 1 : 0;
            }
            if (basicTypeChar == 'I') {
                return (short) stackFrameReader.nextInt();
            }
            if (basicTypeChar == 'J') {
                return (short) ((int) stackFrameReader.nextLong());
            }
            switch (basicTypeChar) {
                case 'B':
                    return (short) stackFrameReader.nextByte();
                case 'C':
                    return (short) stackFrameReader.nextChar();
                case 'D':
                    return (short) ((int) stackFrameReader.nextDouble());
                default:
                    throwUnexpectedType(cls);
                    return 0;
            }
        }

        private static int readPrimitiveAsInt(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?> cls) {
            char basicTypeChar = Wrapper.basicTypeChar(cls);
            if (basicTypeChar == 'F') {
                return (int) stackFrameReader.nextFloat();
            }
            if (basicTypeChar == 'S') {
                return stackFrameReader.nextShort();
            }
            if (basicTypeChar == 'Z') {
                return stackFrameReader.nextBoolean() ? 1 : 0;
            }
            if (basicTypeChar == 'I') {
                return stackFrameReader.nextInt();
            }
            if (basicTypeChar == 'J') {
                return (int) stackFrameReader.nextLong();
            }
            switch (basicTypeChar) {
                case 'B':
                    return stackFrameReader.nextByte();
                case 'C':
                    return stackFrameReader.nextChar();
                case 'D':
                    return (int) stackFrameReader.nextDouble();
                default:
                    throwUnexpectedType(cls);
                    return 0;
            }
        }

        private static long readPrimitiveAsLong(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?> cls) {
            char basicTypeChar = Wrapper.basicTypeChar(cls);
            if (basicTypeChar == 'F') {
                return (long) stackFrameReader.nextFloat();
            }
            if (basicTypeChar == 'S') {
                return (long) stackFrameReader.nextShort();
            }
            if (basicTypeChar != 'Z') {
                if (basicTypeChar == 'I') {
                    return (long) stackFrameReader.nextInt();
                }
                if (basicTypeChar == 'J') {
                    return stackFrameReader.nextLong();
                }
                switch (basicTypeChar) {
                    case 'B':
                        return (long) stackFrameReader.nextByte();
                    case 'C':
                        return (long) stackFrameReader.nextChar();
                    case 'D':
                        return (long) stackFrameReader.nextDouble();
                    default:
                        throwUnexpectedType(cls);
                        return 0;
                }
            } else if (stackFrameReader.nextBoolean()) {
                return 1;
            } else {
                return 0;
            }
        }

        private static float readPrimitiveAsFloat(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?> cls) {
            char basicTypeChar = Wrapper.basicTypeChar(cls);
            if (basicTypeChar == 'F') {
                return stackFrameReader.nextFloat();
            }
            if (basicTypeChar == 'S') {
                return (float) stackFrameReader.nextShort();
            }
            if (basicTypeChar != 'Z') {
                if (basicTypeChar == 'I') {
                    return (float) stackFrameReader.nextInt();
                }
                if (basicTypeChar == 'J') {
                    return (float) stackFrameReader.nextLong();
                }
                switch (basicTypeChar) {
                    case 'B':
                        return (float) stackFrameReader.nextByte();
                    case 'C':
                        return (float) stackFrameReader.nextChar();
                    case 'D':
                        return (float) stackFrameReader.nextDouble();
                    default:
                        throwUnexpectedType(cls);
                        return 0.0f;
                }
            } else if (stackFrameReader.nextBoolean()) {
                return 1.0f;
            } else {
                return 0.0f;
            }
        }

        private static double readPrimitiveAsDouble(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?> cls) {
            char basicTypeChar = Wrapper.basicTypeChar(cls);
            if (basicTypeChar == 'F') {
                return (double) stackFrameReader.nextFloat();
            }
            if (basicTypeChar == 'S') {
                return (double) stackFrameReader.nextShort();
            }
            if (basicTypeChar != 'Z') {
                if (basicTypeChar == 'I') {
                    return (double) stackFrameReader.nextInt();
                }
                if (basicTypeChar == 'J') {
                    return (double) stackFrameReader.nextLong();
                }
                switch (basicTypeChar) {
                    case 'B':
                        return (double) stackFrameReader.nextByte();
                    case 'C':
                        return (double) stackFrameReader.nextChar();
                    case 'D':
                        return stackFrameReader.nextDouble();
                    default:
                        throwUnexpectedType(cls);
                        return 0.0d;
                }
            } else if (stackFrameReader.nextBoolean()) {
                return 1.0d;
            } else {
                return 0.0d;
            }
        }

        private static void explicitCastPrimitives(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?> cls, EmulatedStackFrame.StackFrameWriter stackFrameWriter, Class<?> cls2) {
            char basicTypeChar = Wrapper.basicTypeChar(cls2);
            if (basicTypeChar == 'F') {
                stackFrameWriter.putNextFloat(readPrimitiveAsFloat(stackFrameReader, cls));
            } else if (basicTypeChar == 'S') {
                stackFrameWriter.putNextShort(readPrimitiveAsShort(stackFrameReader, cls));
            } else if (basicTypeChar == 'Z') {
                stackFrameWriter.putNextBoolean(toBoolean(readPrimitiveAsByte(stackFrameReader, cls)));
            } else if (basicTypeChar == 'I') {
                stackFrameWriter.putNextInt(readPrimitiveAsInt(stackFrameReader, cls));
            } else if (basicTypeChar != 'J') {
                switch (basicTypeChar) {
                    case 'B':
                        stackFrameWriter.putNextByte(readPrimitiveAsByte(stackFrameReader, cls));
                        return;
                    case 'C':
                        stackFrameWriter.putNextChar(readPrimitiveAsChar(stackFrameReader, cls));
                        return;
                    case 'D':
                        stackFrameWriter.putNextDouble(readPrimitiveAsDouble(stackFrameReader, cls));
                        return;
                    default:
                        throwUnexpectedType(cls2);
                        return;
                }
            } else {
                stackFrameWriter.putNextLong(readPrimitiveAsLong(stackFrameReader, cls));
            }
        }

        private static void unboxNull(EmulatedStackFrame.StackFrameWriter stackFrameWriter, Class<?> cls) {
            char basicTypeChar = Wrapper.basicTypeChar(cls);
            if (basicTypeChar == 'F') {
                stackFrameWriter.putNextFloat(0.0f);
            } else if (basicTypeChar == 'S') {
                stackFrameWriter.putNextShort(0);
            } else if (basicTypeChar == 'Z') {
                stackFrameWriter.putNextBoolean(false);
            } else if (basicTypeChar == 'I') {
                stackFrameWriter.putNextInt(0);
            } else if (basicTypeChar != 'J') {
                switch (basicTypeChar) {
                    case 'B':
                        stackFrameWriter.putNextByte((byte) 0);
                        return;
                    case 'C':
                        stackFrameWriter.putNextChar(0);
                        return;
                    case 'D':
                        stackFrameWriter.putNextDouble(0.0d);
                        return;
                    default:
                        throwUnexpectedType(cls);
                        return;
                }
            } else {
                stackFrameWriter.putNextLong(0);
            }
        }

        private static void unboxNonNull(Object obj, EmulatedStackFrame.StackFrameWriter stackFrameWriter, Class<?> cls) {
            Class<?> cls2 = obj.getClass();
            char basicTypeChar = Wrapper.basicTypeChar(Wrapper.asPrimitiveType(cls2));
            boolean z = false;
            if (basicTypeChar == 'F') {
                float floatValue = ((Float) obj).floatValue();
                char basicTypeChar2 = Wrapper.basicTypeChar(cls);
                if (basicTypeChar2 == 'F') {
                    stackFrameWriter.putNextFloat(floatValue);
                } else if (basicTypeChar2 == 'S') {
                    stackFrameWriter.putNextShort((short) ((int) floatValue));
                } else if (basicTypeChar2 == 'Z') {
                    if ((((byte) ((int) floatValue)) & 1) != 0) {
                        z = true;
                    }
                    stackFrameWriter.putNextBoolean(z);
                } else if (basicTypeChar2 == 'I') {
                    stackFrameWriter.putNextInt((int) floatValue);
                } else if (basicTypeChar2 != 'J') {
                    switch (basicTypeChar2) {
                        case 'B':
                            stackFrameWriter.putNextByte((byte) ((int) floatValue));
                            return;
                        case 'C':
                            stackFrameWriter.putNextChar((char) ((int) floatValue));
                            return;
                        case 'D':
                            stackFrameWriter.putNextDouble((double) floatValue);
                            return;
                        default:
                            badCast(cls2, cls);
                            return;
                    }
                } else {
                    stackFrameWriter.putNextLong((long) floatValue);
                }
            } else if (basicTypeChar != 'S') {
                long j = 1;
                if (basicTypeChar == 'Z') {
                    boolean booleanValue = ((Boolean) obj).booleanValue();
                    char basicTypeChar3 = Wrapper.basicTypeChar(cls);
                    if (basicTypeChar3 == 'F') {
                        stackFrameWriter.putNextFloat(booleanValue ? 1.0f : 0.0f);
                    } else if (basicTypeChar3 == 'S') {
                        stackFrameWriter.putNextShort(booleanValue ? (short) 1 : 0);
                    } else if (basicTypeChar3 == 'Z') {
                        stackFrameWriter.putNextBoolean(booleanValue);
                    } else if (basicTypeChar3 == 'I') {
                        stackFrameWriter.putNextInt(booleanValue ? 1 : 0);
                    } else if (basicTypeChar3 != 'J') {
                        switch (basicTypeChar3) {
                            case 'B':
                                stackFrameWriter.putNextByte(booleanValue ? (byte) 1 : 0);
                                return;
                            case 'C':
                                stackFrameWriter.putNextChar(booleanValue ? (char) 1 : 0);
                                return;
                            case 'D':
                                stackFrameWriter.putNextDouble(booleanValue ? 1.0d : 0.0d);
                                return;
                            default:
                                badCast(cls2, cls);
                                return;
                        }
                    } else {
                        if (!booleanValue) {
                            j = 0;
                        }
                        stackFrameWriter.putNextLong(j);
                    }
                } else if (basicTypeChar == 'I') {
                    int intValue = ((Integer) obj).intValue();
                    char basicTypeChar4 = Wrapper.basicTypeChar(cls);
                    if (basicTypeChar4 == 'F') {
                        stackFrameWriter.putNextFloat((float) intValue);
                    } else if (basicTypeChar4 == 'S') {
                        stackFrameWriter.putNextShort((short) intValue);
                    } else if (basicTypeChar4 == 'Z') {
                        if ((intValue & 1) == 1) {
                            z = true;
                        }
                        stackFrameWriter.putNextBoolean(z);
                    } else if (basicTypeChar4 == 'I') {
                        stackFrameWriter.putNextInt(intValue);
                    } else if (basicTypeChar4 != 'J') {
                        switch (basicTypeChar4) {
                            case 'B':
                                stackFrameWriter.putNextByte((byte) intValue);
                                return;
                            case 'C':
                                stackFrameWriter.putNextChar((char) intValue);
                                return;
                            case 'D':
                                stackFrameWriter.putNextDouble((double) intValue);
                                return;
                            default:
                                badCast(cls2, cls);
                                return;
                        }
                    } else {
                        stackFrameWriter.putNextLong((long) intValue);
                    }
                } else if (basicTypeChar != 'J') {
                    switch (basicTypeChar) {
                        case 'B':
                            byte byteValue = ((Byte) obj).byteValue();
                            char basicTypeChar5 = Wrapper.basicTypeChar(cls);
                            if (basicTypeChar5 == 'F') {
                                stackFrameWriter.putNextFloat((float) byteValue);
                                return;
                            } else if (basicTypeChar5 == 'S') {
                                stackFrameWriter.putNextShort((short) byteValue);
                                return;
                            } else if (basicTypeChar5 == 'Z') {
                                stackFrameWriter.putNextBoolean(toBoolean(byteValue));
                                return;
                            } else if (basicTypeChar5 == 'I') {
                                stackFrameWriter.putNextInt(byteValue);
                                return;
                            } else if (basicTypeChar5 != 'J') {
                                switch (basicTypeChar5) {
                                    case 'B':
                                        stackFrameWriter.putNextByte(byteValue);
                                        return;
                                    case 'C':
                                        stackFrameWriter.putNextChar((char) byteValue);
                                        return;
                                    case 'D':
                                        stackFrameWriter.putNextDouble((double) byteValue);
                                        return;
                                    default:
                                        badCast(cls2, cls);
                                        return;
                                }
                            } else {
                                stackFrameWriter.putNextLong((long) byteValue);
                                return;
                            }
                        case 'C':
                            char charValue = ((Character) obj).charValue();
                            char basicTypeChar6 = Wrapper.basicTypeChar(cls);
                            if (basicTypeChar6 == 'F') {
                                stackFrameWriter.putNextFloat((float) charValue);
                                return;
                            } else if (basicTypeChar6 == 'S') {
                                stackFrameWriter.putNextShort((short) charValue);
                                return;
                            } else if (basicTypeChar6 == 'Z') {
                                if ((charValue & 1) == 1) {
                                    z = true;
                                }
                                stackFrameWriter.putNextBoolean(z);
                                return;
                            } else if (basicTypeChar6 == 'I') {
                                stackFrameWriter.putNextInt(charValue);
                                return;
                            } else if (basicTypeChar6 != 'J') {
                                switch (basicTypeChar6) {
                                    case 'B':
                                        stackFrameWriter.putNextByte((byte) charValue);
                                        return;
                                    case 'C':
                                        stackFrameWriter.putNextChar(charValue);
                                        return;
                                    case 'D':
                                        stackFrameWriter.putNextDouble((double) charValue);
                                        return;
                                    default:
                                        badCast(cls2, cls);
                                        return;
                                }
                            } else {
                                stackFrameWriter.putNextLong((long) charValue);
                                return;
                            }
                        case 'D':
                            double doubleValue = ((Double) obj).doubleValue();
                            char basicTypeChar7 = Wrapper.basicTypeChar(cls);
                            if (basicTypeChar7 == 'F') {
                                stackFrameWriter.putNextFloat((float) doubleValue);
                                return;
                            } else if (basicTypeChar7 == 'S') {
                                stackFrameWriter.putNextShort((short) ((int) doubleValue));
                                return;
                            } else if (basicTypeChar7 == 'Z') {
                                if ((((byte) ((int) doubleValue)) & 1) != 0) {
                                    z = true;
                                }
                                stackFrameWriter.putNextBoolean(z);
                                return;
                            } else if (basicTypeChar7 == 'I') {
                                stackFrameWriter.putNextInt((int) doubleValue);
                                return;
                            } else if (basicTypeChar7 != 'J') {
                                switch (basicTypeChar7) {
                                    case 'B':
                                        stackFrameWriter.putNextByte((byte) ((int) doubleValue));
                                        return;
                                    case 'C':
                                        stackFrameWriter.putNextChar((char) ((int) doubleValue));
                                        return;
                                    case 'D':
                                        stackFrameWriter.putNextDouble(doubleValue);
                                        return;
                                    default:
                                        badCast(cls2, cls);
                                        return;
                                }
                            } else {
                                stackFrameWriter.putNextLong((long) doubleValue);
                                return;
                            }
                        default:
                            badCast(cls2, cls);
                            return;
                    }
                } else {
                    long longValue = ((Long) obj).longValue();
                    char basicTypeChar8 = Wrapper.basicTypeChar(cls);
                    if (basicTypeChar8 == 'F') {
                        stackFrameWriter.putNextFloat((float) longValue);
                    } else if (basicTypeChar8 == 'S') {
                        stackFrameWriter.putNextShort((short) ((int) longValue));
                    } else if (basicTypeChar8 == 'Z') {
                        if ((longValue & 1) == 1) {
                            z = true;
                        }
                        stackFrameWriter.putNextBoolean(z);
                    } else if (basicTypeChar8 == 'I') {
                        stackFrameWriter.putNextInt((int) longValue);
                    } else if (basicTypeChar8 != 'J') {
                        switch (basicTypeChar8) {
                            case 'B':
                                stackFrameWriter.putNextByte((byte) ((int) longValue));
                                return;
                            case 'C':
                                stackFrameWriter.putNextChar((char) ((int) longValue));
                                return;
                            case 'D':
                                stackFrameWriter.putNextDouble((double) longValue);
                                return;
                            default:
                                badCast(cls2, cls);
                                return;
                        }
                    } else {
                        stackFrameWriter.putNextLong(longValue);
                    }
                }
            } else {
                short shortValue = ((Short) obj).shortValue();
                char basicTypeChar9 = Wrapper.basicTypeChar(cls);
                if (basicTypeChar9 == 'F') {
                    stackFrameWriter.putNextFloat((float) shortValue);
                } else if (basicTypeChar9 == 'S') {
                    stackFrameWriter.putNextShort(shortValue);
                } else if (basicTypeChar9 == 'Z') {
                    if ((shortValue & 1) == 1) {
                        z = true;
                    }
                    stackFrameWriter.putNextBoolean(z);
                } else if (basicTypeChar9 == 'I') {
                    stackFrameWriter.putNextInt(shortValue);
                } else if (basicTypeChar9 != 'J') {
                    switch (basicTypeChar9) {
                        case 'B':
                            stackFrameWriter.putNextByte((byte) shortValue);
                            return;
                        case 'C':
                            stackFrameWriter.putNextChar((char) shortValue);
                            return;
                        case 'D':
                            stackFrameWriter.putNextDouble((double) shortValue);
                            return;
                        default:
                            badCast(cls2, cls);
                            return;
                    }
                } else {
                    stackFrameWriter.putNextLong((long) shortValue);
                }
            }
        }

        private static void unbox(Object obj, EmulatedStackFrame.StackFrameWriter stackFrameWriter, Class<?> cls) {
            if (obj == null) {
                unboxNull(stackFrameWriter, cls);
            } else {
                unboxNonNull(obj, stackFrameWriter, cls);
            }
        }

        private static void box(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?> cls, EmulatedStackFrame.StackFrameWriter stackFrameWriter, Class<?> cls2) {
            Object obj;
            char basicTypeChar = Wrapper.basicTypeChar(cls);
            if (basicTypeChar == 'F') {
                obj = Float.valueOf(stackFrameReader.nextFloat());
            } else if (basicTypeChar == 'S') {
                obj = Short.valueOf(stackFrameReader.nextShort());
            } else if (basicTypeChar == 'Z') {
                obj = Boolean.valueOf(stackFrameReader.nextBoolean());
            } else if (basicTypeChar == 'I') {
                obj = Integer.valueOf(stackFrameReader.nextInt());
            } else if (basicTypeChar != 'J') {
                switch (basicTypeChar) {
                    case 'B':
                        obj = Byte.valueOf(stackFrameReader.nextByte());
                        break;
                    case 'C':
                        obj = Character.valueOf(stackFrameReader.nextChar());
                        break;
                    case 'D':
                        obj = Double.valueOf(stackFrameReader.nextDouble());
                        break;
                    default:
                        throwUnexpectedType(cls);
                        obj = null;
                        break;
                }
            } else {
                obj = Long.valueOf(stackFrameReader.nextLong());
            }
            stackFrameWriter.putNextReference(cls2.cast(obj), cls2);
        }

        private static void explicitCast(EmulatedStackFrame.StackFrameReader stackFrameReader, Class<?> cls, EmulatedStackFrame.StackFrameWriter stackFrameWriter, Class<?> cls2) {
            if (cls.equals(cls2)) {
                EmulatedStackFrame.StackFrameAccessor.copyNext(stackFrameReader, stackFrameWriter, cls);
            } else if (!cls.isPrimitive()) {
                Object nextReference = stackFrameReader.nextReference(cls);
                if (cls2.isPrimitive()) {
                    unbox(nextReference, stackFrameWriter, cls2);
                } else if (cls2.isInterface()) {
                    stackFrameWriter.putNextReference(nextReference, cls2);
                } else {
                    stackFrameWriter.putNextReference(cls2.cast(nextReference), cls2);
                }
            } else if (cls2.isPrimitive()) {
                explicitCastPrimitives(stackFrameReader, cls, stackFrameWriter, cls2);
            } else {
                box(stackFrameReader, cls, stackFrameWriter, cls2);
            }
        }
    }

    static class Loop extends Transformer {
        final MethodHandle[] finis;
        final MethodHandle[] inits;
        final EmulatedStackFrame.Range loopVarsRange;
        final MethodType loopVarsType;
        final MethodHandle[] preds;
        final MethodHandle[] steps;
        final EmulatedStackFrame.Range suffixRange = EmulatedStackFrame.Range.all(type());

        public Loop(Class<?> cls, List<Class<?>> list, MethodHandle[] methodHandleArr, MethodHandle[] methodHandleArr2, MethodHandle[] methodHandleArr3, MethodHandle[] methodHandleArr4) {
            super(MethodType.methodType(cls, list));
            this.inits = methodHandleArr;
            this.steps = methodHandleArr2;
            this.preds = methodHandleArr3;
            this.finis = methodHandleArr4;
            MethodType deduceLoopVarsType = deduceLoopVarsType(methodHandleArr);
            this.loopVarsType = deduceLoopVarsType;
            this.loopVarsRange = EmulatedStackFrame.Range.all(deduceLoopVarsType);
        }

        public void transform(EmulatedStackFrame emulatedStackFrame) throws Throwable {
            EmulatedStackFrame create = EmulatedStackFrame.create(this.loopVarsType);
            EmulatedStackFrame.StackFrameWriter stackFrameWriter = new EmulatedStackFrame.StackFrameWriter();
            init(emulatedStackFrame, create, stackFrameWriter);
            while (true) {
                stackFrameWriter.attach(create);
                int i = 0;
                while (true) {
                    MethodHandle[] methodHandleArr = this.steps;
                    if (i < methodHandleArr.length) {
                        doStep(methodHandleArr[i], emulatedStackFrame, create, stackFrameWriter);
                        if (!doPredicate(this.preds[i], emulatedStackFrame, create)) {
                            doFinish(this.finis[i], emulatedStackFrame, create);
                            return;
                        }
                        i++;
                    }
                }
            }
        }

        private static MethodType deduceLoopVarsType(MethodHandle[] methodHandleArr) {
            ArrayList arrayList = new ArrayList(methodHandleArr.length);
            for (MethodHandle type : methodHandleArr) {
                Class<?> returnType = type.type().returnType();
                if (returnType != Void.TYPE) {
                    arrayList.add(returnType);
                }
            }
            return MethodType.methodType((Class<?>) Void.TYPE, (List<Class<?>>) arrayList);
        }

        private void init(EmulatedStackFrame emulatedStackFrame, EmulatedStackFrame emulatedStackFrame2, EmulatedStackFrame.StackFrameWriter stackFrameWriter) throws Throwable {
            stackFrameWriter.attach(emulatedStackFrame2);
            for (MethodHandle methodHandle : this.inits) {
                EmulatedStackFrame create = EmulatedStackFrame.create(methodHandle.type());
                emulatedStackFrame.copyRangeTo(create, this.suffixRange, 0, 0);
                invokeExactFromTransform(methodHandle, create);
                Class<?> returnType = methodHandle.type().returnType();
                if (returnType != Void.TYPE) {
                    EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
                    stackFrameReader.attach(create).makeReturnValueAccessor();
                    EmulatedStackFrame.StackFrameAccessor.copyNext(stackFrameReader, stackFrameWriter, returnType);
                }
            }
        }

        private EmulatedStackFrame prepareFrame(MethodType methodType, EmulatedStackFrame emulatedStackFrame, EmulatedStackFrame emulatedStackFrame2) {
            EmulatedStackFrame create = EmulatedStackFrame.create(methodType);
            emulatedStackFrame2.copyRangeTo(create, this.loopVarsRange, 0, 0);
            emulatedStackFrame.copyRangeTo(create, this.suffixRange, this.loopVarsRange.numReferences, this.loopVarsRange.numBytes);
            return create;
        }

        private void doStep(MethodHandle methodHandle, EmulatedStackFrame emulatedStackFrame, EmulatedStackFrame emulatedStackFrame2, EmulatedStackFrame.StackFrameWriter stackFrameWriter) throws Throwable {
            EmulatedStackFrame prepareFrame = prepareFrame(methodHandle.type(), emulatedStackFrame, emulatedStackFrame2);
            invokeExactFromTransform(methodHandle, prepareFrame);
            Class<?> returnType = methodHandle.type().returnType();
            if (returnType != Void.TYPE) {
                EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
                stackFrameReader.attach(prepareFrame).makeReturnValueAccessor();
                EmulatedStackFrame.StackFrameAccessor.copyNext(stackFrameReader, stackFrameWriter, returnType);
            }
        }

        private boolean doPredicate(MethodHandle methodHandle, EmulatedStackFrame emulatedStackFrame, EmulatedStackFrame emulatedStackFrame2) throws Throwable {
            EmulatedStackFrame prepareFrame = prepareFrame(methodHandle.type(), emulatedStackFrame, emulatedStackFrame2);
            invokeExactFromTransform(methodHandle, prepareFrame);
            EmulatedStackFrame.StackFrameReader stackFrameReader = new EmulatedStackFrame.StackFrameReader();
            stackFrameReader.attach(prepareFrame).makeReturnValueAccessor();
            return stackFrameReader.nextBoolean();
        }

        private void doFinish(MethodHandle methodHandle, EmulatedStackFrame emulatedStackFrame, EmulatedStackFrame emulatedStackFrame2) throws Throwable {
            EmulatedStackFrame prepareFrame = prepareFrame(methodHandle.type(), emulatedStackFrame, emulatedStackFrame2);
            invokeExactFromTransform(methodHandle, prepareFrame);
            prepareFrame.copyReturnValueTo(emulatedStackFrame);
        }
    }
}
