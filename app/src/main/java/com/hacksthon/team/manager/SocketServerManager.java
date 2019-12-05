package com.hacksthon.team.manager;

import android.text.TextUtils;
import android.util.Log;

import com.hacksthon.team.bean.SocketConfig;
import com.hacksthon.team.interfaces.SocketListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private static final String TAG=SocketServerManager.class.getSimpleName();
    public static SocketServerManager manager;
    private boolean isEnable=true;
    private ExecutorService threadPool;
    private SocketConfig mConfig;
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

    public void setSocketConfig(SocketConfig config) {
        this.mConfig = config;

    }

    public void setSocketListener(SocketListener socketListener) {
        this.mSocketListener = socketListener;
    }

    /**
     * 开启socketServer
     */
    public void startScoketServer() {
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
                InetSocketAddress socketAddress = new InetSocketAddress(mConfig.getPort());
                mServerSocket = new ServerSocket();
                mServerSocket.bind(socketAddress);
                while (isEnable) {
                    socket = mServerSocket.accept();
                    threadPool.submit(new WorkThread());
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
                    InputStream inputStream = socket.getInputStream();
                    byte[] data = new byte[1024 * 4];
                    int temp = 0;
                    while ((temp = inputStream.read(data)) != -1) {
                        String content = new String(data, 0, temp, "UTF-8");
                        Log.d("hackson","服务器收到的数据:"+content);
//                        DeviceInfo deviceInfo = new Gson().fromJson(content, DeviceInfo.class);
//                        EventBus.getDefault().post(new DeviceEvent(deviceInfo));
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
