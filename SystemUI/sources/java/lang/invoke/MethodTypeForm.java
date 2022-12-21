package java.lang.invoke;

import sun.invoke.util.Wrapper;

final class MethodTypeForm {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    public static final int ERASE = 1;
    public static final int INTS = 4;
    public static final int LONGS = 5;
    public static final int NO_CHANGE = 0;
    public static final int RAW_RETURN = 6;
    public static final int UNWRAP = 3;
    public static final int WRAP = 2;
    final long argCounts;
    final int[] argToSlotTable;
    final MethodType basicType;
    final MethodType erasedType;
    final long primCounts;
    final int[] slotToArgTable;

    private boolean assertIsBasicType() {
        return true;
    }

    private static long pack(int i, int i2, int i3, int i4) {
        return (((long) ((i << 16) | i2)) << 32) | ((long) ((i3 << 16) | i4));
    }

    private static char unpack(long j, int i) {
        return (char) ((int) (j >> ((3 - i) * 16)));
    }

    public MethodType erasedType() {
        return this.erasedType;
    }

    public MethodType basicType() {
        return this.basicType;
    }

    protected MethodTypeForm(MethodType methodType) {
        int i;
        int i2;
        int i3;
        boolean z;
        Class cls;
        int[] iArr;
        int[] iArr2;
        int[] iArr3;
        this.erasedType = methodType;
        Class<Integer>[] ptypes = methodType.ptypes();
        int length = ptypes.length;
        int i4 = 0;
        Class<Integer>[] clsArr = ptypes;
        int i5 = 0;
        int i6 = 0;
        for (int i7 = 0; i7 < ptypes.length; i7++) {
            Class<Integer> cls2 = ptypes[i7];
            if (cls2 != Object.class) {
                i6++;
                Wrapper forPrimitiveType = Wrapper.forPrimitiveType((Class<?>) cls2);
                i5 = forPrimitiveType.isDoubleWord() ? i5 + 1 : i5;
                if (forPrimitiveType.isSubwordOrInt() && cls2 != Integer.TYPE) {
                    clsArr = clsArr == ptypes ? (Class[]) clsArr.clone() : clsArr;
                    clsArr[i7] = Integer.TYPE;
                }
            }
        }
        int i8 = length + i5;
        Class returnType = methodType.returnType();
        if (returnType != Object.class) {
            Wrapper forPrimitiveType2 = Wrapper.forPrimitiveType((Class<?>) returnType);
            z = forPrimitiveType2.isDoubleWord();
            cls = (!forPrimitiveType2.isSubwordOrInt() || returnType == Integer.TYPE) ? returnType : Integer.TYPE;
            if (returnType == Void.TYPE) {
                i3 = 0;
                i2 = 0;
                i = 1;
            } else {
                i3 = true + (z ? 1 : 0);
                i2 = 1;
                i = 1;
            }
        } else {
            z = false;
            i = 0;
            cls = returnType;
            i3 = 1;
            i2 = 1;
        }
        if (ptypes == clsArr && cls == returnType) {
            this.basicType = methodType;
            if (i5 != 0) {
                iArr = new int[(i8 + 1)];
                iArr2 = new int[(length + 1)];
                iArr2[0] = i8;
                int i9 = i8;
                while (i4 < ptypes.length) {
                    if (Wrapper.forBasicType((Class<?>) ptypes[i4]).isDoubleWord()) {
                        i9--;
                    }
                    i9--;
                    i4++;
                    iArr[i9] = i4;
                    iArr2[i4] = i9;
                }
            } else {
                if (i6 != 0) {
                    MethodTypeForm form = MethodType.genericMethodType(length).form();
                    iArr3 = form.slotToArgTable;
                    iArr2 = form.argToSlotTable;
                } else {
                    int i10 = length + 1;
                    iArr3 = new int[i10];
                    iArr2 = new int[i10];
                    iArr2[0] = length;
                    int i11 = length;
                    while (i4 < length) {
                        i11--;
                        i4++;
                        iArr3[i11] = i4;
                        iArr2[i4] = i11;
                    }
                }
                iArr = iArr3;
            }
            this.primCounts = pack(z ? 1 : 0, i, i5, i6);
            this.argCounts = pack(i3, i2, i8, length);
            this.argToSlotTable = iArr2;
            this.slotToArgTable = iArr;
            if (i8 >= 256) {
                throw MethodHandleStatics.newIllegalArgumentException("too many arguments");
            }
            return;
        }
        MethodType makeImpl = MethodType.makeImpl(cls, clsArr, true);
        this.basicType = makeImpl;
        MethodTypeForm form2 = makeImpl.form();
        this.primCounts = form2.primCounts;
        this.argCounts = form2.argCounts;
        this.argToSlotTable = form2.argToSlotTable;
        this.slotToArgTable = form2.slotToArgTable;
    }

    public int parameterCount() {
        return unpack(this.argCounts, 3);
    }

    public int parameterSlotCount() {
        return unpack(this.argCounts, 2);
    }

    public int returnCount() {
        return unpack(this.argCounts, 1);
    }

    public int returnSlotCount() {
        return unpack(this.argCounts, 0);
    }

    public int primitiveParameterCount() {
        return unpack(this.primCounts, 3);
    }

    public int longPrimitiveParameterCount() {
        return unpack(this.primCounts, 2);
    }

    public int primitiveReturnCount() {
        return unpack(this.primCounts, 1);
    }

    public int longPrimitiveReturnCount() {
        return unpack(this.primCounts, 0);
    }

    public boolean hasPrimitives() {
        return this.primCounts != 0;
    }

    public boolean hasNonVoidPrimitives() {
        if (this.primCounts == 0) {
            return false;
        }
        if (primitiveParameterCount() != 0) {
            return true;
        }
        if (primitiveReturnCount() == 0 || returnCount() == 0) {
            return false;
        }
        return true;
    }

    public boolean hasLongPrimitives() {
        return (longPrimitiveReturnCount() | longPrimitiveParameterCount()) != 0;
    }

    public int parameterToArgSlot(int i) {
        return this.argToSlotTable[i + 1];
    }

    public int argSlotToParameter(int i) {
        return this.slotToArgTable[i] - 1;
    }

    static MethodTypeForm findForm(MethodType methodType) {
        MethodType canonicalize = canonicalize(methodType, 1, 1);
        if (canonicalize == null) {
            return new MethodTypeForm(methodType);
        }
        return canonicalize.form();
    }

    public static MethodType canonicalize(MethodType methodType, int i, int i2) {
        Class[] ptypes = methodType.ptypes();
        Class[] canonicalizeAll = canonicalizeAll(ptypes, i2);
        Class<?> returnType = methodType.returnType();
        Class<?> canonicalize = canonicalize(returnType, i);
        if (canonicalizeAll == null && canonicalize == null) {
            return null;
        }
        if (canonicalize != null) {
            returnType = canonicalize;
        }
        if (canonicalizeAll != null) {
            ptypes = canonicalizeAll;
        }
        return MethodType.makeImpl(returnType, ptypes, true);
    }

    static Class<?> canonicalize(Class<?> cls, int i) {
        if (cls != Object.class) {
            if (!cls.isPrimitive()) {
                if (i == 1) {
                    return Object.class;
                }
                if (i == 3) {
                    Class<?> asPrimitiveType = Wrapper.asPrimitiveType(cls);
                    if (asPrimitiveType != cls) {
                        return asPrimitiveType;
                    }
                } else if (i != 6) {
                    return null;
                } else {
                    return Object.class;
                }
            } else if (cls == Void.TYPE) {
                if (i == 2) {
                    return Void.class;
                }
                if (i == 6) {
                    return Integer.TYPE;
                }
            } else if (i == 2) {
                return Wrapper.asWrapperType(cls);
            } else {
                if (i != 4) {
                    if (i != 5) {
                        if (i != 6 || cls == Integer.TYPE || cls == Long.TYPE || cls == Float.TYPE || cls == Double.TYPE) {
                            return null;
                        }
                        return Integer.TYPE;
                    } else if (cls == Long.TYPE) {
                        return null;
                    } else {
                        return Long.TYPE;
                    }
                } else if (cls == Integer.TYPE || cls == Long.TYPE) {
                    return null;
                } else {
                    if (cls == Double.TYPE) {
                        return Long.TYPE;
                    }
                    return Integer.TYPE;
                }
            }
        }
        return null;
    }

    static Class<?>[] canonicalizeAll(Class<?>[] clsArr, int i) {
        int length = clsArr.length;
        Class<?>[] clsArr2 = null;
        for (int i2 = 0; i2 < length; i2++) {
            Class<?> canonicalize = canonicalize(clsArr[i2], i);
            if (canonicalize == Void.TYPE) {
                canonicalize = null;
            }
            if (canonicalize != null) {
                if (clsArr2 == null) {
                    clsArr2 = (Class[]) clsArr.clone();
                }
                clsArr2[i2] = canonicalize;
            }
        }
        return clsArr2;
    }

    public String toString() {
        return "Form" + this.erasedType;
    }
}
