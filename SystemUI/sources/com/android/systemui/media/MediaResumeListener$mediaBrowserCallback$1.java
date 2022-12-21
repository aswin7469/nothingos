package com.android.systemui.media;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.media.MediaDescription;
import android.media.session.MediaSession;
import android.util.Log;
import com.android.systemui.media.ResumeMediaBrowser;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000#\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016¨\u0006\n"}, mo64987d2 = {"com/android/systemui/media/MediaResumeListener$mediaBrowserCallback$1", "Lcom/android/systemui/media/ResumeMediaBrowser$Callback;", "addTrack", "", "desc", "Landroid/media/MediaDescription;", "component", "Landroid/content/ComponentName;", "browser", "Lcom/android/systemui/media/ResumeMediaBrowser;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaResumeListener.kt */
public final class MediaResumeListener$mediaBrowserCallback$1 extends ResumeMediaBrowser.Callback {
    final /* synthetic */ MediaResumeListener this$0;

    MediaResumeListener$mediaBrowserCallback$1(MediaResumeListener mediaResumeListener) {
        this.this$0 = mediaResumeListener;
    }

    public void addTrack(MediaDescription mediaDescription, ComponentName componentName, ResumeMediaBrowser resumeMediaBrowser) {
        Intrinsics.checkNotNullParameter(mediaDescription, "desc");
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(resumeMediaBrowser, "browser");
        MediaSession.Token token = resumeMediaBrowser.getToken();
        PendingIntent appIntent = resumeMediaBrowser.getAppIntent();
        PackageManager packageManager = this.this$0.context.getPackageManager();
        String packageName = componentName.getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "component.packageName");
        CharSequence charSequence = packageName;
        Runnable access$getResumeAction = this.this$0.getResumeAction(componentName);
        try {
            CharSequence applicationLabel = packageManager.getApplicationLabel(packageManager.getApplicationInfo(componentName.getPackageName(), 0));
            Intrinsics.checkNotNullExpressionValue(applicationLabel, "pm.getApplicationLabel(\n…omponent.packageName, 0))");
            charSequence = applicationLabel;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("MediaResumeListener", "Error getting package information", e);
        }
        Log.d("MediaResumeListener", "Adding resume controls " + mediaDescription);
        MediaDataManager access$getMediaDataManager$p = this.this$0.mediaDataManager;
        if (access$getMediaDataManager$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
            access$getMediaDataManager$p = null;
        }
        int access$getCurrentUserId$p = this.this$0.currentUserId;
        Intrinsics.checkNotNullExpressionValue(token, "token");
        String obj = charSequence.toString();
        Intrinsics.checkNotNullExpressionValue(appIntent, "appIntent");
        String packageName2 = componentName.getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName2, "component.packageName");
        access$getMediaDataManager$p.addResumptionControls(access$getCurrentUserId$p, mediaDescription, access$getResumeAction, token, obj, appIntent, packageName2);
    }
}
