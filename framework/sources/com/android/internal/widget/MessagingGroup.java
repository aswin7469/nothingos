package com.android.internal.widget;

import android.app.Person;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Icon;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Pools;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import com.android.internal.R;
import com.android.internal.widget.MessagingLinearLayout;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
@RemoteViews.RemoteView
/* loaded from: classes4.dex */
public class MessagingGroup extends LinearLayout implements MessagingLinearLayout.MessagingChild {
    public static final int IMAGE_DISPLAY_LOCATION_AT_END = 1;
    public static final int IMAGE_DISPLAY_LOCATION_EXTERNAL = 2;
    public static final int IMAGE_DISPLAY_LOCATION_INLINE = 0;
    private static Pools.SimplePool<MessagingGroup> sInstancePool = new Pools.SynchronizedPool(10);
    private View mAvatarContainer;
    private Icon mAvatarIcon;
    private ImageView mAvatarView;
    private boolean mCanHideSenderIfFirst;
    private boolean mClippingDisabled;
    private LinearLayout mContentContainer;
    private int mConversationAvatarSize;
    private int mConversationContentStart;
    private boolean mFirstLayout;
    private ViewGroup mImageContainer;
    private int mImageDisplayLocation;
    private boolean mIsHidingAnimated;
    private MessagingImageMessage mIsolatedMessage;
    private int mLayoutColor;
    private MessagingLinearLayout mMessageContainer;
    private List<MessagingMessage> mMessages;
    private ViewGroup mMessagingIconContainer;
    private boolean mNeedsGeneratedAvatar;
    private int mNonConversationAvatarSize;
    private int mNonConversationContentStart;
    private int mNonConversationPaddingStart;
    private int mNotificationTextMarginTop;
    private Person mSender;
    private CharSequence mSenderName;
    private int mSenderTextPaddingSingleLine;
    ImageFloatingTextView mSenderView;
    private ProgressBar mSendingSpinner;
    private View mSendingSpinnerContainer;
    private int mSendingTextColor;
    private int mTextColor;
    private String mAvatarSymbol = "";
    private CharSequence mAvatarName = "";
    private ArrayList<MessagingMessage> mAddedMessages = new ArrayList<>();
    private Point mDisplaySize = new Point();
    private boolean mShowingAvatar = true;
    private boolean mSingleLine = false;
    private int mRequestedMaxDisplayedLines = Integer.MAX_VALUE;
    private boolean mIsFirstGroupInLayout = true;
    private boolean mIsInConversation = true;

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes4.dex */
    private @interface ImageDisplayLocation {
    }

    public MessagingGroup(Context context) {
        super(context);
    }

    public MessagingGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MessagingGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public MessagingGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mMessageContainer = (MessagingLinearLayout) findViewById(R.id.group_message_container);
        this.mSenderView = (ImageFloatingTextView) findViewById(R.id.message_name);
        this.mAvatarView = (ImageView) findViewById(R.id.message_icon);
        this.mImageContainer = (ViewGroup) findViewById(R.id.messaging_group_icon_container);
        this.mSendingSpinner = (ProgressBar) findViewById(R.id.messaging_group_sending_progress);
        this.mMessagingIconContainer = (ViewGroup) findViewById(R.id.message_icon_container);
        this.mContentContainer = (LinearLayout) findViewById(R.id.messaging_group_content_container);
        this.mSendingSpinnerContainer = findViewById(R.id.messaging_group_sending_progress_container);
        Resources res = getResources();
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        this.mDisplaySize.x = displayMetrics.widthPixels;
        this.mDisplaySize.y = displayMetrics.heightPixels;
        this.mSenderTextPaddingSingleLine = res.getDimensionPixelSize(R.dimen.messaging_group_singleline_sender_padding_end);
        this.mConversationContentStart = res.getDimensionPixelSize(R.dimen.conversation_content_start);
        this.mNonConversationContentStart = res.getDimensionPixelSize(R.dimen.notification_content_margin_start);
        this.mNonConversationPaddingStart = res.getDimensionPixelSize(R.dimen.messaging_layout_icon_padding_start);
        this.mConversationAvatarSize = res.getDimensionPixelSize(R.dimen.messaging_avatar_size);
        this.mNonConversationAvatarSize = res.getDimensionPixelSize(R.dimen.notification_icon_circle_size);
        this.mNotificationTextMarginTop = res.getDimensionPixelSize(R.dimen.notification_text_margin_top);
    }

    public void updateClipRect() {
        Rect clipRect;
        int top;
        if (this.mSenderView.getVisibility() != 8 && !this.mClippingDisabled) {
            if (this.mSingleLine) {
                top = 0;
            } else {
                top = (getDistanceFromParent(this.mSenderView, this.mContentContainer) - getDistanceFromParent(this.mMessageContainer, this.mContentContainer)) + this.mSenderView.getHeight();
            }
            int size = Math.max(this.mDisplaySize.x, this.mDisplaySize.y);
            clipRect = new Rect(-size, top, size, size);
        } else {
            clipRect = null;
        }
        this.mMessageContainer.setClipBounds(clipRect);
    }

    private int getDistanceFromParent(View searchedView, ViewGroup parent) {
        int position = 0;
        for (View view = searchedView; view != parent; view = (View) view.getParent()) {
            position = (int) (position + view.getTop() + view.getTranslationY());
        }
        return position;
    }

    public void setSender(Person sender, CharSequence nameOverride) {
        this.mSender = sender;
        if (nameOverride == null) {
            nameOverride = sender.getName();
        }
        this.mSenderName = nameOverride;
        boolean z = true;
        if (this.mSingleLine && !TextUtils.isEmpty(nameOverride)) {
            nameOverride = this.mContext.getResources().getString(R.string.conversation_single_line_name_display, nameOverride);
        }
        this.mSenderView.setText(nameOverride);
        if (sender.getIcon() != null) {
            z = false;
        }
        this.mNeedsGeneratedAvatar = z;
        if (!z) {
            setAvatar(sender.getIcon());
        }
        updateSenderVisibility();
    }

    public void setShowingAvatar(boolean showingAvatar) {
        this.mAvatarView.setVisibility(showingAvatar ? 0 : 8);
        this.mShowingAvatar = showingAvatar;
    }

    public void setSending(boolean sending) {
        int visibility = sending ? 0 : 8;
        if (this.mSendingSpinnerContainer.getVisibility() != visibility) {
            this.mSendingSpinnerContainer.setVisibility(visibility);
            updateMessageColor();
        }
    }

    private int calculateSendingTextColor() {
        TypedValue alphaValue = new TypedValue();
        this.mContext.getResources().getValue(R.dimen.notification_secondary_text_disabled_alpha, alphaValue, true);
        float alpha = alphaValue.getFloat();
        return Color.valueOf(Color.red(this.mTextColor), Color.green(this.mTextColor), Color.blue(this.mTextColor), alpha).toArgb();
    }

    public void setAvatar(Icon icon) {
        this.mAvatarIcon = icon;
        if (this.mShowingAvatar || icon == null) {
            this.mAvatarView.setImageIcon(icon);
        }
        this.mAvatarSymbol = "";
        this.mAvatarName = "";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static MessagingGroup createGroup(MessagingLinearLayout layout) {
        MessagingGroup createdGroup = sInstancePool.acquire();
        if (createdGroup == null) {
            createdGroup = (MessagingGroup) LayoutInflater.from(layout.getContext()).inflate(R.layout.notification_template_messaging_group, (ViewGroup) layout, false);
            createdGroup.addOnLayoutChangeListener(MessagingLayout.MESSAGING_PROPERTY_ANIMATOR);
        }
        layout.addView(createdGroup);
        return createdGroup;
    }

    public void removeMessage(final MessagingMessage messagingMessage) {
        final View view = messagingMessage.getView();
        boolean wasShown = view.isShown();
        final ViewGroup messageParent = (ViewGroup) view.getParent();
        if (messageParent == null) {
            return;
        }
        messageParent.removeView(view);
        Runnable recycleRunnable = new Runnable() { // from class: com.android.internal.widget.MessagingGroup$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                MessagingGroup.lambda$removeMessage$0(ViewGroup.this, view, messagingMessage);
            }
        };
        if (wasShown && !MessagingLinearLayout.isGone(view)) {
            messageParent.addTransientView(view, 0);
            performRemoveAnimation(view, recycleRunnable);
            return;
        }
        recycleRunnable.run();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static /* synthetic */ void lambda$removeMessage$0(ViewGroup messageParent, View view, MessagingMessage messagingMessage) {
        messageParent.removeTransientView(view);
        messagingMessage.recycle();
    }

    public void recycle() {
        MessagingImageMessage messagingImageMessage = this.mIsolatedMessage;
        if (messagingImageMessage != null) {
            this.mImageContainer.removeView(messagingImageMessage);
        }
        for (int i = 0; i < this.mMessages.size(); i++) {
            MessagingMessage message = this.mMessages.get(i);
            this.mMessageContainer.removeView(message.getView());
            message.recycle();
        }
        setAvatar(null);
        this.mAvatarView.setAlpha(1.0f);
        this.mAvatarView.setTranslationY(0.0f);
        this.mSenderView.setAlpha(1.0f);
        this.mSenderView.setTranslationY(0.0f);
        setAlpha(1.0f);
        this.mIsolatedMessage = null;
        this.mMessages = null;
        this.mSenderName = null;
        this.mAddedMessages.clear();
        this.mFirstLayout = true;
        setCanHideSenderIfFirst(false);
        setIsFirstInLayout(true);
        setMaxDisplayedLines(Integer.MAX_VALUE);
        setSingleLine(false);
        setShowingAvatar(true);
        MessagingPropertyAnimator.recycle(this);
        sInstancePool.release(this);
    }

    public void removeGroupAnimated(final Runnable endAction) {
        performRemoveAnimation(this, new Runnable() { // from class: com.android.internal.widget.MessagingGroup$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                MessagingGroup.this.lambda$removeGroupAnimated$1$MessagingGroup(endAction);
            }
        });
    }

    public /* synthetic */ void lambda$removeGroupAnimated$1$MessagingGroup(Runnable endAction) {
        setAlpha(1.0f);
        MessagingPropertyAnimator.setToLaidOutPosition(this);
        if (endAction != null) {
            endAction.run();
        }
    }

    public void performRemoveAnimation(View message, Runnable endAction) {
        performRemoveAnimation(message, -message.getHeight(), endAction);
    }

    private void performRemoveAnimation(View view, int disappearTranslation, Runnable endAction) {
        MessagingPropertyAnimator.startLocalTranslationTo(view, disappearTranslation, MessagingLayout.FAST_OUT_LINEAR_IN);
        MessagingPropertyAnimator.fadeOut(view, endAction);
    }

    public CharSequence getSenderName() {
        return this.mSenderName;
    }

    public static void dropCache() {
        sInstancePool = new Pools.SynchronizedPool(10);
    }

    @Override // com.android.internal.widget.MessagingLinearLayout.MessagingChild
    public int getMeasuredType() {
        if (this.mIsolatedMessage != null) {
            return 1;
        }
        boolean hasNormal = false;
        int i = this.mMessageContainer.getChildCount() - 1;
        while (true) {
            boolean tooSmall = false;
            if (i < 0) {
                return 0;
            }
            View child = this.mMessageContainer.getChildAt(i);
            if (child.getVisibility() != 8 && (child instanceof MessagingLinearLayout.MessagingChild)) {
                int type = ((MessagingLinearLayout.MessagingChild) child).getMeasuredType();
                if (type == 2) {
                    tooSmall = true;
                }
                MessagingLinearLayout.LayoutParams lp = (MessagingLinearLayout.LayoutParams) child.getLayoutParams();
                if (tooSmall || lp.hide) {
                    return hasNormal ? 1 : 2;
                } else if (type == 1) {
                    return 1;
                } else {
                    hasNormal = true;
                }
            }
            i--;
        }
    }

    @Override // com.android.internal.widget.MessagingLinearLayout.MessagingChild
    public int getConsumedLines() {
        int result = 0;
        for (int i = 0; i < this.mMessageContainer.getChildCount(); i++) {
            View child = this.mMessageContainer.getChildAt(i);
            if (child instanceof MessagingLinearLayout.MessagingChild) {
                result += ((MessagingLinearLayout.MessagingChild) child).getConsumedLines();
            }
        }
        return (this.mIsolatedMessage != null ? Math.max(result, 1) : result) + 1;
    }

    @Override // com.android.internal.widget.MessagingLinearLayout.MessagingChild
    public void setMaxDisplayedLines(int lines) {
        this.mRequestedMaxDisplayedLines = lines;
        updateMaxDisplayedLines();
    }

    private void updateMaxDisplayedLines() {
        this.mMessageContainer.setMaxDisplayedLines(this.mSingleLine ? 1 : this.mRequestedMaxDisplayedLines);
    }

    @Override // com.android.internal.widget.MessagingLinearLayout.MessagingChild
    public void hideAnimated() {
        setIsHidingAnimated(true);
        removeGroupAnimated(new Runnable() { // from class: com.android.internal.widget.MessagingGroup$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                MessagingGroup.this.lambda$hideAnimated$2$MessagingGroup();
            }
        });
    }

    public /* synthetic */ void lambda$hideAnimated$2$MessagingGroup() {
        setIsHidingAnimated(false);
    }

    @Override // com.android.internal.widget.MessagingLinearLayout.MessagingChild
    public boolean isHidingAnimated() {
        return this.mIsHidingAnimated;
    }

    @Override // com.android.internal.widget.MessagingLinearLayout.MessagingChild
    public void setIsFirstInLayout(boolean first) {
        if (first != this.mIsFirstGroupInLayout) {
            this.mIsFirstGroupInLayout = first;
            updateSenderVisibility();
        }
    }

    public void setCanHideSenderIfFirst(boolean canHide) {
        if (this.mCanHideSenderIfFirst != canHide) {
            this.mCanHideSenderIfFirst = canHide;
            updateSenderVisibility();
        }
    }

    private void updateSenderVisibility() {
        int i = 0;
        boolean hidden = ((this.mIsFirstGroupInLayout || this.mSingleLine) && this.mCanHideSenderIfFirst) || TextUtils.isEmpty(this.mSenderName);
        ImageFloatingTextView imageFloatingTextView = this.mSenderView;
        if (hidden) {
            i = 8;
        }
        imageFloatingTextView.setVisibility(i);
    }

    @Override // com.android.internal.widget.MessagingLinearLayout.MessagingChild
    public boolean hasDifferentHeightWhenFirst() {
        return this.mCanHideSenderIfFirst && !this.mSingleLine && !TextUtils.isEmpty(this.mSenderName);
    }

    private void setIsHidingAnimated(boolean isHiding) {
        ViewParent parent = getParent();
        this.mIsHidingAnimated = isHiding;
        invalidate();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).invalidate();
        }
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    public Icon getAvatarSymbolIfMatching(CharSequence avatarName, String avatarSymbol, int layoutColor) {
        if (this.mAvatarName.equals(avatarName) && this.mAvatarSymbol.equals(avatarSymbol) && layoutColor == this.mLayoutColor) {
            return this.mAvatarIcon;
        }
        return null;
    }

    public void setCreatedAvatar(Icon cachedIcon, CharSequence avatarName, String avatarSymbol, int layoutColor) {
        if (!this.mAvatarName.equals(avatarName) || !this.mAvatarSymbol.equals(avatarSymbol) || layoutColor != this.mLayoutColor) {
            setAvatar(cachedIcon);
            this.mAvatarSymbol = avatarSymbol;
            setLayoutColor(layoutColor);
            this.mAvatarName = avatarName;
        }
    }

    public void setTextColors(int senderTextColor, int messageTextColor) {
        this.mTextColor = messageTextColor;
        this.mSendingTextColor = calculateSendingTextColor();
        updateMessageColor();
        this.mSenderView.setTextColor(senderTextColor);
    }

    public void setLayoutColor(int layoutColor) {
        if (layoutColor != this.mLayoutColor) {
            this.mLayoutColor = layoutColor;
            this.mSendingSpinner.setIndeterminateTintList(ColorStateList.valueOf(layoutColor));
        }
    }

    private void updateMessageColor() {
        if (this.mMessages != null) {
            int color = this.mSendingSpinnerContainer.getVisibility() == 0 ? this.mSendingTextColor : this.mTextColor;
            for (MessagingMessage message : this.mMessages) {
                message.setColor(message.getMessage().isRemoteInputHistory() ? color : this.mTextColor);
            }
        }
    }

    public void setMessages(List<MessagingMessage> group) {
        int textMessageIndex = 0;
        MessagingImageMessage isolatedMessage = null;
        for (int messageIndex = 0; messageIndex < group.size(); messageIndex++) {
            MessagingMessage message = group.get(messageIndex);
            if (message.getGroup() != this) {
                message.setMessagingGroup(this);
                this.mAddedMessages.add(message);
            }
            boolean isImage = message instanceof MessagingImageMessage;
            if (this.mImageDisplayLocation != 0 && isImage) {
                isolatedMessage = (MessagingImageMessage) message;
            } else {
                if (removeFromParentIfDifferent(message, this.mMessageContainer)) {
                    ViewGroup.LayoutParams layoutParams = message.getView().getLayoutParams();
                    if (layoutParams != null && !(layoutParams instanceof MessagingLinearLayout.LayoutParams)) {
                        message.getView().setLayoutParams(this.mMessageContainer.mo3516generateDefaultLayoutParams());
                    }
                    this.mMessageContainer.addView(message.getView(), textMessageIndex);
                }
                if (isImage) {
                    ((MessagingImageMessage) message).setIsolated(false);
                }
                if (textMessageIndex != this.mMessageContainer.indexOfChild(message.getView())) {
                    this.mMessageContainer.removeView(message.getView());
                    this.mMessageContainer.addView(message.getView(), textMessageIndex);
                }
                textMessageIndex++;
            }
        }
        if (isolatedMessage != null) {
            if (this.mImageDisplayLocation == 1 && removeFromParentIfDifferent(isolatedMessage, this.mImageContainer)) {
                this.mImageContainer.removeAllViews();
                this.mImageContainer.addView(isolatedMessage.getView());
            } else if (this.mImageDisplayLocation == 2) {
                this.mImageContainer.removeAllViews();
            }
            isolatedMessage.setIsolated(true);
        } else if (this.mIsolatedMessage != null) {
            this.mImageContainer.removeAllViews();
        }
        this.mIsolatedMessage = isolatedMessage;
        updateImageContainerVisibility();
        this.mMessages = group;
        updateMessageColor();
    }

    private void updateImageContainerVisibility() {
        this.mImageContainer.setVisibility((this.mIsolatedMessage == null || this.mImageDisplayLocation != 1) ? 8 : 0);
    }

    private boolean removeFromParentIfDifferent(MessagingMessage message, ViewGroup newParent) {
        ViewParent parent = message.getView().getParent();
        if (parent != newParent) {
            if (parent instanceof ViewGroup) {
                ((ViewGroup) parent).removeView(message.getView());
                return true;
            }
            return true;
        }
        return false;
    }

    @Override // android.widget.LinearLayout, android.view.ViewGroup, android.view.View
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (!this.mAddedMessages.isEmpty()) {
            final boolean firstLayout = this.mFirstLayout;
            getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() { // from class: com.android.internal.widget.MessagingGroup.1
                @Override // android.view.ViewTreeObserver.OnPreDrawListener
                public boolean onPreDraw() {
                    Iterator it = MessagingGroup.this.mAddedMessages.iterator();
                    while (it.hasNext()) {
                        MessagingMessage message = (MessagingMessage) it.next();
                        if (message.getView().isShown()) {
                            MessagingPropertyAnimator.fadeIn(message.getView());
                            if (!firstLayout) {
                                MessagingPropertyAnimator.startLocalTranslationFrom(message.getView(), message.getView().getHeight(), MessagingLayout.LINEAR_OUT_SLOW_IN);
                            }
                        }
                    }
                    MessagingGroup.this.mAddedMessages.clear();
                    MessagingGroup.this.getViewTreeObserver().removeOnPreDrawListener(this);
                    return true;
                }
            });
        }
        this.mFirstLayout = false;
        updateClipRect();
    }

    public int calculateGroupCompatibility(MessagingGroup otherGroup) {
        if (TextUtils.equals(getSenderName(), otherGroup.getSenderName())) {
            int result = 1;
            for (int i = 0; i < this.mMessages.size() && i < otherGroup.mMessages.size(); i++) {
                List<MessagingMessage> list = this.mMessages;
                MessagingMessage ownMessage = list.get((list.size() - 1) - i);
                List<MessagingMessage> list2 = otherGroup.mMessages;
                MessagingMessage otherMessage = list2.get((list2.size() - 1) - i);
                if (ownMessage.sameAs(otherMessage)) {
                    result++;
                } else {
                    return result;
                }
            }
            return result;
        }
        return 0;
    }

    public TextView getSenderView() {
        return this.mSenderView;
    }

    public View getAvatar() {
        return this.mAvatarView;
    }

    public Icon getAvatarIcon() {
        return this.mAvatarIcon;
    }

    public MessagingLinearLayout getMessageContainer() {
        return this.mMessageContainer;
    }

    public MessagingImageMessage getIsolatedMessage() {
        return this.mIsolatedMessage;
    }

    public boolean needsGeneratedAvatar() {
        return this.mNeedsGeneratedAvatar;
    }

    public Person getSender() {
        return this.mSender;
    }

    public void setClippingDisabled(boolean disabled) {
        this.mClippingDisabled = disabled;
    }

    public void setImageDisplayLocation(int displayLocation) {
        if (this.mImageDisplayLocation != displayLocation) {
            this.mImageDisplayLocation = displayLocation;
            updateImageContainerVisibility();
        }
    }

    public List<MessagingMessage> getMessages() {
        return this.mMessages;
    }

    public void setSingleLine(boolean singleLine) {
        if (singleLine != this.mSingleLine) {
            this.mSingleLine = singleLine;
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) this.mMessageContainer.getLayoutParams();
            int i = 0;
            p.topMargin = singleLine ? 0 : this.mNotificationTextMarginTop;
            this.mMessageContainer.setLayoutParams(p);
            this.mContentContainer.setOrientation(!singleLine);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) this.mSenderView.getLayoutParams();
            if (singleLine != 0) {
                i = this.mSenderTextPaddingSingleLine;
            }
            layoutParams.setMarginEnd(i);
            this.mSenderView.setSingleLine(singleLine);
            updateMaxDisplayedLines();
            updateClipRect();
            updateSenderVisibility();
        }
    }

    public boolean isSingleLine() {
        return this.mSingleLine;
    }

    public void setIsInConversation(boolean isInConversation) {
        int i;
        if (this.mIsInConversation != isInConversation) {
            this.mIsInConversation = isInConversation;
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) this.mMessagingIconContainer.getLayoutParams();
            if (this.mIsInConversation) {
                i = this.mConversationContentStart;
            } else {
                i = this.mNonConversationContentStart;
            }
            layoutParams.width = i;
            this.mMessagingIconContainer.setLayoutParams(layoutParams);
            int imagePaddingStart = isInConversation ? 0 : this.mNonConversationPaddingStart;
            this.mMessagingIconContainer.setPaddingRelative(imagePaddingStart, 0, 0, 0);
            ViewGroup.LayoutParams avatarLayoutParams = this.mAvatarView.getLayoutParams();
            int size = this.mIsInConversation ? this.mConversationAvatarSize : this.mNonConversationAvatarSize;
            avatarLayoutParams.height = size;
            avatarLayoutParams.width = size;
            this.mAvatarView.setLayoutParams(avatarLayoutParams);
        }
    }
}
