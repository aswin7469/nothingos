package java.lang.reflect;

import android.net.TrafficStats;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import java.lang.annotation.Annotation;
import java.util.Objects;
import kotlin.text.Typography;
import libcore.reflect.AnnotatedElements;
import libcore.reflect.GenericSignatureParser;
import libcore.reflect.ListOfTypes;
import libcore.reflect.Types;

public abstract class Executable extends AccessibleObject implements Member, GenericDeclaration {
    private int accessFlags;
    private long artMethod;
    private Class<?> declaringClass;
    private Class<?> declaringClassOfOverriddenMethod;
    private int dexMethodIndex;
    private volatile transient boolean hasRealParameterData;
    private volatile transient Parameter[] parameters;

    private static int fixMethodFlags(int i) {
        if ((i & 1024) != 0) {
            i &= TrafficStats.TAG_NETWORK_STACK_RANGE_END;
        }
        int i2 = i & -33;
        if ((131072 & i2) != 0) {
            i2 |= 32;
        }
        return i2 & 65535;
    }

    private native <T extends Annotation> T getAnnotationNative(Class<T> cls);

    private native Annotation[] getDeclaredAnnotationsNative();

    private native Annotation[][] getParameterAnnotationsNative();

    private native Parameter[] getParameters0();

    private native String[] getSignatureAnnotation();

    private native boolean isAnnotationPresentNative(Class<? extends Annotation> cls);

    /* access modifiers changed from: package-private */
    public native int compareMethodParametersInternal(Method method);

    public abstract Class<?> getDeclaringClass();

    public abstract Class<?>[] getExceptionTypes();

    /* access modifiers changed from: package-private */
    public final native String getMethodNameInternal();

    /* access modifiers changed from: package-private */
    public final native Class<?> getMethodReturnTypeInternal();

    public abstract int getModifiers();

    public abstract String getName();

    public abstract Annotation[][] getParameterAnnotations();

    /* access modifiers changed from: package-private */
    public final native int getParameterCountInternal();

    public abstract Class<?>[] getParameterTypes();

    /* access modifiers changed from: package-private */
    public final native Class<?>[] getParameterTypesInternal();

    public abstract TypeVariable<?>[] getTypeParameters();

    /* access modifiers changed from: package-private */
    public abstract boolean hasGenericInformation();

    /* access modifiers changed from: package-private */
    public abstract void specificToGenericStringHeader(StringBuilder sb);

    /* access modifiers changed from: package-private */
    public abstract void specificToStringHeader(StringBuilder sb);

    public abstract String toGenericString();

    Executable() {
    }

    /* access modifiers changed from: package-private */
    public boolean equalParamTypes(Class<?>[] clsArr, Class<?>[] clsArr2) {
        if (clsArr.length != clsArr2.length) {
            return false;
        }
        for (int i = 0; i < clsArr.length; i++) {
            if (clsArr[i] != clsArr2[i]) {
                return false;
            }
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    public void separateWithCommas(Class<?>[] clsArr, StringBuilder sb) {
        for (int i = 0; i < clsArr.length; i++) {
            sb.append(clsArr[i].getTypeName());
            if (i < clsArr.length - 1) {
                sb.append(NavigationBarInflaterView.BUTTON_SEPARATOR);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void printModifiersIfNonzero(StringBuilder sb, int i, boolean z) {
        int modifiers = getModifiers() & i;
        if (modifiers == 0 || z) {
            int i2 = modifiers & 7;
            if (i2 != 0) {
                sb.append(Modifier.toString(i2));
                sb.append(' ');
            }
            if (z) {
                sb.append("default ");
            }
            int i3 = modifiers & -8;
            if (i3 != 0) {
                sb.append(Modifier.toString(i3));
                sb.append(' ');
                return;
            }
            return;
        }
        sb.append(Modifier.toString(modifiers));
        sb.append(' ');
    }

    /* access modifiers changed from: package-private */
    public String sharedToString(int i, boolean z, Class<?>[] clsArr, Class<?>[] clsArr2) {
        try {
            StringBuilder sb = new StringBuilder();
            printModifiersIfNonzero(sb, i, z);
            specificToStringHeader(sb);
            sb.append('(');
            separateWithCommas(clsArr, sb);
            sb.append(')');
            if (clsArr2.length > 0) {
                sb.append(" throws ");
                separateWithCommas(clsArr2, sb);
            }
            return sb.toString();
        } catch (Exception e) {
            return "<" + e + ">";
        }
    }

    /* access modifiers changed from: package-private */
    public String sharedToGenericString(int i, boolean z) {
        String str;
        try {
            StringBuilder sb = new StringBuilder();
            printModifiersIfNonzero(sb, i, z);
            TypeVariable[] typeParameters = getTypeParameters();
            if (typeParameters.length > 0) {
                sb.append((char) Typography.less);
                int length = typeParameters.length;
                int i2 = 0;
                boolean z2 = true;
                while (i2 < length) {
                    TypeVariable typeVariable = typeParameters[i2];
                    if (!z2) {
                        sb.append(',');
                    }
                    sb.append(typeVariable.toString());
                    i2++;
                    z2 = false;
                }
                sb.append("> ");
            }
            specificToGenericStringHeader(sb);
            sb.append('(');
            Type[] genericParameterTypes = getGenericParameterTypes();
            for (int i3 = 0; i3 < genericParameterTypes.length; i3++) {
                String typeName = genericParameterTypes[i3].getTypeName();
                if (isVarArgs() && i3 == genericParameterTypes.length - 1) {
                    typeName = typeName.replaceFirst("\\[\\]$", "...");
                }
                sb.append(typeName);
                if (i3 < genericParameterTypes.length - 1) {
                    sb.append(',');
                }
            }
            sb.append(')');
            Type[] genericExceptionTypes = getGenericExceptionTypes();
            if (genericExceptionTypes.length > 0) {
                sb.append(" throws ");
                for (int i4 = 0; i4 < genericExceptionTypes.length; i4++) {
                    Type type = genericExceptionTypes[i4];
                    if (type instanceof Class) {
                        str = ((Class) type).getName();
                    } else {
                        str = type.toString();
                    }
                    sb.append(str);
                    if (i4 < genericExceptionTypes.length - 1) {
                        sb.append(',');
                    }
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return "<" + e + ">";
        }
    }

    public int getParameterCount() {
        throw new AbstractMethodError();
    }

    public Type[] getGenericParameterTypes() {
        return Types.getTypeArray(getMethodOrConstructorGenericInfoInternal().genericParameterTypes, false);
    }

    /* access modifiers changed from: package-private */
    public Type[] getAllGenericParameterTypes() {
        if (!hasGenericInformation()) {
            return getParameterTypes();
        }
        boolean hasRealParameterData2 = hasRealParameterData();
        Type[] genericParameterTypes = getGenericParameterTypes();
        Class[] parameterTypes = getParameterTypes();
        int length = parameterTypes.length;
        Type[] typeArr = new Type[length];
        Parameter[] parameters2 = getParameters();
        if (!hasRealParameterData2) {
            return genericParameterTypes.length == parameterTypes.length ? genericParameterTypes : parameterTypes;
        }
        int i = 0;
        for (int i2 = 0; i2 < length; i2++) {
            Parameter parameter = parameters2[i2];
            if (parameter.isSynthetic() || parameter.isImplicit()) {
                typeArr[i2] = parameterTypes[i2];
            } else {
                typeArr[i2] = genericParameterTypes[i];
                i++;
            }
        }
        return typeArr;
    }

    public Parameter[] getParameters() {
        return (Parameter[]) privateGetParameters().clone();
    }

    private Parameter[] synthesizeAllParams() {
        int parameterCount = getParameterCount();
        Parameter[] parameterArr = new Parameter[parameterCount];
        for (int i = 0; i < parameterCount; i++) {
            parameterArr[i] = new Parameter("arg" + i, 0, this, i);
        }
        return parameterArr;
    }

    private void verifyParameters(Parameter[] parameterArr) {
        if (getParameterTypes().length == parameterArr.length) {
            int length = parameterArr.length;
            int i = 0;
            while (i < length) {
                Parameter parameter = parameterArr[i];
                String realName = parameter.getRealName();
                int modifiers = parameter.getModifiers();
                if (realName != null && (realName.isEmpty() || realName.indexOf(46) != -1 || realName.indexOf(59) != -1 || realName.indexOf(91) != -1 || realName.indexOf(47) != -1)) {
                    throw new MalformedParametersException("Invalid parameter name \"" + realName + "\"");
                } else if (modifiers == (36880 & modifiers)) {
                    i++;
                } else {
                    throw new MalformedParametersException("Invalid parameter modifiers");
                }
            }
            return;
        }
        throw new MalformedParametersException("Wrong number of parameters in MethodParameters attribute");
    }

    private Parameter[] privateGetParameters() {
        Parameter[] parameterArr = this.parameters;
        if (parameterArr == null) {
            try {
                parameterArr = getParameters0();
                if (parameterArr == null) {
                    this.hasRealParameterData = false;
                    parameterArr = synthesizeAllParams();
                } else {
                    this.hasRealParameterData = true;
                    verifyParameters(parameterArr);
                }
                this.parameters = parameterArr;
            } catch (IllegalArgumentException e) {
                MalformedParametersException malformedParametersException = new MalformedParametersException("Invalid parameter metadata in class file");
                malformedParametersException.initCause(e);
                throw malformedParametersException;
            }
        }
        return parameterArr;
    }

    /* access modifiers changed from: package-private */
    public boolean hasRealParameterData() {
        if (this.parameters == null) {
            privateGetParameters();
        }
        return this.hasRealParameterData;
    }

    public Type[] getGenericExceptionTypes() {
        return Types.getTypeArray(getMethodOrConstructorGenericInfoInternal().genericExceptionTypes, false);
    }

    public boolean isVarArgs() {
        return (this.accessFlags & 128) != 0;
    }

    public boolean isSynthetic() {
        return (this.accessFlags & 4096) != 0;
    }

    public <T extends Annotation> T getAnnotation(Class<T> cls) {
        Objects.requireNonNull(cls);
        return getAnnotationNative(cls);
    }

    public <T extends Annotation> T[] getAnnotationsByType(Class<T> cls) {
        return AnnotatedElements.getDirectOrIndirectAnnotationsByType(this, cls);
    }

    public Annotation[] getDeclaredAnnotations() {
        return getDeclaredAnnotationsNative();
    }

    /* access modifiers changed from: package-private */
    public final int getModifiersInternal() {
        return fixMethodFlags(this.accessFlags);
    }

    /* access modifiers changed from: package-private */
    public final Class<?> getDeclaringClassInternal() {
        return this.declaringClass;
    }

    public final boolean isAnnotationPresent(Class<? extends Annotation> cls) {
        Objects.requireNonNull(cls);
        return isAnnotationPresentNative(cls);
    }

    /* access modifiers changed from: package-private */
    public final Annotation[][] getParameterAnnotationsInternal() {
        Annotation[][] parameterAnnotationsNative = getParameterAnnotationsNative();
        if (parameterAnnotationsNative != null) {
            return parameterAnnotationsNative;
        }
        int length = getParameterTypes().length;
        int[] iArr = new int[2];
        iArr[1] = 0;
        iArr[0] = length;
        return (Annotation[][]) Array.newInstance((Class<?>) Annotation.class, iArr);
    }

    public final int getAccessFlags() {
        return this.accessFlags;
    }

    public final long getArtMethod() {
        return this.artMethod;
    }

    static final class GenericInfo {
        final TypeVariable<?>[] formalTypeParameters;
        final ListOfTypes genericExceptionTypes;
        final ListOfTypes genericParameterTypes;
        final Type genericReturnType;

        GenericInfo(ListOfTypes listOfTypes, ListOfTypes listOfTypes2, Type type, TypeVariable<?>[] typeVariableArr) {
            this.genericExceptionTypes = listOfTypes;
            this.genericParameterTypes = listOfTypes2;
            this.genericReturnType = type;
            this.formalTypeParameters = typeVariableArr;
        }
    }

    /* access modifiers changed from: package-private */
    public final boolean hasGenericInformationInternal() {
        return getSignatureAnnotation() != null;
    }

    /* access modifiers changed from: package-private */
    public final GenericInfo getMethodOrConstructorGenericInfoInternal() {
        String signatureAttribute = getSignatureAttribute();
        Class[] exceptionTypes = getExceptionTypes();
        GenericSignatureParser genericSignatureParser = new GenericSignatureParser(getDeclaringClass().getClassLoader());
        if (this instanceof Method) {
            genericSignatureParser.parseForMethod(this, signatureAttribute, exceptionTypes);
        } else {
            genericSignatureParser.parseForConstructor(this, signatureAttribute, exceptionTypes);
        }
        return new GenericInfo(genericSignatureParser.exceptionTypes, genericSignatureParser.parameterTypes, genericSignatureParser.returnType, genericSignatureParser.formalTypeParameters);
    }

    private String getSignatureAttribute() {
        String[] signatureAnnotation = getSignatureAnnotation();
        if (signatureAnnotation == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String append : signatureAnnotation) {
            sb.append(append);
        }
        return sb.toString();
    }

    /* access modifiers changed from: package-private */
    public final boolean equalNameAndParametersInternal(Method method) {
        return getName().equals(method.getName()) && compareMethodParametersInternal(method) == 0;
    }

    /* access modifiers changed from: package-private */
    public final boolean isDefaultMethodInternal() {
        return (this.accessFlags & 4194304) != 0;
    }

    /* access modifiers changed from: package-private */
    public final boolean isBridgeMethodInternal() {
        return (this.accessFlags & 64) != 0;
    }
}
