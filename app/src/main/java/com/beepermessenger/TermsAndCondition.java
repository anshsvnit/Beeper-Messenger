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

public class TermsAndCondition extends AppCompatActivity {
    TextView txt_term_tc,txt_term_date;
    Button btn_term_close;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_condition);
        progress = new ProgressDialog(TermsAndCondition.this);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);

        txt_term_date= (TextView) findViewById(R.id.txt_term_date);
        txt_term_tc= (TextView) findViewById(R.id.txt_term_tc);
        btn_term_close = (Button)findViewById(R.id.btn_term_close);
        btn_term_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        HashMap<String,String> params= new HashMap<>();
        new CustomHttpClient(TermsAndCondition.this).executeHttpPost(CommonUtilities.TERM_CONDITION_URL, params, progress, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                parsingResponse(result);
            }
        }, null);

    }
    private void parsingResponse(String response) {

        try {

            JSONObject jsonObject = new JSONObject(response);
           // String status = jsonObject.getString("status");

            if(jsonObject.optBoolean("status"))
            {
               // Toast.makeText(getApplicationContext(), "" + jsonObject.getString("message"), Toast.LENGTH_LONG).show();

                JSONObject objectTerm = jsonObject.getJSONObject("data");

                txt_term_tc.setText(Html.fromHtml(objectTerm.getString("description")));
                txt_term_date.setText(objectTerm.getString("date"));


            }
            else
            {
                Toast.makeText(getApplicationContext(), ""+jsonObject.getString("message"), Toast.LENGTH_LONG).show();
            }

        } catch (JSONException e) {
            Log.e("inside JSONException:..",e.toString());
        }
    }
}
