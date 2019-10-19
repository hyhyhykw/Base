package com.hy.library.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.hy.library.Base;
import com.hy.utils.SizeUtils;

import androidx.annotation.Nullable;

public class SlideBackView extends View {

    private final Path path;
    private final Path arrowPath;
    private final Paint paint;
    private final Paint arrowPaint;

    private float controlX = 0;//曲线的控制点

    private static final int backViewColor = Color.parseColor("#B3000000");

    private final float backViewHeight;
    //    public float width = 25;
//    public float height = 200;
    private final int screenWidth;
    private final int dp5;

    private int offset;

    public SlideBackView(Context context) {
        this(context, null);
    }

    public SlideBackView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlideBackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        screenWidth = Base.getScreenWidth();
        dp5 = SizeUtils.dp2px(context, 5);

        backViewHeight = SizeUtils.dp2px(getContext(), 200);

        path = new Path();
        arrowPath = new Path();

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(backViewColor);
        paint.setStrokeWidth(1);

        arrowPaint = new Paint();
        arrowPaint.setAntiAlias(true);
        arrowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        arrowPaint.setColor(Color.WHITE);
        arrowPaint.setStrokeWidth(5);
        arrowPaint.setStrokeCap(Paint.Cap.ROUND);

        setAlpha(0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画外面的东西
        float x1 = screenWidth - screenWidth * offset;

        float bezierWidth = Math.abs(x1 - controlX * 2f / 5);
        path.reset();
        path.moveTo(x1, 0);
        path.cubicTo(x1, backViewHeight / 4f, bezierWidth, backViewHeight * 3f / 8, bezierWidth, backViewHeight / 2f);
        path.cubicTo(bezierWidth, backViewHeight * 5f / 8, x1, backViewHeight * 3f / 4, x1, backViewHeight);
        canvas.drawPath(path, paint);


        //画里面的箭头
        float x2 = Math.abs(screenWidth - screenWidth * offset - controlX / 6);

        arrowPath.reset();
        arrowPath.moveTo(x2 + (dp5 * (controlX / (screenWidth / 4f))), backViewHeight * 15.5f / 32);
        arrowPath.lineTo(x2, backViewHeight * 16f / 32);
        arrowPath.moveTo(x2, backViewHeight * 16f / 32);
        arrowPath.lineTo(x2 + (dp5 * (controlX / (screenWidth / 4f))), backViewHeight * 16.5f / 32);
        canvas.drawPath(arrowPath, arrowPaint);

        setAlpha(controlX / (screenWidth / 6f));
    }

    public void updateControlPoint(float controlX, int offset) {
        this.controlX = controlX;
        this.offset = 1 - offset;
        invalidate();
    }

}
