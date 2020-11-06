package com.bac.commonlib.capture;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.bac.commonlib.R;
import com.bac.commonlib.capture.view.ClipImageLayout;
import com.bac.rxbaclib.rx.dialog.RxDialog;
import com.bac.rxbaclib.rx.life.automatic.components.AutomaticRxAppCompatActivity;
import com.bac.rxbaclib.rx.life.automatic.enums.ActivityEvent;
import com.bac.rxbaclib.rx.rxScheduler.RxScheduler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;


/**
 * Created by wujiazhen on 2017/6/27.
 */

public class ClipActivity extends AutomaticRxAppCompatActivity {


    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cl_capute_clip_activity);

        url = getIntent().getStringExtra("url");
        final ClipImageLayout cil = new ClipImageLayout(this,new ClipImageBean(1.6f, 0, url));
        FrameLayout fl = findViewById(R.id.fl);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        cil.setLayoutParams(layoutParams);
        fl.addView(cil);
//        Glide.with(this)
//                .load(url)
//                .into((ImageView)findViewById(R.id.iv));
        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Observable.create(new Observable.OnSubscribe<Bitmap>() {
                    @Override
                    public void call(Subscriber<? super Bitmap> subscriber) {

                        subscriber.onNext(cil.clip());

                    }
                })
                        .map(new Func1<Bitmap, String>() {
                            @Override
                            public String call(Bitmap bitmap) {
                                FileOutputStream stream = null;
                                // 保存图片
                                try {
                                    stream = new FileOutputStream(new File(url));
                                    bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream);
                                } catch (FileNotFoundException e) {
                                } finally {
                                    try{
                                        if (stream!=null) {
                                            stream.close();
                                        }
                                    }catch (Exception e){}

                                }
                                return "";
                            }
                        })
                        .subscribeOn(RxScheduler.RxPoolScheduler())
                        .observeOn(AndroidSchedulers.mainThread())
                        .compose(ClipActivity.this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                        .compose(new RxDialog<String>().rxDialog(ClipActivity.this))
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                // 返回url
                                setResult(RESULT_OK);
                                ClipActivity.this.onBackPressed();
                            }
                        });
            }
        });

    }


}
