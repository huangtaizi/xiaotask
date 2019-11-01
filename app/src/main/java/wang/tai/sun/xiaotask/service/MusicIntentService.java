package wang.tai.sun.xiaotask.service;

import android.app.IntentService;
import android.content.Intent;
import android.media.MediaPlayer;

import wang.tai.sun.xiaotask.R;


public class MusicIntentService extends IntentService {

    public static final String ACTION_MUSIC_START = "wang.tai.sun.xiaotask.service.action.music.start";
    public static final String ACTION_MUSIC_STOP = "wang.tai.sun.xiaotask.service.action.music.stop";

    private static MediaPlayer mMediaPlayer;

    public MusicIntentService() {
        super("MusicIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_MUSIC_START.equals(action)) {
                handleActionMusicStart();
            } else if (ACTION_MUSIC_STOP.equals(action)) {
                handleActionMusicStop();
            }
        }
    }

    private void handleActionMusicStart() {
        mMediaPlayer = MediaPlayer.create(this, R.raw.music_back);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.setVolume(1f, 1f);
        mMediaPlayer.start();
    }

    private void handleActionMusicStop() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
}
