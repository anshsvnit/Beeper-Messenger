package com.beepermessenger;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.beepermessenger.CommonFiles.CommonUtilities;
import com.beepermessenger.util.CustomHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class PrivacyPolicy extends AppCompatActivity {
    TextView txt_privacy_policy, txt_privacy_date;
    Button btn_privacy_close;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);

        txt_privacy_date = (TextView) findViewById(R.id.txt_privacy_date);
        txt_privacy_policy = (TextView) findViewById(R.id.txt_privacy_policy);
        btn_privacy_close = (Button) findViewById(R.id.btn_privacy_close);
        btn_privacy_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        progress = new ProgressDialog(PrivacyPolicy.this);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        HashMap<String, String> params = new HashMap<>();
        new CustomHttpClient(PrivacyPolicy.this).executeHttpPost(CommonUtilities.PRIVACY_POLICY_URL, params, progress, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                parsingResponse(result);
            }
        }, null);
    }

    private void parsingResponse(String response) {

        try {

            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.optBoolean("status")) {
                JSONObject objectTerm = jsonObject.getJSONObject("data");

                txt_privacy_policy.setText(Html.fromHtml(objectTerm.getString("description")));
                txt_privacy_date.setText(objectTerm.getString("date"));


            } else {
                Toast.makeText(getApplicationContext(), "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Log.e("inside JSONException:..", e.toString());
        }
    }
}
