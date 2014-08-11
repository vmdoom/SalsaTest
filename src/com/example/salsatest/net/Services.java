package com.example.salsatest.net;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

public class Services {
	public static int TIMEOUT_CONNECTION = 10000;
    public static int TIMEOUT_SOCKET = 60000;
    
    private static String CLIENT_ID = "0WQH1SGJGVKKZ4YHIUQXKPER4W15CANJ5HGFRFBLLG040K2O";
    private static String CLIENT_SECRET = "USE4V20TEKHVTJFBRDZDRLPIM0XTTCFSUWCFFBPUIDPCAKST";
    private static String URL_LL = "https://api.foursquare.com/v2/venues/search?client_id=%1$s&client_secret=%2$s&ll=%3$s,%4$s&v=20140808&m=foursquare";
    private static String URL_NEAR = "https://api.foursquare.com/v2/venues/search?client_id=%1$s&client_secret=%2$s&near=%3$s&v=20140808&m=foursquare";
    
    public static String getPlaces(double lat, double lon) {
		try {
			URL url = new URL(String.format(URL_LL, CLIENT_ID, CLIENT_SECRET, Double.toString(lon), Double.toString(lat)));
			return callService(url);
		} catch (MalformedURLException e) {
			return null;
		}
    }
    
    public static String getPlaces(String city) {
    	try {
			URL url = new URL(String.format(URL_NEAR, CLIENT_ID, CLIENT_SECRET, URLEncoder.encode(city, "UTF-8")));
			return callService(url);
		} catch (Exception e) {
			return null;
		}	
    }
    
    private static String callService(URL url) {
    	try {
	        HttpsURLConnection conn = (HttpsURLConnection)url.openConnection();
	        conn.setConnectTimeout(TIMEOUT_CONNECTION);
	        conn.setReadTimeout(TIMEOUT_SOCKET);
	        conn.setDoInput(true);
	        conn.setRequestMethod("GET");
	        conn.connect();
	        
	        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
	        StringBuilder sb = new StringBuilder();
	        String line;
	        while ((line = rd.readLine()) != null) {
	        	sb.append(line);
	        }
	        rd.close();
	        conn.disconnect();
	        return sb.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	
    }
}
