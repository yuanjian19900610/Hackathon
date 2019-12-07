package com.hacksthon.team;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hacksthon.team.interfaces.SocketListener;
import com.hacksthon.team.manager.SocketManager;
import com.hacksthon.team.utils.Constants;

import java.io.IOException;
import java.net.Socket;

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
public class ClientMainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_ipaddress;
    private Button btn_connect;
    private EditText et_data;
    private Button btn_send;
    private TextView tv_content;
    private SocketManager mSocketManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_main);
        et_ipaddress = (EditText) findViewById(R.id.et_ipaddress);
        btn_connect= (Button) findViewById(R.id.btn_connect);
        et_data = (EditText) findViewById(R.id.et_data);
        btn_send = (Button) findViewById(R.id.btn_send);
        tv_content = (TextView) findViewById(R.id.tv_content);
        btn_connect.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        et_ipaddress.setText("10.180.1.124");
    }


    private void startSocket(final String ipAddrss) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                try {
                    Socket socket = new Socket(ipAddrss, Constants.PORT);
                    mSocketManager = SocketManager.getInstance();
                    mSocketManager.setSocket(socket);
                    mSocketManager.setEnable(true);
//                    mSocketManager.receiveData();
                    mSocketManager.setSocketListener(new SocketListener() {
                        @Override
                        public void receiveData(final String Data) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_content.setText(Data);
                                }
                            });
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send) {
            if (TextUtils.isEmpty(et_data.getText().toString())) {
                Toast.makeText(getApplicationContext(), "请输入发送的内容", Toast.LENGTH_SHORT);
                return;
            }
//            mSocketManager.sendData(et_data.getText().toString());
        }else if(v.getId()==R.id.btn_connect){

            if (TextUtils.isEmpty(et_ipaddress.getText().toString())) {
                Toast.makeText(getApplicationContext(), "请输入服务器IP地址", Toast.LENGTH_SHORT);
                return;
            }
            startSocket(et_ipaddress.getText().toString());
        }
    }

}
