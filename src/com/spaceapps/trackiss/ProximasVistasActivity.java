package com.spaceapps.trackiss;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class ProximasVistasActivity extends SherlockActivity implements
		LocationListener {

	private LocationManager locationManager;
	private double[] gps = new double[3];
	private ListView listView;
	private ArrayList<IssPassTimes> issPassTimes;
	private int position = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		listView = (ListView) findViewById(R.id.vistas);
		registerForContextMenu(listView);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		android.view.MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.context_menu_list, menu);
		menu.setHeaderTitle("Opciones de vista");
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.main, menu);

		return true;
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {

		switch (item.getItemId()) {
		case R.id.share_date:
			share_date(issPassTimes, position);
			break;
		case R.id.save_date:
			save_date(issPassTimes, position);
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@SuppressLint("InlinedApi")
	private void save_date(ArrayList<IssPassTimes> issPassTimes, int position) {
		Intent save_date = new Intent(Intent.ACTION_INSERT);
		save_date.setData(Events.CONTENT_URI);
		save_date.putExtra(Events.TITLE, "TRACK-ISS");
		save_date.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
				issPassTimes.get(position).getCalendarWithDate().getTime()
						.getTime());
		save_date.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, issPassTimes
				.get(position).getCalendarWithDate().getTime().getTime()
				+ (issPassTimes.get(position).getDuration()* 60)*1000);
		save_date.putExtra(Events.DESCRIPTION, "Rastrear a la Estación Espacial Internacional");
		startActivity(save_date);
	}

	private void share_date(ArrayList<IssPassTimes> issPassTimes, int position) {

		Intent sendIntent = new Intent();
		sendIntent.setAction(Intent.ACTION_SEND);
		sendIntent.setType("text/*");
		sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		if (Activity_Inicio.language.equals("español")) {

			sendIntent.putExtra(Intent.EXTRA_TEXT,
					"Próxima vista de la Estación Espacial Internacional: "
							+ "\n"
							+ issPassTimes.get(position).getStringRiseTimes()
							+ "\n" + "#app TRACK-ISS");
		} else {
			sendIntent.putExtra(Intent.EXTRA_TEXT,
					"I'm catching the International Space Station with TRACK-ISS, join me at: "
							+ Uri.parse("http://bit.ly/XXXfkV "));
		}
		startActivity(sendIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.onBackPressed();
			return true;

		case R.id.buscar_vistas:
			if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				AsyncJson asyncJson = new AsyncJson();
				asyncJson.execute("http://api.open-notify.org/iss/?lat="
						+ gps[0] + "&lon=" + gps[1] + "&alt=" + (int) gps[2]
						+ "&n=10");
			} else {
				Toast.makeText(ProximasVistasActivity.this, "Activa el GPS",
						Toast.LENGTH_SHORT).show();
			}
			return true;
		default:
			break;
		}
		return false;
	}

	private class AsyncJson extends
			AsyncTask<String, Void, ArrayList<IssPassTimes>> {

		ProgressDialog dialog;

		@Override
		protected ArrayList<IssPassTimes> doInBackground(String... arg0) {
			IssPassJson issPassJson = new IssPassJson(arg0[0]);
			return issPassJson.getRequestJson();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(ProximasVistasActivity.this);
			dialog.setTitle("Próximas Vistas");
			dialog.setMessage("Calculando las próximas vistas ....");

			dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancelar",
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			dialog.show();
		}

		@Override
		protected void onPostExecute(ArrayList<IssPassTimes> result) {
			dialog.cancel();
			issPassTimes = result;
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {

					listView.showContextMenuForChild(arg1);
					position = arg2;
				}
			});
			Log.d("TAG_DATE", ""
					+ result.get(0).getCalendarWithDate().getTime().getTime());
			AdapterVistas adapterVistas = new AdapterVistas(
					ProximasVistasActivity.this, result);
			listView.setAdapter(adapterVistas);
			super.onPostExecute(result);
		}
	}

	@Override
	protected void onResume() {

		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				2000, 4, this);
		super.onResume();
	}

	@Override
	protected void onStop() {
		super.onStop();
		locationManager.removeUpdates(this);

	}

	@Override
	public void onLocationChanged(Location arg0) {

		gps[0] = arg0.getLatitude();
		gps[1] = arg0.getLongitude();
		gps[2] = arg0.getAltitude();

	}

	@Override
	public void onProviderDisabled(String arg0) {

	}

	@Override
	public void onProviderEnabled(String arg0) {

	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {

	}
}
