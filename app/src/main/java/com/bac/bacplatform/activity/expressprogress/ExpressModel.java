package com.bac.bacplatform.activity.expressprogress;

import android.content.DialogInterface;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bac.bacplatform.adapter.ExpressProgressListViewAdapter;
import com.bac.bacplatform.bean.ExpressBean;
import com.bac.bacplatform.bean.ExpressUserInfoBean;
import com.bac.bacplatform.http.GrpcTask;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.bac.commonlib.utils.ui.UIUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 怎么快怎么来
 */
public class ExpressModel {
    private ExpressActivity activity;
    private List<ExpressBean> msgListData;
    private ExpressProgressListViewAdapter adapter;
    private String oid;
    ExpressModel(ExpressActivity activity,String oid) {
        this.activity = activity;
        this.oid = oid;
        msgListData = new ArrayList<>();
        adapter = new ExpressProgressListViewAdapter(msgListData,activity);
    }

    public ExpressProgressListViewAdapter getAdapter() {
        return adapter;
    }

    void refreshMsgList() {
        Method method = new Method();
        method.setMethodName("QUERY_EXPRESS_SITUATION");
        method.put("orderId",oid);
        new GrpcTask(activity, method, "查询中...", new TaskPostExecute() {
            @Override
            public void onPostExecute(Method result) {
                if(result.getErrorId() != 0) {
                    UIUtil.showWarnDialog(activity, "提示", result.getMsg(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            activity.finish();
                        }
                    }, "确定");
                    return;
                }
                if (result.getListMap().isEmpty()) {
                    Toast.makeText(activity, "未查询到相关结果", Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String,Object> exp = result.getListMap().get(0);

                Object userObj = exp.get("expUserInfo");
                ExpressUserInfoBean userInfoBean = null;
                if(userObj != null) {
                    userInfoBean = JSONObject.parseObject(userObj.toString(),ExpressUserInfoBean.class);
                    adapter.setHeadBean(userInfoBean);
                }

                Object obj = exp.get("list");
                if(obj == null){
                    return;
                }
                List<ExpressBean> beans = JSONArray.parseArray(obj.toString(),ExpressBean.class);
                for (int i = 0; i < beans.size() ; i++) {
                    beans.get(i).setIndex(i);
                }
                createBottomBeans(userInfoBean,beans);
                msgListData.addAll(beans);
                adapter.notifyDataSetChanged();
            }
        }).execute();
    }

    private void createBottomBeans(ExpressUserInfoBean userBean,List<ExpressBean> beans) {
        if(userBean == null)return;

        ExpressBean beanPost = new ExpressBean();
        beanPost.setIndex(beans.size());
        beanPost.setShowPoint(false);
        beanPost.setSituation("包裹正在揽件");
        beanPost.setTime_ymd(userBean.getCreate_time());
        beanPost.setStatus("已发货");
        beans.add(beanPost);

        ExpressBean beanCreateOrder = new ExpressBean();
        beanCreateOrder.setIndex(beans.size());
        beanCreateOrder.setShowPoint(false);
        beanCreateOrder.setSituation("商品已下单");
        beanCreateOrder.setTime_ymd(userBean.getCreate_time());
        beanCreateOrder.setStatus("已下单");
        beans.add(beanCreateOrder);

    }
}
