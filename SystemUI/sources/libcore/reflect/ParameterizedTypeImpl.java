package libcore.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;

public final class ParameterizedTypeImpl implements ParameterizedType {
    private final ListOfTypes args;
    private final ClassLoader loader;
    private final ParameterizedTypeImpl ownerType0;
    private Type ownerTypeRes;
    private Class rawType;
    private final String rawTypeName;

    public ParameterizedTypeImpl(ParameterizedTypeImpl parameterizedTypeImpl, String str, ListOfTypes listOfTypes, ClassLoader classLoader) {
        listOfTypes.getClass();
        this.ownerType0 = parameterizedTypeImpl;
        this.rawTypeName = str;
        this.args = listOfTypes;
        this.loader = classLoader;
    }

    public Type[] getActualTypeArguments() {
        return (Type[]) this.args.getResolvedTypes().clone();
    }

    public Type getOwnerType() {
        if (this.ownerTypeRes == null) {
            ParameterizedTypeImpl parameterizedTypeImpl = this.ownerType0;
            if (parameterizedTypeImpl != null) {
                this.ownerTypeRes = parameterizedTypeImpl.getResolvedType();
            } else {
                this.ownerTypeRes = getRawType().getDeclaringClass();
            }
        }
        return this.ownerTypeRes;
    }

    public Class getRawType() {
        if (this.rawType == null) {
            try {
                this.rawType = Class.forName(this.rawTypeName, false, this.loader);
            } catch (ClassNotFoundException e) {
                throw new TypeNotPresentException(this.rawTypeName, e);
            }
        }
        return this.rawType;
    }

    /* access modifiers changed from: package-private */
    public Type getResolvedType() {
        return (this.ownerType0 == null && this.args.getResolvedTypes().length == 0) ? getRawType() : this;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof ParameterizedType)) {
            return false;
        }
        ParameterizedType parameterizedType = (ParameterizedType) obj;
        if (!Objects.equals(getRawType(), parameterizedType.getRawType()) || !Objects.equals(getOwnerType(), parameterizedType.getOwnerType()) || !Arrays.equals((Object[]) this.args.getResolvedTypes(), (Object[]) parameterizedType.getActualTypeArguments())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (((Objects.hashCode(getRawType()) * 31) + Objects.hashCode(getOwnerType())) * 31) + Arrays.hashCode((Object[]) this.args.getResolvedTypes());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        ParameterizedTypeImpl parameterizedTypeImpl = this.ownerType0;
        if (parameterizedTypeImpl != null) {
            sb.append((Object) parameterizedTypeImpl);
            sb.append('$');
            sb.append(getRawType().getSimpleName());
        } else {
            sb.append(this.rawTypeName);
        }
        if (this.args.length() > 0) {
            sb.append("<");
            sb.append((Object) this.args);
            sb.append(">");
        }
        return sb.toString();
    }
}
