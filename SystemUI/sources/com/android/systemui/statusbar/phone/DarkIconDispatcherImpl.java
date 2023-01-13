package com.android.systemui.statusbar.phone;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.util.ArrayMap;
import android.widget.ImageView;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import javax.inject.Inject;

@SysUISingleton
public class DarkIconDispatcherImpl implements SysuiDarkIconDispatcher, LightBarTransitionsController.DarkIntensityApplier {
    private float mDarkIntensity;
    private int mDarkModeIconColorSingleTone;
    private int mIconTint = -1;
    private int mLightModeIconColorSingleTone;
    private final ArrayMap<Object, DarkIconDispatcher.DarkReceiver> mReceivers = new ArrayMap<>();
    private final ArrayList<Rect> mTintAreas = new ArrayList<>();
    private final LightBarTransitionsController mTransitionsController;

    public int getTintAnimationDuration() {
        return 120;
    }

    @Inject
    public DarkIconDispatcherImpl(Context context, LightBarTransitionsController.Factory factory, DumpManager dumpManager) {
        this.mDarkModeIconColorSingleTone = context.getColor(C1894R.C1895color.dark_mode_icon_color_single_tone);
        this.mLightModeIconColorSingleTone = context.getColor(C1894R.C1895color.light_mode_icon_color_single_tone);
        this.mTransitionsController = factory.create(this);
        dumpManager.registerDumpable(getClass().getSimpleName(), this);
    }

    public LightBarTransitionsController getTransitionsController() {
        return this.mTransitionsController;
    }

    public void addDarkReceiver(DarkIconDispatcher.DarkReceiver darkReceiver) {
        this.mReceivers.put(darkReceiver, darkReceiver);
        darkReceiver.onDarkChanged(this.mTintAreas, this.mDarkIntensity, this.mIconTint);
    }

    public void addDarkReceiver(ImageView imageView) {
        DarkIconDispatcherImpl$$ExternalSyntheticLambda0 darkIconDispatcherImpl$$ExternalSyntheticLambda0 = new DarkIconDispatcherImpl$$ExternalSyntheticLambda0(this, imageView);
        this.mReceivers.put(imageView, darkIconDispatcherImpl$$ExternalSyntheticLambda0);
        darkIconDispatcherImpl$$ExternalSyntheticLambda0.onDarkChanged(this.mTintAreas, this.mDarkIntensity, this.mIconTint);
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addDarkReceiver$0$com-android-systemui-statusbar-phone-DarkIconDispatcherImpl */
    public /* synthetic */ void mo43978xf0c28192(ImageView imageView, ArrayList arrayList, float f, int i) {
        imageView.setImageTintList(ColorStateList.valueOf(DarkIconDispatcher.getTint(this.mTintAreas, imageView, this.mIconTint)));
    }

    public void removeDarkReceiver(DarkIconDispatcher.DarkReceiver darkReceiver) {
        this.mReceivers.remove(darkReceiver);
    }

    public void removeDarkReceiver(ImageView imageView) {
        this.mReceivers.remove(imageView);
    }

    public void applyDark(DarkIconDispatcher.DarkReceiver darkReceiver) {
        this.mReceivers.get(darkReceiver).onDarkChanged(this.mTintAreas, this.mDarkIntensity, this.mIconTint);
    }

    public void setIconsDarkArea(ArrayList<Rect> arrayList) {
        if (arrayList != null || !this.mTintAreas.isEmpty()) {
            this.mTintAreas.clear();
            if (arrayList != null) {
                this.mTintAreas.addAll(arrayList);
            }
            applyIconTint();
        }
    }

    public void applyDarkIntensity(float f) {
        this.mDarkIntensity = f;
        this.mIconTint = ((Integer) ArgbEvaluator.getInstance().evaluate(f, Integer.valueOf(this.mLightModeIconColorSingleTone), Integer.valueOf(this.mDarkModeIconColorSingleTone))).intValue();
        applyIconTint();
    }

    private void applyIconTint() {
        for (int i = 0; i < this.mReceivers.size(); i++) {
            this.mReceivers.valueAt(i).onDarkChanged(this.mTintAreas, this.mDarkIntensity, this.mIconTint);
        }
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        printWriter.println("DarkIconDispatcher: ");
        printWriter.println("  mIconTint: 0x" + Integer.toHexString(this.mIconTint));
        printWriter.println("  mDarkIntensity: " + this.mDarkIntensity + "f");
        printWriter.println("  mTintAreas: " + this.mTintAreas);
    }
}
