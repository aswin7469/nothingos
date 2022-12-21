package sun.nio.p033ch;

/* renamed from: sun.nio.ch.OptionKey */
class OptionKey {
    private int level;
    private int name;

    OptionKey(int i, int i2) {
        this.level = i;
        this.name = i2;
    }

    /* access modifiers changed from: package-private */
    public int level() {
        return this.level;
    }

    /* access modifiers changed from: package-private */
    public int name() {
        return this.name;
    }
}
