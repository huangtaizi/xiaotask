package wang.tai.sun.xiaotask.element;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.MotionEvent;

import wang.tai.sun.xiaotask.game.GameSurfaceView;

public abstract class BaseElement {
    protected GameSurfaceView mSurfaceView;
    protected Resources mRes;
    protected int mScreenWidth;
    protected int mScreenHeight;

    protected Bitmap mBitmap;
    protected float mBitmapWidth;
    protected float mBitmapHeight;

    protected float mLeftPoint;
    protected float mTopPoint;
    protected float mRightPoint;
    protected float mBottomPoint;

    public BaseElement(GameSurfaceView surfaceView) {
        mSurfaceView = surfaceView;
        mRes = surfaceView.getResources();
        mScreenWidth = surfaceView.getWidth();
        mScreenHeight = surfaceView.getHeight();
    }

    protected void initBitmap(int bitmapRes) {
        if (mBitmap == null || mBitmap.isRecycled()) {
            mBitmap = BitmapFactory.decodeResource(mRes, bitmapRes);
            mBitmapWidth = mBitmap.getWidth();
            mBitmapHeight = mBitmap.getHeight();
        }
    }

    protected void initPoint() {
        mRightPoint = mLeftPoint + mBitmapWidth;
        mBottomPoint = mTopPoint + mBitmapHeight;
    }

    public void onGameDraw(Canvas canvas, Paint paint) {
        canvas.drawBitmap(mBitmap, mLeftPoint, mTopPoint, paint);
    }

    public float getTopPoint() {
        return mTopPoint;
    }

    public float getBottomPoint() {
        return mBottomPoint;
    }

    public float getLeftPoint() {
        return mLeftPoint;
    }

    public float getRightPoint() {
        return mRightPoint;
    }

    public abstract void onGameLogic();

    public abstract void onTouchEvent(MotionEvent event);
}
