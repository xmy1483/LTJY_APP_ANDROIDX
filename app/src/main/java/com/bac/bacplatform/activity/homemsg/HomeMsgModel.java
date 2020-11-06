package com.bac.bacplatform.activity.homemsg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.adapter.HomeFragmentMsgListViewAdapter;
import com.bac.bacplatform.bean.MsgBean;
import com.bac.bacplatform.http.GrpcTask;
import com.bac.bacplatform.module.login.LoginActivity;
import com.bac.bacplatform.utils.ui.UIUtils;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.TaskPostExecute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeMsgModel {
    private HomeMsgActivity activity;
    private List<MsgBean> msgListData;
    private HomeFragmentMsgListViewAdapter adapter;

    HomeMsgModel(HomeMsgActivity activity) {
        this.activity = activity;
        msgListData = new ArrayList<>();
        adapter = new HomeFragmentMsgListViewAdapter(msgListData,activity);
    }

    public HomeFragmentMsgListViewAdapter getAdapter() {
        return adapter;
    }

    public List<MsgBean> getMsgListData() {
        return msgListData;
    }

    void refreshMsgList() {
        if (!PreferenceManager.getDefaultSharedPreferences(activity).contains("certificate")) {
            return;
        }
        Method method = new Method();
        method.setMethodName("QUERY_NOTIFICATION");
        method.put("login_phone", BacApplication.getLoginPhone());
        new GrpcTask(null, method, null, new TaskPostExecute() {
            @Override
            public void onPostExecute(Method result) {
                if(result.getErrorId() != 0) {
                    return;
                }
                msgListData.clear();
                for (Map<String,Object> map:result.getListMap()) {
                    String jsonStr = JSON.toJSONString(map);
                    MsgBean msgBean = JSONObject.parseObject(jsonStr,MsgBean.class);
                    msgListData.add(msgBean);
                }
                adapter.notifyDataSetChanged();
            }
        }).execute();
    }


    void allMsgMarkReaded() {
        AlertDialog.Builder builder=  new AlertDialog.Builder(activity);

        builder.setTitle("全部已读")
                .setMessage("是否全部标记为已读？")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Method m = new Method();
                        m.setMethodName("MARK_NOTIFICATION_READ");
                        m.put("mark_all","1");
                        m.put("login_phone",BacApplication.getLoginPhone());
                        new GrpcTask(null, m, null, new TaskPostExecute() {
                            @Override
                            public void onPostExecute(Method result) {
                                if(result.getErrorId() == 0) {
                                    refreshMsgList();
                                    Toast.makeText(activity, "已完成", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).execute();
                    }
                }).setNegativeButton("取消",null).show();
    }

}
