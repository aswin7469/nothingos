package com.android.settings.display;

import com.android.settings.display.PreviewSeekBarPreferenceFragment;

/* renamed from: com.android.settings.display.PreviewSeekBarPreferenceFragment$onPreviewSeekBarChangeListener$$ExternalSyntheticLambda0 */
/* compiled from: R8$$SyntheticClass */
public final /* synthetic */ class C0927x84d1f24b implements Runnable {
    public final /* synthetic */ PreviewSeekBarPreferenceFragment.onPreviewSeekBarChangeListener f$0;

    public /* synthetic */ C0927x84d1f24b(PreviewSeekBarPreferenceFragment.onPreviewSeekBarChangeListener onpreviewseekbarchangelistener) {
        this.f$0 = onpreviewseekbarchangelistener;
    }

    public final void run() {
        this.f$0.commitOnNextFrame();
    }
}
