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
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beepermessenger.AndroidPlayer;
import com.beepermessenger.PinchActivity;
import com.beepermessenger.R;
import com.beepermessenger.VideoActivity;
import com.beepermessenger.imageloader.ImageLoader;
import com.beepermessenger.rule.Reload;
import com.beepermessenger.util.CustomHttpClient;
import com.beepermessenger.util.MyAndroidPlayer;
import com.beepermessenger.util.SPUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class Post {
    private ProgressDialog progress;
    private Activity activity;
    private long postId;
    private TextView tvLike;
    private TextView tvComment;
    private TextView tvShare;
    private TextView tvLove;
    private TextView tvTitle;
    private boolean isLike;
    private boolean isLove;
    private EditText etComment;
    private ImageView ivComment;
    private Reload reload;
    private ArrayList<View> comments;

    public Post(ProgressDialog progress, Reload reload) {
        this.progress = progress;
        this.reload = reload;
    }

    public View show(final Activity activity, final JSONObject jsonObject) {
        this.activity = activity;
        View view;
        TextView tvName;
        TextView tvDesc;
        TextView tvTime;
        ImageView ivUserImage;
        ImageView ivPost;
        LinearLayout llComment;
        ImageLoader imageLoader;
        imageLoader = new ImageLoader(activity);
        LayoutInflater layoutInflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = layoutInflater.inflate(R.layout.row_post, null);
        tvName = (TextView) view.findViewById(R.id.tv_row_post_user_name);
        tvDesc = (TextView) view.findViewById(R.id.tv_row_post_desc);
        tvTime = (TextView) view.findViewById(R.id.tv_row_post_time);
        ivUserImage = (ImageView) view
                .findViewById(R.id.iv_row_post_user_image);
        ivPost = (ImageView) view.findViewById(R.id.iv_row_post_user_post_image);
        tvTitle = (TextView) view.findViewById(R.id.tv_row_post_title);
        tvLike = (TextView) view.findViewById(R.id.tv_row_post_user_like);
        tvLove = (TextView) view.findViewById(R.id.tv_row_post_user_love);
        tvComment = (TextView) view.findViewById(R.id.tv_row_post_user_comment);
        etComment = (EditText) view.findViewById(R.id.et_row_post_user_comment);
        ivComment = (ImageView) view.findViewById(R.id.iv_row_post_user_comment);
        tvShare = (TextView) view.findViewById(R.id.tv_row_post_user_share);
        postId = jsonObject.optLong("post_id");
        llComment = (LinearLayout) view.findViewById(R.id.ll_row_post_comment);
        //{"status":"true","message":"post Data","data":[{"post_id":"9","title":"It is the testing post for test","description":"Lorem ipsum lorem ipsum lorem lipsum this is the test post lorem lipsum","post_media":"","post_media_type":"","status":"1","date_added":"2016-03-02 00:31:54","date_updated":"0000-00-00 00:00:00","user_id":"73","likes":"0","shares":"0","loves":"0","is_love":"0","is_like":"0","user_name":"Vk","user_profileimage":"uploads\/1456852333_IMG_20160207_195645.jpg","comments":[]}]}

        tvTitle.setText(jsonObject.optString("title"));
        tvDesc.setText(jsonObject.optString("description"));
        if(jsonObject.optString("post_media_type").trim().length()==0)
        {
            ivPost.setVisibility(View.GONE);
        }else
        if (jsonObject.optString("post_media_type").equalsIgnoreCase("image")) {
            if (jsonObject.optString("post_media").trim().length() > 0) {
                imageLoader.DisplayImage(
                        jsonObject.optString("post_media"),
                        R.drawable.ic_launcher, ivPost);
                ivPost.setTag(jsonObject.optString("post_media"));
                ivPost.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.startActivity(new Intent(activity, PinchActivity.class).putExtra("data",(String)v.getTag()));
                    }
                });
            } else {
                ivPost.setVisibility(View.GONE);
            }
        } else if (jsonObject.optString("post_media_type").equalsIgnoreCase("video")) {
            if (jsonObject.optString("thumb").trim().length() > 0) {
                imageLoader.DisplayImage(
                        jsonObject.optString("thumb"),
                        R.drawable.ic_launcher, ivPost);
                ivPost.setTag(jsonObject.optString("post_media"));
                ivPost.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.startActivity(new Intent(activity, VideoActivity.class).putExtra("video_url",(String)v.getTag()));
                        /*Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(((String)v.getTag()).replaceAll(" ","%20")), "video/mp4");
                        activity.startActivity(intent);*/
                    }
                });
            } else {
                ivPost.setVisibility(View.GONE);
            }
        } else if (jsonObject.optString("post_media_type").equalsIgnoreCase("audio")) {
            if (jsonObject.optString("post_media").trim().length() > 0) {
                ivPost.setImageResource(R.mipmap.audio);
                ivPost.setTag(jsonObject.optString("post_media"));
                ivPost.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        activity.startActivity(new Intent(activity, MyAndroidPlayer.class).putExtra("path",(String)v.getTag()));
                        /*Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_VIEW);
                        intent.setDataAndType(Uri.parse(((String)v.getTag()).replaceAll(" ","%20")), "audio/mp3");
                        activity.startActivity(intent);*/
                    }
                });
            } else {
                ivPost.setVisibility(View.GONE);
            }
        }
        tvTime.setText(jsonObject.optString("date_added"));
        //"shares":"0","":"0","":"0","is_like":"0""comments":[]}]}
        tvName.setText(jsonObject.optString("user_name"));
        imageLoader.DisplayImage(jsonObject.optString("user_profileimage"),
                R.drawable.ic_launcher, ivUserImage);
        tvLike.setText(jsonObject.optLong("likes") + " Like(s)");
        isLike = jsonObject.optInt("is_like") == 1;
        if (isLike) {
            tvLike.setBackgroundResource(R.drawable.d_small_button_checked);
        } else {
            tvLike.setBackgroundResource(R.drawable.d_small_button);
        }
        tvLove.setText(jsonObject.optLong("loves") + " Love(s)");
        isLove = jsonObject.optInt("is_love") == 1;
        if (isLove) {
            tvLove.setBackgroundResource(R.drawable.d_small_button_checked);
        } else {
            tvLove.setBackgroundResource(R.drawable.d_small_button);
        }
        tvShare.setText(jsonObject.optLong("shares") + " Share(s)");

        tvLike.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isLike) {
                    likePost();
                }
            }
        });
        ivComment.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (etComment.getText().toString().trim().length() == 0) {
                    etComment.setError("Please enter any comment");
                } else {
                    commentPost();
                }
            }
        });
        tvShare.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                sharePost();

            }
        });
        tvLove.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!isLove) {
                    lovePost();
                }
            }
        });
        //"comments":[{"":"hi","post_id":"10","time":"2016-03-03 21:50:20","cmt_user_id":"73","":"Vk",}]}
        JSONArray jArray = jsonObject.optJSONArray("comments");
        tvComment.setText(jArray.length() + " Comment(s)");
        comments = new ArrayList<>();
        for (int i = 0; i < jArray.length(); i++) {
            JSONObject j = jArray.optJSONObject(i);
            View comment = layoutInflater.inflate(R.layout.row_comment, null);
            imageLoader.DisplayImage(j.optString("cmt_user_profileimage"), R.drawable.ic_launcher, (CircleImageView) comment.findViewById(R.id.civ_row_comment_user_image));
            ((TextView) comment.findViewById(R.id.tv_row_comment_user_name)).setText(j.optString("cmt_user_name"));
            ((TextView) comment.findViewById(R.id.tv_row_comment_user_comment)).setText(j.optString("comment"));
            ((TextView) comment.findViewById(R.id.tv_row_comment_user_comment_time)).setText(j.optString("time"));
            llComment.addView(comment);
            comments.add(comment);
        }
        if (comments.size() > 3) {
            for (int i = 3; i < comments.size(); i++) {
                comments.get(i).setVisibility(View.GONE);
            }
        }
        tvComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < comments.size(); i++) {
                    comments.get(i).setVisibility(View.VISIBLE);
                }
            }
        });
        llComment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < comments.size(); i++) {
                    comments.get(i).setVisibility(View.VISIBLE);
                }
            }
        });
        if (jsonObject.optLong("user_id") == SPUser.getLong(activity, SPUser.USER_ID)) {
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    new AlertDialog.Builder(activity)
                            .setTitle("Delete post")
                            .setMessage("Are you sure you want to delete this post?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deletePost();
                                    dialog.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return false;
                }
            });
        }
        return view;
    }

    private void deletePost() {
        HashMap<String, String> params = new HashMap<>();
        params.put("post_id", postId + "");
        new CustomHttpClient(activity).executeHttpPost(CommonUtilities.DELETE_POST, params, progress, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {
                    reload.reloadDataNow();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    private void commentPost() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", SPUser.getLong(activity, SPUser.USER_ID) + "");
        params.put("post_id", postId + "");
        params.put("comment", etComment.getText().toString().trim());
        new CustomHttpClient(activity).executeHttpPost(CommonUtilities.COMMENT_POST, params, progress, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    tvComment.setText(jsonObject.optLong("new_count") + " Comment(s)");
                    reload.reloadDataNow();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    private void lovePost() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", SPUser.getLong(activity, SPUser.USER_ID) + "");
        params.put("post_id", postId + "");
        new CustomHttpClient(activity).executeHttpPost(CommonUtilities.LOVE_POST, params, progress, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    isLove = true;
                    tvLove.setText(jsonObject.optLong("new_count") + " Love(s)");
                    if (isLove) {
                        tvLove.setBackgroundResource(R.drawable.d_small_button_checked);
                    } else {
                        tvLove.setBackgroundResource(R.drawable.d_small_button);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    private void sharePost() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", SPUser.getLong(activity, SPUser.USER_ID) + "");
        params.put("post_id", postId + "");

        new CustomHttpClient(activity).executeHttpPost(CommonUtilities.SHARE_POST, params, progress, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    tvShare.setText(jsonObject.optLong("new_count") + " Share(s)");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }

    private void likePost() {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", SPUser.getLong(activity, SPUser.USER_ID) + "");
        params.put("post_id", postId + "");
        new CustomHttpClient(activity).executeHttpPost(CommonUtilities.LIKE_POST, params, progress, new CustomHttpClient.OnSuccess() {
            @Override
            public void onSucess(String result) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    isLike = true;
                    tvLike.setText(jsonObject.optLong("new_count") + " Like(s)");
                    if (isLike) {
                        tvLike.setBackgroundResource(R.drawable.d_small_button_checked);
                    } else {
                        tvLike.setBackgroundResource(R.drawable.d_small_button);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);
    }


}
