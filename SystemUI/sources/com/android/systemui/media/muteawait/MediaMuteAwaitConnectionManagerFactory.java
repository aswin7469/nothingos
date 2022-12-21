package com.android.systemui.media.muteawait;

import android.content.Context;
import com.android.settingslib.media.DeviceIconUtil;
import com.android.settingslib.media.LocalMediaManager;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.media.MediaFlags;
import java.util.concurrent.Executor;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@SysUISingleton
@Metadata(mo64986d1 = {"\u00006\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B)\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\b\b\u0001\u0010\b\u001a\u00020\t¢\u0006\u0002\u0010\nJ\u0010\u0010\r\u001a\u0004\u0018\u00010\u000e2\u0006\u0010\u000f\u001a\u00020\u0010R\u000e\u0010\u0004\u001a\u00020\u0005X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0004¢\u0006\u0002\n\u0000¨\u0006\u0011"}, mo64987d2 = {"Lcom/android/systemui/media/muteawait/MediaMuteAwaitConnectionManagerFactory;", "", "mediaFlags", "Lcom/android/systemui/media/MediaFlags;", "context", "Landroid/content/Context;", "logger", "Lcom/android/systemui/media/muteawait/MediaMuteAwaitLogger;", "mainExecutor", "Ljava/util/concurrent/Executor;", "(Lcom/android/systemui/media/MediaFlags;Landroid/content/Context;Lcom/android/systemui/media/muteawait/MediaMuteAwaitLogger;Ljava/util/concurrent/Executor;)V", "deviceIconUtil", "Lcom/android/settingslib/media/DeviceIconUtil;", "create", "Lcom/android/systemui/media/muteawait/MediaMuteAwaitConnectionManager;", "localMediaManager", "Lcom/android/settingslib/media/LocalMediaManager;", "SystemUI_nothingRelease"}, mo64988k = 1, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: MediaMuteAwaitConnectionManagerFactory.kt */
public final class MediaMuteAwaitConnectionManagerFactory {
    private final Context context;
    private final DeviceIconUtil deviceIconUtil = new DeviceIconUtil();
    private final MediaMuteAwaitLogger logger;
    private final Executor mainExecutor;
    private final MediaFlags mediaFlags;

    @Inject
    public MediaMuteAwaitConnectionManagerFactory(MediaFlags mediaFlags2, Context context2, MediaMuteAwaitLogger mediaMuteAwaitLogger, @Main Executor executor) {
        Intrinsics.checkNotNullParameter(mediaFlags2, "mediaFlags");
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(mediaMuteAwaitLogger, "logger");
        Intrinsics.checkNotNullParameter(executor, "mainExecutor");
        this.mediaFlags = mediaFlags2;
        this.context = context2;
        this.logger = mediaMuteAwaitLogger;
        this.mainExecutor = executor;
    }

    public final MediaMuteAwaitConnectionManager create(LocalMediaManager localMediaManager) {
        Intrinsics.checkNotNullParameter(localMediaManager, "localMediaManager");
        if (!this.mediaFlags.areMuteAwaitConnectionsEnabled()) {
            return null;
        }
        return new MediaMuteAwaitConnectionManager(this.mainExecutor, localMediaManager, this.context, this.deviceIconUtil, this.logger);
    }
}
