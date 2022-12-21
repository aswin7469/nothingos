package com.android.systemui.statusbar.notification;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface ShadeViewRefactor {

    public enum RefactorComponent {
        ADAPTER,
        LAYOUT_ALGORITHM,
        STATE_RESOLVER,
        DECORATOR,
        INPUT,
        COORDINATOR,
        SHADE_VIEW
    }

    RefactorComponent value();
}
