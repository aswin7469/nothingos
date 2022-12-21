package libcore.reflect;

import java.lang.annotation.Annotation;
import java.lang.annotation.IncompleteAnnotationException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.p026io.IOException;
import java.p026io.ObjectInputStream;
import java.p026io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.WeakHashMap;

public final class AnnotationFactory implements InvocationHandler, Serializable {
    private static final transient Map<Class<? extends Annotation>, AnnotationMember[]> cache = new WeakHashMap();
    private AnnotationMember[] elements;
    private final Class<? extends Annotation> klazz;

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0014, code lost:
        r0 = r8.getDeclaredMethods();
        r1 = new libcore.reflect.AnnotationMember[r0.length];
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001d, code lost:
        if (r2 >= r0.length) goto L_0x0040;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x001f, code lost:
        r3 = r0[r2];
        r4 = r3.getName();
        r5 = r3.getReturnType();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:?, code lost:
        r1[r2] = new libcore.reflect.AnnotationMember(r4, r3.getDefaultValue(), r5, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0035, code lost:
        r6 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0036, code lost:
        r1[r2] = new libcore.reflect.AnnotationMember(r4, r6, r5, r3);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0040, code lost:
        r0 = cache;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0042, code lost:
        monitor-enter(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:?, code lost:
        r0.put(r8, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:23:0x0046, code lost:
        monitor-exit(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:0x0047, code lost:
        return r1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:29:0x0062, code lost:
        throw new java.lang.IllegalArgumentException("Type is not annotation: " + r8.getName());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0012, code lost:
        if (r8.isAnnotation() == false) goto L_0x004b;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static libcore.reflect.AnnotationMember[] getElementsDescription(java.lang.Class<? extends java.lang.annotation.Annotation> r8) {
        /*
            java.util.Map<java.lang.Class<? extends java.lang.annotation.Annotation>, libcore.reflect.AnnotationMember[]> r0 = cache
            monitor-enter(r0)
            java.lang.Object r1 = r0.get(r8)     // Catch:{ all -> 0x0063 }
            libcore.reflect.AnnotationMember[] r1 = (libcore.reflect.AnnotationMember[]) r1     // Catch:{ all -> 0x0063 }
            if (r1 == 0) goto L_0x000d
            monitor-exit(r0)     // Catch:{ all -> 0x0063 }
            return r1
        L_0x000d:
            monitor-exit(r0)     // Catch:{ all -> 0x0063 }
            boolean r0 = r8.isAnnotation()
            if (r0 == 0) goto L_0x004b
            java.lang.reflect.Method[] r0 = r8.getDeclaredMethods()
            int r1 = r0.length
            libcore.reflect.AnnotationMember[] r1 = new libcore.reflect.AnnotationMember[r1]
            r2 = 0
        L_0x001c:
            int r3 = r0.length
            if (r2 >= r3) goto L_0x0040
            r3 = r0[r2]
            java.lang.String r4 = r3.getName()
            java.lang.Class r5 = r3.getReturnType()
            libcore.reflect.AnnotationMember r6 = new libcore.reflect.AnnotationMember     // Catch:{ all -> 0x0035 }
            java.lang.Object r7 = r3.getDefaultValue()     // Catch:{ all -> 0x0035 }
            r6.<init>(r4, r7, r5, r3)     // Catch:{ all -> 0x0035 }
            r1[r2] = r6     // Catch:{ all -> 0x0035 }
            goto L_0x003d
        L_0x0035:
            r6 = move-exception
            libcore.reflect.AnnotationMember r7 = new libcore.reflect.AnnotationMember
            r7.<init>(r4, r6, r5, r3)
            r1[r2] = r7
        L_0x003d:
            int r2 = r2 + 1
            goto L_0x001c
        L_0x0040:
            java.util.Map<java.lang.Class<? extends java.lang.annotation.Annotation>, libcore.reflect.AnnotationMember[]> r0 = cache
            monitor-enter(r0)
            r0.put(r8, r1)     // Catch:{ all -> 0x0048 }
            monitor-exit(r0)     // Catch:{ all -> 0x0048 }
            return r1
        L_0x0048:
            r8 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0048 }
            throw r8
        L_0x004b:
            java.lang.IllegalArgumentException r0 = new java.lang.IllegalArgumentException
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            java.lang.String r2 = "Type is not annotation: "
            r1.<init>((java.lang.String) r2)
            java.lang.String r8 = r8.getName()
            r1.append((java.lang.String) r8)
            java.lang.String r8 = r1.toString()
            r0.<init>((java.lang.String) r8)
            throw r0
        L_0x0063:
            r8 = move-exception
            monitor-exit(r0)     // Catch:{ all -> 0x0063 }
            throw r8
        */
        throw new UnsupportedOperationException("Method not decompiled: libcore.reflect.AnnotationFactory.getElementsDescription(java.lang.Class):libcore.reflect.AnnotationMember[]");
    }

    public static <A extends Annotation> A createAnnotation(Class<? extends Annotation> cls, AnnotationMember[] annotationMemberArr) {
        AnnotationFactory annotationFactory = new AnnotationFactory(cls, annotationMemberArr);
        return (Annotation) Proxy.newProxyInstance(cls.getClassLoader(), new Class[]{cls}, annotationFactory);
    }

    private AnnotationFactory(Class<? extends Annotation> cls, AnnotationMember[] annotationMemberArr) {
        this.klazz = cls;
        AnnotationMember[] elementsDescription = getElementsDescription(cls);
        if (annotationMemberArr == null) {
            this.elements = elementsDescription;
            return;
        }
        AnnotationMember[] annotationMemberArr2 = new AnnotationMember[elementsDescription.length];
        this.elements = annotationMemberArr2;
        for (int length = annotationMemberArr2.length - 1; length >= 0; length--) {
            int length2 = annotationMemberArr.length;
            int i = 0;
            while (true) {
                if (i >= length2) {
                    this.elements[length] = elementsDescription[length];
                    break;
                }
                AnnotationMember annotationMember = annotationMemberArr[i];
                if (annotationMember.name.equals(elementsDescription[length].name)) {
                    this.elements[length] = annotationMember.setDefinition(elementsDescription[length]);
                    break;
                }
                i++;
            }
        }
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        AnnotationMember[] elementsDescription = getElementsDescription(this.klazz);
        AnnotationMember[] annotationMemberArr = this.elements;
        ArrayList arrayList = new ArrayList(elementsDescription.length + annotationMemberArr.length);
        for (AnnotationMember annotationMember : annotationMemberArr) {
            int length = elementsDescription.length;
            int i = 0;
            while (true) {
                if (i >= length) {
                    arrayList.add(annotationMember);
                    break;
                } else if (elementsDescription[i].name.equals(annotationMember.name)) {
                    break;
                } else {
                    i++;
                }
            }
        }
        for (AnnotationMember annotationMember2 : elementsDescription) {
            int length2 = annotationMemberArr.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length2) {
                    arrayList.add(annotationMember2);
                    break;
                }
                AnnotationMember annotationMember3 = annotationMemberArr[i2];
                if (annotationMember3.name.equals(annotationMember2.name)) {
                    arrayList.add(annotationMember3.setDefinition(annotationMember2));
                    break;
                }
                i2++;
            }
        }
        this.elements = (AnnotationMember[]) arrayList.toArray(new AnnotationMember[arrayList.size()]);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!this.klazz.isInstance(obj)) {
            return false;
        }
        if (Proxy.isProxyClass(obj.getClass())) {
            InvocationHandler invocationHandler = Proxy.getInvocationHandler(obj);
            if (invocationHandler instanceof AnnotationFactory) {
                AnnotationFactory annotationFactory = (AnnotationFactory) invocationHandler;
                AnnotationMember[] annotationMemberArr = this.elements;
                if (annotationMemberArr.length != annotationFactory.elements.length) {
                    return false;
                }
                int length = annotationMemberArr.length;
                int i = 0;
                while (i < length) {
                    AnnotationMember annotationMember = annotationMemberArr[i];
                    AnnotationMember[] annotationMemberArr2 = annotationFactory.elements;
                    int length2 = annotationMemberArr2.length;
                    int i2 = 0;
                    while (i2 < length2) {
                        if (annotationMember.equals(annotationMemberArr2[i2])) {
                            i++;
                        } else {
                            i2++;
                        }
                    }
                    return false;
                }
                return true;
            }
        }
        AnnotationMember[] annotationMemberArr3 = this.elements;
        int length3 = annotationMemberArr3.length;
        int i3 = 0;
        while (i3 < length3) {
            AnnotationMember annotationMember2 = annotationMemberArr3[i3];
            if (annotationMember2.tag == '!') {
                return false;
            }
            try {
                if (!annotationMember2.definingMethod.isAccessible()) {
                    annotationMember2.definingMethod.setAccessible(true);
                }
                Object invoke = annotationMember2.definingMethod.invoke(obj, new Object[0]);
                if (invoke != null) {
                    if (annotationMember2.tag == '[') {
                        if (!annotationMember2.equalArrayValue(invoke)) {
                            return false;
                        }
                    } else if (!annotationMember2.value.equals(invoke)) {
                        return false;
                    }
                } else if (annotationMember2.value != AnnotationMember.NO_VALUE) {
                    return false;
                }
                i3++;
            } catch (Throwable unused) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        int i = 0;
        for (AnnotationMember hashCode : this.elements) {
            i += hashCode.hashCode();
        }
        return i;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("@");
        sb.append(this.klazz.getName());
        sb.append('(');
        for (int i = 0; i < this.elements.length; i++) {
            if (i != 0) {
                sb.append(", ");
            }
            sb.append((Object) this.elements[i]);
        }
        sb.append(')');
        return sb.toString();
    }

    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        AnnotationMember annotationMember;
        String name = method.getName();
        Class<Object>[] parameterTypes = method.getParameterTypes();
        int i = 0;
        if (parameterTypes.length == 0) {
            if ("annotationType".equals(name)) {
                return this.klazz;
            }
            if ("toString".equals(name)) {
                return toString();
            }
            if ("hashCode".equals(name)) {
                return Integer.valueOf(hashCode());
            }
            AnnotationMember[] annotationMemberArr = this.elements;
            int length = annotationMemberArr.length;
            while (true) {
                if (i >= length) {
                    annotationMember = null;
                    break;
                }
                annotationMember = annotationMemberArr[i];
                if (name.equals(annotationMember.name)) {
                    break;
                }
                i++;
            }
            if (annotationMember == null || !method.equals(annotationMember.definingMethod)) {
                throw new IllegalArgumentException(method.toString());
            }
            Object validateValue = annotationMember.validateValue();
            if (validateValue != null) {
                return validateValue;
            }
            throw new IncompleteAnnotationException(this.klazz, name);
        } else if (parameterTypes.length == 1 && parameterTypes[0] == Object.class && "equals".equals(name)) {
            return Boolean.valueOf(equals(objArr[0]));
        } else {
            throw new IllegalArgumentException("Invalid method for annotation type: " + method);
        }
    }
}
