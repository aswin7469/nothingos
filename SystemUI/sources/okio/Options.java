package okio;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.RandomAccess;

public final class Options extends AbstractList<ByteString> implements RandomAccess {
    final ByteString[] byteStrings;
    final int[] trie;

    private Options(ByteString[] byteStringArr, int[] iArr) {
        this.byteStrings = byteStringArr;
        this.trie = iArr;
    }

    /* renamed from: of */
    public static Options m1817of(ByteString... byteStringArr) {
        if (byteStringArr.length == 0) {
            return new Options(new ByteString[0], new int[]{0, -1});
        }
        ArrayList arrayList = new ArrayList(Arrays.asList(byteStringArr));
        Collections.sort(arrayList);
        ArrayList arrayList2 = new ArrayList();
        for (int i = 0; i < arrayList.size(); i++) {
            arrayList2.add(-1);
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            arrayList2.set(Collections.binarySearch(arrayList, byteStringArr[i2]), Integer.valueOf(i2));
        }
        if (((ByteString) arrayList.get(0)).size() != 0) {
            int i3 = 0;
            while (i3 < arrayList.size()) {
                ByteString byteString = (ByteString) arrayList.get(i3);
                int i4 = i3 + 1;
                int i5 = i4;
                while (i5 < arrayList.size()) {
                    ByteString byteString2 = (ByteString) arrayList.get(i5);
                    if (!byteString2.startsWith(byteString)) {
                        continue;
                        break;
                    } else if (byteString2.size() == byteString.size()) {
                        throw new IllegalArgumentException("duplicate option: " + byteString2);
                    } else if (((Integer) arrayList2.get(i5)).intValue() > ((Integer) arrayList2.get(i3)).intValue()) {
                        arrayList.remove(i5);
                        arrayList2.remove(i5);
                    } else {
                        i5++;
                    }
                }
                i3 = i4;
            }
            Buffer buffer = new Buffer();
            buildTrieRecursive(0, buffer, 0, arrayList, 0, arrayList.size(), arrayList2);
            int intCount = intCount(buffer);
            int[] iArr = new int[intCount];
            for (int i6 = 0; i6 < intCount; i6++) {
                iArr[i6] = buffer.readInt();
            }
            if (buffer.exhausted()) {
                return new Options((ByteString[]) byteStringArr.clone(), iArr);
            }
            throw new AssertionError();
        }
        throw new IllegalArgumentException("the empty byte string is not a supported option");
    }

    private static void buildTrieRecursive(long j, Buffer buffer, int i, List<ByteString> list, int i2, int i3, List<Integer> list2) {
        int i4;
        int i5;
        int i6;
        int i7;
        Buffer buffer2;
        int i8;
        Buffer buffer3 = buffer;
        int i9 = i;
        List<ByteString> list3 = list;
        int i10 = i2;
        int i11 = i3;
        List<Integer> list4 = list2;
        if (i10 < i11) {
            int i12 = i10;
            while (i12 < i11) {
                if (list3.get(i12).size() >= i9) {
                    i12++;
                } else {
                    throw new AssertionError();
                }
            }
            ByteString byteString = list.get(i2);
            ByteString byteString2 = list3.get(i11 - 1);
            if (i9 == byteString.size()) {
                int intValue = list4.get(i10).intValue();
                int i13 = i10 + 1;
                i4 = i13;
                i5 = intValue;
                byteString = list3.get(i13);
            } else {
                i5 = -1;
                i4 = i10;
            }
            if (byteString.getByte(i9) != byteString2.getByte(i9)) {
                int i14 = 1;
                for (int i15 = i4 + 1; i15 < i11; i15++) {
                    if (list3.get(i15 - 1).getByte(i9) != list3.get(i15).getByte(i9)) {
                        i14++;
                    }
                }
                long intCount = j + ((long) intCount(buffer)) + 2 + ((long) (i14 * 2));
                buffer3.writeInt(i14);
                buffer3.writeInt(i5);
                for (int i16 = i4; i16 < i11; i16++) {
                    byte b = list3.get(i16).getByte(i9);
                    if (i16 == i4 || b != list3.get(i16 - 1).getByte(i9)) {
                        buffer3.writeInt((int) b & 255);
                    }
                }
                Buffer buffer4 = new Buffer();
                int i17 = i4;
                while (i17 < i11) {
                    byte b2 = list3.get(i17).getByte(i9);
                    int i18 = i17 + 1;
                    int i19 = i18;
                    while (true) {
                        if (i19 >= i11) {
                            i7 = i11;
                            break;
                        } else if (b2 != list3.get(i19).getByte(i9)) {
                            i7 = i19;
                            break;
                        } else {
                            i19++;
                        }
                    }
                    if (i18 == i7 && i9 + 1 == list3.get(i17).size()) {
                        buffer3.writeInt(list4.get(i17).intValue());
                        i8 = i7;
                        buffer2 = buffer4;
                    } else {
                        buffer3.writeInt((int) ((((long) intCount(buffer4)) + intCount) * -1));
                        i8 = i7;
                        buffer2 = buffer4;
                        buildTrieRecursive(intCount, buffer4, i9 + 1, list, i17, i7, list2);
                    }
                    buffer4 = buffer2;
                    i17 = i8;
                }
                Buffer buffer5 = buffer4;
                buffer3.write(buffer5, buffer5.size());
                return;
            }
            int min = Math.min(byteString.size(), byteString2.size());
            int i20 = 0;
            int i21 = i9;
            while (i21 < min && byteString.getByte(i21) == byteString2.getByte(i21)) {
                i20++;
                i21++;
            }
            long intCount2 = 1 + j + ((long) intCount(buffer)) + 2 + ((long) i20);
            buffer3.writeInt(-i20);
            buffer3.writeInt(i5);
            int i22 = i9;
            while (true) {
                i6 = i9 + i20;
                if (i22 >= i6) {
                    break;
                }
                buffer3.writeInt((int) byteString.getByte(i22) & 255);
                i22++;
            }
            if (i4 + 1 != i11) {
                Buffer buffer6 = new Buffer();
                buffer3.writeInt((int) ((((long) intCount(buffer6)) + intCount2) * -1));
                buildTrieRecursive(intCount2, buffer6, i6, list, i4, i3, list2);
                buffer3.write(buffer6, buffer6.size());
            } else if (i6 == list3.get(i4).size()) {
                buffer3.writeInt(list4.get(i4).intValue());
            } else {
                throw new AssertionError();
            }
        } else {
            throw new AssertionError();
        }
    }

    public ByteString get(int i) {
        return this.byteStrings[i];
    }

    public final int size() {
        return this.byteStrings.length;
    }

    private static int intCount(Buffer buffer) {
        return (int) (buffer.size() / 4);
    }
}
