package wang.tai.sun.xiaotask.element;

import android.view.MotionEvent;

import wang.tai.sun.xiaotask.R;
import wang.tai.sun.xiaotask.game.GameSurfaceView;

/**
 * 机器
 */
public class Machine extends BaseElement {
    private int mType;

    public Machine(GameSurfaceView surfaceView, int type) {
        super(surfaceView);
        mType = type;

        initBitmap(R.drawable.machine);
        if (mType == 1) {
            mLeftPoint = (mScreenWidth / 4) - mBitmapWidth / 2;
        } else {
            mLeftPoint = (mScreenWidth / 4 * 3) - mBitmapWidth / 2;
        }
        initPoint();
    }

    @Override
    public void onGameLogic() {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
