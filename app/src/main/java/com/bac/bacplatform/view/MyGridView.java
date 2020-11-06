package com.bac.bacplatform.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * Created by guke on 2017/6/22.
 */

public class MyGridView extends GridView {
    public MyGridView(Context context) {
        super(context);
    }

    public MyGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec =
                MeasureSpec.makeMeasureSpec(
                        Integer.MAX_VALUE >> 2,
                        MeasureSpec.AT_MOST
                );
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

