package br.com.ondeparominhabike;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import br.com.ondeparominhabike.model.Lugares;

public class SplashScreen extends Activity {
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.splash_screen);
		new Thread(new Runnable() {
		    public void run() {
		    	Lugares.sincronizaLugares(SplashScreen.this);
		    	
		    	Intent intent = new Intent(SplashScreen.this, MainActivity.class);
				
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(intent);
		    }
		  }).start();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//		}
		return true;
	}

	@Override
	public void onBackPressed() {
		return;
	}
}
