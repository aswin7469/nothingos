package com.android.systemui.statusbar;

import com.android.systemui.dagger.SysUISingleton;
import dagger.Binds;
import dagger.Module;

@Module
public interface QsFrameTranslateModule {
    @SysUISingleton
    @Binds
    QsFrameTranslateController bindQsFrameTranslateController(QsFrameTranslateImpl qsFrameTranslateImpl);
}
