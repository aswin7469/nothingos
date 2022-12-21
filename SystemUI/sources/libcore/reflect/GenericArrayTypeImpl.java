package libcore.reflect;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Objects;

public final class GenericArrayTypeImpl implements GenericArrayType {
    private final Type componentType;

    public GenericArrayTypeImpl(Type type) {
        this.componentType = type;
    }

    public Type getGenericComponentType() {
        Type type = this.componentType;
        return type instanceof ParameterizedTypeImpl ? ((ParameterizedTypeImpl) type).getResolvedType() : type;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof GenericArrayType)) {
            return false;
        }
        return Objects.equals(getGenericComponentType(), ((GenericArrayType) obj).getGenericComponentType());
    }

    public int hashCode() {
        return Objects.hashCode(getGenericComponentType());
    }

    public String toString() {
        return this.componentType.toString() + "[]";
    }
}
