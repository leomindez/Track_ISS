package com.spaceapps.trackiss;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class IssPassJson {

	private String informationJson;

	public IssPassJson(String informationJson) {
		this.informationJson = informationJson;
	}

	public ArrayList<IssPassTimes> getRequestJson() {
		ArrayList<IssPassTimes> arrayList = new ArrayList<IssPassTimes>();
		try {
			InformationJson iJson = new InformationJson(informationJson);
			JSONObject object = new JSONObject(iJson.getJsonString());
			JSONArray responseJson = object.getJSONArray("response");

			for (int i = 0; i < responseJson.length(); i++) {
				IssPassTimes issPassTimes = new IssPassTimes();
				JSONObject jsonObject = responseJson.getJSONObject(i);
				issPassTimes.setDuration(jsonObject.getInt("duration"));
				issPassTimes.setRisetime(jsonObject.getLong("risetime"));
				arrayList.add(issPassTimes);
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}

		return arrayList;
	}

}
