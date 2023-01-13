package com.android.systemui.privacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.android.systemui.C1894R;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;

@Metadata(mo65042d1 = {"\u0000*\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\t\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\b\u0001\u0018\u00002\b\u0012\u0004\u0012\u00020\u00000\u0001B'\b\u0002\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0003\u0012\u0006\u0010\u0005\u001a\u00020\u0006\u0012\u0006\u0010\u0007\u001a\u00020\u0006¢\u0006\u0002\u0010\bJ\u0016\u0010\u000f\u001a\n \u0011*\u0004\u0018\u00010\u00100\u00102\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010\u0014\u001a\n \u0011*\u0004\u0018\u00010\u00060\u00062\u0006\u0010\u0012\u001a\u00020\u0013R\u0011\u0010\u0004\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u0007\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000b\u0010\fR\u0011\u0010\u0002\u001a\u00020\u0003¢\u0006\b\n\u0000\u001a\u0004\b\r\u0010\nR\u0011\u0010\u0005\u001a\u00020\u0006¢\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\fj\u0002\b\u0015j\u0002\b\u0016j\u0002\b\u0017j\u0002\b\u0018¨\u0006\u0019"}, mo65043d2 = {"Lcom/android/systemui/privacy/PrivacyType;", "", "nameId", "", "iconId", "permGroupName", "", "logName", "(Ljava/lang/String;IIILjava/lang/String;Ljava/lang/String;)V", "getIconId", "()I", "getLogName", "()Ljava/lang/String;", "getNameId", "getPermGroupName", "getIcon", "Landroid/graphics/drawable/Drawable;", "kotlin.jvm.PlatformType", "context", "Landroid/content/Context;", "getName", "TYPE_CAMERA", "TYPE_MICROPHONE", "TYPE_LOCATION", "TYPE_MEDIA_PROJECTION", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: PrivacyItem.kt */
public enum PrivacyType {
    TYPE_CAMERA(C1894R.string.privacy_type_camera, 17303172, "android.permission-group.CAMERA", "camera"),
    TYPE_MICROPHONE(C1894R.string.privacy_type_microphone, 17303177, "android.permission-group.MICROPHONE", "microphone"),
    TYPE_LOCATION(C1894R.string.privacy_type_location, 17303176, "android.permission-group.LOCATION", "location"),
    TYPE_MEDIA_PROJECTION(C1894R.string.privacy_type_media_projection, C1894R.C1896drawable.stat_sys_cast, "android.permission-group.UNDEFINED", "media projection");
    
    private final int iconId;
    private final String logName;
    private final int nameId;
    private final String permGroupName;

    private PrivacyType(int i, int i2, String str, String str2) {
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

    public final String getPermGroupName() {
        return this.permGroupName;
    }

    public final String getLogName() {
        return this.logName;
    }

    public final String getName(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return context.getResources().getString(this.nameId);
    }

    public final Drawable getIcon(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return context.getResources().getDrawable(this.iconId, context.getTheme());
    }
}
