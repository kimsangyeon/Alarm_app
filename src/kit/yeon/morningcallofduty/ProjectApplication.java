package kit.yeon.morningcallofduty;

import java.util.ArrayList;

import android.app.Application;

public class ProjectApplication extends Application{

	private ArrayList<MorningCall> arrayListAlarmTimeItem;
	
	public ArrayList<MorningCall> getData()
	  {
	    return arrayListAlarmTimeItem;
	  }
	  public void setData(ArrayList<MorningCall> data)
	  {
	    this.arrayListAlarmTimeItem = data;
	  }
}
