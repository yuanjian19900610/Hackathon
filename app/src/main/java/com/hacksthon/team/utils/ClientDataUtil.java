package com.hacksthon.team.utils;

import android.content.Context;

import com.hacksthon.team.bean.CmdConstantType;
import com.hacksthon.team.bean.DeviceInfo;

/**
 * <pre>
 * com.hacksthon.team.utils
 *
 * *-------------------------------------------------------------------*
 *     scott
 *                                    江城子 . 程序员之歌
 *     /\__/\
 *    /`    '\                     十年生死两茫茫，写程序，到天亮。
 *  ≈≈≈ 0  0 ≈≈≈ Hello world!          千行代码，Bug何处藏。
 *    \  --  /                     纵使上线又怎样，朝令改，夕断肠。
 *   /        \                    领导每天新想法，天天改，日日忙。
 *  /          \                       相顾无言，惟有泪千行。
 * |            |                  每晚灯火阑珊处，夜难寐，加班狂。
 *  \  ||  ||  /
 *   \_oo__oo_/≡≡≡≡≡≡≡≡o
 *
 * Created by scott on 2019-12-08.
 *
 * *-------------------------------------------------------------------*
 *  </pre>
 */
public class ClientDataUtil {

    public static DeviceInfo getConnectData(Context context){
        String macAddress=SystemUtils.getDeviceIDByMac(context);
        DeviceInfo info = new DeviceInfo();
        info.deviceMac =macAddress;
        info.deviceIp = SystemUtils.getDeviceIpAddress(macAddress);
        info.deviceBattery=SystemUtils.getDeviceBattery(macAddress);
        info.deviceName = SystemUtils.getDevicesName(macAddress);
        info.deviceInfo = "连接设备";
        info.deviceType = SystemUtils.getDevicesType(macAddress);
        info.deviceStatus = "在线";
        info.cmdType = CmdConstantType.CMD_CONNECT;
        return info;
    }

    public static DeviceInfo getLockScreenData(Context context){
        String macAddress=SystemUtils.getDeviceIDByMac(context);
        DeviceInfo info = new DeviceInfo();
        info.deviceMac = macAddress;
        info.deviceName = SystemUtils.getDevicesName(macAddress);
        info.deviceIp = SystemUtils.getDeviceIpAddress(macAddress);
        info.deviceInfo = "锁屏";
        info.deviceBattery=SystemUtils.getDeviceBattery(macAddress);
        info.deviceType = SystemUtils.getDevicesType(macAddress);
        info.cmdType = CmdConstantType.CMD_CLOSE_SCREEN;
        return info;
    }

    public static DeviceInfo getPlaySoundData(Context context){
        String macAddress=SystemUtils.getDeviceIDByMac(context);
        DeviceInfo info = new DeviceInfo();
        info = new DeviceInfo();
        info.deviceMac = macAddress;
        info.deviceName = SystemUtils.getDevicesName(macAddress);
        info.deviceIp = SystemUtils.getDeviceIpAddress(macAddress);
        info.deviceBattery=SystemUtils.getDeviceBattery(macAddress);
        info.deviceInfo = "播放音频";
        info.deviceType = SystemUtils.getDevicesType(macAddress);
        info.cmdType = CmdConstantType.CMD_PLAY_SOUND;
        return info;
    }


}
