package com.bac.commonlib.capture.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import androidx.appcompat.widget.AppCompatImageView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;

import com.bac.commonlib.capture.ClipImageBean;


/**
 * Created by wujiazhen on 2017/6/26.
 * 可缩放的 imageview
 * 以 x 为缩放标准
 */

public class ClipZoomImageView extends AppCompatImageView implements View.OnTouchListener, ViewTreeObserver.OnGlobalLayoutListener {


    private static final String TAG = ClipZoomImageView.class.getSimpleName();
    public static float SCALE_MAX = 4.0f;
    public static float SCALE_MID = 2.0f;

    public static float _SCALE = 0.5f;
    /**
     * 初始化时的缩放比例，如果图片 宽或高 大于屏幕，此值将小于0
     */
    private float initScale = 1.0f;
    private boolean once = true;

    /**
     * 用于存放矩阵的9个值
     */
    private final float[] matrixValuesArray = new float[9];

    /**
     * 缩放的手势检测
     */
    private ScaleGestureDetector mScaleGestureDetector = null;
    private final Matrix mScaleMatrix = new Matrix();

    /**
     * 用于双击检测
     */
    private GestureDetector mGestureDetector;
    private boolean isAutoScale;

    private int mTouchSlop;

    private float mLastX;
    private float mLastY;

    private boolean isCanDrag;
    private int lastPointerCount;

    /**
     * 水平方向与View的边距
     */
    private int mHorizontalPadding;
    /**
     * 垂直方向与View的边距
     */
    private int mVerticalPadding;

    private float ratio;

    public ClipZoomImageView(final Context context, ClipImageBean param) {
        super(context);

        mHorizontalPadding = param.getPadding();
        ratio = param.getRatio();

        /*
         * 绘制时，使用图像矩阵方式缩放。
         * 图像矩阵可以通过 setImageMatrix(Matrix) 设置。
         * 在 XML 中可以使用的语法： android:scaleType="matrix"。
         */
        setScaleType(ScaleType.MATRIX);

        mGestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    /**
                     * 用于双击检测
                     */
                    @Override
                    public boolean onDoubleTap(MotionEvent e) {
                        if (isAutoScale == true)
                            return true;

                        // 缩放点
                        float x = e.getX();
                        float y = e.getY();

                        // 当前缩放 比例 < mid
                        if (getScale() < SCALE_MID) {
                            // 刷新 16s 后
                            ClipZoomImageView.this.postDelayed(
                                    new AutoScaleRunnable(SCALE_MID, x, y),
                                    16);

                            isAutoScale = true;
                        } else // 非最小 都是固定缩放
                        {
                            ClipZoomImageView.this.postDelayed(
                                    new AutoScaleRunnable(initScale, x, y),
                                    16);

                            isAutoScale = true;
                        }

                        return true;
                    }
                });

        /**
         * 缩放的手势检测
         */
        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleGestureDetector.OnScaleGestureListener() {
            @Override
            public boolean onScaleBegin(ScaleGestureDetector detector) {
                return true;
            }

            @Override
            public void onScaleEnd(ScaleGestureDetector detector) {
            }

            @Override
            public boolean onScale(ScaleGestureDetector detector) {
                float scale = getScale();
                float scaleFactor = detector.getScaleFactor();

                if (getDrawable() == null)
                    return true;

                /**
                 * 缩放的范围控制
                 */
                if ((scale < SCALE_MAX && scaleFactor > 1.0f)
                        || (scale > _SCALE && scaleFactor < 1.0f)) {


                    /**
                     * 最大值最小值判断
                     */
                    if (scaleFactor * scale < _SCALE) {
                        scaleFactor = _SCALE / scale;
                    }
                    if (scaleFactor * scale > SCALE_MAX) {
                        scaleFactor = SCALE_MAX / scale;
                    }
                    /**
                     * 设置缩放比例
                     */

                    mScaleMatrix.postScale(scaleFactor, scaleFactor,
                            detector.getFocusX(), detector.getFocusY());
                    checkBorder();
                    setImageMatrix(mScaleMatrix);

                }
                return true;

            }
        });

        this.setOnTouchListener(this);
    }

    /**
     * 自动缩放的任务
     *
     * @author zhy
     */
    private class AutoScaleRunnable implements Runnable {
        static final float BIGGER = 1.07f;
        static final float SMALLER = 0.93f;
        private float mTargetScale;
        private float tmpScale;

        /**
         * 缩放的中心
         */
        private float x;
        private float y;

        /**
         * 传入目标缩放值，根据目标值与当前值，判断应该放大还是缩小
         *
         * @param targetScale
         */
        public AutoScaleRunnable(float targetScale, float x, float y) {
            this.mTargetScale = targetScale;
            this.x = x;
            this.y = y;

            if (getScale() < mTargetScale) {
                tmpScale = BIGGER;
            } else {
                tmpScale = SMALLER;
            }

        }

        @Override
        public void run() {
            // 进行缩放 x y
            mScaleMatrix.postScale(tmpScale, tmpScale, x, y);

            // 检查边界
            checkBorder();
            setImageMatrix(mScaleMatrix);

            final float currentScale = getScale();
            // 如果值在合法范围内，继续缩放
            if (((tmpScale > 1f) && (currentScale < mTargetScale))
                    || ((tmpScale < 1f) && (mTargetScale < currentScale))) {
                ClipZoomImageView.this.postDelayed(this, 16);
            } else
            // 设置为目标的缩放比例
            {
                final float deltaScale = mTargetScale / currentScale;
                mScaleMatrix.postScale(deltaScale, deltaScale, x, y);
                checkBorder();
                setImageMatrix(mScaleMatrix);
                isAutoScale = false;
            }

        }
    }


    /**
     * 根据当前图片的Matrix 获得图片的范围
     *
     * @return
     */
    private RectF getMatrixRectF() {
        Matrix matrix = mScaleMatrix;

        RectF rect = new RectF();
        // 获取 图片
        Drawable d = getDrawable();

        if (null != d) {
            rect.set(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
            matrix.mapRect(rect);
        }
        return rect;
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        /**
         * 用于双击检测
         */
        if (mGestureDetector.onTouchEvent(event))
            return true;

        // 缩放手势 检测
        boolean b = mScaleGestureDetector.onTouchEvent(event);

        float x = 0, y = 0;
        // 拿到触摸点的个数
        final int pointerCount = event.getPointerCount();
        // 得到多个触摸点的x与y均值
        for (int i = 0; i < pointerCount; i++) {
            x += event.getX(i);
            y += event.getY(i);
        }
        x = x / pointerCount;
        y = y / pointerCount;

        /**
         * 每当触摸点数发生变化时，重置mLasX , mLastY
         */
        if (pointerCount != lastPointerCount) {
            isCanDrag = false;
            mLastX = x;
            mLastY = y;
        }

        lastPointerCount = pointerCount;
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mLastX;
                float dy = y - mLastY;

                if (!isCanDrag) {
                    isCanDrag = isCanDrag(dx, dy);
                }
                if (isCanDrag) {
                    if (getDrawable() != null) {

                        RectF rectF = getMatrixRectF();
                        // 如果宽度小于屏幕宽度，则禁止左右移动
                        if (rectF.width() <= getWidth() - mHorizontalPadding * 2) {
                            dx = 0;
                        }
                        // 如果高度小于屏幕高度，则禁止上下移动
                        if (rectF.height() <= getHeight() - mVerticalPadding * 2) {
                            dy = 0;
                        }
                        mScaleMatrix.postTranslate(dx, dy);
                        checkBorder();
                        setImageMatrix(mScaleMatrix);
                    }
                }
                mLastX = x;
                mLastY = y;
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                lastPointerCount = 0;
                break;
        }

        return true;
    }

    /**
     * 获得当前的缩放比例
     *
     * @return
     */
    public final float getScale() {
        // Copy 9 values from the matrix into the array.
        mScaleMatrix.getValues(matrixValuesArray);
        float v = matrixValuesArray[Matrix.MSCALE_X];

        return v;// 返回 scale_x
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        getViewTreeObserver().removeGlobalOnLayoutListener(this);
    }

    @Override
    public void onGlobalLayout() {
        if (once) {
            Drawable d = getDrawable();
            if (d == null)
                return;
            // 垂直方向的边距
            mVerticalPadding = (int) ((getHeight() - (int) (getWidth() / ratio + .5f)) * .5f);

            int width = getWidth();
            int height = getHeight();
            // 拿到图片的宽和高
            int dw = d.getIntrinsicWidth();
            int dh = d.getIntrinsicHeight();
            float scale = 1.0f;


            // 图片的宽度 小于 组件的宽度 - 左右 padding
            if (dw < getWidth() - mHorizontalPadding * 2
                    // 图片的高度 大于 组件的高度 - 上下 padding
                    && dh > getHeight() - mVerticalPadding * 2) {
                // 组件宽度 - 左右 padding ／ 图片宽度
                scale = (getWidth() * 1.f - mHorizontalPadding * 2) / dw;// 宽度缩放 与 组件宽度
                _SCALE = 1.0f;
            }

            // 图片的高度 小于 组件的高度 - 上下 padding
            if (dh < getHeight() - mVerticalPadding * 2
                    // 图片的宽度 大于 组件的宽度 - 左右 padding
                    && dw > getWidth() - mHorizontalPadding * 2) {
                // 组件的高度 - 上下 padding ／ 图片高度
                scale = (getHeight()* 1.f - mVerticalPadding * 2) / dh; // 高度缩放 与 组件高度
                _SCALE = 1.0f;
            }

            // 图片的宽度 小于 组件的宽度 - 左右 padding
            if (dw < getWidth() - mHorizontalPadding * 2
                    // 图片的高度 小于 组件的高度 - 上下 padding
                    && dh < getHeight() - mVerticalPadding * 2) {

                // 组件宽度 - 左右 padding ／ 图片宽度
                float scaleW = (getWidth()* 1.f - mHorizontalPadding * 2) / dw;
                // 组件的高度 - 上下 padding ／ 图片高度
                float scaleH = (getHeight()* 1.f - mVerticalPadding * 2) / dh;
                scale = Math.max(scaleW, scaleH);// 宽度 高度 都小 去最大 缩放
                _SCALE = scale;
            }

            if (dw > getWidth() - mHorizontalPadding * 2
                    // 图片的高度 小于 组件的高度 - 上下 padding
                    && dh > getHeight() - mVerticalPadding * 2) {

                _SCALE = Math.max((getWidth()* 1.f - mHorizontalPadding * 2) / dw, (getHeight()* 1.f - mVerticalPadding * 2) / dh);

            }

            // 设置 initScale , SCALE_MID , SCALE_MAX
            initScale = scale;

            // 计算缩小倍数
            SCALE_MID = initScale * 2;
            SCALE_MAX = initScale * 4;

            // 位移
            mScaleMatrix.postTranslate((width - dw) / 2, (height - dh) / 2);
            // 缩放
            mScaleMatrix.postScale(scale, scale, getWidth() / 2, getHeight() / 2);
            // 图片移动至屏幕中心
            setImageMatrix(mScaleMatrix);
            once = false;
        }

    }

    /**
     * 剪切图片，返回剪切后的bitmap对象
     *
     * @return
     */
    public Bitmap clip() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(),
                Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return Bitmap.createBitmap(
                bitmap,
                mHorizontalPadding,
                mVerticalPadding,
                getWidth() - 2 * mHorizontalPadding,
                getHeight() - 2 * mVerticalPadding);
    }

    /**
     * 边界检测
     * 移动方式
     */
    private void checkBorder() {

        // 获取缩放后的矩形
        RectF rect = getMatrixRectF();
        float deltaX = 0;
        float deltaY = 0;

        // 获取容器的宽高
        int width = getWidth();
        int height = getHeight();

        // 如果宽或高大于屏幕，则控制范围 ;
        // 这里的0.001是因为精度丢失会产生问题，但是误差一般很小，所以我们直接加了一个0.01
        if (rect.width() + 0.01 >= width - 2 * mHorizontalPadding) {
            if (rect.left > mHorizontalPadding) { // 左边
                deltaX = -rect.left + mHorizontalPadding;
            }
            if (rect.right < width - mHorizontalPadding) { // 右边
                deltaX = width - mHorizontalPadding - rect.right;
            }
        }
        if (rect.height() + 0.01 >= height - 2 * mVerticalPadding) {
            if (rect.top > mVerticalPadding) { // 上边
                deltaY = -rect.top + mVerticalPadding;
            }
            if (rect.bottom < height - mVerticalPadding) {// 下边
                deltaY = height - mVerticalPadding - rect.bottom;
            }
        }

        // 矩阵移动 去除 deltaX deltaY
        mScaleMatrix.postTranslate(deltaX, deltaY);

    }

    /**
     * 是否是拖动行为
     *
     * @param dx
     * @param dy
     * @return
     */
    private boolean isCanDrag(float dx, float dy) {
        return Math.sqrt((dx * dx) + (dy * dy)) >= mTouchSlop;
    }

}
