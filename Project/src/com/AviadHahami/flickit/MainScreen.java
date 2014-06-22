package com.AviadHahami.flickit;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pGroup;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.GroupInfoListener;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainScreen extends Activity {

	WifiP2pManager mManager;
	Channel mChannel;
	BroadcastReceiver mReceiver;
	IntentFilter mIntentFilter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_screen);
		
		TextView text = (TextView) findViewById(R.id.loggerTxt);
		text.setText(android.os.Build.DEVICE);
		mManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
		mChannel = mManager.initialize(this, getMainLooper(), null);
		mReceiver = new MyReceiver(mManager, mChannel, this);

		mIntentFilter = new IntentFilter();
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
		mIntentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
		mIntentFilter
				.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
		mIntentFilter
				.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);

		discoverPeers();
	}

	private void discoverPeers() {
		mManager.discoverPeers(mChannel, new WifiP2pManager.ActionListener() {

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onFailure(int reason) {
				// TODO Auto-generated method stub

			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main_screen, menu);
		return true;
	}

	/* register the broadcast receiver with the intent values to be matched */
	@Override
	protected void onResume() {
		super.onResume();
		registerReceiver(mReceiver, mIntentFilter);
	}

	/* unregister the broadcast receiver */
	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(mReceiver);

	}

	public void onScreenLogger(String input) {
		TextView t = (TextView) findViewById(R.id.loggerTxt);
		t.setText(t.getText() + "\n" + input);
	}

	public void killLink(View view) {
		if (mManager == null) {
			onScreenLogger("can not kill, null manager");
			return;
		}
		if (mChannel == null) {
			onScreenLogger("can not kill, null channel");
			return;
		}
		mManager.requestGroupInfo(mChannel, new GroupInfoListener() {

			@Override
			public void onGroupInfoAvailable(WifiP2pGroup group) {
				if (group != null && mManager != null && mChannel != null
						&& group.isGroupOwner()) {
					mManager.removeGroup(mChannel, new ActionListener() {

						@Override
						public void onSuccess() {
							onScreenLogger("killed link");
						}

						@Override
						public void onFailure(int reason) {
							onScreenLogger("couldn't kill link");
						}
					});
				}
			}
		});
		recreate();
	}
}
