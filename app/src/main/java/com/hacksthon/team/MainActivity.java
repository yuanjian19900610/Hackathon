package com.hacksthon.team;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.hacksthon.team.utils.Constants;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnDevicesManager;
    private Button btnPay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDevicesManager = (Button) findViewById(R.id.btn_devices_manager);
        btnPay = (Button) findViewById(R.id.btn_pay);
        btnDevicesManager.setOnClickListener(this);
        btnPay.setOnClickListener(this);
        SocketManager.getInstance().setSocketListener(new SocketListener() {
            @Override
            public void receiveData(String data) {
                final ServerRep serverRep = new Gson().fromJson(data, ServerRep.class);
                if (serverRep.cmdType == CmdConstantType.CMD_PAY) {
                    Log.i("smarhit", "支付信息：" + serverRep.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(MainActivity.this, PayActivity.class);
                            intent.putExtra(PayActivity.PAY_INFO, serverRep);
                            startActivity(intent);
                        }
                    });
                } else if (serverRep.cmdType == CmdConstantType.CMD_CONNECT) {
                    ToastUtils.showShort("连接成功");
                }else if(serverRep.cmdType == CmdConstantType.CMD_CLOSE_SCREEN){
                    ToastUtils.showShort("锁屏成功");
                }
            }
        });

        // SocketServerManager serverManager = SocketServerManager.getInstance();
        //serverManager.setEnable(true);
        //serverManager.startScoketServer();

//        SocketServer server = new SocketServer(new SocketListener() {
//            @Override
//            public void receiveData(String data) {
//                ToastUtils.showLong(data);
//            }
//        });

//        ServerRep resp=new ServerRep();
//        resp.cmdType=CmdConstantType.CMD_PAY;
//        resp.info="小龙坎火锅";
//        resp.amount=new BigDecimal(0.01);
//        resp.orderNo=System.currentTimeMillis()+"";


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        SocketServerManager.getInstance().stopSocketServer();
    }

    @Override
    public void onClick(View v) {
        DeviceInfo info = null;
        switch (v.getId()) {
            case R.id.btn_devices_manager:
//                startActivity(new Intent(this, DeviceManagerActivity.class));
//                SocketManager.getInstance().setEnable(false);
                info = new DeviceInfo();
                info.deviceMac = "20:59:a0:0e:58:c6";
                info.deviceIp = "192.168.0.114";
                info.deviceInfo = "连接设备";
                info.deviceType = 0x01;
                info.cmdType = CmdConstantType.CMD_CONNECT;
                SocketManager.getInstance().sendData(info);
                break;
            case R.id.btn_pay:
//                startSocket();
//                SocketManager.getInstance().setEnable(false);
                info = new DeviceInfo();
                info.deviceMac = "20:59:a0:0e:58:c6";
                info.deviceIp = "192.168.0.114";
                info.deviceInfo = "锁屏";
                info.deviceType = 0x01;
                info.cmdType = CmdConstantType.CMD_CLOSE_SCREEN;
                SocketManager.getInstance().sendData(info);
                break;
        }

    }

    private void startSocket() {
        new Thread() {
            @Override
            public void run() {
                super.run();
                OutputStream outputStream = null;
                InputStream inputStream = null;
                try {
                    Socket socket = new Socket(Constants.IPADDRESS, Constants.PORT);
                    Log.d("smarhit", "socket isconnect=" + socket.isConnected());
                    DeviceInfo info = new DeviceInfo();
                    info.deviceMac = "20:59:a0:0e:58:c6";
                    info.deviceIp = "192.168.0.114";
                    info.deviceInfo = "需要刷卡";
                    info.deviceType = 0x01;
                    info.cmdType = CmdConstantType.CMD_CONNECT;
                    outputStream = socket.getOutputStream();
                    outputStream.write(new Gson().toJson(info).getBytes());
                    Log.d("smarhit", "发送连接数据成功");
                    outputStream.flush();
                    try {
                        while (true) {
                            inputStream = socket.getInputStream();
                            byte[] buff = new byte[1024];
                            String content = new String(buff, 0, inputStream.read(buff));
                            Log.i("smarhit", "socket 接收到到数据：" + content);
                            final ServerRep serverRep = new Gson().fromJson(content, ServerRep.class);
                            if (serverRep.cmdType == CmdConstantType.CMD_PAY) {
                                Log.i("smarhit", "支付信息：" + serverRep.toString());
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(MainActivity.this, PayActivity.class);
                                        intent.putExtra(PayActivity.PAY_INFO, serverRep);
                                        startActivity(intent);
                                    }
                                });
                            } else if (serverRep.cmdType == CmdConstantType.CMD_CONNECT) {
                                info = new DeviceInfo();
                                info.deviceMac = "20:59:a0:0e:58:c6";
                                info.deviceIp = "192.168.0.114";
                                info.deviceInfo = "需要刷卡";
                                info.deviceType = 0x01;
                                info.cmdType = CmdConstantType.CMD_PAY;
                                outputStream = socket.getOutputStream();
                                outputStream.write(new Gson().toJson(info).getBytes());
                                Log.d("smarhit", "发送刷卡数据成功");
                                outputStream.flush();
                            }


                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (outputStream != null) {
                            outputStream.close();
                        }
                        if (inputStream != null) {
                            inputStream.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }


}
