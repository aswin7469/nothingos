package com.nothing.tesla.service;

public class CmdObject {
    public static final int CAR_MODEL_3 = 0;
    public static final int CAR_MODEL_S = 2;
    public static final int CAR_MODEL_X = 3;
    public static final int CAR_MODEL_Y = 1;
    public static final int CMD_AC = 200;
    public static final int CMD_EXE_FAIL = 1;
    public static final int CMD_EXE_OK = 0;
    public static final int CMD_FLASH = 401;
    public static final int CMD_FRONT_TRUNK = 300;
    public static final int CMD_HONK = 400;
    public static final int CMD_HORN = 6;
    public static final int CMD_LOCK_UNLOCK = 100;
    public static final int CMD_OFF = 3;
    public static final int CMD_ON = 2;
    public static final int CMD_REAR_TRUNK = 301;
    public static final int CMD_TEMP_DOWN = 8;
    public static final int CMD_TEMP_UP = 9;
    public static final int CMD_VENT = 500;
    public static final String TESLA_KEY_AUTO_HOTSPOT = "tesla_hotspot";
    public static final String TESLA_KEY_PANEL_ACTIVE = "tesla_active";
    public static final int TESLA_VALUE_AUTO_HOTSPOT = 0;
    public static final int TESLA_VALUE_PANEL_ACTIVE = 0;
    public static final int TESLA_VALUE_PANEL_INACTIVE = 1;
    public static final int TESLA_VALUE_UNAUTO_HOTSPOT = 1;
    public static final int TYPE_CMD = 0;
    public static final int TYPE_TEMPERATURE = 2;
    public static final int TYPE_VEHICLES_TITLE = 1;
    public int carType;
    public int cmd;
    public String cmdValue;
    public boolean status;
    public String subTitle;
    public String title;
    public int type;

    public boolean getStatus() {
        return this.status;
    }

    public void setStatus(boolean z) {
        this.status = z;
    }

    public int getCmd() {
        return this.cmd;
    }

    public void setCmd(int i) {
        this.cmd = i;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String str) {
        this.title = str;
    }

    public String getSubTitle() {
        return this.subTitle;
    }

    public String getCmdValue() {
        return this.cmdValue;
    }

    public void setCmdValue(String str) {
        this.cmdValue = str;
    }

    public void setSubTitle(String str) {
        this.subTitle = str;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int i) {
        this.type = i;
    }
}
