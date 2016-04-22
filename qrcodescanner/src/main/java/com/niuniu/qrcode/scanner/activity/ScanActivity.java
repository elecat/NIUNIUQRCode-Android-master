package com.niuniu.qrcode.scanner.activity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;


import com.niuniu.qrcode.scanner.R;
import com.niuniu.qrcode.scanner.constant.Constant;
import com.niuniu.qrcode.scanner.decode.MyQRCodeDecoder;
import com.niuniu.qrcode.scanner.utils.BitMapUtils;
import com.niuniu.qrcode.scanner.utils.CustomViewUtils;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ScanActivity extends BaseAppCompatActivity implements QRCodeView.Delegate, MyQRCodeDecoder.MyDecodeDelegate, View.OnClickListener {
    private static final String TAG = ScanActivity.class.getSimpleName();
    private static final int GET_PICTURE_FROM_ALBUM = 100;

    private QRCodeView mQRCodeView;
    private Toolbar toolbar;
    private TextView menu_tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        setViews();
        CustomViewUtils.initToolbar(this, R.id.toolbar, R.id.toolbar_title, R.string.qrcode, true, true, R.id.menu_tv, R.string.album);
        menu_tv.setOnClickListener(this);
        mQRCodeView.setResultHandler(this);
    }

    private void setViews() {
        menu_tv = (TextView) findViewById(R.id.menu_tv);
        mQRCodeView = (ZXingView) findViewById(R.id.zxingview);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            Bitmap bitmap = BitMapUtils.getSmallBitmap(picturePath, 720, 960);
            decodeQRCodeBitmap(bitmap);
        } else {

        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.e(TAG, "打开相机出错");
    }

    private void decodeQRCodeBitmap(Bitmap bitmap) {
        MyQRCodeDecoder.decodeQRCodeBitmap(bitmap, this);
    }

    private void initView() {
//        显示扫描框
        mQRCodeView.startSpotAndShowRect();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mQRCodeView.startCamera();
        mQRCodeView.startCamera();
        initView();
    }

    @Override
    protected void onStop() {
        mQRCodeView.stopCamera();
        super.onStop();
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        showScanResult(result);
    }

    //解析图片成功
    @Override
    public void myOnDecodeQRCodeSuccess(String result) {
        showScanResult(result);
    }

    //解析图片失败
    @Override
    public void myOnEncodeQRCodeFailure() {
        Toast.makeText(this, "该图片不是二维码图片噢~", Toast.LENGTH_LONG).show();
    }

    private void showScanResult(String result) {
        boolean isurl = URLUtil.isHttpUrl(result) || URLUtil.isHttpsUrl(result);
        if (isurl) {
            Intent intent = new Intent(ScanActivity.this, HttpResultActivity.class);
            intent.putExtra(Constant.SCAN_RESULT, result);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, ScanResultActivity.class);
            intent.putExtra(Constant.SCAN_RESULT, result);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, GET_PICTURE_FROM_ALBUM);
    }

}