package com.beepermessenger.fragment;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.beepermessenger.R;
import com.beepermessenger.adapter.AllAdapter;


public class GroupListFragment extends Fragment {

    private ListView lvFriend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_group_list, container, false);

        lvFriend = (ListView) view.findViewById(R.id.lv_friend);
        lvFriend.setAdapter(new AllAdapter(getActivity(), getActivity(),R.layout.list_item_group_list));
        return view;

    }


}
