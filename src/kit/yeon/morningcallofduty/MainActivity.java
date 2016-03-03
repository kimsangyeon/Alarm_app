package kit.yeon.morningcallofduty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private DBAdapter mDb = null;
	public static Context mContext;

	private AlarmManager alarmManager; // alarmManager
	private GregorianCalendar gregorianCalendar;
	private MorningCallListAdapter mla;
	private MorningCall mc;
	private LinearLayout mainLayout;
	private ListView lv;

	private AlertDialog mDialog = null;

	private ImageView sunimg;
	private ImageView monimg;
	private ImageView tueimg;
	private ImageView wedimg;
	private ImageView thuimg;
	private ImageView friimg;
	private ImageView satimg;

	private ArrayList<MorningCall> arrayListAlarmTimeItem = new ArrayList<MorningCall>();
	GregorianCalendar currentCalendar = new GregorianCalendar(
			TimeZone.getTimeZone("GMT+09:00"));

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainLayout = (LinearLayout) findViewById(R.id.LinearLayout1);
		lv = (ListView) findViewById(R.id.listid);

		mDb = new DBAdapter(this);
		arrayListAlarmTimeItem = mDb.getMorningCallList();

		((ProjectApplication) this.getApplication())
				.setData(arrayListAlarmTimeItem);

		mla = new MorningCallListAdapter(getApplicationContext(),
				arrayListAlarmTimeItem);

		lv.setAdapter(mla);

		alarmManager = (AlarmManager) getApplicationContext().getSystemService(
				Context.ALARM_SERVICE);
		alarmManager.setTimeZone("GMT+09:00");

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parentView,
					View clickedView, int position, long id) {

				int listpos = position;

				String mMusic;
				int hClock;
				int mClock;
				String ampm;

				int reqCode;

				Uri uri;

				boolean check;
				boolean[] mDay = new boolean[7];
				boolean modify = true;

				mMusic = arrayListAlarmTimeItem.get(position).getMusic();
				hClock = arrayListAlarmTimeItem.get(position).gethClock();
				mClock = arrayListAlarmTimeItem.get(position).getmClock();
				ampm = arrayListAlarmTimeItem.get(position).getAmpm();

				reqCode = arrayListAlarmTimeItem.get(position).getReqCode();
				uri = arrayListAlarmTimeItem.get(position).getUri();
				check = arrayListAlarmTimeItem.get(position).getCheck();
				mDay = arrayListAlarmTimeItem.get(position).getDay();

				Intent i = new Intent(getApplicationContext(),
						NewActivity.class);
				i.putExtra("position", listpos);
				i.putExtra("modify", modify);
				i.putExtra("music", mMusic);
				i.putExtra("hClock", hClock);
				i.putExtra("mClock", mClock);
				i.putExtra("ampm", ampm);
				i.putExtra("reqCode", reqCode);
				i.putExtra("uri", uri);
				i.putExtra("check", check);
				i.putExtra("Day", mDay);

				startActivityForResult(i, 0);
			}
		});

		lv.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parentView,
					View clickedView, int position, long id) {

				mc = arrayListAlarmTimeItem.get(position);
				
				mDialog = createInflaterDialog();
				mDialog.show();

				return false;
			}
		});
		
		mContext = this;
	}
	
	@Override
	public void onResume(){
		super.onResume();
		Log.v("resume!!!!!!", "!!!!!1");
		mla.notifyDataSetChanged();
	}

	private AlertDialog createInflaterDialog() {
		final View innerView = getLayoutInflater().inflate(
				R.layout.dialog_layout_del, null);
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("알람 제거");
		ab.setView(innerView);

		ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				mDb.deleteMorningCall(mc.getReqCode());
				
				Intent intent = new Intent(MainActivity.this,
						AlarmReceiver.class);
				
				PendingIntent pi = PendingIntent.getBroadcast(
						MainActivity.this, mc.getReqCode(), intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.cancel(pi);
				
				Toast.makeText(getApplicationContext(), "알람 삭제",
						Toast.LENGTH_SHORT).show();
				setDismiss(mDialog);
				
				Log.v("resume@@@@@@@", "@@@@@@@");
				((MainActivity)(MainActivity.mContext)).onResume();
			}
		});

		ab.setNegativeButton("취소", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				setDismiss(mDialog);
			}
		});

		return ab.create();
	}

	protected void setDismiss(AlertDialog mDialog2) {

	}

	public void onClickAddbtn(View v) {

		Intent i = new Intent(this, NewActivity.class);
		startActivityForResult(i, 0);

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {

			Uri uri = data.getParcelableExtra("uri");

			String mMusic = data.getStringExtra("music");
			int hClock = data.getIntExtra("hclock", 0);
			int mClock = data.getIntExtra("mclock", 0);
			int reqCode = data.getIntExtra("reqCode", 0);
			boolean[] mDay = data.getBooleanArrayExtra("day");
			boolean check = data.getBooleanExtra("check", true);
			boolean modify = data.getBooleanExtra("modify", false);
			
			boolean chvibe = data.getBooleanExtra("vibe", false);

			String ampm = "";

			int currentYY = currentCalendar.get(Calendar.YEAR);
			int currentMM = currentCalendar.get(Calendar.MONTH);
			int currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);

			gregorianCalendar = new GregorianCalendar(
					TimeZone.getTimeZone("GMT+09:00"));
			gregorianCalendar.set(currentYY, currentMM, currentDD, hClock,
					mClock, 0);

			// 날짜 시간 오차 재조정
			if (gregorianCalendar.getTimeInMillis() < currentCalendar
					.getTimeInMillis()) {
				gregorianCalendar.set(currentYY, currentMM, currentDD + 1,
						hClock, mClock, 0);
				Log.v("TAG", gregorianCalendar.getTimeInMillis() + ":");
			}

			// 오전 오후 구분
			if (hClock < 12) {
				ampm = "AM";
			} else {
				ampm = "PM";
				hClock -= 12;

				if (hClock == 0)
					hClock = 12;
			}

			/* 모닝콜 객체 생성 */
			MorningCall mc = new MorningCall(mMusic, hClock, mClock, ampm,
					mDay, reqCode, uri, check);
			
			mc.setVibe(chvibe);

			Log.v("modify: " + modify,
					"position" + data.getIntExtra("position", 0));

			// 리스트에 모닝콜 추가
			if (modify == false) {
				mDb.insertMorningCall(mc);

				arrayListAlarmTimeItem.add(mc);

				Intent intent = new Intent(MainActivity.this,
						AlarmReceiver.class);
				intent.putExtra("uri", uri);
				intent.putExtra("music", mMusic);
				intent.putExtra("day", mDay);
				intent.putExtra("vibe", chvibe);

				PendingIntent pi = PendingIntent.getBroadcast(
						MainActivity.this, reqCode, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
						gregorianCalendar.getTimeInMillis(),
						AlarmManager.INTERVAL_DAY, pi);

				Toast.makeText(getApplicationContext(),
						hClock + ":" + mClock + ampm + "알람 설정",
						Toast.LENGTH_SHORT).show();
			} else if (modify == true) {
				mDb.updateMorningCall(mc);

				sunimg = (ImageView) findViewById(R.id.sunid);
				monimg = (ImageView) findViewById(R.id.monid);
				tueimg = (ImageView) findViewById(R.id.tueid);
				wedimg = (ImageView) findViewById(R.id.wedid);
				thuimg = (ImageView) findViewById(R.id.thuid);
				friimg = (ImageView) findViewById(R.id.friid);
				satimg = (ImageView) findViewById(R.id.satid);

				if (mDay[0] == true) {
					sunimg.setImageResource(R.drawable.sun2);
				} else if (mDay[0] == false) {
					sunimg.setImageResource(R.drawable.sun1);
				}

				if (mDay[1] == true) {
					monimg.setImageResource(R.drawable.mon2);
				} else if (mDay[1] == false) {
					monimg.setImageResource(R.drawable.mon1);
				}

				if (mDay[2] == true) {
					tueimg.setImageResource(R.drawable.tue2);
				} else if (mDay[2] == false) {
					tueimg.setImageResource(R.drawable.tue1);
				}

				if (mDay[3] == true) {
					wedimg.setImageResource(R.drawable.wed2);
				} else if (mDay[3] == false) {
					wedimg.setImageResource(R.drawable.wed1);
				}

				if (mDay[4] == true) {
					thuimg.setImageResource(R.drawable.thu2);
				} else if (mDay[4] == false) {
					thuimg.setImageResource(R.drawable.thu1);
				}

				if (mDay[5] == true) {
					friimg.setImageResource(R.drawable.fri2);
				} else if (mDay[5] == false) {
					friimg.setImageResource(R.drawable.fri1);
				}

				if (mDay[6] == true) {
					satimg.setImageResource(R.drawable.sat2);
				} else if (mDay[6] == false) {
					satimg.setImageResource(R.drawable.sat1);
				}

				arrayListAlarmTimeItem.set(data.getIntExtra("position", 0), mc);
				mla.notifyDataSetChanged();

				Intent intent = new Intent(MainActivity.this,
						AlarmReceiver.class);
				intent.putExtra("uri", uri);
				intent.putExtra("music", mMusic);
				intent.putExtra("day", mDay);
				intent.putExtra("vibe", chvibe);
				
				PendingIntent pi = PendingIntent.getBroadcast(
						MainActivity.this, reqCode, intent,
						PendingIntent.FLAG_UPDATE_CURRENT);
				alarmManager.cancel(pi);

				if (check == true) {
					alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
							gregorianCalendar.getTimeInMillis(),
							AlarmManager.INTERVAL_DAY, pi);

					Toast.makeText(getApplicationContext(),
							hClock + ":" + mClock + ampm + " 알람 수정",
							Toast.LENGTH_SHORT).show();
				}

				Toast.makeText(getApplicationContext(),
						hClock + ":" + mClock + ampm + " 알람 수정 CHECK:OFF",
						Toast.LENGTH_SHORT).show();
			}
		}
	}

}
