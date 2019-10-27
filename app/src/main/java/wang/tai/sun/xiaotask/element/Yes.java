package wang.tai.sun.xiaotask.element;

import android.view.MotionEvent;

import wang.tai.sun.xiaotask.R;
import wang.tai.sun.xiaotask.game.GameSurfaceView;

public class Yes extends BaseElement {
    private int mOverNum = 0;

    public Yes(GameSurfaceView surfaceView) {
        super(surfaceView);
        initBitmap(R.drawable.yes);
        mTopPoint = (mScreenHeight - mBitmapHeight) / 2;
        mLeftPoint = (mScreenWidth - mBitmapWidth) / 2;
        initPoint();
    }

    @Override
    public void onGameLogic() {
        if (mOverNum > 20) {
            mSurfaceView.nextGameState();
            return;
        }
        mOverNum++;
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
