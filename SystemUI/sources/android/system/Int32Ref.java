package android.system;

import libcore.util.Objects;

public class Int32Ref {
    public int value;

    public Int32Ref(int i) {
        this.value = i;
    }

    public String toString() {
        return Objects.toString(this);
    }
}
