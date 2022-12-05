package androidx.core.text;

import android.annotation.SuppressLint;
import android.os.Build;
import android.text.Html;
import android.text.Spanned;
@SuppressLint({"InlinedApi"})
/* loaded from: classes.dex */
public final class HtmlCompat {
    public static Spanned fromHtml(String source, int flags) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Html.fromHtml(source, flags);
        }
        return Html.fromHtml(source);
    }

    public static String toHtml(Spanned text, int options) {
        if (Build.VERSION.SDK_INT >= 24) {
            return Html.toHtml(text, options);
        }
        return Html.toHtml(text);
    }
}
