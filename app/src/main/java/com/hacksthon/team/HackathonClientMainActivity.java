package com.hacksthon.team;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.hacksthon.team.bean.CmdConstantType;
import com.hacksthon.team.bean.DeviceInfo;
import com.hacksthon.team.bean.ServerRep;
import com.hacksthon.team.interfaces.SocketListener;
import com.hacksthon.team.manager.MediaPlayerManager;
import com.hacksthon.team.manager.SocketManager;
import com.hacksthon.team.utils.ClientDataUtil;
import com.hacksthon.team.utils.Constants;
import com.hacksthon.team.utils.SharedPreferencesUtil;
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

    private LinearLayout mLayoutTitle;
    private LinearLayout mLayoutIpaddress;
    private EditText mEtIpAddress;
    private Button mBtnSure;
    private Button mBtnLock;
    private Button mBtnPlayer;
    private String macAddress;

    private int clickCount=0;
    private long lastTime;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hackathon_client_main);
        getSupportActionBar().hide();
        macAddress = SystemUtils.getDeviceIDByMac(this);

        mLayoutTitle= (LinearLayout) findViewById(R.id.layout_title);
        mLayoutIpaddress= (LinearLayout) findViewById(R.id.layout_ipaddress);
        mEtIpAddress= (EditText) findViewById(R.id.et_ipaddress);
        mBtnSure= (Button) findViewById(R.id.btn_sure);
        mBtnLock = (Button) findViewById(R.id.btn_lock);
        mBtnPlayer = (Button) findViewById(R.id.btn_player);
        mBtnSure.setOnClickListener(this);
        mBtnLock.setOnClickListener(this);
        mBtnPlayer.setOnClickListener(this);
        mLayoutTitle.setOnClickListener(this);
        String ipAddress = SharedPreferencesUtil.getInstance(getApplicationContext()).getSP(Constants.IPADDRESS);
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

        if(TextUtils.isEmpty(ipAddress)){
            ToastUtils.showShort("请输入服务器IP地址");
        }else{
            SocketManager.getInstance().setIpAddress(ipAddress);
            mHandler.sendEmptyMessageDelayed(0, 1000);
        }




    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DeviceInfo info = ClientDataUtil.getConnectData(HackathonClientMainActivity.this);
            SocketManager.getInstance().sendData(info);
            mHandler.removeMessages(0);
            mHandler.sendEmptyMessageDelayed(0, 3000);
        }
    };

    @Override
    public void onClick(View v) {
        DeviceInfo info = null;
        switch (v.getId()) {
            case R.id.btn_lock:
                info = ClientDataUtil.getLockScreenData(this);
                SocketManager.getInstance().sendData(info);
                break;
            case R.id.btn_player:
                info = ClientDataUtil.getPlaySoundData(this);
                SocketManager.getInstance().sendData(info);
                break;
            case R.id.layout_title:
                Log.d("smarhit","lastTime="+lastTime+",差距="+(System.currentTimeMillis()-lastTime)+",clickCount="+clickCount);
                if(System.currentTimeMillis()-lastTime<500) {
                    clickCount++;
                    if(clickCount>=3){
                        mLayoutIpaddress.setVisibility(View.VISIBLE);
                    }
                }else {
                    clickCount=1;
                }
                lastTime = System.currentTimeMillis();
                break;
            case R.id.btn_sure:
                String ipAddress=mEtIpAddress.getText().toString().trim();
                Log.d("smarhit","手动设置的ip地址="+ipAddress);
                SharedPreferencesUtil.getInstance(getApplicationContext()).putSP(Constants.IPADDRESS,ipAddress);
                ToastUtils.showShort("设置IP地址成功");
                mLayoutIpaddress.setVisibility(View.GONE);
                clickCount=0;
                lastTime = System.currentTimeMillis();
                SocketManager.getInstance().setIpAddress(ipAddress);
                mHandler.sendEmptyMessageDelayed(0, 1000);
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
