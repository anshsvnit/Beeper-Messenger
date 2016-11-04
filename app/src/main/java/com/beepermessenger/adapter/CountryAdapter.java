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
import android.widget.TextView;

import com.beepermessenger.R;
import com.beepermessenger.model.CountryDTO;

import java.util.ArrayList;

/**
 * Created by Shree on 2/21/2016.
 */
public class CountryAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<CountryDTO> items;
    private LayoutInflater layoutInflater;


    public CountryAdapter(Activity activity, ArrayList<CountryDTO> alCountry) {
        this.activity = activity;
        this.items = alCountry;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public CountryDTO getItem(int position) {
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
        final CountryDTO dto = getItem(position);
        if (convertView == null) {
            layoutInflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.row_country, null);
            holder = new ViewHolder();

            holder.tvName = (TextView) convertView
                    .findViewById(R.id.tv_row_country_name);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setTag(holder);

        holder.tvName.setText(dto.getCountry_name());

        return convertView;

    }

    static class ViewHolder {
        TextView tvName;
    }
}
