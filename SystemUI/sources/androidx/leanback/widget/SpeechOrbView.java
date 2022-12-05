package androidx.leanback.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import androidx.leanback.R$color;
import androidx.leanback.R$drawable;
import androidx.leanback.R$fraction;
import androidx.leanback.R$layout;
import androidx.leanback.widget.SearchOrbView;
/* loaded from: classes.dex */
public class SpeechOrbView extends SearchOrbView {
    private int mCurrentLevel;
    private boolean mListening;
    private SearchOrbView.Colors mListeningOrbColors;
    private SearchOrbView.Colors mNotListeningOrbColors;
    private final float mSoundLevelMaxZoom;

    public SpeechOrbView(Context context) {
        this(context, null);
    }

    public SpeechOrbView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SpeechOrbView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mCurrentLevel = 0;
        this.mListening = false;
        Resources resources = context.getResources();
        this.mSoundLevelMaxZoom = resources.getFraction(R$fraction.lb_search_bar_speech_orb_max_level_zoom, 1, 1);
        this.mNotListeningOrbColors = new SearchOrbView.Colors(resources.getColor(R$color.lb_speech_orb_not_recording), resources.getColor(R$color.lb_speech_orb_not_recording_pulsed), resources.getColor(R$color.lb_speech_orb_not_recording_icon));
        int i = R$color.lb_speech_orb_recording;
        this.mListeningOrbColors = new SearchOrbView.Colors(resources.getColor(i), resources.getColor(i), 0);
        showNotListening();
    }

    @Override // androidx.leanback.widget.SearchOrbView
    int getLayoutResourceId() {
        return R$layout.lb_speech_orb;
    }

    public void showListening() {
        setOrbColors(this.mListeningOrbColors);
        setOrbIcon(getResources().getDrawable(R$drawable.lb_ic_search_mic));
        animateOnFocus(true);
        enableOrbColorAnimation(false);
        scaleOrbViewOnly(1.0f);
        this.mCurrentLevel = 0;
        this.mListening = true;
    }

    public void showNotListening() {
        setOrbColors(this.mNotListeningOrbColors);
        setOrbIcon(getResources().getDrawable(R$drawable.lb_ic_search_mic_out));
        animateOnFocus(hasFocus());
        scaleOrbViewOnly(1.0f);
        this.mListening = false;
    }

    public void setSoundLevel(int level) {
        if (!this.mListening) {
            return;
        }
        int i = this.mCurrentLevel;
        if (level > i) {
            this.mCurrentLevel = i + ((level - i) / 2);
        } else {
            this.mCurrentLevel = (int) (i * 0.7f);
        }
        scaleOrbViewOnly((((this.mSoundLevelMaxZoom - getFocusedZoom()) * this.mCurrentLevel) / 100.0f) + 1.0f);
    }
}
