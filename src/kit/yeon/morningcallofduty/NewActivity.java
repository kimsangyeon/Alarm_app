package kit.yeon.morningcallofduty;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class NewActivity extends Activity {

	public static final int DEFAULT_ALARM_REQUEST = 800;
	private Vibrator vibe;
	private ArrayList<MorningCall> arrayListAlarmTimeItem;

	private AlertDialog mDialog = null;

	private final int TIME_DIALOG_ID = 0;

	private TextView hourtxt;
	private TextView mintxt1;
	private TextView mintxt2;

	private LinearLayout LayoutHour;
	private LinearLayout.LayoutParams layparam;
	private ImageButton hourup;
	private ImageButton hourdown;

	private ImageButton min1up;
	private ImageButton min1down;

	private ImageButton min2up;
	private ImageButton min2down;

	private ImageButton ampmimg;

	private ImageView sunimg;
	private ImageView monimg;
	private ImageView tueimg;
	private ImageView wedimg;
	private ImageView thuimg;
	private ImageView friimg;
	private ImageView satimg;

	private Button soundbtn;
	private CheckBox vibecheck;
	
	
	private Uri uri;
	private int reqCode;
	private String mMusic;
	private String ampm;

	private boolean modify;
	private boolean check;
	private boolean chvibe;
	
	private int listPos;

	boolean[] mDay = { false, false, false, false, false, false, false };

	/** Called when the activity is first created. */
	@SuppressLint("SimpleDateFormat")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new);
		
		Intent i = getIntent();
		modify = i.getBooleanExtra("modify", false);

		arrayListAlarmTimeItem = ((ProjectApplication) this.getApplication())
				.getData();

		// timePickerAlarmTime = (TimePicker) findViewById(R.id.timeid);
		// timePickerAlarmTime.setIs24HourView(false);

		hourtxt = (TextView) findViewById(R.id.hourtxt);
		mintxt1 = (TextView) findViewById(R.id.mintxt1);
		mintxt2 = (TextView) findViewById(R.id.mintxt2);

		LayoutHour = (LinearLayout) findViewById(R.id.Layhourid);
		layparam = (LinearLayout.LayoutParams) LayoutHour.getLayoutParams();

		hourup = (ImageButton) findViewById(R.id.hourupid);
		hourdown = (ImageButton) findViewById(R.id.hourdownid);

		min1up = (ImageButton) findViewById(R.id.min1upid);
		min1down = (ImageButton) findViewById(R.id.min1downid);

		min2up = (ImageButton) findViewById(R.id.min2upid);
		min2down = (ImageButton) findViewById(R.id.min2downid);

		ampmimg = (ImageButton) findViewById(R.id.ampmimg);

		sunimg = (ImageView) findViewById(R.id.sunid);
		monimg = (ImageView) findViewById(R.id.monid);
		tueimg = (ImageView) findViewById(R.id.tueid);
		wedimg = (ImageView) findViewById(R.id.wedid);
		thuimg = (ImageView) findViewById(R.id.thuid);
		friimg = (ImageView) findViewById(R.id.friid);
		satimg = (ImageView) findViewById(R.id.satid);

		soundbtn = (Button) findViewById(R.id.soundid);
		vibecheck = (CheckBox) findViewById(R.id.vibecheck);
		
		vibecheck.setOnClickListener(listener);
		vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);    
		
		mDialog = createInflaterDialog();

		Log.v("!!" + i.getBooleanExtra("modify", false), "!!!");

		if (modify == true) {
			listPos = i.getIntExtra("position", 0);

			String mMusic = i.getStringExtra("music");

			int hh = i.getIntExtra("hClock", 0);
			int mm = i.getIntExtra("mClock", 0);
			int reqCode = i.getIntExtra("reqCode", 0);

			this.mMusic = mMusic;
			this.reqCode = reqCode;
			this.ampm = i.getStringExtra("ampm");

			Uri uri = i.getParcelableExtra("uri");

			this.uri = uri;

			mDay = i.getBooleanArrayExtra("Day");
			boolean check = i.getBooleanExtra("check", false);

			this.check = check;

			int min1;
			int min2;

			if (mm > 10) {
				min1 = mm / 10;
				min2 = mm % 10;
			} else {
				min1 = 0;
				min2 = mm;
			}

			if (hh >= 10) {
				layparam.leftMargin = 50;
				LayoutHour.setLayoutParams(layparam);
			} else {
				layparam.leftMargin = 120;
				LayoutHour.setLayoutParams(layparam);
			}

			hourtxt.setText(Integer.toString(hh));
			mintxt1.setText(Integer.toString(min1));
			mintxt2.setText(Integer.toString(min2));

			soundbtn.setText(mMusic);

			// Log.v("AMPM:" + Integer.toBinaryString(ampm) +":", "AMPM:"+ "PM"+
			// ":");

			if (ampm.equals("PM")) {
				ampmimg.setImageResource(R.drawable.pm);
			}
			if (ampm.equals("AM")) {
				ampmimg.setImageResource(R.drawable.am);
			}

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

		} else {
			this.check = true;

			long now = System.currentTimeMillis();
			Date date = new Date(now);
			SimpleDateFormat CurHourFormat = new SimpleDateFormat("HH");
			SimpleDateFormat CurMinuteFormat = new SimpleDateFormat("mm");

			String strCurHour = CurHourFormat.format(date);
			String strCurMinute = CurMinuteFormat.format(date);

			if (Integer.parseInt(strCurHour) >= 10) {
				layparam.leftMargin = 50;
				LayoutHour.setLayoutParams(layparam);
			} else {
				layparam.leftMargin = 120;
				LayoutHour.setLayoutParams(layparam);
			}

			if (Integer.parseInt(strCurHour) < 12) {
				ampm = "AM";
				ampmimg.setImageResource(R.drawable.am);
				if (Integer.parseInt(strCurHour) == 0)
					hourtxt.setText("12");
				else
					hourtxt.setText(strCurHour);
			} else {
				ampm = "PM";
				ampmimg.setImageResource(R.drawable.pm);
				hourtxt.setText(Integer.toString(Integer.parseInt(strCurHour) - 12));
			}

			if (Integer.parseInt(strCurMinute) < 10) {
				mintxt1.setText("0");
				mintxt2.setText(strCurMinute.substring(1));
			} else {
				mintxt1.setText(strCurMinute.substring(0, 1));
				mintxt2.setText(strCurMinute.substring(1));
			}
		}
	}
	
	
	
	private final OnClickListener listener = new OnClickListener() {
		public void onClick(View v) {
			if (v instanceof CheckBox) {
				CheckBox check = (CheckBox) v;
				
				if (check.isChecked() == true){
					vibe.vibrate(500);
					chvibe = true;
				}else if(check.isChecked() == false){
					chvibe = false;
				}
			}
		}
	};

	@SuppressWarnings("deprecation")
	public void onClickTimeSet(View v) {
		showDialog(TIME_DIALOG_ID);
	}

	@Override
	protected Dialog onCreateDialog(int id) {

		int hourOfDay;
		int minute;

		int min1 = Integer.parseInt(mintxt1.getText().toString());
		int min2 = Integer.parseInt(mintxt2.getText().toString());

		minute = (min1 * 10) + min2;

		if (ampm.equals("PM")) {
			hourOfDay = Integer.parseInt(hourtxt.getText().toString()) + 12;
		} else {
			hourOfDay = Integer.parseInt(hourtxt.getText().toString());
		}

		switch (id) {

		case TIME_DIALOG_ID:
			return new TimePickerDialog(this, mTimeSetListener, hourOfDay,
					minute, false);
		}
		return null;
	}

	private TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
		@Override
		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

			if (hourOfDay >= 10) {
				layparam.leftMargin = 50;
				LayoutHour.setLayoutParams(layparam);
			} else {
				layparam.leftMargin = 120;
				LayoutHour.setLayoutParams(layparam);
			}
			
			if (hourOfDay < 12) {
				ampm = "AM";
				ampmimg.setImageResource(R.drawable.am);
				if (hourOfDay == 0)
					hourtxt.setText("12");
				else
					hourtxt.setText(Integer.toString(hourOfDay));
			} else {
				ampm = "PM";
				ampmimg.setImageResource(R.drawable.pm);
				if (hourOfDay - 12 == 0) {
					hourtxt.setText(Integer.toString(hourOfDay));
				} else {
					hourtxt.setText(Integer.toString(hourOfDay - 12));
				}
			}

			if (minute < 10) {
				mintxt1.setText("0");
				mintxt2.setText(Integer.toString(minute));
			} else {
				mintxt1.setText(Integer.toString(minute).substring(0, 1));
				mintxt2.setText(Integer.toString(minute).substring(1));
			}
		}

	};

	public void onClickHour(View v) {
		if (v.getId() == hourup.getId()) {

			if (Integer.parseInt(hourtxt.getText().toString()) == 9) {
				layparam.leftMargin = 50;
				LayoutHour.setLayoutParams(layparam);
			}

			if (Integer.parseInt(hourtxt.getText().toString()) == 12) {
				hourtxt.setText("1");
				layparam.leftMargin = 120;
				LayoutHour.setLayoutParams(layparam);
			} else {
				hourtxt.setText(Integer.toString(Integer.parseInt(hourtxt
						.getText().toString()) + 1));
			}

		} else if (v.getId() == hourdown.getId()) {

			if (Integer.parseInt(hourtxt.getText().toString()) == 10) {
				layparam.leftMargin = 120;
				LayoutHour.setLayoutParams(layparam);
			}

			if (Integer.parseInt(hourtxt.getText().toString()) == 1) {
				hourtxt.setText("12");
				layparam.leftMargin = 50;
				LayoutHour.setLayoutParams(layparam);
			} else {
				hourtxt.setText(Integer.toString(Integer.parseInt(hourtxt
						.getText().toString()) - 1));
			}
		}
	}

	public void onClickMin1(View v) {
		if (v.getId() == min1up.getId()) {
			if (Integer.parseInt(mintxt1.getText().toString()) == 5) {
				mintxt1.setText("0");
			} else {
				mintxt1.setText(Integer.toString(Integer.parseInt(mintxt1
						.getText().toString()) + 1));
			}
		} else if (v.getId() == min1down.getId()) {
			if (Integer.parseInt(mintxt1.getText().toString()) == 0) {
				mintxt1.setText("5");
			} else {
				mintxt1.setText(Integer.toString(Integer.parseInt(mintxt1
						.getText().toString()) - 1));
			}
		}
	}

	public void onClickMin2(View v) {
		if (v.getId() == min2up.getId()) {
			if (Integer.parseInt(mintxt2.getText().toString()) == 9) {
				mintxt2.setText("0");
			} else {
				mintxt2.setText(Integer.toString(Integer.parseInt(mintxt2
						.getText().toString()) + 1));
			}
		} else if (v.getId() == min2down.getId()) {
			if (Integer.parseInt(mintxt2.getText().toString()) == 0) {
				mintxt2.setText("9");
			} else {
				mintxt2.setText(Integer.toString(Integer.parseInt(mintxt2
						.getText().toString()) - 1));
			}
		}
	}

	public void onClickAmpm(View v) {
		if (ampm.equals("AM")) {
			ampmimg.setImageResource(R.drawable.pm);
			ampm = "PM";
		} else if (ampm.equals("PM")) {
			ampmimg.setImageResource(R.drawable.am);
			ampm = "AM";
		}
	}

	public void onClickSoundbtn(View v) {
		Intent i = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
		startActivityForResult(i, 0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 0:
			if (data == null)
				break;

			uri = data
					.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);

			Ringtone ringtone = RingtoneManager.getRingtone(
					getApplicationContext(), uri);
			mMusic = ringtone.getTitle(getApplicationContext());

			soundbtn.setText(mMusic);

			break;
		}
	}

	public void onClickDayImg(View v) {

		if (v.getId() == sunimg.getId()) {
			if (mDay[0] == false) {
				sunimg.setImageResource(R.drawable.sun2);
				mDay[0] = true;
			} else if (mDay[0] == true) {
				sunimg.setImageResource(R.drawable.sun1);
				mDay[0] = false;
			}
		} else if (v.getId() == monimg.getId()) {
			if (mDay[1] == false) {
				monimg.setImageResource(R.drawable.mon2);
				mDay[1] = true;
			} else if (mDay[1] == true) {
				monimg.setImageResource(R.drawable.mon1);
				mDay[1] = false;
			}
		} else if (v.getId() == tueimg.getId()) {
			if (mDay[2] == false) {
				tueimg.setImageResource(R.drawable.tue2);
				mDay[2] = true;
			} else if (mDay[2] == true) {
				tueimg.setImageResource(R.drawable.tue1);
				mDay[2] = false;
			}
		} else if (v.getId() == wedimg.getId()) {
			if (mDay[3] == false) {
				wedimg.setImageResource(R.drawable.wed2);
				mDay[3] = true;
			} else if (mDay[3] == true) {
				wedimg.setImageResource(R.drawable.wed1);
				mDay[3] = false;
			}
		} else if (v.getId() == thuimg.getId()) {
			if (mDay[4] == false) {
				thuimg.setImageResource(R.drawable.thu2);
				mDay[4] = true;
			} else if (mDay[4] == true) {
				thuimg.setImageResource(R.drawable.thu1);
				mDay[4] = false;
			}
		} else if (v.getId() == friimg.getId()) {
			if (mDay[5] == false) {
				friimg.setImageResource(R.drawable.fri2);
				mDay[5] = true;
			} else if (mDay[5] == true) {
				friimg.setImageResource(R.drawable.fri1);
				mDay[5] = false;
			}
		} else if (v.getId() == satimg.getId()) {
			if (mDay[6] == false) {
				satimg.setImageResource(R.drawable.sat2);
				mDay[6] = true;
			} else if (mDay[6] == true) {
				satimg.setImageResource(R.drawable.sat1);
				mDay[6] = false;
			}
		}

	}

	public void onClickOkbtn(View v) {

		boolean flag = true;

		for (int i = 0; i < 7; i++) {
			if (mDay[i] == true) {
				flag = false;
				break;
			}
		}

		if (uri == null || flag == true) {
			mDialog.show();
			return;
		}

		int min1 = Integer.parseInt(mintxt1.getText().toString());
		int min2 = Integer.parseInt(mintxt2.getText().toString());

		int hh = Integer.parseInt(hourtxt.getText().toString());
		int mm = (min1 * 10) + min2;

		if (modify == false) {
			reqCode = DEFAULT_ALARM_REQUEST + arrayListAlarmTimeItem.size();
		}

		if (ampm == "PM") {
			hh += 12;
		}

		Intent intent = new Intent(NewActivity.this, MainActivity.class);

		intent.putExtra("position", listPos);
		intent.putExtra("uri", uri);

		intent.putExtra("music", mMusic);
		intent.putExtra("hclock", hh);
		intent.putExtra("mclock", mm);
		intent.putExtra("day", mDay);
		intent.putExtra("reqCode", reqCode);
		intent.putExtra("check", check);
		intent.putExtra("modify", modify);
		
		intent.putExtra("vibe", chvibe);

		// intent.putExtra("data", "알람: " +
		// currentCalendar.getTime().toLocaleString());

		setResult(Activity.RESULT_OK, intent);
		finish();
	}

	public void onClickCancelbtn(View v) {
		finish();
	}

	private AlertDialog createInflaterDialog() {
		final View innerView = getLayoutInflater().inflate(
				R.layout.dialog_layout, null);
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		ab.setTitle("ERROR");
		ab.setView(innerView);

		ab.setPositiveButton("확인", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				setDismiss(mDialog);
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

	/**
	 * 다이얼로그 종료
	 * 
	 * @param dialog
	 */
	private void setDismiss(Dialog dialog) {
		if (dialog != null && dialog.isShowing())
			dialog.dismiss();
	}

}
