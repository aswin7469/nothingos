package com.android.keyguard;

import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;

@Metadata(mo65042d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b6\u0018\u00002\u00020\u0001B\u0007\b\u0004¢\u0006\u0002\u0010\u0002R\u0012\u0010\u0003\u001a\u00020\u0004X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R\u0012\u0010\u0007\u001a\u00020\bX¦\u0004¢\u0006\u0006\u001a\u0004\b\t\u0010\nR\u0012\u0010\u000b\u001a\u00020\fX¦\u0004¢\u0006\u0006\u001a\u0004\b\r\u0010\u000e\u0001\u0003\u000f\u0010\u0011¨\u0006\u0012"}, mo65043d2 = {"Lcom/android/keyguard/KeyguardListenModel;", "", "()V", "listening", "", "getListening", "()Z", "timeMillis", "", "getTimeMillis", "()J", "userId", "", "getUserId", "()I", "Lcom/android/keyguard/KeyguardFingerprintListenModel;", "Lcom/android/keyguard/KeyguardFaceListenModel;", "Lcom/android/keyguard/KeyguardActiveUnlockModel;", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: KeyguardListenModel.kt */
public abstract class KeyguardListenModel {
    public /* synthetic */ KeyguardListenModel(DefaultConstructorMarker defaultConstructorMarker) {
        this();
    }

    public abstract boolean getListening();

    public abstract long getTimeMillis();

    public abstract int getUserId();

    private KeyguardListenModel() {
    }
}
