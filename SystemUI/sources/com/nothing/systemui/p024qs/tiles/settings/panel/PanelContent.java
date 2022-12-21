package com.nothing.systemui.p024qs.tiles.settings.panel;

import android.content.Intent;
import androidx.core.graphics.drawable.IconCompat;
import java.util.List;

/* renamed from: com.nothing.systemui.qs.tiles.settings.panel.PanelContent */
public interface PanelContent {
    Intent getHeaderIconIntent() {
        return null;
    }

    IconCompat getIcon() {
        return null;
    }

    List<String> getLists();

    CharSequence getSubTitle() {
        return null;
    }

    CharSequence getTitle();

    boolean isProgressBarVisible() {
        return false;
    }

    void registerCallback(PanelContentCallback panelContentCallback) {
    }
}
