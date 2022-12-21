package com.android.p019wm.shell;

import android.util.SparseArray;
import android.view.SurfaceControl;
import android.window.DisplayAreaAppearedInfo;
import android.window.DisplayAreaInfo;
import android.window.DisplayAreaOrganizer;
import java.p026io.PrintWriter;
import java.util.List;
import java.util.concurrent.Executor;

/* renamed from: com.android.wm.shell.RootDisplayAreaOrganizer */
public class RootDisplayAreaOrganizer extends DisplayAreaOrganizer {
    private static final String TAG = "RootDisplayAreaOrganizer";
    private final SparseArray<DisplayAreaInfo> mDisplayAreasInfo = new SparseArray<>();
    private final SparseArray<SurfaceControl> mLeashes = new SparseArray<>();

    public RootDisplayAreaOrganizer(Executor executor) {
        super(executor);
        List registerOrganizer = registerOrganizer(0);
        for (int size = registerOrganizer.size() - 1; size >= 0; size--) {
            onDisplayAreaAppeared(((DisplayAreaAppearedInfo) registerOrganizer.get(size)).getDisplayAreaInfo(), ((DisplayAreaAppearedInfo) registerOrganizer.get(size)).getLeash());
        }
    }

    public void attachToDisplayArea(int i, SurfaceControl.Builder builder) {
        SurfaceControl surfaceControl = this.mLeashes.get(i);
        if (surfaceControl != null) {
            builder.setParent(surfaceControl);
        }
    }

    public void onDisplayAreaAppeared(DisplayAreaInfo displayAreaInfo, SurfaceControl surfaceControl) {
        if (displayAreaInfo.featureId == 0) {
            int i = displayAreaInfo.displayId;
            if (this.mDisplayAreasInfo.get(i) == null) {
                this.mDisplayAreasInfo.put(i, displayAreaInfo);
                this.mLeashes.put(i, surfaceControl);
                return;
            }
            throw new IllegalArgumentException("Duplicate DA for displayId: " + i + " displayAreaInfo:" + displayAreaInfo + " mDisplayAreasInfo.get():" + this.mDisplayAreasInfo.get(i));
        }
        throw new IllegalArgumentException("Unknown feature: " + displayAreaInfo.featureId + "displayAreaInfo:" + displayAreaInfo);
    }

    public void onDisplayAreaVanished(DisplayAreaInfo displayAreaInfo) {
        int i = displayAreaInfo.displayId;
        if (this.mDisplayAreasInfo.get(i) != null) {
            this.mDisplayAreasInfo.remove(i);
            return;
        }
        throw new IllegalArgumentException("onDisplayAreaVanished() Unknown DA displayId: " + i + " displayAreaInfo:" + displayAreaInfo + " mDisplayAreasInfo.get():" + this.mDisplayAreasInfo.get(i));
    }

    public void onDisplayAreaInfoChanged(DisplayAreaInfo displayAreaInfo) {
        int i = displayAreaInfo.displayId;
        if (this.mDisplayAreasInfo.get(i) != null) {
            this.mDisplayAreasInfo.put(i, displayAreaInfo);
            return;
        }
        throw new IllegalArgumentException("onDisplayAreaInfoChanged() Unknown DA displayId: " + i + " displayAreaInfo:" + displayAreaInfo + " mDisplayAreasInfo.get():" + this.mDisplayAreasInfo.get(i));
    }

    public void dump(PrintWriter printWriter, String str) {
        (str + "  ") + "  ";
        printWriter.println(str + this);
    }

    public String toString() {
        return TAG + "#" + this.mDisplayAreasInfo.size();
    }
}
