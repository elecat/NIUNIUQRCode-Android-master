package com.niuniu.qrcode.scanner.decode;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

/**
 * Created by liangminhua on 16/4/16.
 */
public class MyQRCodeDecoder {


    public static void decodeQRCodeBitmap(Bitmap bitmap, final MyDecodeDelegate delegate){
        new AsyncTask<Bitmap, Void, String>(){

            @Override
            protected String doInBackground(Bitmap... params) {
                MultiFormatReader multiFormatReader = new MultiFormatReader();
                multiFormatReader.setHints(changeZXingDecodeDataMode());

                int width = params[0].getWidth();
                int height = params[0].getHeight();
                int[] pixels = new int[width * height];
                params[0].getPixels(pixels, 0, width, 0, 0, width, height);

                Result rawResult = null;
                RGBLuminanceSource source = new RGBLuminanceSource(width, height, pixels);

                if (source != null) {
                    BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(source));
                    try {
                        rawResult = multiFormatReader.decodeWithState(binaryBitmap);
                    } catch (ReaderException re) {
                        // continue
                    } finally {
                        multiFormatReader.reset();
                    }
                }
                return rawResult != null ? rawResult.getText() : null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
              if (delegate!=null){
                  if(s!=null){
                      delegate.myOnDecodeQRCodeSuccess(s);
                  }else {
                      delegate.myOnEncodeQRCodeFailure();
                  }

              }
            }
        }.execute(bitmap);



    }

    private static Map<DecodeHintType, Object> changeZXingDecodeDataMode() {
        Map<DecodeHintType, Object> hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
        Collection<BarcodeFormat> decodeFormats = new ArrayList<BarcodeFormat>();

                decodeFormats.addAll(DecodeFormatManager.getBarCodeFormats());
                decodeFormats.addAll(DecodeFormatManager.getQrCodeFormats());

        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        return hints;
    }

    public interface MyDecodeDelegate {
        /**
         * 解析二维码图片成功
         *
         * @param
         */
        void myOnDecodeQRCodeSuccess(String s);

        /**
         * 解析二维码图片失败
         */
        void myOnEncodeQRCodeFailure();
    }
}
