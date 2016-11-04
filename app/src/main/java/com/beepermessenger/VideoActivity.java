package com.beepermessenger;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.annotation.TargetApi;
import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

/*this screen is used to view videos*/


public class VideoActivity extends Activity {

    private VideoView videoView;
    String videoUrl;
    //private ProgressDialog mProgressDialog;
    private boolean doubleBackToExitPressedOnce;
    private ProgressBar progressBar1;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_video);
        videoUrl = getIntent().getExtras().getString("video_url").replaceAll(" ","%20");
        videoView = (VideoView) findViewById(R.id.videoview);
        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar1.setInterpolator(new LinearInterpolator());
        MediaController mediacontroller = new MediaController(
                VideoActivity.this);
        mediacontroller.setAnchorView(videoView);

        System.out.println("aaaa===========>"+videoUrl);
        Uri uri = Uri.parse(videoUrl);
        videoView.setMediaController(mediacontroller);
        videoView.setVideoURI(uri);

        videoView.setOnPreparedListener(new OnPreparedListener() {

            @Override
            public void onPrepared(MediaPlayer mp) {
                // TODO Auto-generated method stub
                progressBar1.setVisibility(View.GONE);
                //videoView.setZOrderOnTop(false);
                videoView.start();
                //    mProgressDialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        //   mProgressDialog.dismiss();
        doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

}
