package com.beepermessenger.CommonFiles;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.beepermessenger.R;
import com.beepermessenger.imageloader.ImageLoader;
import com.beepermessenger.util.SPUser;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shree on 3/7/2016.
 */
public class Message {
    private View view;
    private TextView tvMessage;
    private TextView tvTime;
    private CircleImageView civUser;


    public View show(final Activity activity, long fromId, long toId, String title, String message,String fromImage,String time)
    {
        LayoutInflater layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if(fromId== SPUser.getLong(activity,SPUser.USER_ID)) {
            view = layoutInflater.inflate(R.layout.row_outgoing, null);
            tvMessage = (TextView) view.findViewById(R.id.tv_outgoing_message_message);
            tvTime = (TextView) view.findViewById(R.id.tv_outgoing_message_time);
            civUser = (CircleImageView)view.findViewById(R.id.civ_outgoing_message_user_image);
            new ImageLoader(activity).DisplayImage(SPUser.getString(activity,SPUser.PROFILE_IMAGE),R.drawable.ic_launcher,civUser);
        }else{
            view = layoutInflater.inflate(R.layout.row_incomming, null);
            tvMessage = (TextView) view.findViewById(R.id.tv_incomming_message_message);
            tvTime = (TextView) view.findViewById(R.id.tv_incomming_message_time);
            civUser = (CircleImageView)view.findViewById(R.id.civ_incomming_message_user_image);
            new ImageLoader(activity).DisplayImage(fromImage,R.drawable.ic_launcher,civUser);
        }
        tvMessage.setText(message);
        tvTime.setText(time);
        return  view;
    }
}
