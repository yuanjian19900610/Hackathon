package com.hacksthon.team.event;

import com.hacksthon.team.bean.DeviceInfo;

public class DevicePayEvent {

    public DeviceInfo mDeviceInfo;

    public DevicePayEvent(DeviceInfo deviceInfo) {
        this.mDeviceInfo = deviceInfo;
    }

}
