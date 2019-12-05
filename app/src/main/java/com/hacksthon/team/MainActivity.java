package com.hacksthon.team;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnDevicesManager;
    private Button btnPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDevicesManager= (Button) findViewById(R.id.btn_devices_manager);
        btnPay= (Button) findViewById(R.id.btn_pay);
        btnDevicesManager.setOnClickListener(this);
        btnPay.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_devices_manager:

                break;
            case R.id.btn_pay:
                startActivity(new Intent(this,PayActivity.class));
                break;
        }

    }
}
