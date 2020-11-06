package com.bac.bacplatform.module.main.view;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.recharge.ObuInterface;
import com.bac.bacplatform.BacApplication;
import com.bac.bacplatform.R;
import com.bac.bacplatform.old.base.SuperActivity;

import etc.obu.data.ServiceStatus;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class jinYi extends SuperActivity {
   // private BluetoothAdapter bluetoothAdapter;

    private final String TAG = this.getClass().getSimpleName();

    private static final int REQUEST_ENABLE_BT = 1;
    private final int PERMISSION = 0;
    BluetoothAdapter mBtAdapter;

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    final ObuInterface obuInterface = new ObuInterface(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.home_more_activity);
        setContentView(R.layout.activity_jin_yi);

        //  rvExchange = (RecyclerView) findViewById(R.id.rv_exchange);
        initToolBar("etc");

        init();




        Button btn2 = (Button) findViewById(R.id.btn_search_jinyi);

        btn2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                connectDevice();

            }
        });


    }

    private void init() {
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBtAdapter == null) {
            Toast.makeText(this, "Bluetooth is not available",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (!mBtAdapter.isEnabled()) {
            Log.i(TAG, "onResume - BT not enabled yet");
            Intent enableIntent = new Intent(
                    BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，申请权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION);
        }
    }



    /**
     * 连接设备
     */
    private void connectDevice() {
        Observable.create(new Observable.OnSubscribe<ServiceStatus>() {
            @Override
            public void call(Subscriber<? super ServiceStatus> subscriber) {
                subscriber.onNext(BacApplication.obuInterface.connectDevice());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<ServiceStatus>() {
            @Override
            public void call(ServiceStatus serviceStatus) {
                String str;
                if (serviceStatus.getServiceCode() == 0) {
                    str = "连接成功！";
//                    btnConnect.setText("断开连接");
//                    btnReadCard.setEnabled(true);
                } else {
                    str = "连接失败！";
                }
                Log.e(TAG, "status:" + serviceStatus.getServiceCode());
                Log.e(TAG, "message:" + serviceStatus.getMessage());
                Log.e(TAG, "message:" + serviceStatus.getServiceInfo());
//                tvResult.setText(str);

                System.out.println("str ........" + str);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i].toString() + " 权限打开失败!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Toast.makeText(this, " 权限打开成功!", Toast.LENGTH_LONG).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "Bluetooth has turned on ",
                        Toast.LENGTH_SHORT).show();
            } else {
                Log.d(TAG, "BT not enabled");
                Toast.makeText(this, "Problem in BT Turning ON ",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
