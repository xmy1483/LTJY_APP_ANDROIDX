package com.bac.commonlib.keyboard;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.bac.commonlib.R;
import com.jakewharton.rxbinding.widget.RxTextView;

import java.util.Random;

import rx.functions.Action1;

/**
 * Created by wujiazhen on 2017/7/7.
 */

public class BacInputTextView {
    private final PopupWindow popupWindow;
    public BacInputTextView(Context context, View view, final int max, final BacInputTextViewCallback inputTextViewCallback) {

        View popup = View.inflate(context, R.layout.cl_keyboard_pop, null);
        Random random = new Random();

        //view1
        View view1 = popup.findViewById(R.id.view1);
        LinearLayout.LayoutParams view1P = (LinearLayout.LayoutParams) view1.getLayoutParams();
        view1P.weight= random.nextInt(20);
        view1.setLayoutParams(view1P);
          //view2
        View view2 = popup.findViewById(R.id.view2);
        LinearLayout.LayoutParams view2P = (LinearLayout.LayoutParams) view2.getLayoutParams();
        view2P.weight= random.nextInt(20);
        view2.setLayoutParams(view2P);

        TextView bet = popup.findViewById(R.id.bet);
        KeyboardView kv = popup.findViewById(R.id.kv);
        KeyboardUtil keyboardUtil = new KeyboardUtil(bet, kv, new KeyboardUtil.KeyboardUtilCallback() {
            @Override
            public void callback() {
                dismiss();
            }
        });
        Keyboard keyboard = null;
        int i = random.nextInt(2);
        if ((i % 2 == 0)) {
            keyboard = new Keyboard(context, R.xml.cl_number_1);
        } else {
            keyboard = new Keyboard(context, R.xml.cl_number_2);
        }
        keyboardUtil.setKeyboard(keyboard);
        keyboardUtil.showKeyboard();

        // pop
        popupWindow = new PopupWindow(popup, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        // 设置获取焦点
        popupWindow.setFocusable(true);
        // 设置边缘点击收起
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new ColorDrawable());
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        RxTextView.textChanges(bet)
                .subscribe(new Action1<CharSequence>() {
                    @Override
                    public void call(CharSequence charSequence) {
                        if (charSequence.length()==max) {
                            inputTextViewCallback.callback(charSequence.toString());
                        }
                    }
                });
    }

    public void dismiss(){
        popupWindow.dismiss();
    }

    public interface BacInputTextViewCallback {
        void callback(String text);
    }
}
