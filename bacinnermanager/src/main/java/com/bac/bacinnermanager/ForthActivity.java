package com.bac.bacinnermanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by guke on 2017/8/16.
 */

public class ForthActivity extends AppCompatActivity {
    private TextView textView;
    private Button btn;
    private List<String> method;
    private List<String> method_name;
    private List<Map<String,Object>> text;

    private int length;
    private int i;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forth);
//        method_name = SecondActivity.getData();
//        method = SecondActivity.getData3();
//        i = SecondActivity.getPosition1();
//        length = method_name.size();
//
//
//
//        BacHttpBean bacHttpBean = new BacHttpBean().setMethodName(method.get(i))
//                .put("method_id", data2.get(position)
//                );
//
//
//
//        HttpHelperLib.getInstance()
//                .net(bacHttpBean, null, null)
//                .observeOn(RxScheduler.RxPoolScheduler())
//                .map(new Func1<String, List<Map<String,Object>>>() {
//                    @Override
//                    public List<Map<String,Object>> call(String s) {
//                        return JSON.parseObject(s, new TypeReference<List<Map<String,Object>>>() {
//                        }.getType());
//                    }
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<List<Map<String,Object>>>() {
//                    @Override
//                    public void call(List<Map<String,Object>> maps) {
//
//                        startActivity(new Intent(SecondActivity.this, ThirdActivity.class).putExtra("Bean", (Serializable) been));
//                    }
//                });
//        ;

//        text = ThirdActivity.getFourth();
//        String mytext = text.toString();
        textView = findViewById(R.id.text_forth);
        //传入接口详细内容所有数据
        ArrayList<String> data = getIntent().getStringArrayListExtra("data");
        textView.setText(JSON.toJSONString( JSON.parseObject( getIntent().getStringExtra("json"),new TypeReference<List<Map<String,Object>>>(){}.getType()),true));
        //button设置返回接口列表
        btn = findViewById(R.id.btn);
        btn.setText("返回接口列表");
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForthActivity.this, SecondActivity.class));
            }
        });
    }
}
