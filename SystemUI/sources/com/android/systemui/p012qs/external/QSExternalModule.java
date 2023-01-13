package com.android.systemui.p012qs.external;

import android.service.quicksettings.IQSService;
import dagger.Binds;
import dagger.Module;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\bg\u0018\u00002\u00020\u0001J\u0010\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H'ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0006À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/qs/external/QSExternalModule;", "", "bindsIQSService", "Landroid/service/quicksettings/IQSService;", "impl", "Lcom/android/systemui/qs/external/TileServices;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
@Module
/* renamed from: com.android.systemui.qs.external.QSExternalModule */
/* compiled from: QSExternalModule.kt */
public interface QSExternalModule {
    @Binds
    IQSService bindsIQSService(TileServices tileServices);
}
