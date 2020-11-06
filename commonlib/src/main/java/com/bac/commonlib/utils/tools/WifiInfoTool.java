package com.bac.commonlib.utils.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

/**
 * Created by wujiazhen on 2017/7/10.
 */

public class WifiInfoTool {

    private final Context context;
    //描述任何Wifi连接状态
    private WifiInfo mWifiInfo;
    public WifiManager mWifiManager;

    public WifiInfoTool(Context context) {
        this.context = context;
        //获取系统Wifi服务   WIFI_SERVICE
        this.mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        //获取连接信息
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
    }

    public WifiInfo getWifiInfo() {
        return mWifiInfo;

        // 需要定位权限
        //mWifiManager.startScan();
        //List<ScanResult> scanResults = mWifiManager.getScanResults();
    }

    private void registerBroadcast() {
        IntentFilter filter = new IntentFilter();
        filter.setPriority(2147483647);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        filter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        context.registerReceiver(mReceiver, filter);
    }

    /**
     * 广播接收，监听网络
     */
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            // 该扫描已成功完成。
            if (action.equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            } else if (intent.getAction().equals(
                    WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                int wifiState = intent.getIntExtra(
                        WifiManager.EXTRA_WIFI_STATE, 0);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_ENABLED:

                        mWifiManager.startScan();
//					refreshWifiStatusOnTime();
                        break;
                    case WifiManager.WIFI_STATE_DISABLED:

                        break;
                }
            }
        }
    };
}
