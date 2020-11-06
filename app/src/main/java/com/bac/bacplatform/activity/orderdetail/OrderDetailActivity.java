package com.bac.bacplatform.activity.orderdetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bac.bacplatform.R;
import com.bac.bacplatform.activity.base.SimpleBaseActivity;
import com.bac.bacplatform.http.GrpcTask;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.bac.commonlib.utils.ui.UIUtil;

import java.util.Map;

public class OrderDetailActivity extends SimpleBaseActivity {
    @Override
    public int setLayoutId() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void bindView() {

    }

    @Override
    public void initDataAfterBindView() {
        initParam();
    }

    private void initForm(Map<String,Object> json) {
        Object objOid = json.get("out_trade_no");
        if(objOid!=null) {
            ((TextView) findViewById(R.id.txt_order_id)).setText(objOid.toString());
        }

        Object objStatus = json.get("order_status");
        if(objStatus!=null) {
            ((TextView) findViewById(R.id.txt_order_status)).setText(objStatus.toString());
        }

        Object objTime = json.get("create_time");
        if(objTime!=null) {
            ((TextView) findViewById(R.id.txt_order_time)).setText(objTime.toString());
        }

        Object objName = json.get("type_name");
        if(objName != null) {
            ((TextView) findViewById(R.id.txt_order_name)).setText(objName.toString());
        }

        Object objDeno = json.get("denomination");
        if(objDeno != null) {
            String text = objDeno.toString()+"元";
            ((TextView) findViewById(R.id.txt_money)).setText(text);
        }

        Object objSale = json.get("sale_price");
        if (objSale != null) {
            String text = objSale.toString()+"元";
            ((TextView) findViewById(R.id.txt_price)).setText(text);
        }

        Object objNum = json.get("cnums");
        if (objNum!=null) {
            String msg = objNum.toString();
            if(msg.contains(",")){
                msg = msg.substring(0,msg.lastIndexOf(","));
            }
            msg += "张";
            ((TextView) findViewById(R.id.txt_count)).setText(msg);
        }

        Object objPay = json.get("pay_money");
        if(objPay!=null) {
            String text = objPay.toString()+"元";
            ((TextView) findViewById(R.id.txt_pay_money)).setText(text);
        }

//        ((TextView) findViewById(R.id.txt_company)).setText(json.get("out_trade_no").toString());
        Object postNo = json.get("post_no");
        if(postNo!=null) {
            ((TextView) findViewById(R.id.txt_exp_id)).setText(postNo.toString());
        }
    }

    private void initParam() {
        Method method = new Method();
        method.setMethodName("QUERY_CASHCARD_ORDER_INFO");
        method.put("orderId","1810251709369555268");
        new GrpcTask(null, method, null, new TaskPostExecute() {
            @Override
            public void onPostExecute(Method result) {
                String s = JSON.toJSONString(result);
                if(result.getErrorId() != 0) {
                    UIUtil.showWarnDialog(OrderDetailActivity.this, "提示", result.getMsg(), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            onBackPressed();
                        }
                    }, "确定");
                    return;
                }
                if(!result.getListMap().isEmpty()) {
                    initForm(result.getListMap().get(0));
                } else {
                    UIUtil.showWarnDialog(OrderDetailActivity.this,"提示","未查询到相关信息",null,null);
                }
            }
        }).execute();
        Intent it = getIntent();
        // todo 参数
    }
}
