package kit.yeon.morningcallofduty;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

// SQLite3 데이터베이스에 연결하기 위한 어댑터 클래스
public class DBAdapter extends SQLiteOpenHelper {
	private SQLiteDatabase db;

	public static final int ALL = -1;

	// DB이름
	private static final String DB_NAME = "MorningCall.db";
	private static final int VERSION = 1;

	public DBAdapter(Context context) {
		super(context, DB_NAME, null, VERSION);
		db = this.getWritableDatabase();
	}

	@Override
	public synchronized void close() {
		db.close();
		super.close();
	}

	@Override
	protected void finalize() throws Throwable {
		close();
		super.finalize();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String createQuery = "CREATE TABLE morningCall "
				+ " (id INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ "   music VARCHAR(50) ," + "   hClock INTEGER ,"
				+ "   mClock INTEGER ," + "" + "   ampm VARCHAR(10) ,"
				+ "   sun INTEGER ," + "   mon INTEGER ," + "   tue INTEGER ,"
				+ "   wed INTEGER ," + "   thu INTEGER ," + "   fri INTEGER ,"
				+ "   sat INTEGER ," + "   reqCode INTEGER ,"
				+ "   isuri VARCHAR(150) ," + "   ischeck INTEGER ," + " vibecheck INTEGER" +
						" ); ";
		db.execSQL(createQuery);
	}

	public MorningCall getMorningCallById(int reqCode) {
		MorningCall ret = null;
		String query = "select * from morningCall WHERE reqCode ="
				+ Integer.toString(reqCode) + ";";

		Cursor c = db.rawQuery(query, null);

		if (c.moveToFirst()) {

			final int idxMusic = c.getColumnIndex("music");
			final int idxHClock = c.getColumnIndex("hClock");
			final int idxMClock = c.getColumnIndex("mClock");
			final int idxAmpm = c.getColumnIndex("ampm");
			final int idxSun = c.getColumnIndex("sun");
			final int idxMon = c.getColumnIndex("mon");
			final int idxTue = c.getColumnIndex("tue");
			final int idxWed = c.getColumnIndex("wed");
			final int idxThu = c.getColumnIndex("thu");
			final int idxFri = c.getColumnIndex("fri");
			final int idxSat = c.getColumnIndex("sat");
			final int idxReqCode = c.getColumnIndex("reqCode");
			final int idxUri = c.getColumnIndex("isuri");
			final int idxCheck = c.getColumnIndex("ischeck");
			final int idxVibe = c.getColumnIndex("vibecheck");
			
			boolean[] Day = { (c.getInt(idxSun) != 0), (c.getInt(idxMon) != 0),
					(c.getInt(idxTue) != 0), (c.getInt(idxWed) != 0),
					(c.getInt(idxThu) != 0), (c.getInt(idxFri) != 0),
					(c.getInt(idxSat) != 0) };
			boolean check = (c.getInt(idxCheck) != 0);

			Uri uri = Uri.parse(c.getString(idxUri));

			MorningCall m = new MorningCall(c.getString(idxMusic),
					c.getInt(idxHClock), c.getInt(idxMClock),
					c.getString(idxAmpm), Day, c.getInt(idxReqCode), uri, check);
			
			m.setVibe((c.getInt(idxVibe) != 0));

			ret = m;
		}

		c.close();

		return ret;
	}

	public ArrayList<MorningCall> getMorningCallList() {

		ArrayList<MorningCall> ret = new ArrayList<MorningCall>();
		String query = " select * from morningCall;";

		Cursor c = db.rawQuery(query, null);

		if (c.moveToFirst()) {
			do {
				final int idxMusic = c.getColumnIndex("music");
				final int idxHClock = c.getColumnIndex("hClock");
				final int idxMClock = c.getColumnIndex("mClock");
				final int idxAmpm = c.getColumnIndex("ampm");
				final int idxSun = c.getColumnIndex("sun");
				final int idxMon = c.getColumnIndex("mon");
				final int idxTue = c.getColumnIndex("tue");
				final int idxWed = c.getColumnIndex("wed");
				final int idxThu = c.getColumnIndex("thu");
				final int idxFri = c.getColumnIndex("fri");
				final int idxSat = c.getColumnIndex("sat");
				final int idxReqCode = c.getColumnIndex("reqCode");
				final int idxUri = c.getColumnIndex("isuri");
				final int idxCheck = c.getColumnIndex("ischeck");
				final int idxVibe = c.getColumnIndex("vibecheck");
				
				boolean[] Day = { (c.getInt(idxSun) != 0),
						(c.getInt(idxMon) != 0), (c.getInt(idxTue) != 0),
						(c.getInt(idxWed) != 0), (c.getInt(idxThu) != 0),
						(c.getInt(idxFri) != 0), (c.getInt(idxSat) != 0) };
				boolean check = (c.getInt(idxCheck) != 0);

				Uri uri = Uri.parse(c.getString(idxUri));

				MorningCall m = new MorningCall(c.getString(idxMusic),
						c.getInt(idxHClock), c.getInt(idxMClock),
						c.getString(idxAmpm), Day, c.getInt(idxReqCode), uri,
						check); 
				
				m.setVibe((idxVibe!=0));

				ret.add(m);

			} while (c.moveToNext());

		}

		c.close();

		return ret;
	}

	public void insertMorningCall(MorningCall morningcall) {

		String insertQuery = ""
				+ "INSERT INTO morningCall"
				+ "(music, hClock, mClock, ampm, sun, mon, tue, wed, thu, fri, sat, reqCode, isuri, ischeck , vibecheck)"
				+ "VALUES" + "( '"
				+ morningcall.getMusic()
				+ "' , "
				+ +morningcall.gethClock()
				+ " , "
				+ +morningcall.getmClock()
				+ " , '"
				+ morningcall.getAmpm()
				+ "' , "
				+ +morningcall.getSun()
				+ " , "
				+ +morningcall.getMon()
				+ " , "
				+ +morningcall.getTue()
				+ " , "
				+ +morningcall.getWed()
				+ " , "
				+ +morningcall.getThu()
				+ " , "
				+ +morningcall.getFri()
				+ " , "
				+ +morningcall.getSat()
				+ " , "
				+ +morningcall.getReqCode()
				+ " , '"
				+ morningcall.getUri().toString()
				+ "' , "
				+ (morningcall.getCheck() ? 1 : 0) 
				+ (morningcall.getVibe() ? 1 : 0) +" );";

		// INSERT INTO money (desc, money, dt, is_expense);

		// VALUES
		// ('핫도그 사먹음' ,
		// 20000,
		// '2012-04=01' ,
		// 1);

		db.execSQL(insertQuery);
	}

	public void updateMorningCall(MorningCall morningcall) {

		String updateQuery = "" + " update morningCall " + " set music = '"
				+ morningcall.getMusic() + "' , " + "hClock = "
				+ morningcall.gethClock() + " , " + "mClock = "
				+ morningcall.getmClock() + " , " + "ampm = '"
				+ morningcall.getAmpm() + "' , " + "sun = "
				+ morningcall.getSun() + " , " + "mon = "
				+ morningcall.getMon() + " , " + "tue = "
				+ morningcall.getTue() + " , " + "wed = "
				+ morningcall.getWed() + " , " + "thu = "
				+ morningcall.getThu() + " , " + "fri = "
				+ morningcall.getFri() + " , " + "sat = "
				+ morningcall.getSat() + " , " + "reqCode = "
				+ morningcall.getReqCode() + " , " + "isuri = '"
				+ morningcall.getUri().toString() + "' , " + "ischeck = "
				+ (morningcall.getCheck() ? 1 : 0) + " , " + "vibecheck = "
				+ (morningcall.getVibe() ? 1 : 0) + " where reqCode = "
				+ Integer.toString(morningcall.getReqCode()) + ";";

		db.execSQL(updateQuery);
	}

	public void deleteMorningCall(int reqCode) {
		String updateQuery = "" + " delete from morningCall " 
							+ "where reqCode = "
							+ Integer.toString(reqCode) + ";";

		db.execSQL(updateQuery);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TABLE DELETION QUERY HERE!!
		// db.execSQL( "drop table if exists people;");

		onCreate(db);
	}

}
