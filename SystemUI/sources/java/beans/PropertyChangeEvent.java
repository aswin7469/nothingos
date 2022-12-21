package java.beans;

import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.util.EventObject;

public class PropertyChangeEvent extends EventObject {
    private static final long serialVersionUID = 7042693688939648123L;
    private Object newValue;
    private Object oldValue;
    private Object propagationId;
    private String propertyName;

    /* access modifiers changed from: package-private */
    public void appendTo(StringBuilder sb) {
    }

    public PropertyChangeEvent(Object obj, String str, Object obj2, Object obj3) {
        super(obj);
        this.propertyName = str;
        this.newValue = obj3;
        this.oldValue = obj2;
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public Object getNewValue() {
        return this.newValue;
    }

    public Object getOldValue() {
        return this.oldValue;
    }

    public void setPropagationId(Object obj) {
        this.propagationId = obj;
    }

    public Object getPropagationId() {
        return this.propagationId;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append("[propertyName=");
        sb.append(getPropertyName());
        appendTo(sb);
        sb.append("; oldValue=");
        sb.append(getOldValue());
        sb.append("; newValue=");
        sb.append(getNewValue());
        sb.append("; propagationId=");
        sb.append(getPropagationId());
        sb.append("; source=");
        sb.append(getSource());
        sb.append(NavigationBarInflaterView.SIZE_MOD_END);
        return sb.toString();
    }
}
