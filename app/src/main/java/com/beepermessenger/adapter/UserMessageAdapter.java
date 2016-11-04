package com.beepermessenger.adapter;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.beepermessenger.R;
import com.beepermessenger.fragment.FriendListFragment;
import com.beepermessenger.imageloader.ImageLoader;
import com.beepermessenger.model.UserMessageDTO;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shree on 08/03/2016.
 */
public class UserMessageAdapter extends BaseAdapter {
    private Activity activity;
    private ArrayList<UserMessageDTO> items;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader ;
    public UserMessageAdapter(Activity activity,
                              ArrayList<UserMessageDTO> alUser) {
        this.activity = activity;
        this.items = alUser;
        this.imageLoader = new ImageLoader(activity);
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public UserMessageDTO getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final UserMessageDTO userMessage = getItem(position);
        if (convertView == null) {
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_user_message, null);
            holder = new ViewHolder();

            holder.tvName= (TextView) convertView
                    .findViewById(R.id.tv_row_user_message_list_name);
            holder.civPic= (CircleImageView) convertView
                    .findViewById(R.id.civ_row_user_message_list_pic);
            holder.tvMessage= (TextView) convertView
                    .findViewById(R.id.tv_row_user_list_message_message);
            holder.tvTime = (TextView) convertView
                    .findViewById(R.id.tv_row_user_list_message_time);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setTag(holder);
        imageLoader.DisplayImage(userMessage.getUser_image(), R.drawable.ic_launcher, holder.civPic);
        holder.tvName.setText(userMessage.getUser_name());
        holder.tvMessage.setText(userMessage.getLast_message());
        holder.tvTime.setText(userMessage.getTime());
        return convertView;

    }

    static class ViewHolder {
        TextView tvName;
        CircleImageView civPic;
        TextView tvTime;
        TextView tvMessage;
    }
}
