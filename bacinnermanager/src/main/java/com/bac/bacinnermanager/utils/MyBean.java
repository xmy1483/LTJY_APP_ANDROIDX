package com.bac.bacinnermanager.utils;

import android.content.Context;

import java.io.Serializable;
import java.util.List;

/**
 * Created by guke on 2017/8/21.
 */

public class MyBean implements Serializable {
    private Context context;
    private List<String> data;
    private List<String> data3;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public List<String> getData3() {
        return data3;
    }

    public void setData3(List<String> data3) {
        this.data3 = data3;
    }
}
