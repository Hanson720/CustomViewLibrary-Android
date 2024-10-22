package com.hanson.customviewlibrary.ui.custom;


import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;


import com.hanson.customviewlibrary.R;

import org.jetbrains.annotations.Nullable;

public class PathMeasureView extends FrameLayout {
    private Paint mPaint;
    private Path mRoundRectPath; // 圆角矩形路径
    private Path mDstPath;
    private PathMeasure mPathMeasure;
    private float pathMeasureLength;
    private ValueAnimator mValueAnimator = ValueAnimator.ofFloat(0, 1);
    public float animatedValue;
    private boolean mIsWindow;
    private boolean mIsArrow;
    private float[] mPos = new float[2];
    private float[] mTan = new float[2];
    private boolean infinite = false;

    private int animationDuration = 2000; // 动画持续时间
    private int drawColor = Color.BLACK; // 绘制颜色
    private float cornerRadius = 18; // 圆角大小

    public PathMeasureView(Context context) {
        super(context);
        init();
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(attrs);
        init();
    }

    public PathMeasureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(attrs);
        init();
    }

    private void initAttrs(AttributeSet attrs) {
        final TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.PathMeasureView);
        animationDuration = a.getInt(R.styleable.PathMeasureView_animationDuration, animationDuration); // 默认动画时间2秒
        cornerRadius = a.getDimensionPixelSize(R.styleable.PathMeasureView_cornerRadius, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cornerRadius, getResources().getDisplayMetrics())); // 默认圆角40dp
        drawColor = a.getColor(R.styleable.PathMeasureView_drawColor, drawColor); // 默认颜色黑色
        a.recycle();
    }

    private float strokeWidth = 6f;

    public int getAnimationDuration() {
        return animationDuration;
    }

    public void setAnimationDuration(int animationDuration) {
        mValueAnimator.setDuration(animationDuration);
        this.animationDuration = animationDuration;
    }

    public int getDrawColor() {
        return drawColor;
    }

    public void setDrawColor(int drawColor) {
        mPaint.setColor(drawColor); // 设置画笔颜色
        this.drawColor = drawColor;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public float getStrokeWidth() {
        return strokeWidth;
    }

    public void setStrokeWidth(float strokeWidth) {
        mPaint.setStrokeWidth(strokeWidth);
        this.strokeWidth = strokeWidth;
    }

    private void init() {
        // 初始化画笔
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setColor(drawColor); // 设置画笔颜色

        // 初始化路径和PathMeasure对象
        mRoundRectPath = new Path();
        mDstPath = new Path();
        mPathMeasure = new PathMeasure();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 定义圆角矩形的四个角的坐标和圆角的半径
        RectF roundRect = new RectF(strokeWidth / 2, strokeWidth / 2, w - strokeWidth / 2, h - strokeWidth / 2);
        // 添加圆角矩形路径
        mRoundRectPath.addRoundRect(roundRect, cornerRadius, cornerRadius, Path.Direction.CW);
        // 设置PathMeasure对象的路径
        mPathMeasure.setPath(mRoundRectPath, true);
        // 获取被测量路径的总长度
        pathMeasureLength = mPathMeasure.getLength();
        // 初始化动画
        setAnimationDuration(animationDuration);
        if (infinite) {
            mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        }
        mValueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animatedValue = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mDstPath.reset();
        mDstPath.lineTo(0, 0);

        float startD = 0;
        float stopD = animatedValue * pathMeasureLength;
        if (mIsWindow) {
            startD = (float) (stopD - ((0.5 - Math.abs(animatedValue - 0.5)) * pathMeasureLength));
        }

        mPathMeasure.getSegment(startD, stopD, mDstPath, true);
        canvas.save();
        canvas.translate(0, 0); //将画布原点向右移动dx个单位，向下移动dy个单位
        canvas.drawPath(mDstPath, mPaint);
        canvas.restore();

        if (mIsArrow) {
            mPathMeasure.getPosTan(animatedValue * pathMeasureLength, mPos, mTan);
            float degree = (float) (Math.atan2(mTan[1], mTan[0]) * 180 / Math.PI);
            canvas.save();
            canvas.translate(0, 0);
            canvas.drawCircle(mPos[0], mPos[1], 10, mPaint);
            canvas.rotate(degree);
            canvas.drawLine(0, -200, 200, -200, mPaint);
            canvas.restore();
        }
    }

    public void setWindow(boolean window) {
        mIsWindow = window;
        invalidate();
    }

    public void setArrow(boolean arrow) {
        mIsArrow = arrow;
        invalidate();
    }

    public void startAnim() {
        if (mValueAnimator != null) {
            mValueAnimator.start();
        }
    }

    public void pauseAnim() {
        if (mValueAnimator != null && mValueAnimator.isStarted() && mValueAnimator.isRunning()) {
            mValueAnimator.pause();
        }
    }
}