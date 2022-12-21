package com.android.systemui.statusbar.notification.row;

import javax.inject.Inject;

public class ExpandableOutlineViewController {
    private final ExpandableViewController mExpandableViewController;
    private final ExpandableOutlineView mView;

    @Inject
    public ExpandableOutlineViewController(ExpandableOutlineView expandableOutlineView, ExpandableViewController expandableViewController) {
        this.mView = expandableOutlineView;
        this.mExpandableViewController = expandableViewController;
    }

    public void init() {
        this.mExpandableViewController.init();
    }
}
