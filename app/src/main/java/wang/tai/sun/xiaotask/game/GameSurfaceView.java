package wang.tai.sun.xiaotask.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import wang.tai.sun.xiaotask.MainActivity;
import wang.tai.sun.xiaotask.element.Background;
import wang.tai.sun.xiaotask.element.Basin;
import wang.tai.sun.xiaotask.element.Candy;
import wang.tai.sun.xiaotask.element.Fairy;
import wang.tai.sun.xiaotask.element.FlyCandy;
import wang.tai.sun.xiaotask.element.Machine;
import wang.tai.sun.xiaotask.element.Rabbit;
import wang.tai.sun.xiaotask.element.ReduceFairy;
import wang.tai.sun.xiaotask.element.Tortoise;
import wang.tai.sun.xiaotask.element.Valve;
import wang.tai.sun.xiaotask.model.CandyModle;
import wang.tai.sun.xiaotask.utils.CofUtils;
import wang.tai.sun.xiaotask.utils.GameState;

public class GameSurfaceView extends SurfaceView implements GameContract.View, SurfaceHolder.Callback {
    private GameState mCurrentState;

    private SurfaceHolder mHolder;

    private Canvas mCanvas;
    private Paint mPaint;

    private Thread mDrawThread;
    private boolean mDrawFlag;

    private Background mBackground;
    private Machine mMachineA;
    private Machine mMachineB;
    private Candy mCandyA;
    private Candy mCandyB;
    private Candy mAddCandy;
    private FlyCandy mReduceCandy;
    private Valve mValve;
    private Basin mBasinA;
    private Basin mBasinB;
    private Rabbit mRabbit;
    private Tortoise mTortoise;
    private Fairy mFairy;
    private ReduceFairy mReduceFairy;

    private CandyModle mCurrentCandyModle;
    private int mCurrentCandyPoint = -1;

    public GameSurfaceView(Context context) {
        super(context);
    }

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GameSurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void initView() {
        setKeepScreenOn(true);
        mHolder = getHolder();
        mHolder.addCallback(this);

        mHolder.setFormat(PixelFormat.TRANSPARENT);
        setZOrderOnTop(true);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    public void onCreate() {
        initView();
    }

    @Override
    public void onStart() {
        if (initGame()) {
            mDrawFlag = true;
            mDrawThread = new Thread(new DrawRunnalbe());
            mDrawThread.start();
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    private boolean initGame() {

        if (CofUtils.candyModleList == null || CofUtils.candyModleList.size() <= 0) {
            Toast.makeText(getContext(), "配置糖果 <= 0", Toast.LENGTH_LONG).show();
            mCurrentState = GameState.END;
            return false;
        }

        if (CofUtils.candyModleList.size() > mCurrentCandyPoint + 1) {
            if (mCurrentCandyPoint == -1) {
                Toast.makeText(getContext(), "开始游戏", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "下一关", Toast.LENGTH_LONG).show();
            }
            mCurrentCandyModle = CofUtils.candyModleList.get(mCurrentCandyPoint + 1);
            Log.d("suntaiwang", mCurrentCandyModle + " start");
            mCurrentCandyPoint++;
        } else {
            Toast.makeText(getContext(), "游戏结束", Toast.LENGTH_LONG).show();
            mCurrentState = GameState.END;
            return false;
        }

        mCurrentState = GameState.READY;

        mBackground = new Background(this);
        mMachineA = new Machine(this, 1);
        mMachineB = new Machine(this, 2);
        mBasinA = new Basin(this, 1);
        mBasinB = new Basin(this, 2);
        mCandyA = new Candy(this, 1, mMachineA.getBottomPoint(), mBasinA.getTopPoint(), mCurrentCandyModle.a, mCurrentCandyModle.sizeA);
        mCandyB = new Candy(this, 2, mMachineB.getBottomPoint(), mBasinA.getTopPoint(), mCurrentCandyModle.b, mCurrentCandyModle.sizeB);
        mAddCandy = new Candy(this, 1, mMachineA.getBottomPoint(), mBasinA.getTopPoint(), mCurrentCandyModle.add, mCurrentCandyModle.sizeAdd);
        mReduceCandy = new FlyCandy(this, mBasinA.getTopPoint(), mCurrentCandyModle.reduce, mCurrentCandyModle.sizeReduce);
        mValve = new Valve(this);
        mRabbit = new Rabbit(this);
        mTortoise = new Tortoise(this);
        mFairy = new Fairy(this, getWidth() / 4 + 200, mMachineA.getBottomPoint());
        mReduceFairy = new ReduceFairy(this, mBasinA.getTopPoint(), mBasinA.getRightPoint(), mCurrentCandyModle.sizeReduce);
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mDrawFlag = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (mCurrentState) {
            case READY:
                mValve.onTouchEvent(event);
                break;
            case DROP_END:
                mBasinA.onTouchEvent(event);
                mBasinB.onTouchEvent(event);
                break;
            case END:
                // TODO: 2019/10/19 未知状态
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 绘制界面
     *
     * @param canvas
     */
    private void onGameDraw(Canvas canvas, Paint paint) {
        //清屏
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);

        mBackground.onGameDraw(canvas, paint);
        mMachineA.onGameDraw(canvas, paint);
        mMachineB.onGameDraw(canvas, paint);
        mBasinA.onGameDraw(canvas, paint);
        mBasinB.onGameDraw(canvas, paint);
        mRabbit.onGameDraw(canvas, paint);
        mTortoise.onGameDraw(canvas, paint);

        switch (mCurrentState) {
            case READY:
                mValve.onGameDraw(canvas, paint);
                break;
            case NORMAL_ADD_A:
                mCandyA.onGameDraw(canvas, paint);
                break;
            case NORMAL_ADD_A_OVER:
                mCandyA.onGameOverDraw(canvas, paint);
                break;
            case NORMAL_ADD_B:
                mCandyB.onGameDraw(canvas, paint);
                break;
            case NORMAL_ADD_B_OVER:
                mCandyB.onGameOverDraw(canvas, paint);
                break;
            case ABNORMAL_ADD_A:
                mFairy.onGameDraw(canvas, paint);
                mAddCandy.onGameDraw(canvas, paint);
                break;
            case ABNORMAL_ADD_A_OVER:
                mFairy.onGameDraw(canvas, paint);
                mAddCandy.onGameOverDraw(canvas, paint);
                break;
            case ABNORMAL_REDUCE_A:
                mReduceFairy.onGameDraw(canvas, paint);
                mReduceCandy.onGameDraw(canvas, paint);
                break;
            case END:
                // TODO: 2019/10/19 未知
                break;
            default:
                break;
        }
    }

    /**
     * 处理逻辑性问题
     */
    private void onLogic() {
        switch (mCurrentState) {
            case READY:
                break;
            case NORMAL_ADD_A:
                mCandyA.onGameLogic();
                break;
            case NORMAL_ADD_A_OVER:
                mCandyA.onGameLogic();
                break;
            case NORMAL_ADD_B:
                mCandyB.onGameLogic();
                break;
            case NORMAL_ADD_B_OVER:
                mCandyB.onGameLogic();
                break;
            case ABNORMAL_ADD_A:
                mFairy.onGameLogic();
                mAddCandy.onGameLogic();
                break;
            case ABNORMAL_ADD_A_OVER:
                mFairy.onGameLogic();
                mAddCandy.onGameLogic();
                break;
            case ABNORMAL_REDUCE_A:
                mReduceFairy.onGameLogic();
                mReduceCandy.onGameLogic();
                break;
            case END:
                saveGameResult();
                mDrawFlag = false;
                break;
            default:
                break;
        }
    }

    public GameState getGameState() {
        return mCurrentState;
    }

    public void nextGameState() {
        GameState state = mCurrentState;
        if (state == GameState.READY) {
            mCurrentState = GameState.NORMAL_ADD_A;
        } else if (state == GameState.NORMAL_ADD_A) {
            mCurrentState = GameState.NORMAL_ADD_A_OVER;
        } else if (state == GameState.NORMAL_ADD_A_OVER) {
            if (mCurrentCandyModle.add > 0) {
                mCurrentState = GameState.ABNORMAL_ADD_A;
            } else if (mCurrentCandyModle.reduce > 0) {
                mCurrentState = GameState.ABNORMAL_REDUCE_A;
            } else {
                mCurrentState = GameState.NORMAL_ADD_B;
            }
        } else if (state == GameState.NORMAL_ADD_B) {
            mCurrentState = GameState.NORMAL_ADD_B_OVER;
        } else if (state == GameState.NORMAL_ADD_B_OVER) {
            mCurrentState = GameState.DROP_END;
        } else if (state == GameState.ABNORMAL_ADD_A) {
            mCurrentState = GameState.ABNORMAL_ADD_A_OVER;
        } else if (state == GameState.ABNORMAL_ADD_A_OVER) {
            mCurrentState = GameState.NORMAL_ADD_B;
        } else if (state == GameState.ABNORMAL_REDUCE_A) {
            mCurrentState = GameState.NORMAL_ADD_B;
        }
        Log.d("suntaiwang", "state=" + mCurrentState.name());
    }


    public void nextGame(int type) {
        mCurrentCandyModle.result = type;
        Log.d("suntaiwang", mCurrentCandyModle + " end");
        initGame();
    }

    private void saveGameResult() {
        Log.d("suntaiwang", "开始保存结果");
        String resultJson = MainActivity.mGson.toJson(CofUtils.candyModleList);
        SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String fileName = format.format(new Date()) + ".json";
        Log.d("suntaiwang", "fileName=" + fileName);
        File resultFile = new File(Environment.getExternalStorageDirectory(), fileName);
        FileWriter fileWriter = null;
        try {
            if (resultFile.exists()) {
                resultFile.delete();
            }
            resultFile.createNewFile();

            fileWriter = new FileWriter(resultFile);
            fileWriter.write(resultJson);
        } catch (IOException e) {
            Log.d("suntaiwang", "保存结果失败" + e.toString());
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (IOException e) {
                    Log.d("suntaiwang", "保存结果失败" + e.toString());
                }
            }
        }
    }

    /**
     * 绘制线程
     */
    public class DrawRunnalbe implements Runnable {

        @Override
        public void run() {
            while (mDrawFlag) {
                long startTime = System.currentTimeMillis();

                mCanvas = mHolder.lockCanvas();
                if (mCanvas != null) {
                    onGameDraw(mCanvas, mPaint);
                    mHolder.unlockCanvasAndPost(mCanvas);
                }
                onLogic();

                long endTime = System.currentTimeMillis();
                if (endTime - startTime < CofUtils.THREAD_TIME) {
                    try {
                        Thread.sleep(CofUtils.THREAD_TIME - (endTime - startTime));
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}