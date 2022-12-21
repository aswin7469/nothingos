package com.android.systemui.privacy.logging;

import com.android.systemui.privacy.PrivacyItem;
import kotlin.Metadata;
import kotlin.jvm.internal.PropertyReference1Impl;

@Metadata(mo64988k = 3, mo64989mv = {1, 6, 0}, mo64991xi = 48)
/* compiled from: PrivacyLogger.kt */
/* synthetic */ class PrivacyLogger$listToString$1 extends PropertyReference1Impl {
    public static final PrivacyLogger$listToString$1 INSTANCE = new PrivacyLogger$listToString$1();

    PrivacyLogger$listToString$1() {
        super(PrivacyItem.class, "log", "getLog()Ljava/lang/String;", 0);
    }

    public Object get(Object obj) {
        return ((PrivacyItem) obj).getLog();
    }
}
