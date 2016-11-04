package com.beepermessenger.fragment;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.beepermessenger.CommonFiles.Action;
import com.beepermessenger.CommonFiles.CommonUtilities;
import com.beepermessenger.CommonFiles.RequestCodes;
import com.beepermessenger.R;
import com.beepermessenger.rule.Reload;
import com.beepermessenger.util.CustomHttpClient;
import com.beepermessenger.util.SPUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class ActivityFragment extends Fragment implements Reload{

    private int currentPage = 1;
    private int tmpPage = 1;
    private TextView tvAddPost;
    private Activity activity;
    private ProgressDialog progress;
    private LinearLayout llmainPost;
public static ActivityFragment homeFragmentNew;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        homeFragmentNew = ActivityFragment.this;
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_home_new, container, false);
        llmainPost = (LinearLayout) view.findViewById(R.id.ll_home_posts);
        tvAddPost = (TextView) view.findViewById(R.id.tv_home_add_post);
        tvAddPost.setVisibility(View.GONE);
        progress = new ProgressDialog(activity);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        loadPost();
        return view;
    }

    public void loadPost() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", SPUser.getLong(activity, SPUser.USER_ID) + "");
        params.put("post_per_page", CommonUtilities.POST_PER_PAGE + "");
        params.put("page_number", tmpPage + "");
        new CustomHttpClient(activity).executeHttpPost(CommonUtilities.GET_ACTIVITY, params, progress, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {
                    currentPage = tmpPage;
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jArray = jsonObject.optJSONArray("data");


                    EditText e = new EditText(activity);

                    if (jArray != null) {
                        if (jArray.length() > 0) {
                            llmainPost.removeAllViews();
                            if (currentPage != 1) {

                                TextView tv = new TextView(activity);

                                tv.setText("Previous");

                                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT);
                                params.setMargins(5, 5, 5, 5); // substitute parameters for left, top, right, bottom
                                tv.setLayoutParams(params);
                                tv.setBackgroundResource(R.drawable.borderstyle);
                                tv.setPadding(5, 5, 5, 5);
                                tv.setGravity(Gravity.CENTER_HORIZONTAL);
                                tv.setTypeface(null, Typeface.BOLD);

                                tv.setOnClickListener(new View.OnClickListener() {

                                    @Override
                                    public void onClick(View v) {
                                        tmpPage--;
                                        loadPost();
                                    }
                                });
                                llmainPost.addView(tv);
                                tv.requestFocus();
                                tv.setVisibility(View.GONE);
                            }
                            llmainPost.addView(e);

                            if (jArray.length() > 0) {
                                for (int i = 0; i < jArray.length(); i++) {
                                    llmainPost.addView(new Action(progress,ActivityFragment.this).show(
                                            activity, jArray.optJSONObject(i)));
                                }
                            }
                            TextView tv = new TextView(activity);
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                    LinearLayout.LayoutParams.WRAP_CONTENT);
                            params.setMargins(5, 5, 5, 5); // substitute parameters for left, top, right, bottom
                            tv.setLayoutParams(params);
                            tv.setText("Next");
                            tv.setBackgroundResource(R.drawable.borderstyle);
                            tv.setPadding(5, 5, 5, 5);
                            tv.setGravity(Gravity.CENTER_HORIZONTAL);
                            tv.setTypeface(null, Typeface.BOLD);

                            tv.setOnClickListener(new View.OnClickListener() {

                                @Override
                                public void onClick(View v) {
                                    tmpPage++;
                                    loadPost();
                                }
                            });
                            llmainPost.addView(tv);
                            e.requestFocus();
                            e.setVisibility(View.GONE);
                            tv.setVisibility(View.GONE);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new CustomHttpClient.OnFailure() {
            @Override
            public void onFailure(String result) {
                tmpPage = currentPage;
            }
        });

    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodes.ADD_POST /*&& requestCode == Activity.RESULT_OK*/) {
            loadPost();
        }

    }

    @Override
    public void reloadDataNow() {
        loadPost();
    }
}
