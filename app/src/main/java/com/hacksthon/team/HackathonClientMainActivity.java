package com.hacksthon.team;

import android.content.Intent;
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
import com.hacksthon.team.manager.SocketManager;

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

    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DeviceInfo info = new DeviceInfo();
            info.deviceMac = "20:59:a0:0e:58:c6";
            info.deviceIp = "192.168.0.114";
            info.deviceInfo = "连接设备";
            info.deviceType = 0x01;
            info.cmdType = CmdConstantType.CMD_CONNECT;
            SocketManager.getInstance().sendData(info);
            mHandler.removeMessages(0);
            mHandler.sendEmptyMessageDelayed(0,5000);
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hackathon_client_main);
        mBtnLock = (Button) findViewById(R.id.btn_lock);
        mBtnLock.setOnClickListener(this);

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
                    ToastUtils.showShort("连接成功");
                } else if (serverRep.cmdType == CmdConstantType.CMD_CLOSE_SCREEN) {
                    ToastUtils.showShort("锁屏成功");
                }
            }
        });

        mHandler.sendEmptyMessageDelayed(0,2000);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_lock:
                DeviceInfo info = new DeviceInfo();
                info.deviceMac = "20:59:a0:0e:58:c6";
                info.deviceIp = "192.168.0.114";
                info.deviceInfo = "锁屏";
                info.deviceType = 0x01;
                info.cmdType = CmdConstantType.CMD_CLOSE_SCREEN;
                SocketManager.getInstance().sendData(info);
                break;
            default:
                break;
        }
    }
}
