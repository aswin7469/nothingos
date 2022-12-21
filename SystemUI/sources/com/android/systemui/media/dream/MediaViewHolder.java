package com.android.systemui.media.dream;

import android.view.View;
import android.widget.FrameLayout;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import javax.inject.Inject;
import javax.inject.Named;

public class MediaViewHolder implements Complication.ViewHolder {
    private final FrameLayout mContainer;
    private final ComplicationLayoutParams mLayoutParams;
    private final MediaComplicationViewController mViewController;

    @Inject
    MediaViewHolder(@Named("media_complication_container") FrameLayout frameLayout, MediaComplicationViewController mediaComplicationViewController, @Named("media_complication_layout_params") ComplicationLayoutParams complicationLayoutParams) {
        this.mContainer = frameLayout;
        this.mViewController = mediaComplicationViewController;
        mediaComplicationViewController.init();
        this.mLayoutParams = complicationLayoutParams;
    }

    public View getView() {
        return this.mContainer;
    }

    public ComplicationLayoutParams getLayoutParams() {
        return this.mLayoutParams;
    }
}
