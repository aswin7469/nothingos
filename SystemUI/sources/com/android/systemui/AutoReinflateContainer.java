package com.android.systemui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOverlay;
import android.widget.FrameLayout;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.util.ArrayList;
import java.util.List;

public class AutoReinflateContainer extends FrameLayout implements ConfigurationController.ConfigurationListener {
    private final List<InflateListener> mInflateListeners = new ArrayList();
    private final int mLayout;

    public interface InflateListener {
        void onInflated(View view);
    }

    /* access modifiers changed from: protected */
    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return super.generateDefaultLayoutParams();
    }

    public /* bridge */ /* synthetic */ ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return super.generateLayoutParams(attributeSet);
    }

    public /* bridge */ /* synthetic */ ViewOverlay getOverlay() {
        return super.getOverlay();
    }

    public AutoReinflateContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1894R.styleable.AutoReinflateContainer);
        if (obtainStyledAttributes.hasValue(0)) {
            this.mLayout = obtainStyledAttributes.getResourceId(0, 0);
            obtainStyledAttributes.recycle();
            inflateLayout();
            return;
        }
        throw new IllegalArgumentException("AutoReinflateContainer must contain a layout");
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).addCallback(this);
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).removeCallback(this);
    }

    /* access modifiers changed from: protected */
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

    public void addInflateListener(InflateListener inflateListener) {
        this.mInflateListeners.add(inflateListener);
        inflateListener.onInflated(getChildAt(0));
    }

    public void onDensityOrFontScaleChanged() {
        inflateLayout();
    }

    public void onThemeChanged() {
        inflateLayout();
    }

    public void onUiModeChanged() {
        inflateLayout();
    }

    public void onLocaleListChanged() {
        inflateLayout();
    }
}
