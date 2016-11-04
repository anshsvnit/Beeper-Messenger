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
import com.beepermessenger.util.NetworkConnection;
import com.beepermessenger.util.SPUser;

import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import al.upload.data.Upload;

public class AddPostActivity extends AppCompatActivity {
    private Button btnSave;
    private EditText etTitle;
    private EditText etDescription;
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final int MEDIA_TYPE_IMAGE = 2;

    private Uri fileUri;
    private static final String IMAGE_DIRECTORY_NAME = "TempImages";
    private ImageView ivImage;
    private String selectedImagePath;
    private String title;
    private String desc;
    private String mediaType="";
private String thumbPath = "";
    private File tfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_post);
        initializeComponents();
    }

    private void initializeComponents() {
        btnSave = (Button) findViewById(R.id.btn_add_post_save);
        etTitle = (EditText) findViewById(R.id.et_add_post_title);
        etDescription = (EditText) findViewById(R.id.et_add_post_description);
        ivImage = (ImageView) findViewById(R.id.iv_add_post_pic);
        btnSave.setOnClickListener(new OnButtonClick());
        ivImage.setOnClickListener(new OnButtonClick());
    }

    private class OnButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_add_post_save:
                    if (checkValidation()) {
                         if (NetworkConnection.getInstance(AddPostActivity.this).isOnline(AddPostActivity.this)) {
                             new PostTask().execute();
                        }else{
                             Toast.makeText(AddPostActivity.this, "Please Provide Internet.", Toast.LENGTH_LONG).show();
                         }

                    }
                    break;
                case R.id.iv_add_post_pic:
                    CharSequence colors[] = new CharSequence[]{"Upload Image",
                            "Upload Video","Upload Audio"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(AddPostActivity.this);
                    builder.setTitle("Upload photo from");
                    builder.setItems(colors, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    selectImage();
                                    break;
                                case 1:
                                    //captureImage();
                                    uploadVideo();
                                    break;
                                case 2:
                                    //captureImage();
                                    uploadAudio();
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    builder.show();
                    break;
            }
        }
    }

    private void uploadVideo() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_PICK);
        try {
            intent.putExtra("return-data", true);
            startActivityForResult(
                    Intent.createChooser(intent, "Select Video"), RequestCodes.SELECT_VIDEO_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean checkValidation() {
        boolean isValidated = true;

      /*  if (etTitle.getText().toString().trim().equals("")) {
            etTitle.setError("Please Enter Title");
            isValidated = false;
        }*/

        if (etDescription.getText().toString().trim().equals("")) {
            etDescription.setError("Pelase Enter Description");
            isValidated = false;
        }
        return isValidated;
    }

    public void uploadAudio() {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("audio/mpeg");

        try {
            intent.putExtra("return-data", true);
            startActivityForResult(
                    Intent.createChooser(intent, "Select Audio"), RequestCodes.SELECT_AUDIO_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);

        try {
            intent.putExtra("return-data", true);
            startActivityForResult(
                    Intent.createChooser(intent, "Select Picture"), RequestCodes.SELECT_IMAGE_IMAGE_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /*
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    public void captureImage() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        try {
            intent.putExtra("return-data", true);
            // start the image capture Intent
            startActivityForResult(intent, RequestCodes.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
     * returning image / video
     */
    private static File getOutputMediaFile(int type) {
        Log.e("before directory", "yes");
        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            Log.e("storage directory", "yes");
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }

        return mediaFile;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCodes.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                try {

                    selectedImagePath = fileUri.getPath();
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    Bitmap bitmap = BitmapFactory
                            .decodeFile(selectedImagePath, options);
                    ivImage.setImageBitmap(bitmap);
                    mediaType = "image";
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // //

            } else if (resultCode == Activity.RESULT_CANCELED) {

            } else {
                // failed to capture image
                Toast.makeText(AddPostActivity.this, "Sorry! Failed to capture image",
                        Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == RequestCodes.SELECT_IMAGE_IMAGE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    Uri selectedImageUri = data.getData();
                    selectedImagePath = getPath(selectedImageUri);
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 8;
                    Bitmap bitmap = BitmapFactory
                            .decodeFile(selectedImagePath, options);
                    ivImage.setImageBitmap(bitmap);
                    mediaType = "image";
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }else if (requestCode == RequestCodes.SELECT_VIDEO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    Uri selectedImageUri = data.getData();
                    selectedImagePath = getPath(selectedImageUri);
                    mediaType = "video";
                    ivImage.setImageResource(R.mipmap.video);
                    ImageView videoview;
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(selectedImagePath, MediaStore.Images.Thumbnails.MINI_KIND);
                    Matrix matrix = new Matrix();
                    Bitmap bmThumbnail = Bitmap.createBitmap(thumb, 0, 0,
                            thumb.getWidth(), thumb.getHeight(), matrix, true);
                    ivImage.setImageBitmap(bmThumbnail);





                    tfile = getOutputMediaFile(MEDIA_TYPE_IMAGE);
                    FileOutputStream fOut = new FileOutputStream(tfile);

                    bmThumbnail.compress(Bitmap.CompressFormat.PNG, 85, fOut);
                    fOut.flush();
                    fOut.close();

                    System.out.println(tfile.getAbsolutePath());

                    thumbPath = tfile.getAbsolutePath();
                    if(thumbPath==null)
                    {
                        thumbPath="";
                    }
                    System.gc();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode ==  RequestCodes.SELECT_AUDIO_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                try {

                    Uri selectedImageUri = data.getData();
                    selectedImagePath = getPath(selectedImageUri);
                    if(selectedImagePath!=null) {
                        ivImage.setImageResource(R.mipmap.audio);
                        mediaType = "audio";
                    }else{
                        Toast.makeText(AddPostActivity.this,"Your Phone is not supporing media selection",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String getPath(Uri uri) {
        // just some safety built in
        if (uri == null) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

    private class PostTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress;
        private String reply;
        private Upload u;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(AddPostActivity.this);
            progress.setMessage("Please wait...");
            progress.show();
            progress.setCancelable(false);
            getParams();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {

                u = new Upload(CommonUtilities.ADD_POST, "UTF-8");
                u.addField("title", title);
                u.addField("description", desc);
                u.addField("user_id", SPUser.getLong(AddPostActivity.this,SPUser.USER_ID)+"");
                if (selectedImagePath != null) {
                    u.addFile("post_media", selectedImagePath);
                    System.out.println("post_media===>" + selectedImagePath);
                    u.addField("post_media_type", mediaType);
                    System.out.println("thumb===>" + thumbPath);
                    u.addFile("thumb", thumbPath);

                }

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            try {
                reply = u.startUploading();
                if (reply != null) {
                    System.out.println(reply);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                progress.cancel();
                if (reply == null) {
                    Toast.makeText(AddPostActivity.this,
                            "Error in network", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject jsonObject = new JSONObject(reply);
                    if (jsonObject.optBoolean("status")) {
                        if (!jsonObject.optString("message").equals("")) {
                            Toast.makeText(AddPostActivity.this,
                                    jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                        }
                        setResult(RESULT_OK);
                        if(HomeFragmentNew.homeFragmentNew!=null)
                        {
                            //HomeFragmentNew.homeFragmentNew.reloadDataNow();
                        }
                        finish();
                    } else {

                        Toast.makeText(AddPostActivity.this,
                                jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void getParams() {
        title = etTitle.getText().toString().trim();
        desc = etDescription.getText().toString().trim();
    }
}

