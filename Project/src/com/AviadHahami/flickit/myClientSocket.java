/**
@author AviadHahami
 */

package com.AviadHahami.flickit;

import java.net.Socket;

import android.widget.TextView;

public class myClientSocket {
	boolean running = false;
	private MainScreen mActivity;
	Socket clientSocket;

	public myClientSocket(MainScreen activity) {
		this.mActivity = activity;
	}

	public void go() {
		if (!running) {
			running = true;
			TextView t = (TextView) mActivity.findViewById(R.id.chatText);
			t.setText("Yoyo im client!");
			try {
				clientSocket = new Socket("0.0.0.0", 9328);
				mActivity.onScreenLogger("Client connected");
			} catch (Exception e) {
				mActivity.onScreenLogger("clientSocket exception! " + e);
			}
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
