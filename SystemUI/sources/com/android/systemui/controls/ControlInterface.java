package com.android.systemui.controls;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import kotlin.Metadata;

@Metadata(mo65042d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\u0010\r\n\u0002\b\u0005\bf\u0018\u00002\u00020\u0001R\u0012\u0010\u0002\u001a\u00020\u0003X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X¦\u0004¢\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0014\u0010\n\u001a\u0004\u0018\u00010\u000bX¦\u0004¢\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0018\u0010\u000e\u001a\u00020\u000fX¦\u0004¢\u0006\f\u0012\u0004\b\u0010\u0010\u0011\u001a\u0004\b\u0012\u0010\u0013R\u0012\u0010\u0014\u001a\u00020\u0015X¦\u0004¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017R\u0014\u0010\u0018\u001a\u00020\u00158VX\u0004¢\u0006\u0006\u001a\u0004\b\u0019\u0010\u0017R\u0012\u0010\u001a\u001a\u00020\u001bX¦\u0004¢\u0006\u0006\u001a\u0004\b\u001c\u0010\u001dR\u0012\u0010\u001e\u001a\u00020\u001bX¦\u0004¢\u0006\u0006\u001a\u0004\b\u001f\u0010\u001dø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006 À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/controls/ControlInterface;", "", "component", "Landroid/content/ComponentName;", "getComponent", "()Landroid/content/ComponentName;", "controlId", "", "getControlId", "()Ljava/lang/String;", "customIcon", "Landroid/graphics/drawable/Icon;", "getCustomIcon", "()Landroid/graphics/drawable/Icon;", "deviceType", "", "getDeviceType$annotations", "()V", "getDeviceType", "()I", "favorite", "", "getFavorite", "()Z", "removed", "getRemoved", "subtitle", "", "getSubtitle", "()Ljava/lang/CharSequence;", "title", "getTitle", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: ControlStatus.kt */
public interface ControlInterface {
    static /* synthetic */ void getDeviceType$annotations() {
    }

    ComponentName getComponent();

    String getControlId();

    Icon getCustomIcon();

    int getDeviceType();

    boolean getFavorite();

    boolean getRemoved() {
        return false;
    }

    CharSequence getSubtitle();

    CharSequence getTitle();
}
