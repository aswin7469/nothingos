package sun.nio.p033ch;

/* renamed from: sun.nio.ch.AllocatedNativeObject */
class AllocatedNativeObject extends NativeObject {
    AllocatedNativeObject(int i, boolean z) {
        super(i, z);
    }

    /* access modifiers changed from: package-private */
    public synchronized void free() {
        if (this.allocationAddress != 0) {
            unsafe.freeMemory(this.allocationAddress);
            this.allocationAddress = 0;
        }
    }
}
