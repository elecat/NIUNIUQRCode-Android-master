package com.niuniu.qrcode.scanner.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.umeng.analytics.AnalyticsConfig;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by liangminhua on 16/4/18.
 */
public class BaseAppCompatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AnalyticsConfig.setAppkey(this, "571458b2e0f55a963c0007ca");
        AnalyticsConfig.setChannel("fr");
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
