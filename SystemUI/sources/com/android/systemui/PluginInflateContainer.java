package com.android.systemui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.ViewProvider;
import com.android.systemui.shared.plugins.PluginManager;

public class PluginInflateContainer extends AutoReinflateContainer implements PluginListener<ViewProvider> {
    private static final String TAG = "PluginInflateContainer";
    private Class<ViewProvider> mClass;
    private View mPluginView;

    public PluginInflateContainer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, C1894R.styleable.PluginInflateContainer);
        String string = obtainStyledAttributes.getString(0);
        obtainStyledAttributes.recycle();
        try {
            this.mClass = Class.forName(string);
        } catch (Exception e) {
            Log.d(TAG, "Problem getting class info " + string, e);
            this.mClass = null;
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mClass != null) {
            ((PluginManager) Dependency.get(PluginManager.class)).addPluginListener(this, this.mClass);
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mClass != null) {
            ((PluginManager) Dependency.get(PluginManager.class)).removePluginListener(this);
        }
    }

    /* access modifiers changed from: protected */
    public void inflateLayoutImpl() {
        View view = this.mPluginView;
        if (view != null) {
            addView(view);
        } else {
            super.inflateLayoutImpl();
        }
    }

    public void onPluginConnected(ViewProvider viewProvider, Context context) {
        this.mPluginView = viewProvider.getView();
        inflateLayout();
    }

    public void onPluginDisconnected(ViewProvider viewProvider) {
        this.mPluginView = null;
        inflateLayout();
    }
}
