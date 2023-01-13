package com.android.systemui.media;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceConfig;
import android.app.smartspace.SmartspaceManager;
import android.app.smartspace.SmartspaceSession;
import android.app.smartspace.SmartspaceTarget;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.MediaDescription;
import android.media.MediaMetadata;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.Trace;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import androidx.media.utils.MediaConstants;
import com.android.internal.logging.InstanceId;
import com.android.settingslib.Utils;
import com.android.systemui.C1894R;
import com.android.systemui.Dumpable;
import com.android.systemui.biometrics.AuthDialog;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dagger.SysUISingleton;
import com.android.systemui.dagger.qualifiers.Background;
import com.android.systemui.dagger.qualifiers.Main;
import com.android.systemui.demomode.DemoMode;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.Assert;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.time.SystemClock;
import com.google.android.setupcompat.internal.FocusChangedMetricHelper;
import java.p026io.IOException;
import java.p026io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import kotlin.Metadata;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.sequences.SequencesKt;

@SysUISingleton
@Metadata(mo65042d1 = {"\u0000Ñ\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0005\n\u0002\b\u0003\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0010\t\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0011\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0012\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0012*\u0001/\b\u0007\u0018\u0000 ¥\u00012\u00020\u00012\u00020\u0002:\u0004¥\u0001¦\u0001B\u0001\b\u0017\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0001\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016\u0012\u0006\u0010\u0017\u001a\u00020\u0018\u0012\u0006\u0010\u0019\u001a\u00020\u001a\u0012\u0006\u0010\u001b\u001a\u00020\u001c\u0012\u0006\u0010\u001d\u001a\u00020\u001e\u0012\u0006\u0010\u001f\u001a\u00020 \u0012\u0006\u0010!\u001a\u00020\"\u0012\u0006\u0010#\u001a\u00020$\u0012\u0006\u0010%\u001a\u00020&¢\u0006\u0002\u0010'B©\u0001\u0012\u0006\u0010\u0003\u001a\u00020\u0004\u0012\b\b\u0001\u0010\u0005\u001a\u00020\u0006\u0012\b\b\u0001\u0010\u0007\u001a\u00020\b\u0012\u0006\u0010\t\u001a\u00020\n\u0012\u0006\u0010\r\u001a\u00020\u000e\u0012\u0006\u0010\u000b\u001a\u00020\f\u0012\u0006\u0010\u000f\u001a\u00020\u0010\u0012\u0006\u0010\u0011\u001a\u00020\u0012\u0012\u0006\u0010\u0013\u001a\u00020\u0014\u0012\u0006\u0010\u0015\u001a\u00020\u0016\u0012\u0006\u0010\u0017\u001a\u00020\u0018\u0012\u0006\u0010\u0019\u001a\u00020\u001a\u0012\u0006\u0010\u001b\u001a\u00020\u001c\u0012\u0006\u0010\u001d\u001a\u00020\u001e\u0012\u0006\u0010(\u001a\u00020)\u0012\u0006\u0010*\u001a\u00020)\u0012\u0006\u0010+\u001a\u00020 \u0012\u0006\u0010!\u001a\u00020\"\u0012\u0006\u0010#\u001a\u00020$\u0012\u0006\u0010%\u001a\u00020&¢\u0006\u0002\u0010,J\u0010\u0010C\u001a\u00020)2\u0006\u0010D\u001a\u000203H\u0002J\u000e\u0010E\u001a\u00020F2\u0006\u0010D\u001a\u000203J>\u0010G\u001a\u00020F2\u0006\u0010H\u001a\u00020B2\u0006\u0010I\u001a\u00020J2\u0006\u0010K\u001a\u00020L2\u0006\u0010M\u001a\u00020N2\u0006\u0010O\u001a\u0002062\u0006\u0010P\u001a\u00020Q2\u0006\u0010R\u001a\u000206J(\u0010S\u001a\u001a\u0012\n\u0012\b\u0012\u0004\u0012\u00020V0U\u0012\n\u0012\b\u0012\u0004\u0012\u00020B0U0T2\u0006\u0010W\u001a\u00020XH\u0002J\"\u0010Y\u001a\u0004\u0018\u00010Z2\u0006\u0010R\u001a\u0002062\u0006\u0010[\u001a\u00020\\2\u0006\u0010]\u001a\u00020^H\u0002J\u0006\u0010_\u001a\u00020FJ\u0016\u0010`\u001a\u00020)2\u0006\u0010a\u001a\u0002062\u0006\u0010b\u001a\u00020cJ\u0016\u0010d\u001a\u00020F2\u0006\u0010a\u001a\u0002062\u0006\u0010b\u001a\u00020cJ%\u0010e\u001a\u00020F2\u0006\u0010f\u001a\u00020g2\u000e\u0010h\u001a\n\u0012\u0006\b\u0001\u0012\u0002060iH\u0016¢\u0006\u0002\u0010jJ\u001a\u0010k\u001a\u0004\u0018\u0001062\u0006\u0010a\u001a\u0002062\u0006\u0010R\u001a\u000206H\u0002J(\u0010l\u001a\u00020V2\u0006\u0010m\u001a\u00020n2\u0006\u0010R\u001a\u0002062\u0006\u0010[\u001a\u00020\\2\u0006\u0010o\u001a\u00020pH\u0002J\u0010\u0010q\u001a\u00020V2\u0006\u0010K\u001a\u00020LH\u0002J\"\u0010r\u001a\u0004\u0018\u00010V2\u0006\u0010[\u001a\u00020\\2\u0006\u0010s\u001a\u00020c2\u0006\u0010K\u001a\u00020cH\u0002J\u0006\u0010t\u001a\u00020)J\u0006\u0010u\u001a\u00020)J\u0006\u0010v\u001a\u00020)J\u0006\u0010w\u001a\u00020)J\u0018\u0010x\u001a\u00020)2\u0006\u0010s\u001a\u00020c2\u0006\u0010K\u001a\u00020cH\u0002J\u0010\u0010y\u001a\u00020)2\u0006\u0010W\u001a\u00020XH\u0002J\u0012\u0010z\u001a\u0004\u0018\u00010{2\u0006\u0010|\u001a\u00020}H\u0002J\u0012\u0010z\u001a\u0004\u0018\u00010{2\u0006\u0010~\u001a\u00020H\u0002J/\u0010\u0001\u001a\u00020F2\u0006\u0010a\u001a\u0002062\u0006\u0010W\u001a\u00020X2\t\u0010\u0001\u001a\u0004\u0018\u0001062\t\b\u0002\u0010\u0001\u001a\u00020)H\u0002J-\u0010\u0001\u001a\u00020F2\u0006\u0010a\u001a\u0002062\u0006\u0010W\u001a\u00020X2\t\u0010\u0001\u001a\u0004\u0018\u0001062\t\b\u0002\u0010\u0001\u001a\u00020)JB\u0010\u0001\u001a\u00020F2\u0006\u0010H\u001a\u00020B2\u0006\u0010I\u001a\u00020J2\u0007\u0010\u0001\u001a\u00020L2\u0006\u0010M\u001a\u00020N2\u0006\u0010O\u001a\u0002062\u0006\u0010P\u001a\u00020Q2\u0006\u0010R\u001a\u000206H\u0002J%\u0010\u0001\u001a\u00020F2\u0006\u0010a\u001a\u0002062\t\u0010\u0001\u001a\u0004\u0018\u0001062\u0007\u0010\u0001\u001a\u000207H\u0002J\u0011\u0010\u0001\u001a\u00020F2\u0006\u0010a\u001a\u000206H\u0002J\u001a\u0010\u0001\u001a\u00020F2\u0006\u0010a\u001a\u0002062\u0007\u0010\u0001\u001a\u00020:H\u0002J\u001a\u0010\u0001\u001a\u00020F2\u0006\u0010a\u001a\u0002062\u0007\u0010\u0001\u001a\u00020)H\u0002J#\u0010\u0001\u001a\u00020F2\u0006\u0010a\u001a\u0002062\t\u0010\u0001\u001a\u0004\u0018\u0001062\u0007\u0010\u0001\u001a\u000207J\u0017\u0010\u0001\u001a\u00020F2\u0006\u0010a\u001a\u0002062\u0006\u0010W\u001a\u00020XJ\u000f\u0010\u0001\u001a\u00020F2\u0006\u0010a\u001a\u000206J\u0019\u0010\u0001\u001a\u00020F2\u000e\u0010\u0001\u001a\t\u0012\u0005\u0012\u00030\u00010UH\u0016J\u0007\u0010\u0001\u001a\u00020FJ\u0014\u0010R\u001a\u0004\u0018\u0001062\b\u0010\u0001\u001a\u00030\u0001H\u0002J\u0011\u0010\u0001\u001a\u00020F2\u0006\u0010R\u001a\u000206H\u0002J\u0011\u0010\u0001\u001a\u00020F2\u0006\u0010a\u001a\u000206H\u0002J\u000f\u0010\u0001\u001a\u00020F2\u0006\u0010D\u001a\u000203J\u0012\u0010\u0001\u001a\u00020)2\u0007\u0010\u0001\u001a\u00020QH\u0002J\u0010\u0010\u0001\u001a\u00020F2\u0007\u0010\u0001\u001a\u00020)J\u0019\u0010\u0001\u001a\u00020F2\u0006\u0010a\u001a\u0002062\b\u0010K\u001a\u0004\u0018\u00010LJ+\u0010\u0001\u001a\u00020F2\u0006\u0010a\u001a\u0002062\u0007\u0010\u0001\u001a\u00020)2\t\b\u0002\u0010 \u0001\u001a\u00020)H\u0000¢\u0006\u0003\b¡\u0001J\u001c\u0010¢\u0001\u001a\u00020:2\b\u0010\u0001\u001a\u00030\u00012\u0007\u0010£\u0001\u001a\u00020)H\u0002J\u0019\u0010¤\u0001\u001a\u00020F2\u0006\u0010a\u001a\u0002062\u0006\u0010m\u001a\u00020nH\u0002R\u000e\u0010\u001b\u001a\u00020\u001cX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010-\u001a\u00020)X\u000e¢\u0006\u0002\n\u0000R\u0010\u0010.\u001a\u00020/X\u0004¢\u0006\u0004\n\u0002\u00100R\u000e\u0010\u0005\u001a\u00020\u0006X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0003\u001a\u00020\u0004X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0004¢\u0006\u0002\n\u0000R\u0014\u00101\u001a\b\u0012\u0004\u0012\u00020302X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010%\u001a\u00020&X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0019\u001a\u00020\u001aX\u0004¢\u0006\u0002\n\u0000R*\u00104\u001a\u001e\u0012\u0004\u0012\u000206\u0012\u0004\u0012\u00020705j\u000e\u0012\u0004\u0012\u000206\u0012\u0004\u0012\u000207`8X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010#\u001a\u00020$X\u0004¢\u0006\u0002\n\u0000R\u001a\u00109\u001a\u00020:X\u000e¢\u0006\u000e\n\u0000\u001a\u0004\b;\u0010<\"\u0004\b=\u0010>R\u000e\u0010\u001d\u001a\u00020\u001eX\u0004¢\u0006\u0002\n\u0000R\u0010\u0010?\u001a\u0004\u0018\u00010@X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010+\u001a\u00020 X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010A\u001a\u00020BX\u0004¢\u0006\u0002\n\u0000R\u000e\u0010!\u001a\u00020\"X\u0004¢\u0006\u0002\n\u0000R\u000e\u0010(\u001a\u00020)X\u000e¢\u0006\u0002\n\u0000R\u000e\u0010*\u001a\u00020)X\u0004¢\u0006\u0002\n\u0000¨\u0006§\u0001"}, mo65043d2 = {"Lcom/android/systemui/media/MediaDataManager;", "Lcom/android/systemui/Dumpable;", "Lcom/android/systemui/plugins/BcSmartspaceDataPlugin$SmartspaceTargetListener;", "context", "Landroid/content/Context;", "backgroundExecutor", "Ljava/util/concurrent/Executor;", "foregroundExecutor", "Lcom/android/systemui/util/concurrency/DelayableExecutor;", "mediaControllerFactory", "Lcom/android/systemui/media/MediaControllerFactory;", "dumpManager", "Lcom/android/systemui/dump/DumpManager;", "broadcastDispatcher", "Lcom/android/systemui/broadcast/BroadcastDispatcher;", "mediaTimeoutListener", "Lcom/android/systemui/media/MediaTimeoutListener;", "mediaResumeListener", "Lcom/android/systemui/media/MediaResumeListener;", "mediaSessionBasedFilter", "Lcom/android/systemui/media/MediaSessionBasedFilter;", "mediaDeviceManager", "Lcom/android/systemui/media/MediaDeviceManager;", "mediaDataCombineLatest", "Lcom/android/systemui/media/MediaDataCombineLatest;", "mediaDataFilter", "Lcom/android/systemui/media/MediaDataFilter;", "activityStarter", "Lcom/android/systemui/plugins/ActivityStarter;", "smartspaceMediaDataProvider", "Lcom/android/systemui/media/SmartspaceMediaDataProvider;", "clock", "Lcom/android/systemui/util/time/SystemClock;", "tunerService", "Lcom/android/systemui/tuner/TunerService;", "mediaFlags", "Lcom/android/systemui/media/MediaFlags;", "logger", "Lcom/android/systemui/media/MediaUiEventLogger;", "(Landroid/content/Context;Ljava/util/concurrent/Executor;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/media/MediaControllerFactory;Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/broadcast/BroadcastDispatcher;Lcom/android/systemui/media/MediaTimeoutListener;Lcom/android/systemui/media/MediaResumeListener;Lcom/android/systemui/media/MediaSessionBasedFilter;Lcom/android/systemui/media/MediaDeviceManager;Lcom/android/systemui/media/MediaDataCombineLatest;Lcom/android/systemui/media/MediaDataFilter;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/media/SmartspaceMediaDataProvider;Lcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/tuner/TunerService;Lcom/android/systemui/media/MediaFlags;Lcom/android/systemui/media/MediaUiEventLogger;)V", "useMediaResumption", "", "useQsMediaPlayer", "systemClock", "(Landroid/content/Context;Ljava/util/concurrent/Executor;Lcom/android/systemui/util/concurrency/DelayableExecutor;Lcom/android/systemui/media/MediaControllerFactory;Lcom/android/systemui/broadcast/BroadcastDispatcher;Lcom/android/systemui/dump/DumpManager;Lcom/android/systemui/media/MediaTimeoutListener;Lcom/android/systemui/media/MediaResumeListener;Lcom/android/systemui/media/MediaSessionBasedFilter;Lcom/android/systemui/media/MediaDeviceManager;Lcom/android/systemui/media/MediaDataCombineLatest;Lcom/android/systemui/media/MediaDataFilter;Lcom/android/systemui/plugins/ActivityStarter;Lcom/android/systemui/media/SmartspaceMediaDataProvider;ZZLcom/android/systemui/util/time/SystemClock;Lcom/android/systemui/tuner/TunerService;Lcom/android/systemui/media/MediaFlags;Lcom/android/systemui/media/MediaUiEventLogger;)V", "allowMediaRecommendations", "appChangeReceiver", "com/android/systemui/media/MediaDataManager$appChangeReceiver$1", "Lcom/android/systemui/media/MediaDataManager$appChangeReceiver$1;", "internalListeners", "", "Lcom/android/systemui/media/MediaDataManager$Listener;", "mediaEntries", "Ljava/util/LinkedHashMap;", "", "Lcom/android/systemui/media/MediaData;", "Lkotlin/collections/LinkedHashMap;", "smartspaceMediaData", "Lcom/android/systemui/media/SmartspaceMediaData;", "getSmartspaceMediaData", "()Lcom/android/systemui/media/SmartspaceMediaData;", "setSmartspaceMediaData", "(Lcom/android/systemui/media/SmartspaceMediaData;)V", "smartspaceSession", "Landroid/app/smartspace/SmartspaceSession;", "themeText", "", "addInternalListener", "listener", "addListener", "", "addResumptionControls", "userId", "desc", "Landroid/media/MediaDescription;", "action", "Ljava/lang/Runnable;", "token", "Landroid/media/session/MediaSession$Token;", "appName", "appIntent", "Landroid/app/PendingIntent;", "packageName", "createActionsFromNotification", "Lkotlin/Pair;", "", "Lcom/android/systemui/media/MediaAction;", "sbn", "Landroid/service/notification/StatusBarNotification;", "createActionsFromState", "Lcom/android/systemui/media/MediaButton;", "controller", "Landroid/media/session/MediaController;", "user", "Landroid/os/UserHandle;", "destroy", "dismissMediaData", "key", "delay", "", "dismissSmartspaceRecommendation", "dump", "pw", "Ljava/io/PrintWriter;", "args", "", "(Ljava/io/PrintWriter;[Ljava/lang/String;)V", "findExistingEntry", "getCustomAction", "state", "Landroid/media/session/PlaybackState;", "customAction", "Landroid/media/session/PlaybackState$CustomAction;", "getResumeMediaAction", "getStandardAction", "stateActions", "hasActiveMedia", "hasActiveMediaOrRecommendation", "hasAnyMedia", "hasAnyMediaOrRecommendation", "includesAction", "isRemoteCastNotification", "loadBitmapFromUri", "Landroid/graphics/Bitmap;", "metadata", "Landroid/media/MediaMetadata;", "uri", "Landroid/net/Uri;", "loadMediaData", "oldKey", "logEvent", "loadMediaDataInBg", "loadMediaDataInBgForResumption", "resumeAction", "notifyMediaDataLoaded", "info", "notifyMediaDataRemoved", "notifySmartspaceMediaDataLoaded", "notifySmartspaceMediaDataRemoved", "immediately", "onMediaDataLoaded", "data", "onNotificationAdded", "onNotificationRemoved", "onSmartspaceTargetsUpdated", "targets", "Landroid/os/Parcelable;", "onSwipeToDismiss", "target", "Landroid/app/smartspace/SmartspaceTarget;", "removeAllForPackage", "removeEntry", "removeListener", "sendPendingIntent", "intent", "setMediaResumptionEnabled", "isEnabled", "setResumeAction", "setTimedOut", "timedOut", "forceUpdate", "setTimedOut$SystemUI_nothingRelease", "toSmartspaceMediaData", "isActive", "updateState", "Companion", "Listener", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
/* compiled from: MediaDataManager.kt */
public final class MediaDataManager implements Dumpable, BcSmartspaceDataPlugin.SmartspaceTargetListener {
    public static final Companion Companion = new Companion((DefaultConstructorMarker) null);
    public static final String EXTRAS_MEDIA_SOURCE_PACKAGE_NAME = "package_name";
    public static final int MAX_COMPACT_ACTIONS = 3;
    public static final int MAX_NOTIFICATION_ACTIONS = MediaViewHolder.Companion.getGenericButtonIds().size();
    public static final String SMARTSPACE_UI_SURFACE_LABEL = "media_data_manager";
    private final ActivityStarter activityStarter;
    /* access modifiers changed from: private */
    public boolean allowMediaRecommendations;
    private final MediaDataManager$appChangeReceiver$1 appChangeReceiver;
    private final Executor backgroundExecutor;
    private final BroadcastDispatcher broadcastDispatcher;
    /* access modifiers changed from: private */
    public final Context context;
    private final DelayableExecutor foregroundExecutor;
    private final Set<Listener> internalListeners;
    private final MediaUiEventLogger logger;
    private final MediaControllerFactory mediaControllerFactory;
    private final MediaDataFilter mediaDataFilter;
    private final LinkedHashMap<String, MediaData> mediaEntries;
    private final MediaFlags mediaFlags;
    private SmartspaceMediaData smartspaceMediaData;
    private final SmartspaceMediaDataProvider smartspaceMediaDataProvider;
    private SmartspaceSession smartspaceSession;
    private final SystemClock systemClock;
    private final int themeText;
    private final TunerService tunerService;
    private boolean useMediaResumption;
    private final boolean useQsMediaPlayer;

    /* access modifiers changed from: private */
    /* renamed from: createActionsFromNotification$lambda-27$lambda-26  reason: not valid java name */
    public static final void m2794createActionsFromNotification$lambda27$lambda26() {
    }

    private final boolean includesAction(long j, long j2) {
        return ((j2 == 4 || j2 == 2) && (512 & j) > 0) || (j & j2) != 0;
    }

    public MediaDataManager(Context context2, @Background Executor executor, @Main DelayableExecutor delayableExecutor, MediaControllerFactory mediaControllerFactory2, BroadcastDispatcher broadcastDispatcher2, DumpManager dumpManager, MediaTimeoutListener mediaTimeoutListener, MediaResumeListener mediaResumeListener, MediaSessionBasedFilter mediaSessionBasedFilter, MediaDeviceManager mediaDeviceManager, MediaDataCombineLatest mediaDataCombineLatest, MediaDataFilter mediaDataFilter2, ActivityStarter activityStarter2, SmartspaceMediaDataProvider smartspaceMediaDataProvider2, boolean z, boolean z2, SystemClock systemClock2, TunerService tunerService2, MediaFlags mediaFlags2, MediaUiEventLogger mediaUiEventLogger) {
        Context context3 = context2;
        Executor executor2 = executor;
        DelayableExecutor delayableExecutor2 = delayableExecutor;
        MediaControllerFactory mediaControllerFactory3 = mediaControllerFactory2;
        BroadcastDispatcher broadcastDispatcher3 = broadcastDispatcher2;
        DumpManager dumpManager2 = dumpManager;
        MediaTimeoutListener mediaTimeoutListener2 = mediaTimeoutListener;
        MediaResumeListener mediaResumeListener2 = mediaResumeListener;
        MediaDataFilter mediaDataFilter3 = mediaDataFilter2;
        ActivityStarter activityStarter3 = activityStarter2;
        SmartspaceMediaDataProvider smartspaceMediaDataProvider3 = smartspaceMediaDataProvider2;
        SystemClock systemClock3 = systemClock2;
        TunerService tunerService3 = tunerService2;
        Intrinsics.checkNotNullParameter(context3, "context");
        Intrinsics.checkNotNullParameter(executor2, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(delayableExecutor2, "foregroundExecutor");
        Intrinsics.checkNotNullParameter(mediaControllerFactory3, "mediaControllerFactory");
        Intrinsics.checkNotNullParameter(broadcastDispatcher3, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(dumpManager2, "dumpManager");
        Intrinsics.checkNotNullParameter(mediaTimeoutListener2, "mediaTimeoutListener");
        Intrinsics.checkNotNullParameter(mediaResumeListener2, "mediaResumeListener");
        Intrinsics.checkNotNullParameter(mediaSessionBasedFilter, "mediaSessionBasedFilter");
        Intrinsics.checkNotNullParameter(mediaDeviceManager, "mediaDeviceManager");
        Intrinsics.checkNotNullParameter(mediaDataCombineLatest, "mediaDataCombineLatest");
        Intrinsics.checkNotNullParameter(mediaDataFilter3, "mediaDataFilter");
        Intrinsics.checkNotNullParameter(activityStarter3, "activityStarter");
        Intrinsics.checkNotNullParameter(smartspaceMediaDataProvider3, "smartspaceMediaDataProvider");
        Intrinsics.checkNotNullParameter(systemClock3, "systemClock");
        Intrinsics.checkNotNullParameter(tunerService2, "tunerService");
        MediaFlags mediaFlags3 = mediaFlags2;
        Intrinsics.checkNotNullParameter(mediaFlags3, "mediaFlags");
        MediaUiEventLogger mediaUiEventLogger2 = mediaUiEventLogger;
        Intrinsics.checkNotNullParameter(mediaUiEventLogger2, "logger");
        TunerService tunerService4 = tunerService2;
        this.context = context3;
        this.backgroundExecutor = executor2;
        this.foregroundExecutor = delayableExecutor2;
        this.mediaControllerFactory = mediaControllerFactory3;
        this.broadcastDispatcher = broadcastDispatcher3;
        this.mediaDataFilter = mediaDataFilter3;
        this.activityStarter = activityStarter3;
        this.smartspaceMediaDataProvider = smartspaceMediaDataProvider3;
        this.useMediaResumption = z;
        this.useQsMediaPlayer = z2;
        this.systemClock = systemClock3;
        this.tunerService = tunerService4;
        this.mediaFlags = mediaFlags3;
        this.logger = mediaUiEventLogger2;
        this.themeText = Utils.getColorAttr(context3, 16842806).getDefaultColor();
        this.internalListeners = new LinkedHashSet();
        this.mediaEntries = new LinkedHashMap<>();
        this.smartspaceMediaData = MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA();
        this.allowMediaRecommendations = MediaDataManagerKt.allowMediaRecommendations(context2);
        MediaDataManager$appChangeReceiver$1 mediaDataManager$appChangeReceiver$1 = new MediaDataManager$appChangeReceiver$1(this);
        this.appChangeReceiver = mediaDataManager$appChangeReceiver$1;
        dumpManager2.registerDumpable("MediaDataManager", this);
        addInternalListener(mediaTimeoutListener2);
        addInternalListener(mediaResumeListener2);
        MediaSessionBasedFilter mediaSessionBasedFilter2 = mediaSessionBasedFilter;
        TunerService tunerService5 = tunerService4;
        addInternalListener(mediaSessionBasedFilter2);
        MediaDeviceManager mediaDeviceManager2 = mediaDeviceManager;
        mediaSessionBasedFilter2.addListener(mediaDeviceManager2);
        MediaDataCombineLatest mediaDataCombineLatest2 = mediaDataCombineLatest;
        mediaSessionBasedFilter2.addListener(mediaDataCombineLatest2);
        mediaDeviceManager2.addListener(mediaDataCombineLatest2);
        mediaDataCombineLatest2.addListener(mediaDataFilter3);
        mediaTimeoutListener2.setTimeoutCallback(new Function2<String, Boolean, Unit>(this) {
            final /* synthetic */ MediaDataManager this$0;

            {
                this.this$0 = r1;
            }

            public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
                invoke((String) obj, ((Boolean) obj2).booleanValue());
                return Unit.INSTANCE;
            }

            public final void invoke(String str, boolean z) {
                Intrinsics.checkNotNullParameter(str, "key");
                MediaDataManager.setTimedOut$SystemUI_nothingRelease$default(this.this$0, str, z, false, 4, (Object) null);
            }
        });
        mediaTimeoutListener2.setStateCallback(new Function2<String, PlaybackState, Unit>(this) {
            final /* synthetic */ MediaDataManager this$0;

            {
                this.this$0 = r1;
            }

            public /* bridge */ /* synthetic */ Object invoke(Object obj, Object obj2) {
                invoke((String) obj, (PlaybackState) obj2);
                return Unit.INSTANCE;
            }

            public final void invoke(String str, PlaybackState playbackState) {
                Intrinsics.checkNotNullParameter(str, "key");
                Intrinsics.checkNotNullParameter(playbackState, AuthDialog.KEY_BIOMETRIC_STATE);
                this.this$0.updateState(str, playbackState);
            }
        });
        mediaResumeListener2.setManager(this);
        mediaDataFilter3.setMediaDataManager$SystemUI_nothingRelease(this);
        BroadcastDispatcher.registerReceiver$default(broadcastDispatcher2, mediaDataManager$appChangeReceiver$1, new IntentFilter("android.intent.action.PACKAGES_SUSPENDED"), (Executor) null, UserHandle.ALL, 0, (String) null, 48, (Object) null);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.PACKAGE_REMOVED");
        intentFilter.addAction("android.intent.action.PACKAGE_RESTARTED");
        intentFilter.addDataScheme("package");
        context3.registerReceiver(mediaDataManager$appChangeReceiver$1, intentFilter);
        smartspaceMediaDataProvider3.registerListener(this);
        Object systemService = context3.getSystemService(SmartspaceManager.class);
        Intrinsics.checkNotNullExpressionValue(systemService, "context.getSystemService…spaceManager::class.java)");
        SmartspaceSession createSmartspaceSession = ((SmartspaceManager) systemService).createSmartspaceSession(new SmartspaceConfig.Builder(context3, SMARTSPACE_UI_SURFACE_LABEL).build());
        this.smartspaceSession = createSmartspaceSession;
        if (createSmartspaceSession != null) {
            createSmartspaceSession.addOnTargetsAvailableListener(Executors.newCachedThreadPool(), new MediaDataManager$$ExternalSyntheticLambda13(this));
        }
        SmartspaceSession smartspaceSession2 = this.smartspaceSession;
        if (smartspaceSession2 != null) {
            smartspaceSession2.requestSmartspaceUpdate();
        }
        tunerService5.addTunable(new TunerService.Tunable(this) {
            final /* synthetic */ MediaDataManager this$0;

            {
                this.this$0 = r1;
            }

            public void onTuningChanged(String str, String str2) {
                MediaDataManager mediaDataManager = this.this$0;
                mediaDataManager.allowMediaRecommendations = MediaDataManagerKt.allowMediaRecommendations(mediaDataManager.context);
                if (!this.this$0.allowMediaRecommendations) {
                    MediaDataManager mediaDataManager2 = this.this$0;
                    mediaDataManager2.dismissSmartspaceRecommendation(mediaDataManager2.getSmartspaceMediaData().getTargetId(), 0);
                }
            }
        }, "qs_media_recommend");
    }

    @Metadata(mo65042d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\b\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002R\u0010\u0010\u0003\u001a\u00020\u00048\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\u0005\u001a\u00020\u00068\u0006XD¢\u0006\u0002\n\u0000R\u0010\u0010\u0007\u001a\u00020\u00068\u0006X\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\b\u001a\u00020\u00048\u0006XD¢\u0006\u0002\n\u0000¨\u0006\t"}, mo65043d2 = {"Lcom/android/systemui/media/MediaDataManager$Companion;", "", "()V", "EXTRAS_MEDIA_SOURCE_PACKAGE_NAME", "", "MAX_COMPACT_ACTIONS", "", "MAX_NOTIFICATION_ACTIONS", "SMARTSPACE_UI_SURFACE_LABEL", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: MediaDataManager.kt */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    public final SmartspaceMediaData getSmartspaceMediaData() {
        return this.smartspaceMediaData;
    }

    public final void setSmartspaceMediaData(SmartspaceMediaData smartspaceMediaData2) {
        Intrinsics.checkNotNullParameter(smartspaceMediaData2, "<set-?>");
        this.smartspaceMediaData = smartspaceMediaData2;
    }

    private final boolean isRemoteCastNotification(StatusBarNotification statusBarNotification) {
        return statusBarNotification.getNotification().extras.containsKey("android.mediaRemoteDevice");
    }

    /* JADX INFO: this call moved to the top of the method (can break code semantics) */
    @Inject
    public MediaDataManager(Context context2, @Background Executor executor, @Main DelayableExecutor delayableExecutor, MediaControllerFactory mediaControllerFactory2, DumpManager dumpManager, BroadcastDispatcher broadcastDispatcher2, MediaTimeoutListener mediaTimeoutListener, MediaResumeListener mediaResumeListener, MediaSessionBasedFilter mediaSessionBasedFilter, MediaDeviceManager mediaDeviceManager, MediaDataCombineLatest mediaDataCombineLatest, MediaDataFilter mediaDataFilter2, ActivityStarter activityStarter2, SmartspaceMediaDataProvider smartspaceMediaDataProvider2, SystemClock systemClock2, TunerService tunerService2, MediaFlags mediaFlags2, MediaUiEventLogger mediaUiEventLogger) {
        this(context2, executor, delayableExecutor, mediaControllerFactory2, broadcastDispatcher2, dumpManager, mediaTimeoutListener, mediaResumeListener, mediaSessionBasedFilter, mediaDeviceManager, mediaDataCombineLatest, mediaDataFilter2, activityStarter2, smartspaceMediaDataProvider2, com.android.systemui.util.Utils.useMediaResumption(context2), com.android.systemui.util.Utils.useQsMediaPlayer(context2), systemClock2, tunerService2, mediaFlags2, mediaUiEventLogger);
        Intrinsics.checkNotNullParameter(context2, "context");
        Intrinsics.checkNotNullParameter(executor, "backgroundExecutor");
        Intrinsics.checkNotNullParameter(delayableExecutor, "foregroundExecutor");
        Intrinsics.checkNotNullParameter(mediaControllerFactory2, "mediaControllerFactory");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        Intrinsics.checkNotNullParameter(broadcastDispatcher2, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(mediaTimeoutListener, "mediaTimeoutListener");
        Intrinsics.checkNotNullParameter(mediaResumeListener, "mediaResumeListener");
        Intrinsics.checkNotNullParameter(mediaSessionBasedFilter, "mediaSessionBasedFilter");
        Intrinsics.checkNotNullParameter(mediaDeviceManager, "mediaDeviceManager");
        Intrinsics.checkNotNullParameter(mediaDataCombineLatest, "mediaDataCombineLatest");
        Intrinsics.checkNotNullParameter(mediaDataFilter2, "mediaDataFilter");
        Intrinsics.checkNotNullParameter(activityStarter2, "activityStarter");
        Intrinsics.checkNotNullParameter(smartspaceMediaDataProvider2, "smartspaceMediaDataProvider");
        Intrinsics.checkNotNullParameter(systemClock2, DemoMode.COMMAND_CLOCK);
        Intrinsics.checkNotNullParameter(tunerService2, "tunerService");
        Intrinsics.checkNotNullParameter(mediaFlags2, "mediaFlags");
        Intrinsics.checkNotNullParameter(mediaUiEventLogger, "logger");
    }

    /* access modifiers changed from: private */
    /* renamed from: lambda-2$lambda-1  reason: not valid java name */
    public static final void m2803lambda2$lambda1(MediaDataManager mediaDataManager, List list) {
        Intrinsics.checkNotNullParameter(mediaDataManager, "this$0");
        SmartspaceMediaDataProvider smartspaceMediaDataProvider2 = mediaDataManager.smartspaceMediaDataProvider;
        Intrinsics.checkNotNullExpressionValue(list, "targets");
        smartspaceMediaDataProvider2.onTargetsAvailable(list);
    }

    public final void destroy() {
        this.smartspaceMediaDataProvider.unregisterListener(this);
        this.context.unregisterReceiver(this.appChangeReceiver);
    }

    public final void onNotificationAdded(String str, StatusBarNotification statusBarNotification) {
        String str2 = str;
        StatusBarNotification statusBarNotification2 = statusBarNotification;
        Intrinsics.checkNotNullParameter(str2, "key");
        Intrinsics.checkNotNullParameter(statusBarNotification2, "sbn");
        if (!this.useQsMediaPlayer || !MediaDataManagerKt.isMediaNotification(statusBarNotification)) {
            onNotificationRemoved(str);
            return;
        }
        Assert.isMainThread();
        String packageName = statusBarNotification.getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "sbn.packageName");
        String findExistingEntry = findExistingEntry(str2, packageName);
        boolean z = true;
        if (findExistingEntry == null) {
            InstanceId newInstanceId = this.logger.getNewInstanceId();
            MediaData access$getLOADING$p = MediaDataManagerKt.LOADING;
            String packageName2 = statusBarNotification.getPackageName();
            Intrinsics.checkNotNullExpressionValue(packageName2, "sbn.packageName");
            this.mediaEntries.put(str2, MediaData.copy$default(access$getLOADING$p, 0, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, (List) null, (List) null, (MediaButton) null, packageName2, (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, false, (Runnable) null, 0, false, (String) null, false, (Boolean) null, false, 0, newInstanceId, 0, 25164799, (Object) null));
        } else if (!Intrinsics.areEqual((Object) findExistingEntry, (Object) str2)) {
            MediaData remove = this.mediaEntries.remove(findExistingEntry);
            Intrinsics.checkNotNull(remove);
            this.mediaEntries.put(str2, remove);
        } else {
            z = false;
        }
        loadMediaData(str2, statusBarNotification2, findExistingEntry, z);
    }

    /* access modifiers changed from: private */
    public final void removeAllForPackage(String str) {
        Assert.isMainThread();
        Map linkedHashMap = new LinkedHashMap();
        for (Map.Entry entry : this.mediaEntries.entrySet()) {
            if (Intrinsics.areEqual((Object) ((MediaData) entry.getValue()).getPackageName(), (Object) str)) {
                linkedHashMap.put(entry.getKey(), entry.getValue());
            }
        }
        for (Map.Entry key : linkedHashMap.entrySet()) {
            removeEntry((String) key.getKey());
        }
    }

    public final void setResumeAction(String str, Runnable runnable) {
        Intrinsics.checkNotNullParameter(str, "key");
        MediaData mediaData = this.mediaEntries.get(str);
        if (mediaData != null) {
            mediaData.setResumeAction(runnable);
            mediaData.setHasCheckedForResume(true);
        }
    }

    public final void addResumptionControls(int i, MediaDescription mediaDescription, Runnable runnable, MediaSession.Token token, String str, PendingIntent pendingIntent, String str2) {
        int i2;
        String str3 = str2;
        Intrinsics.checkNotNullParameter(mediaDescription, "desc");
        Intrinsics.checkNotNullParameter(runnable, "action");
        Intrinsics.checkNotNullParameter(token, "token");
        Intrinsics.checkNotNullParameter(str, "appName");
        Intrinsics.checkNotNullParameter(pendingIntent, "appIntent");
        Intrinsics.checkNotNullParameter(str3, FocusChangedMetricHelper.Constants.ExtraKey.PACKAGE_NAME);
        if (!this.mediaEntries.containsKey(str3)) {
            InstanceId newInstanceId = this.logger.getNewInstanceId();
            try {
                ApplicationInfo applicationInfo = this.context.getPackageManager().getApplicationInfo(str3, 0);
                Integer valueOf = applicationInfo != null ? Integer.valueOf(applicationInfo.uid) : null;
                Intrinsics.checkNotNull(valueOf);
                i2 = valueOf.intValue();
            } catch (PackageManager.NameNotFoundException e) {
                Log.w("MediaDataManager", "Could not get app UID for " + str3, e);
                i2 = -1;
            }
            InstanceId instanceId = newInstanceId;
            this.mediaEntries.put(str3, MediaData.copy$default(MediaDataManagerKt.LOADING, 0, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, (List) null, (List) null, (MediaButton) null, str2, (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, false, runnable, 0, false, (String) null, true, (Boolean) null, false, 0, instanceId, i2, 7830527, (Object) null));
            this.logger.logResumeMediaAdded(i2, str3, instanceId);
        }
        this.backgroundExecutor.execute(new MediaDataManager$$ExternalSyntheticLambda3(this, i, mediaDescription, runnable, token, str, pendingIntent, str2));
    }

    /* access modifiers changed from: private */
    /* renamed from: addResumptionControls$lambda-7  reason: not valid java name */
    public static final void m2791addResumptionControls$lambda7(MediaDataManager mediaDataManager, int i, MediaDescription mediaDescription, Runnable runnable, MediaSession.Token token, String str, PendingIntent pendingIntent, String str2) {
        Intrinsics.checkNotNullParameter(mediaDataManager, "this$0");
        Intrinsics.checkNotNullParameter(mediaDescription, "$desc");
        Intrinsics.checkNotNullParameter(runnable, "$action");
        Intrinsics.checkNotNullParameter(token, "$token");
        Intrinsics.checkNotNullParameter(str, "$appName");
        Intrinsics.checkNotNullParameter(pendingIntent, "$appIntent");
        Intrinsics.checkNotNullParameter(str2, "$packageName");
        mediaDataManager.loadMediaDataInBgForResumption(i, mediaDescription, runnable, token, str, pendingIntent, str2);
    }

    private final String findExistingEntry(String str, String str2) {
        if (this.mediaEntries.containsKey(str)) {
            return str;
        }
        if (this.mediaEntries.containsKey(str2)) {
            return str2;
        }
        return null;
    }

    static /* synthetic */ void loadMediaData$default(MediaDataManager mediaDataManager, String str, StatusBarNotification statusBarNotification, String str2, boolean z, int i, Object obj) {
        if ((i & 8) != 0) {
            z = false;
        }
        mediaDataManager.loadMediaData(str, statusBarNotification, str2, z);
    }

    private final void loadMediaData(String str, StatusBarNotification statusBarNotification, String str2, boolean z) {
        this.backgroundExecutor.execute(new MediaDataManager$$ExternalSyntheticLambda5(this, str, statusBarNotification, str2, z));
    }

    /* access modifiers changed from: private */
    /* renamed from: loadMediaData$lambda-8  reason: not valid java name */
    public static final void m2805loadMediaData$lambda8(MediaDataManager mediaDataManager, String str, StatusBarNotification statusBarNotification, String str2, boolean z) {
        Intrinsics.checkNotNullParameter(mediaDataManager, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        Intrinsics.checkNotNullParameter(statusBarNotification, "$sbn");
        mediaDataManager.loadMediaDataInBg(str, statusBarNotification, str2, z);
    }

    public final void addListener(Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.mediaDataFilter.addListener(listener);
    }

    public final void removeListener(Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.mediaDataFilter.removeListener(listener);
    }

    private final boolean addInternalListener(Listener listener) {
        return this.internalListeners.add(listener);
    }

    private final void notifyMediaDataLoaded(String str, String str2, MediaData mediaData) {
        for (Listener onMediaDataLoaded$default : this.internalListeners) {
            Listener.onMediaDataLoaded$default(onMediaDataLoaded$default, str, str2, mediaData, false, 0, false, 56, (Object) null);
        }
    }

    private final void notifySmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData2) {
        for (Listener onSmartspaceMediaDataLoaded$default : this.internalListeners) {
            Listener.onSmartspaceMediaDataLoaded$default(onSmartspaceMediaDataLoaded$default, str, smartspaceMediaData2, false, 4, (Object) null);
        }
    }

    private final void notifyMediaDataRemoved(String str) {
        for (Listener onMediaDataRemoved : this.internalListeners) {
            onMediaDataRemoved.onMediaDataRemoved(str);
        }
    }

    private final void notifySmartspaceMediaDataRemoved(String str, boolean z) {
        for (Listener onSmartspaceMediaDataRemoved : this.internalListeners) {
            onSmartspaceMediaDataRemoved.onSmartspaceMediaDataRemoved(str, z);
        }
    }

    public static /* synthetic */ void setTimedOut$SystemUI_nothingRelease$default(MediaDataManager mediaDataManager, String str, boolean z, boolean z2, int i, Object obj) {
        if ((i & 4) != 0) {
            z2 = false;
        }
        mediaDataManager.setTimedOut$SystemUI_nothingRelease(str, z, z2);
    }

    public final void setTimedOut$SystemUI_nothingRelease(String str, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(str, "key");
        MediaData mediaData = this.mediaEntries.get(str);
        if (mediaData != null) {
            if (z && !z2) {
                this.logger.logMediaTimeout(mediaData.getAppUid(), mediaData.getPackageName(), mediaData.getInstanceId());
            }
            if (mediaData.getActive() != (!z) || z2) {
                mediaData.setActive(!z);
                Log.d("MediaDataManager", "Updating " + str + " timedOut: " + z);
                onMediaDataLoaded(str, str, mediaData);
            } else if (mediaData.getResumption()) {
                Log.d("MediaDataManager", "timing out resume player " + str);
                dismissMediaData(str, 0);
            }
        }
    }

    /* access modifiers changed from: private */
    public final void updateState(String str, PlaybackState playbackState) {
        String str2 = str;
        MediaData mediaData = this.mediaEntries.get(str2);
        if (mediaData == null) {
            return;
        }
        if (mediaData.getToken() == null) {
            Log.d("MediaDataManager", "State updated, but token was null");
            return;
        }
        String packageName = mediaData.getPackageName();
        MediaController create = this.mediaControllerFactory.create(mediaData.getToken());
        Intrinsics.checkNotNullExpressionValue(create, "mediaControllerFactory.create(it.token)");
        MediaData copy$default = MediaData.copy$default(mediaData, 0, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, (List) null, (List) null, createActionsFromState(packageName, create, new UserHandle(mediaData.getUserId())), (String) null, (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, false, (Runnable) null, 0, false, (String) null, false, Boolean.valueOf(NotificationMediaManager.isPlayingState(playbackState.getState())), false, 0, (InstanceId) null, 0, 32505343, (Object) null);
        Log.d("MediaDataManager", "State updated outside of notification");
        onMediaDataLoaded(str2, str2, copy$default);
    }

    private final void removeEntry(String str) {
        MediaData remove = this.mediaEntries.remove(str);
        if (remove != null) {
            this.logger.logMediaRemoved(remove.getAppUid(), remove.getPackageName(), remove.getInstanceId());
        }
        notifyMediaDataRemoved(str);
    }

    public final boolean dismissMediaData(String str, long j) {
        Intrinsics.checkNotNullParameter(str, "key");
        boolean z = this.mediaEntries.get(str) != null;
        this.backgroundExecutor.execute(new MediaDataManager$$ExternalSyntheticLambda6(this, str));
        this.foregroundExecutor.executeDelayed(new MediaDataManager$$ExternalSyntheticLambda7(this, str), j);
        return z;
    }

    /* access modifiers changed from: private */
    /* renamed from: dismissMediaData$lambda-18  reason: not valid java name */
    public static final void m2795dismissMediaData$lambda18(MediaDataManager mediaDataManager, String str) {
        MediaSession.Token token;
        Intrinsics.checkNotNullParameter(mediaDataManager, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        MediaData mediaData = mediaDataManager.mediaEntries.get(str);
        if (mediaData != null && mediaData.isLocalSession() && (token = mediaData.getToken()) != null) {
            mediaDataManager.mediaControllerFactory.create(token).getTransportControls().stop();
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: dismissMediaData$lambda-19  reason: not valid java name */
    public static final void m2796dismissMediaData$lambda19(MediaDataManager mediaDataManager, String str) {
        Intrinsics.checkNotNullParameter(mediaDataManager, "this$0");
        Intrinsics.checkNotNullParameter(str, "$key");
        mediaDataManager.removeEntry(str);
    }

    public final void dismissSmartspaceRecommendation(String str, long j) {
        Intrinsics.checkNotNullParameter(str, "key");
        if (Intrinsics.areEqual((Object) this.smartspaceMediaData.getTargetId(), (Object) str) && this.smartspaceMediaData.isValid()) {
            Log.d("MediaDataManager", "Dismissing Smartspace media target");
            if (this.smartspaceMediaData.isActive()) {
                this.smartspaceMediaData = SmartspaceMediaData.copy$default(MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA(), this.smartspaceMediaData.getTargetId(), false, (String) null, (SmartspaceAction) null, (List) null, (Intent) null, 0, this.smartspaceMediaData.getInstanceId(), 126, (Object) null);
            }
            this.foregroundExecutor.executeDelayed(new MediaDataManager$$ExternalSyntheticLambda0(this), j);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: dismissSmartspaceRecommendation$lambda-20  reason: not valid java name */
    public static final void m2797dismissSmartspaceRecommendation$lambda20(MediaDataManager mediaDataManager) {
        Intrinsics.checkNotNullParameter(mediaDataManager, "this$0");
        mediaDataManager.notifySmartspaceMediaDataRemoved(mediaDataManager.smartspaceMediaData.getTargetId(), true);
    }

    private final void loadMediaDataInBgForResumption(int i, MediaDescription mediaDescription, Runnable runnable, MediaSession.Token token, String str, PendingIntent pendingIntent, String str2) {
        Icon icon;
        InstanceId instanceId;
        String str3 = str2;
        if (TextUtils.isEmpty(mediaDescription.getTitle())) {
            Log.e("MediaDataManager", "Description incomplete");
            this.mediaEntries.remove(str3);
            return;
        }
        Log.d("MediaDataManager", "adding track for " + i + " from browser: " + mediaDescription);
        Bitmap iconBitmap = mediaDescription.getIconBitmap();
        if (iconBitmap == null && mediaDescription.getIconUri() != null) {
            Uri iconUri = mediaDescription.getIconUri();
            Intrinsics.checkNotNull(iconUri);
            iconBitmap = loadBitmapFromUri(iconUri);
        }
        if (iconBitmap != null) {
            icon = Icon.createWithBitmap(iconBitmap);
        } else {
            icon = null;
            Icon icon2 = null;
        }
        Icon icon3 = icon;
        MediaData mediaData = this.mediaEntries.get(str3);
        if (mediaData == null || (instanceId = mediaData.getInstanceId()) == null) {
            instanceId = this.logger.getNewInstanceId();
        }
        InstanceId instanceId2 = instanceId;
        int appUid = mediaData != null ? mediaData.getAppUid() : -1;
        MediaAction resumeMediaAction = getResumeMediaAction(runnable);
        long elapsedRealtime = this.systemClock.elapsedRealtime();
        DelayableExecutor delayableExecutor = this.foregroundExecutor;
        MediaDataManager$$ExternalSyntheticLambda11 mediaDataManager$$ExternalSyntheticLambda11 = r0;
        MediaDataManager$$ExternalSyntheticLambda11 mediaDataManager$$ExternalSyntheticLambda112 = new MediaDataManager$$ExternalSyntheticLambda11(this, str2, i, str, mediaDescription, icon3, resumeMediaAction, token, pendingIntent, runnable, elapsedRealtime, instanceId2, appUid);
        delayableExecutor.execute(mediaDataManager$$ExternalSyntheticLambda11);
    }

    /* access modifiers changed from: private */
    /* renamed from: loadMediaDataInBgForResumption$lambda-21  reason: not valid java name */
    public static final void m2807loadMediaDataInBgForResumption$lambda21(MediaDataManager mediaDataManager, String str, int i, String str2, MediaDescription mediaDescription, Icon icon, MediaAction mediaAction, MediaSession.Token token, PendingIntent pendingIntent, Runnable runnable, long j, InstanceId instanceId, int i2) {
        MediaDataManager mediaDataManager2 = mediaDataManager;
        Intrinsics.checkNotNullParameter(mediaDataManager2, "this$0");
        Intrinsics.checkNotNullParameter(str, "$packageName");
        Intrinsics.checkNotNullParameter(str2, "$appName");
        Intrinsics.checkNotNullParameter(mediaDescription, "$desc");
        Intrinsics.checkNotNullParameter(mediaAction, "$mediaAction");
        Intrinsics.checkNotNullParameter(token, "$token");
        Intrinsics.checkNotNullParameter(pendingIntent, "$appIntent");
        Intrinsics.checkNotNullParameter(runnable, "$resumeAction");
        Intrinsics.checkNotNullParameter(instanceId, "$instanceId");
        CharSequence subtitle = mediaDescription.getSubtitle();
        CharSequence title = mediaDescription.getTitle();
        List listOf = CollectionsKt.listOf(mediaAction);
        MediaData mediaData = r1;
        List listOf2 = CollectionsKt.listOf(0);
        MediaButton mediaButton = r28;
        MediaButton mediaButton2 = new MediaButton(mediaAction, (MediaAction) null, (MediaAction) null, (MediaAction) null, (MediaAction) null, false, false, 126, (DefaultConstructorMarker) null);
        MediaData mediaData2 = new MediaData(i, true, str2, (Icon) null, subtitle, title, icon, listOf, listOf2, mediaButton, str, token, pendingIntent, (MediaDeviceData) null, false, runnable, 0, true, str, true, (Boolean) null, false, j, instanceId, i2, 3211264, (DefaultConstructorMarker) null);
        mediaDataManager2.onMediaDataLoaded(str, (String) null, mediaData);
    }

    public static /* synthetic */ void loadMediaDataInBg$default(MediaDataManager mediaDataManager, String str, StatusBarNotification statusBarNotification, String str2, boolean z, int i, Object obj) {
        if ((i & 8) != 0) {
            z = false;
        }
        mediaDataManager.loadMediaDataInBg(str, statusBarNotification, str2, z);
    }

    /* JADX WARNING: Removed duplicated region for block: B:64:0x01a8  */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x01aa  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x021d  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x022a  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void loadMediaDataInBg(java.lang.String r33, android.service.notification.StatusBarNotification r34, java.lang.String r35, boolean r36) {
        /*
            r32 = this;
            r2 = r32
            r3 = r33
            r5 = r34
            java.lang.String r0 = "key"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r3, r0)
            java.lang.String r0 = "sbn"
            kotlin.jvm.internal.Intrinsics.checkNotNullParameter(r5, r0)
            android.app.Notification r0 = r34.getNotification()
            android.os.Bundle r0 = r0.extras
            java.lang.String r1 = "android.mediaSession"
            java.lang.Class<android.media.session.MediaSession$Token> r4 = android.media.session.MediaSession.Token.class
            java.lang.Object r0 = r0.getParcelable(r1, r4)
            r14 = r0
            android.media.session.MediaSession$Token r14 = (android.media.session.MediaSession.Token) r14
            if (r14 != 0) goto L_0x0025
            return
        L_0x0025:
            com.android.systemui.media.MediaControllerFactory r0 = r2.mediaControllerFactory
            android.media.session.MediaController r0 = r0.create(r14)
            android.media.MediaMetadata r1 = r0.getMetadata()
            android.app.Notification r15 = r34.getNotification()
            java.lang.String r4 = "sbn.notification"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r15, r4)
            r4 = 0
            if (r1 == 0) goto L_0x0041
            android.graphics.Bitmap r6 = r2.loadBitmapFromUri((android.media.MediaMetadata) r1)
            goto L_0x0042
        L_0x0041:
            r6 = r4
        L_0x0042:
            if (r6 != 0) goto L_0x004e
            if (r1 == 0) goto L_0x004d
            java.lang.String r6 = "android.media.metadata.ART"
            android.graphics.Bitmap r6 = r1.getBitmap(r6)
            goto L_0x004e
        L_0x004d:
            r6 = r4
        L_0x004e:
            if (r6 != 0) goto L_0x005a
            if (r1 == 0) goto L_0x0059
            java.lang.String r6 = "android.media.metadata.ALBUM_ART"
            android.graphics.Bitmap r6 = r1.getBitmap(r6)
            goto L_0x005a
        L_0x0059:
            r6 = r4
        L_0x005a:
            if (r6 != 0) goto L_0x0061
            android.graphics.drawable.Icon r6 = r15.getLargeIcon()
            goto L_0x0065
        L_0x0061:
            android.graphics.drawable.Icon r6 = android.graphics.drawable.Icon.createWithBitmap(r6)
        L_0x0065:
            r10 = r6
            android.content.Context r6 = r2.context
            android.app.Notification$Builder r6 = android.app.Notification.Builder.recoverBuilder(r6, r15)
            java.lang.String r6 = r6.loadHeaderAppName()
            android.app.Notification r7 = r34.getNotification()
            android.graphics.drawable.Icon r7 = r7.getSmallIcon()
            kotlin.jvm.internal.Ref$ObjectRef r9 = new kotlin.jvm.internal.Ref$ObjectRef
            r9.<init>()
            if (r1 == 0) goto L_0x0086
            java.lang.String r8 = "android.media.metadata.DISPLAY_TITLE"
            java.lang.String r8 = r1.getString(r8)
            goto L_0x0087
        L_0x0086:
            r8 = r4
        L_0x0087:
            r9.element = r8
            T r8 = r9.element
            if (r8 != 0) goto L_0x0099
            if (r1 == 0) goto L_0x0096
            java.lang.String r8 = "android.media.metadata.TITLE"
            java.lang.String r8 = r1.getString(r8)
            goto L_0x0097
        L_0x0096:
            r8 = r4
        L_0x0097:
            r9.element = r8
        L_0x0099:
            T r8 = r9.element
            if (r8 != 0) goto L_0x00a3
            java.lang.CharSequence r8 = com.android.systemui.statusbar.notification.row.HybridGroupManager.resolveTitle(r15)
            r9.element = r8
        L_0x00a3:
            kotlin.jvm.internal.Ref$ObjectRef r8 = new kotlin.jvm.internal.Ref$ObjectRef
            r8.<init>()
            if (r1 == 0) goto L_0x00b1
            java.lang.String r11 = "android.media.metadata.ARTIST"
            java.lang.String r1 = r1.getString(r11)
            goto L_0x00b2
        L_0x00b1:
            r1 = r4
        L_0x00b2:
            r8.element = r1
            T r1 = r8.element
            if (r1 != 0) goto L_0x00be
            java.lang.CharSequence r1 = com.android.systemui.statusbar.notification.row.HybridGroupManager.resolveText(r15)
            r8.element = r1
        L_0x00be:
            kotlin.jvm.internal.Ref$ObjectRef r13 = new kotlin.jvm.internal.Ref$ObjectRef
            r13.<init>()
            boolean r1 = r2.isRemoteCastNotification(r5)
            java.lang.String r11 = "MediaDataManager"
            if (r1 == 0) goto L_0x0140
            android.app.Notification r1 = r34.getNotification()
            android.os.Bundle r1 = r1.extras
            java.lang.String r12 = "android.mediaRemoteDevice"
            java.lang.CharSequence r12 = r1.getCharSequence(r12, r4)
            java.lang.String r4 = "android.mediaRemoteIcon"
            r28 = r15
            r15 = -1
            int r4 = r1.getInt(r4, r15)
            java.lang.String r15 = "android.mediaRemoteIntent"
            r29 = r14
            java.lang.Class<android.app.PendingIntent> r14 = android.app.PendingIntent.class
            java.lang.Object r1 = r1.getParcelable(r15, r14)
            r23 = r1
            android.app.PendingIntent r23 = (android.app.PendingIntent) r23
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r3)
            java.lang.String r14 = " is RCN for "
            java.lang.StringBuilder r1 = r1.append((java.lang.String) r14)
            java.lang.StringBuilder r1 = r1.append((java.lang.Object) r12)
            java.lang.String r1 = r1.toString()
            android.util.Log.d(r11, r1)
            r1 = -1
            if (r12 == 0) goto L_0x0145
            if (r4 <= r1) goto L_0x0145
            if (r23 == 0) goto L_0x0118
            boolean r14 = r23.isActivity()
            if (r14 == 0) goto L_0x0118
            r20 = 1
            goto L_0x011a
        L_0x0118:
            r20 = 0
        L_0x011a:
            java.lang.String r14 = r34.getPackageName()
            android.graphics.drawable.Icon r4 = android.graphics.drawable.Icon.createWithResource(r14, r4)
            android.content.Context r14 = r2.context
            android.content.Context r14 = r5.getPackageContext(r14)
            android.graphics.drawable.Drawable r21 = r4.loadDrawable(r14)
            com.android.systemui.media.MediaDeviceData r4 = new com.android.systemui.media.MediaDeviceData
            r24 = 0
            r25 = 0
            r26 = 16
            r27 = 0
            r19 = r4
            r22 = r12
            r19.<init>(r20, r21, r22, r23, r24, r25, r26, r27)
            r13.element = r4
            goto L_0x0145
        L_0x0140:
            r29 = r14
            r28 = r15
            r1 = -1
        L_0x0145:
            kotlin.jvm.internal.Ref$ObjectRef r12 = new kotlin.jvm.internal.Ref$ObjectRef
            r12.<init>()
            java.util.List r4 = kotlin.collections.CollectionsKt.emptyList()
            r12.element = r4
            kotlin.jvm.internal.Ref$ObjectRef r14 = new kotlin.jvm.internal.Ref$ObjectRef
            r14.<init>()
            java.util.List r4 = kotlin.collections.CollectionsKt.emptyList()
            r14.element = r4
            java.lang.String r4 = r34.getPackageName()
            java.lang.String r15 = "sbn.packageName"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r4, r15)
            java.lang.String r1 = "mediaController"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r0, r1)
            android.os.UserHandle r1 = r34.getUser()
            r21 = r13
            java.lang.String r13 = "sbn.user"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r1, r13)
            com.android.systemui.media.MediaButton r13 = r2.createActionsFromState(r4, r0, r1)
            if (r13 != 0) goto L_0x018c
            kotlin.Pair r1 = r2.createActionsFromNotification(r5)
            java.lang.Object r4 = r1.getFirst()
            r12.element = r4
            java.lang.Object r1 = r1.getSecond()
            r14.element = r1
        L_0x018c:
            boolean r1 = r2.isRemoteCastNotification(r5)
            if (r1 == 0) goto L_0x0195
            r1 = 2
            r4 = 1
            goto L_0x01ab
        L_0x0195:
            android.media.session.MediaController$PlaybackInfo r1 = r0.getPlaybackInfo()
            if (r1 == 0) goto L_0x01a4
            int r1 = r1.getPlaybackType()
            r4 = 1
            if (r1 != r4) goto L_0x01a5
            r1 = r4
            goto L_0x01a6
        L_0x01a4:
            r4 = 1
        L_0x01a5:
            r1 = 0
        L_0x01a6:
            if (r1 == 0) goto L_0x01aa
            r1 = 0
            goto L_0x01ab
        L_0x01aa:
            r1 = r4
        L_0x01ab:
            android.media.session.PlaybackState r0 = r0.getPlaybackState()
            if (r0 == 0) goto L_0x01c0
            int r0 = r0.getState()
            boolean r0 = com.android.systemui.statusbar.NotificationMediaManager.isPlayingState(r0)
            java.lang.Boolean r0 = java.lang.Boolean.valueOf((boolean) r0)
            r22 = r0
            goto L_0x01c2
        L_0x01c0:
            r22 = 0
        L_0x01c2:
            java.util.LinkedHashMap<java.lang.String, com.android.systemui.media.MediaData> r0 = r2.mediaEntries
            java.lang.Object r0 = r0.get(r3)
            r17 = r0
            com.android.systemui.media.MediaData r17 = (com.android.systemui.media.MediaData) r17
            if (r17 == 0) goto L_0x01d4
            com.android.internal.logging.InstanceId r0 = r17.getInstanceId()
            if (r0 != 0) goto L_0x01da
        L_0x01d4:
            com.android.systemui.media.MediaUiEventLogger r0 = r2.logger
            com.android.internal.logging.InstanceId r0 = r0.getNewInstanceId()
        L_0x01da:
            r23 = r13
            r13 = r0
            android.content.Context r0 = r2.context     // Catch:{ NameNotFoundException -> 0x0200 }
            android.content.pm.PackageManager r0 = r0.getPackageManager()     // Catch:{ NameNotFoundException -> 0x0200 }
            java.lang.String r4 = r34.getPackageName()     // Catch:{ NameNotFoundException -> 0x0200 }
            r3 = 0
            android.content.pm.ApplicationInfo r0 = r0.getApplicationInfo(r4, r3)     // Catch:{ NameNotFoundException -> 0x01fe }
            if (r0 == 0) goto L_0x01f5
            int r0 = r0.uid     // Catch:{ NameNotFoundException -> 0x01fe }
            java.lang.Integer r4 = java.lang.Integer.valueOf((int) r0)     // Catch:{ NameNotFoundException -> 0x01fe }
            goto L_0x01f6
        L_0x01f5:
            r4 = 0
        L_0x01f6:
            kotlin.jvm.internal.Intrinsics.checkNotNull(r4)     // Catch:{ NameNotFoundException -> 0x01fe }
            int r0 = r4.intValue()     // Catch:{ NameNotFoundException -> 0x01fe }
            goto L_0x021b
        L_0x01fe:
            r0 = move-exception
            goto L_0x0202
        L_0x0200:
            r0 = move-exception
            r3 = 0
        L_0x0202:
            java.lang.StringBuilder r4 = new java.lang.StringBuilder
            java.lang.String r3 = "Could not get app UID for "
            r4.<init>((java.lang.String) r3)
            java.lang.String r3 = r34.getPackageName()
            java.lang.StringBuilder r3 = r4.append((java.lang.String) r3)
            java.lang.String r3 = r3.toString()
            java.lang.Throwable r0 = (java.lang.Throwable) r0
            android.util.Log.w(r11, r3, r0)
            r0 = -1
        L_0x021b:
            if (r36 == 0) goto L_0x022a
            com.android.systemui.media.MediaUiEventLogger r3 = r2.logger
            java.lang.String r4 = r34.getPackageName()
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r4, r15)
            r3.logActiveMediaAdded(r0, r4, r13, r1)
            goto L_0x0245
        L_0x022a:
            if (r17 == 0) goto L_0x0235
            int r3 = r17.getPlaybackLocation()
            if (r1 != r3) goto L_0x0235
            r18 = 1
            goto L_0x0237
        L_0x0235:
            r18 = 0
        L_0x0237:
            if (r18 != 0) goto L_0x0245
            com.android.systemui.media.MediaUiEventLogger r3 = r2.logger
            java.lang.String r4 = r34.getPackageName()
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r4, r15)
            r3.logPlaybackLocationChange(r0, r4, r13, r1)
        L_0x0245:
            com.android.systemui.util.time.SystemClock r3 = r2.systemClock
            long r19 = r3.elapsedRealtime()
            com.android.systemui.util.concurrency.DelayableExecutor r15 = r2.foregroundExecutor
            com.android.systemui.media.MediaDataManager$$ExternalSyntheticLambda10 r11 = new com.android.systemui.media.MediaDataManager$$ExternalSyntheticLambda10
            r18 = r1
            r1 = r11
            r2 = r32
            r3 = r33
            r4 = r35
            r5 = r34
            r30 = r11
            r11 = r12
            r12 = r14
            r16 = r21
            r21 = r13
            r13 = r23
            r14 = r29
            r31 = r15
            r15 = r28
            r17 = r18
            r18 = r22
            r22 = r0
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r21, r22)
            r1 = r30
            r0 = r31
            r0.execute(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaDataManager.loadMediaDataInBg(java.lang.String, android.service.notification.StatusBarNotification, java.lang.String, boolean):void");
    }

    /* access modifiers changed from: private */
    /* renamed from: loadMediaDataInBg$lambda-24  reason: not valid java name */
    public static final void m2806loadMediaDataInBg$lambda24(MediaDataManager mediaDataManager, String str, String str2, StatusBarNotification statusBarNotification, String str3, Icon icon, Ref.ObjectRef objectRef, Ref.ObjectRef objectRef2, Icon icon2, Ref.ObjectRef objectRef3, Ref.ObjectRef objectRef4, MediaButton mediaButton, MediaSession.Token token, Notification notification, Ref.ObjectRef objectRef5, int i, Boolean bool, long j, InstanceId instanceId, int i2) {
        MediaDataManager mediaDataManager2 = mediaDataManager;
        String str4 = str;
        Ref.ObjectRef objectRef6 = objectRef;
        Ref.ObjectRef objectRef7 = objectRef2;
        Ref.ObjectRef objectRef8 = objectRef3;
        Ref.ObjectRef objectRef9 = objectRef4;
        Notification notification2 = notification;
        Ref.ObjectRef objectRef10 = objectRef5;
        Intrinsics.checkNotNullParameter(mediaDataManager2, "this$0");
        Intrinsics.checkNotNullParameter(str4, "$key");
        Intrinsics.checkNotNullParameter(statusBarNotification, "$sbn");
        Intrinsics.checkNotNullParameter(objectRef6, "$artist");
        Intrinsics.checkNotNullParameter(objectRef7, "$song");
        Intrinsics.checkNotNullParameter(objectRef8, "$actionIcons");
        Intrinsics.checkNotNullParameter(objectRef9, "$actionsToShowCollapsed");
        Intrinsics.checkNotNullParameter(notification2, "$notif");
        Intrinsics.checkNotNullParameter(objectRef10, "$device");
        Intrinsics.checkNotNullParameter(instanceId, "$instanceId");
        MediaData mediaData = mediaDataManager2.mediaEntries.get(str4);
        Runnable resumeAction = mediaData != null ? mediaData.getResumeAction() : null;
        MediaData mediaData2 = mediaDataManager2.mediaEntries.get(str4);
        boolean z = mediaData2 != null && mediaData2.getHasCheckedForResume();
        MediaData mediaData3 = mediaDataManager2.mediaEntries.get(str4);
        boolean active = mediaData3 != null ? mediaData3.getActive() : true;
        String packageName = statusBarNotification.getPackageName();
        Intrinsics.checkNotNullExpressionValue(packageName, "sbn.packageName");
        MediaData mediaData4 = r1;
        MediaData mediaData5 = new MediaData(statusBarNotification.getNormalizedUserId(), true, str3, icon, (CharSequence) objectRef6.element, (CharSequence) objectRef7.element, icon2, (List) objectRef8.element, (List) objectRef9.element, mediaButton, packageName, token, notification2.contentIntent, (MediaDeviceData) objectRef10.element, active, resumeAction, i, false, str, z, bool, statusBarNotification.isClearable(), j, instanceId, i2, 131072, (DefaultConstructorMarker) null);
        mediaDataManager2.onMediaDataLoaded(str, str2, mediaData4);
    }

    private final Pair<List<MediaAction>, List<Integer>> createActionsFromNotification(StatusBarNotification statusBarNotification) {
        List<Integer> list;
        MediaDataManager$$ExternalSyntheticLambda14 mediaDataManager$$ExternalSyntheticLambda14;
        Icon icon;
        Notification notification = statusBarNotification.getNotification();
        List arrayList = new ArrayList();
        Notification.Action[] actionArr = notification.actions;
        int[] intArray = notification.extras.getIntArray(NotificationCompat.EXTRA_COMPACT_ACTIONS);
        if (intArray == null || (list = ArraysKt.toMutableList(intArray)) == null) {
            list = new ArrayList<>();
        }
        int size = list.size();
        int i = MAX_COMPACT_ACTIONS;
        if (size > i) {
            Log.e("MediaDataManager", "Too many compact actions for " + statusBarNotification.getKey() + ",limiting to first " + i);
            list = list.subList(0, i);
        }
        if (actionArr != null) {
            int length = actionArr.length;
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    break;
                }
                Notification.Action action = actionArr[i2];
                int i3 = MAX_NOTIFICATION_ACTIONS;
                if (i2 == i3) {
                    Log.w("MediaDataManager", "Too many notification actions for " + statusBarNotification.getKey() + ", limiting to first " + i3);
                    break;
                }
                if (action.getIcon() == null) {
                    Log.i("MediaDataManager", "No icon for action " + i2 + ' ' + action.title);
                    list.remove((Object) Integer.valueOf(i2));
                } else {
                    if (action.actionIntent != null) {
                        mediaDataManager$$ExternalSyntheticLambda14 = new MediaDataManager$$ExternalSyntheticLambda14(action, this);
                    } else {
                        mediaDataManager$$ExternalSyntheticLambda14 = null;
                        Runnable runnable = null;
                    }
                    MediaDataManager$$ExternalSyntheticLambda14 mediaDataManager$$ExternalSyntheticLambda142 = mediaDataManager$$ExternalSyntheticLambda14;
                    Icon icon2 = action.getIcon();
                    if (icon2 != null && icon2.getType() == 2) {
                        String packageName = statusBarNotification.getPackageName();
                        Icon icon3 = action.getIcon();
                        Intrinsics.checkNotNull(icon3);
                        icon = Icon.createWithResource(packageName, icon3.getResId());
                    } else {
                        icon = action.getIcon();
                    }
                    arrayList.add(new MediaAction(icon.setTint(this.themeText).loadDrawable(this.context), mediaDataManager$$ExternalSyntheticLambda142, action.title, (Drawable) null, (Integer) null, 16, (DefaultConstructorMarker) null));
                }
                i2++;
            }
        }
        return new Pair<>(arrayList, list);
    }

    /* access modifiers changed from: private */
    /* renamed from: createActionsFromNotification$lambda-27  reason: not valid java name */
    public static final void m2792createActionsFromNotification$lambda27(Notification.Action action, MediaDataManager mediaDataManager) {
        Intrinsics.checkNotNullParameter(mediaDataManager, "this$0");
        if (action.actionIntent.isActivity()) {
            mediaDataManager.activityStarter.startPendingIntentDismissingKeyguard(action.actionIntent);
        } else if (action.isAuthenticationRequired()) {
            mediaDataManager.activityStarter.dismissKeyguardThenExecute(new MediaDataManager$$ExternalSyntheticLambda8(mediaDataManager, action), new MediaDataManager$$ExternalSyntheticLambda9(), true);
        } else {
            PendingIntent pendingIntent = action.actionIntent;
            Intrinsics.checkNotNullExpressionValue(pendingIntent, "action.actionIntent");
            mediaDataManager.sendPendingIntent(pendingIntent);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: createActionsFromNotification$lambda-27$lambda-25  reason: not valid java name */
    public static final boolean m2793createActionsFromNotification$lambda27$lambda25(MediaDataManager mediaDataManager, Notification.Action action) {
        Intrinsics.checkNotNullParameter(mediaDataManager, "this$0");
        PendingIntent pendingIntent = action.actionIntent;
        Intrinsics.checkNotNullExpressionValue(pendingIntent, "action.actionIntent");
        return mediaDataManager.sendPendingIntent(pendingIntent);
    }

    private final MediaButton createActionsFromState(String str, MediaController mediaController, UserHandle userHandle) {
        MediaAction mediaAction;
        MediaAction mediaAction2;
        MediaAction mediaAction3;
        MediaAction mediaAction4;
        PlaybackState playbackState = mediaController.getPlaybackState();
        MediaAction mediaAction5 = null;
        if (playbackState == null || !this.mediaFlags.areMediaSessionActionsEnabled(str, userHandle)) {
            return null;
        }
        if (NotificationMediaManager.isConnectingState(playbackState.getState())) {
            Drawable drawable = this.context.getDrawable(17303371);
            if (drawable != null) {
                ((Animatable) drawable).start();
                mediaAction = new MediaAction(drawable, (Runnable) null, this.context.getString(C1894R.string.controls_media_button_connecting), this.context.getDrawable(C1894R.C1896drawable.ic_media_connecting_container), 17303371);
            } else {
                throw new NullPointerException("null cannot be cast to non-null type android.graphics.drawable.Animatable");
            }
        } else {
            if (NotificationMediaManager.isPlayingState(playbackState.getState())) {
                mediaAction4 = getStandardAction(mediaController, playbackState.getActions(), 2);
            } else {
                mediaAction4 = getStandardAction(mediaController, playbackState.getActions(), 4);
            }
            mediaAction = mediaAction4;
        }
        MediaController mediaController2 = mediaController;
        MediaAction standardAction = getStandardAction(mediaController2, playbackState.getActions(), 16);
        MediaAction standardAction2 = getStandardAction(mediaController2, playbackState.getActions(), 32);
        List<PlaybackState.CustomAction> customActions = playbackState.getCustomActions();
        Intrinsics.checkNotNullExpressionValue(customActions, "state.customActions");
        Iterator it = SequencesKt.map(SequencesKt.filterNotNull(CollectionsKt.asSequence(customActions)), new MediaDataManager$createActionsFromState$customActions$1(this, playbackState, str, mediaController)).iterator();
        Bundle extras = mediaController.getExtras();
        boolean z = extras != null && extras.getBoolean(MediaConstants.SESSION_EXTRAS_KEY_SLOT_RESERVATION_SKIP_TO_PREV);
        Bundle extras2 = mediaController.getExtras();
        boolean z2 = extras2 != null && extras2.getBoolean(MediaConstants.SESSION_EXTRAS_KEY_SLOT_RESERVATION_SKIP_TO_NEXT);
        if (standardAction != null) {
            mediaAction2 = standardAction;
        } else if (!z) {
            mediaAction2 = createActionsFromState$nextCustomAction(it);
        } else {
            MediaAction mediaAction6 = null;
            mediaAction2 = null;
        }
        if (standardAction2 != null) {
            mediaAction3 = standardAction2;
        } else {
            if (!z2) {
                mediaAction5 = createActionsFromState$nextCustomAction(it);
            } else {
                MediaAction mediaAction7 = null;
            }
            mediaAction3 = mediaAction5;
        }
        return new MediaButton(mediaAction, mediaAction3, mediaAction2, createActionsFromState$nextCustomAction(it), createActionsFromState$nextCustomAction(it), z2, z);
    }

    private static final MediaAction createActionsFromState$nextCustomAction(Iterator<MediaAction> it) {
        if (it.hasNext()) {
            return it.next();
        }
        return null;
    }

    private final MediaAction getStandardAction(MediaController mediaController, long j, long j2) {
        if (!includesAction(j, j2)) {
            return null;
        }
        if (j2 == 4) {
            return new MediaAction(this.context.getDrawable(C1894R.C1896drawable.ic_media_play), new MediaDataManager$$ExternalSyntheticLambda15(mediaController), this.context.getString(C1894R.string.controls_media_button_play), this.context.getDrawable(C1894R.C1896drawable.ic_media_play_container), (Integer) null, 16, (DefaultConstructorMarker) null);
        }
        if (j2 == 2) {
            return new MediaAction(this.context.getDrawable(C1894R.C1896drawable.ic_media_pause), new MediaDataManager$$ExternalSyntheticLambda16(mediaController), this.context.getString(C1894R.string.controls_media_button_pause), this.context.getDrawable(C1894R.C1896drawable.ic_media_pause_container), (Integer) null, 16, (DefaultConstructorMarker) null);
        }
        if (j2 == 16) {
            return new MediaAction(this.context.getDrawable(C1894R.C1896drawable.ic_media_prev), new MediaDataManager$$ExternalSyntheticLambda1(mediaController), this.context.getString(C1894R.string.controls_media_button_prev), (Drawable) null, (Integer) null, 16, (DefaultConstructorMarker) null);
        }
        if (j2 == 32) {
            return new MediaAction(this.context.getDrawable(C1894R.C1896drawable.ic_media_next), new MediaDataManager$$ExternalSyntheticLambda2(mediaController), this.context.getString(C1894R.string.controls_media_button_next), (Drawable) null, (Integer) null, 16, (DefaultConstructorMarker) null);
        }
        return null;
    }

    /* access modifiers changed from: private */
    /* renamed from: getStandardAction$lambda-28  reason: not valid java name */
    public static final void m2799getStandardAction$lambda28(MediaController mediaController) {
        Intrinsics.checkNotNullParameter(mediaController, "$controller");
        mediaController.getTransportControls().play();
    }

    /* access modifiers changed from: private */
    /* renamed from: getStandardAction$lambda-29  reason: not valid java name */
    public static final void m2800getStandardAction$lambda29(MediaController mediaController) {
        Intrinsics.checkNotNullParameter(mediaController, "$controller");
        mediaController.getTransportControls().pause();
    }

    /* access modifiers changed from: private */
    /* renamed from: getStandardAction$lambda-30  reason: not valid java name */
    public static final void m2801getStandardAction$lambda30(MediaController mediaController) {
        Intrinsics.checkNotNullParameter(mediaController, "$controller");
        mediaController.getTransportControls().skipToPrevious();
    }

    /* access modifiers changed from: private */
    /* renamed from: getStandardAction$lambda-31  reason: not valid java name */
    public static final void m2802getStandardAction$lambda31(MediaController mediaController) {
        Intrinsics.checkNotNullParameter(mediaController, "$controller");
        mediaController.getTransportControls().skipToNext();
    }

    /* access modifiers changed from: private */
    public final MediaAction getCustomAction(PlaybackState playbackState, String str, MediaController mediaController, PlaybackState.CustomAction customAction) {
        return new MediaAction(Icon.createWithResource(str, customAction.getIcon()).loadDrawable(this.context), new MediaDataManager$$ExternalSyntheticLambda4(mediaController, customAction), customAction.getName(), (Drawable) null, (Integer) null, 16, (DefaultConstructorMarker) null);
    }

    /* access modifiers changed from: private */
    /* renamed from: getCustomAction$lambda-32  reason: not valid java name */
    public static final void m2798getCustomAction$lambda32(MediaController mediaController, PlaybackState.CustomAction customAction) {
        Intrinsics.checkNotNullParameter(mediaController, "$controller");
        Intrinsics.checkNotNullParameter(customAction, "$customAction");
        mediaController.getTransportControls().sendCustomAction(customAction, customAction.getExtras());
    }

    private final Bitmap loadBitmapFromUri(MediaMetadata mediaMetadata) {
        for (String str : MediaDataManagerKt.ART_URIS) {
            String string = mediaMetadata.getString(str);
            if (!TextUtils.isEmpty(string)) {
                Uri parse = Uri.parse(string);
                Intrinsics.checkNotNullExpressionValue(parse, "parse(uriString)");
                Bitmap loadBitmapFromUri = loadBitmapFromUri(parse);
                if (loadBitmapFromUri != null) {
                    Log.d("MediaDataManager", "loaded art from " + str);
                    return loadBitmapFromUri;
                }
            }
        }
        return null;
    }

    private final boolean sendPendingIntent(PendingIntent pendingIntent) {
        try {
            pendingIntent.send();
            return true;
        } catch (PendingIntent.CanceledException e) {
            Log.d("MediaDataManager", "Intent canceled", e);
            return false;
        }
    }

    private final Bitmap loadBitmapFromUri(Uri uri) {
        if (uri.getScheme() == null) {
            return null;
        }
        if (!uri.getScheme().equals("content") && !uri.getScheme().equals("android.resource") && !uri.getScheme().equals("file")) {
            return null;
        }
        try {
            return ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.context.getContentResolver(), uri), new MediaDataManager$$ExternalSyntheticLambda12());
        } catch (IOException e) {
            Log.e("MediaDataManager", "Unable to load bitmap", e);
            Bitmap bitmap = null;
            return null;
        } catch (RuntimeException e2) {
            Log.e("MediaDataManager", "Unable to load bitmap", e2);
            Bitmap bitmap2 = null;
            return null;
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: loadBitmapFromUri$lambda-33  reason: not valid java name */
    public static final void m2804loadBitmapFromUri$lambda33(ImageDecoder imageDecoder, ImageDecoder.ImageInfo imageInfo, ImageDecoder.Source source) {
        imageDecoder.setAllocator(1);
    }

    private final MediaAction getResumeMediaAction(Runnable runnable) {
        return new MediaAction(Icon.createWithResource(this.context, C1894R.C1896drawable.ic_media_play).setTint(this.themeText).loadDrawable(this.context), runnable, this.context.getString(C1894R.string.controls_media_resume), this.context.getDrawable(C1894R.C1896drawable.ic_media_play_container), (Integer) null, 16, (DefaultConstructorMarker) null);
    }

    public void onSmartspaceTargetsUpdated(List<? extends Parcelable> list) {
        List<? extends Parcelable> list2 = list;
        Intrinsics.checkNotNullParameter(list2, "targets");
        if (!this.allowMediaRecommendations) {
            Log.d("MediaDataManager", "Smartspace recommendation is disabled in Settings.");
            return;
        }
        Collection arrayList = new ArrayList();
        for (Object next : list2) {
            if (next instanceof SmartspaceTarget) {
                arrayList.add(next);
            }
        }
        List list3 = (List) arrayList;
        int size = list3.size();
        if (size != 0) {
            if (size != 1) {
                Log.wtf("MediaDataManager", "More than 1 Smartspace Media Update. Resetting the status...");
                notifySmartspaceMediaDataRemoved(this.smartspaceMediaData.getTargetId(), false);
                this.smartspaceMediaData = MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA();
                return;
            }
            SmartspaceTarget smartspaceTarget = (SmartspaceTarget) list3.get(0);
            if (!Intrinsics.areEqual((Object) this.smartspaceMediaData.getTargetId(), (Object) smartspaceTarget.getSmartspaceTargetId())) {
                Log.d("MediaDataManager", "Forwarding Smartspace media update.");
                SmartspaceMediaData smartspaceMediaData2 = toSmartspaceMediaData(smartspaceTarget, true);
                this.smartspaceMediaData = smartspaceMediaData2;
                notifySmartspaceMediaDataLoaded(smartspaceMediaData2.getTargetId(), this.smartspaceMediaData);
            }
        } else if (this.smartspaceMediaData.isActive()) {
            Log.d("MediaDataManager", "Set Smartspace media to be inactive for the data update");
            SmartspaceMediaData copy$default = SmartspaceMediaData.copy$default(MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA(), this.smartspaceMediaData.getTargetId(), false, (String) null, (SmartspaceAction) null, (List) null, (Intent) null, 0, this.smartspaceMediaData.getInstanceId(), 126, (Object) null);
            this.smartspaceMediaData = copy$default;
            notifySmartspaceMediaDataRemoved(copy$default.getTargetId(), false);
        }
    }

    public final void onNotificationRemoved(String str) {
        String str2 = str;
        Intrinsics.checkNotNullParameter(str2, "key");
        Assert.isMainThread();
        MediaData remove = this.mediaEntries.remove(str2);
        if (this.useMediaResumption) {
            Boolean bool = null;
            if ((remove != null ? remove.getResumeAction() : null) != null) {
                if (remove != null) {
                    bool = Boolean.valueOf(remove.isLocalSession());
                }
                if (bool.booleanValue()) {
                    Log.d("MediaDataManager", "Not removing " + str2 + " because resumable");
                    Runnable resumeAction = remove.getResumeAction();
                    Intrinsics.checkNotNull(resumeAction);
                    MediaAction resumeMediaAction = getResumeMediaAction(resumeAction);
                    List listOf = CollectionsKt.listOf(resumeMediaAction);
                    MediaButton mediaButton = r14;
                    MediaButton mediaButton2 = new MediaButton(resumeMediaAction, (MediaAction) null, (MediaAction) null, (MediaAction) null, (MediaAction) null, false, false, 126, (DefaultConstructorMarker) null);
                    boolean z = false;
                    MediaData copy$default = MediaData.copy$default(remove, 0, false, (String) null, (Icon) null, (CharSequence) null, (CharSequence) null, (Icon) null, listOf, CollectionsKt.listOf(0), mediaButton, (String) null, (MediaSession.Token) null, (PendingIntent) null, (MediaDeviceData) null, false, (Runnable) null, 0, true, (String) null, false, false, true, 0, (InstanceId) null, 0, 30258303, (Object) null);
                    String packageName = remove.getPackageName();
                    if (this.mediaEntries.put(packageName, copy$default) == null) {
                        z = true;
                    }
                    if (z) {
                        notifyMediaDataLoaded(packageName, str2, copy$default);
                    } else {
                        notifyMediaDataRemoved(str);
                        notifyMediaDataLoaded(packageName, packageName, copy$default);
                    }
                    this.logger.logActiveConvertedToResume(copy$default.getAppUid(), packageName, copy$default.getInstanceId());
                    return;
                }
            }
        }
        if (remove != null) {
            notifyMediaDataRemoved(str);
            this.logger.logMediaRemoved(remove.getAppUid(), remove.getPackageName(), remove.getInstanceId());
        }
    }

    public final void setMediaResumptionEnabled(boolean z) {
        if (this.useMediaResumption != z) {
            this.useMediaResumption = z;
            if (!z) {
                Map linkedHashMap = new LinkedHashMap();
                for (Map.Entry entry : this.mediaEntries.entrySet()) {
                    if (!((MediaData) entry.getValue()).getActive()) {
                        linkedHashMap.put(entry.getKey(), entry.getValue());
                    }
                }
                for (Map.Entry entry2 : linkedHashMap.entrySet()) {
                    this.mediaEntries.remove(entry2.getKey());
                    notifyMediaDataRemoved((String) entry2.getKey());
                    this.logger.logMediaRemoved(((MediaData) entry2.getValue()).getAppUid(), ((MediaData) entry2.getValue()).getPackageName(), ((MediaData) entry2.getValue()).getInstanceId());
                }
            }
        }
    }

    public final void onSwipeToDismiss() {
        this.mediaDataFilter.onSwipeToDismiss();
    }

    public final boolean hasActiveMediaOrRecommendation() {
        return this.mediaDataFilter.hasActiveMediaOrRecommendation();
    }

    public final boolean hasAnyMediaOrRecommendation() {
        return this.mediaDataFilter.hasAnyMediaOrRecommendation();
    }

    public final boolean hasActiveMedia() {
        return this.mediaDataFilter.hasActiveMedia();
    }

    public final boolean hasAnyMedia() {
        return this.mediaDataFilter.hasAnyMedia();
    }

    @Metadata(mo65042d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001J@\u0010\u0002\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\u0010\u0006\u001a\u0004\u0018\u00010\u00052\u0006\u0010\u0007\u001a\u00020\b2\b\b\u0002\u0010\t\u001a\u00020\n2\b\b\u0002\u0010\u000b\u001a\u00020\f2\b\b\u0002\u0010\r\u001a\u00020\nH\u0016J\u0010\u0010\u000e\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u0005H\u0016J\"\u0010\u000f\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\u0006\u0010\u0007\u001a\u00020\u00102\b\b\u0002\u0010\u0011\u001a\u00020\nH\u0016J\u001a\u0010\u0012\u001a\u00020\u00032\u0006\u0010\u0004\u001a\u00020\u00052\b\b\u0002\u0010\t\u001a\u00020\nH\u0016ø\u0001\u0000\u0002\u0006\n\u0004\b!0\u0001¨\u0006\u0013À\u0006\u0001"}, mo65043d2 = {"Lcom/android/systemui/media/MediaDataManager$Listener;", "", "onMediaDataLoaded", "", "key", "", "oldKey", "data", "Lcom/android/systemui/media/MediaData;", "immediately", "", "receivedSmartspaceCardLatency", "", "isSsReactivated", "onMediaDataRemoved", "onSmartspaceMediaDataLoaded", "Lcom/android/systemui/media/SmartspaceMediaData;", "shouldPrioritize", "onSmartspaceMediaDataRemoved", "SystemUI_nothingRelease"}, mo65044k = 1, mo65045mv = {1, 6, 0}, mo65047xi = 48)
    /* compiled from: MediaDataManager.kt */
    public interface Listener {
        void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, int i, boolean z2) {
            Intrinsics.checkNotNullParameter(str, "key");
            Intrinsics.checkNotNullParameter(mediaData, "data");
        }

        void onMediaDataRemoved(String str) {
            Intrinsics.checkNotNullParameter(str, "key");
        }

        void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
            Intrinsics.checkNotNullParameter(str, "key");
            Intrinsics.checkNotNullParameter(smartspaceMediaData, "data");
        }

        void onSmartspaceMediaDataRemoved(String str, boolean z) {
            Intrinsics.checkNotNullParameter(str, "key");
        }

        static /* synthetic */ void onMediaDataLoaded$default(Listener listener, String str, String str2, MediaData mediaData, boolean z, int i, boolean z2, int i2, Object obj) {
            if (obj == null) {
                if ((i2 & 8) != 0) {
                    z = true;
                }
                listener.onMediaDataLoaded(str, str2, mediaData, z, (i2 & 16) != 0 ? 0 : i, (i2 & 32) != 0 ? false : z2);
                return;
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: onMediaDataLoaded");
        }

        static /* synthetic */ void onSmartspaceMediaDataLoaded$default(Listener listener, String str, SmartspaceMediaData smartspaceMediaData, boolean z, int i, Object obj) {
            if (obj == null) {
                if ((i & 4) != 0) {
                    z = false;
                }
                listener.onSmartspaceMediaDataLoaded(str, smartspaceMediaData, z);
                return;
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: onSmartspaceMediaDataLoaded");
        }

        static /* synthetic */ void onSmartspaceMediaDataRemoved$default(Listener listener, String str, boolean z, int i, Object obj) {
            if (obj == null) {
                if ((i & 2) != 0) {
                    z = true;
                }
                listener.onSmartspaceMediaDataRemoved(str, z);
                return;
            }
            throw new UnsupportedOperationException("Super calls with default arguments not supported in this target, function: onSmartspaceMediaDataRemoved");
        }
    }

    private final SmartspaceMediaData toSmartspaceMediaData(SmartspaceTarget smartspaceTarget, boolean z) {
        Intent intent = (smartspaceTarget.getBaseAction() == null || smartspaceTarget.getBaseAction().getExtras() == null) ? null : (Intent) smartspaceTarget.getBaseAction().getExtras().getParcelable("dismiss_intent");
        String packageName = packageName(smartspaceTarget);
        if (packageName != null) {
            String smartspaceTargetId = smartspaceTarget.getSmartspaceTargetId();
            Intrinsics.checkNotNullExpressionValue(smartspaceTargetId, "target.smartspaceTargetId");
            SmartspaceAction baseAction = smartspaceTarget.getBaseAction();
            List iconGrid = smartspaceTarget.getIconGrid();
            Intrinsics.checkNotNullExpressionValue(iconGrid, "target.iconGrid");
            return new SmartspaceMediaData(smartspaceTargetId, z, packageName, baseAction, iconGrid, intent, smartspaceTarget.getCreationTimeMillis(), this.logger.getNewInstanceId());
        }
        SmartspaceMediaData empty_smartspace_media_data = MediaDataManagerKt.getEMPTY_SMARTSPACE_MEDIA_DATA();
        String smartspaceTargetId2 = smartspaceTarget.getSmartspaceTargetId();
        Intrinsics.checkNotNullExpressionValue(smartspaceTargetId2, "target.smartspaceTargetId");
        return SmartspaceMediaData.copy$default(empty_smartspace_media_data, smartspaceTargetId2, z, (String) null, (SmartspaceAction) null, (List) null, intent, smartspaceTarget.getCreationTimeMillis(), this.logger.getNewInstanceId(), 28, (Object) null);
    }

    private final String packageName(SmartspaceTarget smartspaceTarget) {
        List<SmartspaceAction> iconGrid = smartspaceTarget.getIconGrid();
        if (iconGrid == null || iconGrid.isEmpty()) {
            Log.w("MediaDataManager", "Empty or null media recommendation list.");
            return null;
        }
        for (SmartspaceAction extras : iconGrid) {
            Bundle extras2 = extras.getExtras();
            if (extras2 != null) {
                String string = extras2.getString(EXTRAS_MEDIA_SOURCE_PACKAGE_NAME);
                if (string != null) {
                    Intrinsics.checkNotNullExpressionValue(string, "getString(EXTRAS_MEDIA_SOURCE_PACKAGE_NAME)");
                    return string;
                }
                Void voidR = null;
            }
        }
        Log.w("MediaDataManager", "No valid package name is provided.");
        return null;
    }

    public void dump(PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        printWriter.println("internalListeners: " + this.internalListeners);
        printWriter.println("externalListeners: " + this.mediaDataFilter.getListeners$SystemUI_nothingRelease());
        printWriter.println("mediaEntries: " + this.mediaEntries);
        printWriter.println("useMediaResumption: " + this.useMediaResumption);
    }

    public final void onMediaDataLoaded(String str, String str2, MediaData mediaData) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData, "data");
        Trace.beginSection("MediaDataManager#onMediaDataLoaded");
        try {
            Assert.isMainThread();
            if (this.mediaEntries.containsKey(str)) {
                this.mediaEntries.put(str, mediaData);
                notifyMediaDataLoaded(str, str2, mediaData);
            }
            Unit unit = Unit.INSTANCE;
        } finally {
            Trace.endSection();
        }
    }
}
