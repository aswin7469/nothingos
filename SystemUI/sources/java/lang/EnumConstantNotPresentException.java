package java.lang;

import com.android.launcher3.icons.cache.BaseIconCache;

public class EnumConstantNotPresentException extends RuntimeException {
    private static final long serialVersionUID = -6046998521960521108L;
    private String constantName;
    private Class<? extends Enum> enumType;

    public EnumConstantNotPresentException(Class<? extends Enum> cls, String str) {
        super(cls.getName() + BaseIconCache.EMPTY_CLASS_NAME + str);
        this.enumType = cls;
        this.constantName = str;
    }

    public Class<? extends Enum> enumType() {
        return this.enumType;
    }

    public String constantName() {
        return this.constantName;
    }
}
