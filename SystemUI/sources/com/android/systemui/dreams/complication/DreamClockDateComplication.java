package com.android.systemui.dreams.complication;

import android.content.Context;
import android.view.View;
import com.android.systemui.CoreStartable;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.complication.Complication;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

public class DreamClockDateComplication implements Complication {
    private final Provider<DreamClockDateViewHolder> mDreamClockDateViewHolderProvider;

    public int getRequiredTypeAvailability() {
        return 2;
    }

    @Inject
    public DreamClockDateComplication(Provider<DreamClockDateViewHolder> provider) {
        this.mDreamClockDateViewHolderProvider = provider;
    }

    public Complication.ViewHolder createView(ComplicationViewModel complicationViewModel) {
        return this.mDreamClockDateViewHolderProvider.get();
    }

    public static class Registrant extends CoreStartable {
        private final DreamClockDateComplication mComplication;
        private final DreamOverlayStateController mDreamOverlayStateController;

        @Inject
        public Registrant(Context context, DreamOverlayStateController dreamOverlayStateController, DreamClockDateComplication dreamClockDateComplication) {
            super(context);
            this.mDreamOverlayStateController = dreamOverlayStateController;
            this.mComplication = dreamClockDateComplication;
        }

        public void start() {
            this.mDreamOverlayStateController.addComplication(this.mComplication);
        }
    }

    public static class DreamClockDateViewHolder implements Complication.ViewHolder {
        private final ComplicationLayoutParams mLayoutParams;
        private final View mView;

        @Inject
        DreamClockDateViewHolder(@Named("clock_date_complication_view") View view, @Named("clock_date_complication_layout_params") ComplicationLayoutParams complicationLayoutParams) {
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
