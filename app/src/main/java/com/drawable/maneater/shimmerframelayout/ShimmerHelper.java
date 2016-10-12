package com.drawable.maneater.shimmerframelayout;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;

public class ShimmerHelper {
    private WeakReference<ShimmerView> shimmerView = null;
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

    private float mBaseAlpha = 0.8f;
    private boolean mAutoStart = true;
    private boolean mIsStarted = false;
    private int mRepeatCount = ValueAnimator.INFINITE;
    private int mRepeatMode = ValueAnimator.REVERSE;
    private int mDuration = 800;

    public ShimmerHelper(ShimmerView targetView) {
        this.shimmerView = new WeakReference<ShimmerView>(targetView);
        targetView.setWillNotDraw(false);
    }

    public void init(Context context, AttributeSet attrs) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskLengthFactor = 0.5f;
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.ShimmerView);
        try {
            mAutoStart = ta.getBoolean(R.styleable.ShimmerView_s_auto_start, mAutoStart);
            mBaseAlpha = ta.getFloat(R.styleable.ShimmerView_s_base_alpha, mBaseAlpha);
            mRepeatCount = ta.getInt(R.styleable.ShimmerView_s_repeat_count, mRepeatCount);
            mRepeatMode = ta.getInt(R.styleable.ShimmerView_s_repeat_mode, mRepeatMode);
            mDuration = ta.getInt(R.styleable.ShimmerView_s_duration, mDuration);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ta.recycle();
        }

    }


    public void dispatchDraw(Canvas canvas) {
        ShimmerView targetView = this.shimmerView.get();
        if (targetView instanceof ViewGroup) {

            int viewWidth = targetView.getWidth();
            int viewHeight = targetView.getHeight();

            //        draw alpha base
            canvas.saveLayerAlpha(0, 0, viewWidth, viewHeight, (int) (255 * mBaseAlpha), LAYER_FLAGS);
            targetView.callSuperOnDispatchDraw(canvas);
            canvas.restore();


            float left = xOffsetFactor * viewWidth;
            float length = viewWidth * maskLengthFactor;

            canvas.saveLayer(0, 0, viewWidth, viewHeight, null, LAYER_FLAGS);
            targetView.callSuperOnDispatchDraw(canvas);

            Shader shader = new LinearGradient(left, 0, left + length, viewHeight, new int[]{Color.TRANSPARENT, Color.BLACK, Color.TRANSPARENT}, new float[]{0, 0.5f, 1.0f},
                    Shader.TileMode.CLAMP);
            mPaint.setShader(shader);
            mPaint.setXfermode(DST_IN_PORTER_DUFF_XFERMODE);
            canvas.drawRect(0, 0, viewWidth, viewHeight, mPaint);

            canvas.restore();
        }
    }

    public void onDraw(Canvas canvas) {
        //// TODO: 2016/10/12 0012
        System.out.printf("not imp");
        ShimmerView shimmerView = this.shimmerView.get();

        if (shimmerView != null) {
            shimmerView.callSupperOnDraw(canvas);
        }
    }

    public void onAttachedToWindow() {
        if (mAutoStart && !mIsStarted) {
            startShimmer();
        }
    }

    public void onDetachedFromWindow() {
        stopShimmer();
    }


    public void startShimmer() {
        mIsStarted = true;
        getShimmerAnimation().start();
    }

    public void stopShimmer() {
        mIsStarted = false;
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
        invalidateShimmerView();
    }


    public ShimmerView getShimmerView() {
        return shimmerView != null ? shimmerView.get() : null;
    }


    private void invalidateShimmerView() {
        ShimmerView shimmerView = getShimmerView();
        if (shimmerView != null) {
            shimmerView.invalidate();
        }
    }

    private Animator getShimmerAnimation() {
        if (mAnimator != null) {
            return mAnimator;
        }

        mAnimator = ValueAnimator.ofFloat(-1f, 1.0f);
        mAnimator.setDuration(mDuration);
        mAnimator.setRepeatCount(mRepeatCount);
        mAnimator.setRepeatMode(mRepeatMode);

        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                xOffsetFactor = (float) animation.getAnimatedValue();
                ShimmerView targetView = shimmerView.get();
                if (targetView != null) {
                    targetView.invalidate();
                }
            }
        });
        return mAnimator;
    }


    public interface ShimmerView {
        void callSuperOnDispatchDraw(Canvas canvas);

        void callSupperOnDraw(Canvas canvas);

        int getWidth();

        int getHeight();

        void setWillNotDraw(boolean flag);

        void invalidate();
    }


}
