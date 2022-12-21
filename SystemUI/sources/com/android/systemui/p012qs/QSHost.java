package com.android.systemui.p012qs;

import android.content.Context;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.plugins.p011qs.QSTile;
import com.nothing.systemui.p024qs.QSPanelExpandListener;
import java.util.Collection;

/* renamed from: com.android.systemui.qs.QSHost */
public interface QSHost {

    /* renamed from: com.android.systemui.qs.QSHost$Callback */
    public interface Callback {
        void onTilesChanged();
    }

    void addCallback(Callback callback);

    void collapsePanels();

    void forceCollapsePanels();

    Context getContext();

    InstanceId getNewInstanceId();

    Collection<QSTile> getTiles();

    UiEventLogger getUiEventLogger();

    Context getUserContext();

    int getUserId();

    int indexOf(String str);

    void openPanels();

    void removeCallback(Callback callback);

    void removeTile(String str);

    void removeTiles(Collection<String> collection);

    void setQSPanelExpandListener(QSPanelExpandListener qSPanelExpandListener);

    void unmarkTileAsAutoAdded(String str);

    void warn(String str, Throwable th);
}
