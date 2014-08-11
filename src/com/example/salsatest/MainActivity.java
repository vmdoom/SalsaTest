package com.example.salsatest;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.example.salsatest.VenuesListFragment.ListCallbacks;
import com.example.salsatest.VenuesMapFragment.MapCallbacks;
import com.example.salsatest.VenuesTaskFragment.TaskCallbacks;
import com.example.salsatest.adapter.MainPagerAdapter;
import com.example.salsatest.dto.Venue;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;

public class MainActivity extends FragmentActivity implements GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener, ActionBar.TabListener, TaskCallbacks, 
	MapCallbacks, ListCallbacks {
	
	//Constants
	private static final int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	private static final int[] tabs = { R.string.tab_list, R.string.tab_map };
	private static final String TAG_TASK_FRAGMENT = "state";
	private static final String STATE_VENUES = "VENUES";
	private static final String STATE_LAT = "LAT";
	private static final String STATE_LNG = "LON";
	
	private VenuesTaskFragment mTaskFragment;
	public ProgressDialog mProgress;
	private LocationClient mLocationClient;
	private ViewPager viewPager;
	private MainPagerAdapter mAdapter;
	private ActionBar actionBar;
	private ArrayList<Venue> mVenues;
	private double mLng, mLat;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//State fragment for task
		FragmentManager fm = getSupportFragmentManager();
	    mTaskFragment = (VenuesTaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);
	    if (mTaskFragment == null) {
	    	mTaskFragment = new VenuesTaskFragment();
	    	fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
	    } else if (mTaskFragment.isTaskRunning()) {
	    	onPreExecute();
	    } else if (savedInstanceState != null) {
	    	mVenues = savedInstanceState.getParcelableArrayList(STATE_VENUES);
	    	mLat = savedInstanceState.getDouble(STATE_LAT);
	    	mLng = savedInstanceState.getDouble(STATE_LNG);
	    }
		
		viewPager = (ViewPager) findViewById(R.id.pager);
		actionBar = getActionBar();
		mAdapter = new MainPagerAdapter(getSupportFragmentManager());

		viewPager.setAdapter(mAdapter);
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		for (int tab_name : tabs) {
			actionBar.addTab(actionBar.newTab().setText(tab_name)
					.setTabListener(this));
		}

		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// on changing the page
				// make respected tab selected
				actionBar.setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
		
		EditText edtSearch = (EditText) findViewById(R.id.edtSearch);
		edtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		    @Override
		    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
		        	mTaskFragment.executeTask(v.getText().toString());
		            return true;
		        }
		        return false;
		    }
		});
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putParcelableArrayList(STATE_VENUES, mVenues);
		outState.putDouble(STATE_LAT, mLat);
		outState.putDouble(STATE_LNG, mLng);
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mLocationClient = new LocationClient(this, this, this);
        mLocationClient.connect();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		mLocationClient.disconnect();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
        case CONNECTION_FAILURE_RESOLUTION_REQUEST:
            if (resultCode == Activity.RESULT_OK) {
                mLocationClient = new LocationClient(this, this, this);
                mLocationClient.connect();
            }
            break;
		}
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (result.hasResolution()) {
            try {
            	result.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
            }
        } else {
        	//TODO: show error
        }
	}
	
	@Override
	public void onConnected(Bundle connectionHint) {
		getLocation();
	}
	
	@Override
	public void onDisconnected() {
		
	}
	
	@Override
	public void onTabReselected(Tab arg0, FragmentTransaction arg1) {
		
	}

	@Override
	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		viewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		
	}
	
	public void getLocation() {
        if (servicesConnected(this, CONNECTION_FAILURE_RESOLUTION_REQUEST)) {
            Location currentLocation = mLocationClient.getLastLocation();
            if (currentLocation != null) {
                //Location obtained 
            	mLat = currentLocation.getLatitude();
            	mLng = currentLocation.getLongitude();
            } else {
                LocationManager lm = (LocationManager) getSystemService(android.content.Context.LOCATION_SERVICE);
                if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER) || !lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                    //TODO: show error
                } else {
                    //TODO: show error
                }
            }
        }
    }
	
	public static boolean servicesConnected(FragmentActivity context, int resolution_request) {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            return true;
        } else {
            // Get the error dialog from Google Play services
            Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
                    resultCode, context, resolution_request);

            // If Google Play services can provide an error dialog
            if (errorDialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(errorDialog);
                errorFragment.show(context.getSupportFragmentManager(), "Location Updates");
            }
            return false;
        }
    }
    
    public static void showLocationErrorDialog(FragmentActivity context, int errorCode, int resolution_request) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
            errorCode, context, resolution_request);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            errorFragment.show(context.getSupportFragmentManager(), "LocationError");
        }
    }
    
 // Define a DialogFragment that displays the error dialog
    public static class ErrorDialogFragment extends DialogFragment {
        // Global field to contain the error dialog
        private Dialog mDialog;

        // Default constructor. Sets the dialog field to null
        public ErrorDialogFragment() {
            super();
            mDialog = null;
        }

        // Set the dialog to display
        public void setDialog(Dialog dialog) {
            mDialog = dialog;
        }

        // Return a Dialog to the DialogFragment.
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return mDialog;
        }
    }

	@Override
	public void onPreExecute() {
		mProgress = new ProgressDialog(this);
		mProgress.setCancelable(true);
		mProgress.setCanceledOnTouchOutside(false);
		mProgress.setMessage("Loading...");
		mProgress.show();
	}

	@Override
	public void onCancelled() {
		if (mProgress != null) {
			mProgress.dismiss();
		}
	}

	@Override
	public void onPostExecute(String result) {
		if (mProgress != null) {
			mProgress.dismiss();
		}
		try {
			mVenues = new ArrayList<Venue>();
			if (result != null) {
				JSONObject json = new JSONObject(result);
				JSONArray jArray = json.getJSONObject("response").getJSONArray(
						"venues");
				
				for (int i = 0; i < jArray.length(); i++) {
					mVenues.add(Venue.fromJson(jArray.getJSONObject(i)));
				}
			}

			Location currentLocation = new Location("current");
			currentLocation.setLongitude(mLng);
			currentLocation.setLatitude(mLat);
			VenuesListFragment listFr = (VenuesListFragment) mAdapter
					.getRegisteredFragment(0);
			listFr.setVenues(mVenues, currentLocation);

			VenuesMapFragment mapFr = (VenuesMapFragment) mAdapter
					.getRegisteredFragment(1);
			mapFr.setVenues(mVenues);
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMapStart(Fragment fr) {
		VenuesMapFragment mapFr = (VenuesMapFragment) fr;
		if (mapFr != null) {
			//mapFr.setVenues(mVenues);
		}
	}
    
    @Override
    public void onListStart(Fragment fr) {
    	VenuesListFragment listFr = (VenuesListFragment) fr;
		if (listFr != null) {
			Location currentLocation = new Location("current");
			currentLocation.setLongitude(mLng);
			currentLocation.setLatitude(mLat);
			listFr.setVenues(mVenues, currentLocation);
		}
    }
}
