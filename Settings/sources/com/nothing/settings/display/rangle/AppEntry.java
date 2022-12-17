package com.nothing.settings.display.rangle;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import androidx.loader.content.AsyncTaskLoader;
import java.io.File;
import java.util.List;

public class AppEntry {
    private File mApkFile;
    private Context mContext;
    private Drawable mIcon;
    private final ApplicationInfo mInfo;
    private boolean mIsRAngleAdjust;
    private String mLabel;
    private final AsyncTaskLoader<List<AppEntry>> mLoader;
    private final ResolveInfo mResolveInfo;

    public AppEntry(Context context, AsyncTaskLoader<List<AppEntry>> asyncTaskLoader, ApplicationInfo applicationInfo, ResolveInfo resolveInfo) {
        this.mLoader = asyncTaskLoader;
        this.mInfo = applicationInfo;
        this.mResolveInfo = resolveInfo;
        if (!(applicationInfo == null || applicationInfo.sourceDir == null)) {
            this.mApkFile = new File(applicationInfo.sourceDir);
        }
        this.mContext = context;
    }

    public String getLabel() {
        return this.mLabel;
    }

    public void setLabel(String str) {
        this.mLabel = str;
    }

    public void setIcon(Drawable drawable) {
        this.mIcon = drawable;
    }

    public Drawable getIconLoaded() {
        return this.mIcon;
    }

    public ApplicationInfo getInfo() {
        return this.mInfo;
    }

    public boolean isRAngleAdjust() {
        return this.mIsRAngleAdjust;
    }

    public void setIsRAngleAdjust(boolean z) {
        this.mIsRAngleAdjust = z;
    }

    public String toString() {
        return this.mLabel;
    }
}
