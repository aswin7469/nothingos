package com.android.systemui.statusbar.notification.row;

import javax.inject.Inject;

public class NotifBindPipelineInitializer {
    NotifBindPipeline mNotifBindPipeline;
    RowContentBindStage mRowContentBindStage;

    @Inject
    NotifBindPipelineInitializer(NotifBindPipeline notifBindPipeline, RowContentBindStage rowContentBindStage) {
        this.mNotifBindPipeline = notifBindPipeline;
        this.mRowContentBindStage = rowContentBindStage;
    }

    public void initialize() {
        this.mNotifBindPipeline.setStage(this.mRowContentBindStage);
    }
}
