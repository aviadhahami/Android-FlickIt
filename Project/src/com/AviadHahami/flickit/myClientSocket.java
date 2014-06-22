/**
@author AviadHahami
 */

package com.AviadHahami.flickit;

import android.widget.TextView;

public class myClientSocket {
	boolean running = false;
	private MainScreen mActivity;

	public myClientSocket(MainScreen activity) {
		this.mActivity = activity;
	}

	public void go() {
		if (!running) {
			running = true;
			TextView t = (TextView) mActivity.findViewById(R.id.chatText);
			t.setText("Yoyo im client!");

		}

	}

	public void kill() {
		if (!running)
			return;
		running = false;
		TextView t = (TextView) mActivity.findViewById(R.id.chatText);
		t.setText("WTF am I");
		// TODO: remove socket

	}

}
