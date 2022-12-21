package android.system;

import java.p026io.FileDescriptor;
import libcore.util.Objects;

public final class StructPollfd {
    public short events;

    /* renamed from: fd */
    public FileDescriptor f59fd;
    public short revents;
    public Object userData;

    public String toString() {
        return Objects.toString(this);
    }
}
