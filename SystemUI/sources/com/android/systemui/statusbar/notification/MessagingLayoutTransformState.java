package com.android.systemui.statusbar.notification;

import android.text.Layout;
import android.util.Pools;
import android.view.View;
import android.widget.TextView;
import com.android.internal.widget.IMessagingLayout;
import com.android.internal.widget.MessagingGroup;
import com.android.internal.widget.MessagingImageMessage;
import com.android.internal.widget.MessagingLinearLayout;
import com.android.internal.widget.MessagingMessage;
import com.android.internal.widget.MessagingPropertyAnimator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.TransformState;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/* loaded from: classes.dex */
public class MessagingLayoutTransformState extends TransformState {
    private static Pools.SimplePool<MessagingLayoutTransformState> sInstancePool = new Pools.SimplePool<>(40);
    private HashMap<MessagingGroup, MessagingGroup> mGroupMap = new HashMap<>();
    private MessagingLinearLayout mMessageContainer;
    private IMessagingLayout mMessagingLayout;
    private float mRelativeTranslationOffset;

    public static MessagingLayoutTransformState obtain() {
        MessagingLayoutTransformState messagingLayoutTransformState = (MessagingLayoutTransformState) sInstancePool.acquire();
        return messagingLayoutTransformState != null ? messagingLayoutTransformState : new MessagingLayoutTransformState();
    }

    @Override // com.android.systemui.statusbar.notification.TransformState
    public void initFrom(View view, TransformState.TransformInfo transformInfo) {
        super.initFrom(view, transformInfo);
        MessagingLinearLayout messagingLinearLayout = this.mTransformedView;
        if (messagingLinearLayout instanceof MessagingLinearLayout) {
            MessagingLinearLayout messagingLinearLayout2 = messagingLinearLayout;
            this.mMessageContainer = messagingLinearLayout2;
            this.mMessagingLayout = messagingLinearLayout2.getMessagingLayout();
            this.mRelativeTranslationOffset = view.getContext().getResources().getDisplayMetrics().density * 8.0f;
        }
    }

    @Override // com.android.systemui.statusbar.notification.TransformState
    public boolean transformViewTo(TransformState transformState, float f) {
        if (transformState instanceof MessagingLayoutTransformState) {
            transformViewInternal((MessagingLayoutTransformState) transformState, f, true);
            return true;
        }
        return super.transformViewTo(transformState, f);
    }

    @Override // com.android.systemui.statusbar.notification.TransformState
    public void transformViewFrom(TransformState transformState, float f) {
        if (transformState instanceof MessagingLayoutTransformState) {
            transformViewInternal((MessagingLayoutTransformState) transformState, f, false);
        } else {
            super.transformViewFrom(transformState, f);
        }
    }

    private void transformViewInternal(MessagingLayoutTransformState messagingLayoutTransformState, float f, boolean z) {
        float f2;
        float f3;
        float abs;
        ensureVisible();
        ArrayList<MessagingGroup> filterHiddenGroups = filterHiddenGroups(this.mMessagingLayout.getMessagingGroups());
        HashMap<MessagingGroup, MessagingGroup> findPairs = findPairs(filterHiddenGroups, filterHiddenGroups(messagingLayoutTransformState.mMessagingLayout.getMessagingGroups()));
        MessagingGroup messagingGroup = null;
        float f4 = 0.0f;
        for (int size = filterHiddenGroups.size() - 1; size >= 0; size--) {
            MessagingGroup messagingGroup2 = filterHiddenGroups.get(size);
            MessagingGroup messagingGroup3 = findPairs.get(messagingGroup2);
            if (!isGone(messagingGroup2)) {
                if (messagingGroup3 != null) {
                    int transformGroups = transformGroups(messagingGroup2, messagingGroup3, f, z);
                    if (messagingGroup == null) {
                        if (z) {
                            f4 = messagingGroup3.getAvatar().getTranslationY() - transformGroups;
                        } else {
                            f4 = messagingGroup2.getAvatar().getTranslationY();
                        }
                        messagingGroup = messagingGroup2;
                    }
                } else {
                    if (messagingGroup != null) {
                        adaptGroupAppear(messagingGroup2, f, f4, z);
                        float top = messagingGroup2.getTop() + f4;
                        if (!this.mTransformInfo.isAnimating()) {
                            float f5 = (-messagingGroup2.getHeight()) * 0.5f;
                            f3 = top - f5;
                            abs = Math.abs(f5);
                        } else {
                            float f6 = (-messagingGroup2.getHeight()) * 0.75f;
                            f3 = top - f6;
                            abs = Math.abs(f6) + messagingGroup2.getTop();
                        }
                        f2 = Math.max(0.0f, Math.min(1.0f, f3 / abs));
                        if (z) {
                            f2 = 1.0f - f2;
                        }
                    } else {
                        f2 = f;
                    }
                    if (z) {
                        disappear(messagingGroup2, f2);
                    } else {
                        appear(messagingGroup2, f2);
                    }
                }
            }
        }
    }

    private void appear(MessagingGroup messagingGroup, float f) {
        MessagingLinearLayout messageContainer = messagingGroup.getMessageContainer();
        for (int i = 0; i < messageContainer.getChildCount(); i++) {
            View childAt = messageContainer.getChildAt(i);
            if (!isGone(childAt)) {
                appear(childAt, f);
                setClippingDeactivated(childAt, true);
            }
        }
        appear(messagingGroup.getAvatar(), f);
        appear(messagingGroup.getSenderView(), f);
        appear((View) messagingGroup.getIsolatedMessage(), f);
        setClippingDeactivated(messagingGroup.getSenderView(), true);
        setClippingDeactivated(messagingGroup.getAvatar(), true);
    }

    private void adaptGroupAppear(MessagingGroup messagingGroup, float f, float f2, boolean z) {
        float f3;
        if (z) {
            f3 = f * this.mRelativeTranslationOffset;
        } else {
            f3 = (1.0f - f) * this.mRelativeTranslationOffset;
        }
        if (messagingGroup.getSenderView().getVisibility() != 8) {
            f3 *= 0.5f;
        }
        messagingGroup.getMessageContainer().setTranslationY(f3);
        messagingGroup.getSenderView().setTranslationY(f3);
        messagingGroup.setTranslationY(f2 * 0.9f);
    }

    private void disappear(MessagingGroup messagingGroup, float f) {
        MessagingLinearLayout messageContainer = messagingGroup.getMessageContainer();
        for (int i = 0; i < messageContainer.getChildCount(); i++) {
            View childAt = messageContainer.getChildAt(i);
            if (!isGone(childAt)) {
                disappear(childAt, f);
                setClippingDeactivated(childAt, true);
            }
        }
        disappear(messagingGroup.getAvatar(), f);
        disappear(messagingGroup.getSenderView(), f);
        disappear((View) messagingGroup.getIsolatedMessage(), f);
        setClippingDeactivated(messagingGroup.getSenderView(), true);
        setClippingDeactivated(messagingGroup.getAvatar(), true);
    }

    private void appear(View view, float f) {
        if (view == null || view.getVisibility() == 8) {
            return;
        }
        TransformState createFrom = TransformState.createFrom(view, this.mTransformInfo);
        createFrom.appear(f, null);
        createFrom.recycle();
    }

    private void disappear(View view, float f) {
        if (view == null || view.getVisibility() == 8) {
            return;
        }
        TransformState createFrom = TransformState.createFrom(view, this.mTransformInfo);
        createFrom.disappear(f, null);
        createFrom.recycle();
    }

    private ArrayList<MessagingGroup> filterHiddenGroups(ArrayList<MessagingGroup> arrayList) {
        ArrayList<MessagingGroup> arrayList2 = new ArrayList<>(arrayList);
        int i = 0;
        while (i < arrayList2.size()) {
            if (isGone(arrayList2.get(i))) {
                arrayList2.remove(i);
                i--;
            }
            i++;
        }
        return arrayList2;
    }

    private boolean hasEllipses(TextView textView) {
        Layout layout = textView.getLayout();
        return layout != null && layout.getEllipsisCount(layout.getLineCount() - 1) > 0;
    }

    private boolean needsReflow(TextView textView, TextView textView2) {
        return hasEllipses(textView) != hasEllipses(textView2);
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:42:0x00fe  */
    /* JADX WARN: Removed duplicated region for block: B:49:0x00d1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
    */
    private int transformGroups(MessagingGroup messagingGroup, MessagingGroup messagingGroup2, float f, boolean z) {
        TextView senderView;
        View view;
        float f2;
        View view2;
        int i;
        float translationY;
        boolean z2 = messagingGroup2.getIsolatedMessage() == null && !this.mTransformInfo.isAnimating();
        TextView senderView2 = messagingGroup.getSenderView();
        boolean z3 = z2;
        transformView(f, z, senderView2, messagingGroup2.getSenderView(), !needsReflow(senderView2, senderView), z3);
        int transformView = transformView(f, z, messagingGroup.getAvatar(), messagingGroup2.getAvatar(), true, z3);
        List messages = messagingGroup.getMessages();
        List messages2 = messagingGroup2.getMessages();
        boolean z4 = true;
        float f3 = 0.0f;
        int i2 = 0;
        while (i2 < messages.size()) {
            View view3 = ((MessagingMessage) messages.get((messages.size() - 1) - i2)).getView();
            if (isGone(view3)) {
                i = i2;
            } else {
                int size = (messages2.size() - 1) - i2;
                if (size >= 0) {
                    View view4 = ((MessagingMessage) messages2.get(size)).getView();
                    if (!isGone(view4)) {
                        view = view4;
                        if (view == null || f3 >= 0.0f) {
                            f2 = f;
                        } else {
                            float max = Math.max(0.0f, Math.min(1.0f, ((view3.getTop() + view3.getHeight()) + f3) / view3.getHeight()));
                            if (z) {
                                max = 1.0f - max;
                            }
                            f2 = max;
                        }
                        view2 = view;
                        i = i2;
                        int transformView2 = transformView(f2, z, view3, view, false, z2);
                        boolean z5 = messagingGroup2.getIsolatedMessage() != view2;
                        if (f2 != 0.0f && (z5 || messagingGroup2.isSingleLine())) {
                            messagingGroup.setClippingDisabled(true);
                            this.mMessagingLayout.setMessagingClippingDisabled(true);
                        }
                        if (view2 != null) {
                            if (z4) {
                                f3 = senderView2.getTranslationY();
                            }
                            translationY = f3;
                            view3.setTranslationY(translationY);
                            setClippingDeactivated(view3, true);
                        } else {
                            if (messagingGroup.getIsolatedMessage() != view3 && !z5) {
                                if (z) {
                                    f3 = view2.getTranslationY() - transformView2;
                                } else {
                                    translationY = view3.getTranslationY();
                                }
                            }
                            z4 = false;
                        }
                        f3 = translationY;
                        z4 = false;
                    }
                }
                view = null;
                if (view == null) {
                }
                f2 = f;
                view2 = view;
                i = i2;
                int transformView22 = transformView(f2, z, view3, view, false, z2);
                if (messagingGroup2.getIsolatedMessage() != view2) {
                }
                if (f2 != 0.0f) {
                }
                if (view2 != null) {
                }
                f3 = translationY;
                z4 = false;
            }
            i2 = i + 1;
        }
        messagingGroup.updateClipRect();
        return transformView;
    }

    private int transformView(float f, boolean z, View view, View view2, boolean z2, boolean z3) {
        TransformState createFrom = TransformState.createFrom(view, this.mTransformInfo);
        if (z3) {
            createFrom.setDefaultInterpolator(Interpolators.LINEAR);
        }
        int i = 0;
        createFrom.setIsSameAsAnyView(z2 && !isGone(view2));
        if (z) {
            if (view2 != null) {
                TransformState createFrom2 = TransformState.createFrom(view2, this.mTransformInfo);
                if (!isGone(view2)) {
                    createFrom.transformViewTo(createFrom2, f);
                } else {
                    if (!isGone(view)) {
                        createFrom.disappear(f, null);
                    }
                    createFrom.transformViewVerticalTo(createFrom2, f);
                }
                i = createFrom.getLaidOutLocationOnScreen()[1] - createFrom2.getLaidOutLocationOnScreen()[1];
                createFrom2.recycle();
            } else {
                createFrom.disappear(f, null);
            }
        } else if (view2 != null) {
            TransformState createFrom3 = TransformState.createFrom(view2, this.mTransformInfo);
            if (!isGone(view2)) {
                createFrom.transformViewFrom(createFrom3, f);
            } else {
                if (!isGone(view)) {
                    createFrom.appear(f, null);
                }
                createFrom.transformViewVerticalFrom(createFrom3, f);
            }
            i = createFrom.getLaidOutLocationOnScreen()[1] - createFrom3.getLaidOutLocationOnScreen()[1];
            createFrom3.recycle();
        } else {
            createFrom.appear(f, null);
        }
        createFrom.recycle();
        return i;
    }

    private HashMap<MessagingGroup, MessagingGroup> findPairs(ArrayList<MessagingGroup> arrayList, ArrayList<MessagingGroup> arrayList2) {
        this.mGroupMap.clear();
        int i = Integer.MAX_VALUE;
        for (int size = arrayList.size() - 1; size >= 0; size--) {
            MessagingGroup messagingGroup = arrayList.get(size);
            MessagingGroup messagingGroup2 = null;
            int i2 = 0;
            for (int min = Math.min(arrayList2.size(), i) - 1; min >= 0; min--) {
                MessagingGroup messagingGroup3 = arrayList2.get(min);
                int calculateGroupCompatibility = messagingGroup.calculateGroupCompatibility(messagingGroup3);
                if (calculateGroupCompatibility > i2) {
                    i = min;
                    messagingGroup2 = messagingGroup3;
                    i2 = calculateGroupCompatibility;
                }
            }
            if (messagingGroup2 != null) {
                this.mGroupMap.put(messagingGroup, messagingGroup2);
            }
        }
        return this.mGroupMap;
    }

    private boolean isGone(View view) {
        if (view == null || view.getVisibility() == 8 || view.getParent() == null || view.getWidth() == 0) {
            return true;
        }
        MessagingLinearLayout.LayoutParams layoutParams = view.getLayoutParams();
        return (layoutParams instanceof MessagingLinearLayout.LayoutParams) && layoutParams.hide;
    }

    @Override // com.android.systemui.statusbar.notification.TransformState
    public void setVisible(boolean z, boolean z2) {
        super.setVisible(z, z2);
        resetTransformedView();
        ArrayList messagingGroups = this.mMessagingLayout.getMessagingGroups();
        for (int i = 0; i < messagingGroups.size(); i++) {
            MessagingGroup messagingGroup = (MessagingGroup) messagingGroups.get(i);
            if (!isGone(messagingGroup)) {
                MessagingLinearLayout messageContainer = messagingGroup.getMessageContainer();
                for (int i2 = 0; i2 < messageContainer.getChildCount(); i2++) {
                    setVisible(messageContainer.getChildAt(i2), z, z2);
                }
                setVisible(messagingGroup.getAvatar(), z, z2);
                setVisible(messagingGroup.getSenderView(), z, z2);
                MessagingImageMessage isolatedMessage = messagingGroup.getIsolatedMessage();
                if (isolatedMessage != null) {
                    setVisible(isolatedMessage, z, z2);
                }
            }
        }
    }

    private void setVisible(View view, boolean z, boolean z2) {
        if (isGone(view) || MessagingPropertyAnimator.isAnimatingAlpha(view)) {
            return;
        }
        TransformState createFrom = TransformState.createFrom(view, this.mTransformInfo);
        createFrom.setVisible(z, z2);
        createFrom.recycle();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.TransformState
    public void resetTransformedView() {
        super.resetTransformedView();
        ArrayList messagingGroups = this.mMessagingLayout.getMessagingGroups();
        for (int i = 0; i < messagingGroups.size(); i++) {
            MessagingGroup messagingGroup = (MessagingGroup) messagingGroups.get(i);
            if (!isGone(messagingGroup)) {
                MessagingLinearLayout messageContainer = messagingGroup.getMessageContainer();
                for (int i2 = 0; i2 < messageContainer.getChildCount(); i2++) {
                    View childAt = messageContainer.getChildAt(i2);
                    if (!isGone(childAt)) {
                        resetTransformedView(childAt);
                        setClippingDeactivated(childAt, false);
                    }
                }
                resetTransformedView(messagingGroup.getAvatar());
                resetTransformedView(messagingGroup.getSenderView());
                MessagingImageMessage isolatedMessage = messagingGroup.getIsolatedMessage();
                if (isolatedMessage != null) {
                    resetTransformedView(isolatedMessage);
                }
                setClippingDeactivated(messagingGroup.getAvatar(), false);
                setClippingDeactivated(messagingGroup.getSenderView(), false);
                messagingGroup.setTranslationY(0.0f);
                messagingGroup.getMessageContainer().setTranslationY(0.0f);
                messagingGroup.getSenderView().setTranslationY(0.0f);
            }
            messagingGroup.setClippingDisabled(false);
            messagingGroup.updateClipRect();
        }
        this.mMessagingLayout.setMessagingClippingDisabled(false);
    }

    @Override // com.android.systemui.statusbar.notification.TransformState
    public void prepareFadeIn() {
        super.prepareFadeIn();
        setVisible(true, false);
    }

    private void resetTransformedView(View view) {
        TransformState createFrom = TransformState.createFrom(view, this.mTransformInfo);
        createFrom.resetTransformedView();
        createFrom.recycle();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.TransformState
    public void reset() {
        super.reset();
        this.mMessageContainer = null;
        this.mMessagingLayout = null;
    }

    @Override // com.android.systemui.statusbar.notification.TransformState
    public void recycle() {
        super.recycle();
        this.mGroupMap.clear();
        sInstancePool.release(this);
    }
}
