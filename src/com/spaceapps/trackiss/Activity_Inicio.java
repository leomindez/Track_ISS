package com.spaceapps.trackiss;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class Activity_Inicio extends SherlockActivity implements
		OnClickListener {

	private Button prox_vistas, donde_esta, quien_esta_ahi;
	public static String language = Locale.getDefault().getDisplayLanguage();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity__inicio);

		prox_vistas = (Button) findViewById(R.id.proximas_vistas);
		donde_esta = (Button) findViewById(R.id.donde_esta);
		quien_esta_ahi = (Button) findViewById(R.id.quien_esta_ahi);
		prox_vistas.setOnClickListener(this);
		donde_esta.setOnClickListener(this);
		quien_esta_ahi.setOnClickListener(this);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.activity__inicio, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.share_content:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			if (language.equals("español")) {

				sendIntent
						.putExtra(Intent.EXTRA_TEXT,
								"Estoy siguiendo la Estación Espacial Internacional con TRACK-ISS "
										+ Uri.parse("http://bit.ly/XXXfkV ")
												.toString());
			} else {
				sendIntent
						.putExtra(Intent.EXTRA_TEXT,
								"I'm catching the International Space Station with TRACK-ISS, join me at: "
										+ Uri.parse("http://bit.ly/XXXfkV ")
												.toString());
			}
			sendIntent.setType("text/*");
			startActivity(sendIntent);
			return true;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.proximas_vistas:
			Intent intent = new Intent(Activity_Inicio.this,
					ProximasVistasActivity.class);
			startActivity(intent);
			break;

		case R.id.donde_esta:
			Intent intent1 = new Intent(Activity_Inicio.this,
					DondeEstaActivity.class);
			startActivity(intent1);
			break;
		case R.id.quien_esta_ahi:

			Intent intent2 = new Intent(Activity_Inicio.this,
					QuienEstaAhiActivity.class);
			startActivity(intent2);
			break;
		default:
			break;
		}
	}

	public static boolean isConnected(Context context) {

		ConnectivityManager conMan = ((ConnectivityManager) context
				.getSystemService(CONNECTIVITY_SERVICE));
		boolean isWifiEnabled = conMan.getNetworkInfo(
				ConnectivityManager.TYPE_WIFI).isAvailable();
		boolean is3GEnabled = (conMan.getNetworkInfo(
				ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED);

		if (isWifiEnabled && !is3GEnabled) {
			return true;
		} else if (is3GEnabled && !isWifiEnabled) {
			return true;
		} else {
			return false;
		}
	}
}
