package com.hacksthon.team;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hacksthon.team.interfaces.SocketListener;
import com.hacksthon.team.manager.SocketServerManager;
import com.hacksthon.team.utils.Constants;

public class ServiceMainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_data;
    private Button btn_send;
    private TextView tv_content;
    private SocketServerManager serverManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_main);
        et_data = (EditText) findViewById(R.id.et_data);
        btn_send = (Button) findViewById(R.id.btn_send);
        tv_content = (TextView) findViewById(R.id.tv_content);
        btn_send.setOnClickListener(this);
        initSocketService();
    }


    private void initSocketService() {
        serverManager = SocketServerManager.getInstance();
//        serverManager.setSocketConfig(new SocketConfig(Constants.PORT));
        serverManager.setEnable(true);
      //  serverManager.stopSocketServer();
        serverManager.startScoketServer();
        serverManager.setSocketListener(new SocketListener() {
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
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_send) {
            if (TextUtils.isEmpty(et_data.getText().toString())) {
                Toast.makeText(getApplicationContext(), "请输入发送的内容", Toast.LENGTH_SHORT);
                return;
            }
            serverManager.sendData(et_data.getText().toString());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        serverManager.stopSocketServer();
    }
}
