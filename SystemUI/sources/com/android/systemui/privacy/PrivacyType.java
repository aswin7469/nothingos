package com.android.systemui.privacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.android.systemui.R$string;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;
/* compiled from: PrivacyItem.kt */
/* loaded from: classes.dex */
public enum PrivacyType {
    TYPE_CAMERA(R$string.privacy_type_camera, 17303161, "android.permission-group.CAMERA", "camera"),
    TYPE_MICROPHONE(R$string.privacy_type_microphone, 17303166, "android.permission-group.MICROPHONE", "microphone"),
    TYPE_LOCATION(R$string.privacy_type_location, 17303165, "android.permission-group.LOCATION", "location");
    
    private final int iconId;
    @NotNull
    private final String logName;
    private final int nameId;
    @NotNull
    private final String permGroupName;

    /* renamed from: values  reason: to resolve conflict with enum method */
    public static PrivacyType[] valuesCustom() {
        PrivacyType[] valuesCustom = values();
        PrivacyType[] privacyTypeArr = new PrivacyType[valuesCustom.length];
        System.arraycopy(valuesCustom, 0, privacyTypeArr, 0, valuesCustom.length);
        return privacyTypeArr;
    }

    PrivacyType(int i, int i2, String str, String str2) {
        this.nameId = i;
        this.iconId = i2;
        this.permGroupName = str;
        this.logName = str2;
    }

    public final int getNameId() {
        return this.nameId;
    }

    public final int getIconId() {
        return this.iconId;
    }

    @NotNull
    public final String getPermGroupName() {
        return this.permGroupName;
    }

    @NotNull
    public final String getLogName() {
        return this.logName;
    }

    public final String getName(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return context.getResources().getString(this.nameId);
    }

    public final Drawable getIcon(@NotNull Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return context.getResources().getDrawable(this.iconId, context.getTheme());
    }
}
