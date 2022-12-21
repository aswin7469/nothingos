package android.system;

import android.annotation.SystemApi;
import libcore.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class StructCapUserHeader {
    public final int pid;
    public int version;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public StructCapUserHeader(int i, int i2) {
        this.version = i;
        this.pid = i2;
    }

    public String toString() {
        return Objects.toString(this);
    }
}
