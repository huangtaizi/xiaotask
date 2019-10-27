package wang.tai.sun.xiaotask.element;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import wang.tai.sun.xiaotask.R;
import wang.tai.sun.xiaotask.game.GameSurfaceView;
import wang.tai.sun.xiaotask.model.Point;
import wang.tai.sun.xiaotask.utils.GameState;

/**
 * 糖果
 */
public class Candy extends BaseElement {
    private int mType;
    private float mBottomLimit;
    private int mColor;
    private float mRadius;

    private Random mRandom;
    private List<Point> mPointList;

    private int mOverNum = 0;

    private int mCandyNum;
    private int mRangeSize;

    private float drowSpeen;

    public Candy(GameSurfaceView surfaceView, int type, float topPoint, float bottomLimit, int num, int rangeSize) {
        super(surfaceView);
        mCandyNum = num;
        mRangeSize = rangeSize;
        mRandom = new Random();
        mPointList = new ArrayList<>();
        mType = type;
        mRadius = 15;
        mTopPoint = topPoint;
        if (mType == 1) {
            mColor = mRes.getColor(R.color.candy_a);
            mLeftPoint = mScreenWidth / 4;
        } else {
            mColor = mRes.getColor(R.color.candy_b);
            mLeftPoint = mScreenWidth / 4 * 3;
        }
        mBottomLimit = bottomLimit;
        initPoint();

        drowSpeen = (bottomLimit - topPoint - rangeSize) / 24;
        Log.d("suntaiwang", "candy drowSpeen=" + drowSpeen);
    }

    @Override
    public void onGameDraw(Canvas canvas, Paint paint) {
        paint.setColor(mColor);
        if (mPointList.size() < mCandyNum) {
            mPointList.clear();
            if (mType == 1) {
                mLeftPoint = mScreenWidth / 4 - mRangeSize / 2;
            } else {
                mLeftPoint = mScreenWidth / 4 * 3 - mRangeSize / 2;
            }
            int density = (int) (mRangeSize / (mRadius * 2 + 10));

            for (int i = 0; i < mCandyNum; i++) {
                float x = mRandom.nextInt(density) * (mRadius * 2 + 10) + mLeftPoint;
                float y = mRandom.nextInt(density) * (mRadius * 2 + 10) + mTopPoint;
                Point point = new Point(x, y);

                while (mPointList.contains(point)) {
                    x = mRandom.nextInt(density) * (mRadius * 2 + 10) + mLeftPoint;
                    y = mRandom.nextInt(density) * (mRadius * 2 + 10) + mTopPoint;
                    point = new Point(x, y);
                }
                mPointList.add(point);
            }
        }
        for (Point point : mPointList) {
            canvas.drawCircle(point.x, point.y, mRadius, paint);
        }
    }

    public void onGameOverDraw(Canvas canvas, Paint paint) {
        if (mType == 1) {
            initBitmap(R.drawable.candy_over_a);
        } else {
            initBitmap(R.drawable.candy_over_b);
        }
        mTopPoint = mBottomLimit - mBitmapHeight;
        if (mType == 1) {
            mLeftPoint = mScreenWidth / 4 - mBitmapWidth / 2;
        } else {
            mLeftPoint = mScreenWidth / 4 * 3 - mBitmapWidth / 2;
        }
        initPoint();
        canvas.drawBitmap(mBitmap, mLeftPoint, mTopPoint, paint);
    }

    @Override
    public void onGameLogic() {
        if (mSurfaceView.getGameState() == GameState.NORMAL_ADD_A || mSurfaceView.getGameState() == GameState.NORMAL_ADD_B
                || mSurfaceView.getGameState() == GameState.ABNORMAL_ADD_A) {
            for (Point point : mPointList) {
                point.y += drowSpeen;
                if (point.y > mBottomLimit) {
                    mSurfaceView.nextGameState();
                    return;
                }
            }
        } else if (mSurfaceView.getGameState() == GameState.NORMAL_ADD_A_OVER || mSurfaceView.getGameState() == GameState.NORMAL_ADD_B_OVER
                || mSurfaceView.getGameState() == GameState.ABNORMAL_ADD_A_OVER) {
            if (mOverNum > 6) {
                mSurfaceView.nextGameState();
                return;
            }
            mOverNum++;
        }
    }

    @Override
    public void onTouchEvent(MotionEvent event) {

    }
}
