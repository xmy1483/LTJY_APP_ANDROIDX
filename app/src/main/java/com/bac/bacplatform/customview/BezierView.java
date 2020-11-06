package com.bac.bacplatform.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author xmy
 * desc: 绘制二阶贝塞尔曲线
 */
public class BezierView extends View {
    private int wid = 1080,hei = 1000;
    private float bezierDegree = 0.8f; // 下拉程度

    public BezierView(Context context) {
        super(context);
        init();
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setBezierDegree(float degree) {
        if(degree>0) {
            this.bezierDegree = degree;
            reDraw();
        }
    }

    public void setBgColor(int color) {
        paint.setColor(color);
        invalidate();
    }

    public void setBgColor(String colorHex) {
        int color = Color.parseColor(colorHex);
        paint.setColor(color);
        invalidate();
    }

    private void reDraw() {
        path.reset();
        path.moveTo(0,0);
        path.quadTo(wid/2,hei* bezierDegree,wid,0);
        invalidate();
    }


    private Paint paint;
    private Path path;
    private void init() {
        paint = new Paint();
        paint.setColor(Color.parseColor("#0085d0"));
        paint.setStrokeWidth(1);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        path = new Path();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path,paint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        wid = getMeasuredWidth();
        hei = getMeasuredHeight();
        path.moveTo(0,0);
        path.quadTo(wid/2,180,wid,0);
        invalidate();
    }
}
