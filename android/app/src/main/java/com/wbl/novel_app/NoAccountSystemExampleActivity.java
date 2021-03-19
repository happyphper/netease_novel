package com.wbl.novel_app;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NoAccountSystemExampleActivity extends AppCompatActivity {

    private TextView mAccountStateTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_no_account_system_example);

        Button OpenYDBtn = findViewById(R.id.btn_open_yd);
        OpenYDBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoAccountSystemReadWapActivity.startNoAccountSystemReadWapActivity(
                        com.wbl.novel_app.NoAccountSystemExampleActivity.this,
                        com.wbl.novel_app.Constants.YD_URL,
                        com.wbl.novel_app.Constants.YD_APP_NO_ACCOUNT_SYSTEM_CHANNEL
                );
            }
        });

        mAccountStateTV = findViewById(R.id.tv_account_state);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (com.wbl.novel_app.NoAccountSystemStorage.sSDKAuth != null) {
            mAccountStateTV.setText("是否已保存SDKAuth:是");
        } else {
            mAccountStateTV.setText("是否已保存SDKAuth:否");
        }
    }

}
