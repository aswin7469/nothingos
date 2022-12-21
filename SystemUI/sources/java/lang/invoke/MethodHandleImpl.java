package java.lang.invoke;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

public class MethodHandleImpl extends MethodHandle implements Cloneable {
    private HandleInfo info;

    public native Member getMemberInternal();

    MethodHandleImpl(long j, int i, MethodType methodType) {
        super(j, i, methodType);
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    /* access modifiers changed from: package-private */
    public MethodHandleInfo reveal() {
        if (this.info == null) {
            this.info = new HandleInfo(getMemberInternal(), this);
        }
        return this.info;
    }

    static class HandleInfo implements MethodHandleInfo {
        private final MethodHandle handle;
        private final Member member;

        HandleInfo(Member member2, MethodHandle methodHandle) {
            this.member = member2;
            this.handle = methodHandle;
        }

        public int getReferenceKind() {
            int handleKind = this.handle.getHandleKind();
            if (handleKind == 0) {
                return this.member.getDeclaringClass().isInterface() ? 9 : 5;
            }
            if (handleKind != 1) {
                if (handleKind != 2) {
                    if (handleKind == 3) {
                        return 6;
                    }
                    switch (handleKind) {
                        case 8:
                            return 1;
                        case 9:
                            return 3;
                        case 10:
                            return 2;
                        case 11:
                            return 4;
                        default:
                            throw new AssertionError((Object) "Unexpected handle kind: " + this.handle.getHandleKind());
                    }
                } else if (this.member instanceof Constructor) {
                    return 8;
                }
            }
            return 7;
        }

        public Class<?> getDeclaringClass() {
            return this.member.getDeclaringClass();
        }

        public String getName() {
            Member member2 = this.member;
            if (member2 instanceof Constructor) {
                return "<init>";
            }
            return member2.getName();
        }

        public MethodType getMethodType() {
            boolean z;
            MethodType type = this.handle.type();
            if (this.member instanceof Constructor) {
                type = type.changeReturnType(Void.TYPE);
                z = true;
            } else {
                z = false;
            }
            int handleKind = this.handle.getHandleKind();
            if (handleKind == 0 || handleKind == 1 || handleKind == 2 || handleKind == 4 || handleKind == 8 || handleKind == 9) {
                z = true;
            }
            return z ? type.dropParameterTypes(0, 1) : type;
        }

        public <T extends Member> T reflectAs(Class<T> cls, MethodHandles.Lookup lookup) {
            try {
                Class<?> declaringClass = this.member.getDeclaringClass();
                if (Modifier.isNative(getModifiers()) && (MethodHandle.class.isAssignableFrom(declaringClass) || VarHandle.class.isAssignableFrom(declaringClass))) {
                    Member member2 = this.member;
                    if (member2 instanceof Method) {
                        if (((Method) member2).isVarArgs()) {
                            throw new IllegalArgumentException("Reflecting signature polymorphic method");
                        }
                    }
                }
                lookup.checkAccess(declaringClass, declaringClass, this.member.getModifiers(), this.member.getName());
                return this.member;
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException("Unable to access member.", e);
            }
        }

        public int getModifiers() {
            return this.member.getModifiers();
        }

        public String toString() {
            return MethodHandleInfo.toString(getReferenceKind(), getDeclaringClass(), getName(), getMethodType());
        }
    }
}
