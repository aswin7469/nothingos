package com.google.protobuf;

import com.google.protobuf.ByteString;
import com.google.protobuf.FieldSet;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MapEntryLite;
import com.google.protobuf.Writer;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import sun.misc.Unsafe;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class MessageSchema<T> implements Schema<T> {
    private static final int[] EMPTY_INT_ARRAY = new int[0];
    private static final Unsafe UNSAFE = UnsafeUtil.getUnsafe();
    private final int[] buffer;
    private final int checkInitializedCount;
    private final MessageLite defaultInstance;
    private final ExtensionSchema<?> extensionSchema;
    private final boolean hasExtensions;
    private final int[] intArray;
    private final ListFieldSchema listFieldSchema;
    private final boolean lite;
    private final MapFieldSchema mapFieldSchema;
    private final int maxFieldNumber;
    private final int minFieldNumber;
    private final NewInstanceSchema newInstanceSchema;
    private final Object[] objects;
    private final boolean proto3;
    private final int repeatedFieldOffsetStart;
    private final UnknownFieldSchema<?, ?> unknownFieldSchema;
    private final boolean useCachedSizeField;

    private static boolean isEnforceUtf8(int i) {
        return (i & 536870912) != 0;
    }

    private static boolean isRequired(int i) {
        return (i & 268435456) != 0;
    }

    private static long offset(int i) {
        return i & 1048575;
    }

    private static int type(int i) {
        return (i & 267386880) >>> 20;
    }

    private MessageSchema(int[] iArr, Object[] objArr, int i, int i2, MessageLite messageLite, boolean z, boolean z2, int[] iArr2, int i3, int i4, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema<?, ?> unknownFieldSchema, ExtensionSchema<?> extensionSchema, MapFieldSchema mapFieldSchema) {
        this.buffer = iArr;
        this.objects = objArr;
        this.minFieldNumber = i;
        this.maxFieldNumber = i2;
        this.lite = messageLite instanceof GeneratedMessageLite;
        this.proto3 = z;
        this.hasExtensions = extensionSchema != null && extensionSchema.hasExtensions(messageLite);
        this.useCachedSizeField = z2;
        this.intArray = iArr2;
        this.checkInitializedCount = i3;
        this.repeatedFieldOffsetStart = i4;
        this.newInstanceSchema = newInstanceSchema;
        this.listFieldSchema = listFieldSchema;
        this.unknownFieldSchema = unknownFieldSchema;
        this.extensionSchema = extensionSchema;
        this.defaultInstance = messageLite;
        this.mapFieldSchema = mapFieldSchema;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static <T> MessageSchema<T> newSchema(Class<T> cls, MessageInfo messageInfo, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema<?, ?> unknownFieldSchema, ExtensionSchema<?> extensionSchema, MapFieldSchema mapFieldSchema) {
        if (messageInfo instanceof RawMessageInfo) {
            return newSchemaForRawMessageInfo((RawMessageInfo) messageInfo, newInstanceSchema, listFieldSchema, unknownFieldSchema, extensionSchema, mapFieldSchema);
        }
        return newSchemaForMessageInfo((StructuralMessageInfo) messageInfo, newInstanceSchema, listFieldSchema, unknownFieldSchema, extensionSchema, mapFieldSchema);
    }

    /* JADX WARN: Removed duplicated region for block: B:105:0x033c  */
    /* JADX WARN: Removed duplicated region for block: B:121:0x039e  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x0391  */
    /* JADX WARN: Removed duplicated region for block: B:65:0x0277  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0292  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0295  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x027a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    static <T> MessageSchema<T> newSchemaForRawMessageInfo(RawMessageInfo rawMessageInfo, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema<?, ?> unknownFieldSchema, ExtensionSchema<?> extensionSchema, MapFieldSchema mapFieldSchema) {
        int i;
        int charAt;
        int charAt2;
        int charAt3;
        int i2;
        int i3;
        int[] iArr;
        int i4;
        int i5;
        char charAt4;
        int i6;
        char charAt5;
        int i7;
        char charAt6;
        int i8;
        char charAt7;
        int i9;
        char charAt8;
        int i10;
        char charAt9;
        int i11;
        char charAt10;
        int i12;
        char charAt11;
        int i13;
        int i14;
        boolean z;
        int i15;
        int[] iArr2;
        int i16;
        int i17;
        String str;
        Class<?> cls;
        int i18;
        int i19;
        int i20;
        int i21;
        int i22;
        int i23;
        Field reflectField;
        int i24;
        char charAt12;
        int i25;
        int i26;
        Object obj;
        Field reflectField2;
        Object obj2;
        Field reflectField3;
        int i27;
        char charAt13;
        int i28;
        char charAt14;
        int i29;
        char charAt15;
        int i30;
        char charAt16;
        char charAt17;
        int i31 = 0;
        boolean z2 = rawMessageInfo.getSyntax() == ProtoSyntax.PROTO3;
        String stringInfo = rawMessageInfo.getStringInfo();
        int length = stringInfo.length();
        int charAt18 = stringInfo.charAt(0);
        if (charAt18 >= 55296) {
            int i32 = charAt18 & 8191;
            int i33 = 1;
            int i34 = 13;
            while (true) {
                i = i33 + 1;
                charAt17 = stringInfo.charAt(i33);
                if (charAt17 < 55296) {
                    break;
                }
                i32 |= (charAt17 & 8191) << i34;
                i34 += 13;
                i33 = i;
            }
            charAt18 = i32 | (charAt17 << i34);
        } else {
            i = 1;
        }
        int i35 = i + 1;
        int charAt19 = stringInfo.charAt(i);
        if (charAt19 >= 55296) {
            int i36 = charAt19 & 8191;
            int i37 = 13;
            while (true) {
                i30 = i35 + 1;
                charAt16 = stringInfo.charAt(i35);
                if (charAt16 < 55296) {
                    break;
                }
                i36 |= (charAt16 & 8191) << i37;
                i37 += 13;
                i35 = i30;
            }
            charAt19 = i36 | (charAt16 << i37);
            i35 = i30;
        }
        if (charAt19 == 0) {
            i4 = 0;
            charAt = 0;
            charAt2 = 0;
            i2 = 0;
            charAt3 = 0;
            iArr = EMPTY_INT_ARRAY;
            i3 = 0;
        } else {
            int i38 = i35 + 1;
            int charAt20 = stringInfo.charAt(i35);
            if (charAt20 >= 55296) {
                int i39 = charAt20 & 8191;
                int i40 = 13;
                while (true) {
                    i12 = i38 + 1;
                    charAt11 = stringInfo.charAt(i38);
                    if (charAt11 < 55296) {
                        break;
                    }
                    i39 |= (charAt11 & 8191) << i40;
                    i40 += 13;
                    i38 = i12;
                }
                charAt20 = i39 | (charAt11 << i40);
                i38 = i12;
            }
            int i41 = i38 + 1;
            int charAt21 = stringInfo.charAt(i38);
            if (charAt21 >= 55296) {
                int i42 = charAt21 & 8191;
                int i43 = 13;
                while (true) {
                    i11 = i41 + 1;
                    charAt10 = stringInfo.charAt(i41);
                    if (charAt10 < 55296) {
                        break;
                    }
                    i42 |= (charAt10 & 8191) << i43;
                    i43 += 13;
                    i41 = i11;
                }
                charAt21 = i42 | (charAt10 << i43);
                i41 = i11;
            }
            int i44 = i41 + 1;
            int charAt22 = stringInfo.charAt(i41);
            if (charAt22 >= 55296) {
                int i45 = charAt22 & 8191;
                int i46 = 13;
                while (true) {
                    i10 = i44 + 1;
                    charAt9 = stringInfo.charAt(i44);
                    if (charAt9 < 55296) {
                        break;
                    }
                    i45 |= (charAt9 & 8191) << i46;
                    i46 += 13;
                    i44 = i10;
                }
                charAt22 = i45 | (charAt9 << i46);
                i44 = i10;
            }
            int i47 = i44 + 1;
            charAt = stringInfo.charAt(i44);
            if (charAt >= 55296) {
                int i48 = charAt & 8191;
                int i49 = 13;
                while (true) {
                    i9 = i47 + 1;
                    charAt8 = stringInfo.charAt(i47);
                    if (charAt8 < 55296) {
                        break;
                    }
                    i48 |= (charAt8 & 8191) << i49;
                    i49 += 13;
                    i47 = i9;
                }
                charAt = i48 | (charAt8 << i49);
                i47 = i9;
            }
            int i50 = i47 + 1;
            charAt2 = stringInfo.charAt(i47);
            if (charAt2 >= 55296) {
                int i51 = charAt2 & 8191;
                int i52 = 13;
                while (true) {
                    i8 = i50 + 1;
                    charAt7 = stringInfo.charAt(i50);
                    if (charAt7 < 55296) {
                        break;
                    }
                    i51 |= (charAt7 & 8191) << i52;
                    i52 += 13;
                    i50 = i8;
                }
                charAt2 = i51 | (charAt7 << i52);
                i50 = i8;
            }
            int i53 = i50 + 1;
            int charAt23 = stringInfo.charAt(i50);
            if (charAt23 >= 55296) {
                int i54 = charAt23 & 8191;
                int i55 = 13;
                while (true) {
                    i7 = i53 + 1;
                    charAt6 = stringInfo.charAt(i53);
                    if (charAt6 < 55296) {
                        break;
                    }
                    i54 |= (charAt6 & 8191) << i55;
                    i55 += 13;
                    i53 = i7;
                }
                charAt23 = i54 | (charAt6 << i55);
                i53 = i7;
            }
            int i56 = i53 + 1;
            int charAt24 = stringInfo.charAt(i53);
            if (charAt24 >= 55296) {
                int i57 = charAt24 & 8191;
                int i58 = 13;
                while (true) {
                    i6 = i56 + 1;
                    charAt5 = stringInfo.charAt(i56);
                    if (charAt5 < 55296) {
                        break;
                    }
                    i57 |= (charAt5 & 8191) << i58;
                    i58 += 13;
                    i56 = i6;
                }
                charAt24 = i57 | (charAt5 << i58);
                i56 = i6;
            }
            int i59 = i56 + 1;
            charAt3 = stringInfo.charAt(i56);
            if (charAt3 >= 55296) {
                int i60 = charAt3 & 8191;
                int i61 = i59;
                int i62 = 13;
                while (true) {
                    i5 = i61 + 1;
                    charAt4 = stringInfo.charAt(i61);
                    if (charAt4 < 55296) {
                        break;
                    }
                    i60 |= (charAt4 & 8191) << i62;
                    i62 += 13;
                    i61 = i5;
                }
                charAt3 = i60 | (charAt4 << i62);
                i59 = i5;
            }
            i2 = (charAt20 * 2) + charAt21;
            i3 = charAt20;
            i35 = i59;
            int i63 = charAt23;
            iArr = new int[charAt3 + charAt23 + charAt24];
            i31 = charAt22;
            i4 = i63;
        }
        Unsafe unsafe = UNSAFE;
        Object[] objects = rawMessageInfo.getObjects();
        Class<?> cls2 = rawMessageInfo.getDefaultInstance().getClass();
        int[] iArr3 = new int[charAt2 * 3];
        Object[] objArr = new Object[charAt2 * 2];
        int i64 = charAt3 + i4;
        int i65 = charAt3;
        int i66 = i64;
        int i67 = 0;
        int i68 = 0;
        while (i35 < length) {
            int i69 = i35 + 1;
            int charAt25 = stringInfo.charAt(i35);
            int i70 = length;
            if (charAt25 >= 55296) {
                int i71 = charAt25 & 8191;
                int i72 = i69;
                int i73 = 13;
                while (true) {
                    i29 = i72 + 1;
                    charAt15 = stringInfo.charAt(i72);
                    i13 = charAt3;
                    if (charAt15 < 55296) {
                        break;
                    }
                    i71 |= (charAt15 & 8191) << i73;
                    i73 += 13;
                    i72 = i29;
                    charAt3 = i13;
                }
                charAt25 = i71 | (charAt15 << i73);
                i14 = i29;
            } else {
                i13 = charAt3;
                i14 = i69;
            }
            int i74 = i14 + 1;
            int charAt26 = stringInfo.charAt(i14);
            if (charAt26 >= 55296) {
                int i75 = charAt26 & 8191;
                int i76 = i74;
                int i77 = 13;
                while (true) {
                    i28 = i76 + 1;
                    charAt14 = stringInfo.charAt(i76);
                    z = z2;
                    if (charAt14 < 55296) {
                        break;
                    }
                    i75 |= (charAt14 & 8191) << i77;
                    i77 += 13;
                    i76 = i28;
                    z2 = z;
                }
                charAt26 = i75 | (charAt14 << i77);
                i15 = i28;
            } else {
                z = z2;
                i15 = i74;
            }
            int i78 = charAt26 & 255;
            int i79 = charAt;
            if ((charAt26 & 1024) != 0) {
                iArr[i67] = i68;
                i67++;
            }
            int i80 = i67;
            if (i78 >= 51) {
                int i81 = i15 + 1;
                int charAt27 = stringInfo.charAt(i15);
                char c = 55296;
                if (charAt27 >= 55296) {
                    int i82 = charAt27 & 8191;
                    int i83 = 13;
                    while (true) {
                        i27 = i81 + 1;
                        charAt13 = stringInfo.charAt(i81);
                        if (charAt13 < c) {
                            break;
                        }
                        i82 |= (charAt13 & 8191) << i83;
                        i83 += 13;
                        i81 = i27;
                        c = 55296;
                    }
                    charAt27 = i82 | (charAt13 << i83);
                    i81 = i27;
                }
                int i84 = i78 - 51;
                int i85 = i81;
                if (i84 == 9 || i84 == 17) {
                    i26 = i2 + 1;
                    objArr[((i68 / 3) * 2) + 1] = objects[i2];
                } else {
                    if (i84 == 12 && (charAt18 & 1) == 1) {
                        i26 = i2 + 1;
                        objArr[((i68 / 3) * 2) + 1] = objects[i2];
                    }
                    int i86 = charAt27 * 2;
                    obj = objects[i86];
                    if (!(obj instanceof Field)) {
                        reflectField2 = (Field) obj;
                    } else {
                        reflectField2 = reflectField(cls2, (String) obj);
                        objects[i86] = reflectField2;
                    }
                    iArr2 = iArr3;
                    i16 = charAt25;
                    int objectFieldOffset = (int) unsafe.objectFieldOffset(reflectField2);
                    int i87 = i86 + 1;
                    obj2 = objects[i87];
                    if (!(obj2 instanceof Field)) {
                        reflectField3 = (Field) obj2;
                    } else {
                        reflectField3 = reflectField(cls2, (String) obj2);
                        objects[i87] = reflectField3;
                    }
                    str = stringInfo;
                    cls = cls2;
                    i18 = charAt18;
                    i21 = (int) unsafe.objectFieldOffset(reflectField3);
                    i23 = objectFieldOffset;
                    i19 = i78;
                    i35 = i85;
                    i22 = 0;
                }
                i2 = i26;
                int i862 = charAt27 * 2;
                obj = objects[i862];
                if (!(obj instanceof Field)) {
                }
                iArr2 = iArr3;
                i16 = charAt25;
                int objectFieldOffset2 = (int) unsafe.objectFieldOffset(reflectField2);
                int i872 = i862 + 1;
                obj2 = objects[i872];
                if (!(obj2 instanceof Field)) {
                }
                str = stringInfo;
                cls = cls2;
                i18 = charAt18;
                i21 = (int) unsafe.objectFieldOffset(reflectField3);
                i23 = objectFieldOffset2;
                i19 = i78;
                i35 = i85;
                i22 = 0;
            } else {
                iArr2 = iArr3;
                i16 = charAt25;
                int i88 = i2 + 1;
                Field reflectField4 = reflectField(cls2, (String) objects[i2]);
                if (i78 == 9 || i78 == 17) {
                    objArr[((i68 / 3) * 2) + 1] = reflectField4.getType();
                } else {
                    if (i78 == 27 || i78 == 49) {
                        i25 = i88 + 1;
                        objArr[((i68 / 3) * 2) + 1] = objects[i88];
                    } else if (i78 == 12 || i78 == 30 || i78 == 44) {
                        if ((charAt18 & 1) == 1) {
                            i25 = i88 + 1;
                            objArr[((i68 / 3) * 2) + 1] = objects[i88];
                        }
                    } else if (i78 == 50) {
                        int i89 = i65 + 1;
                        iArr[i65] = i68;
                        int i90 = (i68 / 3) * 2;
                        int i91 = i88 + 1;
                        objArr[i90] = objects[i88];
                        if ((charAt26 & 2048) != 0) {
                            i88 = i91 + 1;
                            objArr[i90 + 1] = objects[i91];
                            i65 = i89;
                        } else {
                            i65 = i89;
                            i88 = i91;
                        }
                    }
                    i17 = i78;
                    i88 = i25;
                    int objectFieldOffset3 = (int) unsafe.objectFieldOffset(reflectField4);
                    if ((charAt18 & 1) != 1) {
                        i19 = i17;
                        if (i19 <= 17) {
                            i20 = i15 + 1;
                            int charAt28 = stringInfo.charAt(i15);
                            if (charAt28 >= 55296) {
                                int i92 = charAt28 & 8191;
                                int i93 = 13;
                                while (true) {
                                    i24 = i20 + 1;
                                    charAt12 = stringInfo.charAt(i20);
                                    if (charAt12 < 55296) {
                                        break;
                                    }
                                    i92 |= (charAt12 & 8191) << i93;
                                    i93 += 13;
                                    i20 = i24;
                                }
                                charAt28 = i92 | (charAt12 << i93);
                                i20 = i24;
                            }
                            int i94 = (i3 * 2) + (charAt28 / 32);
                            Object obj3 = objects[i94];
                            str = stringInfo;
                            if (obj3 instanceof Field) {
                                reflectField = (Field) obj3;
                            } else {
                                reflectField = reflectField(cls2, (String) obj3);
                                objects[i94] = reflectField;
                            }
                            cls = cls2;
                            i18 = charAt18;
                            i21 = (int) unsafe.objectFieldOffset(reflectField);
                            i22 = charAt28 % 32;
                            if (i19 >= 18 && i19 <= 49) {
                                iArr[i66] = objectFieldOffset3;
                                i66++;
                            }
                            int i95 = i20;
                            i2 = i88;
                            i23 = objectFieldOffset3;
                            i35 = i95;
                        } else {
                            str = stringInfo;
                            cls = cls2;
                            i18 = charAt18;
                        }
                    } else {
                        str = stringInfo;
                        cls = cls2;
                        i18 = charAt18;
                        i19 = i17;
                    }
                    i20 = i15;
                    i21 = 0;
                    i22 = 0;
                    if (i19 >= 18) {
                        iArr[i66] = objectFieldOffset3;
                        i66++;
                    }
                    int i952 = i20;
                    i2 = i88;
                    i23 = objectFieldOffset3;
                    i35 = i952;
                }
                i17 = i78;
                int objectFieldOffset32 = (int) unsafe.objectFieldOffset(reflectField4);
                if ((charAt18 & 1) != 1) {
                }
                i20 = i15;
                i21 = 0;
                i22 = 0;
                if (i19 >= 18) {
                }
                int i9522 = i20;
                i2 = i88;
                i23 = objectFieldOffset32;
                i35 = i9522;
            }
            int i96 = i68 + 1;
            iArr2[i68] = i16;
            int i97 = i96 + 1;
            int i98 = i18;
            iArr2[i96] = ((charAt26 & 512) != 0 ? 536870912 : 0) | ((charAt26 & 256) != 0 ? 268435456 : 0) | (i19 << 20) | i23;
            int i99 = i97 + 1;
            iArr2[i97] = (i22 << 20) | i21;
            iArr3 = iArr2;
            cls2 = cls;
            charAt18 = i98;
            charAt = i79;
            length = i70;
            charAt3 = i13;
            z2 = z;
            i67 = i80;
            i68 = i99;
            stringInfo = str;
        }
        return new MessageSchema<>(iArr3, objArr, i31, charAt, rawMessageInfo.getDefaultInstance(), z2, false, iArr, charAt3, i64, newInstanceSchema, listFieldSchema, unknownFieldSchema, extensionSchema, mapFieldSchema);
    }

    private static Field reflectField(Class<?> cls, String str) {
        Field[] declaredFields;
        try {
            return cls.getDeclaredField(str);
        } catch (NoSuchFieldException unused) {
            for (Field field : cls.getDeclaredFields()) {
                if (str.equals(field.getName())) {
                    return field;
                }
            }
            throw new RuntimeException("Field " + str + " for " + cls.getName() + " not found. Known fields are " + Arrays.toString(declaredFields));
        }
    }

    static <T> MessageSchema<T> newSchemaForMessageInfo(StructuralMessageInfo structuralMessageInfo, NewInstanceSchema newInstanceSchema, ListFieldSchema listFieldSchema, UnknownFieldSchema<?, ?> unknownFieldSchema, ExtensionSchema<?> extensionSchema, MapFieldSchema mapFieldSchema) {
        boolean z = structuralMessageInfo.getSyntax() == ProtoSyntax.PROTO3;
        FieldInfo[] fields = structuralMessageInfo.getFields();
        if (fields.length != 0) {
            FieldInfo fieldInfo = fields[0];
            throw null;
        }
        int length = fields.length;
        int[] iArr = new int[length * 3];
        Object[] objArr = new Object[length * 2];
        if (fields.length > 0) {
            FieldInfo fieldInfo2 = fields[0];
            throw null;
        }
        int[] checkInitialized = structuralMessageInfo.getCheckInitialized();
        if (checkInitialized == null) {
            checkInitialized = EMPTY_INT_ARRAY;
        }
        if (fields.length > 0) {
            FieldInfo fieldInfo3 = fields[0];
            throw null;
        }
        int[] iArr2 = EMPTY_INT_ARRAY;
        int[] iArr3 = EMPTY_INT_ARRAY;
        int[] iArr4 = new int[checkInitialized.length + iArr2.length + iArr3.length];
        System.arraycopy(checkInitialized, 0, iArr4, 0, checkInitialized.length);
        System.arraycopy(iArr2, 0, iArr4, checkInitialized.length, iArr2.length);
        System.arraycopy(iArr3, 0, iArr4, checkInitialized.length + iArr2.length, iArr3.length);
        return new MessageSchema<>(iArr, objArr, 0, 0, structuralMessageInfo.getDefaultInstance(), z, true, iArr4, checkInitialized.length, checkInitialized.length + iArr2.length, newInstanceSchema, listFieldSchema, unknownFieldSchema, extensionSchema, mapFieldSchema);
    }

    @Override // com.google.protobuf.Schema
    public T newInstance() {
        return (T) this.newInstanceSchema.newInstance(this.defaultInstance);
    }

    @Override // com.google.protobuf.Schema
    public boolean equals(T t, T t2) {
        int length = this.buffer.length;
        for (int i = 0; i < length; i += 3) {
            if (!equals(t, t2, i)) {
                return false;
            }
        }
        if (!this.unknownFieldSchema.mo927getFromMessage(t).equals(this.unknownFieldSchema.mo927getFromMessage(t2))) {
            return false;
        }
        if (!this.hasExtensions) {
            return true;
        }
        return this.extensionSchema.getExtensions(t).equals(this.extensionSchema.getExtensions(t2));
    }

    private boolean equals(T t, T t2, int i) {
        int typeAndOffsetAt = typeAndOffsetAt(i);
        long offset = offset(typeAndOffsetAt);
        switch (type(typeAndOffsetAt)) {
            case 0:
                return arePresentForEquals(t, t2, i) && Double.doubleToLongBits(UnsafeUtil.getDouble(t, offset)) == Double.doubleToLongBits(UnsafeUtil.getDouble(t2, offset));
            case 1:
                return arePresentForEquals(t, t2, i) && Float.floatToIntBits(UnsafeUtil.getFloat(t, offset)) == Float.floatToIntBits(UnsafeUtil.getFloat(t2, offset));
            case 2:
                return arePresentForEquals(t, t2, i) && UnsafeUtil.getLong(t, offset) == UnsafeUtil.getLong(t2, offset);
            case 3:
                return arePresentForEquals(t, t2, i) && UnsafeUtil.getLong(t, offset) == UnsafeUtil.getLong(t2, offset);
            case 4:
                return arePresentForEquals(t, t2, i) && UnsafeUtil.getInt(t, offset) == UnsafeUtil.getInt(t2, offset);
            case 5:
                return arePresentForEquals(t, t2, i) && UnsafeUtil.getLong(t, offset) == UnsafeUtil.getLong(t2, offset);
            case 6:
                return arePresentForEquals(t, t2, i) && UnsafeUtil.getInt(t, offset) == UnsafeUtil.getInt(t2, offset);
            case 7:
                return arePresentForEquals(t, t2, i) && UnsafeUtil.getBoolean(t, offset) == UnsafeUtil.getBoolean(t2, offset);
            case 8:
                return arePresentForEquals(t, t2, i) && SchemaUtil.safeEquals(UnsafeUtil.getObject(t, offset), UnsafeUtil.getObject(t2, offset));
            case 9:
                return arePresentForEquals(t, t2, i) && SchemaUtil.safeEquals(UnsafeUtil.getObject(t, offset), UnsafeUtil.getObject(t2, offset));
            case 10:
                return arePresentForEquals(t, t2, i) && SchemaUtil.safeEquals(UnsafeUtil.getObject(t, offset), UnsafeUtil.getObject(t2, offset));
            case 11:
                return arePresentForEquals(t, t2, i) && UnsafeUtil.getInt(t, offset) == UnsafeUtil.getInt(t2, offset);
            case 12:
                return arePresentForEquals(t, t2, i) && UnsafeUtil.getInt(t, offset) == UnsafeUtil.getInt(t2, offset);
            case 13:
                return arePresentForEquals(t, t2, i) && UnsafeUtil.getInt(t, offset) == UnsafeUtil.getInt(t2, offset);
            case 14:
                return arePresentForEquals(t, t2, i) && UnsafeUtil.getLong(t, offset) == UnsafeUtil.getLong(t2, offset);
            case 15:
                return arePresentForEquals(t, t2, i) && UnsafeUtil.getInt(t, offset) == UnsafeUtil.getInt(t2, offset);
            case 16:
                return arePresentForEquals(t, t2, i) && UnsafeUtil.getLong(t, offset) == UnsafeUtil.getLong(t2, offset);
            case 17:
                return arePresentForEquals(t, t2, i) && SchemaUtil.safeEquals(UnsafeUtil.getObject(t, offset), UnsafeUtil.getObject(t2, offset));
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
                return SchemaUtil.safeEquals(UnsafeUtil.getObject(t, offset), UnsafeUtil.getObject(t2, offset));
            case 50:
                return SchemaUtil.safeEquals(UnsafeUtil.getObject(t, offset), UnsafeUtil.getObject(t2, offset));
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
            case 60:
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
            case 68:
                return isOneofCaseEqual(t, t2, i) && SchemaUtil.safeEquals(UnsafeUtil.getObject(t, offset), UnsafeUtil.getObject(t2, offset));
            default:
                return true;
        }
    }

    @Override // com.google.protobuf.Schema
    public int hashCode(T t) {
        int i;
        int hashLong;
        int length = this.buffer.length;
        int i2 = 0;
        for (int i3 = 0; i3 < length; i3 += 3) {
            int typeAndOffsetAt = typeAndOffsetAt(i3);
            int numberAt = numberAt(i3);
            long offset = offset(typeAndOffsetAt);
            int i4 = 37;
            switch (type(typeAndOffsetAt)) {
                case 0:
                    i = i2 * 53;
                    hashLong = Internal.hashLong(Double.doubleToLongBits(UnsafeUtil.getDouble(t, offset)));
                    i2 = i + hashLong;
                    break;
                case 1:
                    i = i2 * 53;
                    hashLong = Float.floatToIntBits(UnsafeUtil.getFloat(t, offset));
                    i2 = i + hashLong;
                    break;
                case 2:
                    i = i2 * 53;
                    hashLong = Internal.hashLong(UnsafeUtil.getLong(t, offset));
                    i2 = i + hashLong;
                    break;
                case 3:
                    i = i2 * 53;
                    hashLong = Internal.hashLong(UnsafeUtil.getLong(t, offset));
                    i2 = i + hashLong;
                    break;
                case 4:
                    i = i2 * 53;
                    hashLong = UnsafeUtil.getInt(t, offset);
                    i2 = i + hashLong;
                    break;
                case 5:
                    i = i2 * 53;
                    hashLong = Internal.hashLong(UnsafeUtil.getLong(t, offset));
                    i2 = i + hashLong;
                    break;
                case 6:
                    i = i2 * 53;
                    hashLong = UnsafeUtil.getInt(t, offset);
                    i2 = i + hashLong;
                    break;
                case 7:
                    i = i2 * 53;
                    hashLong = Internal.hashBoolean(UnsafeUtil.getBoolean(t, offset));
                    i2 = i + hashLong;
                    break;
                case 8:
                    i = i2 * 53;
                    hashLong = ((String) UnsafeUtil.getObject(t, offset)).hashCode();
                    i2 = i + hashLong;
                    break;
                case 9:
                    Object object = UnsafeUtil.getObject(t, offset);
                    if (object != null) {
                        i4 = object.hashCode();
                    }
                    i2 = (i2 * 53) + i4;
                    break;
                case 10:
                    i = i2 * 53;
                    hashLong = UnsafeUtil.getObject(t, offset).hashCode();
                    i2 = i + hashLong;
                    break;
                case 11:
                    i = i2 * 53;
                    hashLong = UnsafeUtil.getInt(t, offset);
                    i2 = i + hashLong;
                    break;
                case 12:
                    i = i2 * 53;
                    hashLong = UnsafeUtil.getInt(t, offset);
                    i2 = i + hashLong;
                    break;
                case 13:
                    i = i2 * 53;
                    hashLong = UnsafeUtil.getInt(t, offset);
                    i2 = i + hashLong;
                    break;
                case 14:
                    i = i2 * 53;
                    hashLong = Internal.hashLong(UnsafeUtil.getLong(t, offset));
                    i2 = i + hashLong;
                    break;
                case 15:
                    i = i2 * 53;
                    hashLong = UnsafeUtil.getInt(t, offset);
                    i2 = i + hashLong;
                    break;
                case 16:
                    i = i2 * 53;
                    hashLong = Internal.hashLong(UnsafeUtil.getLong(t, offset));
                    i2 = i + hashLong;
                    break;
                case 17:
                    Object object2 = UnsafeUtil.getObject(t, offset);
                    if (object2 != null) {
                        i4 = object2.hashCode();
                    }
                    i2 = (i2 * 53) + i4;
                    break;
                case 18:
                case 19:
                case 20:
                case 21:
                case 22:
                case 23:
                case 24:
                case 25:
                case 26:
                case 27:
                case 28:
                case 29:
                case 30:
                case 31:
                case 32:
                case 33:
                case 34:
                case 35:
                case 36:
                case 37:
                case 38:
                case 39:
                case 40:
                case 41:
                case 42:
                case 43:
                case 44:
                case 45:
                case 46:
                case 47:
                case 48:
                case 49:
                    i = i2 * 53;
                    hashLong = UnsafeUtil.getObject(t, offset).hashCode();
                    i2 = i + hashLong;
                    break;
                case 50:
                    i = i2 * 53;
                    hashLong = UnsafeUtil.getObject(t, offset).hashCode();
                    i2 = i + hashLong;
                    break;
                case 51:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = Internal.hashLong(Double.doubleToLongBits(oneofDoubleAt(t, offset)));
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 52:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = Float.floatToIntBits(oneofFloatAt(t, offset));
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 53:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = Internal.hashLong(oneofLongAt(t, offset));
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 54:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = Internal.hashLong(oneofLongAt(t, offset));
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 55:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = oneofIntAt(t, offset);
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 56:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = Internal.hashLong(oneofLongAt(t, offset));
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 57:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = oneofIntAt(t, offset);
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 58:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = Internal.hashBoolean(oneofBooleanAt(t, offset));
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 59:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = ((String) UnsafeUtil.getObject(t, offset)).hashCode();
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 60:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = UnsafeUtil.getObject(t, offset).hashCode();
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 61:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = UnsafeUtil.getObject(t, offset).hashCode();
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 62:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = oneofIntAt(t, offset);
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 63:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = oneofIntAt(t, offset);
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 64:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = oneofIntAt(t, offset);
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 65:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = Internal.hashLong(oneofLongAt(t, offset));
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 66:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = oneofIntAt(t, offset);
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 67:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = Internal.hashLong(oneofLongAt(t, offset));
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
                case 68:
                    if (isOneofPresent(t, numberAt, i3)) {
                        i = i2 * 53;
                        hashLong = UnsafeUtil.getObject(t, offset).hashCode();
                        i2 = i + hashLong;
                        break;
                    } else {
                        break;
                    }
            }
        }
        int hashCode = (i2 * 53) + this.unknownFieldSchema.mo927getFromMessage(t).hashCode();
        return this.hasExtensions ? (hashCode * 53) + this.extensionSchema.getExtensions(t).hashCode() : hashCode;
    }

    @Override // com.google.protobuf.Schema
    public void mergeFrom(T t, T t2) {
        Objects.requireNonNull(t2);
        for (int i = 0; i < this.buffer.length; i += 3) {
            mergeSingleField(t, t2, i);
        }
        if (!this.proto3) {
            SchemaUtil.mergeUnknownFields(this.unknownFieldSchema, t, t2);
            if (!this.hasExtensions) {
                return;
            }
            SchemaUtil.mergeExtensions(this.extensionSchema, t, t2);
        }
    }

    private void mergeSingleField(T t, T t2, int i) {
        int typeAndOffsetAt = typeAndOffsetAt(i);
        long offset = offset(typeAndOffsetAt);
        int numberAt = numberAt(i);
        switch (type(typeAndOffsetAt)) {
            case 0:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putDouble(t, offset, UnsafeUtil.getDouble(t2, offset));
                setFieldPresent(t, i);
                return;
            case 1:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putFloat(t, offset, UnsafeUtil.getFloat(t2, offset));
                setFieldPresent(t, i);
                return;
            case 2:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putLong(t, offset, UnsafeUtil.getLong(t2, offset));
                setFieldPresent(t, i);
                return;
            case 3:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putLong(t, offset, UnsafeUtil.getLong(t2, offset));
                setFieldPresent(t, i);
                return;
            case 4:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putInt(t, offset, UnsafeUtil.getInt(t2, offset));
                setFieldPresent(t, i);
                return;
            case 5:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putLong(t, offset, UnsafeUtil.getLong(t2, offset));
                setFieldPresent(t, i);
                return;
            case 6:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putInt(t, offset, UnsafeUtil.getInt(t2, offset));
                setFieldPresent(t, i);
                return;
            case 7:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putBoolean(t, offset, UnsafeUtil.getBoolean(t2, offset));
                setFieldPresent(t, i);
                return;
            case 8:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putObject(t, offset, UnsafeUtil.getObject(t2, offset));
                setFieldPresent(t, i);
                return;
            case 9:
                mergeMessage(t, t2, i);
                return;
            case 10:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putObject(t, offset, UnsafeUtil.getObject(t2, offset));
                setFieldPresent(t, i);
                return;
            case 11:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putInt(t, offset, UnsafeUtil.getInt(t2, offset));
                setFieldPresent(t, i);
                return;
            case 12:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putInt(t, offset, UnsafeUtil.getInt(t2, offset));
                setFieldPresent(t, i);
                return;
            case 13:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putInt(t, offset, UnsafeUtil.getInt(t2, offset));
                setFieldPresent(t, i);
                return;
            case 14:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putLong(t, offset, UnsafeUtil.getLong(t2, offset));
                setFieldPresent(t, i);
                return;
            case 15:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putInt(t, offset, UnsafeUtil.getInt(t2, offset));
                setFieldPresent(t, i);
                return;
            case 16:
                if (!isFieldPresent(t2, i)) {
                    return;
                }
                UnsafeUtil.putLong(t, offset, UnsafeUtil.getLong(t2, offset));
                setFieldPresent(t, i);
                return;
            case 17:
                mergeMessage(t, t2, i);
                return;
            case 18:
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
            case 24:
            case 25:
            case 26:
            case 27:
            case 28:
            case 29:
            case 30:
            case 31:
            case 32:
            case 33:
            case 34:
            case 35:
            case 36:
            case 37:
            case 38:
            case 39:
            case 40:
            case 41:
            case 42:
            case 43:
            case 44:
            case 45:
            case 46:
            case 47:
            case 48:
            case 49:
                this.listFieldSchema.mergeListsAt(t, t2, offset);
                return;
            case 50:
                SchemaUtil.mergeMap(this.mapFieldSchema, t, t2, offset);
                return;
            case 51:
            case 52:
            case 53:
            case 54:
            case 55:
            case 56:
            case 57:
            case 58:
            case 59:
                if (!isOneofPresent(t2, numberAt, i)) {
                    return;
                }
                UnsafeUtil.putObject(t, offset, UnsafeUtil.getObject(t2, offset));
                setOneofPresent(t, numberAt, i);
                return;
            case 60:
                mergeOneofMessage(t, t2, i);
                return;
            case 61:
            case 62:
            case 63:
            case 64:
            case 65:
            case 66:
            case 67:
                if (!isOneofPresent(t2, numberAt, i)) {
                    return;
                }
                UnsafeUtil.putObject(t, offset, UnsafeUtil.getObject(t2, offset));
                setOneofPresent(t, numberAt, i);
                return;
            case 68:
                mergeOneofMessage(t, t2, i);
                return;
            default:
                return;
        }
    }

    private void mergeMessage(T t, T t2, int i) {
        long offset = offset(typeAndOffsetAt(i));
        if (!isFieldPresent(t2, i)) {
            return;
        }
        Object object = UnsafeUtil.getObject(t, offset);
        Object object2 = UnsafeUtil.getObject(t2, offset);
        if (object != null && object2 != null) {
            UnsafeUtil.putObject(t, offset, Internal.mergeMessage(object, object2));
            setFieldPresent(t, i);
        } else if (object2 == null) {
        } else {
            UnsafeUtil.putObject(t, offset, object2);
            setFieldPresent(t, i);
        }
    }

    private void mergeOneofMessage(T t, T t2, int i) {
        int typeAndOffsetAt = typeAndOffsetAt(i);
        int numberAt = numberAt(i);
        long offset = offset(typeAndOffsetAt);
        if (!isOneofPresent(t2, numberAt, i)) {
            return;
        }
        Object object = UnsafeUtil.getObject(t, offset);
        Object object2 = UnsafeUtil.getObject(t2, offset);
        if (object != null && object2 != null) {
            UnsafeUtil.putObject(t, offset, Internal.mergeMessage(object, object2));
            setOneofPresent(t, numberAt, i);
        } else if (object2 == null) {
        } else {
            UnsafeUtil.putObject(t, offset, object2);
            setOneofPresent(t, numberAt, i);
        }
    }

    @Override // com.google.protobuf.Schema
    public int getSerializedSize(T t) {
        return this.proto3 ? getSerializedSizeProto3(t) : getSerializedSizeProto2(t);
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    private int getSerializedSizeProto2(T t) {
        int i;
        int i2;
        int computeDoubleSize;
        int computeBoolSize;
        int computeSFixed32Size;
        boolean z;
        int computeSizeFixed32List;
        int computeSizeFixed64ListNoTag;
        int computeTagSize;
        int computeUInt32SizeNoTag;
        Unsafe unsafe = UNSAFE;
        int i3 = -1;
        int i4 = 0;
        int i5 = 0;
        int i6 = 0;
        while (i4 < this.buffer.length) {
            int typeAndOffsetAt = typeAndOffsetAt(i4);
            int numberAt = numberAt(i4);
            int type = type(typeAndOffsetAt);
            if (type <= 17) {
                i = this.buffer[i4 + 2];
                int i7 = 1048575 & i;
                int i8 = 1 << (i >>> 20);
                if (i7 != i3) {
                    i6 = unsafe.getInt(t, i7);
                    i3 = i7;
                }
                i2 = i8;
            } else {
                i = (!this.useCachedSizeField || type < FieldType.DOUBLE_LIST_PACKED.id() || type > FieldType.SINT64_LIST_PACKED.id()) ? 0 : this.buffer[i4 + 2] & 1048575;
                i2 = 0;
            }
            long offset = offset(typeAndOffsetAt);
            int i9 = i3;
            switch (type) {
                case 0:
                    if ((i6 & i2) == 0) {
                        break;
                    } else {
                        computeDoubleSize = CodedOutputStream.computeDoubleSize(numberAt, 0.0d);
                        i5 += computeDoubleSize;
                        break;
                    }
                case 1:
                    if ((i6 & i2) == 0) {
                        break;
                    } else {
                        computeDoubleSize = CodedOutputStream.computeFloatSize(numberAt, 0.0f);
                        i5 += computeDoubleSize;
                        break;
                    }
                case 2:
                    if ((i6 & i2) == 0) {
                        break;
                    } else {
                        computeDoubleSize = CodedOutputStream.computeInt64Size(numberAt, unsafe.getLong(t, offset));
                        i5 += computeDoubleSize;
                        break;
                    }
                case 3:
                    if ((i6 & i2) == 0) {
                        break;
                    } else {
                        computeDoubleSize = CodedOutputStream.computeUInt64Size(numberAt, unsafe.getLong(t, offset));
                        i5 += computeDoubleSize;
                        break;
                    }
                case 4:
                    if ((i6 & i2) == 0) {
                        break;
                    } else {
                        computeDoubleSize = CodedOutputStream.computeInt32Size(numberAt, unsafe.getInt(t, offset));
                        i5 += computeDoubleSize;
                        break;
                    }
                case 5:
                    if ((i6 & i2) == 0) {
                        break;
                    } else {
                        computeDoubleSize = CodedOutputStream.computeFixed64Size(numberAt, 0L);
                        i5 += computeDoubleSize;
                        break;
                    }
                case 6:
                    if ((i6 & i2) != 0) {
                        computeDoubleSize = CodedOutputStream.computeFixed32Size(numberAt, 0);
                        i5 += computeDoubleSize;
                        break;
                    }
                    break;
                case 7:
                    if ((i6 & i2) != 0) {
                        computeBoolSize = CodedOutputStream.computeBoolSize(numberAt, true);
                        i5 += computeBoolSize;
                    }
                    break;
                case 8:
                    if ((i6 & i2) != 0) {
                        Object object = unsafe.getObject(t, offset);
                        if (object instanceof ByteString) {
                            computeBoolSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) object);
                        } else {
                            computeBoolSize = CodedOutputStream.computeStringSize(numberAt, (String) object);
                        }
                        i5 += computeBoolSize;
                    }
                    break;
                case 9:
                    if ((i6 & i2) != 0) {
                        computeBoolSize = SchemaUtil.computeSizeMessage(numberAt, unsafe.getObject(t, offset), getMessageFieldSchema(i4));
                        i5 += computeBoolSize;
                    }
                    break;
                case 10:
                    if ((i6 & i2) != 0) {
                        computeBoolSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) unsafe.getObject(t, offset));
                        i5 += computeBoolSize;
                    }
                    break;
                case 11:
                    if ((i6 & i2) != 0) {
                        computeBoolSize = CodedOutputStream.computeUInt32Size(numberAt, unsafe.getInt(t, offset));
                        i5 += computeBoolSize;
                    }
                    break;
                case 12:
                    if ((i6 & i2) != 0) {
                        computeBoolSize = CodedOutputStream.computeEnumSize(numberAt, unsafe.getInt(t, offset));
                        i5 += computeBoolSize;
                    }
                    break;
                case 13:
                    if ((i6 & i2) != 0) {
                        computeSFixed32Size = CodedOutputStream.computeSFixed32Size(numberAt, 0);
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 14:
                    if ((i6 & i2) != 0) {
                        computeBoolSize = CodedOutputStream.computeSFixed64Size(numberAt, 0L);
                        i5 += computeBoolSize;
                    }
                    break;
                case 15:
                    if ((i6 & i2) != 0) {
                        computeBoolSize = CodedOutputStream.computeSInt32Size(numberAt, unsafe.getInt(t, offset));
                        i5 += computeBoolSize;
                    }
                    break;
                case 16:
                    if ((i6 & i2) != 0) {
                        computeBoolSize = CodedOutputStream.computeSInt64Size(numberAt, unsafe.getLong(t, offset));
                        i5 += computeBoolSize;
                    }
                    break;
                case 17:
                    if ((i6 & i2) != 0) {
                        computeBoolSize = CodedOutputStream.computeGroupSize(numberAt, (MessageLite) unsafe.getObject(t, offset), getMessageFieldSchema(i4));
                        i5 += computeBoolSize;
                    }
                    break;
                case 18:
                    computeBoolSize = SchemaUtil.computeSizeFixed64List(numberAt, (List) unsafe.getObject(t, offset), false);
                    i5 += computeBoolSize;
                    break;
                case 19:
                    z = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeFixed32List(numberAt, (List) unsafe.getObject(t, offset), false);
                    i5 += computeSizeFixed32List;
                    break;
                case 20:
                    z = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeInt64List(numberAt, (List) unsafe.getObject(t, offset), false);
                    i5 += computeSizeFixed32List;
                    break;
                case 21:
                    z = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeUInt64List(numberAt, (List) unsafe.getObject(t, offset), false);
                    i5 += computeSizeFixed32List;
                    break;
                case 22:
                    z = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeInt32List(numberAt, (List) unsafe.getObject(t, offset), false);
                    i5 += computeSizeFixed32List;
                    break;
                case 23:
                    z = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeFixed64List(numberAt, (List) unsafe.getObject(t, offset), false);
                    i5 += computeSizeFixed32List;
                    break;
                case 24:
                    z = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeFixed32List(numberAt, (List) unsafe.getObject(t, offset), false);
                    i5 += computeSizeFixed32List;
                    break;
                case 25:
                    z = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeBoolList(numberAt, (List) unsafe.getObject(t, offset), false);
                    i5 += computeSizeFixed32List;
                    break;
                case 26:
                    computeBoolSize = SchemaUtil.computeSizeStringList(numberAt, (List) unsafe.getObject(t, offset));
                    i5 += computeBoolSize;
                    break;
                case 27:
                    computeBoolSize = SchemaUtil.computeSizeMessageList(numberAt, (List) unsafe.getObject(t, offset), getMessageFieldSchema(i4));
                    i5 += computeBoolSize;
                    break;
                case 28:
                    computeBoolSize = SchemaUtil.computeSizeByteStringList(numberAt, (List) unsafe.getObject(t, offset));
                    i5 += computeBoolSize;
                    break;
                case 29:
                    computeBoolSize = SchemaUtil.computeSizeUInt32List(numberAt, (List) unsafe.getObject(t, offset), false);
                    i5 += computeBoolSize;
                    break;
                case 30:
                    z = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeEnumList(numberAt, (List) unsafe.getObject(t, offset), false);
                    i5 += computeSizeFixed32List;
                    break;
                case 31:
                    z = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeFixed32List(numberAt, (List) unsafe.getObject(t, offset), false);
                    i5 += computeSizeFixed32List;
                    break;
                case 32:
                    z = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeFixed64List(numberAt, (List) unsafe.getObject(t, offset), false);
                    i5 += computeSizeFixed32List;
                    break;
                case 33:
                    z = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeSInt32List(numberAt, (List) unsafe.getObject(t, offset), false);
                    i5 += computeSizeFixed32List;
                    break;
                case 34:
                    z = false;
                    computeSizeFixed32List = SchemaUtil.computeSizeSInt64List(numberAt, (List) unsafe.getObject(t, offset), false);
                    i5 += computeSizeFixed32List;
                    break;
                case 35:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 36:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 37:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeInt64ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 38:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeUInt64ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 39:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeInt32ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 40:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 41:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 42:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeBoolListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 43:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeUInt32ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 44:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeEnumListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 45:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 46:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 47:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeSInt32ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 48:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeSInt64ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeSFixed32Size = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 49:
                    computeBoolSize = SchemaUtil.computeSizeGroupList(numberAt, (List) unsafe.getObject(t, offset), getMessageFieldSchema(i4));
                    i5 += computeBoolSize;
                    break;
                case 50:
                    computeBoolSize = this.mapFieldSchema.getSerializedSize(numberAt, unsafe.getObject(t, offset), getMapFieldDefaultEntry(i4));
                    i5 += computeBoolSize;
                    break;
                case 51:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = CodedOutputStream.computeDoubleSize(numberAt, 0.0d);
                        i5 += computeBoolSize;
                    }
                    break;
                case 52:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = CodedOutputStream.computeFloatSize(numberAt, 0.0f);
                        i5 += computeBoolSize;
                    }
                    break;
                case 53:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = CodedOutputStream.computeInt64Size(numberAt, oneofLongAt(t, offset));
                        i5 += computeBoolSize;
                    }
                    break;
                case 54:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = CodedOutputStream.computeUInt64Size(numberAt, oneofLongAt(t, offset));
                        i5 += computeBoolSize;
                    }
                    break;
                case 55:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = CodedOutputStream.computeInt32Size(numberAt, oneofIntAt(t, offset));
                        i5 += computeBoolSize;
                    }
                    break;
                case 56:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = CodedOutputStream.computeFixed64Size(numberAt, 0L);
                        i5 += computeBoolSize;
                    }
                    break;
                case 57:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeSFixed32Size = CodedOutputStream.computeFixed32Size(numberAt, 0);
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 58:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = CodedOutputStream.computeBoolSize(numberAt, true);
                        i5 += computeBoolSize;
                    }
                    break;
                case 59:
                    if (isOneofPresent(t, numberAt, i4)) {
                        Object object2 = unsafe.getObject(t, offset);
                        if (object2 instanceof ByteString) {
                            computeBoolSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) object2);
                        } else {
                            computeBoolSize = CodedOutputStream.computeStringSize(numberAt, (String) object2);
                        }
                        i5 += computeBoolSize;
                    }
                    break;
                case 60:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = SchemaUtil.computeSizeMessage(numberAt, unsafe.getObject(t, offset), getMessageFieldSchema(i4));
                        i5 += computeBoolSize;
                    }
                    break;
                case 61:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) unsafe.getObject(t, offset));
                        i5 += computeBoolSize;
                    }
                    break;
                case 62:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = CodedOutputStream.computeUInt32Size(numberAt, oneofIntAt(t, offset));
                        i5 += computeBoolSize;
                    }
                    break;
                case 63:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = CodedOutputStream.computeEnumSize(numberAt, oneofIntAt(t, offset));
                        i5 += computeBoolSize;
                    }
                    break;
                case 64:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeSFixed32Size = CodedOutputStream.computeSFixed32Size(numberAt, 0);
                        i5 += computeSFixed32Size;
                    }
                    break;
                case 65:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = CodedOutputStream.computeSFixed64Size(numberAt, 0L);
                        i5 += computeBoolSize;
                    }
                    break;
                case 66:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = CodedOutputStream.computeSInt32Size(numberAt, oneofIntAt(t, offset));
                        i5 += computeBoolSize;
                    }
                    break;
                case 67:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = CodedOutputStream.computeSInt64Size(numberAt, oneofLongAt(t, offset));
                        i5 += computeBoolSize;
                    }
                    break;
                case 68:
                    if (isOneofPresent(t, numberAt, i4)) {
                        computeBoolSize = CodedOutputStream.computeGroupSize(numberAt, (MessageLite) unsafe.getObject(t, offset), getMessageFieldSchema(i4));
                        i5 += computeBoolSize;
                    }
                    break;
            }
            i4 += 3;
            i3 = i9;
        }
        int unknownFieldsSerializedSize = i5 + getUnknownFieldsSerializedSize(this.unknownFieldSchema, t);
        return this.hasExtensions ? unknownFieldsSerializedSize + this.extensionSchema.getExtensions(t).getSerializedSize() : unknownFieldsSerializedSize;
    }

    private int getSerializedSizeProto3(T t) {
        int computeDoubleSize;
        int computeSizeFixed64ListNoTag;
        int computeTagSize;
        int computeUInt32SizeNoTag;
        Unsafe unsafe = UNSAFE;
        int i = 0;
        for (int i2 = 0; i2 < this.buffer.length; i2 += 3) {
            int typeAndOffsetAt = typeAndOffsetAt(i2);
            int type = type(typeAndOffsetAt);
            int numberAt = numberAt(i2);
            long offset = offset(typeAndOffsetAt);
            int i3 = (type < FieldType.DOUBLE_LIST_PACKED.id() || type > FieldType.SINT64_LIST_PACKED.id()) ? 0 : this.buffer[i2 + 2] & 1048575;
            switch (type) {
                case 0:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeDoubleSize(numberAt, 0.0d);
                        break;
                    } else {
                        continue;
                    }
                case 1:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeFloatSize(numberAt, 0.0f);
                        break;
                    } else {
                        continue;
                    }
                case 2:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeInt64Size(numberAt, UnsafeUtil.getLong(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 3:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeUInt64Size(numberAt, UnsafeUtil.getLong(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 4:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeInt32Size(numberAt, UnsafeUtil.getInt(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 5:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeFixed64Size(numberAt, 0L);
                        break;
                    } else {
                        continue;
                    }
                case 6:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeFixed32Size(numberAt, 0);
                        break;
                    } else {
                        continue;
                    }
                case 7:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeBoolSize(numberAt, true);
                        break;
                    } else {
                        continue;
                    }
                case 8:
                    if (isFieldPresent(t, i2)) {
                        Object object = UnsafeUtil.getObject(t, offset);
                        if (object instanceof ByteString) {
                            computeDoubleSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) object);
                            break;
                        } else {
                            computeDoubleSize = CodedOutputStream.computeStringSize(numberAt, (String) object);
                            break;
                        }
                    } else {
                        continue;
                    }
                case 9:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = SchemaUtil.computeSizeMessage(numberAt, UnsafeUtil.getObject(t, offset), getMessageFieldSchema(i2));
                        break;
                    } else {
                        continue;
                    }
                case 10:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) UnsafeUtil.getObject(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 11:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeUInt32Size(numberAt, UnsafeUtil.getInt(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 12:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeEnumSize(numberAt, UnsafeUtil.getInt(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 13:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeSFixed32Size(numberAt, 0);
                        break;
                    } else {
                        continue;
                    }
                case 14:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeSFixed64Size(numberAt, 0L);
                        break;
                    } else {
                        continue;
                    }
                case 15:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeSInt32Size(numberAt, UnsafeUtil.getInt(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 16:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeSInt64Size(numberAt, UnsafeUtil.getLong(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 17:
                    if (isFieldPresent(t, i2)) {
                        computeDoubleSize = CodedOutputStream.computeGroupSize(numberAt, (MessageLite) UnsafeUtil.getObject(t, offset), getMessageFieldSchema(i2));
                        break;
                    } else {
                        continue;
                    }
                case 18:
                    computeDoubleSize = SchemaUtil.computeSizeFixed64List(numberAt, listAt(t, offset), false);
                    break;
                case 19:
                    computeDoubleSize = SchemaUtil.computeSizeFixed32List(numberAt, listAt(t, offset), false);
                    break;
                case 20:
                    computeDoubleSize = SchemaUtil.computeSizeInt64List(numberAt, listAt(t, offset), false);
                    break;
                case 21:
                    computeDoubleSize = SchemaUtil.computeSizeUInt64List(numberAt, listAt(t, offset), false);
                    break;
                case 22:
                    computeDoubleSize = SchemaUtil.computeSizeInt32List(numberAt, listAt(t, offset), false);
                    break;
                case 23:
                    computeDoubleSize = SchemaUtil.computeSizeFixed64List(numberAt, listAt(t, offset), false);
                    break;
                case 24:
                    computeDoubleSize = SchemaUtil.computeSizeFixed32List(numberAt, listAt(t, offset), false);
                    break;
                case 25:
                    computeDoubleSize = SchemaUtil.computeSizeBoolList(numberAt, listAt(t, offset), false);
                    break;
                case 26:
                    computeDoubleSize = SchemaUtil.computeSizeStringList(numberAt, listAt(t, offset));
                    break;
                case 27:
                    computeDoubleSize = SchemaUtil.computeSizeMessageList(numberAt, listAt(t, offset), getMessageFieldSchema(i2));
                    break;
                case 28:
                    computeDoubleSize = SchemaUtil.computeSizeByteStringList(numberAt, listAt(t, offset));
                    break;
                case 29:
                    computeDoubleSize = SchemaUtil.computeSizeUInt32List(numberAt, listAt(t, offset), false);
                    break;
                case 30:
                    computeDoubleSize = SchemaUtil.computeSizeEnumList(numberAt, listAt(t, offset), false);
                    break;
                case 31:
                    computeDoubleSize = SchemaUtil.computeSizeFixed32List(numberAt, listAt(t, offset), false);
                    break;
                case 32:
                    computeDoubleSize = SchemaUtil.computeSizeFixed64List(numberAt, listAt(t, offset), false);
                    break;
                case 33:
                    computeDoubleSize = SchemaUtil.computeSizeSInt32List(numberAt, listAt(t, offset), false);
                    break;
                case 34:
                    computeDoubleSize = SchemaUtil.computeSizeSInt64List(numberAt, listAt(t, offset), false);
                    break;
                case 35:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i3, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        break;
                    } else {
                        continue;
                    }
                case 36:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i3, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        break;
                    } else {
                        continue;
                    }
                case 37:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeInt64ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i3, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        break;
                    } else {
                        continue;
                    }
                case 38:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeUInt64ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i3, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        break;
                    } else {
                        continue;
                    }
                case 39:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeInt32ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i3, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        break;
                    } else {
                        continue;
                    }
                case 40:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i3, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        break;
                    } else {
                        continue;
                    }
                case 41:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i3, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        break;
                    } else {
                        continue;
                    }
                case 42:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeBoolListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i3, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        break;
                    } else {
                        continue;
                    }
                case 43:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeUInt32ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i3, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        break;
                    } else {
                        continue;
                    }
                case 44:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeEnumListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i3, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        break;
                    } else {
                        continue;
                    }
                case 45:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed32ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i3, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        break;
                    } else {
                        continue;
                    }
                case 46:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeFixed64ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i3, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        break;
                    } else {
                        continue;
                    }
                case 47:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeSInt32ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i3, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        break;
                    } else {
                        continue;
                    }
                case 48:
                    computeSizeFixed64ListNoTag = SchemaUtil.computeSizeSInt64ListNoTag((List) unsafe.getObject(t, offset));
                    if (computeSizeFixed64ListNoTag > 0) {
                        if (this.useCachedSizeField) {
                            unsafe.putInt(t, i3, computeSizeFixed64ListNoTag);
                        }
                        computeTagSize = CodedOutputStream.computeTagSize(numberAt);
                        computeUInt32SizeNoTag = CodedOutputStream.computeUInt32SizeNoTag(computeSizeFixed64ListNoTag);
                        computeDoubleSize = computeTagSize + computeUInt32SizeNoTag + computeSizeFixed64ListNoTag;
                        break;
                    } else {
                        continue;
                    }
                case 49:
                    computeDoubleSize = SchemaUtil.computeSizeGroupList(numberAt, listAt(t, offset), getMessageFieldSchema(i2));
                    break;
                case 50:
                    computeDoubleSize = this.mapFieldSchema.getSerializedSize(numberAt, UnsafeUtil.getObject(t, offset), getMapFieldDefaultEntry(i2));
                    break;
                case 51:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeDoubleSize(numberAt, 0.0d);
                        break;
                    } else {
                        continue;
                    }
                case 52:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeFloatSize(numberAt, 0.0f);
                        break;
                    } else {
                        continue;
                    }
                case 53:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeInt64Size(numberAt, oneofLongAt(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 54:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeUInt64Size(numberAt, oneofLongAt(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 55:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeInt32Size(numberAt, oneofIntAt(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 56:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeFixed64Size(numberAt, 0L);
                        break;
                    } else {
                        continue;
                    }
                case 57:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeFixed32Size(numberAt, 0);
                        break;
                    } else {
                        continue;
                    }
                case 58:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeBoolSize(numberAt, true);
                        break;
                    } else {
                        continue;
                    }
                case 59:
                    if (isOneofPresent(t, numberAt, i2)) {
                        Object object2 = UnsafeUtil.getObject(t, offset);
                        if (object2 instanceof ByteString) {
                            computeDoubleSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) object2);
                            break;
                        } else {
                            computeDoubleSize = CodedOutputStream.computeStringSize(numberAt, (String) object2);
                            break;
                        }
                    } else {
                        continue;
                    }
                case 60:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = SchemaUtil.computeSizeMessage(numberAt, UnsafeUtil.getObject(t, offset), getMessageFieldSchema(i2));
                        break;
                    } else {
                        continue;
                    }
                case 61:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeBytesSize(numberAt, (ByteString) UnsafeUtil.getObject(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 62:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeUInt32Size(numberAt, oneofIntAt(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 63:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeEnumSize(numberAt, oneofIntAt(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 64:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeSFixed32Size(numberAt, 0);
                        break;
                    } else {
                        continue;
                    }
                case 65:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeSFixed64Size(numberAt, 0L);
                        break;
                    } else {
                        continue;
                    }
                case 66:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeSInt32Size(numberAt, oneofIntAt(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 67:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeSInt64Size(numberAt, oneofLongAt(t, offset));
                        break;
                    } else {
                        continue;
                    }
                case 68:
                    if (isOneofPresent(t, numberAt, i2)) {
                        computeDoubleSize = CodedOutputStream.computeGroupSize(numberAt, (MessageLite) UnsafeUtil.getObject(t, offset), getMessageFieldSchema(i2));
                        break;
                    } else {
                        continue;
                    }
                default:
            }
            i += computeDoubleSize;
        }
        return i + getUnknownFieldsSerializedSize(this.unknownFieldSchema, t);
    }

    private <UT, UB> int getUnknownFieldsSerializedSize(UnknownFieldSchema<UT, UB> unknownFieldSchema, T t) {
        return unknownFieldSchema.getSerializedSize(unknownFieldSchema.mo927getFromMessage(t));
    }

    private static List<?> listAt(Object obj, long j) {
        return (List) UnsafeUtil.getObject(obj, j);
    }

    @Override // com.google.protobuf.Schema
    public void writeTo(T t, Writer writer) throws IOException {
        if (writer.fieldOrder() == Writer.FieldOrder.DESCENDING) {
            writeFieldsInDescendingOrder(t, writer);
        } else if (this.proto3) {
            writeFieldsInAscendingOrderProto3(t, writer);
        } else {
            writeFieldsInAscendingOrderProto2(t, writer);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:231:0x049e  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x002d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void writeFieldsInAscendingOrderProto2(T t, Writer writer) throws IOException {
        Iterator<Map.Entry<?, Object>> it;
        Map.Entry<?, Object> entry;
        int length;
        int i;
        Map.Entry<?, Object> entry2;
        int i2;
        if (this.hasExtensions) {
            FieldSet<?> extensions = this.extensionSchema.getExtensions(t);
            if (!extensions.isEmpty()) {
                it = extensions.iterator();
                entry = it.next();
                int i3 = -1;
                length = this.buffer.length;
                Unsafe unsafe = UNSAFE;
                i = 0;
                int i4 = 0;
                while (i < length) {
                    int typeAndOffsetAt = typeAndOffsetAt(i);
                    int numberAt = numberAt(i);
                    int type = type(typeAndOffsetAt);
                    if (this.proto3 || type > 17) {
                        entry2 = entry;
                        i2 = 0;
                    } else {
                        int i5 = this.buffer[i + 2];
                        int i6 = i5 & 1048575;
                        Map.Entry<?, ?> entry3 = entry;
                        if (i6 != i3) {
                            i4 = unsafe.getInt(t, i6);
                            i3 = i6;
                        }
                        i2 = 1 << (i5 >>> 20);
                        entry2 = entry3;
                    }
                    while (entry2 != null && this.extensionSchema.extensionNumber(entry2) <= numberAt) {
                        this.extensionSchema.serializeExtension(writer, entry2);
                        entry2 = it.hasNext() ? it.next() : null;
                    }
                    Map.Entry<?, ?> entry4 = entry2;
                    int i7 = i3;
                    long offset = offset(typeAndOffsetAt);
                    switch (type) {
                        case 0:
                            if ((i2 & i4) != 0) {
                                writer.writeDouble(numberAt, doubleAt(t, offset));
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 1:
                            if ((i2 & i4) != 0) {
                                writer.writeFloat(numberAt, floatAt(t, offset));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 2:
                            if ((i2 & i4) != 0) {
                                writer.writeInt64(numberAt, unsafe.getLong(t, offset));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 3:
                            if ((i2 & i4) != 0) {
                                writer.writeUInt64(numberAt, unsafe.getLong(t, offset));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 4:
                            if ((i2 & i4) != 0) {
                                writer.writeInt32(numberAt, unsafe.getInt(t, offset));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 5:
                            if ((i2 & i4) != 0) {
                                writer.writeFixed64(numberAt, unsafe.getLong(t, offset));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 6:
                            if ((i2 & i4) != 0) {
                                writer.writeFixed32(numberAt, unsafe.getInt(t, offset));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 7:
                            if ((i2 & i4) != 0) {
                                writer.writeBool(numberAt, booleanAt(t, offset));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 8:
                            if ((i2 & i4) != 0) {
                                writeString(numberAt, unsafe.getObject(t, offset), writer);
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 9:
                            if ((i2 & i4) != 0) {
                                writer.writeMessage(numberAt, unsafe.getObject(t, offset), getMessageFieldSchema(i));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 10:
                            if ((i2 & i4) != 0) {
                                writer.writeBytes(numberAt, (ByteString) unsafe.getObject(t, offset));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 11:
                            if ((i2 & i4) != 0) {
                                writer.writeUInt32(numberAt, unsafe.getInt(t, offset));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 12:
                            if ((i2 & i4) != 0) {
                                writer.writeEnum(numberAt, unsafe.getInt(t, offset));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 13:
                            if ((i2 & i4) != 0) {
                                writer.writeSFixed32(numberAt, unsafe.getInt(t, offset));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 14:
                            if ((i2 & i4) != 0) {
                                writer.writeSFixed64(numberAt, unsafe.getLong(t, offset));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 15:
                            if ((i2 & i4) != 0) {
                                writer.writeSInt32(numberAt, unsafe.getInt(t, offset));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 16:
                            if ((i2 & i4) != 0) {
                                writer.writeSInt64(numberAt, unsafe.getLong(t, offset));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 17:
                            if ((i2 & i4) != 0) {
                                writer.writeGroup(numberAt, unsafe.getObject(t, offset), getMessageFieldSchema(i));
                            } else {
                                continue;
                            }
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 18:
                            SchemaUtil.writeDoubleList(numberAt(i), (List) unsafe.getObject(t, offset), writer, false);
                            continue;
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 19:
                            SchemaUtil.writeFloatList(numberAt(i), (List) unsafe.getObject(t, offset), writer, false);
                            continue;
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 20:
                            SchemaUtil.writeInt64List(numberAt(i), (List) unsafe.getObject(t, offset), writer, false);
                            continue;
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 21:
                            SchemaUtil.writeUInt64List(numberAt(i), (List) unsafe.getObject(t, offset), writer, false);
                            continue;
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 22:
                            SchemaUtil.writeInt32List(numberAt(i), (List) unsafe.getObject(t, offset), writer, false);
                            continue;
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 23:
                            SchemaUtil.writeFixed64List(numberAt(i), (List) unsafe.getObject(t, offset), writer, false);
                            continue;
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 24:
                            SchemaUtil.writeFixed32List(numberAt(i), (List) unsafe.getObject(t, offset), writer, false);
                            continue;
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 25:
                            SchemaUtil.writeBoolList(numberAt(i), (List) unsafe.getObject(t, offset), writer, false);
                            continue;
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 26:
                            SchemaUtil.writeStringList(numberAt(i), (List) unsafe.getObject(t, offset), writer);
                            break;
                        case 27:
                            SchemaUtil.writeMessageList(numberAt(i), (List) unsafe.getObject(t, offset), writer, getMessageFieldSchema(i));
                            break;
                        case 28:
                            SchemaUtil.writeBytesList(numberAt(i), (List) unsafe.getObject(t, offset), writer);
                            break;
                        case 29:
                            SchemaUtil.writeUInt32List(numberAt(i), (List) unsafe.getObject(t, offset), writer, false);
                            continue;
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 30:
                            SchemaUtil.writeEnumList(numberAt(i), (List) unsafe.getObject(t, offset), writer, false);
                            continue;
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 31:
                            SchemaUtil.writeSFixed32List(numberAt(i), (List) unsafe.getObject(t, offset), writer, false);
                            continue;
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 32:
                            SchemaUtil.writeSFixed64List(numberAt(i), (List) unsafe.getObject(t, offset), writer, false);
                            continue;
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 33:
                            SchemaUtil.writeSInt32List(numberAt(i), (List) unsafe.getObject(t, offset), writer, false);
                            continue;
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 34:
                            SchemaUtil.writeSInt64List(numberAt(i), (List) unsafe.getObject(t, offset), writer, false);
                            continue;
                            i += 3;
                            i3 = i7;
                            entry = entry4;
                        case 35:
                            SchemaUtil.writeDoubleList(numberAt(i), (List) unsafe.getObject(t, offset), writer, true);
                            break;
                        case 36:
                            SchemaUtil.writeFloatList(numberAt(i), (List) unsafe.getObject(t, offset), writer, true);
                            break;
                        case 37:
                            SchemaUtil.writeInt64List(numberAt(i), (List) unsafe.getObject(t, offset), writer, true);
                            break;
                        case 38:
                            SchemaUtil.writeUInt64List(numberAt(i), (List) unsafe.getObject(t, offset), writer, true);
                            break;
                        case 39:
                            SchemaUtil.writeInt32List(numberAt(i), (List) unsafe.getObject(t, offset), writer, true);
                            break;
                        case 40:
                            SchemaUtil.writeFixed64List(numberAt(i), (List) unsafe.getObject(t, offset), writer, true);
                            break;
                        case 41:
                            SchemaUtil.writeFixed32List(numberAt(i), (List) unsafe.getObject(t, offset), writer, true);
                            break;
                        case 42:
                            SchemaUtil.writeBoolList(numberAt(i), (List) unsafe.getObject(t, offset), writer, true);
                            break;
                        case 43:
                            SchemaUtil.writeUInt32List(numberAt(i), (List) unsafe.getObject(t, offset), writer, true);
                            break;
                        case 44:
                            SchemaUtil.writeEnumList(numberAt(i), (List) unsafe.getObject(t, offset), writer, true);
                            break;
                        case 45:
                            SchemaUtil.writeSFixed32List(numberAt(i), (List) unsafe.getObject(t, offset), writer, true);
                            break;
                        case 46:
                            SchemaUtil.writeSFixed64List(numberAt(i), (List) unsafe.getObject(t, offset), writer, true);
                            break;
                        case 47:
                            SchemaUtil.writeSInt32List(numberAt(i), (List) unsafe.getObject(t, offset), writer, true);
                            break;
                        case 48:
                            SchemaUtil.writeSInt64List(numberAt(i), (List) unsafe.getObject(t, offset), writer, true);
                            break;
                        case 49:
                            SchemaUtil.writeGroupList(numberAt(i), (List) unsafe.getObject(t, offset), writer, getMessageFieldSchema(i));
                            break;
                        case 50:
                            writeMapHelper(writer, numberAt, unsafe.getObject(t, offset), i);
                            break;
                        case 51:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeDouble(numberAt, oneofDoubleAt(t, offset));
                                break;
                            }
                            break;
                        case 52:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeFloat(numberAt, oneofFloatAt(t, offset));
                                break;
                            }
                            break;
                        case 53:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeInt64(numberAt, oneofLongAt(t, offset));
                                break;
                            }
                            break;
                        case 54:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeUInt64(numberAt, oneofLongAt(t, offset));
                                break;
                            }
                            break;
                        case 55:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeInt32(numberAt, oneofIntAt(t, offset));
                                break;
                            }
                            break;
                        case 56:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeFixed64(numberAt, oneofLongAt(t, offset));
                                break;
                            }
                            break;
                        case 57:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeFixed32(numberAt, oneofIntAt(t, offset));
                                break;
                            }
                            break;
                        case 58:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeBool(numberAt, oneofBooleanAt(t, offset));
                                break;
                            }
                            break;
                        case 59:
                            if (isOneofPresent(t, numberAt, i)) {
                                writeString(numberAt, unsafe.getObject(t, offset), writer);
                                break;
                            }
                            break;
                        case 60:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeMessage(numberAt, unsafe.getObject(t, offset), getMessageFieldSchema(i));
                                break;
                            }
                            break;
                        case 61:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeBytes(numberAt, (ByteString) unsafe.getObject(t, offset));
                                break;
                            }
                            break;
                        case 62:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeUInt32(numberAt, oneofIntAt(t, offset));
                                break;
                            }
                            break;
                        case 63:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeEnum(numberAt, oneofIntAt(t, offset));
                                break;
                            }
                            break;
                        case 64:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeSFixed32(numberAt, oneofIntAt(t, offset));
                                break;
                            }
                            break;
                        case 65:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeSFixed64(numberAt, oneofLongAt(t, offset));
                                break;
                            }
                            break;
                        case 66:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeSInt32(numberAt, oneofIntAt(t, offset));
                                break;
                            }
                            break;
                        case 67:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeSInt64(numberAt, oneofLongAt(t, offset));
                                break;
                            }
                            break;
                        case 68:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeGroup(numberAt, unsafe.getObject(t, offset), getMessageFieldSchema(i));
                                break;
                            }
                            break;
                    }
                    i += 3;
                    i3 = i7;
                    entry = entry4;
                }
                while (entry != null) {
                    this.extensionSchema.serializeExtension(writer, entry);
                    entry = it.hasNext() ? it.next() : null;
                }
                writeUnknownInMessageTo(this.unknownFieldSchema, t, writer);
            }
        }
        it = null;
        entry = null;
        int i32 = -1;
        length = this.buffer.length;
        Unsafe unsafe2 = UNSAFE;
        i = 0;
        int i42 = 0;
        while (i < length) {
        }
        while (entry != null) {
        }
        writeUnknownInMessageTo(this.unknownFieldSchema, t, writer);
    }

    /* JADX WARN: Removed duplicated region for block: B:275:0x0588  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x0025  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void writeFieldsInAscendingOrderProto3(T t, Writer writer) throws IOException {
        Iterator<Map.Entry<?, Object>> it;
        Map.Entry<?, Object> entry;
        int length;
        int i;
        if (this.hasExtensions) {
            FieldSet<?> extensions = this.extensionSchema.getExtensions(t);
            if (!extensions.isEmpty()) {
                it = extensions.iterator();
                entry = it.next();
                length = this.buffer.length;
                for (i = 0; i < length; i += 3) {
                    int typeAndOffsetAt = typeAndOffsetAt(i);
                    int numberAt = numberAt(i);
                    while (entry != null && this.extensionSchema.extensionNumber(entry) <= numberAt) {
                        this.extensionSchema.serializeExtension(writer, entry);
                        entry = it.hasNext() ? it.next() : null;
                    }
                    switch (type(typeAndOffsetAt)) {
                        case 0:
                            if (isFieldPresent(t, i)) {
                                writer.writeDouble(numberAt, doubleAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if (isFieldPresent(t, i)) {
                                writer.writeFloat(numberAt, floatAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if (isFieldPresent(t, i)) {
                                writer.writeInt64(numberAt, longAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if (isFieldPresent(t, i)) {
                                writer.writeUInt64(numberAt, longAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 4:
                            if (isFieldPresent(t, i)) {
                                writer.writeInt32(numberAt, intAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 5:
                            if (isFieldPresent(t, i)) {
                                writer.writeFixed64(numberAt, longAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 6:
                            if (isFieldPresent(t, i)) {
                                writer.writeFixed32(numberAt, intAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 7:
                            if (isFieldPresent(t, i)) {
                                writer.writeBool(numberAt, booleanAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 8:
                            if (isFieldPresent(t, i)) {
                                writeString(numberAt, UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer);
                                break;
                            } else {
                                break;
                            }
                        case 9:
                            if (isFieldPresent(t, i)) {
                                writer.writeMessage(numberAt, UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), getMessageFieldSchema(i));
                                break;
                            } else {
                                break;
                            }
                        case 10:
                            if (isFieldPresent(t, i)) {
                                writer.writeBytes(numberAt, (ByteString) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 11:
                            if (isFieldPresent(t, i)) {
                                writer.writeUInt32(numberAt, intAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 12:
                            if (isFieldPresent(t, i)) {
                                writer.writeEnum(numberAt, intAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 13:
                            if (isFieldPresent(t, i)) {
                                writer.writeSFixed32(numberAt, intAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 14:
                            if (isFieldPresent(t, i)) {
                                writer.writeSFixed64(numberAt, longAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 15:
                            if (isFieldPresent(t, i)) {
                                writer.writeSInt32(numberAt, intAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 16:
                            if (isFieldPresent(t, i)) {
                                writer.writeSInt64(numberAt, longAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 17:
                            if (isFieldPresent(t, i)) {
                                writer.writeGroup(numberAt, UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), getMessageFieldSchema(i));
                                break;
                            } else {
                                break;
                            }
                        case 18:
                            SchemaUtil.writeDoubleList(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 19:
                            SchemaUtil.writeFloatList(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 20:
                            SchemaUtil.writeInt64List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 21:
                            SchemaUtil.writeUInt64List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 22:
                            SchemaUtil.writeInt32List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 23:
                            SchemaUtil.writeFixed64List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 24:
                            SchemaUtil.writeFixed32List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 25:
                            SchemaUtil.writeBoolList(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 26:
                            SchemaUtil.writeStringList(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer);
                            break;
                        case 27:
                            SchemaUtil.writeMessageList(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, getMessageFieldSchema(i));
                            break;
                        case 28:
                            SchemaUtil.writeBytesList(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer);
                            break;
                        case 29:
                            SchemaUtil.writeUInt32List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 30:
                            SchemaUtil.writeEnumList(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 31:
                            SchemaUtil.writeSFixed32List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 32:
                            SchemaUtil.writeSFixed64List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 33:
                            SchemaUtil.writeSInt32List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 34:
                            SchemaUtil.writeSInt64List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 35:
                            SchemaUtil.writeDoubleList(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 36:
                            SchemaUtil.writeFloatList(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 37:
                            SchemaUtil.writeInt64List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 38:
                            SchemaUtil.writeUInt64List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 39:
                            SchemaUtil.writeInt32List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 40:
                            SchemaUtil.writeFixed64List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 41:
                            SchemaUtil.writeFixed32List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 42:
                            SchemaUtil.writeBoolList(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 43:
                            SchemaUtil.writeUInt32List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 44:
                            SchemaUtil.writeEnumList(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 45:
                            SchemaUtil.writeSFixed32List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 46:
                            SchemaUtil.writeSFixed64List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 47:
                            SchemaUtil.writeSInt32List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 48:
                            SchemaUtil.writeSInt64List(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 49:
                            SchemaUtil.writeGroupList(numberAt(i), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, getMessageFieldSchema(i));
                            break;
                        case 50:
                            writeMapHelper(writer, numberAt, UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), i);
                            break;
                        case 51:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeDouble(numberAt, oneofDoubleAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 52:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeFloat(numberAt, oneofFloatAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 53:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeInt64(numberAt, oneofLongAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 54:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeUInt64(numberAt, oneofLongAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 55:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeInt32(numberAt, oneofIntAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 56:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeFixed64(numberAt, oneofLongAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 57:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeFixed32(numberAt, oneofIntAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 58:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeBool(numberAt, oneofBooleanAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 59:
                            if (isOneofPresent(t, numberAt, i)) {
                                writeString(numberAt, UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer);
                                break;
                            } else {
                                break;
                            }
                        case 60:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeMessage(numberAt, UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), getMessageFieldSchema(i));
                                break;
                            } else {
                                break;
                            }
                        case 61:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeBytes(numberAt, (ByteString) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 62:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeUInt32(numberAt, oneofIntAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 63:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeEnum(numberAt, oneofIntAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 64:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeSFixed32(numberAt, oneofIntAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 65:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeSFixed64(numberAt, oneofLongAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 66:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeSInt32(numberAt, oneofIntAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 67:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeSInt64(numberAt, oneofLongAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 68:
                            if (isOneofPresent(t, numberAt, i)) {
                                writer.writeGroup(numberAt, UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), getMessageFieldSchema(i));
                                break;
                            } else {
                                break;
                            }
                    }
                }
                while (entry != null) {
                    this.extensionSchema.serializeExtension(writer, entry);
                    entry = it.hasNext() ? it.next() : null;
                }
                writeUnknownInMessageTo(this.unknownFieldSchema, t, writer);
            }
        }
        it = null;
        entry = null;
        length = this.buffer.length;
        while (i < length) {
        }
        while (entry != null) {
        }
        writeUnknownInMessageTo(this.unknownFieldSchema, t, writer);
    }

    /* JADX WARN: Removed duplicated region for block: B:275:0x058e  */
    /* JADX WARN: Removed duplicated region for block: B:8:0x002a  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private void writeFieldsInDescendingOrder(T t, Writer writer) throws IOException {
        Iterator<Map.Entry<?, Object>> it;
        Map.Entry<?, Object> entry;
        int length;
        writeUnknownInMessageTo(this.unknownFieldSchema, t, writer);
        if (this.hasExtensions) {
            FieldSet<?> extensions = this.extensionSchema.getExtensions(t);
            if (!extensions.isEmpty()) {
                it = extensions.descendingIterator();
                entry = it.next();
                for (length = this.buffer.length - 3; length >= 0; length -= 3) {
                    int typeAndOffsetAt = typeAndOffsetAt(length);
                    int numberAt = numberAt(length);
                    while (entry != null && this.extensionSchema.extensionNumber(entry) > numberAt) {
                        this.extensionSchema.serializeExtension(writer, entry);
                        entry = it.hasNext() ? it.next() : null;
                    }
                    switch (type(typeAndOffsetAt)) {
                        case 0:
                            if (isFieldPresent(t, length)) {
                                writer.writeDouble(numberAt, doubleAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 1:
                            if (isFieldPresent(t, length)) {
                                writer.writeFloat(numberAt, floatAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 2:
                            if (isFieldPresent(t, length)) {
                                writer.writeInt64(numberAt, longAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 3:
                            if (isFieldPresent(t, length)) {
                                writer.writeUInt64(numberAt, longAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 4:
                            if (isFieldPresent(t, length)) {
                                writer.writeInt32(numberAt, intAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 5:
                            if (isFieldPresent(t, length)) {
                                writer.writeFixed64(numberAt, longAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 6:
                            if (isFieldPresent(t, length)) {
                                writer.writeFixed32(numberAt, intAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 7:
                            if (isFieldPresent(t, length)) {
                                writer.writeBool(numberAt, booleanAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 8:
                            if (isFieldPresent(t, length)) {
                                writeString(numberAt, UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer);
                                break;
                            } else {
                                break;
                            }
                        case 9:
                            if (isFieldPresent(t, length)) {
                                writer.writeMessage(numberAt, UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), getMessageFieldSchema(length));
                                break;
                            } else {
                                break;
                            }
                        case 10:
                            if (isFieldPresent(t, length)) {
                                writer.writeBytes(numberAt, (ByteString) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 11:
                            if (isFieldPresent(t, length)) {
                                writer.writeUInt32(numberAt, intAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 12:
                            if (isFieldPresent(t, length)) {
                                writer.writeEnum(numberAt, intAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 13:
                            if (isFieldPresent(t, length)) {
                                writer.writeSFixed32(numberAt, intAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 14:
                            if (isFieldPresent(t, length)) {
                                writer.writeSFixed64(numberAt, longAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 15:
                            if (isFieldPresent(t, length)) {
                                writer.writeSInt32(numberAt, intAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 16:
                            if (isFieldPresent(t, length)) {
                                writer.writeSInt64(numberAt, longAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 17:
                            if (isFieldPresent(t, length)) {
                                writer.writeGroup(numberAt, UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), getMessageFieldSchema(length));
                                break;
                            } else {
                                break;
                            }
                        case 18:
                            SchemaUtil.writeDoubleList(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 19:
                            SchemaUtil.writeFloatList(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 20:
                            SchemaUtil.writeInt64List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 21:
                            SchemaUtil.writeUInt64List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 22:
                            SchemaUtil.writeInt32List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 23:
                            SchemaUtil.writeFixed64List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 24:
                            SchemaUtil.writeFixed32List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 25:
                            SchemaUtil.writeBoolList(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 26:
                            SchemaUtil.writeStringList(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer);
                            break;
                        case 27:
                            SchemaUtil.writeMessageList(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, getMessageFieldSchema(length));
                            break;
                        case 28:
                            SchemaUtil.writeBytesList(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer);
                            break;
                        case 29:
                            SchemaUtil.writeUInt32List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 30:
                            SchemaUtil.writeEnumList(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 31:
                            SchemaUtil.writeSFixed32List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 32:
                            SchemaUtil.writeSFixed64List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 33:
                            SchemaUtil.writeSInt32List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 34:
                            SchemaUtil.writeSInt64List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, false);
                            break;
                        case 35:
                            SchemaUtil.writeDoubleList(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 36:
                            SchemaUtil.writeFloatList(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 37:
                            SchemaUtil.writeInt64List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 38:
                            SchemaUtil.writeUInt64List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 39:
                            SchemaUtil.writeInt32List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 40:
                            SchemaUtil.writeFixed64List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 41:
                            SchemaUtil.writeFixed32List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 42:
                            SchemaUtil.writeBoolList(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 43:
                            SchemaUtil.writeUInt32List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 44:
                            SchemaUtil.writeEnumList(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 45:
                            SchemaUtil.writeSFixed32List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 46:
                            SchemaUtil.writeSFixed64List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 47:
                            SchemaUtil.writeSInt32List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 48:
                            SchemaUtil.writeSInt64List(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, true);
                            break;
                        case 49:
                            SchemaUtil.writeGroupList(numberAt(length), (List) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer, getMessageFieldSchema(length));
                            break;
                        case 50:
                            writeMapHelper(writer, numberAt, UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), length);
                            break;
                        case 51:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeDouble(numberAt, oneofDoubleAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 52:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeFloat(numberAt, oneofFloatAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 53:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeInt64(numberAt, oneofLongAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 54:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeUInt64(numberAt, oneofLongAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 55:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeInt32(numberAt, oneofIntAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 56:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeFixed64(numberAt, oneofLongAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 57:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeFixed32(numberAt, oneofIntAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 58:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeBool(numberAt, oneofBooleanAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 59:
                            if (isOneofPresent(t, numberAt, length)) {
                                writeString(numberAt, UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), writer);
                                break;
                            } else {
                                break;
                            }
                        case 60:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeMessage(numberAt, UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), getMessageFieldSchema(length));
                                break;
                            } else {
                                break;
                            }
                        case 61:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeBytes(numberAt, (ByteString) UnsafeUtil.getObject(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 62:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeUInt32(numberAt, oneofIntAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 63:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeEnum(numberAt, oneofIntAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 64:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeSFixed32(numberAt, oneofIntAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 65:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeSFixed64(numberAt, oneofLongAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 66:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeSInt32(numberAt, oneofIntAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 67:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeSInt64(numberAt, oneofLongAt(t, offset(typeAndOffsetAt)));
                                break;
                            } else {
                                break;
                            }
                        case 68:
                            if (isOneofPresent(t, numberAt, length)) {
                                writer.writeGroup(numberAt, UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), getMessageFieldSchema(length));
                                break;
                            } else {
                                break;
                            }
                    }
                }
                while (entry != null) {
                    this.extensionSchema.serializeExtension(writer, entry);
                    entry = it.hasNext() ? it.next() : null;
                }
            }
        }
        it = null;
        entry = null;
        while (length >= 0) {
        }
        while (entry != null) {
        }
    }

    private <K, V> void writeMapHelper(Writer writer, int i, Object obj, int i2) throws IOException {
        if (obj != null) {
            writer.writeMap(i, this.mapFieldSchema.forMapMetadata(getMapFieldDefaultEntry(i2)), this.mapFieldSchema.forMapData(obj));
        }
    }

    private <UT, UB> void writeUnknownInMessageTo(UnknownFieldSchema<UT, UB> unknownFieldSchema, T t, Writer writer) throws IOException {
        unknownFieldSchema.writeTo(unknownFieldSchema.mo927getFromMessage(t), writer);
    }

    @Override // com.google.protobuf.Schema
    public void mergeFrom(T t, Reader reader, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        Objects.requireNonNull(extensionRegistryLite);
        mergeFromHelper(this.unknownFieldSchema, this.extensionSchema, t, reader, extensionRegistryLite);
    }

    /* JADX WARN: Code restructure failed: missing block: B:324:0x0077, code lost:
        r0 = r16.checkInitializedCount;
     */
    /* JADX WARN: Code restructure failed: missing block: B:326:0x007b, code lost:
        if (r0 >= r16.repeatedFieldOffsetStart) goto L328;
     */
    /* JADX WARN: Code restructure failed: missing block: B:327:0x007d, code lost:
        r13 = filterMapUnknownEnumValues(r19, r16.intArray[r0], r13, r17);
        r0 = r0 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:329:0x0088, code lost:
        if (r13 == null) goto L332;
     */
    /* JADX WARN: Code restructure failed: missing block: B:330:0x008a, code lost:
        r17.setBuilderToMessage(r19, r13);
     */
    /* JADX WARN: Code restructure failed: missing block: B:331:0x008d, code lost:
        return;
     */
    /* JADX WARN: Code restructure failed: missing block: B:332:?, code lost:
        return;
     */
    /* JADX WARN: Multi-variable type inference failed */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private <UT, UB, ET extends FieldSet.FieldDescriptorLite<ET>> void mergeFromHelper(UnknownFieldSchema<UT, UB> unknownFieldSchema, ExtensionSchema<ET> extensionSchema, T t, Reader reader, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        Object obj = null;
        FieldSet<ET> fieldSet = null;
        while (true) {
            try {
                int fieldNumber = reader.getFieldNumber();
                int positionForFieldNumber = positionForFieldNumber(fieldNumber);
                if (positionForFieldNumber >= 0) {
                    int typeAndOffsetAt = typeAndOffsetAt(positionForFieldNumber);
                    try {
                        switch (type(typeAndOffsetAt)) {
                            case 0:
                                UnsafeUtil.putDouble(t, offset(typeAndOffsetAt), reader.readDouble());
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 1:
                                UnsafeUtil.putFloat(t, offset(typeAndOffsetAt), reader.readFloat());
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 2:
                                UnsafeUtil.putLong(t, offset(typeAndOffsetAt), reader.readInt64());
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 3:
                                UnsafeUtil.putLong(t, offset(typeAndOffsetAt), reader.readUInt64());
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 4:
                                UnsafeUtil.putInt(t, offset(typeAndOffsetAt), reader.readInt32());
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 5:
                                UnsafeUtil.putLong(t, offset(typeAndOffsetAt), reader.readFixed64());
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 6:
                                UnsafeUtil.putInt(t, offset(typeAndOffsetAt), reader.readFixed32());
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 7:
                                UnsafeUtil.putBoolean(t, offset(typeAndOffsetAt), reader.readBool());
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 8:
                                readString(t, typeAndOffsetAt, reader);
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 9:
                                if (isFieldPresent(t, positionForFieldNumber)) {
                                    UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Internal.mergeMessage(UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), reader.readMessageBySchemaWithCheck(getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite)));
                                    break;
                                } else {
                                    UnsafeUtil.putObject(t, offset(typeAndOffsetAt), reader.readMessageBySchemaWithCheck(getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite));
                                    setFieldPresent(t, positionForFieldNumber);
                                    break;
                                }
                            case 10:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), reader.readBytes());
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 11:
                                UnsafeUtil.putInt(t, offset(typeAndOffsetAt), reader.readUInt32());
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 12:
                                int readEnum = reader.readEnum();
                                Internal.EnumVerifier enumFieldVerifier = getEnumFieldVerifier(positionForFieldNumber);
                                if (enumFieldVerifier != null && !enumFieldVerifier.isInRange(readEnum)) {
                                    obj = SchemaUtil.storeUnknownEnum(fieldNumber, readEnum, obj, unknownFieldSchema);
                                    break;
                                }
                                UnsafeUtil.putInt(t, offset(typeAndOffsetAt), readEnum);
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 13:
                                UnsafeUtil.putInt(t, offset(typeAndOffsetAt), reader.readSFixed32());
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 14:
                                UnsafeUtil.putLong(t, offset(typeAndOffsetAt), reader.readSFixed64());
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 15:
                                UnsafeUtil.putInt(t, offset(typeAndOffsetAt), reader.readSInt32());
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 16:
                                UnsafeUtil.putLong(t, offset(typeAndOffsetAt), reader.readSInt64());
                                setFieldPresent(t, positionForFieldNumber);
                                break;
                            case 17:
                                if (isFieldPresent(t, positionForFieldNumber)) {
                                    UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Internal.mergeMessage(UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), reader.readGroupBySchemaWithCheck(getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite)));
                                    break;
                                } else {
                                    UnsafeUtil.putObject(t, offset(typeAndOffsetAt), reader.readGroupBySchemaWithCheck(getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite));
                                    setFieldPresent(t, positionForFieldNumber);
                                    break;
                                }
                            case 18:
                                reader.readDoubleList(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 19:
                                reader.readFloatList(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 20:
                                reader.readInt64List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 21:
                                reader.readUInt64List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 22:
                                reader.readInt32List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 23:
                                reader.readFixed64List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 24:
                                reader.readFixed32List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 25:
                                reader.readBoolList(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 26:
                                readStringList(t, typeAndOffsetAt, reader);
                                break;
                            case 27:
                                readMessageList(t, typeAndOffsetAt, reader, getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite);
                                break;
                            case 28:
                                reader.readBytesList(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 29:
                                reader.readUInt32List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 30:
                                List<Integer> mutableListAt = this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt));
                                reader.readEnumList(mutableListAt);
                                obj = SchemaUtil.filterUnknownEnumList(fieldNumber, mutableListAt, getEnumFieldVerifier(positionForFieldNumber), obj, unknownFieldSchema);
                                break;
                            case 31:
                                reader.readSFixed32List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 32:
                                reader.readSFixed64List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 33:
                                reader.readSInt32List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 34:
                                reader.readSInt64List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 35:
                                reader.readDoubleList(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 36:
                                reader.readFloatList(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 37:
                                reader.readInt64List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 38:
                                reader.readUInt64List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 39:
                                reader.readInt32List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 40:
                                reader.readFixed64List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 41:
                                reader.readFixed32List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 42:
                                reader.readBoolList(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 43:
                                reader.readUInt32List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 44:
                                List<Integer> mutableListAt2 = this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt));
                                reader.readEnumList(mutableListAt2);
                                obj = SchemaUtil.filterUnknownEnumList(fieldNumber, mutableListAt2, getEnumFieldVerifier(positionForFieldNumber), obj, unknownFieldSchema);
                                break;
                            case 45:
                                reader.readSFixed32List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 46:
                                reader.readSFixed64List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 47:
                                reader.readSInt32List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 48:
                                reader.readSInt64List(this.listFieldSchema.mutableListAt(t, offset(typeAndOffsetAt)));
                                break;
                            case 49:
                                readGroupList(t, offset(typeAndOffsetAt), reader, getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite);
                                break;
                            case 50:
                                mergeMap(t, positionForFieldNumber, getMapFieldDefaultEntry(positionForFieldNumber), extensionRegistryLite, reader);
                                break;
                            case 51:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Double.valueOf(reader.readDouble()));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 52:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Float.valueOf(reader.readFloat()));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 53:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Long.valueOf(reader.readInt64()));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 54:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Long.valueOf(reader.readUInt64()));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 55:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Integer.valueOf(reader.readInt32()));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 56:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Long.valueOf(reader.readFixed64()));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 57:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Integer.valueOf(reader.readFixed32()));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 58:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Boolean.valueOf(reader.readBool()));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 59:
                                readString(t, typeAndOffsetAt, reader);
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 60:
                                if (isOneofPresent(t, fieldNumber, positionForFieldNumber)) {
                                    UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Internal.mergeMessage(UnsafeUtil.getObject(t, offset(typeAndOffsetAt)), reader.readMessageBySchemaWithCheck(getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite)));
                                } else {
                                    UnsafeUtil.putObject(t, offset(typeAndOffsetAt), reader.readMessageBySchemaWithCheck(getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite));
                                    setFieldPresent(t, positionForFieldNumber);
                                }
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 61:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), reader.readBytes());
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 62:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Integer.valueOf(reader.readUInt32()));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 63:
                                int readEnum2 = reader.readEnum();
                                Internal.EnumVerifier enumFieldVerifier2 = getEnumFieldVerifier(positionForFieldNumber);
                                if (enumFieldVerifier2 != null && !enumFieldVerifier2.isInRange(readEnum2)) {
                                    obj = SchemaUtil.storeUnknownEnum(fieldNumber, readEnum2, obj, unknownFieldSchema);
                                    break;
                                }
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Integer.valueOf(readEnum2));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 64:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Integer.valueOf(reader.readSFixed32()));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 65:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Long.valueOf(reader.readSFixed64()));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 66:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Integer.valueOf(reader.readSInt32()));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 67:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), Long.valueOf(reader.readSInt64()));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            case 68:
                                UnsafeUtil.putObject(t, offset(typeAndOffsetAt), reader.readGroupBySchemaWithCheck(getMessageFieldSchema(positionForFieldNumber), extensionRegistryLite));
                                setOneofPresent(t, fieldNumber, positionForFieldNumber);
                                break;
                            default:
                                if (obj == null) {
                                    obj = unknownFieldSchema.mo928newBuilder();
                                }
                                if (!unknownFieldSchema.mergeOneFieldFrom(obj, reader)) {
                                    for (int i = this.checkInitializedCount; i < this.repeatedFieldOffsetStart; i++) {
                                        obj = filterMapUnknownEnumValues(t, this.intArray[i], obj, unknownFieldSchema);
                                    }
                                    if (obj == null) {
                                        return;
                                    }
                                    unknownFieldSchema.setBuilderToMessage(t, obj);
                                    return;
                                }
                                break;
                        }
                    } catch (InvalidProtocolBufferException.InvalidWireTypeException unused) {
                        if (!unknownFieldSchema.shouldDiscardUnknownFields(reader)) {
                            if (obj == null) {
                                obj = unknownFieldSchema.mo926getBuilderFromMessage(t);
                            }
                            if (!unknownFieldSchema.mergeOneFieldFrom(obj, reader)) {
                                for (int i2 = this.checkInitializedCount; i2 < this.repeatedFieldOffsetStart; i2++) {
                                    obj = filterMapUnknownEnumValues(t, this.intArray[i2], obj, unknownFieldSchema);
                                }
                                if (obj == null) {
                                    return;
                                }
                                unknownFieldSchema.setBuilderToMessage(t, obj);
                                return;
                            }
                        } else if (!reader.skipField()) {
                            for (int i3 = this.checkInitializedCount; i3 < this.repeatedFieldOffsetStart; i3++) {
                                obj = filterMapUnknownEnumValues(t, this.intArray[i3], obj, unknownFieldSchema);
                            }
                            if (obj == null) {
                                return;
                            }
                            unknownFieldSchema.setBuilderToMessage(t, obj);
                            return;
                        }
                    }
                } else if (fieldNumber == Integer.MAX_VALUE) {
                    for (int i4 = this.checkInitializedCount; i4 < this.repeatedFieldOffsetStart; i4++) {
                        obj = filterMapUnknownEnumValues(t, this.intArray[i4], obj, unknownFieldSchema);
                    }
                    if (obj == null) {
                        return;
                    }
                    unknownFieldSchema.setBuilderToMessage(t, obj);
                    return;
                } else {
                    Object findExtensionByNumber = !this.hasExtensions ? null : extensionSchema.findExtensionByNumber(extensionRegistryLite, this.defaultInstance, fieldNumber);
                    if (findExtensionByNumber != null) {
                        if (fieldSet == null) {
                            fieldSet = extensionSchema.getMutableExtensions(t);
                        }
                        obj = extensionSchema.parseExtension(reader, findExtensionByNumber, extensionRegistryLite, fieldSet, obj, unknownFieldSchema);
                    } else if (!unknownFieldSchema.shouldDiscardUnknownFields(reader)) {
                        if (obj == null) {
                            obj = unknownFieldSchema.mo926getBuilderFromMessage(t);
                        }
                        if (unknownFieldSchema.mergeOneFieldFrom(obj, reader)) {
                        }
                    } else if (reader.skipField()) {
                    }
                }
            } catch (Throwable th) {
                for (int i5 = this.checkInitializedCount; i5 < this.repeatedFieldOffsetStart; i5++) {
                    obj = filterMapUnknownEnumValues(t, this.intArray[i5], obj, unknownFieldSchema);
                }
                if (obj != null) {
                    unknownFieldSchema.setBuilderToMessage(t, obj);
                }
                throw th;
            }
        }
    }

    private Schema getMessageFieldSchema(int i) {
        int i2 = (i / 3) * 2;
        Schema schema = (Schema) this.objects[i2];
        if (schema != null) {
            return schema;
        }
        Schema<T> schemaFor = Protobuf.getInstance().schemaFor((Class) ((Class) this.objects[i2 + 1]));
        this.objects[i2] = schemaFor;
        return schemaFor;
    }

    private Object getMapFieldDefaultEntry(int i) {
        return this.objects[(i / 3) * 2];
    }

    private Internal.EnumVerifier getEnumFieldVerifier(int i) {
        return (Internal.EnumVerifier) this.objects[((i / 3) * 2) + 1];
    }

    @Override // com.google.protobuf.Schema
    public void makeImmutable(T t) {
        int i;
        int i2 = this.checkInitializedCount;
        while (true) {
            i = this.repeatedFieldOffsetStart;
            if (i2 >= i) {
                break;
            }
            long offset = offset(typeAndOffsetAt(this.intArray[i2]));
            Object object = UnsafeUtil.getObject(t, offset);
            if (object != null) {
                UnsafeUtil.putObject(t, offset, this.mapFieldSchema.toImmutable(object));
            }
            i2++;
        }
        int length = this.intArray.length;
        while (i < length) {
            this.listFieldSchema.makeImmutableListAt(t, this.intArray[i]);
            i++;
        }
        this.unknownFieldSchema.makeImmutable(t);
        if (this.hasExtensions) {
            this.extensionSchema.makeImmutable(t);
        }
    }

    private final <K, V> void mergeMap(Object obj, int i, Object obj2, ExtensionRegistryLite extensionRegistryLite, Reader reader) throws IOException {
        long offset = offset(typeAndOffsetAt(i));
        Object object = UnsafeUtil.getObject(obj, offset);
        if (object == null) {
            object = this.mapFieldSchema.newMapField(obj2);
            UnsafeUtil.putObject(obj, offset, object);
        } else if (this.mapFieldSchema.isImmutable(object)) {
            Object newMapField = this.mapFieldSchema.newMapField(obj2);
            this.mapFieldSchema.mergeFrom(newMapField, object);
            UnsafeUtil.putObject(obj, offset, newMapField);
            object = newMapField;
        }
        reader.readMap(this.mapFieldSchema.forMutableMapData(object), this.mapFieldSchema.forMapMetadata(obj2), extensionRegistryLite);
    }

    private final <UT, UB> UB filterMapUnknownEnumValues(Object obj, int i, UB ub, UnknownFieldSchema<UT, UB> unknownFieldSchema) {
        Internal.EnumVerifier enumFieldVerifier;
        int numberAt = numberAt(i);
        Object object = UnsafeUtil.getObject(obj, offset(typeAndOffsetAt(i)));
        return (object == null || (enumFieldVerifier = getEnumFieldVerifier(i)) == null) ? ub : (UB) filterUnknownEnumMap(i, numberAt, this.mapFieldSchema.forMutableMapData(object), enumFieldVerifier, ub, unknownFieldSchema);
    }

    private final <K, V, UT, UB> UB filterUnknownEnumMap(int i, int i2, Map<K, V> map, Internal.EnumVerifier enumVerifier, UB ub, UnknownFieldSchema<UT, UB> unknownFieldSchema) {
        MapEntryLite.Metadata<?, ?> forMapMetadata = this.mapFieldSchema.forMapMetadata(getMapFieldDefaultEntry(i));
        Iterator<Map.Entry<K, V>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<K, V> next = it.next();
            if (!enumVerifier.isInRange(((Integer) next.getValue()).intValue())) {
                if (ub == null) {
                    ub = unknownFieldSchema.mo928newBuilder();
                }
                ByteString.CodedBuilder newCodedBuilder = ByteString.newCodedBuilder(MapEntryLite.computeSerializedSize(forMapMetadata, next.getKey(), next.getValue()));
                try {
                    MapEntryLite.writeTo(newCodedBuilder.getCodedOutput(), forMapMetadata, next.getKey(), next.getValue());
                    unknownFieldSchema.addLengthDelimited(ub, i2, newCodedBuilder.build());
                    it.remove();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ub;
    }

    @Override // com.google.protobuf.Schema
    public final boolean isInitialized(T t) {
        int i;
        int i2 = -1;
        int i3 = 0;
        for (int i4 = 0; i4 < this.checkInitializedCount; i4++) {
            int i5 = this.intArray[i4];
            int numberAt = numberAt(i5);
            int typeAndOffsetAt = typeAndOffsetAt(i5);
            if (!this.proto3) {
                int i6 = this.buffer[i5 + 2];
                int i7 = 1048575 & i6;
                i = 1 << (i6 >>> 20);
                if (i7 != i2) {
                    i3 = UNSAFE.getInt(t, i7);
                    i2 = i7;
                }
            } else {
                i = 0;
            }
            if (isRequired(typeAndOffsetAt) && !isFieldPresent(t, i5, i3, i)) {
                return false;
            }
            int type = type(typeAndOffsetAt);
            if (type == 9 || type == 17) {
                if (isFieldPresent(t, i5, i3, i) && !isInitialized(t, typeAndOffsetAt, getMessageFieldSchema(i5))) {
                    return false;
                }
            } else {
                if (type != 27) {
                    if (type == 60 || type == 68) {
                        if (isOneofPresent(t, numberAt, i5) && !isInitialized(t, typeAndOffsetAt, getMessageFieldSchema(i5))) {
                            return false;
                        }
                    } else if (type != 49) {
                        if (type == 50 && !isMapInitialized(t, typeAndOffsetAt, i5)) {
                            return false;
                        }
                    }
                }
                if (!isListInitialized(t, typeAndOffsetAt, i5)) {
                    return false;
                }
            }
        }
        return !this.hasExtensions || this.extensionSchema.getExtensions(t).isInitialized();
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static boolean isInitialized(Object obj, int i, Schema schema) {
        return schema.isInitialized(UnsafeUtil.getObject(obj, offset(i)));
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <N> boolean isListInitialized(Object obj, int i, int i2) {
        List list = (List) UnsafeUtil.getObject(obj, offset(i));
        if (list.isEmpty()) {
            return true;
        }
        Schema messageFieldSchema = getMessageFieldSchema(i2);
        for (int i3 = 0; i3 < list.size(); i3++) {
            if (!messageFieldSchema.isInitialized(list.get(i3))) {
                return false;
            }
        }
        return true;
    }

    private boolean isMapInitialized(T t, int i, int i2) {
        if (this.mapFieldSchema.forMapData(UnsafeUtil.getObject(t, offset(i))).isEmpty()) {
            return true;
        }
        this.mapFieldSchema.forMapMetadata(getMapFieldDefaultEntry(i2));
        throw null;
    }

    private void writeString(int i, Object obj, Writer writer) throws IOException {
        if (obj instanceof String) {
            writer.writeString(i, (String) obj);
        } else {
            writer.writeBytes(i, (ByteString) obj);
        }
    }

    private void readString(Object obj, int i, Reader reader) throws IOException {
        if (isEnforceUtf8(i)) {
            UnsafeUtil.putObject(obj, offset(i), reader.readStringRequireUtf8());
        } else if (this.lite) {
            UnsafeUtil.putObject(obj, offset(i), reader.readString());
        } else {
            UnsafeUtil.putObject(obj, offset(i), reader.readBytes());
        }
    }

    private void readStringList(Object obj, int i, Reader reader) throws IOException {
        if (isEnforceUtf8(i)) {
            reader.readStringListRequireUtf8(this.listFieldSchema.mutableListAt(obj, offset(i)));
        } else {
            reader.readStringList(this.listFieldSchema.mutableListAt(obj, offset(i)));
        }
    }

    private <E> void readMessageList(Object obj, int i, Reader reader, Schema<E> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        reader.readMessageList(this.listFieldSchema.mutableListAt(obj, offset(i)), schema, extensionRegistryLite);
    }

    private <E> void readGroupList(Object obj, long j, Reader reader, Schema<E> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException {
        reader.readGroupList(this.listFieldSchema.mutableListAt(obj, j), schema, extensionRegistryLite);
    }

    private int numberAt(int i) {
        return this.buffer[i];
    }

    private int typeAndOffsetAt(int i) {
        return this.buffer[i + 1];
    }

    private int presenceMaskAndOffsetAt(int i) {
        return this.buffer[i + 2];
    }

    private static <T> double doubleAt(T t, long j) {
        return UnsafeUtil.getDouble(t, j);
    }

    private static <T> float floatAt(T t, long j) {
        return UnsafeUtil.getFloat(t, j);
    }

    private static <T> int intAt(T t, long j) {
        return UnsafeUtil.getInt(t, j);
    }

    private static <T> long longAt(T t, long j) {
        return UnsafeUtil.getLong(t, j);
    }

    private static <T> boolean booleanAt(T t, long j) {
        return UnsafeUtil.getBoolean(t, j);
    }

    private static <T> double oneofDoubleAt(T t, long j) {
        return ((Double) UnsafeUtil.getObject(t, j)).doubleValue();
    }

    private static <T> float oneofFloatAt(T t, long j) {
        return ((Float) UnsafeUtil.getObject(t, j)).floatValue();
    }

    private static <T> int oneofIntAt(T t, long j) {
        return ((Integer) UnsafeUtil.getObject(t, j)).intValue();
    }

    private static <T> long oneofLongAt(T t, long j) {
        return ((Long) UnsafeUtil.getObject(t, j)).longValue();
    }

    private static <T> boolean oneofBooleanAt(T t, long j) {
        return ((Boolean) UnsafeUtil.getObject(t, j)).booleanValue();
    }

    private boolean arePresentForEquals(T t, T t2, int i) {
        return isFieldPresent(t, i) == isFieldPresent(t2, i);
    }

    private boolean isFieldPresent(T t, int i, int i2, int i3) {
        if (this.proto3) {
            return isFieldPresent(t, i);
        }
        return (i2 & i3) != 0;
    }

    private boolean isFieldPresent(T t, int i) {
        if (this.proto3) {
            int typeAndOffsetAt = typeAndOffsetAt(i);
            long offset = offset(typeAndOffsetAt);
            switch (type(typeAndOffsetAt)) {
                case 0:
                    return UnsafeUtil.getDouble(t, offset) != 0.0d;
                case 1:
                    return UnsafeUtil.getFloat(t, offset) != 0.0f;
                case 2:
                    return UnsafeUtil.getLong(t, offset) != 0;
                case 3:
                    return UnsafeUtil.getLong(t, offset) != 0;
                case 4:
                    return UnsafeUtil.getInt(t, offset) != 0;
                case 5:
                    return UnsafeUtil.getLong(t, offset) != 0;
                case 6:
                    return UnsafeUtil.getInt(t, offset) != 0;
                case 7:
                    return UnsafeUtil.getBoolean(t, offset);
                case 8:
                    Object object = UnsafeUtil.getObject(t, offset);
                    if (object instanceof String) {
                        return !((String) object).isEmpty();
                    }
                    if (object instanceof ByteString) {
                        return !ByteString.EMPTY.equals(object);
                    }
                    throw new IllegalArgumentException();
                case 9:
                    return UnsafeUtil.getObject(t, offset) != null;
                case 10:
                    return !ByteString.EMPTY.equals(UnsafeUtil.getObject(t, offset));
                case 11:
                    return UnsafeUtil.getInt(t, offset) != 0;
                case 12:
                    return UnsafeUtil.getInt(t, offset) != 0;
                case 13:
                    return UnsafeUtil.getInt(t, offset) != 0;
                case 14:
                    return UnsafeUtil.getLong(t, offset) != 0;
                case 15:
                    return UnsafeUtil.getInt(t, offset) != 0;
                case 16:
                    return UnsafeUtil.getLong(t, offset) != 0;
                case 17:
                    return UnsafeUtil.getObject(t, offset) != null;
                default:
                    throw new IllegalArgumentException();
            }
        }
        int presenceMaskAndOffsetAt = presenceMaskAndOffsetAt(i);
        return (UnsafeUtil.getInt(t, (long) (presenceMaskAndOffsetAt & 1048575)) & (1 << (presenceMaskAndOffsetAt >>> 20))) != 0;
    }

    private void setFieldPresent(T t, int i) {
        if (this.proto3) {
            return;
        }
        int presenceMaskAndOffsetAt = presenceMaskAndOffsetAt(i);
        long j = presenceMaskAndOffsetAt & 1048575;
        UnsafeUtil.putInt(t, j, UnsafeUtil.getInt(t, j) | (1 << (presenceMaskAndOffsetAt >>> 20)));
    }

    private boolean isOneofPresent(T t, int i, int i2) {
        return UnsafeUtil.getInt(t, (long) (presenceMaskAndOffsetAt(i2) & 1048575)) == i;
    }

    private boolean isOneofCaseEqual(T t, T t2, int i) {
        long presenceMaskAndOffsetAt = presenceMaskAndOffsetAt(i) & 1048575;
        return UnsafeUtil.getInt(t, presenceMaskAndOffsetAt) == UnsafeUtil.getInt(t2, presenceMaskAndOffsetAt);
    }

    private void setOneofPresent(T t, int i, int i2) {
        UnsafeUtil.putInt(t, presenceMaskAndOffsetAt(i2) & 1048575, i);
    }

    private int positionForFieldNumber(int i) {
        if (i < this.minFieldNumber || i > this.maxFieldNumber) {
            return -1;
        }
        return slowPositionForFieldNumber(i, 0);
    }

    private int slowPositionForFieldNumber(int i, int i2) {
        int length = (this.buffer.length / 3) - 1;
        while (i2 <= length) {
            int i3 = (length + i2) >>> 1;
            int i4 = i3 * 3;
            int numberAt = numberAt(i4);
            if (i == numberAt) {
                return i4;
            }
            if (i < numberAt) {
                length = i3 - 1;
            } else {
                i2 = i3 + 1;
            }
        }
        return -1;
    }
}
