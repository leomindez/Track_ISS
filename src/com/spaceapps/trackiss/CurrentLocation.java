package com.spaceapps.trackiss;

import org.json.JSONException;
import org.json.JSONObject;

public class CurrentLocation {

	private String myUrl;

	public CurrentLocation(String url) {

		myUrl = url;
	}

	public double[] getCurrentLocation() {
		InformationJson json = new InformationJson(myUrl);
		double[] currentLocation = new double[3];

		try {
			JSONObject jsonObject = new JSONObject(json.getJsonString());
			currentLocation[0] = jsonObject.getLong("timestamp");

			JSONObject iss_position = jsonObject.getJSONObject("iss_position");

			currentLocation[1] = iss_position.getDouble("latitude");
			currentLocation[2] = iss_position.getDouble("longitude");

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return currentLocation;
	}

}
