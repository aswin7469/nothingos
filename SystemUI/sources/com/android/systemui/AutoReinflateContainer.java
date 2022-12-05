package com.android.systemui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class AutoReinflateContainer extends FrameLayout implements ConfigurationController.ConfigurationListener {
    private final List<InflateListener> mInflateListeners = new ArrayList();
    private final int mLayout;

    /* loaded from: classes.dex */
    public interface InflateListener {
        void onInflated(View view);
    }

    public AutoReinflateContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.AutoReinflateContainer);
        int i = R$styleable.AutoReinflateContainer_android_layout;
        if (!obtainStyledAttributes.hasValue(i)) {
            throw new IllegalArgumentException("AutoReinflateContainer must contain a layout");
        }
        this.mLayout = obtainStyledAttributes.getResourceId(i, 0);
        obtainStyledAttributes.recycle();
        inflateLayout();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).addCallback(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.ViewGroup, android.view.View
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).removeCallback(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void inflateLayoutImpl() {
        LayoutInflater.from(getContext()).inflate(this.mLayout, this);
    }

    public void inflateLayout() {
        removeAllViews();
        inflateLayoutImpl();
        int size = this.mInflateListeners.size();
        for (int i = 0; i < size; i++) {
            this.mInflateListeners.get(i).onInflated(getChildAt(0));
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onDensityOrFontScaleChanged() {
        inflateLayout();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onOverlayChanged() {
        inflateLayout();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onUiModeChanged() {
        inflateLayout();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onLocaleListChanged() {
        inflateLayout();
    }
}
