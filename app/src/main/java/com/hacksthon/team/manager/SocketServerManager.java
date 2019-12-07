package com.hacksthon.team.manager;

import android.text.TextUtils;
import android.util.Log;
import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.hacksthon.team.HackathonApplication;
import com.hacksthon.team.LockActivity;
import com.hacksthon.team.R;
import com.hacksthon.team.bean.CmdConstantType;
import com.hacksthon.team.bean.DeviceInfo;
import com.hacksthon.team.bean.ServerRep;
import com.hacksthon.team.event.DeviceDisConnectEvent;
import com.hacksthon.team.event.DeviceEvent;
import com.hacksthon.team.event.DevicePayEvent;
import com.hacksthon.team.interfaces.SocketListener;
import com.hacksthon.team.utils.Constants;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.greenrobot.eventbus.EventBus;

/**
 * <pre>
 * com.hacksthon.team.manager
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
public class SocketServerManager {

    public static Map<String, Socket> cacheSocketMap = new HashMap<>();
    public static Map<String, Long> cacheTimes = new HashMap<>();

    private static final String TAG = SocketServerManager.class.getSimpleName();
    public static SocketServerManager manager;
    private boolean isEnable=true;
    private ExecutorService threadPool;
    private ServerSocket mServerSocket;
    private Socket socket;
    private SocketListener mSocketListener;


    public static SocketServerManager getInstance() {
        synchronized (SocketServerManager.class) {
            if (manager == null) {
                manager = new SocketServerManager();
            }
        }
        return manager;
    }

    private SocketServerManager() {
        threadPool = Executors.newCachedThreadPool();
    }


    public void setSocketListener(SocketListener socketListener) {
        this.mSocketListener = socketListener;
    }

    /**
     * 开启socketServer
     */
    public void startScoketServer() {
        Log.d("smarhit","开启了socketServer");
        MonitorThread monitorThread = new MonitorThread();
        monitorThread.start();
    }

    /**
     * 关闭socketServer
     */
    public void stopSocketServer() {
        try {
            mServerSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        isEnable=false;
        mServerSocket = null;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public void sendData(String data){
        if (TextUtils.isEmpty(data)) {
            return;
        }
        if (socket == null) {
            Log.d(TAG," sendData() socket is null");
            return;
        }
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public class MonitorThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
//                InetSocketAddress socketAddress = new InetSocketAddress(mConfig.getPort());
                InetSocketAddress socketAddress = new InetSocketAddress(Constants.IPADDRESS, Constants.PORT);
                mServerSocket = new ServerSocket();
                mServerSocket.bind(socketAddress );
                ToastUtils.showShort("服务启动");
                while (isEnable) {
                    socket = mServerSocket.accept();
                    Log.d("hackson","服务器收到的数据:"+socket);
                   // threadPool.submit(new WorkThread());
                   // threadPool.submit();
                    new DeviceThread(socket).start();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public class DeviceThread extends Thread {
        private Socket mSocket;
        public DeviceThread(Socket socket) {
            this.mSocket = socket;
        }
        @Override
        public void run() {
            if (mSocket != null) {
                try {

                    OutputStream outputStream = socket.getOutputStream();
                    InputStream inputStream = socket.getInputStream();
                    byte buffer[] = new byte[1024 * 4];
                    int temp = 0;
                    String mac = "";
                    while ((temp = inputStream.read(buffer)) != -1) {
                        String content = new String(buffer, 0, temp, "UTF-8");

                        DeviceInfo deviceInfo = new Gson().fromJson(content, DeviceInfo.class);
                        mac = deviceInfo.deviceMac;

                        if (deviceInfo.cmdType == CmdConstantType.CMD_CONNECT) {

                            cacheTimes.put(deviceInfo.deviceMac, System.currentTimeMillis());
                            cacheSocketMap.put(deviceInfo.deviceMac, socket);

                            ServerRep serverRep = new ServerRep();
                            serverRep.cmdType = CmdConstantType.CMD_CONNECT;
                            serverRep.info = "收到连接";
                            outputStream.write(new Gson().toJson(serverRep).getBytes("utf-8"));
                            outputStream.flush();
                          //  ToastUtils.showShort("服务端》》连接指令收到");
                            deviceInfo.deviceStatus = "连接正常";
                            EventBus.getDefault().post(new DeviceEvent(deviceInfo));

                        } else if (deviceInfo.cmdType == CmdConstantType.CMD_CLOSE_SCREEN) {

                            ServerRep serverRep = new ServerRep();
                            serverRep.cmdType = CmdConstantType.CMD_CLOSE_SCREEN;
                            serverRep.info = "锁屏";
                            outputStream.write(new Gson().toJson(serverRep).getBytes("utf-8"));
                            outputStream.flush();
                          //  ToastUtils.showShort("服务端》》锁屏指令收到");
                            ActivityUtils.startActivity(LockActivity.class);

                        } else if (deviceInfo.cmdType == CmdConstantType.CMD_PLAY_SOUND) {

                            ServerRep serverRep = new ServerRep();
                            serverRep.cmdType = CmdConstantType.CMD_PLAY_SOUND;
                            serverRep.info = "播放声音";
                            outputStream.write(new Gson().toJson(serverRep).getBytes("utf-8"));
                            outputStream.flush();
                          //  ToastUtils.showShort("服务端》》播放声音指令收到");
                            MediaPlayerManager.playerSound(HackathonApplication.sContext, R.raw.pos_sound);

                        } else if (deviceInfo.cmdType == CmdConstantType.CMD_PAY) {
                            ServerRep resp=new ServerRep();
                            resp.cmdType=CmdConstantType.CMD_PAY;
                            resp.info="小龙坎火锅";
                            resp.amount=new BigDecimal(0.01).setScale(2,BigDecimal.ROUND_HALF_UP);
                            resp.orderNo=System.currentTimeMillis()+"";
                            outputStream.write(new Gson().toJson(resp).getBytes("utf-8"));
                            outputStream.flush();
                          //  ToastUtils.showShort("请求刷卡。。。");


                        } else if (deviceInfo.cmdType == CmdConstantType.CMD_PAY_SUCCESS) {
                            Log.i("qinglin.fan", "SockertServerManager >>> 支付成功");
                           // ToastUtils.showShort("支付成功。。。");
                            EventBus.getDefault().post(new DevicePayEvent(deviceInfo));

                        } else if (deviceInfo.cmdType == CmdConstantType.CMD_PAY_FAILED) {
                            //ToastUtils.showShort("支付失败。。。");
                            EventBus.getDefault().post(new DevicePayEvent(deviceInfo));
                        }

                    }
                    Log.i("qinglin.fan", "设备已经断开");
                    cacheTimes.remove(mac);
                    cacheSocketMap.remove(mac);
                    EventBus.getDefault().post(new DeviceDisConnectEvent(mac));
                    inputStream.close();
                    outputStream.close();
                    socket.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    public class WorkThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                InputStream inputStream = socket.getInputStream();
                byte buffer[] = new byte[1024 * 4];
                int temp = 0;
                while ((temp = inputStream.read(buffer)) != -1) {
                    String content = new String(buffer, 0, temp, "UTF-8");
                    Log.d("hackson","服务器收到的数据:"+content);
                    if(mSocketListener!=null){
                        mSocketListener.receiveData(content);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
