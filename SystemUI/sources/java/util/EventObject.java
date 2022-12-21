package java.util;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.Serializable;

public class EventObject implements Serializable {
    private static final long serialVersionUID = 5516075349620653480L;
    protected transient Object source;

    public EventObject(Object obj) {
        if (obj != null) {
            this.source = obj;
            return;
        }
        throw new IllegalArgumentException("null source");
    }

    public Object getSource() {
        return this.source;
    }

    public String toString() {
        return getClass().getName() + "[source=" + this.source + NavigationBarInflaterView.SIZE_MOD_END;
    }
}
