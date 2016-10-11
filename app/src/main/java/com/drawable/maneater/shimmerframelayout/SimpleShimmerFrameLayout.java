package com.drawable.maneater.shimmerframelayout;

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


public class SimpleShimmerFrameLayout extends FrameLayout {
    private static final PorterDuffXfermode DST_IN_PORTER_DUFF_XFERMODE = new PorterDuffXfermode(PorterDuff.Mode.DST_IN);

    private static final int LAYER_FLAGS = Canvas.MATRIX_SAVE_FLAG
            | Canvas.CLIP_SAVE_FLAG
            | Canvas.HAS_ALPHA_LAYER_SAVE_FLAG
            | Canvas.FULL_COLOR_LAYER_SAVE_FLAG
            | Canvas.CLIP_TO_LAYER_SAVE_FLAG;
    private ValueAnimator mAnimator;

    public SimpleShimmerFrameLayout(Context context) {
        this(context, null);
    }

    public SimpleShimmerFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private Paint mPaint = null;

    public SimpleShimmerFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    }

    private int xOffset = 0;


    @Override
    protected void dispatchDraw(Canvas canvas) {

//        draw alpha base
        canvas.saveLayerAlpha(0, 0, getWidth(), getHeight(), 0x11, LAYER_FLAGS);
        super.dispatchDraw(canvas);
        canvas.restore();


        xOffset += 10;
//        xOffset = getWidth() / 3;
        int left = (xOffset) % getWidth();
        int length = getWidth() / 2;


        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, LAYER_FLAGS);
//        // draw base rect
        super.dispatchDraw(canvas);
//        // draw alpha rect
        Shader shader = new LinearGradient(left, 0, left + length, getHeight(), new int[]{Color.TRANSPARENT, Color.BLACK, Color.TRANSPARENT}, new float[]{0, 0.5f, 1.0f},
                Shader.TileMode.CLAMP);
        mPaint.setShader(shader);
        canvas.setDrawFilter(new DrawFilter());
        mPaint.setXfermode(DST_IN_PORTER_DUFF_XFERMODE);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
        canvas.restore();

        postInvalidateDelayed(30);
    }

//    private Animator getShimmerAnimation() {
//        if (mAnimator != null) {
//            return mAnimator;
//        }
//        int width = getWidth();
//        int height = getHeight();
//        mAnimator = ValueAnimator.ofFloat(0.0f, 1.0f + (float) mRepeatDelay / mDuration);
//        mAnimator.setDuration(800);
//        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        mAnimator.setRepeatMode(ValueAnimator.RESTART);
//
//        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float value = Math.max(0.0f, Math.min(1.0f, (Float) animation.getAnimatedValue()));
//
//                setMaskOffsetX((int) (mMaskTranslation.fromX * (1 - value) + mMaskTranslation.toX * value));
//                setMaskOffsetY((int) (mMaskTranslation.fromY * (1 - value) + mMaskTranslation.toY * value));
//            }
//        });
//        return mAnimator;
//    }


}
