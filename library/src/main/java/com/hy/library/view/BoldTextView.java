package com.hy.library.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;

import com.hy.library.R;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * Created time : 2019/4/17 12:32 PM.
 *
 * @author HY
 */
public class BoldTextView extends AppCompatTextView {
    public BoldTextView(Context context) {
        this(context, null);
    }

    public BoldTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    private float strokeWidth;

    public BoldTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BoldTextView);
        strokeWidth = a.getFloat(R.styleable.BoldTextView_base_btv_stroke_width, -1);
        a.recycle();
        if (strokeWidth < 0 && strokeWidth != -1) {
            strokeWidth = -1;
        }
    }

    public void setStrokeWidth(float strokeWidth) {
        this.strokeWidth = strokeWidth;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (strokeWidth != -1) {
            TextPaint paint = getPaint();
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            paint.setStrokeWidth(strokeWidth);
        }
        super.onDraw(canvas);
    }
}
