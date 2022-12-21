package com.android.settingslib;

import com.android.systemui.navigationbar.NavigationBarInflaterView;

public class SignalIcon {

    public static class IconGroup {
        public final int[] contentDesc;
        public final int discContentDesc;
        public final String name;
        public final int qsDiscState;
        public final int[][] qsIcons;
        public final int qsNullState;
        public final int sbDiscState;
        public final int[][] sbIcons;
        public final int sbNullState;

        public IconGroup(String str, int[][] iArr, int[][] iArr2, int[] iArr3, int i, int i2, int i3, int i4, int i5) {
            this.name = str;
            this.sbIcons = iArr;
            this.qsIcons = iArr2;
            this.contentDesc = iArr3;
            this.sbNullState = i;
            this.qsNullState = i2;
            this.sbDiscState = i3;
            this.qsDiscState = i4;
            this.discContentDesc = i5;
        }

        public String toString() {
            return "IconGroup(" + this.name + NavigationBarInflaterView.KEY_CODE_END;
        }
    }

    public static class MobileIconGroup extends IconGroup {
        public final int dataContentDescription;
        public final int dataType;

        public MobileIconGroup(String str, int[][] iArr, int[][] iArr2, int[] iArr3, int i, int i2, int i3, int i4, int i5, int i6, int i7) {
            super(str, iArr, iArr2, iArr3, i, i2, i3, i4, i5);
            this.dataContentDescription = i6;
            this.dataType = i7;
        }
    }
}
