package wang.tai.sun.xiaotask.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions and extra parameters.
 */
public class MusicIntentService extends IntentService {

    public static final String ACTION_MUSIC = "wang.tai.sun.xiaotask.service.action.music";

    private MediaPlayer mMediaPlayer;

    public MusicIntentService() {
        super("MusicIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_MUSIC.equals(action)) {
                handleActionMusic();
            }
        }
    }

    private void handleActionMusic() {
        if (mMediaPlayer == null) {
            createMediaPlayer();
        }

        if (!mMediaPlayer.isPlaying()) {
            mMediaPlayer.start();
        }
    }

    private void createMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
        }

        AssetFileDescriptor fd = null;
        try {
            fd = getAssets().openFd("music_back.wav");
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
            mMediaPlayer.setVolume(1.0f, 1.0f);
            mMediaPlayer.setLooping(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
