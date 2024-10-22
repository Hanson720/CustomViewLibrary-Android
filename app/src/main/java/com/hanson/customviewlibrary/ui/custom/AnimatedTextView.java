package com.hanson.customviewlibrary.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.animation.ValueAnimator;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 字符一个一个出现
 */
public class AnimatedTextView extends AppCompatTextView {

    public AnimatedTextView(Context context) {
        super(context);
    }

    public AnimatedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AnimatedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void animateTextChange(final String newText, long duration) {
        final String oldText = getText().toString();
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1f);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = (float) animation.getAnimatedValue();
                int oldTextAlpha = (int) ((1 - progress) * 255);
                int newTextAlpha = (int) (progress * 255);

                setTextColor(getTextColors().withAlpha(oldTextAlpha));
                String partialText = newText.substring(0, (int) (progress * newText.length()));
                setText(partialText);
                setTextColor(getTextColors().withAlpha(newTextAlpha));

                if (progress == 1f) {
                    setText(newText);
                    setTextColor(getTextColors().withAlpha(255)); // 恢复完全不透明
                }
            }
        });
        animator.start();
    }
}