package wang.tai.sun.xiaotask.element;

import android.view.MotionEvent;

import wang.tai.sun.xiaotask.R;
import wang.tai.sun.xiaotask.game.GameSurfaceView;

/**
 * 盆
 */
public class Basin extends BaseElement {
    private int mType;

    public Basin(GameSurfaceView surfaceView, int type) {
        super(surfaceView);
        mType = type;
        initBitmap(R.drawable.basin);
        if (mType == 1) {
            mLeftPoint = (mScreenWidth / 4) - mBitmapWidth / 2;
        } else {
            mLeftPoint = (mScreenWidth / 4 * 3) - mBitmapWidth / 2;
        }
        mTopPoint = mScreenHeight - mBitmapHeight;
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
            mSurfaceView.nextGame(mType);
        }
    }
}
