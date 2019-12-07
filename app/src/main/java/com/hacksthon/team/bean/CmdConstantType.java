package com.hacksthon.team.bean;

/**
 * 命令类型
 */
public class CmdConstantType {

    //锁屏
    public static final int CMD_CLOSE_SCREEN = 0x01;

    //播放声音
    public static final int CMD_PLAY_SOUND = 0x02;

    //确认收到
    public static final int CMD_CONFIRM_RECEIVE = 0x22;

    //连接
    public static final int CMD_CONNECT = 0x04;

    //断开连接
    public static final int CMD_DIS_CONNECT = 0x08;

    //支付
    public static final int CMD_PAY = 0x09;

    //支付设备
    public static final int CMD_PAY_DEVICES = 0x10;

    //支付成功
    public static final int CMD_PAY_SUCCESS= 0x19;

    //支付失败
    public static final int CMD_PAY_FAILED= 0x29;




}
