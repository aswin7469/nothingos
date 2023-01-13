package com.google.android.setupdesign.template;

import android.content.Context;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.partnerconfig.ResourceEntry;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.C3963R;
import com.google.android.setupdesign.GlifLayout;
import com.google.android.setupdesign.view.IllustrationVideoView;

@Deprecated
public class IllustrationProgressMixin implements Mixin {
    private final Context context;
    private final GlifLayout glifLayout;
    private ProgressConfig progressConfig = ProgressConfig.CONFIG_DEFAULT;
    private String progressDescription;

    public IllustrationProgressMixin(GlifLayout glifLayout2) {
        this.glifLayout = glifLayout2;
        this.context = glifLayout2.getContext();
    }

    public void setShown(boolean z) {
        TextView textView;
        if (!z) {
            View peekProgressIllustrationLayout = peekProgressIllustrationLayout();
            if (peekProgressIllustrationLayout != null) {
                peekProgressIllustrationLayout.setVisibility(8);
                return;
            }
            return;
        }
        View progressIllustrationLayout = getProgressIllustrationLayout();
        if (progressIllustrationLayout != null) {
            progressIllustrationLayout.setVisibility(0);
            if (this.progressDescription != null && (textView = (TextView) progressIllustrationLayout.findViewById(C3963R.C3966id.sud_layout_description)) != null) {
                textView.setVisibility(0);
                textView.setText(this.progressDescription);
            }
        }
    }

    public boolean isShown() {
        View peekProgressIllustrationLayout = peekProgressIllustrationLayout();
        return peekProgressIllustrationLayout != null && peekProgressIllustrationLayout.getVisibility() == 0;
    }

    public void setProgressConfig(ProgressConfig progressConfig2) {
        this.progressConfig = progressConfig2;
        if (peekProgressIllustrationLayout() != null) {
            setIllustrationResource();
        }
    }

    public void setProgressIllustrationDescription(String str) {
        View progressIllustrationLayout;
        this.progressDescription = str;
        if (isShown() && (progressIllustrationLayout = getProgressIllustrationLayout()) != null) {
            TextView textView = (TextView) progressIllustrationLayout.findViewById(C3963R.C3966id.sud_layout_description);
            if (str != null) {
                textView.setVisibility(0);
                textView.setText(str);
                return;
            }
            textView.setVisibility(4);
            textView.setText(str);
        }
    }

    private View getProgressIllustrationLayout() {
        ViewStub viewStub;
        if (peekProgressIllustrationLayout() == null && (viewStub = (ViewStub) this.glifLayout.findManagedViewById(C3963R.C3966id.sud_layout_illustration_progress_stub)) != null) {
            viewStub.inflate();
            setIllustrationResource();
        }
        return peekProgressIllustrationLayout();
    }

    private void setIllustrationResource() {
        IllustrationVideoView illustrationVideoView = (IllustrationVideoView) this.glifLayout.findManagedViewById(C3963R.C3966id.sud_progress_illustration);
        ProgressBar progressBar = (ProgressBar) this.glifLayout.findManagedViewById(C3963R.C3966id.sud_progress_bar);
        ResourceEntry illustrationResourceEntry = PartnerConfigHelper.get(this.context).getIllustrationResourceEntry(this.context, this.progressConfig.getPartnerConfig());
        if (illustrationResourceEntry != null) {
            progressBar.setVisibility(8);
            illustrationVideoView.setVisibility(0);
            illustrationVideoView.setVideoResourceEntry(illustrationResourceEntry);
            return;
        }
        progressBar.setVisibility(0);
        illustrationVideoView.setVisibility(8);
    }

    private View peekProgressIllustrationLayout() {
        return this.glifLayout.findViewById(C3963R.C3966id.sud_layout_progress_illustration);
    }

    public enum ProgressConfig {
        CONFIG_DEFAULT(PartnerConfig.CONFIG_PROGRESS_ILLUSTRATION_DEFAULT),
        CONFIG_ACCOUNT(PartnerConfig.CONFIG_PROGRESS_ILLUSTRATION_ACCOUNT),
        CONFIG_CONNECTION(PartnerConfig.CONFIG_PROGRESS_ILLUSTRATION_CONNECTION),
        CONFIG_UPDATE(PartnerConfig.CONFIG_PROGRESS_ILLUSTRATION_UPDATE);
        
        private final PartnerConfig config;

        private ProgressConfig(PartnerConfig partnerConfig) {
            if (partnerConfig.getResourceType() == PartnerConfig.ResourceType.ILLUSTRATION) {
                this.config = partnerConfig;
                return;
            }
            throw new IllegalArgumentException("Illustration progress only allow illustration resource");
        }

        /* access modifiers changed from: package-private */
        public PartnerConfig getPartnerConfig() {
            return this.config;
        }
    }
}
