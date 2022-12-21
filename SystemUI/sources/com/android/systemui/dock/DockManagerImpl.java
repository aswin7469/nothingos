package com.android.systemui.dock;

import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dock.DockManager;

@SysUISingleton
public class DockManagerImpl implements DockManager {
    public void addAlignmentStateListener(DockManager.AlignmentStateListener alignmentStateListener) {
    }

    public void addListener(DockManager.DockEventListener dockEventListener) {
    }

    public boolean isDocked() {
        return false;
    }

    public boolean isHidden() {
        return false;
    }

    public void removeAlignmentStateListener(DockManager.AlignmentStateListener alignmentStateListener) {
    }

    public void removeListener(DockManager.DockEventListener dockEventListener) {
    }
}
