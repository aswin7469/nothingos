package java.beans;

public class IndexedPropertyChangeEvent extends PropertyChangeEvent {
    private static final long serialVersionUID = -320227448495806870L;
    private int index;

    public IndexedPropertyChangeEvent(Object obj, String str, Object obj2, Object obj3, int i) {
        super(obj, str, obj2, obj3);
        this.index = i;
    }

    public int getIndex() {
        return this.index;
    }

    /* access modifiers changed from: package-private */
    public void appendTo(StringBuilder sb) {
        sb.append("; index=");
        sb.append(getIndex());
    }
}
