package com.android.systemui.p012qs.customize;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArraySet;
import android.widget.Button;
import com.android.systemui.C1894R;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.p012qs.QSTileHost;
import com.android.systemui.p012qs.dagger.QSScope;
import com.android.systemui.p012qs.external.CustomTile;
import com.android.systemui.p012qs.tileimpl.QSTileImpl;
import com.android.systemui.plugins.p011qs.QSTile;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.util.leak.GarbageMonitor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executor;
import javax.inject.Inject;

@QSScope
/* renamed from: com.android.systemui.qs.customize.TileQueryHelper */
public class TileQueryHelper {
    private static final String TAG = "TileQueryHelper";
    /* access modifiers changed from: private */
    public final Executor mBgExecutor;
    private final Context mContext;
    private boolean mFinished;
    private TileStateListener mListener;
    private final Executor mMainExecutor;
    private final ArraySet<String> mSpecs = new ArraySet<>();
    private final ArrayList<TileInfo> mTiles = new ArrayList<>();
    private final UserTracker mUserTracker;

    /* renamed from: com.android.systemui.qs.customize.TileQueryHelper$TileStateListener */
    public interface TileStateListener {
        void onTilesChanged(List<TileInfo> list);
    }

    @Inject
    public TileQueryHelper(Context context, UserTracker userTracker, @Main Executor executor, @Background Executor executor2) {
        this.mContext = context;
        this.mMainExecutor = executor;
        this.mBgExecutor = executor2;
        this.mUserTracker = userTracker;
    }

    public void setListener(TileStateListener tileStateListener) {
        this.mListener = tileStateListener;
    }

    public void queryTiles(QSTileHost qSTileHost) {
        this.mTiles.clear();
        this.mSpecs.clear();
        this.mFinished = false;
        addCurrentAndStockTiles(qSTileHost);
    }

    public boolean isFinished() {
        return this.mFinished;
    }

    private void addCurrentAndStockTiles(QSTileHost qSTileHost) {
        QSTile createTile;
        String string = this.mContext.getString(C1894R.string.quick_settings_tiles_stock);
        String string2 = Settings.Secure.getString(this.mContext.getContentResolver(), QSTileHost.TILES_SETTING);
        ArrayList arrayList = new ArrayList();
        if (string2 != null) {
            arrayList.addAll(Arrays.asList(string2.split(NavigationBarInflaterView.BUTTON_SEPARATOR)));
        } else {
            string2 = "";
        }
        for (String str : string.split(NavigationBarInflaterView.BUTTON_SEPARATOR)) {
            if (!string2.contains(str)) {
                arrayList.add(str);
            }
        }
        if (Build.IS_DEBUGGABLE && !string2.contains(GarbageMonitor.MemoryTile.TILE_SPEC)) {
            arrayList.add(GarbageMonitor.MemoryTile.TILE_SPEC);
        }
        ArrayList arrayList2 = new ArrayList();
        arrayList.remove((Object) "cell");
        arrayList.remove((Object) "wifi");
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            if (!str2.startsWith(CustomTile.PREFIX) && (createTile = qSTileHost.createTile(str2)) != null) {
                if (!createTile.isAvailable()) {
                    createTile.setTileSpec(str2);
                    createTile.destroy();
                } else {
                    createTile.setTileSpec(str2);
                    arrayList2.add(createTile);
                }
            }
        }
        new TileCollector(arrayList2, qSTileHost).startListening();
    }

    /* renamed from: com.android.systemui.qs.customize.TileQueryHelper$TilePair */
    private static class TilePair {
        boolean mReady;
        QSTile mTile;

        private TilePair(QSTile qSTile) {
            this.mReady = false;
            this.mTile = qSTile;
        }
    }

    /* renamed from: com.android.systemui.qs.customize.TileQueryHelper$TileCollector */
    private class TileCollector implements QSTile.Callback {
        private final QSTileHost mQSTileHost;
        private final List<TilePair> mQSTileList = new ArrayList();

        TileCollector(List<QSTile> list, QSTileHost qSTileHost) {
            for (QSTile tilePair : list) {
                this.mQSTileList.add(new TilePair(tilePair));
            }
            this.mQSTileHost = qSTileHost;
            if (list.isEmpty()) {
                TileQueryHelper.this.mBgExecutor.execute(new TileQueryHelper$TileCollector$$ExternalSyntheticLambda0(this));
            }
        }

        /* access modifiers changed from: private */
        public void finished() {
            TileQueryHelper.this.notifyTilesChanged(false);
            TileQueryHelper.this.addPackageTiles(this.mQSTileHost);
        }

        /* access modifiers changed from: private */
        public void startListening() {
            for (TilePair next : this.mQSTileList) {
                next.mTile.addCallback(this);
                next.mTile.setListening(this, true);
                next.mTile.refreshState();
            }
        }

        public void onStateChanged(QSTile.State state) {
            boolean z = true;
            for (TilePair next : this.mQSTileList) {
                if (!next.mReady && next.mTile.isTileReady()) {
                    next.mTile.removeCallback(this);
                    next.mTile.setListening(this, false);
                    next.mReady = true;
                } else if (!next.mReady) {
                    z = false;
                }
            }
            if (z) {
                for (TilePair tilePair : this.mQSTileList) {
                    QSTile qSTile = tilePair.mTile;
                    QSTile.State copy = qSTile.getState().copy();
                    copy.label = qSTile.getTileLabel();
                    qSTile.destroy();
                    TileQueryHelper.this.addTile(qSTile.getTileSpec(), (CharSequence) null, copy, true);
                }
                finished();
            }
        }
    }

    /* access modifiers changed from: private */
    public void addPackageTiles(QSTileHost qSTileHost) {
        this.mBgExecutor.execute(new TileQueryHelper$$ExternalSyntheticLambda0(this, qSTileHost));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$addPackageTiles$0$com-android-systemui-qs-customize-TileQueryHelper */
    public /* synthetic */ void mo36556x4cc84b7e(QSTileHost qSTileHost) {
        Collection<QSTile> tiles = qSTileHost.getTiles();
        PackageManager packageManager = this.mContext.getPackageManager();
        List<ResolveInfo> queryIntentServicesAsUser = packageManager.queryIntentServicesAsUser(new Intent("android.service.quicksettings.action.QS_TILE"), 0, this.mUserTracker.getUserId());
        String string = this.mContext.getString(C1894R.string.quick_settings_tiles_stock);
        for (ResolveInfo resolveInfo : queryIntentServicesAsUser) {
            ComponentName componentName = new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
            if (!string.contains(componentName.flattenToString())) {
                CharSequence loadLabel = resolveInfo.serviceInfo.applicationInfo.loadLabel(packageManager);
                String spec = CustomTile.toSpec(componentName);
                QSTile.State state = getState(tiles, spec);
                if (state != null) {
                    addTile(spec, loadLabel, state, false);
                } else if (resolveInfo.serviceInfo.icon != 0 || resolveInfo.serviceInfo.applicationInfo.icon != 0) {
                    Drawable loadIcon = resolveInfo.serviceInfo.loadIcon(packageManager);
                    if ("android.permission.BIND_QUICK_SETTINGS_TILE".equals(resolveInfo.serviceInfo.permission) && loadIcon != null) {
                        loadIcon.mutate();
                        loadIcon.setTint(this.mContext.getColor(17170443));
                        CharSequence loadLabel2 = resolveInfo.serviceInfo.loadLabel(packageManager);
                        createStateAndAddTile(spec, loadIcon, loadLabel2 != null ? loadLabel2.toString() : "null", loadLabel);
                    }
                }
            }
        }
        notifyTilesChanged(true);
    }

    /* access modifiers changed from: private */
    public void notifyTilesChanged(boolean z) {
        this.mMainExecutor.execute(new TileQueryHelper$$ExternalSyntheticLambda1(this, new ArrayList(this.mTiles), z));
    }

    /* access modifiers changed from: package-private */
    /* renamed from: lambda$notifyTilesChanged$1$com-android-systemui-qs-customize-TileQueryHelper */
    public /* synthetic */ void mo36557x8126aa51(ArrayList arrayList, boolean z) {
        TileStateListener tileStateListener = this.mListener;
        if (tileStateListener != null) {
            tileStateListener.onTilesChanged(arrayList);
        }
        this.mFinished = z;
    }

    private QSTile.State getState(Collection<QSTile> collection, String str) {
        for (QSTile next : collection) {
            if (str.equals(next.getTileSpec())) {
                return next.getState().copy();
            }
        }
        return null;
    }

    /* access modifiers changed from: private */
    public void addTile(String str, CharSequence charSequence, QSTile.State state, boolean z) {
        if (!this.mSpecs.contains(str)) {
            state.dualTarget = false;
            state.expandedAccessibilityClassName = Button.class.getName();
            if (z || TextUtils.equals(state.label, charSequence)) {
                charSequence = null;
            }
            state.secondaryLabel = charSequence;
            this.mTiles.add(new TileInfo(str, state, z));
            this.mSpecs.add(str);
        }
    }

    private void createStateAndAddTile(String str, Drawable drawable, CharSequence charSequence, CharSequence charSequence2) {
        QSTile.State state = new QSTile.State();
        state.state = 1;
        state.label = charSequence;
        state.contentDescription = charSequence;
        state.icon = new QSTileImpl.DrawableIcon(drawable);
        addTile(str, charSequence2, state, false);
    }

    /* renamed from: com.android.systemui.qs.customize.TileQueryHelper$TileInfo */
    public static class TileInfo {
        public boolean isSystem;
        public String spec;
        public QSTile.State state;

        public TileInfo(String str, QSTile.State state2, boolean z) {
            this.spec = str;
            this.state = state2;
            this.isSystem = z;
        }
    }
}
