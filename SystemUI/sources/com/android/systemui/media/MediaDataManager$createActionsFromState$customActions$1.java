package com.android.systemui.media;

import android.media.session.MediaController;
import android.media.session.PlaybackState;
import com.android.systemui.biometrics.AuthDialog;
import kotlin.Metadata;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;

@Metadata(mo64986d1 = {"\u0000\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u00012\u0006\u0010\u0002\u001a\u00020\u0003H\n¢\u0006\u0002\b\u0004"}, mo64987d2 = {"<anonymous>", "Lcom/android/systemui/media/MediaAction;", "it", "Landroid/media/session/PlaybackState$CustomAction;", "invoke"}, mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaDataManager.kt */
final class MediaDataManager$createActionsFromState$customActions$1 extends Lambda implements Function1<PlaybackState.CustomAction, MediaAction> {
    final /* synthetic */ MediaController $controller;
    final /* synthetic */ String $packageName;
    final /* synthetic */ PlaybackState $state;
    final /* synthetic */ MediaDataManager this$0;

    /* JADX INFO: super call moved to the top of the method (can break code semantics) */
    MediaDataManager$createActionsFromState$customActions$1(MediaDataManager mediaDataManager, PlaybackState playbackState, String str, MediaController mediaController) {
        super(1);
        this.this$0 = mediaDataManager;
        this.$state = playbackState;
        this.$packageName = str;
        this.$controller = mediaController;
    }

    public final MediaAction invoke(PlaybackState.CustomAction customAction) {
        Intrinsics.checkNotNullParameter(customAction, "it");
        MediaDataManager mediaDataManager = this.this$0;
        PlaybackState playbackState = this.$state;
        Intrinsics.checkNotNullExpressionValue(playbackState, AuthDialog.KEY_BIOMETRIC_STATE);
        return mediaDataManager.getCustomAction(playbackState, this.$packageName, this.$controller, customAction);
    }
}
