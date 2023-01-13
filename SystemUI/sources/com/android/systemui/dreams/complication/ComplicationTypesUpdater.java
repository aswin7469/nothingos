package com.android.systemui.dreams.complication;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.os.UserHandle;
import com.android.settingslib.dream.DreamBackend;
import com.android.systemui.CoreStartable;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.util.settings.SecureSettings;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@SysUISingleton
public class ComplicationTypesUpdater extends CoreStartable {
    private final DreamBackend mDreamBackend;
    /* access modifiers changed from: private */
    public final DreamOverlayStateController mDreamOverlayStateController;
    /* access modifiers changed from: private */
    public final Executor mExecutor;
    private final SecureSettings mSecureSettings;

    @Inject
    ComplicationTypesUpdater(Context context, DreamBackend dreamBackend, @Main Executor executor, SecureSettings secureSettings, DreamOverlayStateController dreamOverlayStateController) {
        super(context);
        this.mDreamBackend = dreamBackend;
        this.mExecutor = executor;
        this.mSecureSettings = secureSettings;
        this.mDreamOverlayStateController = dreamOverlayStateController;
    }

    public void start() {
        C20881 r0 = new ContentObserver((Handler) null) {
            /* access modifiers changed from: package-private */
            /* renamed from: lambda$onChange$0$com-android-systemui-dreams-complication-ComplicationTypesUpdater$1 */
            public /* synthetic */ void mo32603xd450c781() {
                ComplicationTypesUpdater.this.mDreamOverlayStateController.setAvailableComplicationTypes(ComplicationTypesUpdater.this.getAvailableComplicationTypes());
            }

            public void onChange(boolean z) {
                ComplicationTypesUpdater.this.mExecutor.execute(new ComplicationTypesUpdater$1$$ExternalSyntheticLambda0(this));
            }
        };
        this.mSecureSettings.registerContentObserverForUser("screensaver_enabled_complications", (ContentObserver) r0, UserHandle.myUserId());
        r0.onChange(false);
    }

    /* access modifiers changed from: private */
    public int getAvailableComplicationTypes() {
        return ComplicationUtils.convertComplicationTypes(this.mDreamBackend.getEnabledComplications());
    }
}
