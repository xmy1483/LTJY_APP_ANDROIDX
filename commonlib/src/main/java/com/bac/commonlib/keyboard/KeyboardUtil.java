package com.bac.commonlib.keyboard;

import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.view.View;
import android.widget.TextView;

/**
 * Created by wujiazhen on 2017/7/7.
 */
public class KeyboardUtil {


    private KeyboardUtilCallback keyboardUtilCallback;
    private KeyboardView keyboardView;

    private boolean isnun = false;// 是否数据键盘
    private boolean isupper = false;// 是否大写

    private TextView textView;

    public KeyboardUtil(TextView textView, KeyboardView keyboardView) {
        this.textView = textView;
        this.keyboardView = keyboardView;

        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(listener);
    }

    public KeyboardUtil(TextView textView, KeyboardView keyboardView, KeyboardUtilCallback keyboardUtilCallback) {
        this.textView = textView;
        this.keyboardView = keyboardView;

        keyboardView.setEnabled(true);
        keyboardView.setPreviewEnabled(false);
        keyboardView.setOnKeyboardActionListener(listener);
        this.keyboardUtilCallback = keyboardUtilCallback;
    }

    public void setKeyboard(Keyboard k) {
        keyboardView.setKeyboard(k);
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = textView.getEditableText();
            int start = editable.length();// 当前光标

            if (primaryCode == Keyboard.KEYCODE_CANCEL) {// 完成
                hideKeyboard();//隐藏
            } else if (primaryCode == Keyboard.KEYCODE_DELETE) {// 回退
                if (editable != null && editable.length() > 0) {// 输入的长度
                    if (start > 0) {
                        editable.delete(start - 1, start);// 删除
                    }
                }
            } else if (primaryCode == Keyboard.KEYCODE_SHIFT) {// 大小写切换
                changeKey();
            } else if (primaryCode == Keyboard.KEYCODE_MODE_CHANGE) {// 数字键盘切换
                isnun = !isnun;
            } else if (primaryCode == -100) {
                if (keyboardUtilCallback != null) {
                    keyboardUtilCallback.callback();
                } else {
                    hideKeyboard();
                }
            } else {
                editable.insert(start, Character.toString((char) primaryCode));
            }
        }
    };


    public interface KeyboardUtilCallback {
        void callback();
    }

    /**
     * 键盘大小写切换
     */
    private void changeKey() {
//        List<Keyboard.Key> keylist = k1.getKeys();
//        if (isupper) {//大写切换小写
//            isupper = false;
//            for (Keyboard.Key key : keylist) {
//                if (key.label != null && isword(key.label.toString())) {
//                    key.label = key.label.toString().toLowerCase();
//                    key.codes[0] = key.codes[0] + 32;
//                }
//            }
//        } else {//小写切换大写
//            isupper = true;
//            for (Keyboard.Key key : keylist) {
//                if (key.label != null && isword(key.label.toString())) {
//                    key.label = key.label.toString().toUpperCase();
//                    key.codes[0] = key.codes[0] - 32;
//                }
//            }
//        }
    }

    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isword(String str) {
        String wordstr = "abcdefghijklmnopqrstuvwxyz";
        return wordstr.indexOf(str.toLowerCase()) > -1;
    }

}
