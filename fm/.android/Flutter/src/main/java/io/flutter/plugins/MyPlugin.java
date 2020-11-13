package io.flutter.plugins;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.bac.bihupapa.LibApplication;

import java.util.Timer;
import java.util.TimerTask;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.model.CashCardModel;

/**
 * 这个是要给plugin，接收和响应flutter端发来的【请求】
 * 还有activity的生命周期
 */
public class MyPlugin implements FlutterPlugin, MethodChannel.MethodCallHandler, ActivityAware {
    private MethodChannel channel;
    public static String C_NAME = "globalChannel";
    private Context contextaa;

/*
    /// 保留旧版本的兼容
    public static void registerWith(PluginRegistry.Registrar registerWith) {
        Log.e("registerWith", "registerWith");
        channel = new MethodChannel(registerWith.messenger(), C_NAME);
        channel.setMethodCallHandler(new MyPlugin());
    }
*/

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        Log.d("XmyLog123", "onAttachedToEngine: ");
        channel = new MethodChannel(binding.getBinaryMessenger(), C_NAME);
        channel.setMethodCallHandler(new MyPlugin());
//        context = binding.getApplicationContext();
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        Log.d("XmyLog123", "onDetachedFromEngine: ");
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        Log.d("XmyLog123", "onAttachedToActivity: ");
        ContextHolder.getHolder().setContext(binding.getActivity());
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        Log.d("XmyLog123", "onDetachedFromActivityForConfigChanges: ");
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        Log.d("XmyLog123", "onReattachedToActivityForConfigChanges: ");
    }

    @Override
    public void onDetachedFromActivity() {
        Log.d("XmyLog123", "onDetachedFromActivity: ");
    }

    @Override
    public void onMethodCall(@NonNull MethodCall call, @NonNull MethodChannel.Result result) {
        if (call.method.equals("getCashCardListData")) {
            result.success(CashCardModel.getCashCardListData());
        } else if (call.method.equals("showToast")) {
            String s = call.argument("msg");
            Toast.makeText(ContextHolder.getHolder().getContext(), s, Toast.LENGTH_SHORT).show();
            result.success("");
        } else {
            result.success(CashCardModel.getTestStr());
        }
    }
}


































