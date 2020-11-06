package com.bac.bacplatform.tst;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.bac.bacplatform.R;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.wjz.weexlib.weex.activity.WeexActivity3;

/**
 * Created by wujiazhen on 2017/7/25.
 */

public class WeexUrlTestActivity extends AppCompatActivity {

    private EditText et;
    private Button btn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.weex_url_test_activity);

        et = (EditText) findViewById(R.id.et);
        et.setSelection(et.getText().length());
        btn = (Button) findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // weex
                UIUtils.startActivityInAnim(WeexUrlTestActivity.this,
                        new Intent(WeexUrlTestActivity.this, WeexActivity3.class)
                                .setData(Uri.parse(et.getText().toString().trim())
                        ));
            }
        });

    }


}
