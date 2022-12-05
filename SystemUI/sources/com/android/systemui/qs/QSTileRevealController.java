package com.android.systemui.qs;

import android.content.Context;
import android.os.Handler;
import android.util.ArraySet;
import com.android.systemui.Prefs;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSTileRevealController;
import com.android.systemui.qs.customize.QSCustomizerController;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
/* loaded from: classes.dex */
public class QSTileRevealController {
    private final Context mContext;
    private final PagedTileLayout mPagedTileLayout;
    private final QSPanelController mQSPanelController;
    private final QSCustomizerController mQsCustomizerController;
    private final ArraySet<String> mTilesToReveal = new ArraySet<>();
    private final Handler mHandler = new Handler();
    private final Runnable mRevealQsTiles = new AnonymousClass1();

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: com.android.systemui.qs.QSTileRevealController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements Runnable {
        AnonymousClass1() {
        }

        @Override // java.lang.Runnable
        public void run() {
            QSTileRevealController.this.mPagedTileLayout.startTileReveal(QSTileRevealController.this.mTilesToReveal, new Runnable() { // from class: com.android.systemui.qs.QSTileRevealController$1$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    QSTileRevealController.AnonymousClass1.this.lambda$run$0();
                }
            });
        }

        /* JADX INFO: Access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0() {
            if (QSTileRevealController.this.mQSPanelController.isExpanded()) {
                QSTileRevealController qSTileRevealController = QSTileRevealController.this;
                qSTileRevealController.addTileSpecsToRevealed(qSTileRevealController.mTilesToReveal);
                QSTileRevealController.this.mTilesToReveal.clear();
            }
        }
    }

    QSTileRevealController(Context context, QSPanelController qSPanelController, PagedTileLayout pagedTileLayout, QSCustomizerController qSCustomizerController) {
        this.mContext = context;
        this.mQSPanelController = qSPanelController;
        this.mPagedTileLayout = pagedTileLayout;
        this.mQsCustomizerController = qSCustomizerController;
    }

    public void setExpansion(float f) {
        if (f == 1.0f) {
            this.mHandler.postDelayed(this.mRevealQsTiles, 500L);
        } else {
            this.mHandler.removeCallbacks(this.mRevealQsTiles);
        }
    }

    public void updateRevealedTiles(Collection<QSTile> collection) {
        ArraySet<String> arraySet = new ArraySet<>();
        for (QSTile qSTile : collection) {
            arraySet.add(qSTile.getTileSpec());
        }
        Set<String> stringSet = Prefs.getStringSet(this.mContext, "QsTileSpecsRevealed", Collections.EMPTY_SET);
        if (stringSet.isEmpty() || this.mQsCustomizerController.isCustomizing()) {
            addTileSpecsToRevealed(arraySet);
            return;
        }
        arraySet.removeAll(stringSet);
        this.mTilesToReveal.addAll((ArraySet<? extends String>) arraySet);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addTileSpecsToRevealed(ArraySet<String> arraySet) {
        ArraySet arraySet2 = new ArraySet(Prefs.getStringSet(this.mContext, "QsTileSpecsRevealed", Collections.EMPTY_SET));
        arraySet2.addAll((ArraySet) arraySet);
        Prefs.putStringSet(this.mContext, "QsTileSpecsRevealed", arraySet2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class Factory {
        private final Context mContext;
        private final QSCustomizerController mQsCustomizerController;

        /* JADX INFO: Access modifiers changed from: package-private */
        public Factory(Context context, QSCustomizerController qSCustomizerController) {
            this.mContext = context;
            this.mQsCustomizerController = qSCustomizerController;
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        public QSTileRevealController create(QSPanelController qSPanelController, PagedTileLayout pagedTileLayout) {
            return new QSTileRevealController(this.mContext, qSPanelController, pagedTileLayout, this.mQsCustomizerController);
        }
    }
}
