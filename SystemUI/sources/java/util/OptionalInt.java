package java.util;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public final class OptionalInt {
    private static final OptionalInt EMPTY = new OptionalInt();
    private final boolean isPresent;
    private final int value;

    private OptionalInt() {
        this.isPresent = false;
        this.value = 0;
    }

    public static OptionalInt empty() {
        return EMPTY;
    }

    private OptionalInt(int i) {
        this.isPresent = true;
        this.value = i;
    }

    /* renamed from: of */
    public static OptionalInt m1754of(int i) {
        return new OptionalInt(i);
    }

    public int getAsInt() {
        if (this.isPresent) {
            return this.value;
        }
        throw new NoSuchElementException("No value present");
    }

    public boolean isPresent() {
        return this.isPresent;
    }

    public boolean isEmpty() {
        return !this.isPresent;
    }

    public void ifPresent(IntConsumer intConsumer) {
        if (this.isPresent) {
            intConsumer.accept(this.value);
        }
    }

    public void ifPresentOrElse(IntConsumer intConsumer, Runnable runnable) {
        if (this.isPresent) {
            intConsumer.accept(this.value);
        } else {
            runnable.run();
        }
    }

    public IntStream stream() {
        if (this.isPresent) {
            return IntStream.m1782of(this.value);
        }
        return IntStream.empty();
    }

    public int orElse(int i) {
        return this.isPresent ? this.value : i;
    }

    public int orElseGet(IntSupplier intSupplier) {
        return this.isPresent ? this.value : intSupplier.getAsInt();
    }

    public int orElseThrow() {
        if (this.isPresent) {
            return this.value;
        }
        throw new NoSuchElementException("No value present");
    }

    public <X extends Throwable> int orElseThrow(Supplier<? extends X> supplier) throws Throwable {
        if (this.isPresent) {
            return this.value;
        }
        throw ((Throwable) supplier.get());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OptionalInt)) {
            return false;
        }
        OptionalInt optionalInt = (OptionalInt) obj;
        boolean z = this.isPresent;
        if (!z || !optionalInt.isPresent) {
            if (z == optionalInt.isPresent) {
                return true;
            }
        } else if (this.value == optionalInt.value) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.isPresent) {
            return Integer.hashCode(this.value);
        }
        return 0;
    }

    public String toString() {
        if (!this.isPresent) {
            return "OptionalInt.empty";
        }
        return String.format("OptionalInt[%s]", Integer.valueOf(this.value));
    }
}
