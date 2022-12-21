package com.android.internal.statsd;

import android.util.StatsEvent;
import android.util.StatsLog;

public class StatsdStatsLog {
    public static final byte ANNOTATION_ID_EXCLUSIVE_STATE = 4;
    public static final byte ANNOTATION_ID_IS_UID = 1;
    public static final byte ANNOTATION_ID_PRIMARY_FIELD = 3;
    public static final byte ANNOTATION_ID_PRIMARY_FIELD_FIRST_UID = 5;
    public static final byte ANNOTATION_ID_STATE_NESTED = 8;
    public static final byte ANNOTATION_ID_TRIGGER_STATE_RESET = 7;
    public static final byte ANNOTATION_ID_TRUNCATE_TIMESTAMP = 2;
    public static final int ANOMALY_DETECTED = 46;
    public static final int APP_BREADCRUMB_REPORTED = 47;
    public static final int APP_BREADCRUMB_REPORTED__STATE__START = 3;
    public static final int APP_BREADCRUMB_REPORTED__STATE__STOP = 2;
    public static final int APP_BREADCRUMB_REPORTED__STATE__UNKNOWN = 0;
    public static final int APP_BREADCRUMB_REPORTED__STATE__UNSPECIFIED = 1;
    public static final int BINARY_PUSH_STATE_CHANGED = 102;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__CHECKPOINT_NOT_SUPPORTED = 41;

    /* renamed from: BINARY_PUSH_STATE_CHANGED__STATE__INSTALLER_ROLLBACK_BOOT_TRIGGERED */
    public static final int f181xd1ec069c = 13;

    /* renamed from: BINARY_PUSH_STATE_CHANGED__STATE__INSTALLER_ROLLBACK_BOOT_TRIGGERED_FAILURE */
    public static final int f182x90d10ec7 = 14;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALLER_ROLLBACK_FAILURE = 16;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALLER_ROLLBACK_INITIATED = 9;

    /* renamed from: BINARY_PUSH_STATE_CHANGED__STATE__INSTALLER_ROLLBACK_INITIATED_FAILURE */
    public static final int f183x7125b144 = 10;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALLER_ROLLBACK_REQUESTED = 8;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALLER_ROLLBACK_STAGED = 11;

    /* renamed from: BINARY_PUSH_STATE_CHANGED__STATE__INSTALLER_ROLLBACK_STAGED_CANCEL_FAILURE */
    public static final int f184x16ef392c = 19;

    /* renamed from: BINARY_PUSH_STATE_CHANGED__STATE__INSTALLER_ROLLBACK_STAGED_CANCEL_REQUESTED */
    public static final int f185x606c3a90 = 17;

    /* renamed from: BINARY_PUSH_STATE_CHANGED__STATE__INSTALLER_ROLLBACK_STAGED_CANCEL_SUCCESS */
    public static final int f186xe86847a5 = 18;

    /* renamed from: BINARY_PUSH_STATE_CHANGED__STATE__INSTALLER_ROLLBACK_STAGED_FAILURE */
    public static final int f187x8c3eac3 = 12;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALLER_ROLLBACK_SUCCESS = 15;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALL_CANCELLED = 7;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALL_FAILURE = 6;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALL_FAILURE_COMMIT = 25;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALL_FAILURE_DOWNLOAD = 23;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALL_FAILURE_STATE_MISMATCH = 24;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALL_REQUESTED = 1;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALL_STAGED_CANCEL_FAILURE = 22;

    /* renamed from: BINARY_PUSH_STATE_CHANGED__STATE__INSTALL_STAGED_CANCEL_REQUESTED */
    public static final int f188xa6c9030c = 20;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALL_STAGED_CANCEL_SUCCESS = 21;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALL_STAGED_NOT_READY = 3;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALL_STAGED_READY = 4;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALL_STARTED = 2;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__INSTALL_SUCCESS = 5;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__LSKF_PREPARATION_FAILURE = 35;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__LSKF_PREPARATION_INITITATED = 33;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__LSKF_PREPARATION_RETRY = 40;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__LSKF_PREPARATION_SUCCESS = 34;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__NOTIFICATION_RESTART_ELIGIBLE = 29;

    /* renamed from: BINARY_PUSH_STATE_CHANGED__STATE__NO_REBOOT_PROMOTION_STRATEGY_ELIGIBLE */
    public static final int f189xf5a1f799 = 30;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__PENDING_REBOOT_CANCELLED = 43;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__PENDING_REBOOT_MARKED = 42;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__REBOOT_NOT_REQUIRED = 27;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__REBOOT_TRIGGERED = 26;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__REBOOT_TRIGGER_FAILURE = 31;

    /* renamed from: BINARY_PUSH_STATE_CHANGED__STATE__SIGNAL_NOT_READY_TO_REBOOT_RECEIVED */
    public static final int f190xe25b1b04 = 45;

    /* renamed from: BINARY_PUSH_STATE_CHANGED__STATE__SIGNAL_READY_TO_REBOOT_RECEIVED */
    public static final int f191xeec54178 = 44;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__SIM_PIN_PREPARATION_FAILURE = 38;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__SIM_PIN_PREPARATION_SUCCESS = 37;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__SOFT_RESTART_ELIGIBLE = 28;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__UNATTENDED_REBOOT_ABORT = 39;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__UNATTENDED_REBOOT_ELIGIBLE = 32;

    /* renamed from: BINARY_PUSH_STATE_CHANGED__STATE__UNATTENDED_REBOOT_NOT_TRIGGERED */
    public static final int f192xe6f45e6c = 36;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__UNKNOWN = 0;
    public static final int BINARY_PUSH_STATE_CHANGED__STATE__UNTRACKED_TRAIN_CANCELLED = 46;
    public static final int BINDER_CALLS = 10022;
    public static final int CPU_TIME_PER_UID_FREQ = 10010;
    public static final int DAVEY_OCCURRED = 58;
    public static final int ISOLATED_UID_CHANGED = 43;
    public static final int ISOLATED_UID_CHANGED__EVENT__CREATED = 1;
    public static final int ISOLATED_UID_CHANGED__EVENT__REMOVED = 0;
    public static final int LOOPER_STATS = 10024;
    public static final int TRAIN_INFO = 10051;
    public static final int TRAIN_INFO__STATUS__INSTALLER_ROLLBACK_BOOT_TRIGGERED = 13;
    public static final int TRAIN_INFO__STATUS__INSTALLER_ROLLBACK_BOOT_TRIGGERED_FAILURE = 14;
    public static final int TRAIN_INFO__STATUS__INSTALLER_ROLLBACK_FAILURE = 16;
    public static final int TRAIN_INFO__STATUS__INSTALLER_ROLLBACK_INITIATED = 9;
    public static final int TRAIN_INFO__STATUS__INSTALLER_ROLLBACK_INITIATED_FAILURE = 10;
    public static final int TRAIN_INFO__STATUS__INSTALLER_ROLLBACK_REQUESTED = 8;
    public static final int TRAIN_INFO__STATUS__INSTALLER_ROLLBACK_STAGED = 11;
    public static final int TRAIN_INFO__STATUS__INSTALLER_ROLLBACK_STAGED_CANCEL_FAILURE = 19;
    public static final int TRAIN_INFO__STATUS__INSTALLER_ROLLBACK_STAGED_CANCEL_REQUESTED = 17;
    public static final int TRAIN_INFO__STATUS__INSTALLER_ROLLBACK_STAGED_CANCEL_SUCCESS = 18;
    public static final int TRAIN_INFO__STATUS__INSTALLER_ROLLBACK_STAGED_FAILURE = 12;
    public static final int TRAIN_INFO__STATUS__INSTALLER_ROLLBACK_SUCCESS = 15;
    public static final int TRAIN_INFO__STATUS__INSTALL_CANCELLED = 7;
    public static final int TRAIN_INFO__STATUS__INSTALL_FAILURE = 6;
    public static final int TRAIN_INFO__STATUS__INSTALL_FAILURE_COMMIT = 25;
    public static final int TRAIN_INFO__STATUS__INSTALL_FAILURE_DOWNLOAD = 23;
    public static final int TRAIN_INFO__STATUS__INSTALL_FAILURE_STATE_MISMATCH = 24;
    public static final int TRAIN_INFO__STATUS__INSTALL_REQUESTED = 1;
    public static final int TRAIN_INFO__STATUS__INSTALL_STAGED_CANCEL_FAILURE = 22;
    public static final int TRAIN_INFO__STATUS__INSTALL_STAGED_CANCEL_REQUESTED = 20;
    public static final int TRAIN_INFO__STATUS__INSTALL_STAGED_CANCEL_SUCCESS = 21;
    public static final int TRAIN_INFO__STATUS__INSTALL_STAGED_NOT_READY = 3;
    public static final int TRAIN_INFO__STATUS__INSTALL_STAGED_READY = 4;
    public static final int TRAIN_INFO__STATUS__INSTALL_STARTED = 2;
    public static final int TRAIN_INFO__STATUS__INSTALL_SUCCESS = 5;
    public static final int TRAIN_INFO__STATUS__REBOOT_TRIGGERED = 26;
    public static final int TRAIN_INFO__STATUS__UNKNOWN = 0;
    public static final int WATCHDOG_ROLLBACK_OCCURRED = 147;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_APP_CRASH = 3;

    /* renamed from: WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_APP_NOT_RESPONDING */
    public static final int f193x2b461621 = 4;

    /* renamed from: WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_EXPLICIT_HEALTH_CHECK */
    public static final int f194x5c7b9946 = 2;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_NATIVE_CRASH = 1;

    /* renamed from: WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_NATIVE_CRASH_DURING_BOOT */
    public static final int f195x2d1647ba = 5;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_REASON__REASON_UNKNOWN = 0;

    /* renamed from: WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__ROLLBACK_BOOT_TRIGGERED */
    public static final int f196x2e3f8c3e = 4;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__ROLLBACK_FAILURE = 3;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__ROLLBACK_INITIATE = 1;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__ROLLBACK_SUCCESS = 2;
    public static final int WATCHDOG_ROLLBACK_OCCURRED__ROLLBACK_TYPE__UNKNOWN = 0;

    public static void write(int i, int i2, int i3, int i4) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeInt(i2);
        if (47 == i) {
            newBuilder.addBooleanAnnotation(ANNOTATION_ID_IS_UID, true);
        }
        newBuilder.writeInt(i3);
        newBuilder.writeInt(i4);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, int i2, long j) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeInt(i2);
        if (58 == i) {
            newBuilder.addBooleanAnnotation(ANNOTATION_ID_IS_UID, true);
        }
        newBuilder.writeLong(j);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, int i2, long j, long j2) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeInt(i2);
        if (46 == i) {
            newBuilder.addBooleanAnnotation(ANNOTATION_ID_IS_UID, true);
        }
        newBuilder.writeLong(j);
        newBuilder.writeLong(j2);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, int i2, String str, int i3, int i4, String str2, byte[] bArr) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeInt(i2);
        newBuilder.writeString(str);
        newBuilder.writeInt(i3);
        newBuilder.writeInt(i4);
        newBuilder.writeString(str2);
        if (bArr == null) {
            bArr = new byte[0];
        }
        newBuilder.writeByteArray(bArr);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static void write(int i, String str, long j, boolean z, boolean z2, boolean z3, int i2, byte[] bArr, int i3, int i4, boolean z4) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeString(str);
        newBuilder.writeLong(j);
        newBuilder.writeBoolean(z);
        newBuilder.writeBoolean(z2);
        newBuilder.writeBoolean(z3);
        newBuilder.writeInt(i2);
        if (bArr == null) {
            bArr = new byte[0];
        }
        newBuilder.writeByteArray(bArr);
        newBuilder.writeInt(i3);
        newBuilder.writeInt(i4);
        newBuilder.writeBoolean(z4);
        newBuilder.usePooledBuffer();
        StatsLog.write(newBuilder.build());
    }

    public static StatsEvent buildStatsEvent(int i, int i2, int i3, long j) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeInt(i2);
        if (10010 == i) {
            newBuilder.addBooleanAnnotation(ANNOTATION_ID_IS_UID, true);
        }
        newBuilder.writeInt(i3);
        newBuilder.writeLong(j);
        return newBuilder.build();
    }

    public static StatsEvent buildStatsEvent(int i, int i2, String str, String str2, long j, long j2, long j3, long j4, long j5, long j6, long j7, long j8, long j9, boolean z, int i3) {
        int i4 = i;
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        int i5 = i2;
        newBuilder.writeInt(i2);
        if (10022 == i4) {
            newBuilder.addBooleanAnnotation(ANNOTATION_ID_IS_UID, true);
        }
        String str3 = str;
        newBuilder.writeString(str);
        String str4 = str2;
        newBuilder.writeString(str2);
        long j10 = j;
        newBuilder.writeLong(j);
        long j11 = j2;
        newBuilder.writeLong(j2);
        long j12 = j3;
        newBuilder.writeLong(j3);
        long j13 = j4;
        newBuilder.writeLong(j4);
        newBuilder.writeLong(j5);
        newBuilder.writeLong(j6);
        newBuilder.writeLong(j7);
        newBuilder.writeLong(j8);
        newBuilder.writeLong(j9);
        newBuilder.writeBoolean(z);
        newBuilder.writeInt(i3);
        return newBuilder.build();
    }

    public static StatsEvent buildStatsEvent(int i, int i2, String str, String str2, String str3, long j, long j2, long j3, long j4, long j5, boolean z, long j6, long j7, long j8, long j9, long j10) {
        int i3 = i;
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        int i4 = i2;
        newBuilder.writeInt(i2);
        if (10024 == i3) {
            newBuilder.addBooleanAnnotation(ANNOTATION_ID_IS_UID, true);
        }
        String str4 = str;
        newBuilder.writeString(str);
        String str5 = str2;
        newBuilder.writeString(str2);
        String str6 = str3;
        newBuilder.writeString(str3);
        long j11 = j;
        newBuilder.writeLong(j);
        long j12 = j2;
        newBuilder.writeLong(j2);
        long j13 = j3;
        newBuilder.writeLong(j3);
        newBuilder.writeLong(j4);
        newBuilder.writeLong(j5);
        newBuilder.writeBoolean(z);
        newBuilder.writeLong(j6);
        newBuilder.writeLong(j7);
        newBuilder.writeLong(j8);
        newBuilder.writeLong(j9);
        newBuilder.writeLong(j10);
        return newBuilder.build();
    }

    public static StatsEvent buildStatsEvent(int i, long j, byte[] bArr, String str, int i2) {
        StatsEvent.Builder newBuilder = StatsEvent.newBuilder();
        newBuilder.setAtomId(i);
        newBuilder.writeLong(j);
        if (bArr == null) {
            bArr = new byte[0];
        }
        newBuilder.writeByteArray(bArr);
        newBuilder.writeString(str);
        newBuilder.writeInt(i2);
        return newBuilder.build();
    }
}
