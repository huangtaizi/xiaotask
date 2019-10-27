package wang.tai.sun.xiaotask.element;

import android.view.MotionEvent;

import wang.tai.sun.xiaotask.R;
import wang.tai.sun.xiaotask.game.GameSurfaceView;

/**
 * 兔子
 */
public class Rabbit extends BaseElement {
    public Rabbit(GameSurfaceView surfaceView) {
        super(surfaceView);
        initBitmap(R.drawable.rabbit);
        mLeftPoint = mScreenWidth - mBitmapWidth - 100;
        mTopPoint = mScreenHeight - mBitmapHeight - 20;
        initPoint();
    }

    @Override
    public void onGameLogic() {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
