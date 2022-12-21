package androidx.leanback.widget;

import android.view.View;
import androidx.leanback.widget.ItemBridgeAdapter;

public class ItemBridgeAdapterShadowOverlayWrapper extends ItemBridgeAdapter.Wrapper {
    private final ShadowOverlayHelper mHelper;

    public ItemBridgeAdapterShadowOverlayWrapper(ShadowOverlayHelper shadowOverlayHelper) {
        this.mHelper = shadowOverlayHelper;
    }

    public View createWrapper(View view) {
        return this.mHelper.createShadowOverlayContainer(view.getContext());
    }

    public void wrap(View view, View view2) {
        ((ShadowOverlayContainer) view).wrap(view2);
    }
}
