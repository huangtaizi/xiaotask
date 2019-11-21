package wang.tai.sun.xiaotask;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import wang.tai.sun.xiaotask.game.GameContract;
import wang.tai.sun.xiaotask.model.CandyModle;
import wang.tai.sun.xiaotask.model.ConfModle;
import wang.tai.sun.xiaotask.service.MusicIntentService;
import wang.tai.sun.xiaotask.utils.CofUtils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private GameContract.View gameSurfaceView;
    private LinearLayout mConfLL;
    private EditText mUserIdET;
    private Button mConfBtn;
    public static Gson mGson;
    private List<CandyModle> allCandyModleList;

    private boolean isStartGame = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

        setContentView(R.layout.activity_main);
        gameSurfaceView = findViewById(R.id.game_surface);
        gameSurfaceView.onCreate();

        mConfLL = findViewById(R.id.ll_conf);

        mUserIdET = findViewById(R.id.et_user_id);
        mConfBtn = findViewById(R.id.btn_conf);
        mConfBtn.setOnClickListener(this);

        mGson = new Gson();
        allCandyModleList = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameSurfaceView.onResume();

        if (isStartGame) {
            Intent intent = new Intent(MainActivity.this, MusicIntentService.class);
            intent.setAction(MusicIntentService.ACTION_MUSIC_START);
            startService(intent);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameSurfaceView.onPause();

        if (isStartGame) {
            Intent intent = new Intent(MainActivity.this, MusicIntentService.class);
            intent.setAction(MusicIntentService.ACTION_MUSIC_STOP);
            startService(intent);
        }
    }

    @Override
    public void onClick(View v) {
        File confFile = new File(Environment.getExternalStorageDirectory(), "conf.json");
        Log.d("suntaiwang", "confFile=" + confFile.getAbsolutePath());
        try {
            FileReader fileReader = new FileReader(confFile);
            ConfModle confModle = mGson.fromJson(fileReader, ConfModle.class);
            String userID = mUserIdET.getText().toString().trim();
            Log.d("suntaiwang", "userID:" + userID);
            if (checkDataFormat(confModle) && checkUserID(userID)) {
                CofUtils.candyModleList = allCandyModleList;
                CofUtils.GameType = confModle.type;
                CofUtils.userID = userID;
                mConfLL.setVisibility(View.GONE);
                gameSurfaceView.onStart();
                isStartGame = true;
                if (isStartGame) {
                    Intent intent = new Intent(MainActivity.this, MusicIntentService.class);
                    intent.setAction(MusicIntentService.ACTION_MUSIC_START);
                    startService(intent);
                }
            } else {
                allCandyModleList.clear();
            }
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "配置文件找不到", Toast.LENGTH_LONG).show();
            allCandyModleList.clear();
        }
    }

    private boolean checkUserID(String userID) {
        if (TextUtils.isEmpty(userID)) {
            Toast.makeText(this, "请输入用户ID", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkDataFormat(ConfModle confModle) {
        if (confModle == null) {
            Toast.makeText(this, "json格式异常", Toast.LENGTH_LONG).show();
            return false;
        }
        if (confModle.type == ConfModle.TYPE_NORMAL) {
            if (!checkNormalCandyFormat(confModle.thanOne, "thanOne")) {
                return false;
            }
            if (!checkNormalCandyFormat(confModle.thanTwo, "thanTwo")) {
                return false;
            }
            if (!checkNormalCandyFormat(confModle.thanThree, "thanThree")) {
                return false;
            }
            if (!checkNormalCandyFormat(confModle.thanFour, "thanFour")) {
                return false;
            }
            if (!checkNormalCandyFormat(confModle.thanFive, "thanFive")) {
                return false;
            }
            if (!checkNormalCandyFormat(confModle.thanSix, "thanSix")) {
                return false;
            }
        } else if (confModle.type == ConfModle.TYPE_ABNORMAL) {
            if (!checkAbnormalCandyFormat(confModle.thanOneAdd, confModle.thanOneReduce, "thanOneAdd、thanOneReduce")) {
                return false;
            }
            if (!checkAbnormalCandyFormat(confModle.thanTwoAdd, confModle.thanTwoReduce, "thanTwoAdd、thanTwoReduce")) {
                return false;
            }
            if (!checkAbnormalCandyFormat(confModle.thanThreeAdd, confModle.thanThreeReduce, "thanThreeAdd、thanThreeReduce")) {
                return false;
            }
            if (!checkAbnormalCandyFormat(confModle.thanFourAdd, confModle.thanFourReduce, "thanFourAdd、thanFourReduce")) {
                return false;
            }
            if (!checkAbnormalCandyFormat(confModle.thanFiveAdd, confModle.thanFiveReduce, "thanFiveAdd、thanFiveReduce")) {
                return false;
            }
            if (!checkAbnormalCandyFormat(confModle.thanSixAdd, confModle.thanSixReduce, "thanSixAdd、thanSixReduce")) {
                return false;
            }
        } else {
            Toast.makeText(this, "type配置异常", Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private boolean checkNormalCandyFormat(List<CandyModle> candyModles, String toastText) {
        if (candyModles == null || candyModles.size() <= 0) {
            Toast.makeText(this, toastText + "中未配置糖果", Toast.LENGTH_LONG).show();
            return false;
        }
        for (int i = 0; i < candyModles.size(); i++) {
            CandyModle candyModle = candyModles.get(i);
            if (candyModle.a <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 a 异常", Toast.LENGTH_LONG).show();
                return false;
            }

            if (candyModle.sizeA <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 sizeA 异常", Toast.LENGTH_LONG).show();
                return false;
            }

            if (candyModle.b <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 b 异常", Toast.LENGTH_LONG).show();
                return false;
            }

            if (candyModle.sizeB <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 sizeB 异常", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        Collections.shuffle(candyModles);
        allCandyModleList.addAll(candyModles);
        return true;
    }

    private boolean checkAbnormalCandyFormat(List<CandyModle> addModles, List<CandyModle> reduceModles, String toastText) {
        if (addModles == null || addModles.size() <= 0) {
            Toast.makeText(this, toastText + "ADD未配置糖果", Toast.LENGTH_LONG).show();
            return false;
        }
        if (reduceModles == null || reduceModles.size() <= 0) {
            Toast.makeText(this, toastText + "reduce未配置糖果", Toast.LENGTH_LONG).show();
            return false;
        }
        if (addModles.size() != reduceModles.size()) {
            Toast.makeText(this, toastText + "add和reduce配置糖果比例不相等", Toast.LENGTH_LONG).show();
            return false;
        }
        for (int i = 0; i < addModles.size(); i++) {
            CandyModle candyModle = addModles.get(i);
            if (candyModle.a <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 a 异常", Toast.LENGTH_LONG).show();
                return false;
            }

            if (candyModle.sizeA <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 sizeA 异常", Toast.LENGTH_LONG).show();
                return false;
            }

            if (candyModle.b <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 b 异常", Toast.LENGTH_LONG).show();
                return false;
            }

            if (candyModle.sizeB <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 sizeB 异常", Toast.LENGTH_LONG).show();
                return false;
            }

            if (candyModle.add <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 add 异常", Toast.LENGTH_LONG).show();
                return false;
            }

            if (candyModle.sizeAdd <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 sizeAdd 异常", Toast.LENGTH_LONG).show();
                return false;
            }
        }

        for (int i = 0; i < reduceModles.size(); i++) {
            CandyModle candyModle = reduceModles.get(i);
            if (candyModle.a <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 a 异常", Toast.LENGTH_LONG).show();
                return false;
            }

            if (candyModle.sizeA <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 sizeA 异常", Toast.LENGTH_LONG).show();
                return false;
            }

            if (candyModle.b <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 b 异常", Toast.LENGTH_LONG).show();
                return false;
            }

            if (candyModle.sizeB <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 sizeB 异常", Toast.LENGTH_LONG).show();
                return false;
            }

            if (candyModle.reduce <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 reduce 异常", Toast.LENGTH_LONG).show();
                return false;
            }

            if (candyModle.sizeReduce <= 0) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果未配置 sizeReduce 异常", Toast.LENGTH_LONG).show();
                return false;
            }

            if (candyModle.a < candyModle.reduce) {
                Toast.makeText(this, toastText + "中第 " + i + " 个糖果 a < reduce 异常", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        Collections.shuffle(addModles);
        Collections.shuffle(reduceModles);

        for (int i = 0; i < addModles.size(); i++) {
            allCandyModleList.add(addModles.get(i));
            allCandyModleList.add(reduceModles.get(i));
        }
        return true;
    }
}
