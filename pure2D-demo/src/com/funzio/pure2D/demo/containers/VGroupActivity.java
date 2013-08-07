package com.funzio.pure2D.demo.containers;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.CheckBox;

import com.funzio.pure2D.BaseScene;
import com.funzio.pure2D.containers.VGroup;
import com.funzio.pure2D.demo.R;
import com.funzio.pure2D.demo.activities.StageActivity;
import com.funzio.pure2D.gl.gl10.GLState;
import com.funzio.pure2D.gl.gl10.textures.Texture;
import com.funzio.pure2D.shapes.Sprite;

public class VGroupActivity extends StageActivity {
    private List<Texture> mTextures = new ArrayList<Texture>();
    private VGroup mContainer;

    /*
     * (non-Javadoc)
     * @see com.funzio.pure2D.samples.activities.StageActivity#getLayout()
     */
    @Override
    protected int getLayout() {
        return R.layout.stage_repeating;
    }

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // to allow swiping
        mScene.setUIEnabled(true);
        mScene.setListener(new BaseScene.Listener() {

            @Override
            public void onSurfaceCreated(final GLState glState, final boolean firstTime) {
                if (firstTime) {

                    // load the textures
                    loadTextures();

                    // generate a lot of squares
                    addGroup(mRandom.nextInt(mDisplaySize.x), mRandom.nextInt(mDisplaySize.y));
                }
            }
        });
    }

    private void loadTextures() {
        final int[] ids = {
                R.drawable.cc_175, // cc
                R.drawable.mw_175, // mw
                R.drawable.ka_175, // ka
        };

        for (int id : ids) {
            // add texture to list
            mTextures.add(mScene.getTextureManager().createDrawableTexture(id, null));
        }
    }

    private void addGroup(final float x, final float y) {
        mContainer = new VGroup();
        mContainer.setGap(50);
        mContainer.setSize(200, mDisplaySize.y);
        mContainer.setSwipeEnabled(true);
        mContainer.setCacheEnabled(true);

        // mContainer.setClippingEnabled(true);
        // mContainer.setDebugFlags(Pure2D.DEBUG_FLAG_GLOBAL_BOUNDS);
        // mContainer.setScale(1.5f);
        // mContainer.setRotation(5);

        for (int n = 0; n < 3; n++) {
            Texture texture = mTextures.get(n % mTextures.size());
            // create object
            Sprite sq = new Sprite();
            sq.setTexture(texture);
            // sq.setSize(200, 100 + n * 30);

            // add to container
            mContainer.addChild(sq);
        }
        mContainer.setPosition(mDisplaySizeDiv2.x - mContainer.getContentSize().x / 2, 0);

        // add to scene
        mScene.addChild(mContainer);
    }

    @Override
    protected int getNumObjects() {
        return mScene.getNumGrandChildren();
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        // forward the event to scene
        mScene.onTouchEvent(event);

        return true;
    }

    public void onClickRepeating(final View view) {
        mContainer.setRepeating(((CheckBox) view).isChecked());
        if (!mContainer.isRepeating()) {
            mContainer.scrollTo(0, 0);
        }
    }
}
