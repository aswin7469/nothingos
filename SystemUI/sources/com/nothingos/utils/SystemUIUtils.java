package com.nothingos.utils;
/* loaded from: classes2.dex */
public class SystemUIUtils {
    private boolean mShouldUseSplitNotificationShade;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        private static SystemUIUtils INSTANCE = new SystemUIUtils();
    }

    private SystemUIUtils() {
    }

    public static SystemUIUtils getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void updateShouldUseSplitNotificationShade(boolean z) {
        this.mShouldUseSplitNotificationShade = z;
    }

    public boolean shouldUseSplitNotificationShade() {
        return this.mShouldUseSplitNotificationShade;
    }
}
