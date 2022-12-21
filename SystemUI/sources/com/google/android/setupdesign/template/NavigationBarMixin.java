package com.google.android.setupdesign.template;

import android.view.View;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.template.Mixin;
import com.google.android.setupdesign.C3953R;
import com.google.android.setupdesign.view.NavigationBar;

public class NavigationBarMixin implements Mixin {
    private final TemplateLayout templateLayout;

    public NavigationBarMixin(TemplateLayout templateLayout2) {
        this.templateLayout = templateLayout2;
    }

    public NavigationBar getNavigationBar() {
        View findManagedViewById = this.templateLayout.findManagedViewById(C3953R.C3956id.sud_layout_navigation_bar);
        if (findManagedViewById instanceof NavigationBar) {
            return (NavigationBar) findManagedViewById;
        }
        return null;
    }

    public void setNextButtonText(int i) {
        getNavigationBar().getNextButton().setText(i);
    }

    public void setNextButtonText(CharSequence charSequence) {
        getNavigationBar().getNextButton().setText(charSequence);
    }

    public CharSequence getNextButtonText() {
        return getNavigationBar().getNextButton().getText();
    }

    public void setNavigationBarListener(NavigationBar.NavigationBarListener navigationBarListener) {
        getNavigationBar().setNavigationBarListener(navigationBarListener);
    }
}
