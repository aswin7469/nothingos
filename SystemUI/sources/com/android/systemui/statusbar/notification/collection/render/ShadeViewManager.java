package com.android.systemui.statusbar.notification.collection.render;

import android.content.Context;
import android.view.View;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.ShadeListBuilder;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.NotificationIconAreaController;
import java.util.List;
import kotlin.Pair;
import kotlin.collections.CollectionsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt;
import org.jetbrains.annotations.NotNull;
/* compiled from: ShadeViewManager.kt */
/* loaded from: classes.dex */
public final class ShadeViewManager {
    @NotNull
    private final NotificationIconAreaController notificationIconAreaController;
    @NotNull
    private final RootNodeController rootController;
    @NotNull
    private final NotifViewBarn viewBarn;
    @NotNull
    private final ShadeViewDiffer viewDiffer;

    public ShadeViewManager(@NotNull Context context, @NotNull NotificationListContainer listContainer, @NotNull ShadeViewDifferLogger logger, @NotNull NotifViewBarn viewBarn, @NotNull NotificationIconAreaController notificationIconAreaController) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(listContainer, "listContainer");
        Intrinsics.checkNotNullParameter(logger, "logger");
        Intrinsics.checkNotNullParameter(viewBarn, "viewBarn");
        Intrinsics.checkNotNullParameter(notificationIconAreaController, "notificationIconAreaController");
        this.viewBarn = viewBarn;
        this.notificationIconAreaController = notificationIconAreaController;
        RootNodeController rootNodeController = new RootNodeController(listContainer, new View(context));
        this.rootController = rootNodeController;
        this.viewDiffer = new ShadeViewDiffer(rootNodeController, logger);
    }

    public final void attach(@NotNull ShadeListBuilder listBuilder) {
        Intrinsics.checkNotNullParameter(listBuilder, "listBuilder");
        listBuilder.setOnRenderListListener(new ShadeListBuilder.OnRenderListListener() { // from class: com.android.systemui.statusbar.notification.collection.render.ShadeViewManager$attach$1
            @Override // com.android.systemui.statusbar.notification.collection.ShadeListBuilder.OnRenderListListener
            public final void onRenderList(@NotNull List<? extends ListEntry> p0) {
                Intrinsics.checkNotNullParameter(p0, "p0");
                ShadeViewManager.this.onNewNotifTree(p0);
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final void onNewNotifTree(List<? extends ListEntry> list) {
        this.viewDiffer.applySpec(buildTree(list));
    }

    private final NodeSpec buildTree(List<? extends ListEntry> list) {
        Sequence asSequence;
        NodeController headerController;
        NodeController headerController2;
        NodeSpecImpl nodeSpecImpl = new NodeSpecImpl(null, this.rootController);
        ListEntry listEntry = (ListEntry) CollectionsKt.firstOrNull(list);
        NotifSection section = listEntry == null ? null : listEntry.getSection();
        if (section != null && (headerController2 = section.getHeaderController()) != null) {
            nodeSpecImpl.getChildren().add(new NodeSpecImpl(nodeSpecImpl, headerController2));
        }
        asSequence = CollectionsKt___CollectionsKt.asSequence(list);
        for (Pair pair : SequencesKt.zipWithNext(asSequence)) {
            ListEntry listEntry2 = (ListEntry) pair.component2();
            NotifSection section2 = listEntry2.getSection();
            if (!(!Intrinsics.areEqual(section2, ((ListEntry) pair.component1()).getSection()))) {
                section2 = null;
            }
            if (section2 != null && (headerController = section2.getHeaderController()) != null) {
                nodeSpecImpl.getChildren().add(new NodeSpecImpl(nodeSpecImpl, headerController));
            }
            nodeSpecImpl.getChildren().add(buildNotifNode(listEntry2, nodeSpecImpl));
        }
        this.notificationIconAreaController.updateNotificationIcons(list);
        return nodeSpecImpl;
    }

    private final NodeSpec buildNotifNode(ListEntry listEntry, NodeSpec nodeSpec) {
        if (listEntry instanceof NotificationEntry) {
            return new NodeSpecImpl(nodeSpec, this.viewBarn.requireView(listEntry));
        }
        if (!(listEntry instanceof GroupEntry)) {
            throw new RuntimeException(Intrinsics.stringPlus("Unexpected entry: ", listEntry));
        }
        NotifViewBarn notifViewBarn = this.viewBarn;
        GroupEntry groupEntry = (GroupEntry) listEntry;
        NotificationEntry summary = groupEntry.getSummary();
        if (summary == null) {
            throw new IllegalStateException("Required value was null.".toString());
        }
        NodeSpecImpl nodeSpecImpl = new NodeSpecImpl(nodeSpec, notifViewBarn.requireView(summary));
        List<NotificationEntry> children = groupEntry.getChildren();
        Intrinsics.checkNotNullExpressionValue(children, "entry.children");
        for (NotificationEntry it : children) {
            List<NodeSpec> children2 = nodeSpecImpl.getChildren();
            Intrinsics.checkNotNullExpressionValue(it, "it");
            children2.add(buildNotifNode(it, nodeSpecImpl));
        }
        return nodeSpecImpl;
    }
}
