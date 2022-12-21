package androidx.leanback.widget;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.leanback.C0742R;
import androidx.leanback.widget.Presenter;

public class ControlButtonPresenterSelector extends PresenterSelector {
    private final Presenter[] mPresenters;
    private final Presenter mPrimaryPresenter;
    private final Presenter mSecondaryPresenter = new ControlButtonPresenter(C0742R.layout.lb_control_button_secondary);

    public ControlButtonPresenterSelector() {
        ControlButtonPresenter controlButtonPresenter = new ControlButtonPresenter(C0742R.layout.lb_control_button_primary);
        this.mPrimaryPresenter = controlButtonPresenter;
        this.mPresenters = new Presenter[]{controlButtonPresenter};
    }

    public Presenter getPrimaryPresenter() {
        return this.mPrimaryPresenter;
    }

    public Presenter getSecondaryPresenter() {
        return this.mSecondaryPresenter;
    }

    public Presenter getPresenter(Object obj) {
        return this.mPrimaryPresenter;
    }

    public Presenter[] getPresenters() {
        return this.mPresenters;
    }

    static class ActionViewHolder extends Presenter.ViewHolder {
        View mFocusableView;
        ImageView mIcon;
        TextView mLabel;

        public ActionViewHolder(View view) {
            super(view);
            this.mIcon = (ImageView) view.findViewById(C0742R.C0745id.icon);
            this.mLabel = (TextView) view.findViewById(C0742R.C0745id.label);
            this.mFocusableView = view.findViewById(C0742R.C0745id.button);
        }
    }

    static class ControlButtonPresenter extends Presenter {
        private int mLayoutResourceId;

        ControlButtonPresenter(int i) {
            this.mLayoutResourceId = i;
        }

        public Presenter.ViewHolder onCreateViewHolder(ViewGroup viewGroup) {
            return new ActionViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(this.mLayoutResourceId, viewGroup, false));
        }

        public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object obj) {
            Action action = (Action) obj;
            ActionViewHolder actionViewHolder = (ActionViewHolder) viewHolder;
            actionViewHolder.mIcon.setImageDrawable(action.getIcon());
            if (actionViewHolder.mLabel != null) {
                if (action.getIcon() == null) {
                    actionViewHolder.mLabel.setText(action.getLabel1());
                } else {
                    actionViewHolder.mLabel.setText((CharSequence) null);
                }
            }
            CharSequence label1 = TextUtils.isEmpty(action.getLabel2()) ? action.getLabel1() : action.getLabel2();
            if (!TextUtils.equals(actionViewHolder.mFocusableView.getContentDescription(), label1)) {
                actionViewHolder.mFocusableView.setContentDescription(label1);
                actionViewHolder.mFocusableView.sendAccessibilityEvent(32768);
            }
        }

        public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
            ActionViewHolder actionViewHolder = (ActionViewHolder) viewHolder;
            actionViewHolder.mIcon.setImageDrawable((Drawable) null);
            if (actionViewHolder.mLabel != null) {
                actionViewHolder.mLabel.setText((CharSequence) null);
            }
            actionViewHolder.mFocusableView.setContentDescription((CharSequence) null);
        }

        public void setOnClickListener(Presenter.ViewHolder viewHolder, View.OnClickListener onClickListener) {
            ((ActionViewHolder) viewHolder).mFocusableView.setOnClickListener(onClickListener);
        }
    }
}
