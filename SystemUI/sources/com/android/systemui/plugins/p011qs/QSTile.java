package com.android.systemui.plugins.p011qs;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.metrics.LogMaker;
import android.view.View;
import com.android.internal.logging.InstanceId;
import com.android.systemui.plugins.annotations.Dependencies;
import com.android.systemui.plugins.annotations.DependsOn;
import com.android.systemui.plugins.annotations.ProvidesInterface;
import java.util.ArrayList;
import java.util.function.Supplier;

@Dependencies({@DependsOn(target = QSIconView.class), @DependsOn(target = Callback.class), @DependsOn(target = Icon.class), @DependsOn(target = State.class)})
@ProvidesInterface(version = 4)
/* renamed from: com.android.systemui.plugins.qs.QSTile */
public interface QSTile {
    public static final int VERSION = 4;

    @ProvidesInterface(version = 2)
    /* renamed from: com.android.systemui.plugins.qs.QSTile$Callback */
    public interface Callback {
        public static final int VERSION = 2;

        void onStateChanged(State state);
    }

    void addCallback(Callback callback);

    @Deprecated
    void clearState() {
    }

    void click(View view);

    QSIconView createTileView(Context context);

    void destroy();

    InstanceId getInstanceId();

    @Deprecated
    int getMetricsCategory();

    State getState();

    CharSequence getTileLabel();

    String getTileSpec();

    boolean isAvailable();

    boolean isListening();

    boolean isTileReady() {
        return false;
    }

    void longClick(View view);

    LogMaker populate(LogMaker logMaker) {
        return logMaker;
    }

    void refreshState();

    void removeCallback(Callback callback);

    void removeCallbacks();

    void secondaryClick(View view);

    void setDetailListening(boolean z);

    void setListening(Object obj, boolean z);

    void setTileSpec(String str);

    void userSwitch(int i);

    String getMetricsSpec() {
        return getClass().getSimpleName();
    }

    @ProvidesInterface(version = 1)
    /* renamed from: com.android.systemui.plugins.qs.QSTile$Icon */
    public static abstract class Icon {
        public static final int VERSION = 1;
        public boolean isTesla;
        public boolean skipTintBt;

        public abstract Drawable getDrawable(Context context);

        public int getPadding() {
            return 0;
        }

        public String toString() {
            return "Icon";
        }

        public Drawable getInvisibleDrawable(Context context) {
            return getDrawable(context);
        }

        public int hashCode() {
            return Icon.class.hashCode();
        }
    }

    @ProvidesInterface(version = 1)
    /* renamed from: com.android.systemui.plugins.qs.QSTile$State */
    public static class State {
        public static final int DEFAULT_STATE = 2;
        public static final int VERSION = 1;
        public ArrayList<CharSequence> addressList;
        public CharSequence contentDescription;
        public boolean disabledByPolicy;
        public CharSequence dualLabelContentDescription;
        public boolean dualTarget = false;
        public String expandedAccessibilityClassName;
        public boolean handlesLongClick = true;
        public Icon icon;
        public ArrayList<Icon> iconList;
        public Supplier<Icon> iconSupplier;
        public boolean isTransient = false;
        public CharSequence label;
        public ArrayList<CharSequence> labelList;
        public CharSequence secondaryLabel;
        public ArrayList<CharSequence> secondaryLabelList;
        public boolean showRippleEffect = true;
        public Drawable sideViewCustomDrawable;
        public SlashState slash;
        public String spec;
        public int state = 2;
        public CharSequence stateDescription;
        public int[] stateList;

        /* JADX WARNING: Removed duplicated region for block: B:63:0x017a  */
        /* JADX WARNING: Removed duplicated region for block: B:66:0x01c4  */
        /* JADX WARNING: Removed duplicated region for block: B:67:0x01c9  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public boolean copyTo(com.android.systemui.plugins.p011qs.QSTile.State r7) {
            /*
                r6 = this;
                if (r7 == 0) goto L_0x01df
                java.lang.Class r0 = r7.getClass()
                java.lang.Class r1 = r6.getClass()
                boolean r0 = r0.equals(r1)
                if (r0 == 0) goto L_0x01d9
                java.lang.String r0 = r7.spec
                java.lang.String r1 = r6.spec
                boolean r0 = java.util.Objects.equals(r0, r1)
                r1 = 0
                r2 = 1
                if (r0 == 0) goto L_0x00ef
                com.android.systemui.plugins.qs.QSTile$Icon r0 = r7.icon
                com.android.systemui.plugins.qs.QSTile$Icon r3 = r6.icon
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                java.util.function.Supplier<com.android.systemui.plugins.qs.QSTile$Icon> r0 = r7.iconSupplier
                java.util.function.Supplier<com.android.systemui.plugins.qs.QSTile$Icon> r3 = r6.iconSupplier
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                java.lang.CharSequence r0 = r7.label
                java.lang.CharSequence r3 = r6.label
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                java.lang.CharSequence r0 = r7.secondaryLabel
                java.lang.CharSequence r3 = r6.secondaryLabel
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                java.lang.CharSequence r0 = r7.contentDescription
                java.lang.CharSequence r3 = r6.contentDescription
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                java.lang.CharSequence r0 = r7.stateDescription
                java.lang.CharSequence r3 = r6.stateDescription
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                java.lang.CharSequence r0 = r7.dualLabelContentDescription
                java.lang.CharSequence r3 = r6.dualLabelContentDescription
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                java.lang.String r0 = r7.expandedAccessibilityClassName
                java.lang.String r3 = r6.expandedAccessibilityClassName
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                boolean r0 = r7.disabledByPolicy
                java.lang.Boolean r0 = java.lang.Boolean.valueOf((boolean) r0)
                boolean r3 = r6.disabledByPolicy
                java.lang.Boolean r3 = java.lang.Boolean.valueOf((boolean) r3)
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                int r0 = r7.state
                java.lang.Integer r0 = java.lang.Integer.valueOf((int) r0)
                int r3 = r6.state
                java.lang.Integer r3 = java.lang.Integer.valueOf((int) r3)
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                boolean r0 = r7.isTransient
                java.lang.Boolean r0 = java.lang.Boolean.valueOf((boolean) r0)
                boolean r3 = r6.isTransient
                java.lang.Boolean r3 = java.lang.Boolean.valueOf((boolean) r3)
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                boolean r0 = r7.dualTarget
                java.lang.Boolean r0 = java.lang.Boolean.valueOf((boolean) r0)
                boolean r3 = r6.dualTarget
                java.lang.Boolean r3 = java.lang.Boolean.valueOf((boolean) r3)
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                com.android.systemui.plugins.qs.QSTile$SlashState r0 = r7.slash
                com.android.systemui.plugins.qs.QSTile$SlashState r3 = r6.slash
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                boolean r0 = r7.handlesLongClick
                java.lang.Boolean r0 = java.lang.Boolean.valueOf((boolean) r0)
                boolean r3 = r6.handlesLongClick
                java.lang.Boolean r3 = java.lang.Boolean.valueOf((boolean) r3)
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                boolean r0 = r7.showRippleEffect
                java.lang.Boolean r0 = java.lang.Boolean.valueOf((boolean) r0)
                boolean r3 = r6.showRippleEffect
                java.lang.Boolean r3 = java.lang.Boolean.valueOf((boolean) r3)
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 == 0) goto L_0x00ef
                android.graphics.drawable.Drawable r0 = r7.sideViewCustomDrawable
                android.graphics.drawable.Drawable r3 = r6.sideViewCustomDrawable
                boolean r0 = java.util.Objects.equals(r0, r3)
                if (r0 != 0) goto L_0x00ed
                goto L_0x00ef
            L_0x00ed:
                r0 = r1
                goto L_0x00f0
            L_0x00ef:
                r0 = r2
            L_0x00f0:
                if (r0 != 0) goto L_0x0175
                java.util.ArrayList<java.lang.CharSequence> r3 = r6.addressList
                if (r3 == 0) goto L_0x0175
                int r3 = r3.size()
                java.util.ArrayList<java.lang.CharSequence> r4 = r7.addressList
                int r4 = r4.size()
                if (r3 == r4) goto L_0x0104
                goto L_0x0176
            L_0x0104:
                java.util.ArrayList<java.lang.CharSequence> r3 = r6.addressList
                int r3 = r3.size()
                r4 = r1
            L_0x010b:
                if (r4 >= r3) goto L_0x0175
                java.util.ArrayList<com.android.systemui.plugins.qs.QSTile$Icon> r0 = r7.iconList
                java.lang.Object r0 = r0.get(r4)
                java.util.ArrayList<com.android.systemui.plugins.qs.QSTile$Icon> r5 = r6.iconList
                java.lang.Object r5 = r5.get(r4)
                boolean r0 = java.util.Objects.equals(r0, r5)
                if (r0 == 0) goto L_0x016e
                java.util.ArrayList<java.lang.CharSequence> r0 = r7.labelList
                java.lang.Object r0 = r0.get(r4)
                java.util.ArrayList<java.lang.CharSequence> r5 = r6.labelList
                java.lang.Object r5 = r5.get(r4)
                boolean r0 = java.util.Objects.equals(r0, r5)
                if (r0 == 0) goto L_0x016e
                java.util.ArrayList<java.lang.CharSequence> r0 = r7.secondaryLabelList
                java.lang.Object r0 = r0.get(r4)
                java.util.ArrayList<java.lang.CharSequence> r5 = r6.secondaryLabelList
                java.lang.Object r5 = r5.get(r4)
                boolean r0 = java.util.Objects.equals(r0, r5)
                if (r0 == 0) goto L_0x016e
                java.util.ArrayList<java.lang.CharSequence> r0 = r7.addressList
                java.lang.Object r0 = r0.get(r4)
                java.util.ArrayList<java.lang.CharSequence> r5 = r6.addressList
                java.lang.Object r5 = r5.get(r4)
                boolean r0 = java.util.Objects.equals(r0, r5)
                if (r0 == 0) goto L_0x016e
                int[] r0 = r7.stateList
                r0 = r0[r4]
                java.lang.Integer r0 = java.lang.Integer.valueOf((int) r0)
                int[] r5 = r6.stateList
                r5 = r5[r4]
                java.lang.Integer r5 = java.lang.Integer.valueOf((int) r5)
                boolean r0 = java.util.Objects.equals(r0, r5)
                if (r0 != 0) goto L_0x016c
                goto L_0x016e
            L_0x016c:
                r0 = r1
                goto L_0x016f
            L_0x016e:
                r0 = r2
            L_0x016f:
                if (r0 == 0) goto L_0x0172
                goto L_0x0175
            L_0x0172:
                int r4 = r4 + 1
                goto L_0x010b
            L_0x0175:
                r2 = r0
            L_0x0176:
                java.util.ArrayList<java.lang.CharSequence> r0 = r6.addressList
                if (r0 == 0) goto L_0x018c
                java.util.ArrayList<com.android.systemui.plugins.qs.QSTile$Icon> r1 = r6.iconList
                r7.iconList = r1
                java.util.ArrayList<java.lang.CharSequence> r1 = r6.labelList
                r7.labelList = r1
                java.util.ArrayList<java.lang.CharSequence> r1 = r6.secondaryLabelList
                r7.secondaryLabelList = r1
                r7.addressList = r0
                int[] r0 = r6.stateList
                r7.stateList = r0
            L_0x018c:
                java.lang.String r0 = r6.spec
                r7.spec = r0
                com.android.systemui.plugins.qs.QSTile$Icon r0 = r6.icon
                r7.icon = r0
                java.util.function.Supplier<com.android.systemui.plugins.qs.QSTile$Icon> r0 = r6.iconSupplier
                r7.iconSupplier = r0
                java.lang.CharSequence r0 = r6.label
                r7.label = r0
                java.lang.CharSequence r0 = r6.secondaryLabel
                r7.secondaryLabel = r0
                java.lang.CharSequence r0 = r6.contentDescription
                r7.contentDescription = r0
                java.lang.CharSequence r0 = r6.stateDescription
                r7.stateDescription = r0
                java.lang.CharSequence r0 = r6.dualLabelContentDescription
                r7.dualLabelContentDescription = r0
                java.lang.String r0 = r6.expandedAccessibilityClassName
                r7.expandedAccessibilityClassName = r0
                boolean r0 = r6.disabledByPolicy
                r7.disabledByPolicy = r0
                int r0 = r6.state
                r7.state = r0
                boolean r0 = r6.dualTarget
                r7.dualTarget = r0
                boolean r0 = r6.isTransient
                r7.isTransient = r0
                com.android.systemui.plugins.qs.QSTile$SlashState r0 = r6.slash
                if (r0 == 0) goto L_0x01c9
                com.android.systemui.plugins.qs.QSTile$SlashState r0 = r0.copy()
                goto L_0x01ca
            L_0x01c9:
                r0 = 0
            L_0x01ca:
                r7.slash = r0
                boolean r0 = r6.handlesLongClick
                r7.handlesLongClick = r0
                boolean r0 = r6.showRippleEffect
                r7.showRippleEffect = r0
                android.graphics.drawable.Drawable r6 = r6.sideViewCustomDrawable
                r7.sideViewCustomDrawable = r6
                return r2
            L_0x01d9:
                java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
                r6.<init>()
                throw r6
            L_0x01df:
                java.lang.IllegalArgumentException r6 = new java.lang.IllegalArgumentException
                r6.<init>()
                throw r6
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.plugins.p011qs.QSTile.State.copyTo(com.android.systemui.plugins.qs.QSTile$State):boolean");
        }

        public String toString() {
            return toStringBuilder().toString();
        }

        /* access modifiers changed from: protected */
        public StringBuilder toStringBuilder() {
            StringBuilder append = new StringBuilder(getClass().getSimpleName()).append("[spec=");
            append.append(this.spec);
            append.append(",icon=").append((Object) this.icon);
            append.append(",iconSupplier=").append((Object) this.iconSupplier);
            append.append(",label=").append(this.label);
            append.append(",secondaryLabel=").append(this.secondaryLabel);
            append.append(",contentDescription=").append(this.contentDescription);
            append.append(",stateDescription=").append(this.stateDescription);
            append.append(",dualLabelContentDescription=").append(this.dualLabelContentDescription);
            append.append(",expandedAccessibilityClassName=").append(this.expandedAccessibilityClassName);
            append.append(",disabledByPolicy=").append(this.disabledByPolicy);
            append.append(",dualTarget=").append(this.dualTarget);
            append.append(",isTransient=").append(this.isTransient);
            append.append(",state=").append(this.state);
            append.append(",slash=\"").append((Object) this.slash).append("\",sideViewCustomDrawable=");
            append.append((Object) this.sideViewCustomDrawable);
            return append.append(']');
        }

        public State copy() {
            State state2 = new State();
            copyTo(state2);
            return state2;
        }
    }

    @ProvidesInterface(version = 1)
    /* renamed from: com.android.systemui.plugins.qs.QSTile$BooleanState */
    public static class BooleanState extends State {
        public static final int VERSION = 1;
        public boolean forceExpandIcon;
        public boolean value;

        public boolean copyTo(State state) {
            BooleanState booleanState = (BooleanState) state;
            boolean z = (!super.copyTo(state) && booleanState.value == this.value && booleanState.forceExpandIcon == this.forceExpandIcon) ? false : true;
            booleanState.value = this.value;
            booleanState.forceExpandIcon = this.forceExpandIcon;
            return z;
        }

        /* access modifiers changed from: protected */
        public StringBuilder toStringBuilder() {
            StringBuilder stringBuilder = super.toStringBuilder();
            stringBuilder.insert(stringBuilder.length() - 1, ",value=" + this.value);
            stringBuilder.insert(stringBuilder.length() - 1, ",forceExpandIcon=" + this.forceExpandIcon);
            return stringBuilder;
        }

        public State copy() {
            BooleanState booleanState = new BooleanState();
            copyTo(booleanState);
            return booleanState;
        }
    }

    @ProvidesInterface(version = 1)
    /* renamed from: com.android.systemui.plugins.qs.QSTile$SignalState */
    public static final class SignalState extends BooleanState {
        public static final int VERSION = 1;
        public boolean activityIn;
        public boolean activityOut;
        public boolean isOverlayIconWide;
        public int overlayIconId;

        public boolean copyTo(State state) {
            SignalState signalState = (SignalState) state;
            boolean z = signalState.activityIn;
            boolean z2 = this.activityIn;
            boolean z3 = (z == z2 && signalState.activityOut == this.activityOut && signalState.isOverlayIconWide == this.isOverlayIconWide && signalState.overlayIconId == this.overlayIconId) ? false : true;
            signalState.activityIn = z2;
            signalState.activityOut = this.activityOut;
            signalState.isOverlayIconWide = this.isOverlayIconWide;
            signalState.overlayIconId = this.overlayIconId;
            if (super.copyTo(state) || z3) {
                return true;
            }
            return false;
        }

        /* access modifiers changed from: protected */
        public StringBuilder toStringBuilder() {
            StringBuilder stringBuilder = super.toStringBuilder();
            stringBuilder.insert(stringBuilder.length() - 1, ",activityIn=" + this.activityIn);
            stringBuilder.insert(stringBuilder.length() - 1, ",activityOut=" + this.activityOut);
            return stringBuilder;
        }

        public State copy() {
            SignalState signalState = new SignalState();
            copyTo(signalState);
            return signalState;
        }
    }

    @ProvidesInterface(version = 2)
    /* renamed from: com.android.systemui.plugins.qs.QSTile$SlashState */
    public static class SlashState {
        public static final int VERSION = 2;
        public boolean isSlashed;
        public float rotation;

        public String toString() {
            return "isSlashed=" + this.isSlashed + ",rotation=" + this.rotation;
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            try {
                return ((SlashState) obj).rotation == this.rotation && ((SlashState) obj).isSlashed == this.isSlashed;
            } catch (ClassCastException unused) {
                return false;
            }
        }

        public SlashState copy() {
            SlashState slashState = new SlashState();
            slashState.rotation = this.rotation;
            slashState.isSlashed = this.isSlashed;
            return slashState;
        }
    }
}
