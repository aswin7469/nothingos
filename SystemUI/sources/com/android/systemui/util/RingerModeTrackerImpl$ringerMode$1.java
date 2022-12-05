package com.android.systemui.util;

import android.media.AudioManager;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: RingerModeTrackerImpl.kt */
/* loaded from: classes2.dex */
/* synthetic */ class RingerModeTrackerImpl$ringerMode$1 extends FunctionReferenceImpl implements Function0<Integer> {
    /* JADX INFO: Access modifiers changed from: package-private */
    public RingerModeTrackerImpl$ringerMode$1(AudioManager audioManager) {
        super(0, audioManager, AudioManager.class, "getRingerMode", "getRingerMode()I", 0);
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [int, java.lang.Integer] */
    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke */
    public final Integer mo1951invoke() {
        return ((AudioManager) this.receiver).getRingerMode();
    }

    @Override // kotlin.jvm.functions.Function0
    /* renamed from: invoke  reason: collision with other method in class */
    public /* bridge */ /* synthetic */ Integer mo1951invoke() {
        return Integer.valueOf(mo1951invoke());
    }
}
