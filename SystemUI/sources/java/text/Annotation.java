package java.text;

import com.android.systemui.navigationbar.NavigationBarInflaterView;

public class Annotation {
    private Object value;

    public Annotation(Object obj) {
        this.value = obj;
    }

    public Object getValue() {
        return this.value;
    }

    public String toString() {
        return getClass().getName() + "[value=" + this.value + NavigationBarInflaterView.SIZE_MOD_END;
    }
}
