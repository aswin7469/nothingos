package dalvik.system;

import android.annotation.SystemApi;

@SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
public final class AnnotatedStackTraceElement {
    private Object blockedOn;
    private Object[] heldLocks;
    private StackTraceElement stackTraceElement;

    private AnnotatedStackTraceElement() {
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public StackTraceElement getStackTraceElement() {
        return this.stackTraceElement;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public Object[] getHeldLocks() {
        return this.heldLocks;
    }

    @SystemApi(client = SystemApi.Client.MODULE_LIBRARIES)
    public Object getBlockedOn() {
        return this.blockedOn;
    }
}
