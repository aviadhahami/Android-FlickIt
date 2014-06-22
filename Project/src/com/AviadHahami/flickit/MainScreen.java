package com.AviadHahami.flickit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainScreen extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}

	// Broadcasting intent
	public void broadcastIntent(View view) {
		Intent intent = new Intent();
		intent.setAction("com.AviadHahami.SEND_DATA");
		intent.putExtra("sentData", "Some data goes here");
		sendBroadcast(intent);
	}

}
