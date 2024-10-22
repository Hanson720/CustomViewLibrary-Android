package com.hanson.customviewlibrary.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.hanson.customviewlibrary.R;

/**
 *     <declare-styleable name="UnderlinedTextView">
 *         <attr name="underlineWidth" format="dimension" />
 *         <attr name="underlineColor" format="color" />
 *     </declare-styleable>
 */

/**
 * 绘制下划线的TextView
 *
 * TextView有下划线参数，这个类开用于扩展其他绘制内容时进行参考
 */
public class UnderlinedTextView extends androidx.appcompat.widget.AppCompatTextView {

    private float underlineWidth = 2f; // 默认下划线宽度
    public UnderlinedTextView(Context context) {
        super(context);
    }

    public UnderlinedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UnderlinedTextView);
        // 可以通过自定义属性来设置下划线宽度
        paint.setStrokeWidth(typedArray.getDimension(R.styleable.UnderlinedTextView_underlineWidth, underlineWidth));
        paint.setColor(typedArray.getColor(R.styleable.UnderlinedTextView_underlineColor, Color.LTGRAY));
    }

    public UnderlinedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UnderlinedTextView);
        paint.setStrokeWidth(typedArray.getDimension(R.styleable.UnderlinedTextView_underlineWidth, underlineWidth));
        paint.setColor(typedArray.getColor(R.styleable.UnderlinedTextView_underlineColor, Color.LTGRAY));
    }

    // 设置下划线宽度的方法
    public void setUnderlineWidth(float underlineWidth) {
        this.underlineWidth = underlineWidth;
        invalidate(); // 通知重绘
    }

    Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(0, getLineHeight(), getWidth(), getLineHeight(), paint);
    }
}
