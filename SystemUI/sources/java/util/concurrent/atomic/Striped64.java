package java.util.concurrent.atomic;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.function.DoubleBinaryOperator;

abstract class Striped64 extends Number {
    private static final VarHandle BASE;
    private static final VarHandle CELLSBUSY;
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    private static final VarHandle THREAD_PROBE;
    volatile transient long base;
    volatile transient Cell[] cells;
    volatile transient int cellsBusy;

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.Striped64.advanceProbe(int):int, dex: classes4.dex
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
    static final int advanceProbe(int r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.Striped64.advanceProbe(int):int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.Striped64.advanceProbe(int):int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.Striped64.getProbe():int, dex: classes4.dex
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
    static final int getProbe() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.Striped64.getProbe():int, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.Striped64.getProbe():int");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.atomic.Striped64.casBase(long, long):boolean, dex: classes4.dex
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
        	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
        	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
        	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
        Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic/range'
        	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
        	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
        	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
        	... 5 more
        */
    final boolean casBase(long r1, long r3) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.atomic.Striped64.casBase(long, long):boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.Striped64.casBase(long, long):boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.Striped64.casCellsBusy():boolean, dex: classes4.dex
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
    final boolean casCellsBusy() {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.Striped64.casCellsBusy():boolean, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.Striped64.casCellsBusy():boolean");
    }

    /*  JADX ERROR: Method load error
        jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.Striped64.getAndSetBase(long):long, dex: classes4.dex
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
    final long getAndSetBase(long r1) {
        /*
        // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.Striped64.getAndSetBase(long):long, dex: classes4.dex
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.Striped64.getAndSetBase(long):long");
    }

    static final class Cell {
        private static final VarHandle VALUE;
        volatile long value;

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.atomic.Striped64.Cell.cas(long, long):boolean, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic/range'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 6 more
            */
        final boolean cas(long r1, long r3) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic/range' in method: java.util.concurrent.atomic.Striped64.Cell.cas(long, long):boolean, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.Striped64.Cell.cas(long, long):boolean");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.Striped64.Cell.getAndSet(long):long, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 6 more
            */
        final long getAndSet(long r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.Striped64.Cell.getAndSet(long):long, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.Striped64.Cell.getAndSet(long):long");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.Striped64.Cell.reset():void, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 6 more
            */
        final void reset() {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.Striped64.Cell.reset():void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.Striped64.Cell.reset():void");
        }

        /*  JADX ERROR: Method load error
            jadx.core.utils.exceptions.DecodeException: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.Striped64.Cell.reset(long):void, dex: classes4.dex
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:151)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:286)
            	at jadx.core.dex.nodes.ClassNode.load(ClassNode.java:292)
            	at jadx.core.ProcessClass.process(ProcessClass.java:36)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1540)
            	at jadx.core.ProcessClass.generateCode(ProcessClass.java:59)
            	at jadx.core.dex.nodes.ClassNode.decompile(ClassNode.java:273)
            Caused by: jadx.core.utils.exceptions.DecodeException: Unknown instruction: 'invoke-polymorphic'
            	at jadx.core.dex.instructions.InsnDecoder.decode(InsnDecoder.java:588)
            	at jadx.core.dex.instructions.InsnDecoder.process(InsnDecoder.java:78)
            	at jadx.core.dex.nodes.MethodNode.load(MethodNode.java:136)
            	... 6 more
            */
        final void reset(long r1) {
            /*
            // Can't load method instructions: Load method exception: Unknown instruction: 'invoke-polymorphic' in method: java.util.concurrent.atomic.Striped64.Cell.reset(long):void, dex: classes4.dex
            */
            throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.Striped64.Cell.reset(long):void");
        }

        Cell(long j) {
            this.value = j;
        }

        static {
            try {
                VALUE = MethodHandles.lookup().findVarHandle(Cell.class, "value", Long.TYPE);
            } catch (ReflectiveOperationException e) {
                throw new ExceptionInInitializerError((Throwable) e);
            }
        }
    }

    static {
        Class<Striped64> cls = Striped64.class;
        try {
            MethodHandles.Lookup lookup = MethodHandles.lookup();
            BASE = lookup.findVarHandle(cls, "base", Long.TYPE);
            CELLSBUSY = lookup.findVarHandle(cls, "cellsBusy", Integer.TYPE);
            THREAD_PROBE = ((MethodHandles.Lookup) AccessController.doPrivileged(new PrivilegedAction<MethodHandles.Lookup>() {
                public MethodHandles.Lookup run() {
                    try {
                        return MethodHandles.privateLookupIn(Thread.class, MethodHandles.lookup());
                    } catch (ReflectiveOperationException e) {
                        throw new ExceptionInInitializerError((Throwable) e);
                    }
                }
            })).findVarHandle(Thread.class, "threadLocalRandomProbe", Integer.TYPE);
        } catch (ReflectiveOperationException e) {
            throw new ExceptionInInitializerError((Throwable) e);
        }
    }

    Striped64() {
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x007f, code lost:
        if (r11.cells != r4) goto L_0x008b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x0081, code lost:
        r11.cells = (java.util.concurrent.atomic.Striped64.Cell[]) java.util.Arrays.copyOf((T[]) r4, r5 << 1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void longAccumulate(long r12, java.util.function.LongBinaryOperator r14, boolean r15) {
        /*
            r11 = this;
            int r0 = getProbe()
            r1 = 1
            if (r0 != 0) goto L_0x000f
            java.util.concurrent.ThreadLocalRandom.current()
            int r0 = getProbe()
            r15 = r1
        L_0x000f:
            r2 = 0
        L_0x0010:
            r3 = r2
        L_0x0011:
            java.util.concurrent.atomic.Striped64$Cell[] r4 = r11.cells
            if (r4 == 0) goto L_0x0098
            int r5 = r4.length
            if (r5 <= 0) goto L_0x0098
            int r6 = r5 + -1
            r6 = r6 & r0
            r6 = r4[r6]
            if (r6 != 0) goto L_0x004f
            int r4 = r11.cellsBusy
            if (r4 != 0) goto L_0x004d
            java.util.concurrent.atomic.Striped64$Cell r4 = new java.util.concurrent.atomic.Striped64$Cell
            r4.<init>(r12)
            int r5 = r11.cellsBusy
            if (r5 != 0) goto L_0x004d
            boolean r5 = r11.casCellsBusy()
            if (r5 == 0) goto L_0x004d
            java.util.concurrent.atomic.Striped64$Cell[] r5 = r11.cells     // Catch:{ all -> 0x0049 }
            if (r5 == 0) goto L_0x0046
            int r6 = r5.length     // Catch:{ all -> 0x0049 }
            if (r6 <= 0) goto L_0x0046
            int r6 = r6 + -1
            r6 = r6 & r0
            r7 = r5[r6]     // Catch:{ all -> 0x0049 }
            if (r7 != 0) goto L_0x0046
            r5[r6] = r4     // Catch:{ all -> 0x0049 }
            r11.cellsBusy = r2
            goto L_0x00d4
        L_0x0046:
            r11.cellsBusy = r2
            goto L_0x0011
        L_0x0049:
            r12 = move-exception
            r11.cellsBusy = r2
            throw r12
        L_0x004d:
            r3 = r2
            goto L_0x0092
        L_0x004f:
            if (r15 != 0) goto L_0x0053
            r15 = r1
            goto L_0x0092
        L_0x0053:
            long r7 = r6.value
            if (r14 != 0) goto L_0x005a
            long r9 = r7 + r12
            goto L_0x005e
        L_0x005a:
            long r9 = r14.applyAsLong(r7, r12)
        L_0x005e:
            boolean r6 = r6.cas(r7, r9)
            if (r6 == 0) goto L_0x0066
            goto L_0x00d4
        L_0x0066:
            int r6 = NCPU
            if (r5 >= r6) goto L_0x004d
            java.util.concurrent.atomic.Striped64$Cell[] r6 = r11.cells
            if (r6 == r4) goto L_0x006f
            goto L_0x004d
        L_0x006f:
            if (r3 != 0) goto L_0x0073
            r3 = r1
            goto L_0x0092
        L_0x0073:
            int r6 = r11.cellsBusy
            if (r6 != 0) goto L_0x0092
            boolean r6 = r11.casCellsBusy()
            if (r6 == 0) goto L_0x0092
            java.util.concurrent.atomic.Striped64$Cell[] r3 = r11.cells     // Catch:{ all -> 0x008e }
            if (r3 != r4) goto L_0x008b
            int r3 = r5 << 1
            java.lang.Object[] r3 = java.util.Arrays.copyOf((T[]) r4, (int) r3)     // Catch:{ all -> 0x008e }
            java.util.concurrent.atomic.Striped64$Cell[] r3 = (java.util.concurrent.atomic.Striped64.Cell[]) r3     // Catch:{ all -> 0x008e }
            r11.cells = r3     // Catch:{ all -> 0x008e }
        L_0x008b:
            r11.cellsBusy = r2
            goto L_0x0010
        L_0x008e:
            r12 = move-exception
            r11.cellsBusy = r2
            throw r12
        L_0x0092:
            int r0 = advanceProbe(r0)
            goto L_0x0011
        L_0x0098:
            int r5 = r11.cellsBusy
            if (r5 != 0) goto L_0x00c3
            java.util.concurrent.atomic.Striped64$Cell[] r5 = r11.cells
            if (r5 != r4) goto L_0x00c3
            boolean r5 = r11.casCellsBusy()
            if (r5 == 0) goto L_0x00c3
            java.util.concurrent.atomic.Striped64$Cell[] r5 = r11.cells     // Catch:{ all -> 0x00bf }
            if (r5 != r4) goto L_0x00bb
            r14 = 2
            java.util.concurrent.atomic.Striped64$Cell[] r14 = new java.util.concurrent.atomic.Striped64.Cell[r14]     // Catch:{ all -> 0x00bf }
            r15 = r0 & 1
            java.util.concurrent.atomic.Striped64$Cell r0 = new java.util.concurrent.atomic.Striped64$Cell     // Catch:{ all -> 0x00bf }
            r0.<init>(r12)     // Catch:{ all -> 0x00bf }
            r14[r15] = r0     // Catch:{ all -> 0x00bf }
            r11.cells = r14     // Catch:{ all -> 0x00bf }
            r11.cellsBusy = r2
            goto L_0x00d4
        L_0x00bb:
            r11.cellsBusy = r2
            goto L_0x0011
        L_0x00bf:
            r12 = move-exception
            r11.cellsBusy = r2
            throw r12
        L_0x00c3:
            long r4 = r11.base
            if (r14 != 0) goto L_0x00ca
            long r6 = r4 + r12
            goto L_0x00ce
        L_0x00ca:
            long r6 = r14.applyAsLong(r4, r12)
        L_0x00ce:
            boolean r4 = r11.casBase(r4, r6)
            if (r4 == 0) goto L_0x0011
        L_0x00d4:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.Striped64.longAccumulate(long, java.util.function.LongBinaryOperator, boolean):void");
    }

    private static long apply(DoubleBinaryOperator doubleBinaryOperator, long j, double d) {
        double d2;
        double longBitsToDouble = Double.longBitsToDouble(j);
        if (doubleBinaryOperator == null) {
            d2 = longBitsToDouble + d;
        } else {
            d2 = doubleBinaryOperator.applyAsDouble(longBitsToDouble, d);
        }
        return Double.doubleToRawLongBits(d2);
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x007e, code lost:
        if (r11.cells != r4) goto L_0x008a;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:48:0x0080, code lost:
        r11.cells = (java.util.concurrent.atomic.Striped64.Cell[]) java.util.Arrays.copyOf((T[]) r4, r5 << 1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void doubleAccumulate(double r12, java.util.function.DoubleBinaryOperator r14, boolean r15) {
        /*
            r11 = this;
            int r0 = getProbe()
            r1 = 1
            if (r0 != 0) goto L_0x000f
            java.util.concurrent.ThreadLocalRandom.current()
            int r0 = getProbe()
            r15 = r1
        L_0x000f:
            r2 = 0
        L_0x0010:
            r3 = r2
        L_0x0011:
            java.util.concurrent.atomic.Striped64$Cell[] r4 = r11.cells
            if (r4 == 0) goto L_0x0097
            int r5 = r4.length
            if (r5 <= 0) goto L_0x0097
            int r6 = r5 + -1
            r6 = r6 & r0
            r6 = r4[r6]
            if (r6 != 0) goto L_0x0053
            int r4 = r11.cellsBusy
            if (r4 != 0) goto L_0x0051
            java.util.concurrent.atomic.Striped64$Cell r4 = new java.util.concurrent.atomic.Striped64$Cell
            long r5 = java.lang.Double.doubleToRawLongBits(r12)
            r4.<init>(r5)
            int r5 = r11.cellsBusy
            if (r5 != 0) goto L_0x0051
            boolean r5 = r11.casCellsBusy()
            if (r5 == 0) goto L_0x0051
            java.util.concurrent.atomic.Striped64$Cell[] r5 = r11.cells     // Catch:{ all -> 0x004d }
            if (r5 == 0) goto L_0x004a
            int r6 = r5.length     // Catch:{ all -> 0x004d }
            if (r6 <= 0) goto L_0x004a
            int r6 = r6 + -1
            r6 = r6 & r0
            r7 = r5[r6]     // Catch:{ all -> 0x004d }
            if (r7 != 0) goto L_0x004a
            r5[r6] = r4     // Catch:{ all -> 0x004d }
            r11.cellsBusy = r2
            goto L_0x00d2
        L_0x004a:
            r11.cellsBusy = r2
            goto L_0x0011
        L_0x004d:
            r12 = move-exception
            r11.cellsBusy = r2
            throw r12
        L_0x0051:
            r3 = r2
            goto L_0x0091
        L_0x0053:
            if (r15 != 0) goto L_0x0057
            r15 = r1
            goto L_0x0091
        L_0x0057:
            long r7 = r6.value
            long r9 = apply(r14, r7, r12)
            boolean r6 = r6.cas(r7, r9)
            if (r6 == 0) goto L_0x0065
            goto L_0x00d2
        L_0x0065:
            int r6 = NCPU
            if (r5 >= r6) goto L_0x0051
            java.util.concurrent.atomic.Striped64$Cell[] r6 = r11.cells
            if (r6 == r4) goto L_0x006e
            goto L_0x0051
        L_0x006e:
            if (r3 != 0) goto L_0x0072
            r3 = r1
            goto L_0x0091
        L_0x0072:
            int r6 = r11.cellsBusy
            if (r6 != 0) goto L_0x0091
            boolean r6 = r11.casCellsBusy()
            if (r6 == 0) goto L_0x0091
            java.util.concurrent.atomic.Striped64$Cell[] r3 = r11.cells     // Catch:{ all -> 0x008d }
            if (r3 != r4) goto L_0x008a
            int r3 = r5 << 1
            java.lang.Object[] r3 = java.util.Arrays.copyOf((T[]) r4, (int) r3)     // Catch:{ all -> 0x008d }
            java.util.concurrent.atomic.Striped64$Cell[] r3 = (java.util.concurrent.atomic.Striped64.Cell[]) r3     // Catch:{ all -> 0x008d }
            r11.cells = r3     // Catch:{ all -> 0x008d }
        L_0x008a:
            r11.cellsBusy = r2
            goto L_0x0010
        L_0x008d:
            r12 = move-exception
            r11.cellsBusy = r2
            throw r12
        L_0x0091:
            int r0 = advanceProbe(r0)
            goto L_0x0011
        L_0x0097:
            int r5 = r11.cellsBusy
            if (r5 != 0) goto L_0x00c6
            java.util.concurrent.atomic.Striped64$Cell[] r5 = r11.cells
            if (r5 != r4) goto L_0x00c6
            boolean r5 = r11.casCellsBusy()
            if (r5 == 0) goto L_0x00c6
            java.util.concurrent.atomic.Striped64$Cell[] r5 = r11.cells     // Catch:{ all -> 0x00c2 }
            if (r5 != r4) goto L_0x00be
            r14 = 2
            java.util.concurrent.atomic.Striped64$Cell[] r14 = new java.util.concurrent.atomic.Striped64.Cell[r14]     // Catch:{ all -> 0x00c2 }
            r15 = r0 & 1
            java.util.concurrent.atomic.Striped64$Cell r0 = new java.util.concurrent.atomic.Striped64$Cell     // Catch:{ all -> 0x00c2 }
            long r12 = java.lang.Double.doubleToRawLongBits(r12)     // Catch:{ all -> 0x00c2 }
            r0.<init>(r12)     // Catch:{ all -> 0x00c2 }
            r14[r15] = r0     // Catch:{ all -> 0x00c2 }
            r11.cells = r14     // Catch:{ all -> 0x00c2 }
            r11.cellsBusy = r2
            goto L_0x00d2
        L_0x00be:
            r11.cellsBusy = r2
            goto L_0x0011
        L_0x00c2:
            r12 = move-exception
            r11.cellsBusy = r2
            throw r12
        L_0x00c6:
            long r4 = r11.base
            long r6 = apply(r14, r4, r12)
            boolean r4 = r11.casBase(r4, r6)
            if (r4 == 0) goto L_0x0011
        L_0x00d2:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: java.util.concurrent.atomic.Striped64.doubleAccumulate(double, java.util.function.DoubleBinaryOperator, boolean):void");
    }
}
