package java.util.stream;

import java.util.EnumMap;
import java.util.Map;
import java.util.Spliterator;

public enum StreamOpFlag {
    DISTINCT(0, set(Type.SPLITERATOR).set(Type.STREAM).setAndClear(Type.OP)),
    SORTED(1, set(Type.SPLITERATOR).set(Type.STREAM).setAndClear(Type.OP)),
    ORDERED(2, set(Type.SPLITERATOR).set(Type.STREAM).setAndClear(Type.OP).clear(Type.TERMINAL_OP).clear(Type.UPSTREAM_TERMINAL_OP)),
    SIZED(3, set(Type.SPLITERATOR).set(Type.STREAM).clear(Type.OP)),
    SHORT_CIRCUIT(12, set(Type.OP).set(Type.TERMINAL_OP));
    
    private static final int CLEAR_BITS = 2;
    private static final int FLAG_MASK = 0;
    private static final int FLAG_MASK_IS = 0;
    private static final int FLAG_MASK_NOT = 0;
    public static final int INITIAL_OPS_VALUE = 0;
    public static final int IS_DISTINCT = 0;
    public static final int IS_ORDERED = 0;
    public static final int IS_SHORT_CIRCUIT = 0;
    public static final int IS_SIZED = 0;
    public static final int IS_SORTED = 0;
    public static final int NOT_DISTINCT = 0;
    public static final int NOT_ORDERED = 0;
    public static final int NOT_SIZED = 0;
    public static final int NOT_SORTED = 0;
    public static final int OP_MASK = 0;
    private static final int PRESERVE_BITS = 3;
    private static final int SET_BITS = 1;
    public static final int SPLITERATOR_CHARACTERISTICS_MASK = 0;
    public static final int STREAM_MASK = 0;
    public static final int TERMINAL_OP_MASK = 0;
    public static final int UPSTREAM_TERMINAL_OP_MASK = 0;
    private final int bitPosition;
    private final int clear;
    private final Map<Type, Integer> maskTable;
    private final int preserve;
    private final int set;

    enum Type {
        SPLITERATOR,
        STREAM,
        OP,
        TERMINAL_OP,
        UPSTREAM_TERMINAL_OP
    }

    static {
        StreamOpFlag streamOpFlag;
        StreamOpFlag streamOpFlag2;
        StreamOpFlag streamOpFlag3;
        StreamOpFlag streamOpFlag4;
        StreamOpFlag streamOpFlag5;
        SPLITERATOR_CHARACTERISTICS_MASK = createMask(Type.SPLITERATOR);
        int createMask = createMask(Type.STREAM);
        STREAM_MASK = createMask;
        OP_MASK = createMask(Type.OP);
        TERMINAL_OP_MASK = createMask(Type.TERMINAL_OP);
        UPSTREAM_TERMINAL_OP_MASK = createMask(Type.UPSTREAM_TERMINAL_OP);
        FLAG_MASK = createFlagMask();
        FLAG_MASK_IS = createMask;
        int i = createMask << 1;
        FLAG_MASK_NOT = i;
        INITIAL_OPS_VALUE = createMask | i;
        IS_DISTINCT = streamOpFlag.set;
        NOT_DISTINCT = streamOpFlag.clear;
        IS_SORTED = streamOpFlag2.set;
        NOT_SORTED = streamOpFlag2.clear;
        IS_ORDERED = streamOpFlag3.set;
        NOT_ORDERED = streamOpFlag3.clear;
        IS_SIZED = streamOpFlag4.set;
        NOT_SIZED = streamOpFlag4.clear;
        IS_SHORT_CIRCUIT = streamOpFlag5.set;
    }

    private static MaskBuilder set(Type type) {
        return new MaskBuilder(new EnumMap(Type.class)).set(type);
    }

    private static class MaskBuilder {
        final Map<Type, Integer> map;

        MaskBuilder(Map<Type, Integer> map2) {
            this.map = map2;
        }

        /* access modifiers changed from: package-private */
        public MaskBuilder mask(Type type, Integer num) {
            this.map.put(type, num);
            return this;
        }

        /* access modifiers changed from: package-private */
        public MaskBuilder set(Type type) {
            return mask(type, 1);
        }

        /* access modifiers changed from: package-private */
        public MaskBuilder clear(Type type) {
            return mask(type, 2);
        }

        /* access modifiers changed from: package-private */
        public MaskBuilder setAndClear(Type type) {
            return mask(type, 3);
        }

        /* access modifiers changed from: package-private */
        public Map<Type, Integer> build() {
            for (Type putIfAbsent : Type.values()) {
                this.map.putIfAbsent(putIfAbsent, 0);
            }
            return this.map;
        }
    }

    private StreamOpFlag(int i, MaskBuilder maskBuilder) {
        this.maskTable = maskBuilder.build();
        int i2 = i * 2;
        this.bitPosition = i2;
        this.set = 1 << i2;
        this.clear = 2 << i2;
        this.preserve = 3 << i2;
    }

    public int set() {
        return this.set;
    }

    public int clear() {
        return this.clear;
    }

    public boolean isStreamFlag() {
        return this.maskTable.get(Type.STREAM).intValue() > 0;
    }

    public boolean isKnown(int i) {
        return (i & this.preserve) == this.set;
    }

    public boolean isCleared(int i) {
        return (i & this.preserve) == this.clear;
    }

    public boolean isPreserved(int i) {
        int i2 = this.preserve;
        return (i & i2) == i2;
    }

    public boolean canSet(Type type) {
        return (this.maskTable.get(type).intValue() & 1) > 0;
    }

    private static int createMask(Type type) {
        int i = 0;
        for (StreamOpFlag streamOpFlag : values()) {
            i |= streamOpFlag.maskTable.get(type).intValue() << streamOpFlag.bitPosition;
        }
        return i;
    }

    private static int createFlagMask() {
        int i = 0;
        for (StreamOpFlag streamOpFlag : values()) {
            i |= streamOpFlag.preserve;
        }
        return i;
    }

    private static int getMask(int i) {
        if (i == 0) {
            return FLAG_MASK;
        }
        return ~(((i & FLAG_MASK_NOT) >> 1) | ((FLAG_MASK_IS & i) << 1) | i);
    }

    public static int combineOpFlags(int i, int i2) {
        return i | (i2 & getMask(i));
    }

    public static int toStreamFlags(int i) {
        return i & ((~i) >> 1) & FLAG_MASK_IS;
    }

    public static int toCharacteristics(int i) {
        return i & SPLITERATOR_CHARACTERISTICS_MASK;
    }

    public static int fromCharacteristics(Spliterator<?> spliterator) {
        int characteristics = spliterator.characteristics();
        if ((characteristics & 4) == 0 || spliterator.getComparator() == null) {
            return SPLITERATOR_CHARACTERISTICS_MASK & characteristics;
        }
        return SPLITERATOR_CHARACTERISTICS_MASK & characteristics & -5;
    }

    public static int fromCharacteristics(int i) {
        return i & SPLITERATOR_CHARACTERISTICS_MASK;
    }
}
