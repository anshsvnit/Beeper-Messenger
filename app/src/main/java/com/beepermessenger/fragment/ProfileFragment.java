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
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beepermessenger.BaseActivity;
import com.beepermessenger.ChangePassword;
import com.beepermessenger.CommonFiles.AppFragments;
import com.beepermessenger.CommonFiles.CommonUtilities;
import com.beepermessenger.CommonFiles.RequestCodes;
import com.beepermessenger.LoginActivity;
import com.beepermessenger.R;
import com.beepermessenger.RegisterActivity;
import com.beepermessenger.RegistrationIntentService;
import com.beepermessenger.imageloader.ImageLoader;
import com.beepermessenger.util.CustomHttpClient;
import com.beepermessenger.util.SPUser;

import java.util.HashMap;


public class ProfileFragment extends Fragment {

    private ListView lvFriend, lv_comment;

    TextView title_profile, txt_email_profile, txt_contact_profile, txt_dob_profile, txt_state_profile, txt_gender_profile,
            txt_edit_profile, txt_change_password_profile;
    private ImageView ivImage;
    private Button btnLogout;
    private Activity activity;
    private ProgressDialog progress;
    private TextView tvActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        activity = getActivity();
        progress = new ProgressDialog(activity);
        progress.setMessage("Please wait...");
        progress.setCancelable(false);
        initialize(view);
        setProfile();

        return view;

    }

    private void setProfile() {

        txt_email_profile.setText(SPUser.getString(getActivity(), SPUser.EMAIL));
        txt_contact_profile.setText(SPUser.getString(getActivity(), SPUser.PHONE));
        txt_gender_profile.setText(SPUser.getString(getActivity(), SPUser.GENDER));
        txt_dob_profile.setText(SPUser.getString(getActivity(), SPUser.D_O_B));
        title_profile.setText(SPUser.getString(getActivity(), SPUser.NAME));
        txt_state_profile.setText(SPUser.getString(getActivity(), SPUser.STATE));


    }

    private void initialize(View view) {
        Bundle extras = getActivity().getIntent().getExtras();


        title_profile = (TextView) view.findViewById(R.id.title_profile);
        txt_email_profile = (TextView) view.findViewById(R.id.txt_email_profile);
        txt_contact_profile = (TextView) view.findViewById(R.id.txt_contact_profile);
        txt_dob_profile = (TextView) view.findViewById(R.id.txt_dob_profile);
        txt_state_profile = (TextView) view.findViewById(R.id.txt_state_profile);
        txt_gender_profile = (TextView) view.findViewById(R.id.txt_gender_profile);
        btnLogout = (Button) view.findViewById(R.id.btn_edit_profole_logout);
        txt_edit_profile = (TextView) view.findViewById(R.id.txt_edit_profile);
        txt_edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), RegisterActivity.class);
                startActivityForResult(i, RequestCodes.EDIT_PROFILE);
                // getActivity().finish();
            }
        });

        txt_change_password_profile = (TextView) view.findViewById(R.id.txt_change_password_profile);
        txt_change_password_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ChangePassword.class);
                startActivity(i);
                // getActivity().finish();
            }
        });
        ivImage = (ImageView) view.findViewById(R.id.imageView_user_profile);
        new ImageLoader(getActivity()).DisplayImage(SPUser.getString(getActivity(), SPUser.PROFILE_IMAGE), R.drawable.ic_launcher, ivImage);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> params = new HashMap<>();
                params.put("user_id", SPUser.getLong(activity, SPUser.USER_ID) + "");
                String android_id = Settings.Secure.getString(activity.getContentResolver(),
                        Settings.Secure.ANDROID_ID);
                params.put("device_id", android_id);
                new CustomHttpClient(getActivity()).executeHttpPost(CommonUtilities.LOGOUT, params, progress, new CustomHttpClient.OnSuccess() {
                    @Override
                    public void onSucess(String result) {
                        SPUser.clear(activity);
                        activity.startActivity(new Intent(activity, LoginActivity.class));
                        activity.finish();
                    }
                }, null);
            }
        });
        tvActivity = (TextView) view.findViewById(R.id.txt_edit_profile_activity);
        tvActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activity instanceof BaseActivity)
                {
                    ((BaseActivity) activity).channgeFragment(AppFragments.ACTIVITY,null);
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RequestCodes.EDIT_PROFILE) {
            txt_email_profile.setText(SPUser.getString(getActivity(), SPUser.EMAIL));
            txt_contact_profile.setText(SPUser.getString(getActivity(), SPUser.PHONE));
            txt_gender_profile.setText(SPUser.getString(getActivity(), SPUser.GENDER));
            txt_dob_profile.setText(SPUser.getString(getActivity(), SPUser.D_O_B));
            title_profile.setText(SPUser.getString(getActivity(), SPUser.NAME));
            txt_state_profile.setText(SPUser.getString(getActivity(), SPUser.STATE));

        }
    }
}
