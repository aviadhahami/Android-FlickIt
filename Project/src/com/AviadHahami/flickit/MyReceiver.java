/**
@author AviadHahami
 */

package com.AviadHahami.flickit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		Bundle incoming = intent.getExtras();
		String incomingData = "";
		if (incoming != null) {
			incomingData = incoming.getString("sentData");
		}
		Toast.makeText(context, incomingData, Toast.LENGTH_LONG).show();
		Vibrator v = (Vibrator) context
				.getSystemService(Context.VIBRATOR_SERVICE);
		// Vibrate for 500 milliseconds
		v.vibrate(500);
	}

}
