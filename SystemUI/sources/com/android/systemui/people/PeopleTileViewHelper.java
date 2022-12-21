package com.android.systemui.people;

import android.app.PendingIntent;
import android.app.people.ConversationStatus;
import android.app.people.PeopleSpaceTile;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.net.Uri;
import android.os.Bundle;
import android.os.UserHandle;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.util.Pair;
import android.util.Size;
import android.util.SizeF;
import android.util.TypedValue;
import android.widget.RemoteViews;
import android.widget.TextView;
import androidx.core.app.NotificationCompat;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.core.math.MathUtils;
import com.android.launcher3.icons.FastBitmapDrawable;
import com.android.systemui.C1893R;
import com.android.systemui.people.widget.LaunchConversationActivity;
import com.android.systemui.people.widget.PeopleSpaceWidgetProvider;
import com.android.systemui.people.widget.PeopleTileKey;
import java.p026io.IOException;
import java.text.NumberFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PeopleTileViewHelper {
    private static final Pattern ANY_DOUBLE_MARK_PATTERN = Pattern.compile("[!?][!?]+");
    static final String BRIEF_PAUSE_ON_TALKBACK = "\n\n";
    private static final int DAYS_IN_A_WEEK = 7;
    private static final boolean DEBUG = false;
    private static final Pattern DOUBLE_EXCLAMATION_PATTERN = Pattern.compile("[!][!]+");
    private static final Pattern DOUBLE_QUESTION_PATTERN = Pattern.compile("[?][?]+");
    private static final CharSequence EMOJI_CAKE = "🎂";
    private static final Pattern EMOJI_PATTERN = Pattern.compile(UNICODE_EMOJI_REGEX);
    public static final String EMPTY_STRING = "";
    private static final int FIXED_HEIGHT_DIMENS_FOR_LARGE_NOTIF_CONTENT = 62;
    private static final int FIXED_HEIGHT_DIMENS_FOR_LARGE_STATUS_CONTENT = 76;
    private static final int FIXED_HEIGHT_DIMENS_FOR_MEDIUM_CONTENT_BEFORE_PADDING = 12;
    private static final int FIXED_HEIGHT_DIMENS_FOR_SMALL_HORIZONTAL = 10;
    private static final int FIXED_HEIGHT_DIMENS_FOR_SMALL_VERTICAL = 18;
    private static final int FIXED_WIDTH_DIMENS_FOR_SMALL_HORIZONTAL = 16;
    private static final int FIXED_WIDTH_DIMENS_FOR_SMALL_VERTICAL = 8;
    public static final int LAYOUT_LARGE = 2;
    public static final int LAYOUT_MEDIUM = 1;
    public static final int LAYOUT_SMALL = 0;
    private static final int MAX_MEDIUM_PADDING = 16;
    private static final int MESSAGES_COUNT_OVERFLOW = 6;
    private static final int MIN_CONTENT_MAX_LINES = 2;
    private static final int MIN_MEDIUM_VERTICAL_PADDING = 4;
    private static final Pattern MIXED_MARK_PATTERN = Pattern.compile("![?].*|.*[?]!");
    private static final int NAME_MAX_LINES_WITHOUT_LAST_INTERACTION = 3;
    private static final int NAME_MAX_LINES_WITH_LAST_INTERACTION = 1;
    private static final int ONE_DAY = 1;
    private static final String TAG = "PeopleTileView";
    private static final String UNICODE_EMOJI_REGEX = "\\p{RI}\\p{RI}|(\\p{Emoji}(\\p{EMod}|\\x{FE0F}\\x{20E3}?|[\\x{E0020}-\\x{E007E}]+\\x{E007F})|[\\p{Emoji}&&\\p{So}])(\\x{200D}\\p{Emoji}(\\p{EMod}|\\x{FE0F}\\x{20E3}?|[\\x{E0020}-\\x{E007E}]+\\x{E007F})?)*";
    private int mAppWidgetId;
    private Context mContext;
    private float mDensity;
    private int mHeight;
    private NumberFormat mIntegerFormat;
    private boolean mIsLeftToRight;
    private PeopleTileKey mKey;
    private int mLayoutSize = getLayoutSize();
    private Locale mLocale;
    private int mMediumVerticalPadding;
    private PeopleSpaceTile mTile;
    private int mWidth;

    PeopleTileViewHelper(Context context, PeopleSpaceTile peopleSpaceTile, int i, int i2, int i3, PeopleTileKey peopleTileKey) {
        this.mContext = context;
        this.mTile = peopleSpaceTile;
        this.mKey = peopleTileKey;
        this.mAppWidgetId = i;
        this.mDensity = context.getResources().getDisplayMetrics().density;
        this.mWidth = i2;
        this.mHeight = i3;
        this.mIsLeftToRight = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 0;
    }

    public static RemoteViews createRemoteViews(Context context, PeopleSpaceTile peopleSpaceTile, int i, Bundle bundle, PeopleTileKey peopleTileKey) {
        return new RemoteViews((Map) getWidgetSizes(context, bundle).stream().distinct().collect(Collectors.toMap(Function.identity(), new PeopleTileViewHelper$$ExternalSyntheticLambda5(context, peopleSpaceTile, i, peopleTileKey))));
    }

    private static List<SizeF> getWidgetSizes(Context context, Bundle bundle) {
        float f = context.getResources().getDisplayMetrics().density;
        ArrayList parcelableArrayList = bundle.getParcelableArrayList("appWidgetSizes");
        if (parcelableArrayList != null && !parcelableArrayList.isEmpty()) {
            return parcelableArrayList;
        }
        int sizeInDp = getSizeInDp(context, C1893R.dimen.default_width, f);
        int sizeInDp2 = getSizeInDp(context, C1893R.dimen.default_height, f);
        ArrayList arrayList = new ArrayList(2);
        arrayList.add(new SizeF((float) bundle.getInt("appWidgetMinWidth", sizeInDp), (float) bundle.getInt("appWidgetMaxHeight", sizeInDp2)));
        arrayList.add(new SizeF((float) bundle.getInt("appWidgetMaxWidth", sizeInDp), (float) bundle.getInt("appWidgetMinHeight", sizeInDp2)));
        return arrayList;
    }

    /* access modifiers changed from: package-private */
    public RemoteViews getViews() {
        RemoteViews viewForTile = getViewForTile();
        return setLaunchIntents(setCommonRemoteViewsFields(viewForTile, getMaxAvatarSize(viewForTile)));
    }

    private RemoteViews getViewForTile() {
        List list;
        PeopleSpaceTile peopleSpaceTile = this.mTile;
        if (peopleSpaceTile == null || peopleSpaceTile.isPackageSuspended() || this.mTile.isUserQuieted()) {
            return createSuppressedView();
        }
        if (isDndBlockingTileData(this.mTile)) {
            return createDndRemoteViews().mRemoteViews;
        }
        if (Objects.equals(this.mTile.getNotificationCategory(), NotificationCompat.CATEGORY_MISSED_CALL)) {
            return createMissedCallRemoteViews();
        }
        if (this.mTile.getNotificationKey() != null) {
            return createNotificationRemoteViews();
        }
        if (this.mTile.getStatuses() == null) {
            list = Arrays.asList(new ConversationStatus[0]);
        } else {
            list = (List) this.mTile.getStatuses().stream().filter(new PeopleTileViewHelper$$ExternalSyntheticLambda3(this)).collect(Collectors.toList());
        }
        ConversationStatus birthdayStatus = getBirthdayStatus(list);
        if (birthdayStatus != null) {
            return createStatusRemoteViews(birthdayStatus);
        }
        if (!list.isEmpty()) {
            return createStatusRemoteViews((ConversationStatus) list.stream().max(Comparator.comparing(new PeopleTileViewHelper$$ExternalSyntheticLambda4())).get());
        }
        return createLastInteractionRemoteViews();
    }

    private static boolean isDndBlockingTileData(PeopleSpaceTile peopleSpaceTile) {
        if (peopleSpaceTile == null) {
            return false;
        }
        int notificationPolicyState = peopleSpaceTile.getNotificationPolicyState();
        if ((notificationPolicyState & 1) != 0) {
            return false;
        }
        if ((notificationPolicyState & 4) != 0 && peopleSpaceTile.isImportantConversation()) {
            return false;
        }
        if ((notificationPolicyState & 8) != 0 && peopleSpaceTile.getContactAffinity() == 1.0f) {
            return false;
        }
        if ((notificationPolicyState & 16) == 0 || (peopleSpaceTile.getContactAffinity() != 0.5f && peopleSpaceTile.getContactAffinity() != 1.0f)) {
            return !peopleSpaceTile.canBypassDnd();
        }
        return false;
    }

    private RemoteViews createSuppressedView() {
        RemoteViews remoteViews;
        PeopleSpaceTile peopleSpaceTile = this.mTile;
        if (peopleSpaceTile == null || !peopleSpaceTile.isUserQuieted()) {
            remoteViews = new RemoteViews(this.mContext.getPackageName(), C1893R.layout.people_tile_suppressed_layout);
        } else {
            remoteViews = new RemoteViews(this.mContext.getPackageName(), C1893R.layout.people_tile_work_profile_quiet_layout);
        }
        Drawable mutate = this.mContext.getDrawable(C1893R.C1895drawable.ic_conversation_icon).mutate();
        mutate.setColorFilter(FastBitmapDrawable.getDisabledColorFilter());
        remoteViews.setImageViewBitmap(C1893R.C1897id.icon, PeopleSpaceUtils.convertDrawableToBitmap(mutate));
        return remoteViews;
    }

    private void setMaxLines(RemoteViews remoteViews, boolean z) {
        int i;
        int i2;
        if (this.mLayoutSize == 2) {
            i2 = getLineHeightFromResource(C1893R.dimen.name_text_size_for_large_content);
            i = C1893R.dimen.content_text_size_for_large;
        } else {
            i2 = getLineHeightFromResource(C1893R.dimen.name_text_size_for_medium_content);
            i = C1893R.dimen.content_text_size_for_medium;
        }
        int max = Math.max(2, Math.floorDiv(getContentHeightForLayout(i2, remoteViews.getLayoutId() == C1893R.layout.people_tile_large_with_status_content), getLineHeightFromResource(i)));
        if (z) {
            max--;
        }
        remoteViews.setInt(C1893R.C1897id.text_content, "setMaxLines", max);
    }

    private int getLineHeightFromResource(int i) {
        try {
            TextView textView = new TextView(this.mContext);
            textView.setTextSize(0, this.mContext.getResources().getDimension(i));
            textView.setTextAppearance(16974253);
            return (int) (((float) textView.getLineHeight()) / this.mDensity);
        } catch (Exception e) {
            Log.e(TAG, "Could not create text view: " + e);
            return this.getSizeInDp(C1893R.dimen.content_text_size_for_medium);
        }
    }

    private int getSizeInDp(int i) {
        return getSizeInDp(this.mContext, i, this.mDensity);
    }

    public static int getSizeInDp(Context context, int i, float f) {
        return (int) (context.getResources().getDimension(i) / f);
    }

    private int getContentHeightForLayout(int i, boolean z) {
        int i2 = this.mLayoutSize;
        if (i2 == 1) {
            return this.mHeight - ((i + 12) + (this.mMediumVerticalPadding * 2));
        }
        if (i2 != 2) {
            return -1;
        }
        return this.mHeight - ((getSizeInDp(C1893R.dimen.max_people_avatar_size_for_large_content) + i) + (z ? 76 : 62));
    }

    private int getLayoutSize() {
        if (this.mHeight >= getSizeInDp(C1893R.dimen.required_height_for_large) && this.mWidth >= getSizeInDp(C1893R.dimen.required_width_for_large)) {
            return 2;
        }
        if (this.mHeight < getSizeInDp(C1893R.dimen.required_height_for_medium) || this.mWidth < getSizeInDp(C1893R.dimen.required_width_for_medium)) {
            return 0;
        }
        this.mMediumVerticalPadding = Math.max(4, Math.min(Math.floorDiv(this.mHeight - ((getSizeInDp(C1893R.dimen.avatar_size_for_medium) + 4) + getLineHeightFromResource(C1893R.dimen.name_text_size_for_medium_content)), 2), 16));
        return 1;
    }

    private int getMaxAvatarSize(RemoteViews remoteViews) {
        int layoutId = remoteViews.getLayoutId();
        int sizeInDp = getSizeInDp(C1893R.dimen.avatar_size_for_medium);
        if (layoutId == C1893R.layout.people_tile_medium_empty) {
            return getSizeInDp(C1893R.dimen.max_people_avatar_size_for_large_content);
        }
        if (layoutId == C1893R.layout.people_tile_medium_with_content) {
            return getSizeInDp(C1893R.dimen.avatar_size_for_medium);
        }
        if (layoutId == C1893R.layout.people_tile_small) {
            sizeInDp = Math.min(this.mHeight - (Math.max(18, getLineHeightFromResource(C1893R.dimen.name_text_size_for_small)) + 18), this.mWidth - 8);
        }
        if (layoutId == C1893R.layout.people_tile_small_horizontal) {
            sizeInDp = Math.min(this.mHeight - 10, this.mWidth - 16);
        }
        if (layoutId == C1893R.layout.people_tile_large_with_notification_content) {
            return Math.min(this.mHeight - ((getLineHeightFromResource(C1893R.dimen.content_text_size_for_large) * 3) + 62), getSizeInDp(C1893R.dimen.max_people_avatar_size_for_large_content));
        }
        if (layoutId == C1893R.layout.people_tile_large_with_status_content) {
            return Math.min(this.mHeight - ((getLineHeightFromResource(C1893R.dimen.content_text_size_for_large) * 3) + 76), getSizeInDp(C1893R.dimen.max_people_avatar_size_for_large_content));
        }
        if (layoutId == C1893R.layout.people_tile_large_empty) {
            sizeInDp = Math.min(this.mHeight - (((((getLineHeightFromResource(C1893R.dimen.name_text_size_for_large) + 28) + getLineHeightFromResource(C1893R.dimen.content_text_size_for_large)) + 16) + 10) + 16), this.mWidth - 28);
        }
        if (isDndBlockingTileData(this.mTile) && this.mLayoutSize != 0) {
            sizeInDp = createDndRemoteViews().mAvatarSize;
        }
        return Math.min(sizeInDp, getSizeInDp(C1893R.dimen.max_people_avatar_size));
    }

    private RemoteViews setCommonRemoteViewsFields(RemoteViews remoteViews, int i) {
        int i2;
        try {
            PeopleSpaceTile peopleSpaceTile = this.mTile;
            if (peopleSpaceTile == null) {
                return remoteViews;
            }
            if (peopleSpaceTile.getStatuses() != null && this.mTile.getStatuses().stream().anyMatch(new PeopleTileViewHelper$$ExternalSyntheticLambda6())) {
                remoteViews.setViewVisibility(C1893R.C1897id.availability, 0);
                i2 = this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.availability_dot_shown_padding);
                remoteViews.setContentDescription(C1893R.C1897id.availability, this.mContext.getString(C1893R.string.person_available));
            } else {
                remoteViews.setViewVisibility(C1893R.C1897id.availability, 8);
                i2 = this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.availability_dot_missing_padding);
            }
            boolean z = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) == 0;
            remoteViews.setViewPadding(C1893R.C1897id.padding_before_availability, z ? i2 : 0, 0, z ? 0 : i2, 0);
            boolean hasNewStory = getHasNewStory(this.mTile);
            remoteViews.setImageViewBitmap(C1893R.C1897id.person_icon, getPersonIconBitmap(this.mContext, this.mTile, i, hasNewStory));
            if (hasNewStory) {
                remoteViews.setContentDescription(C1893R.C1897id.person_icon, this.mContext.getString(C1893R.string.new_story_status_content_description, new Object[]{this.mTile.getUserName()}));
            } else {
                remoteViews.setContentDescription(C1893R.C1897id.person_icon, (CharSequence) null);
            }
            return remoteViews;
        } catch (Exception e) {
            Log.e(TAG, "Failed to set common fields: " + e);
            return remoteViews;
        }
    }

    static /* synthetic */ boolean lambda$setCommonRemoteViewsFields$3(ConversationStatus conversationStatus) {
        return conversationStatus.getAvailability() == 0;
    }

    private static boolean getHasNewStory(PeopleSpaceTile peopleSpaceTile) {
        return peopleSpaceTile.getStatuses() != null && peopleSpaceTile.getStatuses().stream().anyMatch(new PeopleTileViewHelper$$ExternalSyntheticLambda0());
    }

    static /* synthetic */ boolean lambda$getHasNewStory$4(ConversationStatus conversationStatus) {
        return conversationStatus.getActivity() == 3;
    }

    private RemoteViews setLaunchIntents(RemoteViews remoteViews) {
        if (PeopleTileKey.isValid(this.mKey) && this.mTile != null) {
            try {
                Intent intent = new Intent(this.mContext, LaunchConversationActivity.class);
                intent.addFlags(1350598656);
                intent.putExtra(PeopleSpaceWidgetProvider.EXTRA_TILE_ID, this.mKey.getShortcutId());
                intent.putExtra(PeopleSpaceWidgetProvider.EXTRA_PACKAGE_NAME, this.mKey.getPackageName());
                intent.putExtra(PeopleSpaceWidgetProvider.EXTRA_USER_HANDLE, new UserHandle(this.mKey.getUserId()));
                PeopleSpaceTile peopleSpaceTile = this.mTile;
                if (peopleSpaceTile != null) {
                    intent.putExtra(PeopleSpaceWidgetProvider.EXTRA_NOTIFICATION_KEY, peopleSpaceTile.getNotificationKey());
                }
                remoteViews.setOnClickPendingIntent(16908288, PendingIntent.getActivity(this.mContext, this.mAppWidgetId, intent, 167772160));
                return remoteViews;
            } catch (Exception e) {
                Log.e(TAG, "Failed to add launch intents: " + e);
            }
        }
        return remoteViews;
    }

    private RemoteViewsAndSizes createDndRemoteViews() {
        int i;
        RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), getViewForDndRemoteViews());
        int sizeInDp = getSizeInDp(C1893R.dimen.avatar_size_for_medium_empty);
        int sizeInDp2 = getSizeInDp(C1893R.dimen.max_people_avatar_size);
        String string = this.mContext.getString(C1893R.string.paused_by_dnd);
        remoteViews.setTextViewText(C1893R.C1897id.text_content, string);
        int i2 = this.mLayoutSize == 2 ? C1893R.dimen.content_text_size_for_large : C1893R.dimen.content_text_size_for_medium;
        remoteViews.setTextViewTextSize(C1893R.C1897id.text_content, 0, this.mContext.getResources().getDimension(i2));
        int lineHeightFromResource = getLineHeightFromResource(i2);
        if (this.mLayoutSize == 1) {
            remoteViews.setInt(C1893R.C1897id.text_content, "setMaxLines", (this.mHeight - 16) / lineHeightFromResource);
        } else {
            int dpToPx = dpToPx((float) 16);
            int dpToPx2 = dpToPx((float) 14);
            int sizeInDp3 = getSizeInDp(this.mLayoutSize == 0 ? C1893R.dimen.regular_predefined_icon : C1893R.dimen.largest_predefined_icon);
            int i3 = (this.mHeight - 32) - sizeInDp3;
            int sizeInDp4 = getSizeInDp(C1893R.dimen.padding_between_suppressed_layout_items) * 2;
            int i4 = (i3 - sizeInDp) - sizeInDp4;
            int estimateTextHeight = estimateTextHeight(string, i2, this.mWidth - 32);
            if (estimateTextHeight > i4 || this.mLayoutSize != 2) {
                i = C1893R.C1897id.predefined_icon;
                if (this.mLayoutSize != 0) {
                    remoteViews = new RemoteViews(this.mContext.getPackageName(), C1893R.layout.people_tile_small);
                }
                int maxAvatarSize = getMaxAvatarSize(remoteViews);
                remoteViews.setViewVisibility(C1893R.C1897id.messages_count, 8);
                remoteViews.setViewVisibility(C1893R.C1897id.name, 8);
                remoteViews.setContentDescription(C1893R.C1897id.predefined_icon, string);
                sizeInDp = maxAvatarSize;
            } else {
                remoteViews.setViewVisibility(C1893R.C1897id.text_content, 0);
                remoteViews.setInt(C1893R.C1897id.text_content, "setMaxLines", i4 / lineHeightFromResource);
                remoteViews.setContentDescription(C1893R.C1897id.predefined_icon, (CharSequence) null);
                int clamp = MathUtils.clamp(Math.min(this.mWidth - 32, (i3 - estimateTextHeight) - sizeInDp4), dpToPx(10.0f), sizeInDp2);
                i = C1893R.C1897id.predefined_icon;
                remoteViews.setViewPadding(16908288, dpToPx, dpToPx2, dpToPx, dpToPx);
                float f = (float) sizeInDp3;
                remoteViews.setViewLayoutWidth(i, f, 1);
                remoteViews.setViewLayoutHeight(i, f, 1);
                sizeInDp = clamp;
            }
            remoteViews.setViewVisibility(i, 0);
            remoteViews.setImageViewResource(i, C1893R.C1895drawable.ic_qs_dnd_on);
        }
        return new RemoteViewsAndSizes(remoteViews, sizeInDp);
    }

    private RemoteViews createMissedCallRemoteViews() {
        RemoteViews viewForContentLayout = setViewForContentLayout(new RemoteViews(this.mContext.getPackageName(), getLayoutForContent()));
        setPredefinedIconVisible(viewForContentLayout);
        viewForContentLayout.setViewVisibility(C1893R.C1897id.text_content, 0);
        viewForContentLayout.setViewVisibility(C1893R.C1897id.messages_count, 8);
        setMaxLines(viewForContentLayout, false);
        CharSequence notificationContent = this.mTile.getNotificationContent();
        viewForContentLayout.setTextViewText(C1893R.C1897id.text_content, notificationContent);
        setContentDescriptionForNotificationTextContent(viewForContentLayout, notificationContent, this.mTile.getUserName());
        viewForContentLayout.setColorAttr(C1893R.C1897id.text_content, "setTextColor", 16844099);
        viewForContentLayout.setColorAttr(C1893R.C1897id.predefined_icon, "setColorFilter", 16844099);
        viewForContentLayout.setImageViewResource(C1893R.C1897id.predefined_icon, C1893R.C1895drawable.ic_phone_missed);
        if (this.mLayoutSize == 2) {
            viewForContentLayout.setInt(C1893R.C1897id.content, "setGravity", 80);
            viewForContentLayout.setViewLayoutHeightDimen(C1893R.C1897id.predefined_icon, C1893R.dimen.larger_predefined_icon);
            viewForContentLayout.setViewLayoutWidthDimen(C1893R.C1897id.predefined_icon, C1893R.dimen.larger_predefined_icon);
        }
        setAvailabilityDotPadding(viewForContentLayout, C1893R.dimen.availability_dot_notification_padding);
        return viewForContentLayout;
    }

    private void setPredefinedIconVisible(RemoteViews remoteViews) {
        remoteViews.setViewVisibility(C1893R.C1897id.predefined_icon, 0);
        if (this.mLayoutSize == 1) {
            int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.before_predefined_icon_padding);
            boolean z = this.mIsLeftToRight;
            remoteViews.setViewPadding(C1893R.C1897id.name, z ? 0 : dimensionPixelSize, 0, z ? dimensionPixelSize : 0, 0);
        }
    }

    private RemoteViews createNotificationRemoteViews() {
        CharSequence charSequence;
        RemoteViews viewForContentLayout = setViewForContentLayout(new RemoteViews(this.mContext.getPackageName(), getLayoutForNotificationContent()));
        CharSequence notificationSender = this.mTile.getNotificationSender();
        Uri notificationDataUri = this.mTile.getNotificationDataUri();
        if (notificationDataUri != null) {
            String string = this.mContext.getString(C1893R.string.new_notification_image_content_description, new Object[]{this.mTile.getUserName()});
            viewForContentLayout.setContentDescription(C1893R.C1897id.image, string);
            viewForContentLayout.setViewVisibility(C1893R.C1897id.image, 0);
            viewForContentLayout.setViewVisibility(C1893R.C1897id.text_content, 8);
            try {
                viewForContentLayout.setImageViewBitmap(C1893R.C1897id.image, PeopleSpaceUtils.convertDrawableToBitmap(resolveImage(notificationDataUri, this.mContext)));
            } catch (IOException | SecurityException e) {
                Log.e(TAG, "Could not decode image: " + e);
                viewForContentLayout.setTextViewText(C1893R.C1897id.text_content, string);
                viewForContentLayout.setViewVisibility(C1893R.C1897id.text_content, 0);
                viewForContentLayout.setViewVisibility(C1893R.C1897id.image, 8);
            }
        } else {
            setMaxLines(viewForContentLayout, !TextUtils.isEmpty(notificationSender));
            CharSequence notificationContent = this.mTile.getNotificationContent();
            if (notificationSender != null) {
                charSequence = notificationSender;
            } else {
                charSequence = this.mTile.getUserName();
            }
            setContentDescriptionForNotificationTextContent(viewForContentLayout, notificationContent, charSequence);
            viewForContentLayout = decorateBackground(viewForContentLayout, notificationContent);
            viewForContentLayout.setColorAttr(C1893R.C1897id.text_content, "setTextColor", 16842806);
            viewForContentLayout.setTextViewText(C1893R.C1897id.text_content, this.mTile.getNotificationContent());
            if (this.mLayoutSize == 2) {
                viewForContentLayout.setViewPadding(C1893R.C1897id.name, 0, 0, 0, this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.above_notification_text_padding));
            }
            viewForContentLayout.setViewVisibility(C1893R.C1897id.image, 8);
            viewForContentLayout.setImageViewResource(C1893R.C1897id.predefined_icon, C1893R.C1895drawable.ic_message);
        }
        if (this.mTile.getMessagesCount() > 1) {
            if (this.mLayoutSize == 1) {
                int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.before_messages_count_padding);
                boolean z = this.mIsLeftToRight;
                viewForContentLayout.setViewPadding(C1893R.C1897id.name, z ? 0 : dimensionPixelSize, 0, z ? dimensionPixelSize : 0, 0);
            }
            viewForContentLayout.setViewVisibility(C1893R.C1897id.messages_count, 0);
            viewForContentLayout.setTextViewText(C1893R.C1897id.messages_count, getMessagesCountText(this.mTile.getMessagesCount()));
            if (this.mLayoutSize == 0) {
                viewForContentLayout.setViewVisibility(C1893R.C1897id.predefined_icon, 8);
            }
        }
        if (!TextUtils.isEmpty(notificationSender)) {
            viewForContentLayout.setViewVisibility(C1893R.C1897id.subtext, 0);
            viewForContentLayout.setTextViewText(C1893R.C1897id.subtext, notificationSender);
        } else {
            viewForContentLayout.setViewVisibility(C1893R.C1897id.subtext, 8);
        }
        setAvailabilityDotPadding(viewForContentLayout, C1893R.dimen.availability_dot_notification_padding);
        return viewForContentLayout;
    }

    /* access modifiers changed from: package-private */
    public Drawable resolveImage(Uri uri, Context context) throws IOException {
        return ImageDecoder.decodeDrawable(ImageDecoder.createSource(context.getContentResolver(), uri), new PeopleTileViewHelper$$ExternalSyntheticLambda1(this));
    }

    private static int getPowerOfTwoForSampleRatio(double d) {
        return Math.max(1, Integer.highestOneBit((int) Math.floor(d)));
    }

    /* access modifiers changed from: private */
    /* renamed from: onHeaderDecoded */
    public void mo35149x44fa7855(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
        int applyDimension = (int) TypedValue.applyDimension(1, (float) this.mWidth, this.mContext.getResources().getDisplayMetrics());
        int applyDimension2 = (int) TypedValue.applyDimension(1, (float) this.mHeight, this.mContext.getResources().getDisplayMetrics());
        int max = Math.max(applyDimension, applyDimension2);
        int min = (int) (((double) Math.min(applyDimension, applyDimension2)) * 1.5d);
        if (min < max) {
            max = min;
        }
        Size size = imageInfo.getSize();
        int max2 = Math.max(size.getHeight(), size.getWidth());
        imageDecoder.setTargetSampleSize(getPowerOfTwoForSampleRatio(max2 > max ? (double) ((((float) max2) * 1.0f) / ((float) max)) : 1.0d));
    }

    private void setContentDescriptionForNotificationTextContent(RemoteViews remoteViews, CharSequence charSequence, CharSequence charSequence2) {
        remoteViews.setContentDescription(this.mLayoutSize == 0 ? C1893R.C1897id.predefined_icon : C1893R.C1897id.text_content, this.mContext.getString(C1893R.string.new_notification_text_content_description, new Object[]{charSequence2, charSequence}));
    }

    private String getMessagesCountText(int i) {
        if (i >= 6) {
            return this.mContext.getResources().getString(C1893R.string.messages_count_overflow_indicator, new Object[]{6});
        }
        Locale locale = this.mContext.getResources().getConfiguration().getLocales().get(0);
        if (!locale.equals(this.mLocale)) {
            this.mLocale = locale;
            this.mIntegerFormat = NumberFormat.getIntegerInstance(locale);
        }
        return this.mIntegerFormat.format((long) i);
    }

    private RemoteViews createStatusRemoteViews(ConversationStatus conversationStatus) {
        RemoteViews viewForContentLayout = setViewForContentLayout(new RemoteViews(this.mContext.getPackageName(), getLayoutForContent()));
        CharSequence description = conversationStatus.getDescription();
        if (TextUtils.isEmpty(description)) {
            description = getStatusTextByType(conversationStatus.getActivity());
        }
        setPredefinedIconVisible(viewForContentLayout);
        int i = C1893R.C1897id.text_content;
        viewForContentLayout.setTextViewText(C1893R.C1897id.text_content, description);
        if (conversationStatus.getActivity() == 1 || conversationStatus.getActivity() == 8) {
            setEmojiBackground(viewForContentLayout, EMOJI_CAKE);
        }
        Icon icon = conversationStatus.getIcon();
        if (icon != null) {
            viewForContentLayout.setViewVisibility(C1893R.C1897id.scrim_layout, 0);
            viewForContentLayout.setImageViewIcon(C1893R.C1897id.status_icon, icon);
            int i2 = this.mLayoutSize;
            if (i2 == 2) {
                viewForContentLayout.setInt(C1893R.C1897id.content, "setGravity", 80);
                viewForContentLayout.setViewVisibility(C1893R.C1897id.name, 8);
                viewForContentLayout.setColorAttr(C1893R.C1897id.text_content, "setTextColor", 16842806);
            } else if (i2 == 1) {
                viewForContentLayout.setViewVisibility(C1893R.C1897id.text_content, 8);
                viewForContentLayout.setTextViewText(C1893R.C1897id.name, description);
            }
        } else {
            viewForContentLayout.setColorAttr(C1893R.C1897id.text_content, "setTextColor", 16842808);
            setMaxLines(viewForContentLayout, false);
        }
        setAvailabilityDotPadding(viewForContentLayout, C1893R.dimen.availability_dot_status_padding);
        viewForContentLayout.setImageViewResource(C1893R.C1897id.predefined_icon, getDrawableForStatus(conversationStatus));
        CharSequence contentDescriptionForStatus = getContentDescriptionForStatus(conversationStatus);
        String string = this.mContext.getString(C1893R.string.new_status_content_description, new Object[]{this.mTile.getUserName(), contentDescriptionForStatus});
        int i3 = this.mLayoutSize;
        if (i3 == 0) {
            viewForContentLayout.setContentDescription(C1893R.C1897id.predefined_icon, string);
        } else if (i3 == 1) {
            if (icon != null) {
                i = C1893R.C1897id.name;
            }
            viewForContentLayout.setContentDescription(i, string);
        } else if (i3 == 2) {
            viewForContentLayout.setContentDescription(C1893R.C1897id.text_content, string);
        }
        return viewForContentLayout;
    }

    private CharSequence getContentDescriptionForStatus(ConversationStatus conversationStatus) {
        CharSequence userName = this.mTile.getUserName();
        if (!TextUtils.isEmpty(conversationStatus.getDescription())) {
            return conversationStatus.getDescription();
        }
        switch (conversationStatus.getActivity()) {
            case 1:
                return this.mContext.getString(C1893R.string.birthday_status_content_description, new Object[]{userName});
            case 2:
                return this.mContext.getString(C1893R.string.anniversary_status_content_description, new Object[]{userName});
            case 3:
                return this.mContext.getString(C1893R.string.new_story_status_content_description, new Object[]{userName});
            case 4:
                return this.mContext.getString(C1893R.string.audio_status);
            case 5:
                return this.mContext.getString(C1893R.string.video_status);
            case 6:
                return this.mContext.getString(C1893R.string.game_status);
            case 7:
                return this.mContext.getString(C1893R.string.location_status_content_description, new Object[]{userName});
            case 8:
                return this.mContext.getString(C1893R.string.upcoming_birthday_status_content_description, new Object[]{userName});
            default:
                return "";
        }
    }

    private int getDrawableForStatus(ConversationStatus conversationStatus) {
        switch (conversationStatus.getActivity()) {
            case 1:
                return C1893R.C1895drawable.ic_cake;
            case 2:
                return C1893R.C1895drawable.ic_celebration;
            case 3:
                return C1893R.C1895drawable.ic_pages;
            case 4:
                return C1893R.C1895drawable.ic_music_note;
            case 5:
                return C1893R.C1895drawable.ic_video;
            case 6:
                return C1893R.C1895drawable.ic_play_games;
            case 7:
                return C1893R.C1895drawable.ic_location;
            case 8:
                return C1893R.C1895drawable.ic_gift;
            default:
                return C1893R.C1895drawable.ic_person;
        }
    }

    private void setAvailabilityDotPadding(RemoteViews remoteViews, int i) {
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(i);
        int dimensionPixelSize2 = this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.medium_content_padding_above_name);
        boolean z = this.mIsLeftToRight;
        remoteViews.setViewPadding(C1893R.C1897id.medium_content, z ? dimensionPixelSize : 0, 0, z ? 0 : dimensionPixelSize, dimensionPixelSize2);
    }

    private ConversationStatus getBirthdayStatus(List<ConversationStatus> list) {
        Optional<ConversationStatus> findFirst = list.stream().filter(new PeopleTileViewHelper$$ExternalSyntheticLambda2()).findFirst();
        if (findFirst.isPresent()) {
            return findFirst.get();
        }
        if (!TextUtils.isEmpty(this.mTile.getBirthdayText())) {
            return new ConversationStatus.Builder(this.mTile.getId(), 1).build();
        }
        return null;
    }

    static /* synthetic */ boolean lambda$getBirthdayStatus$6(ConversationStatus conversationStatus) {
        return conversationStatus.getActivity() == 1;
    }

    /* access modifiers changed from: private */
    /* renamed from: isStatusValidForEntireStatusView */
    public boolean mo35148xe68ae94c(ConversationStatus conversationStatus) {
        int activity = conversationStatus.getActivity();
        if (activity == 1 || activity == 2 || !TextUtils.isEmpty(conversationStatus.getDescription()) || conversationStatus.getIcon() != null) {
            return true;
        }
        return false;
    }

    private String getStatusTextByType(int i) {
        switch (i) {
            case 1:
                return this.mContext.getString(C1893R.string.birthday_status);
            case 2:
                return this.mContext.getString(C1893R.string.anniversary_status);
            case 3:
                return this.mContext.getString(C1893R.string.new_story_status);
            case 4:
                return this.mContext.getString(C1893R.string.audio_status);
            case 5:
                return this.mContext.getString(C1893R.string.video_status);
            case 6:
                return this.mContext.getString(C1893R.string.game_status);
            case 7:
                return this.mContext.getString(C1893R.string.location_status);
            case 8:
                return this.mContext.getString(C1893R.string.upcoming_birthday_status);
            default:
                return "";
        }
    }

    private RemoteViews decorateBackground(RemoteViews remoteViews, CharSequence charSequence) {
        CharSequence doubleEmoji = getDoubleEmoji(charSequence);
        if (!TextUtils.isEmpty(doubleEmoji)) {
            setEmojiBackground(remoteViews, doubleEmoji);
            setPunctuationBackground(remoteViews, (CharSequence) null);
            return remoteViews;
        }
        CharSequence doublePunctuation = getDoublePunctuation(charSequence);
        setEmojiBackground(remoteViews, (CharSequence) null);
        setPunctuationBackground(remoteViews, doublePunctuation);
        return remoteViews;
    }

    private RemoteViews setEmojiBackground(RemoteViews remoteViews, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            remoteViews.setViewVisibility(C1893R.C1897id.emojis, 8);
            return remoteViews;
        }
        remoteViews.setTextViewText(C1893R.C1897id.emoji1, charSequence);
        remoteViews.setTextViewText(C1893R.C1897id.emoji2, charSequence);
        remoteViews.setTextViewText(C1893R.C1897id.emoji3, charSequence);
        remoteViews.setViewVisibility(C1893R.C1897id.emojis, 0);
        return remoteViews;
    }

    private RemoteViews setPunctuationBackground(RemoteViews remoteViews, CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            remoteViews.setViewVisibility(C1893R.C1897id.punctuations, 8);
            return remoteViews;
        }
        remoteViews.setTextViewText(C1893R.C1897id.punctuation1, charSequence);
        remoteViews.setTextViewText(C1893R.C1897id.punctuation2, charSequence);
        remoteViews.setTextViewText(C1893R.C1897id.punctuation3, charSequence);
        remoteViews.setTextViewText(C1893R.C1897id.punctuation4, charSequence);
        remoteViews.setTextViewText(C1893R.C1897id.punctuation5, charSequence);
        remoteViews.setTextViewText(C1893R.C1897id.punctuation6, charSequence);
        remoteViews.setViewVisibility(C1893R.C1897id.punctuations, 0);
        return remoteViews;
    }

    /* access modifiers changed from: package-private */
    public CharSequence getDoublePunctuation(CharSequence charSequence) {
        if (!ANY_DOUBLE_MARK_PATTERN.matcher(charSequence).find()) {
            return null;
        }
        if (MIXED_MARK_PATTERN.matcher(charSequence).find()) {
            return "!?";
        }
        Matcher matcher = DOUBLE_QUESTION_PATTERN.matcher(charSequence);
        if (!matcher.find()) {
            return "!";
        }
        Matcher matcher2 = DOUBLE_EXCLAMATION_PATTERN.matcher(charSequence);
        if (matcher2.find() && matcher.start() >= matcher2.start()) {
            return "!";
        }
        return "?";
    }

    /* access modifiers changed from: package-private */
    public CharSequence getDoubleEmoji(CharSequence charSequence) {
        Matcher matcher = EMOJI_PATTERN.matcher(charSequence);
        ArrayList arrayList = new ArrayList();
        ArrayList arrayList2 = new ArrayList();
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            arrayList.add(new Pair(Integer.valueOf(start), Integer.valueOf(end)));
            arrayList2.add(charSequence.subSequence(start, end));
        }
        if (arrayList.size() < 2) {
            return null;
        }
        for (int i = 1; i < arrayList.size(); i++) {
            int i2 = i - 1;
            if (((Pair) arrayList.get(i)).first == ((Pair) arrayList.get(i2)).second && Objects.equals(arrayList2.get(i), arrayList2.get(i2))) {
                return (CharSequence) arrayList2.get(i);
            }
        }
        return null;
    }

    private RemoteViews setViewForContentLayout(RemoteViews remoteViews) {
        RemoteViews decorateBackground = decorateBackground(remoteViews, "");
        decorateBackground.setContentDescription(C1893R.C1897id.predefined_icon, (CharSequence) null);
        decorateBackground.setContentDescription(C1893R.C1897id.text_content, (CharSequence) null);
        decorateBackground.setContentDescription(C1893R.C1897id.name, (CharSequence) null);
        decorateBackground.setContentDescription(C1893R.C1897id.image, (CharSequence) null);
        decorateBackground.setAccessibilityTraversalAfter(C1893R.C1897id.text_content, C1893R.C1897id.name);
        if (this.mLayoutSize == 0) {
            decorateBackground.setViewVisibility(C1893R.C1897id.predefined_icon, 0);
            decorateBackground.setViewVisibility(C1893R.C1897id.name, 8);
        } else {
            decorateBackground.setViewVisibility(C1893R.C1897id.predefined_icon, 8);
            decorateBackground.setViewVisibility(C1893R.C1897id.name, 0);
            decorateBackground.setViewVisibility(C1893R.C1897id.text_content, 0);
            decorateBackground.setViewVisibility(C1893R.C1897id.subtext, 8);
            decorateBackground.setViewVisibility(C1893R.C1897id.image, 8);
            decorateBackground.setViewVisibility(C1893R.C1897id.scrim_layout, 8);
        }
        if (this.mLayoutSize == 1) {
            int floor = (int) Math.floor((double) (this.mDensity * 16.0f));
            int floor2 = (int) Math.floor((double) (((float) this.mMediumVerticalPadding) * this.mDensity));
            RemoteViews remoteViews2 = decorateBackground;
            remoteViews2.setViewPadding(C1893R.C1897id.content, floor, floor2, floor, floor2);
            remoteViews2.setViewPadding(C1893R.C1897id.name, 0, 0, 0, 0);
            if (this.mHeight > ((int) (this.mContext.getResources().getDimension(C1893R.dimen.medium_height_for_max_name_text_size) / this.mDensity))) {
                decorateBackground.setTextViewTextSize(C1893R.C1897id.name, 0, (float) ((int) this.mContext.getResources().getDimension(C1893R.dimen.max_name_text_size_for_medium)));
            }
        }
        if (this.mLayoutSize == 2) {
            decorateBackground.setViewPadding(C1893R.C1897id.name, 0, 0, 0, this.mContext.getResources().getDimensionPixelSize(C1893R.dimen.below_name_text_padding));
            decorateBackground.setInt(C1893R.C1897id.content, "setGravity", 48);
        }
        decorateBackground.setViewLayoutHeightDimen(C1893R.C1897id.predefined_icon, C1893R.dimen.regular_predefined_icon);
        decorateBackground.setViewLayoutWidthDimen(C1893R.C1897id.predefined_icon, C1893R.dimen.regular_predefined_icon);
        decorateBackground.setViewVisibility(C1893R.C1897id.messages_count, 8);
        if (this.mTile.getUserName() != null) {
            decorateBackground.setTextViewText(C1893R.C1897id.name, this.mTile.getUserName());
        }
        return decorateBackground;
    }

    private RemoteViews createLastInteractionRemoteViews() {
        RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), getEmptyLayout());
        remoteViews.setInt(C1893R.C1897id.name, "setMaxLines", 1);
        if (this.mLayoutSize == 0) {
            remoteViews.setViewVisibility(C1893R.C1897id.name, 0);
            remoteViews.setViewVisibility(C1893R.C1897id.predefined_icon, 8);
            remoteViews.setViewVisibility(C1893R.C1897id.messages_count, 8);
        }
        if (this.mTile.getUserName() != null) {
            remoteViews.setTextViewText(C1893R.C1897id.name, this.mTile.getUserName());
        }
        String lastInteractionString = getLastInteractionString(this.mContext, this.mTile.getLastInteractionTimestamp());
        if (lastInteractionString != null) {
            remoteViews.setViewVisibility(C1893R.C1897id.last_interaction, 0);
            remoteViews.setTextViewText(C1893R.C1897id.last_interaction, lastInteractionString);
        } else {
            remoteViews.setViewVisibility(C1893R.C1897id.last_interaction, 8);
            if (this.mLayoutSize == 1) {
                remoteViews.setInt(C1893R.C1897id.name, "setMaxLines", 3);
            }
        }
        return remoteViews;
    }

    private int getEmptyLayout() {
        int i = this.mLayoutSize;
        if (i != 1) {
            return i != 2 ? getLayoutSmallByHeight() : C1893R.layout.people_tile_large_empty;
        }
        return C1893R.layout.people_tile_medium_empty;
    }

    private int getLayoutForNotificationContent() {
        int i = this.mLayoutSize;
        if (i != 1) {
            return i != 2 ? getLayoutSmallByHeight() : C1893R.layout.people_tile_large_with_notification_content;
        }
        return C1893R.layout.people_tile_medium_with_content;
    }

    private int getLayoutForContent() {
        int i = this.mLayoutSize;
        if (i != 1) {
            return i != 2 ? getLayoutSmallByHeight() : C1893R.layout.people_tile_large_with_status_content;
        }
        return C1893R.layout.people_tile_medium_with_content;
    }

    private int getViewForDndRemoteViews() {
        int i = this.mLayoutSize;
        if (i != 1) {
            return i != 2 ? getLayoutSmallByHeight() : C1893R.layout.people_tile_with_suppression_detail_content_vertical;
        }
        return C1893R.layout.people_tile_with_suppression_detail_content_horizontal;
    }

    private int getLayoutSmallByHeight() {
        return this.mHeight >= getSizeInDp(C1893R.dimen.required_height_for_medium) ? C1893R.layout.people_tile_small : C1893R.layout.people_tile_small_horizontal;
    }

    public static Bitmap getPersonIconBitmap(Context context, PeopleSpaceTile peopleSpaceTile, int i) {
        return getPersonIconBitmap(context, peopleSpaceTile, i, getHasNewStory(peopleSpaceTile));
    }

    private static Bitmap getPersonIconBitmap(Context context, PeopleSpaceTile peopleSpaceTile, int i, boolean z) {
        Icon userIcon = peopleSpaceTile.getUserIcon();
        if (userIcon == null) {
            Drawable mutate = context.getDrawable(C1893R.C1895drawable.ic_avatar_with_badge).mutate();
            mutate.setColorFilter(FastBitmapDrawable.getDisabledColorFilter());
            return PeopleSpaceUtils.convertDrawableToBitmap(mutate);
        }
        PeopleStoryIconFactory peopleStoryIconFactory = new PeopleStoryIconFactory(context, context.getPackageManager(), IconDrawableFactory.newInstance(context, false), i);
        RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(context.getResources(), userIcon.getBitmap());
        Drawable peopleTileDrawable = peopleStoryIconFactory.getPeopleTileDrawable(create, peopleSpaceTile.getPackageName(), PeopleSpaceUtils.getUserId(peopleSpaceTile), peopleSpaceTile.isImportantConversation(), z);
        if (isDndBlockingTileData(peopleSpaceTile)) {
            peopleTileDrawable.setColorFilter(FastBitmapDrawable.getDisabledColorFilter());
        }
        return PeopleSpaceUtils.convertDrawableToBitmap(peopleTileDrawable);
    }

    public static String getLastInteractionString(Context context, long j) {
        if (j == 0) {
            Log.e(TAG, "Could not get valid last interaction");
            return null;
        }
        Duration ofMillis = Duration.ofMillis(System.currentTimeMillis() - j);
        if (ofMillis.toDays() <= 1) {
            return null;
        }
        if (ofMillis.toDays() < 7) {
            return context.getString(C1893R.string.days_timestamp, new Object[]{Long.valueOf(ofMillis.toDays())});
        } else if (ofMillis.toDays() == 7) {
            return context.getString(C1893R.string.one_week_timestamp);
        } else {
            if (ofMillis.toDays() < 14) {
                return context.getString(C1893R.string.over_one_week_timestamp);
            }
            if (ofMillis.toDays() == 14) {
                return context.getString(C1893R.string.two_weeks_timestamp);
            }
            return context.getString(C1893R.string.over_two_weeks_timestamp);
        }
    }

    private int estimateTextHeight(CharSequence charSequence, int i, int i2) {
        StaticLayout buildStaticLayout = buildStaticLayout(charSequence, i, i2);
        if (buildStaticLayout == null) {
            return Integer.MAX_VALUE;
        }
        return pxToDp((float) buildStaticLayout.getHeight());
    }

    private StaticLayout buildStaticLayout(CharSequence charSequence, int i, int i2) {
        try {
            TextView textView = new TextView(this.mContext);
            textView.setTextSize(0, this.mContext.getResources().getDimension(i));
            textView.setTextAppearance(16974253);
            return StaticLayout.Builder.obtain(charSequence, 0, charSequence.length(), textView.getPaint(), dpToPx((float) i2)).setBreakStrategy(0).build();
        } catch (Exception e) {
            Log.e(TAG, "Could not create static layout: " + e);
            return null;
        }
    }

    private int dpToPx(float f) {
        return (int) (f * this.mDensity);
    }

    private int pxToDp(float f) {
        return (int) (f / this.mDensity);
    }

    private static final class RemoteViewsAndSizes {
        final int mAvatarSize;
        final RemoteViews mRemoteViews;

        RemoteViewsAndSizes(RemoteViews remoteViews, int i) {
            this.mRemoteViews = remoteViews;
            this.mAvatarSize = i;
        }
    }
}
