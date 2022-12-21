package com.nothing.experience.datas;

public class DeviceInfo {
    public String brand_name;
    public String category;
    public String device_color;
    public String device_id;
    public String device_id_2;
    public String is_locked;
    public String language;
    public String model_name;
    public String operating_system;
    public String operating_system_version;
    public String os_version;
    public String ram_size;
    public String rom_size;
    public String sw_build_info;

    public String toString() {
        return "DeviceInfo{category='" + this.category + '\'' + ", brand_name='" + this.brand_name + '\'' + ", model_name='" + this.model_name + '\'' + ", operating_system_version='" + this.operating_system_version + '\'' + ", language='" + this.language + '\'' + ", os_version='" + this.os_version + '\'' + ", operating_system='" + this.operating_system + '\'' + ", ram_size='" + this.ram_size + '\'' + ", rom_size='" + this.rom_size + '\'' + ", device_color='" + this.device_color + '\'' + ", sw_build_info='" + this.sw_build_info + '\'' + '}';
    }
}
