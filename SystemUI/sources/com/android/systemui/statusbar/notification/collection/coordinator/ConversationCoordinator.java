package com.android.systemui.statusbar.notification.collection.coordinator;

import android.app.NotificationChannel;
import com.android.systemui.statusbar.notification.collection.ListEntry;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: ConversationCoordinator.kt */
/* loaded from: classes.dex */
public final class ConversationCoordinator implements Coordinator {
    @NotNull
    public static final Companion Companion = new Companion(null);
    @NotNull
    private final ConversationCoordinator$notificationPromoter$1 notificationPromoter = new NotifPromoter() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator$notificationPromoter$1
        @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifPromoter
        public boolean shouldPromoteToTopLevel(@NotNull NotificationEntry entry) {
            Intrinsics.checkNotNullParameter(entry, "entry");
            NotificationChannel channel = entry.getChannel();
            return Intrinsics.areEqual(channel == null ? null : Boolean.valueOf(channel.isImportantConversation()), Boolean.TRUE);
        }
    };
    @NotNull
    private final PeopleNotificationIdentifier peopleNotificationIdentifier;
    @NotNull
    private final NotifSectioner sectioner;

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator$notificationPromoter$1] */
    public ConversationCoordinator(@NotNull PeopleNotificationIdentifier peopleNotificationIdentifier, @NotNull final NodeController peopleHeaderController) {
        Intrinsics.checkNotNullParameter(peopleNotificationIdentifier, "peopleNotificationIdentifier");
        Intrinsics.checkNotNullParameter(peopleHeaderController, "peopleHeaderController");
        this.peopleNotificationIdentifier = peopleNotificationIdentifier;
        this.sectioner = new NotifSectioner() { // from class: com.android.systemui.statusbar.notification.collection.coordinator.ConversationCoordinator$sectioner$1
            /* JADX INFO: Access modifiers changed from: package-private */
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super("People");
            }

            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
            public boolean isInSection(@NotNull ListEntry entry) {
                boolean isConversation;
                Intrinsics.checkNotNullParameter(entry, "entry");
                ConversationCoordinator conversationCoordinator = ConversationCoordinator.this;
                NotificationEntry representativeEntry = entry.getRepresentativeEntry();
                Intrinsics.checkNotNull(representativeEntry);
                isConversation = conversationCoordinator.isConversation(representativeEntry);
                return isConversation;
            }

            @Override // com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner
            @NotNull
            public NodeController getHeaderNodeController() {
                return peopleHeaderController;
            }
        };
    }

    @NotNull
    public final NotifSectioner getSectioner() {
        return this.sectioner;
    }

    @Override // com.android.systemui.statusbar.notification.collection.coordinator.Coordinator
    public void attach(@NotNull NotifPipeline pipeline) {
        Intrinsics.checkNotNullParameter(pipeline, "pipeline");
        pipeline.addPromoter(this.notificationPromoter);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final boolean isConversation(NotificationEntry notificationEntry) {
        return this.peopleNotificationIdentifier.getPeopleNotificationType(notificationEntry) != 0;
    }

    /* compiled from: ConversationCoordinator.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
