package com.bac.commonlib.utils.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bac.commonlib.R;
import com.jakewharton.rxbinding.widget.RxTextView;

/**
 * Created by wujiazhen on 2017/8/10.
 */

public class UIUtil {

    public static void startActivityInAnim(AppCompatActivity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.cl_slide_right_in, R.anim.cl_slide_left_out);
    }

    public static void startActivityInAnimAndFinishSelf(AppCompatActivity activity, Intent intent) {
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.cl_slide_right_in, R.anim.cl_slide_left_out);
        activity.finish();
    }
    /**
     * activity 的toolbar
     * @param activity
     * @param s
     * @param listener
     * @return
     */
    public static TextView initToolBar(final AppCompatActivity activity, String s,
                                       View.OnClickListener listener) {
        Toolbar toolbar = activity.findViewById(R.id.toolbar);
        toolbar.setTitle("");
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (listener == null) {
            listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.onBackPressed();
                }
            };
        }
        toolbar.setNavigationOnClickListener(listener);
        TextView title = activity.findViewById(R.id.tv_center);
        RxTextView.text(title).call(s == null ? "" : s);
        return title;
    }

    public static void showWarnDialog(Context context, String title, String msg, DialogInterface.OnClickListener listener, String btnText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(msg)
                .setTitle(title);
        if(listener!=null) {
            builder.setPositiveButton(btnText,listener);
        } else  {
            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
        }
        builder.show();
    }

}
