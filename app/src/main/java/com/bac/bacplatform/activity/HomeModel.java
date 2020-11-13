package com.bac.bacplatform.activity;

import android.content.Intent;
import android.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import com.alibaba.fastjson.JSON;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.adapter.HomeGridAdapter;
import com.bac.bacplatform.bean.FuncBean;
import com.bac.bacplatform.bean.JdBean;
import com.bac.bacplatform.bean.MsgBean;
import com.bac.bacplatform.http.GrpcTask;
import com.bac.bacplatform.module.main.view.HomeFragment;
import com.bac.bacplatform.old.module.hybrid.WebAdvActivity;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.TaskPostExecute;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HomeModel {
    public HomeModel(HomeFragment fragment) {
        this.fragment = fragment;
    }

    private HomeFragment fragment;
    private List<JdBean> msgListData = new ArrayList<>(); // 消息集合(现为京东卡集合)

    public void handleMsgListData() {
        if (!PreferenceManager.getDefaultSharedPreferences(fragment.activity).contains("certificate")) {
            return;
        }
        Method method = new Method();
        method.setMethodName("QUERY_NOTIFICATION");
        method.put("login_phone", BacApplication.getLoginPhone()); // fixme 这里换成真实的手机号

        new GrpcTask(null, method, null, new TaskPostExecute() {
            @Override
            public void onPostExecute(Method result) {
                if(result.getErrorId() != 0) {
                    return;
                }

                boolean isHaveUnRead = false;
                Map<String,Object> temp = null;
                for(Map<String,Object> item:result.getListMap()) {
                    Object rs = item.get("read_status");
                    if(rs!=null && rs.toString().equals("0")) {
                        temp = item;
                        isHaveUnRead = true;
                        break;
                    }
                }
                if(isHaveUnRead) {
                    fragment.setHornMsg(temp);
                    fragment.setRightTopRedDotVisibility(View.VISIBLE);
                } else {
                    fragment.setHornMsg("暂无消息");
                    fragment.setRightTopRedDotVisibility(View.INVISIBLE);
                }
//                到此 最新消息已经处理好
//                下面的是消息列表，但是又被去掉了，但是不保证不会被恢复，所以不删除，
         /*       msgListData.clear();
                List<Map<String,Object>> retList;
                if(result.getListMap().size()>6){
                    retList = result.getListMap().subList(0,6);
                } else {
                    retList = new ArrayList<>(result.getListMap());
                }
                msgListData.clear();
                for (Map<String,Object> map:retList) {
                    String jsonStr = JSON.toJSONString(map);
                    MsgBean msgBean = JSONObject.parseObject(jsonStr,MsgBean.class);
                    msgListData.add(msgBean);
                }
                fragment.jdCardListAdapter.notifyDataSetChanged();*/
            }
        }).execute();
    }

    public void getJdCardList() {
        final Method method = new Method();
        method.setMethodName("GIFT_XML.QUERY_GIFT_CARD");
        method.put("login_phone",BacApplication.getLoginPhone());
//        method.put("customers_id",LoginFragment2.getCustomers_id());
        new GrpcTask(null, method, null, new TaskPostExecute() {
            @Override
            public void onPostExecute(Method result) {
//                Log.d("XmyLog1212", "onPostExecute: "+JSON.toJSONString(result));
                if(result.getErrorId() != 0){
                    return;
                }
                List<Map<String,Object>> cards = result.getListMap();
                msgListData.clear();
                for (Map<String,Object> card : cards) {
                    JdBean jd = new JdBean();
                    jd.setOriginPrice(card.get("gift_value"));
                    jd.setSalePrice(card.get("pay_money"));
                    jd.setImgUrl(card.get("gift_image"));
                    String name = "【"+card.get("supplier_name")+"】"+card.get("gift_name");
                    jd.setName(name);
                    if(msgListData.size()<4) {
                        msgListData.add(jd);
                    }
                }
                Log.d("XmyLog1111", "onPostExecute: "+msgListData.size());
                fragment.jdCardListAdapter.notifyDataSetChanged();
            }
        }).execute();
    }

    public List<JdBean> getMsgListData() {
//        for (int i = 1; i <= 4; i++) {
//            JdBean b = new JdBean();
//            b.setId("i"+i);
//            b.setName("【京东超市】京东3000远现金卡，不可干洗不可水洗");
//            b.setOriginPrice((3000*i)+"");
//            String p = (1000*i)+"";
//            b.setSalePrice(p);
//            msgListData.add(b);
//        }
        return msgListData;
    }

    public void turnToWebView(MsgBean bean) {
        Intent it = new Intent(fragment.activity, WebAdvActivity.class)
                .putExtra("title", bean.getType_name())
                .putExtra("ads_url", bean.getTarget_url());
        fragment.activity.startActivity(it);
    }

    public void markMsgRead(final MsgBean bean) {
        if(bean.getRead_status().equals("1")){
            return;
        }
        Method m = new Method();
        m.setMethodName("MARK_NOTIFICATION_READ");
        m.put("id",bean.getId());
        m.put("login_phone",BacApplication.getLoginPhone());
        new GrpcTask(null, m, null, new TaskPostExecute() {
            @Override
            public void onPostExecute(Method result) {
                if(result.getErrorId() == 0) {
                    bean.setRead_status("1");
                }
            }
        }).execute();
    }

    //    =============================================================
    private HomeGridAdapter homeGridAdapter;
    private List<FuncBean> funcBeans = new ArrayList<>();
    private void initHomeGridView() {
        if(homeGridAdapter == null) {
            GridLayoutManager glm = new GridLayoutManager(fragment.activity,5);
            homeGridAdapter = new HomeGridAdapter(funcBeans,fragment);
            fragment.funcGrid.setLayoutManager(glm);
            fragment.funcGrid.setAdapter(homeGridAdapter);
        }
        homeGridAdapter.notifyDataSetChanged();
    }

    public void initFuncData(boolean isBlackList) {
        funcBeans.clear();
        funcBeans.add(new FuncBean("油卡充值", R.mipmap.ic_zsh_recharge));
        if(!isBlackList) {
            funcBeans.add(new FuncBean("实体油卡", R.mipmap.ic_zsh_985));
        }
        funcBeans.add(new FuncBean("话费购油", R.mipmap.ic_hua_fei_oil));
        funcBeans.add(new FuncBean("手机充值", R.mipmap.ic_phone_recharge));
        funcBeans.add(new FuncBean("京东E卡", R.mipmap.ic_jd_2));
        if(!isBlackList) {
            funcBeans.add(new FuncBean("快递查询", R.mipmap.ic_express));
        }
        funcBeans.add(new FuncBean("联系我们", R.mipmap.ic_service));
        funcBeans.add(new FuncBean("常见问题", R.mipmap.ic_question));
        initHomeGridView();
    }

}



















