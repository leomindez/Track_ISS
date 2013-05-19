package com.spaceapps.trackiss;

import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.MenuItem;

public class LiveStreamActivity extends SherlockActivity {

	private VideoView liveStream;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_live_stream);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		liveStream = (VideoView) findViewById(R.id.live_stream);
		liveStream.requestFocus();

		liveStream
				.setVideoURI(Uri
						.parse("http://iphone-streaming.ustream.tv/uhls/9408562/streams/live/iphone/playlist.m3u8?appType=11&appVersion=2"));

		liveStream.setMediaController(new MediaController(this));
		liveStream.buildDrawingCache();
		liveStream.start();
	}

	@Override
	protected void onStop() {
		super.onStop();
		liveStream.stopPlayback();
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
}
