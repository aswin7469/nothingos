package com.android.systemui.media;

import android.content.ComponentName;
import android.content.Context;
import com.android.systemui.media.ResumeMediaBrowser;
import javax.inject.Inject;

public class ResumeMediaBrowserFactory {
    private final MediaBrowserFactory mBrowserFactory;
    private final Context mContext;
    private final ResumeMediaBrowserLogger mLogger;

    @Inject
    public ResumeMediaBrowserFactory(Context context, MediaBrowserFactory mediaBrowserFactory, ResumeMediaBrowserLogger resumeMediaBrowserLogger) {
        this.mContext = context;
        this.mBrowserFactory = mediaBrowserFactory;
        this.mLogger = resumeMediaBrowserLogger;
    }

    public ResumeMediaBrowser create(ResumeMediaBrowser.Callback callback, ComponentName componentName) {
        return new ResumeMediaBrowser(this.mContext, callback, componentName, this.mBrowserFactory, this.mLogger);
    }
}
