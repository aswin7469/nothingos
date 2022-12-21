package com.google.zxing.client.result;

import com.android.settingslib.accessibility.AccessibilityUtils;
import com.google.zxing.Result;
import java.util.ArrayList;

public final class AddressBookAUResultParser extends ResultParser {
    public AddressBookParsedResult parse(Result result) {
        String massagedText = getMassagedText(result);
        String[] strArr = null;
        if (!massagedText.contains("MEMORY") || !massagedText.contains("\r\n")) {
            return null;
        }
        String matchSinglePrefixedField = matchSinglePrefixedField("NAME1:", massagedText, 13, true);
        String matchSinglePrefixedField2 = matchSinglePrefixedField("NAME2:", massagedText, 13, true);
        String[] matchMultipleValuePrefix = matchMultipleValuePrefix("TEL", massagedText);
        String[] matchMultipleValuePrefix2 = matchMultipleValuePrefix("MAIL", massagedText);
        String matchSinglePrefixedField3 = matchSinglePrefixedField("MEMORY:", massagedText, 13, false);
        String matchSinglePrefixedField4 = matchSinglePrefixedField("ADD:", massagedText, 13, true);
        if (matchSinglePrefixedField4 != null) {
            strArr = new String[]{matchSinglePrefixedField4};
        }
        return new AddressBookParsedResult(maybeWrap(matchSinglePrefixedField), (String[]) null, matchSinglePrefixedField2, matchMultipleValuePrefix, (String[]) null, matchMultipleValuePrefix2, (String[]) null, (String) null, matchSinglePrefixedField3, strArr, (String[]) null, (String) null, (String) null, (String) null, (String[]) null, (String[]) null);
    }

    private static String[] matchMultipleValuePrefix(String str, String str2) {
        String matchSinglePrefixedField;
        ArrayList arrayList = null;
        int i = 1;
        while (i <= 3 && (matchSinglePrefixedField = matchSinglePrefixedField(str + i + AccessibilityUtils.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR, str2, 13, true)) != null) {
            if (arrayList == null) {
                arrayList = new ArrayList(3);
            }
            arrayList.add(matchSinglePrefixedField);
            i++;
        }
        if (arrayList == null) {
            return null;
        }
        return (String[]) arrayList.toArray(EMPTY_STR_ARRAY);
    }
}
