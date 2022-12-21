package android.system;

import android.annotation.SystemApi;
import libcore.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class StructCapUserData {
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public final int effective;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public final int inheritable;
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public final int permitted;

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public StructCapUserData(int i, int i2, int i3) {
        this.effective = i;
        this.permitted = i2;
        this.inheritable = i3;
    }

    public String toString() {
        return Objects.toString(this);
    }
}
