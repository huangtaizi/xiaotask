package wang.tai.sun.xiaotask.element;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wang.tai.sun.xiaotask.R;
import wang.tai.sun.xiaotask.game.GameSurfaceView;
import wang.tai.sun.xiaotask.model.Point;
import wang.tai.sun.xiaotask.utils.GameState;

public class FlyCandy extends BaseElement {
    private int mType;
    private float mBottomLimit;
    private int mColor;
    private float mRadius;

    private Random mRandom;
    private List<Point> mPointList;

    private int mCandyNum;
    private int mRangeSize;
    private float upSpeen;

    public FlyCandy(GameSurfaceView surfaceView, float bottomPoint, int num, int rangeSize) {
        super(surfaceView);
        mRandom = new Random();
        mPointList = new ArrayList<>();
        mRadius = 15;
        mColor = mRes.getColor(R.color.candy_a);
        mBottomPoint = bottomPoint;
        mCandyNum = num;
        mRangeSize = rangeSize;
        upSpeen = (bottomPoint + rangeSize) / 24;
    }

    @Override
    public void onGameDraw(Canvas canvas, Paint paint) {
        paint.setColor(mColor);
        if (mPointList.size() < mCandyNum) {
            mPointList.clear();
            mLeftPoint = mScreenWidth / 4 - mRangeSize / 2;
            int density = (int) (mRangeSize / (mRadius * 2 + 10));

            for (int i = 0; i < mCandyNum; i++) {
                float x = mRandom.nextInt(density) * (mRadius * 2 + 10) + mLeftPoint;
                float y = mBottomPoint - mRandom.nextInt(density) * (mRadius * 2 + 10);
                Point point = new Point(x, y);

                while (mPointList.contains(point)) {
                    x = mRandom.nextInt(density) * (mRadius * 2 + 10) + mLeftPoint;
                    y = mBottomPoint - mRandom.nextInt(density) * (mRadius * 2 + 10);
                    point = new Point(x, y);
                }
                mPointList.add(point);
            }
        }
        for (Point point : mPointList) {
            canvas.drawCircle(point.x, point.y, mRadius, paint);
        }
    }

    @Override
    public void onGameLogic() {
        if (mSurfaceView.getGameState() == GameState.ABNORMAL_REDUCE_A) {
            for (Point point : mPointList) {
                point.y -= upSpeen;
                if (point.y < 0) {
                    mSurfaceView.nextGameState();
                    return;
                }
            }
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
