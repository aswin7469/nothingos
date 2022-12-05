package com.android.systemui.statusbar.notification.stack;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.statusbar.NotificationRemoveInterceptor;
import com.android.systemui.statusbar.notification.ForegroundServiceDismissalFeatureController;
import com.android.systemui.statusbar.notification.NotificationEntryListener;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.DungeonRow;
import com.android.systemui.util.Assert;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.comparisons.ComparisonsKt__ComparisonsKt;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* compiled from: ForegroundServiceSectionController.kt */
/* loaded from: classes.dex */
public final class ForegroundServiceSectionController {
    @NotNull
    private final String TAG = "FgsSectionController";
    @NotNull
    private final Set<NotificationEntry> entries = new LinkedHashSet();
    @Nullable
    private View entriesView;
    @NotNull
    private final NotificationEntryManager entryManager;
    @NotNull
    private final ForegroundServiceDismissalFeatureController featureController;

    public ForegroundServiceSectionController(@NotNull NotificationEntryManager entryManager, @NotNull ForegroundServiceDismissalFeatureController featureController) {
        Intrinsics.checkNotNullParameter(entryManager, "entryManager");
        Intrinsics.checkNotNullParameter(featureController, "featureController");
        this.entryManager = entryManager;
        this.featureController = featureController;
        if (featureController.isForegroundServiceDismissalEnabled()) {
            entryManager.addNotificationRemoveInterceptor(new NotificationRemoveInterceptor() { // from class: com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController.1
                @Override // com.android.systemui.statusbar.NotificationRemoveInterceptor
                public final boolean onNotificationRemoveRequested(@NotNull String p0, @Nullable NotificationEntry notificationEntry, int i) {
                    Intrinsics.checkNotNullParameter(p0, "p0");
                    return ForegroundServiceSectionController.this.shouldInterceptRemoval(p0, notificationEntry, i);
                }
            });
            entryManager.addNotificationEntryListener(new NotificationEntryListener() { // from class: com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController.2
                @Override // com.android.systemui.statusbar.notification.NotificationEntryListener
                public void onPostEntryUpdated(@NotNull NotificationEntry entry) {
                    Intrinsics.checkNotNullParameter(entry, "entry");
                    if (ForegroundServiceSectionController.this.entries.contains(entry)) {
                        ForegroundServiceSectionController.this.removeEntry(entry);
                        ForegroundServiceSectionController.this.addEntry(entry);
                        ForegroundServiceSectionController.this.update();
                    }
                }
            });
        }
    }

    @NotNull
    public final NotificationEntryManager getEntryManager() {
        return this.entryManager;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean shouldInterceptRemoval(String str, NotificationEntry notificationEntry, int i) {
        Assert.isMainThread();
        boolean z = i == 3;
        boolean z2 = i == 2 || i == 1;
        if (i != 8) {
        }
        boolean z3 = i == 12;
        if (notificationEntry == null) {
            return false;
        }
        if (z2 && !notificationEntry.getSbn().isClearable()) {
            if (!hasEntry(notificationEntry)) {
                addEntry(notificationEntry);
                update();
            }
            this.entryManager.updateNotifications("FgsSectionController.onNotificationRemoveRequested");
            return true;
        } else if ((z || z3) && !notificationEntry.getSbn().isClearable()) {
            return true;
        } else {
            if (hasEntry(notificationEntry)) {
                removeEntry(notificationEntry);
                update();
            }
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void removeEntry(NotificationEntry notificationEntry) {
        Assert.isMainThread();
        this.entries.remove(notificationEntry);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void addEntry(NotificationEntry notificationEntry) {
        Assert.isMainThread();
        this.entries.add(notificationEntry);
    }

    public final boolean hasEntry(@NotNull NotificationEntry entry) {
        Intrinsics.checkNotNullParameter(entry, "entry");
        Assert.isMainThread();
        return this.entries.contains(entry);
    }

    @NotNull
    public final View createView(@NotNull LayoutInflater li) {
        Intrinsics.checkNotNullParameter(li, "li");
        View inflate = li.inflate(R$layout.foreground_service_dungeon, (ViewGroup) null);
        this.entriesView = inflate;
        Intrinsics.checkNotNull(inflate);
        inflate.setVisibility(8);
        View view = this.entriesView;
        Intrinsics.checkNotNull(view);
        return view;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void update() {
        List<NotificationEntry> sortedWith;
        Assert.isMainThread();
        View view = this.entriesView;
        if (view == null) {
            throw new IllegalStateException("ForegroundServiceSectionController is trying to show dismissed fgs notifications without having been initialized!");
        }
        Intrinsics.checkNotNull(view);
        View findViewById = view.findViewById(R$id.entry_list);
        Objects.requireNonNull(findViewById, "null cannot be cast to non-null type android.widget.LinearLayout");
        LinearLayout linearLayout = (LinearLayout) findViewById;
        linearLayout.removeAllViews();
        sortedWith = CollectionsKt___CollectionsKt.sortedWith(this.entries, new Comparator<T>() { // from class: com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController$update$lambda-2$$inlined$sortedBy$1
            @Override // java.util.Comparator
            public final int compare(T t, T t2) {
                int compareValues;
                compareValues = ComparisonsKt__ComparisonsKt.compareValues(Integer.valueOf(((NotificationEntry) t).getRanking().getRank()), Integer.valueOf(((NotificationEntry) t2).getRanking().getRank()));
                return compareValues;
            }
        });
        for (final NotificationEntry notificationEntry : sortedWith) {
            View inflate = LayoutInflater.from(linearLayout.getContext()).inflate(R$layout.foreground_service_dungeon_row, (ViewGroup) null);
            Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.android.systemui.statusbar.notification.row.DungeonRow");
            final DungeonRow dungeonRow = (DungeonRow) inflate;
            dungeonRow.setEntry(notificationEntry);
            dungeonRow.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.stack.ForegroundServiceSectionController$update$1$2$1
                @Override // android.view.View.OnClickListener
                public final void onClick(View view2) {
                    ForegroundServiceSectionController foregroundServiceSectionController = ForegroundServiceSectionController.this;
                    NotificationEntry entry = dungeonRow.getEntry();
                    Intrinsics.checkNotNull(entry);
                    foregroundServiceSectionController.removeEntry(entry);
                    ForegroundServiceSectionController.this.update();
                    notificationEntry.getRow().unDismiss();
                    notificationEntry.getRow().resetTranslation();
                    ForegroundServiceSectionController.this.getEntryManager().updateNotifications("ForegroundServiceSectionController.onClick");
                }
            });
            linearLayout.addView(dungeonRow);
        }
        if (this.entries.isEmpty()) {
            View view2 = this.entriesView;
            if (view2 == null) {
                return;
            }
            view2.setVisibility(8);
            return;
        }
        View view3 = this.entriesView;
        if (view3 == null) {
            return;
        }
        view3.setVisibility(0);
    }
}
