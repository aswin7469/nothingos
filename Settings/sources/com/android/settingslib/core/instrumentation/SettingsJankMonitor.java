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
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/* compiled from: SettingsJankMonitor.kt */
public final class SettingsJankMonitor {
    @NotNull
    public static final SettingsJankMonitor INSTANCE = new SettingsJankMonitor();
    /* access modifiers changed from: private */
    public static final InteractionJankMonitor jankMonitor = InteractionJankMonitor.getInstance();
    private static final ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    public static /* synthetic */ void getMONITORED_ANIMATION_DURATION_MS$annotations() {
    }

    private SettingsJankMonitor() {
    }

    public static final void detectSwitchPreferenceClickJank(@NotNull RecyclerView recyclerView, @NotNull SwitchPreference switchPreference) {
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

    public static final void detectToggleJank(@Nullable String str, @NotNull View view) {
        Intrinsics.checkNotNullParameter(view, "view");
        InteractionJankMonitor.Configuration.Builder withView = InteractionJankMonitor.Configuration.Builder.withView(57, view);
        if (str != null) {
            withView.setTag(str);
        }
        if (jankMonitor.begin(withView)) {
            scheduledExecutorService.schedule(SettingsJankMonitor$detectToggleJank$1.INSTANCE, 300, TimeUnit.MILLISECONDS);
        }
    }
}
