package com.android.systemui.dreams.complication;

import android.content.Context;
import android.view.View;
import com.android.systemui.CoreStartable;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.Complication;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class DreamClockTimeComplication implements Complication {
    private final Provider<DreamClockTimeViewHolder> mDreamClockTimeViewHolderProvider;

    public int getRequiredTypeAvailability() {
        return 1;
    }

    @Inject
    public DreamClockTimeComplication(Provider<DreamClockTimeViewHolder> provider) {
        this.mDreamClockTimeViewHolderProvider = provider;
    }

    public Complication.ViewHolder createView(ComplicationViewModel complicationViewModel) {
        return this.mDreamClockTimeViewHolderProvider.get();
    }

    public static class Registrant extends CoreStartable {
        private final DreamClockTimeComplication mComplication;
        private final DreamOverlayStateController mDreamOverlayStateController;

        @Inject
        public Registrant(Context context, DreamOverlayStateController dreamOverlayStateController, DreamClockTimeComplication dreamClockTimeComplication) {
            super(context);
            this.mDreamOverlayStateController = dreamOverlayStateController;
            this.mComplication = dreamClockTimeComplication;
        }

        public void start() {
            this.mDreamOverlayStateController.addComplication(this.mComplication);
        }
    }

    public static class DreamClockTimeViewHolder implements Complication.ViewHolder {
        private final ComplicationLayoutParams mLayoutParams;
        private final View mView;

        @Inject
        DreamClockTimeViewHolder(@Named("clock_time_complication_view") View view, @Named("clock_time_complication_layout_params") ComplicationLayoutParams complicationLayoutParams) {
            this.mView = view;
            this.mLayoutParams = complicationLayoutParams;
        }

        public View getView() {
            return this.mView;
        }

        public ComplicationLayoutParams getLayoutParams() {
            return this.mLayoutParams;
        }
    }
}
