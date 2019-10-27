package wang.tai.sun.xiaotask.element;

import android.view.MotionEvent;

import wang.tai.sun.xiaotask.R;
import wang.tai.sun.xiaotask.game.GameSurfaceView;

/**
 * 小仙女
 */
public class Fairy extends BaseElement {
    public Fairy(GameSurfaceView surfaceView, float leftPoint, float topPoint) {
        super(surfaceView);
        initBitmap(R.drawable.fairy);
        mLeftPoint = leftPoint;
        mTopPoint = topPoint;
        initPoint();
    }

    @Override
    public void onGameLogic() {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
