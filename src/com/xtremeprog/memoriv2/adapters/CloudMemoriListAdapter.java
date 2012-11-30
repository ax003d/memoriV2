package com.xtremeprog.memoriv2.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.xtremeprog.memoriv2.R;
import com.xtremeprog.memoriv2.models.CloudMemori;
import com.xtremeprog.memoriv2.utils.Preferences;
import com.xtremeprog.memoriv2.zxing.Contents;
import com.xtremeprog.memoriv2.zxing.Intents;

public class CloudMemoriListAdapter extends BaseAdapter {

	public class ViewHolder {

		public CloudMemori memori;
		public TextView txt_start_timestamp;
		public TextView txt_owners;
		public Button btn_qrcode;

	}

	private ArrayList<CloudMemori> memoris;
	private Context context;

	public CloudMemoriListAdapter(Context context) {
		this.memoris = new ArrayList<CloudMemori>();
		this.context = context;
	}

	public void add_memori(CloudMemori memori) {
		this.memoris.add(memori);
	}

	@Override
	public int getCount() {
		return memoris.size();
	}

	@Override
	public Object getItem(int position) {
		return memoris.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			convertView = LayoutInflater.from(parent.getContext()).inflate(
					R.layout.cloud_memori_item, null);
			holder = buildTag(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		CloudMemori memori = (CloudMemori) getItem(position);
		holder.memori = memori;
		holder.txt_start_timestamp.setText("Start Time: "
				+ memori.get_start_timestamp() + "");
		holder.txt_owners.setText("Owners: " + memori.get_owners().size());

		return convertView;
	}

	private ViewHolder buildTag(View convertView) {
		final ViewHolder holder = new ViewHolder();
		holder.txt_start_timestamp = (TextView) convertView
				.findViewById(R.id.txt_start_timestamp);
		holder.txt_owners = (TextView) convertView
				.findViewById(R.id.txt_owners);
		holder.btn_qrcode = (Button) convertView.findViewById(R.id.btn_qrcode);

		holder.btn_qrcode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intents.Encode.ACTION);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra(Intents.Encode.TYPE, Contents.Type.TEXT);
				intent.putExtra(Intents.Encode.DATA,
						Preferences.getServer(context) + "/v1/memori/join/?id="
								+ holder.memori.get_guid() + "&invite_code="
								+ holder.memori.get_invite_code());
				intent.putExtra(Intents.Encode.FORMAT,
						BarcodeFormat.QR_CODE.toString());
				context.startActivity(intent);
			}

		});

		return holder;
	}

}