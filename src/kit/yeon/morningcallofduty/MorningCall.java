package kit.yeon.morningcallofduty;

import android.net.Uri;

public class MorningCall {

	private String mMusic;
	private int hClock;
	private int mClock;
	private String amPm;

	private boolean check;
	private boolean vibe;

	private boolean[] mDay = new boolean[7];

	public int reqCode;

	private Uri mUri;

	public MorningCall(String music, int hh, int mm, String ampm,
			boolean[] day, int req, Uri uri, boolean check) {
		this.mMusic = music;
		this.hClock = hh;
		this.mClock = mm;
		this.amPm = ampm;
		this.reqCode = req;

		this.mDay = day;

		this.mUri = uri;
		this.check = check;
	}
	
	public boolean getVibe(){
		return vibe;
	}
	
	public void setVibe(boolean vibe){
		this.vibe = vibe;
	}

	public String getClock() {
		if (mClock < 10) {
			return hClock + ":0" + mClock;
		} else {
			return hClock + ":" + mClock;
		}
	}

	public int gethClock() {
		return hClock;
	}

	public int getmClock() {
		return mClock;
	}

	public String getMusic() {
		return mMusic;
	}

	public boolean[] getDay() {
		return mDay;
	}

	public String getAmpm() {
		return amPm;
	}

	public int getReqCode() {
		return reqCode;
	}

	public boolean getCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

	public Uri getUri() {
		return mUri;
	}

	public int getSun() {
		return mDay[0] ? 1 : 0;
	}

	public int getMon() {
		return mDay[1] ? 1 : 0;
	}

	public int getTue() {
		return mDay[2] ? 1 : 0;
	}

	public int getWed() {
		return mDay[3] ? 1 : 0;
	}

	public int getThu() {
		return mDay[4] ? 1 : 0;
	}

	public int getFri() {
		return mDay[5] ? 1 : 0;
	}

	public int getSat() {
		return mDay[6] ? 1 : 0;
	}
}
