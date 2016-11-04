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

import com.beepermessenger.CommonFiles.CommonUtilities;
import com.beepermessenger.R;
import com.beepermessenger.fragment.FriendListFragment;
import com.beepermessenger.imageloader.ImageLoader;
import com.beepermessenger.model.RequestDTO;
import com.beepermessenger.util.CustomHttpClient;
import com.beepermessenger.util.SPUser;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shree on 21/02/2016.
 */
public class RequestAdapter extends BaseAdapter {
    private FriendListFragment fragment;
    private Activity activity;
    private ArrayList<RequestDTO> items;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader ;
    private ProgressDialog progress;

    public RequestAdapter(Activity activity,
                          ArrayList<RequestDTO> alUser, FriendListFragment fragment, ProgressDialog progress) {
        this.activity = activity;
        this.items = alUser;
        this.imageLoader = new ImageLoader(activity);
        this.fragment = fragment;
        this.progress = progress;
    }


    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public RequestDTO getItem(int position) {
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
        final RequestDTO user = getItem(position);
        if (convertView == null) {
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_user_list, null);
            holder = new ViewHolder();

            holder.tvName= (TextView) convertView
                    .findViewById(R.id.tv_row_user_list_name);
            holder.civPic= (CircleImageView) convertView
                    .findViewById(R.id.civ_row_user_list_pic);
            holder.tvSendRequest= (TextView) convertView
                    .findViewById(R.id.tv_row_user_list_send_request);
            holder.tvAcceptRequest = (TextView) convertView
                    .findViewById(R.id.tv_row_user_list_accept);
            holder.tvRejectRequest= (TextView) convertView
                    .findViewById(R.id.tv_row_user_list_reject);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
            holder.tvAcceptRequest.setVisibility(View.VISIBLE);
            holder.tvRejectRequest.setVisibility(View.VISIBLE);

        convertView.setTag(holder);
        imageLoader.DisplayImage(user.getSender_image(), R.drawable.ic_launcher, holder.civPic);
        holder.tvName.setText(user.getSender_name());
        holder.tvSendRequest.setTag(user);
        holder.tvAcceptRequest.setTag(user);
        holder.tvRejectRequest.setTag(user);
        holder.tvAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestDTO dto = (RequestDTO) v.getTag();
                HashMap<String,String> params= new HashMap<>();
                params.put("user_id", SPUser.getLong(activity, SPUser.USER_ID)+"");
                params.put("r_id", dto.getRequest_id()+"");
                new CustomHttpClient(activity).executeHttpPost(CommonUtilities.ACCEPT_REQUEST, params, progress, new CustomHttpClient.OnSuccess() {
                    @Override
                    public void onSucess(String result) {
                        try {
                            if(fragment!=null)
                            {
                                fragment.loadUserData();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
            }
        });
        holder.tvRejectRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestDTO dto = (RequestDTO) v.getTag();
                HashMap<String,String> params= new HashMap<>();
                params.put("user_id", SPUser.getLong(activity, SPUser.USER_ID)+"");
                params.put("request_id", dto.getRequest_id()+"");
                new CustomHttpClient(activity).executeHttpPost(CommonUtilities.REJECT_REQUEST, params, progress, new CustomHttpClient.OnSuccess() {
                    @Override
                    public void onSucess(String result) {
                        try {
                            if(fragment!=null)
                            {
                                fragment.loadUserData();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
            }
        });
        return convertView;

    }

    static class ViewHolder {
        TextView tvName;
        CircleImageView civPic;
        TextView tvSendRequest;
        TextView tvAcceptRequest;
        TextView tvRejectRequest;
    }
}
