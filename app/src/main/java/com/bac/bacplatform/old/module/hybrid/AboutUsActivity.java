package com.bac.bacplatform.old.module.hybrid;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.TextView;

import com.bac.bacplatform.R;
import com.bac.bacplatform.conf.Constants;
import com.bac.bacplatform.extended.base.components.AutomaticBaseActivity;

public class AboutUsActivity extends AutomaticBaseActivity {
private  TextView text;
    private TextView text2;

    @Override
    protected void initView() {
        setContentView(R.layout.about_us_activity);
        initToolBar("关于我们");

        text = (TextView) findViewById(R.id.tv_num);
        text.setText(getAppVersionName(this));
        text2 = (TextView) findViewById(R.id.tv_num2);
        text2.setText(getVersionCode(this));
    }

    @Override
    protected void initFragment() {

    }


    public static String getAppVersionName(Context context) {

        return Constants.APP.VERSION_NAME;
    }

    public String getVersionCode(Context context){
        PackageManager packageManager=context.getPackageManager();
        PackageInfo packageInfo;
        String versionCode="";
        try {
            packageInfo=packageManager.getPackageInfo(context.getPackageName(),0);
            versionCode=packageInfo.versionCode+"";
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }


}
