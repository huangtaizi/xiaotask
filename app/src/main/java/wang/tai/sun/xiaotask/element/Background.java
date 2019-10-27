package wang.tai.sun.xiaotask.element;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import wang.tai.sun.xiaotask.R;
import wang.tai.sun.xiaotask.game.GameSurfaceView;

public class Background extends BaseElement {
    public Background(GameSurfaceView surfaceView) {
        super(surfaceView);
    }

    @Override
    public void onGameDraw(Canvas canvas, Paint paint) {
        paint.setColor(mRes.getColor(R.color.background));
        canvas.drawRect(0, 0, mScreenWidth, mScreenHeight, paint);
    }

    @Override
    public void onGameLogic() {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
