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

    public void onGameEnd(Canvas canvas, Paint paint) {
        paint.setColor(mRes.getColor(R.color.background));
        canvas.drawRect(0, 0, mScreenWidth, mScreenHeight, paint);

        initBitmap(R.drawable.back_end);
        initPoint();
        super.onGameDraw(canvas, paint);

        paint.setColor(mRes.getColor(R.color.back_text));
        paint.setTextSize(24);
        canvas.drawText("作者：梁小爷 & 孙大爷", 50, 100, paint);
        canvas.drawText("备注：小朋友，不要怪哥哥，我也是被逼无奈，呜呜...呜..", 50, 130, paint);
        canvas.drawText("你们终于解放啦，太艰难！！！咋这么多题！！！", 50, 160, paint);
    }

    @Override
    public void onGameLogic() {

    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
