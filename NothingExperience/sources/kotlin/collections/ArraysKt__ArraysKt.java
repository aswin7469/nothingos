package kotlin.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.TuplesKt;
import kotlin.UByteArray;
import kotlin.UIntArray;
import kotlin.ULongArray;
import kotlin.UShortArray;
import kotlin.collections.unsigned.UArraysKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

@Metadata(mo14007d1 = {"\u0000H\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010!\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a5\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0002*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u00032\u0010\u0010\u0004\u001a\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0003H\u0001¢\u0006\u0004\b\u0005\u0010\u0006\u001a#\u0010\u0007\u001a\u00020\b\"\u0004\b\u0000\u0010\u0002*\f\u0012\u0006\b\u0001\u0012\u0002H\u0002\u0018\u00010\u0003H\u0001¢\u0006\u0004\b\t\u0010\n\u001a?\u0010\u000b\u001a\u00020\f\"\u0004\b\u0000\u0010\u0002*\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00032\n\u0010\r\u001a\u00060\u000ej\u0002`\u000f2\u0010\u0010\u0010\u001a\f\u0012\b\u0012\u0006\u0012\u0002\b\u00030\u00030\u0011H\u0002¢\u0006\u0004\b\u0012\u0010\u0013\u001a+\u0010\u0014\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0015\"\u0004\b\u0000\u0010\u0002*\u0012\u0012\u000e\b\u0001\u0012\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u00030\u0003¢\u0006\u0002\u0010\u0016\u001a;\u0010\u0017\u001a\u0002H\u0018\"\u0010\b\u0000\u0010\u0019*\u0006\u0012\u0002\b\u00030\u0003*\u0002H\u0018\"\u0004\b\u0001\u0010\u0018*\u0002H\u00192\f\u0010\u001a\u001a\b\u0012\u0004\u0012\u0002H\u00180\u001bH\bø\u0001\u0000¢\u0006\u0002\u0010\u001c\u001a)\u0010\u001d\u001a\u00020\u0001*\b\u0012\u0002\b\u0003\u0018\u00010\u0003H\b\u0002\u000e\n\f\b\u0000\u0012\u0002\u0018\u0001\u001a\u0004\b\u0003\u0010\u0000¢\u0006\u0002\u0010\u001e\u001aG\u0010\u001f\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u0015\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00180\u00150 \"\u0004\b\u0000\u0010\u0002\"\u0004\b\u0001\u0010\u0018*\u0016\u0012\u0012\b\u0001\u0012\u000e\u0012\u0004\u0012\u0002H\u0002\u0012\u0004\u0012\u0002H\u00180 0\u0003¢\u0006\u0002\u0010!\u0002\u0007\n\u0005\b20\u0001¨\u0006\""}, mo14008d2 = {"contentDeepEqualsImpl", "", "T", "", "other", "contentDeepEquals", "([Ljava/lang/Object;[Ljava/lang/Object;)Z", "contentDeepToStringImpl", "", "contentDeepToString", "([Ljava/lang/Object;)Ljava/lang/String;", "contentDeepToStringInternal", "", "result", "Ljava/lang/StringBuilder;", "Lkotlin/text/StringBuilder;", "processed", "", "contentDeepToStringInternal$ArraysKt__ArraysKt", "([Ljava/lang/Object;Ljava/lang/StringBuilder;Ljava/util/List;)V", "flatten", "", "([[Ljava/lang/Object;)Ljava/util/List;", "ifEmpty", "R", "C", "defaultValue", "Lkotlin/Function0;", "([Ljava/lang/Object;Lkotlin/jvm/functions/Function0;)Ljava/lang/Object;", "isNullOrEmpty", "([Ljava/lang/Object;)Z", "unzip", "Lkotlin/Pair;", "([Lkotlin/Pair;)Lkotlin/Pair;", "kotlin-stdlib"}, mo14009k = 5, mo14010mv = {1, 6, 0}, mo14012xi = 49, mo14013xs = "kotlin/collections/ArraysKt")
/* compiled from: Arrays.kt */
class ArraysKt__ArraysKt extends ArraysKt__ArraysJVMKt {
    public static final <T> List<T> flatten(T[][] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "<this>");
        Object[] objArr = (Object[]) tArr;
        int length = objArr.length;
        int i = 0;
        int i2 = 0;
        int i3 = 0;
        while (i2 < length) {
            Object obj = objArr[i2];
            i2++;
            i3 += ((Object[]) obj).length;
        }
        ArrayList arrayList = new ArrayList(i3);
        int length2 = objArr.length;
        while (i < length2) {
            T[] tArr2 = tArr[i];
            i++;
            CollectionsKt.addAll(arrayList, tArr2);
        }
        return arrayList;
    }

    public static final <T, R> Pair<List<T>, List<R>> unzip(Pair<? extends T, ? extends R>[] pairArr) {
        Intrinsics.checkNotNullParameter(pairArr, "<this>");
        ArrayList arrayList = new ArrayList(pairArr.length);
        ArrayList arrayList2 = new ArrayList(pairArr.length);
        int length = pairArr.length;
        int i = 0;
        while (i < length) {
            Pair<? extends T, ? extends R> pair = pairArr[i];
            i++;
            arrayList.add(pair.getFirst());
            arrayList2.add(pair.getSecond());
        }
        return TuplesKt.m52to(arrayList, arrayList2);
    }

    private static final boolean isNullOrEmpty(Object[] objArr) {
        if (objArr != null) {
            if (!(objArr.length == 0)) {
                return false;
            }
        }
        return true;
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [R, C] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static final <C extends java.lang.Object & R, R> R ifEmpty(C r1, kotlin.jvm.functions.Function0<? extends R> r2) {
        /*
            java.lang.String r0 = "defaultValue"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r2, r0)
            int r0 = r1.length
            if (r0 != 0) goto L_0x000a
            r0 = 1
            goto L_0x000b
        L_0x000a:
            r0 = 0
        L_0x000b:
            if (r0 == 0) goto L_0x0011
            java.lang.Object r1 = r2.invoke()
        L_0x0011:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.ArraysKt__ArraysKt.ifEmpty(java.lang.Object[], kotlin.jvm.functions.Function0):java.lang.Object");
    }

    public static final <T> boolean contentDeepEquals(T[] tArr, T[] tArr2) {
        if (tArr == tArr2) {
            return true;
        }
        if (tArr == null || tArr2 == null || tArr.length != tArr2.length) {
            return false;
        }
        int length = tArr.length;
        int i = 0;
        while (i < length) {
            int i2 = i + 1;
            T t = tArr[i];
            T t2 = tArr2[i];
            if (t != t2) {
                if (t == null || t2 == null) {
                    return false;
                }
                if (!(t instanceof Object[]) || !(t2 instanceof Object[])) {
                    if (!(t instanceof byte[]) || !(t2 instanceof byte[])) {
                        if (!(t instanceof short[]) || !(t2 instanceof short[])) {
                            if (!(t instanceof int[]) || !(t2 instanceof int[])) {
                                if (!(t instanceof long[]) || !(t2 instanceof long[])) {
                                    if (!(t instanceof float[]) || !(t2 instanceof float[])) {
                                        if (!(t instanceof double[]) || !(t2 instanceof double[])) {
                                            if (!(t instanceof char[]) || !(t2 instanceof char[])) {
                                                if (!(t instanceof boolean[]) || !(t2 instanceof boolean[])) {
                                                    if (!(t instanceof UByteArray) || !(t2 instanceof UByteArray)) {
                                                        if (!(t instanceof UShortArray) || !(t2 instanceof UShortArray)) {
                                                            if (!(t instanceof UIntArray) || !(t2 instanceof UIntArray)) {
                                                                if (!(t instanceof ULongArray) || !(t2 instanceof ULongArray)) {
                                                                    if (!Intrinsics.areEqual((Object) t, (Object) t2)) {
                                                                        return false;
                                                                    }
                                                                } else if (!UArraysKt.m639contentEqualslec5QzE(((ULongArray) t).m335unboximpl(), ((ULongArray) t2).m335unboximpl())) {
                                                                    return false;
                                                                }
                                                            } else if (!UArraysKt.m635contentEqualsKJPZfPQ(((UIntArray) t).m257unboximpl(), ((UIntArray) t2).m257unboximpl())) {
                                                                return false;
                                                            }
                                                        } else if (!UArraysKt.m634contentEqualsFGO6Aew(((UShortArray) t).m439unboximpl(), ((UShortArray) t2).m439unboximpl())) {
                                                            return false;
                                                        }
                                                    } else if (!UArraysKt.m637contentEqualskV0jMPg(((UByteArray) t).m179unboximpl(), ((UByteArray) t2).m179unboximpl())) {
                                                        return false;
                                                    }
                                                } else if (!Arrays.equals((boolean[]) t, (boolean[]) t2)) {
                                                    return false;
                                                }
                                            } else if (!Arrays.equals((char[]) t, (char[]) t2)) {
                                                return false;
                                            }
                                        } else if (!Arrays.equals((double[]) t, (double[]) t2)) {
                                            return false;
                                        }
                                    } else if (!Arrays.equals((float[]) t, (float[]) t2)) {
                                        return false;
                                    }
                                } else if (!Arrays.equals((long[]) t, (long[]) t2)) {
                                    return false;
                                }
                            } else if (!Arrays.equals((int[]) t, (int[]) t2)) {
                                return false;
                            }
                        } else if (!Arrays.equals((short[]) t, (short[]) t2)) {
                            return false;
                        }
                    } else if (!Arrays.equals((byte[]) t, (byte[]) t2)) {
                        return false;
                    }
                } else if (!ArraysKt.contentDeepEquals((Object[]) t, (Object[]) t2)) {
                    return false;
                }
            }
            i = i2;
        }
        return true;
    }

    public static final <T> String contentDeepToString(T[] tArr) {
        if (tArr == null) {
            return "null";
        }
        StringBuilder sb = new StringBuilder((RangesKt.coerceAtMost(tArr.length, 429496729) * 5) + 2);
        contentDeepToStringInternal$ArraysKt__ArraysKt(tArr, sb, new ArrayList());
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "StringBuilder(capacity).…builderAction).toString()");
        return sb2;
    }

    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v1, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v2, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v3, resolved type: long[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v4, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v5, resolved type: int[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v6, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v7, resolved type: short[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v8, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v10, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v11, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v12, resolved type: byte[]} */
    /* JADX DEBUG: Multi-variable search result rejected for TypeSearchVarInfo{r4v13, resolved type: byte[]} */
    /* JADX WARNING: Multi-variable type inference failed */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static final <T> void contentDeepToStringInternal$ArraysKt__ArraysKt(T[] r5, java.lang.StringBuilder r6, java.util.List<java.lang.Object[]> r7) {
        /*
            boolean r0 = r7.contains(r5)
            if (r0 == 0) goto L_0x000c
            java.lang.String r5 = "[...]"
            r6.append(r5)
            return
        L_0x000c:
            r7.add(r5)
            r0 = 91
            r6.append(r0)
            r0 = 0
            int r1 = r5.length
        L_0x0016:
            if (r0 >= r1) goto L_0x0122
            int r2 = r0 + 1
            if (r0 == 0) goto L_0x0021
            java.lang.String r3 = ", "
            r6.append(r3)
        L_0x0021:
            r0 = r5[r0]
            if (r0 != 0) goto L_0x002c
            java.lang.String r0 = "null"
            r6.append(r0)
            goto L_0x011f
        L_0x002c:
            boolean r3 = r0 instanceof java.lang.Object[]
            if (r3 == 0) goto L_0x0037
            java.lang.Object[] r0 = (java.lang.Object[]) r0
            contentDeepToStringInternal$ArraysKt__ArraysKt(r0, r6, r7)
            goto L_0x011f
        L_0x0037:
            boolean r3 = r0 instanceof byte[]
            java.lang.String r4 = "toString(this)"
            if (r3 == 0) goto L_0x004b
            byte[] r0 = (byte[]) r0
            java.lang.String r0 = java.util.Arrays.toString(r0)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r4)
            r6.append(r0)
            goto L_0x011f
        L_0x004b:
            boolean r3 = r0 instanceof short[]
            if (r3 == 0) goto L_0x005d
            short[] r0 = (short[]) r0
            java.lang.String r0 = java.util.Arrays.toString(r0)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r4)
            r6.append(r0)
            goto L_0x011f
        L_0x005d:
            boolean r3 = r0 instanceof int[]
            if (r3 == 0) goto L_0x006f
            int[] r0 = (int[]) r0
            java.lang.String r0 = java.util.Arrays.toString(r0)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r4)
            r6.append(r0)
            goto L_0x011f
        L_0x006f:
            boolean r3 = r0 instanceof long[]
            if (r3 == 0) goto L_0x0081
            long[] r0 = (long[]) r0
            java.lang.String r0 = java.util.Arrays.toString(r0)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r4)
            r6.append(r0)
            goto L_0x011f
        L_0x0081:
            boolean r3 = r0 instanceof float[]
            if (r3 == 0) goto L_0x0093
            float[] r0 = (float[]) r0
            java.lang.String r0 = java.util.Arrays.toString(r0)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r4)
            r6.append(r0)
            goto L_0x011f
        L_0x0093:
            boolean r3 = r0 instanceof double[]
            if (r3 == 0) goto L_0x00a5
            double[] r0 = (double[]) r0
            java.lang.String r0 = java.util.Arrays.toString(r0)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r4)
            r6.append(r0)
            goto L_0x011f
        L_0x00a5:
            boolean r3 = r0 instanceof char[]
            if (r3 == 0) goto L_0x00b6
            char[] r0 = (char[]) r0
            java.lang.String r0 = java.util.Arrays.toString(r0)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r4)
            r6.append(r0)
            goto L_0x011f
        L_0x00b6:
            boolean r3 = r0 instanceof boolean[]
            if (r3 == 0) goto L_0x00c7
            boolean[] r0 = (boolean[]) r0
            java.lang.String r0 = java.util.Arrays.toString(r0)
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r4)
            r6.append(r0)
            goto L_0x011f
        L_0x00c7:
            boolean r3 = r0 instanceof kotlin.UByteArray
            r4 = 0
            if (r3 == 0) goto L_0x00dc
            kotlin.UByteArray r0 = (kotlin.UByteArray) r0
            if (r0 == 0) goto L_0x00d4
            byte[] r4 = r0.m179unboximpl()
        L_0x00d4:
            java.lang.String r0 = kotlin.collections.unsigned.UArraysKt.m651contentToString2csIQuQ(r4)
            r6.append(r0)
            goto L_0x011f
        L_0x00dc:
            boolean r3 = r0 instanceof kotlin.UShortArray
            if (r3 == 0) goto L_0x00f0
            kotlin.UShortArray r0 = (kotlin.UShortArray) r0
            if (r0 == 0) goto L_0x00e8
            short[] r4 = r0.m439unboximpl()
        L_0x00e8:
            java.lang.String r0 = kotlin.collections.unsigned.UArraysKt.m655contentToStringd6D3K8(r4)
            r6.append(r0)
            goto L_0x011f
        L_0x00f0:
            boolean r3 = r0 instanceof kotlin.UIntArray
            if (r3 == 0) goto L_0x0104
            kotlin.UIntArray r0 = (kotlin.UIntArray) r0
            if (r0 == 0) goto L_0x00fc
            int[] r4 = r0.m257unboximpl()
        L_0x00fc:
            java.lang.String r0 = kotlin.collections.unsigned.UArraysKt.m654contentToStringXUkPCBk(r4)
            r6.append(r0)
            goto L_0x011f
        L_0x0104:
            boolean r3 = r0 instanceof kotlin.ULongArray
            if (r3 == 0) goto L_0x0118
            kotlin.ULongArray r0 = (kotlin.ULongArray) r0
            if (r0 == 0) goto L_0x0110
            long[] r4 = r0.m335unboximpl()
        L_0x0110:
            java.lang.String r0 = kotlin.collections.unsigned.UArraysKt.m657contentToStringuLth9ew(r4)
            r6.append(r0)
            goto L_0x011f
        L_0x0118:
            java.lang.String r0 = r0.toString()
            r6.append(r0)
        L_0x011f:
            r0 = r2
            goto L_0x0016
        L_0x0122:
            r5 = 93
            r6.append(r5)
            int r5 = kotlin.collections.CollectionsKt.getLastIndex(r7)
            r7.remove(r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.collections.ArraysKt__ArraysKt.contentDeepToStringInternal$ArraysKt__ArraysKt(java.lang.Object[], java.lang.StringBuilder, java.util.List):void");
    }
}
