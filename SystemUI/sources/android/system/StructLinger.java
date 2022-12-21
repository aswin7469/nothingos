package android.system;

import android.annotation.SystemApi;
import libcore.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class StructLinger {
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public final int l_linger;
    public final int l_onoff;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public StructLinger(int i, int i2) {
        this.l_onoff = i;
        this.l_linger = i2;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public boolean isOn() {
        return this.l_onoff != 0;
    }

    public String toString() {
        return Objects.toString(this);
    }
}
