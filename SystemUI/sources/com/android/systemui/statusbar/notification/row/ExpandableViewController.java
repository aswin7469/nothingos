package com.android.systemui.statusbar.notification.row;

import javax.inject.Inject;

public class ExpandableViewController {
    private final ExpandableView mView;

    public void init() {
    }

    @Inject
    public ExpandableViewController(ExpandableView expandableView) {
        this.mView = expandableView;
    }
}
