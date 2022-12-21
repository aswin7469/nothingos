package com.android.systemui.media.dream;

import android.content.Context;
import com.android.systemui.CoreStartable;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.media.MediaData;
import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.SmartspaceMediaData;
import javax.inject.Inject;

public class MediaDreamSentinel extends CoreStartable {
    /* access modifiers changed from: private */
    public final MediaDreamComplication mComplication;
    /* access modifiers changed from: private */
    public final DreamOverlayStateController mDreamOverlayStateController;
    private MediaDataManager.Listener mListener = new MediaDataManager.Listener() {
        private boolean mAdded;

        public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        }

        public void onSmartspaceMediaDataRemoved(String str, boolean z) {
        }

        public void onMediaDataRemoved(String str) {
            if (this.mAdded && !MediaDreamSentinel.this.mMediaDataManager.hasActiveMedia()) {
                this.mAdded = false;
                MediaDreamSentinel.this.mDreamOverlayStateController.removeComplication(MediaDreamSentinel.this.mComplication);
            }
        }

        public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i, boolean z2) {
            if (!this.mAdded && MediaDreamSentinel.this.mMediaDataManager.hasActiveMedia()) {
                this.mAdded = true;
                MediaDreamSentinel.this.mDreamOverlayStateController.addComplication(MediaDreamSentinel.this.mComplication);
            }
        }
    };
    /* access modifiers changed from: private */
    public final MediaDataManager mMediaDataManager;

    @Inject
    public MediaDreamSentinel(Context context, MediaDataManager mediaDataManager, DreamOverlayStateController dreamOverlayStateController, MediaDreamComplication mediaDreamComplication) {
        super(context);
        this.mMediaDataManager = mediaDataManager;
        this.mDreamOverlayStateController = dreamOverlayStateController;
        this.mComplication = mediaDreamComplication;
    }

    public void start() {
        this.mMediaDataManager.addListener(this.mListener);
    }
}
