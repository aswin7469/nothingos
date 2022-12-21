package android.system;

import android.annotation.SystemApi;
import libcore.util.Objects;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class StructRlimit {
    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public final long rlim_cur;
    public final long rlim_max;

    public StructRlimit(long j, long j2) {
        this.rlim_cur = j;
        this.rlim_max = j2;
    }

    public String toString() {
        return Objects.toString(this);
    }
}
