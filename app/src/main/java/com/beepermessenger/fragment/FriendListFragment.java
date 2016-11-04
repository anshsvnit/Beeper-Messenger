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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.beepermessenger.CommonFiles.CommonUtilities;
import com.beepermessenger.R;
import com.beepermessenger.adapter.RequestAdapter;
import com.beepermessenger.adapter.UserAdapter;
import com.beepermessenger.model.RequestDTO;
import com.beepermessenger.model.UserDTO;
import com.beepermessenger.util.CustomHttpClient;
import com.beepermessenger.util.SPUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FriendListFragment extends Fragment {

    private ListView lvFriend;
    private Activity activity;
    private ProgressDialog progress;
    private ArrayList<UserDTO> alUser;
    private String search;
    private EditText etSearch;
    private boolean canSearch = true;
    private ImageView ivAccept;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        etSearch = (EditText) view.findViewById(R.id.et_friend_list_search);
        ivAccept = (ImageView) view.findViewById(R.id.iv_friend_list_accept_request);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 0) {
                    loadUserData();
                } else if (s.toString().length() % 2 == 0 && canSearch) {
                    canSearch = false;
                    search = s.toString();
                    searchData();
                }
            }
        });
        ivAccept.setVisibility(View.GONE);
        ivAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadMyRequests();
            }
        });
        search = "";
        lvFriend = (ListView) view.findViewById(R.id.lv_friend);
        progress = new ProgressDialog(activity);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        loadMyRequests();
        return view;
    }

    public void searchData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", SPUser.getLong(activity, SPUser.USER_ID) + "");
        params.put("search_string", search);
        new CustomHttpClient(activity).executeHttpPost(CommonUtilities.SEARCH_USER, params, progress, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                canSearch = true;
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.optJSONArray("list");
                    if (jsonArray.length() > 0) {
                        alUser = new Gson().fromJson(jsonArray.toString(),
                                new TypeToken<List<UserDTO>>() {
                                }.getType());
                        lvFriend.setAdapter(new UserAdapter(activity, alUser, true, FriendListFragment.this, progress));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new CustomHttpClient.OnFailure() {
            @Override
            public void onFailure(String result) {
                canSearch = true;
            }
        });

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
                        lvFriend.setAdapter(new UserAdapter(activity, alUser,true));
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

    public void loadMyRequests() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", SPUser.getLong(activity, SPUser.USER_ID) + "");
        new CustomHttpClient(activity).executeHttpPost(CommonUtilities.MY_REQUEST, params, progress, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    JSONArray jsonArray = jsonObject.optJSONArray("list");
                    if (jsonArray.length() > 0) {
                        ArrayList<RequestDTO> alRequest = new Gson().fromJson(jsonArray.toString(),
                                new TypeToken<List<RequestDTO>>() {
                                }.getType());
                        lvFriend.setAdapter(new RequestAdapter(activity, alRequest, FriendListFragment.this, progress));
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
