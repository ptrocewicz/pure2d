/**
 * 
 */
package com.funzio.pure2D.particles.nova.vo;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.funzio.pure2D.Manipulatable;
import com.funzio.pure2D.animators.Animator;
import com.funzio.pure2D.animators.ScaleAnimator;
import com.funzio.pure2D.particles.nova.NovaConfig;

/**
 * @author long
 */
public class ScaleAnimatorVO extends TweenAnimatorVO {

    public List<Float> x_from;
    public List<Float> x_to;
    public List<Float> y_from;
    public List<Float> y_to;

    public ScaleAnimatorVO() {
        super();
    }

    public ScaleAnimatorVO(final JSONObject json) throws JSONException {
        super(json);

        x_from = NovaVO.getListFloat(json.optJSONArray("x_from"));
        x_to = NovaVO.getListFloat(json.optJSONArray("x_to"));
        y_from = NovaVO.getListFloat(json.optJSONArray("y_from"));
        y_to = NovaVO.getListFloat(json.optJSONArray("y_to"));
    }

    @Override
    public Animator createAnimator(final Manipulatable target, final Animator... animators) {
        return init(target, new ScaleAnimator(NovaConfig.getInterpolator(interpolation)));
    }

    @Override
    public void resetAnimator(final Manipulatable target, final Animator animator) {
        super.resetAnimator(target, animator);

        final ScaleAnimator scale = (ScaleAnimator) animator;
        if (scale != null) {
            scale.setValues(x_from != null ? NovaConfig.getRandomFloat(x_from) : 1, //
                    y_from != null ? NovaConfig.getRandomFloat(y_from) : 1, //
                    x_to != null ? NovaConfig.getRandomFloat(x_to) : 1, //
                    y_to != null ? NovaConfig.getRandomFloat(y_to) : 1);

            scale.setDuration(NovaConfig.getRandomInt(duration));
        }
    }
}
