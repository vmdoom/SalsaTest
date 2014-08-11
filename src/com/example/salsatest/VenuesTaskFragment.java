package com.example.salsatest;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.AsyncTask.Status;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.example.salsatest.net.Services;

public class VenuesTaskFragment extends Fragment {
	public interface TaskCallbacks {
		void onPreExecute();
		void onCancelled();
		void onPostExecute(String result);
	}

	private TaskCallbacks mCallbacks;
	private GetVenuesTask mTask;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (TaskCallbacks) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Retain this fragment across configuration changes.
		setRetainInstance(true);
	}

	/**
	 * Set the callback to null so we don't accidentally leak the Activity
	 * instance.
	 */
	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}

	private class GetVenuesTask extends AsyncTask<String, Void, String> {

		@Override
		protected void onPreExecute() {

			super.onPreExecute();
			mCallbacks.onPreExecute();
		}

		@Override
		protected String doInBackground(String... params) {
			return Services.getPlaces(params[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			mCallbacks.onPostExecute(result);
		}
		
		@Override
		protected void onCancelled() {
			super.onCancelled();
			mCallbacks.onCancelled();
		}
	}

	public void executeTask(String params) {
		mTask = new GetVenuesTask();
		mTask.execute(params);
	}

	public boolean isTaskRunning() {
		return mTask != null && mTask.getStatus() == Status.RUNNING;
	}
}
