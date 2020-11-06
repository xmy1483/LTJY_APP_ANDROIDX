package com.bac.bacplatform.weex;

import androidx.appcompat.app.AlertDialog;

import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.http.HttpHelper;
import com.bac.commonlib.domain.BacHttpBean;
import com.bac.commonlib.utils.fun.JsonFunc1;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by wujiazhen on 2017/7/24.
 */

public class VoucherModel extends WXModule {

    @JSMethod
    public void info(Map<String, Object> map, final JSCallback jsCallback) {

        if (map != null && map.size() > 0) {

            //请求数据
            ArrayList<Integer> intParam = new ArrayList<>();
            intParam.add(0);
            intParam.add(1);
            intParam.add(2);

            ArrayList<Integer> intParam2 = new ArrayList<>();
            intParam2.add(0);

            //查询券
            HttpHelper.getInstance().bacNet(new BacHttpBean()
                    .setActionType(0)
                    .setMethodName("QUERY_VOUCHER")
                    .put("login_phone", BacApplication.getLoginPhone())
                    .put("status", intParam2)
                    .put("voucher_type", intParam)
            )
                    .compose(((AutomaticRxAppCompatActivity)mWXSDKInstance.getContext()).<String>bindUntilEvent(ActivityEvent.DESTROY))
                    .compose(new RxDialog<String>().rxDialog(mWXSDKInstance.getContext()))
                    .observeOn(RxScheduler.RxPoolScheduler())
                    .map(new JsonFunc1<String, List<Map<String, Object>>>())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Action1<List<Map<String, Object>>>() {
                        @Override
                        public void call(List<Map<String, Object>> maps) {
                            if (maps != null) {
                                if (maps.size() > 0) {

                                    HashMap<String, Object> hashMap = new HashMap<>();
                                    hashMap.put("data", maps);
                                    jsCallback.invokeAndKeepAlive(hashMap);

                                } else {
                                    new AlertDialog.Builder(mWXSDKInstance.getContext())
                                            .setTitle("提示")
                                            .setMessage("没有可激活券")
                                            .setPositiveButton("确定", null)
                                            .setNegativeButton("取消", null)
                                            .show();
                                }
                            }
                        }
                    });

        }

    }



}
