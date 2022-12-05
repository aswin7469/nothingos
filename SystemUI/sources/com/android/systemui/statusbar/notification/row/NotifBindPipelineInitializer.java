package com.android.systemui.statusbar.notification.row;
/* loaded from: classes.dex */
public class NotifBindPipelineInitializer {
    NotifBindPipeline mNotifBindPipeline;
    RowContentBindStage mRowContentBindStage;

    /* JADX INFO: Access modifiers changed from: package-private */
    public NotifBindPipelineInitializer(NotifBindPipeline notifBindPipeline, RowContentBindStage rowContentBindStage) {
        this.mNotifBindPipeline = notifBindPipeline;
        this.mRowContentBindStage = rowContentBindStage;
    }

    public void initialize() {
        this.mNotifBindPipeline.setStage(this.mRowContentBindStage);
    }
}
