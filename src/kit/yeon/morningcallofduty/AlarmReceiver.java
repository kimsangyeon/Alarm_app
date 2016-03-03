package kit.yeon.morningcallofduty;

import java.util.Calendar;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		Log.v("receiver", "receiver");
		/*
		if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
			
			GregorianCalendar gregorianCalendar;
			GregorianCalendar currentCalendar = new GregorianCalendar(
					TimeZone.getTimeZone("GMT+09:00"));
			
			int currentYY = currentCalendar.get(Calendar.YEAR);
			int currentMM = currentCalendar.get(Calendar.MONTH);
			int currentDD = currentCalendar.get(Calendar.DAY_OF_MONTH);
			
			gregorianCalendar = new GregorianCalendar(
					TimeZone.getTimeZone("GMT+09:00"));
			gregorianCalendar.set(currentYY, currentMM, currentDD, hClock,
					mClock, 0);
			
			if (gregorianCalendar.getTimeInMillis() < currentCalendar
					.getTimeInMillis()) {
				gregorianCalendar.set(currentYY, currentMM, currentDD + 1,
						hClock, mClock, 0);
				Log.v("TAG", gregorianCalendar.getTimeInMillis() + ":");
			}

			AlarmManager alarmManager = (AlarmManager) context
					.getSystemService(Context.ALARM_SERVICE);
			alarmManager.setTimeZone("GMT+09:00");

			PendingIntent pi = PendingIntent.getBroadcast(context, reqCode,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
					gregorianCalendar.getTimeInMillis(),
					AlarmManager.INTERVAL_DAY, pi);
			
			
			
			Log.v("!!!!!!!!", "!!!!!!!!");
		}
		 */
		boolean[] mDay = new boolean[7];

		mDay = intent.getBooleanArrayExtra("day");

		Calendar cal = Calendar.getInstance();

		if (!mDay[cal.get(Calendar.DAY_OF_WEEK) - 1]) {
			return;
		} else {
			Intent i = new Intent(context, AlramActivity.class);
			i.putExtra("uri", intent.getParcelableExtra("uri"));

			PendingIntent pi = PendingIntent.getActivity(context, 0, i,
					PendingIntent.FLAG_ONE_SHOT);
			try {
				pi.send();
			} catch (CanceledException e) {
				e.printStackTrace();
			}

		}

	}

}
