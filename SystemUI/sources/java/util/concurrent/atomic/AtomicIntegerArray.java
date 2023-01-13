package java.util.concurrent.atomic;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.p026io.Serializable;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;

public class AtomicIntegerArray implements Serializable {

    /* renamed from: AA */
    private static final VarHandle f760AA = MethodHandles.arrayElementVarHandle(int[].class);
    private static final long serialVersionUID = 2862133569453604235L;
    private final int[] array;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.addAndGet(int, int):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final int addAndGet(int r1, int r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.addAndGet(int, int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.addAndGet(int, int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.compareAndExchange(int, int, int):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final int compareAndExchange(int r1, int r2, int r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.compareAndExchange(int, int, int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.compareAndExchange(int, int, int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.compareAndExchangeAcquire(int, int, int):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final int compareAndExchangeAcquire(int r1, int r2, int r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.compareAndExchangeAcquire(int, int, int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.compareAndExchangeAcquire(int, int, int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.compareAndExchangeRelease(int, int, int):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final int compareAndExchangeRelease(int r1, int r2, int r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.compareAndExchangeRelease(int, int, int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.compareAndExchangeRelease(int, int, int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.compareAndSet(int, int, int):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final boolean compareAndSet(int r1, int r2, int r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.compareAndSet(int, int, int):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.compareAndSet(int, int, int):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.decrementAndGet(int):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final int decrementAndGet(int r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.decrementAndGet(int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.decrementAndGet(int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.get(int):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final int get(int r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.get(int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.get(int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.getAcquire(int):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final int getAcquire(int r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.getAcquire(int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.getAcquire(int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.getAndAdd(int, int):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final int getAndAdd(int r1, int r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.getAndAdd(int, int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.getAndAdd(int, int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.getAndDecrement(int):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final int getAndDecrement(int r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.getAndDecrement(int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.getAndDecrement(int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.getAndIncrement(int):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final int getAndIncrement(int r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.getAndIncrement(int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.getAndIncrement(int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.getAndSet(int, int):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final int getAndSet(int r1, int r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.getAndSet(int, int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.getAndSet(int, int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.getOpaque(int):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final int getOpaque(int r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.getOpaque(int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.getOpaque(int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.getPlain(int):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final int getPlain(int r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.getPlain(int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.getPlain(int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.incrementAndGet(int):int, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final int incrementAndGet(int r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.incrementAndGet(int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.incrementAndGet(int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.lazySet(int, int):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final void lazySet(int r1, int r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.lazySet(int, int):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.lazySet(int, int):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.set(int, int):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final void set(int r1, int r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.set(int, int):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.set(int, int):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.setOpaque(int, int):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final void setOpaque(int r1, int r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.setOpaque(int, int):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.setOpaque(int, int):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.setPlain(int, int):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final void setPlain(int r1, int r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.setPlain(int, int):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.setPlain(int, int):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.setRelease(int, int):void, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final void setRelease(int r1, int r2) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.setRelease(int, int):void, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.setRelease(int, int):void");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSet(int, int, int):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final boolean weakCompareAndSet(int r1, int r2, int r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSet(int, int, int):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSet(int, int, int):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSetAcquire(int, int, int):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final boolean weakCompareAndSetAcquire(int r1, int r2, int r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSetAcquire(int, int, int):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSetAcquire(int, int, int):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSetPlain(int, int, int):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final boolean weakCompareAndSetPlain(int r1, int r2, int r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSetPlain(int, int, int):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSetPlain(int, int, int):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSetRelease(int, int, int):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final boolean weakCompareAndSetRelease(int r1, int r2, int r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSetRelease(int, int, int):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSetRelease(int, int, int):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSetVolatile(int, int, int):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    public final boolean weakCompareAndSetVolatile(int r1, int r2, int r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSetVolatile(int, int, int):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.AtomicIntegerArray.weakCompareAndSetVolatile(int, int, int):boolean");
    }

    public AtomicIntegerArray(int i) {
        this.array = new int[i];
    }

    public AtomicIntegerArray(int[] iArr) {
        this.array = (int[]) iArr.clone();
    }

    public final int length() {
        return this.array.length;
    }

    public final int getAndUpdate(int i, IntUnaryOperator intUnaryOperator) {
        int i2 = get(i);
        boolean z = false;
        int i3 = 0;
        while (true) {
            if (!z) {
                i3 = intUnaryOperator.applyAsInt(i2);
            }
            if (weakCompareAndSetVolatile(i, i2, i3)) {
                return i2;
            }
            int i4 = get(i);
            int i5 = i4;
            z = i2 == i4;
            i2 = i5;
        }
    }

    public final int updateAndGet(int i, IntUnaryOperator intUnaryOperator) {
        int i2 = get(i);
        boolean z = false;
        int i3 = 0;
        while (true) {
            if (!z) {
                i3 = intUnaryOperator.applyAsInt(i2);
            }
            if (weakCompareAndSetVolatile(i, i2, i3)) {
                return i3;
            }
            int i4 = get(i);
            int i5 = i4;
            z = i2 == i4;
            i2 = i5;
        }
    }

    public final int getAndAccumulate(int i, int i2, IntBinaryOperator intBinaryOperator) {
        int i3 = get(i);
        boolean z = false;
        int i4 = 0;
        while (true) {
            if (!z) {
                i4 = intBinaryOperator.applyAsInt(i3, i2);
            }
            if (weakCompareAndSetVolatile(i, i3, i4)) {
                return i3;
            }
            int i5 = get(i);
            int i6 = i5;
            z = i3 == i5;
            i3 = i6;
        }
    }

    public final int accumulateAndGet(int i, int i2, IntBinaryOperator intBinaryOperator) {
        int i3 = get(i);
        boolean z = false;
        int i4 = 0;
        while (true) {
            if (!z) {
                i4 = intBinaryOperator.applyAsInt(i3, i2);
            }
            if (weakCompareAndSetVolatile(i, i3, i4)) {
                return i4;
            }
            int i5 = get(i);
            int i6 = i5;
            z = i3 == i5;
            i3 = i6;
        }
    }

    public String toString() {
        int length = this.array.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder(NavigationBarInflaterView.SIZE_MOD_START);
        int i = 0;
        while (true) {
            sb.append(get(i));
            if (i == length) {
                sb.append(']');
                return sb.toString();
            }
            sb.append(", ");
            i++;
        }
    }
}
