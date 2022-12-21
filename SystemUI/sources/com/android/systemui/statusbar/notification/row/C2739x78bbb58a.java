package com.android.systemui.statusbar.notification.row;

import android.app.NotificationChannel;
import java.util.Comparator;
import kotlin.Metadata;
import kotlin.comparisons.ComparisonsKt;

@Metadata(mo64986d1 = {"\u0000\n\n\u0000\n\u0002\u0010\b\n\u0002\b\u0007\u0010\u0000\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u00022\u000e\u0010\u0003\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u00022\u000e\u0010\u0005\u001a\n \u0004*\u0004\u0018\u0001H\u0002H\u0002H\n¢\u0006\u0004\b\u0006\u0010\u0007¨\u0006\b"}, mo64987d2 = {"<anonymous>", "", "T", "a", "kotlin.jvm.PlatformType", "b", "compare", "(Ljava/lang/Object;Ljava/lang/Object;)I", "kotlin/comparisons/ComparisonsKt__ComparisonsKt$compareBy$2"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* renamed from: com.android.systemui.statusbar.notification.row.ChannelEditorDialogController$getDisplayableChannels$$inlined$compareBy$1 */
/* compiled from: Comparisons.kt */
public final class C2739x78bbb58a<T> implements Comparator {
    public final int compare(T t, T t2) {
        String str;
        String str2;
        NotificationChannel notificationChannel = (NotificationChannel) t;
        CharSequence name = notificationChannel.getName();
        if (name == null || (str = name.toString()) == null) {
            str = notificationChannel.getId();
        }
        Comparable comparable = str;
        NotificationChannel notificationChannel2 = (NotificationChannel) t2;
        CharSequence name2 = notificationChannel2.getName();
        if (name2 == null || (str2 = name2.toString()) == null) {
            str2 = notificationChannel2.getId();
        }
        return ComparisonsKt.compareValues(comparable, str2);
    }
}
