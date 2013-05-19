package com.spaceapps.trackiss;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class DondeEstaActivity extends SherlockFragmentActivity {

	private GoogleMap googleMap;
	private AsyncCurrentLocation asyncCurrentLocation;
	private AsyncCurrentLocationTimer asyncCurrentLocationTimer;
	private Timer asyncTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donde_esta);
		asyncCurrentLocation = new AsyncCurrentLocation();
		asyncCurrentLocation.execute("http://api.open-notify.org/iss-now/v1/");

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		googleMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.mapa)).getMap();
		googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = new MenuInflater(DondeEstaActivity.this);
		inflater.inflate(R.menu.donde_esta, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.onBackPressed();
			return true;
		case R.id.camara_nave:
			Intent liveStream = new Intent(DondeEstaActivity.this,
					LiveStreamActivity.class);
			startActivity(liveStream);

			return true;
		case R.id.refresh:
			asyncCurrentLocationTimer.cancel(true);
			asyncCurrentLocation.cancel(true);
			AsyncCurrentLocation currentLocation = new AsyncCurrentLocation();
			currentLocation.execute("http://api.open-notify.org/iss-now/v1/");
			return true;
		default:
			break;
		}
		return false;
	}

	private void marcarLugar(GoogleMap map, double lat, double lon,
			String titulo, String info) {

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(new LatLng(lat, lon));

		markerOptions
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.iss));
		map.addMarker(markerOptions);

		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon),
				5));

	}

	@Override
	protected void onResume() {
		super.onResume();

		asyncTimer = TimerLocation();
	}

	@Override
	protected void onPause() {
		asyncTimer.cancel();
		asyncCurrentLocation.cancel(true);
		asyncCurrentLocationTimer.cancel(true);
		super.onPause();

	}

	@Override
	protected void onStop() {
		super.onStop();
		asyncTimer.cancel();
		asyncCurrentLocation.cancel(true);
		asyncCurrentLocationTimer.cancel(true);

	}

	private class AsyncCurrentLocation extends
			AsyncTask<String, Void, double[]> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			dialog = new ProgressDialog(DondeEstaActivity.this);
			dialog.setTitle("Posición");
			dialog.setMessage("Ubicando ISS....");

			dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							TimerLocation().cancel();
							asyncCurrentLocation.cancel(true);
						}
					});
			dialog.show();

		}

		@Override
		protected double[] doInBackground(String... params) {
			CurrentLocation currentLocation = new CurrentLocation(params[0]);
			return currentLocation.getCurrentLocation();
		}

		@Override
		protected void onPostExecute(double[] result) {
			super.onPostExecute(result);
			dialog.cancel();

			marcarLugar(googleMap, result[1], result[2], "ISS Ubicación",
					dateCurrentLocation(result[0]));

		}
	}

	private class AsyncCurrentLocationTimer extends
			AsyncTask<String, Void, double[]> {

		@Override
		protected double[] doInBackground(String... params) {
			CurrentLocation currentLocation = new CurrentLocation(params[0]);
			return currentLocation.getCurrentLocation();
		}

		@Override
		protected void onPostExecute(double[] result) {
			super.onPostExecute(result);

			currentPlace(googleMap, result[1], result[2], "ISS Ubicación",
					dateCurrentLocation(result[0]));
		}
	}

	@SuppressLint("SimpleDateFormat")
	private String dateCurrentLocation(double timeStamp) {
		Date date = new Date((long) timeStamp * 1000);
		String hourRise;
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"dd-MM-yyyy HH:mm:ss");
		hourRise = dateFormat.format(date.getTime());

		return hourRise;
	}

	private void currentPlace(GoogleMap map, double lat, double lon,
			String titulo, String info) {
		map.clear();

		MarkerOptions markerOptions = new MarkerOptions();
		markerOptions.position(new LatLng(lat, lon));

		markerOptions
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.iss));

		map.addMarker(markerOptions);

		map.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(lat, lon)));

	}

	private Timer TimerLocation() {

		Timer timer = new Timer();
		timer.schedule(new TimerTask() {

			@Override
			public void run() {

				asyncCurrentLocationTimer = new AsyncCurrentLocationTimer();
				asyncCurrentLocationTimer
						.execute("http://api.open-notify.org/iss-now/v1/");
			}
		},TRIM_MEMORY_RUNNING_LOW, 1100);

		return timer;
	}
}