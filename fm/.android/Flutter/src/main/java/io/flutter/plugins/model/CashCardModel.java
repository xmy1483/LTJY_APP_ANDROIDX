package io.flutter.plugins.model;

import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.flutter.plugins.ContextHolder;

public class CashCardModel {

    public static String getCashCardListData() {
        JSONObject json = new JSONObject();
        JSONArray cashCardArray= new JSONArray();
        JSONArray fuChongCardArray= new JSONArray();

        for (int i = 1; i < 5; i++) {
            JSONObject card = new JSONObject();
            card.put("cardName","中石化"+(i*1000)+"元加油卡");
            card.put("originPrice",i*1000+"");
            card.put("salePrice",i*1000-16+"");
            card.put("cardImg","http://192.168.0.98:9999/a.png");
            cashCardArray.add(card);
        }
        json.put("cashCardList",cashCardArray);

        for (int i = 1; i < 12; i++) {
            JSONObject card = new JSONObject();
            card.put("cardName","中石化"+(i*1000)+"元复充卡");
            card.put("originPrice",i*1000+"");
            card.put("salePrice",i*1000-16+"");
            card.put("cardImg","http://192.168.0.98:9999/a.png");
            fuChongCardArray.add(card);
        }
        json.put("fuChongCardList",fuChongCardArray);

        json.put("errorId",0);
        Log.d("xmylog", "getCashCardListData: "+json.toJSONString());
        return json.toJSONString();
    }


    public static String getTestStr() {
        return abc();
    }



    public static String abc() {
        JSONObject j = new JSONObject();
        JSONObject data = new JSONObject();
        j.put("data",data);
        j.put("person","coder");
        data.put("name","xmy");
        data.put("age","23");
        System.out.println("j.toJSONString() = " + j.toJSONString());
        return j.toJSONString();
    }

    public void toast(String msg) {
        if(!TextUtils.isEmpty(msg)){
            Toast.makeText(ContextHolder.getHolder().getContext(), msg, Toast.LENGTH_SHORT).show();
        }
    }
}
