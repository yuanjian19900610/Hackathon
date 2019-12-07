package com.hacksthon.team;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.View.OnClickListener;

public class LockActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lock);
        findViewById(R.id.ll_content).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
    }
}
