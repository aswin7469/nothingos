package com.android.systemui.statusbar.notification.collection.coordinator;

import com.android.systemui.dump.DumpManager;
import com.android.systemui.statusbar.notification.NotifPipelineFlags;
import com.android.systemui.statusbar.notification.collection.NotifPipeline;
import com.android.systemui.statusbar.notification.collection.coordinator.dagger.CoordinatorScope;
import com.android.systemui.statusbar.notification.collection.listbuilder.pluggable.NotifSectioner;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000Ò\u0001\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010!\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\u0010\u000e\n\u0002\b\u0003\b\u0007\u0018\u0000 E2\u00020\u0001:\u0001EBÏ\u0001\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u0012\u0006\u0010\n\u001a\u00020\u000b\u0012\u0006\u0010\f\u001a\u00020\r\u0012\u0006\u0010\u000e\u001a\u00020\u000f\u0012\u0006\u0010\u0010\u001a\u00020\u0011\u0012\u0006\u0010\u0012\u001a\u00020\u0013\u0012\u0006\u0010\u0014\u001a\u00020\u0015\u0012\u0006\u0010\u0016\u001a\u00020\u0017\u0012\u0006\u0010\u0018\u001a\u00020\u0019\u0012\u0006\u0010\u001a\u001a\u00020\u001b\u0012\u0006\u0010\u001c\u001a\u00020\u001d\u0012\u0006\u0010\u001e\u001a\u00020\u001f\u0012\u0006\u0010 \u001a\u00020!\u0012\u0006\u0010\"\u001a\u00020#\u0012\u0006\u0010$\u001a\u00020%\u0012\u0006\u0010&\u001a\u00020'\u0012\u0006\u0010(\u001a\u00020)\u0012\u0006\u0010*\u001a\u00020+\u0012\u0006\u0010,\u001a\u00020-\u0012\u0006\u0010.\u001a\u00020/\u0012\u0006\u00100\u001a\u000201\u0012\u0006\u00102\u001a\u000203¢\u0006\u0002\u00104J\u0010\u0010:\u001a\u00020;2\u0006\u0010<\u001a\u00020=H\u0016J#\u0010>\u001a\u00020;2\u0006\u0010?\u001a\u00020@2\f\u0010A\u001a\b\u0012\u0004\u0012\u00020C0BH\u0016¢\u0006\u0002\u0010DR\u0014\u00105\u001a\b\u0012\u0004\u0012\u00020706X\u0004¢\u0006\u0002\n\u0000R\u0014\u00108\u001a\b\u0012\u0004\u0012\u00020906X\u0004¢\u0006\u0002\n\u0000¨\u0006F"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/NotifCoordinatorsImpl;", "Lcom/android/systemui/statusbar/notification/collection/coordinator/NotifCoordinators;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "notifPipelineFlags", "Lcom/android/systemui/statusbar/notification/NotifPipelineFlags;", "dataStoreCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/DataStoreCoordinator;", "hideLocallyDismissedNotifsCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HideLocallyDismissedNotifsCoordinator;", "hideNotifsForOtherUsersCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HideNotifsForOtherUsersCoordinator;", "keyguardCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/KeyguardCoordinator;", "rankingCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/RankingCoordinator;", "appOpsCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/AppOpsCoordinator;", "deviceProvisionedCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/DeviceProvisionedCoordinator;", "bubbleCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/BubbleCoordinator;", "headsUpCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator;", "gutsCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/GutsCoordinator;", "conversationCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/ConversationCoordinator;", "debugModeCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/DebugModeCoordinator;", "groupCountCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/GroupCountCoordinator;", "mediaCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/MediaCoordinator;", "preparationCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/PreparationCoordinator;", "remoteInputCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator;", "rowAppearanceCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/RowAppearanceCoordinator;", "stackCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/StackCoordinator;", "shadeEventCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/ShadeEventCoordinator;", "smartspaceDedupingCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/SmartspaceDedupingCoordinator;", "viewConfigCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/ViewConfigCoordinator;", "visualStabilityCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/VisualStabilityCoordinator;", "sensitiveContentCoordinator", "Lcom/android/systemui/statusbar/notification/collection/coordinator/SensitiveContentCoordinator;", "(Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/statusbar/notification/NotifPipelineFlags;Lcom/android/systemui/statusbar/notification/collection/coordinator/DataStoreCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/HideLocallyDismissedNotifsCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/HideNotifsForOtherUsersCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/KeyguardCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/RankingCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/AppOpsCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/DeviceProvisionedCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/BubbleCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/HeadsUpCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/GutsCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/ConversationCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/DebugModeCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/GroupCountCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/MediaCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/PreparationCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/RemoteInputCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/RowAppearanceCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/StackCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/ShadeEventCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/SmartspaceDedupingCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/ViewConfigCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/VisualStabilityCoordinator;Lcom/android/systemui/statusbar/notification/collection/coordinator/SensitiveContentCoordinator;)V", "mCoordinators", "", "Lcom/android/systemui/statusbar/notification/collection/coordinator/Coordinator;", "mOrderedSections", "Lcom/android/systemui/statusbar/notification/collection/listbuilder/pluggable/NotifSectioner;", "attach", "", "pipeline", "Lcom/android/systemui/statusbar/notification/collection/NotifPipeline;", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "Companion", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@CoordinatorScope
/* compiled from: NotifCoordinators.kt */
public final class NotifCoordinatorsImpl implements NotifCoordinators {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    private static final String TAG = "NotifCoordinators";
    private final List<Coordinator> mCoordinators;
    private final List<NotifSectioner> mOrderedSections;

    @Inject
    public NotifCoordinatorsImpl(DumpManager dumpManager, NotifPipelineFlags notifPipelineFlags, DataStoreCoordinator dataStoreCoordinator, HideLocallyDismissedNotifsCoordinator hideLocallyDismissedNotifsCoordinator, HideNotifsForOtherUsersCoordinator hideNotifsForOtherUsersCoordinator, KeyguardCoordinator keyguardCoordinator, RankingCoordinator rankingCoordinator, AppOpsCoordinator appOpsCoordinator, DeviceProvisionedCoordinator deviceProvisionedCoordinator, BubbleCoordinator bubbleCoordinator, HeadsUpCoordinator headsUpCoordinator, GutsCoordinator gutsCoordinator, ConversationCoordinator conversationCoordinator, DebugModeCoordinator debugModeCoordinator, GroupCountCoordinator groupCountCoordinator, MediaCoordinator mediaCoordinator, PreparationCoordinator preparationCoordinator, RemoteInputCoordinator remoteInputCoordinator, RowAppearanceCoordinator rowAppearanceCoordinator, StackCoordinator stackCoordinator, ShadeEventCoordinator shadeEventCoordinator, SmartspaceDedupingCoordinator smartspaceDedupingCoordinator, ViewConfigCoordinator viewConfigCoordinator, VisualStabilityCoordinator visualStabilityCoordinator, SensitiveContentCoordinator sensitiveContentCoordinator) {
        DataStoreCoordinator dataStoreCoordinator2 = dataStoreCoordinator;
        HideLocallyDismissedNotifsCoordinator hideLocallyDismissedNotifsCoordinator2 = hideLocallyDismissedNotifsCoordinator;
        HideNotifsForOtherUsersCoordinator hideNotifsForOtherUsersCoordinator2 = hideNotifsForOtherUsersCoordinator;
        KeyguardCoordinator keyguardCoordinator2 = keyguardCoordinator;
        RankingCoordinator rankingCoordinator2 = rankingCoordinator;
        AppOpsCoordinator appOpsCoordinator2 = appOpsCoordinator;
        DeviceProvisionedCoordinator deviceProvisionedCoordinator2 = deviceProvisionedCoordinator;
        BubbleCoordinator bubbleCoordinator2 = bubbleCoordinator;
        ConversationCoordinator conversationCoordinator2 = conversationCoordinator;
        DebugModeCoordinator debugModeCoordinator2 = debugModeCoordinator;
        GroupCountCoordinator groupCountCoordinator2 = groupCountCoordinator;
        MediaCoordinator mediaCoordinator2 = mediaCoordinator;
        PreparationCoordinator preparationCoordinator2 = preparationCoordinator;
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(notifPipelineFlags, "notifPipelineFlags");
        Intrinsics.checkNotNullParameter(dataStoreCoordinator2, "dataStoreCoordinator");
        Intrinsics.checkNotNullParameter(hideLocallyDismissedNotifsCoordinator2, "hideLocallyDismissedNotifsCoordinator");
        Intrinsics.checkNotNullParameter(hideNotifsForOtherUsersCoordinator2, "hideNotifsForOtherUsersCoordinator");
        Intrinsics.checkNotNullParameter(keyguardCoordinator2, "keyguardCoordinator");
        Intrinsics.checkNotNullParameter(rankingCoordinator2, "rankingCoordinator");
        Intrinsics.checkNotNullParameter(appOpsCoordinator2, "appOpsCoordinator");
        Intrinsics.checkNotNullParameter(deviceProvisionedCoordinator2, "deviceProvisionedCoordinator");
        Intrinsics.checkNotNullParameter(bubbleCoordinator2, "bubbleCoordinator");
        Intrinsics.checkNotNullParameter(headsUpCoordinator, "headsUpCoordinator");
        Intrinsics.checkNotNullParameter(gutsCoordinator, "gutsCoordinator");
        Intrinsics.checkNotNullParameter(conversationCoordinator2, "conversationCoordinator");
        Intrinsics.checkNotNullParameter(debugModeCoordinator2, "debugModeCoordinator");
        Intrinsics.checkNotNullParameter(groupCountCoordinator2, "groupCountCoordinator");
        Intrinsics.checkNotNullParameter(mediaCoordinator2, "mediaCoordinator");
        Intrinsics.checkNotNullParameter(preparationCoordinator, "preparationCoordinator");
        Intrinsics.checkNotNullParameter(remoteInputCoordinator, "remoteInputCoordinator");
        Intrinsics.checkNotNullParameter(rowAppearanceCoordinator, "rowAppearanceCoordinator");
        Intrinsics.checkNotNullParameter(stackCoordinator, "stackCoordinator");
        Intrinsics.checkNotNullParameter(shadeEventCoordinator, "shadeEventCoordinator");
        Intrinsics.checkNotNullParameter(smartspaceDedupingCoordinator, "smartspaceDedupingCoordinator");
        Intrinsics.checkNotNullParameter(viewConfigCoordinator, "viewConfigCoordinator");
        Intrinsics.checkNotNullParameter(visualStabilityCoordinator, "visualStabilityCoordinator");
        Intrinsics.checkNotNullParameter(sensitiveContentCoordinator, "sensitiveContentCoordinator");
        List<Coordinator> arrayList = new ArrayList<>();
        this.mCoordinators = arrayList;
        List<NotifSectioner> arrayList2 = new ArrayList<>();
        this.mOrderedSections = arrayList2;
        List<NotifSectioner> list = arrayList2;
        dumpManager.registerDumpable(TAG, this);
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            arrayList.add(dataStoreCoordinator2);
        }
        arrayList.add(hideLocallyDismissedNotifsCoordinator2);
        arrayList.add(hideNotifsForOtherUsersCoordinator2);
        arrayList.add(keyguardCoordinator2);
        arrayList.add(rankingCoordinator2);
        arrayList.add(appOpsCoordinator2);
        arrayList.add(deviceProvisionedCoordinator2);
        arrayList.add(bubbleCoordinator2);
        arrayList.add(debugModeCoordinator2);
        arrayList.add(conversationCoordinator2);
        arrayList.add(groupCountCoordinator2);
        arrayList.add(mediaCoordinator2);
        arrayList.add(rowAppearanceCoordinator);
        List<NotifSectioner> list2 = list;
        arrayList.add(stackCoordinator);
        SmartspaceDedupingCoordinator smartspaceDedupingCoordinator2 = smartspaceDedupingCoordinator;
        arrayList.add(shadeEventCoordinator);
        arrayList.add(viewConfigCoordinator);
        arrayList.add(visualStabilityCoordinator);
        arrayList.add(sensitiveContentCoordinator);
        if (notifPipelineFlags.isSmartspaceDedupingEnabled()) {
            arrayList.add(smartspaceDedupingCoordinator2);
        }
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            arrayList.add(headsUpCoordinator);
            arrayList.add(gutsCoordinator);
            arrayList.add(preparationCoordinator);
            arrayList.add(remoteInputCoordinator);
        } else {
            HeadsUpCoordinator headsUpCoordinator2 = headsUpCoordinator;
        }
        if (notifPipelineFlags.isNewPipelineEnabled()) {
            list2.add(headsUpCoordinator.getSectioner());
        }
        NotifSectioner sectioner = appOpsCoordinator.getSectioner();
        Intrinsics.checkNotNullExpressionValue(sectioner, "appOpsCoordinator.sectioner");
        list2.add(sectioner);
        list2.add(conversationCoordinator.getSectioner());
        NotifSectioner alertingSectioner = rankingCoordinator.getAlertingSectioner();
        Intrinsics.checkNotNullExpressionValue(alertingSectioner, "rankingCoordinator.alertingSectioner");
        list2.add(alertingSectioner);
        NotifSectioner silentSectioner = rankingCoordinator.getSilentSectioner();
        Intrinsics.checkNotNullExpressionValue(silentSectioner, "rankingCoordinator.silentSectioner");
        list2.add(silentSectioner);
        NotifSectioner minimizedSectioner = rankingCoordinator.getMinimizedSectioner();
        Intrinsics.checkNotNullExpressionValue(minimizedSectioner, "rankingCoordinator.minimizedSectioner");
        list2.add(minimizedSectioner);
    }

    public void attach(NotifPipeline notifPipeline) {
        Intrinsics.checkNotNullParameter(notifPipeline, "pipeline");
        for (Coordinator attach : this.mCoordinators) {
            attach.attach(notifPipeline);
        }
        notifPipeline.setSections(this.mOrderedSections);
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println();
        printWriter.println("NotifCoordinators:");
        for (Coordinator coordinator : this.mCoordinators) {
            printWriter.println("\t" + coordinator.getClass());
        }
        for (NotifSectioner name : this.mOrderedSections) {
            printWriter.println("\t" + name.getName());
        }
    }

    @Metadata(mo65042d1 = {"\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004XT¢\u0006\u0002\n\u0000¨\u0006\u0005"}, mo65043d2 = {"Lcom/android/systemui/statusbar/notification/collection/coordinator/NotifCoordinatorsImpl$Companion;", "", "()V", "TAG", "", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: NotifCoordinators.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
