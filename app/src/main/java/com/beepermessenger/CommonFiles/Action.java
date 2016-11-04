package com.beepermessenger.CommonFiles;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beepermessenger.R;
import com.beepermessenger.imageloader.ImageLoader;
import com.beepermessenger.rule.Reload;
import com.beepermessenger.util.CustomHttpClient;
import com.beepermessenger.util.SPUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class Action {
    private ProgressDialog progress;
    private Activity activity;
    private Reload reload;
    public Action(ProgressDialog progress, Reload reload) {
        this.progress = progress;
        this.reload = reload;
    }

    public View show(final Activity activity, final JSONObject jsonObject) {
        this.activity = activity;
        View view;
        TextView tvDesc;
        TextView tvTime;
        LayoutInflater layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.row_action, null);
        tvDesc = (TextView) view.findViewById(R.id.tv_row_action_desc);
        tvTime = (TextView) view.findViewById(R.id.tv_row_action_time);
        tvDesc.setText(jsonObject.optString("activity_message"));
        tvTime.setText(jsonObject.optString("time"));
        return view;
    }
}
