/**
@author AviadHahami
 */

package com.AviadHahami.flickit;

import java.util.Iterator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Toast;

public class MyReceiver extends BroadcastReceiver {

	private WifiP2pManager mManager;
	private Channel mChannel;
	private MainScreen mActivity;
	private WifiP2pDeviceList peers;

	public MyReceiver(WifiP2pManager manager, Channel channel,
			MainScreen activity) {
		super();
		this.mManager = manager;
		this.mChannel = channel;
		this.mActivity = activity;
	}

	@Override
	public void onReceive(final Context context, Intent intent) {

		Bundle incoming = intent.getExtras();
		String incomingData = "";
		if (incoming != null) {
			incomingData = incoming.getString("sentData");
		}

		String action = intent.getAction();

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
				// Wifi P2P is enabled
				Toast.makeText(context, "Wifi P2P enabled", Toast.LENGTH_LONG)
						.show();
			} else {
				// Wi-Fi P2P is not enabled
				Toast.makeText(context, "Wifi P2P disabled", Toast.LENGTH_LONG)
						.show();
			}
		}

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			// Check to see if Wi-Fi is enabled and notify appropriate activity
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			// Call WifiP2pManager.requestPeers() to get a list of current peers
			Toast.makeText(context, action, Toast.LENGTH_LONG).show();
			if (mManager != null) {
				mManager.requestPeers(mChannel, new PeerListListener() {

					@Override
					public void onPeersAvailable(WifiP2pDeviceList peers) {
						Toast.makeText(
								context,
								"peers available "
										+ peers.getDeviceList().size(),
								Toast.LENGTH_LONG).show();
						WifiP2pDevice myDevice = null;
						for (Iterator<WifiP2pDevice> iterator = peers
								.getDeviceList().iterator(); iterator.hasNext();) {
							myDevice = iterator.next();
						}
						connectToDevice(myDevice);
					}
				});
			}

		} else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION
				.equals(action)) {
			// Respond to new connection or disconnections
		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
				.equals(action)) {
			// Respond to this device's wifi state changing
		}
	}

	protected void connectToDevice(WifiP2pDevice myDevice) {
		WifiP2pConfig config = new WifiP2pConfig();
		config.deviceAddress = myDevice.deviceAddress;
		mManager.connect(mChannel, config, new ActionListener() {

			@Override
			public void onSuccess() {
				Toast.makeText(mActivity, "Did connect !", Toast.LENGTH_LONG)
						.show();

			}

			@Override
			public void onFailure(int reason) {
				Toast.makeText(mActivity, "Did not connect !",
						Toast.LENGTH_LONG).show();
			}
		});

	

	}

}
