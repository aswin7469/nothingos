package com.android.systemui.statusbar.notification.collection.render;

import android.os.Trace;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderEntryListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderGroupListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnAfterRenderListListener;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000N\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u0007\b\u0007¢\u0006\u0002\u0010\u0002J\u000e\u0010\f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0005J\u000e\u0010\u000f\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\u0007J\u000e\u0010\u0010\u001a\u00020\r2\u0006\u0010\u000e\u001a\u00020\tJ\u000e\u0010\u0011\u001a\u00020\r2\u0006\u0010\u0012\u001a\u00020\u0013J\u001e\u0010\u0014\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000b2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\u001e\u0010\u0018\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000b2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\u001e\u0010\u0019\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000b2\f\u0010\u0015\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\u0016\u0010\u001a\u001a\u00020\r2\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00170\u0016H\u0002J\u000e\u0010\u001c\u001a\u00020\r2\u0006\u0010\u001d\u001a\u00020\u000bJ'\u0010\u001e\u001a\u00020\r*\b\u0012\u0004\u0012\u00020\u00170\u00162\u0012\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020!\u0012\u0004\u0012\u00020\r0 H\bR\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\u00070\u0004X\u0004¢\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\t0\u0004X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\n\u001a\u0004\u0018\u00010\u000bX\u000e¢\u0006\u0002\n\u0000¨\u0006\""}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/RenderStageManager;", "", "()V", "onAfterRenderEntryListeners", "", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/OnAfterRenderEntryListener;", "onAfterRenderGroupListeners", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/OnAfterRenderGroupListener;", "onAfterRenderListListeners", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/OnAfterRenderListListener;", "viewRenderer", "Lcom/android/systemui/statusbar/notification/collection/render/NotifViewRenderer;", "addOnAfterRenderEntryListener", "", "listener", "addOnAfterRenderGroupListener", "addOnAfterRenderListListener", "attach", "listBuilder", "Lcom/android/systemui/statusbar/notification/collection/ShadeListBuilder;", "dispatchOnAfterRenderEntries", "entries", "", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "dispatchOnAfterRenderGroups", "dispatchOnAfterRenderList", "onRenderList", "notifList", "setViewRenderer", "renderer", "forEachNotificationEntry", "action", "Lkotlin/Function1;", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: RenderStageManager.kt */
public final class RenderStageManager {
    private final List<OnAfterRenderEntryListener> onAfterRenderEntryListeners = new ArrayList();
    private final List<OnAfterRenderGroupListener> onAfterRenderGroupListeners = new ArrayList();
    private final List<OnAfterRenderListListener> onAfterRenderListListeners = new ArrayList();
    private NotifViewRenderer viewRenderer;

    public final void attach(ShadeListBuilder shadeListBuilder) {
        Intrinsics.checkNotNullParameter(shadeListBuilder, "listBuilder");
        shadeListBuilder.setOnRenderListListener(new RenderStageManager$$ExternalSyntheticLambda0(this));
    }

    public final void setViewRenderer(NotifViewRenderer notifViewRenderer) {
        Intrinsics.checkNotNullParameter(notifViewRenderer, "renderer");
        this.viewRenderer = notifViewRenderer;
    }

    public final void addOnAfterRenderListListener(OnAfterRenderListListener onAfterRenderListListener) {
        Intrinsics.checkNotNullParameter(onAfterRenderListListener, "listener");
        this.onAfterRenderListListeners.add(onAfterRenderListListener);
    }

    public final void addOnAfterRenderGroupListener(OnAfterRenderGroupListener onAfterRenderGroupListener) {
        Intrinsics.checkNotNullParameter(onAfterRenderGroupListener, "listener");
        this.onAfterRenderGroupListeners.add(onAfterRenderGroupListener);
    }

    public final void addOnAfterRenderEntryListener(OnAfterRenderEntryListener onAfterRenderEntryListener) {
        Intrinsics.checkNotNullParameter(onAfterRenderEntryListener, "listener");
        this.onAfterRenderEntryListeners.add(onAfterRenderEntryListener);
    }

    private final void forEachNotificationEntry(List<? extends ListEntry> list, Function1<? super NotificationEntry, Unit> function1) {
        for (ListEntry listEntry : list) {
            if (listEntry instanceof NotificationEntry) {
                function1.invoke(listEntry);
            } else if (listEntry instanceof GroupEntry) {
                GroupEntry groupEntry = (GroupEntry) listEntry;
                NotificationEntry summary = groupEntry.getSummary();
                if (summary != null) {
                    Intrinsics.checkNotNullExpressionValue(summary, "<get-requireSummary>");
                    function1.invoke(summary);
                    List<NotificationEntry> children = groupEntry.getChildren();
                    Intrinsics.checkNotNullExpressionValue(children, "entry.children");
                    for (Object invoke : children) {
                        function1.invoke(invoke);
                    }
                } else {
                    throw new IllegalStateException(("No Summary: " + groupEntry).toString());
                }
            } else {
                throw new IllegalStateException(("Unhandled entry: " + listEntry).toString());
            }
        }
    }

    /* access modifiers changed from: private */
    public final void onRenderList(List<? extends ListEntry> list) {
        Trace.beginSection("RenderStageManager.onRenderList");
        try {
            NotifViewRenderer notifViewRenderer = this.viewRenderer;
            if (notifViewRenderer != null) {
                notifViewRenderer.onRenderList(list);
                dispatchOnAfterRenderList(notifViewRenderer, list);
                dispatchOnAfterRenderGroups(notifViewRenderer, list);
                dispatchOnAfterRenderEntries(notifViewRenderer, list);
                notifViewRenderer.onDispatchComplete();
                Unit unit = Unit.INSTANCE;
                Trace.endSection();
            }
        } finally {
            Trace.endSection();
        }
    }

    private final void dispatchOnAfterRenderList(NotifViewRenderer notifViewRenderer, List<? extends ListEntry> list) {
        Trace.beginSection("RenderStageManager.dispatchOnAfterRenderList");
        try {
            NotifStackController stackController = notifViewRenderer.getStackController();
            for (OnAfterRenderListListener onAfterRenderList : this.onAfterRenderListListeners) {
                onAfterRenderList.onAfterRenderList(list, stackController);
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }

    private final void dispatchOnAfterRenderGroups(NotifViewRenderer notifViewRenderer, List<? extends ListEntry> list) {
        Trace.beginSection("RenderStageManager.dispatchOnAfterRenderGroups");
        try {
            if (!this.onAfterRenderGroupListeners.isEmpty()) {
                Sequence<GroupEntry> filter = SequencesKt.filter(CollectionsKt.asSequence(list), C2714x81935fa2.INSTANCE);
                Intrinsics.checkNotNull(filter, "null cannot be cast to non-null type kotlin.sequences.Sequence<R of kotlin.sequences.SequencesKt___SequencesKt.filterIsInstance>");
                for (GroupEntry groupEntry : filter) {
                    NotifGroupController groupController = notifViewRenderer.getGroupController(groupEntry);
                    for (OnAfterRenderGroupListener onAfterRenderGroup : this.onAfterRenderGroupListeners) {
                        onAfterRenderGroup.onAfterRenderGroup(groupEntry, groupController);
                    }
                }
                Unit unit = Unit.INSTANCE;
                Trace.endSection();
            }
        } finally {
            Trace.endSection();
        }
    }

    private final void dispatchOnAfterRenderEntries(NotifViewRenderer notifViewRenderer, List<? extends ListEntry> list) {
        Trace.beginSection("RenderStageManager.dispatchOnAfterRenderEntries");
        try {
            if (!this.onAfterRenderEntryListeners.isEmpty()) {
                for (ListEntry listEntry : list) {
                    if (listEntry instanceof NotificationEntry) {
                        NotificationEntry notificationEntry = (NotificationEntry) listEntry;
                        NotifRowController rowController = notifViewRenderer.getRowController(notificationEntry);
                        for (OnAfterRenderEntryListener onAfterRenderEntry : this.onAfterRenderEntryListeners) {
                            onAfterRenderEntry.onAfterRenderEntry(notificationEntry, rowController);
                        }
                    } else if (listEntry instanceof GroupEntry) {
                        GroupEntry groupEntry = (GroupEntry) listEntry;
                        NotificationEntry summary = groupEntry.getSummary();
                        if (summary != null) {
                            Intrinsics.checkNotNullExpressionValue(summary, "<get-requireSummary>");
                            NotifRowController rowController2 = notifViewRenderer.getRowController(summary);
                            for (OnAfterRenderEntryListener onAfterRenderEntry2 : this.onAfterRenderEntryListeners) {
                                onAfterRenderEntry2.onAfterRenderEntry(summary, rowController2);
                            }
                            List<NotificationEntry> children = ((GroupEntry) listEntry).getChildren();
                            Intrinsics.checkNotNullExpressionValue(children, "entry.children");
                            for (NotificationEntry notificationEntry2 : children) {
                                NotifRowController rowController3 = notifViewRenderer.getRowController(notificationEntry2);
                                for (OnAfterRenderEntryListener onAfterRenderEntry3 : this.onAfterRenderEntryListeners) {
                                    onAfterRenderEntry3.onAfterRenderEntry(notificationEntry2, rowController3);
                                }
                            }
                        } else {
                            throw new IllegalStateException(("No Summary: " + groupEntry).toString());
                        }
                    } else {
                        throw new IllegalStateException(("Unhandled entry: " + listEntry).toString());
                    }
                }
                Unit unit = Unit.INSTANCE;
                Trace.endSection();
            }
        } finally {
            Trace.endSection();
        }
    }
}
