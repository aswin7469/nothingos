package com.android.systemui.media;

import android.content.ComponentName;
import android.media.MediaDescription;
import android.util.Log;
import com.android.systemui.media.ResumeMediaBrowser;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo64986d1 = {"\u0000%\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003*\u0001\u0000\b\n\u0018\u00002\u00020\u0001J \u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0006\u001a\u00020\u00072\u0006\u0010\b\u001a\u00020\tH\u0016J\b\u0010\n\u001a\u00020\u0003H\u0016J\b\u0010\u000b\u001a\u00020\u0003H\u0016¨\u0006\f"}, mo64987d2 = {"com/android/systemui/media/MediaResumeListener$tryUpdateResumptionList$1", "Lcom/android/systemui/media/ResumeMediaBrowser$Callback;", "addTrack", "", "desc", "Landroid/media/MediaDescription;", "component", "Landroid/content/ComponentName;", "browser", "Lcom/android/systemui/media/ResumeMediaBrowser;", "onConnected", "onError", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaResumeListener.kt */
public final class MediaResumeListener$tryUpdateResumptionList$1 extends ResumeMediaBrowser.Callback {
    final /* synthetic */ ComponentName $componentName;
    final /* synthetic */ String $key;
    final /* synthetic */ MediaResumeListener this$0;

    MediaResumeListener$tryUpdateResumptionList$1(ComponentName componentName, MediaResumeListener mediaResumeListener, String str) {
        this.$componentName = componentName;
        this.this$0 = mediaResumeListener;
        this.$key = str;
    }

    public void onConnected() {
        Log.d("MediaResumeListener", "Connected to " + this.$componentName);
    }

    public void onError() {
        Log.e("MediaResumeListener", "Cannot resume with " + this.$componentName);
        this.this$0.setMediaBrowser((ResumeMediaBrowser) null);
    }

    public void addTrack(MediaDescription mediaDescription, ComponentName componentName, ResumeMediaBrowser resumeMediaBrowser) {
        Intrinsics.checkNotNullParameter(mediaDescription, "desc");
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(resumeMediaBrowser, "browser");
        Log.d("MediaResumeListener", "Can get resumable media from " + this.$componentName);
        MediaDataManager access$getMediaDataManager$p = this.this$0.mediaDataManager;
        if (access$getMediaDataManager$p == null) {
            Intrinsics.throwUninitializedPropertyAccessException("mediaDataManager");
            access$getMediaDataManager$p = null;
        }
        access$getMediaDataManager$p.setResumeAction(this.$key, this.this$0.getResumeAction(this.$componentName));
        this.this$0.updateResumptionList(this.$componentName);
        this.this$0.setMediaBrowser((ResumeMediaBrowser) null);
    }
}
