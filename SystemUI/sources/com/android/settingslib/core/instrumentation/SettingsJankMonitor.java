package com.android.settingslib.core.instrumentation;

import android.view.View;
import androidx.preference.Preference;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.SwitchPreference;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.jank.InteractionJankMonitor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import kotlin.Metadata;
import kotlin.jvm.JvmStatic;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\bÆ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u0018\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000e2\u0006\u0010\u000f\u001a\u00020\u0010H\u0007J\u001a\u0010\u0011\u001a\u00020\f2\b\u0010\u0012\u001a\u0004\u0018\u00010\u00132\u0006\u0010\u0014\u001a\u00020\u0015H\u0007R\u0016\u0010\u0003\u001a\u00020\u00048\u0006XT¢\u0006\b\n\u0000\u0012\u0004\b\u0005\u0010\u0002R\u0016\u0010\u0006\u001a\n \b*\u0004\u0018\u00010\u00070\u0007X\u0004¢\u0006\u0002\n\u0000R\u0016\u0010\t\u001a\n \b*\u0004\u0018\u00010\n0\nX\u0004¢\u0006\u0002\n\u0000¨\u0006\u0016"}, mo65043d2 = {"Lcom/android/settingslib/core/instrumentation/SettingsJankMonitor;", "", "()V", "MONITORED_ANIMATION_DURATION_MS", "", "getMONITORED_ANIMATION_DURATION_MS$annotations", "jankMonitor", "Lcom/android/internal/jank/InteractionJankMonitor;", "kotlin.jvm.PlatformType", "scheduledExecutorService", "Ljava/util/concurrent/ScheduledExecutorService;", "detectSwitchPreferenceClickJank", "", "recyclerView", "Landroidx/recyclerview/widget/RecyclerView;", "preference", "Landroidx/preference/SwitchPreference;", "detectToggleJank", "tag", "", "view", "Landroid/view/View;", "SettingsLib_release"}, mo65044k = 1, mo65045mv = {1, 7, 1}, mo65047xi = 48)
/* compiled from: SettingsJankMonitor.kt */
public final class SettingsJankMonitor {
    public static final SettingsJankMonitor INSTANCE = new SettingsJankMonitor();
    public static final long MONITORED_ANIMATION_DURATION_MS = 300;
    private static final InteractionJankMonitor jankMonitor = InteractionJankMonitor.getInstance();
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public static /* synthetic */ void getMONITORED_ANIMATION_DURATION_MS$annotations() {
    }

    private SettingsJankMonitor() {
    }

    @JvmStatic
    public static final void detectSwitchPreferenceClickJank(RecyclerView recyclerView, SwitchPreference switchPreference) {
        RecyclerView.ViewHolder findViewHolderForAdapterPosition;
        Intrinsics.checkNotNullParameter(recyclerView, "recyclerView");
        Intrinsics.checkNotNullParameter(switchPreference, "preference");
        RecyclerView.Adapter adapter = recyclerView.getAdapter();
        PreferenceGroupAdapter preferenceGroupAdapter = adapter instanceof PreferenceGroupAdapter ? (PreferenceGroupAdapter) adapter : null;
        if (preferenceGroupAdapter != null && (findViewHolderForAdapterPosition = recyclerView.findViewHolderForAdapterPosition(preferenceGroupAdapter.getPreferenceAdapterPosition((Preference) switchPreference))) != null) {
            String key = switchPreference.getKey();
            View view = findViewHolderForAdapterPosition.itemView;
            Intrinsics.checkNotNullExpressionValue(view, "viewHolder.itemView");
            detectToggleJank(key, view);
        }
    }

    @JvmStatic
    public static final void detectToggleJank(String str, View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        InteractionJankMonitor.Configuration.Builder withView = InteractionJankMonitor.Configuration.Builder.withView(57, view);
        if (str != null) {
            withView.setTag(str);
        }
        if (jankMonitor.begin(withView)) {
            scheduledExecutorService.schedule((Runnable) new SettingsJankMonitor$$ExternalSyntheticLambda0(), 300, TimeUnit.MILLISECONDS);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: detectToggleJank$lambda-0  reason: not valid java name */
    public static final void m2506detectToggleJank$lambda0() {
        jankMonitor.end(57);
    }
}
