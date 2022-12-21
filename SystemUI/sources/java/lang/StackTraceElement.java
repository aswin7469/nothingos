package java.lang;

import com.android.launcher3.icons.cache.BaseIconCache;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.p026io.Serializable;
import java.util.Objects;

public final class StackTraceElement implements Serializable {
    private static final long serialVersionUID = 6992337162326171013L;
    private String declaringClass;
    private String fileName;
    private int lineNumber;
    private String methodName;

    public StackTraceElement(String str, String str2, String str3, int i) {
        this.declaringClass = (String) Objects.requireNonNull(str, "Declaring class is null");
        this.methodName = (String) Objects.requireNonNull(str2, "Method name is null");
        this.fileName = str3;
        this.lineNumber = i;
    }

    public String getFileName() {
        return this.fileName;
    }

    public int getLineNumber() {
        return this.lineNumber;
    }

    public String getClassName() {
        return this.declaringClass;
    }

    public String getMethodName() {
        return this.methodName;
    }

    public boolean isNativeMethod() {
        return this.lineNumber == -2;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClassName());
        sb.append(BaseIconCache.EMPTY_CLASS_NAME);
        sb.append(this.methodName);
        if (isNativeMethod()) {
            sb.append("(Native Method)");
        } else if (this.fileName != null) {
            if (this.lineNumber >= 0) {
                sb.append(NavigationBarInflaterView.KEY_CODE_START);
                sb.append(this.fileName);
                sb.append(":");
                sb.append(this.lineNumber);
                sb.append(NavigationBarInflaterView.KEY_CODE_END);
            } else {
                sb.append(NavigationBarInflaterView.KEY_CODE_START);
                sb.append(this.fileName);
                sb.append(NavigationBarInflaterView.KEY_CODE_END);
            }
        } else if (this.lineNumber >= 0) {
            sb.append("(Unknown Source:");
            sb.append(this.lineNumber);
            sb.append(NavigationBarInflaterView.KEY_CODE_END);
        } else {
            sb.append("(Unknown Source)");
        }
        return sb.toString();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof StackTraceElement)) {
            return false;
        }
        StackTraceElement stackTraceElement = (StackTraceElement) obj;
        if (!stackTraceElement.declaringClass.equals(this.declaringClass) || stackTraceElement.lineNumber != this.lineNumber || !Objects.equals(this.methodName, stackTraceElement.methodName) || !Objects.equals(this.fileName, stackTraceElement.fileName)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((((this.declaringClass.hashCode() * 31) + this.methodName.hashCode()) * 31) + Objects.hashCode(this.fileName)) * 31) + this.lineNumber;
    }
}
