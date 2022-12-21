package com.android.systemui.media.dream;

import android.widget.FrameLayout;
import com.android.systemui.media.MediaHost;
import com.android.systemui.util.ViewController;
import javax.inject.Inject;
import javax.inject.Named;

public class MediaComplicationViewController extends ViewController<FrameLayout> {
    private final MediaHost mMediaHost;

    @Inject
    public MediaComplicationViewController(@Named("media_complication_container") FrameLayout frameLayout, @Named("dream") MediaHost mediaHost) {
        super(frameLayout);
        this.mMediaHost = mediaHost;
    }

    /* access modifiers changed from: protected */
    public void onInit() {
        super.onInit();
        this.mMediaHost.setExpansion(0.0f);
        this.mMediaHost.setShowsOnlyActiveMedia(true);
        this.mMediaHost.setFalsingProtectionNeeded(true);
        this.mMediaHost.init(3);
    }

    /* access modifiers changed from: protected */
    public void onViewAttached() {
        this.mMediaHost.hostView.setLayoutParams(new FrameLayout.LayoutParams(-1, -2));
        ((FrameLayout) this.mView).addView(this.mMediaHost.hostView);
    }

    /* access modifiers changed from: protected */
    public void onViewDetached() {
        ((FrameLayout) this.mView).removeView(this.mMediaHost.hostView);
    }
}
