package com.beepermessenger;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class AndroidPlayer extends Activity implements OnPreparedListener {
    MediaPlayer mediaPlayer;
    Button buttonPlayPause, buttonQuit;
    TextView textState;

    private Button btnPlayPause;

    private int current = 0;
    private boolean running = true;
    private boolean canPlay;
    private Button close;
    private int duration = 0;
    private MediaPlayer mPlayer;
    private SeekBar mSeekBarPlayer;
    private TextView mMediaTime;
    private TextView mEndMediaTime;

    private LinearLayout ll_parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.media_player);
        mSeekBarPlayer = (SeekBar) findViewById(R.id.progress_bar);

        ll_parent = (LinearLayout) findViewById(R.id.ll_parent);

        canPlay = false;

        try {
            mPlayer = new MediaPlayer();
            String PATH_TO_FILE = getIntent().getStringExtra("path").replaceAll(" ", "%20");
            System.out.println(PATH_TO_FILE);

            mPlayer.setDataSource(PATH_TO_FILE);
            mPlayer.prepare();
            mPlayer.start();
            mSeekBarPlayer.postDelayed(onEverySecond, 1000);

        } catch (Exception e1) {
            e1.printStackTrace();
        }

        btnPlayPause = (Button) findViewById(R.id.button1);

        mMediaTime = (TextView) findViewById(R.id.mediaTime);
        close = (Button) findViewById(R.id.button3);
        mEndMediaTime = (TextView) findViewById(R.id.mediaEndTime);
        mPlayer.setOnPreparedListener(this);
        if (canPlay) {
            btnPlayPause
                    .setBackgroundResource(android.R.drawable.ic_media_play);
        } else {
            btnPlayPause
                    .setBackgroundResource(android.R.drawable.ic_media_pause);
        }
        mPlayer.setOnCompletionListener(new OnCompletionListener() {

            @Override
            public void onCompletion(MediaPlayer mp) {
                btnPlayPause
                        .setBackgroundResource(android.R.drawable.ic_media_play);
                canPlay = true;
            }
        });
        mSeekBarPlayer
                .setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    @Override
                    public void onProgressChanged(SeekBar seekBar,
                                                  int progress, boolean fromUser) {
                        if (fromUser && progress > 0) {
                            mPlayer.seekTo(progress);
                            updateTime();
                        }
                    }
                });

        btnPlayPause.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (canPlay) {
                    try {
                        // mPlayer.prepare();
                        mPlayer.start();
                        mSeekBarPlayer.postDelayed(onEverySecond, 1000);
                        btnPlayPause
                                .setBackgroundResource(android.R.drawable.ic_media_pause);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }

                } else {
                    try {
                        mPlayer.pause();
                        btnPlayPause
                                .setBackgroundResource(android.R.drawable.ic_media_play);
                    } catch (Exception e) {

                        e.printStackTrace();
                    }
                }
                canPlay = !canPlay;
            }
        });

        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    mPlayer.pause();
                    mPlayer.stop();
                    mPlayer.release();
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private Runnable onEverySecond = new Runnable() {
        @Override
        public void run() {
            try {
                if (running) {
                    if (mSeekBarPlayer != null) {

                        mSeekBarPlayer
                                .setProgress(mPlayer.getCurrentPosition());

                    }

                    if (mPlayer.isPlaying()) {
                        mSeekBarPlayer.postDelayed(onEverySecond, 1000);
                        updateTime();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void updateTime() {
        do {
            current = mPlayer.getCurrentPosition();
            System.out.println("duration - " + duration + " current- "
                    + current);
            int dSeconds = (int) (duration / 1000) % 60;
            int dMinutes = (int) ((duration / (1000 * 60)) % 60);
            int dHours = (int) ((duration / (1000 * 60 * 60)) % 24);

            int cSeconds = (int) (current / 1000) % 60;
            int cMinutes = (int) ((current / (1000 * 60)) % 60);
            int cHours = (int) ((current / (1000 * 60 * 60)) % 24);

            if (dHours == 0) {
                mMediaTime.setText(String.format("%02d:%02d ", cMinutes,
                        cSeconds));
                mEndMediaTime.setText(String.format("%02d:%02d ", dMinutes,
                        dSeconds));
            } else {
                mMediaTime.setText(String.format("%02d:%02d:%02d ", cHours,
                        cMinutes, cSeconds));
                mEndMediaTime.setText(String.format("%02d:%02d:%02d ", dHours,
                        dMinutes, dSeconds));
            }

            try {
                Log.d("Value: ",
                        String.valueOf((int) (current * 100 / duration)));
                if (mSeekBarPlayer.getProgress() >= 100) {
                    break;
                }
            } catch (Exception e) {
            }

        } while (mSeekBarPlayer.getProgress() <= 100);
    }

    @Override
    public void onPrepared(MediaPlayer arg0) {

        duration = mPlayer.getDuration();
        mSeekBarPlayer.setMax(duration);
        mSeekBarPlayer.postDelayed(onEverySecond, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mPlayer.pause();
            mPlayer.stop();
            mPlayer.release();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }
}
