package wang.tai.sun.xiaotask.element;

import android.view.MotionEvent;

import wang.tai.sun.xiaotask.R;
import wang.tai.sun.xiaotask.game.GameSurfaceView;

/**
 * 开关阀门
 */
public class Valve extends BaseElement {
    public Valve(GameSurfaceView surfaceView) {
        super(surfaceView);
        initBitmap(R.drawable.valve);
        mTopPoint = 50;
        mLeftPoint = (mScreenWidth - mBitmapWidth) / 2;
        initPoint();
    }

    @Override
    public void onGameLogic() {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        if (touchX > mLeftPoint && touchX < mRightPoint && touchY < mBottomPoint && touchY > mTopPoint) {
            mSurfaceView.nextGameState();
        }
    }
}
