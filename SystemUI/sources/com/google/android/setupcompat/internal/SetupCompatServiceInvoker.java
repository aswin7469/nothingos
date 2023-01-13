package com.google.android.setupcompat.internal;

import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import com.google.android.setupcompat.ISetupCompatService;
import com.google.android.setupcompat.util.Logger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SetupCompatServiceInvoker {
    private static final Logger LOG = new Logger("SetupCompatServiceInvoker");
    private static final long MAX_WAIT_TIME_FOR_CONNECTION_MS = TimeUnit.SECONDS.toMillis(10);
    private static SetupCompatServiceInvoker instance;
    private final Context context;
    private final ExecutorService loggingExecutor = ExecutorProvider.setupCompatServiceInvoker.get();
    private final long waitTimeInMillisForServiceConnection = MAX_WAIT_TIME_FOR_CONNECTION_MS;

    public void logMetricEvent(int i, Bundle bundle) {
        try {
            this.loggingExecutor.execute(new SetupCompatServiceInvoker$$ExternalSyntheticLambda1(this, i, bundle));
        } catch (RejectedExecutionException e) {
            LOG.mo55171e(String.format("Metric of type %d dropped since queue is full.", Integer.valueOf(i)), e);
        }
    }

    public void bindBack(String str, Bundle bundle) {
        try {
            this.loggingExecutor.execute(new SetupCompatServiceInvoker$$ExternalSyntheticLambda2(this, str, bundle));
        } catch (RejectedExecutionException e) {
            LOG.mo55171e(String.format("Screen %s bind back fail.", str), e);
        }
    }

    public void onFocusStatusChanged(String str, Bundle bundle) {
        try {
            this.loggingExecutor.execute(new SetupCompatServiceInvoker$$ExternalSyntheticLambda0(this, str, bundle));
        } catch (RejectedExecutionException e) {
            LOG.mo55171e(String.format("Screen %s report focus changed failed.", str), e);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: invokeLogMetric */
    public void mo54938xaaa27393(int i, Bundle bundle) {
        try {
            ISetupCompatService iSetupCompatService = SetupCompatServiceProvider.get(this.context, this.waitTimeInMillisForServiceConnection, TimeUnit.MILLISECONDS);
            if (iSetupCompatService != null) {
                iSetupCompatService.logMetric(i, bundle, Bundle.EMPTY);
            } else {
                LOG.mo55175w("logMetric failed since service reference is null. Are the permissions valid?");
            }
        } catch (RemoteException | IllegalStateException | InterruptedException | TimeoutException e) {
            LOG.mo55171e(String.format("Exception occurred while trying to log metric = [%s]", bundle), e);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: invokeOnWindowFocusChanged */
    public void mo54939xcb05bb98(String str, Bundle bundle) {
        try {
            ISetupCompatService iSetupCompatService = SetupCompatServiceProvider.get(this.context, this.waitTimeInMillisForServiceConnection, TimeUnit.MILLISECONDS);
            if (iSetupCompatService != null) {
                iSetupCompatService.onFocusStatusChanged(bundle);
            } else {
                LOG.mo55175w("Report focusChange failed since service reference is null. Are the permission valid?");
            }
        } catch (RemoteException | InterruptedException | UnsupportedOperationException | TimeoutException e) {
            LOG.mo55171e(String.format("Exception occurred while %s trying report windowFocusChange to SetupWizard.", str), e);
        }
    }

    /* access modifiers changed from: private */
    /* renamed from: invokeBindBack */
    public void mo54937x485a6bf2(String str, Bundle bundle) {
        try {
            ISetupCompatService iSetupCompatService = SetupCompatServiceProvider.get(this.context, this.waitTimeInMillisForServiceConnection, TimeUnit.MILLISECONDS);
            if (iSetupCompatService != null) {
                iSetupCompatService.validateActivity(str, bundle);
            } else {
                LOG.mo55175w("BindBack failed since service reference is null. Are the permissions valid?");
            }
        } catch (RemoteException | InterruptedException | TimeoutException e) {
            LOG.mo55171e(String.format("Exception occurred while %s trying bind back to SetupWizard.", str), e);
        }
    }

    private SetupCompatServiceInvoker(Context context2) {
        this.context = context2;
    }

    public static synchronized SetupCompatServiceInvoker get(Context context2) {
        SetupCompatServiceInvoker setupCompatServiceInvoker;
        synchronized (SetupCompatServiceInvoker.class) {
            if (instance == null) {
                instance = new SetupCompatServiceInvoker(context2.getApplicationContext());
            }
            setupCompatServiceInvoker = instance;
        }
        return setupCompatServiceInvoker;
    }

    static void setInstanceForTesting(SetupCompatServiceInvoker setupCompatServiceInvoker) {
        instance = setupCompatServiceInvoker;
    }
}
