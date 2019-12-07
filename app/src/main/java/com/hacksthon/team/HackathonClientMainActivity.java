package com.hacksthon.team;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.hacksthon.team.bean.CmdConstantType;
import com.hacksthon.team.bean.DeviceInfo;
import com.hacksthon.team.bean.ServerRep;
import com.hacksthon.team.interfaces.SocketListener;
import com.hacksthon.team.manager.MediaPlayerManager;
import com.hacksthon.team.manager.SocketManager;
import com.hacksthon.team.utils.SystemUtils;

/**
 * <pre>
 * com.hacksthon.team
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
 * Created by scott on 2019-12-07.
 *
 * *-------------------------------------------------------------------*
 *  </pre>
 */
public class HackathonClientMainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mBtnLock;
    private Button mBtnPlayer;
    private String macAddress;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DeviceInfo info = new DeviceInfo();
            info.deviceMac = macAddress;
            info.deviceIp = "192.168.0.114";
            info.deviceBattery=SystemUtils.getDeviceBattery(macAddress);
            info.deviceName = SystemUtils.getDevicesName(macAddress);
            info.deviceInfo = "连接设备";
            info.deviceType = 0x01;
            info.deviceStatus = "在线";
            info.cmdType = CmdConstantType.CMD_CONNECT;
            SocketManager.getInstance().sendData(info);
            mHandler.removeMessages(0);
            mHandler.sendEmptyMessageDelayed(0, 20000);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hackathon_client_main);
        macAddress = SystemUtils.getDeviceIDByMac(this);
        mBtnLock = (Button) findViewById(R.id.btn_lock);
        mBtnPlayer = (Button) findViewById(R.id.btn_player);
        mBtnLock.setOnClickListener(this);
        mBtnPlayer.setOnClickListener(this);

        SocketManager.getInstance().setSocketListener(new SocketListener() {
            @Override
            public void receiveData(String data) {
                final ServerRep serverRep = new Gson().fromJson(data, ServerRep.class);
                if (serverRep.cmdType == CmdConstantType.CMD_PAY) {
                    Log.i("smarhit", "支付信息：" + serverRep.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(HackathonClientMainActivity.this, PayActivity.class);
                            intent.putExtra(PayActivity.PAY_INFO, serverRep);
                            startActivity(intent);
                        }
                    });
                } else if (serverRep.cmdType == CmdConstantType.CMD_CONNECT) {
//                    ToastUtils.showShort("连接成功");
                } else if (serverRep.cmdType == CmdConstantType.CMD_CLOSE_SCREEN) {
                    ToastUtils.showShort("锁屏成功");
                } else if (serverRep.cmdType == CmdConstantType.CMD_PLAY_SOUND) {
                    MediaPlayerManager.playerSound(HackathonClientMainActivity.this, R.raw.sound);
                }
            }
        });

        mHandler.sendEmptyMessageDelayed(0, 2000);

    }

    @Override
    public void onClick(View v) {
        DeviceInfo info = null;
        switch (v.getId()) {
            case R.id.btn_lock:
                info = new DeviceInfo();
                info.deviceMac = macAddress;
                info.deviceName = SystemUtils.getDevicesName(macAddress);
                info.deviceIp = "192.168.0.114";
                info.deviceInfo = "锁屏";
                info.deviceBattery=SystemUtils.getDeviceBattery(macAddress);
                info.deviceType = 0x01;
                info.cmdType = CmdConstantType.CMD_CLOSE_SCREEN;
                SocketManager.getInstance().sendData(info);
                break;
            case R.id.btn_player:
                info = new DeviceInfo();
                info.deviceMac = macAddress;
                info.deviceName = SystemUtils.getDevicesName(macAddress);
                info.deviceIp = "192.168.0.114";
                info.deviceBattery=SystemUtils.getDeviceBattery(macAddress);
                info.deviceInfo = "播放音频";
                info.deviceType = 0x01;
                info.cmdType = CmdConstantType.CMD_PLAY_SOUND;
                SocketManager.getInstance().sendData(info);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SocketManager.getInstance().closeConnect();
        if (mHandler != null) {
            mHandler.removeMessages(0);
            mHandler = null;
        }

    }
}
