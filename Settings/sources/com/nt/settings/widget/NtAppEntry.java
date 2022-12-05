package com.nt.settings.widget;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import java.io.File;
import java.util.List;
/* loaded from: classes2.dex */
public class NtAppEntry {
    File mApkFile;
    Context mContext;
    Drawable mIcon;
    public final ApplicationInfo mInfo;
    public boolean mIsRAngleAdjust;
    String mLabel;
    final AsyncTaskLoader<List<NtAppEntry>> mLoader;
    boolean mMounted;
    public final ResolveInfo mResolveInfo;

    public NtAppEntry(Context context, AsyncTaskLoader<List<NtAppEntry>> asyncTaskLoader, ApplicationInfo applicationInfo, ResolveInfo resolveInfo) {
        this.mLoader = asyncTaskLoader;
        this.mInfo = applicationInfo;
        this.mResolveInfo = resolveInfo;
        if (applicationInfo != null && applicationInfo.sourceDir != null) {
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

    public Drawable getIconLoaded() {
        return this.mIcon;
    }

    public Drawable getIcon() {
        ActivityInfo activityInfo;
        Drawable drawable = this.mIcon;
        if (drawable == null) {
            ApplicationInfo applicationInfo = this.mInfo;
            if (applicationInfo != null) {
                String str = applicationInfo.packageName;
            } else {
                ResolveInfo resolveInfo = this.mResolveInfo;
                if (resolveInfo != null && (activityInfo = resolveInfo.activityInfo) != null) {
                    String str2 = activityInfo.packageName;
                }
            }
            if (isApkFileExist()) {
                ResolveInfo resolveInfo2 = this.mResolveInfo;
                if (resolveInfo2 != null) {
                    this.mIcon = resolveInfo2.loadIcon(this.mContext.getPackageManager());
                } else {
                    this.mIcon = this.mInfo.loadIcon(this.mContext.getPackageManager());
                }
                return this.mIcon;
            }
            this.mMounted = false;
        } else if (this.mMounted) {
            return drawable;
        } else {
            ApplicationInfo applicationInfo2 = this.mInfo;
            if (applicationInfo2 != null) {
                String str3 = applicationInfo2.packageName;
            } else {
                String str4 = this.mResolveInfo.activityInfo.packageName;
            }
            if (isApkFileExist()) {
                this.mMounted = true;
                ResolveInfo resolveInfo3 = this.mResolveInfo;
                if (resolveInfo3 != null) {
                    this.mIcon = resolveInfo3.loadIcon(this.mContext.getPackageManager());
                } else {
                    this.mIcon = this.mInfo.loadIcon(this.mContext.getPackageManager());
                }
                return this.mIcon;
            }
        }
        return this.mContext.getResources().getDrawable(17303688);
    }

    private boolean isApkFileExist() {
        File file = this.mApkFile;
        return file != null && file.exists();
    }

    public String toString() {
        return this.mLabel;
    }
}
