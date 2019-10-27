package wang.tai.sun.xiaotask.element;

import android.view.MotionEvent;

import wang.tai.sun.xiaotask.R;
import wang.tai.sun.xiaotask.game.GameSurfaceView;

/**
 * 乌龟
 */
public class Tortoise extends BaseElement {
    public Tortoise(GameSurfaceView surfaceView) {
        super(surfaceView);
        initBitmap(R.drawable.tortoise);
        mLeftPoint = 100;
        mTopPoint = mScreenHeight - mBitmapHeight;
        initPoint();
    }

    @Override
    public void onGameLogic() {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
