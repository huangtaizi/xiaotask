package wang.tai.sun.xiaotask.element;

import android.view.MotionEvent;

import wang.tai.sun.xiaotask.R;
import wang.tai.sun.xiaotask.game.GameSurfaceView;

public class No extends BaseElement {
    private int mOverNum = 0;

    public No(GameSurfaceView surfaceView) {
        super(surfaceView);
        initBitmap(R.drawable.no);
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
