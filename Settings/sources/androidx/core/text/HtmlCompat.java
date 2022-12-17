package androidx.core.text;

import android.annotation.SuppressLint;
import android.text.Html;
import android.text.Spanned;

@SuppressLint({"InlinedApi"})
public final class HtmlCompat {
    public static Spanned fromHtml(String str, int i) {
        return Api24Impl.fromHtml(str, i);
    }

    public static String toHtml(Spanned spanned, int i) {
        return Api24Impl.toHtml(spanned, i);
    }

    static class Api24Impl {
        static Spanned fromHtml(String str, int i) {
            return Html.fromHtml(str, i);
        }

        static String toHtml(Spanned spanned, int i) {
            return Html.toHtml(spanned, i);
        }
    }
}
