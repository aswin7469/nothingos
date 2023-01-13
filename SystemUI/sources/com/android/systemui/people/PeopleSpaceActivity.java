package com.android.systemui.people;

import android.app.Activity;
import android.app.people.PeopleSpaceTile;
import android.content.Context;
import android.content.Intent;
import android.graphics.Outline;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.LinearLayout;
import com.android.systemui.C1894R;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.people.widget.PeopleTileKey;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class PeopleSpaceActivity extends Activity {
    private static final boolean DEBUG = false;
    private static final String TAG = "PeopleSpaceActivity";
    private int mAppWidgetId;
    /* access modifiers changed from: private */
    public Context mContext;
    private PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;
    private ViewOutlineProvider mViewOutlineProvider = new ViewOutlineProvider() {
        public void getOutline(View view, Outline outline) {
            outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), PeopleSpaceActivity.this.mContext.getResources().getDimension(C1894R.dimen.people_space_widget_radius));
        }
    };

    @Inject
    public PeopleSpaceActivity(PeopleSpaceWidgetManager peopleSpaceWidgetManager) {
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = getApplicationContext();
        this.mAppWidgetId = getIntent().getIntExtra("appWidgetId", 0);
        setResult(0);
    }

    private void buildActivity() {
        List arrayList = new ArrayList();
        List arrayList2 = new ArrayList();
        try {
            arrayList = this.mPeopleSpaceWidgetManager.getPriorityTiles();
            arrayList2 = this.mPeopleSpaceWidgetManager.getRecentTiles();
        } catch (Exception e) {
            Log.e(TAG, "Couldn't retrieve conversations", e);
        }
        if (!arrayList2.isEmpty() || !arrayList.isEmpty()) {
            setContentView(C1894R.layout.people_space_activity);
            setTileViews(C1894R.C1898id.priority, C1894R.C1898id.priority_tiles, arrayList);
            setTileViews(C1894R.C1898id.recent, C1894R.C1898id.recent_tiles, arrayList2);
            return;
        }
        setContentView(C1894R.layout.people_space_activity_no_conversations);
        ((GradientDrawable) ((LinearLayout) findViewById(16908288)).getBackground()).setColor(this.mContext.getTheme().obtainStyledAttributes(new int[]{17956909}).getColor(0, -1));
    }

    private void setTileViews(int i, int i2, List<PeopleSpaceTile> list) {
        if (list.isEmpty()) {
            ((LinearLayout) findViewById(i)).setVisibility(8);
            return;
        }
        ViewGroup viewGroup = (ViewGroup) findViewById(i2);
        viewGroup.setClipToOutline(true);
        viewGroup.setOutlineProvider(this.mViewOutlineProvider);
        int i3 = 0;
        while (i3 < list.size()) {
            PeopleSpaceTile peopleSpaceTile = list.get(i3);
            setTileView(new PeopleSpaceTileView(this.mContext, viewGroup, peopleSpaceTile.getId(), i3 == list.size() - 1), peopleSpaceTile);
            i3++;
        }
    }

    private void setTileView(PeopleSpaceTileView peopleSpaceTileView, PeopleSpaceTile peopleSpaceTile) {
        try {
            if (peopleSpaceTile.getUserName() != null) {
                peopleSpaceTileView.setName(peopleSpaceTile.getUserName().toString());
            }
            Context context = this.mContext;
            peopleSpaceTileView.setPersonIcon(PeopleTileViewHelper.getPersonIconBitmap(context, peopleSpaceTile, PeopleTileViewHelper.getSizeInDp(context, C1894R.dimen.avatar_size_for_medium, context.getResources().getDisplayMetrics().density)));
            peopleSpaceTileView.setOnClickListener(new PeopleSpaceActivity$$ExternalSyntheticLambda0(this, peopleSpaceTile, new PeopleTileKey(peopleSpaceTile)));
        } catch (Exception e) {
            Log.e(TAG, "Couldn't retrieve shortcut information", e);
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$setTileView$0$com-android-systemui-people-PeopleSpaceActivity */
    public /* synthetic */ void mo35127xf6054568(PeopleSpaceTile peopleSpaceTile, PeopleTileKey peopleTileKey, View view) {
        storeWidgetConfiguration(peopleSpaceTile, peopleTileKey);
    }

    private void storeWidgetConfiguration(PeopleSpaceTile peopleSpaceTile, PeopleTileKey peopleTileKey) {
        this.mPeopleSpaceWidgetManager.addNewWidget(this.mAppWidgetId, peopleTileKey);
        finishActivity();
    }

    private void finishActivity() {
        setActivityResult(-1);
        finish();
    }

    public void dismissActivity(View view) {
        finish();
    }

    private void setActivityResult(int i) {
        Intent intent = new Intent();
        intent.putExtra("appWidgetId", this.mAppWidgetId);
        setResult(i, intent);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        buildActivity();
    }
}
