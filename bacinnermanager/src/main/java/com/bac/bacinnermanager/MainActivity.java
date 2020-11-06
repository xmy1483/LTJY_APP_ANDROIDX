package com.bac.bacinnermanager;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.bac.bacinnermanager.utils.Constants;
import com.bac.bacinnermanager.utils.SeedUtil;
import com.bac.commonlib.http.BacApi;
import com.bac.commonlib.http.HttpHelperLib;
import com.bac.commonlib.param.CommonParam;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import static com.bac.commonlib.http.HttpManager.okHttpInit;
import static com.bac.commonlib.http.HttpManager.retrofitInit;


public class MainActivity extends AppCompatActivity {

    private String url = "http://121.43.172.16:88/app. pay/";
    private String versionName = "2.5.0019";
    private static OkHttpClient sOkHttpClient;
    private static Retrofit sRetrofit;
    private static BacApi sBacApi;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
           //     String aaa = getIntent().getStringExtra("aaa");
                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
//                intent.putExtra("aaa","{}")
                startActivity(intent);
            }
        });

         /*初始化 网络请求*/
        sOkHttpClient = okHttpInit(this, true);
        sRetrofit = retrofitInit(sOkHttpClient, url);
        sBacApi = sRetrofit.create(BacApi.class);

        CommonParam.getInstance()
                .setApplication(this.getApplication())
                .setS(this.getString(R.string.seed_num) + Constants.XX + BuildConfig.appKeySeed + BuildConfig.appKeySeed2)
                .setSeed(SeedUtil.getSeed(this))
                .setVersionName(versionName)
                .setPid("bacplatform")
                .setBacApi(sBacApi)
                .setOkHttpClient(sOkHttpClient)
                .setRetrofit(sRetrofit);
        HttpHelperLib.getInstance().setDebug(true);
    }
}
