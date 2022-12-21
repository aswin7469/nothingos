package com.android.systemui.statusbar.phone.fragment;

import android.content.res.Resources;
import com.android.systemui.C1893R;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kotlin.Metadata;
import kotlin.collections.ArraysKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u001c\u0010\u0000\u001a\b\u0012\u0004\u0012\u00020\u00020\u00012\u0006\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006¨\u0006\u0007"}, mo64987d2 = {"getStatusBarIconBlocklist", "", "", "res", "Landroid/content/res/Resources;", "settings", "Lcom/android/systemui/util/settings/SecureSettings;", "SystemUI_nothingRelease"}, mo64988k = 2, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: StatusBarIconBlocklist.kt */
public final class StatusBarIconBlocklistKt {
    public static final List<String> getStatusBarIconBlocklist(Resources resources, SecureSettings secureSettings) {
        Intrinsics.checkNotNullParameter(resources, "res");
        Intrinsics.checkNotNullParameter(secureSettings, "settings");
        String[] stringArray = resources.getStringArray(C1893R.array.config_collapsed_statusbar_icon_blocklist);
        Intrinsics.checkNotNullExpressionValue(stringArray, "res.getStringArray(\n    …statusbar_icon_blocklist)");
        List list = ArraysKt.toList((T[]) (Object[]) stringArray);
        String string = resources.getString(17041585);
        Intrinsics.checkNotNullExpressionValue(string, "res.getString(R.string.status_bar_volume)");
        boolean z = secureSettings.getIntForUser("status_bar_show_vibrate_icon", 0, -2) == 0;
        Collection arrayList = new ArrayList();
        for (Object next : list) {
            if (!((String) next).equals(string) || z) {
                arrayList.add(next);
            }
        }
        return (List) arrayList;
    }
}
