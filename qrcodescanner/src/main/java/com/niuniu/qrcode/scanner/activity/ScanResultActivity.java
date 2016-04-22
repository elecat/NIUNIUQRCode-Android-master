package com.niuniu.qrcode.scanner.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.niuniu.qrcode.scanner.R;
import com.niuniu.qrcode.scanner.constant.Constant;
import com.niuniu.qrcode.scanner.utils.CommonUtils;
import com.niuniu.qrcode.scanner.utils.CustomViewUtils;

/**
 * Created by liangminhua on 16/4/14.
 */
public class ScanResultActivity extends BaseAppCompatActivity implements View.OnClickListener {
    TextView resultContentTextView, copyContentTextView;
    String resultContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);
        Intent intent = getIntent();
        resultContent = intent.getStringExtra(Constant.SCAN_RESULT);
        setViews();
        CustomViewUtils.initToolbar(this, R.id.toolbar, R.id.toolbar_title, R.string.scan_result, true,false,0,0);
        resultContentTextView.setText(resultContent);
        copyContentTextView.setOnClickListener(this);
    }

    private void setViews() {
        resultContentTextView = (TextView) findViewById(R.id.result_content_tv);
        copyContentTextView = (TextView) findViewById(R.id.copy_content_tv);
    }

    @Override
    public void onClick(View v) {
        CommonUtils.copy(this, resultContentTextView);
        Toast.makeText(this, getResources().getString(R.string.copy_successful), Toast.LENGTH_SHORT).show();

    }
}
