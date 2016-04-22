package com.niuniu.qrcode.scanner.activity;

import android.app.AlertDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.niuniu.qrcode.scanner.R;
import com.niuniu.qrcode.scanner.encode.MyQRCodeEncoder;
import com.niuniu.qrcode.scanner.utils.CommonUtils;
import com.niuniu.qrcode.scanner.utils.CustomViewUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;

import cn.bingoogolapple.qrcode.core.DisplayUtils;

public class Generatectivity extends BaseAppCompatActivity implements MyQRCodeEncoder.MyEncoderDelegate, View.OnClickListener {
    EditText inputContentEditText;
    String chineseInput;
    ImageView showEncodeResultImageView;
    File appDir;
    String appDirPath;
    TextView menuTextView;
    TextView savePictureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generate);
        setViews();
        CustomViewUtils.initToolbar(this, R.id.toolbar, R.id.toolbar_title, R.string.generate_qrcode, true, true, R.id.menu_tv, R.string.scan);
        CustomViewUtils.lengthFilter(this, inputContentEditText, 200, getResources().getString(R.string.input_lenght_out_of_range_warning));
        final ClipboardManager cbm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        if (cbm.getText() != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Generatectivity.this);
            builder.setMessage(getResources().getString(R.string.paste_message)).setCancelable(true).setNegativeButton(getResources().getString(R.string.cancle), null).setPositiveButton(getResources().getString(R.string.ok), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    inputContentEditText.setText(cbm.getText());
                }
            });
            builder.show();
        }
        menuTextView.setOnClickListener(this);
    }

    private void setViews() {
        inputContentEditText = (EditText) findViewById(R.id.et_chinese_encode);
        showEncodeResultImageView = (ImageView) findViewById(R.id.show_encode_result_iv);
        menuTextView = (TextView) findViewById(R.id.menu_tv);
        savePictureTextView = (TextView) findViewById(R.id.save_picture_tv);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.encode_qrcode:
                CommonUtils.hideSoftKeyboard(this);
                showEncodeResultImageView.setVisibility(View.VISIBLE);
                savePictureTextView.setVisibility(View.VISIBLE);
                encodeContent();
                break;
            case R.id.save_picture_tv:
                saveImage();
                break;
            case R.id.menu_tv:
                Intent intent = new Intent(this, ScanActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void encodeContent() {
        chineseInput = inputContentEditText.getText().toString();
        createQRCode(chineseInput);
    }

    private void createQRCode(String chineseInput) {
        try {
            chineseInput = new String(chineseInput.getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        MyQRCodeEncoder.encodeQRCode(chineseInput, DisplayUtils.dp2px(Generatectivity.this, 150), Color.parseColor("#000000"), this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    public void onEncodeQRCodeSuccess(Bitmap bitmap) {
        showEncodeResultImageView.setImageBitmap(bitmap);
    }

    @Override
    public void onEncodeQRCodeFailure() {
        Toast.makeText(this, getResources().getString(R.string.generate_QR_Code_fail), Toast.LENGTH_LONG).show();
    }

    private void saveImage() {
        showEncodeResultImageView.setDrawingCacheEnabled(true);
        showEncodeResultImageView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        Bitmap viewBitmap = Bitmap.createBitmap(showEncodeResultImageView.getDrawingCache());
        if (viewBitmap != null) {
            new SaveImageTask().execute(viewBitmap);
        }
    }

    public class SaveImageTask extends AsyncTask<Bitmap, Void, String> {

        @Override
        protected String doInBackground(Bitmap... params) {
            String result = getResources().getString(R.string.save_picture_success);
            try {
                saveImageToGallery(params[0]);
            } catch (Exception e) {
                result = getResources().getString(R.string.save_picture_failed);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s.equals(getResources().getString(R.string.save_picture_success))) {
                Toast.makeText(Generatectivity.this, getResources().getString(R.string.picture_has_been_save_to) + appDirPath + getResources().getString(R.string.dir), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(Generatectivity.this, s, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveImageToGallery(Bitmap bitmap) throws Exception {
        appDir = new File(Environment.getExternalStorageDirectory(), "QRCodeScanner");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        appDirPath = appDir.getAbsolutePath();
        String fileName = "" + System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        String imagePath = file.getAbsolutePath();
        FileOutputStream fos = new FileOutputStream(file);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] bytes = baos.toByteArray();
        fos.write(bytes);
        fos.flush();
        fos.close();
        MediaStore.Images.Media.insertImage(this.getContentResolver(), imagePath, "", "QRCodeScanner");
        this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + imagePath)));
    }

}