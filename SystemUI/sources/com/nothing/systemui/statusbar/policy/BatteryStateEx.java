package com.nothing.systemui.statusbar.policy;

public class BatteryStateEx {
    public int status;
    public int temperature;
    public int voltage;

    public void copy(BatteryStateEx batteryStateEx) {
        this.status = batteryStateEx.status;
        this.temperature = batteryStateEx.temperature;
        this.voltage = batteryStateEx.voltage;
    }

    public String toString() {
        return "BatteryStateEx {status=" + this.status + ", temperature=" + this.temperature + ", voltage=" + this.voltage + '}';
    }
}
