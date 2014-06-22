/**
@author AviadHahami
 */

package com.AviadHahami.flickit;

import java.util.Iterator;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.net.wifi.p2p.WifiP2pManager.Channel;
import android.net.wifi.p2p.WifiP2pManager.ConnectionInfoListener;
import android.net.wifi.p2p.WifiP2pManager.PeerListListener;
import android.os.Bundle;

public class MyReceiver extends BroadcastReceiver {

	private WifiP2pManager mManager;
	private Channel mChannel;
	private MainScreen mActivity;
	private WifiP2pDeviceList peers;
	private boolean isConnected = false;
	private myClientSocket cs;
	private myServerSocket ss;

	public MyReceiver(WifiP2pManager manager, Channel channel,
			MainScreen activity) {
		super();
		this.mManager = manager;
		this.mChannel = channel;
		this.mActivity = activity;
	}

	@Override
	public void onReceive(final Context context, Intent intent) {
		if (isConnected || mManager != null) {
			mManager.requestConnectionInfo(mChannel,
					new ConnectionInfoListener() {

						@Override
						public void onConnectionInfoAvailable(WifiP2pInfo info) {
							try {
								mActivity.mManager = mManager;
								if (info == null)
									return;
								// InetAddress from WifiP2pInfo struct.
								String groupOwnerAddress = info.groupOwnerAddress
										.getHostAddress();

								// After the group negotiation, we can
								// determine the group owner.
								if (info.groupFormed && info.isGroupOwner) {
									mActivity
											.onScreenLogger("group formed and im leader");
									cs = new myClientSocket(mActivity);
									cs.go();

									// Do whatever tasks are specific to the
									// group owner.
									// One common case is creating a server
									// thread and accepting
									// incoming connections.
								} else if (info.groupFormed) {
									mActivity
											.onScreenLogger("group formed, no leader");
									ss = new myServerSocket(mActivity);
									ss.go();

									// The other device acts as the client.
									// In this case,
									// you'll want to create a client thread
									// that connects to the group
									// owner.
								}

							} catch (Exception e) {
								mActivity.onScreenLogger("caught exception "
										+ e);
							}
						}
					});
		}

		String action = intent.getAction();

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
			if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {

				// Wifi P2P is enabled
				mActivity.onScreenLogger("Wifi P2P enabled");
			} else {
				// Wi-Fi P2P is not enabled
				mActivity.onScreenLogger("Wifi P2P Disabled");
			}
		}

		if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
			// Check to see if Wi-Fi is enabled and notify appropriate activity
		} else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
			// Call WifiP2pManager.requestPeers() to get a list of current peers
			mActivity.onScreenLogger("P2P peers changed action " + action);
			if (mManager != null) {
				mManager.requestPeers(mChannel, new PeerListListener() {

					@Override
					public void onPeersAvailable(WifiP2pDeviceList peers) {
						if (peers.getDeviceList().size() <= 0) {
							isConnected = false;
							if (ss != null) {
								ss.kill();
							} else if (cs != null) {
								cs.kill();
							}
						}
						mActivity.onScreenLogger("Peers available: "
								+ peers.getDeviceList().size());
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
			mActivity.onScreenLogger("connection changed action " + action);
		} else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION
				.equals(action)) {
			// Respond to this device's wifi state changing
			mActivity.onScreenLogger("THIS device changed action " + action);
		}
	}

	protected void connectToDevice(WifiP2pDevice myDevice) {
		if (!isConnected && myDevice != null) {
			final WifiP2pConfig config = new WifiP2pConfig();
			config.deviceAddress = myDevice.deviceAddress;
			mManager.connect(mChannel, config, new ActionListener() {

				@Override
				public void onSuccess() {
					mActivity.onScreenLogger("Connected");
					isConnected = true;
					mActivity.onScreenLogger("" + config.groupOwnerIntent);
					config.wps.setup = WpsInfo.PBC;

				}

				@Override
				public void onFailure(int reason) {
					mActivity.onScreenLogger("did not connect");
				}
			});

		}
	}
}
