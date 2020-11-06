package com.bac.bacplatform.activity.expressorder;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.adapter.ExpressOrderListAdapter;
import com.bac.bacplatform.bean.ExpressOrderBean;
import com.bac.bacplatform.http.GrpcTask;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.bac.commonlib.utils.ui.UIUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderRecordModel {
    private OrderRecordActivity activity;
    private List<ExpressOrderBean> msgListData;
    private ExpressOrderListAdapter adapter;

    OrderRecordModel(OrderRecordActivity activity) {
        this.activity = activity;
        msgListData = new ArrayList<>();
        adapter = new ExpressOrderListAdapter(msgListData,activity);
    }

    public ExpressOrderListAdapter getAdapter() {
        return adapter;
    }

     void refreshMsgList() {
         Method method = new Method();
         method.setMethodName("QUERY_USER_ORDER");
         method.put("login_phone", BacApplication.getLoginPhone());
         new GrpcTask(null, method, null, new TaskPostExecute() {
             @Override
             public void onPostExecute(Method result) {
                 if (result.getErrorId()!=0) {
                     UIUtil.showWarnDialog(activity,"提示",result.getMsg(),null,null);
                     return;
                 }
                 for (Map<String,Object> map:result.getListMap()) {
                    ExpressOrderBean bean = new ExpressOrderBean();
                    bean.setPost_no(map.get("post_no"));
                    bean.setHadPost(map.get("hadPost"));
                    bean.setCnums(map.get("cnums"));
                    bean.setOut_trade_no(map.get("out_trade_no"));
                    bean.setCreate_time(map.get("create_time"));
                    bean.setType_name(map.get("type_name"));
                    msgListData.add(bean);
                 }
                adapter.notifyDataSetChanged();
             }
         }).execute();
    }
}




















