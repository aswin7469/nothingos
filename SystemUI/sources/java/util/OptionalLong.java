package java.util;

import java.util.function.LongConsumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.stream.LongStream;

public final class OptionalLong {
    private static final OptionalLong EMPTY = new OptionalLong();
    private final boolean isPresent;
    private final long value;

    private OptionalLong() {
        this.isPresent = false;
        this.value = 0;
    }

    public static OptionalLong empty() {
        return EMPTY;
    }

    private OptionalLong(long j) {
        this.isPresent = true;
        this.value = j;
    }

    /* renamed from: of */
    public static OptionalLong m1755of(long j) {
        return new OptionalLong(j);
    }

    public long getAsLong() {
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

    public void ifPresent(LongConsumer longConsumer) {
        if (this.isPresent) {
            longConsumer.accept(this.value);
        }
    }

    public void ifPresentOrElse(LongConsumer longConsumer, Runnable runnable) {
        if (this.isPresent) {
            longConsumer.accept(this.value);
        } else {
            runnable.run();
        }
    }

    public LongStream stream() {
        if (this.isPresent) {
            return LongStream.m1784of(this.value);
        }
        return LongStream.empty();
    }

    public long orElse(long j) {
        return this.isPresent ? this.value : j;
    }

    public long orElseGet(LongSupplier longSupplier) {
        return this.isPresent ? this.value : longSupplier.getAsLong();
    }

    public long orElseThrow() {
        if (this.isPresent) {
            return this.value;
        }
        throw new NoSuchElementException("No value present");
    }

    public <X extends Throwable> long orElseThrow(Supplier<? extends X> supplier) throws Throwable {
        if (this.isPresent) {
            return this.value;
        }
        throw ((Throwable) supplier.get());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OptionalLong)) {
            return false;
        }
        OptionalLong optionalLong = (OptionalLong) obj;
        boolean z = this.isPresent;
        if (!z || !optionalLong.isPresent) {
            if (z == optionalLong.isPresent) {
                return true;
            }
        } else if (this.value == optionalLong.value) {
            return true;
        }
        return false;
    }

    public int hashCode() {
        if (this.isPresent) {
            return Long.hashCode(this.value);
        }
        return 0;
    }

    public String toString() {
        if (!this.isPresent) {
            return "OptionalLong.empty";
        }
        return String.format("OptionalLong[%s]", Long.valueOf(this.value));
    }
}
