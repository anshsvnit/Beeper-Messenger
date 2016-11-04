package com.beepermessenger.adapter;
/**
 * Class : 
 * Task : This class 
 * Author: playstore.apps.android@gmail.com
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.Objects;

public class CommentAdapter extends BaseAdapter {
    private Activity activity;
    private Context context;
    private LayoutInflater layoutInflater;
    private int layout;

    public CommentAdapter(Activity activity, Context context,
                      int layout) {
        this.activity = activity;
        this.context = context;
        this.layout = layout;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Objects getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(layout, null);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }

    static class ViewHolder {

    }
}
