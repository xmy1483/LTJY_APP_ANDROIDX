package com.bac.bacplatform.tst;


import android.Manifest;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bac.bacplatform.R;
import com.bac.bacplatform.utils.logger.LogUtil;
import com.bac.commonlib.services.LocationService;
import com.bac.rxbaclib.rx.permission.RxPermissionImpl;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by Wjz on 2016/7/20.
 *
 * 百度地图
 */
public class BaiduLocationActivity extends AppCompatActivity {
    private LocationService locationService;
    private TextView LocationResult;
    private Button startLocation;
    private LocationManager lm;//【位置管理】

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_location_activity);
        LocationResult = (TextView) findViewById(R.id.textView1);
        LocationResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        startLocation = (Button) findViewById(R.id.addfence);

        //得到系统的位置服务，判断GPS是否激活
        lm = (LocationManager) getSystemService(LOCATION_SERVICE);
        boolean ok = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (ok) {

            Toast.makeText(this, "GPS已经开启", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "系统检测到未开启GPS定位服务", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(intent);
        }

/*
        SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                null,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.CONTACT_STATUS},
                new int[]{android.R.id.text1, android.R.id.text2},
                0);
        simpleCursorAdapter.swapCursor();*/


        /* 微信分享*/
        //int mTargetScene = SendMessageToWX.Req.WXSceneSession;
        /*int mTargetScene = SendMessageToWX.Req.WXSceneTimeline;

        String text = "哈哈";

        WXTextObject textObj = new WXTextObject();
        textObj.text = text;


        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;

        msg.description = text;


        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = mTargetScene;

        BacApplication.getmWXApi().sendReq(req);*/


    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }


    /**
     * 显示请求字符串
     *
     * @param str
     */
    public void logMsg(String str) {
        try {
            if (LocationResult != null)
                LocationResult.setText(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /***
     * Stop location service
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        locationService.unregisterListener(mListener); //注销掉监听
        locationService.stop(); //停止定位服务
        super.onStop();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();

        // 定位权限
        Observable.just("")
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {

                    }
                })
                .compose(new RxPermissionImpl(this).ensure(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION))
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean b) {



                        if (b) {
                            //// -----------location config ------------
                            locationService = new LocationService(BaiduLocationActivity.this);
                            ////获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
                            locationService.registerListener(mListener);
                            //注册监听
                            int type = getIntent().getIntExtra("from", 0);
                            if (type == 0) {
                                locationService.setLocationOption(locationService.getDefaultLocationClientOption());
                            } else if (type == 1) {
                                locationService.setLocationOption(locationService.getOption());
                            }
                        }
                    }
                });

        startLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (startLocation.getText().toString().equals("开始定位")) {
                    locationService.start();// 定位SDK
                    // start之后会默认发起一次定位请求，开发者无须判断isstart并主动调用request
                    startLocation.setText("停止定位");
                } else {
                    locationService.stop();
                    startLocation.setText("开始定位");
                }
            }
        });
    }

int count = 0;
    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                final StringBuffer sb = new StringBuffer(256);
                sb.append("定位次数 ："+(++count));
                sb.append("\ntime : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType 定位类型: ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description 定位类型说明: ");// *****对应的定位类型说明*****
                sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude 纬度: ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude 经度: ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius 半径: ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode 国家码: ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry 国家名称: ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode 城市编码: ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity 城市: ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict 区: ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet 街道: ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr 地址信息: ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: 用户室内外判断结果");// *****返回用户室内外判断结果*****
                sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value) 方向: ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe 位置语义化信息: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: POI信息");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\n-----GPS定位结果--------");
                    sb.append("\nspeed 速度 单位：km/h: ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite 卫星数目: ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight 海拔高度 单位：米: ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status gps质量判断: ");
                    sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    sb.append("\n-----网络定位结果--------");
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight 海拔高度: ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers 运营商信息: ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\n-----离线定位结果--------");
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        logMsg(sb.toString());
                    }
                });


                LogUtil.sf(BaiduLocationActivity.this, sb.toString());
            }
        }


    };
}

