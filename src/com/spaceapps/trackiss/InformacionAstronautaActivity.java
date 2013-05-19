package com.spaceapps.trackiss;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class InformacionAstronautaActivity extends SherlockActivity {

	// private String[] wikiNombres = { "Roman_Romanenko", "Thomas_Marshburn",
	// "Chris_Hadfield", "Chris_Cassidy", "Pavel_Vinogradov",
	// "Alexander_Misurkin" };
	private WebView webWikiNombre;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_informacion_astronauta);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		Bundle wiki = null;
		webWikiNombre = (WebView) findViewById(R.id.informacion_astronauta);

		if (getIntent() != null) {
			wiki = getIntent().getExtras();
		}
		setTitle(wiki.getString("name").replace("_", " "));// wikiNombres[wiki.getInt("position")].replace("_",
															// " "))//;

		webWikiNombre.setWebViewClient(new CustomWebView());
		webWikiNombre.loadUrl("http://en.wikipedia.org/wiki/"
				+ wiki.getString("name"));// wikiNombres[wiki.getInt("position")]);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.onBackPressed();
			return true;

		default:
			break;
		}
		return false;
	}

	private class CustomWebView extends WebViewClient {

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			view.loadUrl(url);

			return true;

		}
	}

}
