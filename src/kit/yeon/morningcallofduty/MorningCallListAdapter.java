package kit.yeon.morningcallofduty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.TimeZone;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MorningCallListAdapter extends BaseAdapter {
	
	private DBAdapter mDb = null;
	
	private ArrayList<MorningCall> mList = null;
	private Context context;
	public Hashtable<Integer, View> hashConvertView = new Hashtable<Integer, View>();
	LayoutInflater inflater;

	boolean[] ischeck = new boolean[50];// = new boolean[];

	public MorningCallListAdapter(Context context, ArrayList<MorningCall> MorningCallList) {
		mList = MorningCallList;

		this.context = context;
		inflater = (LayoutInflater) this.context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		for(int i = 0; i < ischeck.length; i++){
			ischeck[i] = true;
		}
	}

	public int getCount() {
		return mList.size();
	}

	public long getItemId(int position) {
		return position;
	}

	public Object getItem(int position) {
		return mList.get(position).reqCode;
	}

	public boolean removeData(int position) {
		mList.remove(position);
		notifyDataSetChanged();
		return false;
	}

	class ViewHolder {
		public CheckBox check;
		public TextView mMusic;
		public TextView mClock;
		public ImageView[] mDay;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row, parent, false);

			holder = new ViewHolder();
			
			mDb = new DBAdapter(context);
			mList = mDb.getMorningCallList();
			MorningCall dbmc = mList.get(position);

			ischeck = dbmc.getDay();
			
			holder.mDay = new ImageView[7];

			holder.mMusic = (TextView) convertView.findViewById(R.id.musicid);
			holder.mClock = (TextView) convertView.findViewById(R.id.clockid);

			holder.mDay[0] = (ImageView) convertView.findViewById(R.id.sunid);
			holder.mDay[1] = (ImageView) convertView.findViewById(R.id.monid);
			holder.mDay[2] = (ImageView) convertView.findViewById(R.id.tueid);
			holder.mDay[3] = (ImageView) convertView.findViewById(R.id.wedid);
			holder.mDay[4] = (ImageView) convertView.findViewById(R.id.thuid);
			holder.mDay[5] = (ImageView) convertView.findViewById(R.id.friid);
			holder.mDay[6] = (ImageView) convertView.findViewById(R.id.satid);

			holder.check = (CheckBox) convertView.findViewById(R.id.check);
			holder.check.setClickable(false);
			holder.check.setFocusable(false);
			holder.check.setId(position);
			holder.check.setOnClickListener(listener);
			
			if (ischeck[position]) {
				holder.check.setChecked(true);
			} else if (ischeck[position] == false){
				Log.v(ischeck[position] + "@", ischeck[position]+"@");
				holder.check.setChecked(false);
			}

			convertView.setTag(holder);
			hashConvertView.put(position, convertView);
		} else {
			// convertView = (View) hashConvertView.get(position)
			holder = (ViewHolder) convertView.getTag();
		}

		MorningCall mc = mList.get(position);

		boolean[] mDay = mc.getDay();

		holder.mMusic.setText(mc.getMusic());
		holder.mClock.setText(mc.getClock() + " " + mc.getAmpm());

		if (mDay[0] == true) {
			holder.mDay[0].setImageResource(R.drawable.sun2);
		}
		if (mDay[1] == true) {
			holder.mDay[1].setImageResource(R.drawable.mon2);
		}
		if (mDay[2] == true) {
			holder.mDay[2].setImageResource(R.drawable.tue2);
		}
		if (mDay[3] == true) {
			holder.mDay[3].setImageResource(R.drawable.wed2);
		}
		if (mDay[4] == true) {
			holder.mDay[4].setImageResource(R.drawable.thu2);
		}
		if (mDay[5] == true) {
			holder.mDay[5].setImageResource(R.drawable.fri2);
		}
		if (mDay[6] == true) {
			holder.mDay[6].setImageResource(R.drawable.sat2);
		}

		return convertView;
	}

	private final OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			if (v instanceof CheckBox) {
				CheckBox check = (CheckBox) v;
				
				Log.v(v.getId()+":", "!!");

				Intent intent = new Intent(context, AlarmReceiver.class);
				MorningCall mc = mList.get(v.getId());

				intent.putExtra("day", mc.getDay());
				intent.putExtra("uri", mc.getUri());

				AlarmManager am = (AlarmManager) context
						.getSystemService(Context.ALARM_SERVICE);

				PendingIntent pi = PendingIntent.getBroadcast(context,
						mc.reqCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

				if (check.isChecked() == false) {

					ischeck[v.getId()] = false;
					mc.setCheck(false);
					
					mDb.updateMorningCall(mc);

					am.cancel(pi);

				} else if (check.isChecked() == true) {

					ischeck[v.getId()] = true;
					mc.setCheck(true);
					
					mDb.updateMorningCall(mc);

					GregorianCalendar currentCalendar = new GregorianCalendar(
							TimeZone.getTimeZone("GMT+09:00"));

					am.setTimeZone("GMT+09:00");

					int currentYY = currentCalendar.get(Calendar.YEAR);
					int currentMM = currentCalendar.get(Calendar.MONTH);
					int currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);

					int hClock = mc.gethClock();
					int mClock = mc.getmClock();

					String ampm = mc.getAmpm();

					if (ampm == "PM")
						hClock += 12;

					GregorianCalendar gregorianCalendar = new GregorianCalendar(
							TimeZone.getTimeZone("GMT+09:00"));
					gregorianCalendar.set(currentYY, currentMM, currentDD,
							hClock, mClock, 0);

					// 날짜 시간 오차 재조정
					if (gregorianCalendar.getTimeInMillis() < currentCalendar
							.getTimeInMillis()) {
						gregorianCalendar.set(currentYY, currentMM,
								currentDD + 1, mc.gethClock(), mc.getmClock(),
								0);
						Log.v("TAG", gregorianCalendar.getTimeInMillis() + ":");
					}

					if (ampm == "PM")
						hClock -= 12;

					Toast.makeText(context,
							hClock + ":" + mClock + ampm + "알람 설정",
							Toast.LENGTH_SHORT).show();

					am.setRepeating(AlarmManager.RTC_WAKEUP,
							gregorianCalendar.getTimeInMillis(),
							AlarmManager.INTERVAL_DAY, pi);
				}
				notifyDataSetChanged();
			}
		}
	};
/*
	public Long[] getCheckItemIds() {
		if (hashConvertView == null || hashConvertView.size() == 0)
			return null;

		Integer key;
		View view;
		List<Long> lstIds = new ArrayList<Long>();
		Enumeration<Integer> e = hashConvertView.keys();
		long index = 0;
		while (e.hasMoreElements()) {
			key = (Integer) e.nextElement();
			view = (View) hashConvertView.get(key);
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder.check.isChecked()) {
				lstIds.add(index);
			}
			index++;
		}

		Long[] arrLong = (Long[]) lstIds.toArray(new Long[0]);
		return arrLong;
	}

	public void clearChoices() {
		if (hashConvertView == null || hashConvertView.size() == 0)
			return;

		Integer key;
		View view;
		List<Long> lstIds = new ArrayList<Long>();
		Enumeration<Integer> e = hashConvertView.keys();
		long index = 0;
		while (e.hasMoreElements()) {
			key = (Integer) e.nextElement();
			view = (View) hashConvertView.get(key);
			ViewHolder holder = (ViewHolder) view.getTag();
			if (holder.check.isChecked()) {
				holder.check.setChecked(false);
			}

			index++;
		}
	}*/

}