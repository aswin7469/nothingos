package com.google.zxing.oned.rss.expanded.decoders;

import android.icu.lang.UCharacter;
import com.android.p019wm.shell.common.DisplayImeController;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.statusbar.notification.stack.StackStateAnimator;
import com.google.zxing.NotFoundException;
import java.util.HashMap;
import java.util.Map;

final class FieldParser {
    private static final Map<String, DataLength> FOUR_DIGIT_DATA_LENGTH;
    private static final Map<String, DataLength> THREE_DIGIT_DATA_LENGTH;
    private static final Map<String, DataLength> THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH = new HashMap();
    private static final Map<String, DataLength> TWO_DIGIT_DATA_LENGTH;

    static {
        HashMap hashMap = new HashMap();
        TWO_DIGIT_DATA_LENGTH = hashMap;
        hashMap.put("00", DataLength.fixed(18));
        hashMap.put("01", DataLength.fixed(14));
        hashMap.put("02", DataLength.fixed(14));
        hashMap.put("10", DataLength.variable(20));
        hashMap.put("11", DataLength.fixed(6));
        hashMap.put("12", DataLength.fixed(6));
        hashMap.put("13", DataLength.fixed(6));
        hashMap.put("15", DataLength.fixed(6));
        hashMap.put("17", DataLength.fixed(6));
        hashMap.put("20", DataLength.fixed(2));
        hashMap.put("21", DataLength.variable(20));
        hashMap.put("22", DataLength.variable(29));
        hashMap.put("30", DataLength.variable(8));
        hashMap.put("37", DataLength.variable(8));
        for (int i = 90; i <= 99; i++) {
            TWO_DIGIT_DATA_LENGTH.put(String.valueOf(i), DataLength.variable(30));
        }
        HashMap hashMap2 = new HashMap();
        THREE_DIGIT_DATA_LENGTH = hashMap2;
        hashMap2.put("240", DataLength.variable(30));
        hashMap2.put("241", DataLength.variable(30));
        hashMap2.put("242", DataLength.variable(6));
        hashMap2.put("250", DataLength.variable(30));
        hashMap2.put("251", DataLength.variable(30));
        hashMap2.put("253", DataLength.variable(17));
        hashMap2.put("254", DataLength.variable(20));
        hashMap2.put("400", DataLength.variable(30));
        hashMap2.put("401", DataLength.variable(30));
        hashMap2.put("402", DataLength.fixed(17));
        hashMap2.put("403", DataLength.variable(30));
        hashMap2.put("410", DataLength.fixed(13));
        hashMap2.put("411", DataLength.fixed(13));
        hashMap2.put("412", DataLength.fixed(13));
        hashMap2.put("413", DataLength.fixed(13));
        hashMap2.put("414", DataLength.fixed(13));
        hashMap2.put("420", DataLength.variable(20));
        hashMap2.put("421", DataLength.variable(15));
        hashMap2.put("422", DataLength.fixed(3));
        hashMap2.put("423", DataLength.variable(15));
        hashMap2.put("424", DataLength.fixed(3));
        hashMap2.put("425", DataLength.fixed(3));
        hashMap2.put("426", DataLength.fixed(3));
        for (int i2 = UCharacter.UnicodeBlock.CYPRO_MINOAN_ID; i2 <= 316; i2++) {
            THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH.put(String.valueOf(i2), DataLength.fixed(6));
        }
        for (int i3 = 320; i3 <= 336; i3++) {
            THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH.put(String.valueOf(i3), DataLength.fixed(6));
        }
        for (int i4 = DisplayImeController.ANIMATION_DURATION_HIDE_MS; i4 <= 357; i4++) {
            THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH.put(String.valueOf(i4), DataLength.fixed(6));
        }
        for (int i5 = StackStateAnimator.ANIMATION_DURATION_STANDARD; i5 <= 369; i5++) {
            THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH.put(String.valueOf(i5), DataLength.fixed(6));
        }
        Map<String, DataLength> map = THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH;
        map.put("390", DataLength.variable(15));
        map.put("391", DataLength.variable(18));
        map.put("392", DataLength.variable(15));
        map.put("393", DataLength.variable(18));
        map.put("703", DataLength.variable(30));
        HashMap hashMap3 = new HashMap();
        FOUR_DIGIT_DATA_LENGTH = hashMap3;
        hashMap3.put("7001", DataLength.fixed(13));
        hashMap3.put("7002", DataLength.variable(30));
        hashMap3.put("7003", DataLength.fixed(10));
        hashMap3.put("8001", DataLength.fixed(14));
        hashMap3.put("8002", DataLength.variable(20));
        hashMap3.put("8003", DataLength.variable(30));
        hashMap3.put("8004", DataLength.variable(30));
        hashMap3.put("8005", DataLength.fixed(6));
        hashMap3.put("8006", DataLength.fixed(18));
        hashMap3.put("8007", DataLength.variable(30));
        hashMap3.put("8008", DataLength.variable(12));
        hashMap3.put("8018", DataLength.fixed(18));
        hashMap3.put("8020", DataLength.variable(25));
        hashMap3.put("8100", DataLength.fixed(6));
        hashMap3.put("8101", DataLength.fixed(10));
        hashMap3.put("8102", DataLength.fixed(2));
        hashMap3.put("8110", DataLength.variable(70));
        hashMap3.put("8200", DataLength.variable(70));
    }

    private FieldParser() {
    }

    static String parseFieldsInGeneralPurpose(String str) throws NotFoundException {
        if (str.isEmpty()) {
            return null;
        }
        if (str.length() >= 2) {
            DataLength dataLength = TWO_DIGIT_DATA_LENGTH.get(str.substring(0, 2));
            if (dataLength != null) {
                if (dataLength.variable) {
                    return processVariableAI(2, dataLength.length, str);
                }
                return processFixedAI(2, dataLength.length, str);
            } else if (str.length() >= 3) {
                String substring = str.substring(0, 3);
                DataLength dataLength2 = THREE_DIGIT_DATA_LENGTH.get(substring);
                if (dataLength2 != null) {
                    if (dataLength2.variable) {
                        return processVariableAI(3, dataLength2.length, str);
                    }
                    return processFixedAI(3, dataLength2.length, str);
                } else if (str.length() >= 4) {
                    DataLength dataLength3 = THREE_DIGIT_PLUS_DIGIT_DATA_LENGTH.get(substring);
                    if (dataLength3 == null) {
                        DataLength dataLength4 = FOUR_DIGIT_DATA_LENGTH.get(str.substring(0, 4));
                        if (dataLength4 == null) {
                            throw NotFoundException.getNotFoundInstance();
                        } else if (dataLength4.variable) {
                            return processVariableAI(4, dataLength4.length, str);
                        } else {
                            return processFixedAI(4, dataLength4.length, str);
                        }
                    } else if (dataLength3.variable) {
                        return processVariableAI(4, dataLength3.length, str);
                    } else {
                        return processFixedAI(4, dataLength3.length, str);
                    }
                } else {
                    throw NotFoundException.getNotFoundInstance();
                }
            } else {
                throw NotFoundException.getNotFoundInstance();
            }
        } else {
            throw NotFoundException.getNotFoundInstance();
        }
    }

    private static String processFixedAI(int i, int i2, String str) throws NotFoundException {
        if (str.length() >= i) {
            String substring = str.substring(0, i);
            int i3 = i2 + i;
            if (str.length() >= i3) {
                String substring2 = str.substring(i, i3);
                String str2 = NavigationBarInflaterView.KEY_CODE_START + substring + ')' + substring2;
                String parseFieldsInGeneralPurpose = parseFieldsInGeneralPurpose(str.substring(i3));
                return parseFieldsInGeneralPurpose == null ? str2 : str2 + parseFieldsInGeneralPurpose;
            }
            throw NotFoundException.getNotFoundInstance();
        }
        throw NotFoundException.getNotFoundInstance();
    }

    private static String processVariableAI(int i, int i2, String str) throws NotFoundException {
        String substring = str.substring(0, i);
        int min = Math.min(str.length(), i2 + i);
        String substring2 = str.substring(i, min);
        String str2 = NavigationBarInflaterView.KEY_CODE_START + substring + ')' + substring2;
        String parseFieldsInGeneralPurpose = parseFieldsInGeneralPurpose(str.substring(min));
        return parseFieldsInGeneralPurpose == null ? str2 : str2 + parseFieldsInGeneralPurpose;
    }

    private static final class DataLength {
        final int length;
        final boolean variable;

        private DataLength(boolean z, int i) {
            this.variable = z;
            this.length = i;
        }

        static DataLength fixed(int i) {
            return new DataLength(false, i);
        }

        static DataLength variable(int i) {
            return new DataLength(true, i);
        }
    }
}
