package java.lang;

import java.net.InetAddress;
import java.p026io.FileDescriptor;
import java.security.Permission;

public class SecurityManager {
    @Deprecated
    protected boolean inCheck;

    public void checkAccept(String str, int i) {
    }

    public void checkAccess(Thread thread) {
    }

    public void checkAccess(ThreadGroup threadGroup) {
    }

    public void checkAwtEventQueueAccess() {
    }

    public void checkConnect(String str, int i) {
    }

    public void checkConnect(String str, int i, Object obj) {
    }

    public void checkCreateClassLoader() {
    }

    public void checkDelete(String str) {
    }

    public void checkExec(String str) {
    }

    public void checkExit(int i) {
    }

    public void checkLink(String str) {
    }

    public void checkListen(int i) {
    }

    @Deprecated
    public void checkMemberAccess(Class<?> cls, int i) {
    }

    public void checkMulticast(InetAddress inetAddress) {
    }

    @Deprecated
    public void checkMulticast(InetAddress inetAddress, byte b) {
    }

    public void checkPackageAccess(String str) {
    }

    public void checkPackageDefinition(String str) {
    }

    public void checkPermission(Permission permission) {
    }

    public void checkPermission(Permission permission, Object obj) {
    }

    public void checkPrintJobAccess() {
    }

    public void checkPropertiesAccess() {
    }

    public void checkPropertyAccess(String str) {
    }

    public void checkRead(FileDescriptor fileDescriptor) {
    }

    public void checkRead(String str) {
    }

    public void checkRead(String str, Object obj) {
    }

    public void checkSecurityAccess(String str) {
    }

    public void checkSetFactory() {
    }

    public void checkSystemClipboardAccess() {
    }

    @Deprecated
    public boolean checkTopLevelWindow(Object obj) {
        return true;
    }

    public void checkWrite(FileDescriptor fileDescriptor) {
    }

    public void checkWrite(String str) {
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public int classDepth(String str) {
        return -1;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public int classLoaderDepth() {
        return -1;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public ClassLoader currentClassLoader() {
        return null;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public Class<?> currentLoadedClass() {
        return null;
    }

    /* access modifiers changed from: protected */
    public Class[] getClassContext() {
        return null;
    }

    public Object getSecurityContext() {
        return null;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public boolean inClass(String str) {
        return false;
    }

    /* access modifiers changed from: protected */
    @Deprecated
    public boolean inClassLoader() {
        return false;
    }

    @Deprecated
    public boolean getInCheck() {
        return this.inCheck;
    }

    public ThreadGroup getThreadGroup() {
        return Thread.currentThread().getThreadGroup();
    }
}
