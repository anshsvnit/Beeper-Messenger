package com.beepermessenger;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.beepermessenger.CommonFiles.CommonUtilities;
import com.beepermessenger.CommonFiles.RequestCodes;
import com.beepermessenger.fragment.HomeFragmentNew;
import com.beepermessenger.imageloader.ImageLoader;
import com.beepermessenger.util.NetworkConnection;
import com.beepermessenger.util.SPUser;
import com.beepermessenger.util.TouchImageView;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import al.upload.data.Upload;

public class PinchActivity extends AppCompatActivity {

    private ImageLoader imageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinch);
        TouchImageView tiv = (TouchImageView) findViewById(R.id.tiv_picture);
        imageLoader = new ImageLoader(PinchActivity.this);
        imageLoader.DisplayImage(
                getIntent().getStringExtra("data"),
                R.drawable.ic_launcher, tiv);
    }
}

