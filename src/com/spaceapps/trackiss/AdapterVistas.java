package com.spaceapps.trackiss;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterVistas extends BaseAdapter {

	private ArrayList<IssPassTimes> arrayList;
	private Context myContext;

	public AdapterVistas(Context context, ArrayList<IssPassTimes> issPassTimes) {
		arrayList = issPassTimes;
		myContext = context;
	}

	@Override
	public int getCount() {
		return arrayList.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arrayList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {

		View contenedor = arg1;
		ViewHolder holder = new ViewHolder();

		if (contenedor == null) {
			LayoutInflater inflater = (LayoutInflater) myContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			contenedor = inflater.inflate(R.layout.adapter_vistas, arg2, false);
			holder.duracion = (TextView) contenedor.findViewById(R.id.duracion);
			holder.fecha = (TextView) contenedor.findViewById(R.id.fecha);
			contenedor.setTag(holder);
		} else {
			holder = (ViewHolder) contenedor.getTag();
		}

		IssPassTimes issPassTimes = arrayList.get(arg0);
		holder.duracion.setText("" + issPassTimes.getDuration() + " min");
		holder.fecha.setText(issPassTimes.getStringRiseTimes());
		return contenedor;
	}

	private class ViewHolder {
		TextView duracion;
		TextView fecha;
	}

}
