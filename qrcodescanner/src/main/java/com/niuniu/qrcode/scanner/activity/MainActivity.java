package com.niuniu.qrcode.scanner.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.niuniu.qrcode.scanner.R;
import com.niuniu.qrcode.scanner.utils.CustomViewUtils;

import java.util.List;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseAppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private long lastBackTime=0;
    private long currentBackTime=0;
    private static final int REQUEST_CODE_QRCODE_PERMISSIONS = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CustomViewUtils.initToolbar(this, R.id.toolbar, R.id.toolbar_title, R.string.app_name, false,false,0,0);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scan_qrcode:
                startActivity(new Intent(this, ScanActivity.class));
                break;
            case R.id.generate_qrcode:
                startActivity(new Intent(this, Generatectivity.class));
                break;

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        requestCodeQrcodePermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    @AfterPermissionGranted(REQUEST_CODE_QRCODE_PERMISSIONS)
    private void requestCodeQrcodePermissions() {
        String[] perms = {Manifest.permission.CAMERA, Manifest.permission.FLASHLIGHT};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, getResources().getString(R.string.apply_for_perssion), REQUEST_CODE_QRCODE_PERMISSIONS, perms);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //捕获返回键按下的事件
        if(keyCode == KeyEvent.KEYCODE_BACK){
            //获取当前系统时间的毫秒数
            currentBackTime = System.currentTimeMillis();
            //比较上次按下返回键和当前按下返回键的时间差，如果大于2秒，则提示再按一次退出
            if(currentBackTime - lastBackTime > 2 * 1000){
                Toast.makeText(this,getResources().getString(R.string.exit_warning), Toast.LENGTH_SHORT).show();
                lastBackTime = currentBackTime;
            }else{
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
