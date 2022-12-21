package libcore.reflect;

import java.lang.reflect.MalformedParameterizedTypeException;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

public final class WildcardTypeImpl implements WildcardType {
    private final ListOfTypes extendsBound;
    private final ListOfTypes superBound;

    public WildcardTypeImpl(ListOfTypes listOfTypes, ListOfTypes listOfTypes2) {
        this.extendsBound = listOfTypes;
        this.superBound = listOfTypes2;
    }

    public Type[] getLowerBounds() throws TypeNotPresentException, MalformedParameterizedTypeException {
        return (Type[]) this.superBound.getResolvedTypes().clone();
    }

    public Type[] getUpperBounds() throws TypeNotPresentException, MalformedParameterizedTypeException {
        return (Type[]) this.extendsBound.getResolvedTypes().clone();
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof WildcardType)) {
            return false;
        }
        WildcardType wildcardType = (WildcardType) obj;
        if (!Arrays.equals((Object[]) getLowerBounds(), (Object[]) wildcardType.getLowerBounds()) || !Arrays.equals((Object[]) getUpperBounds(), (Object[]) wildcardType.getUpperBounds())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (Arrays.hashCode((Object[]) getLowerBounds()) * 31) + Arrays.hashCode((Object[]) getUpperBounds());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("?");
        if ((this.extendsBound.length() == 1 && this.extendsBound.getResolvedTypes()[0] != Object.class) || this.extendsBound.length() > 1) {
            sb.append(" extends ");
            sb.append((Object) this.extendsBound);
        } else if (this.superBound.length() > 0) {
            sb.append(" super ");
            sb.append((Object) this.superBound);
        }
        return sb.toString();
    }
}
