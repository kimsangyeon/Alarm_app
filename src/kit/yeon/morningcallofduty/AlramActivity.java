package kit.yeon.morningcallofduty;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

public class AlramActivity extends Activity {

	private MediaPlayer player;
	
	
	!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!여기부터 시작
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alram);

		Intent i = getIntent();
		Uri uri = i.getParcelableExtra("uri");
		
		Log.v("URI : " + uri, "URI :");
		

		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
						| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
						| WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

		player = new MediaPlayer();
		player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
			@Override
			public void onPrepared(MediaPlayer mp) {
				mp.start();
			}
	
		});
		try {
			player.setDataSource(getApplicationContext(), uri);
			player.prepare();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// TODO Auto-generated method stub
	}

	public void onClickStopBtn(View v) {
		player.stop();
		finish();

	}

}
