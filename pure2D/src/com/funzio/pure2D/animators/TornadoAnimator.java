/**
 * 
 */
package com.funzio.pure2D.animators;

import android.graphics.PointF;
import android.view.animation.Interpolator;

/**
 * @author long
 */
public class TornadoAnimator extends TweenAnimator {
    // default values
    public static final int DEFAULT_CIRLCLE_NUM = 10;
    public static final float DEFAULT_CIRCLE_RATIO = 0.25f;
    public static final int DEFAULT_CIRCLE_RADIUS = 100;

    protected float mSrcX = 0;
    protected float mSrcY = 0;
    protected PointF mDelta = new PointF();

    private float mCircleRadius;
    private float mCircleRatio;
    private float mCircleNum;
    private boolean mCircleDirection;
    private Interpolator mCircleInterpolator;
    private float mRadianLength;

    private float mLastX;
    private float mLastY;
    private float mLengthX;
    private float mLengthY;
    private float mSinAngle;
    private float mCosAngle;

    public TornadoAnimator(final Interpolator interpolator) {
        super(interpolator);

        reset();
    }

    @Override
    public void reset(final Object... params) {
        super.reset(params);

        // just set some default params
        mCircleRadius = DEFAULT_CIRCLE_RADIUS;
        mCircleRatio = DEFAULT_CIRCLE_RATIO;
        mCircleNum = DEFAULT_CIRLCLE_NUM;
        mCircleDirection = true;

        // find implicit radian length
        mRadianLength = (float) Math.PI * (mCircleNum * 2) * (mCircleDirection ? 1 : -1);

        mLastX = mLastY = 0;
    }

    public Interpolator getCircleInterpolator() {
        return mCircleInterpolator;
    }

    public void setCircleInterpolator(final Interpolator CircleInterpolator) {
        mCircleInterpolator = CircleInterpolator;
    }

    public boolean isCircleDirection() {
        return mCircleDirection;
    }

    public void setCircleDirection(final boolean circleDirection) {
        mCircleDirection = circleDirection;

        // find implicit radian length
        mRadianLength = (float) Math.PI * (mCircleNum * 2) * (mCircleDirection ? 1 : -1);
    }

    public void setValues(final float srcX, final float srcY, final float dstX, final float dstY) {
        mSrcX = srcX;
        mSrcY = srcY;

        // apply the delta
        setDelta(dstX - srcX, dstY - srcY);
    }

    public void setCircles(final float circleRadius, final float circleNum, final float circleRatio, final Interpolator CircleInterpolator) {
        mCircleRadius = circleRadius != 0 ? circleRadius : DEFAULT_CIRCLE_RADIUS;
        mCircleNum = circleNum != 0 ? circleNum : DEFAULT_CIRLCLE_NUM;
        mCircleRatio = circleRatio != 0 ? circleRatio : DEFAULT_CIRCLE_RATIO;
        mCircleInterpolator = CircleInterpolator;

        // find implicit radian length
        mRadianLength = (float) Math.PI * (mCircleNum * 2) * (mCircleDirection ? 1 : -1);

        // travel length for the center
        mLengthX = mDelta.x - mCircleRadius * mCircleRatio * mCosAngle;
        mLengthY = mDelta.y - mCircleRadius * mCircleRatio * mSinAngle;
    }

    public void setDelta(final float dx, final float dy) {
        mDelta.x = dx;
        mDelta.y = dy;

        // pre-calculate
        final float angle = (float) Math.atan2(mDelta.y, mDelta.x);
        mSinAngle = (float) Math.sin(angle);
        mCosAngle = (float) Math.cos(angle);
        // travel length for the center
        mLengthX = mDelta.x - mCircleRadius * mCircleRatio * mCosAngle;
        mLengthY = mDelta.y - mCircleRadius * mCircleRatio * mSinAngle;
    }

    public void start(final float srcX, final float srcY, final float dstX, final float dstY) {
        setValues(srcX, srcY, dstX, dstY);

        start();
    }

    public void start(final float destX, final float destY) {
        if (mTarget != null) {
            final PointF position = mTarget.getPosition();
            start(position.x, position.y, destX, destY);
        }
    }

    public float getCircleRadius() {
        return mCircleRadius;
    }

    public float getCircleNum() {
        return mCircleNum;
    }

    public float getCircleRatio() {
        return mCircleRatio;
    }

    public PointF getDelta() {
        return mDelta;
    }

    @Override
    protected void onUpdate(final float value) {
        if (mTarget != null) {

            final float centerX = value * mLengthX;
            final float centerY = value * mLengthY;
            final float angle = value * mRadianLength;
            final float radius = mCircleRadius * (mCircleInterpolator == null ? value : mCircleInterpolator.getInterpolation(value));

            final float dx = radius * mCircleRatio * (float) Math.cos(angle);
            final float dy = radius * (float) Math.sin(angle);
            // rotate to the main direction
            final float newX = centerX + dx * mCosAngle - dy * mSinAngle;
            final float newY = centerY + dx * mSinAngle + dy * mCosAngle;

            if (mAccumulating) {
                mTarget.moveBy(newX - mLastX, newY - mLastY);
            } else {
                mTarget.setPosition(mSrcX + newX, mSrcY + newY);
            }
            mLastX = newX;
            mLastY = newY;
        }

        super.onUpdate(value);
    }
}
