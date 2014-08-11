package com.example.salsatest.dto;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Venue implements Parcelable {
	public String id;
	public String name;
	public double lat;
	public double lng;
	public double distance;
	
	public Venue() { }
	public Venue(Parcel in) { 
		
	}
	
	public static Venue fromJson(JSONObject json) {
		Venue v = new Venue();
		try {
			v.id = json.getString("id");
			v.name = json.getString("name");
			JSONObject location = json.getJSONObject("location");
			v.lat = location.getDouble("lat");
			v.lng = location.getDouble("lng");
		} catch (JSONException e) {
			v = null;
		}
		return v;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeDouble(lat);
		dest.writeDouble(lng);
		dest.writeDouble(distance);
	}
	
	public void readFromParcel(Parcel in) {
		id = in.readString();
		name = in.readString();
		lat = in.readDouble();
		lng = in.readDouble();
		distance = in.readDouble();
	}
	
	public static final Parcelable.Creator<Venue> CREATOR = new Parcelable.Creator<Venue>() {
        public Venue createFromParcel(Parcel in) {
            return new Venue(in);
        }

        public Venue[] newArray(int size) {
            return new Venue[size];
        }
    };
}
