package wang.tai.sun.xiaotask.element;

import android.util.Log;
import android.view.MotionEvent;

import wang.tai.sun.xiaotask.R;
import wang.tai.sun.xiaotask.game.GameSurfaceView;

public class ReduceFairy extends BaseElement {
    private float upSpeen;

    public ReduceFairy(GameSurfaceView surfaceView, float topPoint, float leftPoint, int rangeSize) {
        super(surfaceView);
        initBitmap(R.drawable.fairy);
        mTopPoint = topPoint;
        mLeftPoint = leftPoint;
        initPoint();
        upSpeen = (mTopPoint + rangeSize) / 35;

        Log.d("suntaiwang", "candy upSpeen=" + upSpeen);
    }

    @Override
    public void onGameLogic() {
        mTopPoint -= upSpeen;
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
