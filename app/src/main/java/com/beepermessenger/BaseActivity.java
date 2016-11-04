package com.beepermessenger;
/**
 * Class :
 * Task : This class
 * Author: playstore.apps.android@gmail.com
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.beepermessenger.CommonFiles.AppFragments;
import com.beepermessenger.CommonFiles.AppNotification;
import com.beepermessenger.CommonFiles.CommonUtilities;
import com.beepermessenger.CommonFiles.RequestCodes;
import com.beepermessenger.fragment.ActivityFragment;
import com.beepermessenger.fragment.ChatFragment;
import com.beepermessenger.fragment.ChatNowFragment;
import com.beepermessenger.fragment.FriendListFragment;
import com.beepermessenger.fragment.HomeFragmentNew;
import com.beepermessenger.fragment.ProfileFragment;
import com.beepermessenger.fragment.WallFragment;
import com.beepermessenger.imageloader.ImageLoader;
import com.beepermessenger.util.CustomHttpClient;
import com.beepermessenger.util.SPUser;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseActivity extends AppCompatActivity {
    TextView tv_header_home, tv_header_chat, tv_header_invite, tv_header_wall, tv_header_profile;
    private AppFragments currentFragment;
    FrameLayout frameLayout;
    private CircleImageView civUserImage;
    private TextView tvUserName;
    public static BaseActivity activity;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        activity = BaseActivity.this;
        initializeComponents();
        fragmentManager = getFragmentManager();
        if (savedInstanceState == null) {
            tv_header_home.setBackgroundColor(getResources().getColor(android.R.color.white));
            tv_header_home.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            // get an instance of FragmentTransaction from your Activity

            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //add a fragment
            HomeFragmentNew myFragment = new HomeFragmentNew();
            fragment = myFragment;
            currentFragment = AppFragments.HOME;

            // Bundle arguments = new Bundle();
            // arguments.putBoolean("shouldYouCreateAChildFragment", true);
            //myFragment.setArguments(arguments);

            fragmentTransaction.add(R.id.myfragment, myFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        String type = getIntent().getStringExtra("type");
        if (type != null) {
            if (type.equalsIgnoreCase("chat")) {
                channgeFragment(AppFragments.CHAT_NOW, new Long(getIntent().getLongExtra("id", 0)));
            }
        }
    }

    private void initializeComponents() {

        Bundle extras = getIntent().getExtras();


        //frameLayout = (FrameLayout)findViewById(R.id.frameLayout);
        tv_header_home = (TextView) findViewById(R.id.tv_header_home);
        tv_header_home.setOnClickListener(new onButtonClick());

        tv_header_wall = (TextView) findViewById(R.id.tv_header_wall);
        tv_header_wall.setOnClickListener(new onButtonClick());

        tv_header_invite = (TextView) findViewById(R.id.tv_header_invite);
        tv_header_invite.setOnClickListener(new onButtonClick());

        tv_header_chat = (TextView) findViewById(R.id.tv_header_chat);
        tv_header_chat.setOnClickListener(new onButtonClick());

        tv_header_profile = (TextView) findViewById(R.id.tv_header_profile);
        tv_header_profile.setOnClickListener(new onButtonClick());
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvUserName.setText("Welcome " + SPUser.getString(BaseActivity.this, SPUser.NAME));
        civUserImage = (CircleImageView) findViewById(R.id.civ_header_user);
        civUserImage.setOnClickListener(new onButtonClick());
        ImageLoader imageLoader = new ImageLoader(BaseActivity.this);
        imageLoader.DisplayImage(SPUser.getString(BaseActivity.this, SPUser.PROFILE_IMAGE), R.drawable.ic_launcher, civUserImage);


    }

    private class onButtonClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.tv_header_invite:

                    tv_header_invite.setBackgroundColor(getResources().getColor(android.R.color.white));
                    tv_header_invite.setTextColor(getResources().getColor(R.color.colorheadertext));

                    tv_header_home.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_home.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_wall.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_wall.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_chat.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_chat.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_profile.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_profile.setTextColor(getResources().getColor(android.R.color.white));

                    //add a fragment
                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


                    FriendListFragment myFragment = new FriendListFragment();
                    fragmentTransaction.replace(R.id.myfragment, myFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    fragment = myFragment;
                    currentFragment = AppFragments.FRIEND;
                    break;

                case R.id.tv_header_home:

                    tv_header_home.setBackgroundColor(getResources().getColor(android.R.color.white));
                    tv_header_home.setTextColor(getResources().getColor(R.color.colorheadertext));

                    tv_header_invite.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_invite.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_wall.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_wall.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_chat.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_chat.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_profile.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_profile.setTextColor(getResources().getColor(android.R.color.white));


                    //add a fragment
                    FragmentManager fragmentManagerHome = getFragmentManager();
                    FragmentTransaction fragmentTransactionHome = fragmentManagerHome.beginTransaction();

                    HomeFragmentNew homeFragment = new HomeFragmentNew();
                    fragmentTransactionHome.replace(R.id.myfragment, homeFragment);
                    fragmentTransactionHome.addToBackStack(null);
                    fragmentTransactionHome.commit();
                    fragment = homeFragment;
                    currentFragment = AppFragments.HOME;
                    break;
                case R.id.tv_header_wall:

                    tv_header_wall.setBackgroundColor(getResources().getColor(android.R.color.white));
                    tv_header_wall.setTextColor(getResources().getColor(R.color.colorheadertext));

                    tv_header_invite.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_invite.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_home.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_home.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_chat.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_chat.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_profile.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_profile.setTextColor(getResources().getColor(android.R.color.white));

                    //add a fragment
                    FragmentManager fragmentManagerWall = getFragmentManager();
                    FragmentTransaction fragmentTransactionWall = fragmentManagerWall.beginTransaction();

                    WallFragment wallFragment = new WallFragment();
                    fragmentTransactionWall.replace(R.id.myfragment, wallFragment);
                    fragmentTransactionWall.addToBackStack(null);
                    fragmentTransactionWall.commit();
                    fragment = wallFragment;
                    currentFragment = AppFragments.WALL;
                    break;

                case R.id.tv_header_chat:

                    tv_header_chat.setBackgroundColor(getResources().getColor(android.R.color.white));
                    tv_header_chat.setTextColor(getResources().getColor(R.color.colorheadertext));

                    tv_header_wall.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_wall.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_invite.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_invite.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_home.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_home.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_profile.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_profile.setTextColor(getResources().getColor(android.R.color.white));


                    //add a fragment
                    FragmentManager fragmentManagerChat = getFragmentManager();
                    FragmentTransaction fragmentTransactionChat = fragmentManagerChat.beginTransaction();

                    ChatFragment chatFragment = new ChatFragment();
                    fragmentTransactionChat.replace(R.id.myfragment, chatFragment);
                    fragmentTransactionChat.addToBackStack(null);
                    fragmentTransactionChat.commit();
                    fragment = chatFragment;
                    currentFragment = AppFragments.CHAT;
                    break;

                case R.id.tv_header_profile:

                    tv_header_profile.setBackgroundColor(getResources().getColor(android.R.color.white));
                    tv_header_profile.setTextColor(getResources().getColor(R.color.colorheadertext));

                    tv_header_chat.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_chat.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_wall.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_wall.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_invite.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_invite.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_home.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_home.setTextColor(getResources().getColor(android.R.color.white));

                    //add a fragment
                    FragmentManager fragmentManagerProfile = getFragmentManager();
                    FragmentTransaction fragmentTransactionProfile = fragmentManagerProfile.beginTransaction();

                    ProfileFragment profileFragment = new ProfileFragment();

                    Bundle bundle = new Bundle();

                    profileFragment.setArguments(bundle);

                    fragmentTransactionProfile.replace(R.id.myfragment, profileFragment);
                    fragmentTransactionProfile.addToBackStack(null);
                    fragmentTransactionProfile.commit();
                    fragment = profileFragment;
                    currentFragment = AppFragments.PROFILE;
                    break;
                case R.id.civ_header_user:

                    tv_header_profile.setBackgroundColor(getResources().getColor(android.R.color.white));
                    tv_header_profile.setTextColor(getResources().getColor(R.color.colorheadertext));

                    tv_header_chat.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_chat.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_wall.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_wall.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_invite.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_invite.setTextColor(getResources().getColor(android.R.color.white));

                    tv_header_home.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
                    tv_header_home.setTextColor(getResources().getColor(android.R.color.white));

                    //add a fragment
                    FragmentManager fragmentManagerProfile1 = getFragmentManager();
                    FragmentTransaction fragmentTransactionProfile1 = fragmentManagerProfile1.beginTransaction();

                    ProfileFragment profileFragment1 = new ProfileFragment();

                    Bundle bundle1 = new Bundle();

                    profileFragment1.setArguments(bundle1);

                    fragmentTransactionProfile1.replace(R.id.myfragment, profileFragment1);
                    fragmentTransactionProfile1.addToBackStack(null);
                    fragmentTransactionProfile1.commit();
                    fragment = profileFragment1;
                    currentFragment = AppFragments.PROFILE;
                    break;
            }
        }
    }

    public void gotMessage(final long msgId, final long fromId, final long toId, final String title, final String message, final String fromImage, final String time) {
        if (fragment instanceof ChatNowFragment) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((ChatNowFragment) fragment).receiveMessage(msgId, fromId, toId, title, message, fromImage, time);
                }
            });

        } else {
            AppNotification.genrateNotification(BaseActivity.this, new Intent(this, BaseActivity.class).putExtra("type", "chat").putExtra("id", fromId), title, message, toId);
        }
    }

    public void channgeFragment(AppFragments fragments, Object obj) {
        tv_header_chat.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
        tv_header_chat.setTextColor(getResources().getColor(android.R.color.white));

        tv_header_wall.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
        tv_header_wall.setTextColor(getResources().getColor(android.R.color.white));

        tv_header_invite.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
        tv_header_invite.setTextColor(getResources().getColor(android.R.color.white));

        tv_header_home.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
        tv_header_home.setTextColor(getResources().getColor(android.R.color.white));

        tv_header_profile.setBackgroundColor(getResources().getColor(R.color.colorheadertext));
        tv_header_profile.setTextColor(getResources().getColor(android.R.color.white));

        switch (fragments) {
            case HOME:
                tv_header_home.setBackgroundColor(getResources().getColor(android.R.color.white));
                tv_header_home.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new HomeFragmentNew();
                fragmentTransaction.add(R.id.myfragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                currentFragment = AppFragments.HOME;
                break;
            case CHAT:
                tv_header_chat.setBackgroundColor(getResources().getColor(android.R.color.white));
                tv_header_chat.setTextColor(getResources().getColor(R.color.colorheadertext));
                //add a fragment

                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new ChatFragment();
                fragmentTransaction.replace(R.id.myfragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                currentFragment = AppFragments.CHAT;
                break;

            case CHAT_NOW:
                tv_header_chat.setBackgroundColor(getResources().getColor(android.R.color.white));
                tv_header_chat.setTextColor(getResources().getColor(R.color.colorheadertext));
                //add a fragment

                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new ChatNowFragment();
                Bundle bundle = new Bundle();
                long id = ((Long) obj).longValue();

                bundle.putLong("to_id", id);
                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.myfragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                currentFragment = AppFragments.CHAT_NOW;
                break;
            case FRIEND:
                tv_header_invite.setBackgroundColor(getResources().getColor(android.R.color.white));
                tv_header_invite.setTextColor(getResources().getColor(R.color.colorheadertext));

                //add a fragment
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                FriendListFragment myFragment = new FriendListFragment();
                fragmentTransaction.replace(R.id.myfragment, myFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                currentFragment = AppFragments.FRIEND;
                break;
            case WALL:
                tv_header_wall.setBackgroundColor(getResources().getColor(android.R.color.white));
                tv_header_wall.setTextColor(getResources().getColor(R.color.colorheadertext));

                //add a fragment
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();

                fragment = new WallFragment();
                fragmentTransaction.replace(R.id.myfragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                currentFragment = AppFragments.WALL;
                break;
            case PROFILE:
                tv_header_profile.setBackgroundColor(getResources().getColor(android.R.color.white));
                tv_header_profile.setTextColor(getResources().getColor(R.color.colorheadertext));

                //add a fragment
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new ProfileFragment();
                fragmentTransaction.replace(R.id.myfragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                currentFragment = AppFragments.PROFILE;
                break;
            case ACTIVITY:
                tv_header_profile.setBackgroundColor(getResources().getColor(android.R.color.white));
                tv_header_profile.setTextColor(getResources().getColor(R.color.colorheadertext));

                //add a fragment
                fragmentManager = getFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragment = new ActivityFragment();
                fragmentTransaction.replace(R.id.myfragment, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                currentFragment = AppFragments.ACTIVITY;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activity = null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodes.ADD_POST /*&& requestCode == Activity.RESULT_OK*/) {
            if (currentFragment == AppFragments.WALL) {
                ((WallFragment) fragment).reloadDataNow();
            }
        }
    }
}
