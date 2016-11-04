package com.beepermessenger;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.app.ProgressDialog;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beepermessenger.CommonFiles.CommonUtilities;
import com.beepermessenger.util.CustomHttpClient;
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView actEmail;
    private EditText etPassword;


    private View mLoginFormView;
    private Button btnEmailSignInButton;
    private TextView tvLoginCreateAccount;
    private TextView tvLoginForgotPassword;
    private TextView tvLoginTermCondition;
    private TextView tvLoginPrivacyPolicy;
    private ProgressDialog progress;
    private CallbackManager callbackManager;

    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(LoginActivity.this);
        setContentView(R.layout.activity_login);
        progress = new ProgressDialog(LoginActivity.this);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);

        // Set up the login form.

        // printKeyHash(LoginActivity.this);
        facebookCode();

        initializeComponents();
        populateAutoComplete();

        etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        btnEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        tvLoginForgotPassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(i);
                //finish();
            }
        });

        tvLoginCreateAccount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(i);
                finish();
            }
        });


    }

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

    public static String printKeyHash(Activity context) {
        PackageInfo packageInfo;
        String key = null;
        try {
            //getting application package name, as defined in manifest
            String packageName = context.getApplicationContext().getPackageName();

            //Retriving package info
            packageInfo = context.getPackageManager().getPackageInfo(packageName,
                    PackageManager.GET_SIGNATURES);

            Log.e("Package Name=", context.getApplicationContext().getPackageName());

            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                key = new String(Base64.encode(md.digest(), 0));

                // String key = new String(Base64.encodeBytes(md.digest()));
                Log.e("Key Hash=", key);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("Name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("No such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("Exception", e.toString());
        }

        return key;
    }

    private void initializeComponents() {

        LoginButton loginButton = (LoginButton) findViewById(R.id.iv_login_connect);
        loginButton.setReadPermissions("user_friends");
        //loginButton.setFragment(getApplicationContext());
        loginButton.registerCallback(callbackManager, callback);

        etPassword = (EditText) findViewById(R.id.et_login_password);

        actEmail = (AutoCompleteTextView) findViewById(R.id.act_login_email);
        mLoginFormView = findViewById(R.id.login_form);

        btnEmailSignInButton = (Button) findViewById(R.id.btn_login_button);

        tvLoginCreateAccount = (TextView) findViewById(R.id.tv_login_create_account);
        tvLoginForgotPassword = (TextView) findViewById(R.id.tv_login_forgot_password);

        tvLoginTermCondition = (TextView) findViewById(R.id.tv_login_termcondition);
        tvLoginPrivacyPolicy = (TextView) findViewById(R.id.tv_login_privacypolicy);

        tvLoginTermCondition.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, TermsAndCondition.class);
                startActivity(i);
            }
        });
        tvLoginPrivacyPolicy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(LoginActivity.this, PrivacyPolicy.class);
                startActivity(i);
            }
        });

    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }
        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(actEmail, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        actEmail.setError(null);
        etPassword.setError(null);

        // Store values at the time of the login attempt.
        String email = actEmail.getText().toString();
        String password = etPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            etPassword.setError(getString(R.string.error_invalid_password));
            focusView = etPassword;
            cancel = true;
        }
        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            actEmail.setError(getString(R.string.error_field_required));
            focusView = actEmail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            actEmail.setError(getString(R.string.error_invalid_email));
            focusView = actEmail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //


            HashMap<String, String> params = new HashMap<>();
            params.put("email", email);
            params.put("password", password);
            params.put("logintype", "NM");
            String android_id = Settings.Secure.getString(LoginActivity.this.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
            params.put("device_id", android_id);
            System.out.print(android_id);
            params.put("device_token", getSharedPreferences(RegistrationIntentService.TAG, MODE_PRIVATE).getString(RegistrationIntentService.TAG, ""));
            new CustomHttpClient(LoginActivity.this).executeHttpPost(CommonUtilities.LOGIN_URL, params, progress, new CustomHttpClient.OnSuccess() {
                @Override
                public void onSucess(String result) {
                    parsingResponse(result);
                }
            }, null);

            // mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        actEmail.setAdapter(adapter);
    }

    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    private void parsingResponse(String response) {

        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            Log.e("tm", status);

            if (status.equals("true")) {
                //Toast.makeText(getApplicationContext(),"Status True",Toast.LENGTH_LONG).show();
                JSONObject objectProfile = jsonObject.getJSONObject("user_data");
                SPUser.setLong(LoginActivity.this, SPUser.USER_ID, objectProfile.optLong("user_id"));
                SPUser.setString(LoginActivity.this, SPUser.EMAIL, objectProfile.optString("email"));
                SPUser.setString(LoginActivity.this, SPUser.PASSWORD, objectProfile.optString("password"));
                SPUser.setString(LoginActivity.this, SPUser.NAME, objectProfile.optString("name"));
                SPUser.setString(LoginActivity.this, SPUser.ABOUT, objectProfile.optString("about"));
                SPUser.setString(LoginActivity.this, SPUser.PHONE, objectProfile.optString("phone"));
                SPUser.setString(LoginActivity.this, SPUser.GENDER, objectProfile.optString("gender"));
                SPUser.setString(LoginActivity.this, SPUser.D_O_B, objectProfile.optString("dob"));
                SPUser.setString(LoginActivity.this, SPUser.COUNTRY, objectProfile.optString("country"));
                SPUser.setString(LoginActivity.this, SPUser.STATE, objectProfile.optString("state"));
                SPUser.setString(LoginActivity.this, SPUser.PROFILE_IMAGE, objectProfile.optString("profileImage"));

                Log.e("inside register", "" + objectProfile.length());
                Intent i = new Intent(LoginActivity.this, BaseActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Invalid Username or password", Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Log.e("inside JSONException:..", e.toString());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);


    }

    private void displayMessage(Profile profile) {
        // Toast.makeText(getApplicationContext(), "gfbgbngn"+profile.getFirstName(), Toast.LENGTH_LONG).show();
        // Facebook Login code here 147147147147147


        HashMap<String, String> params = new HashMap<>();
        params.put("fbid", profile.getId());
        params.put("logintype", "FB");
        params.put("name", profile.getName());
        String android_id = Settings.Secure.getString(LoginActivity.this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        params.put("device_id", android_id);
        System.out.print(android_id);
        params.put("device_token", getSharedPreferences(RegistrationIntentService.TAG, MODE_PRIVATE).getString(RegistrationIntentService.TAG, ""));

        new CustomHttpClient(LoginActivity.this).executeHttpPost(CommonUtilities.LOGIN_URL, params, progress, new CustomHttpClient.OnSuccess() {
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

    /* @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        displayMessage(profile);
    }
    */

}

