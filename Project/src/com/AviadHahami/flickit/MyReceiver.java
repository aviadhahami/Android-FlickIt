/**
@author AviadHahami
 */

package com.AviadHahami.flickit;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.util.Log;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {
	private WifiP2pManager mManager;
	private Channel mChannel;
	private MainScreen mActivity;
	private List peers = new ArrayList();

	public MyReceiver(WifiP2pManager manager, Channel channel,
			MainScreen activity) {
		super();
		this.mActivity = activity;
		this.mManager = manager;
		this.mChannel = channel;
	}

	@Override
	public void onReceive(Context context, Intent intent) {

		//
		String action = intent.getAction();

		// check in P2P enabled
		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				// Wifi P2P is enabled
				Toast.makeText(context, "Wifi Enabled", Toast.LENGTH_LONG)
						.show();
				Toast.makeText(context, action, Toast.LENGTH_LONG).show();
			} else {
				// Wi-Fi P2P is not enabled
				Toast.makeText(context, "P2P isn't enabled on this device",
						Toast.LENGTH_LONG).show();

			}
		}

		// Post connection handlers
		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			// Check to see if Wi-Fi is enabled and notify appropriate activity

		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			// Call WifiP2pManager.requestPeers() to get a list of current peers
			// request available peers from the wifi p2p manager. This is an
			// asynchronous call and the calling activity is notified with a
			// callback on PeerListListener.onPeersAvailable()
			if (MainScreen.mManager != null) {
				MainScreen.mManager.requestPeers(mChannel, new PeerListListener(){

					@Override
					public void onPeersAvailable(WifiP2pDeviceList peers) {
						// TODO Auto-generated method stub
						 // Out with the old, in with the new.
			            peers.clear();
			            peers.addAll(peerList.getDeviceList());

			            // If an AdapterView is backed by this data, notify it
			            // of the change.  For instance, if you have a ListView of available
			            // peers, trigger an update.
			            ((WiFiPeerListAdapter) getListAdapter()).notifyDataSetChanged();
			            if (peers.size() == 0) {
			                Log.d(WiFiDirectActivity.TAG, "No devices found");
			                return;
			            }
						
					}
					
				});
				this.mManager = MainScreen.mManager;
				
			} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
					.equals(action)) {
				// Respond to new connection or disconnections
			} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
					.equals(action)) {
				// Respond to this device's wifi state changing
			}
		}

	}
}
