package com.example.salsatest;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.salsatest.dto.Venue;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

public class VenuesMapFragment extends Fragment {
	SupportMapFragment mMapFragment;
	GoogleMap map;
	MapCallbacks mCallback;
	
	public VenuesMapFragment() {}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_venues_map, container, false);
	    
	    
		/*final View mapView = mMapFragment.getView();
        if (mapView.getViewTreeObserver().isAlive()) {
        	mapView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

                @SuppressWarnings("deprecation")
                @SuppressLint("NewApi")
                // We check which build version we are using.
                @Override
                public void onGlobalLayout() {
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        mapView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                    //mCallback.onMapStart();
                }
            });
        }*/
		return rootView;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		FragmentManager fm = getChildFragmentManager();
		mMapFragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
		if (mMapFragment == null) {
			mMapFragment = SupportMapFragment.newInstance();
			fm.beginTransaction().replace(R.id.map, mMapFragment).commit();
		}
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (map == null) {
			map = mMapFragment.getMap();
			mCallback.onMapStart(this);
		}
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mCallback = (MapCallbacks) activity;
		} catch (ClassCastException ex) {
		}
	}
	
	public void setVenues(ArrayList<Venue> venues) {
		if (map != null && venues != null && venues.size() > 0) {
			CameraUpdate cu;
			if (venues.size() > 1) {
				//Tries to show all the markers
				LatLngBounds.Builder builder = new LatLngBounds.Builder();
				for (Venue v : venues) {
					LatLng latLng = new LatLng(v.lat, v.lng);
					map.addMarker(new MarkerOptions().position(latLng).title(v.name));
					builder.include(latLng);
				}
				LatLngBounds bounds = builder.build();
				int padding = 0;
				cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
			} else {
				//Shows only one marker with a predefined zoom
				Venue v = venues.get(0);
				LatLng latLng = new LatLng(v.lat, v.lng);
				map.addMarker(new MarkerOptions().position(latLng).title(v.name));
				cu = CameraUpdateFactory.newLatLngZoom(latLng, 15);
			}
			map.moveCamera(cu);
		}
	}
	
	public interface MapCallbacks {
		public void onMapStart(Fragment fr);
	}
}
