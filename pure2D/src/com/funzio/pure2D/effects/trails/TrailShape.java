/**
 * 
 */
package com.funzio.pure2D.effects.trails;

import android.graphics.PointF;
import android.util.FloatMath;

import com.funzio.pure2D.DisplayObject;
import com.funzio.pure2D.Scene;
import com.funzio.pure2D.shapes.Polyline;

/**
 * @author long
 */
public class TrailShape extends Polyline {

    protected int mNumPoints = 0;
    protected int mMinLength = 0;
    protected int mSegmentLength;
    protected float mMotionEasing = 0.5f;

    protected DisplayObject mTarget;
    protected PointF mTargetOffset = new PointF(0, 0);

    /*
     * (non-Javadoc)
     * @see com.funzio.pure2D.BaseDisplayObject#setPosition(float, float)
     */
    @Override
    public void setPosition(final float x, final float y) {
        if (mNumPoints > 0) {
            mPoints[0].set(x, y);
        }
    }

    /*
     * (non-Javadoc)
     * @see com.funzio.pure2D.BaseDisplayObject#update(int)
     */
    @Override
    public boolean update(final int deltaTime) {
        if (mNumPoints > 0) {

            // calculate time loop
            final int loop = deltaTime / Scene.DEFAULT_MSPF;
            for (int n = 0; n < loop; n++) {
                PointF p1, p2;
                float dx, dy;
                for (int i = mNumPoints - 1; i > 0; i--) {
                    p1 = mPoints[i];
                    p2 = mPoints[i - 1];
                    dx = p2.x - p1.x;
                    dy = p2.y - p1.y;
                    if (mSegmentLength == 0 || FloatMath.sqrt(dx * dx + dy * dy) > mSegmentLength) {
                        p1.x += dx * mMotionEasing;
                        p1.y += dy * mMotionEasing;
                    }
                }
            }

            // follow the target
            if (mTarget != null) {
                // set the head
                final PointF pos = mTarget.getPosition();
                mPoints[0].set(pos.x + mTargetOffset.x, pos.y + mTargetOffset.y);
            }

            // apply
            setPoints(mPoints);

            return true;
        }

        return false;
    }

    public int getNumPoints() {
        return mNumPoints;
    }

    public void setNumPoints(final int numPoints) {
        mNumPoints = numPoints;

        if (numPoints < 2) {
            mPoints = null;
            return;
        }

        if (mPoints == null || mPoints.length != numPoints) {
            mPoints = new PointF[numPoints];

            final PointF pos = (mTarget != null) ? mTarget.getPosition() : null;
            for (int i = 0; i < numPoints; i++) {
                mPoints[i] = new PointF();

                if (pos != null) {
                    mPoints[i].set(pos.x + mTargetOffset.x, pos.y + mTargetOffset.y);
                }
            }

            // find the
            mSegmentLength = mMinLength / (numPoints - 1);
        }

        // re-count
        mVerticesNum = numPoints * 2;
    }

    public DisplayObject getTarget() {
        return mTarget;
    }

    public void setTarget(final DisplayObject target) {
        mTarget = target;

        if (mTarget != null) {
            final PointF pos = mTarget.getPosition();
            for (int i = 0; i < mNumPoints; i++) {
                mPoints[i].set(pos.x + mTargetOffset.x, pos.y + mTargetOffset.y);
            }
        }
    }

    public int getMinLength() {
        return mMinLength;
    }

    public void setMinLength(final int totalLength) {
        mMinLength = totalLength;
        mSegmentLength = mMinLength / (mNumPoints < 2 ? 1 : mNumPoints - 1);
    }

    public float getMotionEasing() {
        return mMotionEasing;
    }

    /**
     * @param easing, must be from 0 to 1
     */
    public void setMotionEasing(final float easing) {
        mMotionEasing = easing;
    }

}