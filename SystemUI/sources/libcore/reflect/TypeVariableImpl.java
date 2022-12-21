package libcore.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

public final class TypeVariableImpl<D extends GenericDeclaration> implements TypeVariable<D> {
    private ListOfTypes bounds;
    private final GenericDeclaration declOfVarUser;
    private TypeVariableImpl<D> formalVar;
    private D genericDeclaration;
    private final String name;

    public boolean equals(Object obj) {
        if (!(obj instanceof TypeVariable)) {
            return false;
        }
        TypeVariable typeVariable = (TypeVariable) obj;
        if (!getName().equals(typeVariable.getName()) || !getGenericDeclaration().equals(typeVariable.getGenericDeclaration())) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return (getName().hashCode() * 31) + getGenericDeclaration().hashCode();
    }

    TypeVariableImpl(D d, String str, ListOfTypes listOfTypes) {
        this.genericDeclaration = d;
        this.name = str;
        this.bounds = listOfTypes;
        this.formalVar = this;
        this.declOfVarUser = null;
    }

    TypeVariableImpl(D d, String str) {
        this.name = str;
        this.declOfVarUser = d;
    }

    static TypeVariable findFormalVar(GenericDeclaration genericDeclaration2, String str) {
        for (TypeVariable typeVariable : genericDeclaration2.getTypeParameters()) {
            if (str.equals(typeVariable.getName())) {
                return typeVariable;
            }
        }
        return null;
    }

    private static GenericDeclaration nextLayer(GenericDeclaration genericDeclaration2) {
        if (genericDeclaration2 instanceof Class) {
            Class cls = (Class) genericDeclaration2;
            GenericDeclaration enclosingMethod = cls.getEnclosingMethod();
            if (enclosingMethod == null) {
                enclosingMethod = cls.getEnclosingConstructor();
            }
            if (enclosingMethod != null) {
                return enclosingMethod;
            }
            return cls.getEnclosingClass();
        } else if (genericDeclaration2 instanceof Method) {
            return ((Method) genericDeclaration2).getDeclaringClass();
        } else {
            if (genericDeclaration2 instanceof Constructor) {
                return ((Constructor) genericDeclaration2).getDeclaringClass();
            }
            throw new AssertionError();
        }
    }

    /* access modifiers changed from: package-private */
    public void resolve() {
        if (this.formalVar == null) {
            GenericDeclaration genericDeclaration2 = this.declOfVarUser;
            do {
                TypeVariable findFormalVar = findFormalVar(genericDeclaration2, this.name);
                if (findFormalVar == null) {
                    genericDeclaration2 = nextLayer(genericDeclaration2);
                } else {
                    TypeVariableImpl<D> typeVariableImpl = (TypeVariableImpl) findFormalVar;
                    this.formalVar = typeVariableImpl;
                    this.genericDeclaration = typeVariableImpl.genericDeclaration;
                    this.bounds = typeVariableImpl.bounds;
                    return;
                }
            } while (genericDeclaration2 != null);
            throw new AssertionError((Object) "illegal type variable reference");
        }
    }

    public Type[] getBounds() {
        resolve();
        return (Type[]) this.bounds.getResolvedTypes().clone();
    }

    public D getGenericDeclaration() {
        resolve();
        return this.genericDeclaration;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name;
    }
}
