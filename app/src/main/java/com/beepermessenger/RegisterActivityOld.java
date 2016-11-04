package com.beepermessenger;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beepermessenger.CommonFiles.CommonUtilities;
import com.beepermessenger.CommonFiles.Parser;
import com.beepermessenger.CommonFiles.RequestCodes;
import com.beepermessenger.adapter.CountryAdapter;
import com.beepermessenger.adapter.StateAdapter;
import com.beepermessenger.imageloader.ImageLoader;
import com.beepermessenger.model.CountryDTO;
import com.beepermessenger.model.StateDTO;
import com.beepermessenger.util.CustomHttpClient;
import com.beepermessenger.util.EmailChecker;
import com.beepermessenger.util.NetworkConnection;
import com.beepermessenger.util.SPUser;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import al.upload.data.Upload;

public class RegisterActivityOld extends AppCompatActivity {
    private TextView tvRegisterLogin;
    private Button btnRegisterNext;
    private EditText etRegisterName;
    private EditText etRegisterAboutYou;
    private EditText etRegisterEmail;
    private EditText etRegisterPhone;
    private TextView tvRegisterGender;
    private TextView tvRegisterDOB;
    private EditText etRegisterPassword;
    private EditText etRegisterCpassword;
    private ImageView ivProfile;
    private View mRegisterFormView;

    private Dialog spinnerDialog;

    private int day;
    private int month;
    private int year;
    public static final String EXTRA_MESSAGE = "EXTRA_MESSAGE";

    public static final int MEDIA_TYPE_IMAGE = 2;

    private Uri fileUri;
    private static final String IMAGE_DIRECTORY_NAME = "TempImages";
    private String selectedImagePath;
    private String strName;
    private String strAbout;
    private String strEmai;
    private String strContact;
    private String strGender;
    private String strpassword;
    private String strBirthdate;
    private String country;
    private String countryCode;
    private CallbackManager callbackManager;
    private LoginButton loginButton;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private ProgressDialog progress;
    private TextView tvCountry;
    private ArrayList<CountryDTO> alCountry;
    private ArrayList<StateDTO> alStateDTO;
    private TextView tvState;
    private String state;
    private long state_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(RegisterActivityOld.this);
        setContentView(R.layout.activity_register);
        progress = new ProgressDialog(RegisterActivityOld.this);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        facebookCode();
        initializeComponents();

    }

    private void parsingUserResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject objectProfile = jsonObject.getJSONObject("data");


            etRegisterName.setText(objectProfile.optString("name"));
            etRegisterAboutYou.setText(objectProfile.optString("about"));
            etRegisterEmail.setText(objectProfile.optString("email"));
            etRegisterPhone.setText(objectProfile.optString("phone"));
            tvRegisterGender.setText(objectProfile.optString("gender"));
            tvRegisterDOB.setText(objectProfile.optString("dob"));
            tvCountry.setText(objectProfile.optString("country"));
            tvState.setText(objectProfile.optString("state"));
            if (tvRegisterDOB.getText().toString().trim().length() > 0) {
                String date[] = objectProfile.optString("dob").split("-");
                if (date.length == 3) {
                    day = Integer.parseInt(date[0]);
                    month = Integer.parseInt(date[1]);
                    year = Integer.parseInt(date[2]);
                }
            }
            etRegisterPassword.setText(objectProfile.optString("password"));
            new ImageLoader(RegisterActivityOld.this).DisplayImage(objectProfile.optString("profileImage"), R.drawable.ic_launcher, ivProfile);
            if (alCountry.size() != 0) {
                for (int i = 0; i < alCountry.size(); i++) {
                    if (alCountry.get(i).getCountry_code().trim().equalsIgnoreCase(tvCountry.getText().toString().trim())) {
                        tvCountry.setTag(alCountry.get(i));
                        tvCountry.setText(alCountry.get(i).getCountry_name());
                        HashMap<String, String> params = new HashMap<>();
                        params.put("country_id", alCountry.get(i).getCountry_code());
                        new CustomHttpClient(RegisterActivityOld.this).executeHttpPost(CommonUtilities.GET_STATE, params, progress, new CustomHttpClient.OnSuccess() {
                            @Override
                            public void onSucess(String result) {
                                alStateDTO = Parser.parsingState(result);
                                for (int j = 0; j < alStateDTO.size(); j++) {
                                    if ((alStateDTO.get(j).getState_id()+"").equalsIgnoreCase(tvState.getText().toString().trim())) {
                                        tvState.setText(alStateDTO.get(j).getState_name());
                                        tvState.setTag(alStateDTO.get(j));
                                    }
                                }
                            }
                        }, null);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            Log.e("inside Exception:..", e.toString());
        }
    }

    private void initializeComponents() {
        mRegisterFormView = findViewById(R.id.register_form);
        tvRegisterLogin = (TextView) findViewById(R.id.tv_register_login);
        tvRegisterLogin.setOnClickListener(new OnButtonClick());
        btnRegisterNext = (Button) findViewById(R.id.btn_register_next);
        btnRegisterNext.setOnClickListener(new OnButtonClick());
        etRegisterName = (EditText) findViewById(R.id.et_register_name);
        etRegisterAboutYou = (EditText) findViewById(R.id.et_register_about_us);
        etRegisterEmail = (EditText) findViewById(R.id.et_register_email);
        etRegisterPhone = (EditText) findViewById(R.id.et_register_phone);
        tvRegisterGender = (TextView) findViewById(R.id.tv_register_gender);
        tvRegisterGender.setOnClickListener(new OnButtonClick());
        tvRegisterDOB = (TextView) findViewById(R.id.tv_register_dob);
        tvRegisterDOB.setOnClickListener(new OnButtonClick());
        etRegisterPassword = (EditText) findViewById(R.id.et_register_password);
        etRegisterCpassword = (EditText) findViewById(R.id.et_register_cpassword);
        ivProfile = (ImageView) findViewById(R.id.iv_register_imageView);
        tvCountry = (TextView) findViewById(R.id.tv_register_country);
        tvState = (TextView) findViewById(R.id.tv_register_state);
        tvState.setOnClickListener(new OnButtonClick());
        tvCountry.setOnClickListener(new OnButtonClick());
        ivProfile.setOnClickListener(new OnButtonClick());
        day = 1;
        month = 0;
        year = 1990;
        loginButton = (LoginButton) findViewById(R.id.lb_register_fconnect);
        loginButton.setReadPermissions("user_friends");
        //loginButton.setFragment(getApplicationContext());
        loginButton.registerCallback(callbackManager, callback);
        alStateDTO = new ArrayList<>();
        HashMap<String, String> params = new HashMap<>();
        new CustomHttpClient(RegisterActivityOld.this).executeHttpPost(CommonUtilities.GET_COUNTRY_URL, params, progress, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                alCountry = Parser.parsingCountry(result);
                isEdit();
            }
        }, null);
    }

    private void isEdit() {
        if (SPUser.getLong(RegisterActivityOld.this, SPUser.USER_ID) != 0) {
            ((TextView) findViewById(R.id.tv_register_heading)).setText("Edit Profile");
            loginButton.setVisibility(View.GONE);
            btnRegisterNext.setText("Update");
            ((LinearLayout) findViewById(R.id.ll_register_footer)).setVisibility(View.GONE);
            HashMap<String, String> params = new HashMap<>();
            params.put("user_id", SPUser.getLong(RegisterActivityOld.this, SPUser.USER_ID) + "");
            new CustomHttpClient(RegisterActivityOld.this).executeHttpPost(CommonUtilities.USER_INFO, params, progress, new CustomHttpClient.OnSuccess() {
                @Override
                public void onSucess(String result) {
                    parsingUserResponse(result);
                }
            }, null);
        }
    }

    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            if (profile != null)
                displayMessage(profile);
        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {

        }
    };

    private void facebookCode() {
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                displayMessage(newProfile);
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();
    }

    private void displayMessage(Profile profile) {
        // Toast.makeText(getApplicationContext(), "gfbgbngn"+profile.getFirstName(), Toast.LENGTH_LONG).show();
        // Facebook Login code here 147147147147147


        HashMap<String, String> params = new HashMap<>();
        params.put("fbid", profile.getId());
        params.put("logintype", "FB");
        String android_id = Settings.Secure.getString(RegisterActivityOld.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        params.put("device_id", android_id);
        System.out.print(android_id);
        params.put("device_token", getSharedPreferences(RegistrationIntentService.TAG, MODE_PRIVATE).getString(RegistrationIntentService.TAG,""));

        new CustomHttpClient(RegisterActivityOld.this).executeHttpPost(CommonUtilities.LOGIN_URL, params, progress, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                parsingResponse(result);
            }
        }, null);

    }

    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }


    private void parsingResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            Log.e("tm", status);

            if (status.equals("true")) {
                //Toast.makeText(getApplicationContext(),"Status True",Toast.LENGTH_LONG).show();
                JSONObject objectProfile = jsonObject.getJSONObject("user_data");
                SPUser.setLong(RegisterActivityOld.this, SPUser.USER_ID, objectProfile.optLong("user_id"));
                SPUser.setString(RegisterActivityOld.this, SPUser.EMAIL, objectProfile.optString("email"));
                SPUser.setString(RegisterActivityOld.this, SPUser.PASSWORD, objectProfile.optString("password"));
                SPUser.setString(RegisterActivityOld.this, SPUser.NAME, objectProfile.optString("name"));
                SPUser.setString(RegisterActivityOld.this, SPUser.ABOUT, objectProfile.optString("about"));
                SPUser.setString(RegisterActivityOld.this, SPUser.PHONE, objectProfile.optString("phone"));
                SPUser.setString(RegisterActivityOld.this, SPUser.GENDER, objectProfile.optString("gender"));
                SPUser.setString(RegisterActivityOld.this, SPUser.D_O_B, objectProfile.optString("dob"));
                SPUser.setString(RegisterActivityOld.this, SPUser.COUNTRY, objectProfile.optString("country"));
                SPUser.setString(RegisterActivityOld.this, SPUser.STATE, objectProfile.optString("state"));
                SPUser.setString(RegisterActivityOld.this, SPUser.PROFILE_IMAGE, objectProfile.optString("profileImage"));

                Log.e("inside register", "" + objectProfile.length());
                Intent i = new Intent(RegisterActivityOld.this, BaseActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Username or password", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Log.e("inside JSONException:..", e.toString());
        }
    }

    private class OnButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_register_gender:
                    spinnerDialog = new Dialog(RegisterActivityOld.this);
                    spinnerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    spinnerDialog.setContentView(R.layout.spinercustom);
                    spinnerDialog.show();
                    ListView spinnerListview = (ListView) spinnerDialog
                            .findViewById(R.id.spinneritemlist);
                    TextView txtdialogtitle = (TextView) spinnerDialog
                            .findViewById(R.id.txtdialogtitle);
                    txtdialogtitle.setText("Select Gender");
                    final ArrayList<String> gender = new ArrayList<>();
                    gender.add("Male");
                    gender.add("Female");
                    spinnerListview.setAdapter(new ArrayAdapter<String>(RegisterActivityOld.this,
                            android.R.layout.simple_list_item_1, android.R.id.text1, gender));
                    spinnerListview
                            .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, int position, long id) {

                                    spinnerDialog.dismiss();
                                    tvRegisterGender.setText(gender.get(position));
                                    tvRegisterGender.setTag(gender.get(position));
                                }
                            });

                    break;
                case R.id.tv_register_country:
                    spinnerDialog = new Dialog(RegisterActivityOld.this);
                    spinnerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    spinnerDialog.setContentView(R.layout.spinercustom);
                    spinnerDialog.show();
                    ListView lvCountey = (ListView) spinnerDialog
                            .findViewById(R.id.spinneritemlist);
                    TextView tvCounteyHeading = (TextView) spinnerDialog
                            .findViewById(R.id.txtdialogtitle);
                    tvCounteyHeading.setText("Select Country");

                    lvCountey.setAdapter(new CountryAdapter(RegisterActivityOld.this, alCountry));
                    lvCountey
                            .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, int position, long id) {

                                    spinnerDialog.dismiss();
                                    tvCountry.setText(alCountry.get(position).getCountry_name());
                                    tvCountry.setTag(alCountry.get(position));
                                    HashMap<String, String> params = new HashMap<>();
                                    params.put("country_id", alCountry.get(position).getCountry_code());
                                    new CustomHttpClient(RegisterActivityOld.this).executeHttpPost(CommonUtilities.GET_STATE, params, progress, new CustomHttpClient.OnSuccess() {
                                        @Override
                                        public void onSucess(String result) {
                                            alStateDTO = Parser.parsingState(result);
                                        }
                                    }, null);
                                }
                            });
                    break;
                case R.id.tv_register_state:
                    spinnerDialog = new Dialog(RegisterActivityOld.this);
                    spinnerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    spinnerDialog.setContentView(R.layout.spinercustom);
                    spinnerDialog.show();
                    ListView lvState = (ListView) spinnerDialog
                            .findViewById(R.id.spinneritemlist);
                    TextView tvStateHeading = (TextView) spinnerDialog
                            .findViewById(R.id.txtdialogtitle);
                    tvStateHeading.setText("Select State");

                    lvState.setAdapter(new StateAdapter(RegisterActivityOld.this, alStateDTO));
                    lvState
                            .setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent,
                                                        View view, int position, long id) {
                                    spinnerDialog.dismiss();
                                    tvState.setText(alStateDTO.get(position).getState_name());
                                    tvState.setTag(alStateDTO.get(position));
                                }
                            });
                    break;

                case R.id.tv_register_dob:
                    new DatePickerDialog(RegisterActivityOld.this, fromPickerListener, year,
                            month, day).show();
                    break;
                case R.id.tv_register_login:
                    Intent i = new Intent(RegisterActivityOld.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                    break;

                case R.id.btn_register_next:
                    if (checkValidation()) {
                        if (NetworkConnection.getInstance(RegisterActivityOld.this).isOnline(RegisterActivityOld.this)) {
                            new RegisterTask().execute();
                        } else {
                            Toast.makeText(RegisterActivityOld.this, "Please Provide Internet.", Toast.LENGTH_LONG).show();
                        }

                    }
                    break;
                case R.id.iv_register_imageView:
                    CharSequence colors[] = new CharSequence[]{"Gallery",
                            "Camera"};

                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivityOld.this);
                    builder.setTitle("Upload photo from");
                    builder.setItems(colors, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    selectImage();
                                    break;
                                case 1:
                                    captureImage();
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

    private DatePickerDialog.OnDateSetListener fromPickerListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth;
            day = selectedDay;
            String m = (month < 9) ? "0" + (month + 1)
                    : (month + 1) + "";
            String d = (day < 10) ? "0" + day : day + "";
            tvRegisterDOB.setText(new StringBuilder().append(d).append("-")
                    .append(m).append("-").append(year));
        }
    };


    private boolean checkValidation() {
        boolean isValidated = true;

        if (etRegisterName.getText().toString().trim().equals("")) {
            etRegisterName.setError("Full Name Required");
            isValidated = false;
        }

        if (etRegisterAboutYou.getText().toString().trim().equals("")) {
            etRegisterAboutYou.setError("About Us Required");
            isValidated = false;
        }
        if (etRegisterEmail.getText().toString().trim().equals("")) {
            etRegisterEmail.setError("Email Required");
            isValidated = false;
        }
        if (!EmailChecker.isValid(etRegisterEmail.getText().toString())) {
            etRegisterEmail.setError("Please Enter Valid E-mail");
            isValidated = false;
        }
        /*if (etRegisterPhone.getText().toString().equals("")) {
            etRegisterPhone.setError("Phone Number Required");
            isValidated = false;
        }*/
       /* if (tvRegisterDOB.getText().toString().equals("")) {
            Toast.makeText(RegisterActivity.this, "Birth Date Required", Toast.LENGTH_LONG).show();
            isValidated = false;
        }*/
        if (tvRegisterGender.getText().toString().equals("")) {
            Toast.makeText(RegisterActivityOld.this, "Gender is Required", Toast.LENGTH_LONG).show();
            isValidated = false;
        }
        if (tvCountry.getText().toString().equals("")) {
            Toast.makeText(RegisterActivityOld.this, "Country is Required", Toast.LENGTH_LONG).show();
            isValidated = false;
        }
        if (tvState.getText().toString().equals("")) {
            Toast.makeText(RegisterActivityOld.this, "State is Required", Toast.LENGTH_LONG).show();
            isValidated = false;
        }
        if (etRegisterPassword.getText().toString().equals("")) {
            etRegisterPassword.setError("Password Required");
            isValidated = false;
        }
        if (etRegisterCpassword.getText().toString().equals("")) {
            etRegisterPassword.setError("Confirm Password Required");
            isValidated = false;
        }
        if (etRegisterCpassword.getText().toString().trim().length() < 5) {
            etRegisterPassword.setError("Password is too short");
            isValidated = false;
        }
        if (!etRegisterPassword.getText().toString().equals(etRegisterCpassword.getText().toString())) {
            Toast.makeText(RegisterActivityOld.this, "Password and Confirm Password do not match", Toast.LENGTH_LONG).show();
            isValidated = false;
        }
        if (selectedImagePath == null && SPUser.getLong(RegisterActivityOld.this, SPUser.USER_ID) == 0) {
            Toast.makeText(RegisterActivityOld.this, "Please Upload Picture", Toast.LENGTH_LONG).show();
            isValidated = false;
        }

        return isValidated;
    }

    private void getParams() {

        strName = etRegisterName.getText().toString().trim();
        strAbout = etRegisterAboutYou.getText().toString().trim();
        strEmai = etRegisterEmail.getText().toString().trim();
        strContact = etRegisterPhone.getText().toString().trim();
        strGender = tvRegisterGender.getText().toString().trim();
        strpassword = etRegisterPassword.getText().toString().trim();
        strBirthdate = tvRegisterDOB.getText().toString();
        country = tvCountry.getText().toString();
        countryCode = ((CountryDTO) tvCountry.getTag()).getCountry_code();
        state = tvState.getText().toString();
        state_id = ((StateDTO) tvState.getTag()).getState_id();

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
                    ivProfile.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // //

            } else if (resultCode == Activity.RESULT_CANCELED) {

            } else {
                // failed to capture image
                Toast.makeText(RegisterActivityOld.this, "Sorry! Failed to capture image",
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
                    ivProfile.setImageBitmap(bitmap);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
            callbackManager.onActivityResult(requestCode, resultCode, data);
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

    private class RegisterTask extends AsyncTask<Void, Void, Void> {
        private ProgressDialog progress;
        private String reply;
        private Upload u;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress = new ProgressDialog(RegisterActivityOld.this);
            progress.setMessage("Please wait...");
            progress.show();
            progress.setCancelable(false);
            getParams();

        }

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                if (SPUser.getLong(RegisterActivityOld.this, SPUser.USER_ID) == 0) {
                    u = new Upload(CommonUtilities.SIGN_UP, "UTF-8");
                    String android_id = Settings.Secure.getString(RegisterActivityOld.this.getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    u.addField("device_id", android_id);
                    System.out.print(android_id);
                    u.addField("device_token", getSharedPreferences(RegistrationIntentService.TAG, MODE_PRIVATE).getString(RegistrationIntentService.TAG,""));

                } else {
                    u = new Upload(CommonUtilities.EDIT_USER, "UTF-8");
                    u.addField("id", SPUser.getLong(RegisterActivityOld.this, SPUser.USER_ID) + "");
                }

                u.addField("name", strName);
                u.addField("about", strAbout);
                u.addField("email", strEmai);
                //u.addField("phone", strContact);
                u.addField("gender", strGender);
               // u.addField("dob", strBirthdate);
                u.addField("password", strpassword);
                u.addField("country", country);
                u.addField("country_id", countryCode);
                u.addField("state_id", state_id+"");
                u.addField("state", state);
                if (selectedImagePath != null) {
                    u.addFile("profileImage", selectedImagePath);
                    u.addField("is_image", "1");
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
                    Toast.makeText(RegisterActivityOld.this,
                            "Error in network", Toast.LENGTH_LONG).show();
                } else {
                    JSONObject jsonObject = new JSONObject(reply);
                    if (jsonObject.optBoolean("status")) {
                        if (!jsonObject.optString("message").equals("")) {
                            Toast.makeText(RegisterActivityOld.this,
                                    jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                        }
                        long tmp = SPUser.getLong(RegisterActivityOld.this, SPUser.USER_ID);
                        JSONObject objectProfile;
                        objectProfile = jsonObject.getJSONObject("user_data");
                        SPUser.setLong(RegisterActivityOld.this, SPUser.USER_ID, objectProfile.optLong("user_id"));
                        SPUser.setString(RegisterActivityOld.this, SPUser.EMAIL, objectProfile.optString("email"));
                        SPUser.setString(RegisterActivityOld.this, SPUser.PASSWORD, objectProfile.optString("password"));
                        SPUser.setString(RegisterActivityOld.this, SPUser.NAME, objectProfile.optString("name"));
                        SPUser.setString(RegisterActivityOld.this, SPUser.ABOUT, objectProfile.optString("about"));
                        SPUser.setString(RegisterActivityOld.this, SPUser.PHONE, objectProfile.optString("phone"));
                        SPUser.setString(RegisterActivityOld.this, SPUser.GENDER, objectProfile.optString("gender"));
                        SPUser.setString(RegisterActivityOld.this, SPUser.D_O_B, objectProfile.optString("dob"));
                        SPUser.setString(RegisterActivityOld.this, SPUser.COUNTRY, objectProfile.optString("country"));
                        SPUser.setString(RegisterActivityOld.this, SPUser.STATE, objectProfile.optString("state"));
                        SPUser.setString(RegisterActivityOld.this, SPUser.PROFILE_IMAGE, objectProfile.optString("profileImage"));

                        if (tmp != 0) {
                             finish();
                        } else {
                            Intent i = new Intent(RegisterActivityOld.this, BaseActivity.class);
                            startActivity(i);
                            finish();
                        }

                    } else {

                        Toast.makeText(RegisterActivityOld.this,
                                jsonObject.optString("message"), Toast.LENGTH_LONG).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

