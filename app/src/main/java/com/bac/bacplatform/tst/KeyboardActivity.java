package com.bac.bacplatform.tst;

import android.content.Context;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.bac.bacplatform.R;


/**
 * Created by wujiazhen on 2017/7/7.
 */

public class KeyboardActivity extends AppCompatActivity {


    private EditText tv2;
    private EditText tv;
    private Context context;
    private KeyboardView keyboardView;
    private TextView bet;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cl_keyboard_activity);
//        context = this;
//        bet = (TextView) findViewById(R.id.bet);
//        tv = (EditText) findViewById(R.id.tv);
//        tv2 = (EditText) findViewById(R.id.tv2);
//
//        keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
//
//        bet.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                //new KeyboardUtil(bet, context, keyboardView).showKeyboard();
//                return true;
//            }
//        });
//
//        tv2.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                int inputback = tv2.getInputType();
//                tv2.setInputType(InputType.TYPE_NULL);
//                //   new KeyboardUtil(KeyboardActivity.this, context, tv2).showKeyboard();
//                tv2.setInputType(inputback);
//                return true;
//            }
//        });
    }
}
