package com.bac.bacinnermanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.bac.bacinnermanager.adapter.Lvadapter;
import com.bac.bacinnermanager.utils.DataBean;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.http.HttpHelperLib;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by guke on 2017/8/15.
 */

public class ThirdActivity extends AppCompatActivity {
    private ListView listView;
    private Button btn;
    private List<String> method;
    private int i;
    private EditText editText;
    private String endtext;
    private Lvadapter adapter;
    private ArrayList<DataBean> mList;

    public static List<Map<String, Object>> getFourth() {
        return fourth;
    }

    private static List<Map<String, Object>> fourth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        method = SecondActivity.getData3();
        i = SecondActivity.getPosition1();
        //获取SecondActivity传过来的Bean数据
        mList = (ArrayList<DataBean>) getIntent().getSerializableExtra("Bean");
        // ArrayList<String> data = getIntent().getStringArrayListExtra("data");
        btn = findViewById(R.id.btn);
        btn.setText("完成");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //
                BacHttpBean bacHttpBean = new BacHttpBean().setMethodName(method.get(i));

                for (DataBean dataBean : mList) {


                    bacHttpBean.put(dataBean.getParam_key(), dataBean.getParam_value());
                }
                HttpHelperLib.getInstance()
                        .net(bacHttpBean, null, null,null,null)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                // Toast.makeText(getBaseContext(),"323",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ThirdActivity.this, ForthActivity.class).putExtra("json",s));
                            }
                        });


            }
        });


        //设置listview，传入数据
        listView = findViewById(R.id.list_view);
        adapter = new Lvadapter(this, mList);
        listView.setAdapter(adapter);

       /*
        editText = (EditText) findViewById(R.id.text_3);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int i = count;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                endtext = editText.getText().toString();

            }
        });
        */





    }

}
