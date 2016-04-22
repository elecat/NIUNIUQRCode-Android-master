package com.niuniu.qrcode.scanner.utils;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by liangminhua on 16/4/18.
 */
public class CustomViewUtils {
    //设置toolbar标题内容，设置是否带返回键
    public static Toolbar initToolbar(final AppCompatActivity activity, int id, int titleId, int titleString, Boolean hasBackButton, Boolean hasMenu,int menuID,int menuTitleString) {
        Toolbar toolbar = (Toolbar) activity.findViewById(id);
        TextView titleTextView = (TextView) activity.findViewById(titleId);
        titleTextView.setText(titleString);
        if (hasMenu){
            TextView menuTextView = (TextView) activity.findViewById(menuID);
            menuTextView.setText(menuTitleString);
        }
        activity.setSupportActionBar(toolbar);
        if (hasBackButton) {
            android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setDisplayShowTitleEnabled(false);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                });
            }
        } else {
            android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        return toolbar;
    }


    //EditText、文本框、输入框最大输入数提示 都可以调用此方法。在onCreat()中调用
    public static void lengthFilter(final Context context, final EditText editText, final int max_length, final String err_msg) {

        InputFilter[] filters = new InputFilter[1];

        filters[0] = new InputFilter.LengthFilter(max_length) {

            @Override
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {

                int destLen = getCharacterNum(dest.toString()); //获取字符个数(一个中文算2个字符)
                int sourceLen = getCharacterNum(source.toString());
                if (destLen + sourceLen > max_length) {
                    Toast.makeText(context, err_msg, Toast.LENGTH_SHORT).show();
                    return "";
                }
                return source;

            }

        };

        editText.setFilters(filters);

    }

    /**
     * @param content
     * @return
     * @description 获取一段字符串的字符个数（包含中英文，一个中文算2个字符）
     */
    public static int getCharacterNum(final String content) {
        if (null == content || "".equals(content)) {
            return 0;
        } else {
            return (content.length() + getChineseNum(content));
        }
    }

    /**
     * @param s
     * @return
     * @description 返回字符串里中文字或者全角字符的个数
     */
    public static int getChineseNum(String s) {

        int num = 0;
        char[] myChar = s.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }

}
