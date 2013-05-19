package com.spaceapps.trackiss;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.util.Log;

public class InformationJson {

	private String myUrl;

	public InformationJson(String url) {
		this.myUrl = url;
	}

	public String getJsonString() {

		StringBuilder builder = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet jsonGet = new HttpGet(myUrl);
		try {
			HttpResponse httpResponse = client.execute(jsonGet);
			StatusLine statusLine = httpResponse.getStatusLine();
			// if (statusLine.getStatusCode() == 200) {
			Log.d("TAG_STATUS_CODE", "" + statusLine.getStatusCode());
			HttpEntity entity = httpResponse.getEntity();
			InputStream stream = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					stream));
			String textJson;
			while ((textJson = reader.readLine()) != null) {
				builder.append(textJson + "\n");
			}
			stream.close();

			// } else {
			// HttpEntity entity = httpResponse.getEntity();
			// InputStream stream = entity.getContent();
			// BufferedReader reader = new BufferedReader(
			// new InputStreamReader(stream));
			// String textJson;
			// while ((textJson = reader.readLine()) != null) {
			// builder.append(textJson + "\n");
			// }
			// stream.close();
			// }

		} catch (Exception e) {
			e.printStackTrace();
		}

		return builder.toString();
	}

}
