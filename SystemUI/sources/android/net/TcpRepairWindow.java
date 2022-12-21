package android.net;

public final class TcpRepairWindow {
    public final int maxWindow;
    public final int rcvWnd;
    public final int rcvWndScale;
    public final int rcvWup;
    public final int sndWl1;
    public final int sndWnd;

    public TcpRepairWindow(int i, int i2, int i3, int i4, int i5, int i6) {
        this.sndWl1 = i;
        this.sndWnd = i2;
        this.maxWindow = i3;
        this.rcvWnd = i4;
        this.rcvWup = i5;
        this.rcvWndScale = i6;
    }
}
