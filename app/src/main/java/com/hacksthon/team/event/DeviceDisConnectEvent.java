package com.hacksthon.team.event;

public class DeviceDisConnectEvent {

    public String mac;

    public DeviceDisConnectEvent(String deviceInfo) {
        this.mac = deviceInfo;
    }


}
