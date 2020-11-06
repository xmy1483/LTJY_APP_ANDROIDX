package com.bac.bacplatform.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import android.util.AttributeSet;

/**
 * 加个删除线 需要自定义颜色再写个set
 */
public class DeleteLineTextView extends AppCompatTextView {
    public DeleteLineTextView(Context context) {
        super(context);
    }

    public DeleteLineTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public DeleteLineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setColor(Color.parseColor("#888888"));
        paint.setStrokeWidth(3);
        canvas.drawLine(0,hei/2,getMeasuredWidth(),hei/2,paint);
    }

    private int hei = 0;
    private Paint paint = new Paint();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        hei = getMeasuredHeight();
        if(hei != 0) {
            invalidate();
        }
    }
}
