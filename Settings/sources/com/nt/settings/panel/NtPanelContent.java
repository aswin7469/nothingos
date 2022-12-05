package com.nt.settings.panel;

import android.content.Intent;
import androidx.core.graphics.drawable.IconCompat;
import com.android.settings.panel.PanelContentCallback;
import java.util.List;
/* loaded from: classes2.dex */
public interface NtPanelContent {
    default Intent getHeaderIconIntent() {
        return null;
    }

    default IconCompat getIcon() {
        return null;
    }

    List<String> getLists();

    default CharSequence getSubTitle() {
        return null;
    }

    CharSequence getTitle();

    default boolean isProgressBarVisible() {
        return false;
    }

    default void registerCallback(PanelContentCallback panelContentCallback) {
    }
}
