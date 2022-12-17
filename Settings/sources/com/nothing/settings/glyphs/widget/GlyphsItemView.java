package com.nothing.settings.glyphs.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import com.android.settings.R$drawable;
import com.android.settings.R$id;
import com.android.settings.R$layout;
import com.nothing.settings.glyphs.animator.GlyphsAnimUtils;
import com.nothing.settings.glyphs.animator.GlyphsAnimatorDataBean;
import com.nothing.settings.glyphs.animator.GlyphsAnimatorDataProvider;
import java.util.List;

public class GlyphsItemView extends FrameLayout implements GlyphsAnimUtils.onAnimatorChangedListener {
    private GlyphsAnimUtils glyphsAnimUtils;
    private ImageView imgLed1;
    private ImageView imgLed2;
    private ImageView imgLed3;
    private ImageView imgLed4;
    private ImageView imgLed5;
    private List<GlyphsAnimatorDataBean> mAnimatorData;
    private String mCurrentPath;
    private GlyphsAnimatorDataProvider mGlyphsAnimatorDataProvider;
    private onAnimatorChangedListener mListener;
    long time;

    public interface onAnimatorChangedListener {
        void onFinish(View view);

        void onStart(View view);
    }

    public GlyphsItemView(Context context) {
        this(context, (AttributeSet) null, 0, 0);
    }

    public GlyphsItemView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0);
    }

    public GlyphsItemView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public GlyphsItemView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mCurrentPath = "glyphs/default.csv";
        this.time = 0;
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R$layout.glyphs_item_led_pager, this);
        this.imgLed1 = (ImageView) findViewById(R$id.img_led1);
        this.imgLed2 = (ImageView) findViewById(R$id.img_led2);
        this.imgLed3 = (ImageView) findViewById(R$id.img_led3);
        this.imgLed4 = (ImageView) findViewById(R$id.img_led4);
        this.imgLed5 = (ImageView) findViewById(R$id.img_led5);
        this.mGlyphsAnimatorDataProvider = new GlyphsAnimatorDataProvider();
        initAnimConfig();
    }

    private void initAnimConfig() {
        if (this.mGlyphsAnimatorDataProvider == null) {
            this.mGlyphsAnimatorDataProvider = new GlyphsAnimatorDataProvider();
        }
        this.glyphsAnimUtils = new GlyphsAnimUtils(getContext());
    }

    public void startAnim() {
        this.glyphsAnimUtils.setCallback(this);
        this.glyphsAnimUtils.start();
    }

    public void startAnim(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mCurrentPath = str;
        }
        this.glyphsAnimUtils.updateGlyphs(getContext(), this.mCurrentPath, new GlyphsItemView$$ExternalSyntheticLambda0(this));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnim$0(List list) {
        startAnim();
    }

    public void release() {
        this.glyphsAnimUtils.stop();
        this.glyphsAnimUtils.setCallback((GlyphsAnimUtils.onAnimatorChangedListener) null);
    }

    public void onAnimatorStart() {
        this.imgLed1.setImageResource(R$drawable.ic_default_led1);
        this.imgLed2.setImageResource(R$drawable.ic_default_led2);
        this.imgLed3.setImageResource(R$drawable.ic_default_led3);
        this.imgLed4.setImageResource(R$drawable.ic_default_led4);
        this.imgLed5.setImageResource(R$drawable.ic_default_led5);
        this.mAnimatorData = this.glyphsAnimUtils.getGlyphsAnimators();
        onAnimatorChangedListener onanimatorchangedlistener = this.mListener;
        if (onanimatorchangedlistener != null) {
            onanimatorchangedlistener.onStart(this);
        }
    }

    public void onAnimatorChanged(int i) {
        this.time = System.currentTimeMillis();
        setGlyphsDrawable(this.imgLed1, R$drawable.ic_glyphs_led1, R$drawable.ic_default_led1, this.mAnimatorData.get(i).getValue(0));
        setGlyphsDrawable(this.imgLed2, R$drawable.ic_glyphs_led2, R$drawable.ic_default_led2, this.mAnimatorData.get(i).getValue(1));
        setGlyphsDrawable(this.imgLed3, R$drawable.ic_glyphs_led3, R$drawable.ic_default_led3, this.mAnimatorData.get(i).getValue(2));
        setGlyphsDrawable(this.imgLed4, R$drawable.ic_glyphs_led4, R$drawable.ic_default_led4, this.mAnimatorData.get(i).getValue(3));
        setGlyphsDrawable(this.imgLed5, R$drawable.ic_glyphs_led5, R$drawable.ic_default_led5, this.mAnimatorData.get(i).getValue(4));
    }

    public void onAnimatorEnd() {
        this.imgLed1.setImageResource(R$drawable.ic_default_led1);
        this.imgLed2.setImageResource(R$drawable.ic_default_led2);
        this.imgLed3.setImageResource(R$drawable.ic_default_led3);
        this.imgLed4.setImageResource(R$drawable.ic_default_led4);
        this.imgLed5.setImageResource(R$drawable.ic_default_led5);
        onAnimatorChangedListener onanimatorchangedlistener = this.mListener;
        if (onanimatorchangedlistener != null) {
            onanimatorchangedlistener.onFinish(this);
        }
    }

    private void setGlyphsDrawable(ImageView imageView, int i, int i2, int i3) {
        if (i3 <= 0) {
            imageView.setImageResource(i2);
            return;
        }
        imageView.setImageResource(i);
        imageView.getDrawable().setAlpha(i3);
    }

    public void setOnAnimListener(onAnimatorChangedListener onanimatorchangedlistener) {
        this.mListener = onanimatorchangedlistener;
    }
}
