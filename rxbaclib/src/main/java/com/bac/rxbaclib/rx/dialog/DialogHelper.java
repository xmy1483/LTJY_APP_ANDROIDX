package com.bac.rxbaclib.rx.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Wjz on 2017/5/9.
 */

public class DialogHelper {

    private Context context;
    private ProgressDialog progressDialog;

    public DialogHelper(Context context) {
        this.context = context;
    }

    public void showProgressDialog(String msg,boolean canCancel) {
        if (context != null) {
            if (progressDialog==null) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setCancelable(canCancel);
            }
            if (!progressDialog.isShowing()) {
                progressDialog.setMessage(msg);
                progressDialog.show();
            }
        }
    }

    public void dismissProgressDialog(){
        if (progressDialog!=null&&progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}
