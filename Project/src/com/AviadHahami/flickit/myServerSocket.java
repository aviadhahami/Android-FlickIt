/**
@author AviadHahami
 */

package com.AviadHahami.flickit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.widget.TextView;

public class myServerSocket {
	boolean running = false;
	private MainScreen mActivity;
	ServerSocket serverSocket;

	public myServerSocket(MainScreen activiy) {
		this.mActivity = activiy;
		this.serverSocket = null;
	}

	public void go() {
		if (!running) {
			running = true;
			TextView t = (TextView) mActivity.findViewById(R.id.chatText);
			t.setText("Yoyo im server!");

			// Socket starting here
			try {
				if (serverSocket == null) {

					this.serverSocket = new ServerSocket(9328);
					mActivity.onScreenLogger("waiting for socket connection");
					mActivity.onScreenLogger("Inet address is "
							+ serverSocket.getInetAddress());
					Socket client = serverSocket.accept();
					InputStream inputStream = client.getInputStream();
					StringBuilder sb = new StringBuilder();
					BufferedReader bufferedReader = new BufferedReader(
							new InputStreamReader(inputStream, "UTF-8"));
					OutputStream outputStream = client.getOutputStream();

					String line = bufferedReader.readLine();
					while (line != null) {
						line = bufferedReader.readLine();
					}
				}
			} catch (Exception e) {
				mActivity.onScreenLogger("Caught socket xception: " + e);
			}
		}

	}

	public void kill() {
		if (!running)
			return;
		running = false;
		TextView t = (TextView) mActivity.findViewById(R.id.chatText);
		t.setText("WTF am I");
		try {
			serverSocket.close();
		} catch (Exception e) {

		}
		// TODO: remove socket

	}

}
