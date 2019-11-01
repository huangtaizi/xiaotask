package wang.tai.sun.xiaotask.game;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Environment;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseIntArray;
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
import wang.tai.sun.xiaotask.R;
import wang.tai.sun.xiaotask.element.Background;
import wang.tai.sun.xiaotask.element.Basin;
import wang.tai.sun.xiaotask.element.Candy;
import wang.tai.sun.xiaotask.element.Fairy;
import wang.tai.sun.xiaotask.element.FlyCandy;
import wang.tai.sun.xiaotask.element.Machine;
import wang.tai.sun.xiaotask.element.No;
import wang.tai.sun.xiaotask.element.Rabbit;
import wang.tai.sun.xiaotask.element.ReduceFairy;
import wang.tai.sun.xiaotask.element.Tortoise;
import wang.tai.sun.xiaotask.element.Valve;
import wang.tai.sun.xiaotask.element.Yes;
import wang.tai.sun.xiaotask.model.CandyModle;
import wang.tai.sun.xiaotask.utils.CofUtils;
import wang.tai.sun.xiaotask.utils.GameState;

public class GameSurfaceView extends SurfaceView implements GameContract.View, SurfaceHolder.Callback {
    static final float FULL_LEFT_VOLUME = 1.0f;
    static final float FULL_RIGHT_VOLUME = 1.0f;
    static final int DEFAULT_PRIORITY = 1;
    static final int DO_NOT_LOOP = 0;
    static final float DEFAULT_RATE = 1.0f;

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
    private Yes mYes;
    private No mNO;

    private CandyModle mCurrentCandyModle;
    private int mCurrentCandyPoint = -1;

    private boolean isInitGameSucess = false;

    SoundPool mSoundPool;
    private Handler mMusicHandle = new Handler();
    SparseIntArray mSoundMap = new SparseIntArray();

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

        mSoundPool = new SoundPool(2, AudioManager.STREAM_SYSTEM, 0);
        loadSounds(getContext());
    }

    private void loadSounds(Context context) {
        int[] sounds = {
                R.raw.music_valve,
                R.raw.music_success,
                R.raw.music_fairy_up,
                R.raw.music_fairy_down,
                R.raw.music_erre,
                R.raw.music_candy_in_basin
        };
        for (int sound : sounds) {
            mSoundMap.put(sound, mSoundPool.load(context, sound, 1));
        }
    }

    private void playMusic(final int resId) {
        mMusicHandle.post(new Runnable() {
            @Override
            public void run() {
                int sound = mSoundMap.get(resId);
                mSoundPool.play(sound, FULL_LEFT_VOLUME, FULL_RIGHT_VOLUME, DEFAULT_PRIORITY,
                        DO_NOT_LOOP, DEFAULT_RATE);
            }
        });
    }

    @Override
    public void onCreate() {
        initView();
    }

    @Override
    public void onStart() {
        isInitGameSucess = initGame();
        if (isInitGameSucess) {
            mDrawFlag = true;
            mDrawThread = new Thread(new DrawRunnalbe());
            mDrawThread.start();
        }
    }

    @Override
    public void onResume() {
        if (isInitGameSucess) {
            mDrawFlag = true;
            mDrawThread = new Thread(new DrawRunnalbe());
            mDrawThread.start();
        }
    }

    @Override
    public void onPause() {
        mDrawFlag = false;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    private void showToast(final String test) {
        mMusicHandle.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getContext(), test, Toast.LENGTH_LONG).show();
            }
        });
    }

    private boolean initGame() {

        if (CofUtils.candyModleList == null || CofUtils.candyModleList.size() <= 0) {
            showToast("配置糖果 <= 0");
            mCurrentState = GameState.END;
            return false;
        }

        if (CofUtils.candyModleList.size() > mCurrentCandyPoint + 1) {
            if (mCurrentCandyPoint == -1) {
                showToast("开始游戏");
            } else {
                showToast("下一关");
            }
            mCurrentCandyModle = CofUtils.candyModleList.get(mCurrentCandyPoint + 1);
            Log.d("suntaiwang", mCurrentCandyModle + " start");
            mCurrentCandyPoint++;
        } else {
            showToast("游戏结束");
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
        mYes = new Yes(this);
        mNO = new No(this);
        return true;
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mSoundPool.release();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mCurrentState == null) {
            return super.onTouchEvent(event);
        }
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
            case YES:
                mYes.onGameDraw(canvas, paint);
                break;
            case NO:
                mNO.onGameDraw(canvas, paint);
                break;
            case END:
                mBackground.onGameEnd(canvas, paint);
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
            case YES:
                mYes.onGameLogic();
                break;
            case NO:
                mNO.onGameLogic();
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
            playMusic(R.raw.music_valve);
            mCurrentState = GameState.NORMAL_ADD_A;
        } else if (state == GameState.NORMAL_ADD_A) {
            playMusic(R.raw.music_candy_in_basin);
            mCurrentState = GameState.NORMAL_ADD_A_OVER;
        } else if (state == GameState.NORMAL_ADD_A_OVER) {
            if (mCurrentCandyModle.add > 0) {
                playMusic(R.raw.music_fairy_down);
                mCurrentState = GameState.ABNORMAL_ADD_A;
            } else if (mCurrentCandyModle.reduce > 0) {
                playMusic(R.raw.music_fairy_up);
                mCurrentState = GameState.ABNORMAL_REDUCE_A;
            } else {
                mCurrentState = GameState.NORMAL_ADD_B;
            }
        } else if (state == GameState.NORMAL_ADD_B) {
            playMusic(R.raw.music_candy_in_basin);
            mCurrentState = GameState.NORMAL_ADD_B_OVER;
        } else if (state == GameState.NORMAL_ADD_B_OVER) {
            mCurrentState = GameState.DROP_END;
        } else if (state == GameState.ABNORMAL_ADD_A) {
            playMusic(R.raw.music_candy_in_basin);
            mCurrentState = GameState.ABNORMAL_ADD_A_OVER;
        } else if (state == GameState.ABNORMAL_ADD_A_OVER) {
            mCurrentState = GameState.NORMAL_ADD_B;
        } else if (state == GameState.ABNORMAL_REDUCE_A) {
            mCurrentState = GameState.NORMAL_ADD_B;
        } else if (state == GameState.YES || state == GameState.NO) {
            initGame();
        }
        Log.d("suntaiwang", "state=" + mCurrentState.name());
    }


    public void nextGame(int type) {
        if (CofUtils.GameType == 1) {//比较实验
            if (mCurrentCandyModle.a > mCurrentCandyModle.b) {//选a正确
                if (type == 1) {
                    mCurrentCandyModle.result = 1;
                } else {
                    mCurrentCandyModle.result = 0;
                }
            } else {//选b正确
                if (type == 1) {
                    mCurrentCandyModle.result = 0;
                } else {
                    mCurrentCandyModle.result = 1;
                }
            }
        } else {//计算实验
            if (mCurrentCandyModle.add > 0) {//加法
                if (mCurrentCandyModle.a + mCurrentCandyModle.add > mCurrentCandyModle.b) {//选a正确
                    if (type == 1) {
                        mCurrentCandyModle.result = 1;
                    } else {
                        mCurrentCandyModle.result = 0;
                    }
                } else {//选b正确
                    if (type == 1) {
                        mCurrentCandyModle.result = 0;
                    } else {
                        mCurrentCandyModle.result = 1;
                    }
                }
            } else {//减法
                if (mCurrentCandyModle.a - mCurrentCandyModle.reduce > mCurrentCandyModle.b) {//选a正确
                    if (type == 1) {
                        mCurrentCandyModle.result = 1;
                    } else {
                        mCurrentCandyModle.result = 0;
                    }
                } else {//选b正确
                    if (type == 1) {
                        mCurrentCandyModle.result = 0;
                    } else {
                        mCurrentCandyModle.result = 1;
                    }
                }
            }
        }
        if (mCurrentCandyModle.result == 1) {//正确
            playMusic(R.raw.music_success);
            mCurrentState = GameState.YES;
        } else {//错误
            playMusic(R.raw.music_erre);
            mCurrentState = GameState.NO;
        }
        Log.d("suntaiwang", mCurrentCandyModle + " end");
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
