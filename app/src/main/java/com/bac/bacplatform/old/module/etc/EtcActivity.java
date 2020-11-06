package com.bac.bacplatform.old.module.etc;//package com.bac.bacplatform.old.module.etc;
//
//import android.Manifest;
//import android.bluetooth.BluetoothAdapter;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.View;
//import android.widget.Toast;
//
//import com.bac.bacplatform.BacApplication;
//import com.bac.bacplatform.R;
//import com.taobao.weex.IWXRenderListener;
//import com.taobao.weex.WXSDKInstance;
//import com.taobao.weex.common.WXRenderStrategy;
//import com.taobao.weex.utils.WXFileUtils;
//
//import etc.obu.data.ServiceStatus;
//import rx.Observable;
//import rx.Subscriber;
//import rx.android.schedulers.AndroidSchedulers;
//import rx.functions.Action1;
//import rx.schedulers.Schedulers;
//
//public class EtcActivity extends AppCompatActivity implements IWXRenderListener {
//    private final String TAG = this.getClass().getSimpleName();
//    private WXSDKInstance mWXSDKInstance;
//    BluetoothAdapter mBtAdapter;
//    private final int PERMISSION = 0;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.etc);
//        //init();
//        //connectDevice();
//        mWXSDKInstance=new WXSDKInstance(this);
//        mWXSDKInstance.registerRenderListener(this);
//        //获取服务器端 首页地址
//       //mWXSDKInstance.renderByUrl("WXETC","https://app5.bac365.com:10443/app.pay/photo/weex/ETC/dist/HomePage.js",null, null, -1, -1, WXRenderStrategy.APPEND_ASYNC);
//        mWXSDKInstance.render("WeexDemo", WXFileUtils.loadAsset("ETCRecharge.js",this),null,null,-1,-1, WXRenderStrategy.APPEND_ASYNC);
//        //init();
//    }
//
//    /**
//     * 判断蓝牙是否开启
//     */
//    private void init() {
//        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
//        if (mBtAdapter == null) {
//            Toast.makeText(this, "蓝牙不可用",
//                    Toast.LENGTH_LONG).show();
//            return;
//        }
//        if (!mBtAdapter.isEnabled()) {
//            Log.i(TAG, "蓝牙尚未开启");
//            Intent enableIntent = new Intent(
//                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
//            startActivityForResult(enableIntent, 0x1);
//            //startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
//        }
//
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//            // 没有权限，申请权限
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION);
//        }
//    }
//    @Override
//    public void onViewCreated(WXSDKInstance instance, View view) {
//        setContentView(view);
//    }
//
//    @Override
//    public void onRenderSuccess(WXSDKInstance instance, int width, int height) {
//
//    }
//
//    @Override
//    public void onRefreshSuccess(WXSDKInstance instance, int width, int height) {
//
//    }
//
//    @Override
//    public void onException(WXSDKInstance instance, String errCode, String msg) {
//
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mWXSDKInstance!=null){
//            mWXSDKInstance.onActivityResume();
//        }
//    }
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mWXSDKInstance!=null){
//            mWXSDKInstance.onActivityPause();
//        }
//    }
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mWXSDKInstance!=null)
//            mWXSDKInstance.onActivityStop();
//    }
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mWXSDKInstance!=null)
//            mWXSDKInstance.onActivityDestroy();
//    }
//}
