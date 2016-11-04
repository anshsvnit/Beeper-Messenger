package com.beepermessenger;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.beepermessenger.CommonFiles.CommonUtilities;
import com.beepermessenger.util.CustomHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


public class ForgotPassword extends AppCompatActivity {

    AutoCompleteTextView act_forgot_email;
    Button btn_forgot_submit, btn_forgot_cancel;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        initialize();
        /* Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        */

    }

    private void initialize() {

        progress = new ProgressDialog(ForgotPassword.this);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);

        act_forgot_email = (AutoCompleteTextView) findViewById(R.id.act_forgot_email);
        btn_forgot_submit = (Button) findViewById(R.id.btn_forgot_submit);
        btn_forgot_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<>();

                params.put("email", act_forgot_email.getText().toString().trim());

                new CustomHttpClient(ForgotPassword.this).executeHttpPost(CommonUtilities.FORGET_PASSWORD_URL, params, progress, new CustomHttpClient.OnSuccess() {
                    @Override
                    public void onSucess(String result) {
                        parsingResponse(result);
                    }
                }, null);

            }
        });

        btn_forgot_cancel = (Button) findViewById(R.id.btn_forgot_cancel);
        btn_forgot_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void parsingResponse(String response) {

        try {

            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");

            if (status.equals("true")) {
                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                Intent i = new Intent(ForgotPassword.this, LoginActivity.class);
                startActivity(i);
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Log.e("inside JSONException:..", e.toString());
        }
    }
}
