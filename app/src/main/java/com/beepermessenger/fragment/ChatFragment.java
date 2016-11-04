package com.beepermessenger.fragment;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beepermessenger.BaseActivity;
import com.beepermessenger.CommonFiles.AppFragments;
import com.beepermessenger.CommonFiles.CommonUtilities;
import com.beepermessenger.CommonFiles.Parser;
import com.beepermessenger.R;
import com.beepermessenger.adapter.UserAdapter;
import com.beepermessenger.adapter.UserMessageAdapter;
import com.beepermessenger.model.UserDTO;
import com.beepermessenger.model.UserMessageDTO;
import com.beepermessenger.util.CustomHttpClient;
import com.beepermessenger.util.SPUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ChatFragment extends Fragment {

    private ListView lvFriend;
    private Activity activity;
    private ProgressDialog progress;
    private ArrayList<UserMessageDTO> alUserMessage;
    private ArrayList<UserDTO> alUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        activity = getActivity();
        progress = new ProgressDialog(activity);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        lvFriend = (ListView) view.findViewById(R.id.lv_caht_friend_last_meaasge);
        /*HashMap<String, String> params = new HashMap<>();
        params.put("my_user_id", SPUser.getLong(activity,SPUser.USER_ID)+"");
        new CustomHttpClient(activity).executeHttpPost(CommonUtilities.CHAT_WINDOW, params, progress, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                alUserMessage = Parser.parsingUserMessage(result);
                if (alUserMessage != null) {
                    lvFriend.setAdapter(new UserMessageAdapter(activity, alUserMessage));
                }

            }
        }, null);
        lvFriend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((BaseActivity) activity).channgeFragment(AppFragments.CHAT_NOW, new Long(alUserMessage.get(position).getUser_id()));
            }
        });*/
        loadUserData();
        return view;

    }
    public void loadUserData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", SPUser.getLong(activity, SPUser.USER_ID) + "");
        new CustomHttpClient(activity).executeHttpPost(CommonUtilities.GET_FRIENDS, params, progress, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.optJSONArray("list");
                    if (jsonArray.length() > 0) {
                        alUser = new Gson().fromJson(jsonArray.toString(),
                                new TypeToken<List<UserDTO>>() {
                                }.getType());
                        lvFriend.setAdapter(new UserAdapter(activity, alUser, true));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new CustomHttpClient.OnFailure() {
            @Override
            public void onFailure(String result) {
                alUser = new ArrayList<UserDTO>();
                lvFriend.setAdapter(new UserAdapter(activity, alUser));
            }
        });

    }


}
