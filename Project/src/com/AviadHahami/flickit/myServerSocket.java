/**
@author AviadHahami
 */

package com.AviadHahami.flickit;

import android.widget.TextView;

public class myServerSocket {
	boolean running = false;
	private MainScreen mActivity;

	public myServerSocket(MainScreen activiy) {
		this.mActivity = activiy;
	}

	public void go() {
		if (!running) {
			running = true;
			TextView t = (TextView) mActivity.findViewById(R.id.chatText);
			t.setText("Yoyo im server!");
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
