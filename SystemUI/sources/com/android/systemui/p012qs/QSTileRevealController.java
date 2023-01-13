package com.android.systemui.p012qs;

import android.content.Context;
import android.os.Handler;
import android.util.ArraySet;
import com.android.systemui.Prefs;
import com.android.systemui.p012qs.customize.QSCustomizerController;
import com.android.systemui.p012qs.dagger.QSScope;
import com.android.systemui.plugins.p011qs.QSTile;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import javax.inject.Inject;

/* renamed from: com.android.systemui.qs.QSTileRevealController */
public class QSTileRevealController {
    private static final long QS_REVEAL_TILES_DELAY = 500;
    private final Context mContext;
    private final Handler mHandler = new Handler();
    /* access modifiers changed from: private */
    public final PagedTileLayout mPagedTileLayout;
    /* access modifiers changed from: private */
    public final QSPanelController mQSPanelController;
    private final QSCustomizerController mQsCustomizerController;
    private final Runnable mRevealQsTiles = new Runnable() {
        public void run() {
            QSTileRevealController.this.mPagedTileLayout.startTileReveal(QSTileRevealController.this.mTilesToReveal, new QSTileRevealController$1$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$run$0$com-android-systemui-qs-QSTileRevealController$1  reason: not valid java name */
        public /* synthetic */ void m2947lambda$run$0$comandroidsystemuiqsQSTileRevealController$1() {
            if (QSTileRevealController.this.mQSPanelController.isExpanded()) {
                QSTileRevealController qSTileRevealController = QSTileRevealController.this;
                qSTileRevealController.addTileSpecsToRevealed(qSTileRevealController.mTilesToReveal);
                QSTileRevealController.this.mTilesToReveal.clear();
            }
        }
    };
    /* access modifiers changed from: private */
    public final ArraySet<String> mTilesToReveal = new ArraySet<>();

    QSTileRevealController(Context context, QSPanelController qSPanelController, PagedTileLayout pagedTileLayout, QSCustomizerController qSCustomizerController) {
        this.mContext = context;
        this.mQSPanelController = qSPanelController;
        this.mPagedTileLayout = pagedTileLayout;
        this.mQsCustomizerController = qSCustomizerController;
    }

    public void setExpansion(float f) {
        if (f == 1.0f) {
            this.mHandler.postDelayed(this.mRevealQsTiles, 500);
        } else {
            this.mHandler.removeCallbacks(this.mRevealQsTiles);
        }
    }

    public void updateRevealedTiles(Collection<QSTile> collection) {
        ArraySet arraySet = new ArraySet();
        for (QSTile tileSpec : collection) {
            arraySet.add(tileSpec.getTileSpec());
        }
        Set<String> stringSet = Prefs.getStringSet(this.mContext, Prefs.Key.QS_TILE_SPECS_REVEALED, Collections.EMPTY_SET);
        if (stringSet.isEmpty() || this.mQsCustomizerController.isCustomizing()) {
            addTileSpecsToRevealed(arraySet);
            return;
        }
        arraySet.removeAll(stringSet);
        this.mTilesToReveal.addAll(arraySet);
    }

    /* access modifiers changed from: private */
    public void addTileSpecsToRevealed(ArraySet<String> arraySet) {
        ArraySet arraySet2 = new ArraySet(Prefs.getStringSet(this.mContext, Prefs.Key.QS_TILE_SPECS_REVEALED, Collections.EMPTY_SET));
        arraySet2.addAll(arraySet);
        Prefs.putStringSet(this.mContext, Prefs.Key.QS_TILE_SPECS_REVEALED, arraySet2);
    }

    @QSScope
    /* renamed from: com.android.systemui.qs.QSTileRevealController$Factory */
    static class Factory {
        private final Context mContext;
        private final QSCustomizerController mQsCustomizerController;

        @Inject
        Factory(Context context, QSCustomizerController qSCustomizerController) {
            this.mContext = context;
            this.mQsCustomizerController = qSCustomizerController;
        }

        /* access modifiers changed from: package-private */
        public QSTileRevealController create(QSPanelController qSPanelController, PagedTileLayout pagedTileLayout) {
            return new QSTileRevealController(this.mContext, qSPanelController, pagedTileLayout, this.mQsCustomizerController);
        }
    }
}
