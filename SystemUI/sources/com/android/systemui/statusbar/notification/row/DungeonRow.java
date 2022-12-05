package com.android.systemui.statusbar.notification.row;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.icon.IconPack;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: DungeonRow.kt */
/* loaded from: classes.dex */
public final class DungeonRow extends LinearLayout {
    @Nullable
    private NotificationEntry entry;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public DungeonRow(@NotNull Context context, @NotNull AttributeSet attrs) {
        super(context, attrs);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(attrs, "attrs");
    }

    @Nullable
    public final NotificationEntry getEntry() {
        return this.entry;
    }

    public final void setEntry(@Nullable NotificationEntry notificationEntry) {
        this.entry = notificationEntry;
        update();
    }

    private final void update() {
        ExpandableNotificationRow row;
        StatusBarIconView statusBarIcon;
        View findViewById = findViewById(R$id.app_name);
        Objects.requireNonNull(findViewById, "null cannot be cast to non-null type android.widget.TextView");
        TextView textView = (TextView) findViewById;
        NotificationEntry entry = getEntry();
        StatusBarIcon statusBarIcon2 = null;
        textView.setText((entry == null || (row = entry.getRow()) == null) ? null : row.getAppName());
        View findViewById2 = findViewById(R$id.icon);
        Objects.requireNonNull(findViewById2, "null cannot be cast to non-null type com.android.systemui.statusbar.StatusBarIconView");
        StatusBarIconView statusBarIconView = (StatusBarIconView) findViewById2;
        NotificationEntry entry2 = getEntry();
        IconPack icons = entry2 == null ? null : entry2.getIcons();
        if (icons != null && (statusBarIcon = icons.getStatusBarIcon()) != null) {
            statusBarIcon2 = statusBarIcon.getStatusBarIcon();
        }
        statusBarIconView.set(statusBarIcon2);
    }
}
