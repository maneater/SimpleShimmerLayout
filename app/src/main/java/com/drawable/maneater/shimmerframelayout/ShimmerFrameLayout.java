package com.drawable.maneater.shimmerframelayout;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.widget.FrameLayout;


public class ShimmerFrameLayout extends FrameLayout {

    private ShimmerHelper shimmerHelper = null;

    private static final PorterDuffXfermode DST_IN_PORTER_DUFF_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG
            | Canvas.CLIP_SAVE_FLAG
            | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
            | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
            | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
    private ValueAnimator mAnimator;

    public ShimmerFrameLayout(Context context) {
        this(context, null);
    }

    public ShimmerFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private Paint mPaint = null;
    private float xOffsetFactor = -1f;
    private float maskLengthFactor = 0.5f;

    public ShimmerFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        maskLengthFactor = 0.5f;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {

//        draw alpha base
        canvas.saveLayerAlpha(0, 0, getWidth(), getHeight(), 0x88, LAYER_FLAGS);
        super.dispatchDraw(canvas);
        canvas.restore();


        float left = xOffsetFactor * getWidth();
        float length = getWidth() * maskLengthFactor;

        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, LAYER_FLAGS);
        super.dispatchDraw(canvas);
        Shader shader = new LinearGradient(left, 0, left + length, getHeight(), new int[]{Color.TRANSPARENT, Color.BLACK, Color.TRANSPARENT}, new float[]{0, 0.5f, 1.0f},
                Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        canvas.setDrawFilter(new DrawFilter());
        mPaint.setXfermode(DST_IN_PORTER_DUFF_XFERMODE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        canvas.restore();

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        getShimmerAnimation().start();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
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


}
