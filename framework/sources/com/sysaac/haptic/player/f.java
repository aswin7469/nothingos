package com.sysaac.haptic.player;

import com.sysaac.haptic.sync.SyncCallback;
import java.io.File;
/* loaded from: classes4.dex */
public interface f {
    void a();

    void a(int i, int i2);

    void a(int i, int i2, int i3);

    void a(PlayerEventCallback playerEventCallback);

    void a(File file, int i, int i2, int i3, int i4);

    void a(File file, int i, int i2, SyncCallback syncCallback);

    void a(String str, int i, int i2, int i3, int i4);

    void a(String str, int i, int i2, SyncCallback syncCallback);

    void a(boolean z);

    void a(int[] iArr, int[] iArr2, int[] iArr3, boolean z, int i);

    boolean a(int i);

    void b();

    void b(int i, int i2);

    void b(String str, int i, int i2, SyncCallback syncCallback);

    void c();

    boolean d();

    boolean e();

    int f();

    int g();

    boolean h();

    void i();
}
