package com.drawable.maneater.shimmerframelayout;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.view.View;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

public class ShimmerHelper {
    private WeakReference<View> shimmerView = null;
    private Paint mPaint = null;
    private float xOffsetFactor = -1f;
    private float maskLengthFactor = 0.5f;

    private static final PorterDuffXfermode DST_IN_PORTER_DUFF_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG
            | Canvas.CLIP_SAVE_FLAG
            | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
            | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
            | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
    private ValueAnimator mAnimator;

    public ShimmerHelper(View targetView) {
        this.shimmerView = new WeakReference<View>(targetView);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskLengthFactor = 0.5f;
        targetView.setWillNotDraw(false);
    }

    public ShimmerView getShimmerView() {
        return (ShimmerView) shimmerView.get();
    }

    public void dispatchDraw(Canvas canvas) {
        View targetView = shimmerView.get();
        ShimmerView shimmerView = getShimmerView();
        if (targetView instanceof ViewGroup) {

            int viewWidth = targetView.getWidth();
            int viewHeight = targetView.getHeight();

            //        draw alpha base
            canvas.saveLayerAlpha(0, 0, viewWidth, viewHeight, 0x88, LAYER_FLAGS);
            shimmerView.callSuperOnDispatchDraw(canvas);
            canvas.restore();


            float left = xOffsetFactor * viewWidth;
            float length = viewWidth * maskLengthFactor;

            canvas.saveLayer(0, 0, viewWidth, viewHeight, null, LAYER_FLAGS);
            shimmerView.callSuperOnDispatchDraw(canvas);
            Shader shader = new LinearGradient(left, 0, left + length, viewHeight, new int[]{Color.TRANSPARENT, Color.BLACK, Color.TRANSPARENT}, new float[]{0, 0.5f, 1.0f},
                    Shader.TileMode.CLAMP);
            mPaint.setShader(shader);
            canvas.setDrawFilter(new DrawFilter());
            mPaint.setXfermode(DST_IN_PORTER_DUFF_XFERMODE);
            canvas.drawRect(0, 0, viewWidth, viewHeight, mPaint);
            canvas.restore();
        }
    }

    public void onDraw(Canvas canvas) {
        //// TODO: 2016/10/12 0012
        System.out.printf("not imp");
        View targetView = shimmerView.get();
        ShimmerView shimmerView = getShimmerView();

        if (shimmerView != null) {
            shimmerView.callSupperOnDraw(canvas);
        }
    }

    public void onAttachedToWindow() {
        getShimmerAnimation().start();
    }

    public void onDetachedFromWindow() {
        getShimmerAnimation().cancel();
    }

    private Animator getShimmerAnimation() {
        if (mAnimator != null) {
            return mAnimator;
        }

        mAnimator = ValueAnimator.ofFloat(-1f, 1.0f);
        mAnimator.setDuration(800);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.REVERSE);

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                xOffsetFactor = (float) animation.getAnimatedValue();
            }
        });
        return mAnimator;
    }


    public interface ShimmerView {
        void callSuperOnDispatchDraw(Canvas canvas);

        void callSupperOnDraw(Canvas canvas);
    }


}
