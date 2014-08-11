package com.example.salsatest;

import java.util.ArrayList;

import com.example.salsatest.adapter.VenuesListAdapter;
import com.example.salsatest.dto.Venue;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class VenuesListFragment extends Fragment {
	private ListView lstItems;
	ListCallbacks mCallback;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_venues_list, container, false);
		lstItems = (ListView) rootView.findViewById(R.id.lstItems);
		return rootView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		mCallback.onListStart(this);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (ListCallbacks) activity;
		} catch (ClassCastException ex) {
		}
	}
	
	public void setVenues(ArrayList<Venue> venues, Location currentLocation) {
		//Calculates the distance here to avoid doing it many times on getView
		if (venues != null) {
			for (Venue v : venues) {
				Location venueLocation = new Location("dest");
				venueLocation.setLongitude(v.lng);
				venueLocation.setLatitude(v.lat);
				v.distance = currentLocation.distanceTo(venueLocation) / 1000;
			}
			VenuesListAdapter adapter = new VenuesListAdapter(getActivity(), venues);
			lstItems.setAdapter(adapter);
		}
	}
	
	public interface ListCallbacks {
		public void onListStart(Fragment fr);
	}
}
