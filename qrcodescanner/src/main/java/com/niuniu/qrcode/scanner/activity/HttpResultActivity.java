package com.niuniu.qrcode.scanner.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.niuniu.qrcode.scanner.R;
import com.niuniu.qrcode.scanner.constant.Constant;
import com.niuniu.qrcode.scanner.utils.CommonUtils;
import com.niuniu.qrcode.scanner.utils.CustomViewUtils;

/**
 * Created by liangminhua on 16/4/18.
 */
public class HttpResultActivity extends BaseAppCompatActivity implements View.OnClickListener {
    String resultContent;
    TextView resultContentTextView, copyContentTextView, openLinkTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_result);
        Intent intent = getIntent();
        resultContent = intent.getStringExtra(Constant.SCAN_RESULT);
        setViews();
        CustomViewUtils.initToolbar(this, R.id.toolbar, R.id.toolbar_title, R.string.scan_result, true,false,0,0);
        resultContentTextView.setText(resultContent);
        openLinkTextView.setOnClickListener(this);
    }

    private void setViews() {
        resultContentTextView = (TextView) findViewById(R.id.result_content_tv);
        copyContentTextView = (TextView) findViewById(R.id.copy_content_tv);
        openLinkTextView = (TextView) findViewById(R.id.open_link_tv);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open_link_tv:
                Intent intent = new Intent();
                intent.setAction("android.intent.action.VIEW");
                Uri content_url = Uri.parse(resultContent);
                intent.setData(content_url);
                startActivity(intent);
                break;
            case R.id.copy_content_tv:
                CommonUtils.copy(this, resultContentTextView);
                Toast.makeText(this,getResources().getString(R.string.copy_successful),Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
