package com.example.salsatest.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.salsatest.R;
import com.example.salsatest.dto.Venue;

/**
 * Adapter for the listView in VenuesListFragment
 * @author victor
 *
 */
public class VenuesListAdapter extends BaseAdapter {
	
	private class ViewHolder {
		public TextView name;
		public TextView distance;
	}

	private ArrayList<Venue> mVenues;
	private Context mContext;
	
	public VenuesListAdapter(Context context, ArrayList<Venue> venues) {
		mContext = context;
		mVenues = venues;
	}
	
	@Override
	public int getCount() {
		return mVenues.size();
	}

	@Override
	public Object getItem(int index) {
		return mVenues.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                        .inflate(R.layout.list_item_venue, parent, false);
            
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.txtName);
            viewHolder.distance = (TextView) convertView.findViewById(R.id.txtDistance);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Venue venue = mVenues.get(position);
        viewHolder.name.setText(venue.name);
        viewHolder.distance.setText(String.format("%.1f Km", venue.distance));
        return convertView;
	}

}
