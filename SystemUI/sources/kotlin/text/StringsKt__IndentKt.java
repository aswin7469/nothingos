package kotlin.text;

import androidx.appcompat.R$styleable;
import java.util.ArrayList;
import java.util.List;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: Indent.kt */
/* loaded from: classes2.dex */
public class StringsKt__IndentKt extends StringsKt__AppendableKt {
    @NotNull
    public static String trimIndent(@NotNull String trimIndent) {
        Intrinsics.checkNotNullParameter(trimIndent, "$this$trimIndent");
        return replaceIndent(trimIndent, "");
    }

    @NotNull
    public static final String replaceIndent(@NotNull String replaceIndent, @NotNull String newIndent) {
        String str;
        String mo1949invoke;
        Intrinsics.checkNotNullParameter(replaceIndent, "$this$replaceIndent");
        Intrinsics.checkNotNullParameter(newIndent, "newIndent");
        List<String> lines = StringsKt__StringsKt.lines(replaceIndent);
        ArrayList<String> arrayList = new ArrayList();
        for (Object obj : lines) {
            if (!StringsKt__StringsJVMKt.isBlank((String) obj)) {
                arrayList.add(obj);
            }
        }
        ArrayList arrayList2 = new ArrayList(CollectionsKt.collectionSizeOrDefault(arrayList, 10));
        for (String str2 : arrayList) {
            arrayList2.add(Integer.valueOf(indentWidth$StringsKt__IndentKt(str2)));
        }
        Integer num = (Integer) CollectionsKt.minOrNull(arrayList2);
        int i = 0;
        int intValue = num != null ? num.intValue() : 0;
        int length = replaceIndent.length() + (newIndent.length() * lines.size());
        Function1<String, String> indentFunction$StringsKt__IndentKt = getIndentFunction$StringsKt__IndentKt(newIndent);
        int lastIndex = CollectionsKt.getLastIndex(lines);
        ArrayList arrayList3 = new ArrayList();
        for (Object obj2 : lines) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt.throwIndexOverflow();
            }
            String str3 = (String) obj2;
            if ((i == 0 || i == lastIndex) && StringsKt__StringsJVMKt.isBlank(str3)) {
                str = null;
            } else {
                String drop = StringsKt___StringsKt.drop(str3, intValue);
                if (drop != null && (mo1949invoke = indentFunction$StringsKt__IndentKt.mo1949invoke(drop)) != null) {
                    str3 = mo1949invoke;
                }
                str = str3;
            }
            if (str != null) {
                arrayList3.add(str);
            }
            i = i2;
        }
        String sb = ((StringBuilder) CollectionsKt.joinTo$default(arrayList3, new StringBuilder(length), "\n", null, null, 0, null, null, R$styleable.AppCompatTheme_windowMinWidthMajor, null)).toString();
        Intrinsics.checkNotNullExpressionValue(sb, "mapIndexedNotNull { inde…\"\\n\")\n        .toString()");
        return sb;
    }

    private static final Function1<String, String> getIndentFunction$StringsKt__IndentKt(String str) {
        return str.length() == 0 ? StringsKt__IndentKt$getIndentFunction$1.INSTANCE : new StringsKt__IndentKt$getIndentFunction$2(str);
    }

    private static final int indentWidth$StringsKt__IndentKt(String str) {
        int length = str.length();
        int i = 0;
        while (true) {
            if (i >= length) {
                i = -1;
                break;
            } else if (!CharsKt__CharJVMKt.isWhitespace(str.charAt(i))) {
                break;
            } else {
                i++;
            }
        }
        return i == -1 ? str.length() : i;
    }
}
