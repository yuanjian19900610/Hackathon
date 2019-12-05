package com.hacksthon.team.manager;

import android.text.TextUtils;
import android.util.Log;

import com.hacksthon.team.interfaces.SocketListener;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
public class SocketManager {

    private static final String TAG = SocketManager.class.getSimpleName();
    private static SocketManager mSocketManager;
    private Socket mSocket;
    //是否停止读取数据
    private boolean isEnable=true;
    private SocketListener mSocketListener;

    private ExecutorService threadPool;

    public static SocketManager getInstance(Socket socket) {
        if (mSocketManager == null) {
            synchronized (SocketManager.class) {
                mSocketManager = new SocketManager(socket);
            }
        }
        return mSocketManager;
    }

    private SocketManager(Socket socket) {
        this.mSocket=socket;
        threadPool = Executors.newCachedThreadPool();
    }

    public void setSocketListener(SocketListener socketListener) {
        this.mSocketListener = socketListener;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public boolean isConnect() {
        if (mSocket != null) {
            return mSocket.isConnected();
        }
        return false;
    }

    public void closeConnect() {
        try {
            if (mSocket != null) {
                mSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送数据
     * @param data  发送到数据
     */
    public void sendData(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        if (mSocket == null) {
            Log.d(TAG," sendData() socket is null");
            return;
        }
        try {

            OutputStream outputStream = mSocket.getOutputStream();
            outputStream.write(data.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取数据
     */
    public void receiveData() {
        if (mSocket == null) {
            Log.d(TAG,"receiveData() socket is null");
            return;
        }
        threadPool.submit(new WorkThread());
    }

    /**
     * 工作线程
     */
    public class WorkThread extends Thread {
        @Override
        public void run() {
            super.run();
            try {
                while (isEnable) {
                    InputStream inputStream = mSocket.getInputStream();
                    byte[] buff = new byte[1024];
                    String content = new String(buff, 0, inputStream.read(buff));
                    Log.i(TAG, "socket 接收到到数据：" + content);
                    if (mSocketListener != null) {
                        mSocketListener.receiveData(content);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
