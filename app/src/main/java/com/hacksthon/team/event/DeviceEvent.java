package com.hacksthon.team.event;

public class DeviceEvent {

    public DeviceInfo mDeviceInfo;

    public DeviceEvent(DeviceInfo deviceInfo) {
        this.mDeviceInfo = deviceInfo;
    }

    public static class DeviceInfo {

        public Integer deviceType;
        public String deviceName;
        public String deviceIp;
        public String deviceInfo;
        public String deviceMac;//唯一设备地址

    }

}
