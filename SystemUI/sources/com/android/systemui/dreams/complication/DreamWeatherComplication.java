package com.android.systemui.dreams.complication;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Parcelable;
import android.text.TextUtils;
import android.widget.TextView;
import com.android.systemui.C1894R;
import com.android.systemui.CoreStartable;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.dagger.DreamWeatherComplicationComponent;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController;
import com.android.systemui.util.ViewController;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Named;

public class DreamWeatherComplication implements Complication {
    DreamWeatherComplicationComponent.Factory mComponentFactory;

    public int getRequiredTypeAvailability() {
        return 4;
    }

    @Inject
    public DreamWeatherComplication(DreamWeatherComplicationComponent.Factory factory) {
        this.mComponentFactory = factory;
    }

    public Complication.ViewHolder createView(ComplicationViewModel complicationViewModel) {
        return this.mComponentFactory.create().getViewHolder();
    }

    public static class Registrant extends CoreStartable {
        private final DreamWeatherComplication mComplication;
        private final DreamOverlayStateController mDreamOverlayStateController;
        private final LockscreenSmartspaceController mSmartSpaceController;

        @Inject
        public Registrant(Context context, LockscreenSmartspaceController lockscreenSmartspaceController, DreamOverlayStateController dreamOverlayStateController, DreamWeatherComplication dreamWeatherComplication) {
            super(context);
            this.mSmartSpaceController = lockscreenSmartspaceController;
            this.mDreamOverlayStateController = dreamOverlayStateController;
            this.mComplication = dreamWeatherComplication;
        }

        public void start() {
            if (this.mSmartSpaceController.isEnabled()) {
                this.mDreamOverlayStateController.addComplication(this.mComplication);
            }
        }
    }

    public static class DreamWeatherViewHolder implements Complication.ViewHolder {
        private final ComplicationLayoutParams mLayoutParams;
        private final TextView mView;
        private final DreamWeatherViewController mViewController;

        @Inject
        DreamWeatherViewHolder(@Named("weather_complication_view") TextView textView, DreamWeatherViewController dreamWeatherViewController, @Named("weather_complication_layout_params") ComplicationLayoutParams complicationLayoutParams) {
            this.mView = textView;
            this.mLayoutParams = complicationLayoutParams;
            this.mViewController = dreamWeatherViewController;
            dreamWeatherViewController.init();
        }

        public TextView getView() {
            return this.mView;
        }

        public ComplicationLayoutParams getLayoutParams() {
            return this.mLayoutParams;
        }
    }

    static class DreamWeatherViewController extends ViewController<TextView> {
        private final LockscreenSmartspaceController mSmartSpaceController;
        private BcSmartspaceDataPlugin.SmartspaceTargetListener mSmartspaceTargetListener;

        @Inject
        DreamWeatherViewController(@Named("weather_complication_view") TextView textView, LockscreenSmartspaceController lockscreenSmartspaceController) {
            super(textView);
            this.mSmartSpaceController = lockscreenSmartspaceController;
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onViewAttached$1$com-android-systemui-dreams-complication-DreamWeatherComplication$DreamWeatherViewController */
        public /* synthetic */ void mo32612x5a753507(List list) {
            list.forEach(new C2090xbf1b0f77(this));
        }

        /* access modifiers changed from: protected */
        public void onViewAttached() {
            C2089xbf1b0f76 dreamWeatherComplication$DreamWeatherViewController$$ExternalSyntheticLambda0 = new C2089xbf1b0f76(this);
            this.mSmartspaceTargetListener = dreamWeatherComplication$DreamWeatherViewController$$ExternalSyntheticLambda0;
            this.mSmartSpaceController.addListener(dreamWeatherComplication$DreamWeatherViewController$$ExternalSyntheticLambda0);
        }

        /* access modifiers changed from: package-private */
        /* renamed from: lambda$onViewAttached$0$com-android-systemui-dreams-complication-DreamWeatherComplication$DreamWeatherViewController */
        public /* synthetic */ void mo32611x8ab50168(Parcelable parcelable) {
            SmartspaceAction headerAction;
            if (parcelable instanceof SmartspaceTarget) {
                SmartspaceTarget smartspaceTarget = (SmartspaceTarget) parcelable;
                if (smartspaceTarget.getFeatureType() == 1 && (headerAction = smartspaceTarget.getHeaderAction()) != null && !TextUtils.isEmpty(headerAction.getTitle())) {
                    ((TextView) this.mView).setText(headerAction.getTitle().toString());
                    Icon icon = headerAction.getIcon();
                    if (icon != null) {
                        int dimensionPixelSize = getResources().getDimensionPixelSize(C1894R.dimen.smart_action_button_icon_size);
                        Drawable loadDrawable = icon.loadDrawable(getContext());
                        loadDrawable.setBounds(0, 0, dimensionPixelSize, dimensionPixelSize);
                        ((TextView) this.mView).setCompoundDrawables(loadDrawable, (Drawable) null, (Drawable) null, (Drawable) null);
                        ((TextView) this.mView).setCompoundDrawablePadding(getResources().getDimensionPixelSize(C1894R.dimen.smart_action_button_icon_padding));
                    }
                }
            }
        }

        /* access modifiers changed from: protected */
        public void onViewDetached() {
            this.mSmartSpaceController.removeListener(this.mSmartspaceTargetListener);
        }
    }
}
