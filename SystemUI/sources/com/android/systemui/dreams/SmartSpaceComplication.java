package com.android.systemui.dreams;

import android.content.Context;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.android.systemui.CoreStartable;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.Complication;
import com.android.systemui.dreams.complication.ComplicationLayoutParams;
import com.android.systemui.dreams.complication.ComplicationViewModel;
import com.android.systemui.dreams.smartspace.DreamsSmartspaceController;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import java.util.List;
import javax.inject.Inject;

public class SmartSpaceComplication implements Complication {
    private final Context mContext;
    private final DreamsSmartspaceController mSmartSpaceController;

    public static class Registrant extends CoreStartable {
        /* access modifiers changed from: private */
        public final SmartSpaceComplication mComplication;
        /* access modifiers changed from: private */
        public final DreamOverlayStateController mDreamOverlayStateController;
        /* access modifiers changed from: private */
        public final DreamsSmartspaceController mSmartSpaceController;
        /* access modifiers changed from: private */
        public final BcSmartspaceDataPlugin.SmartspaceTargetListener mSmartspaceListener = new BcSmartspaceDataPlugin.SmartspaceTargetListener() {
            public void onSmartspaceTargetsUpdated(List<? extends Parcelable> list) {
                if (!list.isEmpty()) {
                    Registrant.this.mDreamOverlayStateController.addComplication(Registrant.this.mComplication);
                } else {
                    Registrant.this.mDreamOverlayStateController.removeComplication(Registrant.this.mComplication);
                }
            }
        };

        @Inject
        public Registrant(Context context, DreamOverlayStateController dreamOverlayStateController, SmartSpaceComplication smartSpaceComplication, DreamsSmartspaceController dreamsSmartspaceController) {
            super(context);
            this.mDreamOverlayStateController = dreamOverlayStateController;
            this.mComplication = smartSpaceComplication;
            this.mSmartSpaceController = dreamsSmartspaceController;
        }

        public void start() {
            this.mDreamOverlayStateController.addCallback((DreamOverlayStateController.Callback) new DreamOverlayStateController.Callback() {
                public void onStateChanged() {
                    if (Registrant.this.mDreamOverlayStateController.isOverlayActive()) {
                        Registrant.this.mSmartSpaceController.addListener(Registrant.this.mSmartspaceListener);
                    } else {
                        Registrant.this.mSmartSpaceController.removeListener(Registrant.this.mSmartspaceListener);
                    }
                }
            });
        }
    }

    private static class SmartSpaceComplicationViewHolder implements Complication.ViewHolder {
        private static final int SMARTSPACE_COMPLICATION_WEIGHT = 10;
        private final Context mContext;
        private final DreamsSmartspaceController mSmartSpaceController;

        protected SmartSpaceComplicationViewHolder(Context context, DreamsSmartspaceController dreamsSmartspaceController) {
            this.mSmartSpaceController = dreamsSmartspaceController;
            this.mContext = context;
        }

        public View getView() {
            FrameLayout frameLayout = new FrameLayout(this.mContext);
            frameLayout.addView(this.mSmartSpaceController.buildAndConnectView(frameLayout), new ViewGroup.LayoutParams(-1, -2));
            return frameLayout;
        }

        public ComplicationLayoutParams getLayoutParams() {
            return new ComplicationLayoutParams(0, -2, 5, 2, 10, true);
        }
    }

    @Inject
    public SmartSpaceComplication(Context context, DreamsSmartspaceController dreamsSmartspaceController) {
        this.mContext = context;
        this.mSmartSpaceController = dreamsSmartspaceController;
    }

    public Complication.ViewHolder createView(ComplicationViewModel complicationViewModel) {
        return new SmartSpaceComplicationViewHolder(this.mContext, this.mSmartSpaceController);
    }
}
