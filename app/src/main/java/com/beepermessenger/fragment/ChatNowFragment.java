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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beepermessenger.BaseActivity;
import com.beepermessenger.CommonFiles.AppNotification;
import com.beepermessenger.CommonFiles.CommonUtilities;
import com.beepermessenger.CommonFiles.Message;
import com.beepermessenger.R;
import com.beepermessenger.SplashActivity;
import com.beepermessenger.util.CustomHttpClient;
import com.beepermessenger.util.SPUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;


public class ChatNowFragment extends Fragment {


    private EditText etMessage;
    private ImageView ivSend;
    private long toId;
    private long myId;
    private String order;
    private long msgId = 0;
    private long prevMsgId = 0;
    private long nextMsgId = 0;
    private long incommingMsgId = 0;
    private String myImage;
    private Activity activity;
    private ProgressDialog progress;
    private LinearLayout llChat;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_now, container, false);
        activity = getActivity();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            toId = bundle.getLong("to_id", 0);
        }
        order = "start";
        myId = SPUser.getLong(activity, SPUser.USER_ID);
        myImage = SPUser.getString(activity, SPUser.PROFILE_IMAGE);
        progress = new ProgressDialog(activity);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);

        llChat = (LinearLayout) view.findViewById(R.id.ll_chat_now_chat);
        etMessage = (EditText) view.findViewById(R.id.et_chat_now_message);
        ivSend = (ImageView) view.findViewById(R.id.iv_chat_now_send);
        ivSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("from_user_id", SPUser.getLong(activity, SPUser.USER_ID) + "");
                params.put("to_user_id", toId + "");
                params.put("message", etMessage.getText().toString().trim());

                new CustomHttpClient(activity).executeHttpPost(CommonUtilities.SEND_MESSAGE, params, progress, new CustomHttpClient.OnSuccess() {
                    @Override
                    public void onSucess(String result) {
                        try {
                            //llChat.addView(new Message().show(activity, myId, toId, "", etMessage.getText().toString(), myImage, "Now"));
                            etMessage.setText("");
                            prevMsgId = 0;
                            nextMsgId = 0;
                            order = "start";
                            loadMessage(true, null);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
            }
        });
        loadMessage(true,progress);

        return view;

    }

    private void loadMessage(final boolean end,final ProgressDialog p) {
        HashMap<String, String> params = new HashMap<>();
        params.put("message_id", msgId + "");
        params.put("order", order);
        params.put("my_user_id", SPUser.getLong(activity, SPUser.USER_ID) + "");
        params.put("friend_user_id", toId + "");
        params.put("post_per_page", CommonUtilities.CHAT_PAR_PAGE);

        //methodName="getMessage",message_id,order="next/previous/start",my_user_id,friend_user_id,post_per_page
        new CustomHttpClient(activity).executeHttpPost(CommonUtilities.GET_MESSAGE, params, p, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {
                    JSONObject j = new JSONObject(result);
                    JSONArray jsonArray = j.optJSONArray("data");
                    if (jsonArray.length() > 0) {
                        llChat.removeAllViews();
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
                                msgId = prevMsgId;
                                order = "previous";
                                loadMessage(false,progress);
                            }
                        });
                        llChat.addView(tv);
                        tv.requestFocus();

                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jObj = jsonArray.optJSONObject(i);
                        llChat.addView(new Message().show(activity, jObj.optLong("from_user_id"), jObj.optLong("to_user_id"), "", jObj.optString("message"), jObj.optString("from_user_image"), jObj.optString("time")));
                    }
                    if (jsonArray.length() > 0) {
                        if (msgId != 0) {
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
                                    msgId = nextMsgId;
                                    order = "next";
                                    loadMessage(false,progress);
                                }
                            });
                            llChat.addView(tv);

                        }
                        EditText e = new EditText(activity);
                        if(end) {
                            llChat.addView(e);
                        }

                        e.setLayoutParams(new LinearLayout.LayoutParams(10,2));
                        e.requestFocus();
                        e.setVisibility(View.INVISIBLE);
                        prevMsgId = jsonArray.optJSONObject(0).optLong("message_id");
                        nextMsgId = jsonArray.optJSONObject(jsonArray.length() - 1).optLong("message_id");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new CustomHttpClient.OnFailure() {
            @Override
            public void onFailure(String result) {
                order = "start";
                msgId = prevMsgId = nextMsgId = 0;
            }
        });

    }


    public void receiveMessage(long msgId, long fromId, long toId, String title, String message, String toImage, String time) {
        if (this.toId == fromId) {
            incommingMsgId = msgId;
            //llChat.addView(new Message().show(activity, fromId, toId, title, message, toImage, time));
            msgId = 0;
            prevMsgId = 0;
            nextMsgId = 0;
            order = "start";
            loadMessage(true,null);
        } else {
            AppNotification.genrateNotification(activity, new Intent(activity, BaseActivity.class).putExtra("type","chat").putExtra("id",fromId), title, message, toId);
        }
    }

}
