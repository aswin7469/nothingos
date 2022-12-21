package com.android.okhttp;

import com.android.okhttp.internal.Platform;
import dalvik.system.PathClassLoader;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class TcmIdleTimerMonitor implements InvocationHandler {
    private static Object dpmTcmIface;
    private static Object lockObj = new Object();
    private static Method mTcmRegisterMethod;
    private static Object tcmClient;
    private ConnectionPool connectionPool;
    Object result = null;

    public TcmIdleTimerMonitor(ConnectionPool connectionPool2) {
        Object obj;
        synchronized (lockObj) {
            this.connectionPool = connectionPool2;
            try {
                if (mTcmRegisterMethod == null || tcmClient == null) {
                    PathClassLoader pathClassLoader = new PathClassLoader("/system/framework/tcmclient.jar", ClassLoader.getSystemClassLoader());
                    PathClassLoader pathClassLoader2 = new PathClassLoader("/system/framework/tcmiface.jar", ClassLoader.getSystemClassLoader());
                    Class<?> loadClass = pathClassLoader.loadClass("com.qti.tcmclient.DpmTcmClient");
                    dpmTcmIface = Proxy.newProxyInstance(pathClassLoader2, new Class[]{pathClassLoader2.loadClass("com.quicinc.tcmiface.DpmTcmIface")}, this);
                    tcmClient = loadClass.getDeclaredMethod("getInstance", new Class[0]).invoke((Object) null, new Object[0]);
                    mTcmRegisterMethod = loadClass.getDeclaredMethod("registerTcmMonitor", Object.class);
                }
                Method method = mTcmRegisterMethod;
                if (!(method == null || (obj = tcmClient) == null)) {
                    this.result = method.invoke(obj, dpmTcmIface);
                }
            } catch (ClassNotFoundException unused) {
            } catch (Exception e) {
                Platform platform = Platform.get();
                platform.logW("tcmclient load failed: " + e);
            }
        }
    }

    public Object invoke(Object obj, Method method, Object[] objArr) throws Throwable {
        OnCloseIdleConn();
        return null;
    }

    public void OnCloseIdleConn() {
        ConnectionPool connectionPool2 = this.connectionPool;
        if (connectionPool2 != null) {
            connectionPool2.closeIdleConnections();
        }
    }
}
