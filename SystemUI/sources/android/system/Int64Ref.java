package android.system;

import libcore.util.Objects;

public class Int64Ref {
    public long value;

    public Int64Ref(long j) {
        this.value = j;
    }

    public String toString() {
        return Objects.toString(this);
    }
}
