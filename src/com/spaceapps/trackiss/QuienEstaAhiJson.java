package com.spaceapps.trackiss;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class QuienEstaAhiJson {

	private String myUrl;

	public QuienEstaAhiJson(String url) {
		this.myUrl = url;
	}

	public ArrayList<String> getNombreAstronauta() {
		InformationJson informationJson = new InformationJson(myUrl);
		ArrayList<String> names = new ArrayList<String>();
		try {
			JSONObject jsonObject = new JSONObject(
					informationJson.getJsonString());
			JSONArray array = jsonObject.getJSONArray("people");
			for (int i = 0; i < array.length(); i++) {
				JSONObject peopleJson = array.getJSONObject(i);
				String name;
				name = peopleJson.getString("name");
				names.add(name);

			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return names;
	}
}
