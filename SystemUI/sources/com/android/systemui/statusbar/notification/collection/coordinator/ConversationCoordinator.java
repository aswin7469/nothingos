package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.statusbar.notification.collection.GroupEntry;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.listbuilder.OnBeforeRenderListListener;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.icon.ConversationIconManager;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@CoordinatorScope
@Metadata(mo64986d1 = {"\u0000]\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010%\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0002*\u0001\n\b\u0007\u0018\u0000 \u001f2\u00020\u0001:\u0001\u001fB!\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0001\u0010\u0006\u001a\u00020\u0007¢\u0006\u0002\u0010\bJ\u0010\u0010\u0015\u001a\u00020\u00162\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\u0010\u0010\u0019\u001a\u00020\u001a2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002J\u0010\u0010\u001d\u001a\u00020\u001e2\u0006\u0010\u001b\u001a\u00020\u001cH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0004\n\u0002\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00100\u000fX\u0004¢\u0006\u0002\n\u0000R\u0011\u0010\u0011\u001a\u00020\u0012¢\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014¨\u0006 "}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/ConversationCoordinator;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/Coordinator;", "peopleNotificationIdentifier", "Lcom/android/systemui/statusbar/notification/people/PeopleNotificationIdentifier;", "conversationIconManager", "Lcom/android/systemui/statusbar/notification/icon/ConversationIconManager;", "peopleHeaderController", "Lcom/android/systemui/statusbar/notification/collection/render/NodeController;", "(Lcom/android/systemui/statusbar/notification/people/PeopleNotificationIdentifier;Lcom/android/systemui/statusbar/notification/icon/ConversationIconManager;Lcom/android/systemui/statusbar/notification/collection/render/NodeController;)V", "notificationPromoter", "com/android/systemui/statusbar/notification/collection/coordinator/ConversationCoordinator$notificationPromoter$1", "Lcom/android/systemui/statusbar/notification/collection/coordinator/ConversationCoordinator$notificationPromoter$1;", "onBeforeRenderListListener", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/OnBeforeRenderListListener;", "promotedEntriesToSummaryOfSameChannel", "", "Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;", "sectioner", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifSectioner;", "getSectioner", "()Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifSectioner;", "attach", "", "pipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "getPeopleType", "", "entry", "Lcom/android/systemui/statusbar/notification/collection/ListEntry;", "isConversation", "", "Companion", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: ConversationCoordinator.kt */
public final class ConversationCoordinator implements Coordinator {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "ConversationCoordinator";
    private final ConversationIconManager conversationIconManager;
    private final ConversationCoordinator$notificationPromoter$1 notificationPromoter = new ConversationCoordinator$notificationPromoter$1(this);
    private final OnBeforeRenderListListener onBeforeRenderListListener = new ConversationCoordinator$$ExternalSyntheticLambda0(this);
    private final PeopleNotificationIdentifier peopleNotificationIdentifier;
    /* access modifiers changed from: private */
    public final Map<NotificationEntry, NotificationEntry> promotedEntriesToSummaryOfSameChannel = new LinkedHashMap();
    private final NotifSectioner sectioner;

    @Inject
    public ConversationCoordinator(PeopleNotificationIdentifier peopleNotificationIdentifier2, ConversationIconManager conversationIconManager2, NodeController nodeController) {
        Intrinsics.checkNotNullParameter(peopleNotificationIdentifier2, "peopleNotificationIdentifier");
        Intrinsics.checkNotNullParameter(conversationIconManager2, "conversationIconManager");
        Intrinsics.checkNotNullParameter(nodeController, "peopleHeaderController");
        this.peopleNotificationIdentifier = peopleNotificationIdentifier2;
        this.conversationIconManager = conversationIconManager2;
        this.sectioner = new ConversationCoordinator$sectioner$1(this, nodeController);
    }

    /* access modifiers changed from: private */
    /* renamed from: onBeforeRenderListListener$lambda-2  reason: not valid java name */
    public static final void m3101onBeforeRenderListListener$lambda2(ConversationCoordinator conversationCoordinator, List list) {
        Intrinsics.checkNotNullParameter(conversationCoordinator, "this$0");
        Map<NotificationEntry, NotificationEntry> map = conversationCoordinator.promotedEntriesToSummaryOfSameChannel;
        Collection arrayList = new ArrayList();
        for (Map.Entry next : map.entrySet()) {
            NotificationEntry notificationEntry = (NotificationEntry) next.getKey();
            NotificationEntry notificationEntry2 = (NotificationEntry) next.getValue();
            GroupEntry parent = notificationEntry2.getParent();
            String str = null;
            if (parent != null && !Intrinsics.areEqual((Object) parent, (Object) notificationEntry.getParent()) && parent.getParent() != null && Intrinsics.areEqual((Object) parent.getSummary(), (Object) notificationEntry2)) {
                List<NotificationEntry> children = parent.getChildren();
                Intrinsics.checkNotNullExpressionValue(children, "originalGroup.children");
                Iterable iterable = children;
                boolean z = false;
                if (!(iterable instanceof Collection) || !((Collection) iterable).isEmpty()) {
                    Iterator it = iterable.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            if (Intrinsics.areEqual((Object) ((NotificationEntry) it.next()).getChannel(), (Object) notificationEntry2.getChannel())) {
                                z = true;
                                break;
                            }
                        } else {
                            break;
                        }
                    }
                }
                if (!z) {
                    str = notificationEntry2.getKey();
                }
            }
            if (str != null) {
                arrayList.add(str);
            }
        }
        conversationCoordinator.conversationIconManager.setUnimportantConversations((List) arrayList);
        conversationCoordinator.promotedEntriesToSummaryOfSameChannel.clear();
    }

    public final NotifSectioner getSectioner() {
        return this.sectioner;
    }

    public void attach(NotifPipeline notifPipeline) {
        Intrinsics.checkNotNullParameter(notifPipeline, "pipeline");
        notifPipeline.addPromoter(this.notificationPromoter);
        notifPipeline.addOnBeforeRenderListListener(this.onBeforeRenderListListener);
    }

    /* access modifiers changed from: private */
    public final boolean isConversation(ListEntry listEntry) {
        return getPeopleType(listEntry) != 0;
    }

    /* access modifiers changed from: private */
    public final int getPeopleType(ListEntry listEntry) {
        NotificationEntry representativeEntry = listEntry.getRepresentativeEntry();
        if (representativeEntry != null) {
            return this.peopleNotificationIdentifier.getPeopleNotificationType(representativeEntry);
        }
        return 0;
    }

    @Metadata(mo64986d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo64987d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/ConversationCoordinator$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
    /* compiled from: ConversationCoordinator.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
