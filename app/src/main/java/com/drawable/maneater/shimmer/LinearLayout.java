package com.drawable.maneater.shimmer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import com.drawable.maneater.shimmerframelayout.ShimmerHelper;

/**
 * Created by Administrator on 2016/10/12 0012.
 */

public class LinearLayout extends android.widget.LinearLayout implements ShimmerHelper.ShimmerView {
    private ShimmerHelper shimmerHelper = null;

    public LinearLayout(Context context) {
        this(context, null);
    }

    public LinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        shimmerHelper = new ShimmerHelper(this);
        shimmerHelper.init(context,attrs);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        shimmerHelper.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        shimmerHelper.onDetachedFromWindow();
    }


    @Override
    public void callSuperOnDispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        shimmerHelper.dispatchDraw(canvas);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        shimmerHelper.onDraw(canvas);
    }

    @SuppressLint("WrongCall")
    @Override
    public void callSupperOnDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}
