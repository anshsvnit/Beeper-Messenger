package com.beepermessenger;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.beepermessenger.CommonFiles.CommonUtilities;
import com.beepermessenger.util.CustomHttpClient;
import com.beepermessenger.util.EmailChecker;
import com.beepermessenger.util.SPUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ChangePassword extends AppCompatActivity {

    AutoCompleteTextView act_email_changep, act_oldpassword_changep, act_newpassword_changep, act_confirm_password_changep;
    Button btn_change_submit, btn_change_cancel;

    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initialize();
    }

    private void initialize() {

        Bundle extras = getIntent().getExtras();


        progress = new ProgressDialog(ChangePassword.this);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        act_email_changep = (AutoCompleteTextView) findViewById(R.id.act_email_changep);
        act_email_changep.setText(SPUser.getString(ChangePassword.this,SPUser.EMAIL));
        act_oldpassword_changep = (AutoCompleteTextView) findViewById(R.id.act_oldpassword_changep);
        act_newpassword_changep = (AutoCompleteTextView) findViewById(R.id.act_newpassword_changep);
        act_confirm_password_changep = (AutoCompleteTextView) findViewById(R.id.act_confirm_password_changep);

        btn_change_submit = (Button) findViewById(R.id.btn_change_submit);
        btn_change_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkValidation()) {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("id", SPUser.getLong(ChangePassword.this,SPUser.USER_ID)+"");
                    params.put("old_password", act_oldpassword_changep.getText().toString().trim());
                    params.put("new_password", act_newpassword_changep.getText().toString().trim());
                    new CustomHttpClient(ChangePassword.this).executeHttpPost(CommonUtilities.CHANGE_PASSWORD_URL, params, progress, new CustomHttpClient.OnSuccess() {
                        @Override
                        public void onSucess(String result) {
                            parsingResponse(result);
                        }
                    }, null);
                }
            }
        });

        btn_change_cancel = (Button) findViewById(R.id.btn_change_cancel);
        btn_change_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean checkValidation() {
        boolean flag = true;

        if (act_email_changep.getText().toString().trim().length() == 0) {
            act_email_changep.setError("Please enter Email");
            flag = false;
        }
        if (!EmailChecker.isValid(act_email_changep.getText().toString())) {
            act_email_changep.setError("Please Enter Valid E-mail");
            flag = false;
        }

        if (act_oldpassword_changep.getText().toString().trim().length() == 0) {
            act_oldpassword_changep.setError("Please enter old password");
            flag = false;
        }
        if (act_newpassword_changep.getText().toString().trim().length() == 0) {
            act_newpassword_changep.setError("Please enter new password");
            flag = false;
        }
        if (act_newpassword_changep.getText().toString().trim().length() < 5) {
            act_newpassword_changep.setError("Password is too short");
            flag = false;
        }
        if (!act_newpassword_changep.getText().toString().trim().equals(act_confirm_password_changep.getText().toString())) {
            act_confirm_password_changep.setError("Password and Confirm Password don not match");
            flag = false;
        }
        return flag;
    }

    private void parsingResponse(String response) {

        try {

            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");

            if (status.equals("true")) {
                /*Intent i = new Intent(ChangePassword.this, LoginActivity.class);
                startActivity(i);*/
                finish();
            }

        } catch (JSONException e) {
            Log.e("inside JSONException:..", e.toString());
        }
    }
}
