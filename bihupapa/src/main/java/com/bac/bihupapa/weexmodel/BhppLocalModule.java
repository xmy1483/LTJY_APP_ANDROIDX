package com.bac.bihupapa.weexmodel;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.PowerManager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.bac.bihupapa.bean.Method;
import com.bac.bihupapa.grpc.GrpcTask;
import com.bac.bihupapa.grpc.TaskPostExecute;
import com.bac.commonlib.services.LocationService;
import com.bac.rxbaclib.rx.permission.RxPermissionImpl;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.taobao.weex.annotation.JSMethod;
import com.taobao.weex.bridge.JSCallback;
import com.taobao.weex.common.WXModule;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;

import rx.Observable;
import rx.functions.Action1;

import static android.content.Context.SENSOR_SERVICE;
import static android.widget.Toast.makeText;

/**
 * Created by wujiazhen on 2017/8/31.
 * <p>
 * gsp
 */

public class BhppLocalModule extends WXModule {

    private LocationService locationService;
    private LocationClientOption mOption;

    private Map<String, Object> map1;

    private LinkedBlockingQueue<BDLocation> queue = new LinkedBlockingQueue<>();

    private BDLocation sendBDLocation;


    @Override
    public void onActivityDestroy() {
        super.onActivityDestroy();
        releaseLoc();
    }

    private void releaseLoc() {
        System.out.println("=====================================定位结束");
        if (locationService != null) {
            locationService.unregisterListener(mListener); //注销掉监听
            locationService.stop(); //停止定位服务
        }
        mSensorManager.unregisterListener(mSensorEventListener);
    }

    private int tempStep;
    private SensorEventListener mSensorEventListener = new SensorEventListener() {

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            tempStep = (int) event.values[0];
        }
    };


    //定位模式  1为GPS定位 2为网络定位
    private boolean locationType = false;
    private int index;
    private SensorManager mSensorManager;
    /**
     * 定位结果回调，重写onReceiveLocation方法，
     */
    private BDLocationListener mListener = new BDLocationListener() {
        @Override
        public void onReceiveLocation(BDLocation location) {
            mSensorManager = (SensorManager) mWXSDKInstance.getContext().getSystemService(SENSOR_SERVICE);
            Sensor countSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            mSensorManager.registerListener(mSensorEventListener, countSensor,
                    SensorManager.SENSOR_DELAY_NORMAL);
            System.out.println("=======================================================定位返回码："+location.getLocType());
            // GPS定位结果
            locationType = location.getLocType() == BDLocation.TypeGpsLocation;

//
//            makeText(mWXSDKInstance.getContext(),
//                    "速度:"+((int)(location.getSpeed()+0.5))
//                            +" 卫星:"+location.getSatelliteNumber()+"\n"
//                            +location.getCountry()+"\n"
//                            +location.getAddrStr()+"\n"
//                            +location.getStreet()+"\n"
//                            +location.getCity()+"\n"
//                            +"Radius:"+location.getRadius()
//                    , Toast.LENGTH_SHORT).show();




            if (sendBDLocation == null) {
                sendBDLocation=location;
            }
            if(queue.size()==0) {
                sendBDLocation=location;
            }else{
                queue.add(location);
            }
            System.out.println("======================================================继续监听定位");
            ADD_USER_POSITION(sendBDLocation);

        }
    };
    private class GpsDataTask implements TaskPostExecute {
        @Override
        public void onPostExecute(Method result) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("data", result);
            localCallback.callback(hashMap);

            System.out.println("======================================================上传数据回调");
            if(result.getListMap().size()>0) {
//弹框显示定位信息
                Toast toast = Toast.makeText(mWXSDKInstance.getContext(),
                        JSON.toJSONString(result.getListMap())
                        , Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                //         toast.show();
                sendBDLocation= queue.poll();
                if (sendBDLocation != null) {
                    ADD_USER_POSITION(sendBDLocation);
                }
            }else{
                if (queue.size() == 0) {
                    queue.add(sendBDLocation);
                }
            }

        }


    }

    private void ADD_USER_POSITION(BDLocation location) {
        try{
            Method bacHttpBean=new Method();
            bacHttpBean.setMethodName("ADD_USER_POSITION");
            HashMap<String, Object> hashMap = new HashMap<>();
            //创建时间
            hashMap.put("create_time", location.getTime());
            //记录id
            hashMap.put("record_id", map1.get("record_id"));
            //广告活动id
            hashMap.put("adv_activity_id", map1.get("adv_activity_id"));
            //广告每期任务id
            hashMap.put("adv_task_id", map1.get("adv_task_id"));
            //用户id
            hashMap.put("customers_id", map1.get("customers_id"));
            //登录手机号
            hashMap.put("login_phone", map1.get("login_phone") + "");
            //车辆id
            hashMap.put("car_id", map1.get("car_id"));
            //车牌号
            hashMap.put("car_license_no", map1.get("car_license_no"));
            //是否有GPS状态
            hashMap.put("is_gps", locationType);
            //国家
            hashMap.put("country",location.getCountry());
            //省
            hashMap.put("province",location.getProvince());
            //市
            hashMap.put("city",location.getCity());
            //区
            hashMap.put("district",location.getDistrict());
            //街道
            hashMap.put("street",location.getStreet());
            //卫星个数
            hashMap.put("satellite_num", location.getSatelliteNumber());
            //步数
            hashMap.put("steps", Integer.valueOf(tempStep));
            //经度
            hashMap.put("longitude", (int) (location.getLongitude() * Math.pow(10, 6)));
            //纬度
            hashMap.put("latitude", (int) (location.getLatitude() * Math.pow(10, 6)));
            //速度
            hashMap.put("speed", Integer.valueOf(String.valueOf(location.getSpeed() * Math.pow(10, 4)).substring(0, String.valueOf(location.getSpeed() * Math.pow(10, 4)).lastIndexOf("."))));
            String direction = "";
            direction = String.valueOf(location.getDirection() * Math.pow(10, 4));
            if (direction.length() >= 9) {
                direction = direction.substring(0, 9);
            }
            //方向
            direction = direction.substring(0, direction.indexOf("."));
            hashMap.put("direction", Integer.valueOf(direction));
            //海拔
            hashMap.put("altitude", (int) location.getAltitude() * Math.pow(10, 4));
            //是否能成功读取到gps信息
            hashMap.put("is_succ", location.getLocType() == BDLocation.TypeGpsLocation);
            bacHttpBean.addMap(hashMap);
//            Log.d("这个值是", String.valueOf(location.getLocType() == BDLocation.TypeGpsLocation));
            for(HashMap.Entry<String, Object> entry: hashMap.entrySet())
            {
                System.out.println( entry.getKey()+ "================================================ "+entry.getValue());
            }
            new GrpcTask(null,bacHttpBean,null,new GpsDataTask()).execute();
        } catch (Exception e) {
            makeText(mWXSDKInstance.getContext(),
                    "ERROR:"+e.getMessage()
                    , Toast.LENGTH_LONG).show();
            Observable.error(e);
        }

    }

    /***
     *
     * @return BphhLocationClientOptionq
     */
    public LocationClientOption getBphhLocationClientOption() {
        if (mOption == null) {
            mOption = new LocationClientOption();
            mOption.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
            mOption.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系，如果配合百度地图使用，建议设置为bd09ll;
            mOption.setScanSpan(5000);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于5000ms才是有效的
            mOption.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
            mOption.setIsNeedLocationDescribe(true);//可选，设置是否需要地址描述
            mOption.setNeedDeviceDirect(true);//可选，设置是否需要设备方向结果
            mOption.setLocationNotify(false);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
            mOption.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
            mOption.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
            mOption.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
            mOption.SetIgnoreCacheException(true);//可选，默认false，设置是否收集CRASH信息，默认收集
        }
        return mOption;
    }
    private PowerManager pm;
    private PowerManager.WakeLock wakeLock;
    @JSMethod
    public void local(final Map<String, Object> map, final JSCallback jsCallback) {
        final Context context = mWXSDKInstance.getContext();

        // 申请权限

        Observable.just("")
                .compose(new RxPermissionImpl((AppCompatActivity) context).ensure( Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_WIFI_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE))
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                         /*   //防止手机进入休眠状态
                            pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
                            //保持cpu一直运行，不管屏幕是否黑屏
                            wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "CPUKeepRunning");
                            wakeLock.acquire();*/
                            System.out.println("=============================================第一次定位");
                            // 百度定位
                            locationService = new LocationService(context);
                            locationService.setLocationOption(getBphhLocationClientOption());
                            locationService.registerListener(mListener);
                            locationService.start();// 定位SDK
                        }
                    }
                });

        map1 = map;

        localCallback = new LocalCallback() {
            @Override
            public void callback(Map<String,Object> s) {
                for(Map.Entry<String, Object> entry: s.entrySet())
                {
                    System.out.println( entry.getKey()+ "==========返回============ "+entry.getValue());
                }
                jsCallback.invokeAndKeepAlive(s);
            }
        };

    }


    @JSMethod
    public void stopLoca() {
        releaseLoc();
        //OKDriveSDK.getInstance(mWXSDKInstance.getContext().getApplicationContext()).endDriving();
    }


    private LocalCallback localCallback;

    public interface LocalCallback{
        void callback(Map<String,Object> s);
    }

    private void showDialog(String msg) {
        new AlertDialog.Builder(mWXSDKInstance.getContext()).setTitle("温馨提示")
                .setMessage("定位失败！")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}