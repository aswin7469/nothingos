package com.android.systemui.statusbar.notification.collection.render;

import android.os.Trace;
import com.android.systemui.statusbar.notification.NotificationSectionsFeatureManager;
import com.android.systemui.statusbar.notification.SectionHeaderVisibilityProvider;
import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.NotifSection;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import kotlin.Metadata;
import kotlin.collections.SetsKt;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\"\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B-\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b¢\u0006\u0002\u0010\fJ\u001c\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u00132\f\u0010\u0014\u001a\b\u0012\u0004\u0012\u00020\u00160\u0015J\u0018\u0010\u0017\u001a\u00020\u00112\u0006\u0010\u0018\u001a\u00020\u00112\u0006\u0010\u0019\u001a\u00020\u0016H\u0002R\u0016\u0010\r\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u000f0\u000eX\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000¨\u0006\u001a"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/render/NodeSpecBuilder;", "", "mediaContainerController", "Lcom/android/systemui/statusbar/notification/collection/render/MediaContainerController;", "sectionsFeatureManager", "Lcom/android/systemui/statusbar/notification/NotificationSectionsFeatureManager;", "sectionHeaderVisibilityProvider", "Lcom/android/systemui/statusbar/notification/SectionHeaderVisibilityProvider;", "viewBarn", "Lcom/android/systemui/statusbar/notification/collection/render/NotifViewBarn;", "logger", "Lcom/android/systemui/statusbar/notification/collection/render/NodeSpecBuilderLogger;", "(Lcom/android/systemui/statusbar/notification/collection/render/MediaContainerController;Lcom/android/systemui/statusbar/notification/NotificationSectionsFeatureManager;Lcom/android/systemui/statusbar/notification/SectionHeaderVisibilityProvider;Lcom/android/systemui/statusbar/notification/collection/render/NotifViewBarn;Lcom/android/systemui/statusbar/notification/collection/render/NodeSpecBuilderLogger;)V", "lastSections", "", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/NotifSection;", "buildNodeSpec", "Lcom/android/systemui/statusbar/notification/collection/render/NodeSpec;", "rootController", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "notifList", "", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "buildNotifNode", "parent", "entry", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: NodeSpecBuilder.kt */
public final class NodeSpecBuilder {
    private Set<NotifSection> lastSections = SetsKt.emptySet();
    private final NodeSpecBuilderLogger logger;
    private final MediaContainerController mediaContainerController;
    private final SectionHeaderVisibilityProvider sectionHeaderVisibilityProvider;
    private final NotificationSectionsFeatureManager sectionsFeatureManager;
    private final NotifViewBarn viewBarn;

    public NodeSpecBuilder(MediaContainerController mediaContainerController2, NotificationSectionsFeatureManager notificationSectionsFeatureManager, SectionHeaderVisibilityProvider sectionHeaderVisibilityProvider2, NotifViewBarn notifViewBarn, NodeSpecBuilderLogger nodeSpecBuilderLogger) {
        Intrinsics.checkNotNullParameter(mediaContainerController2, "mediaContainerController");
        Intrinsics.checkNotNullParameter(notificationSectionsFeatureManager, "sectionsFeatureManager");
        Intrinsics.checkNotNullParameter(sectionHeaderVisibilityProvider2, "sectionHeaderVisibilityProvider");
        Intrinsics.checkNotNullParameter(notifViewBarn, "viewBarn");
        Intrinsics.checkNotNullParameter(nodeSpecBuilderLogger, "logger");
        this.mediaContainerController = mediaContainerController2;
        this.sectionsFeatureManager = notificationSectionsFeatureManager;
        this.sectionHeaderVisibilityProvider = sectionHeaderVisibilityProvider2;
        this.viewBarn = notifViewBarn;
        this.logger = nodeSpecBuilderLogger;
    }

    private final NodeSpec buildNotifNode(NodeSpec nodeSpec, ListEntry listEntry) {
        if (listEntry instanceof NotificationEntry) {
            return new NodeSpecImpl(nodeSpec, this.viewBarn.requireNodeController(listEntry));
        }
        if (listEntry instanceof GroupEntry) {
            NotifViewBarn notifViewBarn = this.viewBarn;
            GroupEntry groupEntry = (GroupEntry) listEntry;
            NotificationEntry summary = groupEntry.getSummary();
            if (summary != null) {
                Intrinsics.checkNotNullExpressionValue(summary, "checkNotNull(entry.summary)");
                NodeSpecImpl nodeSpecImpl = new NodeSpecImpl(nodeSpec, notifViewBarn.requireNodeController(summary));
                List<NotificationEntry> children = groupEntry.getChildren();
                Intrinsics.checkNotNullExpressionValue(children, "entry.children");
                for (NotificationEntry notificationEntry : children) {
                    List<NodeSpec> children2 = nodeSpecImpl.getChildren();
                    Intrinsics.checkNotNullExpressionValue(notificationEntry, "it");
                    children2.add(buildNotifNode(nodeSpecImpl, notificationEntry));
                }
                return nodeSpecImpl;
            }
            throw new IllegalStateException("Required value was null.".toString());
        }
        throw new RuntimeException("Unexpected entry: " + listEntry);
    }

    /* JADX INFO: finally extract failed */
    public final NodeSpec buildNodeSpec(NodeController nodeController, List<? extends ListEntry> list) {
        NodeController headerController;
        Intrinsics.checkNotNullParameter(nodeController, "rootController");
        Intrinsics.checkNotNullParameter(list, "notifList");
        Trace.beginSection("NodeSpecBuilder.buildNodeSpec");
        try {
            NodeSpecImpl nodeSpecImpl = new NodeSpecImpl((NodeSpec) null, nodeController);
            if (this.sectionsFeatureManager.isMediaControlsEnabled()) {
                nodeSpecImpl.getChildren().add(new NodeSpecImpl(nodeSpecImpl, this.mediaContainerController));
            }
            Set linkedHashSet = new LinkedHashSet();
            boolean sectionHeadersVisible = this.sectionHeaderVisibilityProvider.getSectionHeadersVisible();
            new ArrayList();
            new LinkedHashMap();
            new LinkedHashMap();
            NotifSection notifSection = null;
            for (ListEntry listEntry : list) {
                NotifSection section = listEntry.getSection();
                Intrinsics.checkNotNull(section);
                if (!linkedHashSet.contains(section)) {
                    if (!Intrinsics.areEqual((Object) section, (Object) notifSection)) {
                        if (!Intrinsics.areEqual((Object) section.getHeaderController(), (Object) notifSection != null ? notifSection.getHeaderController() : null) && sectionHeadersVisible && (headerController = section.getHeaderController()) != null) {
                            nodeSpecImpl.getChildren().add(new NodeSpecImpl(nodeSpecImpl, headerController));
                        }
                        linkedHashSet.add(notifSection);
                        notifSection = section;
                    }
                    nodeSpecImpl.getChildren().add(buildNotifNode(nodeSpecImpl, listEntry));
                } else {
                    throw new RuntimeException("Section " + section.getLabel() + " has been duplicated");
                }
            }
            Trace.endSection();
            return nodeSpecImpl;
        } catch (Throwable th) {
            Trace.endSection();
            throw th;
        }
    }
}
