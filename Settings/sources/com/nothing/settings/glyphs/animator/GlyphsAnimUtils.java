package com.nothing.settings.glyphs.animator;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.android.settingslib.utils.ThreadUtils;
import java.util.ArrayList;
import java.util.List;

public class GlyphsAnimUtils {
    /* access modifiers changed from: private */
    public List<GlyphsAnimatorDataBean> animatorData;
    /* access modifiers changed from: private */
    public onAnimatorChangedListener mCallback;
    private Runnable mCountDownRunnable;
    /* access modifiers changed from: private */
    public GlyphsAnimatorDataProvider mGlyphsAnimatorDataProvider;
    /* access modifiers changed from: private */
    public String mGlyphsCSVPath;
    /* access modifiers changed from: private */
    public int mIndex;
    /* access modifiers changed from: private */
    public final Handler mMainHandler;

    public interface OnDataLoadCallback {
        void onDataLoaded(List<GlyphsAnimatorDataBean> list);
    }

    public interface onAnimatorChangedListener {
        void onAnimatorChanged(int i);

        void onAnimatorEnd();

        void onAnimatorStart();
    }

    public List<GlyphsAnimatorDataBean> getGlyphsAnimators() {
        return this.animatorData;
    }

    private GlyphsAnimUtils() {
        this.animatorData = new ArrayList();
        this.mGlyphsCSVPath = "glyphs/default.csv";
        this.mIndex = 0;
        this.mMainHandler = new Handler(Looper.getMainLooper());
        this.mCountDownRunnable = new Runnable() {
            public void run() {
                if (GlyphsAnimUtils.this.mIndex >= GlyphsAnimUtils.this.animatorData.size()) {
                    GlyphsAnimUtils.this.mIndex = 0;
                    if (GlyphsAnimUtils.this.mCallback != null) {
                        GlyphsAnimUtils.this.mCallback.onAnimatorEnd();
                        return;
                    }
                    return;
                }
                GlyphsAnimUtils.this.mMainHandler.postDelayed(this, 16);
                if (GlyphsAnimUtils.this.mCallback != null) {
                    GlyphsAnimUtils.this.mCallback.onAnimatorChanged(GlyphsAnimUtils.this.mIndex);
                }
                GlyphsAnimUtils glyphsAnimUtils = GlyphsAnimUtils.this;
                glyphsAnimUtils.mIndex = glyphsAnimUtils.mIndex + 1;
            }
        };
    }

    public GlyphsAnimUtils(Context context) {
        this(context, (String) null, (OnDataLoadCallback) null);
    }

    public GlyphsAnimUtils(Context context, String str, OnDataLoadCallback onDataLoadCallback) {
        this();
        if (this.mGlyphsAnimatorDataProvider == null) {
            this.mGlyphsAnimatorDataProvider = new GlyphsAnimatorDataProvider();
        }
        updateGlyphs(context, str, onDataLoadCallback);
    }

    public void updateGlyphs(final Context context, String str, final OnDataLoadCallback onDataLoadCallback) {
        if (!TextUtils.isEmpty(str)) {
            this.mGlyphsCSVPath = str;
            ThreadUtils.postOnBackgroundThread((Runnable) new Runnable() {
                public void run() {
                    GlyphsAnimUtils glyphsAnimUtils = GlyphsAnimUtils.this;
                    glyphsAnimUtils.animatorData = glyphsAnimUtils.mGlyphsAnimatorDataProvider.getGlyphsAnimatorData(context, GlyphsAnimUtils.this.mGlyphsCSVPath);
                    ThreadUtils.postOnMainThread(new Runnable() {
                        public void run() {
                            C20132 r1 = C20132.this;
                            OnDataLoadCallback onDataLoadCallback = onDataLoadCallback;
                            if (onDataLoadCallback != null) {
                                onDataLoadCallback.onDataLoaded(GlyphsAnimUtils.this.animatorData);
                            }
                        }
                    });
                }
            });
        }
    }

    public void stop() {
        this.mIndex = 0;
        this.mMainHandler.removeCallbacksAndMessages((Object) null);
        onAnimatorChangedListener onanimatorchangedlistener = this.mCallback;
        if (onanimatorchangedlistener != null) {
            onanimatorchangedlistener.onAnimatorEnd();
            this.mCallback = null;
        }
    }

    public void start() {
        List<GlyphsAnimatorDataBean> list = this.animatorData;
        if (list != null && list.size() != 0) {
            onAnimatorChangedListener onanimatorchangedlistener = this.mCallback;
            if (onanimatorchangedlistener != null) {
                onanimatorchangedlistener.onAnimatorStart();
            }
            this.mIndex = 0;
            this.mMainHandler.removeCallbacksAndMessages((Object) null);
            this.mMainHandler.post(this.mCountDownRunnable);
        }
    }

    public void setCallback(onAnimatorChangedListener onanimatorchangedlistener) {
        if (onanimatorchangedlistener == null) {
            this.mMainHandler.removeCallbacks(this.mCountDownRunnable);
            this.mCallback = null;
            return;
        }
        this.mCallback = onanimatorchangedlistener;
    }
}
