package com.spaceapps.trackiss;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class QuienEstaAhiActivity extends SherlockListActivity {

	private AsyncQuienEstaAhi quienEstaAhi;
	private ArrayList<String> namesArray;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_quien_esta_ahi);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		if (Activity_Inicio.isConnected(QuienEstaAhiActivity.this)) {
			quienEstaAhi = new AsyncQuienEstaAhi();
			quienEstaAhi.execute("http://api.open-notify.org/astros/v1/");
		} else {
			Toast.makeText(QuienEstaAhiActivity.this, "Connection fail",
					Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				Intent wikiNombre = new Intent(QuienEstaAhiActivity.this,
						InformacionAstronautaActivity.class);
				wikiNombre.putExtra("position", arg2);
				wikiNombre.putExtra("name", namesArray.get(arg2));

				startActivity(wikiNombre);
				overridePendingTransition(android.R.anim.fade_in,
						android.R.anim.fade_out);

			}
		});
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (quienEstaAhi != null) {
			quienEstaAhi.cancel(true);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.quien_esta_ahi, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			this.onBackPressed();
			return true;

		case R.id.refresh_quie_esta:
			AsyncQuienEstaAhi quienEstaAhi = new AsyncQuienEstaAhi();
			quienEstaAhi.execute("http://api.open-notify.org/astros/v1/");
			return true;
		default:
			break;
		}

		return super.onOptionsItemSelected(item);
	}

	private class AsyncQuienEstaAhi extends
			AsyncTask<String, Void, ArrayList<String>> {
		ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(QuienEstaAhiActivity.this);
			dialog.setTitle("¿Quién esta ahí?");
			dialog.setMessage("¡Buscando astronautas!");

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
		protected ArrayList<String> doInBackground(String... params) {

			if (isCancelled()) {
				finish();
			} else {
				QuienEstaAhiJson ahiJson = new QuienEstaAhiJson(params[0]);

				return ahiJson.getNombreAstronauta();
			}
			return null;
		}

		@Override
		protected void onPostExecute(ArrayList<String> result) {
			super.onPostExecute(result);

			if (result != null) {
				namesArray = result;
				dialog.dismiss();
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(
						QuienEstaAhiActivity.this,
						android.R.layout.simple_list_item_1, result);
				getListView().setAdapter(adapter);

			}
		}
	}

}
