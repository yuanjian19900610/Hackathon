package com.hacksthon.team.manager;

import android.content.Intent;
import android.nfc.Tag;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.Gson;
import com.hacksthon.team.MainActivity;
import com.hacksthon.team.PayActivity;
import com.hacksthon.team.bean.CmdConstantType;
import com.hacksthon.team.bean.DeviceInfo;
import com.hacksthon.team.bean.ServerRep;
import com.hacksthon.team.interfaces.SocketListener;
import com.hacksthon.team.utils.Constants;
import com.hacksthon.team.utils.SharedPreferencesUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Parameter;
import java.net.InetSocketAddress;
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
    private boolean isEnable = true;
    private SocketListener mSocketListener;
    private ExecutorService threadPool;
    private OutputStream outputStream = null;
    private InputStream inputStream = null;
    private DeviceInfo mData;
    private  String ipAddress;

    public static SocketManager getInstance() {
        if (mSocketManager == null) {
            synchronized (SocketManager.class) {
                mSocketManager = new SocketManager();
            }
        }
        return mSocketManager;
    }


    private SocketManager() {
        threadPool = Executors.newCachedThreadPool();
    }

    public void setSocket(Socket socket) {
        this.mSocket = socket;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
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

    public Socket getSocket() {
        return mSocket;
    }

    public void closeConnect() {
        try {
            if (mSocket != null) {
                mSocket.close();
            }

            if (inputStream != null) {
                inputStream.close();
            }

            if (outputStream != null) {
                outputStream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.i(TAG,"socket已经关闭");
    }

    public void sendData(final DeviceInfo deviceInfo) {
        this.mData = deviceInfo;
        this.isEnable = true;
        startSocket();
    }


    public void startSocket() {
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
                if (mSocket == null || !mSocket.isConnected()) {
                    mSocket = new Socket(ipAddress, Constants.PORT);
                }
                outputStream = mSocket.getOutputStream();
                Log.i(TAG, "客服端发出的数据：" + new Gson().toJson(mData));
                outputStream.write(new Gson().toJson(mData).getBytes());
                outputStream.flush();
                while (isEnable) {
                    if (mSocket == null || !mSocket.isConnected()) {
                        mSocket = new Socket(ipAddress, Constants.PORT);
                    }
                    inputStream = mSocket.getInputStream();
                    byte[] buff = new byte[1024];
                    String content = new String(buff, 0, inputStream.read(buff));
                    Log.i(TAG, "客服端 接收到到数据：" + content);
                    if (mSocketListener != null) {
                        mSocketListener.receiveData(content);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                Log.d(TAG, "线程结束。。。。。。isEnable=" + isEnable);
            }
        }
    }
}
