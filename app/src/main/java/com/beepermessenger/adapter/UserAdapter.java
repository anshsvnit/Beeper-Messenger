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

import com.beepermessenger.BaseActivity;
import com.beepermessenger.CommonFiles.AppFragments;
import com.beepermessenger.CommonFiles.CommonUtilities;
import com.beepermessenger.R;
import com.beepermessenger.fragment.FriendListFragment;
import com.beepermessenger.imageloader.ImageLoader;
import com.beepermessenger.model.UserDTO;
import com.beepermessenger.util.CustomHttpClient;
import com.beepermessenger.util.SPUser;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Shree on 21/02/2016.
 */
public class UserAdapter extends BaseAdapter {
    private boolean sendMessage;
    private boolean acceptRequest;
    private FriendListFragment fragment;
    private boolean showRequest;
    private Activity activity;
    private ArrayList<UserDTO> items;
    private LayoutInflater layoutInflater;
    private ImageLoader imageLoader ;
    private ProgressDialog progress;
    public UserAdapter(Activity activity,
                       ArrayList<UserDTO> alUser) {
        this.activity = activity;
        this.items = alUser;
        this.imageLoader = new ImageLoader(activity);
        showRequest=false;
    }
    public UserAdapter(Activity activity,
                       ArrayList<UserDTO> alUser,boolean showRequest,FriendListFragment fragment,ProgressDialog progress) {
        this.activity = activity;
        this.items = alUser;
        this.imageLoader = new ImageLoader(activity);
        this.showRequest = showRequest;
        this.fragment = fragment;
        this.progress = progress;
    }

    public UserAdapter(Activity activity, ArrayList<UserDTO> alUser, boolean sendMessage) {
        this.activity = activity;
        this.items = alUser;
        this.imageLoader = new ImageLoader(activity);
       this.sendMessage=sendMessage;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public UserDTO getItem(int position) {
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
        final UserDTO user = getItem(position);
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
        if(showRequest)
        {
            holder.tvSendRequest.setVisibility(View.VISIBLE);
        }
        if(acceptRequest)
        {
            holder.tvAcceptRequest.setVisibility(View.VISIBLE);
            holder.tvRejectRequest.setVisibility(View.VISIBLE);
        }
        convertView.setTag(holder);
        imageLoader.DisplayImage(user.getProfileImage(), R.drawable.ic_launcher, holder.civPic);
        holder.toId = user.getUser_id();
        holder.tvName.setText(user.getName());
        holder.tvSendRequest.setTag(user);
        holder.tvAcceptRequest.setTag(user);
        holder.tvRejectRequest.setTag(user);
        holder.tvSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDTO dto = (UserDTO) v.getTag();
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", SPUser.getLong(activity, SPUser.USER_ID) + "");
                params.put("reciver_user_id", dto.getUser_id() + "");
                new CustomHttpClient(activity).executeHttpPost(CommonUtilities.FRIEND_REQUEST, params, progress, new CustomHttpClient.OnSuccess() {
                    @Override
                    public void onSucess(String result) {
                        try {
                            if (fragment != null) {
                                fragment.searchData();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
            }
        });
        holder.tvAcceptRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserDTO dto = (UserDTO) v.getTag();
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
                UserDTO dto = (UserDTO) v.getTag();
                HashMap<String,String> params= new HashMap<>();
                params.put("user_id", SPUser.getLong(activity, SPUser.USER_ID)+"");
                params.put("r_id", dto.getRequest_id()+"");
                new CustomHttpClient(activity).executeHttpPost(CommonUtilities.REJECT_REQUEST, params, progress, new CustomHttpClient.OnSuccess() {
                    @Override
                    public void onSucess(String result) {
                        try {
                            if(fragment!=null)
                            {
                                fragment.loadMyRequests();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, null);
            }
        });
        if(sendMessage)
        {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(activity instanceof BaseActivity)
                    {
                        long id = ((ViewHolder) v.getTag()).toId;

                        ((BaseActivity) activity).channgeFragment(AppFragments.CHAT_NOW, new Long(id));
                    }
                }
            });
        }
        return convertView;

    }

    static class ViewHolder {
        TextView tvName;
        CircleImageView civPic;
        TextView tvSendRequest;
        TextView tvAcceptRequest;
        TextView tvRejectRequest;
        long toId;
    }
}
