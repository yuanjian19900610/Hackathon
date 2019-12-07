package com.hacksthon.team;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.gson.Gson;
import com.hacksthon.team.bean.DeviceInfo;
import com.hacksthon.team.event.DeviceEvent;
import com.hacksthon.team.interfaces.SocketListener;
import com.hacksthon.team.manager.SocketManager;
import com.hacksthon.team.utils.Constants;

import java.io.IOException;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
 * Created by scott on 2019/12/5.
 *
 * *-------------------------------------------------------------------*
 *  </pre>
 */
public class SocketServer extends IntentService {

    private SocketListener mSocketListener;
    // 通过静态方法创建ScheduledExecutorService的实例
    private ScheduledExecutorService mScheduledExecutorService = Executors.newScheduledThreadPool(2);


    public SocketServer() {
        super(SocketServer.class.getSimpleName());
    }

    public void setSocketListener(SocketListener socketListener) {
        this.mSocketListener = socketListener;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("smarhit","服务已经启动");
        startSocket("10.180.6.241");
        sendData();
    }

    private void startSocket(final String ipAddrss) {
        try {
            Socket socket = new Socket(ipAddrss, Constants.PORT);
            SocketManager mSocketManager = SocketManager.getInstance();
            mSocketManager.setSocket(socket);
            mSocketManager.setEnable(true);
//            mSocketManager.receiveData();
            mSocketManager.setSocketListener(new SocketListener() {
                @Override
                public void receiveData(final String data) {
                    if (mSocketListener != null) {
                        mSocketListener.receiveData(data);
                    }
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 定时发送数据
     */
    private void sendData() {
        // 循环任务，按照上一次任务的发起时间计算下一次任务的开始时间
        mScheduledExecutorService.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
//                DeviceEvent.DeviceInfo deviceInfo = new DeviceEvent.DeviceInfo();
                DeviceInfo info = new DeviceInfo();
                info.deviceMac = "20:59:a0:0e:58:c6";
                info.deviceIp = "192.168.0.124";
                info.deviceInfo = "设备正常";
                info.deviceType = 0x01;
                info.cmdType = 0x04;
//                SocketManager.getInstance().sendData(new Gson().toJson(info));
            }
        }, 5, 10, TimeUnit.SECONDS);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SocketManager.getInstance().closeConnect();
    }
}
