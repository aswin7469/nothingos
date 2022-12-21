package com.android.systemui.media;

import android.content.ComponentName;
import android.content.Context;
import android.media.browse.MediaBrowser;
import android.os.Bundle;
import javax.inject.Inject;

public class MediaBrowserFactory {
    private final Context mContext;

    @Inject
    public MediaBrowserFactory(Context context) {
        this.mContext = context;
    }

    public MediaBrowser create(ComponentName componentName, MediaBrowser.ConnectionCallback connectionCallback, Bundle bundle) {
        return new MediaBrowser(this.mContext, componentName, connectionCallback, bundle);
    }
}
